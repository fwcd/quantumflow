package com.fwcd.quantumflow.circuitbuilder;

import static com.fwcd.quantumflow.utils.ListUtils.listGet;

import java.util.List;
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
	private final int padding = 5;
	private final DrawGraphics g;
	private final int radius;
	private final int lineDistance;
	private final List<Vector2D> positions;
	private final Vector2D firstPos;
	private final Vector2D secondPos;
	private final Vector2D thirdPos;
	private Optional<Rectangle2D> boundingBox = Optional.empty();
	private int width;
	private int height;
	
	public QuantumGateRenderer(
		DrawGraphics g,
		int width,
		int height,
		int radius,
		int lineDistance,
		List<Vector2D> positions,
		Optional<Vector2D> floatingPos
	) {
		this.g = g;
		this.width = width;
		this.height = height;
		this.radius = radius;
		this.lineDistance = lineDistance;
		this.positions = positions;
		
		firstPos = listGet(positions, 0).orElseGet(() -> floatingPos.orElse(Vector2D.ZERO));
		secondPos = listGet(positions, 1).orElseGet(() -> floatingPos.orElse(Vector2D.ZERO));
		thirdPos = listGet(positions, 2).orElseGet(() -> floatingPos.orElse(Vector2D.ZERO));
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
		new Circle2D(firstPos, radius).fill(g);
		new LineSeg2D(firstPos, secondPos).draw(g);
		drawStraightCross(secondPos);
		new Circle2D(secondPos, radius).draw(g);
	}
	
	@Override
	public void visitSwap(SwapGate gate) {
		drawRotatedCross(firstPos);
		new LineSeg2D(firstPos, secondPos).draw(g);
		drawRotatedCross(secondPos);
	}
	
	@Override
	public void visitToffoli(ToffoliGate gate) {
		new Circle2D(firstPos, radius).fill(g);
		new Circle2D(secondPos, radius).fill(g);
		new LineSeg2D(firstPos, secondPos).draw(g);
		new LineSeg2D(secondPos, thirdPos).draw(g);
		drawStraightCross(thirdPos);
		new Circle2D(thirdPos, radius).draw(g);
	}
	
	@Override
	public void visitFredkin(FredkinGate gate) {
		new Circle2D(firstPos, radius).fill(g);
		drawRotatedCross(secondPos);
		new LineSeg2D(firstPos, secondPos).draw(g);
		new LineSeg2D(secondPos, thirdPos).draw(g);
		drawRotatedCross(thirdPos);
	}
	
	@Override
	public void visitPauliX(PauliXGate gate) {
		drawStraightCross(firstPos);
		new Circle2D(firstPos, radius).draw(g);
		boundingBox = Optional.of(new Rectangle2D(firstPos.sub(0, lineDistance / 2), width, height));
	}
	
	@Override
	public void visitGate(QuantumGate gate) {
		String s = gate.getSymbol();
		
		width = g.getStringWidth(s) + (padding * 2);
		height = g.getStringHeight() + (padding * 2);
		
		Rectangle2D bb = new Rectangle2D(firstPos.sub(padding, height / 2), firstPos.add(width * 1.4, height / 2));
		bb.fillWith(DrawColor.WHITE, g);
		bb.draw(g);
		boundingBox = Optional.of(bb.resizedBy(new Vector2D(padding * 2, padding)));
		g.drawString(s, firstPos.getX() + padding, firstPos.getY() + padding, 18);
	}
	
	public NonNull<Rectangle2D> getBoundingBox() {
		return NonNull.of(boundingBox.orElseGet(() -> new Rectangle2D(firstPos, width + padding, height + padding)));
	}
}
