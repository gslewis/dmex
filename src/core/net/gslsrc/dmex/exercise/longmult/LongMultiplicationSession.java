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

package net.gslsrc.dmex.exercise.longmult;

import net.gslsrc.dmex.exercise.ExerciseSession;
import net.gslsrc.dmex.exercise.Problem;
import net.gslsrc.dmex.exercise.longmult.LongMultiplication.Difficulty;
import net.gslsrc.dmex.random.RandomSequence;
import net.gslsrc.dmex.settings.Settings;

/**
 * A session of long multiplication problems.
 *
 * @author Geoff Lewis
 */
public class LongMultiplicationSession extends ExerciseSession {
    private static final long serialVersionUID = -7954862660950145192L;

    // Answer cannot be longer than 7-digits.
    private static final int MAX_ANSWER = 9999999;

    // Attempts made to find terms producing answer < MAX_ANSWER.
    private static final int PASS_LIMIT = 100;

    private Settings settings;
    private RandomSequence randomSeq;

    LongMultiplicationSession(Settings settings) {
        this.settings = settings;

        String eid = LongMultiplication.EXERCISE_ID;

        Difficulty diff =
                (Difficulty)settings.getSetting(eid + ".difficulty")
                    .getSelection();

        // The smallest problem count is 8100, so probably don't need to
        // use a RandomSequence.  Just keep track of all the used pairs.
        // However, not all combinations of multiplicand/multiplier are
        // allowed as there is a limit on the answer length (7 digits).
        randomSeq = new RandomSequence();
        randomSeq.addTerm(diff.getMultiplicandRange());
        randomSeq.addTerm(diff.getMultiplierRange());
        randomSeq.init();
    }

    @Override
    public String getExerciseId() {
        return LongMultiplication.EXERCISE_ID;
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public int getProblemCount() {
        return randomSeq.size();
    }

    @Override
    public Class<? extends Problem> getProblemType() {
        return LongMultiplicationProblem.class;
    }

    @Override
    protected Problem createProblem() {
        int multiplicand = 0;
        int multiplier = 0;
        int answer = 0;

        // Ensure the answer does not exceed the maximum number of digits
        // in length.  This restriction is required for PDF output.
        // Should be a configurable option as it won't apply for other
        // rendered outputs.
        int passCount = 0;
        do {
            ++passCount;
            if (passCount > PASS_LIMIT) {
                throw new IllegalStateException(
                        "Cannot generate terms that produce answer < "
                        + MAX_ANSWER);
            }

            Object[] selection = randomSeq.getNext();

            multiplicand = ((Integer)selection[0]).intValue();
            multiplier = ((Integer)selection[1]).intValue();

            answer = multiplicand * multiplier;
        } while (answer > MAX_ANSWER);

        return new LongMultiplicationProblem(multiplicand, multiplier);
    }
}
