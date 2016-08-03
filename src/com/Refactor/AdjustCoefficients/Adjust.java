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

package com.Refactor.AdjustCoefficients;




import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import com.Refactor.NonInheritance.CMN;
import com.Refactor.NonInheritance.GenerateRefactoringSuggestions;
import com.Refactor.NonInheritance.Main;
import com.Refactor.NonInheritance.MatrixComputing;
import com.Refactor.NonInheritance.MergeMethodNonInheri;
import com.Refactor.NonInheritance.NonInheritanceRefactoring;
import com.Refactor.NonInheritance.change;
import com.Refactor.NonInheritance.preprocessing;
import com.Refactor.NonInheritance.semantic;
import com.Refactor.NonInheritance.tool;
import com.Refactor.classparser.ClassObject;
import com.Refactor.classparser.Feature;
import com.Refactor.classparser.SourceParser;
import com.jeantessier.dependencyfinder.gui.SrcAction;
import com.mathworks.toolbox.javabuilder.MWException;
import com.neu.utils.listener.ProgressBarLisenter;

public class Adjust {
	
	public static double [][] LCOMS ;//3行 1：原始  2：人工 3：重构后
	static double [][] CONECT ;
	static double [][] CBOS ;
	public static double [][] MPCS ;
	public static int Threshold;
	
	public static double pingjunzhi;
	public static double zhongzhi;
	public static double fangcha;
	
	public static void SetAdjustThreshold(int a){
		Threshold = a;
	}
	
	public static ClassObject CBO_MPC(ClassObject clas, String cname) {

		String ss = SourceParser.getonlyclassname(cname);
		ArrayList<String> CBO = new ArrayList<String>();
		ArrayList<String> MPC = new ArrayList<String>();

		for (int e = 0; e < clas.outboundClassList.size(); e++) {
			// 如果包含（） 即是函数的属性 !!切记还有另一种情况!
			if (clas.outboundClassList.get(e) != null) {
				String st =  SourceParser.getonlyclassname(clas.outboundClassList.get(e));

				if (CBO.isEmpty()) {
					if (SrcAction.classname.contains(st) && !ss.equals(st)) {
						CBO.add(st);
					}
				} else {
					if (!CBO.contains(st) && SrcAction.classname.contains(st)&& !ss.equals(st)) {
						CBO.add(st);
					}
				}
			}
		}

		for (int e = 0; e < clas.inboundClassList.size(); e++) {
			// 如果包含（） 即是函数的属性 !!切记还有另一种情况!
			if (clas.inboundClassList.get(e) != null) {
				String st =  SourceParser.getonlyclassname(clas.inboundClassList.get(e));

				if (CBO.isEmpty()) {
					if (SrcAction.classname.contains(st) && !ss.equals(st)) {
						CBO.add(st);
					}
				} else {
					if (!CBO.contains(st) && SrcAction.classname.contains(st)&& !ss.equals(st)) {
						CBO.add(st);
					}
				}
			}
		}

		// 处 理class.feoutbound
		for (int e = 0; e < clas.outboundFeatureList.size(); e++) {
			// 如果包含（） 即是函数的属性 !!切记还有另一种情况!
			if (clas.outboundFeatureList.get(e) != null) {

				String st =  SourceParser.Getonlyclassname(clas.outboundFeatureList.get(e));
				if (CBO.isEmpty()) {
					if (SrcAction.classname.contains(st) && !ss.equals(st)) {
						CBO.add(st);
					}
				} else {
					if (!CBO.contains(st) && SrcAction.classname.contains(st)&& !ss.equals(st)) {
						CBO.add(st);
					}
				}

			}
		}

		// 处 理class.feinbound
		for (int e = 0; e < clas.inboundFeatureList.size(); e++) {
			// 如果包含（） 即是函数的属性 !!切记还有另一种情况!
			if (clas.inboundFeatureList.get(e) != null) {
				String st = SourceParser.Getonlyclassname(clas.inboundFeatureList.get(e));

				if (CBO.isEmpty()) {
					if (SrcAction.classname.contains(st) && !ss.equals(st)) {
						CBO.add(st);
					}
				} else {
					if (!CBO.contains(st) && SrcAction.classname.contains(st)&& !ss.equals(st)) {
						CBO.add(st);
					}
				}

			}
		}

		
		for(Map.Entry<String, Feature> entry : clas.featureMap.entrySet()) {  
		       String key = entry.getKey(); 
		       Feature value = entry.getValue();
			// 处理class.feature.outbound
			for (int s = 0; s < value.outboundClassList.size(); s++) {
				if (value.outboundClassList.get(s) != null) {
					String st = SourceParser.getonlyclassname(value.outboundClassList.get(s));

					if (CBO.isEmpty()) {
						if (SrcAction.classname.contains(st) && !ss.equals(st)) {
							CBO.add(st);
						}
					} else {
						if (!CBO.contains(st) && SrcAction.classname.contains(st)&& !ss.equals(st)) {
							CBO.add(st);
						}
					}

				}
			}
			// 处理class.feature.feoutbound
			for (int f = 0; f < value.outboundFeatureList.size(); f++) {
				if (value.outboundFeatureList.get(f) != null) {
					String st = SourceParser.Getonlyclassname(value.outboundFeatureList.get(f));
					if (CBO.isEmpty()) {
						if (SrcAction.classname.contains(st) && !ss.equals(st)) {
							CBO.add(st);
						}
					} else {
						if (!CBO.contains(st) && SrcAction.classname.contains(st)&& !ss.equals(st)) {
							CBO.add(st);
						}
					}

				//	if (!value.outboundFeatureList.get(f).contains("$")) {
						if (MPC.isEmpty()) {
							if (SrcAction.classname.contains(st) && !ss.equals(st)) {
								MPC.add(value.outboundFeatureList.get(f));
							}
						} else {
							if (!MPC.contains(value.outboundFeatureList.get(f)) && SrcAction.classname.contains(st) && !ss.equals(st)) {
								MPC.add(value.outboundFeatureList.get(f));
							}
						}

				//	}

				}
			}
			for (int s = 0; s < value.inboundClassList.size(); s++) {
				if (value.inboundClassList.get(s) != null) {
					String st = SourceParser.getonlyclassname(value.inboundClassList.get(s));
					if (CBO.isEmpty()) {
						if (SrcAction.classname.contains(st) && !ss.equals(st)) {
							CBO.add(st);
						}
					} else {
						if (!CBO.contains(st) && SrcAction.classname.contains(st)&& !ss.equals(st)) {
							CBO.add(st);
						}
					}
				}
			}
			// 处理class.feature.feoutbound
			for (int f = 0; f < value.inboundFeatureList
					.size(); f++) {
				if (value.inboundFeatureList.get(f) != null) {
					String st = SourceParser.Getonlyclassname(value.inboundFeatureList.get(f));
					if (CBO.isEmpty()) {
						if (SrcAction.classname.contains(st) && !ss.equals(st)) {
							CBO.add(st);
						}
					} else {
						if (!CBO.contains(st) && SrcAction.classname.contains(st)
								&& !ss.equals(st)) {
							CBO.add(st);
						}
					}
				}
			}

		}

		clas.CBO = CBO.size();
		clas.MPC = MPC.size();
		return clas;
	}
	
