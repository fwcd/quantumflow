package com.fwcd.quantumflow.core;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.fwcd.quantumflow.circuitbuilder.CircuitBuilderView;

public class QuantumFlowApp {
	private final JFrame view;
	
	public QuantumFlowApp(String title, int width, int height) {
		view = new JFrame(title);
		view.setSize(width, height);
		view.setLayout(new BorderLayout());
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.add(new CircuitBuilderView().getView(), BorderLayout.CENTER);
		view.setVisible(true);
	}
}
