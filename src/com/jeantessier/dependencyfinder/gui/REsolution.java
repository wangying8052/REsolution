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


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.*;

import org.apache.log4j.*;

import com.Refactor.AdjustCoefficients.Adjust;
import com.Refactor.NonInheritance.CMN;
import com.Refactor.NonInheritance.NonInheritanceRefactoring;
import com.jeantessier.classreader.*;
import com.jeantessier.commandline.*;
import com.jeantessier.dependency.*;
import com.neu.utils.listener.ControlButton;
import com.neu.utils.service.ButtonStyle;

public class REsolution extends JFrame {
	
	public static int SCREEN_HEIGHT=0;
	public static int SCREEN_WIDTH=0;
	
    private boolean minimize;
    private boolean maximize;

    private boolean advancedMode;
    private JPanel  queryPanel   = new JPanel();
    
    private JMenuBar          menuBar                = new JMenuBar();
    private JMenu             fileMenu               = new JMenu();
    private JMenu             viewMenu               = new JMenu();
    private JMenu             helpMenu               = new JMenu();
    private JToolBar          toolbar                = new JToolBar();
    private JTextArea         dependenciesResultArea = new JTextArea();
    private JTextArea         closureResultArea      = new JTextArea();
    private JTextArea         metricsResultArea      = new JTextArea();
    private MetricsTableModel metricsChartModel      = new MetricsTableModel();
    private StatusLine        statusLine             = new StatusLine(420);
    private JProgressBar      progressBar            = new JProgressBar();

    private Collection<String>        inputFiles  = null;
    private ClassfileLoaderDispatcher dispatcher  = null;
    private NodeFactory               nodeFactory = null;
    private Monitor                   monitor     = null;

    private JCheckBox  packageScope          = new JCheckBox("packages");
    private JCheckBox  classScope            = new JCheckBox("classes");
    private JCheckBox  featureScope          = new JCheckBox("features");
    private JTextField scopeIncludes         = new JTextField();
    private JTextField packageScopeIncludes  = new JTextField();
    private JTextField classScopeIncludes    = new JTextField();
    private JTextField featureScopeIncludes  = new JTextField();
    private JTextField scopeExcludes         = new JTextField();
    private JTextField packageScopeExcludes  = new JTextField();
    private JTextField classScopeExcludes    = new JTextField();
    private JTextField featureScopeExcludes  = new JTextField();
    
    private JCheckBox  packageFilter         = new JCheckBox("packages");
    private JCheckBox  classFilter           = new JCheckBox("classes");
    private JCheckBox  featureFilter         = new JCheckBox("features");
    private JTextField filterIncludes        = new JTextField();
    private JTextField packageFilterIncludes = new JTextField();
    private JTextField classFilterIncludes   = new JTextField();
    private JTextField featureFilterIncludes = new JTextField();
    private JTextField filterExcludes        = new JTextField();
    private JTextField packageFilterExcludes = new JTextField();
    private JTextField classFilterExcludes   = new JTextField();
    private JTextField featureFilterExcludes = new JTextField();

    private JCheckBox  showInbounds          = new JCheckBox("<--");
    private JCheckBox  showOutbounds         = new JCheckBox("-->");
    private JCheckBox  showEmptyNodes        = new JCheckBox("empty elements");
    private JCheckBox  copyOnly              = new JCheckBox("copy only");
    
    private JTextField maximumInboundDepth   = new JTextField("0", 2);
    private JTextField maximumOutboundDepth  = new JTextField(2);

    private GraphCopier dependenciesQuery    = null;
    
    /**
     * @author revo begin
     */
    private JPanel optionPanel;
    private JPanel dependenciesResultPanel;
    private JTabbedPane resultRightPanel_JTabbledPane;
    private JPanel controlCheckjPanel;
    private JPanel treeCheckjPanel;
    JTabbedPane optionPane;
    private List<Action> actionList = new ArrayList<Action>();
    private List<JTextField> textList = new ArrayList<JTextField>();
    
    private List<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();
	private List<JButton> buttonList = new ArrayList<JButton>();
	private List<JCheckBox> treeCheckBoxList = new ArrayList<JCheckBox>();
	private List<JButton> treeButtonList = new ArrayList<JButton>();
    /**
     * @author revo end
     */
    
