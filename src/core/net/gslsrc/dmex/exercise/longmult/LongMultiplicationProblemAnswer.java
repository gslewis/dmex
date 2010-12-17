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

package net.gslsrc.dmex.exercise.longmult;

import net.gslsrc.dmex.exercise.ProblemAnswer;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents an submitted answer to a long multiplication problem.  The
 * answer contains the submitted digits for each row of working and the
 * answer.  The answer is correct if all digits have been submitted and match
 * their expected values.  The answer is incomplete if not all digits have
 * been submitted but those that have match their expected values.  The answer
 * is wrong if any submitted digit does not match its expected value.
 *
 * @author Geoff Lewis
 */
public class LongMultiplicationProblemAnswer
    extends ProblemAnswer<LongMultiplicationProblem> {

    private static final long serialVersionUID = 7618184334635537783L;

    /** Enumeration of error types. */
    private enum ErrorType implements ProblemAnswer.ErrorCode {
        INCOMPLETE,
        WRONG;

        @Override
        public String key() {
            return "error.problem." + LongMultiplication.EXERCISE_ID
                    + "." + name();
        }
    }

    private Integer[][] workingSubmit;
    private int[][] workingExpect;

    private Integer[] answerSubmit;
    private int[] answerExpect;

    private Set<ErrorType> errors;
    private Boolean correct;

    public LongMultiplicationProblemAnswer(LongMultiplicationProblem problem) {
        super(problem);

        int[] rows = problem.getWorkingRows();
        workingSubmit = new Integer[rows.length][];
        workingExpect = new int[rows.length][];

        for (int i = 0; i < rows.length; ++i) {
            char[] rc = String.valueOf(rows[i]).toCharArray();

            workingExpect[i] = new int[rc.length];
            arraycopy(workingExpect[i], rc);

            workingSubmit[i] = new Integer[rc.length];
        }

        char[] ac = String.valueOf(problem.getAnswer()).toCharArray();
        answerExpect = new int[ac.length];
        arraycopy(answerExpect, ac);

        answerSubmit = new Integer[ac.length];
    }

    public Integer[] getWorkingSubmit(int rowIndex) {
        return workingSubmit[rowIndex];
    }

    public Integer[] getAnswerSubmit() {
        return answerSubmit;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LongMultiplicationProblemAnswer[");
        sb.append(getProblem().getMultiplicand()).append(" * ");
        sb.append(getProblem().getMultiplier()).append(" = ");
        sb.append(getProblem().getAnswer()).append("]\n");

        for (int ri = 0; ri < workingExpect.length; ++ri) {
            sb.append("Row ").append(ri + 1).append(": expect=")
                    .append(concat(workingExpect[ri]))
                    .append(" submit=").append(concat(workingSubmit[ri]))
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

    public void setWorking(int row, int digit, Integer value) {
        workingSubmit[row][digit] = value;
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

        for (int ri = 0; ri < workingExpect.length; ++ri) {
            set.addAll(checkCorrect(workingExpect[ri], workingSubmit[ri]));
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
