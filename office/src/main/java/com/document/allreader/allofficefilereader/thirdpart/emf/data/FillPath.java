

package com.document.allreader.allofficefilereader.thirdpart.emf.data;

import java.io.IOException;

import com.document.allreader.allofficefilereader.java.awt.Rectangle;
import com.document.allreader.allofficefilereader.java.awt.geom.GeneralPath;
import com.document.allreader.allofficefilereader.thirdpart.emf.EMFInputStream;
import com.document.allreader.allofficefilereader.thirdpart.emf.EMFRenderer;
import com.document.allreader.allofficefilereader.thirdpart.emf.EMFTag;

/**
 * FillPath TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: FillPath.java 10367 2007-01-22 19:26:48Z duns $
 */
public class FillPath extends EMFTag
{

    private Rectangle bounds;

    public FillPath()
    {
        super(62, 1);
    }

    public FillPath(Rectangle bounds)
    {
        this();
        this.bounds = bounds;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new FillPath(emf.readRECTL());
    }

    public String toString()
    {
        return super.toString() + "\n  bounds: " + bounds;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        GeneralPath currentPath = renderer.getPath();
        // fills the current path
        if (currentPath != null)
        {
            renderer.fillShape(currentPath);
            renderer.setPath(null);
        }
    }
}
