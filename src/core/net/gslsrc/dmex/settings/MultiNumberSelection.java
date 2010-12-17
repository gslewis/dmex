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

package net.gslsrc.dmex.settings;

import net.gslsrc.dmex.random.RandomTerm;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

/**
 * Allows selection of multiple integer values from within a range.
 *
 * @author Geoff Lewis
 */
public class MultiNumberSelection extends Setting<int[]> implements RandomTerm {
    private static final long serialVersionUID = 3704306988432262079L;

    private static final int[] EMPTY_INT_ARRAY = new int[0];

    private int lowValue;
    private int highValue;

    private int[] selection;

    public MultiNumberSelection(String id, int lowValue, int highValue) {
        super(id);

        if (lowValue == highValue) {
            throw new IllegalArgumentException(
                    "Low and high values are the same");
        }

        this.lowValue = Math.min(lowValue, highValue);
        this.highValue = Math.max(lowValue, highValue);
    }

    public int getLowValue() {
        return lowValue;
    }

    public int getHighValue() {
        return highValue;
    }

    @Override
    public int[] getSelection() {
        return selection != null ? selection : EMPTY_INT_ARRAY;
    }

    @Override
    public void setSelection(int[] selection) {
        this.selection = selection;
    }

    @Override
    public void apply(String[] values) {
        if (values == null || values.length == 0) {
            setSelection(null);
        } else {
            Set<Integer> set = new TreeSet<Integer>();

            for (String s : values) {
                String ss = s.trim();
                if (ss.isEmpty()) {
                    continue;
                }

                int value = Integer.parseInt(ss);
                if (value < lowValue || value > highValue) {
                    throw new IllegalArgumentException(getId() + " value \""
                            + value + "\" out of bounds [" + lowValue + "-"
                            + highValue + "].");
                }

                set.add(Integer.valueOf(value));
            }

            int[] ia = new int[set.size()];
            int index = 0;
            for (Integer i : set) {
                ia[index] = i.intValue();
                ++index;
            }

            setSelection(ia);
        }
    }

    @Override
    public boolean isSet() {
        return getSelection().length > 0;
    }

    @Override
    public int size() {
        return getSelection().length;
    }

    @Override
    public Object getValue(int index) {
        return Integer.valueOf(getSelection()[index]);
    }

    @Override
    public String toString() {
        String s = "MultiNumberSelection[" + lowValue + "->" + highValue + "]";

        if (selection != null) {
            s += "=" + Arrays.toString(selection);
        }

        return s;
    }

    @Override
    protected ResourceBundle getResourceBundle(Locale locale) {
        return null;
    }
}
