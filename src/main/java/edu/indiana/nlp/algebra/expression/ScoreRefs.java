package edu.indiana.nlp.algebra.expression;

import edu.indiana.nlp.algebra.semiring.dbl.ExpressionSemiring;
import edu.indiana.nlp.algebra.semiring.dbl.ExpressionSemiring.Value;
import edu.indiana.nlp.earleyparser.chart.StateMap;
import edu.indiana.nlp.earleyparser.chart.state.State;
import edu.indiana.nlp.errors.IssueRequest;
import edu.indiana.nlp.rule.Rule;

public class ScoreRefs {
    private final StateMap states;
    private final ExpressionSemiring semiring;

    public ScoreRefs(int capacity, ExpressionSemiring semiring) {
        states = new StateMap(capacity);
        this.semiring = semiring;
    }

    public Value getExpression(Rule rule, int index, int ruleStart, int dot) {
        return states.getDotPositionToScore(rule, index, ruleStart).get(dot);
    }

    void setScore(Rule rule, int index, int ruleStart, int dotPosition, Value set) {
        states.getDotPositionToScore(rule, index, ruleStart)
                .put(dotPosition, set);
    }

    public StateMap getStates() {
        return states;
    }

    public Value getExpression(State state) {
        return getExpression(state.getRule(), state.getPosition(), state.getRuleStartPosition(), state.getRuleDotPosition());
    }

    public Value getOrCreate(State state, double defaultValue) {
        Value exp = getExpression(state);
        if (exp == null) {
            setScore(state, semiring.dbl(defaultValue));
            exp = getExpression(state);
            if (exp == null) throw new IssueRequest("expression should not be null");
            return exp;
        } else return exp;
    }

    private void setScore(State state, Value expression) {
        setScore(state.getRule(), state.getPosition(), state.getRuleStartPosition(), state.getRuleDotPosition(), expression);
    }
}
