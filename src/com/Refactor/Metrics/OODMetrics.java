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

package com.Refactor.Metrics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.Refactor.NonInheritance.UndoClass;
import com.Refactor.classparser.ClassObject;
import com.jeantessier.dependencyfinder.gui.SrcAction;
import com.Refactor.classparser.Feature;



public class OODMetrics {

	private double DSC;
	private double ANA;
	private double DAM;
	private double MPC;
	private double CAM;
	private double MOA;
	private double NOP;
	private double CIS;
	private double NOM;
	private double CBO;
	private double reusability;
	private double flexibility;
	private double understandability;

	
	public OODMetrics(){
		this.DSC = 0;
		this.ANA = 0;
		this.DAM = 0;
		this.MPC = 0;
		this.CAM = 0;
		this.MOA = 0;
		this.NOP = 0;
		this.CIS = 0;
		this.NOM = 0;
		this.CBO = 0;
		this.reusability = 1;
		this.flexibility = 1;
		this.understandability = -0.99;
	}
	
	public OODMetrics(ArrayList<ArrayList<String>> classList, int[][] InheritanceMatrix, List<ClassObject> classesArrList){
		this.DAM = computeDAM(classList, classesArrList);
		this.MPC = computeMPC(classList, classesArrList);
		this.CAM = computeCAM(classList);
		this.MOA = computeMOA(classList, classesArrList);
		this.CIS = computeCIS(classList, classesArrList);
		this.ANA = computeANA(classList, InheritanceMatrix);
		this.DSC = computeDSC(classList, InheritanceMatrix);
		this.NOP = computeNOP(classList,InheritanceMatrix);
		this.NOM = computeNOM(classList,InheritanceMatrix);
		this.CBO = computeCBO(classList, classesArrList);
		this.reusability = 1;
		this.flexibility = 1;
		this.understandability = -0.99;
	}
	
/*
 *  类C调用过的外部类和调用过类C的外部类de总数
 *  */	
	public double computeCBO(ArrayList<ArrayList<String>> classList, List<ClassObject> classesArrList){
		double 	CBO = 0;
		double amountCBO = 0;
		for(int i = 0; i < classList.size(); i++){
			List<String> methodList = new ArrayList<String>();
			for(int j = 0; j < classList.get(i).size(); j++){
				String featureName = classList.get(i).get(j);
				boolean haveFound = false;
				
				for(int k = 0; k < classesArrList.size(); k++){
					if(featureName.contains(classesArrList.get(k).getName())){
						for(int m = 0; m < classesArrList.get(k).getFeatureList().size(); m++){
							if(classesArrList.get(k).getFeatureList().get(m).getName() != null && !classesArrList.get(k).getFeatureList().get(m).getName().isEmpty()
									&& classesArrList.get(k).getFeatureList().get(m).getName().equals(featureName)){
								Feature feature = classesArrList.get(k).getFeatureList().get(m);
								for(int n = 0; n < feature.getOutboundFeatureList().size(); n++){
									String outMethod = feature.getOutboundFeatureList().get(n);
									if(!classList.get(i).contains(outMethod) && !methodList.contains(outMethod)){
										methodList.add(outMethod);
									}
								}
								haveFound = true;
								break;
							}
						}
						if(haveFound){
							haveFound = false;
							break;
						}
					}
				}
				
			}
			for(int j = 0; j < classesArrList.size(); j++){
				for(int k = 0; k < classesArrList.get(j).getFeatureList().size(); k++){
					Feature outfeature = classesArrList.get(j).getFeatureList().get(k);
					if(outfeature.getName() != null && !outfeature.getName().isEmpty() 
							&& !methodList.contains(outfeature.getName()) && !classList.get(i).contains(outfeature.getName())){
						for(int m = 0; m < outfeature.getOutboundFeatureList().size(); m++){
							if(classList.get(i).contains(outfeature.getOutboundFeatureList().get(m))){
								methodList.add(outfeature.getName());
								break;
							}
						}
					}
				}
			}
			List<String> relatedMethod = new ArrayList<String>();
			for(int j = 0; j < methodList.size(); j++){
				String name = methodList.get(j);
				for(int k = 0; k < classList.size(); k++){
					if(classList.get(k).contains(name)){
						name = String.valueOf(k);
						if(!relatedMethod.contains(name)){
							relatedMethod.add(name);
						}
						break;
					}
				}
			}
			amountCBO += relatedMethod.size();
			relatedMethod.clear();
			methodList.clear();
		}
		CBO = amountCBO / classList.size();
		return CBO;
	}
/*
 *  类C中private(protected)属性占所有属性的比值
 *  */	
	public double computeDAM(ArrayList<ArrayList<String>> classList, List<ClassObject> classesArrList){
		double DAM = 0;
		double amontDAM = 0;
		for(int i = 0; i < classList.size(); i++){
			List<String> attrList = new ArrayList<String>();
			List<String> publicAttrList = new ArrayList<String>();
			double privateAttr = 0;
			for(int j = 0; j < classList.get(i).size(); j++){
				if(!classList.get(i).get(j).contains("(") && !classList.get(i).get(j).contains("{") 
						&& !attrList.contains(classList.get(i).get(j))){
					attrList.add(classList.get(i).get(j));
				}
			}
			for(int j = 0; j < classesArrList.size(); j++){
				for(int k = 0; k < classesArrList.get(j).getFeatureList().size(); k++){
					if(!classList.get(i).contains(classesArrList.get(j).getFeatureList().get(k).getName())){
						for(int m = 0; m < attrList.size(); m++){
							if(classesArrList.get(j).getFeatureList().get(k).getOutboundFeatureList().contains(attrList.get(m))){
								if(!publicAttrList.contains(attrList.get(m))){
									publicAttrList.add(attrList.get(m));
								}
							}
						}
					}
				}
			}
			privateAttr = attrList.size() - publicAttrList.size();
			if(attrList.size() != 0){
				amontDAM += (privateAttr/attrList.size());
			}
		}
		DAM = amontDAM / classList.size();
		return DAM;
	}
	
/*
 *  类C调用过的外部类的方法和调用过类C的外部类的方法de总数
 *  */
	public double computeMPC(ArrayList<ArrayList<String>> classList, List<ClassObject> classesArrList){
		double MPC = 0;
		double amountMPC = 0;
		for(int i = 0; i < classList.size(); i++){
			List<String> methodList = new ArrayList<String>();
			for(int j = 0; j < classList.get(i).size(); j++){
				String featureName = classList.get(i).get(j);
				boolean haveFound = false;
				for(int k = 0; k < classesArrList.size(); k++){
					if(featureName.contains(classesArrList.get(k).getName())){
						for(int m = 0; m < classesArrList.get(k).getFeatureList().size(); m++){
							if(classesArrList.get(k).getFeatureList().get(m).getName() != null && !classesArrList.get(k).getFeatureList().get(m).getName().isEmpty()
									&& classesArrList.get(k).getFeatureList().get(m).getName().equals(featureName)){
								Feature feature = classesArrList.get(k).getFeatureList().get(m);
								for(int n = 0; n < feature.getOutboundFeatureList().size(); n++){
									if(feature.getOutboundFeatureList().get(n).contains("(") || feature.getOutboundFeatureList().get(n).contains("{")){
										String outMethod = feature.getOutboundFeatureList().get(n);
										if(!classList.get(i).contains(outMethod) && !methodList.contains(outMethod)){
											methodList.add(outMethod);
										}
									}
								}
								haveFound = true;
								break;
							}
						}
						if(haveFound){
							haveFound = false;
							break;
						}
					}
				}
			}
			for(int j = 0; j < classesArrList.size(); j++){
				for(int k = 0; k < classesArrList.get(j).getFeatureList().size(); k++){
					Feature outfeature = classesArrList.get(j).getFeatureList().get(k);
					if(outfeature.getName() != null && !outfeature.getName().isEmpty() 
							&& (outfeature.getName().contains("(") || outfeature.getName().contains("{"))
							&& !methodList.contains(outfeature.getName()) && !classList.get(i).contains(outfeature.getName())){
						for(int m = 0; m < outfeature.getOutboundFeatureList().size(); m++){
							if(classList.get(i).contains(outfeature.getOutboundFeatureList().get(m))){
								methodList.add(outfeature.getName());
								break;
							}
						}
					}
				}
			}
			List<String> relatedMethod = new ArrayList<String>();
			for(int j = 0; j < methodList.size(); j++){
				String name = methodList.get(j);
				for(int k = 0; k < classList.size(); k++){
					if(classList.get(k).contains(name)){
						String sub = null;
						if(name.contains("(")){
							sub = name.substring(0, name.indexOf("("));
						}
						else if(name.contains("{")){
							sub = name.substring(0, name.indexOf("{"));
						}
						int start = sub.lastIndexOf(".");
						name = String.valueOf(k) + name.substring(start, name.length());
						if(!relatedMethod.contains(name)){
							relatedMethod.add(name);
						}
						break;
					}
				}
			}
			amountMPC += relatedMethod.size();
			relatedMethod.clear();
			methodList.clear();
		}
		MPC = amountMPC / classList.size();
		return MPC;
	}
/*
 *  CAMC = |Pi|总和/|T|*n
 *  Pi = Mi 交 T
 *  T：class中参数的类型个数
 *  Mi：method中参数的类型个数
 *  */	
	public double computeCAM(ArrayList<ArrayList<String>> classList){
		double CAM = 0;
		double amountCAM = 0;
		for(int i = 0; i < classList.size(); i++){
			List<String> parameterList = new ArrayList<String>();
		/////////////////所有方法的
			for(int j = 0; j < classList.get(i).size(); j++){
				if(classList.get(i).get(j).contains("(") || classList.get(i).get(j).contains("{")){
					String method = classList.get(i).get(j);
					String pareList = method.substring(method.indexOf("(")+1, method.length()-1);
					while(pareList.contains(",")){
						String para = pareList.substring(0, pareList.indexOf(","));
						if(!parameterList.contains(para)){
							parameterList.add(para);
						}
						pareList = pareList.substring(pareList.indexOf(",")+2);
					}
					if(!parameterList.contains(pareList)){
						parameterList.add(pareList);
					}
				}
			}
		////////////每个方法的
			int CAMinMethod = 0;
			List<String> classMethodList = new ArrayList<String>();
			for(int j = 0; j < classList.get(i).size(); j++){
				if(classList.get(i).get(j).contains("(") || classList.get(i).get(j).contains("{")){
					String meth = classList.get(i).get(j);
					List<String> paraInMethod = new ArrayList<String>();
					String currentMethod = meth;
					String sub = null;
					if(currentMethod.contains("(")){
						sub = currentMethod.substring(0, currentMethod.indexOf("("));
					}
					else if(currentMethod.contains("{")){
						sub = currentMethod.substring(0, currentMethod.indexOf("{"));
					}
					currentMethod = String.valueOf(i) + currentMethod.substring(sub.lastIndexOf("."), currentMethod.length());
					if(!classMethodList.contains(currentMethod)){
						classMethodList.add(currentMethod);
						String pare = meth.substring(meth.indexOf("(")+1, meth.length()-1);
						while(pare.contains(",")){
							String para = pare.substring(0, pare.indexOf(","));
							if(!paraInMethod.contains(para)){
								paraInMethod.add(para);
							}
							pare = pare.substring(pare.indexOf(",")+2);
						}
						if(!paraInMethod.contains(pare)){
							paraInMethod.add(pare);
						}
					}
					if(parameterList.size() != 0){
						CAMinMethod += (paraInMethod.size() / parameterList.size());
					}
				}
			}
			if(classMethodList.size() != 0){
				amountCAM += (CAMinMethod / classMethodList.size());
			}
		}
		CAM = amountCAM / classList.size();
		
		return CAM;
	}
/*
 *  每个类中定义的自定义类型的属性的个数的均值
 *  */	
	public double computeMOA(ArrayList<ArrayList<String>> classList, List<ClassObject> classesArrList){
		double MOA = 0;
		double amountMOA = 0;
		for(int i = 0; i < classList.size(); i++){
			List<String> classTypeAttr = new ArrayList<String>();
			List<String> classTypeclass = new ArrayList<String>();
			for(int j = 0; j < classList.get(i).size(); j++){
				if(!classList.get(i).get(j).contains("(") && !classList.get(i).get(j).contains("{") && !classList.get(i).get(j).startsWith("java.")){
					if(!classTypeAttr.contains(classList.get(i).get(j))){
						classTypeAttr.add(classList.get(i).get(j));
					}
				}
			}
			for(int j = 0; j < classTypeAttr.size(); j++){
				String attrName = classTypeAttr.get(j);
				Feature attr = null;
				boolean hasFound = false;
				for(int k = 0; k < classesArrList.size(); k++){
					if(attrName.contains(classesArrList.get(k).getName())){
						for(int m = 0; m < classesArrList.get(k).getFeatureList().size(); m++){
							if(attrName.equals(classesArrList.get(k).getFeatureList().get(m).getName())){
								attr = classesArrList.get(k).getFeatureList().get(m);
								hasFound = true;
								break;
							}
						}
					}
					if(hasFound){
						hasFound = false;
						break;
					}
				}
				//System.out.println("++++++++++++"+attrName);
				//System.out.println("------------"+attr+": "+attr.getName());
				for(int q = 0; q < attr.getOutboundClassList().size(); q++){
					for(int k = 0; k < classesArrList.size(); k++){
						if(classesArrList.get(k).getName().equals(attr.getOutboundClassList().get(q))){
							List<String> attrList = new ArrayList<String>();
							for(int m = 0; m < classesArrList.get(k).getFeatureList().size(); m++){
								if(!attrList.contains(classesArrList.get(k).getFeatureList().get(m).getName())){
									attrList.add(classesArrList.get(k).getFeatureList().get(m).getName());
								}
							}
							for(int m = 0; m < attrList.size(); m++){
								for(int n = 0; n < classList.size(); n++){
									if(n != i && classList.get(n).contains(attrList.get(m))){
										if(!classTypeclass.contains(String.valueOf(n))){
											classTypeclass.add(String.valueOf(n));
										}
									}
								}
							}
							break;
						}
					}
				}
			}
			amountMOA += classTypeclass.size();
		}
		MOA = amountMOA / classList.size();
		return MOA;
	}
/*
 * 一个类中public方法的个数均值
 * */
	public double computeCIS(ArrayList<ArrayList<String>> classList, List<ClassObject> classesArrList){
		double CIS = 0;
		double amountCIS = 0;
		for(int i = 0; i < classList.size(); i++){
			int sumpublicMethod = 0;
			List<String> methodList = new ArrayList<String>();
			for(int j = 0; j < classList.get(i).size(); j++){
				if(classList.get(i).get(j).contains("(") || classList.get(i).get(j).contains("{")){
					String methodName = classList.get(i).get(j);
					String currentName = methodName;
					String sub = null;
					if(currentName.contains("(")){
						sub = currentName.substring(0, currentName.indexOf("("));
					}
					else if(currentName.contains("{")){
						sub = currentName.substring(0, currentName.indexOf("{"));
					}
					currentName = String.valueOf(i) + currentName.substring(sub.lastIndexOf("."), currentName.length());
					if(!methodList.contains(currentName)){
						methodList.add(currentName);
						boolean hasFound = false;
						for(int k = 0; k < classesArrList.size(); k++){
							for(int m = 0; m < classesArrList.get(k).getFeatureList().size(); m++){
								if(!classList.get(i).contains(classesArrList.get(k).getFeatureList().get(m).getName())){
									Feature method = classesArrList.get(k).getFeatureList().get(m);
									if(method.getOutboundFeatureList().contains(methodName)){
										sumpublicMethod++;
										hasFound = true;
										break;
									}
								}
							}
							if(hasFound){
								hasFound = false;
								break;
							}
						}
					}
				}
			}
			amountCIS += sumpublicMethod;
		}
		CIS = amountCIS / classList.size();
		return CIS;
	}
	
