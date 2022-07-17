

package com.ntdapp.allreader.allofficefilereader.fc.codec;

public interface BinaryEncoder extends Encoder {
    

    byte[] encode(byte[] source) throws EncoderException;
}  

