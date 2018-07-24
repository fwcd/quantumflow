package com.fwcd.quantumflow.circuitbuilder;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fwcd.fructose.NonNull;
import com.fwcd.fructose.draw.DrawGraphics;
import com.fwcd.fructose.draw.VoidGraphics;
import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.structs.IntList;
import com.fwcd.fructose.swing.Rendereable;
import com.fwcd.fructose.swing.SwingGraphics;
import com.fwcd.quantum.gates.QuantumGate;

public class QuantumGateView implements Rendereable {
	private final QuantumGate model;
	private final int lineDistance;
	private final int qubitCount;
	private final List<Vector2D> positions = new ArrayList<>();
	private final IntList indices = new IntList();
	
	private Optional<Vector2D> floatingPos = Optional.empty();
	private NonNull<Rectangle2D> boundingBox = NonNull.empty();
	
	/**
	 * Creates a new quantum gate view.
	 * 
	 * @param gate - The gate to be rendered
	 * @param pos - A position on the qubit flow line
	 * @param lineDistance - The y-distance between two qubit flow lines
	 */
	public QuantumGateView(QuantumGate gate, int lineDistance) {
		this.model = gate;
		this.lineDistance = lineDistance;
		
		qubitCount = model.qubitCount();
		render(new VoidGraphics());
	}
	
	public void addIndex(int index) {
		indices.add(index);
	}
	
	public int[] getIndices() {
		return indices.toArray();
	}
	
	public void placeNextComponentAt(Vector2D pos) {
		positions.add(pos);
		if (!isFloating()) {
			floatingPos = Optional.empty();
		}
	}
	
	public int floatingQubitIndex() {
		return positions.size();
	}
	
	public boolean isFloating() {
		return positions.size() < qubitCount;
	}
	
	public void setFloatingPos(Vector2D floatingPos) {
		this.floatingPos = Optional.of(floatingPos);
	}
	
	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		render(new SwingGraphics(g2d));
	}

	private void render(DrawGraphics g) {
		int width = 50;
		int height = lineDistance;
		int radius = 10;
		
		if (qubitCount > 1) {
			height += (qubitCount - 2) * lineDistance;
		}
		
		QuantumGateRenderer drawer = new QuantumGateRenderer(g, width, height, radius, lineDistance, positions, floatingPos);
		model.accept(drawer);
		boundingBox = drawer.getBoundingBox();
	}

	public Dimension getSize() {
		return new Dimension((int) boundingBox.get().width(), (int) boundingBox.get().height());
	}
	
	public Dimension getSizePlus(double wDelta, double hDelta) {
		return new Dimension((int) (boundingBox.get().width() + wDelta), (int) (boundingBox.get().height() + hDelta));
	}

	public QuantumGate getModel() {
		return model;
	}
}
