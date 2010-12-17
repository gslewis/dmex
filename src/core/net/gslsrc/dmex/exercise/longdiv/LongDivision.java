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

import net.gslsrc.dmex.exercise.Exercise;
import net.gslsrc.dmex.exercise.ExerciseSession;
import net.gslsrc.dmex.random.NumberRangeTerm;
import net.gslsrc.dmex.settings.Settings;
import net.gslsrc.dmex.settings.BooleanSelection;
import net.gslsrc.dmex.settings.EnumSelection;

/**
 * An exercise to test long division.
 *
 * @author Geoff Lewis
 */
public class LongDivision extends Exercise {

    public static final String EXERCISE_ID = "longdiv";

    public static final String CONFIG_HINTS_ENABLED =
            "longdiv.hints.enabled";
    public static final String CONFIG_HINTS_DEFAULT =
            "longdiv.hints.default";

    private static final boolean DEFAULT_SHOW_HINTS = false;

    private static final long serialVersionUID = -8063283698554907990L;

    /**
     * Enumeration of difficulties including the possible dividend/divisor.
     */
    public enum Difficulty {
        EASIEST(14, 19, 11, 13),
        EASY(101, 999, 14, 19),
        MEDIUM(101, 999, 21, 39),
        HARD(1001, 9999, 41, 99),
        HARDEST(10001, 99999, 51, 499);

        private NumberRangeTerm dividendRange;
        private NumberRangeTerm divisorRange;

        Difficulty(int lowDividend, int highDividend, int lowDivisor,
                int highDivisor) {
            dividendRange = new NumberRangeTerm(lowDividend, highDividend);
            divisorRange = new NumberRangeTerm(lowDivisor, highDivisor);
        }

        public NumberRangeTerm getDividendRange() {
            return dividendRange;
        }

        public NumberRangeTerm getDivisorRange() {
            return divisorRange;
        }
    };

    public LongDivision() {}

    @Override
    public String getId() {
        return EXERCISE_ID;
    }

    @Override
    public Settings newSettings() {
        Settings settings = new Settings(getId());

        settings.add(new EnumSelection<Difficulty>(getId() + ".difficulty",
                                                   Difficulty.values()));

        String hintsEnabled = getConfigParameter(CONFIG_HINTS_ENABLED);
        if (hintsEnabled == null || Boolean.parseBoolean(hintsEnabled)) {
            settings.add(new BooleanSelection(getId() + ".hints",
                        getBoolean(CONFIG_HINTS_DEFAULT, DEFAULT_SHOW_HINTS)));
        }

        return settings;
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        String value = getConfigParameter(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }

        return defaultValue;
    }

    @Override
    public ExerciseSession newSession(Settings settings) {
        return new LongDivisionSession(settings,
                getBoolean(CONFIG_HINTS_DEFAULT, DEFAULT_SHOW_HINTS));
    }

    @Override
    public String toString() {
        return "LongDivision";
    }
}
