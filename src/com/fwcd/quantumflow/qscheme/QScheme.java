package com.fwcd.quantumflow.qscheme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fwcd.quantumflow.qscheme.lib.QSchemeExpression;
import com.fwcd.quantumflow.qscheme.lib.QSchemeLib;

import jscheme.JScheme;

public class QScheme extends JScheme {
	private static final long serialVersionUID = 3664423151288468658L;
	private final QSchemeLib lib = new QSchemeLib();
	
	public QScheme() {
		Set<String> imports = new HashSet<>();
		List<String> definitions = new ArrayList<>();
		
		for (QSchemeExpression func : lib.getFunctions()) {
			imports.addAll(Arrays.stream(func.getImports())
					.map(clazz -> "(import \"" + clazz.getName() + "\")")
					.collect(Collectors.toList())
			);
			definitions.add(func.getExpression());
		}
		
		for (String imp : imports) {
			eval(imp);
		}
		
		for (String def : definitions) {
			eval(def);
		}
	}
}
