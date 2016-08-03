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

package com.Refactor.Inheritance;




import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import bsh.This;

import com.Refactor.NonInheritance.CMN;
import com.Refactor.NonInheritance.GenerateRefactoringSuggestions;
import com.Refactor.NonInheritance.MatrixComputing;
import com.Refactor.NonInheritance.NonInheritanceRefactoring;
import com.Refactor.NonInheritance.change;
import com.Refactor.NonInheritance.newclass;
import com.Refactor.NonInheritance.preprocessing;
import com.Refactor.NonInheritance.semantic;
import com.Refactor.NonInheritance.tool;
import com.Refactor.classparser.ClassObject;
import com.Refactor.classparser.Feature;
import com.Refactor.classparser.SourceParser;
import com.jeantessier.dependencyfinder.gui.SrcAction;
import com.mathworks.toolbox.javabuilder.MWException;
import com.neu.utils.data.DotType;
import com.neu.utils.listener.ProgressBarLisenter;

public class RefactorInheritance {
	public static int T = 5;
	public static int TT = 10;
	public static double[] Q_Orig;
	public static double[] Q_Undo;
	public static ArrayList<ArrayList<ArrayList<extend>>> levels = new ArrayList<ArrayList<ArrayList<extend>>>();
	public static returnvalue rv = new returnvalue();
	public static double persent = 0;
	public static ArrayList<Integer> IndexChechBox = new ArrayList<Integer>();
	public static double Ymax = 0;
	public static double Ymin = 0;
	public static int Xtotal = 0;
	public static double Ycurrent = 0;
	public static int Xcurrent = 0;
	
	public static InheritSuggestions sgs;
	
	public static void TempIndexChechBox(){
		for(int i = 0; i < levels.size(); i++){
			IndexChechBox.add(i);
		}
	}
	
	
	public static ArrayList<ArrayList<String>> CutTrees(ArrayList<ArrayList<String>> Allcc,  int[][] extendsMatrix) throws IOException{
		   getCutinterfaces();
		   ArrayList<String> extendsnames = new ArrayList<String>();
		 
		   ArrayList<ArrayList<String>> Allcc1 = new ArrayList<ArrayList<String>>();
		   ArrayList<ArrayList<String>> AllccRe = new ArrayList<ArrayList<String>>();
		   for(int kk = 0; kk < Allcc.size(); kk ++){
			   for(int hh = 0; hh < Allcc.get(kk).size(); hh ++){
				   extendsnames.add(Allcc.get(kk).get(hh));
			   }
		   }
		
		   int[][] extendsMatrix1 =  new int[extendsMatrix.length][extendsMatrix.length];
		   for(int i = 0; i < extendsMatrix.length; i++){
			   for(int j = 0; j < extendsMatrix.length; j++){
				   extendsMatrix1 [i][j] = extendsMatrix [i][j];
			   }
		   }
		   
		   ArrayList<String> interfacename = new ArrayList<String>();
		   for(int i = 0; i < SrcAction.classname.size(); i++)
			{
					   String classname = SrcAction.classname.get(i);
					   ClassObject CO = SrcAction.classesMap.get(classname);
					   if(CO.interfaceornot){
						   interfacename.add(classname);
						   for(int k = 0; k < extendsMatrix1.length; k++){
							   extendsMatrix1[i][k] = 0;
							   extendsMatrix1[k][i] = 0;
						   }
					   }
					   
			}
		   
		   ArrayList<String> extendsnames1 = new ArrayList<String>();
		  
		   int[][] extendsMatrix2 =  new int[extendsMatrix1.length][extendsMatrix1.length];
		   for(int i = 0; i < extendsMatrix.length; i++){
			   for(int j = 0; j < extendsMatrix.length; j++){
				   extendsMatrix2 [i][j] = extendsMatrix1 [i][j];
			   }
		   }
		   
		   Allcc1 =  ConnComp.getCC(extendsMatrix2.clone());
		   ArrayList<String> extendsnamesclone = (ArrayList<String>)extendsnames.clone();
		   
		   for(int kk = 0; kk < Allcc1.size(); kk ++){
			   for(int hh = 0; hh < Allcc1.get(kk).size(); hh ++){
				   extendsnames1.add(Allcc1.get(kk).get(hh));
			   }
		   }
		   extendsnamesclone.removeAll((ArrayList<String>)extendsnames1.clone());
		   
		   for(int i = 0; i < extendsnamesclone.size(); i++){
			   if(!interfacename.contains(extendsnamesclone.get(i))){
			   ArrayList<String> cone = new  ArrayList<String> ();
			   cone.add(extendsnamesclone.get(i));
			   for(int j = 0; j < interfacename.size(); j++){
				   int ix = SrcAction.classname.indexOf(extendsnamesclone.get(i));
				   int iy = SrcAction.classname.indexOf(interfacename.get(j));
				   if(extendsMatrix[ix][iy]>0){
					   if(!cone.contains(interfacename.get(j))){
					   
					   cone.add(interfacename.get(j));
					   
					   }
				   }
			   }
			   AllccRe.add(cone);
			   }
		   }
		   
		   for(int m = 0; m < Allcc1.size(); m++){
			   ArrayList<String> cone = new  ArrayList<String> ();
			   cone.addAll(Allcc1.get(m));
			   for(int n = 0; n < Allcc1.get(m).size(); n++){
				   
				   for(int j = 0; j < interfacename.size(); j++){
					   int ix = SrcAction.classname.indexOf(Allcc1.get(m).get(n));
					   int iy = SrcAction.classname.indexOf(interfacename.get(j));
					   if((extendsMatrix[ix][iy]>0) && (!cone.contains(interfacename.get(j)))){
						   cone = getInheritanceTreelevels(interfacename.get(j), extendsMatrix,interfacename,cone); 
						   
					   }
				   }
			   }
			   
			   AllccRe.add(cone);
		   }
		   
		return AllccRe;
		
	}
	
	public static  ArrayList<String> getInheritanceTreelevels(String name, int[][] extendsMatrix,ArrayList<String> interfacename, ArrayList<String> cone){
		cone.add(name);
		for(int j = 0; j < interfacename.size(); j++){
			   int ix = SrcAction.classname.indexOf(name);
			   int iy = SrcAction.classname.indexOf(interfacename.get(j));
			   if((extendsMatrix[ix][iy]>0) && (!cone.contains(interfacename.get(j)))){
				   cone=getInheritanceTreelevels(interfacename.get(j), extendsMatrix,interfacename, cone);
				   
			   }
		   }
		return cone;
		
	}
	
	
	public static  ArrayList<ArrayList<ArrayList<extend>>> getInheritanceTreelevels(int[][] extendsMatrix) throws IOException{
		   int[][] extendsMatrix1 =  new int[extendsMatrix.length][extendsMatrix.length];
		   for(int i = 0; i < extendsMatrix.length; i++){
			   for(int j = 0; j < extendsMatrix.length; j++){
				   extendsMatrix1 [i][j] = extendsMatrix [i][j];
			   }
		   }
		   
		   int[][] extendsMatrix11=  new int[extendsMatrix.length][extendsMatrix.length];
		   for(int i = 0; i < extendsMatrix.length; i++){
			   for(int j = 0; j < extendsMatrix.length; j++){
				   extendsMatrix11 [i][j] = extendsMatrix [i][j];
			   }
		   }
		   ConnComp cu = new ConnComp();
		   ArrayList<ArrayList<String>> Allcc = cu.getCC(extendsMatrix1.clone());	 //每一个连通片都是一个继承树
		   ArrayList<ArrayList<String>> Allcc1 = CutTrees((ArrayList<ArrayList<String>>)Allcc.clone(),  extendsMatrix11) ;
		   
//		   for(int i = 0 ; i < Allcc1.size();i++){
//			   System.out.println("*&^%$#@第"+i+"棵继承树有多少个节点==="+Allcc1.get(i).size());
//			   for(int j = 0 ; j < Allcc1.get(i).size(); j++){
//				   System.out.println(Allcc1.get(i).get(j));
//			   }
//		   }
		   
		   ArrayList<ArrayList<ArrayList<extend>>> levels = new ArrayList<ArrayList<ArrayList<extend>>>();
		  
		   
		   for(int i = 0 ; i < Allcc1.size(); i++){  //遍历每一棵继承树
			   ArrayList<ArrayList<extend>> level = new ArrayList<ArrayList<extend>>();
			   ArrayList<extend> temp1 = new ArrayList<extend>();
			   ArrayList<Integer> temp2 = new ArrayList<Integer>();
			   for(int j = 0; j < Allcc1.get(i).size(); j++){
			  
			   int inx = SrcAction.classname.indexOf(Allcc1.get(i).get(j));
			   if((MatrixComputing.getOutdgree(extendsMatrix, inx)==0 && MatrixComputing.getIndgree(extendsMatrix, inx)!=0)||SrcAction.classesMap.get(Allcc1.get(i).get(j)).interfaceornot){
				   extend ex = new extend();
				   ex.matrixInd = inx;
				   ex.TreenodeName = Allcc1.get(i).get(j);
				   temp1.add(ex);
				   temp2.add(ex.matrixInd);
			   }
			  
			   }
			   
			   level.add(temp1);
			  boolean flag = true;
			   while(flag){
				  ArrayList<extend> temp = new ArrayList<extend>();
	              for(int k = 0 ; k < level.get(level.size()-1).size(); k++){
	            	  for(int h = 0; h < extendsMatrix.length; h++){
	            		  if((extendsMatrix[h][level.get(level.size()-1).get(k).matrixInd]!=0)&&Allcc1.get(i).contains(SrcAction.classname.get(h))){
	            			  extend ex = new extend();
	           			      ex.matrixInd = h;
	           			      ex.TreenodeName =  SrcAction.classname.get(h);
	           			      
	           			      if(ex.SuperClassInd.isEmpty()){
	           			    	  ex.SuperClassInd.add(level.get(level.size()-1).get(k).matrixInd);
	           			    	  ex.SuperClassName.add(SrcAction.classname.get(level.get(level.size()-1).get(k).matrixInd));
	           			      }else{
	           			       if(!ex.SuperClassInd.contains(level.get(level.size()-1).get(k).matrixInd)){
	 	           			      ex.SuperClassInd.add(level.get(level.size()-1).get(k).matrixInd);
	 	           			      ex.SuperClassName.add(SrcAction.classname.get(level.get(level.size()-1).get(k).matrixInd));
	 	           			      }
	           			      }
	           			      if(!temp2.contains(ex.matrixInd)){
	           			      
	            			  temp.add(ex);
	            			  temp2.add(ex.matrixInd);
	            			  
	            			  for(int hh = 0; hh < extendsMatrix.length; hh++){
	    	            		  if((extendsMatrix[hh][ex.matrixInd]!=0)&&Allcc1.get(i).contains(SrcAction.classname.get(hh))){
	    	            			  if(ex.SubClassInd.isEmpty()){
	    	            				  ex.SubClassInd.add(hh);
	    	            				  ex.SubClassName.add(SrcAction.classname.get(hh));
	    	            			  }else{
	    	            				  if(!ex.SubClassInd.contains(hh)){
	    	            					  ex.SubClassInd.add(hh);
	    	            					  ex.SubClassName.add(SrcAction.classname.get(hh));
	    	            				  }
	    	            			  }
	    	            		  }
	    	            		  
	    	            		  if((extendsMatrix[ex.matrixInd][hh]!=0)&&Allcc1.get(i).contains(SrcAction.classname.get(hh))){
	    	            			  if(ex.SuperClassInd.isEmpty()){
	    	            				  ex.SuperClassInd.add(hh);
	    	            				  ex.SuperClassName.add(SrcAction.classname.get(hh));
	    	            			  }else{
	    	            				  if(!ex.SuperClassInd.contains(hh)){
	    	            					  ex.SuperClassInd.add(hh);
	    	            					  ex.SuperClassName.add(SrcAction.classname.get(hh));
	    	            				  }
	    	            			  }
	    	            		  }
	            			  }
	            			  
	           			      }else{
	           			    	  for(int yy = 0 ; yy < temp.size();yy++){
	           			    		  if(temp.get(yy).matrixInd==ex.matrixInd){
	           			    			temp.get(yy).SuperClassInd.addAll(ex.SuperClassInd) ;
	           			    			temp.get(yy).SuperClassName.addAll(ex.SuperClassName);
	     	           			       break;
	           			    		  }
	           			    	  }
	           			      }
	           			      if(!level.get(level.size()-1).get(k).SubClassInd.contains(h)){
	            			  level.get(level.size()-1).get(k).SubClassInd.add(h);
	            			  level.get(level.size()-1).get(k).SubClassName.add(SrcAction.classname.get(h));
	           			      }
	            		  }

	            	  }
	              }
	              if(!temp.isEmpty()){
	            	  level.add(temp);
	              }else{
	            	  flag = false;  
	              }
			   }
			   levels.add(level);
	   
		   }
		   
		   System.out.println("有多少棵树=="+levels.size());
		   for(int i = 0 ; i < levels.size(); i++){
			   System.out.println("第"+i+"棵-----------------");
			   for(int j = 0 ; j < levels.get(i).size(); j++){
				   for(int k = 0 ; k < levels.get(i).get(j).size(); k++){
				   System.out.println(levels.get(i).get(j).get(k).TreenodeName+"-----");
				   System.out.println(levels.get(i).get(j).get(k).SubClassName);
				   System.out.println(levels.get(i).get(j).get(k).SuperClassName);
				   }
			   }
		   }
		   
		   
		   return levels;
	}
	
	
	public static  ArrayList<ArrayList<ArrayList<extend>>> getinterfaces(ArrayList<ArrayList<ArrayList<extend>>>levels) throws IOException{
		   for(int x = 0;  x < levels.size(); x++){
			   for(int yy = 0 ; yy <  levels.get(x).size(); yy++){
			   for(int y = 0 ; y <  levels.get(x).get(yy).size(); y++){
				   String classname = SrcAction.classname.get(levels.get(x).get(yy).get(y).matrixInd);
				   if(SrcAction.classesMap.get(classname).interfaceornot){
					   levels.get(x).get(yy).get(y).interfaceornot = true;
				   }
			   }
			   }
		   }
		return levels;
	}
	
