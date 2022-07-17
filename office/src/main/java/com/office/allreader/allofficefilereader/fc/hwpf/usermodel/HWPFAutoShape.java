
package com.office.allreader.allofficefilereader.fc.hwpf.usermodel;

import com.office.allreader.allofficefilereader.fc.ShapeKit;
import com.office.allreader.allofficefilereader.fc.ddf.EscherContainerRecord;


public class HWPFAutoShape extends HWPFShape
{
    public HWPFAutoShape(EscherContainerRecord escherRecord, HWPFShape parent)
    {
        super(escherRecord, parent);
    }
    
    public String getShapeName()
    {
    	return ShapeKit.getShapeName(escherContainer);
    }
}
