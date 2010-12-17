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
import net.gslsrc.dmex.render.xsl.ProblemTemplates;

import java.util.Collection;
import javax.xml.transform.Source;

/**
 * Base for rendering problems to a PDF.  The implementation must provide the
 * problems as an XML source via {@link #getProblemSource} and the XSL
 * {@code Templates} for transforming the problem XML via
 * {@link #getProblemTemplates}.
 *
 * @param <P> the type of problem being rendered
 *
 * @author Geoff Lewis
 */
public abstract class PDFProblemRenderer<P extends Problem>
    extends PDFRenderer implements ProblemRenderer<P> {

    protected PDFProblemRenderer() {}

    @Override
    public void render(Collection<P> problems, RenderContext context)
        throws RenderException {

        if (!(context instanceof PDFRenderContext)) {
            throw new RenderException("Unsupported render context " + context);
        }

        PDFRenderContext prc = (PDFRenderContext)context;

        render(getProblemSource(prc, problems),
               getProblemTemplates().getTemplates(prc.getOutputType()),
               prc);
    }

    protected abstract ProblemTemplates getProblemTemplates();

    protected abstract ProblemRenderer<P> getProblemRenderer();

    protected XMLRenderContext getXMLRenderContext(
            PDFRenderContext parentContext) {
        return new XMLRenderContext(parentContext);
    }

    protected Source getProblemSource(PDFRenderContext parentContext,
            Collection<P> problems) throws RenderException {
        XMLRenderContext context = getXMLRenderContext(parentContext);

        getProblemRenderer().render(problems, context);

        return context.getSource();
    }
}