    public REsolution(CommandLine commandLine) {
    	
    	SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    	SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    	    	
        this.setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT-45));
        this.setTitle("REsolution");
        this.setIconImage(new ImageIcon(getClass().getResource("icons/logoicon.gif")).getImage());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowKiller());

        setNewDependencyGraph();
        
        packageScope.setToolTipText("Select packages");
        classScope.setToolTipText("Select classes (with their package)");
        featureScope.setToolTipText("Select methods and fields (with their class and package)");
        scopeIncludes.setToolTipText("Package, class, method, or field must match any these expressions. E.g., /^com.mycompany/, /\\.get\\w+\\(/");
        packageScopeIncludes.setToolTipText("Package must match any these expressions. E.g., /^com.mycompany/, /\\.get\\w+\\(/");
        classScopeIncludes.setToolTipText("Class must match any these expressions. E.g., /^com.mycompany/, /\\.get\\w+\\(/");
        featureScopeIncludes.setToolTipText("Method or field must match any these expressions. E.g., /^com.mycompany/, /\\.get\\w+\\(/");
        scopeExcludes.setToolTipText("Package, class, method, or field must NOT match any of these expressions. E.g., /Test/");
        packageScopeExcludes.setToolTipText("Package must NOT match any of these expressions. E.g., /Test/");
        classScopeExcludes.setToolTipText("Class must NOT match any of these expressions. E.g., /Test/");
        featureScopeExcludes.setToolTipText("Method or field must NOT match any of these expressions. E.g., /Test/");
        
        packageFilter.setToolTipText("Show dependencies to/from packages");
        classFilter.setToolTipText("Show dependencies to/from classes");
        featureFilter.setToolTipText("Show dependencies to/from methods and fields");
        filterIncludes.setToolTipText("Package, class, method, or field at the other end of the dependency must match any these expressions. E.g., /^com.mycompany/, /\\.get\\w+\\(/");
        packageFilterIncludes.setToolTipText("Package at the other end of the dependency must match any these expressions. E.g., /^com.mycompany/, /\\.get\\w+\\(/");
        classFilterIncludes.setToolTipText("Class at the other end of the dependency must match any these expressions. E.g., /^com.mycompany/, /\\.get\\w+\\(/");
        featureFilterIncludes.setToolTipText("Method or field at the other end of the dependency must match any these expressions. E.g., /^com.mycompany/, /\\.get\\w+\\(/");
        filterExcludes.setToolTipText("Package, class, method, or field at the other end of the dependency must NOT match any of these expressions. E.g., /Test/");
        packageFilterExcludes.setToolTipText("Package at the other end of the dependency must NOT match any of these expressions. E.g., /Test/");
        classFilterExcludes.setToolTipText("Class at the other end of the dependency must NOT match any of these expressions. E.g., /Test/");
        featureFilterExcludes.setToolTipText("Method or field at the other end of the dependency must NOT match any of these expressions. E.g., /Test/");
        
        showInbounds.setToolTipText("Show dependencies that point to the selected packages, classes, methods, or fields");
        showOutbounds.setToolTipText("Show dependencies that originate from the selected packages, classes, methods, or fields");
        showEmptyNodes.setToolTipText("Show selected packages, classes, methods, and fields even if they do not have dependencies");
        copyOnly.setToolTipText("<html>Only copy explicit dependencies to the result graph.<br>Do not introduce implicit dependencies<br>where explicit dependencies match the regular expressions<br>but are otherwise out of scope</html>");

        showInbounds.setFont(getCodeFont(Font.BOLD, 14));
        showOutbounds.setFont(getCodeFont(Font.BOLD, 14));
        
        maximumInboundDepth.setToolTipText("Maximum hops against the direction dependencies.  Empty field means no limit.");
        maximumOutboundDepth.setToolTipText("Maximum hops in the direction of dependencies.  Empty field means no limit.");

        setAdvancedMode(false);
        
        buildMenus(commandLine);
        buildUI();
        
        
        
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            Logger.getLogger(REsolution.class).error("Unable to set look and feel", ex);
        }
        
        statusLine.showInfo("Ready.");
        
    }

    private Font getCodeFont(int style, int size) {
        String fontName = "Monospaced";
        
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String fontName1 : fontNames) {
            if (fontName1.indexOf("Courier") != -1) {
                fontName = fontName1;
            }
        }

        return new Font(fontName, style, size);
    }

    private boolean isAdvancedMode() {
        return advancedMode;
    }

    void setAdvancedMode(boolean advancedMode) {
        this.advancedMode = advancedMode;

        copyOnly.setVisible(advancedMode);
    }
    
    public boolean getMaximize() {
        return maximize;
    }

    private void setMaximize(boolean maximize) {
        this.maximize = maximize;
    }

    public boolean getMinimize() {
        return minimize;
    }

    private void setMinimize(boolean minimize) {
        this.minimize = minimize;
    }
    
    public ClassfileLoaderDispatcher getClassfileLoaderDispatcher() {
        return dispatcher;
    }

    private void setClassfileLoaderDispatcher(ClassfileLoaderDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    private void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }
    
    public Collection<String> getInputFiles() {
        return inputFiles;
    }

    private void setInputFiles(Collection<String> inputFiles) {
        this.inputFiles = inputFiles;
    }
    
    public void addInputFile(File file) {
        inputFiles.add(file.toString());
    }

    public Collection<PackageNode> getPackages() {
        return getNodeFactory().getPackages().values();
    }

    public NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    private void setNodeFactory(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }
    
    StatusLine getStatusLine() {
        return statusLine;
    }
        
    JProgressBar getProgressBar() {
        return progressBar;
    }
    
    /**
     * @author revo begin
     */
    public void clearNodeFactory() {
    	 NodeFactory nodeFactory= getNodeFactory();
    	 nodeFactory.clearAll();
	}
  
	public JPanel getOptionPanel() {
		return optionPanel;
	}

	public JPanel getDependenciesResultPanel() {
		return dependenciesResultPanel;
	}
	public JTabbedPane getResultRightPanelJTabbledPane() {
		return resultRightPanel_JTabbledPane;
	}
	
	public JPanel getControlCheckjPanel() {
		return controlCheckjPanel;
	}
	
	public JPanel getTreeCheckjPanel() {
		return treeCheckjPanel;
	}

	public JTabbedPane getOptionPane() {
		return optionPane;
	}

	public List<Action> getActionList() {
		return actionList;
	}
	
	public void activeNext(Action action) {
		int index = this.getActionList().indexOf(action);
        this.getActionList().get(index+1).setEnabled(true);
	}

	public void activeLast() {
        this.getActionList().get(getActionList().size()-1).setEnabled(true);
	}
	public void activeLast(int count) {
		int strat = getActionList().size()-count;
		for (int i = 0; i < count; i++) {
			this.getActionList().get(strat+i).setEnabled(true);
		}
		
	}
	
	public void activeAt(int index) {
		this.getActionList().get(index).setEnabled(true);
	}
	
	public void inactiveAt(int index) {
		this.getActionList().get(index).setEnabled(false);
	}
	public Action getActionAt(int index) {
		return this.getActionList().get(index);
	}
	
	public void resetState() {
		this.resetActionState();
		this.resetTextState();
	}
	public void resetActionState() {
		for (int i = 0; i < this.getActionList().size(); i++) {
			this.getActionList().get(i).setEnabled(false);
		}
		this.getActionList().get(0).setEnabled(true);
	}
	public void resetTextState() {
		NonInheritanceRefactoring.setDefaultCoefficients();
		this.textList.get(0).setText(NonInheritanceRefactoring.a+"");
		this.textList.get(1).setText(NonInheritanceRefactoring.b+"");
		this.textList.get(2).setText(NonInheritanceRefactoring.c+"");
		this.textList.get(3).setText(NonInheritanceRefactoring.d+"");
	}
	
	public List<JCheckBox> getCheckBoxList() {
		return checkBoxList;
	}

	public void setCheckBoxList(List<JCheckBox> checkBoxList) {
		this.checkBoxList = checkBoxList;
	}

	public List<JButton> getButtonList() {
		return buttonList;
	}

	public void setButtonList(List<JButton> buttonList) {
		this.buttonList = buttonList;
	}
	
	public void batchCheckBoxs(boolean flag){
		System.out.println("batchCheckBoxs->"+flag);
		List<JCheckBox> checkBoxList = this.getCheckBoxList();
		if (checkBoxList!=null && checkBoxList.size()!=0) {
			for (JCheckBox jCheckBox : checkBoxList) {
				jCheckBox.setSelected(flag);
			}
		}
	}
	
	
	public List<JCheckBox> getTreeCheckBoxList() {
		return treeCheckBoxList;
	}

	public void setTreeCheckBoxList(List<JCheckBox> treeCheckBoxList) {
		this.treeCheckBoxList = treeCheckBoxList;
	}

	public List<JButton> getTreeButtonList() {
		return treeButtonList;
	}

	public void setTreeButtonList(List<JButton> treeButtonList) {
		this.treeButtonList = treeButtonList;
	}
	
	public void batchTreeCheckBoxs(boolean flag){
		if (treeCheckBoxList!=null && treeCheckBoxList.size()!=0) {
			for (JCheckBox jCheckBox : treeCheckBoxList) {
				jCheckBox.setSelected(flag);
			}
		}
	}

	/**
     * @author revo end
     */
	
	private void buildMenus(CommandLine commandLine) {
        buildFileMenu(commandLine);
        buildViewMenu();
        buildHelpMenu();

        this.setJMenuBar(menuBar);
    }
    
    private void buildFileMenu(CommandLine commandLine) {
        menuBar.add(fileMenu);

        fileMenu.setText("File");

        ButtonStyle buttonStyle = new ButtonStyle();
        
        Action    action;
        JMenuItem menuItem;
        JButton   button;
        
        //open jar file
        action = new DependencyExtractAction(this);
        menuItem = fileMenu.add(action);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
        menuItem.setMnemonic('d');
        button = toolbar.add(action);
        button.setToolTipText((String) action.getValue(Action.LONG_DESCRIPTION));
        buttonStyle.setButtonStyle(button);
        actionList.add(action);
        
        //open src file
        action = new SrcAction(this, commandLine.getSingleSwitch("encoding"), commandLine.getSingleSwitch("dtd-prefix"));
        menuItem = fileMenu.add(action);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        menuItem.setMnemonic('s');
        button = toolbar.add(action);
        button.setToolTipText((String) action.getValue(Action.LONG_DESCRIPTION));
        buttonStyle.setButtonStyle(button);
        action.setEnabled(false);
        actionList.add(action);
        
        //drawing the networks 
        action = new CMDNAction(this);
//        menuItem = fileMenu.add(action);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
//        menuItem.setMnemonic('c');
        button = toolbar.add(action);
        button.setToolTipText((String) action.getValue(Action.LONG_DESCRIPTION));
        buttonStyle.setButtonStyle(button);
        action.setEnabled(false);
        actionList.add(action);
        
        toolbar.addSeparator();
//        fileMenu.addSeparator();
        
        action = new RefactorAction(this);
//        menuItem = fileMenu.add(action);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK));
//        menuItem.setMnemonic('r');
        button = toolbar.add(action);
        button.setToolTipText((String) action.getValue(Action.LONG_DESCRIPTION));
        buttonStyle.setButtonStyle(button);
        action.setEnabled(false);
        actionList.add(action);
        
        action = new TreeAction(this);
//        menuItem = fileMenu.add(action);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
//        menuItem.setMnemonic('t');
        button = toolbar.add(action);
        button.setToolTipText((String) action.getValue(Action.LONG_DESCRIPTION));
        buttonStyle.setButtonStyle(button);
        action.setEnabled(false);
        actionList.add(action);
        
        action = new InheritRefactorAction(this);
//        menuItem = fileMenu.add(action);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.CTRL_MASK));
//        menuItem.setMnemonic('i');
        button = toolbar.add(action);
        button.setToolTipText((String) action.getValue(Action.LONG_DESCRIPTION));
        buttonStyle.setButtonStyle(button);
        action.setEnabled(false);
        actionList.add(action);
        
        toolbar.addSeparator();
        fileMenu.addSeparator();
        
        action = new MetricsAction(this);
//        menuItem = fileMenu.add(action);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Event.CTRL_MASK));
//        menuItem.setMnemonic('m');
        button = toolbar.add(action);
        button.setToolTipText((String) action.getValue(Action.LONG_DESCRIPTION));
        buttonStyle.setButtonStyle(button);
        action.setEnabled(false);
        actionList.add(action);
        
        action = new CoefficientAction(this);
//        menuItem = fileMenu.add(action);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
//        menuItem.setMnemonic('o');
        button = toolbar.add(action);
        button.setToolTipText((String) action.getValue(Action.LONG_DESCRIPTION));
        buttonStyle.setButtonStyle(button);
        action.setEnabled(false);
        actionList.add(action);
        
        action = new GoAction(this);
//        menuItem = fileMenu.add(action);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK));
//        menuItem.setMnemonic('g');
        button = toolbar.add(action);
        button.setToolTipText((String) action.getValue(Action.LONG_DESCRIPTION));
        buttonStyle.setButtonStyle(button);
        action.setEnabled(false);
        actionList.add(action);
        
        toolbar.addSeparator();
        fileMenu.addSeparator();
        
        action = new ClearAction(this);
//        menuItem = fileMenu.add(action);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK));
//        menuItem.setMnemonic('e');
        button = toolbar.add(action);
        button.setToolTipText((String) action.getValue(Action.LONG_DESCRIPTION));
        buttonStyle.setButtonStyle(button);
        
        toolbar.addSeparator();
        fileMenu.addSeparator();

        action = new ExitAction(this);
        menuItem = fileMenu.add(action);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
        menuItem.setMnemonic('x');
    }
    
    private void buildViewMenu() {
        menuBar.add(viewMenu);

        viewMenu.setText("View");

//        ButtonGroup group = new ButtonGroup();
        JMenuItem menuItem;
        
        menuItem = new JCheckBoxMenuItem(new SimpleQueryPanelAction(this));
        menuItem.setSelected(false);
       
//        group.add(menuItem);
        viewMenu.add(menuItem);
        
//        menuItem = new JRadioButtonMenuItem(new DefaultParameterPanelAction(this));
//        group.add(menuItem);
//        viewMenu.add(menuItem);
//        menuItem = new JRadioButtonMenuItem(new AdvancedQueryPanelAction(this));
//        group.add(menuItem);
//        viewMenu.add(menuItem);
    }

    private void buildHelpMenu() {
        menuBar.add(helpMenu);

        helpMenu.setText("Help");

        Action action;
        JMenuItem menuItem;

        action = new AboutAction(this);
        menuItem = helpMenu.add(action);
        menuItem.setMnemonic('a');
    }

    private void buildUI() {
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(buildControlPanel(), BorderLayout.NORTH);
        this.getContentPane().add(buildResultPanel(), BorderLayout.CENTER);
        this.getContentPane().add(buildStatusPanel(), BorderLayout.SOUTH);
    }

    private JComponent buildControlPanel() {
        JPanel result = new JPanel();

        result.setLayout(new BorderLayout());
        result.add(toolbar, BorderLayout.NORTH);
        result.add(buildQueryPanel(), BorderLayout.CENTER);
        
        return result;
    }
    
    JComponent buildQueryPanel() {
        if (isAdvancedMode()) {
            buildAdvancedQueryPanel();
        } else {
            buildSimpleQueryPanel();
        }
        
        return queryPanel;
    }
    
    static boolean flag = true;
    private void buildSimpleQueryPanel() {
    	
    	if (flag) {
    		queryPanel.removeAll();
            queryPanel.setLayout(new GridLayout(1, 1));
            queryPanel.add(buildSimpleScopePanel());
            //queryPanel.add(buildSimpleFilterPanel());
            queryPanel.revalidate();
            flag = false;
		} 
        /**
         * @author revo begin
         */
        queryPanel.setVisible(!queryPanel.isVisible());
        /**
         * @author revo end
         */
    }
    
    private void buildAdvancedQueryPanel() {
        queryPanel.removeAll();
        queryPanel.setLayout(new GridLayout(1, 2));
        queryPanel.add(buildAdvancedScopePanel());
        queryPanel.add(buildAdvancedFilterPanel());
        queryPanel.revalidate();
    }

    private JComponent buildSimpleScopePanel() {
        JPanel result = new JPanel();

        setBorderStyle(result,"Parameter Configuration");
        result.setLayout(new BorderLayout());

        result.add(buildSimpleScopePanelCheckboxes(), BorderLayout.NORTH);
       // result.add(buildSimpleScopePanelTextFields(), BorderLayout.SOUTH);

        return result;
    }

	private void setBorderStyle(JComponent result,String title) {
		result.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createLineBorder(new Color(10,111,40)), title));
	}
    
    /**
     * @category 暂时不用
     * @return
     */
    private JComponent buildSimpleScopePanelTextFields() {
        JPanel result = new JPanel();

        GridBagLayout      gbl = new GridBagLayout();
        GridBagConstraints c   = new GridBagConstraints();
        c.insets = new Insets(0, 2, 0, 2);
        
        result.setLayout(gbl);

        JLabel scopeIncludesLabel = new JLabel("including:");
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        result.add(scopeIncludesLabel);
        gbl.setConstraints(scopeIncludesLabel, c);

        JLabel scopeExcludesLabel = new JLabel("excluding:");
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        result.add(scopeExcludesLabel);
        gbl.setConstraints(scopeExcludesLabel, c);

        // -scope-includes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        result.add(scopeIncludes);
        gbl.setConstraints(scopeIncludes, c);
        
        // -scope-excludes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        result.add(scopeExcludes);
        gbl.setConstraints(scopeExcludes, c);

        return result;
    }


	public boolean validateParam() {
		
		boolean reault = true;
		
		String sawText = textList.get(0).getText();
		String miwText = textList.get(1).getText();
		String sewText = textList.get(2).getText();
		String sswText = textList.get(3).getText();
		
		Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
        Matcher sawTextIsNum = pattern.matcher(sawText);
        Matcher miwTextIsNum = pattern.matcher(miwText);
        Matcher sewTextIsNum = pattern.matcher(sewText);
        Matcher sswTextIsNum = pattern.matcher(sswText);
		if (sawText.isEmpty() || sawTextIsNum.matches() == false) {
			JOptionPane.showConfirmDialog(null, 
					"You should enter a SAW coefficient value", 
					"system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
			reault = false;
		}else if (miwText.isEmpty() || miwTextIsNum.matches() == false) {
			JOptionPane.showConfirmDialog(null, 
					"You should enter a MIW coefficient value", 
					"system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
			reault = false;
		}else if (sewText.isEmpty() || sewTextIsNum.matches() == false) {
			JOptionPane.showConfirmDialog(null, 
					"You should enter a FCW coefficient value", 
					"system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
			reault = false;
		}else if (sswText.isEmpty() || sswTextIsNum.matches() == false) {
			JOptionPane.showConfirmDialog(null, 
					"You should enter a SSW coefficient value", 
					"system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
			reault = false;
		}else{
			
			float saw = Float.parseFloat(sawText);
			float miw = Float.parseFloat(miwText);
			float sew = Float.parseFloat(sewText);
			float ssw = Float.parseFloat(sswText);
			float sum = saw + miw + sew + ssw;
			
			if (sum==1) {
				NonInheritanceRefactoring.ResetCoefficients(saw, miw, sew, ssw);
			} else {
				JOptionPane.showConfirmDialog(null, 
						"The sum of coefficients is not equal to 1.", 
						"system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
				reault = false;
			}
		}
		return reault;
	}

    private JComponent buildSimpleScopePanelCheckboxes() {
    	
        JPanel result = new JPanel();
        
        JLabel sawLabel = new JLabel("SAW");
        sawLabel.setToolTipText("Sharing Attribute Weight");
        JLabel miwLabel = new JLabel(" +　MIW");
        miwLabel.setToolTipText("Method Invocation Weight");
        JLabel fcwLabel = new JLabel(" + FCW");
        fcwLabel.setToolTipText("Functional Coupling Weight");
        JLabel sswLabel = new JLabel(" + SSW");
        sswLabel.setToolTipText("Semantic Similarity Weight");
        JLabel resLabel = new JLabel(" = 1");
        
        final JTextField sawTextField = new JTextField(4);
        sawTextField.setText(NonInheritanceRefactoring.a+"");
        final JTextField miwTextField = new JTextField(4);
        miwTextField.setText(NonInheritanceRefactoring.b+"");
        final JTextField fcwTextField = new JTextField(4);
        fcwTextField.setText(NonInheritanceRefactoring.c+"");
        final JTextField sswTextField = new JTextField(4);
        sswTextField.setText(NonInheritanceRefactoring.d+"");
        
        textList.add(sawTextField);
        textList.add(miwTextField);
        textList.add(fcwTextField);
        textList.add(sswTextField);
        
        
        JLabel theshold = new JLabel(" Theshold1:");
        theshold.setToolTipText("Threshold of the semantic similarity weights");
        
        final JRadioButton minJRadioButton = new JRadioButton("0.1");
        final JRadioButton midJRadioButton = new JRadioButton("0.2");
        final JRadioButton maxJRadioButton = new JRadioButton("mean");
        maxJRadioButton.setSelected(true);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(minJRadioButton);
        buttonGroup.add(midJRadioButton);
        buttonGroup.add(maxJRadioButton);
        
        JLabel theshold2 = new JLabel(" Theshold2:");
        theshold2.setToolTipText("Controlling the refactoring costs");
        
        final JRadioButton zeroButton = new JRadioButton("0");//value = 0
        final JRadioButton meanButton = new JRadioButton("mean");//value = 1
        final JRadioButton maxButton = new JRadioButton("max");//value = 2
        meanButton.setSelected(true);
        ButtonGroup buttonGroup2 = new ButtonGroup();
        buttonGroup2.add(zeroButton);
        buttonGroup2.add(meanButton);
        buttonGroup2.add(maxButton);
        
        final REsolution esolution = this;
        
        class ScopeControlListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				
				if(e.getActionCommand().equals("Default")){
					NonInheritanceRefactoring.setDefaultCoefficients();
					sawTextField.setText(NonInheritanceRefactoring.a+"");
					miwTextField.setText(NonInheritanceRefactoring.b+"");
					fcwTextField.setText(NonInheritanceRefactoring.c+"");
					sswTextField.setText(NonInheritanceRefactoring.d+"");
				}
				
				if(e.getActionCommand().equals("Reset")){
					sawTextField.setText("");
					miwTextField.setText("");
					fcwTextField.setText("");
					sswTextField.setText("");
				}
				if(e.getActionCommand().equals("OK")){
					String sawText = sawTextField.getText();
					String miwText = miwTextField.getText();
					String sewText = fcwTextField.getText();
					String sswText = sswTextField.getText();
					
					Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
		            Matcher sawTextIsNum = pattern.matcher(sawText);
		            Matcher miwTextIsNum = pattern.matcher(miwText);
		            Matcher sewTextIsNum = pattern.matcher(sewText);
		            Matcher sswTextIsNum = pattern.matcher(sswText);
					if (sawText.isEmpty() || sawTextIsNum.matches() == false) {
						JOptionPane.showConfirmDialog(null, 
								"You should enter a SAW coefficient value", 
								"system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
						return;
					}else if (miwText.isEmpty() || miwTextIsNum.matches() == false) {
						JOptionPane.showConfirmDialog(null, 
								"You should enter a MIW coefficient value", 
								"system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
						return;
					}else if (sewText.isEmpty() || sewTextIsNum.matches() == false) {
						JOptionPane.showConfirmDialog(null, 
								"You should enter a FCW coefficient value", 
								"system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
						return;
					}else if (sswText.isEmpty() || sswTextIsNum.matches() == false) {
						JOptionPane.showConfirmDialog(null, 
								"You should enter a SSW coefficient value", 
								"system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
						return;
					}else{
						
						float saw = Float.parseFloat(sawText);
						float miw = Float.parseFloat(miwText);
						float sew = Float.parseFloat(sewText);
						float ssw = Float.parseFloat(sswText);
						float sum = saw + miw + sew + ssw;
						
						if (sum==1) {
							NonInheritanceRefactoring.ResetCoefficients(saw, miw, sew, ssw);
							
							if (midJRadioButton.isSelected()) {
								Adjust.SetAdjustThreshold(0);
							}else if (minJRadioButton.isSelected()) {
								Adjust.SetAdjustThreshold(1);
							} else if(maxJRadioButton.isSelected()){
								Adjust.SetAdjustThreshold(2);
							}
							
							if (zeroButton.isSelected()) {
								CMN.Th2 = 0;
							}else if (meanButton.isSelected()) {
								CMN.Th2 = 1;
							}else if (maxButton.isSelected()) {
								CMN.Th2 = 2;
							}
							
//							esolution.getActionList().get(3).actionPerformed(null);
							
						} else {
							JOptionPane.showConfirmDialog(null, 
									"The sum of coefficients is not equal to 1.", 
									"system tips", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			}
    	}
        
        ScopeControlListener controlListener = new ScopeControlListener();
        
        
        JButton defaultButton = new JButton(new ImageIcon(getClass().getResource("icons/DefaultGreen.png")));
        ButtonStyle buttonStyle = new ButtonStyle();
        
        buttonStyle.setButtonStyle(defaultButton);
        
        defaultButton.addActionListener(controlListener);
        defaultButton.setActionCommand("Default");
        
        JButton OKButton = new JButton(new ImageIcon(getClass().getResource("icons/okGreen.png")));
        buttonStyle.setButtonStyle(OKButton);
        
        OKButton.addActionListener(controlListener);
        OKButton.setActionCommand("OK");
        
        JButton ResetButton = new JButton(new ImageIcon(getClass().getResource("icons/ResetGreen.png")));
        buttonStyle.setButtonStyle(ResetButton);
        
        ResetButton.addActionListener(controlListener);
        ResetButton.setActionCommand("Reset");
        
        
        
        result.add(sawLabel);
        result.add(sawTextField);
        result.add(miwLabel);
        result.add(miwTextField);
        result.add(fcwLabel);
        result.add(fcwTextField);
        result.add(sswLabel);
        result.add(sswTextField);
        result.add(resLabel);
        result.add(defaultButton);
        result.add(OKButton);
        result.add(ResetButton);
        result.add(theshold);
        result.add(minJRadioButton);
        result.add(midJRadioButton);
        result.add(maxJRadioButton);
        result.add(new JLabel(" "));
        result.add(theshold2);
        result.add(zeroButton);
        result.add(meanButton);
        result.add(maxButton);
        
        return result;
    }
    
    /**
     * @category 暂时不用
     * @return
     */
    private JComponent buildSimpleFilterPanel() {
        JPanel result = new JPanel();

        result.setBorder(BorderFactory.createTitledBorder("Show dependencies (stop for closure)"));

        result.setLayout(new BorderLayout());

        result.add(buildSimpleFilterPanelCheckboxes(), BorderLayout.NORTH);
        result.add(buildSimpleFilterPanelTextFields(), BorderLayout.SOUTH);

        return result;
    }
    
    /**
     * @category 暂时不用
     * @return
     */
    private JComponent buildSimpleFilterPanelTextFields() {
        JPanel result = new JPanel();

        GridBagLayout      gbl = new GridBagLayout();
        GridBagConstraints c   = new GridBagConstraints();
        c.insets = new Insets(0, 2, 0, 2);
        
        result.setLayout(gbl);

        JLabel filterIncludesLabel = new JLabel("including:");
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        result.add(filterIncludesLabel);
        gbl.setConstraints(filterIncludesLabel, c);

        JLabel filterExcludesLabel = new JLabel("excluding:");
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        result.add(filterExcludesLabel);
        gbl.setConstraints(filterExcludesLabel, c);

        // -filter-includes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        result.add(filterIncludes);
        gbl.setConstraints(filterIncludes, c);

        // -filter-excludes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        result.add(filterExcludes);
        gbl.setConstraints(filterExcludes, c);

        return result;
    }
    
    /**
     * @category 暂时不用
     * @return
     */
    private JComponent buildSimpleFilterPanelCheckboxes() {
        JPanel result = new JPanel();

        GridBagLayout      gbl = new GridBagLayout();
        GridBagConstraints c   = new GridBagConstraints();
        c.insets = new Insets(0, 2, 0, 2);
        
        result.setLayout(gbl);

        // -package-filter
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        result.add(packageFilter);
        gbl.setConstraints(packageFilter, c);

        // -class-filter
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        result.add(classFilter);
        gbl.setConstraints(classFilter, c);

        // -feature-filter
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        result.add(featureFilter);
        gbl.setConstraints(featureFilter, c);

        return result;
    }

    /**
     * @category 暂时不用
     * @return
     */
    private JComponent buildAdvancedScopePanel() {
        JPanel result = new JPanel();

        result.setBorder(BorderFactory.createTitledBorder("Select programming elements"));

        GridBagLayout      gbl = new GridBagLayout();
        GridBagConstraints c   = new GridBagConstraints();
        c.insets = new Insets(2, 0, 2, 5);
        
        result.setLayout(gbl);

        // -package-scope
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        result.add(packageScope);
        gbl.setConstraints(packageScope, c);

        // -class-scope
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0;
        c.weighty = 0;
        result.add(classScope);
        gbl.setConstraints(classScope, c);

        // -feature-scope
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0;
        c.weighty = 0;
        result.add(featureScope);
        gbl.setConstraints(featureScope, c);

        JLabel scopeIncludesLabel = new JLabel("including:");
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        result.add(scopeIncludesLabel);
        gbl.setConstraints(scopeIncludesLabel, c);

        // -scope-includes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        result.add(scopeIncludes);
        gbl.setConstraints(scopeIncludes, c);

        // -package-scope-includes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 0;
        result.add(packageScopeIncludes);
        gbl.setConstraints(packageScopeIncludes, c);

        // -class-scope-includes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        c.weighty = 0;
        result.add(classScopeIncludes);
        gbl.setConstraints(classScopeIncludes, c);

        // -feature-scope-includes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 1;
        c.weighty = 0;
        result.add(featureScopeIncludes);
        gbl.setConstraints(featureScopeIncludes, c);

        JLabel scopeExcludesLabel = new JLabel("excluding:");
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        result.add(scopeExcludesLabel);
        gbl.setConstraints(scopeExcludesLabel, c);

        // -scope-excludes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        result.add(scopeExcludes);
        gbl.setConstraints(scopeExcludes, c);

        // -package-scope-excludes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 0;
        result.add(packageScopeExcludes);
        gbl.setConstraints(packageScopeExcludes, c);

        // -class-scope-excludes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 3;
        c.weightx = 1;
        c.weighty = 0;
        result.add(classScopeExcludes);
        gbl.setConstraints(classScopeExcludes, c);

        // -feature-scope-excludes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 4;
        c.weightx = 1;
        c.weighty = 0;
        result.add(featureScopeExcludes);
        gbl.setConstraints(featureScopeExcludes, c);

        return result;
    }
    
    /**
     * @category 暂时不用
     * @return
     */
    private JComponent buildAdvancedFilterPanel() {
        JPanel result = new JPanel();

        result.setBorder(BorderFactory.createTitledBorder("Show dependencies (stop for closure)"));

        GridBagLayout      gbl = new GridBagLayout();
        GridBagConstraints c   = new GridBagConstraints();
        c.insets = new Insets(2, 0, 2, 5);
        
        result.setLayout(gbl);

        // -package-filter
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        result.add(packageFilter);
        gbl.setConstraints(packageFilter, c);

        // -class-filter
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0;
        c.weighty = 0;
        result.add(classFilter);
        gbl.setConstraints(classFilter, c);

        // -feature-filter
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0;
        c.weighty = 0;
        result.add(featureFilter);
        gbl.setConstraints(featureFilter, c);

        JLabel filterIncludesLabel = new JLabel("including:");
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        result.add(filterIncludesLabel);
        gbl.setConstraints(filterIncludesLabel, c);

        // -filter-includes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        result.add(filterIncludes);
        gbl.setConstraints(filterIncludes, c);

        // -package-filter-includes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 0;
        result.add(packageFilterIncludes);
        gbl.setConstraints(packageFilterIncludes, c);

        // -class-filter-includes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        c.weighty = 0;
        result.add(classFilterIncludes);
        gbl.setConstraints(classFilterIncludes, c);

        // -feature-filter-includes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 1;
        c.weighty = 0;
        result.add(featureFilterIncludes);
        gbl.setConstraints(featureFilterIncludes, c);

        JLabel filterExcludesLabel = new JLabel("excluding:");
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        result.add(filterExcludesLabel);
        gbl.setConstraints(filterExcludesLabel, c);

        // -filter-excludes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        result.add(filterExcludes);
        gbl.setConstraints(filterExcludes, c);

        // -package-filter-excludes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 0;
        result.add(packageFilterExcludes);
        gbl.setConstraints(packageFilterExcludes, c);

        // -class-filter-excludes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 3;
        c.weightx = 1;
        c.weighty = 0;
        result.add(classFilterExcludes);
        gbl.setConstraints(classFilterExcludes, c);

        // -feature-filter-excludes
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 4;
        c.weightx = 1;
        c.weighty = 0;
        result.add(featureFilterExcludes);
        gbl.setConstraints(featureFilterExcludes, c);

        return result;
    }
    
    private JComponent buildResultPanel() {
    	JPanel resultPanel = new JPanel(new GridLayout(1, 1));
    	
    	JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,buildOptionPanel(),buildDependenciesResultPanel());

    	jSplitPane.setOneTouchExpandable(true);
    	jSplitPane.setEnabled(false);
    	
    	resultPanel.add(jSplitPane);
    	
        return resultPanel;
    }

    
    
    /**
     * @author revo
     * @return
     */
    private JComponent buildOptionPanel() {
    	
    	optionPanel =new JPanel(new GridLayout(1,2));
    	
    	optionPane = new JTabbedPane();
    	optionPane.add("Class Comps",buildCheckBox());
    	optionPane.add("Inherit Tree",buildTreeCheckBox());
		optionPanel.add(optionPane);

		UIManager.put("TabbedPane.selected", new Color(10,111,40));
		UIManager.put("TabbedPane.unselectedBackground", Color.gray);
		UIManager.put("TabbedPane.foreground", Color.white);
		
//    	result.setBorder(BorderFactory.createTitledBorder("Results"));
//    	checkBoxOptionTab.addTab("Option1", buildCheckBox());
//    	checkBoxOptionTab.addTab("Option2", buildCheckBox());
//    	
//    	result.addTab("Dependencies", buildDependenciesPanel());
//    	result.addTab("Closure",      buildClosurePanel());
//    	result.addTab("Metrics",      buildMetricsPanel());
    	        
    	return optionPanel;
    }
    
    
	private Component buildTreeCheckBox() {
		// 构建选项卡面板
		ControlTreeListener treeListener = new ControlTreeListener(this);

		JRadioButton undoButton = new JRadioButton("Clear All");
		undoButton.addActionListener(treeListener);
		undoButton.setActionCommand("Undo");

		JRadioButton checkAllButton = new JRadioButton("Check All");
		checkAllButton.addActionListener(treeListener);
		checkAllButton.setActionCommand("CheckAll");
		checkAllButton.setSelected(true);

		ButtonGroup group = new ButtonGroup();
		group.add(undoButton);
		group.add(checkAllButton);

		JToolBar control = new JToolBar();
		control.setLayout(new GridLayout(1, 2));
		control.add(undoButton);
		control.add(checkAllButton);

		treeCheckjPanel = new JPanel(new BorderLayout());

		treeCheckjPanel.add(control, BorderLayout.NORTH);
		treeCheckjPanel.setBorder(BorderFactory.createLineBorder(new Color(10,111,40),2));


		return treeCheckjPanel;
	}

	private JComponent buildCheckBox() {
    	
    	//构建选项卡面板
		ControlButton controlButton = new ControlButton(this);
		
		JRadioButton undoButton = new JRadioButton("Clear All");
		undoButton.addActionListener(controlButton);
		undoButton.setActionCommand("Undo");
		
		
		JRadioButton checkAllButton = new JRadioButton("Check All");
		checkAllButton.addActionListener(controlButton);
		checkAllButton.setActionCommand("CheckAll");
		checkAllButton.setSelected(true);
		
		ButtonGroup group = new ButtonGroup();
		group.add(undoButton);
		group.add(checkAllButton);
		
		JToolBar control = new JToolBar();
		control.setLayout(new GridLayout(1,2));
		control.add(undoButton);
		control.add(checkAllButton);
		
	
		controlCheckjPanel = new JPanel(new BorderLayout());
	
		controlCheckjPanel.add(control,BorderLayout.NORTH);
		controlCheckjPanel.setBorder(BorderFactory.createLineBorder(new Color(10,111,40),2));
	
    	return controlCheckjPanel;
    }
    
    private JComponent buildDependenciesPanel() {
        JPanel result = new JPanel();

        result.setLayout(new BorderLayout());
        result.add(buildPrinterControlPanel(),     BorderLayout.NORTH);
        result.add(buildDependenciesResultPanel(), BorderLayout.CENTER);
        
        return result;
    }
    
    private JComponent buildPrinterControlPanel() {
        JPanel result = new JPanel();

        result.add(new JLabel("Show "));
        result.add(showInbounds);
        result.add(showOutbounds);
        result.add(showEmptyNodes);
        result.add(copyOnly);

        PrinterControlAction action = new PrinterControlAction(this);
        showInbounds.addActionListener(action);
        showOutbounds.addActionListener(action);
        showEmptyNodes.addActionListener(action);

        return result;
    }
    
    private JComponent buildDependenciesResultPanel() {
    	
    	dependenciesResultPanel =new JPanel(new GridLayout(1,1));
    	resultRightPanel_JTabbledPane = new JTabbedPane();
    	
//    	resultRightPanel_JTabbledPane.setBorder(BorderFactory.createLineBorder(Color.green));
//        JComponent result = new JScrollPane(dependenciesResultArea);
        
//        dependenciesResultArea.setEditable(false);
//        dependenciesResultArea.setFont(getCodeFont(Font.PLAIN, 12));
//        
//        dependenciesResultPanel.add(result);
        dependenciesResultPanel.add(resultRightPanel_JTabbledPane);
        
        return dependenciesResultPanel;
    }
    
    private JComponent buildClosurePanel() {
        JPanel result = new JPanel();
        
        result.setLayout(new BorderLayout());
        result.add(buildClosureControlPanel(), BorderLayout.NORTH);
        result.add(buildClosureResultPanel(),  BorderLayout.CENTER);
        
        return result;
    }
    
    private JComponent buildClosureControlPanel() {
        JPanel result = new JPanel();
        
        result.add(new JLabel("Follow inbounds: "));
        result.add(maximumInboundDepth);
        result.add(new JLabel("Follow outbounds: "));
        result.add(maximumOutboundDepth);
        
        return result;
    }

    private JComponent buildClosureResultPanel() {
        JComponent result = new JScrollPane(closureResultArea);
        
        closureResultArea.setEditable(false);
        closureResultArea.setFont(getCodeFont(Font.PLAIN, 12));
        
        return result;
    }
    
    private JComponent buildMetricsPanel() {
        return new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildMetricsResultPanel(), buildMetricsChartPanel());
    }

    private JComponent buildMetricsResultPanel() {
        JComponent result = new JScrollPane(metricsResultArea);

        metricsResultArea.setEditable(false);
        
        return result;
    }

    private JComponent buildMetricsChartPanel() {
        JComponent result;

        JTable table = new JTable(metricsChartModel);

        table.setCellSelectionEnabled(true);
        table.setColumnSelectionAllowed(true);
        
        result = new JScrollPane(table);

        return result;
    }
    
    private JComponent buildStatusPanel() {
        JPanel result = new JPanel();

        Dimension size = getProgressBar().getPreferredSize();
        size.width = 100;
        getProgressBar().setPreferredSize(size);
        getProgressBar().setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        getProgressBar().setForeground(new Color(10,111,40));
        
        result.setLayout(new BorderLayout());
        result.add(getStatusLine(),  BorderLayout.CENTER);
        result.add(getProgressBar(), BorderLayout.EAST);
        
        return result;
    }
    
    public void resetQuery() {
       
//    	packageScope.setSelected(true);
//    	classScope.setSelected(true);
//    	featureScope.setSelected(true);
    	
    	packageScope.setSelected(true);
        classScope.setSelected(false);
        featureScope.setSelected(false);
        scopeIncludes.setText("//");
        packageScopeIncludes.setText("");
        classScopeIncludes.setText("");
        featureScopeIncludes.setText("");
        scopeExcludes.setText("");
        packageScopeExcludes.setText("");
        classScopeExcludes.setText("");
        featureScopeExcludes.setText("");
    
        packageFilter.setSelected(true);
        classFilter.setSelected(false);
        featureFilter.setSelected(false);
        filterIncludes.setText("//");
        packageFilterIncludes.setText("");
        classFilterIncludes.setText("");
        featureFilterIncludes.setText("");
        filterExcludes.setText("");
        packageFilterExcludes.setText("");
        classFilterExcludes.setText("");
        featureFilterExcludes.setText("");

        showInbounds.setSelected(true);
        showOutbounds.setSelected(true);
        showEmptyNodes.setSelected(true);
        copyOnly.setSelected(false);
    }
    
    void clearDependencyResult() {
        dependenciesQuery = null;
        dependenciesResultArea.setText("");
    }
    
  /**
   * @category 暂时不用
   */
    void doDependencyQuery() {
    	
    
        RegularExpressionSelectionCriteria scopeCriteria = new RegularExpressionSelectionCriteria();
        
        scopeCriteria.setMatchingPackages(packageScope.isSelected());
        scopeCriteria.setMatchingClasses(classScope.isSelected());
        scopeCriteria.setMatchingFeatures(featureScope.isSelected());
        scopeCriteria.setGlobalIncludes(scopeIncludes.getText());
        scopeCriteria.setGlobalExcludes(scopeExcludes.getText());

        if (isAdvancedMode()) {
            scopeCriteria.setPackageIncludes(packageScopeIncludes.getText());
            scopeCriteria.setClassIncludes(classScopeIncludes.getText());
            scopeCriteria.setFeatureIncludes(featureScopeIncludes.getText());
            scopeCriteria.setPackageExcludes(packageScopeExcludes.getText());
            scopeCriteria.setClassExcludes(classScopeExcludes.getText());
            scopeCriteria.setFeatureExcludes(featureScopeExcludes.getText());
        }
    
        RegularExpressionSelectionCriteria filterCriteria = new RegularExpressionSelectionCriteria();
        
        filterCriteria.setMatchingPackages(packageFilter.isSelected());
        filterCriteria.setMatchingClasses(classFilter.isSelected());
        filterCriteria.setMatchingFeatures(featureFilter.isSelected());
        filterCriteria.setGlobalIncludes(filterIncludes.getText());
        filterCriteria.setGlobalExcludes(filterExcludes.getText());

        if (isAdvancedMode()) {
            filterCriteria.setPackageIncludes(packageFilterIncludes.getText());
            filterCriteria.setClassIncludes(classFilterIncludes.getText());
            filterCriteria.setFeatureIncludes(featureFilterIncludes.getText());
            filterCriteria.setPackageExcludes(packageFilterExcludes.getText());
            filterCriteria.setClassExcludes(classFilterExcludes.getText());
            filterCriteria.setFeatureExcludes(featureFilterExcludes.getText());
        }

        if ((isAdvancedMode() && copyOnly.isSelected()) || getMaximize()) {
            TraversalStrategy strategy = new SelectiveTraversalStrategy(scopeCriteria, filterCriteria);
            dependenciesQuery = new GraphCopier(strategy);
        } else {
            dependenciesQuery = new GraphSummarizer(scopeCriteria, filterCriteria);
        }
        
        dependenciesQuery.traverseNodes(getPackages());

        refreshDependenciesDisplay();
    }

    /**
     * @category 暂时不用
     */
    void refreshDependenciesDisplay() {
        if (dependenciesQuery != null) {
            StringWriter out = new StringWriter();
            com.jeantessier.dependency.TextPrinter printer = new com.jeantessier.dependency.TextPrinter(new PrintWriter(out));

            printer.setShowInbounds(showInbounds.isSelected());
            printer.setShowOutbounds(showOutbounds.isSelected());
            printer.setShowEmptyNodes(showEmptyNodes.isSelected());
            
            printer.traverseNodes(dependenciesQuery.getScopeFactory().getPackages().values());
            
            dependenciesResultArea.setText(out.toString());
        }
    }
    
    void clearClosureResult() {
        closureResultArea.setText("");
    }
    
    void doClosureQuery() {
        RegularExpressionSelectionCriteria startCriteria = new RegularExpressionSelectionCriteria();
        
        startCriteria.setGlobalIncludes(scopeIncludes.getText());
        startCriteria.setGlobalExcludes(scopeExcludes.getText());

        if (isAdvancedMode()) {
            startCriteria.setPackageIncludes(packageScopeIncludes.getText());
            startCriteria.setClassIncludes(classScopeIncludes.getText());
            startCriteria.setFeatureIncludes(featureScopeIncludes.getText());
            startCriteria.setPackageExcludes(packageScopeExcludes.getText());
            startCriteria.setClassExcludes(classScopeExcludes.getText());
            startCriteria.setFeatureExcludes(featureScopeExcludes.getText());
        }
    
        RegularExpressionSelectionCriteria stopCriteria = new RegularExpressionSelectionCriteria();
        
        stopCriteria.setGlobalIncludes(filterIncludes.getText());
        stopCriteria.setGlobalExcludes(filterExcludes.getText());

        if (isAdvancedMode()) {
            stopCriteria.setPackageIncludes(packageFilterIncludes.getText());
            stopCriteria.setClassIncludes(classFilterIncludes.getText());
            stopCriteria.setFeatureIncludes(featureFilterIncludes.getText());
            stopCriteria.setPackageExcludes(packageFilterExcludes.getText());
            stopCriteria.setClassExcludes(classFilterExcludes.getText());
            stopCriteria.setFeatureExcludes(featureFilterExcludes.getText());
        }

        TransitiveClosure selector = new TransitiveClosure(startCriteria, stopCriteria);

        try {
            selector.setMaximumInboundDepth(Long.parseLong(maximumInboundDepth.getText()));
        } catch (NumberFormatException ex) {
            selector.setMaximumInboundDepth(TransitiveClosure.UNBOUNDED_DEPTH);
        }

        try {
            selector.setMaximumOutboundDepth(Long.parseLong(maximumOutboundDepth.getText()));
        } catch (NumberFormatException ex) {
            selector.setMaximumOutboundDepth(TransitiveClosure.UNBOUNDED_DEPTH);
        }
        
        selector.traverseNodes(getPackages());

        RegularExpressionSelectionCriteria scopeCriteria = new RegularExpressionSelectionCriteria();
        
        scopeCriteria.setMatchingPackages(packageScope.isSelected());
        scopeCriteria.setMatchingClasses(classScope.isSelected());
        scopeCriteria.setMatchingFeatures(featureScope.isSelected());
        scopeCriteria.setGlobalIncludes("//");

        RegularExpressionSelectionCriteria filterCriteria = new RegularExpressionSelectionCriteria();
        
        filterCriteria.setMatchingPackages(packageFilter.isSelected());
        filterCriteria.setMatchingClasses(classFilter.isSelected());
        filterCriteria.setMatchingFeatures(featureFilter.isSelected());
        filterCriteria.setGlobalIncludes("//");

        GraphSummarizer summarizer = new GraphSummarizer(scopeCriteria, filterCriteria);
        summarizer.traverseNodes(selector.getFactory().getPackages().values());
        
        StringWriter out = new StringWriter();
        com.jeantessier.dependency.Printer printer = new com.jeantessier.dependency.TextPrinter(new PrintWriter(out));
        printer.traverseNodes(summarizer.getScopeFactory().getPackages().values());
        closureResultArea.setText(out.toString());
    }
    
    void clearMetricsResult() {
        metricsResultArea.setText("");
    }
    
    void doMetricsQuery() {
        RegularExpressionSelectionCriteria scopeCriteria = new RegularExpressionSelectionCriteria();
        
        scopeCriteria.setMatchingPackages(packageScope.isSelected());
        scopeCriteria.setMatchingClasses(classScope.isSelected());
        scopeCriteria.setMatchingFeatures(featureScope.isSelected());
        scopeCriteria.setGlobalIncludes(scopeIncludes.getText());
        scopeCriteria.setGlobalExcludes(scopeExcludes.getText());

        if (isAdvancedMode()) {
            scopeCriteria.setPackageIncludes(packageScopeIncludes.getText());
            scopeCriteria.setClassIncludes(classScopeIncludes.getText());
            scopeCriteria.setFeatureIncludes(featureScopeIncludes.getText());
            scopeCriteria.setPackageExcludes(packageScopeExcludes.getText());
            scopeCriteria.setClassExcludes(classScopeExcludes.getText());
            scopeCriteria.setFeatureExcludes(featureScopeExcludes.getText());
        }
    
        RegularExpressionSelectionCriteria filterCriteria = new RegularExpressionSelectionCriteria();
        
        filterCriteria.setMatchingPackages(packageFilter.isSelected());
        filterCriteria.setMatchingClasses(classFilter.isSelected());
        filterCriteria.setMatchingFeatures(featureFilter.isSelected());
        filterCriteria.setGlobalIncludes(filterIncludes.getText());
        filterCriteria.setGlobalExcludes(filterExcludes.getText());

        if (isAdvancedMode()) {
            filterCriteria.setPackageIncludes(packageFilterIncludes.getText());
            filterCriteria.setClassIncludes(classFilterIncludes.getText());
            filterCriteria.setFeatureIncludes(featureFilterIncludes.getText());
            filterCriteria.setPackageExcludes(packageFilterExcludes.getText());
            filterCriteria.setClassExcludes(classFilterExcludes.getText());
            filterCriteria.setFeatureExcludes(featureFilterExcludes.getText());
        }

        TraversalStrategy                          strategy = new SelectiveTraversalStrategy(scopeCriteria, filterCriteria);
        com.jeantessier.dependency.MetricsGatherer metrics  = new com.jeantessier.dependency.MetricsGatherer(strategy);
        
        metrics.traverseNodes(getPackages());

        StringWriter out = new StringWriter();
        MetricsReport report = new MetricsReport(new PrintWriter(out));
        report.process(metrics);
        
        metricsResultArea.setText(out.toString());
        metricsChartModel.setMetrics(metrics);
    }

    void setNewDependencyGraph() {
        setInputFiles(new LinkedList<String>());
        setClassfileLoaderDispatcher(new ModifiedOnlyDispatcher(ClassfileLoaderEventSource.DEFAULT_DISPATCHER));

        NodeFactory factory = new NodeFactory();
        setNodeFactory(factory);

        CodeDependencyCollector collector       = new CodeDependencyCollector(factory);
        DeletingVisitor         deletingVisitor = new DeletingVisitor(factory);
        setMonitor(new Monitor(collector, deletingVisitor));

        resetQuery();
    }

    public static void showError(CommandLineUsage clu, String msg) {
        System.err.println(msg);
        showError(clu);
    }

    public static void showError(CommandLineUsage clu) {
        System.err.println(clu);
    }

    public static void main(String[] args) {
        // Parsing the command line
        CommandLine commandLine = new CommandLine(new NullParameterStrategy());
        commandLine.addToggleSwitch("minimize");
        commandLine.addToggleSwitch("maximize");
        commandLine.addSingleValueSwitch("encoding", com.jeantessier.dependency.XMLPrinter.DEFAULT_ENCODING);
        commandLine.addSingleValueSwitch("dtd-prefix", com.jeantessier.dependency.XMLPrinter.DEFAULT_DTD_PREFIX);
        commandLine.addSingleValueSwitch("indent-text", com.jeantessier.dependency.XMLPrinter.DEFAULT_INDENT_TEXT);
        commandLine.addToggleSwitch("help");

        CommandLineUsage usage = new CommandLineUsage("REsolution");
        commandLine.accept(usage);

        try {
            commandLine.parse(args);
        } catch (IllegalArgumentException ex) {
            showError(usage, ex.toString());
            System.exit(1);
        }

        if (commandLine.getToggleSwitch("help")) {
            showError(usage);
            System.exit(1);
        }

        if (commandLine.getToggleSwitch("maximize") && commandLine.getToggleSwitch("minimize")) {
            showError(usage, "Only one of -maximize or -minimize allowed");
        }

        /*
         *  Beginning of main processing
         */

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Ignore
        }

        REsolution model = new REsolution(commandLine);
        model.setMaximize(commandLine.getToggleSwitch("maximize"));
        model.setMinimize(commandLine.getToggleSwitch("minimize"));
        model.setVisible(true);
    }

	public void buildDefaultPanel() {
		  queryPanel.setVisible(false);
		
	}

}