	public  double computeNOP(ArrayList<ArrayList<String>> methodsList,int[][] Matrix)
	{//平均重写方法数
		double NOP=0;
		double sum=0;
		ArrayList<ArrayList<Integer>> inboundClassList=GetOutbound(Matrix) ;
		ArrayList<ArrayList<Integer>> outboundClassList=GetInbound(inboundClassList);
		for(int i=0;i<outboundClassList.size();i++)
		{
			int child=outboundClassList.get(i).get(0);
			int parent=outboundClassList.get(i).get(1);
			for(int j=0;j<methodsList.get(child).size();j++)
			{
				int index=methodsList.get(child).get(j).indexOf("(");
				if(index==-1)
					continue;
				char[] a=methodsList.get(child).get(j).substring(0, index).toCharArray();
				int index2=0;
				for(int k=0;k<a.length;k++)
				{
					if(a[k]=='.')
						index2=k;
				}
				String Childmethodname=methodsList.get(child).get(j).substring(index2+1,a.length);
				Childmethodname+=methodsList.get(child).get(j).substring(index,methodsList.get(child).get(j).length());
				for(int t=0;t<methodsList.get(parent).size();t++)
				{
					if(methodsList.get(parent).get(t).contains(Childmethodname))
					{
						//System.out.println("存在重写--孩子："+methodsList.get(child).get(j));
						//System.out.println("存在重写--父亲："+methodsList.get(parent).get(t));
						sum++;
					}
				}
			}
			
		}
		double size=Matrix.length;
		NOP=sum/size;
		System.out.println("平均重写方法数  ：  "+NOP);
		System.out.println("NOP--sum ：  "+sum);
		System.out.println("NOP--size ：  "+sum);
		return NOP;
	}
	