	public static ClassObject Connectivity_Lcom(ClassObject clas) {
		// 求内聚性 LCOM/connectivity
		ArrayList<String> methodlist = new ArrayList<String>();
		
		for(Map.Entry<String, Feature> entry : clas.featureMap.entrySet()) {  
		       String key = entry.getKey(); 
		       Feature value = entry.getValue();
				if (key != null) {
					if (!value.dead) {
						methodlist.add(key);
					}
				}
		}
	
		int n = methodlist.size();
		double[][] x = new double[n][n];
		double[][] y = new double[n][n];
		x = MatrixComputing.makezeroMatrix(x);
		x = MatrixComputing.makezeroMatrix(y);
		
		for (int i = 0; i < n; i++) {
  
					List<String> OutboundFeatureList1 = clas.featureMap.get(methodlist.get(i)).outboundFeatureList;

					for (int j = 0; j < n; j++) {
						if (i != j) {
							for (int m = 0; m < OutboundFeatureList1.size(); m++) {
								if (OutboundFeatureList1.get(m).equals(methodlist.get(j))) {
									x[i][j] = 1;
								}
							}

						}
					}

					ArrayList<String> outboundAttributeList1 = new ArrayList<String>();
					if (methodlist.get(i) != null) {
						if (methodlist.get(i).contains("(")|| methodlist.get(i).contains("{")) {

								outboundAttributeList1 = clas.featureMap.get(methodlist.get(i)).outboundAttributeList;
				      } else {
					    String uu = null;
					    uu = methodlist.get(i);
					    outboundAttributeList1.add(uu);
				             }
			                                           }

			   if (i + 1 < n) {
			    	for (int j = i + 1; j < n; j++) {

					List<String> outboundAttributeList2 = new ArrayList<String>();
					if (methodlist.get(j) != null) {
						if (methodlist.get(j).contains("(")&& methodlist.get(j).contains(")")|| methodlist.get(j).contains("{")&& methodlist.get(j).contains("}")) {

								outboundAttributeList2 = clas.featureMap.get(methodlist.get(j)).outboundAttributeList;

						} else {
							String uu = null;
							uu = methodlist.get(j);
							outboundAttributeList2.add(uu);
						}
					}
					List<String> Intersect = tool.testIntersect(outboundAttributeList1, outboundAttributeList2);
					if (Intersect.size() != 0) {
						y[i][j] = 1;
					}
				}
			}

		}
		
		if((n * (n - 1))!=0){
		clas.connectivity = (MatrixComputing.summatrix(x) + MatrixComputing.summatrix(y)) / (double)((n * (n - 1)) / 2);
		if(clas.connectivity>1){
			clas.connectivity=1;
		}
		}else{
			clas.connectivity = 0;
		}
		if (((n * (n - 1)) / 2) - (2 * MatrixComputing.summatrix(y)) > 0) {
			clas.LCOM = ((n * (n - 1)) / 2) - (2 * MatrixComputing.summatrix(y));
		} else {
			clas.LCOM = 0;
		}

		return clas;

	}
	
	
	
