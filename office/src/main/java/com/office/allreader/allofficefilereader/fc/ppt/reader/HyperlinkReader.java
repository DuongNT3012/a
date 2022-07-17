/*
 * 文件名称:           HyperlinkReader.java
 *  
 * 编译器:             android2.2
 * 时间:               下午1:23:18
 */
package com.office.allreader.allofficefilereader.fc.ppt.reader;

import java.util.Hashtable;
import java.util.Map;

import com.office.allreader.allofficefilereader.common.hyperlink.Hyperlink;
import com.office.allreader.allofficefilereader.fc.openxml4j.opc.PackagePart;
import com.office.allreader.allofficefilereader.fc.openxml4j.opc.PackageRelationship;
import com.office.allreader.allofficefilereader.fc.openxml4j.opc.PackageRelationshipCollection;
import com.office.allreader.allofficefilereader.fc.openxml4j.opc.PackageRelationshipTypes;
import com.office.allreader.allofficefilereader.system.IControl;

/**
 * 解析  hyperlink
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-3-6
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class HyperlinkReader
{
    private static HyperlinkReader hyperlink = new HyperlinkReader();
    

    public static HyperlinkReader instance()
    {
        return hyperlink;
    }
    
    /**
     * 获取hyperlink
     */
    public void getHyperlinkList(IControl control, PackagePart packagePart) throws Exception
    {
        link = new Hashtable<String, Integer>();
        PackageRelationshipCollection hyperlinkRelCollection =
            packagePart.getRelationshipsByType(PackageRelationshipTypes.HYPERLINK_PART);
        for (PackageRelationship hyperlinkRel : hyperlinkRelCollection)
        {
            String id = hyperlinkRel.getId();
            if (getLinkIndex(id) < 0)
            {
                link.put(id, control.getSysKit().getHyperlinkManage().addHyperlink(hyperlinkRel.getTargetURI().toString(), Hyperlink.LINK_URL));
            }
        }
    }
    
    /**
     * get hyperlink index
     * @param id
     * @return
     */
    public int getLinkIndex(String id)
    {
        if (link != null && link.size() > 0)
        {
            Integer index = link.get(id);
            if (index != null)
            {
                return index;
            }
        }
        return -1;
    }
    

    public void disposs()
    {
        if (link != null)
        {
            link.clear();
            link = null;
        }
    }


    private Map<String, Integer> link;
}