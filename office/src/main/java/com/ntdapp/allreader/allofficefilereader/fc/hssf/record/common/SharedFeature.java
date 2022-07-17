

package com.ntdapp.allreader.allofficefilereader.fc.hssf.record.common;

import com.ntdapp.allreader.allofficefilereader.fc.util.LittleEndianOutput;

/**
 * Common Interface for all Shared Features
 */
public interface SharedFeature {
	public String toString();
	public void serialize(LittleEndianOutput out);
	public int getDataSize();
}