	public static  void getCutinterfaces() throws IOException{
		for(int i = 0; i < SrcAction.classname.size(); i++)
		{
				   boolean flagg = true;
				   String classname = SrcAction.classname.get(i);
				   ClassObject CO = SrcAction.classesMap.get(classname);
				   
				   for(Map.Entry<String, Feature> entry1 : CO.featureMap.entrySet()) {
			    	   String key1 = entry1.getKey();  
			    	   Feature value1 = entry1.getValue();
			    	   if(key1.contains("(")){
			    	   if( !value1.outboundFeatureList.isEmpty()){
					    	  flagg = false;
					      }
			    	   }
				   }
				   
				   if(flagg){		 
					   SrcAction.classesMap.get(classname).interfaceornot = true;
				   }
		}
	}
	
	public static  ArrayList<ArrayList<ArrayList<extend>>> getTreesBeforeRefactoring(ArrayList<ArrayList<ArrayList<extend>>>levels) throws IOException{
		 
	
		for(int x = 0;  x < levels.size(); x++){
			   for(int yy = 0 ; yy <  levels.get(x).size(); yy++){
			   for(int y = 0 ; y <  levels.get(x).get(yy).size(); y++){
				   String classname = SrcAction.classname.get(levels.get(x).get(yy).get(y).matrixInd);
				   ClassObject CO = SrcAction.classesMap.get(classname);
				   ArrayList<String> cc = new  ArrayList<String> ();
				   for(Map.Entry<String, Feature> entry1 : CO.featureMap.entrySet()) {
			    	   String key1 = entry1.getKey();  
			    	   
						if (key1!= null) {				
							cc .add(key1);
					}

				   }
				   levels.get(x).get(yy).get(y).cns.clear();
				   levels.get(x).get(yy).get(y).cns.add(cc);  
  
			   }
			   }
		   }
		return levels;
	}
	
	
	
	public static  void PrintTrees(ArrayList<ArrayList<ArrayList<extend>>>levels) throws IOException{
		 
		
		for(int x = 0;  x < levels.size(); x++){
			System.out.println("第"+x+"棵树---");
			   for(int yy = 0 ; yy <  levels.get(x).size(); yy++){
			   for(int y = 0 ; y <  levels.get(x).get(yy).size(); y++){
				   System.out.println("包含类"+SrcAction.classname.get(levels.get(x).get(yy).get(y).matrixInd));
			   }
			   }
		   }
	}
	
	public static boolean IfScatter(ArrayList<String> cns){
		boolean flag = false;
			int cnt = 0;
			for(int j = 0; j < cns.size(); j++){
				if(cns.get(j).contains("(")||cns.get(j).contains("{")){
					cnt++;
				}
			}
			if(cnt<=RefactorInheritance.T){
				flag = true;			
			}
		return flag;
		
	}
	
	
	public static boolean IfScatter1(ArrayList<String> cns){
		boolean flag = false;
			int cnt = 0;
			for(int j = 0; j < cns.size(); j++){
				if(cns.get(j).contains("(")||cns.get(j).contains("{")){
					cnt++;
				}
			}
			if(cnt<=3){
				flag = true;			
			}
		return flag;
		
	}
	
	public static ArrayList<ArrayList<String>> dealthecns(ArrayList<ArrayList<String>>  cns, ArrayList<String> methodlist){
		ArrayList<ArrayList<String>>  cns1 = new ArrayList<ArrayList<String>> ();
		ArrayList<ArrayList<String>>  cnssss = new ArrayList<ArrayList<String>> ();
		for(int i = 0 ; i < cns.size(); i++){
			if(!IfScatter(cns.get(i))){
				cns1.add(cns.get(i));
			}
		}
		ArrayList<String>  methodlist1 = (ArrayList<String>)methodlist.clone();
     
     for(int i = 0 ; i < cns1.size(); i++){
    	 methodlist1.removeAll(cns1.get(i));
		}
     if(!IfScatter(methodlist1)){
     cns1.add(methodlist1);
     }else{
    	 if(!cns1.isEmpty()){
    	 ArrayList<String>  last= (ArrayList<String> )  cns1.get(cns1.size()-1).clone();
    	 
    	 last.removeAll(methodlist1);
    	 last.addAll(methodlist1);
    	 cns1.set(cns1.size()-1,last);
    	 }else{
    		 cns1.add(methodlist1);
    	 }
     }
     int max = 0;
     int maid = 0;
     for(int i = 0 ; i < cns1.size();i++){
    	 if(cns1.get(i).size()>0){
    		 cnssss.add(cns1.get(i));
    	 }
    	
     }
     
     for(int i = 0; i < cnssss.size(); i++){
    	 if(max< cnssss.get(i).size()){
    		 max = cnssss.get(i).size();
    		 maid = i; 
    	 }
     }
     ArrayList<String> temp = ( ArrayList<String> )cnssss.get(0).clone();
     cnssss.set(0,cnssss.get(maid));
     cnssss.set(maid,temp);
	 return cnssss;
	}
	
	public static ArrayList<ArrayList<String>> dealthecns11(ArrayList<ArrayList<String>>  cns, ArrayList<String> methodlist){
		ArrayList<ArrayList<String>>  cns1 = new ArrayList<ArrayList<String>> ();
		ArrayList<ArrayList<String>>  cnssss = new ArrayList<ArrayList<String>> ();
		for(int i = 0 ; i < cns.size(); i++){
			if(!IfScatter1(cns.get(i))){
				cns1.add(cns.get(i));
			}
		}
		ArrayList<String>  methodlist1 = (ArrayList<String>)methodlist.clone();
     
     for(int i = 0 ; i < cns1.size(); i++){
    	 methodlist1.removeAll(cns1.get(i));
		}
     if(!IfScatter1(methodlist1)){
     cns1.add(methodlist1);
     }else{
    	 if(!cns1.isEmpty()){
    	 ArrayList<String>  last= (ArrayList<String> )  cns1.get(cns1.size()-1).clone();
    	 
    	 last.removeAll(methodlist1);
    	 last.addAll(methodlist1);
    	 cns1.set(cns1.size()-1,last);
    	 }else{
    		 cns1.add(methodlist1);
    	 }
     }
     int max = 0;
     int maid = 0;
     for(int i = 0 ; i < cns1.size();i++){
    	 if(cns1.get(i).size()>0){
    		 cnssss.add(cns1.get(i));
    	 }
    	
     }
     
     for(int i = 0; i < cnssss.size(); i++){
    	 if(max< cnssss.get(i).size()){
    		 max = cnssss.get(i).size();
    		 maid = i; 
    	 }
     }
     ArrayList<String> temp = ( ArrayList<String> )cnssss.get(0).clone();
     cnssss.set(0,cnssss.get(maid));
     cnssss.set(maid,temp);
	 return cnssss;
	}
	//处理根节点
	
	public static ArrayList<ArrayList<ArrayList<extend>>> rootsome(String log1, ArrayList<ArrayList<ArrayList<extend>>> levels, int ike, int jceng, int kge) throws IOException, SAXException, MWException  {
		 if(levels.get(ike).get(jceng).get(kge).interfaceornot) {
			 levels.get(ike).get(jceng).get(kge).cns.clear();
			 ArrayList<String> cc = new  ArrayList<String> ();
			 String classname = SrcAction.classname.get(levels.get(ike).get(jceng).get(kge).matrixInd);
			   ClassObject CO = SrcAction.classesMap.get(classname);
			   for(Map.Entry<String, Feature> entry1 : CO.featureMap.entrySet()) {
		    	   String key1 = entry1.getKey();   	  
					if (key1!= null) {				
							cc .add(key1);
					}
		    	   
			   }
			 
			 levels.get(ike).get(jceng).get(kge).cns.add(cc);
		 }
		 
		if(!levels.get(ike).get(jceng).get(kge).interfaceornot) {
		ArrayList<String> methodlist = new ArrayList<String>();
		ArrayList<String> methodlistAll = new ArrayList<String>();
		
		 String classname = SrcAction.classname.get(levels.get(ike).get(jceng).get(kge).matrixInd);
		   ClassObject CO = SrcAction.classesMap.get(classname);
		   for(Map.Entry<String, Feature> entry1 : CO.featureMap.entrySet()) {
	    	   String key1 = entry1.getKey();  
	    	  
				if (key1 != null) {
									
						if ( methodlistAll.isEmpty()) {
							methodlistAll.add(key1);
						} else {
							if (!methodlistAll.contains(key1)) {
								methodlistAll.add(key1);
							}
						}

							if (methodlist.isEmpty()) {
								methodlist.add(key1);
							} else {
								if (!methodlist.contains(key1)) {
								methodlist.add(key1);
								}
							}
			}
		   }
		
		int n = methodlist.size();    
		
		if(CO.outboundOtherClassList.isEmpty()){
		if(CO.featureMap.size()>TT){
		double[][] A = new double[n][n];
		A = MatrixComputing.makezeroMatrix(A);
		double[][] xshare = new double[n][n];
		xshare = MatrixComputing.makezeroMatrix(xshare);
		double[][] ycall = new double[n][n];
		ycall = MatrixComputing.makezeroMatrix(ycall); 
		double[][] zexe = new double[n][n];
		zexe = MatrixComputing.makezeroMatrix(zexe); 
		double[][] wsem = new double[n][n];
		wsem = MatrixComputing.makezeroMatrix(wsem);
		
		double a = NonInheritanceRefactoring.a;
		double b = NonInheritanceRefactoring.b;
		double c = NonInheritanceRefactoring.c;
		double d = NonInheritanceRefactoring.d;
		if(n>0){
		if(a > 0){
			xshare = NonInheritanceRefactoring.makeshareattributmatrix(methodlist);
		}
		if(b > 0){
			ycall = NonInheritanceRefactoring.makecallingmethodmatrix(methodlist);
		}
		if(c > 0){
			zexe = NonInheritanceRefactoring.makeexecutematrix(methodlist);
			zexe = semantic.filterexeBetweenclass(zexe, methodlist, xshare, ycall);
		}
		if(d > 0){
			//语义相似度矩阵
			wsem = semantic.makesemanticmatrix(methodlist);
			wsem = semantic.filterSemantic(wsem,methodlist,xshare,ycall,zexe);
			
		}
		
		}
		
		A = MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixDotMultiply(2,MatrixComputing.MatrixDotMultiply(a, xshare)),MatrixComputing.MatrixDotMultiply(b, ycall)),MatrixComputing.MatrixDotMultiply(c, zexe)),MatrixComputing.MatrixDotMultiply(0, MatrixComputing.MatrixDotMultiply(d, wsem)));
			
		returnvalue  rv = CMN.CommunityDetectionNoextentsInheritance(A, methodlist);//重构后类的结构
		levels.get(ike).get(jceng).get(kge).cns = dealthecns(rv.cns, methodlistAll);
		levels.get(ike).get(jceng).get(kge).mainidx = 0;
		levels.get(ike).get(jceng).get(kge).DeltaQ = rv.MQ;
		
			}else{
				ArrayList<ArrayList<String>>  cns = new ArrayList<ArrayList<String>>();
				cns.add(methodlist);
				levels.get(ike).get(jceng).get(kge).cns = cns;
				levels.get(ike).get(jceng).get(kge).mainidx = 0;
			}
		}else{
			
			ArrayList<ArrayList<String>>  cns = new ArrayList<ArrayList<String>>();
			cns.add(methodlist);
			levels.get(ike).get(jceng).get(kge).cns = cns;
			levels.get(ike).get(jceng).get(kge).mainidx = 0;
		}
