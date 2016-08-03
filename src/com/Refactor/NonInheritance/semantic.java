/*
****************************************************
* REsolution is an automatic software refactoring tool      
****************************************************
 *  Copyright (c) 2016, Wang Ying, Yin Hongjian, YU Hai, ZHU Zhiliang.
 *  E-mail: yuhai@126.com
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

package com.Refactor.NonInheritance;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.xml.sax.SAXException;

import yangyue.svdyy;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

import com.Refactor.AdjustCoefficients.Adjust;
import com.Refactor.Inheritance.SplitTrees;
import com.Refactor.classparser.*;
import com.jeantessier.dependencyfinder.gui.SrcAction;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import com.meterware.httpunit.javascript.JavaScript.Document;
import com.neu.utils.listener.ProgressBarLisenter;


public class semantic {
	static TermDoc TermDoc = new TermDoc();
	static VSM VSM = new VSM();
	static ArrayList<String> wordss = new ArrayList<String>();
	
	 public static double[] Reverbubsort(double[] num){  //大-》小
	        int i, j, flag = 1;  
	        double temp;
	        int MAX = num.length;
	      
	        for(i = 0; i < MAX-1 && flag == 1; i++) {  
	            flag = 0;  
	            for(j = 0; j < MAX-i-1; j++) {  
	                if(num[j+1] > num[j]) {  
	                    temp=num[j+1];  
	                    num[j+1]=num[j];  
	                    num[j]=temp;  
	                    flag = 1;  
	                }  
	            }  
	        }
			return num;
	 }
	 
	 public static int[] bubsort(int[] num, int[] idx){  //小 大
	        int i, j, flag = 1;  
	        int temp;
	        int tempx ;
	        int MAX = num.length;
	      
	        for(i = 0; i < MAX-1 && flag == 1; i++) {  
	            flag = 0;  
	            for(j = 0; j < MAX-i-1; j++) {  
	                if(num[j+1] < num[j]) {  
	                    temp=num[j+1];  
	                    num[j+1]=num[j];  
	                    num[j]=temp;  
	                    tempx=idx[j+1];  
	                    idx[j+1]=idx[j];  
	                    idx[j]=tempx;
	                    flag = 1;  
	                }  
	            }  
	        }
			return idx;
	 }
	 
	 
	 public static double[][] filterSemanticBetweenclass(double[][] SM, ArrayList<String> methodlist, double[][] xshare, double[][] ycall){
			for(int i = 0; i < methodlist.size(); i++){
				for(int j = 0; j < methodlist.size(); j++){
					if(i!=j){
					if(!SourceParser.Getonlyclassname(methodlist.get(i)).equals(SourceParser.Getonlyclassname(methodlist.get(j)))){
					if((xshare[i][j]==0)&&(ycall[i][j]==0)){
						SM[i][j] = 0;
					}
						}
					}
				}
			}
			
			
			
			return SM;		
	 }
	 
	 
	 public static double[][] filterexeBetweenclass(double[][] zexe, ArrayList<String> methodlist, double[][] xshare, double[][] ycall){
			for(int i = 0; i < methodlist.size(); i++){
				for(int j = 0; j < methodlist.size(); j++){
					if(i!=j){
					if(!SourceParser.Getonlyclassname(methodlist.get(i)).equals(SourceParser.Getonlyclassname(methodlist.get(j)))){
					if((xshare[i][j]==0)&&(ycall[i][j]==0)){
						zexe[i][j]= 0;
					}
						}
					}
				}
			}
			
			
			
			return zexe;		
	 }
	 
	 
		public static double[][] filterSemantic(double[][] SM, ArrayList<String> methodlist, double[][] xshare, double[][] ycall, double[][] zexe){
			double min = 0;
			double TD = 0;
			int cnt = 0;
			for(int i = 0; i < SM.length; i++){
				for(int j = 0; j < SM.length; j++){
					if(  SM[i][j] !=0){
						cnt++;
					}
				}
			}
			double num[]=new double[cnt]; 
			int dx = 0;
			for(int i = 0; i < SM.length; i++){
				for(int j = 0; j < SM.length; j++){
					if(  SM[i][j] !=0){
					num[dx] =  SM[i][j];
					dx++;
					}
				}
			}
			
			num = bubsort(num);
			if(num.length>2){
			if(num.length % 2 == 0){//偶数
				min  = (num[num.length/2 - 1] + num[num.length/2])/(double)2;
			}else{
				min  = num[(num.length + 1)/2];
			}
			}
			if(Adjust.Threshold == 0){
				TD = min;
			}
			if(Adjust.Threshold == 1){
				TD = 0.1;
			}
			if(Adjust.Threshold == 2){
				TD = 0.2;
			}
			
			for(int i = 0; i < SM.length; i++){
				for(int j = 0; j < SM.length; j++){
					if(SM[i][j]<TD){
						SM[i][j] = 0;
					}
				}
			}
			
			return SM;		
		}
	
	
	
	 public static ArrayList<UndoClass> ReverbClasssort(ArrayList<UndoClass>  undoClasseSort){  //大-》小
	        int i, j, flag = 1;  
	        UndoClass temp;
	       
	        int MAX = undoClasseSort.size();
	      
	        for(i = 0; i < MAX-1 && flag == 1; i++) {  
	            flag = 0;  
	            for(j = 0; j < MAX-i-1; j++) {  
	                if(undoClasseSort.get(j+1).detaQ > undoClasseSort.get(j).detaQ) {  
	                    temp = undoClasseSort.get(j+1);  
	                    undoClasseSort.set(j+1, undoClasseSort.get(j));
	                    undoClasseSort.set(j, temp);
	                    flag = 1;  
	                }  
	            }  
	        }
	        
	        for(int m = 0; m<undoClasseSort.size();m++){
	        	System.out.println("类类排序啦！"+m+"  "+undoClasseSort.get(m).classname+"   detaA==========="+undoClasseSort.get(m).detaQ);
	        }
			return undoClasseSort;
	 }
	 
	 
	 public static ArrayList<SplitTrees> ReverbSplitTreessort(ArrayList<SplitTrees>  SplitTree){  //大-》小
	        int i, j,  flag = 1;  
	        SplitTrees temp;
	        int MAX = SplitTree.size();
	      
	        for(i = 0; i < MAX-1 && flag == 1; i++) {  
	            flag = 0;  
	            for(j = 0; j < MAX-i-1; j++) {  
	                if(SplitTree.get(j+1).detaQ > SplitTree.get(j).detaQ) {  
	                    temp = SplitTree.get(j+1);  
	                    SplitTree.set(j+1, SplitTree.get(j));
	                    SplitTree.set(j, temp);
	                    flag = 1;  
	                }  
	            }  
	        }
	        
	        for(int m = 0; m<SplitTree.size();m++){
	        	System.out.println("树树排序啦！"+m+"  "+SplitTree.get(m).TreeIndex +"   detaA==========="+SplitTree.get(m).detaQ);
	        }
			return SplitTree;
	 }
	 
	 public static ArrayList<UndoEntity> ReverbEntitySort(ArrayList<UndoEntity>  UndoEntitySort){  //大-》小
	        int i, j, flag = 1;  
	        UndoEntity temp;
	        int MAX = UndoEntitySort.size();
	      
	        for(i = 0; i < MAX-1 && flag == 1; i++) {  
	            flag = 0;  
	            for(j = 0; j < MAX-i-1; j++) {  
	                if(UndoEntitySort.get(j+1).detaQ > UndoEntitySort.get(j).detaQ) {  
	                    temp = UndoEntitySort.get(j+1);  
	                    UndoEntitySort.set(j+1, UndoEntitySort.get(j));
	                    UndoEntitySort.set(j, temp);
	                    flag = 1;  
	                }  
	            }  
	        }
	        
	        for(int m = 0; m<UndoEntitySort.size();m++){
	        	System.out.println("属性排序啦！"+m+"  "+UndoEntitySort.get(m).entityname+"   detaA==========="+UndoEntitySort.get(m).detaQ);
	        }
			return UndoEntitySort;
	 }
	
	public static void FindMethodLinesNo(String sourcepath) throws IOException{
		for (int a = 0; a < SrcAction.classname.size(); a++) {
			Map<String, Feature> featuremap  =  SrcAction.classesMap.get(SrcAction.classname.get(a)).featureMap;
			
			for(Map.Entry<String, Feature> entry : featuremap.entrySet()) {  
			       String key = entry.getKey();  
			       Feature value = entry.getValue(); 
			       if(key!=null){
			    	   if(key.contains("(")){
			    	   value = GetMethodline.getMethodLine(key, SrcAction.sourcepath, value);
			    	   }
			       }
			       SrcAction.classesMap.get(SrcAction.classname.get(a)).featureMap.put(key, value);
			       
			}
			
		}
		// //log
//		for (int a = 0; a < SaveFileAction.classname.size(); a++) {
//            Map<String, Feature> featuremap  =  SaveFileAction.classesMap.get(SaveFileAction.classname.get(a)).featureMap;
//			for(Map.Entry<String, Feature> entry : featuremap.entrySet()) {  
//			       String key = entry.getKey();  
//			       Feature value = entry.getValue();  
//			      // if(key.contains("getImageIcon")){
////			       if(value.methodlinesbe==0&&value.methodlinesaf==0&&key.contains("("))
////			       System.out.println("函数"+key+"从第"+value.methodlinesbe+"开始，从第"+value.methodlinesaf+"结束");
//			     //  }
//			}
//		}
	
	}
	
	
	
	
	
	
	
	
	/**
	 * 找到每个函数所在的行号
	 * @param barLisenter 
	 * 
	 * @throws IOException
	 */
	public static void findmethodlines(ProgressBarLisenter barLisenter)throws IOException {
		String filename = null;// 全路径名filename = E:\Tomcat\apache-tomcat-6.0.14-src\java\org\apache\catalina\connector\InputBuffer.java
		
		for (int a = 0; a < SrcAction.classname.size(); a++) {
			
			int id = 0;
			boolean yn = false;
			String str1[] = {};
			str1 = SrcAction.classname.get(a).split("\\.");
			if (str1.length > 1) {
				filename = str1[0] + "\\";
				for (int b = 1; b < str1.length - 1; b++) {
					filename = filename + str1[b] + "\\";
				}
				filename = filename + str1[str1.length - 1] + ".java";
			}
			if(str1.length == 1){
				filename = str1[str1.length - 1] + ".java";
			}
//				 System.out.println("函数filename=="+filename);
			for(int i = 0; i < SrcAction.allJavaFilePaths.size(); i++){
				if(SrcAction.allJavaFilePaths.get(i).contains(filename)){
					filename = SrcAction.allJavaFilePaths.get(i);
					break;
				}
			}
//			System.out.println("我这里filename ==="+filename );
			 
			barLisenter.endFile();
			
			List<String> methodList = new ArrayList<String>();
			yn = true;
			Map<String, Feature> featuremap  =  SrcAction.classesMap.get(SrcAction.classname.get(a)).featureMap;
			
			for(Map.Entry<String, Feature> entry : featuremap.entrySet()) {  
			       String key = entry.getKey();  
					if (key != null && !methodList.isEmpty() && ! featuremap.isEmpty()) {
						if (key.contains("(")&& key.contains(")")&& (!methodList.contains(key))) {
							methodList.add(key);
						}
					} else {
						String s = key;
						if (s != null) {
							if (s.contains("(") && s.contains(")")) {
								methodList.add(s);
							}
						}
					}
			       
			}

//				Map<String, Feature> featuremap1  =  SaveFileAction.classesMap.get(SaveFileAction.classname.get(a)).featureMap;
//				for(Map.Entry<String, Feature> entry : featuremap1.entrySet()) {  
//				       String key = entry.getKey();  
//				       Feature value = entry.getValue();  
//					if (key!= null&&key.contains("(")) {
//						value = GetMethodline.getMethodLine(key, SaveFileAction.sourcepath,value);
//						SaveFileAction.classesMap.get(SaveFileAction.classname.get(a)).featureMap.put(key, value);
//					}
//					
//				}

			
			
			ArrayList<Methodfeature> methodlines = new ArrayList<Methodfeature>();
			if (methodList.size() != 0) {
				methodlines = GetmethodlinesTinfo(filename, methodList);
			}
			SrcAction.classesMap.get(SrcAction.classname.get(a)).methodlines = methodlines;
				for (int n = 0; n < methodlines.size(); n++) {
					Map<String, Feature> featuremap1  =  SrcAction.classesMap.get(SrcAction.classname.get(a)).featureMap;
					
					for(Map.Entry<String, Feature> entry : featuremap1.entrySet()) {  
					       String key = entry.getKey();  
					       Feature value = entry.getValue();  
						if (methodlines.get(n).methodfullname != null && key!= null) {
							if (key.equals(methodlines.get(n).methodfullname)) {
								value.methodlinesaf = methodlines.get(n).methodlinesaf;
								value.methodlinesbe = methodlines.get(n).methodlinesbe;
							}
							SrcAction.classesMap.get(SrcAction.classname.get(a)).featureMap.put(key, value);
						}
						
					}

				}
//		}//for (int a = 0; a < classname.size(); a++) {
	}
		// //log
//		for (int a = 0; a < SaveFileAction.classname.size(); a++) {
//            Map<String, Feature> featuremap  =  SaveFileAction.classesMap.get(SaveFileAction.classname.get(a)).featureMap;
//			for(Map.Entry<String, Feature> entry : featuremap.entrySet()) {  
//			       String key = entry.getKey();  
//			       Feature value = entry.getValue();  
//			     //  if(key.contains("getImageIcon")){
//			       System.out.println("函数"+key+"从第"+value.methodlinesbe+"开始，从第"+value.methodlinesaf+"结束");
//			      // }
//			}
//		}
	}
	
	
	public static String methodonlyname(String filename) {
		if (filename.contains("$")) {
			filename = filename.replaceAll("\\$", "\\.");
		}
		if (filename.contains(" ")) {
			filename = filename.replaceAll(" ", "");
		}
		String name = null;
		String str3[] = {};
		String str4[] = new String[2];
		if (filename.contains("(")) {
			str3 = filename.split("\\(");
			String methodnametmp = str3[0];

			str4 = methodnametmp.split("\\.");
			name = str4[str4.length - 1];
		}

		if (filename.contains("{")) {
			str3 = filename.split("\\{");
			String methodnametmp = str3[0];

			str4 = methodnametmp.split("\\.");
			name = str4[str4.length - 1];
		}
		if (!filename.contains("{") && !filename.contains("(")) {
			str4 = filename.split("\\.");
			name = str4[str4.length - 1];
		}

		return name;
	}

	
	public static String[] methodparaname(String filename) {
		if (filename.contains("$")) {
			filename = filename.replaceAll("\\$", "\\.");
		}
		String str3[] = {};
		String str4[] = {};
		int i = filename.indexOf("(");
		int j = filename.indexOf(")");
		String methodnametmp = filename.substring(i + 1, j);// (,,)
		if (!methodnametmp.isEmpty() && methodnametmp.contains(",")) {
			str3 = methodnametmp.split("\\,");

		} else {
			if ((j - i > 1)) {
				str3 = methodnametmp.split(" ");
			}

			if ((j - i) == 1) {
				str3 = null;
			}
		}

		return str3;
	}
	
	
	
	
	
	public static void Removecomments_interference(String tempString1) {

		String kh = "'" + "{" + "'";
		String kh1 = "'" + "//" + "'";
		String kh2 = "'" + "(" + "'";
		if (tempString1.contains(kh)) {
			int kkkk = tempString1.indexOf(kh);
			tempString1 = tempString1.substring(0, kkkk) + tempString1.substring(kkkk + 3, tempString1.length());
		}
		if (tempString1.contains(kh1)) {
			int kkkk = tempString1.indexOf(kh1);
			tempString1 = tempString1.substring(0, kkkk) + tempString1.substring(kkkk + 3, tempString1.length());
		}
		if (tempString1.contains(kh2)) {
			int kkkk = tempString1.indexOf(kh2);
			tempString1 = tempString1.substring(0, kkkk) + tempString1.substring(kkkk + 3, tempString1.length());
		}
		if (tempString1.contains("\"") && (tempString1.contains("//"))) {
			boolean you = false;
			int beg = 0;
			int ed = 0;
			for (int o = 0; o < tempString1.length(); o++) {

				if (!you && tempString1.charAt(o) == '\"') {
					you = true;
					beg = o;
					continue;
				}
				if (you && tempString1.charAt(o) == '\"') {
					you = false;
					ed = o;

					if (tempString1.substring(beg, ed).contains("//")) {
						tempString1 = tempString1.substring(0, beg - 1) + tempString1.substring(ed + 1, tempString1.length());
					}
					continue;
				}

			}

		}

		if (tempString1.contains("\"") && ((tempString1.contains("(")) || (tempString1.contains(")")))) {
			boolean you = false;
			int beg = 0;
			int ed = 0;
			for (int o = 0; o < tempString1.length(); o++) {

				if (!you && tempString1.charAt(o) == '\"') {
					you = true;
					beg = o;
					continue;
				}
				if (you && tempString1.charAt(o) == '\"') {
					you = false;
					ed = o;

					if (tempString1.substring(beg, ed).contains("//")|| tempString1.substring(beg, ed).contains("(")|| tempString1.substring(beg, ed).contains(")")) {
						tempString1 = tempString1.substring(0, beg - 1)+ tempString1.substring(ed + 1,tempString1.length());
					}
					continue;
				}

			}

		}

	}

	
	
	
	public static ArrayList<Methodfeature> GetmethodlinesTinfo(String filename, List<String> methodList) throws IOException {
		File file0 = new File(filename);
		/**
		 * 读取 方法和属性列表
		 */

		String javaclassname = null;
		String str0[] = {};
		String str1[] = new String[2];
		String classname = null;
		String sContent = null;

		ArrayList<Methodfeature> methodlines = new ArrayList<Methodfeature>();
		if (filename.endsWith(".java")) {
			str0 = filename.split("\\" + "\\");
			javaclassname = str0[str0.length - 1]; // 得到子类名称
			str1 = javaclassname.split(".java");

			classname = str1[0];
			if(str0.length >= 4)
			{
				sContent = str0[3] + ".";
				for (int wq = 4; wq < str0.length - 1; wq++) {
					sContent = sContent + str0[wq] + ".";

				}
				sContent = sContent + classname;

			}
		}
		/**
		 * 读取 方法和属性列表完毕
		 */
		if (methodList.size() != 0) {

			int line = 1;
			// 一次读入一行，直到读入null为文件结束

			for (int q = 0; q < methodList.size(); q++) {

				BufferedReader reader1 = null;
				Stack<Character> stack = new Stack<Character>();
				Stack<Character> stackkh = new Stack<Character>();
				reader1 = new BufferedReader(new FileReader(file0)); // 读取java文件
				String tempString1 = null;

				String methodListonly = methodonlyname(methodList.get(q));
				String paraname[] = methodparaname(methodList.get(q));
				int start = 0;
				int methodline = 0;
				String lineupanddown = null;
				boolean Throw = false;
				boolean waitnext = false;
				boolean waitAZU = false;
				boolean waitnopu = false;
				boolean waitCloud = false;
				boolean khsametime = false;
				String tempStringfirst = null;
				while ((tempString1 = reader1.readLine()) != null) {
					// 显示行号
					methodline++;
					boolean heheazu = false;
					waitnopu = false;
					if (tempString1.contains("/**")) {
						start = 1;
						continue;
					}
					if ((start != 0) && tempString1.contains("*")) {
						start++;
						continue;
					}
					if ((start != 0) && tempString1.contains("*/")) {
						start = 0;
						continue;
					}

					Removecomments_interference(tempString1);

					boolean flag = false;
					if (waitCloud) {

						if (tempStringfirst != null) {
							tempString1 = tempStringfirst + tempString1;
							tempStringfirst = null;
						}

						if (tempString1.contains("\"") && (tempString1.contains("{") || tempString1.contains("}"))) {
						
							boolean you = false;
							int beg = 0;
							int ed = 0;
							for (int o = 0; o < tempString1.length(); o++) {

								if (!you && tempString1.charAt(o) == '\"') {
									you = true;
									beg = o;
									continue;
								}
								if (you && tempString1.charAt(o) == '\"') {
									you = false;
									ed = o;

									if (tempString1.substring(beg, ed).contains("{") || tempString1.substring(beg, ed).contains("}") || tempString1.substring(beg, ed).contains("//")) {
										tempString1 = tempString1.substring(0,beg - 1) + tempString1.substring(ed + 1,tempString1.length());
									}
									continue;
								}

							}

						}
						if (tempString1.contains("{")
								&& !tempString1.contains("}")) {

							for (int o = 0; o < tempString1.length(); o++) {
								boolean f1 = false;
								if (tempString1.charAt(o) == '{') {
									if (tempString1.contains("//")) {
										if (tempString1.indexOf("//") < o) {
											f1 = true;
										}
									}
									if (!f1)

										stack.push('{');
								}
							}
						}

						if (tempString1.contains("{")
								&& tempString1.contains("}")) {
							for (int o = 0; o < tempString1.length(); o++) {
								boolean f1 = false;
								boolean f2 = false;
								if (tempString1.charAt(o) == '{') {
									if (tempString1.contains("//")) {
										if (tempString1.indexOf("//") < o) {
											f1 = true;
										}
									}
									if (!f1)
										stack.push('{');
								}
								if (tempString1.charAt(o) == '}') {
									if (tempString1.contains("//")) {
										if (tempString1.indexOf("//") < o) {
											f2 = true;
										}
									}
									if (!f2) {
										if(!stack.empty()){
										if (stack.pop() != '{') {
										}
										}
										if (stack.empty()) {
											flag = true;
											stack.clear();
										}
									}
								}
							}
						}
						if (!tempString1.contains("{")
								&& tempString1.contains("}")) {
							for (int o = 0; o < tempString1.length(); o++) {
								boolean f1 = false;
								if (tempString1.charAt(o) == '}') {
									if (tempString1.contains("//")) {
										if (tempString1.indexOf("//") < o) {
											f1 = true;
										}
									}
									if (!f1) {
										if (stack.pop() != '{') {
										}
										if (stack.empty()) {
											flag = true;
											stack.clear();
										}
									}
								}
							}
						}

						if (flag != true) {
							continue;
						} else {
							waitCloud = false;
							if (methodlines.size() >= 1)
								methodlines.get(methodlines.size() - 1).methodlinesaf = methodline;
							break;
						}

					}

					if (waitnext) {

						if (lineupanddown != null && !Throw) {
							lineupanddown = lineupanddown + tempString1.trim();

						}
						if (lineupanddown == null && !Throw) {

							lineupanddown = tempString1.trim();

						}
						
						if (Throw) {
							if (tempString1.contains("{")) {
								Throw = false;
								tempStringfirst = tempString1;
								waitCloud = true;
								stack.clear();
								waitnext = false;
							}

							/**
							 * 为解决 函数（）后第一个{问题
							 */
							if (!tempString1.contains("{")) {
								if (tempString1.contains("(")|| tempString1.contains(")")) {
									waitnext = false;
									lineupanddown = null;
									continue;
								} else {
									continue;
								}
							}
						}

						boolean skh = false;
						boolean come = false;
						if (lineupanddown != null) {
							if ((lineupanddown.contains("(") || lineupanddown.contains(")")) && waitnext) {
								come = true;
								stackkh.clear();
								for (int r = 0; r < lineupanddown.length(); r++) {
									if (lineupanddown.charAt(r) == '(') {
										stackkh.push('(');
									}
									if (lineupanddown.charAt(r) == ')') {
										if (stackkh.pop() == '(') {
											if (stackkh.empty()) {
												skh = true;
												lineupanddown = lineupanddown.substring(1, r);
												stackkh.clear();
												break;
											} else {
												continue;
											}
										}

									}

								}
							}
						}
						if (skh && !tempString1.contains(";")) {
							if (!tempString1.contains("{")) {
								tempString1 = tempString1.trim();
								Throw = true;
								continue;
							}
					
							if (tempString1.contains("{")) {// 包含{
								if (Throw) {
									Throw = false;
								}
								tempStringfirst = tempString1;
								waitCloud = true;
								stack.clear();
								waitnext = false;
							}

						}
						if (skh && tempString1.contains(";")) {				 
							waitnext = false;
							lineupanddown = null;
							continue;

						}
						if (!skh && !waitCloud) {
							come = false;
							continue;

						}

					}// if(waitnext)
					if (waitnext == false && lineupanddown == null&& waitCloud == true) {
						continue;
					}
					if (waitnext == false && lineupanddown != null&& waitCloud == true) {

						if (lineupanddown.contains(",")) {// 多参数的情况
							
							boolean parasame = false;
							String s1[] = {};
							if (lineupanddown.contains("$")) {
								lineupanddown = lineupanddown.replaceAll("\\$","\\.");
							}
							s1 = lineupanddown.split("\\,");
							String ook = lineupanddown.trim();
							if (paraname != null) {
								if (s1.length == paraname.length) {

									lineupanddown = null;
									boolean wuruzheli = false;
									boolean yiwen = false;
									for (int nb = 0; nb < s1.length; nb++) {
										if (paraname[nb].contains("$")) {
											
											paraname[nb] = paraname[nb].replaceAll("\\$", "\\.");
										
										}
										String ok = s1[nb].trim();
										String ok2 = null;
										String yy[] = {};
										if (!ok.contains(" ")) {
											wuruzheli = true;
											break;
										}
										yy = ok.split(" ");
										if (yy[0].equals("final")) {
											ok = yy[1];
											ok2 = yy[2];
										} else {
											ok = yy[0];
											ok2 = yy[1];
										}

										if (!paraname[nb].contains(ok)) {

											yiwen = true;
											lineupanddown = null;
											waitCloud = false;
											continue;
										}
										if (paraname[nb].trim().contains(ok)) {
											if (paraname[nb].trim().contains(
													"[")) {
												if (!ok.contains("[")
														&& !ok2.contains("[")) {
													yiwen = true;
													lineupanddown = null;
													waitCloud = false;
													continue;
												}
											}
										}
									}

									if (!yiwen && !wuruzheli) {
										parasame = true;
									
									}

								}

								if (s1.length != paraname.length) {
									lineupanddown = null;
									waitCloud = false;
									continue;
								}

								if (parasame) {

									Methodfeature Methodfeature1 = new Methodfeature();
									Methodfeature1.methodfullname = methodList
											.get(q);
									Methodfeature1.methodlinesbe = methodline;
									methodlines.add(Methodfeature1);
									continue;
								}

							}
							if (paraname == null) {
								lineupanddown = null;
								waitCloud = false;
								continue;
							}

						}// 多参数
						else {// 一个参数
							boolean parasame = false;
							if (paraname != null) {
								if (paraname.length == 1) {
									if (paraname[0].contains("$")) {
										paraname[0] = paraname[0].replaceAll("\\$", "\\.");
									}
									boolean yiwen = false;
									String ok = lineupanddown.trim();
									String ook = lineupanddown.trim();
									String ok2 = null;
									lineupanddown = null;
									String yy[] = {};
									if (!ok.contains(" ")) {
										continue;
									}
									yy = ok.split(" ");
									if (yy[0].equals("final")) {
										ok = yy[1];
										ok2 = yy[2];
									} else {
										ok = yy[0];
										ok2 = yy[1];
									}
									if (!paraname[0].trim().contains(ok)) {
										yiwen = true;
										waitCloud = false;
										continue;
									}
									if (paraname[0].trim().contains(ok)) {
										if (paraname[0].trim().contains("[")) {
											if (!ok.contains("[")
													&& !ok2.contains("[")) {
												yiwen = true;
												waitCloud = false;
												continue;
											}
										}
									}

									if (!yiwen) {
										parasame = true;
									}

								}

								if (paraname.length != 1) {
									lineupanddown = null;
									waitCloud = false;
									continue;
								}
							}
							if (paraname == null) {
								lineupanddown = null;
								waitCloud = false;
								continue;
							}

							if (parasame) {

								Methodfeature Methodfeature1 = new Methodfeature();
								Methodfeature1.methodfullname = methodList.get(q);
								Methodfeature1.methodlinesbe = methodline;
								methodlines.add(Methodfeature1);
								continue;
							}
						}// 一个参数

					}

					char u;
					char o;
					String oo = null;
					String uu = null;
					boolean before = false;
					if (tempString1.contains(methodListonly)) {
						
						int pp = tempString1.indexOf(methodListonly);
						if (tempString1.contains("(")) {
						
							if (pp + methodListonly.length() < tempString1.length()) {// 大熊
								o = tempString1.charAt(pp+ methodListonly.length());
								oo = String.valueOf(o);
							} else {
								continue;
							}
						}
						if (pp > 0) {// 前一字母
							u = tempString1.charAt(pp - 1);
							uu = String.valueOf(u);
						}

						if (tempString1.contains("//")) {
							int z = tempString1.indexOf("//");
							if (z < pp) {
								before = true; // 被注释掉的内容
							}
						}
					}
					boolean onextkh = false;
					int wy = tempString1.indexOf(methodListonly)
							+ methodListonly.length() + 1;
					if (oo != null) {

						if (!oo.equals("(")&& (oo.equals(" ") || oo.equals("\t"))) {

							if (String.valueOf(tempString1.charAt(wy)).equals("(")) {
								onextkh = true;
					
							}
						}
						if (oo.equals("(") || onextkh) {

							if (!before&& tempString1.contains(methodListonly)&& (!tempString1.contains(";"))&& (!tempString1.contains("||"))&& (!tempString1.contains("if("))
									&& (!tempString1.contains("&&"))&& (!tempString1.contains("="))&& tempString1.contains("(")&& (uu.equals(" ") || uu.equals("\t"))
									&& ((!tempString1.contains("public")&& !tempString1.contains("protected") && !tempString1.contains("private")))) {
								waitnopu = true;
							}
						
							/**
							 * public 函数名 参数
							 */
							if (waitAZU&& !before&& tempString1.contains(methodListonly)&& (!tempString1.contains(";"))&& tempString1.contains("(")&& (uu.equals(".") || uu.equals(" ") || uu.equals("\t"))&& ((!tempString1.contains("public")
											&& !tempString1.contains("protected") && !tempString1.contains("private")))) {
								waitAZU = false;
								heheazu = true;
							}
						}
					}
					if (oo == null) {
						continue;
					}
					/**
					 * public 函数名 参数
					 */
					if (!tempString1.contains("class")&& !tempString1.contains("=")&& !tempString1.contains("(")&& !before&& (!tempString1.contains(methodListonly))
							&& (!tempString1.contains(";"))&& ((tempString1.contains("public")|| tempString1.contains("protected") || tempString1.contains("private")) && (!tempString1.contains("abstract")))
							&& (!tempString1.contains("{"))) {
						waitAZU = true;
						continue;
					}
					// 同时有{}
					if ((!before&& tempString1.contains(methodListonly)&& (tempString1.contains("{"))&& (tempString1.contains("}"))&& tempString1.contains("(")&& (oo.equals("(") || onextkh)
							&& (uu.equals(".") || uu.equals(" ") || uu.equals("\t"))&& (tempString1.contains("public")|| tempString1.contains("protected")|| tempString1.contains("private") || tempString1.contains("void")) && (!tempString1.contains("abstract")))) {
						khsametime = true;
					}
				
					if (khsametime || waitnopu || heheazu || (!before && tempString1.contains(methodListonly) && (!tempString1.contains(";")) && tempString1.contains("(")
									&& (oo.equals("(") || onextkh) && (uu.equals(".") || uu.equals(" ") || uu.equals("\t"))
									&& (tempString1.contains("public")|| tempString1.contains("protected")|| tempString1.contains("private") || tempString1.contains("void")) && (!tempString1.contains("abstract")))) {
					
						heheazu = false;
						waitnopu = false;
						khsametime = false;
						boolean parasame = false;
						boolean otherkh = false;
						boolean Nofirkh = false;
						int x = 0;
						if (onextkh) {
							x = tempString1.indexOf(methodListonly) + methodListonly.length() + 1;
						} else {
							x = tempString1.indexOf(methodListonly) + methodListonly.length();
						}
						int j = 0;
						boolean skh = false;
						// 新增括号匹配算法
						if (tempString1.contains("(")) {
							if (tempString1.charAt(x) == '(') {

								stackkh.clear();
								for (int r = x; r < tempString1.length(); r++) {
									if (tempString1.charAt(r) == '(') {
										stackkh.add('(');
									}
									if (tempString1.charAt(r) == ')') {
										if (stackkh.pop() == '(') {
											if (stackkh.empty()) {
												skh = true;
											} else {
												continue;
											}
										}

									}
									if (skh) {
										stackkh.clear();
										otherkh = true;
										skh = false;
										j = r;
										break;
									}
								}
							}
							if (tempString1.charAt(x) != '(') {
								Nofirkh = true;
								otherkh = false;
							}
						}
						if (!tempString1.contains("(")) {
							Nofirkh = true;
							otherkh = false;
						}

						if (otherkh) {// 括号匹配算法后，有另一个（的情形
							otherkh = false;
							if (j - x > 1) {// 有参数
								if (tempString1.substring(x + 1, j).contains(",")) {// 多参数的情况
									String s[] = {};
									s = tempString1.substring(x + 1, j).split("\\,");

									if (paraname != null) {
										if (s.length == paraname.length) {
											boolean wuruzheli = false;
											boolean yiwen = false;
											for (int nb = 0; nb < s.length; nb++) {
												if (paraname[nb].contains("$")) {
													paraname[nb] = paraname[nb].replaceAll("\\$","\\.");
												}
												String ok = s[nb].trim();
												String ok2 = null;
												String yy[] = {};
												if (!ok.contains(" ")) {
													wuruzheli = true;
													break;
												}
												yy = ok.split(" ");
												/**
												 * 新增几种例外 final && String[]
												 */
												if (yy[0].equals("final")) {
													ok = yy[1];
													ok2 = yy[2];
												} else {
													ok = yy[0];
													ok2 = yy[1];
												}

												if (!paraname[nb].contains(ok)) {
													yiwen = true;
													// continue;
												}
												if (paraname[nb].trim().contains(ok)) {
													if (paraname[nb].trim().contains("[")) {
														if (!ok.contains("[")&& !ok2.contains("[")) {
															yiwen = true;
														}
													}
												}

											}
											if (wuruzheli) {
												continue;
											}
											if (!yiwen) {
												parasame = true;
											}
											if (yiwen) {
												continue;
											}
										}
										if (s.length != paraname.length) {
											continue;
										}
									}
								} else {// 一个参数的情况
									if (paraname != null) {
										if (paraname.length == 1) {
											if (paraname[0].contains("$")) {// 内部类
												paraname[0] = paraname[0].replaceAll("\\$","\\.");
											}
											boolean yiwen = false;
											String ok = tempString1.substring(
													x + 1, j).trim();
											String ok2 = null;
											String yy[] = {};
											if (!ok.contains(" ")) {
												continue;
											}

											yy = ok.split(" ");
											if (yy[0].equals("final")) {
												ok = yy[1];
												ok2 = yy[2];
											} else {
												ok = yy[0];
												ok2 = yy[1];
											}
											if (!paraname[0].trim().contains(ok)) {
												yiwen = true;
											}
											if (paraname[0].trim().contains(ok)) {
												if (paraname[0].trim().contains("[")) {
													if (!ok.contains("[")&& !ok2.contains("[")) {
														yiwen = true;
													}
												}
											}

											if (!yiwen) {
												parasame = true;
											}
											if (yiwen) {
												continue;
											}
										}
										if (paraname.length != 1) {
											continue;
										}
									}
									if (paraname == null) {
										continue;
									}
								}
							} else {// 无参数
								if (paraname == null) {
									parasame = true;
								}
								if (paraname != null) {
									continue;

								}
							}

						} else {
							if (!Nofirkh) {
								waitnext = true;
								lineupanddown = tempString1.substring(x,tempString1.length());
								continue;
							}
							if (Nofirkh) {
								waitnext = true;
								Nofirkh = false;
								lineupanddown = null;
								continue;
							}
						}

						if (parasame) {

							Methodfeature Methodfeature1 = new Methodfeature();
							Methodfeature1.methodfullname = methodList.get(q);
							Methodfeature1.methodlinesbe = methodline;
							methodlines.add(Methodfeature1);
							if (!tempString1.contains("{")) {
								Throw = true;
								waitnext = true;
								lineupanddown = null;
							}
							if (tempString1.contains("{")&& !tempString1.contains("}")) {
								tempStringfirst = tempString1;
								waitCloud = true;
								stack.clear();

							}
							if (tempString1.contains("{")&& tempString1.contains("}"))// 同时包含{}
							{
								methodlines.get(methodlines.size() - 1).methodlinesaf = methodline;
								break;
							}
							continue;
						}
					} else {
						continue;
					}

				}// readline
				reader1.close();

			}// 没个方法循环一次

			Comparatormethodline comparator = new Comparatormethodline();// 临时注掉
			Collections.sort(methodlines, comparator);
			for (int m = 0; m < methodlines.size(); m++) {
				if (methodlines.get(m).methodlinesaf == 0) {
					methodlines.remove(m);
				}

			}
		}
		return methodlines;
	}
	
	public static boolean filterkeyword(String str) {

		boolean fl = false;

		if (str.equalsIgnoreCase("boolean") || str.equalsIgnoreCase("public")
				|| str.equalsIgnoreCase("static")
				|| str.equalsIgnoreCase("double")
				|| str.equalsIgnoreCase("String")
				|| str.equalsIgnoreCase("int")
				|| str.equalsIgnoreCase("return")
				|| str.equalsIgnoreCase("long") || str.equalsIgnoreCase("if")
				|| str.equalsIgnoreCase("else")
				|| str.equalsIgnoreCase("Collections")
				|| str.equalsIgnoreCase("short")
				|| str.equalsIgnoreCase("Integer")
				|| str.equalsIgnoreCase("float")
				|| str.equalsIgnoreCase("void")
				|| str.equalsIgnoreCase("class")
				|| str.equalsIgnoreCase("classloader")
				|| str.equalsIgnoreCase("ArrayList")
				|| str.equalsIgnoreCase("List") || str.equalsIgnoreCase("size")
				|| str.equalsIgnoreCase("null")
				|| str.equalsIgnoreCase("contains")
				|| str.equalsIgnoreCase("IOException")
				|| str.equalsIgnoreCase("wucanshu")
				|| str.equalsIgnoreCase("File") || str.equalsIgnoreCase("for")
				|| str.equalsIgnoreCase("enum") || str.equalsIgnoreCase("math")
				|| str.equalsIgnoreCase("stringbuffer")
				|| str.equalsIgnoreCase("new") || str.equalsIgnoreCase("stack")
				|| str.equalsIgnoreCase("pop") || str.equalsIgnoreCase("push")
				|| str.equalsIgnoreCase("instanceof")
				|| str.equalsIgnoreCase("stringbuilder")
				|| str.equalsIgnoreCase("throwable")
				|| str.equalsIgnoreCase("runnable")
				|| str.equalsIgnoreCase("byte") || str.equalsIgnoreCase("true")
				|| str.equalsIgnoreCase("false")
				|| str.equalsIgnoreCase("valueof")
				|| str.equalsIgnoreCase("tostring")
				|| str.equalsIgnoreCase("equals")
				|| str.equalsIgnoreCase("char")
				|| str.equalsIgnoreCase("runtime")
				|| str.equalsIgnoreCase("throws")
				|| str.equalsIgnoreCase("exception")
				|| str.equalsIgnoreCase("private")
				|| str.equalsIgnoreCase("try") || str.equalsIgnoreCase("catch")
				|| str.equalsIgnoreCase("finaly")
				|| str.equalsIgnoreCase("BufferedReader")
				|| str.equalsIgnoreCase("ClassObject")
				|| str.equalsIgnoreCase("at") || str.equalsIgnoreCase("this")
				|| str.equalsIgnoreCase("super")
				|| str.equalsIgnoreCase("protected")
				|| str.equalsIgnoreCase("to") || str.equalsIgnoreCase("of")
				|| str.equalsIgnoreCase("the") || str.equalsIgnoreCase("is")
				|| str.equalsIgnoreCase("or")) {
			fl = true;
		}
		return fl;
	}
	
	
	public static ArrayList<String> removeDuplicate(ArrayList<String> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).equals(list.get(i))) {
					list.remove(j);
				}
			}
		}
		return list;

	}
	
	public static boolean needsplit(String str, String judge) {
		boolean flag = false;
		char[] ch = str.toCharArray();
		boolean g1 = false;
		boolean g2 = false;
		for (int i = 0; i < ch.length; i++) {
			if (Character.isUpperCase(ch[i])) {
				g1 = true;
				// ch[i]+=32;
			} else {
				g2 = true;
			}
		}
		int id = str.indexOf(judge);
		if (id + judge.length() < str.length()) {
			if (g1 && g2 && String.valueOf(ch[id + judge.length()]).equals("(")) {
				flag = true;
			}
		}
		return flag;
	}
	
	public static ArrayList<String> paraextract1(ArrayList<String> result,
			String str) {

		String tm = str.replaceAll("\t", " ");
		tm = tm.replaceAll("[^a-zA-Z]", " ").trim();

		if (tm.contains(" ")) {
			String sro[] = {};
			sro = tm.split(" ");

			for (int k = 0; k < sro.length; k++) {
				if (sro[k].trim().length() > 1) {
					if (!filterkeyword(sro[k].trim())) {
						if (needsplit(str, sro[k].trim())) {
							ArrayList<String> uu = new ArrayList<String>();

							uu = splitthestring(uu, sro[k].trim());
							uu = removeDuplicate(uu);
							result.addAll(uu);
						} else {
							ArrayList<String> uu = new ArrayList<String>();
							uu = splitthestring(uu, sro[k].trim());
							uu = removeDuplicate(uu);

							result.addAll(uu);
							// result.add(sro[k].trim());
						}
					}
				}
			}

		} else {

			if (tm.length() > 1) {
				if (!filterkeyword(tm.trim())) {

					if (needsplit(str, tm.trim())) {
						splitthestring(result, tm.trim());
					} else {
						ArrayList<String> uu = new ArrayList<String>();
						uu = splitthestring(uu, tm.trim());
						uu = removeDuplicate(uu);
						result.addAll(uu);
					}

				}
			}
		}

		return result;

	}
	/**
	 * 提取词
	 * 
	 * @throws IOException
	 */
	public static void extractword(ProgressBarLisenter barLisenter) throws IOException {
		String filename = null;
		for (int a = 0; a < SrcAction.classname.size(); a++) {
			int id = 0;
			boolean yn = false;
			String str1[] = {};
			str1 = SrcAction.classname.get(a).split("\\.");
			if (str1.length > 1) {
				filename = str1[0] + "\\";
				for (int b = 1; b < str1.length - 1; b++) {
					filename = filename + str1[b] + "\\";
				}
				if (str1[str1.length - 1].contains("$")) {
					int v = str1[str1.length - 1].indexOf("$");
					String temp = str1[str1.length - 1].substring(0, v);
					filename = filename + temp + ".java";
				} else {
					filename = filename + str1[str1.length - 1] + ".java";
				}
			}
			if (str1.length == 1) {
				filename = str1[str1.length - 1] + ".java";
			}
			for(int i = 0; i < SrcAction.allJavaFilePaths.size(); i++){
				if(SrcAction.allJavaFilePaths.get(i).contains(filename)){
					filename = SrcAction.allJavaFilePaths.get(i);
					break;
				}
			}
			File file0 = new File(filename);
			BufferedReader reader1 = new BufferedReader(new FileReader(file0)); // 读取java文件
			String tempString1 = null;

			int methodline = 0;
			while ((tempString1 = reader1.readLine()) != null) {
				methodline++;
				
				Map<String, Feature> featuremap  =  SrcAction.classesMap.get(SrcAction.classname.get(a)).featureMap;
				for(Map.Entry<String, Feature> entry : featuremap.entrySet()) {  
					String key = entry.getKey();
					Feature value = entry.getValue();
					if ((methodline >= value.methodlinesbe) && (methodline <= value.methodlinesaf)) {

						value.extract = paraextract1(value.extract,tempString1);
						SrcAction.classesMap.get(SrcAction.classname.get(a)).featureMap.put(key, value);
//						System.out.println("取词=="+SaveFileAction.classesMap.get(SaveFileAction.classname.get(a)).featureMap.get(key).extract);
						break;
					}
				}

			}

			Map<String, Feature> featuremap  =  SrcAction.classesMap.get(SrcAction.classname.get(a)).featureMap;
			for(Map.Entry<String, Feature> entry : featuremap.entrySet()) {  
				String key = entry.getKey();
				Feature value = entry.getValue();
				if(key!=null&&value.extract==null){
					value.extract = splitthestring(value.extract,methodonlyname(key));
					SrcAction.classesMap.get(SrcAction.classname.get(a)).featureMap.put(key, value);
//					System.out.println("取词=="+SaveFileAction.classesMap.get(SaveFileAction.classname.get(a)).featureMap.get(key).extract);
				}
			}
			barLisenter.endFile();
	}
		
	}
	
	
	/**
	 * 处理函数的驼峰问题
	 */

	public static ArrayList<Integer> continuousCASE(String str) {
		ArrayList<Integer> jj = new ArrayList<Integer> ();
		char[] ch = str.toCharArray();
		boolean flag = false;
		int id = -1;
		int count = 0;
		for (int i = 0; i < ch.length; i++) {
			if(!flag){
			if (!Character.isLowerCase(ch[i])) {
				id = i;
				flag = true;
				count++;
			}
			}else{
				if (!Character.isLowerCase(ch[i])) {
					count++;
				}else{
					if(count<4){
					count = 0;
					flag = false;
					id = -1;
					}
				}
			}
			}
	
		for(int k = 0; k < count; k ++){
			
			jj.add(id);
			id++;
		}
	
		return jj;
	}
	
	public static ArrayList<String> splitthestring(ArrayList<String> str1, String str) {

		ArrayList<Integer> id = new ArrayList<Integer>();
		char[] ch = str.toCharArray();
		boolean flag = false;
		
		ArrayList<Integer> jj = continuousCASE(str);
		for (int i = 0; i < ch.length; i++) {
			if ((int) ch[i] >= 65 && (int) ch[i] <= 90) {
				id.add(i);
			}
			if (ch[i] == '_') {
				id.add(i);
			}
		}
		if (id.size() == str.length()) {// 都是大写字母，用下划线分割
			id.clear();
			for (int i = 0; i < ch.length; i++) {

				if (ch[i] == '_') {
					id.add(i);
				}
			}

		}

		if (id.size() > 0) {

			if (id.get(0) == 0) {
				if (id.size() > 1 &&(jj.size()<4)) {
					if (id.get(1) == 1) {

						String r = String.valueOf(ch[0]);
						r = r.replaceAll("[^a-zA-Z]", " ").trim();
						if (r.length() > 1) {
							String st[] = {};
							st = r.split(" ");
							for (int o = 0; o < st.length; o++) {
								if (!filterkeyword(st[o].trim()) && st[o].length() > 1) {
									str1.add(st[o]);
								}
							}
						}
					} else {
						String r = str.substring(0, id.get(1)).replaceAll("[^a-zA-Z]", " ").trim();
						if (r.length() > 1) {
							String st[] = {};
							st = r.split(" ");
							for (int o = 0; o < st.length; o++) {
								if (!filterkeyword(st[o].trim()) && st[o].length() > 1) {
									str1.add(st[o]);
								}
							}

						}
					}

				}
				
				if(jj.size()>=4){
					if(jj.get(0)==0&&(jj.size()<str.length())){
						String r = str.substring(0, jj.get(jj.size()-1)).replaceAll("[^a-zA-Z]", " ").trim();
						
						if (r.length() > 1) {
							String st[] = {};
							st = r.split(" ");
							for (int o = 0; o < st.length; o++) {
								if (!filterkeyword(st[o].trim()) && st[o].length() > 1) {
									str1.add(st[o]);
								}
							}

						}
						String r1 = str.substring(jj.get(jj.size()-1), str.length()).replaceAll("[^a-zA-Z]", " ").trim();
						
						if (r1.length() > 1) {
							String st[] = {};
							st = r1.split(" ");
							for (int o = 0; o < st.length; o++) {
								if (!filterkeyword(st[o].trim()) && st[o].length() > 1) {
									str1.add(st[o]);
								}
							}

						}
						
						
					}
					if(jj.get(0)==0&&(jj.size()==str.length())){
						String r = str.replaceAll("[^a-zA-Z]", " ").trim();
						
						if (r.length() > 1) {
							String st[] = {};
							st = r.split(" ");
							for (int o = 0; o < st.length; o++) {
								if (!filterkeyword(st[o].trim()) && st[o].length() > 1) {
									str1.add(st[o]);
								}
							}
						}
					}

					
					
					
				}

				if (id.size() == 1) {

					String r = str.replaceAll("[^a-zA-Z]", " ").trim();

					if (r.length() > 1) {
						String st[] = {};
						st = r.split(" ");
						for (int o = 0; o < st.length; o++) {
							if (!filterkeyword(st[o].trim()) && st[o].length() > 1) {
								str1.add(st[o]);
							}
						}
					}
				}
			}

			else {
				if(jj.size()>=4){
				if(jj.get(0)>0&&(jj.size()<str.length())){
					
					String r = str.substring(0, jj.get(0)).replaceAll("[^a-zA-Z]", " ").trim();
				
					if (r.length() > 1) {
						String st[] = {};
						st = r.split(" ");
						for (int o = 0; o < st.length; o++) {
							if (!filterkeyword(st[o].trim()) && st[o].length() > 1) {
								str1.add(st[o]);
							}
						}

					}
					
					String r1 = str.substring(jj.get(0), jj.get(jj.size()-1)).replaceAll("[^a-zA-Z]", " ").trim();
				
					if (r1.length() > 1) {
						String st[] = {};
						st = r1.split(" ");
						for (int o = 0; o < st.length; o++) {
							if (!filterkeyword(st[o].trim()) && st[o].length() > 1) {
								str1.add(st[o]);
							}
						}

					}
					
					String r2 = str.substring(jj.get(jj.size()-1), str.length()).replaceAll("[^a-zA-Z]", " ").trim();
					if (r2.length() > 1) {
						String st[] = {};
						st = r2.split(" ");
						for (int o = 0; o < st.length; o++) {
							if (!filterkeyword(st[o].trim()) && st[o].length() > 1) {
								str1.add(st[o]);
							}
						}

					}
					
				}
				if(jj.get(0)>0&&(jj.size()==str.length())){
					String r = str.substring(0, jj.get(0)-1).replaceAll("[^a-zA-Z]", " ").trim();
					if (r.length() > 1) {
						String st[] = {};
						st = r.split(" ");
						for (int o = 0; o < st.length; o++) {
							if (!filterkeyword(st[o].trim()) && st[o].length() > 1) {
								str1.add(st[o]);
							}
						}

					}
					
					String r1 = str.substring(jj.get(0), str.length()-1).replaceAll("[^a-zA-Z]", " ").trim();
					if (r1.length() > 1) {
						String st[] = {};
						st = r1.split(" ");
						for (int o = 0; o < st.length; o++) {
							if (!filterkeyword(st[o].trim()) && st[o].length() > 1) {
								str1.add(st[o]);
							}
						}

					}
				}
			}
				
				if(jj.size()<4){
				String r = str.substring(0, id.get(0))
						.replaceAll("[^a-zA-Z]", " ").trim();
				if (r.length() > 1) {
					String st[] = {};
					st = r.split(" ");
					for (int o = 0; o < st.length; o++) {
						if (!filterkeyword(st[o].trim()) && st[o].length() > 1) {
							str1.add(st[o]);
						}
					}
				}

				String rr = str.substring(id.get(id.size() - 1), str.length()).replaceAll("[^a-zA-Z]", " ").trim();
				if (rr.length() > 1) {
					String st[] = {};
					st = rr.split(" ");
					for (int o = 0; o < st.length; o++) {
						if (!filterkeyword(st[o].trim()) && st[o].length() > 1) {
							str1.add(st[o]);
							flag = true;
						}
					}
				}
			}

				
			}
			if (id.size() > 1) {
				for (int m = 1; m < id.size(); m++) {

					int length = id.get(m) - id.get(m - 1);
					if (length > 1 && id.get(m - 1) > 0) {
						
						String r = str.substring(id.get(m - 1), id.get(m))
								.replaceAll("[^a-zA-Z]", " ").trim();
						if (r.length() > 1) {
							String st[] = {};
							st = r.split(" ");
							for (int o = 0; o < st.length; o++) {
								if (!filterkeyword(st[o].trim())
										&& st[o].length() > 1) {
									str1.add(st[o]);
								}
							}
							
						}
					}
					if (length == 1 && id.get(m - 1) > 0) {

						String r = String.valueOf(ch[id.get(m - 1)])
								.replaceAll("[^a-zA-Z]", " ").trim();

						if (r.length() > 1) {
							String st[] = {};
							st = r.split(" ");
							for (int o = 0; o < st.length; o++) {
								if (!filterkeyword(st[o].trim())
										&& st[o].length() > 1) {
									str1.add(st[o]);
								}
							}
						}
					}

				}

				if (!flag) {
					
					String r = str
							.substring(id.get(id.size() - 1), str.length())
							.replaceAll("[^a-zA-Z]", " ").trim();
					if (r.length() > 1) {
						String st[] = {};
						st = r.split(" ");
						for (int o = 0; o < st.length; o++) {
							if (!filterkeyword(st[o].trim())
									&& st[o].length() > 1) {
								str1.add(st[o]);
							}
						}
					}
				}

			}
			
		} else {
			if (!filterkeyword(str)) {
				String r = str.replaceAll("[^a-zA-Z]", " ").trim();
				if (r.length() > 1) {
					String st[] = {};
					st = r.split(" ");
					for (int o = 0; o < st.length; o++) {
						if (!filterkeyword(st[o].trim()) && st[o].length() > 1) {
							str1.add(st[o]);
						}
					}
				}
			}
		}
		
		return str1;

	}

	 public static double[] bubsort(double[] num){  
	        int i, j, k, flag = 1;  
	        double temp;
	        long start,end;  
	        int MAX = num.length;
	      
	        for(i = 0; i < MAX-1 && flag == 1; i++) {  
	            flag = 0;  
	            for(j = 0; j < MAX-i-1; j++) {  
	                if(num[j+1] < num[j]) {  
	                    temp=num[j+1];  
	                    num[j+1]=num[j];  
	                    num[j]=temp;  
	                    flag = 1;  
	                }  
	            }  
	        }
			return num;
	 }
	
		public static double[][] filterSemantic1(double[][] SM){
			double min = 0;
			int cnt = 0;
			for(int i = 0; i < SM.length; i++){
				for(int j = 0; j < SM.length; j++){
					if(  SM[i][j] !=0){
						cnt++;
					}
				}
			}
			double num[]=new double[cnt]; 
			int dx = 0;
			for(int i = 0; i < SM.length; i++){
				for(int j = 0; j < SM.length; j++){
					if(  SM[i][j] !=0){
					num[dx] =  SM[i][j];
					}
				}
			}
			
			num = bubsort(num);
			if(num.length>2){
			if(num.length % 2 == 0){//偶数
				min  = (num[num.length/2 - 1] + num[num.length/2])/(double)2;
			}else{
				min  = num[(num.length + 1)/2];
			}
			}
			
			for(int i = 0; i < SM.length; i++){
				for(int j = 0; j < SM.length; j++){
					if(SM[i][j] < min){
						SM[i][j] = 0;
					}
				}
			}		
			
			for(int i = 0; i < SM.length; i++){
				for(int j = i+1; j < SM.length; j++){
					SM[i][j] = SM[i][j] * Math.random();
					SM[j][i] = SM[i][j];
				}
			}
			return SM;		
		}
		public static double[][] filterSemantic(double[][] SM){
			double min = 0;
			int cnt = 0;
			for(int i = 0; i < SM.length; i++){
				for(int j = 0; j < SM.length; j++){
					if(  SM[i][j] !=0){
						cnt++;
					}
				}
			}
			double num[]=new double[cnt]; 
			int dx = 0;
			for(int i = 0; i < SM.length; i++){
				for(int j = 0; j < SM.length; j++){
					if(  SM[i][j] !=0){
					num[dx] =  SM[i][j];
					}
				}
			}
			
			num = bubsort(num);
			if(num.length>2){
			if(num.length % 2 == 0){//偶数
				min  = (num[num.length/2 - 1] + num[num.length/2])/(double)2;
			}else{
				min  = num[(num.length + 1)/2];
			}
			}
			
			for(int i = 0; i < SM.length; i++){
				for(int j = 0; j < SM.length; j++){
					if(SM[i][j] < min){
						SM[i][j] = 0;
					}
				}
			}		
			
		
			return SM;		
		}
		
		public static double[][] filterSemanticIher(double[][] SM){
			double min = 0;
			int cnt = 0;
			for(int i = 0; i < SM.length; i++){
				for(int j = 0; j < SM.length; j++){
					if(  SM[i][j] !=0){
						cnt++;
					}
				}
			}
			double num[]=new double[cnt]; 
			int dx = 0;
			for(int i = 0; i < SM.length; i++){
				for(int j = 0; j < SM.length; j++){
					if(  SM[i][j] !=0){
					num[dx] =  SM[i][j];
					dx++;
					}
				}
			}
			
			num = bubsort(num);
			if(num.length>2){
			if(num.length % 2 == 0){//偶数
				min  = (num[num.length/2 - 1] + num[num.length/2])/(double)2;
			}else{
				min  = num[(num.length + 1)/2];
			}
			}
			
			for(int i = 0; i < SM.length; i++){
				for(int j = 0; j < SM.length; j++){
					if(SM[i][j] < min){
						SM[i][j] = 0;
					}
				}
			}			
			return SM;		
		}

	
	
	@SuppressWarnings("static-access")
	public static double[][] makesemanticmatrix(ArrayList<String> methodlist) throws SAXException, IOException,
			MWException {
		int n = methodlist.size();
		double[][] x = new double[n][n];
		x = MatrixComputing.makezeroMatrix(x);
		if(methodlist.size()>1){
		TermDoc.words.clear();
		TermDoc.toowords.clear();
		TermDoc.newwords.clear();
		TermDoc.vectors.clear();
	
		for (int i = 0; i < n; i++) {
			ArrayList<String> Document = new ArrayList<String>();
			ArrayList<String> extract = new ArrayList<String>();
			if (methodlist.get(i) != null) {
				 ClassObject cObject = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(i)));
				 extract = cObject.featureMap.get(methodlist.get(i)).extract;
				 Document.addAll(extract);
			}
			TermDoc.searchList(Document);

		}
		   wordss = TermDoc.words;
		   if(wordss.size()>0){
		if (wordss.size() >= methodlist.size()) {
			 System.out.println("here=="+wordss.size());
			double[][] s = TermDoc.Term();
			Matrix A =new Matrix( s.length, s[0].length );
			for(int i=0;i<s.length;i++)
			{
				for(int j=0;j<s[i].length;j++)
				{
					A.set(i, j, s[i][j]);
				}
			}
		    SingularValueDecomposition svd = A.svd();						
			DoubleMatrix2D U = getSparseDoubleMatrix(svd.getU());
			DoubleMatrix2D S = getSparseDoubleMatrix(svd.getS());
			DoubleMatrix2D V =getSparseDoubleMatrix( svd.getV());
			DoubleMatrix2D newA = VSM.getnewA(U, S, V);

			for (int i = 0; i < n; i++) {
				x[i] = VSM.cosineMatrix(newA,i);// x[i]为第i个方法与所有方法计算的余弦值（所有第i行）

			}

			 
		} else {
//			 System.out.println("there=="+wordss.size());
			FillWords(methodlist, wordss);
			x = makesemanticmatrix(methodlist);
			
		}

		   }
		   

		}
		for(int i = 0 ; i < x.length; i++){
			x[i][i] = 0;
		}
		
		return x;
	}
	
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

	
	/**将Matrix型矩阵转为DoubleMatrix2D形式
	 * @param a
	 * @return
	 */
	private static DoubleMatrix2D getSparseDoubleMatrix(Matrix a) {
		// TODO Auto-generated method stub
		DoubleMatrix2D newA =new SparseDoubleMatrix2D(a.getRowDimension(), a.getColumnDimension());
		for(int i=0;i<a.getRowDimension();i++)
		{
			for(int j=0;j<a.getColumnDimension();j++)
			{
				newA.set(i, j, a.get(i, j));
			}
		}
		return newA;
	}

	public static ArrayList<String> getExtractfrominbound(ArrayList<String> methodlist,List<String> inboundmethodlist) throws SAXException, IOException {
		ArrayList<String> extract = new ArrayList<String>();
		for (int i = 0; i < inboundmethodlist.size(); i++) {
			if (!methodlist.contains(inboundmethodlist.get(i))) {
				extract.addAll(SrcAction.classesMap.get(SourceParser.Getonlyclassname(inboundmethodlist.get(i))).featureMap.get(inboundmethodlist.get(i)).extract);
			}
		}

		return extract;
	}
	
	
	public static List<String> removedup (List<String> extract, ArrayList<String> words){
		 ArrayList<String> insect = new  ArrayList<String>();
		for(int i= 0; i <  extract.size();i++){
			
			for(int k = 0; k < words.size(); k++){
				if(extract.get(i).equalsIgnoreCase(words.get(k))){
					insect.add(extract.get(i));
				}
				
			}
			
		}
		
		extract.removeAll(insect);
		
		return extract;
		
	}
	
	
	
	public static void FillWords(ArrayList<String> methodlist, ArrayList<String> Wordlist) throws SAXException, IOException {
		
		int n = methodlist.size();
		int m = Wordlist.size();
		int que = n - m;
		if(n - m > 0){
	//	ArrayList<String> newWords = new ArrayList<String>();
		int newwd = 0;
		for (int i = 0; i < n; i++) {
			ArrayList<String> extract = new ArrayList<String>();
			if (methodlist.get(i) != null) {
				//boolean stop = false;
				 ClassObject cObject = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(i)));
				 extract = cObject.featureMap.get(methodlist.get(i)).extract;
				 
					if (!cObject.featureMap.get(methodlist.get(i)).inboundMethodList.isEmpty()) {
						List<String> extractinbound =  (List<String>) getExtractfrominbound(methodlist,cObject.featureMap.get(methodlist.get(i)).inboundMethodList).clone();
						if(!extractinbound.isEmpty()&& (que > 0)){
						
						extractinbound =removedup(extractinbound,Wordlist);
						newwd = newwd + extractinbound.size();
						if (extractinbound.size()>= que) {
						
							extractinbound = extractinbound.subList(0, que);
							Wordlist.addAll(extractinbound);
							cObject.featureMap.get(methodlist.get(i)).extract.addAll(extractinbound);
							SrcAction.classesMap.put(SourceParser.Getonlyclassname(methodlist.get(i)), cObject);
							break;
						}else{
							Wordlist.addAll(extractinbound);
							cObject.featureMap.get(methodlist.get(i)).extract.addAll(extractinbound);
							SrcAction.classesMap.put(SourceParser.Getonlyclassname(methodlist.get(i)), cObject);
							que = n - m - newwd;
						}
									}

					}
					
					if (!cObject.featureMap.get(methodlist.get(i)).outboundMethodList.isEmpty()) {
						List<String> extractoutbound=  (List<String>) getExtractfrominbound(methodlist,cObject.featureMap.get(methodlist.get(i)).inboundMethodList).clone();
						if(!extractoutbound.isEmpty()&& (que > 0)){
						
						extractoutbound =removedup(extractoutbound,Wordlist);
						newwd = newwd + extractoutbound.size();
						if (extractoutbound.size()>= que) {
						
							extractoutbound = extractoutbound.subList(0, que);
							Wordlist.addAll(extractoutbound);
							cObject.featureMap.get(methodlist.get(i)).extract.addAll(extractoutbound);
							SrcAction.classesMap.put(SourceParser.Getonlyclassname(methodlist.get(i)), cObject);
							break;
						}else{
							Wordlist.addAll(extractoutbound);
							cObject.featureMap.get(methodlist.get(i)).extract.addAll(extractoutbound);
							SrcAction.classesMap.put(SourceParser.Getonlyclassname(methodlist.get(i)), cObject);
							que = n - m - newwd;
						}
									}

					}
					
					
					
					
					if (!cObject.featureMap.get(methodlist.get(i)).inboundClassList.isEmpty()) {
						
					for(int b = 0;b < cObject.featureMap.get(methodlist.get(i)).inboundClassList.size();b++){
						boolean stop = false;
						Map<String, Feature> featureMap = SrcAction.classesMap.get(SourceParser.getonlyclassname(cObject.featureMap.get(methodlist.get(i)).inboundClassList.get(b))).featureMap;
						 for(Map.Entry<String, Feature> entry1 : featureMap.entrySet()) {
					    	   String key1 = entry1.getKey();  
					    	   Feature value1 = entry1.getValue();
					    	   
								List<String> extractinbound =  (List<String>)value1.extract.clone();

								if(!extractinbound.isEmpty()&&que >0){
								extractinbound =removedup (extractinbound,Wordlist);
								newwd = newwd + extractinbound.size();
								if (extractinbound.size() >= que) {
									stop = true;
									extractinbound = extractinbound.subList(0, que);
									Wordlist.addAll(extractinbound);
									cObject.featureMap.get(methodlist.get(i)).extract.addAll(extractinbound);
									SrcAction.classesMap.put(SourceParser.Getonlyclassname(methodlist.get(i)), cObject);
									break;
								}else{
									Wordlist.addAll(extractinbound);
									cObject.featureMap.get(methodlist.get(i)).extract.addAll(extractinbound);
									SrcAction.classesMap.put(SourceParser.Getonlyclassname(methodlist.get(i)), cObject);
									que = n - m - newwd;
								}
					    	   
						 }
							
					}
							if(stop){
								break;
							}
					}
					}
					
					
					if (!cObject.featureMap.get(methodlist.get(i)).outboundClassList.isEmpty()) {
						
					for(int b = 0;b < cObject.featureMap.get(methodlist.get(i)).outboundClassList.size();b++){
						boolean stop = false;
						Map<String, Feature> featureMap = SrcAction.classesMap.get(SourceParser.getonlyclassname(cObject.featureMap.get(methodlist.get(i)).outboundClassList.get(b))).featureMap;
						 for(Map.Entry<String, Feature> entry1 : featureMap.entrySet()) {
					    	   String key1 = entry1.getKey();  
					    	   Feature value1 = entry1.getValue();
					    	   
								List<String> extractinbound =  (List<String>)value1.extract.clone();

								if(!extractinbound.isEmpty()&&que >0){
								extractinbound =removedup (extractinbound,Wordlist);
								newwd = newwd + extractinbound.size();
								if (extractinbound.size() >= que) {
									stop = true;
									extractinbound = extractinbound.subList(0, que);
									Wordlist.addAll(extractinbound);
									cObject.featureMap.get(methodlist.get(i)).extract.addAll(extractinbound);
									SrcAction.classesMap.put(SourceParser.Getonlyclassname(methodlist.get(i)), cObject);
									break;
								}else{
									Wordlist.addAll(extractinbound);
									cObject.featureMap.get(methodlist.get(i)).extract.addAll(extractinbound);
									SrcAction.classesMap.put(SourceParser.Getonlyclassname(methodlist.get(i)), cObject);
									que = n - m - newwd;
								}
					    	   
						 }
							
					}
							if(stop){
								break;
							}
					}
					}
		
	
			}
			que = n - m - newwd;

			if(que<=0){
			break;
		}

		}// for(int i = 0 ; i < n ;i++){
		
		if(que>0){
			
			for(Map.Entry<String, ClassObject> entry : SrcAction.classesMap.entrySet()) {  
				   boolean stop = false;
			       ClassObject value = entry.getValue();
			       for(Map.Entry<String, Feature> entry1 : value.featureMap.entrySet()) { 
			    	   String keyString = entry1.getKey();
			    	   Feature value1 = entry1.getValue();
			    	   List<String> extractinbound =  value1.extract;
			    	   if(!methodlist.contains(keyString)){
						if(!extractinbound.isEmpty()&&que >0){

						extractinbound =removedup (extractinbound,Wordlist);
						newwd = newwd + extractinbound.size();
						if(!extractinbound.isEmpty()){
						if (extractinbound.size()>= que) {
							stop = true;
							extractinbound = extractinbound.subList(0, que);
							Wordlist.addAll(extractinbound);	
							SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(0))).featureMap.get(methodlist.get(0)).extract.addAll(extractinbound);
							break;
						}else{

							Wordlist.addAll(extractinbound);
							SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(0))).featureMap.get(methodlist.get(0)).extract.addAll(extractinbound);
							que = n - m - newwd;
							
						}
						}
									}
					
			       }
			       }
			   	if(stop){
					break;
				}
			   	if((n - m - newwd)<0){
					break;
				}
			}
			
			
		}
		
		
	}
	}
}
