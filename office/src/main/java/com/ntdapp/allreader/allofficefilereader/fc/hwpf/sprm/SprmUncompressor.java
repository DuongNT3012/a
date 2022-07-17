

package com.ntdapp.allreader.allofficefilereader.fc.hwpf.sprm;

import com.ntdapp.allreader.allofficefilereader.fc.util.Internal;

@ Internal
public abstract class SprmUncompressor
{
    protected SprmUncompressor()
    {
    }


    public static boolean getFlag(int x)
    {
        if (x != 0)
        {
            return true;
        }
        return false;
    }

}
