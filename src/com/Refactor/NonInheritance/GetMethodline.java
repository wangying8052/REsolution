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
import java.io.IOException;
import java.util.ArrayList;

import com.Refactor.classparser.Feature;

public class GetMethodline {

	private static boolean ContainPara(String Name) {
		// TODO Auto-generated method stub
		boolean mark = false;
		char[] MethodName = Name.toCharArray();
		int RightIndex = 0;
		int LeftIndex = 0;
		for (int i = MethodName.length - 1; i > 0; i--) {
			if (RightIndex != 0 && LeftIndex != 0)
				break;
			if (MethodName[i] == ')')
				RightIndex = i;
			if (MethodName[i] == '(')
				LeftIndex = i;
		}
		for (int i = LeftIndex; i < RightIndex; i++) {
			if ((MethodName[i] >= 'A' && MethodName[i] <= 'Z')
					|| (MethodName[i] >= 'a' && MethodName[i] <= 'z')) {
				mark = true;
				break;
			}
		}
		return mark;
	}

	/*
	 * 通过函数名获取标准形式的参数信息
	 */
	private static String getParaName(String name) {
		// TODO Auto-generated method stub
		String methodName = null;
		char[] text = name.toCharArray();
		int start = 0;
		for (int i = 0; i < text.length; i++)
			if ((text[i] >= 'A' && text[i] <= 'Z')
					|| (text[i] >= 'a' && text[i] <= 'z')) {
				start = i;
				break;
			}
		String s = name.substring(start, name.length());
		name = s;
		int u = getCalssName(name).length();
		String Str = name.substring(u + 1, name.length());
		int kuohaoleft = Str.indexOf("(");
		char[] one = Str.toCharArray();
		String text1 = new String();
		for (int j = kuohaoleft; j >= 0; j--) {
			if (one[j] == '.') {
				text1 = Str.substring(j + 1, kuohaoleft);
				break;
			}
		}
		int mark = kuohaoleft + 1;
		String str = Str.substring(mark, Str.indexOf(")") + 1);
		int t = 0;
		while (str.indexOf(",") != -1 && str.length() != 0) {
			if (t > 0)
				text1 += ",";
			if (t == 0)
				mark = 0;
			t++;
			int a = str.indexOf(",");
			if (a == -1)
				a = str.length();
			char[] single = str.substring(mark, a).toCharArray();
			int aa = -1;
			for (int k = single.length - 1; k > 0; k--) {
				if (single[k] == '.') {
					aa = k;
					break;
				}
			}
			if (aa == -1)// 不需去前缀
			{
				if (!str.substring(mark, mark + 1).equals(" ")
						&& text1.length() != 0) {
					text1 += " ";
					text1 += str.substring(mark, a);
				} else
					text1 += str.substring(mark, a);
			} else {
				if (text1.length() != 0)
					text1 += " ";
				text1 += str.substring(aa + 1, a);
			}
			mark = a;
			String str1 = str.substring(mark + 1, str.indexOf(")") + 1);
			str = str1;
			mark = 0;
		}
		if (str.length() > 0) {
			if (text1.length() - text1.indexOf("(") > 1) {
				if (str.indexOf(",") != -1)
					text1 += ",";
				else
					text1 += ",";
			}
			char[] single2 = str.toCharArray();
			int bb = -1;
			for (int k = single2.length - 1; k > 0; k--) {
				if (single2[k] == '.') {
					bb = k;
					break;
				}
			}
			if (bb == -1)// 不需去前缀
			{
				text1 += str.substring(0, str.length() - 1);
			} else {
				text1 += str.substring(bb + 1, str.length() - 1);
			}
		}
		methodName = text1;
		String[] st = methodName.split(",");
		for (int i = 0; i < st.length; i++) {
			if (st[i].contains("$")) {
				int index = st[i].indexOf("$");
				st[i] = st[i].substring(index + 1, st[i].length());
			}
		}
		String a = st[0];
		for (int i = 1; i < st.length; i++) {
			if (!st[i].substring(0, 1).equals(" ")) {
				a += ", " + st[i];
			} else
				a += "," + st[i];
		}
		methodName = a;
		if (methodName.contains("$")) {
			int index = methodName.indexOf("$");
			methodName = methodName.substring(index + 1, methodName.length());
		}
		return methodName;
	}

	public static ArrayList<String> getJavaText(String filename)throws IOException {
		ArrayList<String> classname = new ArrayList();
		File file = new File(filename);
		FileReader fr = new FileReader(file.getPath());
		BufferedReader br = new BufferedReader(fr);
		String ss = null;
		while ((ss = br.readLine()) != null) {
			classname.add(ss);
		}
		fr.close();
		return classname;
	}

	/*
	 * 通过方法名获取类名
	 */
	private static String getCalssName(String name) {
		// TODO Auto-generated method stub
		String calssName = null;
		char[] nameChar = name.toCharArray();
		for (int i = nameChar.length - 1; i >= 0; i--) {
			if (nameChar[i] == '(') {
				for (int j = i; j > 0; j--) {
					if (nameChar[j] == '.') {
						calssName = name.substring(0, j);
						break;
					}
				}
				break;
			}
		}
		return calssName;
	}

