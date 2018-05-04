package com.fwcd.quantumflow.core;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.fwcd.quantumflow.circuitbuilder.CircuitBuilderTab;
import com.fwcd.quantumflow.qschemeeditor.QSchemeEditorTab;

public class QuantumFlowApp {
	private final JFrame view;
	private final JTabbedPane tabPane;

	private final QSchemeEditorTab scriptEditor;
	private final CircuitBuilderTab circuitBuilder;
	
	public QuantumFlowApp(String title, int width, int height) {
		view = new JFrame(title);
		view.setSize(width, height);
		view.setLayout(new BorderLayout());
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tabPane = new JTabbedPane();
		view.add(tabPane, BorderLayout.CENTER);
		
		scriptEditor = new QSchemeEditorTab();
		tabPane.addTab("QuantumScheme", scriptEditor.getView());
		
		circuitBuilder = new CircuitBuilderTab();
		tabPane.add("CircuitBuilder", circuitBuilder.getView());
		
		view.setVisible(true);
	}
}
