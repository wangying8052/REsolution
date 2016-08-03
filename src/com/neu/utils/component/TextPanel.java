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

package com.neu.utils.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import com.Refactor.Inheritance.InheritSuggestions;
import com.Refactor.Inheritance.RefactorInheritance;
import com.Refactor.Inheritance.SplitTrees;
import com.Refactor.Inheritance.extend;
import com.Refactor.NonInheritance.NonInheritanceRefactoring;
import com.Refactor.NonInheritance.Suggestions;
import com.Refactor.NonInheritance.UndoClass;
import com.Refactor.NonInheritance.UndoEntity;
import com.Refactor.NonInheritance.newclass;
import com.Refactor.NonInheritance.tool;
import com.jeantessier.dependencyfinder.gui.SrcAction;

public class TextPanel extends JPanel {  
	  
    private JTextArea text;  
    private JScrollPane scrollPanel = null;;  
    private int line;  
  
    public TextPanel() {  
        super(); 
        setLayout(new BorderLayout());  
        scrollPanel = new JScrollPane(getText()); 
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanel.getHorizontalScrollBar().setUI(new CBScrollBarUI(Color.gray));
        scrollPanel.getVerticalScrollBar().setUI(new CBScrollBarUI(Color.gray));
        add(scrollPanel); 
                
    }  
  
    public void addContent(String content) {  
        text.setText(content);  
    }  
  
    public JTextArea getText() {  
        if (text == null) {  
            text = new JTextArea();  
            text.setMargin(new Insets(5, 5, 5, 5));  
            text.setEditable(false);  
            text.addCaretListener(new CaretListener() {  
                public void caretUpdate(CaretEvent e) {  
                    if (text.getText().trim().length() == 0)  
                        return;  
  
                    int offset = e.getDot();  
                    // 计算光标所在行列  
                    try {  
                                                //得到光标所在的行数  
                        line = text.getLineOfOffset(offset);  
                        //System.out.println(line);  
                        // int col = offset - text.getLineStartOffset(line);  
                        // System.out.println("col："+col);  
  
                    } catch (BadLocationException e3) {  
                        e3.printStackTrace();  
                    }  
                }  
            });  
            text.addMouseListener(new MouseAdapter() {  
  
                @Override  
                public void mouseClicked(MouseEvent e) {  
                    if (e.getClickCount() == 1) {//单击 高亮  
                        DefaultHighlighter h = (DefaultHighlighter) text.getHighlighter();  
                        MyHighlightPainter p = new MyHighlightPainter(  
                                new Color(72, 240, 121));  
                        try {  
                            int start = text.getLineStartOffset(line);  
                            int end = text.getLineEndOffset(line);  
                            h.removeAllHighlights();  
                            h.addHighlight(start, end, p);  
                        } catch (BadLocationException e1) {  
                            e1.printStackTrace();  
                        }  
                    }  
                    if (e.getClickCount() == 2) {//双击  
                        String[] lines = text.getText().split("\n");  
                        String store = lines[line];  
                        System.out.println(store); 
                        
                        NewFrame newFrame = new NewFrame(store);
                        (new Thread(newFrame)).start();
                    }  
                }  
            });  
        }  
        return text;  
    }  
  
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {  
        public MyHighlightPainter(Color color) {  
            super(color);  
        }  
    }

	public void setText(String contentStr, int totalLines, int undoLines) {
		
		text.setText(contentStr);
		
		int startLines = totalLines - undoLines;
		DefaultHighlighter h = (DefaultHighlighter) text.getHighlighter();  
        MyHighlightPainter p = new MyHighlightPainter(new Color(255, 149, 184));
        h.removeAllHighlights();
        
		for (int line = startLines; line < totalLines; line++) {			
			try {  
	            int start = text.getLineStartOffset(line);  
	            int end = text.getLineEndOffset(line);  
	            h.addHighlight(start, end, p);  
	        } catch (BadLocationException e1) {  
	            e1.printStackTrace();  
	        }  
			
		}
	}

	public void setText(String contentStr) {
		text.setText(contentStr);
		
	}  
}  

class NewFrame extends JFrame implements Runnable{
	
	private String store = null;
	
	public NewFrame(String store){
		this.store = store;
//		initFrame();
	}

	private void initFrame() {
		this.setTitle("content");
		this.setIconImage(new ImageIcon(getClass().getResource("icons/logoicon.gif")).getImage());
		this.setLocation(800, 200);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(500, 450);
		this.setVisible(true);
	}
	
	private void displayDetil(String store) {
		
		JPanel comp = new JPanel(new BorderLayout());
		
		String contentStr = getContentStr(store);		
		
		JTextArea testare = new JTextArea(contentStr);
		
		JScrollPane scrollPane =new JScrollPane(testare);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		comp.add(scrollPane,BorderLayout.CENTER);
		
		this.add(comp);
	}

