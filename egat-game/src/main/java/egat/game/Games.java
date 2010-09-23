/*
 * Games.java
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
 * This class consists exclusively of static methods that operate on games or
 * provide calculation support for games.
 *
 * @author Patrick Jordan
 */
public final class Games {
    private Games() {
    }

    /**
     * Create a player
     *
     * @param id the id of the player
     * @return the player
     */
    public static Player createPlayer(final String id) {
        return new SimplePlayer(id);
    }

    /**
     * Create an action
     *
     * @param id the id of the action
     * @return the action
     */
    public static Action createAction(final String id) {
        return new SimpleAction(id);
    }

    /**
     * Creates a pure strategy
     *
     * @param id the id of the action
     * @return the strategy
     */
    public static Strategy createPureStrategy(final String id) {
        return createPureStrategy(createAction(id));
    }


    /**
     * Creates a pure strategy.
     *
     * @param action the action
     * @return the strategy
     */
    public static Strategy createPureStrategy(Action action) {
        return new PureStrategy(action);
    }

    static class PureStrategy implements Strategy {
        private static final Number ZERO = 0.0;

        private static final Number ONE = 1.0;

        private static final Collection<Number> PROBABILITIES;

        static {
            Collection<Number> c = new ArrayList<Number>();
            c.add(ONE);
            PROBABILITIES = Collections.unmodifiableCollection(c);
        }

        private Action action;

        private Set<Action> actions;

        private Map.Entry<Action, Number> entry;

        private Set<Map.Entry<Action, Number>> entrySet;

        PureStrategy(final Action action) {
            this.action = action;

            actions = new HashSet<Action>();
            actions.add(action);


            entry = new Map.Entry<Action, Number>() {
                public Action getKey() {
                    return action;
                }

                public Number getValue() {
                    return ONE;
                }

                public Number setValue(Number value) {
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
            };

            entrySet = new HashSet<Map.Entry<Action, Number>>();
            entrySet.add(entry);
        }

        public Number getProbability(Action action) {
            if (this.action.equals(action))
                return ONE;
            else
                return ZERO;
        }

        public Set<Action> actions() {
            return actions;
        }

        public Set<Map.Entry<Action, Number>> entrySet() {
            return entrySet;
        }

        public Collection<Number> probabilities() {
            return PROBABILITIES;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Strategy)) return false;

            Strategy that = (Strategy) o;

            if (entrySet() != null ? !entrySet().equals(that.entrySet()) : that.entrySet() != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return entrySet() != null ? entrySet().hashCode() : 0;
        }
    }

    /**
     * Create an outcome traversal object for a strategic simulation.
     *
     * @param simulation the strategic simulation
     * @return the outcome traversal
     */
    public static OutcomeTraversal traversal(StrategicMultiAgentSystem simulation) {
        return new DefaultOutcomeTraversal(simulation);
    }

    /**
     * Create an outcome traversal object for a symmetric simulation.
     * This implemenation will avoid repeated (symmetric) outcomes.
     *
     * @param simulation the symmetric simulation.
     * @return the outcome traversal.
     */
    public static SymmetricOutcomeTraversal symmetricTraversal(SymmetricMultiAgentSystem simulation) {
        return new SymmetricOutcomeTraversalImpl(simulation);
    }

    static class DefaultOutcomeTraversal implements OutcomeTraversal {
        StrategicMultiAgentSystem game;

        public DefaultOutcomeTraversal(StrategicMultiAgentSystem simulation) {
            this.game = simulation;
        }

        public Iterator<Outcome> iterator() {
            return new IteratorImpl(game);
        }


        private static class IteratorImpl implements Iterator<Outcome> {
            private StrategicMultiAgentSystem simulation;
            private CrossProductIterator<Action> iterator;
            private List<Player> players;

