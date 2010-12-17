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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Context for rendering the PDF to a file.
 *
 * @author Geoff Lewis
 */
public class FilePDFRenderContext extends PDFRenderContext {

    private File file;

    public FilePDFRenderContext() {}

    public FilePDFRenderContext(PDFOutputType outputType) {
        super(outputType);
    }

    public FilePDFRenderContext(File file) {
        setFile(file);
    }

    public FilePDFRenderContext(PDFOutputType outputType, File file) {
        super(outputType);

        setFile(file);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (getFile() == null) {
            throw new IOException("Destination file has not been set");
        }

        return new FileOutputStream(getFile());
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        String s = "FilePDFRenderContext";

        if (getTitle() != null) {
            s += "[" + getTitle() + "]";
        }

        if (getFile() != null) {
            s += " " + getFile().getName();
        }

        return s;
    }
}
