package org.litepal.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: classes.dex */
public class LitePalContentHandler extends DefaultHandler {
    private LitePalAttr litePalAttr;

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        this.litePalAttr = LitePalAttr.getInstance();
        this.litePalAttr.getClassNames().clear();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("dbname".equalsIgnoreCase(localName)) {
            for (int i = 0; i < attributes.getLength(); i++) {
                if ("value".equalsIgnoreCase(attributes.getLocalName(i))) {
                    this.litePalAttr.setDbName(attributes.getValue(i).trim());
                }
            }
            return;
        }
        if ("version".equalsIgnoreCase(localName)) {
            for (int i2 = 0; i2 < attributes.getLength(); i2++) {
                if ("value".equalsIgnoreCase(attributes.getLocalName(i2))) {
                    this.litePalAttr.setVersion(Integer.parseInt(attributes.getValue(i2).trim()));
                }
            }
            return;
        }
        if ("mapping".equalsIgnoreCase(localName)) {
            for (int i3 = 0; i3 < attributes.getLength(); i3++) {
                if ("class".equalsIgnoreCase(attributes.getLocalName(i3))) {
                    this.litePalAttr.addClassName(attributes.getValue(i3).trim());
                }
            }
            return;
        }
        if ("cases".equalsIgnoreCase(localName)) {
            for (int i4 = 0; i4 < attributes.getLength(); i4++) {
                if ("value".equalsIgnoreCase(attributes.getLocalName(i4))) {
                    this.litePalAttr.setCases(attributes.getValue(i4).trim());
                }
            }
        }
    }
}
