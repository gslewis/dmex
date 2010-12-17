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
import java.util.MissingResourceException;

/**
 * Allows selection of a single enum value from an array of values.
 *
 * @param <E> the enum type
 *
 * @author Geoff Lewis
 */
public class EnumSelection<E extends Enum> extends Setting<E> {

    private E[] values;
    private E defaultValue;
    private E selection;

    public EnumSelection(String id, E[] values) {
        super(id);

        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values are null or empty");
        }

        this.values = values;
    }

    public EnumSelection(String id, E[] values, E defaultValue) {
        this(id, values);

        this.defaultValue = defaultValue;
    }

    public E[] getValues() {
        return values;
    }

    public E getDefaultValue() {
        return defaultValue;
    }

    @Override
    public E getSelection() {
        if (selection != null) {
            return selection;
        } else if (defaultValue != null) {
            return defaultValue;
        }

        return values[0];
    }

    @Override
    public void setSelection(E selection) {
        this.selection = selection;
    }

    @Override
    public void apply(String[] strs) {
        if (strs == null || strs.length == 0) {
            setSelection(null);
        } else {
            // Find the first string that matches a value in the selection.
            for (String s : strs) {
                String ss = s.trim();
                if (ss.isEmpty()) {
                    continue;
                }

                E value = getValue(ss);
                if (value != null) {
                    setSelection(value);
                    break;
                }
            }
        }
    }

    @Override
    public boolean isSet() {
        // getSelection() always returns non-null
        return true;
    }

    private E getValue(String s) {
        if (s != null) {
            for (E value : values) {
                if (value.name().equalsIgnoreCase(s)) {
                    return value;
                }
            }
        }

        return null;
    }

    @Override
    public String toString() {
        String s = "EnumSelection[" + values[0].getClass().getName() + "]";

        if (selection != null) {
            s += "=" + selection;
        }

        return s;
    }

    @Override
    protected ResourceBundle getResourceBundle(Locale locale) {
        ResourceBundle resources = null;

        // Assume resources come from the underlying Enum class.
        try {
            resources = ResourceBundle.getBundle(
                    values.getClass().getComponentType().getName(),
                    locale != null ? locale : Locale.getDefault());
        } catch (MissingResourceException mre) {
            resources = null;
        }

        return resources;
    }
}
