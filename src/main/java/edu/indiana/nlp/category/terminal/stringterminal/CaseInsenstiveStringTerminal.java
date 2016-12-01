package edu.indiana.nlp.category.terminal.stringterminal;

import edu.indiana.nlp.token.Token;

public class CaseInsenstiveStringTerminal implements StringTerminal {
    public final String string;

    public CaseInsenstiveStringTerminal(String s) {
        this.string = s;
    }

    @Override
    public boolean hasCategory(Token<String> token) {
        return string.equalsIgnoreCase(token.obj);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CaseInsenstiveStringTerminal that = (CaseInsenstiveStringTerminal) o;

        return string != null ? string.equals(that.string) : that.string == null;

    }

    @Override
    public int hashCode() {
        return string != null ? string.hashCode() : 0;
    }
}