	public static void GetMetricsValue(){
		
		for(Map.Entry<String, ClassObject> entry : SrcAction.classesMap.entrySet()) {  
		       String key = entry.getKey(); 
		       ClassObject value = entry.getValue();
		       value = Connectivity_Lcom(value);
		       value = CBO_MPC(value, key);
		       SrcAction.classesMap.put(key, value);
		}

	}
	
	
	public static int[][] GetHighCohesionClass(int[][] dependencyMatrix){
		double sumcohesion = 0;
		int cnt = 0;
		for(Map.Entry<String, ClassObject> entry : SrcAction.classesMap.entrySet()) {  
		       String key = entry.getKey(); 
		       ClassObject value = entry.getValue();
		       if(value.connectivity>0){
		    	   sumcohesion = sumcohesion + value.connectivity;
		    	   cnt++;
		       }
		       
		       }
		
		double avg = sumcohesion/(double)cnt;

		for(Map.Entry<String, ClassObject> entry : SrcAction.classesMap.entrySet()) {  
		       String key = entry.getKey(); 
		       ClassObject value = entry.getValue();
		if((value.connectivity < avg)||(value.featureMap.size()<=4)){
			int i =  SrcAction.classname.indexOf(key);
		for (int k = 0; k < dependencyMatrix.length; k++) {

			dependencyMatrix[i][k] = 0;
			dependencyMatrix[k][i] = 0;
		}
		}
		}
		return dependencyMatrix;
	}
	

	
	public static  ArrayList <ArrayList <Integer>>  getcc( int[][] matrix){
		 
		 matrix = MatrixComputing.directedToundirected(matrix);
	      ArrayList <Integer> nodes = new  ArrayList <Integer>();
	      ArrayList <ArrayList <Integer>> cc = new  ArrayList <ArrayList <Integer>>();
	      for(int i = 0; i < matrix.length; i++){
	    	  if(MatrixComputing.getdgree(matrix,i)!=0){
	    		  nodes.add(i);
	    	  }
	      }
	     ArrayList <Integer> result = new  ArrayList <Integer>();
	      while(!nodes.isEmpty()){
	      boolean stop = true;
	      ArrayList <Integer> neighbour = new  ArrayList <Integer> ();
	      neighbour.add(nodes.get(0));
	    ArrayList <Integer> resultone = new  ArrayList <Integer>();
	  
	      resultone .add(nodes.get(0));
	      result.addAll(resultone);
	      while(!neighbour.isEmpty()){
	    	  ArrayList <Integer> NBs = new ArrayList <Integer> ();
	    	  for(int i =0; i <neighbour.size();i++){
	    	   NBs.addAll(MatrixComputing.getNBs(matrix,neighbour.get(i),result));
	    	   
	    	  }
	    	  resultone.addAll(NBs);
	    		  neighbour= NBs;
	      }
	      nodes.removeAll(result);
	      cc.add(resultone);
	      
	         } 
			return cc;
		    }
	
	
	 public static int generiteint(int i, int j){
		 int rm = (int) Math.round(i + Math.random() * (j-i));
		 
		return rm;
	 }
	
	 public static ArrayList <Integer> generiteint(int i, int j, int n){
		 ArrayList <Integer> nodes = new  ArrayList <Integer>();
		 while(nodes.size() < n){
		 int rm = (int) Math.round(i + Math.random() * (j-i));
		 
		 if(!nodes.contains(rm)){
			 nodes.add(rm);
		 }
		 
		 }
		return nodes;
	 }
	
	 
	 
