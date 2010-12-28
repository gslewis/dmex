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

package net.gslsrc.dmex.exercise.longdiv.render;

import net.gslsrc.dmex.exercise.longdiv.LongDivision;
import net.gslsrc.dmex.exercise.longdiv.LongDivisionProblem;
import net.gslsrc.dmex.exercise.longdiv.LongDivisionProblemAnswer;
import net.gslsrc.dmex.exercise.longdiv.LongDivisionProblem.WorkingRow;
import net.gslsrc.dmex.render.RenderContext;
import net.gslsrc.dmex.render.SAXProblemRenderer;
import net.gslsrc.dmex.render.XMLRenderContext;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Renders {@link LongDivisionProblem}s to an XML SAX stream.
 *
 * @author Geoff Lewis
 */
public class SAXLongDivisionProblemRenderer
    extends SAXProblemRenderer<LongDivisionProblem>
    implements LongDivisionProblemRenderer {

    public SAXLongDivisionProblemRenderer() {}

    @Override
    public boolean rendersContext(Class<? extends RenderContext> type) {
        // need to exclude DOMRenderContext so use equals(), not isAssignable
        return XMLRenderContext.class.equals(type);
    }

    @Override
    protected void renderProblem(ContentHandler handler,
            XMLRenderContext context, LongDivisionProblem problem)
        throws SAXException {

        if (problem == null) {
            return;
        }

        if (handler == null) {
            throw new SAXException("Context handler is null");
        }

        LongDivisionProblemAnswer answer =
                (LongDivisionProblemAnswer)context.getAttribute(
                                            XMLRenderContext.ATTR_ANSWER);

        startElement(handler, XMLRenderContext.TAG_PROBLEM,
                     "type", LongDivision.EXERCISE_ID);

        renderText(handler, "divisor", String.valueOf(problem.getDivisor()));
        renderDigits(handler, "dividend", problem.getDividend());
        renderQuotient(handler, problem, answer);
        renderWorking(handler, problem, answer);

        if (problem.getShowHints()) {
            renderHints(handler, problem.getDivisor());
        }

        endElement(handler, XMLRenderContext.TAG_PROBLEM);
    }

    private void renderQuotient(ContentHandler handler,
            LongDivisionProblem problem,
            LongDivisionProblemAnswer answer) throws SAXException {
        startElement(handler, "quotient");

        char[] qchars = String.valueOf(problem.getQuotient()).toCharArray();

        int[] qstep = calculateSteps(problem, qchars);

        for (int i = 0; i < qchars.length; ++i) {
            String step = String.valueOf(qstep[i]);
            Integer submit = answer != null ? answer.getAnswerSubmit()[i]
                                            : null;

            if (submit != null) {
                renderText(handler, "digit", String.valueOf(qchars[i]),
                        "step", step,
                        "correct", correctAttr(qchars[i], submit));
            } else {
                renderText(handler, "digit", String.valueOf(qchars[i]),
                        "step", step);
            }
        }

        endElement(handler, "quotient");
    }

    private String correctAttr(char d, Integer submit) {
        if (submit != null
                && submit.intValue() == Integer.parseInt(String.valueOf(d))) {
            return "true";
        }

        return "false";
    }

    private int[] calculateSteps(LongDivisionProblem problem, char[] qchars) {
        int[] qstep = new int[qchars.length];

        int step = 1;
        int qindex = 0;
        int qdiff = 1;
        int rindex = 0;
        for (WorkingRow row : problem.getWorkingRows()) {
            // Allocate steps to quotient digits handles by the previous
            // working row.  First step always goes to first quotient digit.
            for (int i = 0; i < qdiff; ++i) {
                qstep[qindex] = step++;
                ++qindex;
            }

            // Advance step by length of row's bigend & subend.
            step += String.valueOf(row.getBigEnd()).length()
                    + String.valueOf(row.getSubEnd()).length();

            ++rindex;

            qdiff = row.getBigEndShift() - row.getSubEndShift();
        }

        // If quotient has trailing zeros, add their steps as well.
        if (qindex < qstep.length) {
            qstep[qindex++] = step++;
        }

        return qstep;
    }

    private void renderWorking(ContentHandler handler,
            LongDivisionProblem problem,
            LongDivisionProblemAnswer answer) throws SAXException {
        startElement(handler, "working");

        // First working step follows first quotient step so always start from
        // step 2.
        int step = 2;
        int rindex = 0;
        for (WorkingRow row : problem.getWorkingRows()) {
            startElement(handler, "row", "index",
                    String.valueOf(row.getIndex()));

            step = renderBigEnd(handler, row.getBigEnd(), step,
                    getBigEndSubmit(rindex, answer),
                    "shift", String.valueOf(row.getBigEndShift()));
            step = renderSubEnd(handler, row.getSubEnd(), step,
                    getSubEndSubmit(rindex, answer),
                    "shift", String.valueOf(row.getSubEndShift()));

            endElement(handler, "row");

            ++rindex;

            // Advance by number of quotient digits handled by this row of
            // working.
            step += row.getBigEndShift() - row.getSubEndShift();
        }

        endElement(handler, "working");
    }

    private Integer[] getBigEndSubmit(int index,
            LongDivisionProblemAnswer answer) {
        if (answer != null) {
            return answer.getBigEndSubmit(index);
        }

        return null;
    }

    private Integer[] getSubEndSubmit(int index,
            LongDivisionProblemAnswer answer) {
        if (answer != null) {
            return answer.getSubEndSubmit(index);
        }

        return null;
    }

    private void renderHints(ContentHandler handler, int divisor)
        throws SAXException {

        startElement(handler, "hints");

        for (int i = 2; i <= 9; ++i) {
            renderText(handler, "hint",
                    i + " x " + divisor + " = " + (i * divisor));
        }

        endElement(handler, "hints");
    }

    private void renderDigits(ContentHandler handler, String tag, int value,
            String... attrs) throws SAXException {

        startElement(handler, tag, attrs);

        char[] digits = String.valueOf(value).toCharArray();
        for (char digit : digits) {
            renderText(handler, "digit", String.valueOf(digit));
        }

        endElement(handler, tag);
    }

    private int renderBigEnd(ContentHandler handler, int value,
            int startingStep, Integer[] submit, String... attrs)
        throws SAXException {

        startElement(handler, "bigend", attrs);

        int step = startingStep;
        char[] digits = String.valueOf(value).toCharArray();
        for (int i = 0; i < digits.length; ++i) {
            String ss = String.valueOf(step++);
            Integer si = submit != null && submit.length == digits.length
                            ? submit[i] : null;

            if (si != null) {
                renderText(handler, "digit", String.valueOf(digits[i]),
                        "step", ss, "correct", correctAttr(digits[i], si));
            } else {
                renderText(handler, "digit", String.valueOf(digits[i]),
                        "step", ss);
            }
        }

        endElement(handler, "bigend");

        return step;
    }

    private int renderSubEnd(ContentHandler handler, int value,
            int startingStep, Integer[] submit, String... attrs)
        throws SAXException {

        startElement(handler, "subend", attrs);

        char[] digits = String.valueOf(value).toCharArray();
        for (int i = 0; i < digits.length; ++i) {
            // Subend is created by subtracting the preceding bigend from the
            // preceding subend (or dividend) so the subend is filled from
            // least-sig-dig to most-sig-dig (right-to-left).
            int step = startingStep + (digits.length - i - 1);

            Integer si = submit != null && submit.length == digits.length
                            ? submit[i] : null;

            if (si != null) {
                renderText(handler, "digit", String.valueOf(digits[i]),
                        "step", String.valueOf(step),
                        "correct", correctAttr(digits[i], si));
            } else {
                renderText(handler, "digit", String.valueOf(digits[i]),
                        "step", String.valueOf(step));
            }
        }

        endElement(handler, "subend");

        return startingStep + digits.length;
    }

    private void renderText(ContentHandler handler, String tag, String text)
        throws SAXException {

        renderText(handler, tag, text, (String[])null);
    }

    private void renderText(ContentHandler handler, String tag, String text,
            String... attrs) throws SAXException {

        startElement(handler, tag, attrs);
        characters(handler, String.valueOf(text));
        endElement(handler, tag);
    }

    @Override
    public String toString() {
        return "SAXLongDivisionProblemRenderer";
    }
}
