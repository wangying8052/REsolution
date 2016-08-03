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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Refactor.Inheritance.ConnComp;
import com.Refactor.classparser.*;
import com.jeantessier.dependencyfinder.gui.SrcAction;
import com.neu.utils.data.Node;
import com.neu.utils.io.NetworkToXMLUtil;
import com.neu.utils.listener.ProgressBarLisenter;
import com.neu.utils.service.CMDNUtil;
import com.opensymphony.webwork.components.If;


public class preprocessing {
	static public int NonIherCount = 0;
	static public int[][] extendsMatrix = new int[1][1];
	static public int[][] dependencyMatrix = new int[1][1];
	static public ArrayList<MergeMethodNonInheri> MergeSetList = new ArrayList<MergeMethodNonInheri>();
	static public double DSC = 0;
	public static void  preprocessingClasses(ProgressBarLisenter barLisenter) throws IOException {
		DSC = SrcAction.classname.size();
		SrcAction.classname = change.FilterClasses();
//		System.out.println("DSC=="+DSC+"  SaveFileAction.classname.size()==="+SrcAction.classname.size());
		extendsMatrix = printextendsandimplements();
		dependencyMatrix = printdependencyrelasions();
		
		int[][] remainMatrix = removeInheritanceNodefromDependency(extendsMatrix.clone(),dependencyMatrix.clone());
		ArrayList <String>  rem = InheritanceNodes(remainMatrix); //继承体系中的非叶子节点的节点列表
	
		MergeSetList = getAllClassToBeMergedfromNoInheritance(remainMatrix,extendsMatrix.clone(),rem);//Method Set to be merged
		
		/**
		 * @author revo begin
		 */
		CMDNUtil.hibernateMatrixToXML(preprocessing.extendsMatrix.clone(), Node.NODE_TYPE_INHERITANCE);
		CMDNUtil.hibernateMatrixToXML(preprocessing.dependencyMatrix.clone(), Node.NODE_TYPE_NON_INHERITANCE);
		
		hibernateMethodListToXmlWrite(MergeSetList);
		/**
		 * @author revo end
		 */
		semantic.findmethodlines(barLisenter);
		semantic.extractword(barLisenter);
		
	}
	
	private static void hibernateMethodListToXmlWrite(
			ArrayList<MergeMethodNonInheri> mergeSetList) {
		
		List<List<String>> list = new ArrayList<List<String>>();
		for (MergeMethodNonInheri mergeMethodNonInheri : mergeSetList) {
			
			List<String> methodList = mergeMethodNonInheri.methodlist;
			list.add(methodList);
		}
		
		CMDNUtil.hibernateMethodListToXmlWrite(list);
		
	}

	/**
	 * 取得继承关系矩阵
	 * 
	 */
	public static int[][] printextendsandimplements() {
		int[][] extendsMatrix = new int[SrcAction.classname.size()][SrcAction.classname.size()];
		for (int i = 0; i < SrcAction.classname.size(); i++) {
			int ind_i = i; //self
			for (int ce = 0; ce < SrcAction.classesMap.get(SrcAction.classname.get(i)).outboundClassList.size(); ce++) {
				String s = SrcAction.classesMap.get(SrcAction.classname.get(i)).outboundClassList.get(ce);
				if (SrcAction.classname.contains(s)) {
					int ind_j = SrcAction.classname.indexOf(s);
					if (ind_i != ind_j) {
						extendsMatrix[ind_i][ind_j] = 1;
					} else {
						extendsMatrix[ind_i][ind_j] = 0;
					}
				}
				
			}
			
			
			for (int ce = 0; ce < SrcAction.classesMap.get(SrcAction.classname.get(i)).inboundClassList.size(); ce++) {
				String s = SrcAction.classesMap.get(SrcAction.classname.get(i)).inboundClassList.get(ce);
				if (SrcAction.classname.contains(s)) {
					int ind_j = SrcAction.classname.indexOf(s);
					if (ind_i != ind_j) {
						extendsMatrix[ind_j][ind_i] = 1;
					} else {
						extendsMatrix[ind_j][ind_i] = 0;
					}
				}
			}
		}

		return extendsMatrix;
	}

	
	
