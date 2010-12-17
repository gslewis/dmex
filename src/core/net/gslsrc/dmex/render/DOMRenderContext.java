/*
Copyright (c) 2010 Geoff Lewis <gsl@gslsrc.net>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package net.gslsrc.dmex.render;

import java.util.Arrays;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;

/**
 * Context for rendering problems to an XML Document.  The context provides
 * the document to which renderers append elements.
 * <p>
 * Note that this context supplies it's own {@link #getSource source} using
 * the document, so {@link #setSource} is disabled.
 *
 * @author Geoff Lewis
 */
public class DOMRenderContext extends XMLRenderContext {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static DocumentBuilder parser;

    private Document doc;

    public DOMRenderContext() {}

    @Override
    public Source getSource() {
        if (doc == null) {
            return null;
        }

        return new DOMSource(doc);
    }

    @Override
    public XMLRenderContext setSource(Source source) {
        throw new UnsupportedOperationException(
                "DOMRenderContext provides its own Source");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (getRootTag() != null) {
            sb.append(getRootTag());
        }

        if (getTitle() != null) {
            if (sb.length() > 0) {
                sb.append(":");
            }
            sb.append(getTitle());
        }

        String s = "DOMRenderContext";

        if (sb.length() > 0) {
            s += "<" + sb + ">";
        }

        return s;
    }

    public Document getDocument() {
        return doc;
    }

    public Document init() {
        return init(EMPTY_STRING_ARRAY);
    }

    public Document init(String... attrs) {
        if (doc != null) {
            throw new IllegalStateException(
                    "Context has already been initialized");
        }

        if (getRootTag() == null) {
            throw new NullPointerException("Document element name is null");
        }

        doc = newDocument(getRootTag());

        if (getTitle() != null) {
            doc.getDocumentElement().setAttribute(ATTR_TITLE, getTitle());
        }

        // Report problem with attrs and ignore the trailing value-less name.
        if (attrs.length % 2 != 0) {
            System.err.println(
                    "Incomplete array of attribute name/value pairs "
                    + Arrays.toString(attrs));
        }

        if (attrs != null && attrs.length > 0) {
            for (int i = 0; i < attrs.length / 2; ++i) {
                String name = attrs[2 * i];
                String value = attrs[2 * i + 1];

                doc.getDocumentElement().setAttribute(name, value);
            }
        }

        return doc;
    }

    private static Document newDocument(String title) {
        assert title != null;

        Document doc = getParser().newDocument();

        doc.appendChild(doc.createElement(title));

        return doc;
    }

    private static synchronized DocumentBuilder getParser() {
        if (parser == null) {
            try {
                parser = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();
            } catch (ParserConfigurationException pce) {
                throw new Error("Failed to get XML parser", pce);
            }
        }

        return parser;
    }
}
