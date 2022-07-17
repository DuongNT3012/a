package com.ntdapp.allreader.allofficefilereader.fc.p014ss.usermodel;

import com.ntdapp.allreader.allofficefilereader.fc.p014ss.util.CellRangeAddressList;

/* renamed from: com.allreader.office.allofficefilereader.fc.ss.usermodel.DataValidationHelper */

public interface DataValidationHelper {
    DataValidationConstraint createCustomConstraint(String str);

    DataValidationConstraint createDateConstraint(int i, String str, String str2, String str3);

    DataValidationConstraint createDecimalConstraint(int i, String str, String str2);

    DataValidationConstraint createExplicitListConstraint(String[] strArr);

    DataValidationConstraint createFormulaListConstraint(String str);

    DataValidationConstraint createIntegerConstraint(int i, String str, String str2);

    DataValidationConstraint createNumericConstraint(int i, int i2, String str, String str2);

    DataValidationConstraint createTextLengthConstraint(int i, String str, String str2);

    DataValidationConstraint createTimeConstraint(int i, String str, String str2);

    DataValidation createValidation(DataValidationConstraint dataValidationConstraint, CellRangeAddressList cellRangeAddressList);
}