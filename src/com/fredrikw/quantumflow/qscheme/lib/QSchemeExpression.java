package com.fredrikw.quantumflow.qscheme.lib;

public interface QSchemeExpression {
	Class<?>[] getImports();
	
	String getName();
	
	String getExpression();
}
