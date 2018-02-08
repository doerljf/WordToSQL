package com.ljfeng.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import com.ljfeng.util.FileDownload;
import com.ljfeng.util.MsgBox;
import com.ljfeng.util.WordFileFilter;
import com.ljfeng.util.WordToSQL;

public class MainForm {

	private JFrame frmWordtosql;
	private JTextField txtFilterTbName;
	private JTextField txtSelDoc;
	private JTextField txtDBName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		//美化包
		try {
			//窗体边框：普通半透明
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);//禁用右上角"设置"按钮
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.frmWordtosql.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//基础字体样式
		Font baseFont = new Font("微软雅黑", Font.PLAIN, 14);
		//基础颜色样式
		Color baseColor = new Color(0, 0, 51);
		
		frmWordtosql = new JFrame();
		frmWordtosql.setResizable(false);
		frmWordtosql.getContentPane().setForeground(baseColor);
		frmWordtosql.setIconImage(Toolkit.getDefaultToolkit().getImage(MainForm.class.getResource("/com/ljfeng/frame/logo.jpg")));
		frmWordtosql.setForeground(Color.LIGHT_GRAY);
		frmWordtosql.setTitle("WordToSQL");
		frmWordtosql.setBounds(0, -24, 549, 360);
		frmWordtosql.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//居中
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();  
		frmWordtosql.setLocation((dim.width - frmWordtosql.getWidth()) / 2, (dim.height - frmWordtosql.getHeight()) / 2); 
		//frmWordtosql.getContentPane().setLayout(new BoxLayout(frmWordtosql.getContentPane(), BoxLayout.X_AXIS));
		
		frmWordtosql.getContentPane().setLayout(null);
		
		/*************数据库名Start*************/
		JLabel lblDBName = new JLabel("数据库名称  :");
		lblDBName.setForeground(baseColor);
		lblDBName.setFont(baseFont);
		lblDBName.setBounds(88, 54, 106, 15);
		frmWordtosql.getContentPane().add(lblDBName);
		
		txtDBName = new JTextField();
		txtDBName.setForeground(baseColor);
		txtDBName.setFont(baseFont);
		txtDBName.setColumns(10);
		txtDBName.setBounds(200, 43, 252, 37);
		frmWordtosql.getContentPane().add(txtDBName);
		/*************数据库名End*************/
		
		JLabel lblSeldoc = new JLabel("选择文档(doc):");
		lblSeldoc.setForeground(baseColor);
		lblSeldoc.setFont(baseFont);
		lblSeldoc.setBounds(88, 101, 106, 15);
		frmWordtosql.getContentPane().add(lblSeldoc);
		
		txtSelDoc = new JTextField();
		txtSelDoc.setForeground(baseColor);
		txtSelDoc.setFont(baseFont);
		txtSelDoc.setColumns(10);
		txtSelDoc.setBounds(200, 90, 162, 37);
		frmWordtosql.getContentPane().add(txtSelDoc);
		
		JButton btnSelDoc = new JButton("浏览");
		btnSelDoc.setForeground(baseColor);
		btnSelDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//选择文档
				
				JFileChooser jfc=new JFileChooser("."); //在当前目录下，创建文件选择器  
				WordFileFilter docFilter = new WordFileFilter(); //文件过滤器    
				jfc.addChoosableFileFilter(docFilter);  
				jfc.setFileFilter(docFilter);  
		        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );  
		        jfc.showDialog(new JLabel(), "选择文档"); 
		        
		        File file=jfc.getSelectedFile();  
		        if(null!=file && file.isFile()){    
			        txtSelDoc.setText(file.getAbsolutePath());
		            /*System.out.println("文件:"+file.getAbsolutePath());
			        System.out.println(jfc.getSelectedFile().getName());  */
		        }  
			}
		});
		btnSelDoc.setFont(baseFont);
		btnSelDoc.setBounds(368,90,84, 37);
		frmWordtosql.getContentPane().add(btnSelDoc);
		
		//筛选表名
		txtFilterTbName = new JTextField();
		txtFilterTbName.setForeground(baseColor);
		txtFilterTbName.setFont(baseFont);
		txtFilterTbName.setBounds(200, 137, 252, 37);
		frmWordtosql.getContentPane().add(txtFilterTbName);
		txtFilterTbName.setColumns(10);
		
		JLabel lblFilterTbName = new JLabel("筛选表(多表,)  :");
		lblFilterTbName.setForeground(baseColor);
		lblFilterTbName.setFont(baseFont);
		lblFilterTbName.setBounds(88, 148, 99, 15);
		frmWordtosql.getContentPane().add(lblFilterTbName);
		
		final String msgGenSQL = "生成SQL";
		final JButton btnGenSQL = new JButton(msgGenSQL);
		btnGenSQL.setForeground(baseColor);
		btnGenSQL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//生成SQL

				btnGenSQL.setText("生成中...");
				btnGenSQL.setEnabled(false);
				
				String filePath = txtSelDoc.getText();//文档全路径
				String tableNameFilter = txtFilterTbName.getText();//筛选表名
				File file = new File(filePath);
				
				Boolean result=false;
				try {  
			        if(!file.isFile()){ 
						MsgBox.show("请选择word(.doc)文件!", "注意哦");
						return;
			        }
					result = WordToSQL.makeSQL(txtDBName.getText(), filePath, tableNameFilter);

					if(result){
						MsgBox.show("已成功生成同名sql文件!", "棒棒哒");
						
						//打开目录，并选择生成的文件
						File fileSql = new File(filePath.replace(".doc", ".sql"));//sql文件
						MsgBox.openDirSelectFile(fileSql);
					}
				} catch (FileNotFoundException fe) {
					fe.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}finally{
					btnGenSQL.setText(msgGenSQL);
					btnGenSQL.setEnabled(true);	
				}
			}
		});
		btnGenSQL.setFont(baseFont);
		btnGenSQL.setBounds(88, 190, 365, 37);
		frmWordtosql.getContentPane().add(btnGenSQL);
		
		//菜单
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(baseFont);
		menuBar.setBounds(0, 0, 543, 21);
		JMenu menu = new JMenu(" 先点这里 ");
		menu.setForeground(baseColor);
		menu.setFont(baseFont);
		menu.setSize(84, 37);
		
		JMenuItem itemTemp = new JMenuItem("模板下载");
		itemTemp.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		itemTemp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {//下载模板
				
				JFileChooser jfc=new JFileChooser("."); //在当前目录下，创建文件选择器  
		        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );  
		        jfc.showDialog(new JLabel(), "选择路径"); 
				
				try {
				        File downDir=jfc.getSelectedFile();  
				        if(null!=downDir && downDir.isDirectory()){    
	
					        String toPath = downDir.getAbsolutePath();
				        	String fileName = "template.doc";//下载的模板名称
				        	
				        	InputStream fileIn = this.getClass().getResourceAsStream(fileName);
				        	File fileTemp= new File(toPath+File.separator+fileName);
				        	FileDownload.inputstreamToFile(fileIn, fileTemp);
				        	//打开模板
					    	MsgBox.openDirSelectFile(fileTemp);
				        	
				        }
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }  
		        
		});
		menu.add(itemTemp);
		JMenuItem itemAbout = new JMenuItem("关于");
		itemAbout.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		itemAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//关于软件
				AboutForm aboutForm = new AboutForm();
				aboutForm.main(null);
				//MsgBox.show("本软件可用于将指定格式的数据库设计文档生成同名sql文件!", "关于");
			}
		});
		menu.add(itemAbout);
		menuBar.add(menu);
		
		frmWordtosql.getContentPane().add(menuBar);
		
	}
}
