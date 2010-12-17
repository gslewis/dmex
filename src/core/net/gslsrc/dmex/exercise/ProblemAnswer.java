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

import java.io.Serializable;
import java.util.Set;

/**
 * Represents a submitted answer for a specific problem.  The implementation
 * should be able to return information allowing any correct partial solution
 * to the problem to be recreated.
 *
 * @author Geoff Lewis
 *
 * @param <P> the problem type to which the answer applies
 */
public abstract class ProblemAnswer<P extends Problem> implements Serializable {

    private P problem;

    protected ProblemAnswer(P problem) {
        if (problem == null) {
            throw new NullPointerException("Problem is null");
        }

        this.problem = problem;
    }

    public final P getProblem() {
        return problem;
    }

    public final String getExerciseId() {
        return problem.getExerciseId();
    }

    public abstract boolean isCorrect();

    /**
     * Gets the error message associated with this problem submission.
     *
     * @return the set of error codes or an empty set if no errors
     */
    public abstract Set<? extends ErrorCode> getErrorCodes();

    /**
     * Interface for error codes provided by answers.  The error codes are
     * used as keys to obtain localized error messages.
     *
     * @see ProblemAnswer#getErrorCodes
     */
    public interface ErrorCode {
        String name();

        /**
         * Fully-qualified key value used to look up localized messages.
         * Should comprise a {@code Problem} identifier and error code
         * {@link #name}.
         *
         * @return the code key
         */
        String key();
    }
}
