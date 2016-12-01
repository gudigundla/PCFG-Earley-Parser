package edu.indiana.nlp.category.terminal.stringterminal;

import edu.indiana.nlp.token.Token;

@SuppressWarnings("WeakerAccess")
public class ExactStringTerminal implements StringTerminal {
    public final String string;

    public ExactStringTerminal(String s) {
        if (s == null) throw new NullPointerException();
        this.string = s;
    }

    @Override
    public boolean hasCategory(Token<String> token) {
        return string.equals(token.obj);
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExactStringTerminal that = (ExactStringTerminal) o;

        return string.equals(that.string);

    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }
}
