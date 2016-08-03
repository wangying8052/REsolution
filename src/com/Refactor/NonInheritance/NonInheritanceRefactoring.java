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

import org.xml.sax.SAXException;

import bsh.This;

import com.Refactor.Inheritance.InheritSuggestions;
import com.Refactor.Inheritance.RefactorInheritance;
import com.Refactor.Inheritance.extend;
import com.Refactor.Inheritance.returnvalue;
import com.Refactor.Metrics.AuxiliaryMeasure;
import com.Refactor.classparser.*;
import com.jeantessier.dependencyfinder.gui.SrcAction;
import com.mathworks.toolbox.javabuilder.MWException;
import com.sun.jdi.Value;


public class NonInheritanceRefactoring {
	public static double a = 0.5;
	public static double b = 0.2;
	public static double c = 0.2;
	public static double d = 0.1;

	public static double persent = 0;
	public static ArrayList<Integer> IndexChechBox = new ArrayList<Integer>();
	public static ArrayList<ArrayList<String>> result0 = new ArrayList<ArrayList<String>>();
	
	public static double YCurrent = 0; //【当前的y坐标】
	public static int XCurrent = 0;//【当前的x坐标】
	public static int Xtotal = 0;//【x坐标轴总长度】
	public static double Ymax = 0;//【Y坐标轴最大值】
	public static double Ymin = 0;//【Y坐标轴最小值】
	
	static int huafenhou = 0;
	public static Suggestions sgs = new Suggestions();
	
	public static void TempIndexChechBox(){
		for(int i = 0; i < preprocessing. MergeSetList.size(); i++){
			IndexChechBox.add(i);
		}
	}
	
	public static void setDefaultCoefficients(){
		a = 0.5;
		b = 0.2;
		c = 0.2;
		d = 0.1;
	}
	
	public static void ResetCoefficients(double aa, double bb, double cc, double dd){
		a = aa;
		b = bb;
		c = cc;
		d = dd;
	}
	
