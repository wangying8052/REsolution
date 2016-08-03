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

package com.Refactor.AdjustCoefficients;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.xml.sax.SAXException;

import com.Refactor.NonInheritance.MergeMethodNonInheri;
import com.Refactor.NonInheritance.NonInheritanceRefactoring;
import com.Refactor.NonInheritance.preprocessing;
import com.Refactor.NonInheritance.semantic;
import com.Refactor.NonInheritance.tool;
import com.jeantessier.dependencyfinder.gui.SrcAction;
import com.mathworks.toolbox.javabuilder.MWException;
import com.neu.utils.listener.ProgressBarLisenter;

public class MainAdjust {
	public static double persent = 0;

	/**
	 * 
	 * @param barLisenter
	 * @return 1 success;-1 There are too few classes for adjusting coefficients
	 * @throws SAXException
	 * @throws IOException
	 * @throws MWException
	 */
	public static int AdjustCoefficients(ProgressBarLisenter barLisenter)
			throws SAXException, IOException, MWException {
		if (preprocessing.DSC > 100) {
			Adjust.GetMetricsValue();
			ArrayList<MergeMethodNonInheri> rms = Adjust
					.selectAllClassToBeMerged();
			// semantic.findmethodlines();
			// semantic.extractword();
			barLisenter.setMaxValue(rms.size());

			Adjust.AdjustCoefficientsABCD(NonInheritanceRefactoring.a,
					NonInheritanceRefactoring.b, NonInheritanceRefactoring.c,
					NonInheritanceRefactoring.d, rms, barLisenter);
			return 1;
		} else {
			return -1;
		}
	}
}
