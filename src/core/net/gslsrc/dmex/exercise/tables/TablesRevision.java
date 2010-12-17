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

import net.gslsrc.dmex.exercise.Exercise;
import net.gslsrc.dmex.exercise.ExerciseSession;
import net.gslsrc.dmex.settings.EnumSelection;
import net.gslsrc.dmex.settings.MultiNumberSelection;
import net.gslsrc.dmex.settings.Settings;

/**
 * An exercise to test times tables.
 *
 * @author Geoff Lewis
 */
public class TablesRevision extends Exercise {

    /** Exercise id. */
    public static final String EXERCISE_ID = "tablesrev";

    private static final long serialVersionUID = -8303102965899114659L;

    /** Enumeration of exercise difficulty levels. */
    public enum Difficulty {
        EASIEST(1),
        EASY(6),
        MEDIUM(8),
        HARD(12),
        HARDEST(15);

        private int maxType;

        Difficulty(int maxType) {
            this.maxType = maxType;
        }

        public int getMaxType() {
            return maxType;
        }
    };

    public TablesRevision() {}

    @Override
    public String getId() {
        return EXERCISE_ID;
    }

    @Override
    public Settings newSettings() {
        return new Settings(getId())
            .add(new MultiNumberSelection(getId() + ".term1", 1, 12))
            .add(new MultiNumberSelection(getId() + ".term2", 1, 12))
            .add(new EnumSelection<Difficulty>(getId() + ".difficulty",
                                              Difficulty.values()));
    }

    @Override
    public ExerciseSession newSession(Settings settings) {
        if (settings == null) {
            throw new NullPointerException("Settings are null");
        }

        return new TablesRevisionSession(settings);
    }

    @Override
    public String toString() {
        return "TablesRevision";
    }
}
