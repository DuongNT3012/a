

package com.ntdapp.allreader.allofficefilereader.fc.sl.usermodel;

import com.ntdapp.allreader.allofficefilereader.java.awt.geom.Rectangle2D;

public interface Shape
{
    public int getShapeType();

    public Rectangle2D getAnchor();

    public void setAnchor(Rectangle2D anchor);

    public void moveTo(float x, float y);

    public Shape getParent();
}
