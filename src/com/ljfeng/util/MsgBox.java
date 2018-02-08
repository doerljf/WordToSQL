package com.ljfeng.util;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

public class MsgBox {
	
	/**
	 * ��ͨ��ȷ�ϰ�ť����
	 * @param content
	 * @param title
	 */
	public static void show(String content, String title){

		JOptionPane.showMessageDialog(null, content, title ,JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * ���ļ�����Ŀ¼����ѡ���ļ�
	 * @param file
	 */
	public static void openDirSelectFile(File file){
		
		try {
			Runtime.getRuntime().exec(
					"rundll32 SHELL32.DLL,ShellExec_RunDLL "
					+ "Explorer.exe /select," + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
