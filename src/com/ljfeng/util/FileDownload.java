package com.ljfeng.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

/**
 * 文件下载工具类
 * @author JeffLee
 *
 */
public class FileDownload {
	 /**
     * 
     * @param fileName  源文件名
     * @param fromPath 源文件路径
     * @param toPath 目标路径
     * @throws IOException
     */
    public static String download(String fileName,String fromPath, String toPath)throws IOException {
	   OutputStream output = null;
	   FileInputStream input =null;
	   try {
		    input = new FileInputStream(fromPath);
		    byte[] buffer = new byte[1024];
		    File des = new File(toPath, fileName);
		    output = new FileOutputStream(des);
		    int len = 0;
		    while (-1 != (len = input.read(buffer))) {
		    	output.write(buffer, 0, len);
		    }
		    
		    if (output != null) {
		     try {
			      if (input != null){
				       output.close();
				       input.close();  
			      }
			      
			      MsgBox.show("下载成功", "提示");
			      if(fileName.endsWith(".doc")){//doc文档时，自动打开目录
			    	  File file = new File(toPath+File.separator+fileName);
			    	  MsgBox.openDirSelectFile(file);
			      }
		     } catch (IOException e) {
		    	 e.printStackTrace();
		     }
		    }
	   } catch (FileNotFoundException e1) {
		   JOptionPane.showMessageDialog(null, "下载失败！("+e1.getMessage()+")", "提示", JOptionPane.ERROR_MESSAGE);
		   e1.printStackTrace();
	   } catch (IOException e1) {
		   JOptionPane.showMessageDialog(null, "下载失败！("+e1.getMessage()+")", "提示", JOptionPane.ERROR_MESSAGE);
		   e1.printStackTrace();
	   }
	   return null;
	}
    
    /**
     * 输入流转文件
     * @param ins
     * @param file
     * @throws FileNotFoundException
     */
    public static void inputstreamToFile(InputStream ins,File file) throws FileNotFoundException{
		try {
	    	OutputStream os = new FileOutputStream(file);
	    	int bytesRead = 0;
	    	byte[] buffer = new byte[8192];
	    	while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
	    	os.write(buffer, 0, bytesRead);
	    	}
	    	os.close();
	    	ins.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
