

package com.document.allreader.allofficefilereader.fc.hslf.record;

import com.document.allreader.allofficefilereader.fc.util.LittleEndian;


/**
 * Tne atom that holds the seed info used by a ExObjList
 *
 * @author Nick Burch
 */

public class ExObjListAtom extends RecordAtom
{
    /**
     * Record header.
     */
    private byte[] _header;

    /**
     * Record data.
     */
    private byte[] _data;

    /**
     * Constructs a brand new link related atom record.
     */
    protected ExObjListAtom()
    {
        _header = new byte[8];
        _data = new byte[4];

        LittleEndian.putShort(_header, 2, (short)getRecordType());
        LittleEndian.putInt(_header, 4, _data.length);

        // It is fine for the other values to be zero
    }

    /**
     * Constructs the link related atom record from its
     *  source data.
     *
     * @param source the source data as a byte array.
     * @param start the start offset into the byte array.
     * @param len the length of the slice in the byte array.
     */
    protected ExObjListAtom(byte[] source, int start, int len)
    {
        // Get the header.
        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);

        // Get the record data.
        _data = new byte[len - 8];
        System.arraycopy(source, start + 8, _data, 0, len - 8);

        // Must be at least 4 bytes long
        if (_data.length < 4)
        {
            throw new IllegalArgumentException(
                "The length of the data for a ExObjListAtom must be at least 4 bytes, but was only "
                    + _data.length);
        }
    }

    /**
     * Gets the object ID seed, which will be used as the unique
     *  OLE identifier for the next OLE object added
     * @return the object ID seed
     */
    public long getObjectIDSeed()
    {
        return LittleEndian.getUInt(_data, 0);
    }

    /**
     * Sets the object ID seed
     * @param seed the new ID seed
     */
    public void setObjectIDSeed(int seed)
    {
        LittleEndian.putInt(_data, 0, seed);
    }

    /**
     * Gets the record type.
     * @return the record type.
     */
    public long getRecordType()
    {
        return RecordTypes.ExObjListAtom.typeID;
    }
    
    
    public void dispose()
    {
        _header = null;
        _data = null;
    }

}