	private static boolean End_Word_isPoint(String text) {
		// TODO Auto-generated method stub
		if (text.contains(";")) {
			if (text.contains("{") && text.contains("}"))
				return false;
			else
				return true;
		} else
			return false;
	}

	/*
	 * OnlyParaName:从传参的函数名中获取的参数类型； Paras:读取文件时获取的函数参数类型 方法名匹配且参数类型匹配，则找到文件中的函数
	 */
	static Feature getMethodLine(String MethodName, String sourcePath,Feature value) throws IOException {
		int StartLine = 0, EndLine = 0;
		String[] className = getCalssName(MethodName).split("\\.");
		for (int t = 0; t < className.length; t++) {
			sourcePath += "\\";
			sourcePath += className[t];
		}
		sourcePath += ".java";
		ArrayList<String> ClassInLine = getJavaText(sourcePath);
		int u = getCalssName(MethodName).length();
		String Str = MethodName.substring(u + 1, MethodName.length());
		int kuohaoleft = Str.indexOf("(");
		String OnlyMethodName = Str.substring(0, kuohaoleft);
		String OnlyParaName = getParaName(MethodName);
		boolean isInterface = false;
		int flag2 = 0;
		for (int t = 0; t < ClassInLine.size(); t++) {
			int mark = 1, search_line_error = 1;
			if (FirstWord_isNotes(ClassInLine.get(t)) == true)
				continue;
			String Name = getCalssName(MethodName);
			int h = Name.length();
			for (; h > 0; h--) {
				if (Name.substring(h - 1, h).equals("."))
					break;
			}
			String OnlyClassName = Name.substring(h, Name.length());
			if ((ClassInLine.get(t).contains("interface")&& ClassInLine.get(t).contains(OnlyClassName) || isInterface == true)|| (ClassInLine.get(t).contains("abstract") && ClassInLine.get(t).contains(OnlyClassName))|| (ClassInLine.get(t).contains("final")&& ClassInLine.get(t).contains(OnlyClassName) || isInterface == true)) 
			{// 是接口或抽象类或final
				isInterface = true;
				if (ClassInLine.get(t).contains(OnlyMethodName)) 
				{// 接口形式1：public+方法名;
					StartLine = t + 1;
					if (ClassInLine.get(t).contains(";") && flag2 == 0) {
						flag2 = 1;
						EndLine = StartLine;
						break;
					}
				}
			}
			if (flag2 == 0) {// 是接口的一般形式或不是接口
				if (ContainPara(OnlyMethodName + "(" + OnlyParaName + ")") == true) {// 分析的是带参数的函数
					if (ClassInLine.get(t).contains(OnlyMethodName + "(")|| ClassInLine.get(t).contains(OnlyMethodName + " (")) {
						if (ClassInLine.get(t).contains("final ")) {
							int y = ClassInLine.get(t).indexOf("final ");
							String str1 = ClassInLine.get(t).substring(0, y);
							String str2 = ClassInLine.get(t).substring(y + 6,ClassInLine.get(t).length());
							ClassInLine.set(t, str1 + str2);
						}
						if (ClassInLine.get(t).contains("static")|| ClassInLine.get(t).contains("abstract")|| ClassInLine.get(t).contains("public")|| ClassInLine.get(t).contains("protected")|| ClassInLine.get(t).contains("private")|| ClassInLine.get(t).contains("void")|| isInterface == true) {
							String ParaName = null;
							if (End_Word_isPoint(ClassInLine.get(t)) == false) {
								String Para = null;
								int left = ClassInLine.get(t).indexOf("(");
								int right = ClassInLine.get(t).indexOf(")");
								if (left + 1 > right && right != -1)
									continue;
								if (left != -1 && right != -1)
									Para = ClassInLine.get(t).substring(left + 1, right);
								if (Para == null) {// 未取到参数信息
									int j = t;
									for (int p = 0; j < ClassInLine.size(); j++) {
										int index = ClassInLine.get(j).indexOf("{");
										int index2 = ClassInLine.get(j).indexOf(")");
										if (index2 != -1)
											p++;
										if (index != -1 && p == 0) {
											mark = 0;
											break;
										}
										if (!ClassInLine.get(j).contains("(")&& p == 1 && j - t >= 1) 
										{// 搜到函数名信息且函数参数行大于1
											// 首行无参数:将参数信息改为一行
											String TwoLine = "";
											if (ContainPara(ClassInLine.get(t)+ ")")) {
												char[] c = ClassInLine.get(t).toCharArray();
												int i = c.length - 1;
												for (; i > 0;) {
													if (c[i] == '\t'|| c[i] == ' ')
														i--;
													else
														break;
												}
												String s = "";
												for (int jj = 0; jj <= i; jj++)
													s += c[jj];
												TwoLine = s;
												ClassInLine.set(t, s);
											}
											if (ClassInLine.get(j).contains("{"))
												index = -1;
											else if (ClassInLine.get(j + 1).contains("{"))
												index = -2;
											char[] ch = ClassInLine.get(t).toCharArray();
											int Nospace = -1;
											for (int m = t + 1; m <= j; m++) {
												ch = ClassInLine.get(m).toCharArray();
												Nospace = -1;
												for (int i = ClassInLine.get(m)
														.length() - 1; i > 0; i--) {
													if (ch[i] != ' ') {
														Nospace = i;
														break;
													}
												}
												if (Nospace != -1)
													TwoLine += ClassInLine.get(m).substring(0,Nospace + 1);
												ClassInLine.set(m, " ");
											}
											String[] str = TwoLine.split(",");
											ch = ClassInLine.get(t).toCharArray();
											int pp = -1;
											String a = "";
											for (int q = left + 1; q < ch.length; q++) {
												if ((ch[q] >= 'A' && ch[q] <= 'Z')|| (ch[q] >= 'a' && ch[q] <= 'z')) {
													pp = q;
													break;
												}
											}
											if (pp != -1&& !ContainPara(ClassInLine.get(t)+ ")"))
												a = ClassInLine.get(t).substring(0, pp);
											else
												a = ClassInLine.get(t);
											index2 = str[0].indexOf("(");
											int jj = 0;
											ch = a.toCharArray();
											int index0 = a.indexOf(",");
											if (index0 != -1) {
												a = delNote(a);
												String[] par = a.split(",");
												int num = 0;
												for (int i = index0 + 1; i < a.length(); i++) {
													if (ch[i] == ',')
														num++;
												}
												if (num + 1 != par.length&& par.length != 1)
													jj++;
											}
											if (a.contains(",")) {
												int ii = 0;
												for (int i = 0; i < ch.length; i++) {
													if (ch[i] == '/')
														break;
													if (ch[i] == ',') {
														jj++;
														ii = i;
													}
												}
												if (ii != 0) {
													for (int w = ii + 1; w < ch.length; w++) {
														if (!(ch[w] == ' '|| ch[w] == '\t' || ch[w] == '/')) {
															String ss = "";
															for (int ww = 0; ww < w; ww++) {
																if (!(ch[ww] == ',' && ww == w - 1))
																	ss += ch[ww];
															}
															a = ss;
															continue;
														}
													}
												}
											} else if (index2 != -1&& index2 != 0 && jj > 0) {
												if (str[0].substring(index2 - 1, index2) != " "&& str[0].substring(index2 - 1,index2) != "\t") {
													jj--;
												}
											} else if (index2 != -1&& index2 != 0) {
												if (str[0].substring(index2 - 1, index2) != " "&& str[0].substring(index2 - 1,index2) != "\t") {
													a = "";
												}
											}
											for (; jj < str.length; jj++) {
												ch = str[jj].toCharArray();
												Nospace = -1;
												for (int i = 0; i < str[jj].length(); i++) {
													if ((ch[i] >= 'A' && ch[i] <= 'Z')|| (ch[i] >= 'a' && ch[i] <= 'z')) {
														int qq = -1;
														for (int q = str[jj].length() - 1; q > i; q--) {
															if ((ch[q] >= 'A' && ch[q] <= 'Z')|| (ch[q] >= 'a' && ch[q] <= 'z')) {
																qq = q;
																break;
															}
														}
														if (i > qq) {
															search_line_error = 0;
															break;
														}
														if (qq != -1 && jj == 0)
															a += str[jj].substring(i,qq + 1);
														else {
															a += ","+ str[jj].substring(i,qq + 1);
														}
														break;
													}
												}
												if (search_line_error == 0)
													break;
											}
											if (search_line_error == 0)
												break;
											if (index == -1) {
												if (a.contains(")"))
													ClassInLine.set(t, a + "{");
												else
													ClassInLine.set(t, a + "){");
												ch = ClassInLine.get(j).toCharArray();
												for (int q = 0; q < ch.length; q++) {
													if (ch[q] == '{') {
														ch[q] = ' ';
														break;
													}
													if (ch[q] != '\t'&& ch[q] != ' ') {
														break;
													}
												}
												String aa = "";
												for (int q = 0; q < ch.length; q++) {
													aa += ch[q];
												}
												ClassInLine.set(j, aa);
											} else if (index == -2) {
												if (ClassInLine.get(j + 1).contains("throws")) {
													ClassInLine.set(t, a + "){");
													ch = ClassInLine.get(j + 1).toCharArray();
													for (int i = 0; i < ch.length; i++) {
														if (ch[i] == '{') {
															String ss = ClassInLine.get(j + 1).substring(i + 1,ch.length);
															ClassInLine.set(j + 1, ss);
														}
													}
												} else {
													if (a.contains(")"))
														ClassInLine.set(t, a+ "{");
													else
														ClassInLine.set(t, a+ "){");
													ch = ClassInLine.get(j + 1).toCharArray();
													for (int q = 0; q < ch.length; q++) {
														if (ch[q] == '{') {
															ch[q] = ' ';
															break;
														}
														if (ch[q] != '\t'&& ch[q] != ' ') {
															break;
														}
													}
													String aa = "";
													for (int q = 0; q < ch.length; q++) {
														aa += ch[q];
													}
													ClassInLine.set(j + 1, aa);
												}
											} else
												ClassInLine.set(t, a);
											break;
										}
									}
									if (search_line_error == 0) {
										t = j + 1;
										continue;
									}
									if (mark == 0)
										continue;
									if (ClassInLine.get(t).contains(")")&& ClassInLine.get(t).contains("{")) 
									{// 文本首行格式2：方法名(-,-){
										left = ClassInLine.get(t).indexOf("(");
										right = ClassInLine.get(t).indexOf(")");
										Para = ClassInLine.get(t).substring(left + 1, right);
										if (ContainPara(Para) == true)
											ParaName = adjust_ParaName(Para);
										else
											ParaName = "";
									}
								} else {// 取到参数信息
									if (ClassInLine.get(t).contains(")")&& ClassInLine.get(t).contains("main")) 
									{// 文本首行格式1：方法名(-,-)
										ParaName = "String[]";
									} else if (ClassInLine.get(t).contains(")")&& !ClassInLine.get(t).contains("{")) 
									{// 文本首行格式1：方法名(-,-)
										if (ContainPara(Para) == true)
											ParaName = adjust_ParaName(Para);
										else
											ParaName = "";
										ParaName = adjust_ParaName(Para);
									} else if (ClassInLine.get(t).contains(")")&& ClassInLine.get(t).contains("{")) 
									{// 文本首行格式2：方法名(-,-){
										Para = ClassInLine.get(t).substring(left + 1, right);
										if (ContainPara(Para) == true)
											ParaName = adjust_ParaName(Para);
										else
											ParaName = "";
									} else if (ClassInLine.get(t + 1).contains("throws")|| ClassInLine.get(t + 1).contains("{")) 
									{// 文本首行格式5：方法名(-,-,-)
									 // throws || {
										Para = ClassInLine.get(t).substring(left + 1, right);
										if (ContainPara("(" + Para + ")") == true)
											ParaName = adjust_ParaName(Para);
										else
											ParaName = "";
										ParaName = adjust_ParaName(Para);
									}
								}
								// OnlyParaName = adjust_ParaName(OnlyParaName);
								if (ParaName != null && ParaName.contains("."))
									ParaName = delPrefix(ParaName);
								if (OnlyParaName.equals(ParaName)) {
									StartLine = t + 1;
									int LeftBracketNum = 0;
									int RightBracket = 0;
									int start = t;
									for (int m = t; m < ClassInLine.size(); m++) {
										String a = delNote(ClassInLine.get(m));
										ClassInLine.set(m, a);
										LeftBracketNum += leftBracket(a);
										RightBracket += rightBracket(a);
										if (LeftBracketNum == RightBracket&& LeftBracketNum != 0) {
											t = m;
											break;
										}
									}
									EndLine = StartLine + t - start;
									break;
								}
							}
						}
					} else {
						if (ClassInLine.get(t).contains(OnlyMethodName)) 
						{// 文本首行格式：方法名
							if (ClassInLine.get(t + 1).contains("(")) {
								if (ClassInLine.get(t).contains("static")|| ClassInLine.get(t).contains("abstract")|| ClassInLine.get(t).contains("public")|| ClassInLine.get(t).contains("protected")|| ClassInLine.get(t).contains("private")|| ClassInLine.get(t).contains("void")|| isInterface == true) {
									if (!ClassInLine.get(++t).contains(";")) {
										String Para = null;
										int left = ClassInLine.get(t).indexOf("(");
										int right = ClassInLine.get(t).indexOf(")");
										if (left + 1 > right && right != -1)
											continue;
										if (left != -1 && right != -1)
											Para = ClassInLine.get(t).substring(left + 1, right);
										String Paras = null;
										if (Para != null) {
											Paras = adjust_ParaName(Para);
										}
										if (Paras != null&& Paras.contains("."))
											Paras = delPrefix(Paras);
										if (OnlyParaName.equals(Paras)) {
											StartLine = t;
											int LeftBracketNum = 0;
											if (ClassInLine.get(t).contains("{"))
												LeftBracketNum = 1;
											int RightBracket = 0;
											int start = t;
											for (int m = t + 1; m < ClassInLine.size(); m++) {
												if (ClassInLine.get(m).contains("{"))
													LeftBracketNum++;
												if (ClassInLine.get(m).contains("}"))
													RightBracket++;
												if (LeftBracketNum == RightBracket&& LeftBracketNum != 0) {
													t = m + 1;
													break;
												}
											}
											EndLine = StartLine + t - start;
											break;
										}
									}

								}
							}
						}
					}
				} else {// 分析的是不带参数的函数
					if (ClassInLine.get(t).contains("static")|| ClassInLine.get(t).contains("abstract")|| ClassInLine.get(t).contains("public")|| ClassInLine.get(t).contains("protected")|| ClassInLine.get(t).contains("private")|| ClassInLine.get(t).contains("void")|| isInterface == true) {
						StartLine = t + 1;
						int LeftBracketNum = 0;
						if (ClassInLine.get(t).contains("{"))
							LeftBracketNum = 1;
						int RightBracket = 0;
						int start = t;
						for (int m = t + 1; m < ClassInLine.size(); m++) {
							if (ClassInLine.get(m).contains("{"))
								LeftBracketNum++;
							if (ClassInLine.get(m).contains("}"))
								RightBracket++;
							if (LeftBracketNum == RightBracket&& LeftBracketNum != 0) {
								t = m;
								break;
							}
						}
						EndLine = StartLine + t - start;
					} else if (ClassInLine.get(t).contains(OnlyMethodName + "()")) {
						if (End_Word_isPoint(ClassInLine.get(t)) == false) {
							StartLine = t + 1;
							int LeftBracketNum = 0;
							if (ClassInLine.get(t).contains("{"))
								LeftBracketNum = 1;
							int RightBracket = 0;
							int start = t;
							for (int m = t + 1; m < ClassInLine.size(); m++) {
								if (ClassInLine.get(m).contains("{"))
									LeftBracketNum++;
								if (ClassInLine.get(m).contains("}"))
									RightBracket++;
								if (LeftBracketNum == RightBracket&& LeftBracketNum != 0) {
									t = m;
									break;
								}
							}
							EndLine = StartLine + t - start;
						} else if (isInterface == true
								&& ClassInLine.get(t).contains(";")) {// 接口形式2：不带特殊关键字+方法名();
							StartLine = t + 1;
							EndLine = t + 1;
						}

					}
				}
			}
		}
		for (int j = StartLine - 2; j > 0; j--) {
			if (FirstWord_isNotes(ClassInLine.get(j)) == true) {
				StartLine--;
			} else
				break;
		}
		if (EndLine == 0 && StartLine == 0 && MethodName.contains("(")) 
		{// 未找到函数：或为不带public等关键字
			u = getCalssName(MethodName).length();
			Str = MethodName.substring(u + 1, MethodName.length());
			kuohaoleft = Str.indexOf("(");
			OnlyMethodName = Str.substring(0, kuohaoleft);
			OnlyParaName = getParaName(MethodName);
			isInterface = false;
			flag2 = 0;
			for (int t = 0; t < ClassInLine.size(); t++) {
				int mark = 1, search_line_error = 1;
				if (FirstWord_isNotes(ClassInLine.get(t)) == true)
					continue;
				String Name = getCalssName(MethodName);
				int h = Name.length();
				for (; h > 0; h--) {
					if (Name.substring(h - 1, h).equals("."))
						break;
				}
				String OnlyClassName = Name.substring(h, Name.length());
				if ((ClassInLine.get(t).contains("interface")&& ClassInLine.get(t).contains(OnlyClassName) || isInterface == true)|| (ClassInLine.get(t).contains("abstract")&& ClassInLine.get(t).contains(OnlyClassName) || isInterface == true)) 
				{// 是接口或抽象类
					isInterface = true;
					if (ClassInLine.get(t).contains(OnlyMethodName)) 
					{// 接口形式1：public+方法名;
						StartLine = t + 1;
						if (ClassInLine.get(t).contains(";") && flag2 == 0) {
							flag2 = 1;
							EndLine = StartLine;
							break;
						}
					}
				}
				if (flag2 == 0) 
				{// 是接口的一般形式或不是接口
					if (ContainPara(OnlyMethodName + "(" + OnlyParaName + ")") == true) 
					{// 分析的是带参数的函数
						if (ClassInLine.get(t).contains(OnlyMethodName + "(")|| ClassInLine.get(t).contains(OnlyMethodName + " (")) {
							if (ClassInLine.get(t).contains("final ")) {
								int y = ClassInLine.get(t).indexOf("final ");
								String str1 = ClassInLine.get(t).substring(0, y);
								String str2 = ClassInLine.get(t).substring(y + 6, ClassInLine.get(t).length());
								ClassInLine.set(t, str1 + str2);
							}
							if (ClassInLine.get(t).contains("=")|| ClassInLine.get(t).contains("+")|| ClassInLine.get(t).contains("-"))
								continue;
							String ParaName = null;
							if (End_Word_isPoint(ClassInLine.get(t)) == false) {
								String Para = null;
								int left = ClassInLine.get(t).indexOf("(");
								int right = ClassInLine.get(t).indexOf(")");
								if (left + 1 > right && right != -1)
									continue;
								if (left != -1 && right != -1)
									Para = ClassInLine.get(t).substring(left + 1, right);
								if (Para == null) 
								{// 未取到参数信息
									int j = t;
									for (int p = 0; j < ClassInLine.size(); j++) {
										int index = ClassInLine.get(j).indexOf("{");
										int index2 = ClassInLine.get(j).indexOf(")");
										if (index2 != -1)
											p++;
										if (index != -1 && p == 0) {
											mark = 0;
											break;
										}
										if (!ClassInLine.get(j).contains("(")&& p == 1 && j - t >= 1) 
										{// 搜到函数名信息且函数参数行大于1
										// 首行无参数:将参数信息改为一行
											String TwoLine = "";
											if (ContainPara(ClassInLine.get(t)+ ")")) {
												char[] c = ClassInLine.get(t).toCharArray();
												int i = c.length - 1;
												for (; i > 0;) {
													if (c[i] == '\t'|| c[i] == ' ')
														i--;
													else
														break;
												}
												String s = "";
												for (int jj = 0; jj <= i; jj++)
													s += c[jj];
												TwoLine = s;
												ClassInLine.set(t, s);
											}
											if (ClassInLine.get(j).contains("{"))
												index = -1;
											else if (ClassInLine.get(j + 1).contains("{"))
												index = -2;
											char[] ch = ClassInLine.get(t).toCharArray();
											int Nospace = -1;
											for (int m = t + 1; m <= j; m++) {
												ch = ClassInLine.get(m).toCharArray();
												Nospace = -1;
												for (int i = ClassInLine.get(m).length() - 1; i > 0; i--) {
													if (ch[i] != ' ') {
														Nospace = i;
														break;
													}
												}
												if (Nospace != -1)
													TwoLine += ClassInLine.get(m).substring(0,Nospace + 1);
												ClassInLine.set(m, " ");
											}
											String[] str = TwoLine.split(",");
											ch = ClassInLine.get(t).toCharArray();
											int pp = -1;
											String a = "";
											for (int q = left + 1; q < ch.length; q++) {
												if ((ch[q] >= 'A' && ch[q] <= 'Z')|| (ch[q] >= 'a' && ch[q] <= 'z')) {
													pp = q;
													break;
												}
											}
											if (pp != -1&& !ContainPara(ClassInLine.get(t)+ ")"))
												a = ClassInLine.get(t).substring(0, pp);
											else
												a = ClassInLine.get(t);
											index2 = str[0].indexOf("(");
											int jj = 0;
											ch = a.toCharArray();
											int index0 = a.indexOf(",");
											if (index0 != -1) {
												a = delNote(a);
												String[] par = a.split(",");
												int num = 0;
												for (int i = index0 + 1; i < a.length(); i++) {
													if (ch[i] == ',')
														num++;
												}
												if (num + 1 != par.length&& par.length != 1)
													jj++;
											}
											if (a.contains(",")) {
												int ii = 0;
												for (int i = 0; i < ch.length; i++) {
													if (ch[i] == '/')
														break;
													if (ch[i] == ',') {
														jj++;
														ii = i;
													}
												}
												if (ii != 0) {
													for (int w = ii + 1; w < ch.length; w++) {
														if (!(ch[w] == ' '|| ch[w] == '\t' || ch[w] == '/')) {
															String ss = "";
															for (int ww = 0; ww < w; ww++) {
																if (!(ch[ww] == ',' && ww == w - 1))
																	ss += ch[ww];
															}
															a = ss;
															continue;
														}
													}
												}
											} else if (index2 != -1&& index2 != 0 && jj > 0) {
												if (str[0].substring(index2 - 1, index2) != " "&& str[0].substring(index2 - 1,index2) != "\t") {
													jj--;
												}
											} else if (index2 != -1&& index2 != 0) {
												if (str[0].substring(index2 - 1, index2) != " "&& str[0].substring(index2 - 1,index2) != "\t") {
													a = "";
												}
											}
											for (; jj < str.length; jj++) {
												ch = str[jj].toCharArray();
												Nospace = -1;
												for (int i = 0; i < str[jj].length(); i++) {
													if ((ch[i] >= 'A' && ch[i] <= 'Z')|| (ch[i] >= 'a' && ch[i] <= 'z')) {
														int qq = -1;
														for (int q = str[jj].length() - 1; q > i; q--) {
															if ((ch[q] >= 'A' && ch[q] <= 'Z')|| (ch[q] >= 'a' && ch[q] <= 'z')) {
																qq = q;
																break;
															}
														}
														if (i > qq) {
															search_line_error = 0;
															break;
														}
														if (qq != -1 && jj == 0)
															a += str[jj].substring(i,qq + 1);
														else {
															a += ","+ str[jj].substring(i,qq + 1);
														}
														break;
													}
												}
												if (search_line_error == 0)
													break;
											}
											if (search_line_error == 0)
												break;
											if (index == -1) {
												if (a.contains(")"))
													ClassInLine.set(t, a + "{");
												else
													ClassInLine.set(t, a + "){");
												ch = ClassInLine.get(j).toCharArray();
												for (int q = 0; q < ch.length; q++) {
													if (ch[q] == '{') {
														ch[q] = ' ';
														break;
													}
													if (ch[q] != '\t'&& ch[q] != ' ') {
														break;
													}
												}
												String aa = "";
												for (int q = 0; q < ch.length; q++) {
													aa += ch[q];
												}
												ClassInLine.set(j, aa);
											} else if (index == -2) {
												if (ClassInLine.get(j + 1).contains("throws")) {
													ClassInLine.set(t, a + "){");
													ch = ClassInLine.get(j + 1).toCharArray();
													for (int i = 0; i < ch.length; i++) {
														if (ch[i] == '{') {
															String ss = ClassInLine.get(j + 1).substring(i + 1,ch.length);
															ClassInLine.set(j + 1, ss);
														}
													}
												} else {
													if (a.contains(")"))
														ClassInLine.set(t, a+ "{");
													else
														ClassInLine.set(t, a+ "){");
													ch = ClassInLine.get(j + 1).toCharArray();
													for (int q = 0; q < ch.length; q++) {
														if (ch[q] == '{') {
															ch[q] = ' ';
															break;
														}
														if (ch[q] != '\t'&& ch[q] != ' ') {
															break;
														}
													}
													String aa = "";
													for (int q = 0; q < ch.length; q++) {
														aa += ch[q];
													}
													ClassInLine.set(j + 1, aa);
												}
											} else
												ClassInLine.set(t, a);
											break;
										}
									}
									if (search_line_error == 0) {
										t = j + 1;
										continue;
									}
									if (mark == 0)
										continue;
									if (ClassInLine.get(t).contains(")")&& ClassInLine.get(t).contains("{")) 
									{// 文本首行格式2：方法名(-,-){
										left = ClassInLine.get(t).indexOf("(");
										right = ClassInLine.get(t).indexOf(")");
										Para = ClassInLine.get(t).substring(left + 1, right);
										ParaName = adjust_ParaName(Para);
									}
								} else 
								{// 取到参数信息
									if (ClassInLine.get(t).contains(")")&& ClassInLine.get(t).contains("main")) 
									{// 文本首行格式1：方法名(-,-)
										ParaName = "String[]";
									} else if (ClassInLine.get(t).contains(")")&& !ClassInLine.get(t).contains("{")) 
									{// 文本首行格式1：方法名(-,-)
										if (ContainPara("(" + Para + ")") == true)
											ParaName = adjust_ParaName(Para);
										else
											ParaName = "";
									} else if (ClassInLine.get(t).contains(")")&& ClassInLine.get(t).contains("{")) 
									{// 文本首行格式2：方法名(-,-){
										Para = ClassInLine.get(t).substring(left + 1, right);
										if (ContainPara("(" + Para + ")") == true)
											ParaName = adjust_ParaName(Para);
										else
											ParaName = "";

									} else if (ClassInLine.get(t + 1).contains("throws")|| ClassInLine.get(t + 1).contains("{")) 
									{// 文本首行格式5：方法名(-,-,-)
										// throws || {
										Para = ClassInLine.get(t).substring(left + 1, right);
										if (ContainPara("(" + Para + ")") == true)
											ParaName = adjust_ParaName(Para);
										else
											ParaName = "";
										ParaName = adjust_ParaName(Para);
									}
								}
								// OnlyParaName = adjust_ParaName(OnlyParaName);
								if (ParaName != null && ParaName.contains("."))
									ParaName = delPrefix(ParaName);
								if (OnlyParaName.equals(ParaName)) {
									StartLine = t + 1;
									int LeftBracketNum = 0;
									int RightBracket = 0;
									int start = t;
									for (int m = t; m < ClassInLine.size(); m++) {
										String a = delNote(ClassInLine.get(m));
										ClassInLine.set(m, a);
										LeftBracketNum += leftBracket(a);
										RightBracket += rightBracket(a);
										if (LeftBracketNum == RightBracket&& LeftBracketNum != 0) {
											t = m;
											break;
										}
									}
									EndLine = StartLine + t - start;
									break;
								}
							}
						} else {
							if (ClassInLine.get(t).contains(OnlyMethodName)) 
							{// 文本首行格式：方法名
								if (ClassInLine.get(t + 1).contains("(")) {
									if (!ClassInLine.get(++t).contains(";")) {
										String Para = null;
										int left = ClassInLine.get(t).indexOf("(");
										int right = ClassInLine.get(t).indexOf(")");
										if (left + 1 > right && right != -1)
											continue;
										if (left != -1 && right != -1)
											Para = ClassInLine.get(t).substring(left + 1, right);
										String Paras = null;
										if (Para != null) {
											Paras = adjust_ParaName(Para);
										}
										if (Paras != null&& Paras.contains("."))
											Paras = delPrefix(Paras);
										if (OnlyParaName.equals(Paras)) {
											StartLine = t;
											int LeftBracketNum = 0;
											if (ClassInLine.get(t).contains("{"))
												LeftBracketNum = 1;
											int RightBracket = 0;
											int start = t;
											for (int m = t + 1; m < ClassInLine.size(); m++) {
												if (ClassInLine.get(m).contains("{"))
													LeftBracketNum++;
												if (ClassInLine.get(m).contains("}"))
													RightBracket++;
												if (LeftBracketNum == RightBracket&& LeftBracketNum != 0) {
													t = m + 1;
													break;
												}
											}
											EndLine = StartLine + t - start;
											break;
										}
									}
								}
							}
						}
					} else 
					{// 分析的是不带参数的函数
						if (ClassInLine.get(t).contains(OnlyMethodName + "()")) {
							if (End_Word_isPoint(ClassInLine.get(t)) == false) {
								StartLine = t + 1;
								int LeftBracketNum = 0;
								if (ClassInLine.get(t).contains("{"))
									LeftBracketNum = 1;
								int RightBracket = 0;
								int start = t;
								for (int m = t + 1; m < ClassInLine.size(); m++) {
									if (ClassInLine.get(m).contains("{"))
										LeftBracketNum++;
									if (ClassInLine.get(m).contains("}"))
										RightBracket++;
									if (LeftBracketNum == RightBracket&& LeftBracketNum != 0) {
										t = m;
										break;
									}
								}
								EndLine = StartLine + t - start;
							} else if (isInterface == true&& ClassInLine.get(t).contains(";")) 
							{// 接口形式2：不带特殊关键字+方法名();
								StartLine = t + 1;
								EndLine = t + 1;
							}
						}
					}
				}
			}
			for (int j = StartLine - 2; j > 0; j--) {
				if (FirstWord_isNotes(ClassInLine.get(j)) == true) {
					StartLine--;
				} else
					break;
			}
		}
		value.methodlinesbe = StartLine;
		value.methodlinesaf = EndLine;
		return value;
	}

