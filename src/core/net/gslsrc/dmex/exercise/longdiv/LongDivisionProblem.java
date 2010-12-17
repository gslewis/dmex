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

import net.gslsrc.dmex.exercise.Problem;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Represents a single long division problem.  Comprises the dividend and
 * divisor terms from which are obtained the answer (quotient) and
 * rows of working (see {@link WorkingRow}).
 *
 * @author Geoff Lewis
 */
public class LongDivisionProblem extends Problem {
    private static final long serialVersionUID = -8025818911601541882L;

    private static final int MIN_DIVIDEND_VALUE = 14;
    private static final int MAX_DIVIDEND_VALUE = 99999;
    private static final int MIN_DIVISOR_VALUE = 11;
    private static final int MAX_DIVISOR_VALUE = 499;

    private Integer dividend;
    private Integer divisor;
    private boolean showHints;

    private WorkingRow[] workingRows;

    public LongDivisionProblem() {
        super(LongDivision.EXERCISE_ID);
    }

    public LongDivisionProblem(int dividend, int divisor) {
        this(dividend, divisor, false);
    }

    public LongDivisionProblem(int dividend, int divisor, boolean showHints) {
        super(LongDivision.EXERCISE_ID);

        setDividend(dividend);
        setDivisor(divisor);
        setShowHints(showHints);
    }

    public int getDividend() {
        return dividend != null ? dividend.intValue() : 0;
    }

    public void setDividend(int value) {
        if (value < MIN_DIVIDEND_VALUE || value > MAX_DIVIDEND_VALUE) {
            throw new IllegalArgumentException("Dividend \"" + value
                    + "\" out of bounds [" + MIN_DIVIDEND_VALUE + "-"
                    + MAX_DIVIDEND_VALUE + "]");
        }

        dividend = Integer.valueOf(value);
    }

    public int getDivisor() {
        return divisor != null ? divisor.intValue() : 0;
    }

    public void setDivisor(int value) {
        if (value < MIN_DIVISOR_VALUE || value > MAX_DIVISOR_VALUE) {
            throw new IllegalArgumentException("Divisor \"" + value
                    + "\" out of bounds [" + MIN_DIVISOR_VALUE + "-"
                    + MAX_DIVISOR_VALUE + "]");
        }

        divisor = Integer.valueOf(value);
    }

    public int getQuotient() {
        if (getDividend() == 0 || getDivisor() == 0) {
            throw new IllegalStateException(
                    this + " has incomplete definition");
        }

        if (getDividend() < getDivisor()) {
            throw new IllegalStateException(this + " dividend (" + dividend
                    + ") must be greater than divisor (" + divisor + ")");
        }

        return (int)(getDividend() / getDivisor());
    }

    // the "working" is assembled using knowledge of the quotient (answer)
    // to assemble the rows of working, we traverse the quotient from
    // most-significant-digit to least
    // each working row comprises two components for its quotient digit:
    //  - the product of the q-digit and the divisor: the biggest "dividend"
    //    (bigend)
    //  - subtract the bigend from the preceding bigend or dividend then
    //    discard least-significant digits until we find the smalled value
    //    larger than the divisor
    //  we repeat until all q-digits have been processed
    public WorkingRow[] getWorkingRows() {
        if (workingRows == null) {
            workingRows = calculateWorking();
        }

        return workingRows;
    }

    public WorkingRow getWorkingRow(int index) {
        if (index >= 0 && index < getWorkingRows().length) {
            return getWorkingRows()[index];
        }

        return null;
    }

    private WorkingRow[] calculateWorking() {
        Collection<WorkingRow> list = new LinkedList<WorkingRow>();

        // The running dividend
        int rdividend = getDividend();

        char[] qchars = String.valueOf(getQuotient()).toCharArray();
        for (int i = 0; i < qchars.length; ++i) {
            // For each digit in the quotient...
            int x = Integer.parseInt(String.valueOf(qchars[i]));
            if (x == 0) {
                continue;
            }

            // If a quotient digit is zero, there is no working row.
            WorkingRow row = new WorkingRow(i);

            // Exponent used to calculate the shift multiplier for this row
            // (10^exp)
            int bigendShift = qchars.length - i - 1;

            row.setBigEnd(getDivisor() * x, bigendShift);

            // Update the running dividend.
            rdividend -= row.getBigEndValue();

            // Calculate the subend for this working row.  The subend is the
            // "subtracted dividend" obtained by subtracting the preceding
            // bigend from the preceding subend or dividend.
            int subend = 0;
            int subendShift = 0;
            String srdiv = String.valueOf(rdividend);
            for (int j = 1; j <= srdiv.length(); ++j) {
                subend = Integer.valueOf(srdiv.substring(0, j)).intValue();
                subendShift = srdiv.length() - j;

                if (subend > getDivisor()) {
                    break;
                }
            }

            row.setSubEnd(subend, subendShift);

            list.add(row);
        }

        return list.toArray(new WorkingRow[list.size()]);
    }

    public boolean getShowHints() {
        return showHints;
    }

    public void setShowHints(boolean value) {
        showHints = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("LongDivisionProblem[");
        sb.append(getDividend()).append(" / ").append(getDivisor());
        sb.append("]");

        return sb.toString();
    }

    /**
     * A single row of working in the long division solution.  Each row
     * corresponds to a digit in the quotient (except for zeros, which are
     * skipped).  The working comprises the "biggest dividend" (bigend) and
     * the "subtracted dividend" (subend).
     * <p>
     * The <i>bigend</i> is the row's quotient digit multiplied by the
     * divisor, shifted to the left so that the least-significant digit in the
     * big end is aligned with the quotient digit.  The "shift" is the
     * exponent of the power-of-10 multiplier the provides the actual
     * contribution of the bigend to the original dividend.
     * <p>
     * The <i>subend</i> is obtained by subtracting the row's "actual" bigend
     * (multiplied by 10^shift) from either the original dividend (if the
     * first row) or the preceding row's actual bigend.  The subend then
     * discards least-significant digits (ie., from the right) to get the
     * smallest number that is still larger than the divisor.  The subend
     * becomes the dividend for the next round of division.
     *
     * @see #getWorkingRows
     */
    public class WorkingRow implements Serializable {
        private static final long serialVersionUID = 6465427580868806639L;

        int index;

        int bigend;
        int bigendShift;
        int subend;
        int subendShift;

        WorkingRow(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public int getBigEnd() {
            return bigend;
        }

        // apply this shift to the bigend
        int getBigEndValue() {
            return bigend * (int)Math.pow(10, bigendShift);
        }

        public int getBigEndShift() {
            return bigendShift;
        }

        void setBigEnd(int bigend, int shift) {
            this.bigend = bigend;
            this.bigendShift = shift;
        }

        public int getSubEnd() {
            return subend;
        }

        public int getSubEndShift() {
            return subendShift;
        }

        void setSubEnd(int subend, int shift) {
            this.subend = subend;
            this.subendShift = shift;
        }

        @Override
        public String toString() {
            return index + ": " + bigend + "[" + bigendShift + "], " + subend
                    + "[" + subendShift + "]";
        }

        private int shift(int value, int shift) {
            if (shift <= 0) {
                return value;
            }

            String s = String.valueOf(value);
            if (shift >= s.length()) {
                return 0;
            }

            s = s.substring(0, s.length() - shift);

            return Integer.parseInt(s);
        }
    }
}
