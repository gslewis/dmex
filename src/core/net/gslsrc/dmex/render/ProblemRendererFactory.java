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

import java.util.ServiceLoader;

/**
 * Basis for factories that supply a {@link ProblemRenderer} implementation
 * based on {@link RenderContext} type.
 * <p>
 * This class also provides the static {@link #getFactory} method to obtain
 * the factory for a given {@link Problem} type.  It searches all
 * {@code ProblemRendererFactory} implementations that have registered as
 * providers with the {@code "net.gslsrc.dmex.render.ProblemRendererFactory"}
 * service.
 *
 * @param <R> the type of {@link ProblemRenderer} this factory produces
 *
 * @author Geoff Lewis
 */
public abstract class ProblemRendererFactory<R extends ProblemRenderer> {

    private static ServiceLoader<ProblemRendererFactory> loader =
            ServiceLoader.load(ProblemRendererFactory.class);


    public abstract boolean rendersProblem(
            Class<? extends Problem> problemType);

    public abstract R getRenderer(Class<? extends RenderContext> contextType);

    public static ProblemRendererFactory<?> getFactory(
            Class<? extends Problem> problemType) {
        if (problemType == null) {
            throw new NullPointerException("Problem type is null");
        }

        for (ProblemRendererFactory<?> factory : loader) {
            if (factory.rendersProblem(problemType)) {
                return factory;
            }
        }

        throw new IllegalArgumentException(
                "Unsupported problem type \"" + problemType.getName() + "\"");
    }
}
