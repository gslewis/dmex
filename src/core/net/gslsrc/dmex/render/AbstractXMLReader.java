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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Base for implementations of the SAX {@code XMLReader}.  Implementations
 * must provide the {@link #parse(InputSource)} method.
 * <p>
 * Based on {@code AbstractObjectReader} from FOP examples.
 *
 * @author Geoff Lewis
 */
public abstract class AbstractXMLReader implements XMLReader {

    private Map<String, Boolean> features = new HashMap<String, Boolean>();

    private ContentHandler contentHandler;
    private ErrorHandler errorHandler;

    public AbstractXMLReader() {}

    @Override
    public abstract void parse(InputSource input)
        throws IOException, SAXException;

    @Override
    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    @Override
    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public DTDHandler getDTDHandler() {
        return null;
    }

    @Override
    public void setDTDHandler(DTDHandler handler) {}

    @Override
    public EntityResolver getEntityResolver() {
        return null;
    }

    @Override
    public void setEntityResolver(EntityResolver resolver) {}

    @Override
    public Object getProperty(String name) {
        return null;
    }

    @Override
    public void setProperty(String name, Object value) {}

    @Override
    public boolean getFeature(String name) {
        return features.get(name).booleanValue();
    }

    @Override
    public void setFeature(String name, boolean value) {
        features.put(name, Boolean.valueOf(value));
    }

    @Override
    public void parse(String systemId) throws IOException, SAXException {
        throw new SAXException(getClass().getName()
                + " cannot be used with system identifiers (URIs)");
    }
}
