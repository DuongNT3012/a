

package com.document.allreader.allofficefilereader.fc.hssf.formula.function;


/**
 * Implementation of Excel function SUMX2PY2()<p/>
 *
 * Calculates the sum of squares in two arrays of the same size.<br/>
 * <b>Syntax</b>:<br/>
 * <b>SUMX2PY2</b>(<b>arrayX</b>, <b>arrayY</b>)<p/>
 *
 * result = &Sigma;<sub>i: 0..n</sub>(x<sub>i</sub><sup>2</sup>+y<sub>i</sub><sup>2</sup>)
 *
 * @author Amol S. Deshmukh &lt; amolweb at ya hoo dot com &gt;
 */
public final class Sumx2py2 extends XYNumericFunction {

	private static final Accumulator XSquaredPlusYSquaredAccumulator = new Accumulator() {
		public double accumulate(double x, double y) {
			return x * x + y * y;
		}
	};

	protected Accumulator createAccumulator() {
		return XSquaredPlusYSquaredAccumulator;
	}
}
