

package com.office.allreader.allofficefilereader.fc.hwpf.model;

import com.office.allreader.allofficefilereader.fc.util.Internal;

@ Internal
public interface CharIndexTranslator
{

    int getByteIndex(int charPos);


    int getCharIndex(int bytePos);


    int getCharIndex(int bytePos, int startCP);


    boolean isIndexInTable(int bytePos);


    public int lookIndexForward(int bytePos);

    public int lookIndexBackward(int bytePos);

}
