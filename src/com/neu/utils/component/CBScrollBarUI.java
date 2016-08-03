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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class CBScrollBarUI extends BasicScrollBarUI {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根

	}
	
	private Color frameColor = new Color(10, 111, 40);
	public CBScrollBarUI() {
		this.frameColor = new Color(10, 111, 40);
	}
	public CBScrollBarUI(Color color) {
		this.frameColor = color;
	}
		
	
	
	@Override
	public Dimension getPreferredSize(JComponent c) {
		
		return new Dimension(16, 16);
	}
	
	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		super.paintThumb(g, c, thumbBounds);
		
		int tw = thumbBounds.width;
		int th = thumbBounds.height; 
		// 重定图形上下文的原点，这句一定要写，不然会出现拖动滑块时滑块不动的现象
		g.translate(thumbBounds.x, thumbBounds.y);
		Graphics2D g2 = (Graphics2D) g;
		GradientPaint gp = null;
		if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			gp = new GradientPaint(0, 0, frameColor, tw, 0,frameColor);
		}
		if (this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
			gp = new GradientPaint(0, 0, frameColor, 0, th,frameColor);
		}
		g2.setPaint(gp);
		g2.fillRoundRect(0, 0, tw - 1, th - 1, 5, 5);
		g2.setColor(frameColor);
		g2.drawRoundRect(0, 0, tw - 1, th - 1, 5, 5);
		
	}
	
	// 重绘滑块的滑动区域背景
	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		// TODO 自动生成的方法存根
//		super.paintTrack(g, c, trackBounds);

		Graphics2D g2 = (Graphics2D) g;
		GradientPaint gp = null;
		if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			gp = new GradientPaint(0, 0, new Color(238,238,238),
					trackBounds.width, 0, new Color(238,238,238));
		}
		if (this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
			gp = new GradientPaint(0, 0, new Color(238,238,238), 0,
					trackBounds.height, new Color(238,238,238));
		}
		g2.setPaint(gp);
		g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width,
				trackBounds.height);
		g2.setColor(Color.black);
//		g2.setColor(new Color(175, 155, 95));
		g2.drawRect(trackBounds.x, trackBounds.y, trackBounds.width - 1,
				trackBounds.height - 1);
		if (trackHighlight == BasicScrollBarUI.DECREASE_HIGHLIGHT)
			this.paintDecreaseHighlight(g);
		if (trackHighlight == BasicScrollBarUI.INCREASE_HIGHLIGHT)
			this.paintIncreaseHighlight(g);
	}
	
	// 重绘当鼠标点击滑动到向上或向左按钮之间的区域
	@Override
	protected void paintDecreaseHighlight(Graphics g) {
//		super.paintDecreaseHighlight(g);
		g.setColor(new Color(255, 149, 184));
		int x = this.getTrackBounds().x;
		int y = this.getTrackBounds().y;
		int w = 0, h = 0;
		if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			w = this.getThumbBounds().width;
			h = this.getThumbBounds().y - y;
		}
		if (this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
			w = this.getThumbBounds().x - x;
			h = this.getThumbBounds().height;
		}
		g.fillRect(x, y, w, h);
		
	}
	
	// 重绘当鼠标点击滑块至向下或向右按钮之间的区域
	@Override
	protected void paintIncreaseHighlight(Graphics g) {
//		super.paintIncreaseHighlight(g);
		Insets insets = scrollbar.getInsets();
		g.setColor(Color.gray);
		int x = this.getThumbBounds().x;
		int y = this.getThumbBounds().y;
		int w = this.getTrackBounds().width;
		int h = this.getTrackBounds().height;
		g.fillRect(x, y, w, h);

	}
	
	@Override
	protected JButton createDecreaseButton(int orientation) {
		// TODO 自动生成的方法存根
		return new BasicArrowButton(orientation);
	}
	
	@Override
	protected JButton createIncreaseButton(int orientation) {
		// TODO 自动生成的方法存根
		return new BasicArrowButton(orientation);
	}
	
	 
}
