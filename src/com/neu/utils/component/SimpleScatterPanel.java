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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.Refactor.Inheritance.InheritSuggestions;
import com.Refactor.Inheritance.RefactorInheritance;
import com.Refactor.NonInheritance.NonInheritanceRefactoring;
import com.Refactor.NonInheritance.Suggestions;
import com.neu.utils.data.Data;
import com.neu.utils.data.DotWithColor;
import com.neu.utils.service.Observer;
import com.neu.utils.service.Subject;

public class SimpleScatterPanel extends JPanel implements Observer ,MouseListener,MouseMotionListener{
	
	public static void main(String [] args){
		
		JFrame frame = new JFrame();
		
		SimpleScatterPanel ssp = new SimpleScatterPanel();
		
		frame.add(ssp);
		
		frame.setTitle("test");
		frame.setLocation(300, 200);
		frame.setSize(600, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/**
	 * 边框属性值
	 */
	private int LEFT_BORDER ;
	private int RIGHT_BORDER ;
	private int TOP_BORDER ;
	private int BOTTOM_BORDER ;
	
	/**
	 * 列属性值
	 */
	private int stepColumnValue;
	private int columnCount;
	private int stepColumnPX;
	/**
	 * 行属性值
	 */
	private double maxValue;
	private double minValue;
	private double stepRowValue;
	private int rowCount;
	private int stepRowPX;
	
	/**
	 * 点的宽/高
	 */
	private int dotWidth = 10;
	private int dotHeight = 10;
	
	
	/**
	 * 十字线
	 */
	private int mouseX = -1;
	private int mouseY = -1;
	
	private static Subject data;//观察数据
	private Graphics2D g2d;//画笔
	private Color green =  new Color(10, 111, 40);
	private Color pink =  new Color(255, 149, 184);
	private Color purple =  new Color(121,9,204);
	private Stroke stroke = new BasicStroke(2f);
	
	private boolean flag =false;
	
	private List<DotWithColor> dotList = new ArrayList<DotWithColor>();
	
	public SimpleScatterPanel() {
		initPanel();
	}

	private void initPanel() {
		
		this.setBackground(Color.white);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
	}

	public SimpleScatterPanel(Subject data) {
		this.data = data;
		this.data.registerObserver(this);
		
		initPanel();
	}
	
	@Override
	public void paint(Graphics g) {
        super.paint(g);
		g2d = (Graphics2D) g;
		drawTable();
		if (flag == false) {
			
			int x = RefactorInheritance.Xcurrent;
			double y = RefactorInheritance.Ycurrent;
						
			DotWithColor dot = calculateDot(x, y);
			dot.setUndoLength(0);
			dotList.add(dot);
			flag = true;
		}
		drawData();
		
	}
	
	/**
	 * 画数据
	 */
	private void drawData() {
		
		for (int i = 0; i < dotList.size(); i++) {
			DotWithColor dot = dotList.get(i);	
			g2d.setColor(dot.getColor());
			g2d.setStroke(stroke);
			if (dot.isClicked() == true) {
				g2d.fillOval(dot.x, dot.y, dotWidth, dotHeight);
			}else {
				g2d.drawOval(dot.x, dot.y, dotWidth, dotHeight);
			}			
		}
		g2d.setColor(purple);
		g2d.drawLine(mouseX, TOP_BORDER, mouseX, getHeight()-BOTTOM_BORDER);
		g2d.drawLine(LEFT_BORDER, mouseY, getWidth()-RIGHT_BORDER, mouseY);
		g2d.setColor(Color.black);
		
	}

	/**
	 * 画表格
	 */
	private void drawTable() {
		initBorder();
		int widthLength = getWidth()-LEFT_BORDER-RIGHT_BORDER;//宽度
		int hightLength = getHeight()-TOP_BORDER-BOTTOM_BORDER;//高度
		calculateColumn(widthLength);
		calculateRow(hightLength);
		
		
		g2d.drawRect(LEFT_BORDER, TOP_BORDER, widthLength, hightLength);
		for (int i = 0; i < this.columnCount; i++) {
			
			int x = LEFT_BORDER+stepColumnPX*i;
			int y = getHeight()-BOTTOM_BORDER;
			g2d.drawString(this.stepColumnValue*i+"", x, y+15);
			
			while (y>TOP_BORDER) {
				g2d.drawLine(x, y, x, y-5);
				 y -=10; 
			}
			
		}
		
		for (int i = 0; i < this.rowCount; i++) {
			double str = (double)Math.round((this.stepRowValue*i+this.minValue)*10000)/10000;
			int y = getHeight()-BOTTOM_BORDER-this.stepRowPX*i;
			int x = LEFT_BORDER; 
			g2d.drawString(str+"", x/2, y);

			while (x<getWidth()-RIGHT_BORDER) {
				g2d.drawLine(x, y, x+5, y);
				 x +=10; 
			}
			
		}
	}
	
	/**
	 * 根据x,y坐标计算点
	 * @param xValue
	 * @param qValue
	 * @return
	 */
	private DotWithColor calculateDot(int xValue, double qValue) {
		
		int x = (int)(new Float(this.stepColumnPX * xValue)/this.stepColumnValue + LEFT_BORDER);
		int y = (int)(getHeight()-this.stepRowPX * (qValue-this.minValue)/this.stepRowValue- BOTTOM_BORDER);		
		DotWithColor dot = new DotWithColor(x - dotWidth/2, y - dotHeight/2, dotWidth, dotHeight);
		dot.setColor(green);
		dot.setxValue(xValue);
		dot.setqValue(qValue);
		return dot;
	}
	
	/**i
	 * 计算列数
	 */
	private void calculateColumn(int length){
	
		int column = RefactorInheritance.Xtotal;
		
		if (column<10) {
			this.stepColumnValue = 1;
			this.columnCount = 10;
		} else {
			float realStep = (new Float(column))/10;
			this.stepColumnValue =(int)(realStep+0.5);
				
			if (realStep>this.stepColumnValue) {//小于四  舍去
				Float stepfloat = new Float(this.stepColumnValue);
					
				float difference =(float)Math.round((realStep-stepfloat)*100)/100;//保留两位小数
				
				int increament = (int)Math.ceil((difference*10)/this.stepColumnValue);
				
				this.columnCount = increament+10;
			} else {
				this.columnCount = 10;
			}
		}
		
		this.stepColumnPX = (int)(length*1.0 / this.columnCount);
		
	}

	
	/**
	 * 计算行数
	 * @param length
	 */
	private void calculateRow(int length) {
		this.maxValue = RefactorInheritance.Ymax;
		this.minValue = RefactorInheritance.Ymin;
		
		this.stepRowValue = ((double)Math.round((this.maxValue - this.minValue)*10000)/10000)/10;
		this.rowCount = 11;
		this.stepRowPX = (int)(length*1.0/this.rowCount);
	}
	
	private void initBorder() {
		LEFT_BORDER = (int) (this.getWidth() * 1.0 / 8);
		RIGHT_BORDER = (int) (this.getWidth() * 1.0 / 8);
		TOP_BORDER = (int) (this.getHeight() * 1.0 / 10);
		BOTTOM_BORDER = (int) (this.getHeight() * 1.0 / 8);
	}
	
	/**
	 * 重置节点列表中其余未选中节点的颜色
	 * @param dot 选中节点
	 */
	private void resetDefaultColor(DotWithColor dot){
		
		int index = dotList.indexOf(dot);
		for (int i = 0; i < dotList.size(); i++) {
			if (index != i) {
				dotList.get(i).setColor(green);
				dotList.get(i).setClicked(false);
			}
		}
	}
	
	
	public void update(Suggestions sg) {
				
		int x = RefactorInheritance.Xcurrent;
		double y = RefactorInheritance.Ycurrent;
		
		boolean isExist = false;
		
		for (int i = 0; i < dotList.size(); i++) {
			DotWithColor dot = dotList.get(i);
			if (dot.getxValue() == x && dot.getqValue() == y) {
				isExist = true;
			}
		}
		if (isExist == false) {
			DotWithColor dot = calculateDot(x, y);
			dot.setUndoLength(((InheritSuggestions)sg).getUndoLength());
			dotList.add(dot);
		}
		
		repaint();
	}

	public List<DotWithColor> getDotList() {
		return dotList;
	}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		showPopupMenu(e);
		drawHVLine(e);
	}

	private void drawHVLine(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();

		if (mouseX>LEFT_BORDER && mouseX<getWidth()-RIGHT_BORDER && mouseY>TOP_BORDER && mouseY<getHeight()-BOTTOM_BORDER) {
			this.mouseX = mouseX;
			this.mouseY = mouseY;
			repaint();
		}
	}

	private void showPopupMenu(MouseEvent e) {
		Point point = e.getPoint();
        int x = (int)point.getX();
		int y = (int)point.getY();
		for (int i = 0; i < dotList.size(); i++) {
			DotWithColor d = dotList.get(i);
			if (x>=d.getX() && x<=(d.getX()+d.getWidth()) && 
					y>=d.getY() && y<=(d.getY()+d.getHeight())) {
		        
		        double yValue = (double)Math.round((d.getqValue()-minValue)*10000)/10000;
		        String str = "Benefit (Modularity increment):" +yValue +"\r\n";
		        str +="Costs (Frequency of moving method/field):"+ d.getxValue();
		   
		        JPopupMenu popup = new JPopupMenu();
		        popup.setLayout(new BorderLayout());
		        JPanel infoPanel = createtInfoPanel(str);  
		        popup.add(infoPanel, BorderLayout.CENTER);  
		        popup.show(this, x+5, y-5);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
				
		leftClick(e);
		
		rightClick(e);
		
	}

	private void rightClick(MouseEvent e) {
		
		if (SwingUtilities.isRightMouseButton(e) == true) {
			
			Point point = e.getPoint();
	        int x = (int)point.getX();
			int y = (int)point.getY();
			
			boolean isDotScope = false;
			
			DotWithColor dot=null;
			
			for (int i = 0; i < dotList.size(); i++) {
				DotWithColor d = dotList.get(i);
				if (x>=d.getX() && x<=(d.getX()+d.getWidth()) && 
						y>=d.getY() && y<=(d.getY()+d.getHeight())) {
					isDotScope = true;
					dot = d;
					break;
				}
			}
			getPopup(this,isDotScope,dot).show(e.getComponent(), e.getX(), e.getY());
			
		}
		
	}
	
	private static JPopupMenu getPopup(final JPanel panel, boolean isDotScope, final DotWithColor dot) {

		JPopupMenu popup = new JPopupMenu("Popup");

		if (isDotScope == true) {
			JMenuItem delete = new JMenuItem("delete");
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((SimpleScatterPanel) panel).getDotList().remove(dot);
					panel.repaint();
				}
			});
			popup.add(delete);
		}
		
		JMenuItem clear = new JMenuItem("clear");
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((SimpleScatterPanel) panel).getDotList().clear();
				
				InheritSuggestions sgs = RefactorInheritance.sgs;
				sgs.setUndoLength(0);
				((Data)data).setMeasurements(sgs);
				
				panel.repaint();
			}
		});
		popup.add(clear);
		
		
		popup.setInvoker(panel);
		return popup;
	}


	private void leftClick(MouseEvent e) {
		if (e.getClickCount() == 1) {
			int mouseX = e.getX();
			int mouseY = e.getY();
			for (int i = 0; i < dotList.size(); i++) {
				DotWithColor dot = dotList.get(i);
				if (mouseX>dot.x && mouseX<dot.x+dotWidth && mouseY>dot.y && mouseY<dot.y+dotHeight) {
					dot.setColor(pink);
					dot.setClicked(true);
					resetDefaultColor(dot);
					showPopupMenu(e);
					this.paint(getGraphics());
					
					InheritSuggestions sgs = RefactorInheritance.sgs;
					sgs.setUndoLength(dot.getUndoLength());
					((Data)this.data).setMeasurements(sgs);
					return;
				}
			}
		}
	}

	private JPanel createtInfoPanel(String content) {
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
        infoPanel.add(new JTextArea(content),BorderLayout.CENTER);  
        return infoPanel;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}

}


