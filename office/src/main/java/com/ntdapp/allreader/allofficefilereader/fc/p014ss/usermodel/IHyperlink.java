package com.ntdapp.allreader.allofficefilereader.fc.p014ss.usermodel;

import com.ntdapp.allreader.allofficefilereader.fc.usermodel.Hyperlink;

/* renamed from: com.allreader.office.allofficefilereader.fc.ss.usermodel.IHyperlink */

public interface IHyperlink extends Hyperlink {
    int getFirstColumn();

    int getFirstRow();

    int getLastColumn();

    int getLastRow();

    void setFirstColumn(int i);

    void setFirstRow(int i);

    void setLastColumn(int i);

    void setLastRow(int i);
}
