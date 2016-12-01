
package edu.indiana.nlp.earleyparser.parse;

import edu.indiana.nlp.Grammar;
import edu.indiana.nlp.category.Category;
import edu.indiana.nlp.earleyparser.chart.state.ScannedTokenState;
import edu.indiana.nlp.earleyparser.chart.state.State;

import java.util.LinkedList;
import java.util.List;
//TODO

public abstract class ParseTree {
    public final Category category;
    @SuppressWarnings("WeakerAccess")
    public final LinkedList<ParseTree> children;

    /**
     * Creates a new parse tree with the specified category and parent parse
     * tree.
     */
    @SuppressWarnings("WeakerAccess")
    public ParseTree(Category category) {
        this(category, new LinkedList<>());
    }

    /**
     * Creates a new parse tree with the specified category, parent, and
     * child trees.
     *
     * @param category The category of the {@link #getCategory() category} of this parse
     *                 tree.
     * @param children The list of children of this parse tree, in their linear
     *                 order.
     */
    @SuppressWarnings("WeakerAccess")
    public ParseTree(Category category, LinkedList<ParseTree> children) {
        this.category = category;
        this.children = children;
    }

    /**
     * Gets the category category of this parse tree.
     *
     * @return <code>NP</code> for a subtree <code>NP -> Det N</code>.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Gets the child parse trees of this parse tree, retaining their linear
     * ordering.
     *
     * @return For a subtree <code>NP -> Det N</code>, returns an array
     * that contains parse trees whose {@link #getCategory() node} is
     * <code>Det, N</code> in that order, or <code>null</code> if this parse
     * tree has no children.
     */
    public List<ParseTree> getChildren() {
        return children;
    }


    /**
     * Gets a string representation of this parse tree.
     *
     * @return For the string &quot;the boy left&quot;, possibly something like:
     * <blockquote><code>[S[NP[Det[the]][N[boy]]][VP[left]]]</code></blockquote>
     * (The actual string would depend on the grammar rules in effect for the
     * parse).
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(category.toString());

        // recursively append children
        if (children != null) for (ParseTree child : children) sb.append(child.toString());

        sb.append(']');

        return sb.toString();
    }

    public void addRightMost(ParseTree tree) {
        children.addLast(tree);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParseTree parseTree = (ParseTree) o;

        return category.equals(parseTree.category) && (children != null ? children.equals(parseTree.children) : parseTree.children == null);

    }

    @Override
    public int hashCode() {
        int result = category.hashCode();
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    @SuppressWarnings("unused")
    public boolean hasChildren() {
        return children == null || children.size() > 0;
    }

    public static class Token<E> extends ParseTree {
        public final edu.indiana.nlp.token.Token<E> token;

        public Token(edu.indiana.nlp.token.Token<E> scannedToken, Category category) {
            super(category, null);
            this.token = scannedToken;
        }

        public Token(ScannedTokenState<E> scannedState) {
            this(scannedState.scannedToken, scannedState.scannedCategory);
        }


        @Override
        public int hashCode() {
            return super.hashCode() + token.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Token && super.equals(o) && token.equals(((Token) o).token);
        }
    }

    public static class NonToken extends ParseTree {
        public NonToken(Category node) {
            super(node);
        }

        public NonToken(Category node, LinkedList<ParseTree> children) {
            super(node, children);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof NonToken && super.equals(o);
        }
    }
}