	private static ArrayList<ArrayList<String>> DeleteDuplication(ArrayList<ArrayList<String>> methodsList) 
	{
		for(int i=0;i<methodsList.size();i++)
		{

			for(int j=0;j<methodsList.get(i).size();j++)
			{
				int index4=methodsList.get(i).get(j).indexOf("(");
				if(index4==-1)
					continue;
				char[] a=methodsList.get(i).get(j).substring(0, index4).toCharArray();
				int index=0;
				for(int k=0;k<a.length;k++)
				{
					if(a[k]=='.')
						index=k;
				}
				String classname=methodsList.get(i).get(j).substring(0,index);
				String methodname=methodsList.get(i).get(j).substring(index+1,a.length);
				methodname+=methodsList.get(i).get(j).substring(index4,methodsList.get(i).get(j).length());
				for(int k=j+1;k<methodsList.get(i).size();k++)
				{
					int index3=methodsList.get(i).get(k).indexOf("(");
					if(index3==-1)
						continue;
					char[] b=methodsList.get(i).get(k).substring(0, index3).toCharArray();
					int index2=0;
					for(int t=0;t<b.length;t++)
					{
						if(b[t]=='.')
							index2=t;
					}
					String methodname2=methodsList.get(i).get(k).substring(index2+1,b.length);
					methodname2+=methodsList.get(i).get(k).substring(index3,methodsList.get(i).get(k).length());
					if(methodname2.equals(methodname))
					{
						methodsList.get(i).remove(k);
						k--;
					}

				}
			}
			//System.out.println("====="+methodsList.get(i));
		}
		return methodsList;
	}

