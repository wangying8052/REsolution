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



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.SAXException;

import com.mathworks.toolbox.javabuilder.MWException;

public class SourceParser {
	
	
	public static String ShortClassname(String classname) {
		classname = SourceParser.getonlyclassname(classname);
		String str[] = {};
		str = classname.split("\\.");
		classname = str[str.length-1];
		return classname;
		
	}
	/**
	 * 判断该属性是不是本系统中的
	 */
	public static boolean judgeFeatureifthisSystem(String featurename, ArrayList<String> classname) {
		boolean same = false;
		if (featurename.contains("(") && featurename.contains(")")|| featurename.contains("{") && featurename.contains("}")) {
			String feature_outfeaturename = null;
			if (featurename.contains("(") && featurename.contains(")")) {
				String str0[] = {};
				int a = featurename.indexOf("(");
				String feature_outfeaturenametemp = featurename.substring(0, a); // 先去括号
				str0 = feature_outfeaturenametemp.toString().split("\\."); // 再取最后一个.之前的字符串，即类名
				if (str0.length > 2) {
					feature_outfeaturename = str0[0] + ".";
					for (int i = 1; i < str0.length - 2; i++) {
						feature_outfeaturename = feature_outfeaturename + str0[i] + ".";
					}
					feature_outfeaturename = feature_outfeaturename + str0[str0.length - 2];
				} else {
					feature_outfeaturename = str0[0];
				}
			}
			if (featurename.contains("{") && featurename.contains("}")) {
				String str0[] = {};
				featurename = featurename.replaceAll(" ", "");
				int a = featurename.indexOf("{");
				String feature_outfeaturenametemp = featurename.substring(0, a); // 先去括号
				str0 = feature_outfeaturenametemp.toString().split("\\."); // 再取最后一个.之前的字符串，即类名
				if (str0.length > 2) {
					feature_outfeaturename = str0[0] + ".";
					for (int i = 1; i < str0.length - 2; i++) {
						feature_outfeaturename = feature_outfeaturename + str0[i] + ".";
					}
					feature_outfeaturename = feature_outfeaturename + str0[str0.length - 2];
				} else {
					feature_outfeaturename = str0[0];
				}
			}

			String feature_outfeaturenamelast = null;
			if (feature_outfeaturename.contains("$")) {
				int nn = feature_outfeaturename.indexOf("$");
				feature_outfeaturenamelast = feature_outfeaturename.substring(0, nn);
			} else {
				feature_outfeaturenamelast = feature_outfeaturename;
			}

				if (classname.contains(feature_outfeaturenamelast)) {
					same = true;
				}

		} else {// 不包含括号
			
			String str0[] = {};
			String feature_outfeaturename = featurename;
			str0 = feature_outfeaturename.toString().split("\\."); // 再取最后一个.之前的字符串，即类名
			if (str0.length > 2) {
				feature_outfeaturename = str0[0] + ".";
				for (int i = 1; i < str0.length - 2; i++) {
					feature_outfeaturename = feature_outfeaturename + str0[i] + ".";
				}
				feature_outfeaturename = feature_outfeaturename + str0[str0.length - 2];
			} else {
				feature_outfeaturename = str0[0];
			}

			String feature_outfeaturenamelast = null;
			if (feature_outfeaturename.contains("$")) {
				int mm = feature_outfeaturename.indexOf("$");
				feature_outfeaturenamelast = feature_outfeaturename.substring(0, mm);
			} else {
				feature_outfeaturenamelast = feature_outfeaturename;
			}
			
			if (classname.contains(feature_outfeaturenamelast)) {
				same = true;
			}

		}

		return same;
	}
	
	
	