            public IteratorImpl(StrategicMultiAgentSystem simulation) {
                this.simulation = simulation;

                List<Set<Action>> actions = new LinkedList<Set<Action>>();
                players = new ArrayList<Player>();
                for (Player p : simulation.players()) {
                    players.add(p);
                    actions.add(simulation.getActions(p));
                }

                iterator = new CrossProductIterator<Action>(actions);
            }

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public Outcome next() {
                return new ArrayListOutcomeImpl(players, iterator.next());
            }

            public void remove() {
                throw new UnsupportedOperationException("remove() not supported in this iterator.");
            }
        }

        private static class CrossProductIterator<T> implements Iterator<List<T>> {
            private Iterator<T> iterator;
            private T point;
            private CrossProductIterator<T> subIterator;
            private List<Set<T>> list;
            private List<T> next;

            private CrossProductIterator() {
            }

            public CrossProductIterator(List<Set<T>> list) {
                this.list = list;

                if (list.size() > 0) {
                    iterator = list.get(0).iterator();
                    update();
                }
            }

            private void update() {

                if (point != null && subIterator != null && subIterator.hasNext()) {
                    next = subIterator.next();
                    next.add(0, point);
                } else if (iterator.hasNext()) {
                    point = iterator.next();

                    if (list.size() > 1) {
                        subIterator = new CrossProductIterator<T>(list.subList(1, list.size()));
                        update();
                    } else {
                        next = new LinkedList<T>();
                        next.add(point);
                    }
                } else {
                    next = null;
                }
            }

            public boolean hasNext() {
                return next != null;
            }

            public List<T> next() {
                List<T> cur = next;
                update();
                return cur;
            }

            public void remove() {
                throw new UnsupportedOperationException("remove() not supported in this iterator.");
            }
        }
    }

    static class SymmetricOutcomeTraversalImpl implements SymmetricOutcomeTraversal {
        private SymmetricMultiAgentSystem simulation;

        public SymmetricOutcomeTraversalImpl(SymmetricMultiAgentSystem simulation) {
            this.simulation = simulation;
        }

        public Iterator<SymmetricOutcome> iterator() {
            return new IteratorImpl(simulation);
        }

        private static class IteratorImpl implements Iterator<SymmetricOutcome> {
            private SymmetricMultiAgentSystem simulation;
            private SymmetryIterator<Action> iterator;

            private List<Player> players;

            public IteratorImpl(SymmetricMultiAgentSystem simulation) {
                this.simulation = simulation;

                Set<Action> actions = new HashSet<Action>();
                players = new ArrayList<Player>();
                for (Player p : simulation.players()) {
                    players.add(p);
                    if (actions != null) {
                        actions = simulation.getActions(p);
                    }
                }

                iterator = new SymmetryIterator<Action>(actions, players.size());
            }

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public SymmetricOutcome next() {
                final List<Action> actions = iterator.next();
                final SymmetricOutcome oc = Games.createSymmetricOutcome(players, actions);
                return oc;
            }

            public void remove() {
                throw new UnsupportedOperationException("remove() not supported in this iterator.");
            }
        }

        private static class SymmetryIterator<T> implements Iterator<List<T>> {
            private Iterator<T> iterator;
            private T point;
            private int count;
            private int countMax;
            private SymmetryIterator<T> subIterator;
            private Set<T> set;
            private List<T> next;

            private SymmetryIterator() {
            }

            public SymmetryIterator(final Set<T> set, final int countMax) {
                //Defensive copy
                this.set = new HashSet<T>(set);

                this.countMax = countMax;
                iterator = set.iterator();
                if (countMax > 0) {
                    count = 0;
                    update();
                }
            }

            private void update() {

                if (point != null && subIterator != null && subIterator.hasNext()) {
                    next = subIterator.next();

                    for (int i = 0; i < count; i++) {
                        next.add(0, point);
                    }

                } else if (point != null && count < countMax) {
                    count++;
                    if (count == countMax) {
                        subIterator = null;
                        next = new LinkedList<T>();
                        for (int i = 0; i < count; i++) {
                            next.add(0, point);
                        }
                    } else {
                        subIterator = new SymmetryIterator(set, countMax - count);
                        update();
                    }
                } else if (iterator.hasNext()) {
                    count = 0;
                    point = iterator.next();
                    set.remove(point);
                    update();
                } else {
                    next = null;
                }
            }

