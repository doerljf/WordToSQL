package com.ljfeng.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class WordFileFilter extends FileFilter {    
    public String getDescription() {    
        return "*.doc";//*.doc;*.docx    
    }    
    
    public boolean accept(File file) {    
        String name = file.getName();    
        return file.isDirectory() || name.toLowerCase().endsWith(".doc") ;  // 仅显示目录和doc文件  
    }    
}    