     public static boolean  judgeIfContain(ArrayList <ArrayList <Integer>> selectedid, ArrayList <Integer> UT){
	    boolean flag = false;
	    if(!selectedid.isEmpty()){
	    for(int i = 0; i < selectedid.size();i++){
	    	if(selectedid.get(i).size()==UT.size()){
	    		int same = 0;
	    		for(int k = 0; k < UT.size(); k++){
	    			if(selectedid.get(i).contains(UT.get(k))){
	    				same++;
	    			}
	    		}
	    		if(same==UT.size()){
	    			flag = true;
	    		}
	    	}
	    }
	    }
		return flag;
     }
	 
	
    public static int  getN(ArrayList <ArrayList <Integer>> allss){
    	int max =0;
    	for(int i = 0; i < allss.size(); i++){
    		if(max<allss.get(i).size()){
    			max = allss.get(i).size();
    		}
    	}
		return max;
    }
     
	 
	public static ArrayList< ArrayList<String> >  getselectedresult(int times){
		int[][] extendsMatrix = preprocessing.printextendsandimplements();
		int[][] dependencyMatrix = preprocessing.printdependencyrelasions();
		dependencyMatrix =  MatrixComputing.directedToundirected(dependencyMatrix);
		int[][] remainMatrix = preprocessing.removeInheritanceNodefromDependency(extendsMatrix,dependencyMatrix);
		dependencyMatrix = GetHighCohesionClass(remainMatrix);
		ArrayList <String>  rem = preprocessing.InheritanceNodes(remainMatrix); //低于平均内聚度的列表	
		
		 ArrayList< ArrayList<String> >  selectedresult = new  ArrayList< ArrayList<String> > ();
		 ArrayList< ArrayList<Integer> >  selectedid = new  ArrayList< ArrayList<Integer> > ();
		 int T = 0;
		 
		 ArrayList <ArrayList <Integer>> cc =  getcc(remainMatrix);
		 
		// int max = getN(cc);
		 int max = 10;
	//	 System.out.println("max=="+max);
		 while(T < times){
		//	 int N = 2;//generiteint(2, 3);
			 int N = generiteint(2, max);
			 ArrayList<Integer> UT = new ArrayList<Integer>();//当次合并的节点
			 ArrayList<Integer> P = new ArrayList<Integer>();//可供选择的节点
			 ArrayList<Integer> Hseed = new ArrayList<Integer>();//已经做过种子节点的
			 int ccid = generiteint(0, cc.size()-1);
			 if(cc.get(ccid).size() < N){
			    continue;
			 }
			 if(cc.get(ccid).size() == N){
				 UT.addAll(cc.get(ccid));
				 if( !judgeIfContain(selectedid, UT)){
				 selectedid.add(UT);
				 T++;
				 continue;
				 }else{
					 continue;
				 }
				 
			 }
			 if(cc.get(ccid).size() > N){
			 int Nseed_id =  generiteint(0, cc.get(ccid).size()-1);
			 int Nseed = cc.get(ccid).get(Nseed_id);
			 boolean stop = false;
			 
		while(!stop){
		//	System.out.println("T=="+T);
			 Hseed.add(Nseed );
			 UT.add(Nseed);
			 ArrayList <Integer> NB_seed = MatrixComputing.getNBs(dependencyMatrix,Nseed);
			 P.addAll(NB_seed);
			 P.removeAll(Hseed);
			 
			 if(NB_seed.size() >= (N - UT.size())){
				 ArrayList <Integer> notes = generiteint(0, NB_seed.size()-1, N - UT.size());
				 for(int t = 0; t<notes.size();t++){
					 UT.add(NB_seed.get(notes.get(t)));
				 }
				 stop = true;
				 selectedid.add(UT);
				 T++;
			 }
			 
			 if((MatrixComputing.getNBs(dependencyMatrix,Nseed).size() < (N - UT.size()))&& !P.isEmpty()){
				 int pid = generiteint(0, P.size()-1);
				 Nseed = P.get(pid);
			 }
			 
	        if((MatrixComputing.getNBs(dependencyMatrix,Nseed).size() < (N - UT.size()))&& P.isEmpty()){
	        	 stop = true;
			 }
			 
			 }
		 }
		 }
		 
		 for(int i = 0;i<selectedid.size();i++){
			 ArrayList<String> mergeOnetime = new ArrayList<String>();
			 for(int j = 0;j<selectedid.get(i).size();j++){
				 mergeOnetime .add(SrcAction.classname.get(selectedid.get(i).get(j)));
			//	 System.out.println("第"+i+"次合并=="+SaveFileAction.classname.get(selectedid.get(i).get(j)));
			 }
			 
			 selectedresult.add(mergeOnetime);
		 }
		 
		return selectedresult;
	 }
	
	
	public static ArrayList<MergeMethodNonInheri> selectAllClassToBeMerged(){
		SrcAction.classname = change.FilterClasses();
		ArrayList<MergeMethodNonInheri> MergeMethodNonInheri = new ArrayList<MergeMethodNonInheri>();
		int times = 50;
		ArrayList<ArrayList<String>> Allcc = getselectedresult(times);
		
		for (int t = 0; t < Allcc.size(); t++) {
			List<String> classnameList = Allcc.get(t);
			MergeMethodNonInheri rm = new MergeMethodNonInheri();
			ArrayList<String> methodlist = new ArrayList<String>();
			ArrayList<ArrayList<String>> methodlistoriginalpart = new ArrayList<ArrayList<String>>();	

			for (int i = 0; i < classnameList.size(); i++) {
				ArrayList<String> methodlisttem = new ArrayList<String>();
				for(Map.Entry<String, Feature> entry : SrcAction.classesMap.get(classnameList.get(i)).featureMap.entrySet()) {  
				       String key = entry.getKey(); 
				       Feature value = entry.getValue();
						if (!value.dead) {
							if (methodlisttem.isEmpty()) {
								methodlisttem.add(key);
							} else {
								if (!methodlisttem.contains(key)) {
									methodlisttem.add(key);
								}
							}
						}
				}
				
				
						methodlist.addAll(methodlisttem);
						methodlistoriginalpart.add(methodlisttem);
					}
			rm.methodlist = methodlist;
			rm.methodlistoriginalpart = methodlistoriginalpart;
			rm.classnameListmerge = classnameList;
			MergeMethodNonInheri.add(rm);
		}

		
		return MergeMethodNonInheri;	
	}
	
	
	
	public static Cohesion  ArtificialGodClassConnectivity_Lcom(ArrayList<String> methodlist) {
		// 求内聚性 LCOM/connectivity
		
		

		int n = methodlist.size();
		double[][] x = new double[n][n];
		double[][] y = new double[n][n];
		x = MatrixComputing.makezeroMatrix(x);
		y = MatrixComputing.makezeroMatrix(y);
		for (int i = 0; i < n; i++) {
			List<String> OutboundFeatureList1 = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(i))).featureMap.get(methodlist.get(i)).outboundFeatureList;

				for (int j = 0; j < n; j++) {
					if (i != j) {
						for (int m = 0; m < OutboundFeatureList1.size(); m++) {
							if (OutboundFeatureList1.get(m).equals(methodlist.get(j))) {
								x[i][j] = 1;
							}
						}

					}
				}

			ArrayList<String> outboundAttributeList1 = new ArrayList<String>();
			
			if (methodlist.get(i) != null) {
				if (methodlist.get(i).contains("(") || methodlist.get(i).contains("{")) {
					
					outboundAttributeList1 = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(i))).featureMap.get(methodlist.get(i)).outboundAttributeList;

				} else {
					String uu = null;
					uu = methodlist.get(i);
					outboundAttributeList1.add(uu);
				}
			}

			if (i + 1 < n) {
				for (int j = i + 1; j < n; j++) {

					List<String> outboundAttributeList2 = new ArrayList<String>();
					if (methodlist.get(j) != null) {
						if (methodlist.get(j).contains("(") && methodlist.get(j).contains(")")|| methodlist.get(j).contains("{")&& methodlist.get(j).contains("}")) {

							outboundAttributeList2 = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(j))).featureMap.get(methodlist.get(j)).outboundAttributeList;
							

						} else {
							String uu = null;
							uu = methodlist.get(j);
							outboundAttributeList2.add(uu);
						}
					}
					tool tl = new tool();
					List<String> Intersect = tl.testIntersect(outboundAttributeList1, outboundAttributeList2);
					if (Intersect.size() != 0) {
						y[i][j] = 1;
					}
				}
			}
		//	}
		}
		

		Cohesion ArtificialGodCohesion = new Cohesion();
		if(((n * (n - 1))!=0)){
			ArtificialGodCohesion.connectivity = (MatrixComputing.summatrix(x) + MatrixComputing.summatrix(y)) / ((n * (n - 1)) / 2);
		if(ArtificialGodCohesion.connectivity>1){
			ArtificialGodCohesion.connectivity=1;
		}
		}else{
			ArtificialGodCohesion.connectivity = 0;
		}
