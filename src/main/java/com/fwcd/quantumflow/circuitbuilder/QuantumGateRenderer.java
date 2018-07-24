package com.fwcd.quantumflow.circuitbuilder;

import java.util.Optional;

import com.fwcd.fructose.NonNull;
import com.fwcd.fructose.draw.DrawColor;
import com.fwcd.fructose.draw.DrawGraphics;
import com.fwcd.fructose.geometry.Circle2D;
import com.fwcd.fructose.geometry.LineSeg2D;
import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.quantum.gates.QuantumGate;
import com.fwcd.quantum.gates.QuantumGateVisitor;
import com.fwcd.quantum.gates.binary.CNOTGate;
import com.fwcd.quantum.gates.binary.SwapGate;
import com.fwcd.quantum.gates.ternary.ToffoliGate;
import com.fwcd.quantum.gates.ternary.FredkinGate;
import com.fwcd.quantum.gates.unary.PauliXGate;

public class QuantumGateRenderer implements QuantumGateVisitor {
	private final DrawGraphics g;
	private final int radius;
	private final int lineDistance;
	private final Vector2D pos;
	private final Vector2D center;
	private final Vector2D topCenter;
	private final Vector2D bottomCenter;
	private Optional<Rectangle2D> boundingBox = Optional.empty();
	private int width;
	private int height;
	
	public QuantumGateRenderer(
		DrawGraphics g,
		int width,
		int height,
		int radius,
		int lineDistance,
		Vector2D pos
	) {
		this.g = g;
		this.width = width;
		this.height = height;
		this.radius = radius;
		this.lineDistance = lineDistance;
		this.pos = pos;
		
		center = pos.add(width / 2, height / 2);
		topCenter = pos.add(width / 2, 0);
		bottomCenter = pos.add(width / 2, height);
	}

	private void drawStraightCross(Vector2D center) {
		new LineSeg2D(center.sub(0, radius), center.add(0, radius)).draw(g);
		new LineSeg2D(center.sub(radius, 0), center.add(radius, 0)).draw(g);
	}

	private void drawRotatedCross(Vector2D center) {
		new LineSeg2D(center.sub(radius, radius), center.add(radius, radius)).draw(g);
		new LineSeg2D(center.sub(radius, -radius), center.add(radius, -radius)).draw(g);
	}
	
	@Override
	public void visitCNOT(CNOTGate gate) {
		new Circle2D(topCenter, radius).fill(g);
		new LineSeg2D(topCenter, bottomCenter).draw(g);
		drawStraightCross(bottomCenter);
		new Circle2D(bottomCenter, radius).draw(g);
	}
	
	@Override
	public void visitSwap(SwapGate gate) {
		drawRotatedCross(topCenter);
		new LineSeg2D(topCenter, bottomCenter).draw(g);
		drawRotatedCross(bottomCenter);
	}
	
	@Override
	public void visitToffoli(ToffoliGate gate) {
		new Circle2D(topCenter, radius).fill(g);
		new Circle2D(center, radius).fill(g);
		new LineSeg2D(topCenter, bottomCenter).draw(g);
		drawStraightCross(bottomCenter);
		new Circle2D(bottomCenter, radius).draw(g);
	}
	
	@Override
	public void visitFredkin(FredkinGate gate) {
		new Circle2D(topCenter, radius).fill(g);
		drawRotatedCross(center);
		new LineSeg2D(topCenter, bottomCenter).draw(g);
		drawRotatedCross(bottomCenter);
	}
	
	@Override
	public void visitPauliX(PauliXGate gate) {
		drawStraightCross(topCenter);
		new Circle2D(topCenter, radius).draw(g);
		boundingBox = Optional.of(new Rectangle2D(pos.sub(0, lineDistance / 2), width, height));
	}
	
	@Override
	public void visitGate(QuantumGate gate) {
		String s = gate.getSymbol();
		final int padding = 5;
		
		width = g.getStringWidth(s) + (padding * 2);
		height = g.getStringHeight() + (padding * 2);
		
		Rectangle2D bb = new Rectangle2D(pos.sub(padding, height / 2), pos.add(width * 1.4, height / 2));
		bb.fillWith(DrawColor.WHITE, g);
		bb.draw(g);
		boundingBox = Optional.of(bb);
		g.drawString(s, pos.getX() + padding, pos.getY() + padding, 18);
	}
	
	public NonNull<Rectangle2D> getBoundingBox() {
		return NonNull.of(boundingBox.orElseGet(() -> new Rectangle2D(pos, width, height)));
	}
}
