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

package net.gslsrc.dmex.exercise.tables.render;

import net.gslsrc.dmex.exercise.tables.TablesRevisionProblem;
import net.gslsrc.dmex.render.RenderContext;
import net.gslsrc.dmex.render.PDFProblemRenderer;
import net.gslsrc.dmex.render.PDFRenderContext;
import net.gslsrc.dmex.render.ProblemRenderer;
import net.gslsrc.dmex.render.xsl.ProblemTemplates;

/**
 * Renders Tables Revision problems to a PDF.
 *
 * @author Geoff Lewis
 */
public class PDFTablesRevisionProblemRenderer
    extends PDFProblemRenderer<TablesRevisionProblem>
    implements TablesRevisionProblemRenderer {

    private ProblemTemplates templates;

    public PDFTablesRevisionProblemRenderer() {}

    @Override
    public boolean rendersContext(Class<? extends RenderContext> type) {
        return PDFRenderContext.class.isAssignableFrom(type);
    }

    @Override
    protected ProblemTemplates getProblemTemplates() {
        if (templates == null) {
            templates = ProblemTemplates.getTemplates(
                                            TablesRevisionProblem.class);
        }

        return templates;
    }

    @Override
    protected ProblemRenderer<TablesRevisionProblem> getProblemRenderer() {
        return new SAXTablesRevisionProblemRenderer();
    }
}
