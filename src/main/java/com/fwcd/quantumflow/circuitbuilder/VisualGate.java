package com.fwcd.quantumflow.circuitbuilder;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.fwcd.fructose.draw.DrawColor;
import com.fwcd.fructose.draw.DrawGraphics;
import com.fwcd.fructose.draw.VoidGraphics;
import com.fwcd.fructose.geometry.Circle2D;
import com.fwcd.fructose.geometry.LineSeg2D;
import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.quantum.gates.MatrixGate;
import com.fwcd.quantum.gates.QuantumGate;
import com.fwcd.quantum.gates.binary.CNOTGate;
import com.fwcd.quantum.gates.binary.SqrtSwapGate;
import com.fwcd.quantum.gates.binary.SwapGate;
import com.fwcd.quantum.gates.ternary.CCNOTGate;
import com.fwcd.quantum.gates.ternary.CSwapGate;
import com.fwcd.quantum.gates.unary.PauliXGate;
import com.fwcd.fructose.swing.Rendereable;
import com.fwcd.fructose.swing.SwingGraphics;

public class VisualGate implements Rendereable {
	private final QuantumGate gate;
	private final int lineDistance;
	private Rectangle2D hitBox;
	private Vector2D pos;
	
	/**
	 * Creates a new quantum gate renderer.
	 * 
	 * @param gate - The gate to be rendered
	 * @param pos - A position on the qubit flow line
	 * @param lineDistance - The y-distance between two qubit flow lines
	 */
	public VisualGate(QuantumGate gate, Vector2D pos, int lineDistance) {
		this.gate = gate;
		this.pos = pos;
		this.lineDistance = lineDistance;
		
		render(new VoidGraphics());
	}
	
	public VisualGate withPos(Vector2D pos) {
		return new VisualGate(gate, pos, lineDistance);
	}
	
	public void moveTo(Vector2D pos) {
		this.pos = pos;
	}
	
	public void moveBy(Vector2D delta) {
		pos = pos.add(delta);
	}

	private void straightCross(Vector2D pos, double radius, DrawGraphics g) {
		new LineSeg2D(pos.sub(0, radius), pos.add(0, radius)).draw(g);
		new LineSeg2D(pos.sub(radius, 0), pos.add(radius, 0)).draw(g);
	}

	private void rotatedCross(Vector2D pos, double radius, DrawGraphics g) {
		new LineSeg2D(pos.sub(radius, radius), pos.add(radius, radius)).draw(g);
		new LineSeg2D(pos.sub(radius, -radius), pos.add(radius, -radius)).draw(g);
	}
	
	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		render(new SwingGraphics(g2d));
	}

	private void render(DrawGraphics g) {
		hitBox = null;
		int width = 50;
		int height = lineDistance;
		
		if (gate instanceof MatrixGate) {
			int minQubits = ((MatrixGate) gate).minQubits();
			if (minQubits > 1) {
				height += (minQubits - 2) * lineDistance;
			}
		}
		
		// Yes, I know, having many instanceof's is generally considered "bad",
		// BUT it decouples the rendering stuff from the actual quantum
		// mechanics. Something like a visitor pattern might
		// be favorable, but would add a lot of unneeded verbosity.
		
		Vector2D center = pos.add(width / 2, height / 2);
		Vector2D topCenter = pos.add(width / 2, 0);
		Vector2D botCenter = pos.add(width / 2, height);
		int radius = 10;
		
		if (gate instanceof CNOTGate) {
			new Circle2D(topCenter, radius).fill(g);
			new LineSeg2D(topCenter, botCenter).draw(g);
			straightCross(botCenter, radius, g);
			new Circle2D(botCenter, radius).draw(g);
		} else if (gate instanceof SqrtSwapGate) {
			rotatedCross(topCenter, radius, g);
			new LineSeg2D(topCenter, botCenter).draw(g);
			rotatedCross(botCenter, radius, g);
			Circle2D c = new Circle2D(center, radius);
			c.fillWith(DrawColor.WHITE, g);
			c.draw(g);
			g.drawString("1/2", center.getX() - radius + 2, center.getY() + radius - 4);
		} else if (gate instanceof SwapGate) {
			rotatedCross(topCenter, radius, g);
			new LineSeg2D(topCenter, botCenter).draw(g);
			rotatedCross(botCenter, radius, g);
		} else if (gate instanceof CCNOTGate) {
			new Circle2D(topCenter, radius).fill(g);
			new Circle2D(center, radius).fill(g);
			new LineSeg2D(topCenter, botCenter).draw(g);
			straightCross(botCenter, radius, g);
			new Circle2D(botCenter, radius).draw(g);
		} else if (gate instanceof CSwapGate) {
			new Circle2D(topCenter, radius).fill(g);
			rotatedCross(center, radius, g);
			new LineSeg2D(topCenter, botCenter).draw(g);
			rotatedCross(botCenter, radius, g);
		} else if (gate instanceof PauliXGate) {
			straightCross(topCenter, radius, g);
			new Circle2D(topCenter, radius).draw(g);
			hitBox = new Rectangle2D(pos.sub(0, lineDistance / 2), width, height);
		} else {
			String s = gate.getSymbol();
			final int padding = 5;
			
			width = g.getStringWidth(s) + (padding * 2);
			height = g.getStringHeight() + (padding * 2);
			
			hitBox = new Rectangle2D(pos.sub(padding, height / 2), pos.add(width * 1.4, height / 2));
			hitBox.fillWith(DrawColor.WHITE, g);
			hitBox.draw(g);
			g.drawString(s, pos.getX() + padding, pos.getY() + padding, 18);
		}
		
		if (hitBox == null) {
			hitBox = new Rectangle2D(pos, width, height);
		}
	}

	public Dimension getSize() {
		return new Dimension((int) hitBox.width(), (int) hitBox.height());
	}
	
	public Dimension getSizePlus(double wDelta, double hDelta) {
		return new Dimension((int) (hitBox.width() + wDelta), (int) (hitBox.height() + hDelta));
	}

	public QuantumGate getGate() {
		return gate;
	}
}
