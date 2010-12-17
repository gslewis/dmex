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

package net.gslsrc.dmex.exercise.longmult.render;

import net.gslsrc.dmex.exercise.longmult.LongMultiplication;
import net.gslsrc.dmex.exercise.longmult.LongMultiplicationProblem;
import net.gslsrc.dmex.exercise.longmult.LongMultiplicationProblemAnswer;
import net.gslsrc.dmex.render.RenderContext;
import net.gslsrc.dmex.render.SAXProblemRenderer;
import net.gslsrc.dmex.render.XMLRenderContext;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Renders {@link LongMultiplicationProblem}s to an XML SAX stream.
 *
 * @author Geoff Lewis
 */
public class SAXLongMultiplicationProblemRenderer
    extends SAXProblemRenderer<LongMultiplicationProblem>
    implements LongMultiplicationProblemRenderer {

    public SAXLongMultiplicationProblemRenderer() {}

    @Override
    public boolean rendersContext(Class<? extends RenderContext> type) {
        // need to exclude DOMRenderContext so use equals(), not isAssignable
        return XMLRenderContext.class.equals(type);
    }

    @Override
    protected void renderProblem(ContentHandler handler,
            XMLRenderContext context, LongMultiplicationProblem problem)
        throws SAXException {

        if (problem == null) {
            return;
        }

        if (handler == null) {
            throw new SAXException("Context handler is null");
        }

        LongMultiplicationProblemAnswer answer =
                (LongMultiplicationProblemAnswer)context.getAttribute("answer");

        startElement(handler, XMLRenderContext.TAG_PROBLEM,
                "type", LongMultiplication.EXERCISE_ID);

        renderLHS(handler, problem);

        int step = 1;
        step = renderWorking(handler, problem, answer, step);

        renderRHS(handler, problem, answer, step);

        endElement(handler, XMLRenderContext.TAG_PROBLEM);
    }

    private void renderLHS(ContentHandler handler,
            LongMultiplicationProblem problem) throws SAXException {
        startElement(handler, "multiply");

        int value = problem.getMultiplicand();
        if (value <= 0) {
            throw new SAXException("Invalid multiplicand \"" + value + "\"");
        }
        renderDigits(handler, "multiplicand", value);

        value = problem.getMultiplier();
        if (value <= 0) {
            throw new SAXException("Invalid multiplier \"" + value + "\"");
        }
        renderDigits(handler, "multiplier", value);

        endElement(handler, "multiply");
    }

    private int renderWorking(ContentHandler handler,
            LongMultiplicationProblem problem,
            LongMultiplicationProblemAnswer answer, int step)
        throws SAXException {

        int si = step;

        startElement(handler, "working");

        int[] workingRows = problem.getWorkingRows();
        for (int i = 0; i < workingRows.length; ++i) {
            Integer[] submit = null;
            if (answer != null) {
                submit = answer.getWorkingSubmit(i);
            }

            si = renderDigits(handler, "row", workingRows[i], submit, si, i);
        }

        endElement(handler, "working");

        return si;
    }

    private void renderRHS(ContentHandler handler,
            LongMultiplicationProblem problem,
            LongMultiplicationProblemAnswer answer, int step)
        throws SAXException {

        int value = problem.getAnswer();

        if (value <= 0) {
            throw new SAXException("Invalid answer \"" + value + "\"");
        }

        Integer[] submit = null;
        if (answer != null) {
            submit = answer.getAnswerSubmit();
        }

        renderDigits(handler, "answer", value, submit, step, 0);
    }

    private int renderDigits(ContentHandler handler, String tag, int value)
        throws SAXException {

        return renderDigits(handler, tag, value, null, 0, 0);
    }

    private int renderDigits(ContentHandler handler, String tag, int value,
            Integer[] submit, int step, int exponent) throws SAXException {

        startElement(handler, tag);

        char[] digits = String.valueOf(value).toCharArray();

        // Steps excludes trailing zeros in rows of working.
        int steps = digits.length - exponent;

        for (int i = 0; i < digits.length; ++i) {
            Integer answer = null;
            if (submit != null && submit.length == digits.length) {
                answer = submit[i];
            }

            // Renders left-to-right, step is right-to-left.  Don't mark a
            // step for trailing zeros.
            int si = 0;
            if (step > 0 && i < steps) {
                si = step + (steps - i - 1);
            }

            renderDigit(handler, digits[i], answer, si);
        }
        endElement(handler, tag);

        return step + steps;
    }

    private void renderDigit(ContentHandler handler, char digit)
        throws SAXException {

        renderDigit(handler, digit, null, 0);
    }

    private void renderDigit(ContentHandler handler, char digit,
            Integer submit, int step) throws SAXException {

        String[] attrs = null;
        if (step > 0) {
            attrs = append(attrs, "step", String.valueOf(step));
        }

        if (submit != null) {
            attrs = append(attrs, "correct", correctValue(digit, submit));
        }

        startElement(handler, "digit", attrs);

        characters(handler, String.valueOf(digit));
        endElement(handler, "digit");
    }

    private String correctValue(char digit, Integer submit) {
        assert submit != null;

        if (submit.intValue() == Integer.parseInt(String.valueOf(digit))) {
            return "true";
        }

        return "false";
    }

    @Override
    public String toString() {
        return "SAXLongMultiplicationProblemRenderer";
    }
}
