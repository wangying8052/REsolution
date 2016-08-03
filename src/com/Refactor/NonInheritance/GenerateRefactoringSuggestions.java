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
import java.util.List;
import java.util.Map;

import org.omg.CORBA.NVList;
import org.xml.sax.SAXException;

import com.Refactor.classparser.*;
import com.jeantessier.dependencyfinder.gui.SrcAction;


public class GenerateRefactoringSuggestions {
	public static int zengjialei = 0;
	public static boolean JudgeConstructor(String featureName) { //判断该函数是否为构造函数
		boolean ifConstructor = false;
		if(featureName.contains("(")){
			String str[] = featureName.split("\\(");
			String str1[] = str[0].split("\\.");
			if(SourceParser.Getonlyclassname(featureName).contains(str1[str1.length-1])){
				ifConstructor = true;
			}
		}
		return ifConstructor;
	}
	
	
	public static ArrayList<ArrayList<String>> MergeNcns(ArrayList<ArrayList<String>> cns,int i,ArrayList<Integer> mergeN,ArrayList<String> merge ) {
		ArrayList<ArrayList<String>> cns1 = new ArrayList<ArrayList<String>>();
		cns.set(i, merge);
		cns1.add(merge);
		for (int m = 0; m < cns.size(); m++) {
			if(!mergeN.contains(m)){
				cns1.add(cns.get(m));
			}
		}
		return cns1;
	}

	public static ArrayList<String> DeletN(ArrayList<String> cns, ArrayList<Integer> del){
		ArrayList<String> cns1 = new ArrayList<String> ();
		for(int i = 0 ; i < cns.size(); i++){
			if(!del.contains(i)){
				cns1 .add(cns.get(i));
			}
		}
		return cns1 ;
	}
	
	public static boolean IfcontainsScatter(ArrayList<ArrayList<String>> cns){
		boolean flag = false;
		for(int i = 0; i < cns.size(); i++){
			int cnt = 0;
			for(int j = 0; j < cns.get(i).size(); j++){
				if(cns.get(i).get(j).contains("(")||cns.get(i).get(j).contains("{")){
					cnt++;
				}
			}
			if(cnt<=3){
				flag = true;
				break;			}
		}
		return flag;
		
	}
	
