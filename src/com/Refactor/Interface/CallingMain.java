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

package com.Refactor.Interface;

//import java.io.IOException;
//import java.util.ArrayList;
//
//import org.xml.sax.SAXException;
//
//import com.Refactor.AdjustCoefficients.Adjust;
//import com.Refactor.AdjustCoefficients.MainAdjust;
//import com.Refactor.Inheritance.MainInheritance;
//import com.Refactor.Inheritance.RefactorInheritance;
//import com.Refactor.Inheritance.SplitTrees;
//import com.Refactor.Inheritance.extend;
//import com.Refactor.Metrics.MainMetrics;
//import com.Refactor.NonInheritance.CMN;
//import com.Refactor.NonInheritance.GenerateRefactoringSuggestions;
//import com.Refactor.NonInheritance.Main;
//import com.Refactor.NonInheritance.NonInheritanceRefactoring;
//import com.Refactor.NonInheritance.preprocessing;
//import com.Refactor.NonInheritance.semantic;
//import com.jeantessier.dependencyfinder.gui.SaveFileAction;
//import com.mathworks.toolbox.javabuilder.MWException;

public class CallingMain {

//	public static void CMDNEvent() throws IOException, SAXException, MWException{
//		preprocessing.preprocessingClasses(); //预处理操作
//	}
	
//	public static void SetDefaultCoefficients(){
//		NonInheritanceRefactoring.setDefaultCoefficients();
//	}
//	
//	public static void ResetCoefficients(double aa, double bb, double cc, double dd){
//		NonInheritanceRefactoring.ResetCoefficients(aa, bb, cc, dd);
//	}
//	
//	public static void SetThreshold(int a){
//		Adjust.SetAdjustThreshold(a);
//	}
	
//	public static void Refactor1Event() throws IOException, SAXException, MWException{
//		 semantic.findmethodlines();
//		 semantic.extractword();
//		 NonInheritanceRefactoring.result0.clear();
//		 double i = 1;
//		 NonInheritanceRefactoring.persent = 0;
//		 
//		 NonInheritanceRefactoring.TempIndexChechBox();//与学弟对完接口删掉
//		 
//		 for (int p = 0; p < preprocessing.MergeSetList.size(); p++) {
//			 if(NonInheritanceRefactoring.IndexChechBox.contains(p)){
//			 ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
//			 temp = NonInheritanceRefactoring.RefactorOneConnect(preprocessing.MergeSetList.get(p), NonInheritanceRefactoring.a, NonInheritanceRefactoring.b, NonInheritanceRefactoring.c, NonInheritanceRefactoring.d); 
//			 NonInheritanceRefactoring.result0.addAll(temp);
//			 NonInheritanceRefactoring.persent = i/(double)preprocessing.MergeSetList.size();
//			 }
//		 }
//		 NonInheritanceRefactoring.FinalSortSuggestions();//分析最终重构建议
//		 GenerateRefactoringSuggestions.PrintRefactoringSuggestions(NonInheritanceRefactoring.sgs);//打印非继承体系重构建议
//		 NonInheritanceRefactoring.GetQSorttoDrawFigure(0, 0,preprocessing.MergeSetList.size());//撤销操作
//	}
	
//	public static void UndoRefactoringOperations(int unClass, int unEntity){
//		NonInheritanceRefactoring.GetQSorttoDrawFigure(unClass, unEntity ,preprocessing.MergeSetList.size());//撤销操作
//	}
//	
//	public static void DrawCCMatrix(int CCindx) throws SAXException, IOException, MWException{
//		CMN.DrawCCMatrix(CCindx);
//	}
//	
//	public static void TreeEvent() throws IOException{
//		RefactorInheritance.levels =  RefactorInheritance.getInheritanceTreelevels(preprocessing.extendsMatrix.clone());
//		RefactorInheritance.levels =  RefactorInheritance.getinterfaces(RefactorInheritance.levels);
//		RefactorInheritance.levels =  RefactorInheritance.getTreesBeforeRefactoring(RefactorInheritance.levels);//重构前的继承树结构
//	}	
	
//	public static void Refactor2Event() throws IOException, SAXException, MWException{
//		 String log1 = "D:\\Tree.txt";
//		 ArrayList<ArrayList<ArrayList<extend>>> levelsRefactor = (ArrayList<ArrayList<ArrayList<extend>>>)RefactorInheritance.levels.clone();
//		 RefactorInheritance.TempIndexChechBox();
//		 RefactorInheritance.persent = 0;
//		 levelsRefactor = RefactorInheritance.RefactorEachLevel(log1,levelsRefactor,preprocessing.extendsMatrix.clone());	 
//		 ArrayList<SplitTrees> SplitTree = RefactorInheritance.TidyResults(levelsRefactor);//需要被分解的树 信息
//		 SplitTree = RefactorInheritance.CalculatInheritanceTreesQ(SplitTree,levelsRefactor);
//		 RefactorInheritance.rv.levelsRefactor = levelsRefactor;
//		 RefactorInheritance.rv.SplitTree = SplitTree;
//		 RefactorInheritance.CalculatInheritanceTreesDetaQ(0);
//	}
	
	
//	public static void UndoTreeRefactoringOperations(int uTree){
//		RefactorInheritance.CalculatInheritanceTreesDetaQ(uTree);//撤销操作
//	}
//	
//	public static void MetricsEvent(){
////	 MainMetrics.MainBeforeOrAfterRefactorMetrics(RefactorInheritance.rv.levelsRefactor, NonInheritanceRefactoring.result0);
//	}
	
//	public static void CoefficientEvent() throws SAXException, IOException, MWException{
////	 MainAdjust.AdjustCoefficients();
//	}
	
//	public static void GoEvent() throws SAXException, IOException, MWException{
//    Main.mainNoninheritance();    
//    MainInheritance.InheritanceOperations();
//	}
}