	public static int[][] printdependencyrelasions() {

		int[][] dependencyMatrix = new int[SrcAction.classname.size()][SrcAction.classname.size()];
		for (int i = 0; i < SrcAction.classname.size(); i++) {
					int ind_i = i;
					// 处 理class.feoutbound
					for (int e = 0; e < SrcAction.classesMap.get(SrcAction.classname.get(i)).outboundFeatureList.size(); e++) {
						// 如果包含（） 即是函数的属性 !!切记还有另一种情况!
						if (SrcAction.classesMap.get(SrcAction.classname.get(i)).outboundFeatureList.get(e) != null) {
							String st = SourceParser.Getonlyclassname(SrcAction.classesMap.get(SrcAction.classname.get(i)).outboundFeatureList.get(e));
							if (SrcAction.classname.contains(st)) {
								int ind_j = SrcAction.classname.indexOf(st);
								if (ind_j != ind_i) {
									dependencyMatrix[ind_i][ind_j] = 1;
								} else {
									dependencyMatrix[ind_i][ind_j] = 0;
								}
							}

						}
					}

					// 处 理class.feinbound
					for (int e = 0; e < SrcAction.classesMap.get(SrcAction.classname.get(i)).inboundFeatureList.size(); e++) {
						// 如果包含（） 即是函数的属性 !!切记还有另一种情况!
						if (SrcAction.classesMap.get(SrcAction.classname.get(i)).inboundFeatureList.get(e) != null) {
							String st = SourceParser.Getonlyclassname(SrcAction.classesMap.get(SrcAction.classname.get(i)).inboundFeatureList.get(e));
							if (SrcAction.classname.contains(st)) {
								int ind_j = SrcAction.classname.indexOf(st);

								if (ind_j != ind_i) {
									dependencyMatrix[ind_j][ind_i] = 1;
								} else {
									dependencyMatrix[ind_j][ind_i] = 0;
								}
							}

						}
					}

					Map<String, Feature> featuremap  =  SrcAction.classesMap.get(SrcAction.classname.get(i)).featureMap;
					for(Map.Entry<String, Feature> entry : featuremap.entrySet()) {  
					       String key = entry.getKey();  
					       Feature value = entry.getValue();  
						for (int s = 0; s < value.outboundClassList.size(); s++) {
							if (value.outboundClassList.get(s) != null) {
								String st = SourceParser.Getonlyclassname(value.outboundClassList.get(s));
								if (SrcAction.classname.contains(st)) {
									int ind_j = SrcAction.classname.indexOf(st);
									if (ind_j != ind_i) {
										dependencyMatrix[ind_i][ind_j] = 1;
									} else {
										dependencyMatrix[ind_i][ind_j] = 0;
									}
								}
							}
						}
						// 处理class.feature.feoutbound
						for (int f = 0; f < value.outboundFeatureList.size(); f++) {
							if (value.outboundFeatureList.get(f) != null) {
								String st = SourceParser.Getonlyclassname(value.outboundFeatureList.get(f));
								if (SrcAction.classname.contains(st)) {
									int ind_j = SrcAction.classname.indexOf(st);
									if (ind_j != ind_i) {
										dependencyMatrix[ind_i][ind_j] = 1;
									} else {
										dependencyMatrix[ind_i][ind_j] = 0;
									}
								}
							}
						}
						for (int s = 0; s < value.inboundClassList.size(); s++) {
							if (value.inboundClassList.get(s) != null) {
								String st = SourceParser.Getonlyclassname(value.inboundClassList.get(s));
								if (SrcAction.classname.contains(st)) {
									int ind_j = SrcAction.classname.indexOf(st);
									if (ind_j != ind_i) {
										dependencyMatrix[ind_j][ind_i] = 1;
									} else {
										dependencyMatrix[ind_j][ind_i] = 0;
									}
								}
							}
						}
						// 处理class.feature.feoutbound
						for (int f = 0; f < value.inboundFeatureList.size(); f++) {
							if (value.inboundFeatureList.get(f) != null) {
								String st = SourceParser.Getonlyclassname(value.inboundFeatureList.get(f));
								if (SrcAction.classname.contains(st)) {
									int ind_j = SrcAction.classname.indexOf(st);
									if (ind_j != ind_i) {
										dependencyMatrix[ind_j][ind_i] = 1;
									} else {
										dependencyMatrix[ind_j][ind_i] = 0;
									}
								}
							}
						}

					}//

		}

		return dependencyMatrix;

	}

	
	