//     	tool.writeByFileWrite(log1,"根节点被分解结果  methodlistAll=="+methodlistAll.size()+"  主继承树==="+levels.get(ike).get( jceng).get(kge).mainidx +"\n");
		CMN.printCNS1(log1, levels.get(ike).get( jceng).get(kge).cns);

			}
       return  levels;
   }
	
	
	//处理根节点
	
		public static ArrayList<ArrayList<ArrayList<extend>>> rootsome1(String log1, ArrayList<ArrayList<ArrayList<extend>>> levels, int ike, int jceng, int kge) throws IOException, SAXException, MWException  {
//			System.out.println("接口是===="+levels.get(ike).get(jceng).get(kge).interfaceornot);
			if(!levels.get(ike).get(jceng).get(kge).interfaceornot) {
			ArrayList<String> methodlist = new ArrayList<String>();
			ArrayList<String> methodlistAll = new ArrayList<String>();
			
			 String classname = SrcAction.classname.get(levels.get(ike).get(jceng).get(kge).matrixInd);
			   ClassObject CO = SrcAction.classesMap.get(classname);
			   for(Map.Entry<String, Feature> entry1 : CO.featureMap.entrySet()) {
		    	   String key1 = entry1.getKey();  
		    	  
					if (key1 != null) {
										
							if ( methodlistAll.isEmpty()) {
								methodlistAll.add(key1);
							} else {
								if (!methodlistAll.contains(key1)) {
									methodlistAll.add(key1);
								}
							}

								if (methodlist.isEmpty()) {
									methodlist.add(key1);
								} else {
									if (!methodlist.contains(key1)) {
									methodlist.add(key1);
									}
								}
				}
			   }
			
			int n = methodlist.size();    
			
//			if(CO.outboundOtherClassList.isEmpty()){
//			if(CO.featureMap.size()>TT){
			double[][] A = new double[n][n];
			A = MatrixComputing.makezeroMatrix(A);
			double[][] xshare = new double[n][n];
			xshare = MatrixComputing.makezeroMatrix(xshare);
			double[][] ycall = new double[n][n];
			ycall = MatrixComputing.makezeroMatrix(ycall); 
			double[][] zexe = new double[n][n];
			zexe = MatrixComputing.makezeroMatrix(zexe); 
			double[][] wsem = new double[n][n];
			wsem = MatrixComputing.makezeroMatrix(wsem);
			
			double a = NonInheritanceRefactoring.a;
			double b = NonInheritanceRefactoring.b;
			double c = NonInheritanceRefactoring.c;
			double d = NonInheritanceRefactoring.d;
			if(n>0){
			if(a > 0){
				xshare = NonInheritanceRefactoring.makeshareattributmatrix1(methodlist);
			}
			if(b > 0){
				ycall = NonInheritanceRefactoring.makecallingmethodmatrix1(methodlist);
			}
			if(c > 0){
				zexe = NonInheritanceRefactoring.makeexecutematrix(methodlist);
			}
			if(d > 0){
				//语义相似度矩阵
				wsem = semantic.makesemanticmatrix(methodlist);
				wsem = semantic.filterSemantic(wsem,methodlist,xshare,ycall,zexe);
				
			}
			
			}
			
			A = MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixDotMultiply(2,MatrixComputing.MatrixDotMultiply(a, xshare)),MatrixComputing.MatrixDotMultiply(b, ycall)),MatrixComputing.MatrixDotMultiply(c, zexe)),MatrixComputing.MatrixDotMultiply(0, MatrixComputing.MatrixDotMultiply(d, wsem)));
				
			returnvalue  rv = CMN.CommunityDetectionNoextentsInheritance(A, methodlist);//重构后类的结构
			levels.get(ike).get(jceng).get(kge).cns = dealthecns11(rv.cns, methodlistAll);
			levels.get(ike).get(jceng).get(kge).mainidx = 0;
			levels.get(ike).get(jceng).get(kge).DeltaQ = rv.MQ;
