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

package com.neu.utils.data;

import java.util.ArrayList;

import com.Refactor.NonInheritance.Suggestions;
import com.neu.utils.service.Observer;
import com.neu.utils.service.Subject;

public class Data implements Subject{
	
	private ArrayList<Observer> observers;

	private Suggestions sg;

	public Data() {
		observers = new ArrayList<Observer>();
	}
	public Data(int x, int y) {
		observers = new ArrayList<Observer>();
	}
	
	public Data(Suggestions sg) {
		observers = new ArrayList<Observer>();
		this.sg = sg;
	}
	public Suggestions getSg() {
		return sg;
	}
	public void setSg(Suggestions sg) {
		this.sg = sg;
	}
	
	public void registerObserver(Observer o) {
		
		observers.add(o);
	}
	public void removeObserver(Observer o) {
		int index = observers.indexOf(o);
		
		if (index>=0) {
			observers.remove(index);
		} 
		
	}
	public void notifyObservers() {
		
		for (Observer observer : observers) {
			observer.update(sg);
		}
	}
	
	public void measurementsChanged(){
		notifyObservers();
	}
	
	public void setMeasurements(Suggestions sg){
		this.sg = sg;
		measurementsChanged();
	}
	
}