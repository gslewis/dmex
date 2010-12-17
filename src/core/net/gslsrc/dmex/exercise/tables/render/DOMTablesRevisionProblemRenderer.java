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

import net.gslsrc.dmex.exercise.tables.TablesRevision;
import net.gslsrc.dmex.exercise.tables.TablesRevisionProblem;
import net.gslsrc.dmex.exercise.tables.TablesRevisionProblem.Field;
import net.gslsrc.dmex.render.RenderContext;
import net.gslsrc.dmex.render.RenderException;
import net.gslsrc.dmex.render.DOMProblemRenderer;
import net.gslsrc.dmex.render.DOMRenderContext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Renders Tables Revision problems to an XML DOM.
 *
 * @author Geoff Lewis
 */
public class DOMTablesRevisionProblemRenderer
    extends DOMProblemRenderer<TablesRevisionProblem>
    implements TablesRevisionProblemRenderer {

    public DOMTablesRevisionProblemRenderer() {}

    @Override
    public boolean rendersContext(Class<? extends RenderContext> type) {
        return DOMRenderContext.class.isAssignableFrom(type);
    }

    @Override
    protected Element renderProblem(DOMRenderContext context, Document doc,
            TablesRevisionProblem problem) throws RenderException {
        if (problem == null) {
            return null;
        }

        if (doc == null) {
            throw new NullPointerException("Document is null");
        }

        Element lhs = makeLHS(doc, problem);
        Element rhs = makeRHS(doc, problem);

        Element element = doc.createElement(DOMRenderContext.TAG_PROBLEM);
        element.setAttribute("type", TablesRevision.EXERCISE_ID);

        if (problem.getFlip()) {
            element.appendChild(rhs);
            element.appendChild(doc.createElement("equals"));
            element.appendChild(lhs);
        } else {
            element.appendChild(lhs);
            element.appendChild(doc.createElement("equals"));
            element.appendChild(rhs);
        }

        return element;
    }

    private Element makeLHS(Document doc, TablesRevisionProblem problem)
        throws RenderException {

        Element element = doc.createElement(
                            problem.isDivide() ? "divide" : "multiply");

        element.appendChild(makeTerm(doc, problem, Field.TERM1));
        element.appendChild(makeTerm(doc, problem, Field.TERM2));

        return element;
    }

    private Element makeRHS(Document doc, TablesRevisionProblem problem)
        throws RenderException {

        Element element = doc.createElement("answer");
        element.setTextContent(String.valueOf(problem.getAnswer()));

        if (Field.ANSWER.equals(problem.getBlank())) {
            setBlank(element);
        }

        return element;
    }

    private Element makeTerm(Document doc, TablesRevisionProblem problem,
            Field field) throws RenderException {
        assert field != null;

        Element element = doc.createElement("term");

        if (field.equals(problem.getFraction())) {
            Element child = doc.createElement("numerator");
            child.setTextContent("1");
            element.appendChild(child);

            child = doc.createElement("denominator");
            child.setTextContent(String.valueOf(problem.getValue(field)));
            if (field.equals(problem.getBlank())) {
                setBlank(element);
            }
            element.appendChild(child);

        } else {
            if (field.equals(problem.getBlank())) {
                setBlank(element);
            }

            element.setTextContent(String.valueOf(problem.getValue(field)));
        }

        return element;
    }

    private void setBlank(Element element) {
        element.setAttribute("blank", "true");
    }
}
