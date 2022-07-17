

package com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.function;

import com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.eval.ErrorEval;
import com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.eval.ValueEval;

/**
 * Convenience base class for functions that must take exactly three arguments.
 *
 * @author Josh Micich
 */
public abstract class Fixed3ArgFunction implements Function3Arg {
	public final ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
		if (args.length != 3) {
			return ErrorEval.VALUE_INVALID;
		}
		return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2]);
	}
}
