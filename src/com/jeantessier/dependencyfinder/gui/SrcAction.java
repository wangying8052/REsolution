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

package com.jeantessier.dependencyfinder.gui;


import com.Refactor.classparser.*;
import com.jeantessier.classreader.ClassfileLoader;
import com.jeantessier.classreader.TransientClassfileLoader;
import com.jeantessier.dependency.Node;
import com.jeantessier.dependency.VisitorBase;
import com.mathworks.toolbox.javabuilder.MWException;
import com.neu.utils.component.CircleProgressBar;
import com.neu.utils.component.ProgressDialog;
import com.neu.utils.io.FileUtils;
import com.neu.utils.io.FileViewer;
import com.neu.utils.listener.ProgressBarLisenter;
import com.Refactor.Inheritance.ConnComp;
import com.Refactor.Inheritance.MainInheritance;
import com.Refactor.NonInheritance.*;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.xml.sax.SAXException;

public class SrcAction extends AbstractAction implements Runnable {
    private REsolution model;
    private String encoding;
    private String dtdPrefix;

    private String indentText;
    private File file;
    
    public static Map <String, ClassObject> classesMap = new HashMap<String, ClassObject>();
    public static ArrayList<String> classname = new ArrayList();
    
    /**
	 * @author revo begin
	 */
    public static List<String> allJavaFilePaths = new ArrayList<String>();
    public static List<String> allSrcPaths = new ArrayList<String>();
    /**
	 * @author revo end
	 */
    
    public static String sourcepath;
    
    public static void setClassesMap(){
    	
    }
    
    public SrcAction(REsolution model, String encoding, String dtdPrefix) {
        this.model = model;
        this.encoding = encoding;
        this.dtdPrefix = dtdPrefix;
        
        putValue(Action.LONG_DESCRIPTION, "Extract semantic vocabularies from source codes");
        putValue(Action.NAME, "Src");
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("icons/SrcGreen.png")));
    }
    
    public void Tempvarible(){//wangying
    	
    	sourcepath = allSrcPaths.get(0);
    	
    	System.out.println("sourcepath->"+sourcepath);
    	
//    	sourcepath = "D:\\开源\\JHotDraw 7.0.6\\src\\";
     // sourcepath = "E:\\开源\\系统级别重构的实验对象\\jfreechart-0.9.10\\src\\";
	//sourcepath = "E:\\开源\\系统级别重构的实验对象\\Apache HSQLDB\\hsqldb\\src\\";
    	//sourcepath = "E:\\开源\\系统级别重构的实验对象\\jEdit\\";
    // sourcepath = "E:\\开源\\系统级别重构的实验对象\\jmol-9\\src\\";
    	
    	/**
    	 * @author revo begin
    	 */
		this.allJavaFilePaths = FileViewer.getListFiles(sourcepath, "java", true);
		
		this.classname = FileViewer.getAllClassFillName(allJavaFilePaths);
		/**
    	 * @author revo end
    	 */
    }

    public String getIndentText() {
        return indentText;
    }

    public void setIndentText(String indentText) {
        this.indentText = indentText;
    }

    public void actionPerformed(ActionEvent e) {
    	
//    	String rootPath=getClass().getResource("/").getFile().toString() + File.separator + "Temp.xml";
    	String rootPath="Temp.xml";
    	file = new File(rootPath);
    	if(file.exists()){
    		file.delete();
    	}
    	file = new File(rootPath);
    	//new Thread(this).start();
    	
    	/**
    	 * @author revo begin
    	 */
    	 JFileChooser chooser;
         if (model.getInputFiles().isEmpty()) {
             chooser = new JFileChooser(new File("."));
         } else {
             chooser = new JFileChooser(model.getInputFiles().iterator().next());
         }
         chooser.addChoosableFileFilter(new JavaBytecodeFileFilter());
         chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
         chooser.setMultiSelectionEnabled(true);
         int returnValue = chooser.showDialog(model, "Source");
         if (returnValue == JFileChooser.APPROVE_OPTION) {
             if (chooser.getSelectedFiles().length > 1) {
            	 JOptionPane.showConfirmDialog(null, "You should select only one project.", "system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
			 }else {
				 for (File file : chooser.getSelectedFiles()) {
		            	
	            	 allSrcPaths.add(file.getAbsolutePath()+"\\");
	             }
	             new Thread(this).start();
			}
         }
         /**
     	 * @author revo end
     	 */

    }
    
    public <T extends Node> Collection<T> order(Collection<T> collection) {
        return new ArrayList<T>(collection);
    }
    
    public static Map <String, ClassObject> getClassesMap(){
    	return classesMap;
    }
    
    
    
    public void run() {
    	Date start = new Date();
    	
        try {
        	
            model.getStatusLine().showInfo("Saving " + file.getName() + " ...");
            PrintWriter out = new PrintWriter(new FileWriter(file));
            com.jeantessier.dependency.Printer printer = new com.jeantessier.dependency.XMLPrinter(out, encoding, dtdPrefix);
            if (indentText != null) {
                printer.setIndentText(indentText);
            }
            
            Tempvarible();
        	
        	long time1 =  System.currentTimeMillis();// 当前时间对应的毫秒数
            printer.traverseNodes(model.getPackages());
            
            VisitorBase.judge = false;
            if(SrcAction.classesMap.size()==0){
            	VisitorBase.judge = true;
            	JOptionPane.showConfirmDialog(null, "The selected source codes do not match the Jar files.", "system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
            	SrcAction.allSrcPaths.clear();
            	return;
            }

            final ProgressBarLisenter barLisenter = new ProgressBarLisenter();
        	final CircleProgressBar progressBar = new CircleProgressBar();
        	SwingUtilities.invokeLater(new Runnable() {
    			@Override
    			public void run() {
    				new ProgressDialog(model, progressBar,"Preprocessing",model.getWidth(),model.getHeight());
    			}
    		});
        	barLisenter.addBars(progressBar);
        	barLisenter.addBars(model.getProgressBar());
        	barLisenter.beginSession();
        	model.getProgressBar().setMaximum(2*SrcAction.classname.size());
        	progressBar.setMaximum(2*SrcAction.classname.size());
        	
//          SourceParser.printlogs1(classesMap);
            out.close();
            model.getStatusLine().showInfo("Saved " + file.getName());
            model.getStatusLine().showInfo("preprocessing Classes...");
          
            preprocessing.preprocessingClasses(barLisenter); //预处理操作
//            Main.mainNoninheritance();
//          // MainAdjust.AdjustCoefficients();
//             MainInheritance.InheritanceOperations();
            long time2 = System.currentTimeMillis();
            System.out.println();
            System.out.println("解析+重构用时：" + (time2-time1));
            
            barLisenter.endSession();
        } catch (Exception e) {
        	
        	FileUtils.fileWrite("src.log", e.getMessage());
            model.getStatusLine().showError("Cannot save: " + e.getClass().getName() + ": " + e.getMessage());
        }
//       catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MWException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        Date stop = new Date();

        model.getStatusLine().showInfo("Done (" + ((stop.getTime() - start.getTime()) / (double) 1000) + " secs).");
        model.setTitle("REsolution - Src");
        model.activeNext(this);
        model.inactiveAt(1);
    }
}
