package edu.indiana.nlp.category;

import edu.indiana.nlp.category.nonterminal.NonTerminal;
import edu.indiana.nlp.category.terminal.Terminal;
import edu.indiana.nlp.rule.Rule;
import edu.indiana.nlp.token.Token;

import java.util.function.Function;

public interface Category {
    /**
     * Special start category for seeding Earley parsers.
     */
    NonTerminal START = new NonTerminal("<start>") {
        /**
         * Overrides {@link Category#equals(Object)} to compare using the
         * <code>==</code> operator (since there is only ever one start
         * category).
         */
        @Override
        public boolean equals(Object obj) {
            return obj instanceof NonTerminal && this == obj;
        }

    };

    /**
     * Gets the terminal status of this category.
     *
     * @return The terminal status specified for this category upon
     * construction.
     */
    static boolean isTerminal(Category c) {
        return c instanceof Terminal;
    }

    /**
     * Creates a new non-terminal category with the specified name.
     *
     * @see Category#terminal(Function)
     */
    static NonTerminal nonTerminal(String name) {
        return new NonTerminal(name);
    }

    /**
     * Creates a new terminal category with the specified name.
     *
     * @see Category#nonTerminal(String)
     */
    static <T> Terminal<T> terminal(Function<Token<T>, Boolean> categoryFunction) {
        if (categoryFunction == null)
            throw new Error("Can not instantiate category with null function. Did you mean to create a null category?");
        return categoryFunction::apply;
    }

    /**
     * Returns the given category
     *
     * @see Category#terminal(Function)
     */
    static <T> Terminal<T> terminal(Terminal<T> terminal) {
        if (terminal == null)
            throw new Error("Can not instantiate category with null function. Did you mean to create a null category?");
        return terminal;
    }
}