	public static int[][] removeInheritanceNodefromDependency(int[][] extendsMatrix, int[][] dependencyMatrix) {

		ArrayList<Integer> dele = new ArrayList<Integer>();

		for (int i = 0; i < extendsMatrix.length; i++) {
			if (MatrixComputing.getOutdgree(extendsMatrix, i)== 0 && MatrixComputing.getIndgree(extendsMatrix, i) != 0 || MatrixComputing.getOutdgree(extendsMatrix, i) != 0 && MatrixComputing.getIndgree(extendsMatrix, i) != 0) {
				dele.add(i);//继承体系中的非叶子节点全部删除
			}

		}

		   for(int i = 0; i< SrcAction.classname.size(); i++){
		   boolean flagg = true;
		   ClassObject CO = SrcAction.classesMap.get( SrcAction.classname.get(i));
		   for(Map.Entry<String, Feature> entry1 : CO.featureMap.entrySet()) {
	    	   String key1 = entry1.getKey();  
	    	   Feature value1 = entry1.getValue();
	    	   if( !value1.outboundFeatureList.isEmpty()){
			    	  flagg = false;
			      }
		   }
		   
		   if(flagg&&!dele.contains(i)){
			   dele.add(i);
		   }
	      }
		
		for (int i = 0; i < dele.size(); i++) {
			dependencyMatrix = MatrixComputing.removeRelaofNodes(dependencyMatrix, dele.get(i));
			
		}

		MatrixComputing.GetLeafNodes(extendsMatrix);

		return dependencyMatrix;

	}
	
	
	public static ArrayList<String> InheritanceNodes(int[][] remainMatrix){
		int count = 0;
		int count1 = 0; 
		
		ArrayList<String> rem = new ArrayList<String>();
		for (int i = 0; i < remainMatrix.length; i++) {
			if (MatrixComputing.getdgree(remainMatrix, i) != 0) {
				count++;		
			}else{
				rem.add(SrcAction.classname.get(i));
				count1++;
			}
		}
		NonIherCount = count;
//		System.out.println("网络中节点总数==="+remainMatrix.length+"   叶子节点和非继承体系节点总数==="+count+"   继承体系中的非叶子节点的节点总数==="+count1);
		return rem;
	}
	
	
	
