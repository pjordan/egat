package edu.umich.eecs.ai.egat.game;

/**
 * @author Patrick Jordan
 */
public class NonexistentPayoffException extends RuntimeException {
    private Outcome outcome;

    public NonexistentPayoffException(Outcome outcome) {
        super(String.format("Payoff for %s does not exist", outcome));
        this.outcome = outcome;
    }

    public Outcome getOutcome() {
        return outcome;
    }
}