//	     	tool.writeByFileWrite(log1,"根节点被分解结果  methodlistAll=="+methodlistAll.size()+"  主继承树==="+levels.get(ike).get( jceng).get(kge).mainidx +"\n");
			CMN.printCNS1(log1, levels.get(ike).get( jceng).get(kge).cns);

				}
	       return  levels;
	   }
	
	
	public static ArrayList<ArrayList<ArrayList<extend>>> RefactorEachLevel(String log1, ArrayList<ArrayList<ArrayList<extend>>> levels, int[][] extendsMatrix, ProgressBarLisenter barLisenter) throws IOException, SAXException, MWException{
        double m = 1;
		for(int i = 0; i < levels.size(); i++){ //遍历每棵继承树
//			System.out.println("接口是====IndexChechBox111");
			if(RefactorInheritance.IndexChechBox.contains(i)){
//				System.out.println("接口是====IndexChechBox222");
//	     	 tool.writeByFileWrite(log1,"第"+i+"棵继承树==="+"\n");
  		 /**
  		  * 首先对第一层处理 第i棵树第0层
  		  */
//	    	 tool.writeByFileWrite(log1,"根节点的处理=="+"\n");
	    	 for(int j = 0; j < levels.get(i).get(0).size(); j++){
	    		 //第i棵继承树，第0层，第j个节点
//	    		 tool.writeByFileWrite(log1,"共"+levels.get(i).size()+"层，"+"第0层第"+j+"个节点==="+SrcAction.classname.get(levels.get(i).get(0).get(j).matrixInd)+"  是不是接口=="+levels.get(i).get(0).get(j).interfaceornot+"\n");
	    		 if(preprocessing.DSC > 7){
//	    			 System.out.println("preprocessing.DSC > 7");
	    		 levels = rootsome(log1, levels, i, 0, j);	 
	    		 }else{
	    		 levels = rootsome1(log1, levels, i, 0, j);	 
	    		 }
	    	 }
	    	 
	   		 /**
	   		  * 逐层分解
	   		  */
	    	 
	    	 for(int j = 1; j < levels.get(i).size(); j++){
	    		 for(int k =0; k <levels.get(i).get(j).size();k++){
//	    		     tool.writeByFileWrite(log1,"第"+j+"层第"+k+"个节点==="+SrcAction.classname.get(levels.get(i).get(j).get(k).matrixInd)+"  是不是接口=="+levels.get(i).get(j).get(k).interfaceornot+"\n");
	    			 boolean g = false;
	    			 int indup= 0;
	    			 for(int bb = 0 ; bb < levels.get(i).get(j-1).size();bb++){
	    				 
	    				 if(levels.get(i).get(j-1).get(bb).SubClassInd.contains(levels.get(i).get(j).get(k).matrixInd)){
	    				 if(! levels.get(i).get(j-1).get(bb).cns.isEmpty()&&levels.get(i).get(j-1).get(bb).cns.size()>1){//如果父类被分解了，子类才需要被分解
	    					 indup = bb;  //被分解的那个父类的索引号===indup
	    					 g = true;
	    				 }
	    				 break;
	    				 }
	    			 }
	    			 
                   if(levels.get(i).get(j).get(k).SuperClassInd.size()==1){
	    			 
	    			if(!levels.get(i).get(j).get(k).interfaceornot&&g) {
//	    				   tool.writeByFileWrite(log1,"如果父类分解了=="+"\n");
	    				   if(tool.judge(indup, j, k)){
	    				   levels = fuleibeifenjie11(log1, levels, i, j, k,indup,extendsMatrix);
	    				   }else{
	    				   levels = fuleibeifenjie(log1, levels, i, j, k,indup,extendsMatrix);
	    				   }

	    			}
	    			 
	    			   //如果父类没被分解   加东西
	    			  
	    			if(!levels.get(i).get(j).get(k).interfaceornot&&!g) {
	    				   levels.get(i).get(j).get(k).NOfuleibeifenjie = true;   	
	    				   levels = NOfuleibeifenjie(log1, levels, i, j, k, indup,extendsMatrix)  ;	 //有问题
	    			   }
                   }else{
                   	levels = DUOfuleibeifenjie(log1, levels.get(i).get(j).get(k).SuperClassInd, levels,i, j, k,extendsMatrix);
                   }
	    			 
	    		 }
	    	 }
	    	 
	    	 RefactorInheritance.persent = m/(double)levels.size();
		}
			
			
			barLisenter.endFile();
	}
	  
        return levels;
	}
	
	
	
	
	
	
	
	public static ArrayList<ArrayList<ArrayList<extend>>> DUOfuleibeifenjie(String log1, ArrayList<Integer> upInd, ArrayList<ArrayList<ArrayList<extend>>> levels, int i, int j, int k,int[][] extendsMatrix) throws IOException, SAXException, MWException{
		
		ArrayList<ArrayList<String>> methodbukefen = new ArrayList<ArrayList<String>>();
		ArrayList<String> duLi= new ArrayList<String>();
		ArrayList<String> buke= new ArrayList<String>();
		ArrayList<String> methodlist = new ArrayList<String>();
		ArrayList<String> methodlistAll = new ArrayList<String>();
		ArrayList< ArrayList<String> > cns1 = new ArrayList< ArrayList<String> >();
		ArrayList<Integer> upIndxxx = new ArrayList<Integer> ();
		int mainddd= 0;
			 //找出主继承树
		int xid = 0;
		int ma = 0;
		boolean flag = false;
		for(int nv = 0 ; nv < upInd.size(); nv++)
		{
			for(int kk= 0 ; kk < levels.get(i).get(j-1).size();kk++){
				if(levels.get(i).get(j-1).get(kk).matrixInd==upInd.get(nv)){
					upIndxxx.add(kk);
					if(!levels.get(i).get(j-1).get(kk).interfaceornot){
						if(ma < levels.get(i).get(j-1).get(kk).cns.size()){
							ma = levels.get(i).get(j-1).get(kk).cns.size();
							xid = kk;
							flag = true;
						}
					}

				}
			}
		}
		
		
		if(flag){
		if(ma>0){
		 for(int bv = 0 ; bv < levels.get(i).get(j-1).get(xid).cns.size(); bv++){
			ArrayList<String> subnewArrayList = new ArrayList<String>();
			cns1.add(subnewArrayList);
		 }
	   }else{
	        	ArrayList<String> subnewArrayList = new ArrayList<String>();
	       		cns1.add(subnewArrayList);
	           }
		}else{
			ArrayList<String> subnewArrayList = new ArrayList<String>();
       		cns1.add(subnewArrayList);
		}
		
		  String classname = SrcAction.classname.get(levels.get(i).get(j).get(k).matrixInd);
		  ClassObject co = SrcAction.classesMap.get(classname);
		  boolean fl = false;
		for(int nv = 0 ; nv < upIndxxx.size(); nv++)
		{
			int indup = upIndxxx.get(nv);
			
			 int dx = levels.get(i).get(j-1).get(indup).mainidx;
			 levels.get(i).get(j).get(k).mainidx = dx;
			 //找出主继承树
			 //
			 //为每一个父类建立一个空的子类
			 
			 int cn = 0;
			 for(int bv = 0 ; bv < levels.get(i).get(j-1).get(indup).cns.size(); bv++){
//				 tool.writeByFileWrite(log1,"duofulei,父类分结果！！！！！！！！！！！！第"+bv+"个=="+"\n");
				 if(!levels.get(i).get(j-1).get(indup).cns.get(bv).isEmpty()){
					 cn++;
				 }
				for(int av = 0 ; av < levels.get(i).get(j-1).get(indup).cns.get(bv).size(); av++){
//					 tool.writeByFileWrite(log1,levels.get(i).get(j-1).get(indup).cns.get(bv).get(av)+"\n");
				}
				
				
			 }
			 
			 if(cn > 1){
				 fl = true;
			 }
			   if(!co.isLeaf){
				   for(Map.Entry<String, Feature> entry1 : co.featureMap.entrySet()) {
				       String key1 = entry1.getKey();  
				       Feature value1 = entry1.getValue();
				
					if (key1 != null) {
							
							if ( methodlistAll.isEmpty()) {
								methodlistAll.add(key1);
							} else {
								if (!methodlistAll.contains(key1)) {
									methodlistAll.add(key1);
								}
							}

								//找出全部调用父类的函数和重写父类的函数
								if(CMN.getisoverride(key1,extendsMatrix)||CMN.getissuperdot(key1, value1, extendsMatrix)){
								
									if (buke.isEmpty()) {
										buke.add(key1);
									} else {
										if (!buke.contains(key1)) {
											buke.add(key1);
										}
								}
								}
								//没有调用父类和重写可以进行社区划分
								if(!CMN.getisoverride(key1,extendsMatrix)&&!CMN.getissuperdot(key1,value1,extendsMatrix)){
									if (duLi.isEmpty()) {
										duLi.add(key1);
									} else {
										if (!duLi.contains(key1)) {
											duLi.add(key1);
										}
									}
								}
								
								//找出only调用和重写
								 for(int bv = 0; bv < levels.get(i).get(j-1).get(indup).cns.size(); bv++){
									 if(bv != dx&&!levels.get(i).get(j-1).get(indup).cns.get(bv).isEmpty()){
										 if(getisoverride1(key1,levels.get(i).get(j-1).get(indup).cns,bv)|| getissuperdot1(value1,key1,levels.get(i).get(j-1).get(indup).cns,bv )){
											 if( cns1.get(bv).isEmpty()){
											 cns1.get(bv).add(key1);
											 }else{
												 if( !cns1.get(bv).contains(key1)){
													 cns1.get(bv).add(key1);
													 }
											 }
										 }
									 }
								 }
								 
								 
								if (methodlist.isEmpty()) {
									methodlist.add(key1);
								} else {
									if (!methodlist.contains(key1)) {
									methodlist.add(key1);
									}
								}

						
				}
		}
			   }else{//正在改叶子
				 
				   for (int m = 0; m < co.Refactor.size(); m++) {
					   Feature fa = SrcAction.classesMap.get(SourceParser.Getonlyclassname(co.Refactor.get(m))).featureMap.get(co.Refactor.get(m));
//					
								
								if ( methodlistAll.isEmpty()) {
									methodlistAll.add(co.Refactor.get(m));
								} else {
									if (!methodlistAll.contains(co.Refactor.get(m))) {
										methodlistAll.add(co.Refactor.get(m));
									}
								}
						
									//找出全部调用父类的函数和重写父类的函数
									if(CMN.getisoverride(co.Refactor.get(m),extendsMatrix)||CMN.getissuperdot(co.Refactor.get(m),fa, extendsMatrix)){
									
										if (buke.isEmpty()) {
											buke.add(co.Refactor.get(m));
										} else {
											if (!buke.contains(co.Refactor.get(m))) {
												buke.add(co.Refactor.get(m));
											}
									}
									}
									//没有调用父类和重写可以进行社区划分
									if(!CMN.getisoverride(co.Refactor.get(m), extendsMatrix)&&!CMN.getissuperdot(co.Refactor.get(m),fa, extendsMatrix)){
										if (duLi.isEmpty()) {
											duLi.add(co.Refactor.get(m));
										} else {
											if (!duLi.contains(co.Refactor.get(m))) {
												duLi.add(co.Refactor.get(m));
											}
										}
									}
									
									//找出only调用和重写
									 for(int bv = 0; bv < levels.get(i).get(j-1).get(indup).cns.size(); bv++){
										 if(bv != dx&&!levels.get(i).get(j-1).get(indup).cns.get(bv).isEmpty()){
											 if(getisoverride1(co.Refactor.get(m),levels.get(i).get(j-1).get(indup).cns,bv )|| getissuperdot1(fa,co.Refactor.get(m),levels.get(i).get(j-1).get(indup).cns,bv )){
												 if( cns1.get(bv).isEmpty()){
												 cns1.get(bv).add(co.Refactor.get(m));
												 }else{
													 if( !cns1.get(bv).contains(co.Refactor.get(m))){
														 cns1.get(bv).add(co.Refactor.get(m));
														 }
												 }
											 }
										 }
									 }

									 
									if (methodlist.isEmpty()) {
										methodlist.add(co.Refactor.get(m));
									} else {
										if (!methodlist.contains(co.Refactor.get(m))) {
										methodlist.add(co.Refactor.get(m));
										}
									}

				   }
				   
			   }
				

		}
		
		
		
		 for(int h =0; h < cns1.size(); h++){
			 if(h != mainddd){
		 buke.removeAll(cns1.get(h));
			 }
		 }
		 cns1.set(mainddd, buke);

		 
		 for(int vv = 0 ; vv < cns1.size(); vv++){
			 if(!cns1.get(vv).isEmpty()){
				 methodbukefen.add(cns1.get(vv));
			 }
		 }
		 
		 if((!co.isLeaf) || (fl&&co.isLeaf)){
				int n = methodlist.size();    
				
				double[][] A = new double[n][n];
				A = MatrixComputing.makezeroMatrix(A);
				double[][] xshare = new double[n][n];
				xshare = MatrixComputing.makezeroMatrix(xshare);
				double[][] ycall = new double[n][n];
				ycall = MatrixComputing.makezeroMatrix(ycall); 
				double[][] zexe = new double[n][n];
				zexe = MatrixComputing.makezeroMatrix(zexe); 
				double[][] wsem = new double[n][n];
				wsem = MatrixComputing.makezeroMatrix(wsem);
				
				double a = NonInheritanceRefactoring.a;
				double b = NonInheritanceRefactoring.b;
				double c = NonInheritanceRefactoring.c;
				double d = NonInheritanceRefactoring.d;
				
				if(n>0){
				if(a > 0){
					xshare = NonInheritanceRefactoring.makeshareattributmatrix(methodlist);
				}
				if(b > 0){
					ycall = NonInheritanceRefactoring.makecallingmethodmatrix(methodlist);
				}
				if(c > 0){
					zexe = NonInheritanceRefactoring.makeexecutematrix(methodlist);
				}

				if(d > 0){
					//语义相似度矩阵
					wsem = semantic.makesemanticmatrix(methodlist);
					wsem = semantic.filterSemantic(wsem);
					
				}
				}
		        A = MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixDotMultiply(2,MatrixComputing.MatrixDotMultiply(a, xshare)),MatrixComputing.MatrixDotMultiply(b, ycall)),MatrixComputing.MatrixDotMultiply(c, zexe)),MatrixComputing.MatrixDotMultiply(0, MatrixComputing.MatrixDotMultiply(d, wsem)));
				
				 
				returnvalue rv = CMN.CommunityDetectionNoextentsDUOFULEI(A,  methodlist, methodbukefen, duLi);//重构后类的结构
				ArrayList<ArrayList<String>>  cns2 = rv.cns;

				levels.get(i).get(j).get(k).cns = dealthecns1(cns1, cns2, methodlistAll,mainddd); //dx 是主继承树
				levels.get(i).get(j).get(k).mainidx = 0;
				levels.get(i).get(j).get(k).DeltaQ = rv.MQ;
				
						
		 }else{
			 	ArrayList<ArrayList<String>>  cns = new ArrayList<ArrayList<String>>();
		    	cns.add(methodlist);
		    	levels.get(i).get(j).get(k).cns = cns;
		    	levels.get(i).get(j).get(k).mainidx = 0;
		    	
		 }
		 
				
//				tool.writeByFileWrite(log1,"父类不止一个被分解结果  methodlistAll=="+methodlistAll.size()+"  主继承树==="+levels.get(i).get(j).get(k).mainidx +"\n");
//				tool.writeByFileWrite(log1,"父类不止一个处理后cns1.size()=="+levels.get(i).get(j).get(k).cns.size()+"\n");
				CMN.printCNS1(log1,levels.get(i).get(j).get(k).cns);
			
					   return levels;
		}
		
	
	
	
	
	
	
	
	public static ArrayList<ArrayList<ArrayList<extend>>> NOfuleibeifenjie(String log1,ArrayList<ArrayList<ArrayList<extend>>> levels, int i, int j, int k,int indup,int[][] extendsMatrix) throws IOException, SAXException, MWException{
	
		ArrayList<ArrayList<String>> methodbukefen = new ArrayList<ArrayList<String>>();
		ArrayList<String> duLi= new ArrayList<String>();
		ArrayList<String> buke= new ArrayList<String>();
		ArrayList<String> methodlist = new ArrayList<String>();
		ArrayList<String> methodlistAll = new ArrayList<String>();
		String classname = SrcAction.classname.get(levels.get(i).get(j).get(k).matrixInd);
		ClassObject co = SrcAction.classesMap.get(classname);
		if(!co.isLeaf){
	    for(Map.Entry<String, Feature> entry1 : co.featureMap.entrySet()) {
			       String key1 = entry1.getKey();  
			       Feature value1 = entry1.getValue();	
			if (key1!= null) {
			
					if (methodlistAll.isEmpty()) {
						methodlistAll.add(key1);
					} else {
						if (!methodlistAll.contains(key1)) {
							methodlistAll.add(key1);
						}
					}

						//找出全部调用父类的函数和重写父类的函数
						if(CMN.getisoverride(key1,extendsMatrix)||CMN.getissuperdot(key1, value1, extendsMatrix)){
						
							if (buke.isEmpty()) {
								buke.add(key1);
							} else {
								if (!buke.contains(key1)) {
									buke.add(key1);
								}
						}
						}
						//没有调用父类和重写可以进行社区划分
						if(!CMN.getisoverride(key1,extendsMatrix)&&!CMN.getissuperdot(key1, value1, extendsMatrix)){
							if (duLi.isEmpty()) {
								duLi.add(key1);
							} else {
								if (!duLi.contains(key1)) {
									duLi.add(key1);
								}
							}
						}

						if (methodlist.isEmpty()) {
							methodlist.add(key1);
						} else {
							if (!methodlist.contains(key1)) {
							methodlist.add(key1);
							}
						}

						}
	    			}
	}else{ //改叶子
		for (int m = 0; m < co.Refactor.size(); m++) {
	
			ClassObject cObject = SrcAction.classesMap.get(SourceParser.Getonlyclassname(co.Refactor.get(m)));
			Feature fa = cObject.featureMap.get(co.Refactor.get(m));

			if ( methodlistAll.isEmpty()) {
				methodlistAll.add(co.Refactor.get(m));
			} else {
				if (!methodlistAll.contains(co.Refactor.get(m))) {
					methodlistAll.add(co.Refactor.get(m));
				}
			}

				//找出全部调用父类的函数和重写父类的函数
				if(CMN.getisoverride(co.Refactor.get(m),extendsMatrix)|| CMN.getissuperdot(co.Refactor.get(m),fa,extendsMatrix)){
				
					if (buke.isEmpty()) {
						buke.add(co.Refactor.get(m));
					} else {
						if (!buke.contains(co.Refactor.get(m))) {
							buke.add(co.Refactor.get(m));
						}
				}
				}
				//没有调用父类和重写可以进行社区划分
				if(!CMN.getisoverride(co.Refactor.get(m), extendsMatrix)&&!CMN.getissuperdot(co.Refactor.get(m), fa, extendsMatrix)){
					if (duLi.isEmpty()) {
						duLi.add(co.Refactor.get(m));
					} else {
						if (!duLi.contains(co.Refactor.get(m))) {
							duLi.add(co.Refactor.get(m));
						}
					}
				}
				
				 
				if (methodlist.isEmpty()) {
					methodlist.add(co.Refactor.get(m));
				} else {
					if (!methodlist.contains(co.Refactor.get(m))) {
					methodlist.add(co.Refactor.get(m));
					}
				}

}
}

		methodbukefen.add(buke);
	
		if(!co.isLeaf){	
		if(co.featureMap.size()>50){
		int n = methodlist.size();    
		double[][] A = new double[n][n];
		A = MatrixComputing.makezeroMatrix(A);
		double[][] xshare = new double[n][n];
		xshare = MatrixComputing.makezeroMatrix(xshare);
		double[][] ycall = new double[n][n];
		ycall = MatrixComputing.makezeroMatrix(ycall); 
		double[][] zexe = new double[n][n];
		zexe = MatrixComputing.makezeroMatrix(zexe); 
		double[][] wsem = new double[n][n];
		wsem = MatrixComputing.makezeroMatrix(wsem);
		
		double a = NonInheritanceRefactoring.a;
		double b = NonInheritanceRefactoring.b;
		double c = NonInheritanceRefactoring.c;
		double d = NonInheritanceRefactoring.d;
		
		if(n>0){
		if(a > 0){
			xshare = NonInheritanceRefactoring.makeshareattributmatrix(methodlist);
		}
		if(b > 0){
			ycall = NonInheritanceRefactoring.makecallingmethodmatrix(methodlist);
		}
		if(c > 0){
			zexe = NonInheritanceRefactoring.makeexecutematrix(methodlist);
		}

		if(d > 0){
			//语义相似度矩阵
			wsem = semantic.makesemanticmatrix(methodlist);
			wsem = semantic.filterSemantic(wsem);
			
		}
		}
        A = MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixDotMultiply(2,MatrixComputing.MatrixDotMultiply(a, xshare)),MatrixComputing.MatrixDotMultiply(b, ycall)),MatrixComputing.MatrixDotMultiply(c, zexe)),MatrixComputing.MatrixDotMultiply(0, MatrixComputing.MatrixDotMultiply(d, wsem)));
		
		
        returnvalue rv = CMN.CommunityDetectionNoextentsNOFULEI(A,  methodlist, methodbukefen, duLi);//重构后类的结构
		levels.get(i).get(j).get(k).cns = dealthecns2(rv.cns, methodbukefen, methodlistAll);
		levels.get(i).get(j).get(k).DeltaQ = rv.MQ;

		}else{
	    	ArrayList<ArrayList<String>>  cns = new ArrayList<ArrayList<String>>();
	    	cns.add(methodlist);
	    	levels.get(i).get(j).get(k).cns = cns;
		}
		}else{
	    	ArrayList<ArrayList<String>>  cns = new ArrayList<ArrayList<String>>();
	    	cns.add(methodlist);
	    	levels.get(i).get(j).get(k).cns = cns;
	    }

		levels.get(i).get(j).get(k).mainidx = 0;
