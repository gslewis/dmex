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

import java.io.Serializable;

/**
 * Basis for an exercise session.  A session is configured with
 * {@link Settings} and can produce a stream of {@link Problem}s.  Depending
 * on the settings, the session may be able to calculate the number of unique
 * problems it can supply (see {@link #getProblemCount}.
 *
 * @author Geoff Lewis
 */
public abstract class ExerciseSession implements Serializable {

    /**
     * The number of problems created in the session.  Automatically
     * incremented by {@link #nextProblem}.
     */
    private int created;

    /** The number of problems submitted in the session. */
    private int submitted;

    /** The number of correct problem submissions. */
    private int correct;

    /** The number of incorrect problem submissions. */
    private int incorrect;

    /** The number of problems skipped in the session. */
    private int skipped;

    private long startTime;
    private Long endTime;

    protected ExerciseSession() {
        startTime = System.currentTimeMillis();
    }

    public abstract String getExerciseId();

    public abstract Settings getSettings();

    /**
     * Gets the number of unique problems that can be generated by this
     * session.
     *
     * @return the number of unique problems or Integer.MAX_VALUE if "many"
     *         problems but exact number not calculated
     */
    public abstract int getProblemCount();

    public abstract Class<? extends Problem> getProblemType();

    public final Problem nextProblem() {
        if (endTime != null) {
            throw new IllegalStateException("Session is closed");
        }

        Problem problem = createProblem();

        if (problem != null) {
            ++created;
        }

        return problem;
    }

    protected abstract Problem createProblem();

    public synchronized void close() {
        endTime = Long.valueOf(System.currentTimeMillis());
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime != null ? endTime.longValue() : 0L;
    }

    public long getElapsedTime() {
        if (endTime != null) {
            return endTime.longValue() - startTime;
        }

        return System.currentTimeMillis() - startTime;
    }

    public final int getCreated() {
        return created;
    }

    public final int getSubmitted() {
        return submitted;
    }

    public final int getCorrect() {
        return correct;
    }

    public final int getIncorrect() {
        return incorrect;
    }

    public final int getSkipped() {
        return skipped;
    }

    /**
     * Call on submission of an answer to the current problem.  Updates the
     * submitted, correct and incorrect counts.
     *
     * @param isCorrect true if the submission was correct, false if incorrect
     */
    public final void notifySubmit(boolean isCorrect) {
        ++submitted;

        if (isCorrect) {
            ++correct;
        } else {
            ++incorrect;
        }
    }

    /**
     * Call on skip of the current problem.  Updates the skipped count.
     */
    public final void notifySkip() {
        ++skipped;
    }
}