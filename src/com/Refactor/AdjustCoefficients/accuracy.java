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

package com.Refactor.AdjustCoefficients;
import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.SAXException;

public class accuracy {

	public static int[][] initzeromatrix(int[][] uu, int h, int l) {
		for (int i = 0; i < h; i++) {

			for (int j = 1; j < l; j++) {
				uu[i][j] = 0;

			}

		}
		return uu;
	}

	public static void printmatrix(double[][] uu) {
		for (int i = 0; i < uu.length; i++) {
			String s = Double.toString(uu[i][0]) + " ";
			for (int j = 1; j < uu.length; j++) {
				s = s + uu[i][j] + " ";

			}
			System.out.println(s);
		}
	}

	public static void printmatrix1(int[][] uu) {
		System.out.println("uu.length" + uu.length);
		for (int i = 0; i < uu.length; i++) {
			String s = Double.toString(uu[i][0]) + " ";
			for (int j = 1; j < 2; j++) {
				s = s + uu[i][j] + " ";

			}
			System.out.println(s);
		}
	}

	public static ArrayList<ArrayList<String>> oneparition() {
		ArrayList<ArrayList<String>> A = new ArrayList<ArrayList<String>>();
		String strA[] = { "1 2 3 4", "5 6 7 8", "9 10 11 12", "13 14 15 16" };
		for (int i = 0; i < strA.length; i++) {
			String str[] = {};
			str = strA[i].split(" ");
			ArrayList<String> cluster = new ArrayList<String>();
			for (int j = 0; j < str.length; j++) {
				// element el = new element();
				// el.label = ;
				cluster.add(str[j]);
			}
			A.add(cluster);
		}
		return A;

	}

	public static ArrayList<ArrayList<String>> twoparition() {
		ArrayList<ArrayList<String>> B = new ArrayList<ArrayList<String>>();
		String strB[] = { "1 2 7 8", "4 5 6 11 12 13 14", "9 10 15 16", "3" };
		for (int i = 0; i < strB.length; i++) {
			String str[] = {};
			str = strB[i].split(" ");
			ArrayList<String> cluster = new ArrayList<String>();
			for (int j = 0; j < str.length; j++) {
				// element el = new element();
				// el.label = ;
				cluster.add(str[j]);
			}
			B.add(cluster);
		}
		return B;

	}

