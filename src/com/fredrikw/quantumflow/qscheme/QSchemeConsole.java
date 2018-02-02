package com.fredrikw.quantumflow.qscheme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.fwcd.fructose.exception.Rethrow;
import com.fwcd.fructose.swing.HintTextField;
import com.fwcd.fructose.swing.InputForm;
import com.fwcd.fructose.swing.Viewable;

public class QSchemeConsole implements Viewable {
	private static QSchemeConsole instance;
	
	private final JPanel view;
	private final JTextPane textArea;
	private final JScrollPane scrollPane;
	private final InputForm input;
	private final HintTextField inputField;
	
	private final List<String> history = new ArrayList<>();
	private int historyIndex = 0;
	
	private final int padding = 5;
	private final float fontSize = 14;
	private QScheme scheme = new QScheme();
	
	public QSchemeConsole() {
		instance = this;
		
		history.add("");
		
		view = new JPanel();
		view.setLayout(new BorderLayout());
		view.setPreferredSize(new Dimension(150, 150));
		
		textArea = new JTextPane();
		textArea.setBackground(Color.DARK_GRAY);
		textArea.setForeground(Color.WHITE);
		textArea.setMargin(new Insets(padding, padding, padding, padding));
		textArea.setFont(textArea.getFont().deriveFont(fontSize));
		
		scrollPane = new JScrollPane(textArea);
		view.add(scrollPane, BorderLayout.CENTER);
		
		input = new InputForm("Enter QuantumScheme expression...", this::submit, 8);
		inputField = input.getTextField();
		inputField.setFont(inputField.getFont().deriveFont(fontSize));
		
		addKeybind(KeyStroke.getKeyStroke("UP"), this::moveUpHistory);
		addKeybind(KeyStroke.getKeyStroke("DOWN"), this::moveDownHistory);
		
		view.add(input.getView(), BorderLayout.SOUTH);
	}
	
	private void addKeybind(KeyStroke keyStroke, Runnable onPress) {
		inputField.getInputMap().put(keyStroke, keyStroke);
		inputField.getActionMap().put(keyStroke, new AbstractAction() {
			private static final long serialVersionUID = 3937933654671713312L;

			@Override
			public void actionPerformed(ActionEvent e) {
				onPress.run();
			}
		});
	}
	
	private void moveUpHistory() {
		if (historyIndex < (history.size() - 1)) {
			historyIndex++;
			inputField.setText(history.get(historyIndex));
		}
	}
	
	private void moveDownHistory() {
		if (historyIndex > 0) {
			historyIndex--;
			inputField.setText(history.get(historyIndex));
		}
	}
	
	private void submit(String input) {
		historyIndex = 0;
		history.add(1, input);
		process(input);
	}

	private void process(String input) {
		try {
			println(scheme.eval(input));
		} catch (Exception e) {
			handle(e);
		}
		
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	
	@Override
	public JComponent getView() {
		return view;
	}
	
	public void clear() {
		textArea.setText(" ==== Run " + Integer.toHexString(ThreadLocalRandom.current().nextInt()) + " ==== \n");
	}
	
	public void run(String program) {
		clear();
		try {
			scheme = new QScheme();
			scheme.load(program);
			scheme.call("main");
		} catch (Exception e) {
			handle(e);
		}
		
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	private void handle(Throwable e) {
		MutableAttributeSet attribs = new SimpleAttributeSet();
		StyleConstants.setForeground(attribs, Color.RED);
		
		e.printStackTrace();
		writeln(">> " + e.getClass().getSimpleName() + ": " + e.getMessage(), attribs);
		
		Throwable cause = e.getCause();
		if (cause != null) {
			handle(cause);
		}
	}
	
	private void write(String s) {
		write(s, null);
	}
	
	private void write(String s, MutableAttributeSet attribs) {
		try {
			StyledDocument doc = textArea.getStyledDocument();
			doc.insertString(doc.getLength(), s, attribs);
		} catch (BadLocationException e) {
			throw new Rethrow(e);
		}
	}
	
	private void writeln(String s) {
		writeln(s, null);
	}
	
	private void writeln(String s, MutableAttributeSet attribs) {
		write(s + "\n", attribs);
	}
	
	public static void print(Object o) {
		if (o != null) {
			instance.write(o.toString());
		}
	}
	
	public static void println(Object o) {
		if (o != null) {
			instance.writeln(o.toString());
		}
	}
}
