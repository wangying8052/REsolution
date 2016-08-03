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

package com.neu.utils.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.Refactor.NonInheritance.MergeMethodNonInheri;
import com.neu.utils.data.Edge;
import com.neu.utils.data.Node;

public class NetworkToXMLUtil {
	
	/**
	 * write graph data to xml file
	 * @param nodelist
	 * @param edgelist
	 * @param type
	 */
	public static void xmlWrite(List<Node> nodelist,List<Edge> edgelist,String type) {
		String strContent="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<!--  An excerpt of an egocentric social network  -->\r\n"
				+ "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">\r\n"
				+ "<graph edgedefault=\"undirected\">\r\n";
		
		strContent+="<!-- data schema -->\r\n"
				+ "<key id=\"name\" for=\"node\" attr.name=\"name\" attr.type=\"string\"/>\r\n"
				+ "<key id=\"type\" for=\"node\" attr.name=\"type\" attr.type=\"string\"/>\r\n";
		
		/*<node id="127">
		  	<data key="name">Xz</data>
		 	<data key="gender">M</data>
		 </node>
		*/
		for (int i = 0; i < nodelist.size(); i++) {
			Node node = nodelist.get(i);
			strContent+="<node id=\""+node.getId()+"\"><data key=\"name\">"+node.getText()+"</data><data key=\"type\">"+type+"</data></node>\r\n";
			
		}
		
		//<edge source="5" target="8"></edge>
		for (int i = 0; i < edgelist.size(); i++) {
			Edge edge = edgelist.get(i);
			strContent+="<edge source=\""+edge.getSourceId()+"\" target=\""+edge.getTargetId()+"\"></edge>\r\n";
			
		}
		
		strContent+="</graph></graphml>";
		
		String fileName = type+"_net.xml";
		
		FileUtils.fileWrite(fileName, strContent);
		
		
	}

	public static void unicomFilmXmlWrite(List<List<String>> cc) {
		
		String strContent="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
		strContent+="<network>\r\n";
		for (List<String> arrayList : cc) {
			strContent+="<unicom>\r\n";
			for (String className : arrayList) {
				strContent+="<methodName>"+className+"</methodName>\r\n";
			}
			strContent+="</unicom>\r\n";
		}
		strContent+="</network>";
		
		String fileName = "method_list.xml";
		
		FileUtils.fileWrite(fileName, strContent);
	}
	
	public static List<List<String>> unicomFilmXmlRead(String fileName) throws DocumentException{
		
		List<List<String>> cc = new ArrayList<List<String>>();
		
		File inputXml = new File(fileName);
		SAXReader saxReader = new SAXReader();
	
		Document document = saxReader.read(inputXml);
		
		Element employees = document.getRootElement();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			List<String> list = new ArrayList<String>();
			for (Iterator j = employee.elementIterator(); j.hasNext();) {
				Element node = (Element) j.next();
				list.add(node.getText());
			}
			cc.add(list);
		}		
		return cc;
	}
	
}
