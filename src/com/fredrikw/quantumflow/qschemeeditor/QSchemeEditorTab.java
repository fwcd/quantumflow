package com.fredrikw.quantumflow.qschemeeditor;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import com.fredrikw.quantumflow.qscheme.QSchemeConsole;
import com.fredrikw.quantumflow.qscheme.QSchemeLang;
import com.fwcd.breeze.core.BreezeComponent;
import com.fwcd.breeze.theme.LightTheme;
import com.fwcd.fructose.io.ResourceFile;
import com.fwcd.fructose.swing.Viewable;

public class QSchemeEditorTab implements Viewable {
	private final JSplitPane view;
	
	private final QSchemeConsole console;
	private final BreezeComponent editor;
	
	public QSchemeEditorTab() {
		view = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		editor = new BreezeComponent(new LightTheme(), new QSchemeLang());
		editor.getEditor().open(new ResourceFile("/resources/HelloWorld.scm").getAsStream());
		view.setTopComponent(editor.getView());
		
		JButton runButton = new JButton("Run");
		runButton.addActionListener(l -> run());
		runButton.setToolTipText("Runs the program. (CTRL + R)");
		editor.getEditor().addKeybind(KeyStroke.getKeyStroke("ctrl R"), this::run);
		editor.addToolbarButton(runButton);
		
		console = new QSchemeConsole();
		view.setBottomComponent(console.getView());
		view.setDividerLocation(400);
	}

	private void run() {
		console.run(editor.getEditor().getText());
	}

	@Override
	public JComponent getView() {
		return view;
	}
}