	public static boolean IfScatter(ArrayList<String> cns){
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
	
	public static boolean IfIsSameClass(String Featurename1, String Featurename2){
		boolean flag = false;
		if(SourceParser.Getonlyclassname(Featurename1).equals(SourceParser.Getonlyclassname(Featurename2))){
			flag = true;
			
		}
		return flag;	
	}
	public static boolean IftheScatterIsSameClass(ArrayList<ArrayList<String>> cns, int current, String classname){
		boolean flag = false;
		ArrayList<String> catogoryCurrent = new ArrayList<String> ();
		for(int i = 0; i < cns.get(current).size(); i++){ //判断当前零散社区
			if(!SourceParser.Getonlyclassname(cns.get(current).get(i)).equals(classname)){
				flag = false;
				break;
			}
			if(catogoryCurrent.isEmpty()){
				catogoryCurrent.add(SourceParser.Getonlyclassname(cns.get(current).get(i)));
			}else{
				if(!catogoryCurrent.contains(SourceParser.Getonlyclassname(cns.get(current).get(i)))){
					catogoryCurrent.add(SourceParser.Getonlyclassname(cns.get(current).get(i)));
				}
			}
		}
		if(catogoryCurrent.size()==1){
			flag = true;
		}
		return flag;		
	}
	
	public static ArrayList<ArrayList<String>> mergeSeveralCommunity(ArrayList<ArrayList<String>> cns, ArrayList<String> mergescatter, ArrayList<Integer> mergeIndex){
		ArrayList<ArrayList<String>> cnsnew = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> cns1  = (ArrayList<ArrayList<String>>) cns.clone();
		for(int i = 0 ; i < cns.size(); i++){
			if(tool.testIntersect(cns1.get(i), mergescatter).isEmpty()){
					cnsnew.add(cns.get(i));
			}
		}
		cnsnew.add(mergescatter);
		return cnsnew;
		
	}
	
	public static ArrayList<ArrayList<String>> MergeScatterMethods(ArrayList<ArrayList<String>> cns,int n,ArrayList<ArrayList<String>>  methodbukefen) {
		while(IfcontainsScatter(cns)){
			ArrayList<String> mergescatter = new ArrayList<String>();
			
			ArrayList<String> catogoryCurrent = new ArrayList<String> ();
			int current = 0; 
			for(int i = 0; i < cns.size(); i++){
				if(IfScatter(cns.get(i))){
					current = i;
					break;
								}
			                                   }
			
			
			
			for(int i = 0; i < cns.get(current).size(); i++){ //判断当前零散社区
				if(catogoryCurrent.isEmpty()){
					catogoryCurrent.add(SourceParser.Getonlyclassname(cns.get(current).get(i)));
				}else{
					if(!catogoryCurrent.contains(SourceParser.Getonlyclassname(cns.get(current).get(i)))){
						catogoryCurrent.add(SourceParser.Getonlyclassname(cns.get(current).get(i)));
					}
				}
			}
			if(catogoryCurrent.size()==1){//如果current零散社区内的函数同属于一个类
				ArrayList<Integer> mergeIndex = new ArrayList<Integer>();
				mergeIndex.add(current);
				mergescatter.addAll(cns.get(current));
				for(int i = 0; i < cns.size(); i++){
					if(i!=current&&IfScatter(cns.get(i))){
						
						if(IftheScatterIsSameClass(cns, i,catogoryCurrent.get(0))){
							mergescatter.addAll(cns.get(i));
							mergeIndex.add(i);
						}
					}
				}
				
				int max = 0;
				int inx = -1;
				for(int i = 0; i < cns.size(); i++){
					if(!mergeIndex.contains(i)){
						int cout = 0;
						for(int k = 0; k < cns.get(i).size(); k++){
							if(SourceParser.Getonlyclassname(cns.get(i).get(k)).equals(catogoryCurrent.get(0))){
								cout ++;
							}
						}
					double bili = (double)cout/((double)cns.get(i).size());
					if(cout > max && (bili > 0.8)){
						max = cout;
						inx = i;
					}
					}
				}
				
			 if(max>0){

				 boolean flag1 = false;
				 boolean flag2 = false;
					for(int c = 0;  c  < methodbukefen.size(); c++ ){
						if(mergescatter.containsAll(methodbukefen.get(c))){
							 flag1 = true;
						}			
					
						if(cns.get(inx).containsAll(methodbukefen.get(c))){
							flag2 = true;
						}
					}
				 if(!(flag1&&flag2)){
					 mergeIndex.add(inx);
					 mergescatter.addAll(cns.get(inx));
				 }
			 }
			 if(max==0&&IfScatter(mergescatter)){
				 
					for(int i = 0; i < cns.size(); i++){
						if(!mergeIndex.contains(i)){
							int cout = 0;
							ArrayList<String> removetemp = (ArrayList<String>)cns.get(i).clone();
							
							for(int k = 0; k < cns.get(i).size(); k++){
								if(SourceParser.Getonlyclassname(cns.get(i).get(k)).equals(catogoryCurrent.get(0))){
									mergescatter.add(cns.get(i).get(k));
									removetemp.remove(cns.get(i).get(k));
								}
							}
							
							cns.set(i, removetemp);
						}
					}
			 }
			 
			 
			 cns = mergeSeveralCommunity(cns,  mergescatter, mergeIndex);
				
				
				
			}else{//如果current零散社区内的函数同不属于一个类
				ArrayList<String> currentList = new  ArrayList<String>();
				ArrayList<String> getcurrent = (ArrayList<String>) cns.get(current).clone();
				for(int p = 0; p < catogoryCurrent.size(); p++){
					for(int u = 0; u < getcurrent.size();u++){
						if(SourceParser.Getonlyclassname(getcurrent.get(u)).equals(catogoryCurrent.get(p))){
							currentList.add(getcurrent.get(u));
						}
					}
					ArrayList<Integer> mergeIndex = new ArrayList<Integer>();
					mergescatter.addAll(currentList);
					
					for(int i = 0; i < cns.size(); i++){
						if(IfScatter(cns.get(i))){
							
							if(IftheScatterIsSameClass(cns, i,catogoryCurrent.get(p))){
								mergescatter.addAll(cns.get(i));
								mergeIndex.add(i);
							}
						}
					}
					
					
					
					int max = 0;
					int inx = -1;
					for(int i = 0; i < cns.size(); i++){
						if(!mergeIndex.contains(i)){
							int cout = 0;
							for(int k = 0; k < cns.get(i).size(); k++){
								if(SourceParser.Getonlyclassname(cns.get(i).get(k)).equals(catogoryCurrent.get(0))){
									cout ++;
								}
							}
						double bili = (double)cout/((double)cns.get(i).size());
						if(cout > max && (bili > 0.8)){
							max = cout;
							inx = i;
						}
						}
					}
					
				 if(max>0){

					 boolean flag1 = false;
					 boolean flag2 = false;
						for(int c = 0;  c  < methodbukefen.size(); c++ ){
							if(mergescatter.containsAll(methodbukefen.get(c))){
								 flag1 = true;
							}			
						
							if(cns.get(inx).containsAll(methodbukefen.get(c))){
								flag2 = true;
							}
						}
					 if(!(flag1&&flag2)){
						 mergeIndex.add(inx);
						 mergescatter.addAll(cns.get(inx));
					 }
				 }
				 if(max==0&&IfScatter(mergescatter)){
					 
						for(int i = 0; i < cns.size(); i++){
							if(!mergeIndex.contains(i)){
								int cout = 0;
								ArrayList<String> removetemp = (ArrayList<String>)cns.get(i).clone();
								
								for(int k = 0; k < cns.get(i).size(); k++){
									if(SourceParser.Getonlyclassname(cns.get(i).get(k)).equals(catogoryCurrent.get(0))){
										mergescatter.add(cns.get(i).get(k));
										removetemp.remove(cns.get(i).get(k));
									}
								}
								
								cns.set(i, removetemp);
							}
						}
				 }
					
				 
				 cns = mergeSeveralCommunity(cns,  mergescatter, mergeIndex);
				}
				
				
			}
			
		}//while(IfcontainsScatter(cns)){
		return cns;	
	}
	
	
	
	
	public static ArrayList<ArrayList<String>> MergeLittleMethodsChange(ArrayList<ArrayList<String>> cns,int n,ArrayList<ArrayList<String>>  methodbukefen) {
		
/**
 * 首先，若一个社区中只含有一个其他类的构造函数，则remove构造函数，独立成社团
 */
		ArrayList<ArrayList<String>> cns1  = (ArrayList<ArrayList<String>> ) cns.clone();
		for (int i = 0; i < cns.size(); i++) {
			for (int j = 0; j < cns.get(i).size(); j++) {
				if(JudgeConstructor(cns.get(i).get(j))&&cns.get(i).size()>1){
					String constructClass = SourceParser.Getonlyclassname(cns.get(i).get(j));	
					ArrayList<String> constr = new ArrayList<String>();
					 constr.add(cns.get(i).get(j));
					 for (int k = 0; k < cns.get(i).size(); k++) {
								if(constructClass.equals(SourceParser.Getonlyclassname(cns.get(i).get(k)))&&JudgeConstructor(cns.get(i).get(k))){
									if(constr.isEmpty()){
										constr.add(cns.get(i).get(k));
									}else{
										if(!constr.contains(cns.get(i).get(k))){
								     	constr.add(cns.get(i).get(k));
										}
									}
								}
					 }
					boolean gg = false;
					for (int k = 0; k < cns.get(i).size(); k++) {
							if(constructClass.equals(SourceParser.Getonlyclassname(cns.get(i).get(k)))&&!JudgeConstructor(cns.get(i).get(k))){
								gg=true;
								break;
							}
					}
					if(!gg){
						ArrayList<String> ne = new ArrayList<String>();
						ne.addAll(constr);
						cns1.add(ne);
						cns1.get(i).remove(j);
					}
				}
			}	
		}
		
		cns = cns1;
		boolean stop = true;
		while (stop) {  //  只要有零散社团就一直循环
			for (int i = 0; i < cns.size(); i++) {
				if (cns.get(i).size() <= 3 && cns.get(i).size() >= 1) {
					ArrayList<String> merge = new ArrayList<String>();
					ArrayList<Integer> mergeN= new ArrayList<Integer>();
					String classname = SourceParser.Getonlyclassname(cns.get(i).get(0));
					merge.addAll(cns.get(i));
					for (int j = 0; j < cns.size(); j++) {
						if(i!=j){
						if ((cns.get(j).size() <= 3) && (!cns.get(j).isEmpty()) && SourceParser.Getonlyclassname(cns.get(j).get(0)).equals(classname)) {
							merge.addAll(cns.get(j));
							mergeN.add(j);
							
						}
						}
					}
					mergeN.add(i);
					cns =  MergeNcns(cns, i, mergeN, merge );//将琐碎节点合并cns.get(0)		
					/**
					 * 将琐碎节点与构造函数合并
					 */
					boolean ifcontainconstr = false;
					for(int p = 0 ; p < cns.get(0).size(); p++){
						
						if(JudgeConstructor(cns.get(0).get(p))){
							ifcontainconstr = true;
							break;
						}
						
						
					}
					String cls= SourceParser.Getonlyclassname(cns.get(0).get(0));
					if(ifcontainconstr&&cns.get(0).size() <= 3){

					ArrayList<String>  wobuchaile  = new ArrayList<String>();
					Map<String, Feature> featuremap  =  SrcAction.classesMap.get(cls).featureMap;
					for(Map.Entry<String, Feature> entry : featuremap.entrySet()) {  
						String key = entry.getKey();
						if(key!=null){
							wobuchaile.add(key);
						}
					}

					cns.set(0, wobuchaile);
					for (int j = 0; j < cns.size(); j++) {
						if(j!=0){
							ArrayList<Integer> del = new ArrayList<Integer> ();	
								for (int k = 0; k < cns.get(j).size(); k++) {
									if(SourceParser.Getonlyclassname(cns.get(j).get(k)).equals(cls)){
										del.add(k);
									}
								}
								cns.set(j, DeletN(cns.get(j), del));
							}
						}
					}
					
					if(!ifcontainconstr){//如果合并的琐碎社团不包含 构造函数
						for (int j = 0; j < cns.size(); j++) {
							if(j!=0){
								for (int k = 0; k < cns.get(j).size(); k++) {
									if(SourceParser.Getonlyclassname(cns.get(j).get(k)).equals(cls)&&JudgeConstructor(cns.get(j).get(k))){

										mt mmMt = new mt();
										mmMt = CMN.mergeCNS_i_jcg( cns, 0, j);//合并后的社团是j
										cns = mmMt.cns;
										int dx = mmMt.i;
										
										boolean flag1 = false;
										boolean flag2 = false;
			
										for(int c = 0;  c  < methodbukefen.size(); c++ ){
											if(SourceParser.Getonlyclassname(methodbukefen.get(c).get(0)).equals(cls)&&cns.get(dx).containsAll(methodbukefen.get(c))){
												 flag1 = true;
											}
											
										
											if((!SourceParser.Getonlyclassname(methodbukefen.get(c).get(0)).equals(cls))&&cns.get(dx).containsAll(methodbukefen.get(c))){
												flag2 = true;
											}
										}
										if(flag1&&flag2){
											
											ArrayList<Integer> del = new ArrayList<Integer> ();
											ArrayList<String> xinda= new ArrayList<String> ();
					                        for (int p = 0; p < cns.get(dx).size(); p++) {
												if(SourceParser.Getonlyclassname(cns.get(dx).get(p)).equals(cls)){
													xinda.add(cns.get(dx).get(p));
													del.add(p);
												}
											}
											cns.set(dx, DeletN(cns.get(dx),  del));
											if(xinda.size()<=3){
												
												ArrayList<String>  wobuchaile  = new ArrayList<String>();
												Map<String, Feature> featuremap  =  SrcAction.classesMap.get(cls).featureMap;
												for(Map.Entry<String, Feature> entry : featuremap.entrySet()) {  
													String key = entry.getKey();
													if(key!=null){
														wobuchaile.add(key);
													}
												}
												
												
												for (int v = 0; v < cns.size(); v++) {
												
														ArrayList<Integer> de = new ArrayList<Integer> ();
														
														for (int kk = 0; kk < cns.get(v).size(); kk++) {
															if(SourceParser.Getonlyclassname(cns.get(v).get(kk)).equals(cls)){
																de.add(kk);
															}
														}
														cns.set(v, DeletN(cns.get(v),  de));
												
												}
												
												cns.add(wobuchaile);
												
											}else{
												cns.add(xinda);
											}
											
											
										}
										
										break;
									}
								}
								
							}
						}
						cns = cns;
					}
					
					
					
					
					
				}
				
		
			}
	
			
			for(int i = 0; i < cns.size();i++){
				for(int j = 0; j < cns.size();j++){
					if(i!=j){
						if(!tool.testIntersect( cns.get(i),cns.get(j)).isEmpty()) {
							if(cns.get(i).size()<cns.get(j).size()){
								cns.get(i).removeAll(tool.testIntersect( cns.get(i),cns.get(j)));
							}else{
								cns.get(j).removeAll(tool.testIntersect( cns.get(i),cns.get(j)));
							}
						}
					}
				}
			}
			
			
			
			boolean flag = false;
			for (int i = 0; i < cns.size(); i++) {
				if (cns.get(i).size() <= 3&&!cns.get(i).isEmpty()) {
					flag = true;
				}
			}
			if (!flag) {
				stop = false;
			}

		}

		return cns;

	}
	
	public static void PrintRefactoringSuggestions(Suggestions sg) throws IOException{
		String log1   =  "D:\\搬移函数重构建议1.txt";  //???
		String log   =  "D:\\提炼类重构建议1.txt";  //???

		for(int i = 0; i < sg.UndoEntitySort.size(); i++){
		       UndoEntity value = sg.UndoEntitySort.get(i);
//		       tool.writeByFileWrite(log1, "Move Method:"+value.entityname+"\n");
		       if(value.newNo>=0){
//			   tool.writeByFileWrite(log1, "from--->"+value.source +"---to--->"+value.target+"_new_"+value.newNo+"\n");
		       }else{
//		    	   tool.writeByFileWrite(log1, "from--->"+value.source +"---to--->"+value.target+"\n");
		       }
		}

		
		for(int i = 0; i < sg.undoClasseSort.size(); i++){ 
			UndoClass value = sg.undoClasseSort.get(i);
			ArrayList<newclass>  ExtractClass = SrcAction.classesMap.get(value.classname).ExtractClass;
			zengjialei = zengjialei + ExtractClass.size() - 1;
//			tool.writeByFileWrite(log, "需要将大类"+value.classname+"  methodsize=  "+SrcAction.classesMap.get(value.classname).featureMap.size()+"分解为"+ ExtractClass.size()+"个新类==="+"\n");
			for(int k = 0; k  < ExtractClass.size(); k++){
				if(ExtractClass.get(k).isLeaf){
//			tool.writeByFileWrite(log, value.classname+"new_"+k+"------"+"   分解后是不是叶子节点--"+ExtractClass.get(k).isLeaf+"   其父类节点是--"+ExtractClass.get(k).superclass+"\n");
				}else{
//			tool.writeByFileWrite(log, value.classname+"new_"+k+"------"+"   分解后是不是叶子节点--"+ExtractClass.get(k).isLeaf+"\n");	
				}
			for(int o = 0; o < ExtractClass.get(k).extractedclass.size(); o++){
//				tool.writeByFileWrite(log,ExtractClass.get(k).extractedclass.get(o)+"\n");
			}

			}
		}

	}
	
	
	
	
	/**
	 * 寻找重构建议
	 */
	public static Suggestions FindRefactoringSuggestions(ArrayList< ArrayList<String> > cns, ArrayList<ArrayList<String>> methodbukefen,List<String> classnameListmerge) throws SAXException, IOException {
		
		
		/**
		 * 先分析 提炼类重构的情况
		 */
		Suggestions sg = new Suggestions();
		String log   =  "D:\\提炼类重构建议.txt";  //???
		String log1   =  "D:\\搬移函数重构建议.txt";  //???
		ArrayList<MoveMethods> Move = new  ArrayList<MoveMethods>();
		for(int i = 0; i < cns.size(); i++){
			ArrayList<String>  classes = new ArrayList<String>();
			ArrayList<Integer>  classescnt = new ArrayList<Integer>();
			for(int j = 0 ; j < cns.get(i).size(); j++){
				String clsn = SourceParser.Getonlyclassname(cns.get(i).get(j));
				if(classes.isEmpty()){
					classes.add(clsn);
					int kk = 1;
					classescnt.add(kk);
				}else{
					if(!classes.contains(clsn)){
						classes.add(clsn);
						int kk = 1;
						classescnt.add(kk);
					}else{
						int dx = classes.indexOf(clsn);
						classescnt.set(dx, (classescnt.get(dx)+1));
					}
				}
			}
			
			if(!classescnt.isEmpty()){
			if(classes.size()==1){
				newclass nc = new newclass();
				nc.extractedclass = cns.get(i);
				SrcAction.classesMap.get(classes.get(0)).ExtractClass.add(nc);
			}else{
				int max = classescnt.get(0);
				int mdx = 0;
				for(int p = 1; p < classescnt.size(); p++){
					if(max < classescnt.get(p)){
						max = classescnt.get(p);
						mdx = p;
					}
				}
				String classn = classes.get(mdx);
				MoveMethods mm = new MoveMethods();
				for(int j = 0 ; j < cns.get(i).size(); j++){
					
					ArrayList<String>  Source = new ArrayList<String>();
					ArrayList<String>  Target = new ArrayList<String>();
					if(SourceParser.Getonlyclassname(cns.get(i).get(j)).equals(classn)){
						mm.Target.add(cns.get(i).get(j));
					}else{
							mm.Source.add(cns.get(i).get(j));
					}
				}
				newclass nc = new newclass();
				nc.extractedclass = mm.Target;
				SrcAction.classesMap.get(classn).ExtractClass.add(nc);
				Move.add(mm);
				
			}
		}
			
			
			
		}
		
		
		for(int vv= 0; vv < classnameListmerge.size(); vv++){
			ArrayList<newclass> ec = SrcAction.classesMap.get(classnameListmerge.get(vv)).ExtractClass;
			if(!ec.isEmpty()){
			if(ec.size()==1){
			}else{
				if(SrcAction.classesMap.get(classnameListmerge.get(vv)).isLeaf){
					for(int uu = 0; uu < methodbukefen.size(); uu++){
						if(SourceParser.Getonlyclassname(methodbukefen.get(uu).get(0)).equals(classnameListmerge.get(vv))){
							for(int k = 0 ; k  < ec.size(); k++){
								if(ec.get(k).extractedclass.containsAll((methodbukefen.get(uu)))){
									if(k !=0){
										ArrayList<String> tem =(ArrayList<String>)SrcAction.classesMap.get(classnameListmerge.get(vv)).ExtractClass.get(0).extractedclass.clone();
										SrcAction.classesMap.get(classnameListmerge.get(vv)).ExtractClass.set(0,ec.get(k));
										newclass nc =  new newclass();
										nc.extractedclass = tem;
										SrcAction.classesMap.get(classnameListmerge.get(vv)).ExtractClass.set(k, nc);
										break;
										}
								}
							}
						}
					}
				}else{
				for(int k = 0 ; k  < ec.size(); k++){
					for(int hh = 0 ; hh < ec.get(k).extractedclass.size(); hh++){
						if(JudgeConstructor(ec.get(k).extractedclass.get(hh))){
							if(k !=0){
							ArrayList<String> tem =(ArrayList<String>)SrcAction.classesMap.get(classnameListmerge.get(vv)).ExtractClass.get(0).extractedclass.clone();
							SrcAction.classesMap.get(classnameListmerge.get(vv)).ExtractClass.set(0,ec.get(k));
							newclass nc =  new newclass();
							nc.extractedclass = tem;
							SrcAction.classesMap.get(classnameListmerge.get(vv)).ExtractClass.set(k, nc);
							break;
							}
						}
					}
				}
				}
			}
			}
		}
		
		
		for(int vv= 0; vv < classnameListmerge.size(); vv++){
			ArrayList<newclass> ec = SrcAction.classesMap.get(classnameListmerge.get(vv)).ExtractClass;
			if(!ec.isEmpty()){
			if(ec.size()>1){
			
				UndoClass undoClass = new UndoClass();
				undoClass.classname = classnameListmerge.get(vv);
				sg.undoClasses.put(classnameListmerge.get(vv), undoClass);
				for(int h = 0; h < ec.size(); h++){
		            	 boolean mm = false;
		            	 for(int kk = 0 ; kk < Main.methodbukefenlocal.size(); kk++){
		            	 if(ec.get(h).extractedclass.containsAll( Main.methodbukefenlocal.get(kk))){
		            		 mm = true;
		            		 break;
		            	 }
		            	 }
		            SrcAction.classesMap.get(classnameListmerge.get(vv)).ExtractClass.get(h).isLeaf = mm;
		            SrcAction.classesMap.get(classnameListmerge.get(vv)).ExtractClass.get(h).superclass = SrcAction.classesMap.get(classnameListmerge.get(vv)).superclass;

				}
			}
			}
		}
		
		/**
		 * 再分析搬移函数重构情况
		 */
		for(int b=0;b <Move.size();b++){
			for(int vv= 0; vv < classnameListmerge.size(); vv++){
				ArrayList<newclass> ec =  SrcAction.classesMap.get(classnameListmerge.get(vv)).ExtractClass;
				ArrayList<ArrayList<String>> ta = new ArrayList<ArrayList<String>>();
				for(int i = 0; i < ec.size(); i++){
					ta.add(ec.get(i).extractedclass);
				}
                if(ta.contains(Move.get(b).Target)){
                	int ix = ec.indexOf(Move.get(b).Target);           	
                	for(int i = 0; i <Move.get(b).Source.size(); i++ ){
                		UndoEntity undoEntity = new UndoEntity();
                		undoEntity.entityname = Move.get(b).Source.get(i);
                		undoEntity.source = SourceParser.Getonlyclassname(Move.get(b).Source.get(i));
                		undoEntity.target = SourceParser.Getonlyclassname(Move.get(b). Target.get(0));
                		String targetclass =  SourceParser.Getonlyclassname(Move.get(b). Target.get(0));
                		if(!SrcAction.classesMap.get(targetclass).ExtractClass.isEmpty()&&SrcAction.classesMap.get(targetclass).ExtractClass.size()>1){
                			for(int it = 0; it < SrcAction.classesMap.get(targetclass).ExtractClass.size();it++){
                				if(SrcAction.classesMap.get(targetclass).ExtractClass.get(it).extractedclass.contains(Move.get(b). Target.get(0))){
                					undoEntity.newNo = it+1;
                					break;
                				}
                			}
                		
                		}
                		undoEntity.targetidx = ix;
                		sg.UndoEntities.put(Move.get(b).Source.get(i), undoEntity);
                	//	tool.writeByFileWrite(log1, "Source:  "+Move.get(b).Source.get(i)+"   move to  Target: "+SourceParser.Getonlyclassname(Move.get(b). Target.get(0))+ix +"\n");  //???
                	}
                }
			}
		}
		
		for(int vv= 0; vv < classnameListmerge.size(); vv++){
			ArrayList<newclass> ec =  SrcAction.classesMap.get(classnameListmerge.get(vv)).ExtractClass;
			for(int k = 0; k < ec.size(); k++){
				for(int m = 0 ; m < cns.size(); m++){
					if(cns.get(m).containsAll(ec.get(k).extractedclass)){
						ec.get(k).extractedclass = cns.get(m);
					}
				}
			}
		}
	
		return sg;
		
		
		

	}
	
	
}