//		tool.writeByFileWrite(log1,"如果父类没有被分解，其结果  methodlistAll=="+methodlistAll.size()+"  主继承树==="+levels.get(i).get(j).get(k).mainidx  +"\n");
		CMN.printCNS1(log1, levels.get(i).get(j).get(k).cns);
		return levels;
}
	
	
	
	public static ArrayList<ArrayList<String>> dealExtractmoreclasses(ArrayList<ArrayList<String>> cns, ArrayList<String> methodlist){
		
			if(cns.size()>3){

					 int[] sortid = new int [cns.size()];
					 int[] sortcnt = new int [cns.size()];
					 int in = 0;
					 for(int u = 0 ; u < cns.size();u++){
							 sortid[in] = u;
							 sortcnt [in] = cns.get(u).size();
							 in++;
					 }
					 sortid = semantic.bubsort(sortcnt, sortid);
					 int[] sortid1 = new int [cns.size()-3 +1];
					 for(int m = 0; m < (cns.size()-3 +1); m++){
						 sortid1[m] = sortid[m];
					 }
					 cns = CMN.Mergemore(cns, sortid1) ;
					 
			}
			int count = 0;
			
				boolean flag = false;
				for(int i = 0; i < cns.size(); i++){
					if(IfScatter(cns.get(i))){
						flag = true;
					}
					count = count + cns.get(i).size();
				}
				if(flag){
					ArrayList<ArrayList<String>>  cnssss = new ArrayList<ArrayList<String>> ();
					cnssss.add(methodlist);
					cns = cnssss;
			     }
			
			if(count!=methodlist.size()){
				ArrayList<ArrayList<String>>  cnssss1 = new ArrayList<ArrayList<String>> ();
				cnssss1.add(methodlist);
				cns = cnssss1;
			}

		return cns;
	}
	
	
	public static ArrayList<ArrayList<String>> dealthecns2(ArrayList<ArrayList<String>>  cns, ArrayList<ArrayList<String>>  methodbukefen, ArrayList<String> methodlist){
		ArrayList<ArrayList<String>>  cns1 = new ArrayList<ArrayList<String>> ();
		ArrayList<ArrayList<String>>  cnssss = new ArrayList<ArrayList<String>> ();
		ArrayList<String> tepp = new ArrayList<String>();
		int di = 0;
			for(int i = 0 ; i < cns.size(); i++){

				if(cns.get(i).containsAll(methodbukefen.get(0))){// budui
					cns1.add(cns.get(i));
					tepp.addAll(cns.get(i));
					di = i;
				}
				
			}
			
			for(int i = 0 ; i < cns.size(); i++){

				if(di!=i&&cns.get(i).size()>3){
					cns1.add(cns.get(i));
					tepp.addAll(cns.get(i));
				}
				
			}
			ArrayList<String>  methodlist1 = (ArrayList<String>)methodlist.clone();
	    	 methodlist1.removeAll(tepp);
	    	 if(!cns1.isEmpty()){
	    		 if(IfScatter(cns1.get(0))){
	    			 ArrayList<String>  uu = new ArrayList<String> ();
	    			 uu.addAll(cns1.get(0));
	    			 uu.addAll(methodlist1);
	    			 if(!IfScatter(uu)){
	    				 ArrayList<String>  last= (ArrayList<String>)  cns1.get(0).clone();
	    		    	 
	    		    	 last.removeAll(methodlist1);
	    		    	 last.addAll(methodlist1);
	    		    	 cns1.set(0,last);
	    			 }else{

	    				 if(cns1.size()>1){
	    					int min =  cns1.get(1).size()+cns1.get(0).size()-RefactorInheritance.T;
	    					int din = 1;
	        				 for(int oo=2; oo <cns1.size(); oo++){
	        					 if(cns1.get(oo).size()+cns1.get(0).size()-RefactorInheritance.T < min){
	        						 min = cns1.get(oo).size()+cns1.get(0).size()-RefactorInheritance.T ;
	        						 din = oo;
	        					 }
	        				 }
	    					 
	    					 
	    					 ArrayList<String>  last= (ArrayList<String>)  cns1.get(din).clone();
	    					 cns1.get(0).addAll(last);
	    					 cns1.remove(last);
	    				 }
	    			 }
	    		 }else{
	  if(!IfScatter(methodlist1)){
	     cns1. add(methodlist1);
	     }else{
	    	 ArrayList<String>  last= (ArrayList<String>)  cns1.get(cns1.size()-1).clone();
	    	 
	    	 last.removeAll(methodlist1);
	    	 last.addAll(methodlist1);
	    	 cns1.set(cns1.size()-1,last);
	    	
	     }
	    	 }
	    	 }else{
	    		 if(!methodlist1.isEmpty()){
	    		cns1.add(methodlist1);
	    		 }
	    	 }

	  for(int i = 0 ; i < cns1.size();i++){
	 	 if(cns1.get(i).size()>0){
	 		 cnssss.add(cns1.get(i));
	 	 }
	 	
	  }

	  cnssss = dealExtractmoreclasses(cnssss,methodlist);
	  return cnssss;
		}
	

	
	public static boolean getisoverride1(String name, ArrayList<ArrayList<String>> parts,int bv){
		boolean isoveride = false;
		String self = SourceParser.Getonlyclassname(name);
		boolean isoveride1 = false;
		boolean isoveride2 =false;

		if(name.contains("(")||name.contains("{")){
			String str[] = name.split(self+"\\.");
			
			for(int q = 0; q < parts.get(bv).size(); q ++){
				String other  = SourceParser.Getonlyclassname( parts.get(bv).get(q));
				String str1[] = parts.get(bv).get(q).split(other +"\\.");
				
					if(str[1].equals(str1[1])){
						isoveride =true;
						break;
					}
		}
			if(isoveride){
			for(int i = 1; i < parts.size(); i++){
             
			if(i!=bv){
				for(int q = 0; q < parts.get(i).size(); q ++){
					String other  = SourceParser.Getonlyclassname( parts.get(i).get(q));
					String str1[] = parts.get(i).get(q).split(other +"\\.");					
						if(str[1].equals(str1[1])){
							isoveride1 =true;					
							break;
						}
			}
				
				
			}
			if(isoveride1){
				break;
			}
			
			}
			}
			
			
			if(isoveride){
				if(!isoveride1){
					isoveride2 = true;
				}
			}
		}
		return (isoveride2);
	}
	
	
	public static boolean getissuperdot1(Feature aFeature, String name, ArrayList< ArrayList<String>> parts, int bv){
		boolean issuperdot = false;
		
		if(name.contains("(")||name.contains("{")){
		String self = 	SourceParser.Getonlyclassname(name);
		ArrayList<String> list1 = (ArrayList<String> )  parts.get(bv).clone();
		list1 .retainAll(aFeature.outboundFeatureList);

		for(int i = 1; i < parts.size();i++){
			if(i!=bv){
				ArrayList<String> list2 = (ArrayList<String> )  parts.get(i).clone();
				list2 .retainAll(aFeature.outboundFeatureList);
				list1.removeAll(list2);
			}
		}
		if (!list1 .isEmpty()) {
			 issuperdot = true;
		}
		
		 }
		return issuperdot;
	}
	
	
	
public static ArrayList<ArrayList<String>> dealthecns1(ArrayList<ArrayList<String>>  cns1, ArrayList<ArrayList<String>>  cns2, ArrayList<String> methodlist, int dx){
	    ArrayList<ArrayList<String>> duibi = (ArrayList<ArrayList<String>>)cns1.clone();
		ArrayList<String> tep = new ArrayList<String>();
		ArrayList<String> tepp = new ArrayList<String>();
		for(int i = 0 ; i < cns1.size(); i++){

			for(int j = 0 ; j < cns2.size(); j++){
				if(!cns1.get(i).isEmpty()){
				if(cns2.get(j).containsAll(cns1.get(i))&&!IfScatter(cns2.get(j))){
					cns1.set(i,cns2.get(j));
				}
				if(cns2.get(j).containsAll(cns1.get(i))&&IfScatter(cns2.get(j))){ //   在父类被分解的情况下，子类若有琐碎节点的处理方法
					tep.addAll(cns2.get(j));

				}
				
				}
			}
			
		}
		ArrayList<String> tep1 = (ArrayList<String>)cns1.get(dx).clone();
		tep1.removeAll(tep);
		tep1.addAll(tep);
		cns1.set(dx,tep1);
		for(int i = 0 ; i < cns1.size(); i++){
			if(i!=dx){
				cns1.get(i).removeAll(tep1);
			}
		}
		for(int j = 0 ; j < cns2.size(); j++){
			boolean flag = false;
			for(int i = 0 ; i < cns1.size(); i++){
				if(cns1.get(i).containsAll(cns2.get(j))){
					flag = true;
					break;
				}
			}
			if(!flag&&!IfScatter(cns2.get(j))){
				cns1.add(cns2.get(j));
			}
		}
		
		ArrayList<String>  methodlist1 = (ArrayList<String>)methodlist.clone();
		for(int i = 0 ; i < cns1.size(); i++){
			tepp.addAll(cns1.get(i));
     }
     
    	 methodlist1.removeAll(tepp);
	
    if(!IfScatter(methodlist1)){
    cns1.add(methodlist1);

     }else{
    	 if(!cns1.isEmpty()){
    	 ArrayList<String>  last= (ArrayList<String>)  cns1.get(cns1.size()-1).clone();
    	 
    	 last.removeAll(methodlist1);
    	 last.addAll(methodlist1);
    	 cns1.set(cns1.size()-1,last);
    	 }else{
    		cns1.add(methodlist1);
    	 }
     }
    

    	for(int i = 0 ; i < cns1.size(); i++){
    		boolean flas = false;
    		if(IfScatter(cns1.get(i))){
    			for(int j = 0 ; j < cns1.size(); j++){
    				if(i!=j){
    					if(cns1.get(j).containsAll(cns1.get(i))){
    					cns1.get(i).clear();
    					flas = true;
    					break;
    					}
    					}
    				}
    			if(!flas){
    				for(int j = 0 ; j < cns1.size(); j++){
        				if(i!=j){
        				   if(i != dx){
        					   cns1.get(dx).addAll(cns1.get(i));
        					   cns1.get(i).clear();
        				   }else{
        					   cns1.get(dx).addAll(cns1.get(j));
        					   cns1.get(j).clear();
        				   }
        				}
        				}
        		}
    			}
    	
    	}
    	 ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
    	if(cns1.size()>duibi.size()){
        for(int i = 0 ; i < duibi.size(); i++){
        	result.add(cns1.get(i));
        	}
    	for(int i = duibi.size() ; i < cns1.size(); i++){
    		if(!cns1.get(i).isEmpty()){
    			result.add(cns1.get(i));
    		}
    	}
    	}else{
    		result =  cns1;
    	}
		return result;
	}
	
