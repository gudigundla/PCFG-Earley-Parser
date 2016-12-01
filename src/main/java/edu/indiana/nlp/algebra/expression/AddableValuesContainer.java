package edu.indiana.nlp.algebra.expression;

import edu.indiana.nlp.algebra.semiring.dbl.ExpressionSemiring;
import edu.indiana.nlp.algebra.semiring.dbl.ExpressionSemiring.*;
import edu.indiana.nlp.rule.Rule;

public class AddableValuesContainer extends ScoreRefs {

    public AddableValuesContainer(int capacity, ExpressionSemiring semiring) {
        super(capacity, semiring);
    }

    public void add(Rule rule, int index, int ruleStart, int dotPosition, Value addValue) {
        Value current = getExpression(rule, index, ruleStart, dotPosition);
        final Value newValue = addValue.plus(current);
        if (current == null) {
            current = newValue;
            setScore(rule, index, ruleStart, dotPosition, current);
        } else
            current.setExpression(newValue.getExpression());
    }

}
