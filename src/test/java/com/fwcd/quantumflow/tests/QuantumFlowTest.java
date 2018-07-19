package com.fwcd.quantumflow.tests;

import com.fwcd.quantum.core.QubitSuperpos;
import com.fwcd.quantum.gates.binary.SqrtSwapGate;
import com.fwcd.quantum.simulator.SimulatedSuperpos;

import org.junit.Test;

public class QuantumFlowTest {
	@Test
	public void test() {
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
