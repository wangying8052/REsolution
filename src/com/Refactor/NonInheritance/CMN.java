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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import com.Refactor.AdjustCoefficients.returnrank;
import com.Refactor.Inheritance.returnvalue;
import com.Refactor.classparser.*;
import com.jeantessier.dependencyfinder.gui.SrcAction;
import com.mathworks.toolbox.javabuilder.MWException;

public class CMN {
	
	public static int Th2 =1;   //默认是0     0：0   1：平均值    2：最大值

	public static ArrayList<String> getAllMehthodnames(String filename)
			throws IOException {
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

	public static double[][] getAllLineFromFile(String filename)
			throws IOException {
		// 从文档中读取矩阵
		int n = 0;
		ArrayList<String> MarixzeroList = new ArrayList<String>();
		File file = new File(filename);
		FileReader fr = new FileReader(file.getPath());
		BufferedReader br = new BufferedReader(fr);
		String ss = null;
		while ((ss = br.readLine()) != null) {
			MarixzeroList.add(ss);

		}
		fr.close();

		n = MarixzeroList.size();

		double[][] x = new double[n][n];

		for (int k = 0; k < n; k++) {

			String co = MarixzeroList.get(k);
			String str[] = new String[n];
			str = co.split(" ");
			for (int t = 0; t < n; t++) {
				x[k][t] = Double.valueOf(str[t]);
			}

		}

		return x;

	}

	public static double[][] MatrixDotMultiply(double a, double[][] matrix) {

		double[][] result = new double[matrix.length][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				result[i][j] = a * matrix[i][j];
			}
		}

		return result;

	}

	public static double[][] MatrixPlus(double[][] matrix1, double[][] matrix2) {

		double[][] result = new double[matrix1.length][matrix1.length];
		for (int i = 0; i < matrix1.length; i++) {
			for (int j = 0; j < matrix1.length; j++) {
				result[i][j] = matrix1[i][j] + matrix2[i][j];
			}
		}

		return result;

	}
	public static ArrayList<ArrayList<String>> initialCNSclss(ArrayList<ArrayList<String>> cns, ArrayList<String> classname) {
		int init = classname.size();
		for (int i = 0; i < init; i++) {
			ArrayList<String> cn = new ArrayList<String>();
			cn.add(classname.get(i));
			cns.add(cn);
		}
		return cns;

	}
	

	public static double[][] initialQclss(ArrayList<ArrayList<String>> cns,
			ArrayList<String> methodname, double[][] A, double W) {
		double[][] Q = new double[A.length][A.length];
		int init = A.length;

		for (int i = 0; i < init; i++) {
			for (int j = 0; j < init; j++) {
				if (i != j) {
					if (A[i][j] != 0) {
						Q[i][j] = get_S_in(cns, methodname, i, j, A) / (2 * W) - get_S(cns, methodname, A, i)* get_S(cns, methodname, A, j) / (4 * W * W);
			
					} else {
						Q[i][j] = 0;
					}
				} else {
					Q[i][j] = 0;
				}
			}
		}

		return Q;

	}
	
	
	
	
	public static double[][] initialQclss2(ArrayList<ArrayList<String>> cns,
			ArrayList<String> methodname, int[][] A, double W) {
		double[][] Q = new double[A.length][A.length];
		int init = A.length;

		for (int i = 0; i < init; i++) {
			for (int j = 0; j < init; j++) {
				if (i != j) {
					if (A[i][j] != 0) {
						Q[i][j] = get_S_in2(cns, methodname, i, j, A) / (2 * W) - get_S2(cns, methodname, A, i)* get_S2(cns, methodname, A, j) / (4 * W * W);
					} else {
						Q[i][j] = 0;
					}
				} else {
					Q[i][j] = 0;
				}
			}
		}

		return Q;

	}
	
	
	public static double[][] updateQclss(ArrayList<ArrayList<String>> cns,
			ArrayList<String> methodname, double[][] A, double[][] Q, int i,
			int j, Double W) {

		for (int k = 0; k < Q.length; k++) {
			if (k != j) {
				
				double ki = get_S_in(cns, methodname, k, i, A);
				double kj = get_S_in(cns, methodname, k, j, A);
				if (ki != 0 && kj != 0) {
					Q[j][k] = Q[i][k] + Q[j][k];
					Q[k][j] = Q[j][k];
				}
				if (ki != 0 && kj == 0) {
					Q[j][k] = Q[i][k] - 2 * get_S(cns, methodname, A, j)
							* get_S(cns, methodname, A, k) / (4 * W * W);
					Q[k][j] = Q[j][k];
				}
				if (kj != 0 && ki == 0) {
					Q[j][k] = Q[j][k] - 2 * get_S(cns, methodname, A, i)
							* get_S(cns, methodname, A, k) / (4 * W * W);
					Q[k][j] = Q[j][k];
				}

			} else {
				Q[k][j] = -1;
			}
		}
		Q = delt_i_j_Q(Q, i);

		
		
		
		return Q;

	}
	
	
	
	public static double[][] updateQclss2(ArrayList<ArrayList<String>> cns,
			ArrayList<String> methodname, int[][] A, double[][] Q, int i,
			int j, Double W) {

		for (int k = 0; k < Q.length; k++) {
			if (k != j) {
				
				double ki = get_S_in2(cns, methodname, k, i, A);
				double kj = get_S_in2(cns, methodname, k, j, A);
				if (ki != 0 && kj != 0) {
					Q[j][k] = Q[i][k] + Q[j][k];
					Q[k][j] = Q[j][k];
				}
				if (ki != 0 && kj == 0) {
					Q[j][k] = Q[i][k] - 2 * get_S2(cns, methodname, A, j)
							* get_S2(cns, methodname, A, k) / (4 * W * W);
					Q[k][j] = Q[j][k];
				}
				if (kj != 0 && ki == 0) {
					Q[j][k] = Q[j][k] - 2 * get_S2(cns, methodname, A, i)
							* get_S2(cns, methodname, A, k) / (4 * W * W);
					Q[k][j] = Q[j][k];
				}

			} else {
				Q[k][j] = -1;
			}
		}
		Q = delt_i_j_Q(Q, i);

		
		
		
		return Q;

	}
	