	public static ArrayList<MergeMethodNonInheri> getAllClassToBeMergedfromNoInheritance(int[][] remainMatrix, int[][] extendsMatrix, ArrayList <String> rem){
		ArrayList<MergeMethodNonInheri> MethodsToBeMerged = new ArrayList<MergeMethodNonInheri>();
		int threhold = 20;
		ArrayList<ArrayList<String>> Allcc = new ArrayList<ArrayList<String>>();
		if(preprocessing.DSC>7){
		Allcc = CMN.CommunityDetectionNoextentsClass(remainMatrix,threhold);//将非继承体系的类及叶子叶节点分成几个社区
		}else{
			Allcc = tool.processExample(Allcc);
		}
		int   mm  = 0;
		int   mmm  = 0;
		for (int t = 0; t < Allcc.size(); t++) {
			
			List<String> classnameList = Allcc.get(t); //即将合并的类的列表
			mmm= mmm+Allcc.get(t).size();
			if(!tool.JudgeExample(classnameList)){
			classnameList.removeAll(rem);
			}
			mm  = mm  + classnameList.size();
			MergeMethodNonInheri rm = new MergeMethodNonInheri();
			ArrayList<String> methodlist = new ArrayList<String>();
			ArrayList<ArrayList<String>> methodlistoriginalpart = new ArrayList<ArrayList<String>>();
			ArrayList<ArrayList<String>> methodbukefen = new ArrayList<ArrayList<String>>();//不可拆分方法，即若叶子节点中调用了父类的方法，或者存在overide的情况，不拆分，且最终该社区仍然要继承其父类
			ArrayList<String> methodduli= new ArrayList<String>();//可以拆分节点，及合并后的函数集合在初始时自身可视为独立社团方法
			for (int i = 0; i < classnameList.size(); i++){
					ArrayList<String> methodlisttem = new ArrayList<String>();//当前类的方法列表
					ArrayList<String> buke = new ArrayList<String>();
					ArrayList<String> duLi = new ArrayList<String>();
					Map<String, Feature> featuremap  =  SrcAction.classesMap.get(classnameList.get(i)).featureMap;
					for(Map.Entry<String, Feature> entry : featuremap.entrySet()) {  
					       String key = entry.getKey();  
					       Feature value = entry.getValue();  
					            
							if(CMN.getisoverride(key,extendsMatrix) || CMN.getissuperdot(key, value, extendsMatrix)){							
									if (buke.isEmpty()) {
										buke.add(key);
									} else {
										if (!buke.contains(key)) {
											buke.add(key);
										                         }
								           }
								}
																
								if(!CMN.getisoverride(key,extendsMatrix)&&!CMN.getissuperdot(key,value,extendsMatrix)){
									if (duLi.isEmpty()) {
										duLi.add(key);
									} else {
										if (!duLi.contains(key)) {
											duLi.add(key);
										                         }
									}
								}
								
									if (methodlisttem.isEmpty()) {
										methodlisttem.add(key);
									} else {
										if (!methodlisttem.contains(key)) {
										methodlisttem.add(key);
										                                  }
									}
					        }

						if(GenerateRefactoringSuggestions.IfScatter(methodlisttem)){//方法总数小于3的类不重构
							Main.buchai.addAll(methodlisttem);
							if(SrcAction.classesMap.get(classnameList.get(i)).isLeaf){
								if(!methodlisttem.isEmpty()){
									SrcAction.classesMap.get(classnameList.get(i)).Refactor = methodlisttem; //叶子节点的不拆分
								                            }	
							}else{
								if(!methodlisttem.isEmpty()){//非叶子节点的不拆分
									Main.jiubuchai.add( methodlisttem);
								                            }
							}

						}
						if(!GenerateRefactoringSuggestions.IfScatter(methodlisttem)){
							if(SrcAction.classesMap.get(classnameList.get(i)).isLeaf){
							}
						methodlist.addAll(methodlisttem);
						methodlistoriginalpart.add(methodlisttem);
						if(!duLi.isEmpty()){
						methodduli.addAll(duLi);
						}
						if(!buke.isEmpty()){
						methodbukefen .add(buke);
						Main.methodbukefenlocal.add(buke);
						}
						}
			}//	for (int i = 0; i < classnameList.size(); i++) {
			rm.methodlist = methodlist;//该类级连通片中包含的所有方法	
			rm.methodlistoriginalpart = methodlistoriginalpart;//该类级连通片中原始的方法划分方式
			rm.methodbukefen = methodbukefen;//不可拆分方法，即若叶子节点中调用了父类的方法，或者存在overide的情况，不拆分，且最终该社区仍然要继承其父类
			rm.methodduli = methodduli;//可以拆分节点，及合并后的函数集合在初始时自身可视为独立社团方法
			rm.classnameListmerge = classnameList;//该类级连通片待合并的类结合

			MethodsToBeMerged.add(rm);
		}   	
		return MethodsToBeMerged;
	}
	
	
}
