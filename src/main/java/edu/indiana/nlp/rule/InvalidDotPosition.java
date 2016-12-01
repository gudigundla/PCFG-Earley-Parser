package edu.indiana.nlp.rule;

import edu.indiana.nlp.category.Category;

import java.security.InvalidParameterException;

public class InvalidDotPosition extends InvalidParameterException {
    public InvalidDotPosition(int dotPosition, Category[] right) {
        super("Dot could not be placed at position " + dotPosition + " for a RHS of length " + right.length);
    }
}