	public static ArrayList<ArrayList<String>> CommunityDetectionNoextentsClass(int[][] x, int threhold) {

		
	
		int[][] A = x;
		ArrayList<ArrayList<String>> cns = new ArrayList<ArrayList<String>>();
		// 社区结构初始化操作
		cns = initialCNSclss(cns, SrcAction.classname);
		double[][] Q = new double[A.length][A.length];
		double W = get_W1(A);
		Q = initialQclss2(cns, SrcAction.classname, A, W);
		double MQ = 0;// 记录模块度

		while (cns.size()> threhold) {

			int id_i = get_Qmaxcls(Q,cns).i;
			int id_j = get_Qmaxcls(Q,cns).j;
			
			if(id_i!=id_j){
			Q = updateQclss2(cns, SrcAction.classname, A, Q, id_i, id_j, W);
			cns = mergeCNS_i_j(cns, id_i, id_j);
			
			MQ = MQ + get_Qmaxcls(Q,cns).max;
			}
			else{
				threhold++;
			}

		}

		return cns;

	}
	
	
	public static ArrayList<ArrayList<String>> dealgetsetattr(ArrayList<ArrayList<String>> cns,ArrayList<String> methodlist){
		for(int i = 0 ; i <methodlist.size(); i ++ ){
			String getString1 = null; 
			String setString1 = null; 
			if(!methodlist.get(i).contains("(")&&!methodlist.get(i).contains("{")){

				String getString = "get"+SourceParser.Shortmethodonlyname(methodlist.get(i));
				String setString = "set"+SourceParser.Shortmethodonlyname(methodlist.get(i));

				for(int k = 0 ; k <methodlist.size(); k ++ ){
					if(methodlist.get(k).contains("(")){
						if(SourceParser.Shortmethodonlyname(methodlist.get(k)).equalsIgnoreCase(setString)&&SourceParser.Getonlyclassname(methodlist.get(k)).equals(SourceParser.Getonlyclassname(methodlist.get(i)))){
							setString1 = methodlist.get(k);
						}
						if(SourceParser.Shortmethodonlyname(methodlist.get(k)).equalsIgnoreCase(getString)&&SourceParser.Getonlyclassname(methodlist.get(k)).equals(SourceParser.Getonlyclassname(methodlist.get(i)))){
							getString1 = methodlist.get(k);
						}
					}
				}
				
				ArrayList<String> attrgetset = new ArrayList<String>();
				if(getString1!=null){
				attrgetset.add(getString1);
				}
				if(setString1!=null){
				attrgetset.add(setString1);
				}
				attrgetset.add(methodlist.get(i));
				
				if(attrgetset.size()>1){
					boolean flag = false;
					for(int ii = 0 ; ii < cns.size(); ii++){
						if(cns.get(ii).size()>1){
							ArrayList<String> inSet = tool.testIntersect((ArrayList<String>)cns.get(ii).clone(), (ArrayList<String>)attrgetset.clone());
							if(!inSet.isEmpty()){
							if(inSet.size() < attrgetset.size()){
								flag = true;
								ArrayList<String> temp = tool.testUnion((ArrayList<String>)cns.get(ii).clone(), (ArrayList<String>)attrgetset.clone());
								ArrayList<ArrayList<String>> cns1 = new  ArrayList<ArrayList<String>>();
								cns1.add(temp);
								for(int p = 0; p<cns.size();p++){
								if(tool.testIntersect((ArrayList<String>)cns.get(p).clone(), (ArrayList<String>)temp).isEmpty()){
									cns1.add(cns.get(p));
								}
								}
								cns = cns1;
								break;
							}
						}
						}
					}
					
					if(!flag){
						ArrayList<ArrayList<String>> cns1 = new  ArrayList<ArrayList<String>>();
						cns1.add(attrgetset);
						for(int p = 0; p<cns.size();p++){
						if(tool.testIntersect((ArrayList<String>)cns.get(p).clone(), (ArrayList<String>)attrgetset).isEmpty()){
							cns1.add(cns.get(p));
						}
						}
						cns = cns1;
					}
					
					
					
				}
				
				
			}//如果是属性

			
		}
		return cns;		
	}
	
	
	public static ArrayList<ArrayList<String>> initialCNS(ArrayList<ArrayList<String>> cns, ArrayList<ArrayList<String>> methodbukefen,ArrayList<String> methodduli) {
		int init1 = methodbukefen.size();
		
		for (int i = 0; i < init1; i++) {
			ArrayList<String> cn = new ArrayList<String>();
			cn.addAll(methodbukefen.get(i));
			cns.add(cn);
			
		}
	
		int init2 = methodduli.size();
		
		for (int i = 0; i < init2; i++) {
			ArrayList<String> cn = new ArrayList<String>();
			cn.add(methodduli.get(i));
			cns.add(cn);
			// cns.get(i).add(classname.get(i));
		}
		

	
		return cns;

	}
	
	public static ArrayList<ArrayList<String>> initialCNS1(ArrayList<ArrayList<String>> cns,ArrayList<String> methodduli) {
		int init2 = methodduli.size();
		
		for (int i = 0; i < init2; i++) {
			ArrayList<String> cn = new ArrayList<String>();
			cn.add(methodduli.get(i));
			cns.add(cn);
			
		}
	
		return cns;

	}
	
	public static double[][] updateQ(ArrayList<ArrayList<String>> cns,
			ArrayList<String> methodname, double[][] A, double[][] Q, int i,
			int j, Double W) {

		for (int k = 0; k < Q.length; k++) {
			if (k != j) {
				double ki = get_S_in(cns, methodname, k, i, A);
				double kj = get_S_in(cns, methodname, k, j, A);
				if (ki != 0 && kj != 0) {
					Q[j][k] = Q[i][k] + Q[j][k];
					Q[k][j] = Q[j][k];
				}
				if (ki != 0 && kj == 0) {
					Q[j][k] = Q[i][k] - 2 * get_S(cns, methodname, A, j)
							* get_S(cns, methodname, A, k) / (4 * W * W);
					Q[k][j] = Q[j][k];
				}
				if (kj != 0 && ki == 0) {
					Q[j][k] = Q[j][k] - 2 * get_S(cns, methodname, A, i)
							* get_S(cns, methodname, A, k) / (4 * W * W);
					Q[k][j] = Q[j][k];
				}
			} else {
				Q[k][j] = 0;
			}
		}
		Q = delt_i_j_Q(Q, i);
		return Q;

	}
	public static ArrayList<ArrayList<String>> CommunityDetectionNoextentsAdjust(double[][] A, ArrayList<String> methodlist) {

		ArrayList<ArrayList<String>> cns = new ArrayList<ArrayList<String>>();
		cns = CMN.initialCNSclss(cns, methodlist);
		double[][] Q = new double[A.length][A.length];
		double W = CMN.get_W(A);
		Q = CMN.initialQ(cns, methodlist, A, W);
		double MQ = 0;// 记录模块度
		while (CMN.get_Qmax(Q).max > 0) {
			int id_i = CMN.get_Qmax(Q).i;
			int id_j = CMN.get_Qmax(Q).j;

			Q = CMN.updateQ(cns, methodlist, A, Q, id_i, id_j, W);
			cns = CMN.mergeCNS_i_j(cns, id_i, id_j);
			MQ = MQ + CMN.get_Qmax(Q).max;
		}

	//	System.out.println("模块度=" + MQ);
		return cns;

	}
	
	public static ArrayList<ArrayList<String>> CommunityDetectionNoextentsMethods(double[][] A, ArrayList<String> methodlist,ArrayList<ArrayList<String>> methodbukefen,
			ArrayList<String> methodduli) {

				CMN cmn = new CMN();

				ArrayList<ArrayList<String>> cns = new ArrayList<ArrayList<String>>();
				// 社区结构初始化操作
				cns = cmn.initialCNS(cns, methodbukefen, methodduli);

				double[][] Q = new double[A.length][A.length];
				double W = cmn.get_W(A);
				Q = cmn.initialQ(cns, methodlist, A, W,methodbukefen,methodduli);
				double MQ = 0;// 记录模块度
				while (cmn.get_Qmax(Q).max > 0) {
					 
					int id_i = cmn.get_Qmax(Q).i;
					int id_j = cmn.get_Qmax(Q).j;

					Q = cmn.updateQ(cns, methodlist, A, Q, id_i, id_j, W,methodbukefen,methodduli);
					cns = cmn.mergeCNS_i_j(cns, id_i, id_j);
					MQ = MQ + cmn.get_Qmax(Q).max;
				}

			
				return cns;

			}
	
	public static ArrayList<ArrayList<String>> mergeMore(ArrayList<ArrayList<String>> cns){
		
		ArrayList<Integer> id = new ArrayList<Integer>();
		int cumt = 0 ; 
		for(int i = 0; i<cns.size(); i++){

			for(int j = 0; j < cns.size(); j++){
				if((i!=j)&&!IsintheSameClass(cns.get(i), cns.get(j))){
					cumt++;
					id.add(j);
				}
			}
			
			if(cumt>3){
				break;
			}
			
			
		}
		
			ArrayList<String> temp = new ArrayList<String>();
			ArrayList<ArrayList<String>>  cns1 = new ArrayList<ArrayList<String>> ();
			boolean flag =false;
			if(cumt>3){
				
				for(int k = 0; k < id.size(); k++){
					temp.addAll(cns.get(id.get(k)));
				}
				
				for(int h = 0; h < cns.size(); h++){
					if(!id.contains(h)){
						cns1.add(cns.get(h));
					}
				}
				cns1.add(temp);
				flag = true;
			}else{
				cns1 = cns;
			}
		
			
			if(flag){
			int cumt1 = 0 ; 
			for(int i = 0; i<cns1.size(); i++){

				for(int j = 0; j < cns1.size(); j++){
					
					if((i!=j)&&!IsintheSameClass(cns1.get(i), cns1.get(j))){
						cumt1++;
					
					}
				}
				
				if(cumt1>3){
					break;
				}
				
				
			}
			
			if(cumt1>3){
				cns1 =  mergeMore(cns1);
			}
					}
			
		return cns1;
		
	}
	
	
	public static boolean IsintheSameClass(ArrayList<String> cnsi,  ArrayList<String> cnsj) {//不属于同一个类返回true
		boolean same = false;
		for(int i = 0; i < cnsi.size(); i++){
			for(int j = 0; j < cnsj.size(); j++){
				if(!SourceParser.Getonlyclassname(cnsi.get(i)).equals(SourceParser.Getonlyclassname(cnsj.get(j)))){
				
					same = true;
				}
			}	
		}
		return same;
	}
	
	
	
	
	public static returnvalue  CommunityDetectionNoextentsNOFULEI(double[][] A, ArrayList<String> methodlist,ArrayList<ArrayList<String>> methodbukefen,ArrayList<String> methodduli) {

		returnvalue rv = new returnvalue();
		ArrayList<ArrayList<String>> cns = new ArrayList<ArrayList<String>>();
		// 社区结构初始化操作
		cns = CMN.initialCNS(cns, methodbukefen, methodduli);
		cns = dealgetsetattr(cns,methodlist);

		double[][] Q = new double[cns.size()][cns.size()];
		double W = CMN.get_W(A);
		Q = CMN.initialQ(cns, methodlist, A, W,methodbukefen,methodduli);
		double MQ = 0;// 记录模块度
		while (CMN.get_Qmax(Q).max > 0) {
			 
			int id_i = CMN.get_Qmax(Q).i;
			int id_j = CMN.get_Qmax(Q).j;

			Q = CMN.updateQ(cns, methodlist, A, Q, id_i, id_j, W,methodbukefen,methodduli);

			cns = CMN.mergeCNS_i_j(cns, id_i, id_j);
			MQ = MQ +CMN.get_Qmax(Q).max;
		}
		
		rv.cns = cns;
		rv.MQ = MQ;
		return rv;

	}
	
