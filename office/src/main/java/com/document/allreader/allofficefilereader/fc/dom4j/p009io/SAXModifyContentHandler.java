package com.document.allreader.allofficefilereader.fc.dom4j.p009io;

import com.document.allreader.allofficefilereader.fc.dom4j.DocumentFactory;
import com.document.allreader.allofficefilereader.fc.dom4j.ElementHandler;
import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/* renamed from: com.allreader.office.allofficefilereader.fc.dom4j.io.SAXModifyContentHandler */

class SAXModifyContentHandler extends SAXContentHandler {
    private XMLWriter xmlWriter;

    public SAXModifyContentHandler() {
    }

    public SAXModifyContentHandler(DocumentFactory documentFactory) {
        super(documentFactory);
    }

    public SAXModifyContentHandler(DocumentFactory documentFactory, ElementHandler elementHandler) {
        super(documentFactory, elementHandler);
    }

    public SAXModifyContentHandler(DocumentFactory documentFactory, ElementHandler elementHandler, ElementStack elementStack) {
        super(documentFactory, elementHandler, elementStack);
    }

    public void setXMLWriter(XMLWriter xMLWriter) {
        this.xmlWriter = xMLWriter;
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
        XMLWriter xMLWriter;
        super.startCDATA();
        if (!activeHandlers() && (xMLWriter = this.xmlWriter) != null) {
            xMLWriter.startCDATA();
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.ext.LexicalHandler
    public void startDTD(String str, String str2, String str3) throws SAXException {
        super.startDTD(str, str2, str3);
        XMLWriter xMLWriter = this.xmlWriter;
        if (xMLWriter != null) {
            xMLWriter.startDTD(str, str2, str3);
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
        super.endDTD();
        XMLWriter xMLWriter = this.xmlWriter;
        if (xMLWriter != null) {
            xMLWriter.endDTD();
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.ext.LexicalHandler
    public void comment(char[] cArr, int i, int i2) throws SAXException {
        XMLWriter xMLWriter;
        super.comment(cArr, i, i2);
        if (!activeHandlers() && (xMLWriter = this.xmlWriter) != null) {
            xMLWriter.comment(cArr, i, i2);
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.ext.LexicalHandler
    public void startEntity(String str) throws SAXException {
        super.startEntity(str);
        XMLWriter xMLWriter = this.xmlWriter;
        if (xMLWriter != null) {
            xMLWriter.startEntity(str);
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        XMLWriter xMLWriter;
        super.endCDATA();
        if (!activeHandlers() && (xMLWriter = this.xmlWriter) != null) {
            xMLWriter.endCDATA();
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.ext.LexicalHandler
    public void endEntity(String str) throws SAXException {
        super.endEntity(str);
        XMLWriter xMLWriter = this.xmlWriter;
        if (xMLWriter != null) {
            xMLWriter.endEntity(str);
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.helpers.DefaultHandler, org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String str, String str2, String str3, String str4) throws SAXException {
        XMLWriter xMLWriter;
        super.unparsedEntityDecl(str, str2, str3, str4);
        if (!activeHandlers() && (xMLWriter = this.xmlWriter) != null) {
            xMLWriter.unparsedEntityDecl(str, str2, str3, str4);
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.helpers.DefaultHandler, org.xml.sax.DTDHandler
    public void notationDecl(String str, String str2, String str3) throws SAXException {
        super.notationDecl(str, str2, str3);
        XMLWriter xMLWriter = this.xmlWriter;
        if (xMLWriter != null) {
            xMLWriter.notationDecl(str, str2, str3);
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        XMLWriter xMLWriter;
        super.startElement(str, str2, str3, attributes);
        if (!activeHandlers() && (xMLWriter = this.xmlWriter) != null) {
            xMLWriter.startElement(str, str2, str3, attributes);
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        super.startDocument();
        XMLWriter xMLWriter = this.xmlWriter;
        if (xMLWriter != null) {
            xMLWriter.startDocument();
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] cArr, int i, int i2) throws SAXException {
        XMLWriter xMLWriter;
        super.ignorableWhitespace(cArr, i, i2);
        if (!activeHandlers() && (xMLWriter = this.xmlWriter) != null) {
            xMLWriter.ignorableWhitespace(cArr, i, i2);
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String str, String str2) throws SAXException {
        XMLWriter xMLWriter;
        super.processingInstruction(str, str2);
        if (!activeHandlers() && (xMLWriter = this.xmlWriter) != null) {
            xMLWriter.processingInstruction(str, str2);
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        super.setDocumentLocator(locator);
        XMLWriter xMLWriter = this.xmlWriter;
        if (xMLWriter != null) {
            xMLWriter.setDocumentLocator(locator);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void skippedEntity(String str) throws SAXException {
        XMLWriter xMLWriter;
        super.skippedEntity(str);
        if (!activeHandlers() && (xMLWriter = this.xmlWriter) != null) {
            xMLWriter.skippedEntity(str);
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        super.endDocument();
        XMLWriter xMLWriter = this.xmlWriter;
        if (xMLWriter != null) {
            xMLWriter.endDocument();
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startPrefixMapping(String str, String str2) throws SAXException {
        super.startPrefixMapping(str, str2);
        XMLWriter xMLWriter = this.xmlWriter;
        if (xMLWriter != null) {
            xMLWriter.startPrefixMapping(str, str2);
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String str, String str2, String str3) throws SAXException {
        XMLWriter xMLWriter;
        ElementHandler handler = getElementStack().getDispatchHandler().getHandler(getElementStack().getPath());
        super.endElement(str, str2, str3);
        if (!activeHandlers() && (xMLWriter = this.xmlWriter) != null) {
            if (handler == null) {
                xMLWriter.endElement(str, str2, str3);
            } else if (handler instanceof SAXModifyElementHandler) {
                try {
                    this.xmlWriter.write(((SAXModifyElementHandler) handler).getModifiedElement());
                } catch (IOException e) {
                    throw new SAXModifyException(e);
                }
            }
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endPrefixMapping(String str) throws SAXException {
        super.endPrefixMapping(str);
        XMLWriter xMLWriter = this.xmlWriter;
        if (xMLWriter != null) {
            xMLWriter.endPrefixMapping(str);
        }
    }

    @Override // fc4j.p009io.SAXContentHandler, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] cArr, int i, int i2) throws SAXException {
        XMLWriter xMLWriter;
        super.characters(cArr, i, i2);
        if (!activeHandlers() && (xMLWriter = this.xmlWriter) != null) {
            xMLWriter.characters(cArr, i, i2);
        }
    }

    protected XMLWriter getXMLWriter() {
        return this.xmlWriter;
    }

    private boolean activeHandlers() {
        return getElementStack().getDispatchHandler().getActiveHandlerCount() > 0;
    }
}
