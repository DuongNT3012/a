

package com.office.allreader.allofficefilereader.fc.hssf.formula.function;

import java.util.Date;

import com.office.allreader.allofficefilereader.fc.hssf.formula.eval.NumberEval;
import com.office.allreader.allofficefilereader.fc.hssf.formula.eval.ValueEval;
import com.office.allreader.allofficefilereader.ss.util.DateUtil;



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