	public static ArrayList<String> findUndo(ArrayList<String> cnsi,  ArrayList<String> cnsj) {
		ArrayList<String> undo = new ArrayList<String>();
		for(int i = 0; i < cnsi.size(); i++){
			for(int j = 0; j < cnsj.size(); j++){
				if(!SourceParser.Getonlyclassname(cnsi.get(i)).equals(SourceParser.Getonlyclassname(cnsj.get(j)))){
				}
			}	
		}
		return undo;
	}
	
	
	public static int getCNSNumber(ArrayList<ArrayList<String>> cns){
		
		int num = 0;
		for(int i = 0; i < cns.size(); i++){
			num = cns.get(i).size() + num;
		}
		return num;
		
	}
	
	
	public static boolean IfOnlySemantic(double[][] xshare, double[][] ycall, double[][] zexe, double[][] wsem, int i, int j){
		boolean flag = false;
		if((xshare[i][j]==0)&&(ycall[i][j]==0)&&(zexe[i][j]==0)&&(wsem[i][j]!=0)){
			flag = true;
		}
		return flag;
	}
	
	
	public static double CalculateWk(ArrayList<String> cn,double[][] A, ArrayList<String> methodlist){
		double Wk = 0;
		for(int i = 0; i < cn.size(); i++){
			for(int j = 0; j < cn.size(); j++){
				int id = methodlist.indexOf(cn.get(i));
				int jd = methodlist.indexOf(cn.get(j));
				Wk = A[id][jd] + Wk;
			}
		}
		return Wk;
	}
	
	public static double CalculateSk(ArrayList<String> cn,double[][] A, ArrayList<String> methodlist){
		double Sk = 0;
		for(int i = 0; i < cn.size(); i++){
			for(int j = 0 ; j < methodlist.size(); j++){
			Sk = Sk + A[methodlist.indexOf(cn.get(i))][j] + A[j][methodlist.indexOf(cn.get(i))];
			}
			}
		return Sk;
	}
	
	public static double CalculateOrigSystemQ(ArrayList<ArrayList<String>> cns,double[][] A, ArrayList<String> methodlist){
		double Q = 0;
		for(int i = 0; i < cns.size(); i++){
			Q = Q + (CalculateWk(cns.get(i),A,methodlist)/get_W(A) - Math.pow((CalculateSk(cns.get(i),A,methodlist)/(2*get_W(A))),2));
		}
		
		
		return Q;		
	}
	
	public static boolean JudgeMoveEntityoperations(ArrayList<String> cns1,ArrayList<String> cns2){//返回true 不合并
		boolean flag = false;
		ArrayList<String> cnsClass1 = new ArrayList<String>();
		ArrayList<String> cnsClass2 = new ArrayList<String>();
		ArrayList<Integer> cnsid1 = new ArrayList<Integer>();
		ArrayList<Integer> cnsid2 = new ArrayList<Integer>();
		int T = 4;
		if(Th2==0){
			T = 10;
		}
		for(int i = 0; i < cns1.size(); i++){
			if(cnsClass1.isEmpty()){
				cnsClass1.add(SourceParser.Getonlyclassname(cns1.get(i)));
				if(cns1.get(i).contains("(")||cns1.get(i).contains("{")){
				cnsid1.add(1);
				}
				if(!cns1.get(i).contains("(")&&!cns1.get(i).contains("{")){
				cnsid1.add(0);
				}
			}else{
				if(!cnsClass1.contains(SourceParser.Getonlyclassname(cns1.get(i)))){
					cnsClass1.add(SourceParser.Getonlyclassname(cns1.get(i)));
					if(cns1.get(i).contains("(")||cns1.get(i).contains("{")){
						cnsid1.add(1);
						}
						if(!cns1.get(i).contains("(")&&!cns1.get(i).contains("{")){
						cnsid1.add(0);
						}
				}else{
					if(cns1.get(i).contains("(")||cns1.get(i).contains("{")){
					cnsid1.set(cnsClass1.indexOf(SourceParser.Getonlyclassname(cns1.get(i))), cnsid1.get(cnsClass1.indexOf(SourceParser.Getonlyclassname(cns1.get(i))))+1);
					}
				}
			}
		}
		
		
		for(int i = 0; i < cns2.size(); i++){
			if(cnsClass2.isEmpty()){
				cnsClass2.add(SourceParser.Getonlyclassname(cns2.get(i)));
				if(cns2.get(i).contains("(")||cns2.get(i).contains("{")){
				cnsid2.add(1);
				}
				if(!cns2.get(i).contains("(")&&!cns2.get(i).contains("{")){
				cnsid2.add(0);
				}
			}else{
				if(!cnsClass2.contains(SourceParser.Getonlyclassname(cns2.get(i)))){
					cnsClass2.add(SourceParser.Getonlyclassname(cns2.get(i)));
					if(cns2.get(i).contains("(")||cns2.get(i).contains("{")){
						cnsid2.add(1);
						}
						if(!cns2.get(i).contains("(")&&!cns2.get(i).contains("{")){
						cnsid2.add(0);
						}
				}else{
					if(cns2.get(i).contains("(")||cns2.get(i).contains("{")){
					cnsid2.set(cnsClass2.indexOf(SourceParser.Getonlyclassname(cns2.get(i))), cnsid2.get(cnsClass2.indexOf(SourceParser.Getonlyclassname(cns2.get(i))))+1);
					}
				}
			}
		}
		
		int max1 = 0;
		int min1 =0;
		for(int i = 0; i < cnsid1.size(); i++){
			
			if(cnsid1.get(i)>max1){
				max1 = cnsid1.get(i);
			}
			if(cnsid1.get(i)<min1){
				min1 = cnsid1.get(i);
			}
		}
		
		
		
		int max2 = 0;
		int min2 =0;
		for(int i = 0; i < cnsid2.size(); i++){
			
			if(cnsid2.get(i)>max2){
				max2 = cnsid2.get(i);
			}
			if(cnsid2.get(i)<min2){
				min2 = cnsid2.get(i);
			}
		}
		
		if(max1 >= max2){
			for(int i = 0; i < cnsid2.size(); i++){
				if(cnsid2.get(i)>= T){
					flag = true;
					break;
				}
			}
		}
		if(max2 > max1){
			for(int i = 0; i < cnsid1.size(); i++){
				if(cnsid1.get(i)>= T){
					flag = true;
					break;
				}
			}
		}
		
		return flag;	
	}
	
	
	public static returnvalue CommunityDetectionNoextentsInheritance(double[][] A,  ArrayList<String> methodlist) {

		returnvalue rv = new returnvalue();
		ArrayList<ArrayList<String>> cns = new ArrayList<ArrayList<String>>();
		// 社区结构初始化操作
		cns = CMN.initialCNS1(cns,  methodlist);
		cns = dealgetsetattr(cns,methodlist);
		double[][] Q = new double[cns.size()][cns.size()];
		double W = CMN.get_W(A);
		Q = CMN.initialQInher(cns, methodlist, A, W);
		double MQ = 0;// 记录模块度

		while (CMN.get_Qmax(Q).max > 0) {
			int id_i = CMN.get_Qmax(Q).i;
			int id_j = CMN.get_Qmax(Q).j;

			Q = CMN.updateQ1(cns, methodlist, A, Q, id_i, id_j, W);

			cns = CMN.mergeCNS_i_j(cns, id_i, id_j);

			MQ = MQ + CMN.get_Qmax(Q).max;
		}
		rv.cns = cns;
		rv.MQ = MQ;

		return rv;

	}

	
	public static ArrayList<ArrayList<String>> Mergemore(ArrayList<ArrayList<String>> cns, int[] sortcnt){
		ArrayList<String>  tmp = new ArrayList<String>();
		ArrayList<ArrayList<String>> cns1 = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < sortcnt.length; i++){
			tmp.addAll(cns.get(sortcnt[i]));
		}

