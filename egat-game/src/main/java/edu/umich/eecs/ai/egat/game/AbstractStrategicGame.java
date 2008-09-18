package edu.umich.eecs.ai.egat.game;

/**
 * @author Patrick Jordan
 */
public abstract class AbstractStrategicGame<T extends PayoffValue> extends DefaultGame implements StrategicGame<T> {
    public AbstractStrategicGame(String name, String description) {
        super(name, description);
    }

    public AbstractStrategicGame(String name) {
        super(name);
    }

    protected AbstractStrategicGame() {
    }

    public Payoff payoff(final Profile profile) {
        Player[] players = players().toArray(new Player[0]);
        double[] payoffs = new double[players.length];

        for (Outcome outcome : Games.traversal(this)) {
            Payoff payoff = payoff(outcome);

            double prob = 1.0;

            for (int i = 0; i < players.length; i++) {
                Strategy strategy = profile.getStrategy(players[i]);
                prob *= strategy.getProbability(outcome.getAction(players[i])).doubleValue();
            }

            for (int i = 0; i < players.length; i++) {
                PayoffValue value = payoff.getPayoff(players[i]);
                if (value != null) {
                    payoffs[i] += prob * value.getValue();
                }
            }
        }

        return PayoffFactory.createPayoff(players, payoffs);
    }
}
