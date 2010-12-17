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
import java.io.OutputStream;

/**
 * Context for rendering the PDF to an {@code OutputStream}.
 *
 * @author Geoff Lewis
 */
public class StreamPDFRenderContext extends PDFRenderContext {

    private OutputStream stream;

    public StreamPDFRenderContext() {}

    public StreamPDFRenderContext(PDFOutputType outputType) {
        super(outputType);
    }

    public StreamPDFRenderContext(OutputStream stream) {
        setOutputStream(stream);
    }

    public StreamPDFRenderContext(PDFOutputType outputType,
            OutputStream stream) {
        super(outputType);

        setOutputStream(stream);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (stream == null) {
            throw new IOException("Destination stream has not been set");
        }

        return stream;
    }

    public void setOutputStream(OutputStream stream) {
        this.stream = stream;
    }

    @Override
    public String toString() {
        String s = "StreamPDFRenderContext";

        if (getTitle() != null) {
            s += "[" + getTitle() + "]";
        }

        return s;
    }
}
