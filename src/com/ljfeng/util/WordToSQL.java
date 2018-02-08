package com.ljfeng.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class WordToSQL {

	public static void main(String [] args) throws Exception {
		String filePath="C:\\Users\\JeffLee\\Desktop\\河长制数据库.doc";
		//String filePath="E:\\ProjectDoc\\赶车人\\02Design\\doc\\数据库设计1.0.doc";
		//String filePath="E:\\ProjectDoc\\派出所\\Design\\doc\\警务系统数据库表设计1.0.doc";
		String tableNameFilter="";
		String dbName="";
		makeSQL(dbName, filePath, tableNameFilter);
	}
	
	//文本换行符号
	public static String HHTxt = "\r\n";
	
	//脚本生成主方法
	public static Boolean makeSQL(String dbName, String filePath,String tableNameFilter) throws FileNotFoundException, IOException{
		
		try{
			
			//删除已有sql文件
			String sqlFilePath = filePath.replace(".doc", ".sql");
			File file = new File(sqlFilePath);
			if(file.exists()){
				file.delete();	
			}

			//计算筛选的表数量
			int realFilterCount = 0;
 	    	if(null!=tableNameFilter && tableNameFilter.length()>0){
 	    		String[] aryFilterTables =  tableNameFilter.split(",");
 	    		for(String fTableName : aryFilterTables){
 	    	    	if(null!=fTableName && fTableName.length()>0){
 	    	    		realFilterCount++;
 	    	    	}
 	    		}
 	    	}
 	    	
            FileInputStream wordFile = new FileInputStream(filePath);//载入文档  
            //如果是office2007  docx格式  
            if(filePath.toLowerCase().endsWith("docx")){  
            	//TODO:提示暂不支持
                
            }else{  
                
                //如果是office2003  doc格式  
                 POIFSFileSystem pfs = new POIFSFileSystem(wordFile);     
                 HWPFDocument hwpf = new HWPFDocument(pfs);     
                 Range range = hwpf.getRange();//得到文档的读取范围
                 
                 //获取段落
                 int num = range.numParagraphs();  
                 if(num>1 && null!=dbName && dbName.length()>0){//先生成 数据库名称 
                	 writeSQLFile(filePath, "CREATE DATABASE `"+dbName+"` /*!40100 CHARACTER SET utf8 COLLATE 'utf8_general_ci' */;" + HHTxt
                	 		+ "USE `"+dbName+"`;" + HHTxt
                	 		+ "SET FOREIGN_KEY_CHECKS=0;" + HHTxt);
                 }
                 
                 String tableName="";
                 int tableCount=1;
                 int filterTableEndCount=0;//筛选表计数
                 Boolean isStartMakeTable = false;//筛选表开始生成标记
                 for (int i=0; i<num; i++) {  
                	 Paragraph paraN = range.getParagraph(i);
                	 
                	 if(null!=paraN && null!=paraN.text()){
                         String paraText = paraN.text().trim().replaceFirst("\r", "");
                         
                         //开始处理表名-start
                         if(filterTableEndCount-1 >= realFilterCount*2){
                        	 break;
                         }
                         if(paraText.contains(":") || paraText.contains("：")){

                            String mEn=":";
                            String mCn="：";
                         	if(paraText.contains(mEn)){
                         		tableName = paraText.split(mEn)[paraText.split(mEn).length-1];
                         	}else if(paraText.contains(mCn)){
                         		tableName = paraText.split(mCn)[paraText.split(mCn).length-1];
                         	}
                         	
                         	if(null!=tableName &&tableName.length()>0){
                         		String regEx = "^[A-Za-z0-9_]+$";//"数字&字母&下划线"组成的表名
                         	    Pattern pattern = Pattern.compile(regEx);
                         	    Matcher matcher = pattern.matcher(tableName);
                         	    // 字符串是否与正则表达式相匹配
                         	    boolean rs = matcher.matches();
                         	    if(rs){
                         	    	//指定筛选某一个表时
                         	    	if(null!=tableNameFilter && tableNameFilter.length()>0){
                             	    	if( (","+tableNameFilter+",").contains(","+tableName+",") ){
                             	    		filterTableEndCount++;
                             	    		isStartMakeTable=true;
                             	    	}else{
                             	    		isStartMakeTable=false;
                             	    		continue;
                             	    	}	
                         	    	}
                         	    	
                                    writeSQLFile(filePath, "/* "+paraText+" */");
                                    //表名
                             		tableName = "`"+tableName+"`";	
                         	    }
                         	}
                         }
                         //开始处理表名-end
                         
                	 }
                     /*if (paraN.isInList()) {
                        String paraText = paraN.text().trim().replaceFirst("\r", "");
                        System.out.println(paraText);
                    }*/
                     
                    if(paraN.isInTable() ){//生成表格
                    	try {
                    		
                            Table table = range.getTable(paraN);   
                       	 	if(table.numRows()<2){
                       	 		continue;
                       	 	}
                       	 	
                       	 	//筛选表时,指定开始生成表
                       	 	if(null!=tableNameFilter && tableNameFilter.length()>0 && !isStartMakeTable){
                       	 		continue;
                       	 	}
                       	 	
                       	 	String sqlTable="";
                       	 	String keyCols="";
                            //字段sql模板:ids int(11) primary key NOT NULL DEFAULT 0 auto_increment COMMENT '主键id' ,
                            String sqlRow=""; 
                       	 	String colName="";
                       	 	String text="";
                       	 	int towNeedCount=0;//两个必须的字段属性计数
                            TableRow tr0 = table.getRow(0);//0行-标题
                            
                            //迭代行，默认从1开始(0行是标题)
                            for (int i1 = 1; i1 < table.numRows(); i1++) {     
                                TableRow tr = table.getRow(i1);     
                                
                                sqlRow="[0] [1]([2])";//至少需要"字段名 类型"
                                towNeedCount=0;//必须属性计数初始化
                                
                                //迭代列，默认从0开始  
                                for (int j = 0; j < tr.numCells(); j++) {     
                                    TableCell td = tr.getCell(j);//取得单元格  
                                    colName = tr0.getCell(j).getParagraph(0).text();
                                    if(null==colName || colName.length()<2 ){
                                    	continue;
                                    }
                                    text = td.getParagraph(0).text();  
                                    
                                    colName = colName.substring(0, colName.length()-1);
                                    //去掉空格,制表符，换行符
                                    Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                                    Matcher m =p.matcher(colName.trim());
                                    colName = m.replaceAll("");
                                    
                                    //去除后面的特殊符号  
                                    if(null!=text && text.length()>=1){  
                                        text=text.substring(0, text.length()-1);
                                        //去掉空格,制表符，换行符
                                        Matcher mT =p.matcher(text.trim());
                                        text = mT.replaceAll("");
                                    }  
                                    
                                    //列名/数据类型/长度/小数位/主键/允许空/说明
                                    switch(colName){
                                    	case "列名":
                                    	case "字段":
                                    	case "字段名":
                                    	case "字段名称":
                                    		if(null!=text && text.length()>0){
                                        		sqlRow = sqlRow.replace("[0]", "`"+ text.trim() +"`");
                                        		towNeedCount++;	
                                    		}
                                            break;
                                            
                                    	case "数据类型":
                                    		if(null!=text && text.length()>0){
	                                    		sqlRow = sqlRow.replace("[1]", text);
	                                    		towNeedCount++;
                                    		}
                                            break;
                                    	case "长度":
                                    		if(null!=text && text.length()>0){
                                        		sqlRow = sqlRow.replace("[2]", text);
                                    		}else{
                                        		sqlRow = sqlRow.replace("([2])", "");
                                    		}
                                            break;
                                    	case "小数位":
                                			if(sqlRow.toLowerCase().contains(" double(") || sqlRow.toLowerCase().contains(" float(")){// double/float类型,加小数位

                                        		Boolean isempty = true;
                                        		if(null!=text){
                                            		Pattern pattern = Pattern.compile("^[0-9]+$");
                                             	    Matcher matcher = pattern.matcher(text);
                                             	    // 字符串是否与正则表达式相匹配
                                                    if(matcher.matches()){
                                                		sqlRow = sqlRow.replaceFirst("\\)", ","+text +")");//插入"小数位"	
                                                		isempty = false;
                                                    }
                                        		}
                                        		if(isempty){//默认0
                                        			sqlRow = sqlRow.replaceFirst("\\)", ",0)");//插入"小数位"	
                                        		}
                                			}
                                            break;
                                    	case "主键":
                                    		if(",是,true,1,".contains(","+text+",")){//是主键
                                    			keyCols += "," + sqlRow.substring(0, sqlRow.indexOf("`", 1)+1 );
                                    		}
                                            break;
                                    	case "允许空":
                                    		if(",否,false,0,not,not null".contains( (","+text+",").toLowerCase() )){//允许空
                                                sqlRow += " NOT NULL";	
                                    		}
                                            break;
                                    	case "默认值":
                                    		if(null!=text && text.length()>0 && !sqlRow.contains("auto_increment")){//非自增字段
                                    			if(text.toUpperCase().equals("CURRENT_TIMESTAMP")){//关键字
                                                    sqlRow += " DEFAULT " + text;	
                                    			}else{
                                    				sqlRow += " DEFAULT " + "'"+text+"'";
                                    			}	
                                    		}
                                            break;
                                    	case "说明":
                                    	case "字段说明":
                                            if("auto_increment".equals(text.toLowerCase())){//自增
                                                sqlRow += " "+text.toUpperCase();
                                                if(sqlRow.contains("DEFAULT ")){//去掉默认值
                                                	String defaultVal = sqlRow.substring(sqlRow.indexOf("DEFAULT "), sqlRow.indexOf(" ", sqlRow.indexOf("DEFAULT ")+"DEFAULT ".length() ));
                                                	sqlRow = sqlRow.replace(defaultVal, ""); 
                                                }
                                            }else if(null!=text && text.length()>0){
                                            	text = text.replaceAll("'", "\"");
                                            	sqlRow += " COMMENT "+"'"+text+"'";
                                            }
                                            break;
                                         default:
                                        	 break;
                                        
                                    }
                                       
                                }   
                                
                                if(towNeedCount>=2){//满足字段要求后
                                	if(sqlRow.contains("([2])")){//替换掉空长度
                                		sqlRow = sqlRow.replace("([2])", "");
                                	} 
                                	
                                	if(i1 < table.numRows()-1){
                                    	sqlTable += HHTxt+"  "+sqlRow + ",";	
                                	}else{
                                    	sqlTable += HHTxt+"  "+sqlRow;
                                	}
                                }
                            }   
                            
                    		if(null!=sqlTable && sqlTable.length()>0){//建表完整脚本
                    			tableCount++;
                    			
                    			//处理主键
                    			if(sqlTable.length()>0){
                    				if(sqlTable.lastIndexOf(",")==sqlTable.length()-1){//避免结尾有,号
                    					sqlTable = sqlTable.substring(0, sqlTable.lastIndexOf(","));
                    				}
                    				sqlTable += HHTxt;
                    				
                        			keyCols = keyCols.replaceFirst(",", "");
                        			if(null!=keyCols && keyCols.length()>0){//有主键
                        				sqlTable += "  ,PRIMARY KEY ("+keyCols+")" + HHTxt;	
                        			}
                    			}
                    			//处理表名
                    			if(null==tableName || tableName.length()<=0){
                                    tableName = "`tablename_"+ tableCount +"`";	
                    			}
                    			sqlTable = "DROP TABLE IF EXISTS "+tableName+";" + HHTxt
                    					+ "CREATE TABLE "+tableName+" (" 
                    					+ sqlTable
                    					+ ") ENGINE=InnoDB  DEFAULT CHARSET=utf8;" + HHTxt;
                    		}
                            writeSQLFile(filePath, sqlTable);
                            
						} catch (Exception e) {
						}
                    }
                    
                 }  
                 //生成脚本完成
                 return true;
            }  
         }catch(Exception e){  
        	 e.printStackTrace(); 
        	 
        	 return false; 
         }  
    	return false; 
	}
	
	//写入sql文件
	public static void writeSQLFile(String fileP,String content){
        System.out.println(content);
        
		String sqlFilePath = fileP.replace(".doc", ".sql");
		try {

			File file = new File(sqlFilePath);
			if(!file.exists()){
				file.createNewFile();
			}
			
			FileWriter writer = new FileWriter(sqlFilePath, true);
            writer.write(content+"\r\n");
            writer.close();
            
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