//		if(((n * (n - 1))!=0)){
//		Connectivity = (summatrix(x) + summatrix(y)) / ((n * (n - 1)) / 2) + Connectivity;
//		}else{
//			Connectivity = 0;
//		}
		if (((n * (n - 1)) / 2) - (2 * MatrixComputing.summatrix(y)) > 0) {
			ArtificialGodCohesion.LCOM = ((n * (n - 1)) / 2) - (2 * MatrixComputing.summatrix(y));
		} else {
			ArtificialGodCohesion.LCOM = 0;
		}
//		 System.out.println(" 人工类  rc.connectivity ===="+ArtificialGodCohesion.connectivity );
//		 System.out.println(" 人工类  rc.LCOM===="+ArtificialGodCohesion.LCOM);
//		
	
		return ArtificialGodCohesion;

	}
	
	
	
	
	/*
	 * 人工类的CBO MPC
	 */
	
	public static Coupling ArtificialGodClassCBO_MPC(ArrayList<String> methodlist) {
		ArrayList<String> classthis = new ArrayList<String>();
	    
		for(int i = 0 ; i < methodlist.size(); i++){
			if(classthis.isEmpty()){
				classthis.add(SourceParser.Getonlyclassname((methodlist.get(i))));
			}else{
				if(!classthis.contains(SourceParser.Getonlyclassname(methodlist.get(i)))){
					classthis.add(SourceParser.Getonlyclassname(methodlist.get(i)));
				}
			}
		}
		
		
		ArrayList<String> CBO = new ArrayList<String>();
		ArrayList<String> MPC = new ArrayList<String>();

		for(int y = 0; y < classthis.size(); y++){
		ClassObject co = SrcAction.classesMap.get(classthis.get(y));
//			for(int g = 0; g < classesArrList.size(); g++){
//				if( getonlyclassname(classesArrList.get(g).name).equals(classthis.get(y))){
					
				
		for (int e = 0; e < co.outboundClassList.size(); e++) {
			// 如果包含（） 即是函数的属性 !!切记还有另一种情况!
			if (co.outboundClassList.get(e) != null) {
				String st = SourceParser.getonlyclassname(co.outboundClassList.get(e));

				if (CBO.isEmpty()) {
					if (SrcAction.classname.contains(st) && !classthis.contains(st)) {
						CBO.add(st);
					}
				} else {
					if (!CBO.contains(st) && SrcAction.classname.contains(st)&& !classthis.contains(st)) {
						CBO.add(st);
					}
				}
			}
		}

		for (int e = 0; e < co.inboundClassList.size(); e++) {
			// 如果包含（） 即是函数的属性 !!切记还有另一种情况!
			if (co.inboundClassList.get(e) != null) {
				String st = SourceParser.getonlyclassname(co.inboundClassList.get(e));

				if (CBO.isEmpty()) {
					if (SrcAction.classname.contains(st) && !classthis.contains(st)) {
						CBO.add(st);
					}
				} else {
					if (!CBO.contains(st) && SrcAction.classname.contains(st)&& !classthis.contains(st)) {
						CBO.add(st);
					}
				}
			}
		}

		// 处 理class.feoutbound
		for (int e = 0; e < co.outboundFeatureList.size(); e++) {
			// 如果包含（） 即是函数的属性 !!切记还有另一种情况!
			if (co.outboundFeatureList.get(e) != null) {

				String st = SourceParser.Getonlyclassname(co.outboundFeatureList.get(e));
				if (CBO.isEmpty()) {
					if (SrcAction.classname.contains(st) && !classthis.contains(st)) {
						CBO.add(st);
					}
				} else {
					if (!CBO.contains(st) && SrcAction.classname.contains(st)&& !classthis.contains(st)) {
						CBO.add(st);
					}
				}

			}
		}

		// 处 理class.feinbound
		for (int e = 0; e < co.inboundFeatureList.size(); e++) {
			// 如果包含（） 即是函数的属性 !!切记还有另一种情况!
			if (co.inboundFeatureList.get(e) != null) {
				String st = SourceParser.Getonlyclassname(co.inboundFeatureList.get(e));

				if (CBO.isEmpty()) {
					if (SrcAction.classname.contains(st) &&!classthis.contains(st)) {
						CBO.add(st);
					}
				} else {
					if (!CBO.contains(st) && SrcAction.classname.contains(st)
							&& !classthis.contains(st)) {
						CBO.add(st);
					}
				}

			}
		}

	//	for (int z = 0; z < classesArrList.get(g).FeatureList.size(); z++) {
		 for(Map.Entry<String, Feature> entry1 : co.featureMap.entrySet()) {
	    	   String key1 = entry1.getKey();  
	    	   Feature value1 = entry1.getValue();
			// 处理class.feature.outbound
			for (int s = 0; s < value1.outboundClassList.size(); s++) {
				if (value1.outboundClassList.get(s) != null) {
					String st = SourceParser.getonlyclassname(value1.outboundClassList.get(s));

					if (CBO.isEmpty()) {
						if (SrcAction.classname.contains(st) && !classthis.contains(st)) {
							CBO.add(st);
						}
					} else {
						if (!CBO.contains(st) && SrcAction.classname.contains(st)&&!classthis.contains(st)) {
							CBO.add(st);
						}
					}

				}
			}
			// 处理class.feature.feoutbound
			for (int f = 0; f < value1.outboundFeatureList.size(); f++) {
				if (value1.outboundFeatureList.get(f) != null) {
					String st = SourceParser.Getonlyclassname(value1.outboundFeatureList.get(f));
					if (CBO.isEmpty()) {
						if (SrcAction.classname.contains(st) && !classthis.contains(st)) {
							CBO.add(st);
						}
					} else {
						if (!CBO.contains(st) && SrcAction.classname.contains(st)
								&&!classthis.contains(st)) {
							CBO.add(st);
						}
					}

					if (!value1.outboundFeatureList.get(f).contains("$")) {
						if (MPC.isEmpty()) {
							if (SrcAction.classname.contains(st) && !classthis.contains(st)) {
								MPC.add(value1.outboundFeatureList.get(f));
							}
						} else {
							if (!MPC.contains(value1.outboundFeatureList.get(f))&& SrcAction.classname.contains(st) &&!classthis.contains(st)) {
								MPC.add(value1.outboundFeatureList.get(f));
							}
						}

					}

				}
			}
			for (int s = 0; s < value1.inboundClassList.size(); s++) {
				if (value1.inboundClassList.get(s) != null) {
					String st = SourceParser.getonlyclassname(value1.inboundClassList.get(s));
					if (CBO.isEmpty()) {
						if (SrcAction.classname.contains(st) && !classthis.contains(st)) {
							CBO.add(st);
						}
					} else {
						if (!CBO.contains(st) && SrcAction.classname.contains(st)
								&& !classthis.contains(st)) {
							CBO.add(st);
						}
					}
				}
			}
			// 处理class.feature.feoutbound
			for (int f = 0; f < value1.inboundFeatureList.size(); f++) {
				if (value1.inboundFeatureList.get(f) != null) {
					String st = SourceParser.Getonlyclassname(value1.inboundFeatureList.get(f));
					if (CBO.isEmpty()) {
						if (SrcAction.classname.contains(st) && !classthis.contains(st)) {
							CBO.add(st);
						}
					} else {
						if (!CBO.contains(st) && SrcAction.classname.contains(st)&& !classthis.contains(st)) {
							CBO.add(st);
						}
					}
				}
			}

		}
//			}
//			}
		}
		Coupling ArtificialGodCoupling= new Coupling();
		ArtificialGodCoupling .CBO = CBO.size();
		ArtificialGodCoupling.MPC = MPC.size();
		 
//		 System.out.println(" 人工类 rc .CBO  ===="+ArtificialGodCoupling.CBO  );
//		 System.out.println(" 人工类  rc .MPC ===="+ ArtificialGodCoupling.MPC );
//		 
		return ArtificialGodCoupling;
	}
	
	
	
	
	
	/**
	 * get LCOM Connectivityafter refactoring
	 */
	public static Cohesion GetLcomAfterRefactoring(ArrayList< ArrayList<String> > cn){
		double LCOM  =  0;
		double Connectivity  = 0;
		for(int z = 0 ; z <  cn.size(); z++){
			ArrayList<String> methodlist = cn.get(z);	
		int n = methodlist.size();
		double[][] x = new double[n][n];
		double[][] y = new double[n][n];
		x = MatrixComputing.makezeroMatrix(x);
		y = MatrixComputing.makezeroMatrix(y);
		
		for (int i = 0; i < n; i++) {
						
				List<String> OutboundFeatureList1 = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(i))).featureMap.get(methodlist.get(i)).outboundFeatureList;

				for (int j = 0; j < n; j++) {
					if (i != j) {
						for (int m = 0; m < OutboundFeatureList1.size(); m++) {
							if (OutboundFeatureList1.get(m).equals(methodlist.get(j))) {
								x[i][j] = 1;
							}
						}

					}
				}
				
				
				
				ArrayList<String> outboundAttributeList1 = new ArrayList<String>();
				
				if (methodlist.get(i) != null) {
					if (methodlist.get(i).contains("(") || methodlist.get(i).contains("{")) {
						
						outboundAttributeList1 = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(i))).featureMap.get(methodlist.get(i)).outboundAttributeList;

					} else {
						String uu = null;
						uu = methodlist.get(i);
						outboundAttributeList1.add(uu);
					}
				}

				if (i + 1 < n) {
					for (int j = i + 1; j < n; j++) {

						List<String> outboundAttributeList2 = new ArrayList<String>();
						if (methodlist.get(j) != null) {
							if (methodlist.get(j).contains("(") && methodlist.get(j).contains(")")|| methodlist.get(j).contains("{")&& methodlist.get(j).contains("}")) {

								outboundAttributeList2 = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(j))).featureMap.get(methodlist.get(j)).outboundAttributeList;
								

							} else {
								String uu = null;
								uu = methodlist.get(j);
								outboundAttributeList2.add(uu);
							}
						}
						tool tl = new tool();
						List<String> Intersect = tl.testIntersect(outboundAttributeList1, outboundAttributeList2);
						if (Intersect.size() != 0) {
							y[i][j] = 1;
						}
					}
				}

		}
		
		
		if(((n * (n - 1))!=0)){
		double temp = (MatrixComputing.summatrix(x) + MatrixComputing.summatrix(y)) / ((n * (n - 1)) / 2);
		if(temp < 1){
		Connectivity = temp + Connectivity;
		}else{
			Connectivity = 1 + Connectivity;
		}
		}else{

			Connectivity = 0 + Connectivity;
		}
		if (((n * (n - 1)) / 2) - (2 * MatrixComputing.summatrix(y)) > 0) {
			LCOM = LCOM + ((n * (n - 1)) / 2) - (2 * MatrixComputing.summatrix(y));
		} else {
			LCOM = LCOM + 0;
		}
		
		
		
		}
		
		Connectivity = Connectivity /  (double)cn.size();
		
		LCOM = LCOM /  (double)cn.size();
		Cohesion ac = new Cohesion();
		ac.LCOM = LCOM ;
		ac.connectivity = Connectivity;
