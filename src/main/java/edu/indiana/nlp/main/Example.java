package edu.indiana.nlp.main;

import edu.indiana.nlp.Grammar;
import edu.indiana.nlp.algebra.semiring.dbl.LogSemiring;
import edu.indiana.nlp.category.Category;
import edu.indiana.nlp.category.nonterminal.NonTerminal;
import edu.indiana.nlp.category.terminal.Terminal;
import edu.indiana.nlp.category.terminal.stringterminal.CaseInsenstiveStringTerminal;
import edu.indiana.nlp.category.terminal.stringterminal.ExactStringTerminal;
import edu.indiana.nlp.category.terminal.stringterminal.StringTerminal;
import edu.indiana.nlp.earleyparser.Parser;
import edu.indiana.nlp.token.Tokens;

/**
 * Created by hari.gudigundla on 16-11-30.
 */
public class Example {
    // NonTerminals are just wrappers around a string
    private static final NonTerminal S = Category.nonTerminal("S");
    private static final NonTerminal NP = Category.nonTerminal("NP");
    private static final NonTerminal VP = Category.nonTerminal("VP");
    private static final NonTerminal TV = Category.nonTerminal("TV");
    private static final NonTerminal Det = Category.nonTerminal("Det");
    private static final NonTerminal N = Category.nonTerminal("N");
    private static final NonTerminal Mod = Category.nonTerminal("Mod");

    // Token types are realized by implementing Terminal, specifically the function hasCategory. Terminal is a functional interface.
    private static final Terminal transitiveVerb = (StringTerminal) token -> token.obj.matches("(hit|chased)");
    // Some utility terminal types are pre-defined:
    private static final Terminal the = new CaseInsenstiveStringTerminal("the");
    private static final Terminal a = new CaseInsenstiveStringTerminal("a");
    private static final Terminal man = new ExactStringTerminal("man");
    private static final Terminal dog= new ExactStringTerminal("dog");

    private static final Terminal stick = new ExactStringTerminal("stick");
    private static final Terminal with = new ExactStringTerminal("with");

    private static final Grammar grammar = new Grammar.Builder("test")
            .setSemiring(new LogSemiring()) // If not set, defaults to Log semiring which is probably what you want
            .addRule(
                    1.0,   // Probability between 0.0 and 1.0, defaults to 1.0. The builder takes care of converting it to the semiring element
                    S,     // Left hand side of the rule
                    NP, VP // Right hand side of the rule
            )
            .addRule(
                    NP,
                    Det, N // eg. The man
            )
            .addRule(
                    NP,
                    Det, N, Mod // eg. The man (with a stick)
            )
            .addRule(
                    0.4,
                    VP,
                    TV, NP, Mod // eg. (chased) (the man) (with a stick)
            )
            .addRule(
                    0.6,
                    VP,
                    TV, NP // eg. (chased) (the man with a stick)
            )
            .addRule(Det, a)
            .addRule(Det, the)
            .addRule(N, man)
            .addRule(N, dog)
            .addRule(N, stick)
            .addRule(TV, transitiveVerb)
            .addRule(Mod, with, NP) // eg. with a stick
            .build();

    public static void main(String[] args) {
        System.out.println(
                Parser.recognize(S, grammar, Tokens.tokenize("The man chased the man with a stick"))
        );
        System.out.println(
                Parser.recognize(S, grammar, Tokens.tokenize("The man chased the dog"))
        );

        System.out.println(
                Parser.recognize(S, grammar, Tokens.tokenize("the", "stick", "chased", "the", "man"))
        );
    }
}