            public boolean hasNext() {
                return next != null;
            }

            public List<T> next() {
                List<T> cur = next;
                update();
                return cur;
            }

            public void remove() {
                throw new UnsupportedOperationException("remove() not supported in this iterator.");
            }
        }
    }

    /**
     * Create an outcome by wrapping the players and actions arrays.
     *
     * @param players the array of players in the outcome.
     * @param actions the array of actions in the outcome.
     * @return the outcome represented by the mapping between the players and actions.
     */
    public static Outcome createOutcome(final Player[] players, final Action[] actions) {
        return new ArrayListOutcomeImpl(Arrays.asList(players), Arrays.asList(actions));
    }

    /**
     * Create an outcome by wrapping the players and actions arrays.
     *
     * @param players the array of players in the outcome.
     * @param actions the array of actions in the outcome.
     * @return the outcome represented by the mapping between the players and actions.
     */
    public static Outcome createOutcome(final List<? extends Player> players, final List<? extends Action> actions) {
        return new ArrayListOutcomeImpl(new ArrayList<Player>(players), new ArrayList<Action>(actions));
    }

    /**
     * Create a deviation of an outcome.
     *
     * @param player  the player deviating
     * @param action  the action
     * @param outcome the outcome being deviated
     * @return the outcome represented by the mapping between the players and actions.
     */
    public static Outcome createOutcomeDeviation(final Outcome outcome, final Player player, final Action action) {
        Player[] players = outcome.players().toArray(new Player[0]);
        Action[] actions = new Action[players.length];

        for (int i = 0; i < players.length; i++) {
            if (players[i].equals(player))
                actions[i] = action;
            else
                actions[i] = outcome.getAction(players[i]);
        }

        return new ArrayListOutcomeImpl(Arrays.asList(players), Arrays.asList(actions));
    }

    /**
     * Create a symmetric outcome by wrapping the outcome.
     *
     * @param outcome the outcome
     * @return the outcome represented by the mapping between the players and actions.
     */
    public static SymmetricOutcome createSymmetricOutcome(Outcome outcome) {
        return (outcome instanceof SymmetricOutcome) ? (SymmetricOutcome) outcome : new SymmetricOutcomeImpl(outcome);
    }

    /**
     * Create a symmetric outcome by wrapping the players and actions arrays.
     *
     * @param players the array of players in the outcome.
     * @param actions the array of actions in the outcome.
     * @return the outcome represented by the mapping between the players and actions.
     */
    public static SymmetricOutcome createSymmetricOutcome(final Player[] players, final Action[] actions) {
        return createSymmetricOutcome(createOutcome(players, actions));
    }

    /**
     * Create a symmetric outcome by wrapping the players and actions arrays.
     *
     * @param players the array of players in the outcome.
     * @param actions the array of actions in the outcome.
     * @return the outcome represented by the mapping between the players and actions.
     */
    public static SymmetricOutcome createSymmetricOutcome(final List<? extends Player> players, final List<? extends Action> actions) {
        return createSymmetricOutcome(createOutcome(players, actions));
    }

    /**
     * Create a strategy by wrapping the actions and probabilities arrays.
     *
     * @param actions      the array of actions in the strategy.
     * @param distribution the array of probabilities in the strategy.
     * @return the strategy represented by the mapping between the players and values.
     */
    public static Strategy createStrategy(final Action[] actions, final Number[] distribution) {
        return new StrategyImpl(actions, distribution);
    }

    /**
     * Create a strategy by wrapping the actions and probabilities arrays.
     *
     * @param actions      the array of actions in the strategy.
     * @param distribution the array of probabilities in the strategy.
     * @return the strategy represented by the mapping between the players and values.
     */
    public static Strategy createStrategy(final List<? extends Action> actions, final List<? extends Number> distribution) {
        return new StrategyImpl(actions, distribution);
    }