//		System.out.println(" 重构后LCOM =="+LCOM );
//		System.out.println(" 重构后Connectivity =="+Connectivity );
		return ac;
	}
	
	
	/**
	 * get cbo mpc 
	 */
	public static Coupling GetCBO_MPCAfterRefactoring(ArrayList< ArrayList<String> > cn, double cbo,double mpc) {

	//	String ss = getonlyclassname(clas.name);
		double CBO = 0;
		double MPC = 0;
        double[][] xx = new double[cn.size()][cn.size()]; 
    	xx = MatrixComputing.makezeroMatrix(xx);
		for(int  v= 0 ; v <  cn.size(); v++){
			ArrayList<String> methodlisti = cn.get(v);	
		for(int  t= 0 ; t <  cn.size(); t++){
			if(t!=v){
				ArrayList<String> methodlistj = cn.get(t);	
				for(int k = 0; k < methodlisti.size(); k++){

				List<String> OutboundFeatureList1 = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlisti.get(k))).featureMap.get(methodlisti.get(k)).outboundFeatureList;
				tool tl = new tool();
				List<String> Intersect = tl.testIntersect(OutboundFeatureList1, methodlistj);
				if( !Intersect.isEmpty()){
				MPC = MPC + Intersect.size();
					xx[v][t]=1;
					xx[t][v] =1;
				}
			}
		}// i!=j
		
		}  //j
		}//i
		Coupling ac =new Coupling();
		CBO = (MatrixComputing.summatrix(xx)+cbo)/(double)cn.size();
		MPC = (MPC+mpc)/(double)cn.size();
		ac.CBO =CBO;
		ac.MPC=MPC ;
		
