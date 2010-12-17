package net.gslsrc.dmex.random;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomSequenceTest {

    //@BeforeClass public static void setup() {}

    @Test public void testRandomSequenceEmpty() {
        RandomSequence seq = new RandomSequence();
        assertEquals("Size mismatch for empty sequence", 0, seq.size());

        try {
            seq.init();
            fail("Failed to fail on init().");
        } catch (IllegalStateException ise) {
            System.err.println("Empty: init() " + ise);
        }

        try {
            seq.next();
            fail("Failed to fail on next().");
        } catch (IllegalStateException ise) {
            System.err.println("Empty: next() " + ise);
        }

        try {
            seq.getNext();
            fail("Failed to fail on getNext().");
        } catch (IllegalStateException ise) {
            System.err.println("Empty: getNext() " + ise);
        }

        try {
            seq.get();
            fail("Failed to fail on get().");
        } catch (IllegalStateException ise) {
            System.err.println("Empty: get() " + ise);
        }
    }

    //@Ignore
    @Test public void testRandomSequenceSingleTerm() {
        RandomSequence seq = new RandomSequence();
        assertEquals("Size mismatch for empty sequence", 0, seq.size());

        seq.addTerm(new NumberRangeTerm("term1", 1, 10));
        seq.init();
        assertEquals("Size mismatch after init", 10, seq.size());

        Set<Integer> values = new HashSet<Integer>();
        for (int i = 0; i < seq.size(); ++i) {
            Object[] result = seq.getNext();

            assertNotNull(i + ": result is null", result);
            assertEquals(i + ": result size mismatch", 1, result.length);
            assertTrue(i + ": single result not an integer",
                    result[0] instanceof Integer);

            assertTrue(i + ": value is not unique",
                    values.add((Integer)result[0]));
        }

        for (int i = 0; i < seq.size(); ++i) {
            Object[] result = seq.getNext();

            assertNotNull(i + ": result is null", result);
            assertEquals(i + ": result size mismatch", 1, result.length);
            assertTrue(i + ": single result not an integer",
                    result[0] instanceof Integer);

            assertFalse(
                    "Have not exhausted unique values after size() selections",
                    values.add((Integer)result[0]));
        }
    }
}
