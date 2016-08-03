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

package com.neu.utils.listener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JProgressBar;

public class ProgressBarLisenter {
    private List<JProgressBar> progressBars;

    public ProgressBarLisenter() {
    	progressBars = new ArrayList<JProgressBar>();
	}

    public boolean addBars(JProgressBar progressBar) {
		return progressBars.add(progressBar);
	}
    
    
    public void beginSession() {    	
		for (JProgressBar jProgressBar : progressBars) {
			jProgressBar.setValue(0);
			jProgressBar.setStringPainted(true);
		}
    }

    public void endSession() {
    	for (JProgressBar jProgressBar : progressBars) {
			jProgressBar.setStringPainted(false);
			jProgressBar.setValue(0);
		}
    }
    
    public void endFile() {
    	
    	for (JProgressBar jProgressBar : progressBars) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
		}
    	
//        getProgressBar().setValue(getProgressBar().getValue() + 1);
    }

	public void setMaxValue(int size) {
		
		for (JProgressBar jProgressBar : progressBars) {
			jProgressBar.setMaximum(size);
		}
	}

}
