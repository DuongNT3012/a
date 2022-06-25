
package com.document.allreader.allofficefilereader.fc.doc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.document.allreader.allofficefilereader.constant.MainConstant;
import com.document.allreader.allofficefilereader.constant.wp.WPModelConstant;
import com.document.allreader.allofficefilereader.simpletext.model.AttrManage;
import com.document.allreader.allofficefilereader.simpletext.model.IAttributeSet;
import com.document.allreader.allofficefilereader.simpletext.model.IDocument;
import com.document.allreader.allofficefilereader.simpletext.model.LeafElement;
import com.document.allreader.allofficefilereader.simpletext.model.ParagraphElement;
import com.document.allreader.allofficefilereader.simpletext.model.SectionElement;
import com.document.allreader.allofficefilereader.system.AbstractReader;
import com.document.allreader.allofficefilereader.system.IControl;
import com.document.allreader.allofficefilereader.wp.model.WPDocument;

public class TXTReader extends AbstractReader
{


    public TXTReader(IControl control, String filePath, String encoding)
    {
        this.control = control;
        this.filePath = filePath;
        this.encoding = encoding;
    }
    

    public boolean authenticate(String password)
    {
        if(encoding != null)
        {
            return true;
        }
        else
        {
            encoding = password;
            if(encoding != null)
            {
                try
                {  
                    control.actionEvent(MainConstant.HANDLER_MESSAGE_SUCCESS, getModel());
                    return true;
                }
                catch(Throwable e)
                {
                    control.getSysKit().getErrorKit().writerLog(e);
                }
            }  
        }
        
        return false;
    }
    
    
    public Object getModel() throws Exception
    {
        if (wpdoc != null)
        {
            return wpdoc;
        }
        wpdoc = new WPDocument();
        if(encoding != null)
        {
            readFile();
        }        
        return wpdoc;
    }

    
    
    public void readFile() throws Exception
    {

        SectionElement secElem = new SectionElement();

        IAttributeSet attr = secElem.getAttribute();

        AttrManage.instance().setPageWidth(attr, 11906);

        AttrManage.instance().setPageHeight(attr, 16838);

        AttrManage.instance().setPageMarginLeft(attr, 1800);

        AttrManage.instance().setPageMarginRight(attr, 1800);

        AttrManage.instance().setPageMarginTop(attr, 1440);

        AttrManage.instance().setPageMarginBottom(attr, 1440);;
        secElem.setStartOffset(offset);
        
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
        String line;
        while ((line = br.readLine()) != null || offset == 0)
        {
            if (abortReader)
            {
                break;
            }

            line = line == null ? "\n" : line.concat("\n");
            line = line.replace('\t', ' ');
            int len = line.length();
            if (len > 500)
            {
                int end = 200;
                int start = 0;
                while (end <= len)
                {
                    String str = line.substring(start, end).concat("\n");
                    
                    ParagraphElement paraElem = new ParagraphElement();
                    paraElem.setStartOffset(offset);
                    LeafElement leafElem = new LeafElement(str);
                    
                    leafElem.setStartOffset(offset);
                    offset += str.length();
                    leafElem.setEndOffset(offset);
                    paraElem.appendLeaf(leafElem);
                    paraElem.setEndOffset(offset);
                    wpdoc.appendParagraph(paraElem, WPModelConstant.MAIN);
                    if (end == len)
                    {
                        break;
                    }
                    start = end;
                    end += 100;
                    if (end > len)
                    {
                        end = len;
                    }
                }
            }
            else
            {
                ParagraphElement paraElem = new ParagraphElement();
                paraElem.setStartOffset(offset);
                LeafElement leafElem = new LeafElement(line);
                
                leafElem.setStartOffset(offset);
                offset += line.length();
                leafElem.setEndOffset(offset);
                paraElem.appendLeaf(leafElem);
                paraElem.setEndOffset(offset);
                wpdoc.appendParagraph(paraElem, WPModelConstant.MAIN);
            }
        }
        br.close();
        secElem.setEndOffset(offset);
        
        wpdoc.appendSection(secElem);      
    }
    
    
    
    public boolean searchContent(File file, String key) throws Exception
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;
        while ((line = br.readLine()) != null)
        {
            if (line.indexOf(key) > 0)
            {
                return true;
            }
        }
        return false;
    }
    
    
    
    public void dispose()
    {
        if (isReaderFinish())
        {
            wpdoc = null;
            filePath = null;
            control = null;
        }
    }
    

    private long offset;

    private String filePath;

    private String encoding;

    private IDocument wpdoc;
}
