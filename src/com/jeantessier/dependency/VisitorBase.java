/*
 *  Copyright (c) 2001-2009, Jean Tessier
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *  
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *  
 *      * Neither the name of Jean Tessier nor the names of his contributors
 *        may be used to endorse or promote products derived from this software
 *        without specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jeantessier.dependency;

import java.util.*;

import org.apache.log4j.*;
import org.apache.tools.ant.taskdefs.FixCRLF.AddAsisRemove;

import com.Refactor.classparser.*;
import com.jeantessier.dependencyfinder.gui.SrcAction;

/**
 *  This is a basic implementation of Visitor.
 *
 *  @see Visitor
 *  @author Jean Tessier
 */
public abstract class VisitorBase implements Visitor {
    private TraversalStrategy strategy;
    private boolean flag = false;
    public static boolean judge = false;
    private LinkedList<Node> currentNodes = new LinkedList<Node>();

    public VisitorBase() {
        this(new ComprehensiveTraversalStrategy());
    }

    public VisitorBase(TraversalStrategy strategy) {
        this.strategy = strategy;
    }

    public TraversalStrategy getStrategy() {
        return strategy;
    }

//    /**
//     * @author revo
//     * @param nodes
//     * @return true is not match; false is matched.
//     */
//    public static boolean isMatch(Collection<? extends Node> nodes){
//    	for (Node node : nodes) {
//    		String name = node.getName();
//    		for (String classname : SrcAction.classname) {
//				if (false == classname.contains(name)) {
//					judge = true;
//					return judge;
//				}
//			}
//		}
//    	return judge;
//    }
    
