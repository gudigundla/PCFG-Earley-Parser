package edu.indiana.nlp.errors;

public class IssueRequest extends Error {
    public IssueRequest(String message) {
        super(message + "\nPlease submit an issue at https://github.com/digitalheir/java-probabilistic-earley-parser/issues");
    }
}
