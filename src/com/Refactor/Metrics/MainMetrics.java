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

package com.Refactor.Metrics;




import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.Refactor.Inheritance.extend;
import com.Refactor.NonInheritance.MatrixComputing;
import com.Refactor.NonInheritance.preprocessing;
import com.Refactor.classparser.ClassObject;
import com.Refactor.classparser.Feature;
import com.jeantessier.dependencyfinder.gui.SrcAction;
import com.neu.utils.listener.ProgressBarLisenter;



public class MainMetrics {
	public static CompareBeforeExtend MainBeforeOrAfterRefactorMetrics
	(ArrayList<ArrayList<ArrayList<extend>>> levels, ArrayList<ArrayList<String>> result0, 
			ProgressBarLisenter barLisenter){
		AuxiliaryMeasure am = new AuxiliaryMeasure();
		ArrayList<ArrayList<String>> BeforeRefactoring =  BeforeRefactoring();
		am.setBeforeRefactor(BeforeRefactoring);
//		int[][] extendsMatrix = preprocessing.printextendsandimplements();
		am.setBeforeRefactorInheritance(preprocessing.extendsMatrix.clone());		
		
	    ArrayList<ArrayList<String>> result = getResults(levels,result0);  

	    int[][] RefactorExtendsMatrix = getRefactorExtendsMatrix(result, levels);
		am.setExtendRefactor(result);
		am.setExtendRefactorInheritance(RefactorExtendsMatrix);
		List<ClassObject> classesArrList = ConvertMaptoList();
		CompareBeforeExtend  cm = new CompareBeforeExtend (am, classesArrList, barLisenter);
		return cm;
	}
	
	public static ArrayList<ArrayList<String>> BeforeRefactoring(){
		ArrayList<ArrayList<String>> BeforeRefactoring = new ArrayList<ArrayList<String>>();
		BeforeRefactoring.clear();
		for(Map.Entry<String, ClassObject> entry : SrcAction.classesMap.entrySet()) {
	    	   ClassObject value = entry.getValue();
	    	   ArrayList<String> cla = new ArrayList<String>();
	    	   
	    	   Map<String, Feature> featureMap = value.featureMap;
	    	   for(Map.Entry<String, Feature> entry1 : featureMap.entrySet()) {
	    		  String key =  entry1.getKey();
	    		  cla.add(key);
	    	   }

			BeforeRefactoring.add(cla);
		}
		
		return BeforeRefactoring;
	}
	
	
	public static int[][] getRefactorExtendsMatrix(ArrayList<ArrayList<String>> result, ArrayList<ArrayList<ArrayList<extend>>> levels){
		  int[][] RefactorExtendsMatrix  = new int[result.size()][result.size()];
		  for(int i = 0; i < result.size(); i ++){
			  for(int j = 0; j < result.size(); j ++){
				  RefactorExtendsMatrix[i][j]=0;
			  }
		  }
		  for(int i = 0; i < levels.size(); i++){ //遍历每棵继承树
			  for(int j = 0; j < levels.get(i).size(); j++){//每棵继承树的每层
				  for(int k =0; k <levels.get(i).get(j).size();k++){  //遍历每层的每个节点
					  for(int o = 0; o < levels.get(i).get(j).get(k).cns.size(); o++){
						  if(levels.get(i).get(j).get(k).cns.get(o).size()!=0){
					    int ii = result.indexOf(levels.get(i).get(j).get(k).cns.get(o));
					    if( !levels.get(i).get(j).get(k).SubClassInd.isEmpty()&& (levels.get(i).size()>=(j+2))){
					    	for(int yy = 0 ; yy < levels.get(i).get(j+1).size();yy++){
					    		if(levels.get(i).get(j).get(k).SubClassInd.contains( levels.get(i).get(j+1).get(yy).matrixInd)){
					    		//	System.out.println("j==="+j+"  levels.get(i).size()== "+levels.get(i).size() +"  o==="+o+"  levels.get(i).get(j+1).get(yy).cns.size()=="+ levels.get(i).get(j+1).get(yy).cns.size());
					    		//	System.out.println( levels.get(i).get(j+1).get(yy).upInd);
					    			if(levels.get(i).get(j+1).get(yy).cns.size()>=(o+1)){
					    					if(levels.get(i).get(j+1).get(yy).cns.get(o).size()!=0){
					    						  int jj = result.indexOf(levels.get(i).get(j+1).get(yy).cns.get(o));
					    						  RefactorExtendsMatrix[jj][ii]=1;
					    					}else{
					    						int father = levels.get(i).get(j).get(k).matrixInd;
					    						boolean bbb= false;
					    						for(int hh = j+2 ; hh < levels.get(i).size();hh++){
					    							for(int vv = 0; vv <levels.get(i).get(hh).size(); vv++ ){
					    								if(!levels.get(i).get(hh).get(vv).SuperClassInd.isEmpty()){
					    								if(levels.get(i).get(hh).get(vv).SuperClassInd.contains(father)){
					    									if(levels.get(i).get(hh).get(vv).cns.size()>=(o+1)){
					    										if(levels.get(i).get(hh).get(vv).cns.get(o).size()!=0){
										    						  int jj = result.indexOf(levels.get(i).get(hh).get(vv).cns.get(o));
										    						  RefactorExtendsMatrix[jj][ii]=1;
										    						  bbb = true;
										    						  break;
										    					}else{
										    						father = levels.get(i).get(hh).get(vv).matrixInd;
										    					}
					    									}
					    								}
					    								}
					    							}
					    							if(bbb){
					    								break;
					    							}
					    								
					    						}
					    					}
					    			}
					    			
					    		}
					    	}
					    }
						  }
					  }
				  }
			  }
		  }
		  return RefactorExtendsMatrix;
	}
	
