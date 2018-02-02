package com.fredrikw.quantumflow.circuitbuilder;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.fwcd.fructose.ListenerList;
import com.fwcd.fructose.Pair;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.quantum.simulator.SimulatedQuantumCircuit;
import com.fwcd.fructose.swing.RenderPanel;
import com.fwcd.fructose.swing.Rendereable;
import com.fwcd.fructose.swing.Viewable;

public class VisualCircuit implements Viewable, Rendereable {
	private final JPanel view;
	private SimulatedQuantumCircuit model = new SimulatedQuantumCircuit();

	private final int padding = 20;
	private final int lineDistance = padding * 4;
	private final SortedMap<Integer, QubitFlowLine> lines = new TreeMap<>();
	
	private boolean viewSelectedGate = false;
	private Optional<VisualGate> selectedGate = Optional.empty();
	
	private final ListenerList changeListeners = new ListenerList();
	
	private final Vector2D initialOffset = new Vector2D(padding * 1.5, 0);
	private Vector2D offset = initialOffset;
	private Vector2D lastMousePos;
	private int x = padding;
	private int y = padding;
	
	public VisualCircuit() {
		view = new RenderPanel(this);
		MouseAdapter adapter = new MouseAdapter() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				onMouseMove(pos(e));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				onMouseClick(pos(e));
			}

			private Vector2D pos(MouseEvent e) {
				return new Vector2D(e.getX(), e.getY());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				viewSelectedGate = true;
				view.repaint();
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				viewSelectedGate = false;
				view.repaint();
			}
			
		};
		view.addMouseListener(adapter);
		view.addMouseMotionListener(adapter);
		
		addListener(() -> lines.values().forEach(QubitFlowLine::hideOutput));
	}
	
	public void calculateResult() {
		Iterator<QubitFlowLine> lineIt = lines.values().iterator();
		for (boolean outBit : model.computeResult().getBits()) {
			lineIt.next().setOutput(outBit);
		}
		view.repaint();
	}
	
	private void onMouseMove(Vector2D pos) {
		lastMousePos = pos;
		selectedGate.ifPresent(gate -> gate.moveTo(pos));
		view.repaint();
	}

	private Pair<Integer, QubitFlowLine> nearestLine() {
		int minYD = Integer.MAX_VALUE;
		QubitFlowLine minLine = null;
		int minI = -1;
		
		int i = 0;
		for (int yPos : lines.keySet()) {
			QubitFlowLine line = lines.get(yPos);
			
			int diff = (int) Math.abs(yPos - lastMousePos.getY());
			
			if (diff < minYD) {
				minYD = diff;
				minLine = line;
				minI = i;
			}
			
			i++;
		}
		
		return new Pair<>(minI, minLine);
	}
	
	private void onMouseClick(Vector2D pos) {
		for (QubitFlowLine line : lines.values()) {
			line.onMouseClick(pos);
		}
		
		selectedGate.ifPresent(gate -> {
			Pair<Integer, QubitFlowLine> indexedLine = nearestLine();
			int lineIndex = indexedLine.getA();
			QubitFlowLine line = indexedLine.getB();
			
			if (line != null) {
				model.addOperation(gate.getGate(), lineIndex);
				line.addGate(gate, offset);
				offset = offset.add(gate.getSize().getWidth() + 2, 0);
				selectedGate = Optional.empty();
			}
		});
		
		updateInput();
		view.repaint();
	}
	
	private void updateInput() {
		int i = 0;
		for (QubitFlowLine line : lines.values()) {
			model.setInputQubit(i, line.getInput());
			i++;
		}
		changeListeners.fire();
	}

	public void addQubit() {
		model.addQubit(false);
		lines.put(y, new QubitFlowLine(new Vector2D(x, y), new Vector2D(x, y)));
		y += lineDistance;
		view.repaint();
		changeListeners.fire();
	}
	
	public void reset() {
		model = new SimulatedQuantumCircuit();
		offset = initialOffset;
		lines.clear();
		y = padding;
		view.repaint();
		changeListeners.fire();
	}
	
	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		g2d.setStroke(new BasicStroke(2));
		
		for (QubitFlowLine line : lines.values()) {
			line.setEnd(new Vector2D(canvasSize.getWidth() - x, line.getEnd().getY()));
			line.render(g2d, canvasSize);
		}
		
		if (viewSelectedGate) {
			selectedGate.ifPresent(gate -> gate.render(g2d, canvasSize));
		}
	}

	public void addListener(Runnable listener) {
		changeListeners.add(listener);
	}
	
	@Override
	public JComponent getView() {
		return view;
	}

	public void select(VisualGate gate) {
		selectedGate = Optional.of(gate);
		view.repaint();
	}

	public int getLineDistance() {
		return lineDistance;
	}

	public SimulatedQuantumCircuit getModel() {
		return model;
	}
}
