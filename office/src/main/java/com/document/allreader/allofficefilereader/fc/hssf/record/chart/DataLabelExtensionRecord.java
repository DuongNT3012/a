

package com.document.allreader.allofficefilereader.fc.hssf.record.chart;


import com.document.allreader.allofficefilereader.fc.hssf.record.RecordInputStream;
import com.document.allreader.allofficefilereader.fc.hssf.record.StandardRecord;
import com.document.allreader.allofficefilereader.fc.util.BitField;
import com.document.allreader.allofficefilereader.fc.util.BitFieldFactory;
import com.document.allreader.allofficefilereader.fc.util.HexDump;
import com.document.allreader.allofficefilereader.fc.util.LittleEndianOutput;
import com.document.allreader.allofficefilereader.fc.util.StringUtil;


/**
 * DATALABEXT - Chart Data Label Extension (0x086A) <br/>
 * 
 * @author Patrick Cheng
 */
public final class DataLabelExtensionRecord extends StandardRecord {
	public static final short sid = 0x086B;
	
    private static final BitField showSeriesName          = BitFieldFactory.getInstance(0x0001);
    private static final BitField showCategoryName        = BitFieldFactory.getInstance(0x0002);
    private static final BitField showValue               = BitFieldFactory.getInstance(0x0004);
    private static final BitField showPercent             = BitFieldFactory.getInstance(0x0008);
    private static final BitField showBubbleSizes         = BitFieldFactory.getInstance(0x0010);
    		
	private int rt;
	private int grbitFrt;
	private byte[] unused = new byte[8];
//	Option flags for chart data labels
//	The grbit  field contains the following data label option flags:
//
//		Bits	Mask	Flag Name		Contents
//		0		0001h	fSeriesName		=1 if the data labels contain the series name 
//		 								=0 otherwise
//		1		0002h	fCategoryName	=1 if the data labels contain the category name (x-value)
//		 								=0 otherwise 
//		2		0004h	fValue			=1 if the data labels contain the y-value
//		 								=0 otherwise 
//		3		0008h	fPercent		=1 if the data labels contain a percentage
//										=0 otherwise
//		4		0010h	fBubbleSizes	=1 if the data labels contain bubble size
//		 								=0 otherwise
//		15-5	FFE0h	(unused)		Reserved; must be zero
		 

	private short grbit;
	//Count of characters in the separator string
	private short cchSep;
	//Separator string for use in chart data labels.
	//(See section titled ‘Unicode Strings in Biff8 ’ for more information about Unicode encodings.) 
	private String rgchSep;
	
	public DataLabelExtensionRecord(RecordInputStream in) {
		rt = in.readShort();
		grbitFrt = in.readShort();
		in.readFully(unused);
		
		grbit = in.readShort();
		cchSep = in.readShort();
		
		byte[] datas = new byte[in.available()];
		in.readFully(datas);
		
		rgchSep = StringUtil.getFromUnicodeLE(datas);
		
	}
	
	@Override
	protected int getDataSize() {
		return 2 + 2 + 8;
	}

	@Override
	public short getSid() {
		return sid;
	}

	/**
     * 
     * @return
     */
    public boolean isShowSeriesName()
    {
        return showSeriesName.isSet(grbit);
    }
    
    /**
     * 
     * @return
     */
    public boolean isShowCategoryName()
    {
        return showCategoryName.isSet(grbit);
    }
    
    /**
     * 
     * @return
     */
    public boolean isShowValue()
    {
        return showValue.isSet(grbit);
    }
    
    public boolean isShowPercent()
    {
    	return showPercent.isSet(grbit);
    }
    
    public boolean isShowBubbleSizes()
    {
    	return  showBubbleSizes.isSet(grbit);
    }
    
    public String getSeparator()
    {
    	return rgchSep;
    }
    
	@Override
	protected void serialize(LittleEndianOutput out) {
		out.writeShort(rt);
		out.writeShort(grbitFrt);
		out.write(unused);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("[DATALABEXT]\n");
		buffer.append("    .rt      =").append(HexDump.shortToHex(rt)).append('\n');
		buffer.append("    .grbitFrt=").append(HexDump.shortToHex(grbitFrt)).append('\n');
		buffer.append("    .unused  =").append(HexDump.toHex(unused)).append('\n');

		buffer.append("[/DATALABEXT]\n");
		return buffer.toString();
	}
}
