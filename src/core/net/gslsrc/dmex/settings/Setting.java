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

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
 * Basis for individual settings.
 *
 * @param <T> the setting type
 *
 * @author Geoff Lewis
 */
public abstract class Setting<T> implements Serializable {

    private String id;

    protected Setting(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Id is null or empty");
        }

        this.id = id;
    }

    public final String getId() {
        return id;
    }

    public abstract T getSelection();

    public abstract void setSelection(T selection);

    // uses a string repr to set the selection value
    // throws IllArgEx if bad string repr
    // IllArgEx message should be a prefix for a ResBundle key from the
    // owner exercise's RB: the setting id is appended to the prefix
    public abstract void apply(String[] values);

    public abstract boolean isSet();

    public String getMessage(String key) {
        return getMessage(Locale.getDefault(), key, null);
    }

    public String getMessage(Locale locale, String key) {
        return getMessage(locale, key, null);
    }

    public String getMessage(Locale locale, String key, String defaultMsg) {
        if (key == null) {
            return null;
        }

        ResourceBundle rb = getResourceBundle(locale);

        String msg = null;

        if (rb != null) {
            try {
                msg = rb.getString(key);
            } catch (MissingResourceException mre) {
                msg = null;
            }
        }

        if (msg == null) {
            msg = defaultMsg != null ? defaultMsg : key;
        }

        return msg;
    }

    protected abstract ResourceBundle getResourceBundle(Locale locale);
}