	public static ArrayList<ArrayList<String>> RefactorOneConnect(MergeMethodNonInheri rm, double a, double b, double c, double d) throws IOException, SAXException, MWException{
		
		ArrayList<ArrayList<String>> result0 = new ArrayList<ArrayList<String>>();
		ArrayList<String> methodlist = rm.methodlist;  //调节
		ArrayList<ArrayList<String>> methodlistoriginalpart = rm.methodlistoriginalpart;// 原始类的结构                             //调节
		ArrayList<ArrayList<String>> methodbukefen = rm.methodbukefen ;
		ArrayList<String> methodduli= rm.methodduli;
		ArrayList<ArrayList<String>> cns = new ArrayList<ArrayList<String>>();

		if(preprocessing.DSC>7){
	    cns = CMN.CommunityDetectionNoextentsCondition(a, b, c, d, methodlist, methodbukefen, methodduli,methodlistoriginalpart);//重构后类的结构
		}else{
		cns = CMN.CommunityDetectionNoextentsCondition1(a, b, c, d, methodlist, methodbukefen, methodduli);//重构后类的结构
		}
		
        for(int pp = 0; pp < cns.size(); pp++){
       	 boolean mm = false;
       	 for(int kk = 0 ; kk <rm.methodbukefen.size(); kk++){
       	 if(cns.get(pp).containsAll(rm.methodbukefen.get(kk))){
       		SrcAction.classesMap.get(SourceParser.Getonlyclassname(rm.methodbukefen.get(kk).get(0))).Refactor = cns.get(pp);
       		 mm = true;
       	 }
       	 }
       	 
       	 if(!mm){
       		 result0.add( cns.get(pp));
       	 }
       }
        
        for(int vc = 0 ; vc < Main.jiubuchai.size();vc++){
       	 result0.add(Main.jiubuchai.get(vc));
        }

        Suggestions sg = GenerateRefactoringSuggestions.FindRefactoringSuggestions(cns, methodbukefen,rm.classnameListmerge) ;
        CMN.calculateDetaQ(sg,rm.methodlistoriginalpart, methodlist);
        
        sgs.undoClasses.putAll(sg.undoClasses);
        sgs.UndoEntities.putAll(sg.UndoEntities);
        
        
		return result0;
	    
	}
	
	
	public static void Clear(){
		Main.OriQ = 0;
		result0.clear();
		sgs.undoClasses.clear();
		sgs.undoClasseSort.clear();
		sgs.UndoEntities.clear();
		sgs.UndoEntitySort.clear();
		for(int i = 0; i < SrcAction.classname.size(); i++){
		SrcAction.classesMap.get(SrcAction.classname.get(i)).ExtractClass.clear();
		SrcAction.classesMap.get(SrcAction.classname.get(i)).C3=0;
		SrcAction.classesMap.get(SrcAction.classname.get(i)).CBO=0;
		SrcAction.classesMap.get(SrcAction.classname.get(i)).connectivity=0;
		SrcAction.classesMap.get(SrcAction.classname.get(i)).FeatureList.clear();
		SrcAction.classesMap.get(SrcAction.classname.get(i)).LCOM=0;
		SrcAction.classesMap.get(SrcAction.classname.get(i)).Refactor.clear();
//		SrcAction.classesMap.get(SrcAction.classname.get(i)).methodlines.clear();
		}
		GenerateRefactoringSuggestions.zengjialei = 0;
		YCurrent=0;//非继承体系重构结束后，系统的Q值
		Ymax = 0;
		Ymin = 0;
		Xtotal = 0;
		Main.buchai.clear();;// 如果一个类内部只包含3个函数，不做拆分
		Main.jiubuchai.clear();;// 如果一个类内部只包含3个函数，不做拆分
		Main.methodbukefenlocal.clear();
		Main.RefQ = 0;
	}
	
	
	public static void ClearAction(){
		
		Main.OriQ = 0;
		sgs.undoClasses.clear();
		sgs.undoClasseSort.clear();
		sgs.UndoEntities.clear();
		sgs.UndoEntitySort.clear();

		GenerateRefactoringSuggestions.zengjialei = 0;
		SrcAction.classesMap.clear();
		SrcAction.classname.clear();
		SrcAction.allJavaFilePaths.clear();
		SrcAction.allSrcPaths.clear();
		IndexChechBox.clear();
		result0.clear();
		YCurrent=0;//非继承体系重构结束后，系统的Q值
		Ymax = 0;
		Ymin = 0;
		Xtotal = 0;
		Main.buchai.clear();;// 如果一个类内部只包含3个函数，不做拆分
		Main.jiubuchai.clear();;// 如果一个类内部只包含3个函数，不做拆分
		Main.methodbukefenlocal.clear();
		Main.OriQ = 0;
		Main.RefQ = 0;
		RefactorInheritance.Ymax = 0;
		RefactorInheritance.Ymin = 0;
		RefactorInheritance.Xtotal = 0;
		RefactorInheritance.Ycurrent = 0;
		RefactorInheritance.Xcurrent = 0;
		preprocessing.NonIherCount = 0;
		preprocessing.extendsMatrix = new int[1][1];
		preprocessing.dependencyMatrix = new int[1][1];
		preprocessing.MergeSetList.clear();;
		preprocessing.DSC = 0;
		semantic.wordss.clear(); 
		InheritSuggestions.SplitTree.clear();
		RefactorInheritance.rv.levelsRefactor.clear();
		RefactorInheritance.rv.cns.clear();
		RefactorInheritance.rv.MQ=0;
		RefactorInheritance.rv.SplitTree.clear();
		RefactorInheritance.levels.clear();
		IndexChechBox.clear();;
		
	}
	
	
public static void ClearAction1(){
	for(int i = 0; i < SrcAction.classname.size(); i++){
	SrcAction.classesMap.get(SrcAction.classname.get(i)).ExtractClass.clear();
	SrcAction.classesMap.get(SrcAction.classname.get(i)).C3=0;
	SrcAction.classesMap.get(SrcAction.classname.get(i)).CBO=0;
	SrcAction.classesMap.get(SrcAction.classname.get(i)).connectivity=0;
	SrcAction.classesMap.get(SrcAction.classname.get(i)).FeatureList.clear();
	SrcAction.classesMap.get(SrcAction.classname.get(i)).LCOM=0;
	SrcAction.classesMap.get(SrcAction.classname.get(i)).Refactor.clear();
//	SrcAction.classesMap.get(SrcAction.classname.get(i)).methodlines.clear();
	}
		Main.OriQ = 0;
		sgs.undoClasses.clear();
		sgs.undoClasseSort.clear();
		sgs.UndoEntities.clear();
		sgs.UndoEntitySort.clear();
		GenerateRefactoringSuggestions.zengjialei = 0;
		result0.clear();;
		YCurrent=0;//非继承体系重构结束后，系统的Q值
		Ymax = 0;
		Ymin = 0;
		Xtotal = 0;
		Main.buchai.clear();;// 如果一个类内部只包含3个函数，不做拆分
		Main.jiubuchai.clear();;// 如果一个类内部只包含3个函数，不做拆分
		Main.methodbukefenlocal.clear();
		Main.OriQ = 0;
		Main.RefQ = 0;
		RefactorInheritance.Ymax = 0;
		RefactorInheritance.Ymin = 0;
		RefactorInheritance.Xtotal = 0;
		RefactorInheritance.Ycurrent = 0;
		RefactorInheritance.Xcurrent = 0;
		RefactorInheritance.rv.levelsRefactor.clear();
		RefactorInheritance.rv.cns.clear();
		RefactorInheritance.rv.MQ=0;
		RefactorInheritance.rv.SplitTree.clear();
		semantic.wordss.clear(); 
		InheritSuggestions.SplitTree.clear();
		RefactorInheritance.levels.clear();
		
	}
	//取得绘图数据
	public static void GetQSorttoDrawFigure(int unClass, int unEntity,int rmsize){
		int count = 1;
		int count1 = 1;
		Main.RefQ = Main.OriQ/rmsize;
	//	System.out.println("sgs.undoClasseSort.size() + sgs.UndoEntitySort.size()+1=="+(sgs.undoClasseSort.size() + sgs.UndoEntitySort.size()+1));
		sgs.Q_Orig = new double[sgs.undoClasseSort.size() + sgs.UndoEntitySort.size()+1];
		sgs.Q_Orig[0] = Main.OriQ/rmsize;
		double[] Q_Undo = new double[sgs.undoClasseSort.size() + sgs.UndoEntitySort.size() - unClass - unEntity +1];
		Q_Undo[0] = Main.OriQ/rmsize;
		XCurrent = 0;
		for(int i = 0; i < sgs.UndoEntitySort.size(); i++){
			sgs.Q_Orig[count] = sgs.Q_Orig[count - 1] + sgs.UndoEntitySort.get(i).detaQ/rmsize;
			count ++;
			if(i < sgs.UndoEntitySort.size() - unEntity){
				Q_Undo[count1] = Q_Undo[count1 - 1] + sgs.UndoEntitySort.get(i).detaQ/rmsize;
				count1++;
				XCurrent = XCurrent + 1;
			}
		}
		
		for(int i = 0; i < sgs.undoClasseSort.size(); i++){
			sgs.Q_Orig[count] = sgs.Q_Orig[count - 1] + sgs.undoClasseSort.get(i).detaQ/rmsize;
			count ++;
			if(i < sgs.undoClasseSort.size() - unClass){
				Q_Undo[count1] = Q_Undo[count1 - 1] + sgs.undoClasseSort.get(i).detaQ/rmsize;
				count1++;
				XCurrent = XCurrent + sgs.undoClasseSort.get(i).xEntity;
			}
		}
		
		
		
		sgs.Q_Undo = Q_Undo;
		
		YCurrent = sgs.Q_Undo[sgs.Q_Undo.length-1];
		Ymin = sgs.Q_Orig[0];
		Ymax = sgs.Q_Orig[sgs.Q_Orig.length-1];
		
		for(int i = 0 ; i  < sgs.Q_Orig.length; i++ ){
			 System.out.println("sgs.Q_Orig==="+i+"  "+ sgs.Q_Orig[i]);
		}
		 System.out.println("撤销==="+unClass+"步提炼类重构操作==="+ "  撤销==="+unEntity+"步搬移函数重构操作===" + "===XCurrent==" + XCurrent + "===Xtotal=="+Xtotal+"  ===YCurrent==="+ YCurrent);
		 System.out.println(" sgs.Q_Orig[sgs.Q_Orig.length-1] -  sgs.Q_Orig[0]"+(sgs.Q_Orig[sgs.Q_Orig.length-1]-sgs.Q_Orig[0]));
		 
		for(int i = 0 ; i  < sgs.Q_Undo.length; i++ ){
			 System.out.println("sgs.Q_Undo==="+i+"  "+ sgs.Q_Undo[i]);
		}
	}
	
	
	
