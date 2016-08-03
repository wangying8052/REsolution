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

package com.Refactor.NonInheritance;
import java.util.ArrayList;
import java.util.List;

import com.jeantessier.dependencyfinder.gui.SrcAction;


public class MatrixComputing {
	
	public static double getOutdgree(int[][] extendsMatrix, int i) {
		double degree = 0;
		for (int k = 0; k < extendsMatrix.length; k++) {

			degree = degree + extendsMatrix[i][k];
		}

		return degree;
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
	
	public static double[][] makezeroMatrix(double[][] x){
		for(int i = 0; i < x.length; i++){
			for(int j = 0; j < x.length; j++){
				x[i][j] = 0;
			}
		}
		return x;		
	}
	
	
	public static double summatrix(double[][] x) {
		double sum = 0;
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x.length; j++) {
				sum = sum + x[i][j];
			}
		}
		return sum;

	}
	
	 public static int[][] directedToundirected(int[][] matrix){
		 
		 for(int i = 0; i < matrix.length; i++){
			 for(int j = 0; j < matrix.length; j++){
			 if(matrix[i][j]!=0){
				 matrix[j][i] = matrix[i][j];
			 }
			 }
		 }
		return matrix;
		 
		 
	 }
	
	public static double getIndgree(int[][] extendsMatrix, int i) {
		double degree = 0;
		for (int k = 0; k < extendsMatrix.length; k++) {

			degree = degree + extendsMatrix[k][i];
		}

		return degree;
	}
	
	 public static ArrayList <Integer> getNBs(int[][] extendsMatrix,int i,ArrayList <Integer> result){
		 ArrayList <Integer> NBs = new  ArrayList <Integer>();
		 for(int k = 0; k < extendsMatrix.length; k++){
			 
			 if(extendsMatrix[i][k]!=0&&!result.contains(k)){
				if(NBs.isEmpty()){
					NBs.add(k);
					result.add(k);
				}else{
					if(!NBs.contains(k)){
						NBs.add(k);
						result.add(k);
					}
				}
			 }
		 }
		 
		return  NBs ;
	 }
	 
	
	public static double getdgree(int[][] extendsMatrix, int i) {
		double degree = 0;
		for (int k = 0; k < extendsMatrix.length; k++) {

			degree = degree + extendsMatrix[i][k] + extendsMatrix[k][i];
		}

		return degree;
	}
	
	
	
	 public static ArrayList <Integer> getNBs(int[][] Matrix,int i){
		 ArrayList <Integer> NBs = new  ArrayList <Integer>();
		 for(int k = 0; k < Matrix.length; k++){			 
			 if(Matrix[i][k]!=0&&!NBs.contains(k)){				
					NBs.add(k);			
					}
				}	 
		return  NBs ;
	 }
	
	
	public static int[][] removeRelaofNodes(int[][] dependencyMatrix, int i) {

		for (int k = 0; k < dependencyMatrix.length; k++) {

			dependencyMatrix[i][k] = 0;
			dependencyMatrix[k][i] = 0;
		}

		return dependencyMatrix;
	}
	
	public static void GetLeafNodes(int[][] extendsMatrix) {
			for (int i = 0; i < extendsMatrix.length; i++) {
				if( getOutdgree(extendsMatrix, i) != 0 && getIndgree(extendsMatrix, i) == 0){
					SrcAction.classesMap.get(SrcAction.classname.get(i)).isLeaf = true;
					for(int j = 0; j < extendsMatrix.length; j++){
						if(i!=j){
							if(extendsMatrix[i][j]!=0){
								SrcAction.classesMap.get(SrcAction.classname.get(i)).superclass = SrcAction.classname.get(j);
							}
						}
					}
				}
			}
	}
	
	
}
