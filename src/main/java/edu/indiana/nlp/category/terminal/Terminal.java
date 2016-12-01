package edu.indiana.nlp.category.terminal;

import edu.indiana.nlp.category.Category;
import edu.indiana.nlp.token.Token;

public interface Terminal<T> extends Category {

    /**
     * Expected to run in O(1), or else the complexity analysis does not apply anymore
     *
     * @param token Token to test
     * @return Whether this category conforms to the given token
     */
    boolean hasCategory(Token<T> token);
}