	public static boolean judgeIsClassorFeature(String name) {
		boolean same = false;
		if (name.contains("(") && name.contains(")")|| name.contains("{") && name.contains("}")) {
			same = true;
		}
		return same;//true is feature false is class
	}
	
	
	public static String Getonlyclassname(String featurename) {
		
		String feature_outfeaturenamelast = null;
		if (featurename.contains("(") && featurename.contains(")") || featurename.contains("{") && featurename.contains("}")) {
			String feature_outfeaturename = null;
			if (featurename.contains("(") && featurename.contains(")")) {
				String str0[] = {};
				int a = featurename.indexOf("(");
				String feature_outfeaturenametemp = featurename.substring(0, a); // 先去括号
				str0 = feature_outfeaturenametemp.toString().split("\\."); // 再取最后一个.之前的字符串，即类名

				if (str0.length > 2) {
					feature_outfeaturename = str0[0] + ".";
					for (int i = 1; i < str0.length - 2; i++) {
						feature_outfeaturename = feature_outfeaturename
								+ str0[i] + ".";
					}
					feature_outfeaturename = feature_outfeaturename
							+ str0[str0.length - 2];
				} else {
					feature_outfeaturename = str0[0];
				}
			}
			if (featurename.contains("{") && featurename.contains("}")) {
				String str0[] = {};
				featurename = featurename.replaceAll(" ", "");
				int a = featurename.indexOf("{");
				String feature_outfeaturenametemp = featurename.substring(0, a); // 先去括号
				str0 = feature_outfeaturenametemp.toString().split("\\."); // 再取最后一个.之前的字符串，即类名
				
				if (str0.length > 2) {
					feature_outfeaturename = str0[0] + ".";
					for (int i = 1; i < str0.length - 2; i++) {
						feature_outfeaturename = feature_outfeaturename
								+ str0[i] + ".";
					}
					feature_outfeaturename = feature_outfeaturename
							+ str0[str0.length - 2];
				} else {
					feature_outfeaturename = str0[0];
				}
			}

			if (feature_outfeaturename.contains("$0")
					|| feature_outfeaturename.contains("$1")
					|| feature_outfeaturename.contains("$2")
					|| feature_outfeaturename.contains("$3")
					|| feature_outfeaturename.contains("$4")
					|| feature_outfeaturename.contains("$5")
					|| feature_outfeaturename.contains("$6")
					|| feature_outfeaturename.contains("$7")
					|| feature_outfeaturename.contains("$8")
					|| feature_outfeaturename.contains("$9")) {
				int nn = feature_outfeaturename.indexOf("$");
				feature_outfeaturenamelast = feature_outfeaturename.substring(
						0, nn);
				
			} else {
				feature_outfeaturenamelast = feature_outfeaturename;
			}

		} else {// 不包含括号
			String str0[] = {};
			String feature_outfeaturename = featurename;
			str0 = feature_outfeaturename.toString().split("\\."); // 再取最后一个.之前的字符串，即类名
			if (str0.length > 2) {
				feature_outfeaturename = str0[0] + ".";
				for (int i = 1; i < str0.length - 2; i++) {
					feature_outfeaturename = feature_outfeaturename + str0[i]
							+ ".";
				}
				feature_outfeaturename = feature_outfeaturename
						+ str0[str0.length - 2];
			} else {
				feature_outfeaturename = str0[0];
			}

			if (feature_outfeaturename.contains("$0")
					|| feature_outfeaturename.contains("$1")
					|| feature_outfeaturename.contains("$2")
					|| feature_outfeaturename.contains("$3")
					|| feature_outfeaturename.contains("$4")
					|| feature_outfeaturename.contains("$5")
					|| feature_outfeaturename.contains("$6")
					|| feature_outfeaturename.contains("$7")
					|| feature_outfeaturename.contains("$8")
					|| feature_outfeaturename.contains("$9")) {
				int mm = feature_outfeaturename.indexOf("$");
				feature_outfeaturenamelast = feature_outfeaturename.substring(0, mm);
			} else {
				feature_outfeaturenamelast = feature_outfeaturename;
			}

		}
		return feature_outfeaturenamelast;
	}



	
	public static String getonlyclassname(String ss) {
		// 输入的waitjudge是类名

		if (ss.contains("$0") || ss.contains("$1") || ss.contains("$2") || ss.contains("$3") || ss.contains("$4") || ss.contains("$5") || ss.contains("$6") || ss.contains("$7") || ss.contains("$8") || ss.contains("$9")) {
			int a = ss.indexOf("$");
			ss = ss.substring(0, a);
		}
		return ss;

	}
	
	
	public static String methodonlyname(String filename) {// 带

		if (filename.contains(" ")) {
			filename = filename.replaceAll(" ", "");
		}
		String name = null;
		String str3[] = {};
		String str4[] = new String[2];
		if (filename.contains("(")) {
			str3 = filename.split("\\(");
			name = str3[0];

		}

		if (filename.contains("{")) {
			str3 = filename.split("\\{");
			name = str3[0];

		}
		if (!filename.contains("{") && !filename.contains("(")) {

			name = filename;
		}

		return name;
	}
	
	
	public static String ShortTablemethodonlyname(String filename) {
		//System.out.println("函数==="+filename);
		String classname = SourceParser.Getonlyclassname(filename);
		String str4[] = new String[2];
		str4 = filename.split(classname);
		String methodfirst = str4[1].substring(1, str4[1].length());
		//System.out.println("methodfirst==="+methodfirst);
		if(str4[1].contains("(")){
			String paraString = null;
			int id1 = methodfirst.indexOf("(");
			int id2 = methodfirst.length()-1;
		//	System.out.println("id1==="+id1+"  id2==="+id2);
			if((id2-id1)==1){
			paraString = "()";
		//	System.out.println("1111methodfirst==="+methodfirst);
			}else{
				String str5[] = {};
				paraString = methodfirst.substring(id1, id2);
				str5 = paraString.split("\\,");
				for(int i = 0 ; i < str5.length; i++){
					String str6[] = {};
					str6 = str5[i].split("\\.");
					str5[i] = str6[str6.length-1];
				}
				paraString = str5[0];
				for(int i = 1 ; i < str5.length; i++){
					paraString = paraString+","+str5[i];
				}
				paraString ="("+ paraString+")";
			//	System.out.println("paraString==="+paraString);
			}
			filename = methodfirst.substring(0, id1) + paraString;
			//System.out.println("filename==="+filename);
		}
	   if(!str4[1].contains("(")){
		   filename = methodfirst;
		 //  System.out.println("!!!!!!!!filename==="+filename);
		}
		return filename;// 带
		
	}
	
	
	
