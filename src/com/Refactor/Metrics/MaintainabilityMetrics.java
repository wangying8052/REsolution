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

package com.Refactor.Metrics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.Refactor.classparser.ClassObject;
import com.Refactor.classparser.Feature;
import com.Refactor.classparser.SourceParser;
import com.jeantessier.dependencyfinder.gui.SrcAction;


public class MaintainabilityMetrics {

	private double Mt;
	private double WMC;
	private double RFC;
	private double LCOM2;
	private double MPC;
	private double ANA;
	private double NOC;
	private double CBO;

	
	public MaintainabilityMetrics(){
		 this.Mt = 1;
		 this.WMC = 0;
		 this.RFC = 0;
		 this.LCOM2 = 0;
		 this.MPC = 0;
		 this.ANA = 0;
		 this.NOC = 0;
		 this.CBO = 0;
	}
	public MaintainabilityMetrics(ArrayList<ArrayList<String>> classList, int[][] InheritanceMatrix, List<ClassObject> classesArrList){
		this.WMC = computeWMC(classList);
		this.RFC = computeRFC(classList, classesArrList);
		this.MPC = computeMPC(classList, classesArrList);
		this.CBO = computeCBO(classList, classesArrList);
		this.LCOM2 = computeLCOM2(classList, InheritanceMatrix, classesArrList);
		this.ANA = computeANA(classList, InheritanceMatrix);
		this.NOC = computeNOC(classList, InheritanceMatrix);
		this.Mt = 1;
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
						if(!relatedMethod.contains(String.valueOf(k))){
							relatedMethod.add(String.valueOf(k));
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
 * 每个类中方法的个数
 *  */
	public double computeWMC(ArrayList<ArrayList<String>> classList){
		double WMC = 0;
		double sumMethod = 0;
		for(int i = 0; i < classList.size(); i++){
			List<String> method = new ArrayList<String>();
			for(int j = 0; j < classList.get(i).size(); j++){
				if(classList.get(i).get(j).contains("(") || classList.get(i).get(j).contains("{")){
					String methodName = classList.get(i).get(j);
					String subName = null;
					if(methodName.contains("(")){
						subName = methodName.substring(0, methodName.indexOf("("));
					}
					else if(methodName.contains("{")){
						subName = methodName.substring(0, methodName.indexOf("{"));
					}
					int start = subName.lastIndexOf(".");
					methodName = methodName.substring(start+1, methodName.length());
					if(!method.contains(methodName)){
						method.add(methodName);
					}
				}
			}
			sumMethod += method.size();
			method.clear();
		}
		WMC = sumMethod / classList.size();
		return WMC;
	}
/*
 *  RFC = |Ms(C) U Mr(C)|
 *  Ms(C): C中方法的集合；
 *  Mr(C)：被C调用的方法的集合
 *  */
	public double computeRFC(ArrayList<ArrayList<String>> classList, List<ClassObject> classesArrList){
		double RFC = 0;
		double claRFC = 0;
		for(int i = 0; i < classList.size(); i++){
			List<String> Ms = new ArrayList<String>();
			List<String> Mc = new ArrayList<String>();
			double amountMs = 0;
			double amountMc = 0;
			for(int j = 0; j < classList.get(i).size(); j++){
				if(classList.get(i).get(j).contains("(") || classList.get(i).get(j).contains("{")){
					String methodName = classList.get(i).get(j);
					String subName = null;
					if(methodName.contains("(")){
						subName = methodName.substring(0, methodName.indexOf("("));
					}
					else if(methodName.contains("{")){
						subName = methodName.substring(0, methodName.indexOf("{"));
					}
					int start = subName.lastIndexOf(".");
					methodName = methodName.substring(start+1, methodName.length());
					if(!Ms.contains(methodName)){
						Ms.add(methodName);
					}
					boolean haveFound = false;
					for(int k = 0; k < classesArrList.size(); k++){
						if(classList.get(i).get(j).contains(classesArrList.get(k).getName())){
							for(int m = 0; m < classesArrList.get(k).getFeatureList().size(); m++){
								if(classesArrList.get(k).getFeatureList().get(m).getName() != null && !classesArrList.get(k).getFeatureList().get(m).getName().isEmpty()
										&& classesArrList.get(k).getFeatureList().get(m).getName().equals(classList.get(i).get(j))){
									Feature feature = classesArrList.get(k).getFeatureList().get(m);
									for(int n = 0; n < feature.getOutboundFeatureList().size(); n++){
										if(feature.getOutboundFeatureList().get(n).contains("(") || feature.getOutboundFeatureList().get(n).contains("{")){
											if(!Mc.contains(feature.getOutboundFeatureList().get(n)) && !classList.get(i).contains(feature.getOutboundFeatureList().get(n))){
												Mc.add(feature.getOutboundFeatureList().get(n));
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
			}
			amountMs = Ms.size();
			int size = Mc.size();
			int j = 0;
			while(j < size){
				String methodName = Mc.remove(0);
				j++;
				for(int k = 0; k < classList.size(); k++){
					if(classList.get(k).contains(methodName)){
						String subname = methodName.substring(0, methodName.indexOf("("));
						int start = subname.lastIndexOf(".");
						methodName = String.valueOf(k) + methodName.substring(start, methodName.length());
						if(!Mc.contains(methodName)){
							Mc.add(methodName);
							amountMc++;
						}
						break;
					}
				}
			}
			claRFC += amountMs + amountMc;
//			System.out.println(claRFC+"="+amountMs+"+" + amountMc);
			Ms.clear();
			Mc.clear();
		}
		RFC = claRFC / classList.size();
		return RFC;
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
	
	public double computeLCOM2(ArrayList<ArrayList<String>> methodsList,int[][] Matrix,List<ClassObject> classesArrList )
	{
		double LCOM2 = 0;
		double size=Matrix.length;
		ArrayList<String> clas=new ArrayList<String>();
		for(int k=0;k<classesArrList.size();k++)
			clas.add(classesArrList.get(k).getName());
		for(int i=0;i<methodsList.size();i++)
		{
			if(!methodsList.get(i).isEmpty()&&!SrcAction.classesMap.get(SourceParser.Getonlyclassname(methodsList.get(i).get(0))).interfaceornot)
			{
			if(methodsList.get(i).size()==0)
				continue;
			//一个类中的所有方法
			ArrayList<String> OneClassMethod=methodsList.get(i);
			int classnameindex=-1;
			double totle=0;
			double gongxiangsum=0;
			totle=OneClassMethod.size()*(OneClassMethod.size()-1)/2;
			ArrayList<String> FeatureList=new ArrayList<String>();
			for(int j=0;j<OneClassMethod.size();j++)
			{
				String method1="";
				ArrayList<String> Method2List=new ArrayList<String>();
				ArrayList<ArrayList<String>> parentFeature=new ArrayList<ArrayList<String>>();
				//取出的是方法而非属性(第i个类中的第j个方法：)
				if(OneClassMethod.get(j)==null)
					continue;
				if(OneClassMethod.get(j).contains("("))
				{
					method1=OneClassMethod.get(j);
					//类i.方法j的outbound
					char a1[]=OneClassMethod.get(j).toCharArray();
					int index3=0;
					for(int t=0;t<a1.length;t++)
					{
						if(a1[t]=='(')
							break;
						if(a1[t]=='.')
							index3=t;
					}
					String classname2="";
					for(int t=0;t<index3;t++)
						classname2+=a1[t];
					classnameindex=clas.indexOf(classname2);
					if(classnameindex==-1)
						continue;
					List<String> MethodAndFeature=new ArrayList<String>();
					for(int m=0;m<classesArrList.get(classnameindex).FeatureList.size();m++)
					{
						if(classesArrList.get(classnameindex).FeatureList.get(m).getName()==null)
							continue;
						{							
							MethodAndFeature=classesArrList.get(classnameindex).FeatureList.get(m).outboundFeatureList;										
						}
					}
					//如果方法1没有outbound
					if(MethodAndFeature.size()==0)
						break;
					//System.out.println("clas==="+classesArrList.get(classnameindex).getName());
					//System.out.println("method1---"+method1);
					//System.out.println("outboundMethodAndFeature==**"+MethodAndFeature);	
					for(int y=j+1;y<methodsList.get(i).size();y++)
					{
						if(methodsList.get(i).get(y)==null)
							continue;
						if(methodsList.get(i).get(y).contains("("))
							Method2List.add(methodsList.get(i).get(y));
					}						
					//System.out.println("Method2===="+Method2List);						
					//----------验证成功----------------
					//找其他方法中的属性名
					for(int k=0;k<Method2List.size();k++)
					{
						if(Method2List.get(k)==null)
							continue;
						char a[]=Method2List.get(k).toCharArray();
						int index=0;
						for(int t=0;t<a.length;t++)
						{
							if(a[t]=='(')
								break;
							if(a[t]=='.')
								index=t;
						}
						String classname="";
						for(int t=0;t<index;t++)
							classname+=a[t];
						//若方法2是其他类的
						if(method1.contains(classname))
						{
							int index2=clas.indexOf(classname);
							if(index2==-1)
							{
//								System.out.println("==="+classname);
//								System.out.println("---"+method1);
							}
							if(index2!=-1)//方法2在其他类中
							{
								//System.out.println("方法2的方法名（另类）===="+Method2List.get(k));
								ArrayList<String> arr=new ArrayList<String>();
								for(int u=0;u<classesArrList.get(index2).FeatureList.size();u++)
								{
									if(classesArrList.get(index2).FeatureList.get(u).getName()==null)
										continue;
									if(classesArrList.get(index2).FeatureList.get(u).getName().equals(Method2List.get(k)))
									{
										
										for(int n=0;n<classesArrList.get(index2).FeatureList.get(u).outboundFeatureList.size();n++)
										{
											
											if(!classesArrList.get(index2).FeatureList.get(u).outboundFeatureList.get(n).contains("("))
											{
												arr.add(classesArrList.get(index2).FeatureList.get(u).outboundFeatureList.get(n));
											}
										}
										break;
									}
								}
								parentFeature.add(arr);
							}
						}
						else//方法2也在此类中
						{
							//System.out.println("方法2的方法名（同类）===="+Method2List.get(k));
							int index2=clas.indexOf(classname);
							if(index2!=-1)
							{
								ArrayList<String> arr=new ArrayList<String>();
								for(int u=0;u<classesArrList.get(index2).FeatureList.size();u++)
								{
									//System.out.println("*******"+classesArrList.get(index2).FeatureList.get(u).getName());
									//System.out.println("======="+Method2List.get(k));
									if(classesArrList.get(index2).FeatureList.get(u).getName()==null)
										continue;
									if(classesArrList.get(index2).FeatureList.get(u).getName().equals(Method2List.get(k)))
									{	
										for(int n=0;n<classesArrList.get(index2).FeatureList.get(u).outboundFeatureList.size();n++)
										{											
											if(!classesArrList.get(index2).FeatureList.get(u).outboundFeatureList.get(n).contains("("))
											{
												arr.add(classesArrList.get(index2).FeatureList.get(u).outboundFeatureList.get(n));
											}
										}
										break;
									}
								}
								parentFeature.add(arr);
							}
						}
					}						
				}
				//System.out.println("方法2List中包含的属性"+parentFeature);				
				ArrayList<String> method1Feature=new ArrayList<String>();
				if(OneClassMethod.get(j)==null)
					continue;
				char aa[]=OneClassMethod.get(j).toCharArray();
				int index5=0;
				for(int t=0;t<aa.length;t++)
				{
					if(aa[t]=='(')
						break;
					if(aa[t]=='.')
						index5=t;
				}
				String classname="";
				for(int t=0;t<index5;t++)
					classname+=aa[t];
				int lei=clas.indexOf(classname);
				if(lei==-1)
					continue;				
				for(int r=0;r<classesArrList.get(lei).FeatureList.size();r++)
				{
					if(classesArrList.get(lei).FeatureList.get(r).getName()==null)
						continue;
					if(classesArrList.get(lei).FeatureList.get(r).getName().equals(OneClassMethod.get(j)))
					{
						for(int k=0;k<classesArrList.get(lei).FeatureList.get(r).outboundFeatureList.size();k++)
						{
							if(!classesArrList.get(lei).FeatureList.get(r).outboundFeatureList.get(k).contains("("))
								method1Feature.add(classesArrList.get(lei).FeatureList.get(r).outboundFeatureList.get(k));
						}
						break;
					}
				}			
				//System.out.println("方法1的属性===="+method1Feature);
				//找方法1和方法2们有没有共享属性
				for(int k=0;k<method1Feature.size();k++)
				{
					if(method1Feature.get(k)==null)
						continue;
					char b[]=method1Feature.get(k).toCharArray();
					int index2=0;
					for(int r=0;r<b.length;r++)
					{
						if(b[r]=='(')
							break;
						if(b[r]=='.')
							index2=r;
					}
					String Method1Featurename=method1Feature.get(k).substring(index2+1, b.length);
					//System.out.println("方法1纯属性名==="+Method1Featurename);
					for(int t=0;t<parentFeature.size();t++)
					{
						
						for(int m=0;m<parentFeature.get(t).size();m++)
						{
							char a[]=parentFeature.get(t).get(m).toCharArray();
							int index=0;
							for(int r=0;r<a.length;r++)
							{
								if(a[r]=='(')
									break;
								if(a[r]=='.')
									index=r;
							}
							String Featurename=parentFeature.get(t).get(m).substring(index+1, a.length);
							if(Featurename.equals(Method1Featurename))
							{
								gongxiangsum++;
								break;
							}								
						}
					}
				}
				if(gongxiangsum==0)//当无共享属性关系时，判断方法的调用
				{
					char a1[]=OneClassMethod.get(j).toCharArray();
					int index3=0;
					for(int t=0;t<a1.length;t++)
					{
						if(a1[t]=='(')
							break;
						if(a1[t]=='.')
							index3=t;
					}
					String classname2="";
					for(int t=0;t<index3;t++)
						classname2+=a1[t];
					int index=clas.indexOf(classname2);
					ArrayList<String> FeatureAndMethod=new ArrayList<String>();
					for(int k=0;k<classesArrList.get(index).FeatureList.size();k++)
					{
						if(classesArrList.get(index).FeatureList.get(k).getName()==null)
							continue;
						if(classesArrList.get(index).FeatureList.get(k).getName().equals(OneClassMethod.get(j)))
						{
							FeatureAndMethod.addAll(classesArrList.get(index).FeatureList.get(k).outboundFeatureList);
							break;
						}
					}
					int flag=0;
					for(int k=0;k<FeatureAndMethod.size();k++)
					{
						if(FeatureAndMethod.get(k).contains("("))
						{
							char a[]=FeatureAndMethod.get(k).toCharArray();
							int index2=0;
							for(int t=0;t<a.length;t++)
							{
								if(a[t]=='(')
									break;
								if(a[t]=='.')
									index2=t;
							}
							String classname1="";
							for(int t=0;t<index2;t++)
								classname1+=a[t];
							if(OneClassMethod.get(j).contains(classname1))
							{								
								flag=1;
								break;
							}							
						}
					}						
					if(gongxiangsum==0)//方法1没有调用其他方法时，找其他方法调用他
					{
						ArrayList<String> method=new ArrayList<String>();
						for(int r=0;r<methodsList.get(i).size();r++)
						{
							if(r!=j)
							{
								method.add(methodsList.get(i).get(r));
							}
						}
						for(int m=0;m<method.size();m++)
						{
							if(method.get(m)==null)
								continue;
							char a[]=method.get(m).toCharArray();
							int index2=0;
							for(int t=0;t<a.length;t++)
							{
								if(a[t]=='(')
									break;
								if(a[t]=='.')
									index2=t;
							}
							String classname1="";
							for(int t=0;t<index2;t++)
								classname1+=a[t];
							int leihao=clas.indexOf(classname1);
							if(leihao==-1)
								continue;
							for(int t=0;t<classesArrList.get(leihao).FeatureList.size();t++)
							{
								if(classesArrList.get(leihao).FeatureList.get(t).getName()==null)
									continue;
								if(classesArrList.get(leihao).FeatureList.get(t).getName().equals(method.get(m)))
								{
									List<String> methodfeature=classesArrList.get(leihao).FeatureList.get(t).outboundFeatureList;
									if(methodfeature.indexOf(OneClassMethod.get(j))!=-1)
									{
										flag=1;
										break;
									}									
								}
							}
							if(flag==1)
								break;
						}
					}
					if(flag==1)
					{
						gongxiangsum++;
					}
											
				}
			}
			double lcom = totle-2*gongxiangsum;
			if(lcom>0&&lcom<10000){
			LCOM2+=lcom;	
			}
			
		}
	}
		int hh = 0;
		for(Map.Entry<String, ClassObject> entry1 : SrcAction.classesMap.entrySet()) {
			ClassObject cObject = entry1.getValue();
			if(cObject.interfaceornot){
				hh++;
			}
		 }
		
		if(LCOM2<0)
			LCOM2=0;
		LCOM2/=(size-hh);
		return LCOM2;
	}
	
	private ArrayList<String> DelMethodtoOtherClass(List<String> methodAndFeature,String method) 
	{
		ArrayList<String> methodList=new ArrayList<String>();
		char a[]=method.toCharArray();
		int index=0;
		for(int i=0;i<a.length;i++)
		{
			if(a[i]=='(')
				break;
			if(a[i]=='.')
				index=i;
		}
		String class1="";
		for(int i=0;i<index;i++)
			class1+=a[i];
		//System.out.println(index+"=========="+class1);
		for(int i=0;i<methodAndFeature.size();i++)
		{
			if(methodAndFeature.get(i).contains(class1))
			{
				methodList.add(methodAndFeature.get(i));
				//System.out.println("uuu"+methodAndFeature.get(i));
			}
				
		}
		return methodList;
	}
	
	private ArrayList<String> OneClassMethod(List<ClassObject> classesArrList, int i) 
	{//得到一个类下的所有方法，删掉类中的属性
			ArrayList<String> method=new ArrayList<String>();
			//System.out.println("---"+classesArrList.get(i).getName());
			for(int j=0;j<classesArrList.get(i).FeatureList.size();j++)
			{
				if(classesArrList.get(i).FeatureList.get(j).getName()==null)
					continue;
				int index1=classesArrList.get(i).FeatureList.get(j).getName().indexOf("(");
				if(index1==-1)
					continue;					
				else
					method.add(classesArrList.get(i).FeatureList.get(j).getName());
			}
		return method;
	}
	
	private ArrayList<String> OnlyFeature(List<ClassObject> classesArrList, int i) 
	{
		ArrayList<String> feature=new ArrayList<String>();
			for(int j=0;j<classesArrList.get(i).FeatureList.size();j++)
			{
				if(classesArrList.get(i).FeatureList.get(j).getName()==null)
					continue;
				int index1=classesArrList.get(i).FeatureList.get(j).getName().indexOf("(");
				if(index1==-1)
					feature.add(classesArrList.get(i).FeatureList.get(j).getName());
									
				else
					continue;
			}

		return feature;
	}
	
	public double computeNOC(ArrayList<ArrayList<String>> methodsList,int[][] Matrix)
	{
		double NOC = 0;
		double sum=0;
		double size=Matrix.length;
		for(int i=0;i<Matrix.length;i++)
		{
			double Onesum=0;
			for(int j=0;j<Matrix.length;j++)
			{
				Onesum+=Matrix[i][j];
			}
			sum+=Onesum;
		}
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
		NOC=sum/size;
		System.out.println("平均每个类的亲孩子个数："+NOC);
		return NOC;
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
//				System.out.println("===="+i+"====="+j);
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
	
	public double getWMC() {
		return WMC;
	}
	public void setWMC(double wMC) {
		WMC = wMC;
	}
	public double getRFC() {
		return RFC;
	}
	public void setRFC(double rFC) {
		RFC = rFC;
	}
	public double getLCOM() {
		return LCOM2;
	}
	public void setLCOM(double lCOM) {
		LCOM2 = lCOM;
	}
	public double getMPC() {
		return MPC;
	}
	public void setMPC(double mPC) {
		MPC = mPC;
	}
	public double getANA() {
		return ANA;
	}
	public void setANA(double dIT) {
		ANA = dIT;
	}
	public double getNOC() {
		return NOC;
	}
	public void setNOC(double nOC) {
		NOC = nOC;
	}
	public double getMt() {
		return Mt;
	}
	public void setMt(double mt) {
		Mt = mt;
	}
	public double getCBO() {
		return CBO;
	}
	public void setCBO(double cBO) {
		CBO = cBO;
	}

}
