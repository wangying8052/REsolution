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
import java.util.ArrayList;


public class statisitic {
	 /**
	  * 求给定双精度数组中值的最大值
	  * 
	  * @param inputData
	  *            输入数据数组
	  * @return 运算结果,如果输入值不合法，返回为-1
	  */
	 public static double getMax(double[] inputData) {
	  if (inputData == null || inputData.length == 0)
	   return -1;
	  int len = inputData.length;
	  double max = inputData[0];
	  for (int i = 0; i < len; i++) {
	   if (max < inputData[i])
	    max = inputData[i];
	  }
	  return max;
	 }

	 /**
	  * 求求给定双精度数组中值的最小值
	  * 
	  * @param inputData
	  *            输入数据数组
	  * @return 运算结果,如果输入值不合法，返回为-1
	  */
	 public static double getMin(double[] inputData) {
	  if (inputData == null || inputData.length == 0)
	   return -1;
	  int len = inputData.length;
	  double min = inputData[0];
	  for (int i = 0; i < len; i++) {
	   if (min > inputData[i])
	    min = inputData[i];
	  }
	  return min;
	 }

	 /**
	  * 求给定双精度数组中值的和
	  * 
	  * @param inputData
	  *            输入数据数组
	  * @return 运算结果
	  */
	 public static double getSum(double[] inputData) {
	  if (inputData == null || inputData.length == 0)
	   return -1;
	  int len = inputData.length;
	  double sum = 0;
	  for (int i = 0; i < len; i++) {
	   sum = sum + inputData[i];
	  }

	  return sum;

	 }

	 
	 public static double getSum1(ArrayList<Double> inputData) {
		  if (inputData == null || inputData.size() == 0)
		   return -1;
		  int len = inputData.size();
		  double sum = 0;
		  for (int i = 0; i < len; i++) {
		   sum = sum + inputData.get(i);
		  }

		  return sum;

		 }
	 /**
	  * 求给定双精度数组中值的数目
	  * 
	  * @param input
	  *            Data 输入数据数组
	  * @return 运算结果
	  */
	 public static int getCount(double[] inputData) {
	  if (inputData == null)
	   return -1;

	  return inputData.length;
	 }

	 
	 public static int getCount1(ArrayList<Double> inputData) {
		  if (inputData == null)
		   return -1;

		  return inputData.size();
		 }

	 /**
	  * 求给定双精度数组中值的平均值
	  * 
	  * @param inputData
	  *            输入数据数组
	  * @return 运算结果
	  */
	 public static double getAverage(double[] inputData) {
	  if (inputData == null || inputData.length == 0)
	   return -1;
	  int len = inputData.length;
	  double result;
	  result = getSum(inputData) / len;
	  
	  return result;
	 }

	 
	 public static double getAverage1(ArrayList<Double> inputData) {
		  if (inputData == null || inputData.size() == 0)
		   return -1;
		  int len = inputData.size() ;
		  double result;
		  result = getSum1(inputData) / len;
		  
		  return result;
		 }
	 /**
	  * 求给定双精度数组中值的平方和
	  * 
	  * @param inputData
	  *            输入数据数组
	  * @return 运算结果
	  */
	 public static double getSquareSum(double[] inputData) {
		  if(inputData==null||inputData.length==0)
		      return -1;
		     int len=inputData.length;
		  double sqrsum = 0.0;
		  for (int i = 0; i <len; i++) {
		   sqrsum = sqrsum + inputData[i] * inputData[i];
		  }

		  
		  return sqrsum;
		 }

	 
	 
	 
	 public static double getSquareSum1(ArrayList<Double> inputData) {
		  if(inputData==null||inputData.size()==0)
		      return -1;
		     int len=inputData.size();
		  double sqrsum = 0.0;
		  for (int i = 0; i <len; i++) {
		   sqrsum = sqrsum + inputData.get(i) * inputData.get(i) ;
		  }

		  
		  return sqrsum;
		 }
	 /**
	  * 求给定双精度数组中值的方差
	  * 
	  * @param inputData
	  *            输入数据数组
	  * @return 运算结果
	  */
	 public static double getVariance(double[] inputData) {
	  int count = getCount(inputData);
	  double sqrsum = getSquareSum(inputData);
	  double average = getAverage(inputData);
	  double result;
	  result = (sqrsum - count * average * average) / count;

	     return result; 
	 }

	 
	 
	 public static double getVariance1(ArrayList<Double> inputData) {
		  int count = getCount1(inputData);
		  double sqrsum = getSquareSum1(inputData);
		  double average = getAverage1(inputData);
		  double result;
		  result = (sqrsum - count * average * average) / count;

		     return result; 
		 }

	 /**
	  * 求给定双精度数组中值的标准差
	  * 
	  * @param inputData
	  *            输入数据数组
	  * @return 运算结果
	  */
	 public static double getStandardDiviation(double[] inputData) {
	  double result;
	  //绝对值化很重要
	  result = Math.sqrt(Math.abs(getVariance(inputData)));
	  
	  return result;

	 }
	 
	 
	 public static double getStandardDiviation1(ArrayList<Double> inputData) {
		  double result;
		  //绝对值化很重要
		  result = Math.sqrt(Math.abs(getVariance1(inputData)));
		  
		  return result;

		 }
	 
	 
	   public static double[] bubbleSort(double[] args){//冒泡排序算法
		                  for(int i=0;i<args.length-1;i++){
		                          for(int j=i+1;j<args.length;j++){
		                                 if (args[i]>args[j]){
		                                	 double temp=args[i];
		                                         args[i]=args[j];
		                                         args[j]=temp;
		                                 }
		                         }
		                 }
		                return args;
		        }
	   
	   
	   
	   public static ArrayList<Double> bubbleSort1(ArrayList<Double> args){//冒泡排序算法
		                  for(int i=0;i<args.size()-1;i++){
		                          for(int j=i+1;j<args.size();j++){
		                                 if (args.get(i)>args.get(j)){
		                                	 double temp=args.get(i);
		                                	 args.set(i, args.get(j));
		                                	 args.set(j, temp);
		                                 }
		                         }
		                 }
		                return args;
		        }
	   
	   
	   public static double getminValue(double[] inputData) {
		   inputData = bubbleSort(inputData);
		   double min = 0;
		   if(inputData.length%2==0){
			//   System.out.println("偶数==");
			   min = (inputData[(inputData.length)/2-1]+inputData[(inputData.length)/2])/2;
			        
		   }else{
			   min = inputData[(inputData.length)/2];
		   }
		   
		return min;
		   
	   }
	   
	   
	   
	   public static double getminValue1(ArrayList<Double> inputData) {
		   inputData = bubbleSort1(inputData);
		   double min = 0;
		   if(inputData.size()%2==0){
			//   System.out.println("偶数==");
			   min = (inputData.get((inputData.size())/2-1)+inputData.get((inputData.size())/2))/2;
			        
		   }else{
			   min = inputData.get((inputData.size())/2);
		   }
		   
		return min;
		   
	   }
	   
}
