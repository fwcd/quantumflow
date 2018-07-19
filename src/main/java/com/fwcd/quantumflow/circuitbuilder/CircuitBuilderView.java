package com.fwcd.quantumflow.circuitbuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.fwcd.fructose.swing.Viewable;

public class CircuitBuilderView implements Viewable {
	private final JPanel view;
	
	private final CircuitToolBar toolBar;
	private final VisualCircuit circuitBuilder;
	private final JTextPane output;
	
	public CircuitBuilderView() {
		view = new JPanel();
		view.setLayout(new BorderLayout());
		
		circuitBuilder = new VisualCircuit();
		circuitBuilder.addListener(this::onChange);
		view.add(circuitBuilder.getView(), BorderLayout.CENTER);
		
		toolBar = new CircuitToolBar(circuitBuilder);
		view.add(toolBar.getView(), BorderLayout.SOUTH);
		
		output = new JTextPane();
		output.setPreferredSize(new Dimension(180, 180));
		view.add(new JScrollPane(output), BorderLayout.EAST);
		
		onChange();
	}
	
	private void onChange() {
		output.setText(circuitBuilder.getModel().compute().toString());
		output.repaint();
	}

	@Override
	public JComponent getView() {
		return view;
	}
}
