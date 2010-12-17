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

package net.gslsrc.dmex.exercise.longdiv;

import net.gslsrc.dmex.exercise.ExerciseSession;
import net.gslsrc.dmex.exercise.Problem;
import net.gslsrc.dmex.exercise.longdiv.LongDivision.Difficulty;
import net.gslsrc.dmex.random.RandomSequence;
import net.gslsrc.dmex.settings.Settings;
import net.gslsrc.dmex.settings.BooleanSelection;

/**
 * A session of long division problems.
 *
 * @author Geoff Lewis
 */
public class LongDivisionSession extends ExerciseSession {
    private static final long serialVersionUID = -5287839529491637174L;

    private Settings settings;
    private RandomSequence randomSeq;
    private boolean showHints;

    LongDivisionSession(Settings settings, boolean defaultShowHints) {
        this.settings = settings;

        String eid = LongDivision.EXERCISE_ID;

        Difficulty diff =
                (Difficulty)settings.getSetting(eid + ".difficulty")
                    .getSelection();

        randomSeq = new RandomSequence();
        randomSeq.addTerm(diff.getDividendRange());
        randomSeq.addTerm(diff.getDivisorRange());
        randomSeq.init();

        BooleanSelection hs =
                (BooleanSelection)settings.getSetting(eid + ".hints");
        if (hs != null) {
            showHints = hs.getSelection().booleanValue();
        } else {
            showHints = defaultShowHints;
        }
    }

    @Override
    public String getExerciseId() {
        return LongDivision.EXERCISE_ID;
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
        return LongDivisionProblem.class;
    }

    @Override
    protected Problem createProblem() {
        Object[] selection = randomSeq.getNext();

        int dividend = ((Integer)selection[0]).intValue();
        int divisor = ((Integer)selection[1]).intValue();

        return new LongDivisionProblem(dividend, divisor, showHints);
    }
}
