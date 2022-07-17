

package com.ntdapp.allreader.allofficefilereader.fc.hssf.eventmodel;

import com.ntdapp.allreader.allofficefilereader.fc.hssf.record.Record;


public interface ERFListener
{
    public boolean processRecord(Record rec);
}
