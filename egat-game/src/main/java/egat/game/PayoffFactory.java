/*
 * PayoffFactory.java
 *
 * Copyright (C) 2006-2010 Patrick R. Jordan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package egat.game;

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class PayoffFactory {
    private PayoffFactory() {
    }


    /**
     * Create a symmetric payoff by wrapping the players and value maps.
     *
     * @param payoffs the action-payoff value map
     * @param actions the player-action map
     * @return the payoff represented by the mapping between the players and values.
     */
    public static <T extends PayoffValue> SymmetricPayoff<T> createSymmetricPayoff(Map<Action, T> payoffs, Map<Player, Action> actions) {
        return new ArrayListSymmetricPayoffImpl<T>(payoffs, actions);
    }

    /**
     * Create a symmetric payoff by wrapping the players and value maps.
     *
     * @param payoffs the action-payoff value map
     * @param outcome the player-action map
     * @return the payoff represented by the mapping between the players and values.
     */
    public static <T extends PayoffValue> SymmetricPayoff createSymmetricPayoff(Map<Action, T> payoffs, Outcome outcome) {
        return new ArrayListSymmetricPayoffImpl<T>(payoffs, outcome);
    }

    /**
     * Create a payoff by wrapping the players and value arrays.
     *
     * @param players the array of players in the payoff.
     * @param values  the array of values in the payoff.
     * @return the payoff represented by the mapping between the players and values.
     */
    public static Payoff createPayoff(final Player[] players, final double[] values) {
        return new ArrayListPayoffImpl<PayoffValue>(Arrays.asList(players), Arrays.asList(createPayoffValues(values)));
    }

    /**
     * Create a payoff by wrapping the players and value arrays.
     *
     * @param players the array of players in the payoff.
     * @param values  the array of values in the payoff.
     * @return the payoff represented by the mapping between the players and values.
     */
    public static Payoff<EmpiricalPayoffValue> createEmpiricalPayoff(final Player[] players, final EmpiricalPayoffValue[] values) {
        return new EmpiricalPayoffImpl(players, values);
    }

    /**
     * Create payoff values by wrapping the value array.
     *
     * @param values the array of values in the payoff.
     * @return the wrapped payoff values
     */
    public static PayoffValue[] createPayoffValues(final double[] values) {
        PayoffValue[] payoffValues = new PayoffValue[values.length];

        for (int i = 0; i < values.length; i++) {
            payoffValues[i] = createPayoffValue(values[i]);
        }

        return payoffValues;
    }

    /**
     * Create payoff value by wrapping the value.
     *
     * @param value the value of the payoff.
     * @return the wrapped payoff value
     */
    public static PayoffValue createPayoffValue(final double value) {
        return new ExactPayoffValue(value);
    }

    /**
     * Create an empirical payoff value by wrapping the mean, std, and sample size.
     *
     * @param mean the mean of the payoff sample.
     * @param std  the standard deviation of the payoff sample.
     * @param size the size of the payoff sample.
     * @return the wrapped payoff value
     */
    public static EmpiricalPayoffValue createEmpiricalPayoffValue(final double mean, final double std, final int size) {
        return new EmpiricalPayoffValueImpl(mean, std, size);
    }

    /**
     * Calculate the minimum payoff of a simulation.  This is the minimum values over all outcomes and players.
     *
     * @param game the simulation
     * @return the minimum values over all outcomes and players.
     */
    public static Number minPayoff(final StrategicGame game) {
        double min = Double.POSITIVE_INFINITY;
        Player[] players = game.players().toArray(new Player[0]);

        try {
            for (Outcome outcome : Games.traversal(game)) {
                Payoff payoff = game.payoff(outcome);

                for (Player player : players) {
                    PayoffValue value = payoff.getPayoff(player);
                    if (value != null)
                        min = Math.min(min, value.getValue());
                }
            }
        } catch (Exception e) {
            min = Double.NaN;
        }

        return min;
    }

    /**
     * Calculate the maximum payoff of a simulation.  This is the maximum values over all outcomes and players.
     *
     * @param game the simulation
     * @return the minimum values over all outcomes and players.
     */
    public static Number maxPayoff(final StrategicGame game) {
        double max = Double.NEGATIVE_INFINITY;
        Player[] players = game.players().toArray(new Player[0]);

        try {
            for (Outcome outcome : Games.traversal(game)) {
                Payoff payoff = game.payoff(outcome);

                for (Player player : players) {
                    PayoffValue value = payoff.getPayoff(player);
                    if (value != null)
                        max = Math.max(max, value.getValue());
                }
            }
        } catch (Exception e) {
            max = Double.NaN;
        }

        return max;
    }


    static class ArrayListPayoffImpl<T extends PayoffValue> implements Payoff<T> {
        private List<Player> players;
        private List<T> payoffs;
        private Set<Player> playerSet;
        private Set<Map.Entry<Player, T>> entrySet;

        public ArrayListPayoffImpl(List<Player> players, List<T> payoffs) {
            this.players = players;
            this.payoffs = payoffs;
        }

        public T getPayoff(Player player) {

            int index = findPlayer(player);
            if (index < 0) {
                return null;
            } else {
                return payoffs.get(index);
            }
        }

        public Set<Player> players() {
            if (playerSet == null) {
                playerSet = new HashSet<Player>(players);
            }
            return playerSet;
        }

        public Set<Map.Entry<Player, T>> entrySet() {

            if (entrySet == null) {

                entrySet = new HashSet<Map.Entry<Player, T>>();
                for (int i = 0; i < players.size(); i++) {
                    entrySet.add(new PayoffEntry(i));
                }
            }

            return entrySet;
        }

        public Collection<T> values() {
            return payoffs;
        }

        private int findPlayer(Player player) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).equals(player)) {
                    return i;
                }
            }

            return -1;
        }

        private class PayoffEntry implements Map.Entry<Player, T> {
            private int index;

            private PayoffEntry(int index) {
                this.index = index;
            }

            public Player getKey() {
                return players.get(index);
            }

            public T getValue() {
                return payoffs.get(index);
            }

            public T setValue(T value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int hashCode() {
                return (getKey() == null ? 0 : getKey().hashCode()) ^
                        (getValue() == null ? 0 : getValue().hashCode());
            }

            @Override
            public boolean equals(Object object) {
                if (!(object instanceof Map.Entry)) {
                    return false;
                }

                Map.Entry e = (Map.Entry) object;

                return (getKey() == null ?
                        e.getKey() == null : getKey().equals(e.getKey())) &&
                        (getValue() == null ?
                                e.getValue() == null : getValue().equals(e.getValue()));
            }
        }

        @Override
        public int hashCode() {
            return entrySet().hashCode();
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Payoff)) {
                return false;
            }

            Payoff o = (Payoff) object;

            return entrySet().equals(o.entrySet());
        }
    }

    static class PayoffImpl extends Games.UnmodifiableReducedMapImpl<Player, PayoffValue> implements Payoff {
        public PayoffImpl(Player[] players, PayoffValue[] payoffs) {
            super(players, payoffs);
        }

        public PayoffValue getPayoff(Player player) {
            return get(player);
        }

        public boolean equals(Object object) {
            if (!(object instanceof Payoff)) {
                return false;
            }

            Payoff o = (Payoff) object;

            return entrySet().equals(o.entrySet());
        }

        public final Set<Player> players() {
            return keySet();
        }

        public final Set<Map.Entry<Player, PayoffValue>> entrySet() {
            return super.entrySet();
        }
    }

    static class ArrayListSymmetricPayoffImpl<T extends PayoffValue> implements SymmetricPayoff<T> {
        private List<Player> players;
        private int[] playerActionIndex;
        private List<Action> actions;
        private List<T> payoffs;

        private Set<Player> playerSet;
        private Set<Action> actionSet;
        private Set<Map.Entry<Player, T>> entrySet;
        private Map<Action, T> valueMap;
        private int hashCode;

        public ArrayListSymmetricPayoffImpl(Map<Action, T> values, Map<Player, Action> actions) {
            this.actions = new ArrayList<Action>(values.keySet());

            this.players = new ArrayList<Player>(actions.keySet());

            this.payoffs = new ArrayList<T>();

            for (int i = 0; i < actions.size(); i++) {
                payoffs.set(i, values.get(actions.get(i)));
            }

            this.playerActionIndex = new int[players.size()];

            for (int i = 0; i < players.size(); i++) {
                this.playerActionIndex[i] = findAction(actions.get(players.get(i)));
            }
        }

        public ArrayListSymmetricPayoffImpl(Map<Action, T> values, Outcome outcome) {
            this.actions = new ArrayList<Action>(values.keySet());

            this.players = new ArrayList<Player>(outcome.players());

            this.payoffs = new ArrayList<T>();

            for (int i = 0; i < actions.size(); i++) {
                payoffs.add(i, values.get(actions.get(i)));
            }

            this.playerActionIndex = new int[players.size()];

            for (int i = 0; i < players.size(); i++) {
                this.playerActionIndex[i] = findAction(outcome.getAction(players.get(i)));
            }
        }

        public T getPayoff(Action action) {
            int index = findAction(action);
            if (index < 0) {
                return null;
            } else {
                return payoffs.get(index);
            }
        }

        public Set<Action> actions() {
            if (actionSet == null) {
                actionSet = new HashSet<Action>(actions);
            }
            return actionSet;
        }

        public Map<Action, T> valueMap() {
            if (valueMap == null) {
                valueMap = new HashMap<Action, T>();
                for (int i = 0; i < actions.size(); i++) {
                    valueMap.put(actions.get(i), payoffs.get(i));
                }
            }

            return valueMap;
        }

        public T getPayoff(Player player) {

            int index = findPlayer(player);
            if (index < 0) {
                return null;
            } else {
                return payoffs.get(playerActionIndex[index]);
            }
        }

        public Set<Player> players() {
            if (playerSet == null) {
                playerSet = new HashSet<Player>(players);
            }
            return playerSet;
        }

        public Set<Map.Entry<Player, T>> entrySet() {
            if (entrySet == null) {
                entrySet = new HashSet<Map.Entry<Player, T>>();
                for (int i = 0; i < players.size(); i++) {
                    entrySet.add(new PayoffEntry(i));
                }
            }

            return entrySet;
        }

        public Collection<T> values() {
            return payoffs;
        }

        private int findPlayer(Player player) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).equals(player)) {
                    return i;
                }
            }

            return -1;
        }

        private int findAction(Action action) {
            for (int i = 0; i < actions.size(); i++) {
                if (actions.get(i).equals(action)) {
                    return i;
                }
            }

            return -1;
        }

        private class PayoffEntry implements Map.Entry<Player, T> {
            private int index;

            private PayoffEntry(int index) {
                this.index = index;
            }

            public Player getKey() {
                return players.get(index);
            }

            public T getValue() {
                return payoffs.get(playerActionIndex[index]);
            }

            public T setValue(T value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int hashCode() {
                return (getKey() == null ? 0 : getKey().hashCode()) ^
                        (getValue() == null ? 0 : getValue().hashCode());
            }

            @Override
            public boolean equals(Object object) {
                if (!(object instanceof Map.Entry)) {
                    return false;
                }

                Map.Entry e = (Map.Entry) object;

                return (getKey() == null ?
                        e.getKey() == null : getKey().equals(e.getKey())) &&
                        (getValue() == null ?
                                e.getValue() == null : getValue().equals(e.getValue()));
            }
        }

        @Override
        public int hashCode() {

            if (entrySet == null) {
                hashCode = entrySet().hashCode();
            }

            return hashCode;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Payoff)) {
                return false;
            }

            Payoff o = (Payoff) object;

            return entrySet().equals(o.entrySet());
        }
    }

    static class SymmetricPayoffImpl<T extends PayoffValue> implements SymmetricPayoff<T> {
        private Map<Player, T> map;
        private Map<Action, T> actionMap;

        public SymmetricPayoffImpl(Map<Action, T> values, Map<Player, Action> actions) {
            actionMap = values;
            map = new HashMap<Player, T>();
            for (Player player : actions.keySet()) {
                map.put(player, values.get(actions.get(player)));
            }
        }

        public SymmetricPayoffImpl(Map<Action, T> values, Outcome outcome) {
            actionMap = values;
            map = new HashMap<Player, T>();
            for (Player player : outcome.players()) {
                map.put(player, values.get(outcome.getAction(player)));
            }
        }


        public T getPayoff(Action action) {
            return actionMap.get(action);
        }

        public Set<Action> actions() {
            return actionMap.keySet();
        }

        public T getPayoff(Player player) {
            return map.get(player);
        }

        public boolean equals(Object object) {
            if (!(object instanceof Payoff)) {
                return false;
            }

            Payoff o = (Payoff) object;

            return entrySet().equals(o.entrySet());
        }

        public final Set<Player> players() {
            return map.keySet();
        }

        public final Set<Map.Entry<Player, T>> entrySet() {
            return map.entrySet();
        }


        public Collection<T> values() {
            return map.values();
        }


        public Map<Action, T> valueMap() {
            return actionMap;
        }
    }

    static class EmpiricalPayoffImpl extends Games.UnmodifiableReducedMapImpl<Player, EmpiricalPayoffValue> implements Payoff<EmpiricalPayoffValue> {
        public EmpiricalPayoffImpl(Player[] players, EmpiricalPayoffValue[] payoffs) {
            super(players, payoffs);
        }

        public EmpiricalPayoffValue getPayoff(Player player) {
            return get(player);
        }

        public boolean equals(Object object) {
            if (!(object instanceof Payoff)) {
                return false;
            }

            Payoff o = (Payoff) object;

            return entrySet().equals(o.entrySet());
        }

        public final Set<Player> players() {
            return keySet();
        }

        public final Set<Map.Entry<Player, EmpiricalPayoffValue>> entrySet() {
            return super.entrySet();
        }
    }

    static class ExactPayoffValue implements PayoffValue {
        private final double value;

        public ExactPayoffValue(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public int compareTo(PayoffValue payoffValue) {
            //TODO: Check this, I am not sure that this is the correct way
            if (payoffValue == null)
                return 0;

            return Double.compare(value, payoffValue.getValue());
        }

        public boolean equals(Object object) {
            if (object instanceof PayoffValue) {
                PayoffValue payoffValue = (PayoffValue) object;

                if (payoffValue == null)
                    return false;
                return value == payoffValue.getValue();
            }
            return false;
        }

        public int hashCode() {
            return (int) value;
        }
    }

    static class EmpiricalPayoffValueImpl implements EmpiricalPayoffValue {
        private final double mean;
        private final double std;
        private final int size;

        public EmpiricalPayoffValueImpl(Double mean, Double std, Integer size) {
            this.mean = mean;
            this.std = std;
            this.size = size;
        }

        public double getValue() {
            return getMean();
        }

        public int getSampleSize() {
            return size;
        }

        public double getStandardDeviation() {
            return std;
        }

        public double getMean() {
            return mean;
        }

        public int compareTo(PayoffValue payoffValue) {
            //TODO: Check this, I am not sure that this is the correct way
            if (payoffValue == null)
                return 0;

            return Double.compare(mean, payoffValue.getValue());
        }

        public boolean equals(Object object) {
            if (object instanceof PayoffValue) {
                PayoffValue payoffValue = (PayoffValue) object;

                if (payoffValue == null)
                    return false;
                return mean == payoffValue.getValue();
            }
            return false;
        }

        public int hashCode() {
            return (int) mean;
        }
    }
}