	/*
	 * 去除org.openscience.jmol.Atom前缀
	 */
	private static String delPrefix(String paraName) {
		// TODO Auto-generated method stub
		String para = "";
		String str[] = paraName.split(",");
		for (int i = 0; i < str.length; i++) {
			int flag = 1;
			while (str[i].contains(".")) {
				int index = str[i].indexOf(".");
				str[i] = str[i].substring(index + 1, str[i].length());
				flag = 0;
			}
			if (i == 0)
				para = str[i];
			else {
				if (flag == 0)
					para += ", " + str[i];
				else
					para += "," + str[i];
			}
		}
		return para;
	}

	private static int rightBracket(String text) {
		// TODO Auto-generated method stub
		int num = 0;
		while (text.contains("}")) {
			int index = text.indexOf("}");
			num++;
			text = text.substring(index + 1, text.length());
		}
		return num;
	}

	private static int leftBracket(String text) {
		// TODO Auto-generated method stub
		int num = 0;
		while (text.contains("{")) {
			int index = text.indexOf("{");
			num++;
			text = text.substring(index + 1, text.length());
		}
		return num;
	}

	private static String delNote(String text) {
		// TODO Auto-generated method stub
		String delText = "";
		if (text.contains("/")) {
			char[] ch = text.toCharArray();

			for (int i = 0; i < ch.length; i++) {
				if (ch[i] == '/')
					break;
				else
					delText += ch[i];
			}
		} else
			delText = text;
		return delText;
	}

