package edu.indiana.nlp.earleyparser;

import edu.indiana.nlp.algebra.semiring.dbl.DblSemiring;
import edu.indiana.nlp.earleyparser.chart.state.State;
import edu.indiana.nlp.earleyparser.parse.ParseTree;

public class ParseTreeWithScore {
    public final ParseTree parseTree;
    public final State.ViterbiScore score;
    public final DblSemiring semiring;

    public ParseTreeWithScore( ParseTree parseTree,  State.ViterbiScore score,  DblSemiring semiring) {
        this.parseTree = parseTree;
        this.score = score;
        this.semiring = semiring;
    }

    double getProbability() {
        return semiring.toProbability(score.getScore());
    }

    
    public ParseTree getParseTree() {
        return parseTree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParseTreeWithScore that = (ParseTreeWithScore) o;
        return parseTree.equals(that.parseTree) && score.equals(that.score) && semiring.equals(that.semiring);

    }

    @Override
    public int hashCode() {
        int result = parseTree.hashCode();
        result = 31 * result + score.hashCode();
        result = 31 * result + semiring.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ParseTree{" +
                "p = " + getProbability() +
                ", parseTree = " + parseTree +
                '}';
    }
}