	public static ArrayList<String> removeDuplicate(ArrayList<String> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).equals(list.get(i))) {
					list.remove(j);
				}
			}
		}
		return list;

	}

	public static returnrank outputPermutation(int relationCollectionLen,
			int nodeNum, int currentOutputNum, returnrank re) {
		if (currentOutputNum == nodeNum) {
			ArrayList<String> group = new ArrayList<String>();
			for (int j = 0; j < nodeNum; j++) {
				group.add(Integer.toString(re.GroupIds[j]));
			//	System.out.print(re.GroupIds[j] + " ");
			}
			group = removeDuplicate(group);
			re.groups.add(group);
		//	System.out.println();
			// return;
		}
		if (currentOutputNum < nodeNum) {
			for (int i = re.indexScale[currentOutputNum][0]; i <= re.indexScale[currentOutputNum][1]; i++) {
				re.GroupIds[currentOutputNum] = re.relationCollection[i][1];
				re = outputPermutation(relationCollectionLen, nodeNum, currentOutputNum + 1, re);
			}
		}
		return re;
	}

	public static double getMoJOFM(ArrayList<ArrayList<String>> A, ArrayList<ArrayList<String>> B) {
		double MoJOFM = 0;

		ArrayList<ArrayList<element>> Apartition = new ArrayList<ArrayList<element>>();
		ArrayList<ArrayList<element>> Bpartition = new ArrayList<ArrayList<element>>();

		for (int i = 0; i < A.size(); i++) {
			ArrayList<element> cluster = new ArrayList<element>();
			for (int j = 0; j < A.get(i).size(); j++) {
				element el = new element();
				el.label = A.get(i).get(j);
				cluster.add(el);
			}
			Apartition.add(cluster);

		}

		for (int i = 0; i < B.size(); i++) {
			ArrayList<element> cluster = new ArrayList<element>();
			for (int j = 0; j < B.get(i).size(); j++) {
				element el = new element();
				el.label = B.get(i).get(j);
				cluster.add(el);
			}
			Bpartition.add(cluster);

		}
		int nn = 0;

		for (int i = 0; i < Apartition.size(); i++) {

			nn = nn + Apartition.get(i).size();

		}


		int[][] V = new int[Apartition.size()][Bpartition.size()];
		initzeromatrix(V, Apartition.size(), Bpartition.size());
		for (int i = 0; i < Apartition.size(); i++) {
			for (int j = 0; j < Apartition.get(i).size(); j++) {
				element el = Apartition.get(i).get(j);
				for (int m = 0; m < Bpartition.size(); m++) {
					for (int n = 0; n < Bpartition.get(m).size(); n++) {
						element ele = Bpartition.get(m).get(n);
						if (el.label.equals(ele.label)) {
							el.tag = m;
							Apartition.get(i).get(j).tag = el.tag;
							V[i][m] = V[i][m] + 1;
						}
					}

				}

			}

		}

//		printmatrix1(V);
		int maxmj = 0;
		for (int i = 0; i < Apartition.size(); i++) {
			int max = V[i][0];
			for (int j = 0; j < Bpartition.size(); j++) {
				if (max < V[i][j]) {
					max = V[i][j];
				}
			}
			maxmj = maxmj + max;
		}

		int l = Apartition.size();

		ArrayList<ArrayList<element>> Group = new ArrayList<ArrayList<element>>(Bpartition.size());
		for (int i = 0; i < Bpartition.size(); i++) {
			ArrayList<element> tp = new ArrayList<element>();
			Group.add(tp);
		}

		ArrayList<ArrayList<Integer>> id = new ArrayList<ArrayList<Integer>>(); // A划分中的每个cluster
																				// 所有可能属于
																				// Group的索引号

		for (int i = 0; i < Apartition.size(); i++) {
			ArrayList<Integer> count = new ArrayList<Integer>(Bpartition.size());
			for (int j = 0; j < Apartition.get(i).size(); j++) {

				for (int k = 0; k < Bpartition.size(); k++) {
					count.add(0);
				}
				int p = count.get(Apartition.get(i).get(j).tag) + 1;
				count.set(Apartition.get(i).get(j).tag, p);

			}
			int max = 0;
			ArrayList<Integer> in = new ArrayList<Integer>();
			for (int o = 0; o < count.size(); o++) {
				if (max < count.get(o)) {
					max = count.get(o);
				}
			}

			for (int o = 0; o < count.size(); o++) {
				if (max == count.get(o)) {
					in.add(o);
				}
			}
			id.add(in);

		}
		for (int i = 0; i < id.size(); i++) {
			for (int j = 0; j < id.get(i).size(); j++) {
			}
		}

		int relationCollectionlength = 0;
		int relationCollectionLen = 1;
		for (int i = 0; i < id.size(); i++) {// 最外层循环

			relationCollectionLen = relationCollectionLen * id.get(i).size();
			relationCollectionlength = relationCollectionlength + id.get(i).size();
		}

		int[][] indexScale = new int[Apartition.size()][2];
		int line = 0;
		for (int i = 0; i < id.size(); i++) {

			indexScale[i][0] = line;
			indexScale[i][1] = line + id.get(i).size() - 1;
			line = line + id.get(i).size();

		}

		int nodenum = l;
		returnrank re = new returnrank();
		re.nodeNum = nodenum;
		re.GroupIds = new int[re.nodeNum];
		re.indexScale = indexScale;
		re.relationCollection = new int[relationCollectionlength][2];

		int ct = 0;
		for (int i = 0; i < id.size(); i++) {

			for (int j = 0; j < id.get(i).size(); j++) {
				re.relationCollection[ct][0] = i;
				re.relationCollection[ct][1] = id.get(i).get(j);
				ct++;
			}
		}
		re = outputPermutation(relationCollectionLen, nodenum, 0, re);
		if (!re.groups.isEmpty()) {
			int maxg = re.groups.get(0).size();
			int ming = re.groups.get(0).size();
			for (int i = 0; i < re.groups.size(); i++) {
				if (maxg < re.groups.get(i).size()) {
					maxg = re.groups.get(i).size();
				}
				if (ming > re.groups.get(i).size()) {
					ming = re.groups.get(i).size();
				}
			}
			double monomin = nn - maxmj + l - (double) maxg;
			double monomax = nn - (double) ming;
			MoJOFM = 1 - (monomin / monomax);

		} else {
			MoJOFM = 1;
		}


		return MoJOFM;
	}

	// 暂时存放结果的长度为nodeNum数组

}
