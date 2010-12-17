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

import net.gslsrc.dmex.exercise.ProblemAnswer;

import java.util.Collections;
import java.util.Set;

/**
 * Represents a submitted answer for a tables revision problem.
 *
 * @author Geoff Lewis
 */
public class TablesRevisionProblemAnswer
    extends ProblemAnswer<TablesRevisionProblem> {

    private static final long serialVersionUID = -2654739573597796124L;

    /** The error types. */
    public enum ErrorType implements ProblemAnswer.ErrorCode {
        /** The answer field is empty. */
        EMPTY,

        /** The answer field is not a number. */
        NON_NUMBER,

        /** The answer is a number but not correct. */
        WRONG;

        @Override
        public String key() {
            return "error.problem." + TablesRevision.EXERCISE_ID
                    + "." + name();
        }
    }

    private Integer answer;
    private ErrorType errorType;

    public TablesRevisionProblemAnswer(TablesRevisionProblem problem) {
        super(problem);
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;

        if (answer != null) {
            if (getProblem().isCorrect(answer.intValue())) {
                errorType = null;
            } else {
                errorType = ErrorType.WRONG;
            }
        }
    }

    @Override
    public boolean isCorrect() {
        return errorType == null;
    }

    @Override
    public Set<? extends ErrorCode> getErrorCodes() {
        if (isCorrect()) {
            return Collections.emptySet();
        }

        return Collections.singleton(errorType);
    }
}
