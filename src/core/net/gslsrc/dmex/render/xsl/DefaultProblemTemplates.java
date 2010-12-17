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

package net.gslsrc.dmex.render.xsl;

import net.gslsrc.dmex.exercise.Problem;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Default implementatin of the {@link ProblemTemplates}.  Builds a map of
 * {@link ProblemTemplates.OutputType}s to XSL stylesheet resources from a
 * properties resource.  All instances of the given resource on the classpath
 * are loaded.
 * <p>
 * The property "key" must resolve to a {@code OutputType} instance.  If the
 * {@code OutputType} is an enum, the property format is:
 *
 * <blockquote><code>
 * <i>enum_class_name</i>#<i>enum_name</i> = <i>xsl_stylesheet_resource</i>
 * </code></blockquote>
 *
 * @author Geoff Lewis
 */
public class DefaultProblemTemplates extends ProblemTemplates {

    private static final OutputType[] EMPTY_OUTPUT_TYPE_ARRAY =
                                                        new OutputType[0];

    private Class<? extends Problem> problemType;

    private Map<OutputType, String> stylesheets;
    private OutputType[] supportedTypes;

    protected DefaultProblemTemplates(Class<? extends Problem> problemType,
            String resourceName) {

        if (problemType == null) {
            throw new NullPointerException("Problem type is null");
        }

        this.problemType = problemType;

        if (resourceName != null) {
            loadOutputTypes(resourceName);
        }
    }

    @Override
    public boolean appliesTo(Class<? extends Problem> type) {
        return problemType.equals(type);
    }

    @Override
    public OutputType[] getSupportedTypes() {
        if (supportedTypes == null) {
            if (stylesheets != null) {
                supportedTypes = stylesheets.keySet()
                                    .toArray(EMPTY_OUTPUT_TYPE_ARRAY);
            } else {
                supportedTypes = EMPTY_OUTPUT_TYPE_ARRAY;
            }
        }

        return supportedTypes;
    }

    @Override
    public String getXSLResource(OutputType type) {
        if (type == null) {
            return null;
        }

        return stylesheets != null ? stylesheets.get(type) : null;
    }

    private void loadOutputTypes(String resourceName) {
        if (resourceName == null) {
            return;
        }

        String name = getClass().getPackage().getName().replace('.', '/');
        name += "/" + resourceName;

        Enumeration<URL> resources = null;
        try {
            resources =
                    getClass().getClassLoader().getResources(name);
        } catch (IOException ioe) {
            System.err.println("Failed to find resources '" + name
                    + "' - " + ioe);
            return;
        }

        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();

            try {
                loadOutputTypes(url.openStream());
            } catch (IOException ioe) {
                System.err.println("Failed to load output types from " + url
                        + " - " + ioe);
            }
        }
    }

    private void loadOutputTypes(InputStream in) throws IOException {
        if (in == null) {
            return;
        }

        // We need to maintain the order so we can't use Properties.
        BufferedReader rin = new BufferedReader(new InputStreamReader(in));

        String line;
        try {
            while ((line = rin.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.charAt(0) == '#') {
                    continue;
                }

                int index = line.indexOf('=');
                if (index == -1) {
                    continue;
                }

                String name = line.substring(0, index).trim();
                if (name.isEmpty()) {
                    continue;
                }

                OutputType ot = null;
                try {
                    ot = makeOutputType(name);
                // CHECKSTYLE:OFF
                } catch (Exception e) {
                    System.err.println("Failed to create OutputType form '"
                            + name + "' - " + e);

                    continue;
                }
                // CHECKSTYLE:ON

                String value = line.substring(index + 1).trim();
                if (value.isEmpty()) {
                    continue;
                }

                if (stylesheets == null) {
                    stylesheets = new LinkedHashMap<OutputType, String>();
                }

                stylesheets.put(ot, value);
            }
        } finally {
            closeQuietly(rin);
            closeQuietly(in);
        }
    }

    private OutputType makeOutputType(String value) throws Exception {
        if (value == null) {
            return null;
        }

        String s = value.trim();
        if (s.isEmpty()) {
            return null;
        }

        OutputType result = null;

        int index = s.indexOf('#');
        if (index != -1) {
            String clsname = s.substring(0, index);
            String name = s.substring(index + 1);

            result = makeEnumOutputType(clsname, name);
        } else {
            // Singleton
            throw new UnsupportedOperationException("NOT IMPLEMENTED");
        }

        return result;
    }

    // throws ClassCastEx and IllArgEx as well
    @SuppressWarnings("unchecked")
    private OutputType makeEnumOutputType(String clsname, String name)
        throws ClassNotFoundException {

        Class<?> cls = Class.forName(clsname);

        return (OutputType)Enum.valueOf((Class<? extends Enum>)cls, name);
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