	//取得绘图数据
	
	
	
	public static void FinalSortSuggestions(){
		Xtotal = 0;
		for(Map.Entry<String, UndoClass> entry : sgs.undoClasses.entrySet()) { 
			UndoClass Value = entry.getValue();
			sgs.undoClasseSort.add(Value);
			Xtotal = Xtotal  + Value.xEntity;
		}
		
		for(Map.Entry<String, UndoEntity> entry : sgs.UndoEntities.entrySet()) { 
			UndoEntity Value = entry.getValue();
			sgs.UndoEntitySort.add(Value);
			Xtotal = Xtotal  + 1;
		}
		
		sgs.UndoEntitySort = semantic.ReverbEntitySort(sgs.UndoEntitySort);
		sgs.undoClasseSort = semantic.ReverbClasssort(sgs.undoClasseSort);

	}
	
	public static void RefactorNonIheritance(double a, double b, double c, double d) throws IOException, SAXException, MWException{
		 
		 NonInheritanceRefactoring.result0.clear();
		 double i = 1;
		 persent = 0;
		 TempIndexChechBox();
		 for (int p = 0; p < preprocessing.MergeSetList.size(); p++) {
			 if(NonInheritanceRefactoring.IndexChechBox.contains(p)){
			 ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
			 temp = NonInheritanceRefactoring.RefactorOneConnect(preprocessing.MergeSetList.get(p), NonInheritanceRefactoring.a, NonInheritanceRefactoring.b, NonInheritanceRefactoring.c, NonInheritanceRefactoring.d); 
			 result0.addAll(temp);
			 persent = i/(double)preprocessing.MergeSetList.size();
			 }
		 }
		 FinalSortSuggestions();//分析最终重构建议
		 GenerateRefactoringSuggestions.PrintRefactoringSuggestions(NonInheritanceRefactoring.sgs);//打印非继承体系重构建议
		 tool.GetQlog(0, 0,preprocessing.MergeSetList.size());//撤销操作
	}
	
	
	

	
	

