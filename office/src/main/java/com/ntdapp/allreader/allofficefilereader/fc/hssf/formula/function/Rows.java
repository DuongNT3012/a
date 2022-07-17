

package com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.function;


import com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.TwoDEval;
import com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.eval.ErrorEval;
import com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.eval.NumberEval;
import com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.eval.RefEval;
import com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.eval.ValueEval;


/**
 * Implementation for Excel ROWS function.
 *
 * @author Josh Micich
 */
public final class Rows extends Fixed1ArgFunction {

	public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {

		int result;
		if (arg0 instanceof TwoDEval) {
			result = ((TwoDEval) arg0).getHeight();
		} else if (arg0 instanceof RefEval) {
			result = 1;
		} else { // anything else is not valid argument
			return ErrorEval.VALUE_INVALID;
		}
		return new NumberEval(result);
	}
}