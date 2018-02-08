package com.ljfeng.util;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

public class MsgBox {
	
	/**
	 * 普通带确认按钮弹窗
	 * @param content
	 * @param title
	 */
	public static void show(String content, String title){

		JOptionPane.showMessageDialog(null, content, title ,JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * 打开文件所在目录，并选择文件
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
