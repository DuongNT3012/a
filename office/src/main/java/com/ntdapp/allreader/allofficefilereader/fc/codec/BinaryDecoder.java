

package com.ntdapp.allreader.allofficefilereader.fc.codec;

public interface BinaryDecoder extends Decoder {

    byte[] decode(byte[] source) throws DecoderException;
}  

