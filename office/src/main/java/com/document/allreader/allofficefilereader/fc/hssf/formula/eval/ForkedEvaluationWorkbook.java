

package com.document.allreader.allofficefilereader.fc.hssf.formula.eval;

import java.util.HashMap;
import java.util.Map;

import com.document.allreader.allofficefilereader.fc.hssf.formula.EvaluationCell;
import com.document.allreader.allofficefilereader.fc.hssf.formula.EvaluationName;
import com.document.allreader.allofficefilereader.fc.hssf.formula.EvaluationSheet;
import com.document.allreader.allofficefilereader.fc.hssf.formula.EvaluationWorkbook;
import com.document.allreader.allofficefilereader.fc.hssf.formula.ptg.NamePtg;
import com.document.allreader.allofficefilereader.fc.hssf.formula.ptg.NameXPtg;
import com.document.allreader.allofficefilereader.fc.hssf.formula.ptg.Ptg;
import com.document.allreader.allofficefilereader.fc.hssf.formula.udf.UDFFinder;
import com.document.allreader.allofficefilereader.fc.ss.usermodel.Workbook;



/**
 * Represents a workbook being used for forked evaluation. Most operations are delegated to the
 * shared master workbook, except those that potentially involve cell values that may have been
 * updated after a call to {@link #getOrCreateUpdatableCell(String, int, int)}.
 *
 * @author Josh Micich
 */
final class ForkedEvaluationWorkbook implements EvaluationWorkbook {

	private final EvaluationWorkbook _masterBook;
	private final Map<String, ForkedEvaluationSheet> _sharedSheetsByName;

	public ForkedEvaluationWorkbook(EvaluationWorkbook master) {
		_masterBook = master;
		_sharedSheetsByName = new HashMap<String, ForkedEvaluationSheet>();
	}

	public ForkedEvaluationCell getOrCreateUpdatableCell(String sheetName, int rowIndex,
			int columnIndex) {
		ForkedEvaluationSheet sheet = getSharedSheet(sheetName);
		return sheet.getOrCreateUpdatableCell(rowIndex, columnIndex);
	}

	public EvaluationCell getEvaluationCell(String sheetName, int rowIndex, int columnIndex) {
		ForkedEvaluationSheet sheet = getSharedSheet(sheetName);
		return sheet.getCell(rowIndex, columnIndex);
	}

	private ForkedEvaluationSheet getSharedSheet(String sheetName) {
		ForkedEvaluationSheet result = _sharedSheetsByName.get(sheetName);
		if (result == null) {
			result = new ForkedEvaluationSheet(_masterBook.getSheet(_masterBook
					.getSheetIndex(sheetName)));
			_sharedSheetsByName.put(sheetName, result);
		}
		return result;
	}

	public void copyUpdatedCells(Workbook workbook) {
//		String[] sheetNames = new String[_sharedSheetsByName.size()];
//		_sharedSheetsByName.keySet().toArray(sheetNames);
//		OrderedSheet[] oss = new OrderedSheet[sheetNames.length];
//		for (int i = 0; i < sheetNames.length; i++) {
//			String sheetName = sheetNames[i];
//			oss[i] = new OrderedSheet(sheetName, _masterBook.getSheetIndex(sheetName));
//		}
//		for (int i = 0; i < oss.length; i++) {
//			String sheetName = oss[i].getSheetName();
//			ForkedEvaluationSheet sheet = _sharedSheetsByName.get(sheetName);
//			sheet.copyUpdatedCells(workbook.getSheet(sheetName));
//		}
	}

	public int convertFromExternSheetIndex(int externSheetIndex) {
		return _masterBook.convertFromExternSheetIndex(externSheetIndex);
	}

	public ExternalSheet getExternalSheet(int externSheetIndex) {
		return _masterBook.getExternalSheet(externSheetIndex);
	}

	public Ptg[] getFormulaTokens(EvaluationCell cell) {
		if (cell instanceof ForkedEvaluationCell) {
			// doesn't happen yet because formulas cannot be modified from the master workbook
			throw new RuntimeException("Updated formulas not supported yet");
		}
		return _masterBook.getFormulaTokens(cell);
	}

	public EvaluationName getName(NamePtg namePtg) {
		return _masterBook.getName(namePtg);
	}

    public EvaluationName getName(String name, int sheetIndex){
        return _masterBook.getName(name, sheetIndex);
    }

	public EvaluationSheet getSheet(int sheetIndex) {
		return getSharedSheet(getSheetName(sheetIndex));
	}
	
	public ExternalName getExternalName(int externSheetIndex, int externNameIndex) {
	   return _masterBook.getExternalName(externSheetIndex, externNameIndex);
	}

	public int getSheetIndex(EvaluationSheet sheet) {
		if (sheet instanceof ForkedEvaluationSheet) {
			ForkedEvaluationSheet mes = (ForkedEvaluationSheet) sheet;
			return mes.getSheetIndex(_masterBook);
		}
		return _masterBook.getSheetIndex(sheet);
	}

	public int getSheetIndex(String sheetName) {
		return _masterBook.getSheetIndex(sheetName);
	}

	public String getSheetName(int sheetIndex) {
		return _masterBook.getSheetName(sheetIndex);
	}

	public String resolveNameXText(NameXPtg ptg) {
		return _masterBook.resolveNameXText(ptg);
	}

    public UDFFinder getUDFFinder(){
        return _masterBook.getUDFFinder();
    }

	private static final class OrderedSheet implements Comparable<OrderedSheet> {
		private final String _sheetName;
		private final int _index;

		public OrderedSheet(String sheetName, int index) {
			_sheetName = sheetName;
			_index = index;
		}
		public String getSheetName() {
			return _sheetName;
		}
		public int compareTo(OrderedSheet o) {
			return _index - o._index;
		}
	}
}
