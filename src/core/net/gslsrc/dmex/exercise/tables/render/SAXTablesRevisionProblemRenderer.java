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

import net.gslsrc.dmex.exercise.ProblemAnswer;
import net.gslsrc.dmex.exercise.tables.TablesRevision;
import net.gslsrc.dmex.exercise.tables.TablesRevisionProblem;
import net.gslsrc.dmex.exercise.tables.TablesRevisionProblem.Field;
import net.gslsrc.dmex.render.RenderContext;
import net.gslsrc.dmex.render.SAXProblemRenderer;
import net.gslsrc.dmex.render.XMLRenderContext;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Renders Tables Revision problems to an XML SAX stream.
 *
 * @author Geoff Lewis
 */
public class SAXTablesRevisionProblemRenderer
    extends SAXProblemRenderer<TablesRevisionProblem>
    implements TablesRevisionProblemRenderer {

    private static final Attributes BLANK_ATTRS =
            makeAttributes("blank", "true");
    private static final Attributes ERROR_ATTRS =
            makeAttributes("blank", "true", "error", "true");

    public SAXTablesRevisionProblemRenderer() {}

    @Override
    public boolean rendersContext(Class<? extends RenderContext> type) {
        // need to exclude DOMRenderContext so use equals(), not isAssignable
        return XMLRenderContext.class.equals(type);
    }

    @Override
    protected void renderProblem(ContentHandler handler,
            XMLRenderContext context, TablesRevisionProblem problem)
        throws SAXException {

        if (problem == null) {
            return;
        }

        if (handler == null) {
            throw new SAXException("Context handler is null");
        }

        ProblemAnswer answer = (ProblemAnswer)context.getAttribute("answer");
        boolean error = answer != null && !answer.isCorrect();

        startElement(handler, XMLRenderContext.TAG_PROBLEM,
                "type", TablesRevision.EXERCISE_ID);

        if (problem.getFlip()) {
            renderRHS(handler, problem, error);
            element(handler, "equals");
            renderLHS(handler, problem, error);
        } else {
            renderLHS(handler, problem, error);
            element(handler, "equals");
            renderRHS(handler, problem, error);
        }

        endElement(handler, XMLRenderContext.TAG_PROBLEM);
    }

    private void renderLHS(ContentHandler handler,
            TablesRevisionProblem problem, boolean error) throws SAXException {
        String name = problem.isDivide() ? "divide" : "multiply";
        startElement(handler, name);

        renderTerm(handler, problem, Field.TERM1, error);
        renderTerm(handler, problem, Field.TERM2, error);

        endElement(handler, name);
    }

    private void renderRHS(ContentHandler handler,
            TablesRevisionProblem problem, boolean error) throws SAXException {

        startElement(handler, "answer",
                     getBlankAttributes(problem, Field.ANSWER, error));
        characters(handler, String.valueOf(problem.getAnswer()));
        endElement(handler, "answer");
    }

    private void renderTerm(ContentHandler handler,
            TablesRevisionProblem problem, Field field, boolean error)
        throws SAXException {

        assert field != null;

        if (field.equals(problem.getFraction())) {
            startElement(handler, "term");

            element(handler, "numerator", "1");

            startElement(handler, "denominator",
                         getBlankAttributes(problem, field, error));
            characters(handler, String.valueOf(problem.getValue(field)));
            endElement(handler, "denominator");

            endElement(handler, "term");

        } else {
            startElement(handler, "term",
                    getBlankAttributes(problem, field, error));
            characters(handler, String.valueOf(problem.getValue(field)));
            endElement(handler, "term");
        }
    }

    private Attributes getBlankAttributes(TablesRevisionProblem problem,
            Field field, boolean error) {
        Attributes attrs = null;

        if (field.equals(problem.getBlank())) {
            if (error) {
                attrs = ERROR_ATTRS;
            } else {
                attrs = BLANK_ATTRS;
            }
        } else {
            attrs = EMPTY_ATTRS;
        }

        return attrs;
    }
}
