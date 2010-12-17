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

import net.gslsrc.dmex.exercise.Problem;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Base for rendering problems to an XML SAX stream.
 *
 * @param <P> the type of problem being rendered
 *
 * @author Geoff Lewis
 */
public abstract class SAXProblemRenderer<P extends Problem>
    implements ProblemRenderer<P> {

    protected static final Attributes EMPTY_ATTRS = new AttributesImpl();

    protected SAXProblemRenderer() {}

    @Override
    public void render(Collection<P> problems, RenderContext context)
        throws RenderException {

        if (!(context instanceof XMLRenderContext)) {
            throw new RenderException("Unsupported render context " + context);
        }

        final XMLRenderContext xrc = (XMLRenderContext)context;

        InputSource in = new ProblemInputSource<P>(problems);
        XMLReader reader = new AbstractXMLReader() {
            @Override
            @SuppressWarnings("unchecked")
            public void parse(InputSource in) throws IOException, SAXException {
                if (in instanceof ProblemInputSource) {
                    parseProblems(getContentHandler(), xrc,
                                  ((ProblemInputSource<P>)in).getProblems());
                } else {
                    throw new SAXException("Unsupported InputSource " + in);
                }
            }
        };

        xrc.setSource(new SAXSource(reader, in));
    }

    private void parseProblems(ContentHandler handler,
            XMLRenderContext context, Collection<P> problems)
        throws SAXException {

        assert context != null;

        if (handler == null) {
            throw new SAXException("Missing ContentHandler");
        }

        handler.startDocument();

        String tag = context.getRootTag();
        if (tag == null) {
            tag = XMLRenderContext.TAG_WORKSHEET;
        }

        if (context.getTitle() != null) {
            startElement(handler, tag, XMLRenderContext.ATTR_TITLE,
                         context.getTitle());
        } else {
            startElement(handler, tag);
        }

        for (P problem : problems) {
            if (problem != null) {
                renderProblem(handler, context, problem);
            }
        }

        endElement(handler, tag);
        handler.endDocument();
    }

    protected abstract void renderProblem(ContentHandler handler,
           XMLRenderContext context, P problem) throws SAXException;

    protected void startElement(ContentHandler handler, String name)
        throws SAXException {

        startElement(handler, name, EMPTY_ATTRS);
    }

    protected void startElement(ContentHandler handler, String name,
            String... attrs) throws SAXException {

        startElement(handler, name, makeAttributes(attrs));
    }

    protected void startElement(ContentHandler handler, String name,
            Attributes attrs) throws SAXException {

        handler.startElement(null, name, name, attrs);
    }

    protected void endElement(ContentHandler handler, String name)
        throws SAXException {

        handler.endElement(null, name, name);
    }

    protected void element(ContentHandler handler, String name)
        throws SAXException {

        element(handler, name, null);
    }

    protected void element(ContentHandler handler, String name, String text)
        throws SAXException {

        startElement(handler, name);

        if (text != null) {
            characters(handler, text);
        }

        endElement(handler, name);
    }

    protected void characters(ContentHandler handler, String text)
        throws SAXException {

        if (text != null) {
            handler.characters(text.toCharArray(), 0, text.length());
        }
    }

    protected static Attributes makeAttributes(String... values) {
        if (values == null || values.length == 0) {
            return EMPTY_ATTRS;
        }

        if (values.length % 2 != 0) {
            System.err.println(
                    "Incomplete array of attribute name/value pairs "
                    + Arrays.toString(values));
        }

        AttributesImpl attrs = new AttributesImpl();

        for (int i = 0; i < values.length / 2; ++i) {
            String name = values[2 * i];
            String value = values[2 * i + 1];

            attrs.addAttribute(null, name, name, "CDATA", value);
        }

        return attrs;
    }

    protected static String[] append(String[] src, String name, String value) {
        if (src == null) {
            return new String[] { name, value };
        }

        String[] dest = new String[src.length + 2];
        System.arraycopy(src, 0, dest, 0, src.length);
        dest[src.length] = name;
        dest[src.length + 1] = value;

        return dest;
    }

    /**
     * Dummy input source that simply supplies the problems to be rendered to
     * the XMLReader.
     */
    private class ProblemInputSource<P> extends InputSource {
        private Collection<P> problems;

        ProblemInputSource(Collection<P> list) {
            // Make a local copy of the list of problems.
            problems = new LinkedList<P>(list);
        }

        Collection<P> getProblems() {
            return problems;
        }
    }
}
