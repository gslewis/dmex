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

import net.gslsrc.dmex.exercise.Problem;

/**
 * A single times tables revision problem.
 *
 * @author Geoff Lewis
 */
public final class TablesRevisionProblem extends Problem {
    private static final long serialVersionUID = -2232250799312194415L;

    /** Enumeration of the fields in a times tables problem. */
    public enum Field {
        TERM1,
        TERM2,
        ANSWER;
    }

    private int term1;
    private int term2;
    private int answer;

    private Field blank = Field.ANSWER;
    private Field fraction;

    private boolean divide;

    private boolean flip;

    private TablesRevisionProblem() {
        super(TablesRevision.EXERCISE_ID);
    }

    public boolean isCorrect(int value) {
        // answer should match whichever field is blank
        return value == getValue(blank != null ? blank : Field.ANSWER);
    }

    public int getValue(Field field) {
        if (field == null) {
            throw new NullPointerException("Field is null");
        }

        int value = 0;

        switch (field) {
            case TERM1:
                value = getTerm1();
                break;
            case TERM2:
                value = getTerm2();
                break;
            case ANSWER:
                value = getAnswer();
                break;
            default:
                throw new Error("Unexpected field");
        }

        return value;
    }

    public int getTerm1() {
        return term1;
    }

    public void setTerm1(int term1) {
        this.term1 = term1;
    }

    public void setFractionTerm1(int term1) {
        this.term1 = term1;
        fraction = Field.TERM1;
    }

    public int getTerm2() {
        return term2;
    }

    public void setTerm2(int term2) {
        this.term2 = term2;
    }

    public void setFractionTerm2(int term2) {
        this.term2 = term2;
        fraction = Field.TERM2;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public Field getBlank() {
        return blank != null ? blank : Field.ANSWER;
    }

    public void setBlank(Field blank) {
        this.blank = blank;
    }

    public Field getFraction() {
        return fraction;
    }

    public void setFraction(Field fraction) {
        if (Field.ANSWER.equals(fraction)) {
            throw new IllegalArgumentException("Answer cannot be a fraction");
        }

        this.fraction = fraction;
    }

    public boolean isDivide() {
        return divide;
    }

    public void setDivide(boolean divide) {
        this.divide = divide;
    }

    public boolean getFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TablesRevisionProblem[");

        if (flip) {
            sb.append(makeRHS()).append(" = ").append(makeLHS());
        } else {
            sb.append(makeLHS()).append(" = ").append(makeRHS());
        }

        sb.append("]");

        return sb.toString();
    }

    private String makeLHS() {
        StringBuilder sb = new StringBuilder();

        sb.append(makeTerm(getTerm1(), Field.TERM1));

        if (isDivide()) {
            sb.append(" / ");
        } else {
            sb.append(" * ");
        }

        sb.append(makeTerm(getTerm2(), Field.TERM2));

        return sb.toString();
    }

    private String makeTerm(int value, Field field) {
        StringBuilder sb = new StringBuilder();

        if (field.equals(fraction)) {
            sb.append("1/");
        }

        sb.append(makeField(value, field));

        return sb.toString();
    }

    private String makeRHS() {
        return makeField(getAnswer(), Field.ANSWER);
    }

    private String makeField(int value, Field field) {
        StringBuilder sb = new StringBuilder();

        if (field.equals(blank)) {
            sb.append("{").append(value).append("}");
        } else {
            sb.append(value);
        }

        return sb.toString();
    }

    public static TablesRevisionProblem createProblem(int type, int term1,
            int term2, boolean flip) {
        TablesRevisionProblem problem = new TablesRevisionProblem();

        problem.setFlip(flip);

        switch (type) {
            case 0:
                problem.setTerm1(term1);
                problem.setTerm2(term2);
                problem.setAnswer(term1 * term2);
                break;
            case 1:
                problem.setTerm1(term2);
                problem.setTerm2(term1);
                problem.setAnswer(term1 * term2);
                break;
            case 2:
                problem.setTerm1(term1);
                problem.setTerm2(term2);
                problem.setAnswer(term1 * term2);
                problem.setBlank(Field.TERM2);
                break;
            case 3:
                problem.setTerm1(term2);
                problem.setTerm2(term1);
                problem.setAnswer(term1 * term2);
                problem.setBlank(Field.TERM2);
                break;
            case 4:
                problem.setTerm1(term1);
                problem.setTerm2(term2);
                problem.setAnswer(term1 * term2);
                problem.setBlank(Field.TERM1);
                break;
            case 5:
                problem.setTerm1(term2);
                problem.setTerm2(term1);
                problem.setAnswer(term1 * term2);
                problem.setBlank(Field.TERM1);
                break;
            case 6:
                problem.setTerm1(term1 * term2);
                problem.setTerm2(term1);
                problem.setAnswer(term2);
                problem.setDivide(true);
                break;
            case 7:
                problem.setTerm1(term1 * term2);
                problem.setTerm2(term1);
                problem.setAnswer(term2);
                problem.setDivide(true);
                problem.setBlank(Field.TERM2);
                break;
            case 8:
                problem.setTerm1(term1 * term2);
                problem.setTerm2(term1);
                problem.setAnswer(term2);
                problem.setDivide(true);
                problem.setBlank(Field.TERM1);
                break;
            case 9:
                problem.setTerm1(term1 * term2);
                problem.setFractionTerm2(term1);
                problem.setAnswer(term2);
                break;
            case 10:
                problem.setFractionTerm1(term1);
                problem.setTerm2(term1 * term2);
                problem.setAnswer(term2);
                break;
            case 11:
                problem.setTerm1(term1 * term2);
                problem.setFractionTerm2(term1);
                problem.setAnswer(term2);
                problem.setBlank(Field.TERM1);
                break;
            case 12:
                problem.setTerm1(term1 * term2);
                problem.setFractionTerm2(term1);
                problem.setAnswer(term2);
                problem.setBlank(Field.TERM2);
                break;
            case 13:
                problem.setTerm1(term1);
                problem.setFractionTerm2(term2);
                problem.setAnswer(term1 * term2);
                problem.setDivide(true);
                break;
            case 14:
                problem.setTerm1(term1);
                problem.setFractionTerm2(term2);
                problem.setAnswer(term1 * term2);
                problem.setDivide(true);
                problem.setBlank(Field.TERM1);
                break;
            case 15:
                problem.setTerm1(term1);
                problem.setFractionTerm2(term2);
                problem.setAnswer(term1 * term2);
                problem.setDivide(true);
                problem.setBlank(Field.TERM1);
                break;
            default:
                throw new IllegalArgumentException(
                        "Unknown problem type \"" + type + "\"");
        }

        return problem;
    }
}