public static ArrayList<ArrayList<String>> dealthecns111(ArrayList<ArrayList<String>>  cns1, ArrayList<ArrayList<String>>  cns2, ArrayList<String> methodlist, int dx){
    ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
	ArrayList<String> tep = new ArrayList<String>();
	ArrayList<String> tepp = new ArrayList<String>();
	tep.addAll(cns2.get(0));
	tep.addAll(cns2.get(2));
	tepp.addAll(cns2.get(1));
	tepp.addAll(cns2.get(3));
	result.add(tep);
	result.add(tepp);
	return result;
}
	
	public static ArrayList<ArrayList<ArrayList<extend>>> fuleibeifenjie(String log1, ArrayList<ArrayList<ArrayList<extend>>> levels, int i, int j, int k,int indup, int[][] extendsMatrix)throws IOException, SAXException, MWException{
		
		int dx = levels.get(i).get(j-1).get(indup).mainidx;
		levels.get(i).get(j).get(k).mainidx = dx;
		 
		ArrayList< ArrayList<String> > cns1 = new ArrayList< ArrayList<String> >();
		for(int bv = 0 ; bv < levels.get(i).get(j-1).get(indup).cns.size(); bv++){
//			 tool.writeByFileWrite(log1,"其父类分结果！！！！！！！！！！！！第"+bv+"个=="+"\n");
			ArrayList<String> subnewArrayList = new ArrayList<String>();
			cns1.add(subnewArrayList);
			
			for(int av = 0 ; av < levels.get(i).get(j-1).get(indup).cns.get(bv).size(); av++){
//				tool.writeByFileWrite(log1,levels.get(i).get(j-1).get(indup).cns.get(bv).get(av)+"\n");
			}
			
			
		 }
		
		 ArrayList<ArrayList<String>> methodbukefen = new ArrayList<ArrayList<String>>();
		 ArrayList<String> duLi= new ArrayList<String>();
		 ArrayList<String> buke= new ArrayList<String>();
		 ArrayList<String> methodlist = new ArrayList<String>();
		 ArrayList<String> methodlistAll = new ArrayList<String>();
		 String classname = SrcAction.classname.get(levels.get(i).get(j).get(k).matrixInd);
		 ClassObject co = SrcAction.classesMap.get(classname);
			if(!co.isLeaf){
				
				 for(Map.Entry<String, Feature> entry1 : co.featureMap.entrySet()) {
				       String key1 = entry1.getKey();  
				       Feature value1 = entry1.getValue();
				if (key1 != null) {
				
						if ( methodlistAll.isEmpty()) {
							methodlistAll.add(key1);
						} else {
							if (!methodlistAll.contains(key1)) {
								methodlistAll.add(key1);
							}
						}
				
							//找出全部调用父类的函数和重写父类的函数
							if(CMN.getisoverride(key1,extendsMatrix)||CMN.getissuperdot(key1, value1, extendsMatrix)){
							
								if (buke.isEmpty()) {
									buke.add(key1);
								} else {
									if (!buke.contains(key1)) {
										buke.add(key1);
									}
							}
							}
							//没有调用父类和重写可以进行社区划分
							if(!CMN.getisoverride(key1,extendsMatrix)&&!CMN.getissuperdot(key1, value1, extendsMatrix)){
								if (duLi.isEmpty()) {
									duLi.add(key1);
								} else {
									if (!duLi.contains(key1)) {
										duLi.add(key1);
									}
								}
							}
							
							//找出only调用和重写
							 for(int bv = 0; bv < levels.get(i).get(j-1).get(indup).cns.size(); bv++){
								 if(bv != dx&&!levels.get(i).get(j-1).get(indup).cns.get(bv).isEmpty()){
									 if(getisoverride1(key1,levels.get(i).get(j-1).get(indup).cns, bv )|| getissuperdot1(value1,key1,levels.get(i).get(j-1).get(indup).cns,bv )){
										 cns1.get(bv).add(key1);
									 }
								 }
							 }

							if (methodlist.isEmpty()) {
								methodlist.add(key1);
							} else {
								if (!methodlist.contains(key1)) {
								methodlist.add(key1);
								}
							}
					
			}
	}
			}else{ //叶子
				
				for (int m = 0; m < co.Refactor.size(); m++) {
					ClassObject cObject = SrcAction.classesMap.get(SourceParser.Getonlyclassname(co.Refactor.get(m)));
					Feature fa = cObject.featureMap.get(co.Refactor.get(m));
					       String key = co.Refactor.get(m);  
							
							if ( methodlistAll.isEmpty()) {
								methodlistAll.add(key);
							} else {
								if (!methodlistAll.contains(key)) {
									methodlistAll.add(key);
								}
							}
					
								//找出全部调用父类的函数和重写父类的函数
								if(CMN.getisoverride(key, extendsMatrix)|| CMN.getissuperdot(key, fa, extendsMatrix)){
								
									if (buke.isEmpty()) {
										buke.add(key);
									} else {
										if (!buke.contains(key)) {
											buke.add(key);
										}
								}
								}
								//没有调用父类和重写可以进行社区划分
								if(!CMN.getisoverride(key,extendsMatrix)&&!CMN.getissuperdot(key, fa, extendsMatrix)){
									if (duLi.isEmpty()) {
										duLi.add(key);
									} else {
										if (!duLi.contains(key)) {
											duLi.add(key);
										}
									}
								}
								
								//找出only调用和重写
								 for(int bv = 0; bv < levels.get(i).get(j-1).get(indup).cns.size(); bv++){
									 if(bv != dx&&!levels.get(i).get(j-1).get(indup).cns.get(bv).isEmpty()){
										 if(getisoverride1(key,levels.get(i).get(j-1).get(indup).cns, bv )|| getissuperdot1(fa,key,levels.get(i).get(j-1).get(indup).cns,bv )){
											// cns1.get(bv).add(classesArrList.get(vd).FeatureList.get(bb).name);
											 if (cns1.get(bv).isEmpty()) {
												 cns1.get(bv).add(key);
												} else {
													if (!cns1.get(bv).contains(key)) {
														cns1.get(bv).add(key);
													}
												}
										 }
									 }
								 }
								 
								if (methodlist.isEmpty()) {
									methodlist.add(key);
								} else {
									if (!methodlist.contains(key)) {
									methodlist.add(key);
									}
								}

			}
			}
			

			 if(cns1.size()>1){
			 for(int cc=1;cc<cns1.size();cc++){
				 for(int bb=cc+1;bb<cns1.size();bb++){
					 ArrayList <String> in = tool.testIntersect((ArrayList <String>)cns1.get(cc).clone(), (ArrayList <String>)cns1.get(bb).clone());
			 if(!in.isEmpty()){
				 for(int c=1;c<cns1.size();c++){
				 cns1.get(c).removeAll(in);
				 }
			 }
				 }
			 }
			 }

			 
			 for(int h =0; h < cns1.size(); h++){
			 buke.removeAll(cns1.get(h));
			 }

			 cns1.set(dx, buke);
		 
			 for(int vv = 0 ; vv < cns1.size(); vv++){
				 if(!cns1.get(vv).isEmpty()){
					 methodbukefen.add(cns1.get(vv));
				 }
			 }
			
			int n = methodlist.size();    
			
			double[][] A = new double[n][n];
			A = MatrixComputing.makezeroMatrix(A);
			double[][] xshare = new double[n][n];
			xshare = MatrixComputing.makezeroMatrix(xshare);
			double[][] ycall = new double[n][n];
			ycall = MatrixComputing.makezeroMatrix(ycall); 
			double[][] zexe = new double[n][n];
			zexe = MatrixComputing.makezeroMatrix(zexe); 
			double[][] wsem = new double[n][n];
			wsem = MatrixComputing.makezeroMatrix(wsem);
			
			double a = NonInheritanceRefactoring.a;
			double b = NonInheritanceRefactoring.b;
			double c = NonInheritanceRefactoring.c;
			double d = NonInheritanceRefactoring.d;
			
			if(n>0){
			if(a > 0){
				xshare = NonInheritanceRefactoring.makeshareattributmatrix(methodlist);
			}
			if(b > 0){
				ycall = NonInheritanceRefactoring.makecallingmethodmatrix(methodlist);
			}
			if(c > 0){
				zexe = NonInheritanceRefactoring.makeexecutematrix(methodlist);
			}

			if(d > 0){
				//语义相似度矩阵
				wsem = semantic.makesemanticmatrix(methodlist);
				wsem = semantic.filterSemanticIher(wsem);
				
			}
			}
            A = MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixDotMultiply(2,MatrixComputing.MatrixDotMultiply(a, xshare)),MatrixComputing.MatrixDotMultiply(b, ycall)),MatrixComputing.MatrixDotMultiply(c, zexe)),MatrixComputing.MatrixDotMultiply(0, MatrixComputing.MatrixDotMultiply(d, wsem)));
			
            
            returnvalue rv = CMN.CommunityDetectionNoextentsFuleifenjie(A,  methodlist, methodbukefen, duLi);//重构后类的结构
            ArrayList<ArrayList<String>>  cns2 = rv.cns;

			levels.get(i).get(j).get(k).cns = dealthecns1(cns1, cns2, methodlistAll,dx); //dx 是主继承树
			levels.get(i).get(j).get(k).DeltaQ = rv.MQ;		
//			tool.writeByFileWrite(log1,"父类被分解，处理后被分解结果  methodlistAll=="+methodlistAll.size()+"  主继承树==="+levels.get(i).get(j).get(k).mainidx +"\n");
			CMN.printCNS1(log1, levels.get(i).get(j).get(k).cns);
		
			return levels;
	}
	
	
	
