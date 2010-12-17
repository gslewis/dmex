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

import net.gslsrc.dmex.exercise.Problem;

/**
 * Represents a single long multiplication problem.  Comprises the
 * multiplicand (top) and multiplier (bottom) terms and supplies the resulting
 * answer.  Terms are validated to ensure they lie within the allowable range
 * (10 to 9999) but the answer is not validated for length.
 *
 * @author Geoff Lewis
 */
public class LongMultiplicationProblem extends Problem {
    private static final long serialVersionUID = -7276140256772786928L;

    private static final int MIN_VALUE = 10;
    private static final int MAX_VALUE = 9999;

    private Integer multiplicand;
    private Integer multiplier;

    public LongMultiplicationProblem() {
        super(LongMultiplication.EXERCISE_ID);
    }

    LongMultiplicationProblem(int multiplicand, int multiplier) {
        super(LongMultiplication.EXERCISE_ID);

        setMultiplicand(multiplicand);
        setMultiplier(multiplier);
    }

    public int getMultiplicand() {
        return multiplicand != null ? multiplicand.intValue() : 0;
    }

    public void setMultiplicand(int value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("Multiplicand \""
                    + value + "\" out of bounds ["
                    + MIN_VALUE + "-" + MAX_VALUE + "]");
        }

        multiplicand = Integer.valueOf(value);
    }

    public int getMultiplier() {
        return multiplier != null ? multiplier.intValue() : 0;
    }

    public void setMultiplier(int value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("Multiplier \""
                    + value + "\" out of bounds ["
                    + MIN_VALUE + "-" + MAX_VALUE + "]");
        }

        multiplier = Integer.valueOf(value);
    }

    /**
     * Gets the intermediate values calculated when performing the long
     * multiplication.  There will be one row of "working" for each digit in
     * the multiplier.  The first row corresponds to the last (least
     * significant) digit, the last row corresponds to the first (most
     * siginificant) digit.
     *
     * @return the rows used in working out the calculation
     */
    public int[] getWorkingRows() {
        char[] mpd = String.valueOf(getMultiplier()).toCharArray();

        int[] rows = new int[mpd.length];
        int tens = 1;
        for (int i = 0; i < rows.length; ++i) {
            // Work from least significant digit to most.
            int index = rows.length - 1 - i;
            int digit = Integer.parseInt(String.valueOf(mpd[index]));

            rows[i] = getMultiplicand() * digit * tens;

            // Multiply by ten as we advance from least to most significant
            // digit.
            tens *= 10;
        }

        return rows;
    }

    public int getAnswer() {
        return getMultiplicand() * getMultiplier();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("LongMultiplicationProblem[");
        sb.append(getMultiplicand()).append(" * ").append(getMultiplier());
        sb.append(" = ").append(getAnswer()).append("]");

        return sb.toString();
    }
}