	public static String Shortmethodonlyname(String filename) {// 带

		if (filename.contains(" ")) {
			filename = filename.replaceAll(" ", "");
		}
		String name = null;
		String str3[] = {};
		String str4[] = new String[2];
		if (filename.contains("(")) {
			str3 = filename.split("\\(");
			name = str3[0];

		}

		if (filename.contains("{")) {
			str3 = filename.split("\\{");
			name = str3[0];

		}
		if (!filename.contains("{") && !filename.contains("(")) {

			name = filename;
		}
		String str5[] = {};
		if (name.contains(".")) {
			str5 = name.split("\\.");
		}
		name = str5[str5.length-1];
		return name;
	}
	
	
	/*
	 * 输出log
	 */
	public static void printlogsSpecial(Map <String, ClassObject> classesMap1)
			throws SAXException, IOException, MWException {
		
		 Set<String> keys = classesMap1.keySet();  
	        for(String key : keys) {   
	        	if(key.equals("com.jrefinery.chart.ChartUtilities")){
	            ClassObject clas = classesMap1.get(key);
//	            for (int ce = 0; ce < clas.outboundClassList.size(); ce++) {
//					System.out.println("outboundClassList" + ce + " = "+ clas.outboundClassList.get(ce) + "\n");
//				}
//				for (int y = 0; y < clas.inboundClassList.size(); y++) {
//					System.out.println("inboundClassList" + y + " = "+ clas.inboundClassList.get(y) + "\n");
//				}
//				 for(int e =0;e<clas.outboundFeatureList.size();e++){
//				 System.out.println("outboundFeatureList" +
//				 e+" = "+clas.outboundFeatureList.get(e)+"\n");
//				 }
//				 for(int x =0;x<clas.inboundFeatureList.size();x++){
//				 System.out.println("inboundFeatureList" +
//				 x+" = "+clas.inboundFeatureList.get(x)+"\n");
//				 }
				 Set<String> keys1 = clas.featureMap.keySet();
				 for(String key1 : keys1) {
//					 System.out.println("Featurename = " + key1 + "\n");  
//					 for(int r=0;r<clas.featureMap.get(key1).getOutboundClassList().size();r++)
//					{
//						   
//							 System.out.println("feature  "  + "outboundClassList" + r+" = "+clas.featureMap.get(key1).getOutboundClassList().get(r)+"\n");
//						  
//					}
	
							 for(int q=0;q<clas.featureMap.get(key1).outboundFeatureList.size();q++)
					{
								 if(SourceParser.Getonlyclassname(clas.featureMap.get(key1).getOutboundFeatureList().get(q)).equals("com.jrefinery.chart.ChartUtilities")){
							 System.out.println(key1+ "\t"+clas.featureMap.get(key1).getOutboundFeatureList().get(q)+"\n");
								 }
				    }
							 
//							 for(int q=0;q<clas.featureMap.get(key1).outboundMethodList.size();q++)
//					{
//							 System.out.println("feature  " + "outboundMethodList" + q+" = "+clas.featureMap.get(key1).getOutboundMethodList().get(q)+"\n");
//				    }
							 
//							 for(int q=0;q<clas.featureMap.get(key1).outboundAttributeList.size();q++)
//					{
//							 System.out.println("feature  " + "outboundAttibuteList" + q+" = "+clas.featureMap.get(key1).getOutboundAttributeList().get(q)+"\n");
//				    }
							 
//					for(int r=0;r<clas.featureMap.get(key1).inboundClassList.size();r++)
//					{ 
//							System.out.println("feature  "  + "inboundClassList" + r+" = "+clas.featureMap.get(key1).inboundClassList.get(r)+"\n");
//					
//					}
//					 for(int q=0;q<clas.featureMap.get(key1).inboundFeatureList.size();q++)
//					{
//						 if(SourceParser.Getonlyclassname(clas.featureMap.get(key1).inboundFeatureList.get(q)).equals("com.jrefinery.chart.ChartUtilities")){
//					 System.out.println(clas.featureMap.get(key1).inboundFeatureList.get(q)+ "\t"+clas.featureMap.get(key1).getOutboundFeatureList().get(q)+"\n");
//						 }
////							System.out.println("feature  " + "inboundFeatureList" + q+" = "+clas.featureMap.get(key1).inboundFeatureList.get(q)+"\n");
//					}	
					 
//					 for(int q=0;q<clas.featureMap.get(key1).inboundMethodList.size();q++)
//		        	{
//					 System.out.println("feature  " + "inboundMethodList" + q+" = "+clas.featureMap.get(key1).getInboundMethodList().get(q)+"\n");
//	        	    }
//					 
//					 for(int q=0;q<clas.featureMap.get(key1).inboundAttributeList.size();q++)
//			        {
//					 System.out.println("feature  " + "inboundAttibuteList" + q+" = "+clas.featureMap.get(key1).getInboundAttributeList().get(q)+"\n");
//		            }
							 
				 }
	        }
	        }  

	
	        }  
	
	
	
	
	/*
	 * 输出log
	 */
	public static void printlogs1(Map <String, ClassObject> classesMap1)
			throws IOException{
		
		 Set<String> keys = classesMap1.keySet();  
	        for(String key : keys) {  
	        	
	      //  	if(key.equals("org.jhotdraw.draw.CompositeFigure")){
	            System.out.println("Classname = " + key);  
	            ClassObject clas = classesMap1.get(key);
	            for (int ce = 0; ce < clas.outboundClassList.size(); ce++) {
	            	//if(clas.outboundClassList.get(ce).contains("$")){
					System.out.println("outboundClassList" + ce + " = "
							+ clas.outboundClassList.get(ce) + "\n");
	            	//}
				}
				for (int y = 0; y < clas.inboundClassList.size(); y++) {
				//	if(clas.inboundClassList.get(y).contains("$")){
					System.out.println("inboundClassList" + y + " = "
							+ clas.inboundClassList.get(y) + "\n");
				//	}
				}
				 for(int e =0;e<clas.outboundFeatureList.size();e++){
				 System.out.println("outboundFeatureList" +
				 e+" = "+clas.outboundFeatureList.get(e)+"\n");
				 }
				 for(int x =0;x<clas.inboundFeatureList.size();x++){
				 System.out.println("inboundFeatureList" +
				 x+" = "+clas.inboundFeatureList.get(x)+"\n");
				 }
				 Set<String> keys1 = clas.featureMap.keySet();
				// System.out.println("keys1=="+keys1.size());
				 for(String key1 : keys1) {
					
//				if(key1.equals("org.jhotdraw.draw.TriangleRotationHandler.getLocation()")||SourceParser.Getonlyclassname(key1).equals("org.jhotdraw.draw.TriangleFigure")){
					 System.out.println("Featurename = " + key1 + "\n");  
					 for(int r=0;r<clas.featureMap.get(key1).getOutboundClassList().size();r++)
					{
						   //  if(clas.featureMap.get(key1).getOutboundClassList().get(r).contains("$")){
							 System.out.println("feature  "  + "outboundClassList" + r+" = "+clas.featureMap.get(key1).getOutboundClassList().get(r)+"\n");
						  //   }
					}
	
							 for(int q=0;q<clas.featureMap.get(key1).outboundFeatureList.size();q++)
					{
							 System.out.println("feature  " + "outboundFeatureList" + q+" = "+clas.featureMap.get(key1).getOutboundFeatureList().get(q)+"\n");
				    }
							 
							 for(int q=0;q<clas.featureMap.get(key1).outboundMethodList.size();q++)
					{
							 System.out.println("feature  " + "outboundMethodList" + q+" = "+clas.featureMap.get(key1).getOutboundMethodList().get(q)+"\n");
				    }
							 
							 for(int q=0;q<clas.featureMap.get(key1).outboundAttributeList.size();q++)
					{
							 System.out.println("feature  " + "outboundAttibuteList" + q+" = "+clas.featureMap.get(key1).getOutboundAttributeList().get(q)+"\n");
				    }
							 
//					for(int r=0;r<clas.featureMap.get(key1).inboundClassList.size();r++)
//					{  //if(clas.featureMap.get(key1).inboundClassList.get(r).contains("$")){
//							System.out.println("feature  "  + "inboundClassList" + r+" = "+clas.featureMap.get(key1).inboundClassList.get(r)+"\n");
//					//}
//					}
//					 for(int q=0;q<clas.featureMap.get(key1).inboundFeatureList.size();q++)
//					{
//							System.out.println("feature  " + "inboundFeatureList" + q+" = "+clas.featureMap.get(key1).inboundFeatureList.get(q)+"\n");
//					}	
//					 
//					 for(int q=0;q<clas.featureMap.get(key1).inboundMethodList.size();q++)
//		        	{
//					 System.out.println("feature  " + "inboundMethodList" + q+" = "+clas.featureMap.get(key1).getInboundMethodList().get(q)+"\n");
//	        	    }
//					 
//					 for(int q=0;q<clas.featureMap.get(key1).inboundAttributeList.size();q++)
//			        {
//					 System.out.println("feature  " + "inboundAttibuteList" + q+" = "+clas.featureMap.get(key1).getInboundAttributeList().get(q)+"\n");
//		            }
							 
//				 }
				 }
	      //  } 
	        }  

	
	        }  

	


}
