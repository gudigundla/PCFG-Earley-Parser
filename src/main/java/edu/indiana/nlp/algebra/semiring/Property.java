package edu.indiana.nlp.algebra.semiring;

public enum Property {
    /**
     * If times right-distributes wrt plus
     */
    RightSemiring,
    /**
     * If times left-distributes wrt plus
     */
    LeftSemiring,
    /**
     * An idempotent semiring is one whose addition is idempotent: a + a = a, for all a
     */
    Idempotent,
    /**
     * A commutative semiring is one whose multiplication is commutative.
     */
    Commutative,
    /**
     * ∀ a, b: a ⊕ b = a or a ⊕ b = b
     */
    Path
}
