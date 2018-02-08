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
 * �ļ����ع�����
 * @author JeffLee
 *
 */
public class FileDownload {
	 /**
     * 
     * @param fileName  Դ�ļ���
     * @param fromPath Դ�ļ�·��
     * @param toPath Ŀ��·��
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
			      
			      MsgBox.show("���سɹ�", "��ʾ");
			      if(fileName.endsWith(".doc")){//doc�ĵ�ʱ���Զ���Ŀ¼
			    	  File file = new File(toPath+File.separator+fileName);
			    	  MsgBox.openDirSelectFile(file);
			      }
		     } catch (IOException e) {
		    	 e.printStackTrace();
		     }
		    }
	   } catch (FileNotFoundException e1) {
		   JOptionPane.showMessageDialog(null, "����ʧ�ܣ�("+e1.getMessage()+")", "��ʾ", JOptionPane.ERROR_MESSAGE);
		   e1.printStackTrace();
	   } catch (IOException e1) {
		   JOptionPane.showMessageDialog(null, "����ʧ�ܣ�("+e1.getMessage()+")", "��ʾ", JOptionPane.ERROR_MESSAGE);
		   e1.printStackTrace();
	   }
	   return null;
	}
    
    /**
     * ������ת�ļ�
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
