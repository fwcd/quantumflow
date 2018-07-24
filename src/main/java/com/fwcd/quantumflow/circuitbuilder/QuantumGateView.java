package com.fwcd.quantumflow.circuitbuilder;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.fwcd.fructose.NonNull;
import com.fwcd.fructose.draw.DrawGraphics;
import com.fwcd.fructose.draw.VoidGraphics;
import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.Rendereable;
import com.fwcd.fructose.swing.SwingGraphics;
import com.fwcd.quantum.gates.MatrixGate;
import com.fwcd.quantum.gates.QuantumGate;

public class QuantumGateView implements Rendereable {
	private final QuantumGate gate;
	private final int lineDistance;
	private Vector2D pos;
	private NonNull<Rectangle2D> boundingBox = NonNull.empty();
	
	/**
	 * Creates a new quantum gate renderer.
	 * 
	 * @param gate - The gate to be rendered
	 * @param pos - A position on the qubit flow line
	 * @param lineDistance - The y-distance between two qubit flow lines
	 */
	public QuantumGateView(QuantumGate gate, Vector2D pos, int lineDistance) {
		this.gate = gate;
		this.pos = pos;
		this.lineDistance = lineDistance;
		
		render(new VoidGraphics());
	}
	
	public QuantumGateView withPos(Vector2D pos) {
		return new QuantumGateView(gate, pos, lineDistance);
	}
	
	public void moveTo(Vector2D pos) {
		this.pos = pos;
	}
	
	public void moveBy(Vector2D delta) {
		pos = pos.add(delta);
	}
	
	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		render(new SwingGraphics(g2d));
	}

	private void render(DrawGraphics g) {
		int width = 50;
		int height = lineDistance;
		int radius = 10;
		
		int qubitCount = ((MatrixGate) gate).qubitCount();
		if (qubitCount > 1) {
			height += (qubitCount - 2) * lineDistance;
		}
		
		QuantumGateRenderer drawer = new QuantumGateRenderer(g, width, height, radius, lineDistance, pos);
		gate.accept(drawer);
		boundingBox = drawer.getBoundingBox();
	}

	public Dimension getSize() {
		return new Dimension((int) boundingBox.get().width(), (int) boundingBox.get().height());
	}
	
	public Dimension getSizePlus(double wDelta, double hDelta) {
		return new Dimension((int) (boundingBox.get().width() + wDelta), (int) (boundingBox.get().height() + hDelta));
	}

	public QuantumGate getGate() {
		return gate;
	}
}
