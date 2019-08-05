package fwcd.quantumflow.circuitbuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import fwcd.fructose.swing.Viewable;

public class CircuitBuilderView implements Viewable {
	private final JPanel component;
	
	private final CircuitToolBar toolBar;
	private final QuantumCircuitView circuit;
	private final JTextPane output;
	
	public CircuitBuilderView() {
		component = new JPanel();
		component.setLayout(new BorderLayout());
		
		circuit = new QuantumCircuitView();
		circuit.addListener(this::onChange);
		component.add(circuit.getComponent(), BorderLayout.CENTER);
		
		toolBar = new CircuitToolBar(circuit);
		component.add(toolBar.getComponent(), BorderLayout.SOUTH);
		
		output = new JTextPane();
		output.setPreferredSize(new Dimension(180, 180));
		component.add(new JScrollPane(output), BorderLayout.EAST);
		
		onChange();
	}
	
	private void onChange() {
		output.setText(circuit.getModel().compute().toString());
		output.repaint();
	}

	@Override
	public JComponent getComponent() {
		return component;
	}
}
