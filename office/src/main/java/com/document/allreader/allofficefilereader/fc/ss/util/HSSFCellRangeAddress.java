

package com.document.allreader.allofficefilereader.fc.ss.util;


import com.document.allreader.allofficefilereader.fc.hssf.formula.SheetNameFormatter;
import com.document.allreader.allofficefilereader.fc.hssf.record.RecordInputStream;
import com.document.allreader.allofficefilereader.fc.hssf.record.SelectionRecord;
import com.document.allreader.allofficefilereader.fc.util.LittleEndianByteArrayOutputStream;
import com.document.allreader.allofficefilereader.fc.util.LittleEndianOutput;


/**
 * See OOO documentation: excelfileformat.pdf sec 2.5.14 - 'Cell Range Address'<p/>
 * 
 * <p>In the Microsoft documentation, this is also known as a 
 *  Ref8U - see page 831 of version 1.0 of the documentation.
 *
 * Note - {@link SelectionRecord} uses the BIFF5 version of this structure
 * @author Dragos Buleandra (dragos.buleandra@trade2b.ro)
 */
public class HSSFCellRangeAddress extends CellRangeAddressBase {
	/*
	 * TODO - replace  org.apache.poi.hssf.util.Region
	 */
	public static final int ENCODED_SIZE = 8;

	public HSSFCellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol) {
		super(firstRow, lastRow, firstCol, lastCol);
	}

	/**
	 * @deprecated use {@link #serialize(LittleEndianOutput)}
	 */
	public int serialize(int offset, byte[] data) {
		serialize(new LittleEndianByteArrayOutputStream(data, offset, ENCODED_SIZE));
		return ENCODED_SIZE;
	}
	public void serialize(LittleEndianOutput out) {
		out.writeShort(getFirstRow());
		out.writeShort(getLastRow());
		out.writeShort(getFirstColumn());
		out.writeShort(getLastColumn());
	}

	public HSSFCellRangeAddress(RecordInputStream in) {
		super(readUShortAndCheck(in), in.readUShort(), in.readUShort(), in.readUShort());
	}

	private static int readUShortAndCheck(RecordInputStream in) {
		if (in.remaining() < ENCODED_SIZE) {
			// Ran out of data
			throw new RuntimeException("Ran out of data reading CellRangeAddress");
		}
		return in.readUShort();
	}

	public HSSFCellRangeAddress copy() {
		return new HSSFCellRangeAddress(getFirstRow(), getLastRow(), getFirstColumn(), getLastColumn());
	}

	public static int getEncodedSize(int numberOfItems) {
		return numberOfItems * ENCODED_SIZE;
	}

    /**
     * @return the text format of this range.  Single cell ranges are formatted
     *         like single cell references (e.g. 'A1' instead of 'A1:A1').
     */
    public String formatAsString() {
        return formatAsString(null, false);
    }

    /**
     * @return the text format of this range using specified sheet name.
     */
    public String formatAsString(String sheetName, boolean useAbsoluteAddress) {
        StringBuffer sb = new StringBuffer();
        if (sheetName != null) {
            sb.append(SheetNameFormatter.format(sheetName));
            sb.append("!");
        }
        CellReference cellRefFrom = new CellReference(getFirstRow(), getFirstColumn(),
                useAbsoluteAddress, useAbsoluteAddress);
        CellReference cellRefTo = new CellReference(getLastRow(), getLastColumn(),
                useAbsoluteAddress, useAbsoluteAddress);
        sb.append(cellRefFrom.formatAsString());

        //for a single-cell reference return A1 instead of A1:A1
        if(!cellRefFrom.equals(cellRefTo)){
            sb.append(':');
            sb.append(cellRefTo.formatAsString());
        }
        return sb.toString();
    }

    /**
     * @param ref usually a standard area ref (e.g. "B1:D8").  May be a single cell
     *            ref (e.g. "B5") in which case the result is a 1 x 1 cell range.
     */
    public static HSSFCellRangeAddress valueOf(String ref) {
        int sep = ref.indexOf(":");
        CellReference a;
        CellReference b;
        if (sep == -1) {
            a = new CellReference(ref);
            b = a;
        } else {
            a = new CellReference(ref.substring(0, sep));
            b = new CellReference(ref.substring(sep + 1));
        }
        return new HSSFCellRangeAddress(a.getRow(), b.getRow(), a.getCol(), b.getCol());
    }
}