//		System.out.println(" 重构后CBO1 =="+ac.CBO);
//		System.out.println(" 重构后MPC1=="+ac.MPC);
//		
		return ac;
	}
	
	
	
	public static void AdjustCoefficientsABCD(double a, double b, double c, double d, ArrayList<MergeMethodNonInheri> rms, ProgressBarLisenter barLisenter) throws SAXException, IOException, MWException{
		 Metrics mt = new Metrics();
		 
		 double[] MethodSUM =new double[rms.size()];
		 int ind = 0;
		//3行 1：原始  2：人工 3：重构后
			LCOMS =  new double[3][rms.size()];
			CONECT =  new double[3][rms.size()];
			CBOS =  new double[3][rms.size()];
			MPCS =  new double[3][rms.size()];
		 
		 double m = 1;
		 for (int p = 0; p < rms.size(); p++) {
			 
			 MergeMethodNonInheri rm = rms.get(p);
			 ArrayList<String> methodlist = rm.methodlist;    
			 //调节
			 ArrayList<ArrayList<String>> methodlistoriginalpart = rm.methodlistoriginalpart;// 原始类的结构                             //调节
			 int n = methodlist.size();    

			 MethodSUM[p] = (double)n;//调节
				ArrayList<String> methodlist1 = (ArrayList<String>)methodlist.clone();
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
					wsem = semantic.filterSemantic(wsem,methodlist,xshare,ycall,zexe);
			
				}
				}
				
			if(!tool.JudgeAdjustCoeffient(a, b, c, d))	{
			 A = MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixDotMultiply(a, xshare),MatrixComputing.MatrixDotMultiply(b, ycall)),MatrixComputing.MatrixDotMultiply(c, zexe)),MatrixComputing.MatrixDotMultiply(d, wsem)); 
			}else{
			 A = MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixDotMultiply(2,MatrixComputing.MatrixDotMultiply(a, xshare)),MatrixComputing.MatrixDotMultiply(b, ycall)),MatrixComputing.MatrixDotMultiply(c, zexe)),MatrixComputing.MatrixDotMultiply(0,MatrixComputing.MatrixDotMultiply(d, wsem))); 
			}
			 
			 ArrayList< ArrayList<String> > cns = CMN.CommunityDetectionNoextentsAdjust( A, methodlist);//重构后类的结构

			 double MoJOFM = accuracy.getMoJOFM (cns, methodlistoriginalpart);
			
			 mt.MoJOFMSUM.add(MoJOFM);
//			
//			//重构前的类所有度量
			beforeallmetrics bm = beforeallmetrics.GetLcombeforeRefactoring(rm);
//			//人工类的内聚
			Cohesion ArtificialGodCohesion = ArtificialGodClassConnectivity_Lcom(methodlist);
//			//人工耦合
			Coupling ArtificialGodCoupling = ArtificialGodClassCBO_MPC( methodlist);
//			
//			//重构后新类的LCOM，connectivity均值
			Cohesion aftercohesion= GetLcomAfterRefactoring(cns);
