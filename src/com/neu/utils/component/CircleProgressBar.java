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
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JProgressBar;

public class CircleProgressBar extends JProgressBar {
	
	/**
	 * 背景颜色。
	 */
	private Color backgroundColor;

	/**
	 * 前景颜色。
	 */
	private Color foregroundColor;
	
	
	public CircleProgressBar() {
		
		setMinimum(0);
		setMaximum(100);
		setValue(0);
		setBackground(new Color(209, 206, 200));
		setForeground(new Color(172, 168, 163));
	}
	
	/**
	 * 绘制圆形进度条。
	 * 
	 * @param g
	 *            画笔。
	 */
	@Override
	public void paint(Graphics g) {
		
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		int fontSize = 0;
		if (getWidth() >= getHeight()) {
			x = (getWidth() - getHeight()) / 2 + 25;
			y = 25;
			width = getHeight() - 50;
			height = getHeight() - 50;
			fontSize = getWidth() / 8;
		} else {
			x = 25;
			y = (getHeight() - getWidth()) / 2 + 25;
			width = getWidth() - 50;
			height = getWidth() - 50;
			fontSize = getHeight() / 8;
		}
		
		Graphics2D graphics2d = (Graphics2D) g;
		
		graphics2d.setColor(new Color(238, 238, 238));
		graphics2d.fillRect(0, 0, getWidth(), getHeight());
		
		// 开启抗锯齿
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		graphics2d.setStroke(new BasicStroke(15.0f));
		graphics2d.setColor(backgroundColor);
		graphics2d.drawArc(x, y, width, height, 0, 360);
		graphics2d.setColor(foregroundColor);
		
		
		graphics2d.drawArc(x, y, width, height, 90, -(int) (360 * ((getValue() * 1.0) / (getMaximum() - getMinimum()))));
		graphics2d.setFont(new Font("黑体", Font.BOLD, fontSize));
		FontMetrics fontMetrics = graphics2d.getFontMetrics();
		int digitalWidth = fontMetrics.stringWidth(getValue() + "%");
		int digitalAscent = fontMetrics.getAscent();
		graphics2d.setColor(foregroundColor);
		double value = (double)Math.round(getPercentComplete()*100);
		graphics2d.drawString( value+ "%", getWidth() / 2 - digitalWidth *2/3, getHeight() / 2 + digitalAscent / 2);
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		this.foregroundColor = fg;
		this.repaint();
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		this.backgroundColor = bg;
		this.repaint();
	}
	
	@Override
	public void setValue(int n) {
		super.setValue(n);
		this.repaint();
	}
	
}