	private static boolean FirstWord_isNotes(String text) {
		// TODO Auto-generated method stub
		boolean isNotes = false;
		char[] ch = text.toCharArray();
		int note = -1;
		for (int i = 0; i < ch.length; i++) {
			if (ch[i] == '/' || ch[i] == '*') {
				note = i;
				break;
			}
			if ((ch[i] >= 'A' && ch[i] <= 'Z')|| (ch[i] >= 'a' && ch[i] <= 'z')) {
				note = -1;
				break;
			}
		}
		if (note != -1)
			isNotes = true;
		return isNotes;
	}

	private static String adjust_ParaName(String Para) {
		// TODO Auto-generated method stub
		String Paras = null;
		char[] c = Para.toCharArray();
		String str = "";
		for (int i = 0; i < c.length;) {
			if (c[i] == ' ' || c[i] == '\t')
				i++;
			else {
				str = Para.substring(i, c.length);
				break;
			}
		}
		if (str != "")
			Para = str;
		while (Para.indexOf(",") != -1) {
			String a = Para.substring(0, Para.indexOf(","));
			int m = a.indexOf(" ");
			if (m != -1) {
				if (Paras == null)
					Paras = a.substring(0, m);
				else
					Paras += ", " + a.substring(0, m);
			}
			for (int i = Para.indexOf(","), mark = 0; i < Para.length(); i++) {
				if((i + 2)<=(Para.length()-1)){
				if (!Para.substring(i + 1, i + 2).equals(" ")) {
					a = Para.substring(Para.indexOf(",") + mark + 1, Para.length());
					Para = a;
					break;
				} else
					mark++;
				}
			}
		}
		String[] a = Para.split(" ");
		if (Paras == null)
			Paras = a[0];
		else
			Paras += ", " + a[0];
		return Paras;
	}
}