	public static List<ClassObject>  ConvertMaptoList(){
		List<ClassObject> classesArrList = new ArrayList<ClassObject>();
		for(Map.Entry<String, ClassObject> entry : SrcAction.classesMap.entrySet()) {
			String key = entry.getKey();
			ClassObject value = entry.getValue();
			value.name = key;
			List<Feature> FeatureList = new ArrayList<Feature>();
			Map<String, Feature> featureMap = value.featureMap;
			
			 for(Map.Entry<String, Feature> entry1 : featureMap.entrySet()) {
	    		  String key1 =  entry1.getKey();
	    		  Feature value1 = entry1.getValue();
	    		  value1.name = key1;
	    		  FeatureList.add(value1);
	    	   }
			 value.FeatureList = FeatureList;
			 classesArrList.add(value);
		}
		return classesArrList;
	}
	
	
	
	
	public static ArrayList<ArrayList<String>> getResults(ArrayList<ArrayList<ArrayList<extend>>> levels,ArrayList<ArrayList<String>> result0){
		  ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>> ();
		  for(int i = 0; i < levels.size(); i++){ //遍历每棵继承树
			  for(int j = 0; j < levels.get(i).size(); j++){
				  for(int k =0; k <levels.get(i).get(j).size();k++){
					  if(levels.get(i).get(j).get(k).interfaceornot&&levels.get(i).get(j).get(k).cns.isEmpty()){
						  ArrayList<String> cn = new ArrayList<String>();
						  
						  String cname = SrcAction.classname.get(levels.get(i).get(j).get(k).matrixInd);
						  Map<String, Feature> featureMap = SrcAction.classesMap.get(cname).featureMap;
						  
							 for(Map.Entry<String, Feature> entry1 : featureMap.entrySet()) {
					    		  String key1 =  entry1.getKey();
					    		  if(key1!=null){
					    		  cn.add(key1);
					    		  }
					    	   }

						  levels.get(i).get(j).get(k).cns.add(cn);
					  }
					  
					  for(int o = 0; o < levels.get(i).get(j).get(k).cns.size(); o++){
						if(!levels.get(i).get(j).get(k).cns.get(o).isEmpty()&&!result.contains(levels.get(i).get(j).get(k).cns.get(o))){
							result.add(levels.get(i).get(j).get(k).cns.get(o));
						}
					  }

				  }
			  }
		  }
		  
		  for(int n = 0; n < result0.size(); n++){
			  if(!result0.get(n).isEmpty()&&!result.contains(result0.get(n))){
				  result.add(result0.get(n));
			  }
		  }
		  return result;
	}
}
