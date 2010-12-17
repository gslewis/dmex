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

package net.gslsrc.dmex.exercise.longdiv;

import net.gslsrc.dmex.exercise.ProblemAnswer;
import net.gslsrc.dmex.exercise.longdiv.LongDivisionProblem.WorkingRow;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents an submitted answer to a long division problem.
 *
 * @author Geoff Lewis
 */
public class LongDivisionProblemAnswer
    extends ProblemAnswer<LongDivisionProblem> {

    private static final long serialVersionUID = -8063283698554907990L;

    /** Enumeration of error types. */
    private enum ErrorType implements ProblemAnswer.ErrorCode {
        INCOMPLETE,
        WRONG;

        @Override
        public String key() {
            return "error.problem." + LongDivision.EXERCISE_ID + "." + name();
        }
    }

    private Integer[][] bigendSubmit;
    private int[][] bigendExpect;

    private Integer[][] subendSubmit;
    private int[][] subendExpect;

    private Integer[] answerSubmit;
    private int[] answerExpect;

    private Set<ErrorType> errors;
    private Boolean correct;

    public LongDivisionProblemAnswer(LongDivisionProblem problem) {
        super(problem);

        WorkingRow[] rows = problem.getWorkingRows();
        bigendSubmit = new Integer[rows.length][];
        bigendExpect = new int[rows.length][];
        subendSubmit = new Integer[rows.length][];
        subendExpect = new int[rows.length][];

        for (int i = 0; i < rows.length; ++i) {
            char[] rc;

            rc = String.valueOf(rows[i].getBigEnd()).toCharArray();
            bigendExpect[i] = new int[rc.length];
            arraycopy(bigendExpect[i], rc);
            bigendSubmit[i] = new Integer[rc.length];

            rc = String.valueOf(rows[i].getSubEnd()).toCharArray();
            subendExpect[i] = new int[rc.length];
            arraycopy(subendExpect[i], rc);
            subendSubmit[i] = new Integer[rc.length];
        }

        char[] ac = String.valueOf(problem.getQuotient()).toCharArray();
        answerExpect = new int[ac.length];
        arraycopy(answerExpect, ac);

        answerSubmit = new Integer[ac.length];
    }

    public Integer[] getBigEndSubmit(int rowIndex) {
        return bigendSubmit[rowIndex];
    }

    public Integer[] getSubEndSubmit(int rowIndex) {
        return subendSubmit[rowIndex];
    }

    public Integer[] getAnswerSubmit() {
        return answerSubmit;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LongDivisionProblemAnswer[");
        sb.append(getProblem().getDividend()).append(" / ");
        sb.append(getProblem().getDivisor()).append(" = ");
        sb.append(getProblem().getQuotient()).append("]\n");

        for (int ri = 0; ri < bigendExpect.length; ++ri) {
            sb.append("Row ").append(ri + 1).append(": be.expect=")
                    .append(concat(bigendExpect[ri]))
                    .append(" be.submit=").append(concat(bigendSubmit[ri]))
                    .append(" se.expect=").append(concat(subendExpect[ri]))
                    .append(" se.submit=").append(concat(subendSubmit[ri]))
                    .append("\n");
        }

        sb.append("Answer: expect=").append(concat(answerExpect))
                .append(" submit=").append(concat(answerSubmit));

        return sb.toString();
    }

    private String concat(int[] array) {
        StringBuilder sb = new StringBuilder();

        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                sb.append(array[i]);
            }
        }

        return sb.toString();
    }

    private String concat(Integer[] array) {
        StringBuilder sb = new StringBuilder();

        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                sb.append(array[i] != null ? array[i] : "_");
            }
        }

        return sb.toString();
    }

    public void setBigEnd(int row, int digit, Integer value) {
        bigendSubmit[row][digit] = value;
        correct = null;
        errors = null;
    }

    public void setSubEnd(int row, int digit, Integer value) {
        subendSubmit[row][digit] = value;
        correct = null;
        errors = null;
    }

    public void setAnswer(int digit, Integer value) {
        answerSubmit[digit] = value;
        correct = null;
        errors = null;
    }

    private void arraycopy(int[] dest, char[] src) {
        assert dest != null;
        assert src != null;
        assert dest.length == src.length;

        for (int i = 0; i < src.length; ++i) {
            dest[i] = Integer.parseInt(String.valueOf(src[i]));
        }
    }

    @Override
    public boolean isCorrect() {
        if (correct == null) {
            errors = checkCorrect();
            correct = Boolean.valueOf(errors.isEmpty());
        }

        return correct.booleanValue();
    }

    private Set<ErrorType> checkCorrect() {
        EnumSet<ErrorType> set = EnumSet.noneOf(ErrorType.class);

        for (int ri = 0; ri < bigendExpect.length; ++ri) {
            set.addAll(checkCorrect(bigendExpect[ri], bigendSubmit[ri]));
        }

        for (int ri = 0; ri < subendExpect.length; ++ri) {
            set.addAll(checkCorrect(subendExpect[ri], subendSubmit[ri]));
        }

        set.addAll(checkCorrect(answerExpect, answerSubmit));

        return set;
    }

    private Set<ErrorType> checkCorrect(int[] expect, Integer[] submit) {
        EnumSet<ErrorType> set = EnumSet.noneOf(ErrorType.class);

        for (int di = 0; di < expect.length; ++di) {
            Integer value = submit[di];

            if (value == null) {
                set.add(ErrorType.INCOMPLETE);
            } else if (value.intValue() != expect[di]) {
                set.add(ErrorType.WRONG);
            }
        }

        return set;
    }

    @Override
    public Set<? extends ErrorCode> getErrorCodes() {
        if (isCorrect()) {
            return Collections.emptySet();
        }

        return errors;
    }
}
