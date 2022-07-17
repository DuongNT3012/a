


package com.ntdapp.allreader.allofficefilereader.fc.hssf.eventusermodel;


import com.ntdapp.allreader.allofficefilereader.fc.hssf.record.Record;



public abstract class AbortableHSSFListener implements HSSFListener
{

	public void processRecord(Record record)
	{
	}


    public abstract short abortableProcessRecord(Record record) throws HSSFUserException;
}
