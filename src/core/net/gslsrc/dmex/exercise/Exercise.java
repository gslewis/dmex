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

package net.gslsrc.dmex.exercise;

import net.gslsrc.dmex.settings.Settings;

import java.io.Closeable;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Basis for all exercises.
 * <p>
 * Localized message resources will be loaded from
 * "<i>&lt;classname&gt;</i>.properties".  Configuration parameters (not
 * localized) will be loaded from "<i>&lt;classname&gt;</i>.config".
 *
 * @author Geoff Lewis
 */
public abstract class Exercise implements Serializable {

    private transient Properties properties;

    protected Exercise() {}

    public abstract String getId();

    public abstract Settings newSettings();

    // session contains the settings to be filled out
    // session has "nextProblem()" method
    public abstract ExerciseSession newSession(Settings settings);

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

        String msg = null;

        try {
            ResourceBundle resources = ResourceBundle.getBundle(
                    getClass().getName(),
                    locale != null ? locale : Locale.getDefault());

            msg = resources.getString(key);

        } catch (MissingResourceException mre) {
            // Not an error if there are no resources (just use the key).
            msg = null;
        }

        if (msg == null) {
            msg = defaultMsg != null ? defaultMsg : key;
        }

        return msg;
    }

    protected String getConfigParameter(String key) {
        return getConfigParameter(key, null);
    }

    protected String getConfigParameter(String key, String defaultValue) {
        if (key == null) {
            return null;
        }

        return getProperties().getProperty(key, defaultValue);
    }

    private Properties getProperties() {
        if (properties == null) {
            synchronized (getClass()) {
                properties = new Properties();

                String filename = getClass().getSimpleName() + ".config";

                InputStream in = getClass().getResourceAsStream(filename);
                if (in != null) {
                    try {
                        properties.load(in);
                    } catch (IOException ioe) {
                        System.err.println(
                                "Failed to load config properties from "
                                + filename + " - " + ioe);
                    } finally {
                        closeQuietly(in);
                    }
                }
            }
        }

        return properties;
    }

    private static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            // CHECKSTYLE:OFF
            } catch (IOException ioe) {}
            // CHECKSTYLE:ON
        }
    }
}
