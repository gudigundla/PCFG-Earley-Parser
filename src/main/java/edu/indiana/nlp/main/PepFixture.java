
package edu.indiana.nlp.main;

import com.google.common.collect.Lists;
import edu.indiana.nlp.Grammar;
import edu.indiana.nlp.category.Category;
import edu.indiana.nlp.category.nonterminal.NonTerminal;
import edu.indiana.nlp.category.terminal.stringterminal.CaseInsenstiveStringTerminal;
import edu.indiana.nlp.category.terminal.stringterminal.ExactStringTerminal;
import edu.indiana.nlp.earleyparser.chart.state.State;
import edu.indiana.nlp.rule.Rule;
import edu.indiana.nlp.token.Token;

import java.util.List;
import java.util.stream.Collectors;


public final class PepFixture {
    public static final NonTerminal S = Category.nonTerminal("S");
    public static final NonTerminal seed = S;
    public static final NonTerminal NP = Category.nonTerminal("NP");
    public static final NonTerminal VP = Category.nonTerminal("VP");
    public static final NonTerminal Det = Category.nonTerminal("Det");
    public static final NonTerminal N = Category.nonTerminal("N");

    public final static NonTerminal A = Category.nonTerminal("A");
    public final static NonTerminal B = Category.nonTerminal("B");
    public final static NonTerminal C = Category.nonTerminal("C");
    public final static NonTerminal D = Category.nonTerminal("D");
    public final static NonTerminal E = Category.nonTerminal("E");
    public final static NonTerminal X = Category.nonTerminal("X");
    public final static NonTerminal Y = Category.nonTerminal("Y");
    public final static NonTerminal Z = Category.nonTerminal("Z");

    public final static Category a = new ExactStringTerminal("a");
    public final static Category b = new ExactStringTerminal("b");
    public final static Category c = new ExactStringTerminal("c");
    public final static Category d = new ExactStringTerminal("d");
    public final static Category e = new ExactStringTerminal("e");
    public final static Category the = new ExactStringTerminal("the");
    public final static Category boy = new ExactStringTerminal("boy");
    public final static Category girl = new ExactStringTerminal("girl");
    public final static Category left = new ExactStringTerminal("left");

    public final static Category aA = new CaseInsenstiveStringTerminal("a");
    public final static Category bB = new CaseInsenstiveStringTerminal("b");
    public final static Category ThE = new CaseInsenstiveStringTerminal("the");
    public final static Category bOy = new CaseInsenstiveStringTerminal("boy");
    public final static Category GirL = new CaseInsenstiveStringTerminal("girl");
    public final static Category LefT = new CaseInsenstiveStringTerminal("left");

    public final static Rule rule1 = Rule.create(1.0, A, B, C, D, E);
    public final static Rule rule2 = Rule.create(1.0, A, a);
    public final static Rule rule3 = Rule.create(1.0, X, Y, Z);
    public final static Rule rule4 = Rule.create(1.0, A, X, a);
    public final static Rule rule5 = Rule.create(1.0, X, a, Z);
    public final static Rule rule6 = Rule.create(1.0, Z, b);
    public final static Rule rule7 = Rule.create(1.0, X, a);
    public final static Rule rule8 = Rule.create(1.0, X, b);
    public static final Grammar mixed = new Grammar.Builder("mixed")
            .addRule(rule4)
            .addRule(rule5)
            .addRule(rule6)
            .addRule(rule7)
            .addRule(rule8)
            .build();
    public static final State edge1 = new State(rule1, 2, 3, 1);
    public static final State edge2 = new State(rule3, 0);
    public static final State edge3 = new State(rule2, 1, 2, 1);
    public static final List<Token<String>> tokens = Lists.newArrayList("the", "boy", "left").stream()
            .map(Token::new)
            .collect(Collectors.toList());

}
