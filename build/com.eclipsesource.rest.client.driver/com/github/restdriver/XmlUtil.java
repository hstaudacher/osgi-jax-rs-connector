/**
 * Copyright © 2010-2011 Nokia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.restdriver;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.github.restdriver.exception.RuntimeXmlParseException;

public final class XmlUtil {

    private XmlUtil() {
    }

    private static final int PARSE_ERROR_EXCERPT_LENGTH = 16;

    private static Element throwRuntimeXmlParseException(String xml, Exception e) {
        throw new RuntimeXmlParseException("Can't parse XML.  Bad content >> " + xml.substring(0, PARSE_ERROR_EXCERPT_LENGTH) + "...", e);
    }

    /**
     * Converts the given string to an XML element.
     *
     * @param xml The XML string to be converted
     * @return The converted element
     */
    public static Element asXml(String xml) {

        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes("UTF-8"))).getDocumentElement();

        } catch (IOException e) {
            return throwRuntimeXmlParseException(xml, e);

        } catch (SAXException e) {
            return throwRuntimeXmlParseException(xml, e);

        } catch (ParserConfigurationException e) {
            return throwRuntimeXmlParseException(xml, e);
        }

    }

}
