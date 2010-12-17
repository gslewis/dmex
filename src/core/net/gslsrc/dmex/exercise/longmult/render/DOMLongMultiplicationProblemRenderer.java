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
import net.gslsrc.dmex.render.RenderException;
import net.gslsrc.dmex.render.DOMProblemRenderer;
import net.gslsrc.dmex.render.DOMRenderContext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Renders {@link LongMultiplicationProblem}s to an XML DOM.
 *
 * @author Geoff Lewis
 */
public class DOMLongMultiplicationProblemRenderer
    extends DOMProblemRenderer<LongMultiplicationProblem>
    implements LongMultiplicationProblemRenderer {

    public DOMLongMultiplicationProblemRenderer() {}

    @Override
    public boolean rendersContext(Class<? extends RenderContext> type) {
        return DOMRenderContext.class.isAssignableFrom(type);
    }

    @Override
    protected Element renderProblem(DOMRenderContext context, Document doc,
            LongMultiplicationProblem problem) throws RenderException {
        if (problem == null) {
            return null;
        }

        if (doc == null) {
            throw new NullPointerException("Document is null");
        }

        LongMultiplicationProblemAnswer answer =
                (LongMultiplicationProblemAnswer)context.getAttribute(
                                                DOMRenderContext.ATTR_ANSWER);

        Element element = doc.createElement(DOMRenderContext.TAG_PROBLEM);
        element.setAttribute("type", LongMultiplication.EXERCISE_ID);

        // Put step in array so it can be passed around without need to use
        // return value.
        int[] step = new int[] { 1 };

        element.appendChild(makeLHS(doc, problem));
        element.appendChild(makeWorking(doc, problem, answer, step));
        element.appendChild(makeRHS(doc, problem, answer, step));

        return element;
    }

    private Element makeLHS(Document doc, LongMultiplicationProblem problem)
        throws RenderException {

        Element element = doc.createElement("multiply");

        int value = problem.getMultiplicand();
        if (value <= 0) {
            throw new RenderException("Invalid multiplicand \"" + value + "\"");
        }
        element.appendChild(makeDigits(doc, "multiplicand", value));

        value = problem.getMultiplier();
        if (value <= 0) {
            throw new RenderException("Invalid multiplier \"" + value + "\"");
        }
        element.appendChild(makeDigits(doc, "multiplier", value));

        return element;
    }

    private Element makeWorking(Document doc,
            LongMultiplicationProblem problem,
            LongMultiplicationProblemAnswer answer, int[] step)
        throws RenderException {

        Element element = doc.createElement("working");

        int[] workingRows = problem.getWorkingRows();
        for (int i = 0; i < workingRows.length; ++i) {
            Integer[] submit = null;
            if (answer != null) {
                submit = answer.getWorkingSubmit(i);
            }

            element.appendChild(
                    makeDigits(doc, "row", workingRows[i], submit, step, i));
        }

        return element;
    }

    private Element makeRHS(Document doc, LongMultiplicationProblem problem,
            LongMultiplicationProblemAnswer answer, int[] step)
        throws RenderException {

        int value = problem.getAnswer();
        if (value <= 0) {
            throw new RenderException("Invalid answer \"" + value + "\"");
        }

        Integer[] submit = null;
        if (answer != null) {
            submit = answer.getAnswerSubmit();
        }

        return makeDigits(doc, "answer", value, submit, step, 0);
    }

    private Element makeDigits(Document doc, String tag, int value)
        throws RenderException {

        return makeDigits(doc, tag, value, null, null, 0);
    }

    private Element makeDigits(Document doc, String tag, int value,
            Integer[] submit, int[] step, int exponent)
        throws RenderException {

        Element element = doc.createElement(tag);

        char[] digits = String.valueOf(value).toCharArray();
        int steps = digits.length - exponent;

        for (int i = 0; i < digits.length; ++i) {
            Integer answer = null;
            if (submit != null && submit.length == digits.length) {
                answer = submit[i];
            }

            int si = 0;
            if (step != null && i < steps) {
                si = step[0] + (steps - i - 1);
            }

            element.appendChild(makeDigit(doc, digits[i], answer, si));
        }

        if (step != null) {
            step[0] = step[0] + steps;
        }

        return element;
    }

    private Element makeDigit(Document doc, char digit) {
        return makeDigit(doc, digit, null, 0);
    }

    private Element makeDigit(Document doc, char digit, Integer submit,
            int step) {
        Element element = doc.createElement("digit");

        if (submit != null) {
            element.setAttribute("correct", correctAttr(digit, submit));
        }

        if (step > 0) {
            element.setAttribute("step", String.valueOf(step));
        }

        element.setTextContent(String.valueOf(digit));

        return element;
    }

    private String correctAttr(char digit, Integer submit) {
        assert submit != null;

        if (submit.intValue() == Integer.parseInt(String.valueOf(digit))) {
            return "true";
        }

        return "false";
    }

    @Override
    public String toString() {
        return "DOMLongMultiplicationProblemRenderer";
    }
}
