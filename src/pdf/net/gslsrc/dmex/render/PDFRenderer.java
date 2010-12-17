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

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.MimeConstants;

/**
 * A general PDF renderer that applies XSL {@code Templates} to a
 * {@code Source} of content to be rendered.  The rendered result is sent to
 * the output stream obtained from the {@link PDFRenderContext}.
 *
 * @author Geoff Lewis
 */
public class PDFRenderer {

    private static FopFactory fopFactory;

    public PDFRenderer() {}

    public void render(Source renderContent, Templates templates,
            PDFRenderContext context) throws RenderException {

        // Create Fop object with output dest
        OutputStream out = null;
        try {
            out = context.getOutputStream();
        } catch (IOException ioe) {
            throw new RenderException("Failed to get output destination");
        }
        Fop fop = createFop(context, out);

        // Transform from XML Source to SAXResult/Fop which directs to
        // the output dest.
        try {
            templates.newTransformer()
                    .transform(renderContent,
                               new SAXResult(fop.getDefaultHandler()));
        } catch (TransformerException te) {
            throw new RenderException("Failed to transform to PDF", te);
        } catch (FOPException fe) {
            throw new RenderException("Failed to get FOP handler", fe);
        } finally {
            closeQuietly(out);
        }
    }

    private Fop createFop(PDFRenderContext context, OutputStream out)
        throws RenderException {

        try {
            return getFopFactory().newFop(MimeConstants.MIME_PDF,
                                          createUserAgent(context), out);
        } catch (FOPException fe) {
            throw new RenderException("Failed to initialize Fop", fe);
        }
    }

    private FOUserAgent createUserAgent(PDFRenderContext context) {
        FOUserAgent ua = getFopFactory().newFOUserAgent();

        // do stuff

        return ua;
    }

    private static FopFactory getFopFactory() {
        if (fopFactory == null) {
            fopFactory = FopFactory.newInstance();

            /*
            java.net.URL url = PDFRenderer.class.getResource("fop-config.xml");
            if (url != null) {
                try {
                    fopFactory.setUserConfig(url.toString());
                } catch (Exception e) {
                    System.err.println(
                            "Failed to apply user config from " + url
                            + " - " + e);
                }
            }
            */
        }

        return fopFactory;
    }

    private static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            // CHECKSTYLE:OFF
            } catch (IOException ioe) {}
            // CHECKSTYLE:ON
        }
    }
}
