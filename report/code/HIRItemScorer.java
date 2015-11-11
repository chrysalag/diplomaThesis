/**
 * An {@link ItemScorer} that implements the HIR algorithm.
 */
public class HIRItemScorer extends AbstractItemScorer {
    protected final UserEventDAO dao;
    protected final ItemDAO idao;
    protected HIRModel model;
    protected final PreferenceDomain domain;
    protected double directAssociation;
    protected double proximity;

    @Inject
    public HIRItemScorer(UserEventDAO dao,
                         HIRModel model,
                         ItemDAO idao,
                         @Nullable PreferenceDomain dom,
                         @DirectAssociationParameter double direct,
                         @ProximityParameter double prox) {
        this.dao = dao;
        this.model = model;
        this.idao = idao;
        domain = dom;
        directAssociation = direct;
        proximity = prox;
    }

    @Nonnull
    @Override
    public ResultMap scoreWithDetails(long user, @Nonnull Collection<Long> items) {
        UserHistory<Rating> history = dao.getEventsForUser(user, Rating.class);

        if (history == null) { history = History.forUser(user); }

        SparseVector historyVector = RatingVectorUserHistorySummarizer.makeRatingVector(history);
        List<Result> results = new ArrayList<>();
        MutableSparseVector preferenceVector = MutableSparseVector.create(idao.getItemIds(), 0);

        double total = 0.0;
        for (VectorEntry e: historyVector.fast()) {
            final long key = e.getKey();
            final double value = e.getValue();
            preferenceVector.set(key, value);
            total += value;
        }

        if (total != 0) {  preferenceVector.multiply(1/total); }

        final double preferenceInResults = 1 - directAssociation - proximity;
        MutableSparseVector rankingVector = preferenceVector.copy();
        rankingVector.multiply(preferenceInResults);

        for (VectorEntry e: preferenceVector.fast()) {
            final double prefValue = e.getValue();
            if (prefValue != 0) {
                final long prefKey = e.getKey();
                MutableSparseVector coratingsVector = model.getCoratingsVector(prefKey, items);
                coratingsVector.multiply(directAssociation);
                MutableSparseVector proximityVector = model.getProximityVector(prefKey, items);
                proximityVector.multiply(proximity);
                coratingsVector.add(proximityVector);
                coratingsVector.multiply(prefValue);
                rankingVector.add(coratingsVector);
            }
        }

        for (VectorEntry e: rankingVector.fast()) {
            final long key = e.getKey();
            if (!historyVector.containsKey(key)) {
                results.add(Results.create(key, e.getValue()));
            }
        }

        return Results.newResultMap(results);
    }

    public HIRModel getModel() { return model; }
}
