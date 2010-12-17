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

import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.Source;

/**
 * Render context XML content.  The context provides a {@code Source} that
 * supplies the rendered XML.
 * <p>
 * The default renderer that populates this context is
 * {@link SAXProblemRenderer}.
 *
 * @author Geoff Lewis
 */
public class XMLRenderContext implements RenderContext {

    public static final String TAG_WORKSHEET = "WorkSheet";
    public static final String TAG_PROBLEM = "problem";
    public static final String TAG_PROMPT = "prompt";

    public static final String ATTR_TITLE = "title";
    public static final String ATTR_ANSWER = "answer";

    private XMLRenderContext parent;

    private String tag;
    private String title;

    private Source source;

    private Map<String, Object> attributes;

    public XMLRenderContext() {}

    public XMLRenderContext(XMLRenderContext parent) {
        this.parent = parent;
    }

    public Source getSource() {
        return source;
    }

    public XMLRenderContext setSource(Source source) {
        this.source = source;

        return this;
    }

    public XMLRenderContext getParent() {
        return parent;
    }

    public XMLRenderContext setParent(XMLRenderContext parent) {
        this.parent = parent;

        return this;
    }

    public String getRootTag() {
        if (tag == null && parent != null) {
            return parent.getRootTag();
        }

        return tag != null ? tag : TAG_WORKSHEET;
    }

    public XMLRenderContext setRootTag(String newTag) {
        if (newTag == null) {
            tag = null;
        } else {
            String s = newTag.trim();
            if (s.isEmpty()) {
                throw new IllegalArgumentException(
                        "Document element name cannot be empty");
            }

            tag = s;
        }

        return this;
    }

    public String getTitle() {
        if (title == null && parent != null) {
            return parent.getTitle();
        }

        return title;
    }

    public XMLRenderContext setTitle(String title) {
        this.title = title;

        return this;
    }

    public Object getAttribute(String attrName) {
        return attributes != null ? attributes.get(attrName) : null;
    }

    public XMLRenderContext setAttribute(String attrName, Object attrValue) {
        if (attrName != null) {
            if (attrValue != null) {
                if (attributes == null) {
                    attributes = new HashMap<String, Object>();
                }

                attributes.put(attrName, attrValue);
            } else if (attributes != null) {
                attributes.remove(attrName);
            }
        }

        return this;
    }

    @Override
    public String toString() {
        String s = "XMLRenderContext";

        if (getTitle() != null) {
            s += "[" + getTitle() + "]";
        }

        return s;
    }
}
