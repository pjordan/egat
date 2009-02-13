/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.game;

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
        return new AbstractPlayer() {

            public String getID() {
                return id;
            }
        };
    }

    /**
     * Create an action
     *
     * @param id the id of the action
     * @return the action
     */
    public static Action createAction(final String id) {
        return new AbstractAction() {

            public String getID() {
                return id;
            }
        };
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
        return createStrategy(new Action[] { action }, new Number[] {1.0});
    }
    /**
     * Create an outcome traversal object for a strategic simulation.
     *
     * @param simulation the strategic simulation
     * @return the outcome traversal
     */
    public static OutcomeTraversal traversal(StrategicSimulation simulation) {
        return new DefaultOutcomeTraversal(simulation);
    }

    /**
     * Create an outcome traversal object for a symmetric simulation.
     * This implemenation will avoid repeated (symmetric) outcomes.
     *
     * @param simulation the symmetric simulation.
     * @return the outcome traversal.
     */
    public static SymmetricOutcomeTraversal symmetricTraversal(SymmetricSimulation simulation) {
        return new SymmetricOutcomeTraversalImpl(simulation);
    }

    static class DefaultOutcomeTraversal implements OutcomeTraversal {
        StrategicSimulation game;

        public DefaultOutcomeTraversal(StrategicSimulation simulation) {
            this.game = simulation;
        }

        public Iterator<Outcome> iterator() {
            return new IteratorImpl(game);
        }


        private static class IteratorImpl implements Iterator<Outcome> {
            private StrategicSimulation simulation;
            private CrossProductIterator<Action> iterator;
            private List<Player> players;

            public IteratorImpl(StrategicSimulation simulation) {
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
                List<Action> actions = iterator.next();
                OutcomeImpl oc = new OutcomeImpl(players.toArray(new Player[0]),
                        actions.toArray(new Action[0]));
                return oc;
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
        private SymmetricSimulation simulation;

        public SymmetricOutcomeTraversalImpl(SymmetricSimulation simulation) {
            this.simulation = simulation;
        }

        public Iterator<SymmetricOutcome> iterator() {
            return new IteratorImpl(simulation);
        }

        private static class IteratorImpl implements Iterator<SymmetricOutcome> {
            private SymmetricSimulation simulation;
            private SymmetryIterator<Action> iterator;

            private List<Player> players;

            public IteratorImpl(SymmetricSimulation simulation) {
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
                final SymmetricOutcome oc = Games.createSymmetricOutcome(players.toArray(new Player[0]), actions.toArray(new Action[0]));
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
        return new OutcomeImpl(players, actions);
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

        return new OutcomeImpl(players, actions);
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

    static class OutcomeImpl extends UnmodifiableReducedMapImpl<Player, Action> implements Outcome {
        public OutcomeImpl(Player[] players, Action[] actions) {
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
            if(outcome.getCount(action) > 0) {
                Player[] players = outcome.players().toArray(new Player[0]);
                Action[] actions = new Action[players.length];

                int index = -1;

                for(int i = 0; i < players.length; i++) {
                    actions[i] = outcome.getAction(players[i]);

                    if(action.equals(actions[i])) {
                        index = i;
                    }
                }


                double outcomePayoff = game.payoff(outcome).getPayoff(action).getValue();

                for(Action other : otherActions) {
                    actions[index] = other;
                    SymmetricOutcome deviation = Games.createSymmetricOutcome(players,actions);

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
            if(action.equals(outcome.getAction(player))) {
                Player[] players = outcome.players().toArray(new Player[0]);
                Action[] actions = new Action[players.length];

                int index = -1;

                for(int i = 0; i < players.length; i++) {
                    actions[i] = outcome.getAction(players[i]);

                    if(player.equals(players[i])) {
                        index = i;
                    }
                }


                double outcomePayoff = game.payoff(outcome).getPayoff(player).getValue();

                for(Action other : otherActions) {
                    actions[index] = other;
                    Outcome deviation = Games.createOutcome(players,actions);

                    double otherPayoff = game.payoff(deviation).getPayoff(player).getValue();
                    epsilon = Math.max(epsilon, otherPayoff - outcomePayoff);
                }
            }
        }

        return epsilon;
    }

    public static double regret(SymmetricOutcome outcome, SymmetricGame game) {
        double epsilon = 0.0;

        for (SymmetricOutcome deviation : DeviationFactory.deviationTraversal(outcome, game)) {
            Player[] players = findDeviatingPlayers(deviation, outcome);
            epsilon = Math.max(epsilon, deviationGain(game.payoff(outcome), game.payoff(deviation), players));
        }

        return epsilon;
    }

    public static double regret(Outcome outcome, StrategicGame game) {
        double epsilon = 0.0;

        for (Player player : game.players()) {
            for (Outcome deviation : DeviationFactory.deviationTraversal(outcome, game, player)) {
                epsilon = Math.max(epsilon, deviationGain(game.payoff(outcome), game.payoff(deviation), player));
            }
        }
        return epsilon;
    }
}
