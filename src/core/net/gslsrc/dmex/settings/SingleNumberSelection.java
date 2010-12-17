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

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Allows selection of a single integer from within a range.
 *
 * @author Geoff Lewis
 */
public class SingleNumberSelection extends Setting<Integer> {
    private static final long serialVersionUID = -3663809564042696986L;

    private int lowValue;
    private int highValue;

    private Integer defaultValue;

    private Integer selection;

    public SingleNumberSelection(String id, int lowValue, int highValue) {
        super(id);

        if (lowValue == highValue) {
            throw new IllegalArgumentException(
                    "Low and high values are the same");
        }

        this.lowValue = Math.min(lowValue, highValue);
        this.highValue = Math.max(lowValue, highValue);
    }

    public SingleNumberSelection(String id, int lowValue, int highValue,
            int defaultValue) {
        this(id, lowValue, highValue);

        this.defaultValue = Integer.valueOf(defaultValue);
    }

    @Override
    public Integer getSelection() {
        if (selection != null) {
            return selection;
        } else if (defaultValue != null) {
            return defaultValue;
        }

        return Integer.valueOf(lowValue);
    }

    @Override
    public void setSelection(Integer selection) {
        this.selection = selection;
    }

    @Override
    public void apply(String[] values) {
        if (values == null || values.length == 0) {
            setSelection(null);
        } else if (values.length > 1) {
            throw new IllegalArgumentException(getId()
                    + " expected single value, got " + values.length
                    + " values.");
        } else {
            Integer value = null;
            try {
                value = Integer.valueOf(values[0]);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException(getId()
                        + " invalid value \"" + values[0] + "\"");
            }

            if (value.intValue() < lowValue || value.intValue() > highValue) {
                throw new IllegalArgumentException(getId() + " value \""
                        + value + "\" out of bounds [" + lowValue + "-"
                        + highValue + "].");
            }

            setSelection(value);
        }
    }

    @Override
    public boolean isSet() {
        // getSelection() always returns a value.
        return true;
    }

    @Override
    public String toString() {
        String s = "SingleNumberSelection[" + lowValue + "->" + highValue + "]";

        if (selection != null) {
            s += "=" + selection;
        }

        return s;
    }

    @Override
    protected ResourceBundle getResourceBundle(Locale locale) {
        return null;
    }
}
