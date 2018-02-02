package com.fwcd.quantumflow;

import com.fwcd.fructose.quantum.core.QubitSuperpos;
import com.fwcd.fructose.quantum.gates.binary.SqrtSwapGate;
import com.fwcd.fructose.quantum.simulator.SimulatedSuperpos;
import com.fwcd.quantumflow.core.QuantumFlowApp;

public class QuantumFlowMain {
	private static final boolean TEST_MODE = false;
	
	public static void main(String[] args) {
		if (TEST_MODE) {
			test();
		} else {
			new QuantumFlowApp("QuantumFlow", 800, 600);
		}
	}
	
	private static void test() {
		QubitSuperpos superpos = new SimulatedSuperpos(true, true, false, true);
		
		System.out.println(superpos);
		superpos = superpos
				.apply(new SqrtSwapGate(), 1)
				.apply(new SqrtSwapGate(), 0)
				.apply(new SqrtSwapGate(), 1);
		System.out.println(superpos);
		System.out.println(superpos.collapse());
	}
}
