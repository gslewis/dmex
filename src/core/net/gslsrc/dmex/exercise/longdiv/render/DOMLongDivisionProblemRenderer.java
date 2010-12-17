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
import net.gslsrc.dmex.render.RenderException;
import net.gslsrc.dmex.render.DOMProblemRenderer;
import net.gslsrc.dmex.render.DOMRenderContext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Renders {@link LongDivisionProblem}s to an XML DOM.
 *
 * @author Geoff Lewis
 */
public class DOMLongDivisionProblemRenderer
    extends DOMProblemRenderer<LongDivisionProblem>
    implements LongDivisionProblemRenderer {

    public DOMLongDivisionProblemRenderer() {}

    @Override
    public boolean rendersContext(Class<? extends RenderContext> type) {
        return DOMRenderContext.class.isAssignableFrom(type);
    }

    @Override
    protected Element renderProblem(DOMRenderContext context, Document doc,
            LongDivisionProblem problem) throws RenderException {
        if (problem == null) {
            return null;
        }

        if (doc == null) {
            throw new NullPointerException("Document is null");
        }

        LongDivisionProblemAnswer answer =
                (LongDivisionProblemAnswer)context.getAttribute(
                                            DOMRenderContext.ATTR_ANSWER);

        Element element = doc.createElement(DOMRenderContext.TAG_PROBLEM);
        element.setAttribute("type", LongDivision.EXERCISE_ID);

        element.appendChild(makeText(doc, "divisor",
                                     String.valueOf(problem.getDivisor())));
        element.appendChild(makeDigits(doc, "dividend", problem.getDividend()));

        // Allocate the steps in the working first (store quotient steps)
        // then allocate steps in the quotient.  Append the quotient before
        // the working though.
        char[] qchars = String.valueOf(problem.getQuotient()).toCharArray();
        int[] qstep = new int[qchars.length];

        Element working = makeWorking(doc, problem, answer, qstep);
        Element quotientE = makeQuotient(doc, answer, qchars, qstep);

        element.appendChild(quotientE);
        element.appendChild(working);

        if (problem.getShowHints()) {
            element.appendChild(makeHints(doc, problem.getDivisor()));
        }

        return element;
    }

    private Element makeWorking(Document doc,
            LongDivisionProblem problem, LongDivisionProblemAnswer answer,
            int[] qstep) throws RenderException {
        Element element = doc.createElement("working");

        // Start from step 2 (step 1 is first quotient digit)
        int[] step = { 1 };
        int rindex = 0;
        Integer[] submit = null;

        for (WorkingRow row : problem.getWorkingRows()) {
            qstep[rindex] = step[0]++;

            Element child = doc.createElement("row");
            child.setAttribute("index", String.valueOf(row.getIndex()));

            if (answer != null) {
                submit = answer.getBigEndSubmit(rindex);
            }

            Element bigend = makeDigits(doc, "bigend", row.getBigEnd(),
                                        submit, step, 1);
            bigend.setAttribute("shift", String.valueOf(row.getBigEndShift()));
            child.appendChild(bigend);

            if (answer != null) {
                submit = answer.getSubEndSubmit(rindex);
            }

            Element subend = makeDigits(doc, "subend", row.getSubEnd(),
                                        submit, step, -1);
            subend.setAttribute("shift", String.valueOf(row.getSubEndShift()));
            child.appendChild(subend);

            element.appendChild(child);

            ++rindex;
        }

        while (rindex < qstep.length) {
            qstep[rindex++] = step[0]++;
        }

        return element;
    }

    private Element makeQuotient(Document doc, LongDivisionProblemAnswer answer,
            char[] qchars, int[] qstep) throws RenderException {
        Element element = doc.createElement("quotient");

        for (int i = 0; i < qchars.length; ++i) {
            Element child = makeText(doc, "digit", String.valueOf(qchars[i]));

            if (qstep != null) {
                child.setAttribute("step", String.valueOf(qstep[i]));
            }

            if (answer != null) {
                child.setAttribute("correct",
                        correctAttr(qchars[i], answer.getAnswerSubmit()[i]));
            }

            element.appendChild(child);
        }

        return element;
    }

    private Element makeHints(Document doc, int divisor) {
        Element element = doc.createElement("hints");

        for (int i = 2; i <= 9; ++i) {
            element.appendChild(makeText(doc, "hint",
                        i + " x " + divisor + " = " + (i * divisor)));
        }

        return element;
    }

    private Element makeDigits(Document doc, String tag, int value)
        throws RenderException {

        return makeDigits(doc, tag, value, null, null, 0);
    }

    private Element makeDigits(Document doc, String tag, int value,
            Integer[] submit, int[] stepStore, int increment)
        throws RenderException {

        Element element = doc.createElement(tag);

        char[] digits = String.valueOf(value).toCharArray();

        int step = 0;
        if (stepStore != null) {
            step = stepStore[0];

            // If -ve increment, step increases from right-to-left (for
            // subend).
            if (increment == -1) {
                step += digits.length - 1;
            }
        }

        for (int i = 0; i < digits.length; ++i) {
            Element child = makeText(doc, "digit", String.valueOf(digits[i]));

            if (step != 0) {
                child.setAttribute("step", String.valueOf(step));
                step += increment;
            }

            if (submit != null && submit.length == digits.length) {
                element.setAttribute("correct",
                                     correctAttr(digits[i], submit[i]));
            }

            element.appendChild(child);
        }

        if (stepStore != null) {
            stepStore[0] += digits.length;
        }

        return element;
    }

    private String correctAttr(char d, Integer submit) {
        if (submit != null
                && submit.intValue() == Integer.parseInt(String.valueOf(d))) {
            return "true";
        }

        return "false";
    }

    private Element makeText(Document doc, String tag, String digit) {
        Element element = doc.createElement(tag);
        element.setTextContent(String.valueOf(digit));

        return element;
    }

    @Override
    public String toString() {
        return "DOMLongDivisionProblemRenderer";
    }
}