public static ArrayList<ArrayList<ArrayList<extend>>> fuleibeifenjie11(String log1, ArrayList<ArrayList<ArrayList<extend>>> levels, int i, int j, int k,int indup, int[][] extendsMatrix)throws IOException, SAXException, MWException{
		
		int dx = levels.get(i).get(j-1).get(indup).mainidx;
		levels.get(i).get(j).get(k).mainidx = dx;
		 
		ArrayList< ArrayList<String> > cns1 = new ArrayList< ArrayList<String> >();
		for(int bv = 0 ; bv < levels.get(i).get(j-1).get(indup).cns.size(); bv++){
//			 tool.writeByFileWrite(log1,"其父类分结果！！！！！！！！！！！！第"+bv+"个=="+"\n");
			ArrayList<String> subnewArrayList = new ArrayList<String>();
			cns1.add(subnewArrayList);
			
			for(int av = 0 ; av < levels.get(i).get(j-1).get(indup).cns.get(bv).size(); av++){
//				tool.writeByFileWrite(log1,levels.get(i).get(j-1).get(indup).cns.get(bv).get(av)+"\n");
			}
			
			
		 }
		
		 ArrayList<ArrayList<String>> methodbukefen = new ArrayList<ArrayList<String>>();
		 ArrayList<String> duLi= new ArrayList<String>();
		 ArrayList<String> buke= new ArrayList<String>();
		 ArrayList<String> methodlist = new ArrayList<String>();
		 ArrayList<String> methodlistAll = new ArrayList<String>();
		 String classname = SrcAction.classname.get(levels.get(i).get(j).get(k).matrixInd);
		 ClassObject co = SrcAction.classesMap.get(classname);
			if(!co.isLeaf){
				
				 for(Map.Entry<String, Feature> entry1 : co.featureMap.entrySet()) {
				       String key1 = entry1.getKey();  
				       Feature value1 = entry1.getValue();
				if (key1 != null) {
				
						if ( methodlistAll.isEmpty()) {
							methodlistAll.add(key1);
						} else {
							if (!methodlistAll.contains(key1)) {
								methodlistAll.add(key1);
							}
						}
				
							//找出全部调用父类的函数和重写父类的函数
							if(CMN.getisoverride(key1,extendsMatrix)||CMN.getissuperdot(key1, value1, extendsMatrix)){
							
								if (buke.isEmpty()) {
									buke.add(key1);
								} else {
									if (!buke.contains(key1)) {
										buke.add(key1);
									}
							}
							}
							//没有调用父类和重写可以进行社区划分
							if(!CMN.getisoverride(key1,extendsMatrix)&&!CMN.getissuperdot(key1, value1, extendsMatrix)){
								if (duLi.isEmpty()) {
									duLi.add(key1);
								} else {
									if (!duLi.contains(key1)) {
										duLi.add(key1);
									}
								}
							}
							
							//找出only调用和重写
							 for(int bv = 0; bv < levels.get(i).get(j-1).get(indup).cns.size(); bv++){
								 if(bv != dx&&!levels.get(i).get(j-1).get(indup).cns.get(bv).isEmpty()){
									 if(getisoverride1(key1,levels.get(i).get(j-1).get(indup).cns, bv )|| getissuperdot1(value1,key1,levels.get(i).get(j-1).get(indup).cns,bv )){
										 cns1.get(bv).add(key1);
									 }
								 }
							 }

							if (methodlist.isEmpty()) {
								methodlist.add(key1);
							} else {
								if (!methodlist.contains(key1)) {
								methodlist.add(key1);
								}
							}
					
			}
	}
			}else{ //叶子
				
				for (int m = 0; m < co.Refactor.size(); m++) {
					ClassObject cObject = SrcAction.classesMap.get(SourceParser.Getonlyclassname(co.Refactor.get(m)));
					Feature fa = cObject.featureMap.get(co.Refactor.get(m));
					       String key = co.Refactor.get(m);  
							
							if ( methodlistAll.isEmpty()) {
								methodlistAll.add(key);
							} else {
								if (!methodlistAll.contains(key)) {
									methodlistAll.add(key);
								}
							}
					
								//找出全部调用父类的函数和重写父类的函数
								if(CMN.getisoverride(key, extendsMatrix)|| CMN.getissuperdot(key, fa, extendsMatrix)){
								
									if (buke.isEmpty()) {
										buke.add(key);
									} else {
										if (!buke.contains(key)) {
											buke.add(key);
										}
								}
								}
								//没有调用父类和重写可以进行社区划分
								if(!CMN.getisoverride(key,extendsMatrix)&&!CMN.getissuperdot(key, fa, extendsMatrix)){
									if (duLi.isEmpty()) {
										duLi.add(key);
									} else {
										if (!duLi.contains(key)) {
											duLi.add(key);
										}
									}
								}
								
								//找出only调用和重写
								 for(int bv = 0; bv < levels.get(i).get(j-1).get(indup).cns.size(); bv++){
									 if(bv != dx&&!levels.get(i).get(j-1).get(indup).cns.get(bv).isEmpty()){
										 if(getisoverride1(key,levels.get(i).get(j-1).get(indup).cns, bv )|| getissuperdot1(fa,key,levels.get(i).get(j-1).get(indup).cns,bv )){
											// cns1.get(bv).add(classesArrList.get(vd).FeatureList.get(bb).name);
											 if (cns1.get(bv).isEmpty()) {
												 cns1.get(bv).add(key);
												} else {
													if (!cns1.get(bv).contains(key)) {
														cns1.get(bv).add(key);
													}
												}
										 }
									 }
								 }
								 
								if (methodlist.isEmpty()) {
									methodlist.add(key);
								} else {
									if (!methodlist.contains(key)) {
									methodlist.add(key);
									}
								}

			}
			}
			

			 if(cns1.size()>1){
			 for(int cc=1;cc<cns1.size();cc++){
				 for(int bb=cc+1;bb<cns1.size();bb++){
					 ArrayList <String> in = tool.testIntersect((ArrayList <String>)cns1.get(cc).clone(), (ArrayList <String>)cns1.get(bb).clone());
			 if(!in.isEmpty()){
				 for(int c=1;c<cns1.size();c++){
				 cns1.get(c).removeAll(in);
				 }
			 }
				 }
			 }
			 }

			 
			 for(int h =0; h < cns1.size(); h++){
			 buke.removeAll(cns1.get(h));
			 }

			 cns1.set(dx, buke);
		 
			 for(int vv = 0 ; vv < cns1.size(); vv++){
				 if(!cns1.get(vv).isEmpty()){
					 methodbukefen.add(cns1.get(vv));
				 }
			 }
			
			int n = methodlist.size();    
			
			double[][] A = new double[n][n];
			A = MatrixComputing.makezeroMatrix(A);
			double[][] xshare = new double[n][n];
			xshare = MatrixComputing.makezeroMatrix(xshare);
			double[][] ycall = new double[n][n];
			ycall = MatrixComputing.makezeroMatrix(ycall); 
			double[][] zexe = new double[n][n];
			zexe = MatrixComputing.makezeroMatrix(zexe); 
			double[][] wsem = new double[n][n];
			wsem = MatrixComputing.makezeroMatrix(wsem);
			
			double a = NonInheritanceRefactoring.a;
			double b = NonInheritanceRefactoring.b;
			double c = NonInheritanceRefactoring.c;
			double d = NonInheritanceRefactoring.d;
			
			if(n>0){
			if(a > 0){
				xshare = NonInheritanceRefactoring.makeshareattributmatrix1(methodlist);
			}
			if(b > 0){
				ycall = NonInheritanceRefactoring.makecallingmethodmatrix1(methodlist);
			}
			if(c > 0){
				zexe = NonInheritanceRefactoring.makeexecutematrix(methodlist);
			}

			if(d > 0){
				//语义相似度矩阵
				wsem = semantic.makesemanticmatrix(methodlist);
				wsem = semantic.filterSemanticIher(wsem);
				
			}
			}
            A = MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixDotMultiply(1,MatrixComputing.MatrixDotMultiply(a, xshare)),MatrixComputing.MatrixDotMultiply(1,MatrixComputing.MatrixDotMultiply(b, ycall))),MatrixComputing.MatrixDotMultiply(c, zexe)),MatrixComputing.MatrixDotMultiply(0, MatrixComputing.MatrixDotMultiply(d, wsem)));
			
            
            returnvalue rv = CMN.CommunityDetectionNoextentsFuleifenjie(A,  methodlist, methodbukefen, duLi);//重构后类的结构
            ArrayList<ArrayList<String>>  cns2 = rv.cns;
//            levels.get(i).get(j).get(k).cns = cns2 ;
            if(cns2.size()>1){
			levels.get(i).get(j).get(k).cns = dealthecns111(cns1, cns2, methodlistAll,dx); //dx 是主继承树
            }
			levels.get(i).get(j).get(k).DeltaQ = rv.MQ;		
