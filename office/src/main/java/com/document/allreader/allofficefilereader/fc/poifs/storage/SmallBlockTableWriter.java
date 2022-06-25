

        

package com.document.allreader.allofficefilereader.fc.poifs.storage;



import java.util.*;

import com.document.allreader.allofficefilereader.fc.poifs.common.POIFSBigBlockSize;
import com.document.allreader.allofficefilereader.fc.poifs.common.POIFSConstants;
import com.document.allreader.allofficefilereader.fc.poifs.filesystem.BATManaged;
import com.document.allreader.allofficefilereader.fc.poifs.filesystem.POIFSDocument;
import com.document.allreader.allofficefilereader.fc.poifs.property.RootProperty;

import java.io.*;

/**
 * This class implements storage for writing the small blocks used by
 * small documents.
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 */

public class SmallBlockTableWriter
    implements BlockWritable, BATManaged
{
    private BlockAllocationTableWriter _sbat;
    private List                       _small_blocks;
    private int                        _big_block_count;
    private RootProperty               _root;

    /**
     * Creates new SmallBlockTable
     *
     * @param documents a List of POIFSDocument instances
     * @param root the Filesystem's root property
     */

    public SmallBlockTableWriter(final POIFSBigBlockSize bigBlockSize,
                                 final List documents,
                                 final RootProperty root)
    {
        _sbat         = new BlockAllocationTableWriter(bigBlockSize);
        _small_blocks = new ArrayList();
        _root         = root;
        Iterator iter = documents.iterator();

        while (iter.hasNext())
        {
            POIFSDocument   doc    = ( POIFSDocument ) iter.next();
            BlockWritable[] blocks = doc.getSmallBlocks();

            if (blocks.length != 0)
            {
                doc.setStartBlock(_sbat.allocateSpace(blocks.length));
                for (int j = 0; j < blocks.length; j++)
                {
                    _small_blocks.add(blocks[ j ]);
                }
            } else {
            	doc.setStartBlock(POIFSConstants.END_OF_CHAIN);
            }
        }
        _sbat.simpleCreateBlocks();
        _root.setSize(_small_blocks.size());
        _big_block_count = SmallDocumentBlock.fill(bigBlockSize,_small_blocks);
    }

    /**
     * Get the number of SBAT blocks
     *
     * @return number of SBAT big blocks
     */
    
    public int getSBATBlockCount()
    {
	return (_big_block_count + 15) / 16;
    }

    /**
     * Get the SBAT
     *
     * @return the Small Block Allocation Table
     */

    public BlockAllocationTableWriter getSBAT()
    {
        return _sbat;
    }

    /* ********** START implementation of BATManaged ********** */

    /**
     * Return the number of BigBlock's this instance uses
     *
     * @return count of BigBlock instances
     */

    public int countBlocks()
    {
        return _big_block_count;
    }

    /**
     * Set the start block for this instance
     *
     * @param start_block
     */

    public void setStartBlock(int start_block)
    {
        _root.setStartBlock(start_block);
    }

    /* **********  END  implementation of BATManaged ********** */
    /* ********** START implementation of BlockWritable ********** */

    /**
     * Write the storage to an OutputStream
     *
     * @param stream the OutputStream to which the stored data should
     *               be written
     *
     * @exception IOException on problems writing to the specified
     *            stream
     */

    public void writeBlocks(final OutputStream stream)
        throws IOException
    {
        Iterator iter = _small_blocks.iterator();

        while (iter.hasNext())
        {
            (( BlockWritable ) iter.next()).writeBlocks(stream);
        }
    }

    /* **********  END  implementation of BlockWritable ********** */
}
