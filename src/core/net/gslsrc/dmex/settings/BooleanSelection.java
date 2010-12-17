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
 * Allows selection of a true/false value.
 *
 * @author Geoff Lewis
 */
public class BooleanSelection extends Setting<Boolean> {
    private static final long serialVersionUID = 9068658249634961565L;

    private boolean defaultValue;
    private Boolean selection;

    public BooleanSelection(String id) {
        this(id, false);
    }

    public BooleanSelection(String id, boolean defaultValue) {
        super(id);

        this.defaultValue = defaultValue;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Boolean getSelection() {
        return selection != null ? selection
                                 : Boolean.valueOf(getDefaultValue());
    }

    @Override
    public void setSelection(Boolean selection) {
        this.selection = selection;
    }

    @Override
    public void apply(String[] strs) {
        if (strs == null || strs.length == 0) {
            setSelection(null);
        } else {
            setSelection(Boolean.valueOf(strs[0]));
        }
    }

    @Override
    public boolean isSet() {
        return true;
    }

    @Override
    public String toString() {
        return "BooleanSelection[" + getId() + "=" + getSelection() + "]";
    }

    @Override
    protected ResourceBundle getResourceBundle(Locale locale) {
        return null;
    }
}
