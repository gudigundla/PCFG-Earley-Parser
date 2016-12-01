package edu.indiana.nlp.earleyparser.parse;

@FunctionalInterface
public interface ScanProbability {
    double getProbability(int index);
}
