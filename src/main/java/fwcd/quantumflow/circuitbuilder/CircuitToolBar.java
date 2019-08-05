package fwcd.quantumflow.circuitbuilder;

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

import fwcd.fructose.swing.Viewable;
import fwcd.quantum.gates.QuantumGate;
import fwcd.quantum.gates.binary.CNOTGate;
import fwcd.quantum.gates.binary.SwapGate;
import fwcd.quantum.gates.ternary.FredkinGate;
import fwcd.quantum.gates.ternary.ToffoliGate;
import fwcd.quantum.gates.unary.HadamardGate;
import fwcd.quantum.gates.unary.PauliXGate;
import fwcd.quantum.gates.unary.PauliYGate;
import fwcd.quantum.gates.unary.PauliZGate;
import fwcd.quantum.gates.unary.SqrtNOTGate;

public class CircuitToolBar implements Viewable {
	private final JPanel component;
	private JButton addQubitButton;
	private JButton clearButton;

	private final int padding = 10;
	private JToolBar toolBar;

	public CircuitToolBar(QuantumCircuitView builder) {
		component = new JPanel();
		component.setLayout(new BorderLayout());
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
		
		component.add(toolBar, BorderLayout.CENTER);
		
		JButton computeButton = new JButton(" Compute result ");
		computeButton.addActionListener(l -> builder.calculateResult());
		computeButton.setFont(new Font("Default", Font.PLAIN, 24));
		component.add(computeButton, BorderLayout.EAST);
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
	public JComponent getComponent() {
		return component;
	}
}
