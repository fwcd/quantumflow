package fwcd.quantumflow.circuitbuilder;

import java.awt.Dimension;
import java.awt.Graphics2D;

import fwcd.fructose.draw.DrawColor;
import fwcd.fructose.geometry.Circle2D;
import fwcd.fructose.geometry.Vector2D;
import fwcd.fructose.swing.Renderable;
import fwcd.fructose.swing.SwingGraphics;

/**
 * Represents a visual, determined (qu)bit.
 * 
 * @author Fredrik
 *
 */
public class BitView implements Renderable {
	private final boolean editable;
	private boolean state = false;
	private boolean showState = true;
	private Circle2D frame;
	
	public BitView(boolean editable, Vector2D pos) {
		this.editable = editable;
		frame = new Circle2D(pos, 15);
	}

	public void hide() {
		showState = false;
	}
	
	public void moveTo(Vector2D pos) {
		frame = frame.movedTo(pos);
	}
	
	private DrawColor getColor() {
		return showState ? (state ? DrawColor.MAGENTA : DrawColor.BLACK) : DrawColor.GRAY;
	}
	
	public boolean onMouseClick(Vector2D pos) {
		if (editable && frame.contains(pos)) {
			state = !state;
			return true;
		} else return false;
	}
	
	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		SwingGraphics g = new SwingGraphics(g2d);
		frame.fillWith(getColor(), g);
	}

	public void set(boolean state) {
		showState = true;
		this.state = state;
	}

	public boolean get() {
		return state;
	}
}