//			//重构后新类的CBO，MPC均值
			Coupling aftercoupling = GetCBO_MPCAfterRefactoring(cns, bm.CBO,bm.mpc);
			
			mt = TidyValues(mt, bm, ArtificialGodCohesion, ArtificialGodCoupling, aftercohesion,aftercoupling,p);
			
			MainAdjust.persent = m/(double)rms.size();

			barLisenter.endFile();
			
		 	}//	for (int p = 0; p < rms.size(); p++) {

			fangcha = statisitic.getStandardDiviation1(mt.MoJOFMSUM);
			pingjunzhi  = statisitic.getAverage1(mt.MoJOFMSUM);
			zhongzhi = statisitic.getminValue1(mt.MoJOFMSUM);
			double fangcha1 = statisitic.getStandardDiviation(MethodSUM);
			double pingjunzhi1  = statisitic.getAverage(MethodSUM);
			double zhongzhi1 = statisitic.getminValue(MethodSUM);

			String content = pingjunzhi + " "+ zhongzhi + " " +fangcha+" "+pingjunzhi1 + " "+ zhongzhi1 + " " +fangcha1+"\n";
			tool.writeByFileWrite("D:\\"+Adjust.Threshold+"_abc.txt", content);
	}
	
	
	public static Metrics TidyValues(Metrics mt, beforeallmetrics bm, Cohesion ArtificialGodCohesion, Coupling ArtificialGodCoupling, Cohesion aftercohesion,Coupling aftercoupling,int id) throws IOException{
		
		double[] Lcoms = new double[3];
		double[] Connectivity = new double[3];
		double[] CBO = new double[3];
		double[] MPC = new double[3];
		Lcoms[0] = bm.LCOMavg;
		Lcoms[1] = ArtificialGodCohesion.LCOM;
		Lcoms[2] = aftercohesion.LCOM;
		Connectivity[0] = bm.Connectivityavg;
		Connectivity[1] = ArtificialGodCohesion.connectivity;
		Connectivity[2] = aftercohesion.connectivity;
		CBO[0] = bm.CBOavg;
		CBO[1] = ArtificialGodCoupling.CBO;
		CBO[2] = aftercoupling.CBO;
		MPC[0] = bm.mpcavg;
		MPC[1] = ArtificialGodCoupling.MPC;
		MPC[2] = aftercoupling.MPC;
		Lcoms =  semantic.Reverbubsort(Lcoms);
		Connectivity =  semantic.Reverbubsort(Connectivity);
		CBO =  semantic.Reverbubsort(CBO);
		MPC =  semantic.Reverbubsort(MPC);
		ArtificialGodCohesion.LCOM = Lcoms[0];
		bm.LCOMavg = Lcoms[1];
		aftercohesion.LCOM = Lcoms[2];
		bm.Connectivityavg = Connectivity[1];
		ArtificialGodCohesion.connectivity = Connectivity[2];
		aftercohesion.connectivity = Connectivity[0];
		bm.CBOavg =CBO[1];
		ArtificialGodCoupling.CBO = CBO[0];
		aftercoupling.CBO = CBO[2];
		bm.mpcavg =MPC[1];
		ArtificialGodCoupling.MPC = MPC[0];
		aftercoupling.MPC= MPC[2];
		//3行 1：原始  2：人工 3：重构后
		LCOMS[0][id] =  bm.LCOMavg;
		LCOMS[1][id] =  ArtificialGodCohesion.LCOM;
		LCOMS[2][id] =  aftercohesion.LCOM;
		CONECT[0][id] =  bm.Connectivityavg;
		CONECT[1][id] =  ArtificialGodCohesion.connectivity;
		CONECT[2][id] =  aftercohesion.connectivity;
		
		CBOS[0][id] =  bm.CBOavg;
		CBOS[1][id] =  ArtificialGodCoupling.CBO;
		CBOS[2][id] =  aftercoupling.CBO;
		
		MPCS[0][id] =  bm.mpcavg;
		MPCS[1][id] =  ArtificialGodCoupling.MPC;
		MPCS[2][id] =  aftercoupling.MPC;
		
		
		mt.LCOMSbefore.add(bm.LCOMavg);
		mt.LCOMSGod.add(ArtificialGodCohesion.LCOM);
		mt.LCOMSRefa.add(aftercohesion.LCOM);
		
		mt.Conbefore.add(bm.Connectivityavg);
		mt.ConGod.add(ArtificialGodCohesion.connectivity);
		mt.ConRefa.add(aftercohesion.connectivity);
		
		mt.CBObefore.add(bm.CBOavg);
		mt.CBOGod.add(ArtificialGodCoupling.CBO);
		mt.CBORefa.add(aftercoupling.CBO);
		
		mt.MPCbefore.add(bm.mpcavg);
		mt.MPCGod.add(ArtificialGodCoupling.MPC);
		mt.MPCRefa.add(ArtificialGodCoupling.MPC);
		
//		System.out.println("Tidy bm.LCOMavg!=="+bm.LCOMavg);
//		System.out.println("Tidy ArtificialGodCohesion.LCOM!=="+ArtificialGodCohesion.LCOM);
//		System.out.println("Tidy aftercohesion.LCOM!=="+aftercohesion.LCOM);
//	
//		System.out.println("Tidy bm.Connectivityavg!=="+bm.Connectivityavg);
//		System.out.println("Tidy ArtificialGodCohesion.connectivity!=="+ArtificialGodCohesion.connectivity);
//		System.out.println("Tidy aftercohesion.connectivity!=="+aftercohesion.connectivity);
//		
//		System.out.println("Tidy bm.CBOavg!=="+bm.CBOavg);
//		System.out.println("Tidy ArtificialGodCoupling.CBO!=="+ArtificialGodCoupling.CBO);
//		System.out.println("Tidy aftercoupling.CBO!=="+aftercoupling.CBO);
//		
//		System.out.println("Tidy bm.mpcavg!=="+bm.mpcavg);
//		System.out.println("Tidy ArtificialGodCoupling.MPC!=="+ArtificialGodCoupling.MPC);
//		System.out.println("Tidy aftercoupling.MPC!=="+aftercoupling.MPC);

		return mt;

	}
	

}
