package com.document.allreader.allofficefilereader.fc.p014ss.util;

import com.document.allreader.allofficefilereader.fc.p014ss.SpreadsheetVersion;
import java.util.ArrayList;
import java.util.StringTokenizer;

/* renamed from: com.allreader.office.allofficefilereader.fc.ss.util.AreaReference */

public class AreaReference {
    private static final char CELL_DELIMITER = ':';
    private static final char SHEET_NAME_DELIMITER = '!';
    private static final char SPECIAL_NAME_DELIMITER = '\'';
    private final CellReference _firstCell;
    private final boolean _isSingleCell;
    private final CellReference _lastCell;

    public AreaReference(String str) {
        if (isContiguous(str)) {
            String[] separateAreaRefs = separateAreaRefs(str);
            String str2 = separateAreaRefs[0];
            if (separateAreaRefs.length == 1) {
                CellReference cellReference = new CellReference(str2);
                this._firstCell = cellReference;
                this._lastCell = cellReference;
                this._isSingleCell = true;
            } else if (separateAreaRefs.length == 2) {
                String str3 = separateAreaRefs[1];
                if (!isPlainColumn(str2)) {
                    this._firstCell = new CellReference(str2);
                    this._lastCell = new CellReference(str3);
                    this._isSingleCell = str2.equals(str3);
                } else if (isPlainColumn(str3)) {
                    boolean isPartAbsolute = CellReference.isPartAbsolute(str2);
                    boolean isPartAbsolute2 = CellReference.isPartAbsolute(str3);
                    int convertColStringToIndex = CellReference.convertColStringToIndex(str2);
                    int convertColStringToIndex2 = CellReference.convertColStringToIndex(str3);
                    this._firstCell = new CellReference(0, convertColStringToIndex, true, isPartAbsolute);
                    this._lastCell = new CellReference(65535, convertColStringToIndex2, true, isPartAbsolute2);
                    this._isSingleCell = false;
                } else {
                    throw new RuntimeException("Bad area ref '" + str + "'");
                }
            } else {
                throw new IllegalArgumentException("Bad area ref '" + str + "'");
            }
        } else {
            throw new IllegalArgumentException("References passed to the AreaReference must be contiguous, use generateContiguous(ref) if you have non-contiguous references");
        }
    }

    private boolean isPlainColumn(String str) {
        for (int length = str.length() - 1; length >= 0; length--) {
            char charAt = str.charAt(length);
            if (!(charAt == '$' && length == 0) && (charAt < 'A' || charAt > 'Z')) {
                return false;
            }
        }
        return true;
    }

    public AreaReference(CellReference cellReference, CellReference cellReference2) {
        boolean z;
        int i;
        boolean z2;
        int i2;
        boolean z3;
        boolean z4;
        short s;
        short s2;
        boolean z5 = true;
        boolean z6 = cellReference.getRow() > cellReference2.getRow();
        z5 = cellReference.getCol() <= cellReference2.getCol() ? false : z5;
        if (z6 || z5) {
            if (z6) {
                i2 = cellReference2.getRow();
                z2 = cellReference2.isRowAbsolute();
                i = cellReference.getRow();
                z = cellReference.isRowAbsolute();
            } else {
                i2 = cellReference.getRow();
                z2 = cellReference.isRowAbsolute();
                i = cellReference2.getRow();
                z = cellReference2.isRowAbsolute();
            }
            if (z5) {
                s2 = cellReference2.getCol();
                z3 = cellReference2.isColAbsolute();
                s = cellReference.getCol();
                z4 = cellReference.isColAbsolute();
            } else {
                s2 = cellReference.getCol();
                boolean isColAbsolute = cellReference.isColAbsolute();
                s = cellReference2.getCol();
                boolean isColAbsolute2 = cellReference2.isColAbsolute();
                z3 = isColAbsolute;
                z4 = isColAbsolute2;
            }
            this._firstCell = new CellReference(i2, s2, z2, z3);
            this._lastCell = new CellReference(i, s, z, z4);
        } else {
            this._firstCell = cellReference;
            this._lastCell = cellReference2;
        }
        this._isSingleCell = false;
    }

    public static boolean isContiguous(String str) {
        return str.indexOf(44) == -1;
    }

    public static AreaReference getWholeRow(String str, String str2) {
        return new AreaReference("$A" + str + ":$IV" + str2);
    }