    /**
     * Create a profile by wrapping the players and strategies arrays.
     *
     * @param players    the array of players in the profile.
     * @param strategies the array of strategies in the profile.
     * @return the profile represented by the mapping between the players and values.
     */
    public static Profile createProfile(final Player[] players, final Strategy[] strategies) {
        return new ProfileImpl(players, strategies);
    }

    static class ProfileImpl extends UnmodifiableReducedMapImpl<Player, Strategy> implements Profile {
        public ProfileImpl(Player[] players, Strategy[] strategies) {
            super(players, strategies);
        }

        public Strategy getStrategy(Player player) {
            return get(player);
        }

        public boolean equals(Object object) {
            if (!(object instanceof Outcome)) {
                return false;
            }

            Outcome o = (Outcome) object;

            return entrySet().equals(o.entrySet());
        }

        public final Set<Player> players() {
            return keySet();
        }

        public final Set<Map.Entry<Player, Strategy>> entrySet() {
            return super.entrySet();
        }

        public Collection<Strategy> strategies() {
            return super.values();
        }

    }

    static class SymmetricOutcomeImpl implements SymmetricOutcome {
        private Outcome outcome;
        private Map<Action, Integer> counts;

        public SymmetricOutcomeImpl(Outcome outcome) {
            this.outcome = outcome;
            counts = new HashMap<Action, Integer>();

            for (Player player : outcome.players()) {
                Action a = outcome.getAction(player);

                if (!counts.containsKey(a)) {
                    counts.put(a, new Integer(0));
                }

                counts.put(a, counts.get(a) + 1);
            }
        }

        public Action getAction(Player player) {
            return outcome.getAction(player);
        }

        public boolean equals(Object object) {
            if (!(object instanceof Outcome)) {
                return false;
            }

            return outcome.equals(object);
        }

        public final Set<Player> players() {
            return outcome.players();
        }

        public final Set<Map.Entry<Player, Action>> entrySet() {
            return outcome.entrySet();
        }

        public Collection<Action> actions() {
            return outcome.actions();
        }


        public Integer getCount(Action action) {
            Integer count = counts.get(action);
            return count == null ? new Integer(0) : count;
        }

        public Set<Map.Entry<Action, Integer>> actionEntrySet() {
            return counts.entrySet();
        }

        public boolean symmetricEquals(SymmetricOutcome outcome) {
            if (outcome == null)
                return false;

            return actionEntrySet().equals(outcome.actionEntrySet());
        }


        public String toString() {
            return outcome.toString();
        }
    }

    static class ArrayListOutcomeImpl implements Outcome {
        private List<Player> players;
        private List<Action> actions;
        private Set<Player> playerSet;
        private Set<Map.Entry<Player, Action>> entrySet;
        private int hashCode;

        ArrayListOutcomeImpl(List<Player> players, List<Action> actions) {
            this.players = players;
            this.actions = actions;
        }

        public Action getAction(Player player) {

            int index = findPlayer(player);

            if (index < 0) {
                return null;
            } else {
                return actions.get(index);
            }
        }

        public Set<Player> players() {
            if (playerSet == null) {
                this.playerSet = new HashSet<Player>(players);
            }
            return playerSet;
        }

        public Set<Map.Entry<Player, Action>> entrySet() {
            if (entrySet == null) {
                entrySet = new HashSet<Map.Entry<Player, Action>>();
                for (int i = 0; i < players.size(); i++) {
                    entrySet.add(new OutcomeEntry(i));
                }
            }
            return entrySet;
        }

        public Collection<Action> actions() {
            return actions;
        }

