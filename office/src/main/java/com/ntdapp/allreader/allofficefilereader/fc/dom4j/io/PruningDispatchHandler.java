

package com.ntdapp.allreader.allofficefilereader.fc.dom4j.io;

import com.ntdapp.allreader.allofficefilereader.fc.dom4j.ElementPath;


class PruningDispatchHandler extends DispatchHandler
{
    public void onEnd(ElementPath elementPath)
    {
        super.onEnd(elementPath);

        if (getActiveHandlerCount() == 0)
        {
            elementPath.getCurrent().detach();
        }
    }
}

