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
 * Context for rendering problems to PDF.  Implementations of this context
 * must supply an {@code OutputStream} to which FOP will write the PDF it
 * generates.  The type of output depends on the {@link PDFOutputType} value:
 *
 * <ul>
 * <li>{@code PROBLEMS} - a problem sheet with blank fields to be filled</li>
 * <li>{@code ANSWERS} - an answer sheet with filled "blank" fields</li>
 * <li>{@code PROBLEMS_WITH_ANSWERS} - a problem sheet with answers in the
 * margin</li>
 * </ul>
 *
 * @author Geoff Lewis
 */
public abstract class PDFRenderContext extends XMLRenderContext {

    private PDFOutputType outputType;

    protected PDFRenderContext() {}

    protected PDFRenderContext(PDFOutputType outputType) {
        setOutputType(outputType);
    }

    public PDFOutputType getOutputType() {
        return outputType != null ? outputType : PDFOutputType.PROBLEMS;
    }

    public void setOutputType(PDFOutputType outputType) {
        this.outputType = outputType;
    }

    public abstract OutputStream getOutputStream() throws IOException;
}