    public static AreaReference getWholeColumn(String str, String str2) {
        return new AreaReference(str + "$1:" + str2 + "$65536");
    }

    public static boolean isWholeColumnReference(CellReference cellReference, CellReference cellReference2) {
        return cellReference.getRow() == 0 && cellReference.isRowAbsolute() && cellReference2.getRow() == SpreadsheetVersion.EXCEL97.getLastRowIndex() && cellReference2.isRowAbsolute();
    }

    public boolean isWholeColumnReference() {
        return isWholeColumnReference(this._firstCell, this._lastCell);
    }

    public static AreaReference[] generateContiguous(String str) {
        ArrayList arrayList = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        while (stringTokenizer.hasMoreTokens()) {
            arrayList.add(new AreaReference(stringTokenizer.nextToken()));
        }
        return (AreaReference[]) arrayList.toArray(new AreaReference[arrayList.size()]);
    }

    public boolean isSingleCell() {
        return this._isSingleCell;
    }

    public CellReference getFirstCell() {
        return this._firstCell;
    }

    public CellReference getLastCell() {
        return this._lastCell;
    }

    public CellReference[] getAllReferencedCells() {
        if (this._isSingleCell) {
            return new CellReference[]{this._firstCell};
        }
        int max = Math.max(this._firstCell.getRow(), this._lastCell.getRow());
        int min = Math.min((int) this._firstCell.getCol(), (int) this._lastCell.getCol());
        int max2 = Math.max((int) this._firstCell.getCol(), (int) this._lastCell.getCol());
        String sheetName = this._firstCell.getSheetName();
        ArrayList arrayList = new ArrayList();
        for (int min2 = Math.min(this._firstCell.getRow(), this._lastCell.getRow()); min2 <= max; min2++) {
            for (int i = min; i <= max2; i++) {
                arrayList.add(new CellReference(sheetName, min2, i, this._firstCell.isRowAbsolute(), this._firstCell.isColAbsolute()));
            }
        }
        return (CellReference[]) arrayList.toArray(new CellReference[arrayList.size()]);
    }

    public String formatAsString() {
        if (isWholeColumnReference()) {
            return CellReference.convertNumToColString(this._firstCell.getCol()) + ":" + CellReference.convertNumToColString(this._lastCell.getCol());
        }
        StringBuffer stringBuffer = new StringBuffer(32);
        stringBuffer.append(this._firstCell.formatAsString());
        if (!this._isSingleCell) {
            stringBuffer.append(CELL_DELIMITER);
            if (this._lastCell.getSheetName() == null) {
                stringBuffer.append(this._lastCell.formatAsString());
            } else {
                this._lastCell.appendCellReference(stringBuffer);
            }
        }
        return stringBuffer.toString();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(64);
        stringBuffer.append(getClass().getName());
        stringBuffer.append(" [");
        stringBuffer.append(formatAsString());
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    private static String[] separateAreaRefs(String str) {
        int length = str.length();
        int i = -1;
        int i2 = 0;
        boolean z = false;
        while (i2 < length) {
            char charAt = str.charAt(i2);
            if (charAt != '\'') {
                if (charAt == ':' && !z) {
                    if (i < 0) {
                        i = i2;
                    } else {
                        throw new IllegalArgumentException("More than one cell delimiter ':' appears in area reference '" + str + "'");
                    }
                }
            } else if (!z) {
                z = true;
            } else if (i2 < length - 1) {
                int i3 = i2 + 1;
                if (str.charAt(i3) == '\'') {
                    i2 = i3;
                } else {
                    z = false;
                }
            } else {
                throw new IllegalArgumentException("Area reference '" + str + "' ends with special name delimiter '" + SPECIAL_NAME_DELIMITER + "'");
            }
            i2++;
        }
        if (i < 0) {
            return new String[]{str};
        }
        String substring = str.substring(0, i);
        String substring2 = str.substring(i + 1);
        if (substring2.indexOf(33) < 0) {
            int lastIndexOf = substring.lastIndexOf(33);
            if (lastIndexOf < 0) {
                return new String[]{substring, substring2};
            }
            String substring3 = substring.substring(0, lastIndexOf + 1);
            return new String[]{substring, substring3 + substring2};
        }
        throw new RuntimeException("Unexpected ! in second cell reference of '" + str + "'");
    }
}
