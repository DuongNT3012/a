

package com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.function;

import java.util.Date;

import com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.eval.NumberEval;
import com.ntdapp.allreader.allofficefilereader.fc.hssf.formula.eval.ValueEval;
import com.ntdapp.allreader.allofficefilereader.ss.util.DateUtil;



/**
 * Implementation of Excel NOW() Function
 *
 * @author Frank Taffelt
 */
public final class Now extends Fixed0ArgFunction {

	public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
		Date now = new Date(System.currentTimeMillis());
		return new NumberEval(DateUtil.getExcelDate(now));
	}
}
