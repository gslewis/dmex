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

import java.util.Collection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Base for rendering problems to an XML DOM document.
 *
 * @param <P> the type of problem being rendered
 *
 * @author Geoff Lewis
 */
public abstract class DOMProblemRenderer<P extends Problem>
    implements ProblemRenderer<P> {

    protected DOMProblemRenderer() {}

    @Override
    public void render(Collection<P> problems, RenderContext context)
        throws RenderException {

        if (!(context instanceof DOMRenderContext)) {
            throw new RenderException("Unsupported render context " + context);
        }

        DOMRenderContext xrc = (DOMRenderContext)context;

        Document doc = xrc.init();

        for (P problem : problems) {
            if (problem == null) {
                continue;
            }

            try {
                Element e = renderProblem(xrc, doc, problem);
                if (e != null) {
                    doc.getDocumentElement().appendChild(e);
                }
            } catch (RenderException re) {
                System.err.println("Failed to render " + problem);
            }
        }
    }

    protected abstract Element renderProblem(DOMRenderContext context,
            Document doc, P problem) throws RenderException;
}
