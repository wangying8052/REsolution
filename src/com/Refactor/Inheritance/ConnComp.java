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
import java.util.ArrayList;

import com.jeantessier.dependencyfinder.gui.SrcAction;

//建立一个类，使用里面的变量。使用它要实例化  
public class ConnComp {

	public static double getdgree(int[][] extendsMatrix, int i) {
		double degree = 0;
		for (int k = 0; k < extendsMatrix.length; k++) {

			degree = degree + extendsMatrix[i][k] + extendsMatrix[k][i];
		}

		return degree;
	}

	public static ArrayList<Integer> getNBs(int[][] extendsMatrix, int i, ArrayList<Integer> result) {
		ArrayList<Integer> NBs = new ArrayList<Integer>();
		for (int k = 0; k < extendsMatrix.length; k++) {

			if (extendsMatrix[i][k] != 0 && !result.contains(k)) {
				if (NBs.isEmpty()) {
					NBs.add(k);
					result.add(k);
				} else {
					if (!NBs.contains(k)) {
						NBs.add(k);
						result.add(k);
					}
				}
			}
		}

		return NBs;
	}

	public static int[][] directedToundirected(int[][] matrix) {

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] != 0) {
					matrix[j][i] = matrix[i][j];
				}
			}
		}
		return matrix;

	}
	
	
	public static ArrayList<ArrayList<Integer>> getcc1(int[][] matrix) {

		
		
		matrix = directedToundirected(matrix);
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> cc = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < matrix.length; i++) {
			if (getdgree(matrix, i) != 0) {
				nodes.add(i);
			}
		}
		ArrayList<Integer> result = new ArrayList<Integer>();
		while (!nodes.isEmpty()) {
			ArrayList<Integer> neighbour = new ArrayList<Integer>();
			neighbour.add(nodes.get(0));
			ArrayList<Integer> resultone = new ArrayList<Integer>();

			resultone.add(nodes.get(0));
			result.addAll(resultone);
			while (!neighbour.isEmpty()) {
				ArrayList<Integer> NBs = new ArrayList<Integer>();
				for (int i = 0; i < neighbour.size(); i++) {
					NBs.addAll(getNBs(matrix, neighbour.get(i), result));

				}
				resultone.addAll(NBs);
				neighbour = NBs;

			}
			nodes.removeAll(result);
			cc.add(resultone);

		}

		return cc;
	}
	
	public static ArrayList<ArrayList<String>> getCC(int[][] matrix) {

		matrix = directedToundirected(matrix);
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		//ArrayList<Integer> node = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> cc = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<String>> Allcc = new ArrayList<ArrayList<String>>();
		// ArrayList <Integer> result = new ArrayList <Integer>();
		for (int i = 0; i < matrix.length; i++) {
			if (getdgree(matrix, i) != 0) {
				nodes.add(i);
			}
		}
		ArrayList<Integer> result = new ArrayList<Integer>();
		while (!nodes.isEmpty()) {
			ArrayList<Integer> neighbour = new ArrayList<Integer>();
			neighbour.add(nodes.get(0));
			ArrayList<Integer> resultone = new ArrayList<Integer>();

			resultone.add(nodes.get(0));
			result.addAll(resultone);
			while (!neighbour.isEmpty()) {
				ArrayList<Integer> NBs = new ArrayList<Integer>();
				for (int i = 0; i < neighbour.size(); i++) {
					NBs.addAll(getNBs(matrix, neighbour.get(i), result));

				}
				resultone.addAll(NBs);
				neighbour = NBs;
			}
			nodes.removeAll(result);
			cc.add(resultone);

		}
		
		
		
		int ii = 0;
		for (int i = 0; i < cc.size(); i++) {
			ArrayList<String> onecc = new ArrayList<String>();
			ii = ii + cc.get(i).size();
			for (int j = 0; j < cc.get(i).size(); j++) {
				onecc.add(SrcAction.classname.get(cc.get(i).get(j)));
			}
			Allcc.add(onecc);
		}

		return Allcc;
	}

	public static ArrayList<ArrayList<String>> getcc(int[][] matrix) {
		   int[][] extendsMatrix1 =  new int[matrix.length][matrix.length];
		   for(int i = 0; i < matrix.length; i++){
			   for(int j = 0; j < matrix.length; j++){
				   extendsMatrix1 [i][j] = matrix [i][j];
			   }
		   }
		extendsMatrix1 = directedToundirected(extendsMatrix1);
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		ArrayList<Integer> node = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> cc = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<String>> Allcc = new ArrayList<ArrayList<String>>();
		// ArrayList <Integer> result = new ArrayList <Integer>();
		for (int i = 0; i < extendsMatrix1.length; i++) {
			if (getdgree(extendsMatrix1, i) != 0) {
				nodes.add(i);
			}else{
				node.add(i);
			}
		}
		ArrayList<Integer> result = new ArrayList<Integer>();
		while (!nodes.isEmpty()) {
			ArrayList<Integer> neighbour = new ArrayList<Integer>();
			neighbour.add(nodes.get(0));
			ArrayList<Integer> resultone = new ArrayList<Integer>();

			resultone.add(nodes.get(0));
			result.addAll(resultone);
			while (!neighbour.isEmpty()) {
				ArrayList<Integer> NBs = new ArrayList<Integer>();
				for (int i = 0; i < neighbour.size(); i++) {
					NBs.addAll(getNBs(extendsMatrix1, neighbour.get(i), result));

				}
				resultone.addAll(NBs);
				neighbour = NBs;
			}
			nodes.removeAll(result);
			cc.add(resultone);

		}
		
		
		for (int i = 0; i < node.size(); i++) {
			ArrayList<Integer> resultone = new ArrayList<Integer>();
			resultone.add(node.get(i));
			cc.add(resultone);
		}
		
		int ii = 0;
		for (int i = 0; i < cc.size(); i++) {
			ArrayList<String> onecc = new ArrayList<String>();
			ii = ii + cc.get(i).size();
			for (int j = 0; j < cc.get(i).size(); j++) {
				onecc.add(SrcAction.classname.get(cc.get(i).get(j)));
			}
			Allcc.add(onecc);
		}

		return Allcc;
	}

}