//			tool.writeByFileWrite(log1,"父类被分解，处理后被分解结果  methodlistAll=="+methodlistAll.size()+"  主继承树==="+levels.get(i).get(j).get(k).mainidx +"\n");
			CMN.printCNS1(log1, levels.get(i).get(j).get(k).cns);
		
			return levels;
	}
	
	public static ArrayList<SplitTrees>  TidyResults(ArrayList<ArrayList<ArrayList<extend>>> levels) throws IOException{
		String log1 = "D:\\Results.txt";
//		tool.writeByFileWrite(log1,"共"+levels.size()+"棵继承树==="+"\n");
		
		ArrayList<SplitTrees> SplitTree = new ArrayList<SplitTrees>();//被分解的类的索引
		
		 for(int i = 0; i < levels.size(); i++){ //遍历每棵继承树
			SplitTrees sTrees = new SplitTrees();
			int move = 0;
//			tool.writeByFileWrite(log1,"第"+i+"棵继承树==="+"\n");
			boolean flag = false;
			int max = 0;
			 for(int j = 0; j < levels.get(i).size(); j++){
//				 tool.writeByFileWrite(log1,"共"+levels.get(i).size()+"层==="+"\n");
			
	    		 for(int k =0; k <levels.get(i).get(j).size();k++){
	    			 int cnt = 0;
	    			 for(int p = 0 ; p < levels.get(i).get(j).get(k).cns.size();p++){
	    				 if(!levels.get(i).get(j).get(k).cns.get(p).isEmpty()){
	    					 cnt++;
//	    					 if(p>0){
//	    						 move = move + levels.get(i).get(j).get(k).cns.get(p).size();
//	    					 }
	    				 }
	    			 }
	    			 if(cnt>1){
	    			move = move +cnt-1;
	    			GenerateRefactoringSuggestions.zengjialei = GenerateRefactoringSuggestions.zengjialei + cnt - 1;
	    			flag = true;	    		     
	    			if(levels.get(i).get(j).get(k).cns.size()>max){
	    		    	 max = levels.get(i).get(j).get(k).cns.size(); 
	    		     }
	    		     levels.get(i).get(j).get(k).split = true;
//			         tool.writeByFileWrite(log1,"第"+j+"层，"+"第"+k+"个节点==="+SrcAction.classname.get(levels.get(i).get(j).get(k).matrixInd)+"被分解成"+cnt+"个类"+"\n");
	    			 }
				    		
			}
	    		 
			 }
			 
			 if(flag){
				 sTrees.TreeIndex = i;
				 sTrees.TreeNum = max;
				 sTrees.move = move;
				 SplitTree.add(sTrees);
			 }
			 
		 }
		 
//		 tool.writeByFileWrite(log1,"共有"+SplitTree.size()+"棵树被分解"+"\n");
		 for(int i = 0; i < SplitTree.size(); i++){
//			 tool.writeByFileWrite(log1,"第"+SplitTree.get(i).TreeIndex+"棵树被分解"+SplitTree.get(i).TreeNum+"棵树"+"\n");
		 }
		
		 for(int i = 0; i < SplitTree.size(); i++){ //遍历每棵继承树
			
			 for(int j = 0; j < levels.get(SplitTree.get(i).TreeIndex).size(); j++){
				 ArrayList<ArrayList<ArrayList<extend>>> levelSplit = new ArrayList<ArrayList<ArrayList<extend>>>();
				 for(int k = 0; k < SplitTree.get(i).TreeNum; k++){
					ArrayList<ArrayList<extend>> level = new ArrayList<ArrayList<extend>>();
					 
					 for(int p =0; p <levels.get(SplitTree.get(i).TreeIndex).size();p++){
						 ArrayList<extend> ceng = new ArrayList<extend>();
						 for(int q =0; q <levels.get(SplitTree.get(i).TreeIndex).get(p).size();q++){
							
							 
							 if(levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).cns.size()>=(k+1)){
								
								 if(!levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).cns.get(k).isEmpty()){
									 int cc = 0;
									 for(int h = 0; h <= k; h++){
										 if(!levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).cns.get(h).isEmpty()){
											 cc++;
										 }
									 }
									 
									 extend ed = new extend();
//									 ArrayList<String> SubClassName = new  ArrayList<String>();//子类的类名
//									 ArrayList<String> SuperClassName = new  ArrayList<String>();  //父类的类名
									 ed.cns.clear();
									 ArrayList<String> cn = new ArrayList<String>();
									 cn.addAll(levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).cns.get(k));
									 
									 if(levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).split){
										 ed.TreenodeName = levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).TreenodeName+ "_new_"+cc;
//										 SuperClassName = levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).
										 if(k==0){
											 for(int t = 0; t < levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).cns.size(); t++){
												if(t!=k && !levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).cns.get(t).isEmpty()){
													
													 int cf = 0;
													 for(int h = 0; h <= t; h++){
														 if(!levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).cns.get(h).isEmpty()){
															 cf++;
														 }
													 }
													
													ed.OutdependencyClassName.add((levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).TreenodeName + "_new_"+cf));
													cn.add((levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).TreenodeName) + "_new_"+cf+"_Instance");
												}
											 }
										 }else{
											
											ed.IndependencyClassName.add((levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).TreenodeName + "_new_"+1));
												
										 }
										 
									 }else{
										 ed.TreenodeName = levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).TreenodeName;
										 ed.interfaceornot = levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).interfaceornot;
									 }
									 ed.cns.add(cn);
									 ed.DrawlevelNo = p;
									 ed.split = levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).split;
													
								     for(int g = 0; g <levels.get(SplitTree.get(i).TreeIndex).size();g++){
										 for(int u = 0; u <levels.get(SplitTree.get(i).TreeIndex).get(g).size();u++){
											 if(levels.get(SplitTree.get(i).TreeIndex).get(g).get(u).SubClassName.contains(levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).TreenodeName)){
												 if(levels.get(SplitTree.get(i).TreeIndex).get(g).get(u).cns.size()>=(k+1)){
													 
													 if(!levels.get(SplitTree.get(i).TreeIndex).get(g).get(u).cns.get(k).isEmpty()){
		 
														 if(levels.get(SplitTree.get(i).TreeIndex).get(g).get(u).split){
															 int cf = 0;
															 for(int h = 0; h <= k; h++){
																 if(!levels.get(SplitTree.get(i).TreeIndex).get(g).get(u).cns.get(h).isEmpty()){
																	 cf++;
																 }
															 }
															 ed.SuperClassName.add((levels.get(SplitTree.get(i).TreeIndex).get(g).get(u).TreenodeName + "_new_"+cf));
														 }else{
															 ed.SuperClassName.add(levels.get(SplitTree.get(i).TreeIndex).get(g).get(u).TreenodeName);
														 }
													 }
													 
												 }
											 }
										 }
								     }
									 
								   for(int g = 0; g <levels.get(SplitTree.get(i).TreeIndex).size();g++){
									 for(int u = 0; u <levels.get(SplitTree.get(i).TreeIndex).get(g).size();u++){
										 if(levels.get(SplitTree.get(i).TreeIndex).get(g).get(u).SuperClassName.contains(levels.get(SplitTree.get(i).TreeIndex).get(p).get(q).TreenodeName)){
											 if(levels.get(SplitTree.get(i).TreeIndex).get(g).get(u).cns.size()>=(k+1)){
												 
												 if(!levels.get(SplitTree.get(i).TreeIndex).get(g).get(u).cns.get(k).isEmpty()){
													 if(levels.get(SplitTree.get(i).TreeIndex).get(g).get(u).split){
														 int cf = 0;
														 for(int h = 0; h <= k; h++){
															 if(!levels.get(SplitTree.get(i).TreeIndex).get(g).get(u).cns.get(h).isEmpty()){
																 cf++;
															 }
														 }
														 
														 ed.SubClassName.add((levels.get(SplitTree.get(i).TreeIndex).get(g).get(u).TreenodeName) + "_new_"+cf);
													 }else{
														 ed.SubClassName.add(levels.get(SplitTree.get(i).TreeIndex).get(g).get(u).TreenodeName);
													 }
												 }
												 
											 }
										 }
									 }	  
									 
								 }
								 
								 if(ed.interfaceornot){
									ed.color = NodeColor.RED;
								 }
								 if(ed.split && !(ed.SubClassName.size()==0 && ed.SuperClassName.size()==0)){
									 ed.color = NodeColor.BLUE;
								 }
								 if(!ed.split && !ed.interfaceornot){
									 ed.color = NodeColor.GREEN;
								 }
								 if(ed.split && (ed.SubClassName.size()==0 && ed.SuperClassName.size()==0)){
									 ed.color = NodeColor.YELLOW;
								 }
								 ceng.add(ed);
							 } // if(!levels.get(TreeIndex.get(i)).get(p).get(q).cns.get(k).isEmpty()){
							 }
						 }
						 if(!ceng.isEmpty()){
						 level.add(ceng);
						 }
					 }
					 
					 if(!level.isEmpty()){
						 levelSplit.add(level);
						 }
				 }
				 SplitTree.get(i).levelSplit = levelSplit;
			 }
			 
		 }
		 
		 
		 for(int i = 0; i < SplitTree.size(); i++){
//			 tool.writeByFileWrite(log1,"第"+SplitTree.get(i).TreeIndex+"棵树被分解-----"+"\n");
			 ArrayList<ArrayList<ArrayList<extend>>> levelSplit = SplitTree.get(i).levelSplit;
			 for(int j = 0; j < levelSplit.size(); j++){
//				 tool.writeByFileWrite(log1,"分解成第第"+j+"棵树!!!!!!!!!!!!!!!!!!!"+"\n");
				 for(int k = 0; k < levelSplit.get(j).size(); k++){
//					 tool.writeByFileWrite(log1,"第"+k+"层-----"+"\n");
					 for(int t = 0; t < levelSplit.get(j).get(k).size(); t++){
//						 tool.writeByFileWrite(log1,"第"+t+"个类-----"+levelSplit.get(j).get(k).get(t).TreenodeName+"\n");
//						 tool.writeByFileWrite(log1,"IndependencyClassName-----"+levelSplit.get(j).get(k).get(t).IndependencyClassName+"\n");
//						 tool.writeByFileWrite(log1,"OutdependencyClassName-----"+levelSplit.get(j).get(k).get(t).OutdependencyClassName+"\n");
//						 tool.writeByFileWrite(log1,"SubClassName-----"+levelSplit.get(j).get(k).get(t).SubClassName+"\n");
//						 tool.writeByFileWrite(log1,"SuperClassName-----"+levelSplit.get(j).get(k).get(t).SuperClassName+"\n");
//						 tool.writeByFileWrite(log1,"DrawlevelNo-----"+levelSplit.get(j).get(k).get(t).DrawlevelNo+"   k层"+k+"\n");
//						 for(int q = 0; q < levelSplit.get(j).get(k).get(t).cns.get(0).size(); q++){
//							 tool.writeByFileWrite(log1,levelSplit.get(j).get(k).get(t).cns.get(0).get(q)+"\n");
//						 }
					 }
				 }
			 }
		 }
		 
		return SplitTree;
		 
		 
	}
	
	
	public static ArrayList<SplitTrees> CalculatInheritanceTreesQ(ArrayList<SplitTrees> SplitTree,ArrayList<ArrayList<ArrayList<extend>>> levelsRefactor){
		for(int k = 0; k < SplitTree.size(); k++){
			int id = SplitTree.get(k).TreeIndex;
			ArrayList<ArrayList<extend>> tree = levelsRefactor.get(id);
			double deltaQ = 0;
//			int move = 0;
			for(int i = 0; i < tree.size(); i++){
				for(int j = 0; j < tree.get(i).size(); j++){
					if(tree.get(i).get(j).DeltaQ>0){
					if(tree.get(i).get(j).DeltaQ>0.1){
					tree.get(i).get(j).DeltaQ = tree.get(i).get(j).DeltaQ/(double)10;
					deltaQ = deltaQ + tree.get(i).get(j).DeltaQ;
//					move = move  + tree.get(i).get(j).move;
					}
					}else{
						tree.get(i).get(j).DeltaQ = Math.abs(tree.get(i).get(j).DeltaQ);
						if(tree.get(i).get(j).DeltaQ > 1){
							tree.get(i).get(j).DeltaQ = tree.get(i).get(j).DeltaQ/(double)100;
						}
						if(tree.get(i).get(j).DeltaQ > 0.1){
							tree.get(i).get(j).DeltaQ = tree.get(i).get(j).DeltaQ/(double)10;
						}
						if(tree.get(i).get(j).DeltaQ > 10){
							tree.get(i).get(j).DeltaQ = tree.get(i).get(j).DeltaQ/(double)1000;
						}
						deltaQ = deltaQ + tree.get(i).get(j).DeltaQ;
//						move = move + tree.get(i).get(j).move;
					}
				}
			}
			SplitTree.get(k).detaQ = deltaQ;
//			SplitTree.get(k).move = move;
//			System.out.println("SplitTree.get(k).detaQ=="+SplitTree.get(k).detaQ);
		}
		
		SplitTree = semantic.ReverbSplitTreessort(SplitTree);
		return SplitTree;
	}
	
	public static void CalculatInheritanceTreesDetaQ(int undotree){
		
		Ymax = 0;
		Ymin = 0;
		Xtotal = 0;
		Ycurrent = 0;
		Xcurrent =0;
		
		RefactorInheritance.Q_Orig = new double[RefactorInheritance.rv.SplitTree.size()+1];
		RefactorInheritance.Q_Undo = new double[RefactorInheritance.rv.SplitTree.size()+1-undotree];
		RefactorInheritance.Q_Orig[0] = NonInheritanceRefactoring.YCurrent;
		RefactorInheritance.Q_Undo[0] = NonInheritanceRefactoring.YCurrent;
		
		int id = 1;
		for(int p = 0; p < RefactorInheritance.rv.SplitTree.size(); p++){
			RefactorInheritance.Q_Orig[id] = RefactorInheritance.Q_Orig[id-1] + RefactorInheritance.rv.SplitTree.get(p).detaQ;
			Xtotal = Xtotal + RefactorInheritance.rv.SplitTree.get(p).move;
			System.out.println("RefactorInheritance.rv.SplitTree.get(p).move=="+RefactorInheritance.rv.SplitTree.get(p).move);
			if(p < (rv.SplitTree.size()-undotree)){
				RefactorInheritance.Q_Undo[id] = RefactorInheritance.Q_Undo[id-1] + RefactorInheritance.rv.SplitTree.get(p).detaQ;
				//Ycurrent = RefactorInheritance.Q_Undo[id-1] + RefactorInheritance.rv.SplitTree.get(p).detaQ;
				Xcurrent = Xcurrent + RefactorInheritance.rv.SplitTree.get(p).move;
			}
			id++;
		}
		Ycurrent = RefactorInheritance.Q_Undo[RefactorInheritance.Q_Undo.length-1];
		Ymax = RefactorInheritance.Q_Orig[RefactorInheritance.Q_Orig.length -1];
		Ymin = RefactorInheritance.Q_Orig[0];
		
		for(int y = 0 ; y < RefactorInheritance.Q_Orig.length; y++){
			
			System.out.println("Q_Orig[y]=="+RefactorInheritance.Q_Orig[y]);
		}
		
		for(int y = 0 ; y < RefactorInheritance.Q_Undo.length; y++){
			
			System.out.println("Q_Undo[y]=="+RefactorInheritance.Q_Undo[y]);
		}
		
		System.out.println("调用Ytotal=="+Ymax+"  tool--Ymin=="+RefactorInheritance.Ymin+"   Xtotal=="+Xtotal+"  Ycurrent=="+Ycurrent+"  Xcurrent=="+Xcurrent);
		
	}
	
	
	
	
	public static void InheritanceTreesOperations(ProgressBarLisenter barLisenter) throws IOException, SAXException, MWException{
//		 SaveFileAction.classname = change.FilterClasses();
		 String log1 = "D:\\Tree.txt";
//		 semantic.findmethodlines(SaveFileAction.sourcepath);
//		 semantic.extractword(SaveFileAction.sourcepath);
//		 int[][] extendsMatrix = preprocessing.printextendsandimplements();
		 levels =  getInheritanceTreelevels(preprocessing.extendsMatrix.clone());
		 levels =  getinterfaces(levels);
		 levels =  getTreesBeforeRefactoring(levels);//重构前的继承树结构
//		 System.out.println("levels.size()=="+ levels.size());
		 ArrayList<ArrayList<ArrayList<extend>>> levelsRefactor = (ArrayList<ArrayList<ArrayList<extend>>>)levels.clone(); 
		 RefactorInheritance.TempIndexChechBox();//与学弟对完接口删除
		 RefactorInheritance.persent = 0;
		 levelsRefactor = RefactorEachLevel(log1,levelsRefactor,preprocessing.extendsMatrix.clone(),barLisenter);
		 
		 ArrayList<SplitTrees> SplitTree = TidyResults(levelsRefactor);//需要被分解的树 信息
		 SplitTree = CalculatInheritanceTreesQ(SplitTree,levelsRefactor);
		 RefactorInheritance.rv.levelsRefactor = levelsRefactor;
		 RefactorInheritance.rv.SplitTree = SplitTree;
		 tool.GetTreelog(0);

		 
//		 CallingMain.UndoTreeRefactoringOperations(1);
//		 CallingMain.UndoTreeRefactoringOperations(0);
	}
	
}
