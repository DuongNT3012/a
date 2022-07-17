

package com.ntdapp.allreader.allofficefilereader.fc.hssf.usermodel;


import com.ntdapp.allreader.allofficefilereader.fc.hssf.record.CFRuleRecord;
import com.ntdapp.allreader.allofficefilereader.fc.hssf.record.cf.PatternFormatting;


/**
 * High level representation for Conditional Formatting settings
 * 
 * @author Dmitriy Kumshayev
 *
 */
public class HSSFPatternFormatting implements com.ntdapp.allreader.allofficefilereader.fc.ss.usermodel.PatternFormatting
{
	private final CFRuleRecord cfRuleRecord;
	private final PatternFormatting patternFormatting;
	
	protected HSSFPatternFormatting(CFRuleRecord cfRuleRecord)
	{
		this.cfRuleRecord = cfRuleRecord; 
		this.patternFormatting = cfRuleRecord.getPatternFormatting();
	}

	protected PatternFormatting getPatternFormattingBlock()
	{
		return patternFormatting;
	}

	/**
	 * @see com.ntdapp.allreader.allofficefilereader.fc.hssf.record.cf.PatternFormatting#getFillBackgroundColor()
	 */
	public short getFillBackgroundColor()
	{
		return (short)patternFormatting.getFillBackgroundColor();
	}

	/**
	 * @see com.ntdapp.allreader.allofficefilereader.fc.hssf.record.cf.PatternFormatting#getFillForegroundColor()
	 */
	public short getFillForegroundColor()
	{
		return (short)patternFormatting.getFillForegroundColor();
	}

	/**
	 * @see com.ntdapp.allreader.allofficefilereader.fc.hssf.record.cf.PatternFormatting#getFillPattern()
	 */
	public short getFillPattern()
	{
		return (short)patternFormatting.getFillPattern();
	}

	/**
	 * @param bg
	 * @see com.ntdapp.allreader.allofficefilereader.fc.hssf.record.cf.PatternFormatting#setFillBackgroundColor(int)
	 */
	public void setFillBackgroundColor(short bg)
	{
		patternFormatting.setFillBackgroundColor(bg);
		if( bg != 0)
		{
			cfRuleRecord.setPatternBackgroundColorModified(true);
		}
	}

	/**
	 * @param fg
	 * @see com.ntdapp.allreader.allofficefilereader.fc.hssf.record.cf.PatternFormatting#setFillForegroundColor(int)
	 */
	public void setFillForegroundColor(short fg)
	{
		patternFormatting.setFillForegroundColor(fg);
		if( fg != 0)
		{
			cfRuleRecord.setPatternColorModified(true);
		}
	}

	/**
	 * @param fp
	 * @see com.ntdapp.allreader.allofficefilereader.fc.hssf.record.cf.PatternFormatting#setFillPattern(int)
	 */
	public void setFillPattern(short fp)
	{
		patternFormatting.setFillPattern(fp);
		if( fp != 0)
		{
			cfRuleRecord.setPatternStyleModified(true);
		}
	}
}
