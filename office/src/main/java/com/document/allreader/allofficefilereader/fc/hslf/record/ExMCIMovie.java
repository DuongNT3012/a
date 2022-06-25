

package com.document.allreader.allofficefilereader.fc.hslf.record;

import com.document.allreader.allofficefilereader.fc.util.LittleEndian;
import com.document.allreader.allofficefilereader.fc.util.POILogger;


/**
 * A container record that specifies information about a movie stored externally.
 *
 * @author Yegor Kozlov
 */
public class ExMCIMovie extends RecordContainer { // TODO - instantiable superclass
    private byte[] _header;

    //An ExVideoContainer record that specifies information about the MCI movie
    private ExVideoContainer exVideo;

    /**
     * Set things up, and find our more interesting children
     */
    protected ExMCIMovie(byte[] source, int start, int len) {

        _header = new byte[8];
        System.arraycopy(source, start, _header, 0, 8);


        _children = Record.findChildRecords(source, start + 8, len - 8);
        findInterestingChildren();
    }

    /**
     * Create a new ExMCIMovie, with blank fields
     */
    public ExMCIMovie() {
        _header = new byte[8];
        // Setup our header block
        _header[0] = 0x0f; // We are a container record
        LittleEndian.putShort(_header, 2, (short) getRecordType());

        exVideo = new ExVideoContainer();
        _children = new Record[]{exVideo};

    }

    /**
     * Go through our child records, picking out the ones that are
     * interesting, and saving those for use by the easy helper
     * methods.
     */
    private void findInterestingChildren() {

        // First child should be the ExVideoContainer
        if (_children[0] instanceof ExVideoContainer) {
            exVideo = (ExVideoContainer) _children[0];
        } else {
            logger.log(POILogger.ERROR, "First child record wasn't a ExVideoContainer, was of type " + _children[0].getRecordType());
        }
    }

    /**
     * We are of type 4103
     */
    public long getRecordType() {
        return RecordTypes.ExMCIMovie.typeID;
    }

    /**
     * Returns the ExVideoContainer that specifies information about the MCI movie
     */
    public ExVideoContainer getExVideo() {
        return exVideo; }

    
    public void dispose()
    {
        super.dispose();
        _header = null;
        if (exVideo != null)
        {
            exVideo.dispose();
            exVideo = null;
        }
    }


}
