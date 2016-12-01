package edu.indiana.nlp.earleyparser.chart;

import edu.indiana.nlp.Grammar;
import edu.indiana.nlp.algebra.semiring.dbl.DblSemiring;
import edu.indiana.nlp.category.Category;
import edu.indiana.nlp.earleyparser.chart.state.State;

import java.util.Collection;
import java.util.HashSet;

public class Predict {
    /**
     * Don't instantiate
     */
    private Predict() {
        throw new Error();
    }

    static void predict(int index, Grammar grammar, StateSets stateSets) {
        final DblSemiring sr = grammar.getSemiring();

        final Collection<State> statesToPredictOn = stateSets.getStatesActiveOnNonTerminals(index);
        final Collection<State.StateWithScore> newStates = new HashSet<>(20);

        // O(|stateset(i)|) = O(|grammar|): For all states <code>i: X<sub>k</sub> → λ·Zμ</code>...
        statesToPredictOn.forEach(statePredecessor -> {
            final Category Z = statePredecessor.getActiveCategory();
            double prevForward = stateSets.getForwardScore(statePredecessor);

            // For all productions Y → v such that R(Z =*L> Y) is nonzero
            grammar.getLeftStarCorners()
                    .getNonZeroScores(Z).stream()
//                    .parallel()
                    .flatMap(Y -> grammar.getRules(Y).stream()) // ?
//                    .parallel()
                    // we predict state <code>i: Y<sub>i</sub> → ·v</code>
                    .forEach(Y_to_v -> {
                        final Category Y = Y_to_v.getLeft();

                        final double innerScore;
                        final State predicted;
//                        synchronized (stateSets) {
                            predicted = stateSets.get(index, index, 0, Y_to_v); // We might want to increment the probability of an existing state
                            innerScore = stateSets.getInnerScore(predicted);
//                        }

                        // γ' = P(Y → v)
                        final double Y_to_vProbability = Y_to_v.getScore();

                        // α' = α * R(Z =*L> Y) * P(Y → v)
                        final double fw = sr.times(prevForward, grammar.getLeftStarScore(Z, Y), Y_to_vProbability);

                        if (!(Y_to_vProbability == innerScore || sr.zero() == innerScore))
                            throw new Error(Y_to_vProbability + " != " + innerScore);

//                        synchronized (stateSets) {
                            if (predicted != null) {
                                stateSets.addForwardScore(predicted, fw);
                                stateSets.setInnerScore(predicted, Y_to_vProbability);
                                stateSets.setViterbiScore(new State.ViterbiScore(Y_to_vProbability, statePredecessor, predicted, grammar.getSemiring()));
                            } else {
                                State predicted2 = State.create(index, index, 0, Y_to_v);
                                stateSets.setViterbiScore(new State.ViterbiScore(Y_to_vProbability, statePredecessor, predicted2, grammar.getSemiring()));
                                newStates.add(new State.StateWithScore(predicted2, fw, Y_to_vProbability, null));
                            }
//                        }
                    });
        });

        newStates.forEach(ss -> {
            final State state = ss.getState();
            stateSets.add(state);
            stateSets.addForwardScore(state, ss.getForwardScore());
            stateSets.setInnerScore(state, ss.getInnerScore());
        });
    }
}
