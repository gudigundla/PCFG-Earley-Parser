package edu.indiana.nlp.earleyparser;

import edu.indiana.nlp.Grammar;
import edu.indiana.nlp.algebra.semiring.dbl.DblSemiring;
import edu.indiana.nlp.category.Category;
import edu.indiana.nlp.category.nonterminal.NonTerminal;
import edu.indiana.nlp.category.terminal.Terminal;
import edu.indiana.nlp.earleyparser.chart.Chart;
import edu.indiana.nlp.earleyparser.chart.state.ScannedTokenState;
import edu.indiana.nlp.earleyparser.chart.state.State;
import edu.indiana.nlp.earleyparser.parse.ParseTree;
import edu.indiana.nlp.earleyparser.parse.ScanProbability;
import edu.indiana.nlp.errors.IssueRequest;
import edu.indiana.nlp.rule.Rule;
import edu.indiana.nlp.token.Token;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class Parser {

    /**
     * Parses the given list of tokens and returns he parse probability
     *
     * @param goal    Goal category, typically S for Sentence
     * @param grammar Grammar to apply to tokens
     * @param tokens  list of tokens to parse
     * @return Probability that given string of tokens mathces gven non-terminal with given grammar
     */
    public static <E> double recognize(NonTerminal goal,
                                       Grammar grammar,
                                       Iterable<Token<E>> tokens) {
        final ChartWithInputPosition parse = parseAndCountTokens(goal, grammar, tokens, null);
        final Collection<State> completedStates = parse.chart.stateSets.getCompletedStates(parse.index, Category.START);
        if (completedStates.size() > 0) {
            if (completedStates.size() > 1)
                throw new IssueRequest("Multiple final states found. This is likely an error.");
            return completedStates.stream().mapToDouble(finalState ->
                    grammar.getSemiring().toProbability(
                            parse.chart.getForwardScore(finalState)
                    )).sum();
        } else {
            return 0.0;
        }
    }


    /**
     * Performs the backward part of the forward-backward algorithm
     * <p/>
     * TODO make this an iterative algorithm instead of recursive: might be more efficient?
     */
    public static ParseTree getViterbiParse(State state, Chart chart) {
        if (state.getRuleDotPosition() <= 0)
            // Prediction state
            return new ParseTree.NonToken(state.getRule().getLeft());
        else {
            Category prefixEnd = state.getRule().getRight()[state.getRuleDotPosition() - 1];
            if (prefixEnd instanceof Terminal) {
                // Scanned terminal state
                if (!(state instanceof ScannedTokenState))
                    throw new IssueRequest("Expected state to be a scanned state. This is a bug.");

                // let \'a = \, call
                ParseTree T = getViterbiParse(
                        chart.stateSets.get(
                                state.getPosition() - 1,
                                state.getRuleStartPosition(),
                                state.getRuleDotPosition() - 1,
                                state.getRule()
                        ),
                        chart
                );
                final ScannedTokenState scannedState = (ScannedTokenState) state;
                //noinspection unchecked
                T.addRightMost(new ParseTree.Token<>(scannedState));
                return T;
            } else {
                if (!(prefixEnd instanceof NonTerminal)) throw new IssueRequest("Something went terribly wrong.");

                // Completed non-terminal state
                State.ViterbiScore viterbi = chart.getViterbiScore(state); // must exist

                // Completed state that led to the current state
                State origin = viterbi.getOrigin();

                // Recurse for predecessor state (before the completion happened)
                ParseTree T = getViterbiParse(
                        chart.stateSets.get(
                                origin.ruleStartPosition,
                                state.getRuleStartPosition(),
                                state.getRuleDotPosition() - 1,
                                state.getRule()
                        )
                        , chart);
                // Recurse for completed state
                ParseTree Tprime = getViterbiParse(origin, chart);

                T.addRightMost(Tprime);
                return T;
            }
        }
    }

    public static <E> Chart parse(NonTerminal S,
                                  Grammar grammar,
                                  Iterable<Token<E>> tokens) {
        return parse(S, grammar, tokens, null);
    }

    public static <E> ParseTree getViterbiParse(NonTerminal S, Grammar grammar, Iterable<Token<E>> tokens) {
        final ParseTreeWithScore viterbiParseWithScore = getViterbiParseWithScore(S, grammar, tokens);
        if (viterbiParseWithScore == null) return null;
        return viterbiParseWithScore.getParseTree();
    }

    public static <E> ParseTreeWithScore getViterbiParseWithScore(NonTerminal S, Grammar grammar, Iterable<Token<E>> tokens) {
        ChartWithInputPosition chart = parseAndCountTokens(S, grammar, tokens, null);

        List<ParseTreeWithScore> parses = chart.chart.stateSets.getCompletedStates(chart.index, Category.START).stream()
                .map(state -> new ParseTreeWithScore(getViterbiParse(state, chart.chart), chart.chart.getViterbiScore(state), grammar.getSemiring()))
                .collect(Collectors.toList());
        if (parses.size() > 1) throw new Error("Found more than one Viterbi parses. This is a bug.");
        return parses.size() == 0 ? null : parses.get(0);
    }

    public static <E> ChartWithInputPosition parseAndCountTokens(NonTerminal S,
                                                                 Grammar grammar,
                                                                 Iterable<Token<E>> tokens,
                                                                 ScanProbability scanProbability) {
        Chart chart = new Chart(grammar);
        DblSemiring sr = grammar.getSemiring();

        // Initial state
        State initialState = new State(Rule.create(sr, 1.0, Category.START, S), 0);
        chart.addState(0, initialState, sr.one(), sr.one());

        // Cycle through input
        int i = 0;
        for (Token<E> token : tokens) {
            chart.predict(i);

            chart.scan(i, token, scanProbability);

            Set<State> completedStates = new HashSet<>(chart.stateSets.getCompletedStates(i + 1));
            chart.completeNoViterbi(i + 1);
            completedStates.forEach(s -> chart.setViterbiScores(s, new HashSet<>(), grammar.getSemiring()));
//            chart.computeViterbi(i + 1);
            i++;
        }

        //Set<State> completed = chart.getCompletedStates(i, Category.START);
        //if (completed.size() > 1) throw new Error("This is a bug");
        return new ChartWithInputPosition(chart, i);
    }

    public static <E> Chart parse(NonTerminal S,
                                  Grammar grammar,
                                  Iterable<Token<E>> tokens,
                                  ScanProbability scanProbability) {
        return parseAndCountTokens(S, grammar, tokens, scanProbability).chart;
    }

    static class ChartWithInputPosition {
        public final Chart chart;
        public final int index;

        public ChartWithInputPosition(Chart chart, int index) {
            this.chart = chart;
            this.index = index;
        }
    }
}
