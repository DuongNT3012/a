

package com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.function;

import com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.eval.ErrorEval;
import com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.eval.ValueEval;

/**
 * Implementation of Excel function NA()
 *
 * @author Josh Micich
 */
public final class Na extends Fixed0ArgFunction {

	public ValueEval evaluate(int srcCellRow, int srcCellCol) {
		return ErrorEval.NA;
	}
}