	private String getContentStr(String store) {
		
		String contentStr="";
		Suggestions sg = NonInheritanceRefactoring.sgs;
		
		if (store.indexOf("Method")>=0) {
			
			int index = Integer.parseInt(store.split(" ")[0]);
			UndoEntity entity = sg.getUndoEntitySort().get(index - 1);

			contentStr += "Method:" + entity.getEntityname() + "\r\n";
			contentStr += "Source Class:" + entity.getSource() + "\r\n";
			if (entity.newNo >= 0 ) {
				contentStr += "Target Class:" + entity.getTarget() +"_new_"+entity.newNo+ "\r\n";
			} else {
				contentStr += "Target Class:" + entity.getTarget() + "\r\n";
			}
			
			contentStr += "Modularity increment of system by this refactoring operation:"
					+ (double) Math.round(entity.getDetaQ() * 10000)/ 10000 + "\r\n";

		} else if(store.indexOf("Class")>=0){
			int zengjialei = 0;
			int index = Integer.parseInt(store.split(" ")[0]);
			int size = index-sg.getUndoEntitySort().size()-1;
			
			UndoClass value = sg.getUndoClasseSort().get(size);
			ArrayList<newclass> ExtractClass = SrcAction.classesMap
					.get(value.getClassname()).ExtractClass;
			zengjialei = zengjialei + ExtractClass.size() - 1;
			contentStr += "God Class "
					+ value.getClassname()
					+ " (Number of Entities:  "
					+ SrcAction.classesMap.get(value.getClassname()).featureMap
							.size() + ") needs to be split into " + ExtractClass.size() + " new classes " 
					+ "\r\n";
			contentStr += "Modularity increment of system by this refactoring operation: " + value.getDetaQ()
					+ "\r\n";
			for (int k = 0; k < ExtractClass.size(); k++) {
				if (ExtractClass.get(k).isLeaf) {
					contentStr += value.getClassname() + "_new_" + (k+1) + "------"
							+ "   (If it is the leaf node of inheritance hierarchy?(True/False))--" + ExtractClass.get(k).isLeaf
							+ "   Its super class is: " + ExtractClass.get(k).superclass 
							+ "\r\n";
				} else {
					contentStr += "Extracted class:  " + value.getClassname() + "_new_" + (k+1) + "------"
							+ "   (If it is the leaf node of inheritance hierarchy?(True/False))--" + ExtractClass.get(k).isLeaf
							+ "\r\n";
				}
				contentStr+="Its entities are described as follows:\r\n";
				for (int o = 0; o < ExtractClass.get(k).getExtractedclass()
						.size(); o++) {
					contentStr += ExtractClass.get(k).getExtractedclass()
							.get(o)
							+ "\r\n";
				}
			}
		
		} else if(store.indexOf("Tree")>=0){
			ArrayList<SplitTrees> splitTrees = RefactorInheritance.rv.SplitTree; 
			int index = Integer.parseInt(store.split(" ")[0]);
			SplitTrees splitTree = splitTrees.get(index-1);			
			contentStr+="Tree "+splitTree.TreeIndex+" need to be split---\r\n";		
			 ArrayList<ArrayList<ArrayList<extend>>> levelSplit = splitTree.getLevelSplit();
			 for(int j = 0; j < levelSplit.size(); j++){
				 
				 for(int k = 0; k < levelSplit.get(j).size(); k++){
					 contentStr+="level ----"+k+"\r\n";
					 for(int t = 0; t < levelSplit.get(j).get(k).size(); t++){
						 contentStr+= "Class---" + levelSplit.get(j).get(k).get(t).TreenodeName +"\r\n";
						 contentStr+= "IndependencyClassName-----"+levelSplit.get(j).get(k).get(t).IndependencyClassName+"\r\n";
						 contentStr+= "OutdependencyClassName-----"+levelSplit.get(j).get(k).get(t).OutdependencyClassName+"\r\n";
						 contentStr+= "SubClassName-----"+levelSplit.get(j).get(k).get(t).SubClassName+"\r\n";
						 contentStr+= "SuperClassName-----"+levelSplit.get(j).get(k).get(t).SuperClassName+"\r\n";
						 contentStr+= "Entities-----"+"\r\n";
						 for(int q = 0; q < levelSplit.get(j).get(k).get(t).cns.get(0).size(); q++){
							 contentStr+= levelSplit.get(j).get(k).get(t).cns.get(0).get(q)+"\r\n";
						 }
					 }
				 }
			 }

		}else{
			contentStr+="There is no need for classes to be refactored... ...";
		}
		
		
		return contentStr;
	}

	public void run() {
		displayDetil(this.store);
		initFrame();
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

}