		for(int i = 0; i < cns.size(); i++){
			if(tool.testIntersect((ArrayList<String>) tmp.clone(),(ArrayList<String>) cns.get(i).clone()).isEmpty()){
				cns1.add(cns.get(i));
			}
		}
		cns1.add(tmp);
		return cns1;
		
	}
	
	public static ArrayList<ArrayList<String>> dealExtractmoreclasses(ArrayList<ArrayList<String>> cns){
		for(int i = 0; i < SrcAction.classname.size(); i++){
			ArrayList<Integer> id = new ArrayList<Integer>();
			ArrayList<Integer> cnt = new ArrayList<Integer>();
			ArrayList<Integer> chun = new ArrayList<Integer>();
			int chunc = 0;
			for(int k = 0; k < cns.size(); k++){
				int ct = 0;
				for(int t = 0; t < cns.get(k).size(); t++){
					if(SourceParser.Getonlyclassname(cns.get(k).get(t)).equals(SrcAction.classname.get(i))){
						ct++;
					}
				}
				if(ct>0){
					id.add(k);
					cnt.add(ct);
					if(ct!=cns.get(k).size()){
					chun.add(0);
					}else{
						chun.add(1);
						chunc++;
					}
				}
			}
			if(id.size()>3){

				if(chunc > (id.size()-3 +1)){
					 int[] sortid = new int [chunc];
					 int[] sortcnt = new int [chunc];
					 int in = 0;
					 for(int u = 0 ; u < chun.size();u++){
						 if (chun.get(u) ==1){

							 sortid[in] = id.get(u);
							 
							 sortcnt [in] = cnt.get(u);
							 in++;
						 }
					 }
					 sortid = semantic.bubsort(sortcnt, sortid);
					 int[] sortid1 = new int [id.size()-3 +1];
					 for(int m = 0; m < (id.size()-3 +1); m++){
						 sortid1[m] = sortid[m];
					 }
					 cns = Mergemore(cns, sortid1) ;
				}else{
					 int[] sortid = new int [chunc];
					 int[] sortcnt = new int [chunc];
					 int in = 0;
					 for(int u = 0 ; u < chun.size();u++){
						 if (chun.get(u) ==1){
							 sortid[in] = id.get(u);
							 sortcnt [in] = cnt.get(u);
							 in++;
						 }
					 }
					 sortid = semantic.bubsort(sortcnt, sortid);
					 cns = Mergemore(cns, sortid) ;
				}
			}
		}
		return cns;
	}
	
	
	public static returnvalue CommunityDetectionNoextentsFuleifenjie(double[][] A, ArrayList<String> methodlist, ArrayList<ArrayList<String>> methodbukefen, ArrayList<String> methodduli) {

				returnvalue rv = new returnvalue();

				ArrayList<ArrayList<String>> cns = new ArrayList<ArrayList<String>>();

				// 社区结构初始化操作
				cns = CMN.initialCNS(cns, methodbukefen, methodduli);
				cns = dealgetsetattr(cns,methodlist);

				double[][] Q = new double[cns.size()][cns.size()];
				double W = CMN.get_W(A);
				Q = CMN.initialQ(cns, methodlist, A, W,methodbukefen,methodduli);
				double MQ = 0;// 记录模块度

				while (CMN.get_Qmax(Q).max > 0) {
					 
					int id_i = CMN.get_Qmax(Q).i;
					int id_j = CMN.get_Qmax(Q).j;

					Q = CMN.updateQ(cns, methodlist, A, Q, id_i, id_j, W,methodbukefen,methodduli);

					cns = CMN.mergeCNS_i_j(cns, id_i, id_j);
					MQ = MQ + CMN.get_Qmax(Q).max;
				}
				rv.cns = cns;
				rv.MQ = MQ;

				return rv;

			}

	public static ArrayList<double[][]> DrawCCMatrix(int CCindx)
			throws SAXException, IOException, MWException {
		ArrayList<String> methodlist = preprocessing.MergeSetList.get(CCindx).methodlist;
		ArrayList<double[][]> resultlist = new ArrayList<double[][]>();

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
		if (n > 0) {
			xshare = NonInheritanceRefactoring.makeshareattributmatrix(methodlist);
			ycall = NonInheritanceRefactoring.makecallingmethodmatrix(methodlist);
			zexe = NonInheritanceRefactoring.makeexecutematrix(methodlist);
			// 语义相似度矩阵
			wsem = semantic.makesemanticmatrix(methodlist);
			wsem = semantic.filterSemantic1(wsem);
		}

		resultlist.add(xshare);
		resultlist.add(ycall);
		resultlist.add(zexe);
		resultlist.add(wsem);

		return resultlist;
	}
	
	public static ArrayList<ArrayList<String>> CommunityDetectionNoextentsCondition(double a, double b, double c, double d, ArrayList<String> methodlist, 
			ArrayList<ArrayList<String>> methodbukefen, ArrayList<String> methodduli,ArrayList<ArrayList<String>> methodlistoriginalpart) throws IOException, SAXException, MWException {
		      
		        ArrayList<String> methodlist1 = (ArrayList<String>)methodlist.clone();
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
					wsem = semantic.filterSemantic(wsem);
					wsem = semantic.filterSemanticBetweenclass(wsem,methodlist,xshare,ycall);
					
				}

				}
				if(!tool.JudgeCoeffient(a, b, c, d)){
				A = MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixDotMultiply(2,MatrixComputing.MatrixDotMultiply(a, xshare)),MatrixComputing.MatrixDotMultiply(b, ycall)),MatrixComputing.MatrixDotMultiply(c, zexe)),MatrixComputing.MatrixDotMultiply(0, MatrixComputing.MatrixDotMultiply(d, wsem)));
				}else{
				A = MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixDotMultiply(2,MatrixComputing.MatrixDotMultiply(a, xshare)),MatrixComputing.MatrixDotMultiply(b, ycall)),MatrixComputing.MatrixDotMultiply(c, zexe)),MatrixComputing.MatrixDotMultiply(d, wsem));	
				}
				Main.A = A;
				ArrayList<ArrayList<String>> cns = new ArrayList<ArrayList<String>>();
				cns = initialCNS(cns, methodbukefen, methodduli);

				cns = dealgetsetattr(cns,methodlist);
		
				double W = get_W(A);
				
				double[][] Q = initialQ(cns, methodlist, A, W, methodbukefen, methodduli);
				double MQ = 0;// 记录模块度
				double threshold = 0;
				double max = 0;
				int count = 0;
				while (get_QmaxCondition(Q, cns).max > 0) {
					 
					int id_i = get_QmaxCondition(Q, cns).i;
					int id_j = get_QmaxCondition(Q, cns).j;
					MQ = MQ + get_QmaxCondition(Q, cns).max;

					if(max < get_QmaxCondition(Q, cns).max){
						max = get_QmaxCondition(Q, cns).max;
					}
					
					Q = updateQ(cns, methodlist, A, Q, id_i, id_j, W,methodbukefen,methodduli);
					cns = mergeCNS_i_j(cns, id_i, id_j);

					count++;
				}

				threshold = tool.th2(threshold, max);
				//搬移函数重构
				while ((get_Qmax(Q).max >threshold)&&(!JudgeMoveEntityoperations(cns.get(get_Qmax(Q).i),cns.get(get_Qmax(Q).j)))) {
					int id_i = get_Qmax(Q).i;
					int id_j = get_Qmax(Q).j;
					MQ = MQ + get_Qmax(Q).max;
					Q = updateQ(cns, methodlist, A, Q, id_i, id_j, W,methodbukefen,methodduli);
					cns = mergeCNS_i_j(cns, id_i, id_j);
				}

				if(!GenerateRefactoringSuggestions.IfScatter(methodlist1)){
				      cns = GenerateRefactoringSuggestions.MergeScatterMethods(cns, A.length, methodbukefen);
			    }
				Q = initialQCondition(cns, methodlist,  A,  W, methodbukefen);
				
				
				while (get_QmaxCondition(Q, cns).max > 0) {

					int id_i = get_QmaxCondition(Q, cns).i;
					int id_j = get_QmaxCondition(Q, cns).j;
					MQ = MQ + get_QmaxCondition(Q, cns).max;
					Q = updateQ(cns, methodlist, A, Q, id_i, id_j, W,methodbukefen,methodduli);
					cns = mergeCNS_i_j(cns, id_i, id_j);
				}
				cns = dealExtractmoreclasses(cns);
				return cns;

			}

	
	public static ArrayList<ArrayList<String>> CommunityDetectionNoextentsCondition1(double a, double b, double c, double d, ArrayList<String> methodlist, 
			ArrayList<ArrayList<String>> methodbukefen, ArrayList<String> methodduli) throws IOException, SAXException, MWException {
				ArrayList<String> methodlist1 = (ArrayList<String>)methodlist.clone();

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
					wsem = semantic.filterSemantic(wsem);
				
					
				}

				}
				ArrayList<ArrayList<String>> cns = new ArrayList<ArrayList<String>>();
				if(!tool.JudgeCoeffient(a, b, c, d)){
				A = MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixDotMultiply(2,MatrixComputing.MatrixDotMultiply(a, xshare)),MatrixComputing.MatrixDotMultiply(b, ycall)),MatrixComputing.MatrixDotMultiply(1,MatrixComputing.MatrixDotMultiply(c, zexe))),MatrixComputing.MatrixDotMultiply(0, MatrixComputing.MatrixDotMultiply(d, wsem)));
				}else{
					A = MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixPlus(MatrixComputing.MatrixDotMultiply(2,MatrixComputing.MatrixDotMultiply(a, xshare)),MatrixComputing.MatrixDotMultiply(b, ycall)),MatrixComputing.MatrixDotMultiply(1,MatrixComputing.MatrixDotMultiply(c, zexe))),MatrixComputing.MatrixDotMultiply(d, wsem));
				}
				Main.A = A;
					
				
				cns = initialCNS(cns, methodbukefen, methodduli);
				cns = dealgetsetattr(cns,methodlist);
			
		
				double W = get_W(A);
				
				double[][] Q = initialQ(cns, methodlist, A, W, methodbukefen, methodduli);
				double MQ = 0;// 记录模块度
				while (get_Qmax(Q).max > 0) {
					int id_i = get_Qmax(Q).i;
					int id_j = get_Qmax(Q).j;
					MQ = MQ + get_Qmax(Q).max;
					
					Q = updateQ(cns, methodlist, A, Q, id_i, id_j, W,methodbukefen,methodduli);
					cns = mergeCNS_i_j(cns, id_i, id_j);
					
				}
				BigDecimal   bb   =   new   BigDecimal(d);  
				double   f1   =   bb.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  

				if(f1 == 0&&cns.size()>3){
					
				}else{
				if(!GenerateRefactoringSuggestions.IfScatter(methodlist1)){
				      cns = GenerateRefactoringSuggestions.MergeScatterMethods(cns, A.length, methodbukefen);
			    }
				CMN.printCNS(cns);
				cns = tool.scatter (cns, a, b, c, d, f1);
				}
				return cns;

			}

	
	public static double[][] initialQ1(ArrayList<ArrayList<String>> cns, ArrayList<String> methodname, double[][] A, double W) {
		double[][] Q = new double[cns.size()][cns.size()];
		int init = A.length;

		for (int i = 0; i < init; i++) {
			for (int j = 0; j < init; j++) {
				if (i != j) {
					if (A[i][j] != 0) {
						Q[i][j] = get_S_in(cns, methodname, i, j, A) / (2 * W)
								- get_S(cns, methodname, A, i)
								* get_S(cns, methodname, A, j) / (4 * W * W);
					} else {
						Q[i][j] = 0;
					}
				} else {
					Q[i][j] = 0;
				}
			}
		}

		return Q;

	}

	public static double[][] initialQInher(ArrayList<ArrayList<String>> cns, ArrayList<String> methodname, double[][] A, double W) {
		double[][] Q = new double[cns.size()][cns.size()];
		int init = Q.length;

		
		for (int i = 0; i < init; i++) {
		
			for (int j = 0; j < init; j++) {
			
				if (i != j) {

					if (get_S_in(cns, methodname, i, j, A) != 0) {
						Q[i][j] = get_S_in(cns, methodname, i, j, A) / (2 * W) - get_S(cns, methodname, A, i) * get_S(cns, methodname, A, j) / (4 * W * W);
						
					} else {
						Q[i][j] = 0;
					}
			
					
				} else {
					Q[i][j] = -1;
				}
				
			}
		}
		

		return Q;

	}
	
	public static double[][] updateQ1(ArrayList<ArrayList<String>> cns,
			ArrayList<String> methodname, double[][] A, double[][] Q, int i,
			int j, Double W) {

		for (int k = 0; k < Q.length; k++) {
			if (k != j) {
				double ki = get_S_in(cns, methodname, k, i, A);
				double kj = get_S_in(cns, methodname, k, j, A);
				if (ki != 0 && kj != 0) {
					Q[j][k] = Q[i][k] + Q[j][k];
					Q[k][j] = Q[j][k];
				}
				if (ki != 0 && kj == 0) {
					Q[j][k] = Q[i][k] - 2 * get_S(cns, methodname, A, j)
							* get_S(cns, methodname, A, k) / (4 * W * W);
					Q[k][j] = Q[j][k];
				}
				if (kj != 0 && ki == 0) {
					Q[j][k] = Q[j][k] - 2 * get_S(cns, methodname, A, i)
							* get_S(cns, methodname, A, k) / (4 * W * W);
					Q[k][j] = Q[j][k];
				}
			} else {
				Q[k][j] = -1;
			}
		}
		Q = delt_i_j_Q(Q, i);
		return Q;

	}
	
	
	public static Suggestions ExtractClassDetaQ(Suggestions sg, String Classname, ArrayList<ArrayList<String>> OriPart,ArrayList<String> methodlist){
		ArrayList<ArrayList<String>> oriPartNow = (ArrayList<ArrayList<String>>) OriPart.clone();
		int exid = 0;
		for(int i = 0; i < oriPartNow.size(); i++){
			if(SourceParser.Getonlyclassname(oriPartNow.get(i).get(0)).equals(Classname)){
				exid = i;
			}
		}
		
		oriPartNow.remove(exid);
		ArrayList<newclass>  ExtractClass = SrcAction.classesMap.get(Classname).ExtractClass;
		int xEntity = 0;
		
		for(int i = 0 ; i < ExtractClass.size(); i++){
			if(!ExtractClass.get(i).extractedclass.isEmpty()){
				oriPartNow.add(ExtractClass.get(i).extractedclass);		
				if(i!=0){
					xEntity = xEntity + ExtractClass.get(i).extractedclass.size();
				}
			}		
		}
		
		double selfQ = CalculateOrigSystemQ(OriPart,Main.A, methodlist);
		double Qa = CalculateOrigSystemQ(oriPartNow,Main.A, methodlist);
		sg.undoClasses.get(Classname).detaQ = Math.abs(Qa-selfQ);
		sg.undoClasses.get(Classname).setxEntity(xEntity);
		System.out.println(Classname+ "   sg.undoClasses.get(Classname).detaQ =="+ sg.undoClasses.get(Classname).detaQ  + "==xEntity==="+sg.undoClasses.get(Classname).xEntity );
		return sg;	
	}
	
	
	
	
	public static Suggestions moveEntityDetaQ(ArrayList<String> eg, String target, ArrayList<ArrayList<String>> OriPart, ArrayList<String> methodlist, Suggestions sg){
		ArrayList<ArrayList<String>> oriPartclone = (ArrayList<ArrayList<String>>) OriPart.clone();
		ArrayList<String> Source = (ArrayList<String>) eg.clone() ;
		ArrayList<String> Target = new ArrayList<String>();
		int sourceid = 0;
		int targetid = 0;
		for(int i = 0; i < oriPartclone.size(); i++){
			if(oriPartclone.get(i).contains(eg.get(0))){
				sourceid = i;
			}
			if(SourceParser.Getonlyclassname(oriPartclone.get(i).get(0)).equals(target)){
				Target = (ArrayList<String>) oriPartclone.get(i).clone();
				targetid = i;
			}
		}
		
		
		double selfQ = CalculateOrigSystemQ(OriPart,Main.A, methodlist);
		Main.OriQ = Main.OriQ + selfQ;

		ArrayList<String> sort = new ArrayList<String> ();
		double[] QQ = new double[Source.size()];

		ArrayList<String> Targetnow = (ArrayList<String>) Target.clone();
		ArrayList<String> Sourcenow = (ArrayList<String>) Source.clone();
		
		for(int i = 0 ; i < Source.size(); i++){//搬移总次数

			int idmax = 0;
			double Qmax = -10;
			
			for(int k = 0 ; k < Sourcenow.size(); k++){
				
			ArrayList<String> Targetclone = (ArrayList<String>) Targetnow.clone();
			ArrayList<String> Sourceclone = (ArrayList<String>) Sourcenow.clone();
			String temp = Sourceclone.get(k);
			Sourceclone.remove(temp);
			Targetclone.add(temp);
			if(!Sourceclone.isEmpty()){
			oriPartclone.set(sourceid,Sourceclone);
			}
			oriPartclone.set(targetid,Targetclone);
			double DetaQ = CalculateOrigSystemQ(oriPartclone,Main.A, methodlist) - selfQ;
			if(DetaQ > Qmax){
				Qmax = DetaQ;
				idmax  = k;
			}
			
			}

			String temp = Sourcenow.get(idmax);
			Sourcenow.remove(temp);
			Targetnow.add(temp);
			sort.add(temp);
			
			QQ[i] = Math.abs(Qmax);

			
		}
		QQ = semantic.Reverbubsort(QQ);
	
		for(int i = 0 ; i < sort.size(); i++){
			
			sg.UndoEntities.get(sort.get(i)).detaQ = QQ[i];

		
		}
		
		
		
		return sg;	
		
		
//		System.out.println("源eg==========================" );
//		
//		for(int i = 0 ; i < eg.size(); i++){
//			System.out.println(eg.get(i));
//		}
//		System.out.println("source==========================" );
//		for(int i = 0 ; i < Source.size(); i++){
//			System.out.println(Source.get(i));
//		}
//		
//		System.out.println("目标=========================="+target);
//		for(int i = 0 ; i < Target.size(); i++){
//			System.out.println(Target.get(i));
//		}
		
		
	}
	
	public static void calculateDetaQ(Suggestions sg, ArrayList<ArrayList<String>> OriPart,ArrayList<String> methodlist){
		ArrayList<ArrayList<String>> entitygroup = new ArrayList<ArrayList<String>>();
		ArrayList<String> record = new ArrayList<String>();
		
		for(Map.Entry<String, UndoEntity> entry : sg.UndoEntities.entrySet()) {  
		       String key = entry.getKey(); 
		       UndoEntity ud = entry.getValue();
		       ArrayList<String> eg = new ArrayList<String>();
		       eg.add(key);
		       
		       if(record.isEmpty()||!record.contains(key)){
		       record.add(key);
		       
		       for(Map.Entry<String, UndoEntity> entry1 : sg.UndoEntities.entrySet()) {  
				       String key1 = entry1.getKey(); 
				       UndoEntity ud1 = entry1.getValue();
				       if(!key.equals(key1)&&ud.source.equals(ud1.source)&&ud.target.equals(ud1.target)&&!record.contains(key1)){
				    	   eg.add(key1);
					       record.add(key1);
				       }
				       }
				
				entitygroup.add(eg);
				
				}
		}
		for(int i = 0; i < entitygroup.size(); i++){
		
			sg = moveEntityDetaQ(entitygroup.get(i), sg.UndoEntities.get(entitygroup.get(i).get(0)).target,  OriPart, methodlist,sg);
		
		}
		
		for(Map.Entry<String, UndoClass> entry : sg.undoClasses.entrySet()) { 
			String key = entry.getKey();
			sg = ExtractClassDetaQ(sg, key, OriPart,methodlist);		
			
		}
		
    
//		for(int i = 0 ; i < entitygroup.size(); i++){
//			System.out.println("第"+i+"组建议===========================");
//			for(int j = 0 ; j < entitygroup.get(i).size(); j++){
//				System.out.println(entitygroup.get(i).get(j));
//			}
//		}
		
		
		
	}
	
	
	public static double[][] initialQ(ArrayList<ArrayList<String>> cns,
			ArrayList<String> methodname, double[][] A, double W) {
		double[][] Q = new double[cns.size()][cns.size()];
		int init = cns.size();

		for (int i = 0; i < init; i++) {
			for (int j = 0; j < init; j++) {
				if (i != j) {
					if (A[i][j] != 0) {
						Q[i][j] = get_S_in(cns, methodname, i, j, A) / (2 * W)
								- get_S(cns, methodname, A, i)
								* get_S(cns, methodname, A, j) / (4 * W * W);

					} else {
						Q[i][j] = 0;
					}
				} else {
					Q[i][j] = 0;
				}
			}
		}

		return Q;

	}
	
	public static double[][] initialQ(ArrayList<ArrayList<String>> cns,ArrayList<String> methodname, double[][] A, double W, ArrayList<ArrayList<String>> methodbukefen,ArrayList<String> methodduli) {
		int init = cns.size();
		double[][] Q = new double[init][init];

      
		for (int i = 0; i < init; i++) {
			boolean ifi = false;
		for(int m = 0;m<methodbukefen.size();m++){
			if(cns.get(i).containsAll(methodbukefen.get(m))){
				ifi = true;
				break;
			}
		}
			for (int j = 0; j < init; j++) {
				boolean ifj = true;
				if (i != j) {
					
					for(int m = 0;m<methodbukefen.size();m++){
						if(cns.get(j).containsAll(methodbukefen.get(m))){
							ifj = true;
							break;
						}
					}
					if(ifi&&ifj){	
						
						Q[i][j] = -1;
					}else{
					if (get_S_in(cns, methodname, i, j, A) != 0) {
						Q[i][j] = get_S_in(cns, methodname, i, j, A) / (2 * W) - get_S(cns, methodname, A, i) * get_S(cns, methodname, A, j) / (4 * W * W);
						
					} else {
						Q[i][j] = 0;
					}
				}
					
				} else {
					Q[i][j] = -1;
				}
				
			}
		}

		return Q;

	}
	
	
	
	public static double[][] initialQCondition(ArrayList<ArrayList<String>> cns,ArrayList<String> methodname, double[][] A, double W, ArrayList<ArrayList<String>> methodbukefen) {
		int init = cns.size();
		double[][] Q = new double[init][init];

      
		for (int i = 0; i < init; i++) {
			boolean ifi = false;
		for(int m = 0;m<methodbukefen.size();m++){
			if(cns.get(i).containsAll(methodbukefen.get(m))){
				ifi = true;
				break;
			}
		}
			for (int j = 0; j < init; j++) {
				boolean ifj = true;
				if (i != j) {
					
					for(int m = 0;m<methodbukefen.size();m++){
						if(cns.get(j).containsAll(methodbukefen.get(m))){
							ifj = true;
							break;
						}
					}
					if(ifi&&ifj){	
						
						Q[i][j] = -1;
					}else{
					if (get_S_in(cns, methodname, i, j, A) != 0) {
						Q[i][j] = get_S_in(cns, methodname, i, j, A) / (2 * W) - get_S(cns, methodname, A, i) * get_S(cns, methodname, A, j) / (4 * W * W);
						
					} else {
						Q[i][j] = 0;
					}
				}
					
				} else {
					Q[i][j] = -1;
				}
				
			}
		}

		return Q;

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
	
	public static void printCNS(ArrayList<ArrayList<String>> cns) throws IOException {
		String log   =  "D:\\重构以后.txt";
		int kkk = 0;
		for (int i = 0; i < cns.size(); i++) {
			kkk = kkk + cns.get(i).size();
			//writeByFileWrite(log,"第" + i + "个社团结构：" + cns.get(i).size()+"\n");
			for (int j = 0; j < cns.get(i).size(); j++) {
		
			// writeByFileWrite(log,cns.get(i).get(j)+"\n");
			}
		}
		
		//writeByFileWrite(log,"------------------------------------------"+"\n");
		NonInheritanceRefactoring.huafenhou  =  kkk;
	}
	
	public static void printCNSq(ArrayList<ArrayList<String>> cns) throws IOException {
		String log   =  "D:\\chushi.txt";
		int kkk = 0;
		for (int i = 0; i < cns.size(); i++) {
			kkk = kkk + cns.get(i).size();
		//	writeByFileWrite(log,"第" + i + "个社团结构：" + cns.get(i).size()+"\n");
			for (int j = 0; j < cns.get(i).size(); j++) {
				
		//	 writeByFileWrite(log,cns.get(i).get(j)+"\n");
			}
		}
		
	//	writeByFileWrite(log,"共"+kkk+"------------------------------------------"+"\n");
		NonInheritanceRefactoring.huafenhou  =  kkk;

	}
	
	public static void printCNS1(String log1, ArrayList<ArrayList<String>> cns) throws IOException {
		int kkk = 0;
		for (int i = 0; i < cns.size(); i++) {
			//tool.writeByFileWrite(log1,"第" + i + "个社团结构：" + cns.get(i).size()+"\n");
			for (int j = 0; j < cns.get(i).size(); j++) {
				kkk = kkk + cns.get(i).size();			
		   // 	tool.writeByFileWrite(log1,cns.get(i).get(j)+"\n");
		
			}
		}
	}

	public static double get_S_in(ArrayList<ArrayList<String>> cns,
			ArrayList<String> methodname, int i, int j, double[][] A) {
		double weight = 0;
		ArrayList<String> cn_i = cns.get(i);
		ArrayList<String> cn_j = cns.get(j);
		for (int m = 0; m < cn_i.size(); m++) {
			String namei = cn_i.get(m);
			int idi = methodname.indexOf(namei);
			for (int n = 0; n < cn_j.size(); n++) {
				String namej = cn_j.get(n);
				int idj = methodname.indexOf(namej);
				weight = weight + A[idi][idj];
			}
		}
		return weight;
	}
	
	public static double get_S_in2(ArrayList<ArrayList<String>> cns,
			ArrayList<String> methodname, int i, int j, int[][] A) {
		double weight = 0;
		ArrayList<String> cn_i = cns.get(i);
		ArrayList<String> cn_j = cns.get(j);
		for (int m = 0; m < cn_i.size(); m++) {
			String namei = cn_i.get(m);
			int idi = methodname.indexOf(namei);
			for (int n = 0; n < cn_j.size(); n++) {
				String namej = cn_j.get(n);
				int idj = methodname.indexOf(namej);
				weight = weight + A[idi][idj];
			}
		}
		return weight;
	}

	public static double get_W(double[][] A) {
		double W = 0;
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A.length; j++) {
				W = W + A[i][j];
			}
		}
		return W;
	}

	
	public static double get_W1(int[][] A) {
		double W = 0;
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A.length; j++) {
				W = W + A[i][j];
			}
		}
		return W;
	}
	public static double get_S(ArrayList<ArrayList<String>> cns,
			ArrayList<String> methodname, double[][] A, int i) {
		double S = 0;
		ArrayList<String> cni = cns.get(i);

		for (int k = 0; k < cni.size(); k++) {
			String namei = cni.get(k);
			int idi = methodname.indexOf(namei);
			for (int n = 0; n < A.length; n++) {
				S = S + A[idi][n];
			}
		}
		return S;
	}

	public static double get_S2(ArrayList<ArrayList<String>> cns,
			ArrayList<String> methodname, int[][] A, int i) {
		double S = 0;
		ArrayList<String> cni = cns.get(i);

		for (int k = 0; k < cni.size(); k++) {
			String namei = cni.get(k);
			int idi = methodname.indexOf(namei);
			for (int n = 0; n < A.length; n++) {
				S = S + A[idi][n];
			}
		}
		return S;
	}
	
	public static index get_Qmax(double[][] Q) {
		double Qmax = -100;
		index id = new index();
		for (int i = 0; i < Q.length; i++) {
		for (int j = 0; j < Q.length; j++) {
			if(i!=j){
			if (Q[i][j] > Qmax) {
					Qmax = Q[i][j];
					id.i = i;
					id.j = j;
					id.max = Qmax;
								}
				     }
			}
		}
		return id;
	}
	
	public static index get_QmaxCondition(double[][] Q, ArrayList<ArrayList<String>> cns) {//纯同类合并！
		double Qmax = -100;
		index id = new index();
		for (int i = 0; i < Q.length; i++) {
		for (int j = 0; j < Q.length; j++) {
			if(i!=j){
			if ((Q[i][j] > Qmax) && (!IsintheSameClass(cns.get(i), cns.get(j))))
			{
					Qmax = Q[i][j];
					id.i = i;
					id.j = j;
					id.max = Qmax;
			}
			}
			}
		}
		return id;
	}
	
	public static returnvalue CommunityDetectionNoextentsDUOFULEI (double[][] A, ArrayList<String> methodlist,ArrayList<ArrayList<String>> methodbukefen,
			ArrayList<String> methodduli) {

		        returnvalue rv = new returnvalue();
				ArrayList<ArrayList<String>> cns = new ArrayList<ArrayList<String>>();
				// 社区结构初始化操作
				cns = CMN.initialCNS(cns, methodbukefen, methodduli);
				cns = dealgetsetattr(cns,methodlist);

				double[][] Q = new double[cns.size()][cns.size()];
				double W = CMN.get_W(A);
				Q = CMN.initialQ(cns, methodlist, A, W,methodbukefen,methodduli);
				double MQ = 0;// 记录模块度

				while (CMN.get_Qmax(Q).max > 0) {
					 
					int id_i = CMN.get_Qmax(Q).i;
					int id_j = CMN.get_Qmax(Q).j;

					Q = CMN.updateQ(cns, methodlist, A, Q, id_i, id_j, W,methodbukefen,methodduli);
					cns = CMN.mergeCNS_i_j(cns, id_i, id_j);
					MQ = MQ + CMN.get_Qmax(Q).max;
				}
				
				rv.cns = cns;
				rv.MQ = MQ;
				return rv;

			}

	
	public static ArrayList<ArrayList<String>> CommunityDetectionNoextents(double[][] A, ArrayList<String> methodlist,ArrayList<ArrayList<String>> methodbukefen,
			ArrayList<String> methodduli) {
		
				ArrayList<ArrayList<String>> cns = new ArrayList<ArrayList<String>>();
				// 社区结构初始化操作
				cns = initialCNS(cns, methodbukefen, methodduli);
			
				double[][] Q = new double[A.length][A.length];
				double W = get_W(A);
				Q = initialQ(cns, methodlist, A, W,methodbukefen,methodduli);
				double MQ = 0;// 记录模块度
				
				while (get_Qmax(Q).max > 0) {
					 
					int id_i = get_Qmax(Q).i;
					int id_j = get_Qmax(Q).j;

					Q = updateQ(cns, methodlist, A, Q, id_i, id_j, W,methodbukefen,methodduli);

					cns = mergeCNS_i_j(cns, id_i, id_j);
					MQ = MQ + get_Qmax(Q).max;
				}
		
				System.out.println("模块度=" + MQ);
				return cns;

			}
	
	
	public static index get_Qmaxcls(double[][] Q,ArrayList<ArrayList<String>> cns) {
		double Qmax = -100;
		index id = new index();
		for (int i = 0; i < Q.length; i++) {
			for (int j = 0; j < Q.length; j++) {
				if(i!=j){
					int sz = 0;
					int szj  = 0;
					for(int u = 0;  u < cns.get(i).size(); u++){
						sz = SrcAction.classesMap.get(cns.get(i).get(u)).featureMap.size()+sz;
					}
					
					for(int u = 0;  u < cns.get(j).size(); u++){
						szj = SrcAction.classesMap.get(cns.get(j).get(u)).featureMap.size()+szj;
					}
				if ((Q[i][j] > Qmax)&& (sz +szj)<=250 ){
					Qmax = Q[i][j];
					id.i = i;
					id.j = j;
					id.max = Qmax;
				}
				}
			}
		}
		return id;
	}
	
	

	public static void printmatrix(double[][] uu) throws IOException {
		// String
		for (int i = 0; i < uu.length; i++) {
			String s = Double.toString(uu[i][0]) + " ";
			for (int j = 1; j < uu.length; j++) {
				s = s + uu[i][j] + " ";

			}
//			 writeByFileWrite("D:\\matrix.txt", s+"\n");
			System.out.println(s);
		}
	}


	
	public static mt mergeCNS_i_jcg(
			ArrayList<ArrayList<String>> cns, int i, int j) {
		tool tl = new tool();
		ArrayList<String> ii = (ArrayList<String>)cns.get(i).clone();
		ArrayList<String> jj = (ArrayList<String>)cns.get(j).clone();

		ArrayList<String> union = tl.testUnion(ii, jj);

		cns.set(j, union);
		ArrayList<ArrayList<String>> cnss = new ArrayList<ArrayList<String>>();
		//cns.remove(cns.get(i));
		for (int t = 0; t < cns.size(); t++) {
			if (t!=i) {
				cnss.add(cns.get(t));
			}
		}
         mt  mmMt = new mt();
		int dx = cnss.indexOf(union);
	   mmMt.i = dx;
	   mmMt.cns = cnss;
		return mmMt ;

	}

	
	
	
	public static ArrayList<ArrayList<String>> mergeCNS_i_j(ArrayList<ArrayList<String>> cns, int i, int j) {
		tool tl = new tool();
		ArrayList<String> ii = (ArrayList<String>)cns.get(i).clone();
		ArrayList<String> jj = (ArrayList<String>)cns.get(j).clone();
		ArrayList<String> union = tl.testUnion(ii, jj);
		cns.set(j, union);
		ArrayList<ArrayList<String>> cnss = new ArrayList<ArrayList<String>>();
		for (int t = 0; t < cns.size(); t++) {
			if (t!=i) {
				cnss.add(cns.get(t));
			}
		}

		return cnss;

	}

	public static double[][] updateQ(ArrayList<ArrayList<String>> cns, ArrayList<String> methodname, double[][] A, double[][] Q, int i,
			int j, Double W, ArrayList<ArrayList<String>> methodbukefen, ArrayList<String> methodduli) {

		for (int k = 0; k < Q.length; k++) {//
			if (k != j) {
				double ki = get_S_in(cns, methodname, k, i, A);
				double kj = get_S_in(cns, methodname, k, j, A);
				if (ki != 0 && kj != 0) {
					Q[j][k] = Q[i][k] + Q[j][k];
					Q[k][j] = Q[j][k];
				}
				if (ki != 0 && kj == 0) {
					Q[j][k] = Q[i][k] - 2 * get_S(cns, methodname, A, j) * get_S(cns, methodname, A, k) / (4 * W * W);
					Q[k][j] = Q[j][k];
				}
				if (kj != 0 && ki == 0) {
					Q[j][k] = Q[j][k] - 2 * get_S(cns, methodname, A, i) * get_S(cns, methodname, A, k) / (4 * W * W);
					Q[k][j] = Q[j][k];
				}
			} else {
				Q[k][j] = -1;
			}
		}
				Q = delt_i_j_Q(Q, i);
		
		
		 
		for (int m = 0; m < Q.length; m++) {
			boolean ifi = false;
			for(int mm = 0;mm<methodbukefen.size();mm++){
				if(cns.get(m).containsAll(methodbukefen.get(mm))){
					ifi = true;
					break;
				}
														}
			for (int n = 0; n < Q.length; n++) {
				boolean ifj = false;
				if (m != n) {
					
					for(int mm = 0;mm < methodbukefen.size(); mm++){
						if(cns.get(n).containsAll(methodbukefen.get(mm))){
							ifj = true;
							break;
						}
						}
					if(ifi&&ifj){	
						Q[m][n] = -1;	
								}
					
				}else{
					Q[m][n] = -1;	
				}
												}
			}
		
		return Q;

	}

	
	public static ArrayList<Integer> getAllFathers(int[][] extendsMatrix, int k){
		ArrayList<Integer> fathers = new ArrayList<Integer>();
		ArrayList<Integer> current = new ArrayList<Integer>();
		for(int i = 0; i <extendsMatrix.length; i++ ){
			if(extendsMatrix[k][i]!=0){
				fathers.add(i);
				current.add(i);
			}
		}
		while(!current.isEmpty()){
			ArrayList<Integer> tem = new ArrayList<Integer>();
			
			for(int i = 0; i <current.size(); i++){
				for(int j = 0; j <extendsMatrix.length; j++ ){
					if(extendsMatrix[current.get(i)][j]!=0){
						if(!fathers.contains(j)){
						fathers.add(j);
						tem.add(i);
						}
					}
				}
			}
			current.clear();
			current.addAll(tem);
		}
		return fathers;
	}
	
	public static boolean getisoverride(String featurename,int[][] extendsMatrix){
		boolean isoveride = false;
		String self = 	SourceParser.Getonlyclassname(featurename);
		int id1 = SrcAction.classname.indexOf(self);
		
		ArrayList<Integer> fathr = getAllFathers(extendsMatrix, id1);

		if(featurename.contains("(")||featurename.contains("{")){
			String str[] = featurename.split(self+"\\.");
	
			for(int q = 0; q < fathr.size(); q ++){
				for(Map.Entry<String, Feature> entry1 : SrcAction.classesMap.get(SrcAction.classname.get(fathr.get(q))).featureMap.entrySet()) {  
					String key = entry1.getKey();
					if(key!=null){
					String str1[] = key.split(SrcAction.classname.get(fathr.get(q))+"\\.");
				
					if(str[1].equals(str1[1])){
						isoveride =true;
						break;
					}
					}
				}
		
		}
		}
		return isoveride;
	}
	
	
	public static boolean getissuperdot(String featurename, Feature aFeature,int[][] extendsMatrix){
		boolean issuperdot = false;		
		if(featurename.contains("(")||featurename.contains("{")){
		String self = 	SourceParser.Getonlyclassname(featurename);
		for(int i = 0 ; i < aFeature.outboundFeatureList.size(); i++){
		String fname  =  aFeature.outboundFeatureList.get(i);
		String other = 	SourceParser.Getonlyclassname(fname);
		if(!self.equals(other)){
			if(SrcAction.classname.contains(self) && SrcAction.classname.contains(other)){
			int id1 = SrcAction.classname.indexOf(self);
			int id2 = SrcAction.classname.indexOf(other);
			if(extendsMatrix[id1][id2]!=0){
				issuperdot =true;
			}
		                                                                       }
		                       }
		                                                             }
                                                                    }
		return issuperdot;
	}
	
	
	public static double[][] delt_i_j_Q(double[][] Q, int d) {
		double[][] QQ = new double[Q.length - 1][Q.length - 1];
		ArrayList<String> MarixzeroList = new ArrayList<String>();

		for (int i = 0; i < Q.length; i++) {

			if (i != d) {
				if (d != 0) {
					String s = Double.toString(Q[i][0]) + " ";
					for (int j = 1; j < Q.length; j++) {
						if (j != d) {
							s = s + Double.toString(Q[i][j]) + " ";
						}

					}
					MarixzeroList.add(s);
				} else {
					String s = Double.toString(Q[i][1]) + " ";
					for (int j = 2; j < Q.length; j++) {

						s = s + Double.toString(Q[i][j]) + " ";

					}
					MarixzeroList.add(s);
				}
			}
		}
		for (int i = 0; i < MarixzeroList.size(); i++) {
			String s = MarixzeroList.get(i);
			String str[] = {};
			str = s.split(" ");
			for (int k = 0; k < str.length; k++) {
				QQ[i][k] = Double.valueOf(str[k]);
			}
		}

		return QQ;
	}

}
