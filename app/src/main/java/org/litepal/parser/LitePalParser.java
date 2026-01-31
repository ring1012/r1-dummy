package org.litepal.parser;

import android.content.res.AssetManager;
import android.content.res.Resources;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.litepal.LitePalApplication;
import org.litepal.exceptions.ParseConfigurationFileException;
import org.litepal.util.Const;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class LitePalParser {
    static final String ATTR_CLASS = "class";
    static final String ATTR_VALUE = "value";
    static final String NODE_CASES = "cases";
    static final String NODE_DB_NAME = "dbname";
    static final String NODE_LIST = "list";
    static final String NODE_MAPPING = "mapping";
    static final String NODE_VERSION = "version";
    private static LitePalParser parser;

    public static void parseLitePalConfiguration() throws SAXException, IOException {
        if (parser == null) {
            parser = new LitePalParser();
        }
        parser.useSAXParser();
    }

    void useSAXParser() throws SAXException, IOException {
        XMLReader xmlReader;
        LitePalContentHandler handler;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            xmlReader = factory.newSAXParser().getXMLReader();
            handler = new LitePalContentHandler();
        } catch (Resources.NotFoundException e) {
        } catch (IOException e2) {
        } catch (ParserConfigurationException e3) {
        } catch (SAXException e4) {
        }
        try {
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(getConfigInputStream()));
        } catch (Resources.NotFoundException e5) {
            throw new ParseConfigurationFileException(ParseConfigurationFileException.CAN_NOT_FIND_LITEPAL_FILE);
        } catch (IOException e6) {
            throw new ParseConfigurationFileException(ParseConfigurationFileException.IO_EXCEPTION);
        } catch (ParserConfigurationException e7) {
            throw new ParseConfigurationFileException(ParseConfigurationFileException.PARSE_CONFIG_FAILED);
        } catch (SAXException e8) {
            throw new ParseConfigurationFileException(ParseConfigurationFileException.FILE_FORMAT_IS_NOT_CORRECT);
        }
    }

    void usePullParse() throws XmlPullParserException, IOException {
        try {
            LitePalAttr litePalAttr = LitePalAttr.getInstance();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(getConfigInputStream(), "UTF-8");
            for (int eventType = xmlPullParser.getEventType(); eventType != 1; eventType = xmlPullParser.next()) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case 2:
                        if (NODE_DB_NAME.equals(nodeName)) {
                            String dbName = xmlPullParser.getAttributeValue("", ATTR_VALUE);
                            litePalAttr.setDbName(dbName);
                            break;
                        } else if (NODE_VERSION.equals(nodeName)) {
                            String version = xmlPullParser.getAttributeValue("", ATTR_VALUE);
                            litePalAttr.setVersion(Integer.parseInt(version));
                            break;
                        } else if (NODE_MAPPING.equals(nodeName)) {
                            String className = xmlPullParser.getAttributeValue("", ATTR_CLASS);
                            litePalAttr.addClassName(className);
                            break;
                        } else if (NODE_CASES.equals(nodeName)) {
                            String cases = xmlPullParser.getAttributeValue("", ATTR_VALUE);
                            litePalAttr.setCases(cases);
                            break;
                        } else {
                            break;
                        }
                }
            }
        } catch (IOException e) {
            throw new ParseConfigurationFileException(ParseConfigurationFileException.IO_EXCEPTION);
        } catch (XmlPullParserException e2) {
            throw new ParseConfigurationFileException(ParseConfigurationFileException.FILE_FORMAT_IS_NOT_CORRECT);
        }
    }

    private InputStream getConfigInputStream() throws IOException {
        AssetManager assetManager = LitePalApplication.getContext().getAssets();
        String[] fileNames = assetManager.list("");
        if (fileNames != null && fileNames.length > 0) {
            for (String fileName : fileNames) {
                if (Const.LitePal.CONFIGURATION_FILE_NAME.equalsIgnoreCase(fileName)) {
                    return assetManager.open(fileName, 3);
                }
            }
        }
        throw new ParseConfigurationFileException(ParseConfigurationFileException.CAN_NOT_FIND_LITEPAL_FILE);
    }
}
