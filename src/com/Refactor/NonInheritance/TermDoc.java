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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;

public class TermDoc {
	VSM data = new VSM();
	 static ArrayList<String> words;
	static ArrayList<String> toowords;
	static ArrayList<String> newwords = new ArrayList<String>();
	static ArrayList<double[]> vectors = new ArrayList<double[]>();
	static StringTokenizer tokenizer = null; // 单个词
	static DoubleMatrix2D TDmatrix;

	public TermDoc() {
		this.words = new ArrayList<String>();
		// newwords = new ArrayList<String>();
		this.toowords = new ArrayList<String>();
		// vectors = new ArrayList<double[]>();
	}

	public DoubleMatrix2D getTDmatrix() {
		return this.TDmatrix;
	}

	public void setTDmatrix(DoubleMatrix2D TDmatrix) {
		this.TDmatrix = TDmatrix;
	}

	public ArrayList<String> getNewwords() {
		return this.newwords;
	}

	public void setNewwords(ArrayList<String> newwords) {
		this.newwords = newwords;
	}

	public ArrayList<double[]> getVectors() {
		return this.vectors;
	}

	public void setVectors(ArrayList<double[]> vectors) {
		this.vectors = vectors;
	}

	// 从列表里搜索的结果列表
	@SuppressWarnings("unused")
	public static void searchList(ArrayList<String> document) {
		tokenizer = new StringTokenizer(document.toString(),
				" .,:;-()<>[]!?/-*\"\r\n");
		String aWord = "";
		// 分用来比较的词
		int vSize = words.size();// words是每加入一个文档去重后的数组
		double[] aVector;
		double[] newVector;
		double[] tmpVector;
		if (words.size() == 0) {
			aVector = new double[1];
		} else {
			aVector = new double[vSize];
		}
		while (tokenizer.hasMoreTokens()) {
			aWord = tokenizer.nextToken().toLowerCase();// 分割去重文档的字符串，将大写转换为小写
			if (words.indexOf(aWord) == -1) {
				// 得到初始文档的词
				words.add(aWord);
				// 测试取到的词
				vSize = words.size();// vSize初始文档数组的大小
				newVector = new double[vSize];
				// 实现数组的复制:将原数组复制给目的数组newVector
				System.arraycopy(aVector, 0, newVector, 0, vSize - 1);
				newVector[vSize - 1] = 1.0;// 新复制来的词，相同为1
				aVector = new double[vSize];
				// 将aVector替换
				aVector = newVector;
				if (vectors.size() > 0) {
					for (int counter = 0; counter < vectors.size(); counter++) {
						newVector = new double[vSize];
						tmpVector = vectors.get(counter);

						System.arraycopy(tmpVector, 0, newVector, 0, vSize - 1);
						newVector[vSize - 1] = 0.0;
						vectors.set(counter, newVector);
					}
				}
			} else if (words.indexOf(aWord) >= 0)// 比词
			{
				aVector[words.indexOf(aWord)]++;
			}
		}
		vectors.add(aVector);
	}

	// 行为词，列为文档的关系矩阵
	public static DoubleMatrix2D createMatrix() {

		DoubleMatrix2D matrix = new SparseDoubleMatrix2D(words.size(),
				vectors.size());
		for (int vectorNr = 0; vectorNr < vectors.size(); vectorNr++) {
			double aVector[] = vectors.get(vectorNr);
			for (int wordNr = 0; wordNr < words.size(); wordNr++) {
				matrix.set(wordNr, vectorNr, aVector[wordNr]);
			}
			// 每添加一个文本后的新矩阵
		}
		return matrix;
	}

	// 输出行为词，列为文档的关系矩阵
	public void showMatrix() {
		System.out.println("当前矩阵:\n----------------");
		DoubleMatrix2D matrix = createMatrix();
		for (int row = 0; row < matrix.rows(); row++) {
			for (int column = 0; column < matrix.columns(); column++) {
				System.out.printf("%3.0f ", matrix.get(row, column));
			}
			System.out.print("\n");
		}
	}

