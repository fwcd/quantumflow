package com.fwcd.quantumflow.circuitbuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.quantum.gates.QuantumGate;
import com.fwcd.quantum.gates.binary.CNOTGate;
import com.fwcd.quantum.gates.binary.SwapGate;
import com.fwcd.quantum.gates.ternary.ToffoliGate;
import com.fwcd.quantum.gates.ternary.FredkinGate;
import com.fwcd.quantum.gates.unary.HadamardGate;
import com.fwcd.quantum.gates.unary.PauliXGate;
import com.fwcd.quantum.gates.unary.PauliYGate;
import com.fwcd.quantum.gates.unary.PauliZGate;
import com.fwcd.quantum.gates.unary.SqrtNOTGate;
import com.fwcd.fructose.swing.Viewable;

public class CircuitToolBar implements Viewable {
	private final JPanel view;
	private JButton addQubitButton;
	private JButton clearButton;
	
	private final int padding = 10;
	private JToolBar toolBar;
	
	public CircuitToolBar(QuantumCircuitView builder) {
		view = new JPanel();
		view.setLayout(new BorderLayout());
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
		clearButton = new JButton("Clear");
		clearButton.addActionListener(l -> builder.reset());
		toolBar.add(clearButton);
		
		addQubitButton = new JButton("Add qubit");
		addQubitButton.setBackground(new Color(0x55ff55)); // Light green
		addQubitButton.addActionListener(l -> builder.addQubit());
		toolBar.add(addQubitButton);
		
		toolBar.addSeparator();
		
		JPanel unaryGates = gatePanel("Unary gates");
		unaryGates.add(gateButton(builder, new HadamardGate()));
		unaryGates.add(gateButton(builder, new PauliXGate()));
		unaryGates.add(gateButton(builder, new PauliYGate()));
		unaryGates.add(gateButton(builder, new PauliZGate()));
		unaryGates.add(gateButton(builder, new SqrtNOTGate()));
		toolBar.add(unaryGates);
		toolBar.addSeparator();

		JPanel binaryGates = gatePanel("Binary gates");
		binaryGates.add(gateButton(builder, new CNOTGate()));
		binaryGates.add(gateButton(builder, new SwapGate()));
		toolBar.add(binaryGates);
		toolBar.addSeparator();
		
		JPanel ternaryGates = gatePanel("Ternary gates");
		ternaryGates.add(gateButton(builder, new ToffoliGate()));
		ternaryGates.add(gateButton(builder, new FredkinGate()));
		toolBar.add(ternaryGates);
		
		view.add(toolBar, BorderLayout.CENTER);
		
		JButton computeButton = new JButton(" Compute result ");
		computeButton.addActionListener(l -> builder.calculateResult());
		computeButton.setFont(new Font("Default", Font.PLAIN, 24));
		view.add(computeButton, BorderLayout.EAST);
	}
	
	private JPanel gatePanel(String string) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel(string));
		panel.add(Box.createVerticalGlue());
		return panel;
	}

	private JButton gateButton(QuantumCircuitView builder, QuantumGate gate) {
		JButton button = new JButton(gate.getClass().getSimpleName());
		button.addActionListener(l -> builder.select(new QuantumGateView(gate, builder.getLineDistance())));
		return button;
	}
	
	@Override
	public JComponent getView() {
		return view;
	}
}
