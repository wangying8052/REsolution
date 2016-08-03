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

package com.neu.utils.component;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

import com.jeantessier.dependencyfinder.gui.CMDNAction;
import com.jeantessier.dependencyfinder.gui.REsolution;
import com.jeantessier.dependencyfinder.gui.InheritRefactorAction;
import com.jeantessier.dependencyfinder.gui.RefactorAction;
import com.neu.utils.data.TabName;

/**
 * 可关闭的JTabbedPane标签
 *
 * @author monitor Created on 2011-2-25, 23:52:41
 */
public class TabbedPanel extends JPanel {

	private static int count = -1;
	private REsolution dfmodel;
	final JLabel label;
	private final JTabbedPane pane;

	public TabbedPanel(final JTabbedPane pane, REsolution model) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.dfmodel = model;

		if (pane == null)
			throw new NullPointerException("TabbedPane is null");
		this.pane = pane;
		setOpaque(false);
		// tab标题
		label = new JLabel() {
			@Override
			public String getText() {

				int i = pane.indexOfTabComponent(TabbedPanel.this);
				if (i != -1)
					return pane.getTitleAt(i);
				return null;
			}

		};
		add(label);
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
		label.setForeground(Color.white);

		add(new TabButton());
		setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));

		// 实现部分标签页的拖拽
		pageDrag(pane, label, dfmodel);
		pageChange(pane, label);

	}

	private void pageChange(final JTabbedPane pane, final JLabel label) {
		label.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					int i = pane.indexOfTabComponent(TabbedPanel.this);
					pane.setSelectedIndex(i);
				}
			}
		});
	}

	private void pageDrag(final JTabbedPane pane, final JLabel label,
			final REsolution model) {
		label.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {}

			@Override
			public void mouseDragged(MouseEvent e) {
				int i = pane.indexOfTabComponent(TabbedPanel.this);
				int selectedIndex = pane.getSelectedIndex();
				if (i == selectedIndex) {
					label.addMouseListener(new MouseListener() {
						@Override
						public void mouseReleased(MouseEvent e) {
							String title = label.getText();
							if (TabName.RN.equals(title)) {
								Component comp = pane.getSelectedComponent();
								DragFrame frame = new DragFrame(e.getXOnScreen(), e.getYOnScreen(), pane, label.getText(),model);
								frame.addPanel(comp);
								TabbedPanel.count--;
								return;
							}
						}

						@Override
						public void mousePressed(MouseEvent e) {}
						@Override
						public void mouseExited(MouseEvent e) {}
						@Override
						public void mouseEntered(MouseEvent e) {}
						@Override
						public void mouseClicked(MouseEvent e) {}
					});
				}
			}
		});
	}

	/** tab上的关闭按钮 */
	private class TabButton extends JButton {
		public TabButton() {
			int size = 17;
			setPreferredSize(new Dimension(size, size));
			setToolTipText("关闭");
			setUI(new BasicButtonUI());
			setContentAreaFilled(false);
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			// 翻转效果
			setRolloverEnabled(true);
			// 鼠标事件，进入时画边框，移出时取消边框
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					Component component = e.getComponent();
					if (component instanceof AbstractButton) {
						AbstractButton button = (AbstractButton) component;
						button.setBorderPainted(true);
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					Component component = e.getComponent();
					if (component instanceof AbstractButton) {
						AbstractButton button = (AbstractButton) component;
						button.setBorderPainted(false);
					}
				}
			});
			// 单击关闭按钮事件
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					int i = pane.indexOfTabComponent(TabbedPanel.this);

					if (i != -1) {

						if (TabName.RN.equals(label.getText())
								&& CMDNAction.flag == true
								&& TabbedPanel.count >= 0) {
							CMDNAction.flag = false;
							TabbedPanel.count--;
							if (dfmodel.getCheckBoxList() != null) {
								dfmodel.getCheckBoxList().clear();
							}
							if (null != dfmodel.getButtonList()) {
								dfmodel.getButtonList().clear();
							}
							dfmodel.repaint();
							dfmodel.activeAt(2);
						} else if (TabName.RA.equals(label.getText())
								&& RefactorAction.flag == true
								&& TabbedPanel.count >= 0) {
							RefactorAction.flag = false;
							TabbedPanel.count--;
							dfmodel.activeAt(3);
						} else if (TabbedPanel.count >= 0) {
							TabbedPanel.count--;
						}
						pane.remove(i);
					}
				}
			});
		}

		@Override
		public void updateUI() {
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			// 鼠标按下时偏移一个坐标点
			if (getModel().isPressed()) {
				g2.translate(1, 1);
			}
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.BLACK);
			// 鼠标在按钮上时为红色
			if (getModel().isRollover()) {
				g2.setColor(Color.RED);
			}
			int delta = 6;
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight()
					- delta - 1);
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight()
					- delta - 1);
			g2.dispose();
		}
	}

	public static int getCount() {
		return ++count;
	}

	public static int Count() {
		return count;
	}

	public static void clear() {
		count = -1;
		CMDNAction.flag = false;

		System.out.println("clear->" + CMDNAction.flag);

	}

}

class DragFrame extends JFrame {

	REsolution model;
	JPanel panel = new JPanel(new BorderLayout());
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu();
	Component centerComponent;
	// String title;

	JTabbedPane pane;

	public DragFrame() {
	}

	public DragFrame(int x, int y, JTabbedPane pane, String title,
			REsolution model) {
		init(title);
		this.setLocation(x, y);
		this.setTitle(title);
		this.pane = pane;
		this.model = model;
	}

	public void init(final String title) {

		JMenuItem tingkaoButton = new JMenuItem("Dock");
		tingkaoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				pane.add(title, centerComponent);
				int index = TabbedPanel.getCount();
				pane.setTabComponentAt(index, new TabbedPanel(pane, model));
				pane.setSelectedIndex(index);

				disposeFrame();
			}
		});

		fileMenu.add(tingkaoButton);

		fileMenu.setText("Window");

		menuBar.add(fileMenu);

		this.setJMenuBar(menuBar);
		this.setIconImage(new ImageIcon(getClass().getResource(
				"icons/logoicon.gif")).getImage());
		this.add(panel, BorderLayout.CENTER);
		this.setTitle("RE-NewFrame");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setAlwaysOnTop(true);
		this.setVisible(true);

	}

	public void addPanel(Component comp) {
		centerComponent = comp;
		panel.add(centerComponent);

	}

	private void disposeFrame() {
		this.dispose();
	}

}