    public void traverseNodes(Collection<? extends Node> nodes) {
    	
    	System.out.println("ndoes size ->"+nodes.size()+"--------------------------------------------");
    	
    	
        if (Logger.getLogger(getClass()).isDebugEnabled()) {
            Logger.getLogger(getClass()).debug("nodes = " + nodes);
        }
       
        for (Node node : getStrategy().order(nodes)) {
        	boolean c = node instanceof ClassNode;//后加的
        	boolean f = node instanceof FeatureNode;//后加的
        	if(c){//node 是class 类型
      
        	String name = node.getName();
        	if(SrcAction.classname.contains(name))
        	{
        		
        	flag = true;
        	ClassObject co = new ClassObject();
        	ArrayList<String> inboundClassList = new ArrayList<String>();
        	ArrayList<String> inboundFeatureList = new ArrayList<String>();
            for(Object obj : node.getInboundDependencies())
            {
     		  String in = obj.toString();
     		  if(SourceParser.judgeIsClassorFeature(in)){//是属性
     			  if(SourceParser.judgeFeatureifthisSystem(in, SrcAction.classname)&&(!SourceParser.methodonlyname(in).contains("$"))){
     			    inboundFeatureList.add(in);
     			  }
     		  }else{//是类
     			 if(SrcAction.classname.contains(SourceParser.getonlyclassname(in))){
     			 inboundClassList.add(in);
     			 }else{
      				if(SourceParser.judgeFeatureifthisSystem(in, SrcAction.classname)&&(!SourceParser.methodonlyname(in).contains("$"))){
         			    inboundFeatureList.add(in);
         			  }
     			 }
     		  }
     	    }
        	co.setInboundClassList(inboundClassList);
        	co.setInboundFeatureList(inboundFeatureList);
        	ArrayList<String> outboundClassList = new ArrayList<String>();
        	ArrayList<String> outboundFeatureList = new ArrayList<String>();
            ArrayList<String> outboundOtherClassList = new ArrayList<String>();
            for(Object obj : node.getOutboundDependencies())
            {
     		  String out = obj.toString() ; 
     		  if(SourceParser.judgeIsClassorFeature(out)){//是属性
     			  if(SourceParser.judgeFeatureifthisSystem(out, SrcAction.classname)&&(!SourceParser.methodonlyname(out).contains("$"))){
     			    outboundFeatureList.add(out);
     			  }
     		  }else{//是类     这里是继承关系
     			 if(SrcAction.classname.contains(SourceParser.getonlyclassname(out))){
     			 outboundClassList.add(out);
     			 }else{
      				if(SourceParser.judgeFeatureifthisSystem(out, SrcAction.classname)&&(!SourceParser.methodonlyname(out).contains("$"))){
         			    outboundFeatureList.add(out);
         			  }else{
         				  if((!out.contains("("))&&(!out.contains("{"))&&(!out.equals("java.lang.Object"))){
         					 outboundOtherClassList.add(out);
         				  }
         			  }
     			 }
     		  }
     	    }
            co.setOutboundClassList(outboundClassList);
            co.setOutboundFeatureList(outboundFeatureList);
            co.setOutboundOtherClassList(outboundOtherClassList);
            SrcAction.classesMap.put(name,co);
            
            
        	}else{
        		flag = false;
        	}
        	} else if(flag&&f){ //如果节点是属性
        		
            String featurename = node.getName();
            if(!SourceParser.methodonlyname(featurename).contains("$")){
            Map<String, Feature> featureMap = new HashMap<String, Feature>();
            Feature fe = new Feature();
            
            
            ArrayList<String> inboundClassList = new ArrayList<String>();
            ArrayList<String> inboundFeatureList = new ArrayList<String>();
            ArrayList<String> inboundAttributeList = new ArrayList<String>();
            ArrayList<String> inboundMethodList = new ArrayList<String>();
            
            for(Object obj : node.getInboundDependencies())
            {
         		  String in = obj.toString() ; 
         		  if(SourceParser.judgeIsClassorFeature(in)){//是属性
         			  if(SourceParser.judgeFeatureifthisSystem(in, SrcAction.classname)&&(!SourceParser.methodonlyname(in).contains("$"))){
         			    inboundFeatureList.add(in);
         			    inboundMethodList.add(in);
         			  }
         		  }else{//是类
         			 if(SrcAction.classname.contains(SourceParser.getonlyclassname(in))){
         			 inboundClassList.add(in);
         			 }else{
         				if(SourceParser.judgeFeatureifthisSystem(in, SrcAction.classname)&&(!SourceParser.methodonlyname(in).contains("$"))){
             			    inboundFeatureList.add(in);
             			    inboundAttributeList.add(in);
             			  }
         			 }
         		  } 
         	}
        	fe.setInboundClassList(inboundClassList);
        	fe.setInboundFeatureList(inboundFeatureList);
        	fe.setInboundMethodList(inboundMethodList);
        	fe.setInboundAttributeList(inboundAttributeList);
        	
        	ArrayList<String> outboundClassList = new ArrayList<String>();
        	ArrayList<String> outboundFeatureList = new ArrayList<String>();
            ArrayList<String> outboundAttributeList = new ArrayList<String>();
            ArrayList<String> outboundMethodList = new ArrayList<String>();
        	 for(Object obj : node.getOutboundDependencies())
             {
      		  String out = obj.toString() ; 
      		  if(SourceParser.judgeIsClassorFeature(out)){//method
      			  if(SourceParser.judgeFeatureifthisSystem(out, SrcAction.classname)&&(!SourceParser.methodonlyname(out).contains("$"))){
      			    outboundFeatureList.add(out);
      			    outboundMethodList.add(out);
      			  }
      		  }else{//是类
      			 if(SrcAction.classname.contains(SourceParser.getonlyclassname(out))){
      			 outboundClassList.add(out);
      			 }else{//attribute
      				if(SourceParser.judgeFeatureifthisSystem(out, SrcAction.classname)&&(!SourceParser.methodonlyname(out).contains("$"))){
         			    outboundFeatureList.add(out);
         			    outboundAttributeList.add(out);
         			  }
     			 }
      		  }
      	    }
             fe.setOutboundClassList(outboundClassList);
             fe.setOutboundFeatureList(outboundFeatureList);
             fe.setOutboundMethodList(outboundMethodList);
         	 fe.setOutboundAttributeList(outboundAttributeList);
             String classnameString =  SourceParser.Getonlyclassname(featurename);
             if(SrcAction.classesMap.containsKey(classnameString)){
            	 ClassObject cObject =  SrcAction.classesMap.get(classnameString);
            	 featureMap = cObject.getFeatureMap();
            	 featureMap.put(featurename,fe);
            	 cObject.setFeatureMap(featureMap);
            	 SrcAction.classesMap.put(classnameString, cObject);
             }else{
            	 System.out.println("abnormality");
             }
        	}
        	}
        	
            node.accept(this);
        }
//        }
        
    }

    public void traverseInbound(Collection<? extends Node> nodes) {
        for (Node node : getStrategy().order(nodes)) {
            node.acceptInbound(this);
        }
    }

    public void traverseOutbound(Collection<? extends Node> nodes) {
        for (Node node : getStrategy().order(nodes)) {
            node.acceptOutbound(this);
        }
    }

    protected Node getCurrentNode() {
        Node result = null;

        if (!currentNodes.isEmpty()) {
            result = currentNodes.getLast();
        }

        if (Logger.getLogger(getClass()).isDebugEnabled()) {
            Logger.getLogger(getClass()).debug(currentNodes + ": " + result);
        }

        return result;
    }

    protected void pushNode(Node currentNode) {
        if (Logger.getLogger(getClass()).isDebugEnabled()) {
            Logger.getLogger(getClass()).debug(currentNodes + " + " + currentNode);
        }

        currentNodes.addLast(currentNode);
    }

