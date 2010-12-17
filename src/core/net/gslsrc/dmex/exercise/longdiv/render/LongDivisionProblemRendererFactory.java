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

package net.gslsrc.dmex.exercise.longdiv.render;

import net.gslsrc.dmex.exercise.Problem;
import net.gslsrc.dmex.exercise.longdiv.LongDivisionProblem;
import net.gslsrc.dmex.render.ProblemRendererFactory;
import net.gslsrc.dmex.render.RenderContext;

import java.util.ServiceLoader;

/**
 * Factory for obtaining a {@link LongDivisionProblemRenderer} for a
 * given output type.
 *
 * @author Geoff Lewis
 */
public final class LongDivisionProblemRendererFactory
    extends ProblemRendererFactory<LongDivisionProblemRenderer> {

    private static ServiceLoader<LongDivisionProblemRenderer> loader =
            ServiceLoader.load(LongDivisionProblemRenderer.class);

    private static LongDivisionProblemRendererFactory instance;

    public LongDivisionProblemRendererFactory() {}

    public static LongDivisionProblemRendererFactory getInstance() {
        if (instance == null) {
            instance = new LongDivisionProblemRendererFactory();
        }

        return instance;
    }

    @Override
    public boolean rendersProblem(Class<? extends Problem> problemType) {
        return LongDivisionProblem.class.equals(problemType);
    }

    @Override
    public LongDivisionProblemRenderer getRenderer(
            Class<? extends RenderContext> contextType) {
        if (contextType == null) {
            throw new NullPointerException("Render context type is null");
        }

        for (LongDivisionProblemRenderer renderer : loader) {
            if (renderer.rendersContext(contextType)) {
                return renderer;
            }
        }

        throw new IllegalArgumentException(
                "Unsupported renderer context type \""
                + contextType.getName() + "\"");
    }
}