	//得到outboundList
	private ArrayList<ArrayList<Integer>> GetOutbound(int[][] Matrix) 
	{
		ArrayList<ArrayList<Integer>> outboundClassList=new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<Matrix.length;i++)
		{
			ArrayList<Integer> outbound =new ArrayList<Integer>();
			int flag=0;
			for(int j=0;j<Matrix.length ;j++)
			{
				if(Matrix[i][j]!=0)
				{
					flag=1;
					break;
				}				
			}
			if(flag==1)//第i类有父类
			{
				outbound.add(i);
				for(int j=0;j<Matrix.length ;j++)
				{
					if(Matrix[i][j]!=0)
					{
						outbound.add(j);
						break;
					}				
				}
			}
			if(outbound.size()!=0)
				outboundClassList.add(outbound);
		}
		return outboundClassList;
	}
	
	private ArrayList<ArrayList<Integer>> GetInbound(ArrayList<ArrayList<Integer>> outboundClassList) 
	{
		ArrayList<ArrayList<Integer>> inboundClassList=new ArrayList<ArrayList<Integer>>();
		
		//！！！转换inboundClassList！！！
		for(int i=0;i<outboundClassList.size();i++)
		{
			ArrayList<Integer> inbound = new ArrayList<Integer> ();		
			for(int j=1;j<outboundClassList.get(i).size();j++)//outboundClassList[i]中遍历父类
			{
				int parent=outboundClassList.get(i).get(j);
				//若inboundClassList已取完parent类，不再重复取
				int flag=0;
				for(int m=0;m<inboundClassList.size();m++)
				{
					if(inboundClassList.get(m).get(0).equals(parent))
					{
						flag=1;
						break;
					}						
				}
				if(flag==1)
					continue;
				inbound.add(parent);
				inbound.add(outboundClassList.get(i).get(0));			
				for(int k=0;k<outboundClassList.size();k++)
				{
					if(k==i)
						continue;
					for(int t=1;t<outboundClassList.get(k).size();t++)
					{						
						if(outboundClassList.get(k).get(t).equals(parent))
						{
							inbound.add(outboundClassList.get(k).get(0));
							break;
						}
					}					
				}				
			}
			if(!inbound.isEmpty())
				inboundClassList.add(inbound);			
		}
		for(int i=0;i<inboundClassList.size();i++)
		{
			if(inboundClassList.get(i).size()==0)
			{
				inboundClassList.remove(i);
				i--;
			}
		}
		return inboundClassList;
	}
	
	//找祖先节点
	private static ArrayList<Integer> DealHighClass(ArrayList<ArrayList<Integer>> inboundClassList,ArrayList<ArrayList<Integer>> outboundClassList) 	
	{
		//找未处理的最顶层的类
		ArrayList<Integer> NoParentClass = new ArrayList<Integer> ();
		for(int i=0;i<inboundClassList.size();i++)
		{
			int j=0;
			for(;j<outboundClassList.size();j++)
			{
				if(inboundClassList.get(i).get(0).equals(outboundClassList.get(j).get(0)))
					break;
			}
			if(j==outboundClassList.size())
				NoParentClass.add(inboundClassList.get(i).get(0));
		}
		//System.out.println("未处理的最顶层的类:"+NoParentClass);
		return NoParentClass;
	}
	
	private static int Connect(int Child, int Parent, ArrayList<Integer> noParent,ArrayList<ArrayList<Integer>> outboundClassList) 
	{
		//System.out.println("inbound!!!"+inboundClassList);
//		System.out.println("Child      "+Child+"        Parent          "+Parent+"");
		int index=noParent.indexOf(Parent);
		if(index!=-1)
			noParent.remove(index);
		if(Child==Parent)
		{
			//System.out.println("对对对");
			return 1;
		}
		if(noParent.contains(Child))
		{
			//System.out.println("错错错");
			return 0;
		}
		for(int i=0;i<outboundClassList.size();i++)
		{
			if(outboundClassList.get(i).get(0).equals(Child))
			{
				//System.out.println("========="+outboundClassList.get(i)+Child);
				if(Connect(outboundClassList.get(i).get(1),Parent,noParent,outboundClassList)==1)
				{
					return 1;
				}				
			}
		}
		return 0;
	}

	public double computeNOM(ArrayList<ArrayList<String>> methodsList,int[][] Matrix)
	{
		//平均方法数
				double NOM = 0;
				double sum=0;				
				ArrayList<ArrayList<String>> NewMethodsList=DeleteDuplication(methodsList);
				double size=NewMethodsList.size();
				for(int i=0;i<NewMethodsList.size();i++)
				{
					sum+=NewMethodsList.get(i).size();
				}			
				NOM=sum/size;
				System.out.println("平均方法数："+NOM);
				return NOM;
	}
	
	public double computeANA(ArrayList<ArrayList<String>> methodsList,int[][] Matrix)
	{
		double ANA = 0;
		ArrayList<ArrayList<Integer>> inboundClassList=GetOutbound(Matrix) ;
		ArrayList<ArrayList<Integer>> outboundClassList=GetInbound(inboundClassList);
		ArrayList<Integer> NoParentClass=new ArrayList<Integer>();
		NoParentClass=DealHighClass(inboundClassList,outboundClassList);
		int SumAncestors=0;
		for(int i=0;i<Matrix.length;i++)
		{
			for(int j=0;j<Matrix.length;j++)
			{
				if(i==j)
					continue;
				if(Connect(i,j,NoParentClass,outboundClassList)==1)
				{
					//System.out.println("孩子："+i+"父亲："+j+"  距离："+Distance);
					SumAncestors++;
				}		
			}			
		}
		//System.out.println("所有类的祖先总数："+SumAncestors);
		double SumAncestors1=SumAncestors;
		double size=Matrix.length;
		for(int i=0;i<Matrix.length;i++)
		{
			int flag=0;
			for(int j=0;j<Matrix.length;j++)
			{
				if(Matrix[i][j]!=0)
				{
					flag=1;
					break;
				}
			}
			if(flag==0)
				size--;
		}		
		ANA=SumAncestors1/size;
		System.out.println("平均类的祖先总数："+ANA);		
		return ANA;
	}

	public double computeDSC(ArrayList<ArrayList<String>> methodsList,int[][] Matrix)
	{
		double DSC = 0;
		DSC=Matrix.length;
		System.out.println("类的个数  ：  "+DSC);
		return DSC;
	}
		
	public double getDSC() {
		return DSC;
	}
	public void setDSC(double dSC) {
		DSC = dSC;
	}
	public double getANA() {
		return ANA;
	}
	public void setANA(double aNA) {
		ANA = aNA;
	}
	public double getDAM() {
		return DAM;
	}
	public void setDAM(double dAM) {
		DAM = dAM;
	}
	public double getMPC() {
		return MPC;
	}
	public void setMPC(double mPC) {
		MPC = mPC;
	}
	public double getCAM() {
		return CAM;
	}
	public void setCAM(double cAM) {
		CAM = cAM;
	}
	public double getMOA() {
		return MOA;
	}
	public void setMOA(double mOA) {
		MOA = mOA;
	}
	public double getNOP() {
		return NOP;
	}
	public void setNOP(double nOP) {
		NOP = nOP;
	}
	public double getCIS() {
		return CIS;
	}
	public void setCIS(double cIS) {
		CIS = cIS;
	}
	public double getNOM() {
		return NOM;
	}
	public void setNOM(double nOM) {
		NOM = nOM;
	}

	public double getReusability() {
		return reusability;
	}

	public void setReusability(double reusability) {
		this.reusability = reusability;
	}

	public double getFlexibility() {
		return flexibility;
	}

	public void setFlexibility(double flexibility) {
		this.flexibility = flexibility;
	}

	public double getUnderstandability() {
		return understandability;
	}

	public void setUnderstandability(double understandability) {
		this.understandability = understandability;
	}

	public double getCBO() {
		return CBO;
	}

	public void setCBO(double cBO) {
		CBO = cBO;
	}
}