	// 计算每篇文档中词的个数
	public double[][] oldTF() {
		double[][] tf = new double[words.size()][vectors.size()];
		for (int vectorNr = 0; vectorNr < vectors.size(); vectorNr++) {
			double aVector[] = vectors.get(vectorNr);
			for (int wordNr = 0; wordNr < words.size(); wordNr++) {
				tf[wordNr][vectorNr] = aVector[wordNr];
			}

		}
		return tf;// 坐标为【行、列】=TF
	}

	public static double[][] TF() {
		double[] num = null;
		double[][] tf = new double[words.size()][vectors.size()];
		for (int vectorNr = 0; vectorNr < vectors.size(); vectorNr++) {
			double aVector[] = vectors.get(vectorNr);
			num = new double[words.size()];
			for (int wordNr = 0; wordNr < words.size(); wordNr++) {
				if (aVector[wordNr] != 0) {
					num[vectorNr] += aVector[wordNr]; // 数组越界了
				}
			}

			for (int wordNr = 0; wordNr < words.size(); wordNr++) {
				if (aVector[wordNr] != 0) {
					tf[wordNr][vectorNr] = aVector[wordNr] / num[vectorNr];
				} else
					tf[wordNr][vectorNr] = 0;
			}

		}
		return tf;// 坐标为【行、列】=TF
	}

	// 计算文档的IDF值
	public static double[] IDF() {
		double D = vectors.size();
		double[] Dt = new double[words.size()];
		double[] idf = new double[words.size()];
		DoubleMatrix2D matrix = createMatrix();
		for (int row = 0; row < words.size(); row++) {
			for (int column = 0; column < D; column++) {
				if (matrix.get(row, column) != 0)
					Dt[row]++;
			}
//			idf[row] = (Math.log((1 + D) / Dt[row])) / (Math.log(10));
			idf[row] = Math.log(D/Dt[row]+1);
		}
		return idf;// 【行】=idf
	}

	// 计算TF-IDF的值
	public static double[][] TFIDF() {
		double[][] tf = TF();
		double[] idf = IDF();
		double[][] tfidf = new double[words.size()][vectors.size()];
		for (int vectorNr = 0; vectorNr < vectors.size(); vectorNr++) {
			for (int wordNr = 0; wordNr < words.size(); wordNr++) {
				if (tf[wordNr][vectorNr] != 0) {
					tfidf[wordNr][vectorNr] = tf[wordNr][vectorNr]
							* idf[wordNr];
				}

			}
		}
		return tfidf;
	}

