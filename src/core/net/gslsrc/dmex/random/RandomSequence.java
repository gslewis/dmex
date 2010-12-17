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

package net.gslsrc.dmex.random;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.Random;

/**
 * Generates non-repeating sequences if values from a list of
 * {@link RandomTerm}s.  The terms may either be added in the constructor or
 * appended using {@link #addTerm}.  In the latter case, {@link #init} should
 * be called when all terms have been added (otherwise it will be called on
 * first call to {@link #getNext}).
 * <p>
 * The size of the pool of sequences is calculated given the number of
 * possible values in each term.  This class attempts to ensure that no
 * sequence is repeated in "size" calls to {@link #getNext}.  It will fail if:
 * <ul>
 * <li>a term has duplicate values: different random arrangements will produce
 * the same result</li>
 * <li>the size of the pool is very large and the number of generated results
 * approaches the pool size: it becomes harder to randomly locate an unused
 * result</li>
 * </ul>
 *
 * The first case is faulty input.  The second case is incorrect use of this
 * class which is only suitable for relatively small pools or relatively small
 * numbers of generated selections.
 * <p>
 * For sufficiently small pools, all arrangements of the terms are
 * precalculated.  Otherwise the selections are randomly generated and if/when
 * all arrangements have been generated, we switch to precalculated mode.
 *
 * @author Geoff Lewis
 */
public class RandomSequence implements Serializable {
    private static final long serialVersionUID = 8438798313246814155L;

    /**
     * If the number of possible selection is less than this limit,
     * precalculate all selections.
     */
    private static final int PRECALC_LIMIT = 100;

    /**
     * Number of passes made in an attempt to find an unused selection when
     * randomly generating selections.  See {@link #nextGenerated}.  If this
     * limit is being hit, we shouldn't be using this class to produce
     * sequences.
     */
    private static final int GENERATED_PASS_LIMIT = 100;

    private Random rng;

    private List<RandomTerm> termList;

    // The number of unique selections that can be supplied by the sequence.
    private int size;

    // If size small enough to precalculate, all possible arrangements are
    // calcualted and placed in unusedSelections list.  Random selection then
    // just involves picking unused index from this list.
    private boolean precalculated;

    private List<Object[]> unusedSelections;

    // If not precalculated, collect all selections until size() is reached,
    // then switch to precalculated mode.
    private List<Object[]> usedSelections;

    private Set<String> generatedResults;

    private boolean initialized;

    private Object[] currentSelection;

    public RandomSequence() {
        this((Random)null);
    }

    public RandomSequence(Random rng) {
        this.rng = rng != null ? rng : makeRandom();
    }

    public RandomSequence(RandomTerm... terms) {
        this(null, terms);
    }

    public RandomSequence(Random rng, RandomTerm... terms) {
        this(rng);

        if (terms == null) {
            throw new NullPointerException("Terms are null");
        }

        if (terms.length == 0) {
            throw new IllegalArgumentException("Terms are empty");
        }

        // does this need to be array list?
        termList = new ArrayList<RandomTerm>(Arrays.asList(terms));
    }

    public RandomSequence addTerm(RandomTerm term) {
        if (term == null) {
            throw new NullPointerException("Term is null");
        }

        if (termList == null) {
            termList = new ArrayList<RandomTerm>();
        }

        termList.add(term);

        return this;
    }

    public RandomSequence init() {
        if (termList == null || termList.isEmpty()) {
            throw new IllegalStateException(
                    "RandomSequence has no random terms");
        }

        size = 1;
        for (RandomTerm term : termList) {
            size *= term.size();
        }

        if (size == 0) {
            throw new IllegalStateException(
                    "Random terms produce zero sequence size");
        }

        usedSelections = newList();

        if (size < PRECALC_LIMIT) {
            unusedSelections = newList();
            precalculate(unusedSelections, 0, new LinkedList<Object>());

            if (unusedSelections.size() == 0) {
                throw new IllegalStateException(
                        "Precalculation failed to produce any sequences");
            }

            precalculated = true;
        }

        initialized = true;

        return this;
    }

    /**
     * Gets the number of random selections that can be provided by this
     * sequence.
     *
     * @return the size of the random sequence
     */
    public int size() {
        return size;
    }

    /**
     * Gets the next selection of random term values.  The selection becomes
     * the current selection which can be repeatedly retrieved using
     * {@link #get()}.
     *
     * @return the next selection of random term values
     */
    public Object[] getNext() {
        next();

        return currentSelection;
    }

    /**
     * Advance to the next selection of random term values.  The selection can
     * then be retrieved using {@link #get()}.  To advance <i>and</i>
     * retrieve, use {@link #getNext}.
     */
    public void next() {
        if (!initialized) {
            init();
        }

        currentSelection = precalculated ? nextArrangement() : nextGenerated();
    }

    /**
     * Gets the current selection without advancing to the next selection.
     * (Unless there is no current selection, in which case {@link #next} is
     * called first.)
     *
     * @return the current selection of random term values
     */
    public Object[] get() {
        if (currentSelection == null) {
            next();
        }

        return currentSelection;
    }

    private Object[] nextArrangement() {
        Object[] result = null;

        // If we have exhausted the unused selections, swap with the used
        // selections.
        if (unusedSelections.isEmpty()) {
            List<Object[]> tmp = unusedSelections;
            unusedSelections = usedSelections;
            usedSelections = tmp;
        }

        int index = unusedSelections.size() > 1
                ? rng.nextInt(unusedSelections.size()) : 0;

        result = unusedSelections.remove(index);
        usedSelections.add(result);

        return result;
    }

    private Object[] nextGenerated() {
        Object[] result = new Object[termList.size()];
        String key = null;
        int pass = 0;

        do {
            if (++pass > GENERATED_PASS_LIMIT) {
                // Give up and use what we've got.  If it's taken this many
                // passes, assume the pool of generated values is big so a
                // duplicate isn't going to be spotted (easily).  If this is
                // happening, you probably shouldn't be using this class.
                break;
            }

            for (int i = 0; i < termList.size(); ++i) {
                RandomTerm term = termList.get(i);

                int index = rng.nextInt(term.size());
                result[i] = term.getValue(index);
            }

            key = makeKey(result);
        } while (generatedResults != null && generatedResults.contains(key));

        if (generatedResults == null) {
            generatedResults = new HashSet<String>();
        }

        // Don't add the result if it's already been generated and we've timed
        // out trying to find an unused selection.
        if (generatedResults.add(key)) {
            usedSelections.add(result);
        }

        // If we have generated all possible selections, switch to
        // precalculated mode.
        if (generatedResults.size() == size()) {
            unusedSelections = usedSelections;
            usedSelections = newList();
            precalculated = true;
        }

        return result;
    }

    private void precalculate(List<Object[]> list, int index,
            Deque<Object> stack) {
        assert termList != null;

        if (index == termList.size()) {
            list.add(stack.toArray());
            return;
        }

        RandomTerm term = termList.get(index);
        for (int i = 0; i < term.size(); ++i) {
            stack.addLast(term.getValue(i));
            precalculate(list, index + 1, stack);
            stack.removeLast();
        }
    }

    private List<Object[]> newList() {
        return new ArrayList<Object[]>();
    }

    private Random makeRandom() {
        return new Random();
    }

    private String makeKey(Object[] selection) {
        assert selection != null;

        StringBuilder sb = new StringBuilder();
        for (Object value : selection) {
            // Assumes values don't contain commas.
            if (sb.length() > 0) {
                sb.append(',');
            }

            sb.append(value);
        }

        return sb.toString();
    }
}
