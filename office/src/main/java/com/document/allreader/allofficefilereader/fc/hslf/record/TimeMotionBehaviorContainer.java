
package com.document.allreader.allofficefilereader.fc.hslf.record;

public class TimeMotionBehaviorContainer extends PositionDependentRecordContainer
{
    private byte[] _header;
    public static long RECORD_ID = 0xF12E;

    public long getRecordType()
    {
        return RECORD_ID;
    }

    protected TimeMotionBehaviorContainer(byte[] source, int start, int len)
    {

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);


        _children = Record.findChildRecords(source, start + 8, len - 8);
    }
}
