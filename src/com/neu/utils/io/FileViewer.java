/*
 * ***************************************************
 * REsolution is an automatic software refactoring tool      
 * ***************************************************
 *  Copyright (c) 2016, Wang Ying, Yin Hongjian
 *  E-mail: wangying8052@163.com
 *  All rights reserved.
 *
 * This file is part of REsolution.
 *
 * REsolution is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * REsolution is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with REsolution.  If not, see <http://www.gnu.org/licenses/>.
 *
 */


package com.neu.utils.io;
import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.neu.utils.service.RemoveComments;

/**
 * 读取目录及子目录下指定文件名的路径, 返回一个List
 */

public class FileViewer {
	
	public static void main(String[] args)
	{
		
		String path = "C:\\Users\\revo\\Desktop\\Apache HSQLDB\\hsqldb\\src\\org\\hsqldb\\test\\TestBugBase.java";
		String fillname = FileViewer.getClassFillName(path);
		System.out.println("fillname->"+fillname);
	}
	
	
	
	private static Logger logger = Logger.getLogger(FileViewer.class);

	static final String  encoding="UTF-8";
	
	/**
	 * @param path
	 *            文件路径
	 * @param suffix
	 *            后缀名, 为空则表示所有文件
	 * @param isdepth
	 *            是否遍历子目录
	 * @return list
	 */
	public static List<String> getListFiles(String path, String suffix,
			boolean isdepth) {
		
		List<String> lstFileNames = new ArrayList<String>();
		File file = new File(path);
		return FileViewer.listFile(lstFileNames, file, suffix, isdepth);
	}

	private static List<String> listFile(List<String> lstFileNames, File f,
			String suffix, boolean isdepth) {
		// 若是目录, 采用递归的方法遍历子目录
		if (f.isDirectory()) {
			File[] t = f.listFiles();

			for (int i = 0; i < t.length; i++) {
				if (isdepth || t[i].isFile()) {
					listFile(lstFileNames, t[i], suffix, isdepth);
				}
			}
		} else {
			String filePath = f.getAbsolutePath();
			if (!suffix.equals("")) {
				int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
				String tempsuffix = "";

				if (begIndex != -1) {
					tempsuffix = filePath.substring(begIndex + 1,
							filePath.length());
					if (tempsuffix.equals(suffix)) {
						lstFileNames.add(filePath);
					}
				}
			} else {
				lstFileNames.add(filePath);
			}
		}
		return lstFileNames;
	}

	/**
	 * @author revo
	 * @param all Java File Paths
	 * @return Packages Name by list
	 */
	public static ArrayList<String> getAllClassFillName(List<String> allJavaFilePaths) {
		
		ArrayList<String> allClassFillName = new ArrayList<String>();
		
		for (String path : allJavaFilePaths) {
			allClassFillName.add(FileViewer.getClassFillName(path));
		}
		return allClassFillName;
	}

	/**
	 * @author revo
	 * @param java file path
	 * @return Package Name string or class name
	 */
	private static String getClassFillName(String path) {
		
		String temp = "";
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));
			RemoveComments c = new RemoveComments();
			String text = c.remove(br);
			
			temp = getPackageName(text);

		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
		}
		
		if (temp != null) {
			return temp+"."+getClassName(path);
		}else {
			return getClassName(path);
		}
		
	}
	
	/**
	 * @author revo
	 * @param java File Path 
	 * @return Class Name
	 */
	private static String getClassName(String javaFilePath)
	{
		return javaFilePath.substring(javaFilePath.lastIndexOf("\\")+1,javaFilePath.length()-5);
	}
	
	/**
	 * @author revo
	 * @param str is file content
	 * @return Package Name or null
	 */
	private static String getPackageName(String str){
		
		String[] strlines = null ;		
		
		if(str.indexOf("\r\n")>=0){//windows file
			strlines=str.split("\r\n");
//			System.out.println("//windows file");
						
		}else if(str.indexOf("\n")>=0)//linux or unix file
		{
			strlines=str.split("\n");
//			System.out.println("//linux or unix file");
			
		}else if (str.indexOf("\r")>=0) {
			strlines=str.split("\r");
//			System.out.println("//mac");
		}else {
			JOptionPane.showMessageDialog(null, "can not recognise the file", "system tips", JOptionPane.YES_NO_OPTION);
		}
		
		String regex="package";
		
		for (String string : strlines) {
			
			//System.out.println(string.indexOf(regex));
			
			if (string.indexOf(regex)>=0) {
				
				String[] strtemp= string.split(";");
				return strtemp[0].replaceAll("\t", "").replaceAll(" ", "").substring(7);
			}
		}
		return null;
	}
}