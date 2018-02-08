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

		//������
		try {
			//����߿���ͨ��͸��
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);//�������Ͻ�"����"��ť
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
		//����������ʽ
		Font baseFont = new Font("΢���ź�", Font.PLAIN, 14);
		//������ɫ��ʽ
		Color baseColor = new Color(0, 0, 51);
		
		frmWordtosql = new JFrame();
		frmWordtosql.setResizable(false);
		frmWordtosql.getContentPane().setForeground(baseColor);
		frmWordtosql.setIconImage(Toolkit.getDefaultToolkit().getImage(MainForm.class.getResource("/com/ljfeng/frame/logo.jpg")));
		frmWordtosql.setForeground(Color.LIGHT_GRAY);
		frmWordtosql.setTitle("WordToSQL");
		frmWordtosql.setBounds(0, -24, 549, 360);
		frmWordtosql.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//����
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();  
		frmWordtosql.setLocation((dim.width - frmWordtosql.getWidth()) / 2, (dim.height - frmWordtosql.getHeight()) / 2); 
		//frmWordtosql.getContentPane().setLayout(new BoxLayout(frmWordtosql.getContentPane(), BoxLayout.X_AXIS));
		
		frmWordtosql.getContentPane().setLayout(null);
		
		/*************���ݿ���Start*************/
		JLabel lblDBName = new JLabel("���ݿ�����  :");
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
		/*************���ݿ���End*************/
		
		JLabel lblSeldoc = new JLabel("ѡ���ĵ�(doc):");
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
		
		JButton btnSelDoc = new JButton("���");
		btnSelDoc.setForeground(baseColor);
		btnSelDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//ѡ���ĵ�
				
				JFileChooser jfc=new JFileChooser("."); //�ڵ�ǰĿ¼�£������ļ�ѡ����  
				WordFileFilter docFilter = new WordFileFilter(); //�ļ�������    
				jfc.addChoosableFileFilter(docFilter);  
				jfc.setFileFilter(docFilter);  
		        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );  
		        jfc.showDialog(new JLabel(), "ѡ���ĵ�"); 
		        
		        File file=jfc.getSelectedFile();  
		        if(null!=file && file.isFile()){    
			        txtSelDoc.setText(file.getAbsolutePath());
		            /*System.out.println("�ļ�:"+file.getAbsolutePath());
			        System.out.println(jfc.getSelectedFile().getName());  */
		        }  
			}
		});
		btnSelDoc.setFont(baseFont);
		btnSelDoc.setBounds(368,90,84, 37);
		frmWordtosql.getContentPane().add(btnSelDoc);
		
		//ɸѡ����
		txtFilterTbName = new JTextField();
		txtFilterTbName.setForeground(baseColor);
		txtFilterTbName.setFont(baseFont);
		txtFilterTbName.setBounds(200, 137, 252, 37);
		frmWordtosql.getContentPane().add(txtFilterTbName);
		txtFilterTbName.setColumns(10);
		
		JLabel lblFilterTbName = new JLabel("ɸѡ��(���,)  :");
		lblFilterTbName.setForeground(baseColor);
		lblFilterTbName.setFont(baseFont);
		lblFilterTbName.setBounds(88, 148, 99, 15);
		frmWordtosql.getContentPane().add(lblFilterTbName);
		
		final String msgGenSQL = "����SQL";
		final JButton btnGenSQL = new JButton(msgGenSQL);
		btnGenSQL.setForeground(baseColor);
		btnGenSQL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//����SQL

				btnGenSQL.setText("������...");
				btnGenSQL.setEnabled(false);
				
				String filePath = txtSelDoc.getText();//�ĵ�ȫ·��
				String tableNameFilter = txtFilterTbName.getText();//ɸѡ����
				File file = new File(filePath);
				
				Boolean result=false;
				try {  
			        if(!file.isFile()){ 
						MsgBox.show("��ѡ��word(.doc)�ļ�!", "ע��Ŷ");
						return;
			        }
					result = WordToSQL.makeSQL(txtDBName.getText(), filePath, tableNameFilter);

					if(result){
						MsgBox.show("�ѳɹ�����ͬ��sql�ļ�!", "������");
						
						//��Ŀ¼����ѡ�����ɵ��ļ�
						File fileSql = new File(filePath.replace(".doc", ".sql"));//sql�ļ�
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
		
		//�˵�
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(baseFont);
		menuBar.setBounds(0, 0, 543, 21);
		JMenu menu = new JMenu(" �ȵ����� ");
		menu.setForeground(baseColor);
		menu.setFont(baseFont);
		menu.setSize(84, 37);
		
		JMenuItem itemTemp = new JMenuItem("ģ������");
		itemTemp.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		itemTemp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {//����ģ��
				
				JFileChooser jfc=new JFileChooser("."); //�ڵ�ǰĿ¼�£������ļ�ѡ����  
		        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );  
		        jfc.showDialog(new JLabel(), "ѡ��·��"); 
				
				try {
				        File downDir=jfc.getSelectedFile();  
				        if(null!=downDir && downDir.isDirectory()){    
	
					        String toPath = downDir.getAbsolutePath();
				        	String fileName = "template.doc";//���ص�ģ������
				        	
				        	InputStream fileIn = this.getClass().getResourceAsStream(fileName);
				        	File fileTemp= new File(toPath+File.separator+fileName);
				        	FileDownload.inputstreamToFile(fileIn, fileTemp);
				        	//��ģ��
					    	MsgBox.openDirSelectFile(fileTemp);
				        	
				        }
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }  
		        
		});
		menu.add(itemTemp);
		JMenuItem itemAbout = new JMenuItem("����");
		itemAbout.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		itemAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//�������
				AboutForm aboutForm = new AboutForm();
				aboutForm.main(null);
				//MsgBox.show("����������ڽ�ָ����ʽ�����ݿ�����ĵ�����ͬ��sql�ļ�!", "����");
			}
		});
		menu.add(itemAbout);
		menuBar.add(menu);
		
		frmWordtosql.getContentPane().add(menuBar);
		
	}
}
