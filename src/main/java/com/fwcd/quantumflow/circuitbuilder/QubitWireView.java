package com.fwcd.quantumflow.circuitbuilder;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.fwcd.fructose.geometry.LineSeg2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.Rendereable;
import com.fwcd.fructose.swing.SwingGraphics;

public class QubitWireView implements Rendereable {
	private final BitView inBit;
	private final BitView outBit;
	private Vector2D startPos;
	private Vector2D endPos;
	
	private final List<QuantumGateView> gates = new ArrayList<>();
	
	public QubitWireView(Vector2D startPos, Vector2D endPos) {
		this.startPos = startPos;
		this.endPos = endPos;
		inBit = new BitView(/* editable */ true, startPos);
		outBit = new BitView(/* editable */ false, endPos);
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		SwingGraphics g = new SwingGraphics(g2d);
		
		new LineSeg2D(startPos, endPos).draw(g);
		inBit.render(g2d, canvasSize);
		outBit.render(g2d, canvasSize);
		
		for (QuantumGateView gate : gates) {
			gate.render(g2d, canvasSize);
		}
	}

	public boolean onMouseClick(Vector2D pos) {
		if (inBit.onMouseClick(pos)) return true;
		if (outBit.onMouseClick(pos)) return true;
		return false;
	}
	
	public Vector2D getEnd() {
		return endPos;
	}

	public void setOutput(boolean value) {
		outBit.set(value);
	}
	
	public void setStart(Vector2D pos) {
		startPos = pos;
		inBit.moveTo(pos);
	}
	
	public void setEnd(Vector2D pos) {
		endPos = pos;
		outBit.moveTo(endPos);
	}

	public void addGate(QuantumGateView gate, Vector2D offset) {
		gate.placeNextComponentAt(startPos.add(offset));
		if (!gate.isFloating()) {
			gates.add(gate);
		}
	}

	public boolean getInput() {
		return inBit.get();
	}
	
	public boolean getOutput() {
		return outBit.get();
	}
	
	public void hideOutput() {
		outBit.hide();
	}
}