	public static double[][] makeMatrix(double a, double b, double c, double d, ArrayList<String> methodlist) throws SAXException, IOException, MWException{
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
		if(n>0){
		if(a > 0){
			xshare = makeshareattributmatrix(methodlist);
		}
		if(b > 0){
			ycall = makecallingmethodmatrix(methodlist);
		}
		if(c > 0){
			zexe = makeexecutematrix(methodlist);
		}
		
		if(d > 0){
			//语义相似度矩阵
			wsem = semantic.makesemanticmatrix(methodlist);
			wsem = semantic.filterSemantic(wsem, methodlist, xshare,  ycall, zexe);
			
	//		tool.writeMatrix(wsem, "E:\\yuyi\\"+SourceParser.Getonlyclassname(methodlist.get(0))+".txt");
			
		}
		}
		
		A = MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixDotMultiply(a, xshare),MatrixComputing.MatrixDotMultiply(b, ycall)),MatrixComputing.MatrixDotMultiply(c, zexe)),MatrixComputing.MatrixDotMultiply(d, wsem));
		
		return A;
	}
	
	
	
	public static double[][] makeshareattributmatrix(ArrayList<String> methodlist) {
		int n = methodlist.size();

		double[][] x = new double[n][n];
		for (int i = 0; i < n; i++) {
			ArrayList<String> outboundAttributeList1 = new ArrayList<String>();
			if (methodlist.get(i) != null) {
				if (methodlist.get(i).contains("(")|| methodlist.get(i).contains("{")) {
					Map<String, Feature> featureMap = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(i))).featureMap;
					outboundAttributeList1 = featureMap.get(methodlist.get(i)).outboundAttributeList;
		
				} else {
					outboundAttributeList1.add(methodlist.get(i));
				}
			}
		

			if (i + 1 < n) {
				for (int j = i + 1; j < n; j++) {
					if(!(GenerateRefactoringSuggestions.JudgeConstructor(methodlist.get(j))&&GenerateRefactoringSuggestions.JudgeConstructor(methodlist.get(i))&&!GenerateRefactoringSuggestions.IfIsSameClass(methodlist.get(j), methodlist.get(i)))){
					List<String> outboundAttributeList2 = new ArrayList<String>();
					if (methodlist.get(j) != null) {
						if (methodlist.get(j).contains("(")|| methodlist.get(j).contains("{")) {
						
							Map<String, Feature> featureMap = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(j))).featureMap;
							outboundAttributeList2 = featureMap.get(methodlist.get(j)).outboundAttributeList;
																
						
						} else {

							outboundAttributeList2.add(methodlist.get(j));
						}
					}
					
					tool tl = new tool();

					List<String> Intersect = tl.testIntersect(outboundAttributeList1, outboundAttributeList2);
					List<String> Union = tl.testUnion(outboundAttributeList1, outboundAttributeList2);
				
					if (!Union.isEmpty()) {
						double zi = Intersect.size();
						double mu = Union.size();
						x[i][j] = zi / mu;
						x[j][i] = zi / mu;
					} else {
						x[i][j] = 0;
					}
				}
			}
			}
		}


		return x;
	}

	
	public static double[][] makeshareattributmatrix1(ArrayList<String> methodlist) {
		int n = methodlist.size();

		double[][] x = new double[n][n];
		for (int i = 0; i < n; i++) {
			ArrayList<String> outboundAttributeList1 = new ArrayList<String>();
			if (methodlist.get(i) != null) {
				if (methodlist.get(i).contains("(")|| methodlist.get(i).contains("{")) {
					Map<String, Feature> featureMap = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(i))).featureMap;
					outboundAttributeList1 = featureMap.get(methodlist.get(i)).outboundAttributeList;
		
				} else {
					outboundAttributeList1.add(methodlist.get(i));
				}
			}
		

			if (i + 1 < n) {
				for (int j = i + 1; j < n; j++) {
					
					List<String> outboundAttributeList2 = new ArrayList<String>();
					if (methodlist.get(j) != null) {
						if (methodlist.get(j).contains("(")|| methodlist.get(j).contains("{")) {
						
							Map<String, Feature> featureMap = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(j))).featureMap;
							outboundAttributeList2 = featureMap.get(methodlist.get(j)).outboundAttributeList;
																
						
						} else {

							outboundAttributeList2.add(methodlist.get(j));
						}
					}
					
					tool tl = new tool();

					List<String> Intersect = tl.testIntersect(outboundAttributeList1, outboundAttributeList2);
					List<String> Union = tl.testUnion(outboundAttributeList1, outboundAttributeList2);
				
					if (!Union.isEmpty()) {
						double zi = Intersect.size();
						double mu = Union.size();
						x[i][j] = zi / mu;
						x[j][i] = zi / mu;
					} else {
						x[i][j] = 0;
					}
			
			}
			}
		}


		return x;
	}
	
	public static double[][] makecallingmethodmatrix1(ArrayList<String> methodlist) {
		int n = methodlist.size();
		double[][] x = new double[n][n];
		for (int i = 0; i < n; i++) {
			List<String> OutboundFeatureList1 = new ArrayList<String>();
			Map<String, Feature> featureMap = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(i))).featureMap;
			OutboundFeatureList1  = featureMap.get(methodlist.get(i)).outboundFeatureList;
						
			for (int j = 0; j < n; j++) {
				if (i != j) {
				
					List<String> inboundMethod2 = new ArrayList<String>();
					if (OutboundFeatureList1.contains(methodlist.get(j))) {
						Map<String, Feature> featureMap1 = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(j))).featureMap;
						inboundMethod2  = featureMap1.get(methodlist.get(j)).inboundMethodList;
						
					}
					if (!inboundMethod2.isEmpty()) {
						double zi = 1;
						double mu = inboundMethod2.size();
						x[i][j] = zi / mu;

					} else {
						x[i][j] = 0;
					}
					
				
					
				}
			}
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (x[i][j] > x[j][i]) {
					x[j][i] = x[i][j];
				}
				if (x[i][j] < x[j][i]) {
					x[i][j] = x[j][i];
				}
			}
		}

		return x;
	}

	
	public static double[][] makecallingmethodmatrix(ArrayList<String> methodlist) {
		int n = methodlist.size();
		double[][] x = new double[n][n];
		for (int i = 0; i < n; i++) {
			List<String> OutboundFeatureList1 = new ArrayList<String>();
			Map<String, Feature> featureMap = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(i))).featureMap;
			OutboundFeatureList1  = featureMap.get(methodlist.get(i)).outboundFeatureList;
						
			for (int j = 0; j < n; j++) {
				if (i != j) {
					if(!(GenerateRefactoringSuggestions.JudgeConstructor(methodlist.get(i))&&!GenerateRefactoringSuggestions.IfIsSameClass(methodlist.get(j), methodlist.get(i)))){
					if(!(GenerateRefactoringSuggestions.JudgeConstructor(methodlist.get(j))&&!GenerateRefactoringSuggestions.IfIsSameClass(methodlist.get(j), methodlist.get(i)))){
					List<String> inboundMethod2 = new ArrayList<String>();
					if (OutboundFeatureList1.contains(methodlist.get(j))) {
						Map<String, Feature> featureMap1 = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(j))).featureMap;
						inboundMethod2  = featureMap1.get(methodlist.get(j)).inboundMethodList;
						
					}
					if (!inboundMethod2.isEmpty()) {
						double zi = 1;
						double mu = inboundMethod2.size();
						x[i][j] = zi / mu;

					} else {
						x[i][j] = 0;
					}
					}
				}
					
				}
			}
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (x[i][j] > x[j][i]) {
					x[j][i] = x[i][j];
				}
				if (x[i][j] < x[j][i]) {
					x[i][j] = x[j][i];
				}
			}
		}

		return x;
	}

	
	public static double[][] makeexecutematrix(ArrayList<String> methodlist) {
		int n = methodlist.size();

		double[][] x = new double[n][n];
		for (int i = 0; i < n; i++) {

			for (int j = i+1; j < n; j++) {

				    ArrayList<String> inboundMethod2 = new ArrayList<String>();
					Map<String, Feature> featureMap1 = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(i))).featureMap;
					inboundMethod2  = featureMap1.get(methodlist.get(i)).inboundMethodList;
					ArrayList<String> inboundMethod3 = new ArrayList<String>();
					Map<String, Feature> featureMap2 = SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodlist.get(j))).featureMap;
					inboundMethod3  = featureMap2.get(methodlist.get(j)).inboundMethodList;
					ArrayList<String> zilistArrayList = tool.testIntersect((ArrayList<String>)inboundMethod2.clone(), (ArrayList<String>)inboundMethod3.clone());
					if (inboundMethod2.isEmpty() && inboundMethod3.isEmpty()) {
						x[i][j] = 0;
					} else {

						double zi = zilistArrayList.size();
						double mu = inboundMethod2.size() + inboundMethod3.size();
						x[i][j] = zi / mu;
						x[j][i] = zi / mu;
					}

			}
		}

		return x;
	}
	
}
