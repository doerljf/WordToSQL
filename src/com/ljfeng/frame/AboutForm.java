package com.ljfeng.frame;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import java.awt.Window.Type;

public class AboutForm {

	private JFrame aboutForm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//������
		try {
			//����߿��������͸��
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
				
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AboutForm window = new AboutForm();
					window.aboutForm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AboutForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		aboutForm = new JFrame();
		aboutForm.setType(Type.UTILITY);
		aboutForm.setResizable(false);
		aboutForm.setTitle("����");
		aboutForm.setBounds(100, 100, 311, 331);
		aboutForm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		//����
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();  
		aboutForm.setLocation((dim.width - aboutForm.getWidth()) / 2, (dim.height - aboutForm.getHeight()) / 2);
		aboutForm.getContentPane().setLayout(null);
		
        ImageIcon img = new ImageIcon(MainForm.class.getResource("/com/ljfeng/frame/logo.jpg"));
        JLabel label = new JLabel();
        label.setLocation(58, 14);
        aboutForm.getContentPane().add(label);
        label.setSize(188, 184);
        label.setIcon(img);
        
        JLabel lblsql = new JLabel("<html>��������ڽ�ָ����ʽ�����ݿ�<br/>����ĵ�����ͬ��sql�ļ�!</html>");
        lblsql.setBounds(70, 246, 175, 32);
        aboutForm.getContentPane().add(lblsql);
        
        JLabel lblWordtosql = new JLabel("WordToSQL������");
        lblWordtosql.setBounds(95, 208, 115, 15);
        aboutForm.getContentPane().add(lblWordtosql);
        
        JLabel lblljf = new JLabel("���ߣ�LJF");
        lblljf.setBounds(104, 228, 97, 15);
        aboutForm.getContentPane().add(lblljf);
	}

}
