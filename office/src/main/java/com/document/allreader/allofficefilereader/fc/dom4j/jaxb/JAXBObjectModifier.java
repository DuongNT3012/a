

package com.document.allreader.allofficefilereader.fc.dom4j.jaxb;

import com.document.allreader.allofficefilereader.fc.dom4j.Element;

/**
 * JAXBObjectHandler implementations can be registered with the {@link
 * JAXBModifier} in order to change unmarshalled XML fragments.
 * 
 * @author Wonne Keysers (Realsoftware.be)
 */
public interface JAXBObjectModifier
{
    /**
     * Called when the {@link JAXBModifier}has finished parsing the xml path
     * the handler was registered for. The provided object is the unmarshalled
     * representation of the XML fragment. It can be casted to the appropriate
     * implementation class that is generated by the JAXB compiler. <br>
     * The modified JAXB element that returns from this method will be
     * marshalled by the {@link JAXBModifier}and put in the DOM4J tree.
     * 
     * @param jaxbElement
     *            the JAXB object to be modified
     * 
     * @return the modified JAXB object, or null when it must be removed.
     * 
     * @throws Exception
     *             possibly thrown by the implementation.
     */
    Element modifyObject(Element jaxbElement) throws Exception;
}

