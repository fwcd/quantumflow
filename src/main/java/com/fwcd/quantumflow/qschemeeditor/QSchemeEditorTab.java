package com.fwcd.quantumflow.qschemeeditor;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import com.fwcd.fructose.io.ResourceFile;
import com.fwcd.fructose.swing.Viewable;
import com.fwcd.palm.config.PalmConfigured;
import com.fwcd.palm.editor.PalmEditor;
import com.fwcd.palm.languages.ProgrammingLang;
import com.fwcd.palm.theme.LightTheme;
import com.fwcd.palm.theme.Theme;
import com.fwcd.quantumflow.qscheme.QSchemeConsole;
import com.fwcd.quantumflow.qscheme.QSchemeLang;

public class QSchemeEditorTab implements Viewable, PalmConfigured {
	private final JPanel view;
	private final JSplitPane splitPane;
	private final JFileChooser fileChooser = new JFileChooser();

	private final JToolBar toolBar;
	private final QSchemeConsole console;
	private final PalmEditor editor;

	private final Theme theme = new LightTheme();
	private final ProgrammingLang lang = new QSchemeLang();

	public QSchemeEditorTab() {
		view = new JPanel();
		view.setLayout(new BorderLayout());

		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		view.add(toolBar, BorderLayout.NORTH);

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		view.add(splitPane, BorderLayout.CENTER);

		editor = new PalmEditor(this);
		new ResourceFile("/HelloWorld.scm").withStream(stream -> editor.open(stream));
		splitPane.setTopComponent(editor.getView());

		JButton loadButton = new JButton("Open");
		loadButton.addActionListener(l -> openFile());
		toolBar.add(loadButton);

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(l -> saveFile());
		toolBar.add(saveButton);

		JButton runButton = new JButton("Run");
		runButton.addActionListener(l -> run());
		runButton.setToolTipText("Runs the program. (CTRL + R)");
		editor.addKeybind(KeyStroke.getKeyStroke("ctrl R"), this::run);
		toolBar.add(runButton);

		console = new QSchemeConsole();
		splitPane.setBottomComponent(console.getView());
		splitPane.setDividerLocation(400);
	}

	private void openFile() {
		if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (file != null) {
				editor.open(file);
			}
		}
	}

	private void saveFile() {
		if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (file != null) {
				editor.save(file);
			}
		}
	}

	private void run() {
		console.run(editor.getText());
	}

	@Override
	public JComponent getView() { return view; }

	@Override
	public ProgrammingLang getLanguage() { return lang; }

	@Override
	public Theme getTheme() { return theme; }
}
