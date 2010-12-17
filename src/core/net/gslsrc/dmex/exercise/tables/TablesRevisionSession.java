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

package net.gslsrc.dmex.exercise.tables;

import net.gslsrc.dmex.exercise.ExerciseSession;
import net.gslsrc.dmex.exercise.Problem;
import net.gslsrc.dmex.exercise.tables.TablesRevision.Difficulty;
import net.gslsrc.dmex.random.BooleanTerm;
import net.gslsrc.dmex.random.NumberRangeTerm;
import net.gslsrc.dmex.random.RandomSequence;
import net.gslsrc.dmex.settings.MultiNumberSelection;
import net.gslsrc.dmex.settings.Settings;

/**
 * A session of times tables revision.
 *
 * @author Geoff Lewis
 */
public class TablesRevisionSession extends ExerciseSession {
    private static final long serialVersionUID = 305351359190292585L;

    private Settings settings;
    private RandomSequence randomSeq;

    TablesRevisionSession(Settings settings) {
        this.settings = settings;

        randomSeq = new RandomSequence();

        String eid = TablesRevision.EXERCISE_ID;

        // Randomly select terms from the given values in the settings.
        randomSeq.addTerm(
                settings.getSetting(MultiNumberSelection.class,
                                    eid + ".term1"));
        randomSeq.addTerm(
                settings.getSetting(MultiNumberSelection.class,
                                    eid + ".term2"));

        // Use difficulty selection to get range of problem types from
        // which we can randomly select a type.
        Difficulty diff =
                (Difficulty)settings.getSetting(eid + ".difficulty")
                    .getSelection();
        randomSeq.addTerm(
                new NumberRangeTerm("type", 0, diff.getMaxType()));

        // Randomly decide whether to flip LHS and RHS of equation.
        randomSeq.addTerm(new BooleanTerm("flip"));

        randomSeq.init();
    }

    @Override
    public String getExerciseId() {
        return TablesRevision.EXERCISE_ID;
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
        return TablesRevisionProblem.class;
    }

    @Override
    protected Problem createProblem() {
        Object[] selection = randomSeq.getNext();

        int t1 = ((Integer)selection[0]).intValue();
        int t2 = ((Integer)selection[1]).intValue();
        int type = ((Integer)selection[2]).intValue();
        boolean flip = ((Boolean)selection[3]).booleanValue();

        return TablesRevisionProblem.createProblem(type, t1, t2, flip);
    }
}
