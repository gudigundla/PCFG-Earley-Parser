package edu.indiana.nlp.rule;

import edu.indiana.nlp.algebra.semiring.dbl.DblSemiring;
import edu.indiana.nlp.algebra.semiring.dbl.LogSemiring;
import edu.indiana.nlp.category.Category;
import edu.indiana.nlp.category.nonterminal.NonTerminal;

public class RuleFactory {
    private final DblSemiring semiring;

    public RuleFactory(DblSemiring semiring) {
        this.semiring = semiring;
    }

    public RuleFactory() {
        this(new LogSemiring());
    }

    /**
     * Instantiates a new rule with a probability score of one (whatever that means for the given semiring)
     */
    public Rule newRule(NonTerminal LHS, Category... RHS) {
        return newRuleWithRawProbability(semiring.one(), LHS, RHS);
    }

    /**
     * Instantiates a new rule with given probability <strong>as a probability between 0 and 1</strong>. The
     * semiring will take care in converting the number.
     */
    public Rule newRule(double probability, NonTerminal LHS, Category... RHS) {
        return newRuleWithRawProbability(semiring.fromProbability(probability), LHS, RHS);
    }

    /**
     * Instantiates a new rule with given probability <strong>as a probability between 0 and 1</strong>. The
     * semiring will take care in converting the number.
     */
    public Rule newRuleWithRawProbability(double probability, NonTerminal LHS, Category... RHS) {
        return new Rule(probability, LHS, RHS);
    }
}
