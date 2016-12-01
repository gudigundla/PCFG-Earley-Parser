package edu.indiana.nlp.earleyparser.chart.state;

import edu.indiana.nlp.category.Category;
import edu.indiana.nlp.rule.Rule;
import edu.indiana.nlp.token.Token;

public class ScannedTokenState<E> extends State {
    public final Token<E> scannedToken;
    public final Category scannedCategory;


    public ScannedTokenState(Token<E> scannedToken, Rule rule, int ruleStartPosition, int positionInInput, int ruleDotPosition) {
        super(rule, ruleStartPosition, positionInInput, ruleDotPosition);
        this.scannedToken = scannedToken;
        this.scannedCategory = rule.getRight()[ruleDotPosition - 1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ScannedTokenState<?> that = (ScannedTokenState<?>) o;

        return scannedToken.equals(that.scannedToken) && scannedCategory.equals(that.scannedCategory);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + scannedToken.hashCode();
        result = 31 * result + scannedCategory.hashCode();
        return result;
    }
}