    protected Node popNode() {
        Node result = currentNodes.removeLast();

        if (Logger.getLogger(getClass()).isDebugEnabled()) {
            Logger.getLogger(getClass()).debug(currentNodes + " -> " + result);
        }

        return result;
    }

    public void visitPackageNode(PackageNode node) {
        boolean inScope = isInScope(node);
        
        if (inScope) {
            preprocessPackageNode(node);
            
            if (getStrategy().doPreOutboundTraversal()) {
                traverseOutbound(node.getOutboundDependencies());
            }
            
            if (getStrategy().doPreInboundTraversal()) {
                traverseInbound(node.getInboundDependencies());
            }
            
            preprocessAfterDependenciesPackageNode(node);
        }
            
        traverseNodes(node.getClasses());

        if (inScope) {
            postprocessBeforeDependenciesPackageNode(node);

            if (getStrategy().doPostOutboundTraversal()) {
                traverseOutbound(node.getOutboundDependencies());
            }
            
            if (getStrategy().doPostInboundTraversal()) {
                traverseInbound(node.getInboundDependencies());
            }
            
            postprocessPackageNode(node);
        }
    }

    protected boolean isInScope(PackageNode node) {
        return getStrategy().isInScope(node);
    }

    protected void preprocessPackageNode(PackageNode node) {
        pushNode(node);
    }
    
    protected void preprocessAfterDependenciesPackageNode(PackageNode node) {
        // Do nothing
    }
    
    protected void postprocessBeforeDependenciesPackageNode(PackageNode node) {
        // Do nothing
    }
    
    protected void postprocessPackageNode(PackageNode node) {
        if (node.equals(getCurrentNode())) {
            popNode();
        }
    }
    
    public void visitInboundPackageNode(PackageNode node) {
        // Do nothing
    }

    public void visitOutboundPackageNode(PackageNode node) {
        // Do nothing
    }

    public void visitClassNode(ClassNode node) {
        boolean inScope = isInScope(node);
        
        if (inScope) {
            preprocessClassNode(node);
            
            if (getStrategy().doPreOutboundTraversal()) {
                traverseOutbound(node.getOutboundDependencies());
            }
            
            if (getStrategy().doPreInboundTraversal()) {
                traverseInbound(node.getInboundDependencies());
            }

            preprocessAfterDependenciesClassNode(node);
        }
        
        traverseNodes(node.getFeatures());
            
        if (inScope) {
            postprocessBeforeDependenciesClassNode(node);

            if (getStrategy().doPostOutboundTraversal()) {
                traverseOutbound(node.getOutboundDependencies());
            }
            
            if (getStrategy().doPostInboundTraversal()) {
                traverseInbound(node.getInboundDependencies());
            }
            
            postprocessClassNode(node);
        }
    }

    protected boolean isInScope(ClassNode node) {
        return getStrategy().isInScope(node);
    }

    protected void preprocessClassNode(ClassNode node) {
        pushNode(node);
    }
    
    protected void preprocessAfterDependenciesClassNode(ClassNode node) {
        // Do nothing
    }

    protected void postprocessBeforeDependenciesClassNode(ClassNode node) {
        // Do nothing
    }

    protected void postprocessClassNode(ClassNode node) {
        if (node.equals(getCurrentNode())) {
            popNode();
        }
    }

    public void visitInboundClassNode(ClassNode node) {
        // Do nothing
    }

    public void visitOutboundClassNode(ClassNode node) {
        // Do nothing
    }

    public void visitFeatureNode(FeatureNode node) {
        if (isInScope(node)) {
            preprocessFeatureNode(node);
            
            if (getStrategy().doPreOutboundTraversal()) {
                traverseOutbound(node.getOutboundDependencies());
            }
            
            if (getStrategy().doPreInboundTraversal()) {
                traverseInbound(node.getInboundDependencies());
            }
            
            if (getStrategy().doPostOutboundTraversal()) {
                traverseOutbound(node.getOutboundDependencies());
            }
            
            if (getStrategy().doPostInboundTraversal()) {
                traverseInbound(node.getInboundDependencies());
            }
            
            postprocessFeatureNode(node);
        }
    }

    protected boolean isInScope(FeatureNode node) {
        return getStrategy().isInScope(node);
    }

    protected void preprocessFeatureNode(FeatureNode node) {
        pushNode(node);
    }
    
    protected void postprocessFeatureNode(FeatureNode node) {
        if (node.equals(getCurrentNode())) {
            popNode();
        }
    }

    public void visitInboundFeatureNode(FeatureNode node) {
        // Do nothing
    }

    public void visitOutboundFeatureNode(FeatureNode node) {
        // Do nothing
    }
}
