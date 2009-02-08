package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class EGames {
    private EGames() {
    }

    public static Payoff meanPayoff(Collection<Payoff> list, Player[] players) {
        double[] values = new double[players.length];

        for(Payoff payoff : list) {
            for(int i = 0; i < players.length; i++) {
                values[i] += payoff.getPayoff(players[i]).getValue();
            }
        }


        if( !list.isEmpty() ) {
            for(int i = 0, n = list.size(); i < players.length; i++) {
                    values[i] /= n;
            }
        }

        return PayoffFactory.createPayoff(players,values);
    }

    public static PayoffValue meanPayoff(List<SymmetricPayoff> payoffs, Action a) {
        double mu = 0.0;
        double n = 0.0;


        for (SymmetricPayoff payoff : payoffs) {
            mu += payoff.getPayoff(a).getValue();
            n++;
        }

        if(n>0)
            mu /= n;


        return PayoffFactory.createPayoffValue(mu);
    }


    public static SymmetricPayoff meanObservationPayoff(List<SymmetricPayoff> payoffs, SymmetricOutcome outcome) {
        Map<Action,PayoffValue> map = new HashMap<Action,PayoffValue>();

        for(Map.Entry<Action,Integer> entry : outcome.actionEntrySet()) {
            map.put(entry.getKey(), meanPayoff(payoffs,entry.getKey()));
        }

        return PayoffFactory.createSymmetricPayoff(map,outcome);
    }
}