        private int findPlayer(Player player) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).equals(player)) {
                    return i;
                }
            }

            return -1;
        }

        private class OutcomeEntry implements Map.Entry<Player, Action> {
            private int index;

            private OutcomeEntry(int index) {
                this.index = index;
            }

            public Player getKey() {
                return players.get(index);
            }

            public Action getValue() {
                return actions.get(index);
            }

            public Action setValue(Action value) {
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
            if (!(object instanceof Outcome)) {
                return false;
            }

            Outcome o = (Outcome) object;

            return entrySet().equals(o.entrySet());
        }
    }

    static class OutcomeImpl extends UnmodifiableReducedMapImpl<Player, Action> implements Outcome {
        public OutcomeImpl(Player[] players, Action[] actions) {
            super(players, actions);
        }

        public OutcomeImpl(List<? extends Player> players, List<? extends Action> actions) {
            super(players, actions);
        }

        public Action getAction(Player player) {
            return get(player);
        }

        public boolean equals(Object object) {
            super.equals(object);

            if (!(object instanceof Outcome)) {
                return false;
            }

            Outcome o = (Outcome) object;

            return entrySet().equals(o.entrySet());
        }

        public final Set<Player> players() {
            return keySet();
        }

        public final Set<Map.Entry<Player, Action>> entrySet() {
            return super.entrySet();
        }

        public Collection<Action> actions() {
            return super.values();
        }
    }

    static class StrategyImpl extends UnmodifiableReducedMapImpl<Action, Number> implements Strategy {
        public StrategyImpl(Action[] keys, Number[] values) {
            super(keys, values);
        }

        public StrategyImpl(List<? extends Action> keys, List<? extends Number> values) {
            super(keys, values);
        }

        public Number getProbability(Action action) {
            return get(action) == null ? 0.0 : get(action).doubleValue();
        }

        public boolean equals(Object object) {
            if (!(object instanceof Strategy)) {
                return false;
            }

            Strategy o = (Strategy) object;

            return entrySet().equals(o.entrySet());
        }

        public final Set<Action> actions() {
            return keySet();
        }

        public final Set<Map.Entry<Action, Number>> entrySet() {
            return super.entrySet();
        }

        public Collection<Number> probabilities() {
            return super.values();
        }
    }

    static class UnmodifiableReducedMapImpl<T, S> {
        private Map<T, S> map;
        private int hashCode;

        protected UnmodifiableReducedMapImpl(T[] keys, S[] values) throws IllegalArgumentException {
            if (keys.length != values.length)
                throw new IllegalArgumentException("key array not same size as values");

            map = new HashMap<T, S>();
            for (int i = 0; i < keys.length; i++) {
                map.put(keys[i], values[i]);
            }
            map = Collections.unmodifiableMap(map);
            hashCode = map.hashCode();
        }

        protected UnmodifiableReducedMapImpl(List<? extends T> keys, List<? extends S> values) throws IllegalArgumentException {
            if (keys.size() != values.size())
                throw new IllegalArgumentException("key array not same size as values");

            map = new HashMap<T, S>();
            for (int i = 0, n = keys.size(); i < n; i++) {
                map.put(keys.get(i), values.get(i));
            }
            map = Collections.unmodifiableMap(map);
            hashCode = map.hashCode();
        }

        public S get(T t) {
            return map.get(t);
        }


        public boolean equals(Object object) {
            if (!(object instanceof UnmodifiableReducedMapImpl))
                return false;
            if (hashCode != object.hashCode())
                return false;
            return entrySet().equals(((UnmodifiableReducedMapImpl) object).entrySet());
        }

        public int hashCode() {
            return hashCode;
        }

        protected Set<T> keySet() {
            return map.keySet();
        }

        protected Set<Map.Entry<T, S>> entrySet() {
            return map.entrySet();
        }

        public String toString() {
            return map.toString();
        }

        public Collection<S> values() {
            return map.values();
        }
    }

    public static Player findDeviatingPlayer(Outcome to, Outcome from) {
        Player player = null;

        for (Player p : to.players()) {
            if (!to.getAction(p).equals(from.getAction(p))) {
                player = p;
                break;
            }
        }

        return player;
    }

    public static double deviationGain(Payoff<? extends PayoffValue> from, Payoff<? extends PayoffValue> to, Player p) {
        return to.getPayoff(p).getValue() - from.getPayoff(p).getValue();
    }

    public static <T extends SymmetricOutcome> Player[] findDeviatingPlayers(T to, T from) {
        Player[] players = new Player[2];


        Action a = null;
        Action b = null;

        Collection<Action> toActions = to.actions();
        Collection<Action> fromActions = from.actions();

        for (Action action : toActions) {
            if (a == null)
                a = action;

            if (to.getCount(action) > from.getCount(action)) {
                a = action;
                break;
            }
        }

        for (Action action : fromActions) {
            if (b == null)
                b = action;
            if (to.getCount(action) < from.getCount(action)) {
                b = action;
                break;
            }
        }

        for (Player p : to.players()) {
            if (to.getAction(p) == a) {
                players[0] = p;
                break;
            }

        }

        for (Player p : from.players()) {
            if (from.getAction(p) == b) {
                players[1] = p;
                break;
            }

        }

        return players;
    }

    public static double deviationGain(Payoff<? extends PayoffValue> from, Payoff<? extends PayoffValue> to, Player[] p) {
        return to.getPayoff(p[0]).getValue() - from.getPayoff(p[1]).getValue();
    }

    public static double robustRegret(Action action, SymmetricGame game) {
        double epsilon = 0.0;

        Set<Action> otherActions = new HashSet<Action>(game.getActions());
        otherActions.remove(action);

        for (SymmetricOutcome outcome : Games.symmetricTraversal(game)) {
            if (outcome.getCount(action) > 0) {
                Player[] players = outcome.players().toArray(new Player[0]);
                Action[] actions = new Action[players.length];

                int index = -1;

                for (int i = 0; i < players.length; i++) {
                    actions[i] = outcome.getAction(players[i]);

                    if (action.equals(actions[i])) {
                        index = i;
                    }
                }


                double outcomePayoff = game.payoff(outcome).getPayoff(action).getValue();

                for (Action other : otherActions) {
                    actions[index] = other;
                    SymmetricOutcome deviation = Games.createSymmetricOutcome(players, actions);

                    double otherPayoff = game.payoff(deviation).getPayoff(other).getValue();
                    epsilon = Math.max(epsilon, otherPayoff - outcomePayoff);
                }
            }
        }

        return epsilon;
    }

    public static double robustRegret(Player player, Action action, StrategicGame game) {
        double epsilon = 0.0;

        Set<Action> otherActions = new HashSet<Action>(game.getActions(player));
        otherActions.remove(action);

        for (Outcome outcome : Games.traversal(game)) {
            if (action.equals(outcome.getAction(player))) {
                Player[] players = outcome.players().toArray(new Player[0]);
                Action[] actions = new Action[players.length];

                int index = -1;

                for (int i = 0; i < players.length; i++) {
                    actions[i] = outcome.getAction(players[i]);

                    if (player.equals(players[i])) {
                        index = i;
                    }
                }


                double outcomePayoff = game.payoff(outcome).getPayoff(player).getValue();

                for (Action other : otherActions) {
                    actions[index] = other;
                    Outcome deviation = Games.createOutcome(players, actions);

                    double otherPayoff = game.payoff(deviation).getPayoff(player).getValue();
                    epsilon = Math.max(epsilon, otherPayoff - outcomePayoff);
                }
            }
        }

        return epsilon;
    }

    public static double regret(SymmetricOutcome outcome, SymmetricGame game) {
        double epsilon = 0.0;

        for (Player player : game.players()) {
            epsilon = Math.max(epsilon, playerRegret(game, outcome, player));
        }

        return epsilon;
    }

    public static double regret(Outcome outcome, StrategicGame game) {
        double epsilon = 0.0;

        for (Player player : game.players()) {
            epsilon = Math.max(epsilon, playerRegret(game, outcome, player));
        }

        return epsilon;
    }


    public static double regret(Profile profile, StrategicGame game) {
        double epsilon = 0.0;

        for (Player player : game.players()) {
            epsilon = Math.max(epsilon, playerRegret(game, profile, player));
        }

        return epsilon;
    }

    public static double playerRegret(StrategicGame game, Outcome outcome, Player player) {
        Player[] players = outcome.players().toArray(new Player[0]);
        Action[] actions = new Action[players.length];
        double regret = 0.0;

        int playerIndex = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i].equals(player)) {
                playerIndex = i;
            } else {
                actions[i] = outcome.getAction(players[i]);
            }
        }

        double originalPayoff = game.payoff(outcome).getPayoff(player).getValue();

        for (Action action : game.getActions(player)) {

            actions[playerIndex] = action;

            double deviatingPayoff = game.payoff(Games.createOutcome(players, actions)).getPayoff(player).getValue();

            regret = Math.max(regret, deviatingPayoff - originalPayoff);
        }

        return regret;
    }

    public static double playerRegret(StrategicGame game, Profile profile, Player player) {
        Player[] players = profile.players().toArray(new Player[0]);
        Strategy[] strategies = new Strategy[players.length];
        double regret = 0.0;


        int playerIndex = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i].equals(player)) {
                playerIndex = i;
            } else {
                strategies[i] = profile.getStrategy(players[i]);
            }
        }

        double originalPayoff = game.payoff(profile).getPayoff(player).getValue();

        for (Action action : game.getActions(player)) {

            strategies[playerIndex] = Games.createPureStrategy(action);

            double deviatingPayoff = game.payoff(Games.createProfile(players, strategies)).getPayoff(player).getValue();

            regret = Math.max(regret, deviatingPayoff - originalPayoff);
        }

        return regret;
    }

    public static double playerGain(StrategicGame game, Outcome outcome, Player player, Action action) {
        Player[] players = outcome.players().toArray(new Player[0]);
        Action[] actions = new Action[players.length];

        for (int i = 0; i < players.length; i++) {
            if (players[i].equals(player)) {
                actions[i] = action;
            } else {
                actions[i] = outcome.getAction(players[i]);
            }
        }

        double originalPayoff = game.payoff(outcome).getPayoff(player).getValue();
        double deviatingPayoff = game.payoff(Games.createOutcome(players, actions)).getPayoff(player).getValue();

        return deviatingPayoff - originalPayoff;
    }

    public static double playerGain(StrategicGame game, Profile profile, Player player, Strategy strategy) {
        Player[] players = profile.players().toArray(new Player[0]);
        Strategy[] strategies = new Strategy[players.length];

        for (int i = 0; i < players.length; i++) {
            if (players[i].equals(player)) {
                strategies[i] = strategy;
            } else {
                strategies[i] = profile.getStrategy(players[i]);
            }
        }

        double originalPayoff = game.payoff(profile).getPayoff(player).getValue();
        double deviatingPayoff = game.payoff(Games.createProfile(players, strategies)).getPayoff(player).getValue();

        return deviatingPayoff - originalPayoff;
    }

    public static double playerGain(StrategicGame game, Profile profile, Player player, Action action) {
        return playerGain(game, profile, player, Games.createPureStrategy(action));
    }

    public static Payoff computeStrategicPayoff(final Profile profile, final StrategicGame game) {
        Player[] players = game.players().toArray(new Player[0]);

        double[] payoffs = new double[players.length];

        for (Outcome outcome : Games.traversal(game)) {

            Payoff payoff = game.payoff(outcome);

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

    public static Payoff computeStrategicPayoffUsingReduction(final Profile profile, final StrategicGame game) {
        Player[] players = game.players().toArray(new Player[0]);
        double[] payoffs = new double[players.length];

        StrategicGame reducedGame = game;

        for (int i = 0; i < players.length; i++) {
            reducedGame = new ActionReducedStrategicGame(reducedGame, players[i], profile.getStrategy(players[i]).actions());
        }

        for (Outcome outcome : Games.traversal(reducedGame)) {

            Payoff payoff = game.payoff(outcome);

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
