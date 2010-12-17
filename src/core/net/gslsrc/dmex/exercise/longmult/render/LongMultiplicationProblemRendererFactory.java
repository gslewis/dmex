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

package net.gslsrc.dmex.exercise.longmult.render;

import net.gslsrc.dmex.exercise.Problem;
import net.gslsrc.dmex.exercise.longmult.LongMultiplicationProblem;
import net.gslsrc.dmex.render.ProblemRendererFactory;
import net.gslsrc.dmex.render.RenderContext;

import java.util.ServiceLoader;

/**
 * Factory for obtaining a {@link LongMultiplicationProblemRenderer} for a
 * given output type.
 *
 * @author Geoff Lewis
 */
public final class LongMultiplicationProblemRendererFactory
    extends ProblemRendererFactory<LongMultiplicationProblemRenderer> {

    private static ServiceLoader<LongMultiplicationProblemRenderer> loader =
            ServiceLoader.load(LongMultiplicationProblemRenderer.class);

    private static LongMultiplicationProblemRendererFactory instance;

    public LongMultiplicationProblemRendererFactory() {}

    public static LongMultiplicationProblemRendererFactory getInstance() {
        if (instance == null) {
            instance = new LongMultiplicationProblemRendererFactory();
        }

        return instance;
    }

    @Override
    public boolean rendersProblem(Class<? extends Problem> problemType) {
        return LongMultiplicationProblem.class.equals(problemType);
    }

    @Override
    public LongMultiplicationProblemRenderer getRenderer(
            Class<? extends RenderContext> contextType) {
        if (contextType == null) {
            throw new NullPointerException("Render context type is null");
        }

        for (LongMultiplicationProblemRenderer renderer : loader) {
            if (renderer.rendersContext(contextType)) {
                return renderer;
            }
        }

        throw new IllegalArgumentException(
                "Unsupported renderer context type \""
                + contextType.getName() + "\"");
    }
}
