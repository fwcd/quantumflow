package com.fwcd.quantumflow.qscheme;

import java.util.Arrays;

import com.fwcd.fructose.quantum.core.QubitState;

import jsint.Pair;

public class QSUtils {
	public static final boolean TRUE = true;
	public static final boolean FALSE = false;
	
	public static boolean[] concat(boolean[] b1, boolean[] b2) {
		boolean[] result = new boolean[b1.length + b2.length];
		System.arraycopy(b1, 0, result, 0, b1.length);
		System.arraycopy(b2, 0, result, b1.length, b2.length);
		return result;
	}
	
	public static boolean[] subArray(boolean[] arr, int start) {
		return Arrays.copyOfRange(arr, start, arr.length);
	}
	
	public static Pair toSchemeIntList(QubitState state) {
		return toSchemeIntList(state.getBits());
	}
	
	public static int toInt(boolean b) {
		return b ? 1 : 0;
	}
	
	public static Pair toSchemeIntList(boolean[] array) {
		if (array.length == 0) {
			return Pair.EMPTY;
		} else {
			return new Pair(toInt(array[0]), toSchemeIntList(subArray(array, 1)));
		}
	}
	
	public static boolean[] toBoolArray(Pair list) {
		Object restObj = list.rest;
		boolean[] rest;
		
		if (restObj instanceof Pair) {
			Pair restPair = (Pair) restObj;
			if (restPair.isEmpty()) {
				rest = new boolean[0];
			} else {
				rest = toBoolArray(restPair);
			}
		} else {
			rest = new boolean[] {(boolean) restObj};
		}
		
		return concat(
				new boolean[] {(boolean) list.first},
				rest
		);
	}
}
