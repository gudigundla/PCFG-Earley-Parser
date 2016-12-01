package edu.indiana.nlp.earleyparser.chart;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import edu.indiana.nlp.earleyparser.chart.state.State;
import edu.indiana.nlp.rule.Rule;

import java.util.HashMap;
import java.util.Map;

public class StateIndex {
    private final Map<
            Rule,
                    /*index*/
            TIntObjectMap<
                            /*rule start*/
                    TIntObjectMap<
                                    /*dot position*/
                            TIntObjectMap<
                                    State
                                    >
                            >
                    >
            > states;

    public StateIndex(int capacity) {
        states = new HashMap<>(capacity);
    }


    public State getState(Rule rule, int index, int ruleStart, int ruleDot) {
        TIntObjectMap<TIntObjectMap<State>> ruleStartToDotToState = getRuleStartToDotToState(rule, index);
        TIntObjectMap<State> ruleDotToState = getDotPositionToState(ruleStart, ruleStartToDotToState);
        return ruleDotToState.get(ruleDot);
    }

    private TIntObjectMap<State> getDotPositionToState(int ruleStart, TIntObjectMap<TIntObjectMap<State>> ruleStartToDotToState) {
        if (!ruleStartToDotToState.containsKey(ruleStart))
            ruleStartToDotToState.put(ruleStart, new TIntObjectHashMap<>(50));
        return ruleStartToDotToState.get(ruleStart);
    }

    public TIntObjectMap<TIntObjectMap<State>> getRuleStartToDotToState(Rule rule, int index) {
        if (!states.containsKey(rule)) states.put(rule, new TIntObjectHashMap<>(30));
        TIntObjectMap<TIntObjectMap<TIntObjectMap<State>>> indexToRest = states.get(rule);

        if (!indexToRest.containsKey(index)) indexToRest.put(index, new TIntObjectHashMap<>(50));
        return indexToRest.get(index);
    }

    public TIntObjectMap<State> getDotToState(Rule rule, int index, int ruleStart) {
        if (!states.containsKey(rule)) states.put(rule, new TIntObjectHashMap<>(30));
        TIntObjectMap<TIntObjectMap<TIntObjectMap<State>>> iToRest = states.get(rule);

        if (!iToRest.containsKey(index))
            iToRest.put(index, new TIntObjectHashMap<>(50));
        TIntObjectMap<TIntObjectMap<State>> ruleStartToDotToState = iToRest.get(index);

        if (!ruleStartToDotToState.containsKey(ruleStart))
            ruleStartToDotToState.put(ruleStart, new TIntObjectHashMap<>(50));
        return ruleStartToDotToState.get(ruleStart);
    }
}
