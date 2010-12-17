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

package net.gslsrc.dmex.random;

/**
 * Provides an integer sequence from a range.
 *
 * @author Geoff Lewis
 */
public class NumberRangeTerm implements RandomTerm {
    private static final long serialVersionUID = 524434523778265396L;

    private String name;

    private int lowValue;
    private int highValue;
    private int size;

    public NumberRangeTerm(int lowValue, int highValue) {
        this(null, lowValue, highValue);
    }

    public NumberRangeTerm(String name, int lowValue, int highValue) {
        this.name = name;

        if (lowValue == highValue) {
            throw new IllegalArgumentException("Low value equals high value");
        }

        this.lowValue = Math.min(lowValue, highValue);
        this.highValue = Math.max(lowValue, highValue);

        size = Math.abs(highValue - lowValue) + 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Object getValue(int index) {
        return Integer.valueOf(lowValue + index);
    }

    @Override
    public String toString() {
        String s = "NumberRangeTerm[";

        if (name != null) {
            s += name + ", ";
        }

        s += lowValue + "->" + highValue + "]";

        return s;
    }
}
