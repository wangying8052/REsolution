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

package com.Refactor.classparser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class filename {

	// 写文件函数
	public static void writeByFileWrite(String _sDestFile, String _sContent)

	throws IOException {

		FileWriter fw = null;

		try {

			fw = new FileWriter(_sDestFile, true);

			fw.write(_sContent);

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {

			if (fw != null) {

				fw.close();

				fw = null;

			}

		}

	}

	// 读取文件

	public static ArrayList<String> readfilelist(ArrayList<String> array) {
		/**
		 * 以行为单位读取文件，常用于读面向行的格式化文件
		 */

		// System.out.println("以行为单位读取文件内容，一次读一整行：");
		String tempString = null;
		ArrayList<String> arraya = new ArrayList();

		for (int i = 0; i < array.size(); i++) {
			// 显示行号
			tempString = array.get(i);
			String javaclassname = null;

			String str0[] = {};
			String str1[] = new String[2];

			if (tempString.endsWith(".java")) {

				str0 = tempString.split("\\" + "\\");
				javaclassname = str0[str0.length - 1]; // 得到子类名称

				str1 = javaclassname.split(".java");
				String sContent = null;
				String classname = str1[0];

				sContent = str0[0] + ".";
				for (int wq = 1; wq < str0.length - 1; wq++) {
					sContent = sContent + str0[wq] + ".";

				}
				sContent = sContent + classname;
				arraya.add(sContent);
				//
			}
		}

		return arraya;
	}

	// }

	/**
	 * 
	 * 读取某个文件夹下的所有文件夹和文件, 返回所有文件名
	 * 
	 * @param filepath
	 *            String
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @return Map<Integer, String> pathMap
	 * 
	 */
	public static Map<Integer, String> readfile(String filepath,
			Map<Integer, String> pathMap) throws Exception {
		if (pathMap == null) {
			pathMap = new HashMap<Integer, String>();
		}

		File file = new File(filepath);
		// 文件
		if (!file.isDirectory()) {
			pathMap.put(pathMap.size(), file.getPath());

		} else if (file.isDirectory()) { // 如果是目录， 遍历所有子目录取出所有文件名
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(filepath + "\\" + filelist[i]);
				if (!readfile.isDirectory()) {
					pathMap.put(pathMap.size(), readfile.getPath());

				} else if (readfile.isDirectory()) { // 子目录的目录
					readfile(filepath + "\\" + filelist[i], pathMap);
				}
			}
		}
		return pathMap;
	}

	public static ArrayList<String> getallclassnames(String sourcepath) {
		ArrayList<String> arraya = new ArrayList();
		try {
			int index = sourcepath.length();
			ArrayList<String> array = new ArrayList<String>();
			Map<Integer, String> map = readfile(sourcepath, null);
			for (int i = 0; i < map.size(); i++) {
				String path = map.get(i);
				String parkegename = path.substring(index);

				String sContent = parkegename;
				array.add(sContent);

			}
			arraya = readfilelist(array);
		} catch (Exception ex) {
		}

		System.out.println("arraya.size==" + arraya.size());
		return arraya;
	}

}
