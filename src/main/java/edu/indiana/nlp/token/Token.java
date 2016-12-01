package edu.indiana.nlp.token;

public class Token<T> {
    public final T obj;

    public Token(T source) {
        if (source == null)
            throw new Error("Source object can't be null for an instantiated token. Did you mean to create a null token?");
        this.obj = source;
    }

    public static <T> Token<T> from(T t) {
        if (t == null) return null;
        else return new Token<>(t);
    }

    @Override
    public String toString() {
        return obj.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token<?> token = (Token<?>) o;

        return obj.equals(token.obj);

    }

    @Override
    public int hashCode() {
        return obj.hashCode();
    }
}
