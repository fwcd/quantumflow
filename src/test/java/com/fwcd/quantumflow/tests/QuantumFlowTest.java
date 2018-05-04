import com.fwcd.fructose.quantum.core.QubitSuperpos;
import com.fwcd.fructose.quantum.gates.binary.SqrtSwapGate;
import com.fwcd.fructose.quantum.simulator.SimulatedSuperpos;

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