/*
****************************************************
* REsolution is an automatic software refactoring tool      
****************************************************
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

import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import com.jeantessier.classreader.*;
import com.jeantessier.classreader.impl.*;
import com.jeantessier.dependency.*;

public class DependencyExtractAction extends AbstractAction implements Runnable {

	public static boolean flag = false;
	private REsolution model;

	public DependencyExtractAction(REsolution model) {
		this.model = model;
		putValue(Action.LONG_DESCRIPTION,
				"Extract dependencies from compiled source codes");
		putValue(Action.NAME, "Jar");
		putValue(Action.SMALL_ICON,
				new ImageIcon(getClass().getResource("icons/JarGreen.png")));
	}

	public void actionPerformed(ActionEvent e) {
		if (flag == false) {
			JFileChooser chooser;
			if (model.getInputFiles().isEmpty()) {
				chooser = new JFileChooser(new File("."));
			} else {
				chooser = new JFileChooser(model.getInputFiles().iterator().next());
			}
			chooser.addChoosableFileFilter(new JavaBytecodeFileFilter());
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setMultiSelectionEnabled(true);
			int returnValue = chooser.showDialog(model, "Extract");
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				for (File file : chooser.getSelectedFiles()) {
					model.addInputFile(file);
				}
				new Thread(this).start();
			}
		}else {
			JOptionPane.showConfirmDialog(null, 
					"You should clear the variables before refactoring a new software system.", 
					"system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
		}
		
		
	}

	public void run() {
		if (flag == false) {
			
			Date start = new Date();

			model.getStatusLine().showInfo("Scanning ...");
			ClassfileScanner scanner = new ClassfileScanner();
			scanner.load(model.getInputFiles());

			model.getProgressBar().setMaximum(scanner.getNbFiles());
			model.getMonitor().setClosedSession(false);
			ClassfileLoader loader = new TransientClassfileLoader(
					model.getClassfileLoaderDispatcher());
			loader.addLoadListener(new VerboseListener(model.getStatusLine(),
					model.getProgressBar()));
			loader.addLoadListener(model.getMonitor());
			loader.load(model.getInputFiles());

			if (model.getMaximize()) {
				model.getStatusLine().showInfo("Maximizing ...");
				new LinkMaximizer().traverseNodes(model.getPackages());
			} else if (model.getMinimize()) {
				model.getStatusLine().showInfo("Minimizing ...");
				new LinkMinimizer().traverseNodes(model.getPackages());
			}

			Date stop = new Date();
			model.getStatusLine().showInfo(
							"Done ("+ ((stop.getTime() - start.getTime()) / (double) 1000)
							+ " secs).");
			model.setTitle("REsolution - Extractor");

			model.activeNext(this);
			flag = true;
		}

	}

}
