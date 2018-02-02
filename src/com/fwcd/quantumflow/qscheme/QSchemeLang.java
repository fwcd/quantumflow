package com.fwcd.quantumflow.qscheme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fwcd.breeze.languages.ProgrammingLang;
import com.fwcd.quantumflow.qscheme.lib.QSchemeExpression;
import com.fwcd.quantumflow.qscheme.lib.QSchemeLib;

public class QSchemeLang implements ProgrammingLang {
	private final QSchemeLib lib = new QSchemeLib();
	private final String[] keywords;
	private final String[] extensions = {"qsh"};
	
	public QSchemeLang() {
		List<String> kwBuffer = new ArrayList<>(Arrays.asList(
				"abs", "acos", "append", "apply", "asin", "assoc",
				"assq", "assv", "atan", "boolean", "caaaar", "caaar",
				"caadar", "caaddr", "caadr", "caar", "cadaar",
				"cadadr", "cadar", "caddar", "cadddr", "caddr",
				"cadr", "call-with-current-continuation",
				"call-with-input-file", "call-with-output-file",
				"car", "cdaaar", "cdaadr", "cdaadr", "cdaar",
				"cdadar", "cdaddr", "cdadr", "cdar", "cddaar",
				"cddadr", "cddar", "cdddar", "cddddar", "cddddr",
				"cdddr", "cddr", "cdr", "ceiling", "exact", "inexact",
				"char", "integer", "char-ci", "char-ci=?",
				"char-ci>=?", "char-ci>?", "char-downcase",
				"char-lower-case?", "char-numeric?", "char-upcase",
				"char-upper-case?", "char-whitespace?",
				"char=?", "char>=?", "char>?", "char?", "close-input-port",
				"close-output-port", "cons", "cos", "display", "define", "eq?",
				"equal?", "eqv?", "eval", "even?", "exact?", "exp",
				"expt", "floor", "for-each", "force", "gcd", "inexact?",
				"input-port?", "interaction-environment", "integer?",
				"lcm", "lambda", "length", "list", "string", "vector", "list-ref",
				"list-tail", "list?", "load", "log", "macroexpand",
				"make-string", "make-vector", "map", "max", "member",
				"memq", "memv", "min", "modulo", "negative?", "newline",
				"not", "null-environment", "null?", "number->string",
				"number?", "integer->char", "odd?", "open-input-file",
				"open-output-file", "output-port?", "pair?", "peek-char",
				"positive?", "procedure?", "quotient", "read", "read-char",
				"remainder", "reverse", "round", "scheme-report-environment",
				"set-car!", "set-cdr!", "sin", "sqrt", "string", "string->list",
				"string->number", "string->symbol", "string-append", "string-ci<=?",
				"string-ci", "string-ci=?", "string-ci>=?", "string-copy",
				"string-fill!", "string-length", "string-ref", "string-set!",
				"string<=?", "string=?", "string>=?", "string>?", "string?",
				"substring", "symbol->string", "symbol?", "tan", "truncate",
				"vector", "vector->list", "vector-fill!", "vector-length",
				"vector-ref", "vector-set!", "vector?", "write", "write-char",
				"zero?"
		));
		
		for (QSchemeExpression function : lib.getFunctions()) {
			String name = function.getName();
			if (name.length() > 0) {
				kwBuffer.add(name);
			}
		}
		
		keywords = kwBuffer.toArray(new String[0]);
	}
		
	@Override
	public String[] getFileExtensions() {
		return extensions;
	}

	@Override
	public String[] getKeywords() {
		return keywords;
	}
}