	public static double[][] Term() {
		double[][] x;
		Algebra algebra = new Algebra();
		DoubleMatrix2D matrix = createMatrix();
		DoubleMatrix2D singlematrix = new SparseDoubleMatrix2D(1,
				vectors.size());
		DoubleMatrix2D newmatrix = new SparseDoubleMatrix2D(words.size(),
				vectors.size());
		DoubleMatrix2D newmatrix2 = null;
		double[][] tfidf = TFIDF();
		double[][] value;// value多维3列【行、列、值】
		int[] num = new int[vectors.size()];// 文档数
		int[] flag = new int[words.size()];
		int[] flag2 = new int[words.size()];
		int totle = 0;
		int q = 0;
		int play = 0;
		for (int vectorNr = 0; vectorNr < vectors.size(); vectorNr++) {
			for (int wordNr = 0; wordNr < words.size(); wordNr++)
				if (tfidf[wordNr][vectorNr] != 0)
					num[vectorNr]++;
		}

		for (int vectorNr = 0; vectorNr < vectors.size(); vectorNr++) {

			value = new double[num[vectorNr]][3];
			int i = 0;
			for (int wordNr = 0; wordNr < words.size(); wordNr++) {
				if (tfidf[wordNr][vectorNr] != 0) {
					value[i][2] = tfidf[wordNr][vectorNr];
					value[i][0] = wordNr;
					value[i][1] = vectorNr;
					i++;
				}
			}
		
			Sort(value, i, 3);
			for (int j = 0; j < value.length / 2; j++) {
				if (flag[(int) value[j][0]] == 0) {
					totle++;
					flag[(int) value[j][0]] = 1;
				}
			}

			for (int j = 0; j < value.length / 2; j++) {
				if (flag2[(int) value[j][0]] == 0) {
					singlematrix = algebra.subMatrix(matrix, (int) value[j][0],
							(int) value[j][0], 0, vectors.size() - 1);
					
					for (int p = 0; p < vectors.size(); p++) {
						newmatrix.set(q, p, singlematrix.get(0, p));
					}
					q++;

					flag2[(int) value[j][0]] = 1;
				}
				toowords.add(words.get((int) value[j][0]));

			}

		}
		int rr = 4;
		int newtotle = 0;
		while (totle < vectors.size() && newtotle < vectors.size()) {
			newtotle = 0;
			int newq = 0;
			play = 1;
			rr--;
			toowords.clear();
			int[] newflag = new int[words.size()];
			int[] newflag2 = new int[words.size()];
			newmatrix2 = new SparseDoubleMatrix2D(words.size(), vectors.size());
			for (int vectorNr = 0; vectorNr < vectors.size(); vectorNr++) {

				value = new double[num[vectorNr]][3];
				int i = 0;
				for (int wordNr = 0; wordNr < words.size(); wordNr++) {
					if (tfidf[wordNr][vectorNr] != 0) {
						value[i][2] = tfidf[wordNr][vectorNr];
						value[i][0] = wordNr;
						value[i][1] = vectorNr;
						i++;
					}
				}
				
				Sort(value, i, 3);
				for (int j = 0; j < value.length / 2 + value.length / rr; j++) {
					if (value.length <= j)
						break;
					
					if (newflag[(int) value[j][0]] == 0) {
						newtotle++;
						newflag[(int) value[j][0]] = 1;
					}
				}

				for (int j = 0; j < value.length / 2 + value.length / rr; j++) {
					if (value.length <= j)
						break;
					if (newflag2[(int) value[j][0]] == 0) {
						singlematrix = algebra.subMatrix(matrix,
								(int) value[j][0], (int) value[j][0], 0,
								vectors.size() - 1);
						
						for (int p = 0; p < vectors.size(); p++) {
							newmatrix2.set(newq, p, singlematrix.get(0, p));
						}
						newq++;

						newflag2[(int) value[j][0]] = 1;
					}
					toowords.add(words.get((int) value[j][0]));
					
				}
			
			}
		}
		// 得到取出的Term词
		for (int i = 0; i < toowords.size(); i++) {
			if (newwords.indexOf(toowords.get(i)) == -1) {
				newwords.add(toowords.get(i));
				// System.out.println(toowords.get(i));
			}
		}
		
		if (play == 0) {
			TDmatrix = algebra.subMatrix(newmatrix, 0, totle - 1, 0,
					vectors.size() - 1);
			x = new double[TDmatrix.rows()][TDmatrix.columns()];
			for (int i = 0; i < TDmatrix.rows(); i++)
				for (int j = 0; j < TDmatrix.columns(); j++)
					x[i][j] = TDmatrix.get(i, j);
			return x;
		} else {
			TDmatrix = algebra.subMatrix(newmatrix2, 0, newtotle - 1, 0,
					vectors.size() - 1);
			x = new double[TDmatrix.rows()][TDmatrix.columns()];
			for (int i = 0; i < TDmatrix.rows(); i++)
				for (int j = 0; j < TDmatrix.columns(); j++)
					x[i][j] = TDmatrix.get(i, j);
			return x;
		}

	}

	public static void Sort(double[][] n, int row, int colum) {
		double temp;
		double row1;
		double colum1;
		for (int i = 0; i < row; i++)
			for (int j = 0; j < row - 1; j++)
				if (n[j][2] > n[j + 1][2]) {
					temp = n[j][2];
					row1 = n[j][0];
					colum1 = n[j][1];
					n[j][2] = n[j + 1][2];
					n[j][0] = n[j + 1][0];
					n[j][1] = n[j + 1][1];
					n[j + 1][2] = temp;
					n[j + 1][0] = row1;
					n[j + 1][1] = colum1;
				}
	}
}
