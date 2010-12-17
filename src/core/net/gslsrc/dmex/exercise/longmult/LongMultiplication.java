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

import net.gslsrc.dmex.exercise.Exercise;
import net.gslsrc.dmex.exercise.ExerciseSession;
import net.gslsrc.dmex.random.NumberRangeTerm;
import net.gslsrc.dmex.settings.EnumSelection;
import net.gslsrc.dmex.settings.Settings;

/**
 * An exercise to test long multiplication.
 *
 * @author Geoff Lewis
 */
public class LongMultiplication extends Exercise {

    /** Exercise id. */
    public static final String EXERCISE_ID = "longmult";

    private static final long serialVersionUID = 3957438624600027514L;

    /** Enumeration of exercise difficulty levels. */
    public enum Difficulty {
        EASIEST(10, 99, 10, 99),
        EASY(10, 999, 10, 99),
        MEDIUM(100, 999, 10, 999),
        HARD(100, 9999, 10, 999),
        HARDEST(1000, 9999, 100, 9999);

        private NumberRangeTerm multiplicandRange;
        private NumberRangeTerm multiplierRange;

        Difficulty(int lowMultiplicand, int highMultiplicand,
                int lowMultiplier, int highMultiplier) {
            multiplicandRange =
                    new NumberRangeTerm(lowMultiplicand, highMultiplicand);
            multiplierRange =
                    new NumberRangeTerm(lowMultiplier, highMultiplier);
        }

        public NumberRangeTerm getMultiplicandRange() {
            return multiplicandRange;
        }

        public NumberRangeTerm getMultiplierRange() {
            return multiplierRange;
        }
    };

    public LongMultiplication() {}

    @Override
    public String getId() {
        return EXERCISE_ID;
    }

    @Override
    public Settings newSettings() {
        return new Settings(getId()).add(
                new EnumSelection<Difficulty>(getId() + ".difficulty",
                                              Difficulty.values()));
    }

    @Override
    public ExerciseSession newSession(Settings settings) {
        return new LongMultiplicationSession(settings);
    }

    @Override
    public String toString() {
        return "LongMultiplication";
    }
}
