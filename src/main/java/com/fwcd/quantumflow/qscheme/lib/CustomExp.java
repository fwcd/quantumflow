package com.fwcd.quantumflow.qscheme.lib;

public class CustomExp implements QSchemeExpression {
	private final Class<?>[] imports;
	private final String name;
	private final String expression;
	
	public CustomExp(String expression, Class<?>... imports) {
		this("", expression, imports);
	}
	
	public CustomExp(String name, String expression, Class<?>... imports) {
		this.imports = imports;
		this.name = name;
		this.expression = expression;
	}

	@Override
	public Class<?>[] getImports() {
		return imports;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getExpression() {
		return expression;
	}
}
