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

package net.gslsrc.dmex.render;

import net.gslsrc.dmex.exercise.Problem;

import java.util.Collection;

/**
 * Renders a problem representation into a particular format.  The output of
 * the rendering process is passed to the {@link RenderContext}.
 * <p>
 * The idea is that there will be abstract class implementations corresponding
 * to render contexts (ie., DOM renderer, SAX renderer, etc) that are helpers
 * for actual problem renderer implementations.  This interface will be
 * extended by each problem type, possibly just as a marker.  So each actual
 * renderer will extend an abstract context renderer and implement the
 * problem's type renderer interface.
 *
 * @param <P> the type of problem that is rendered
 *
 * @author Geoff Lewis
 */
public interface ProblemRenderer<P extends Problem> {

    void render(Collection<P> problems, RenderContext context)
        throws RenderException;
}
