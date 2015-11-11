/**
 * Pre-computes the number of mutual rating users for every pair
 * of items and stores the results in a {@code DirectAssociationMatrix}.
 * Provides this matrix and the matrices that factorize the {
 * @code ProximityMatrix} to the model.
 * These matrices are later used by a
 * {@code HIRItemScorer}.
 */
@SuppressWarnings("deprecation")
public class HIRModelBuilder implements Provider<HIRModel> {

    private final DirectAssociationMatrix DAMatrix;
    private final RowStochasticFactorOfProximity RSMatrix;
    private final TransposedFactorOfProximity TFMatrix;
    private final ItemItemBuildContext buildContext;

    @Inject
    public HIRModelBuilder(@Transient @Nonnull ItemDAO dao,
                           @Transient @Nonnull ItemGenreDAO gDao,
                           @Transient ItemItemBuildContext context) {
        buildContext = context;
        DAMatrix = new DirectAssociationMatrix(dao);
        RSMatrix = new RowStochasticFactorOfProximity(dao, gDao);
        TFMatrix = new TransposedFactorOfProximity(dao, gDao);
    }

    /**
     * Constructs and returns a {@link HIRModel}.
     */

    @Override
    public HIRModel get() {
        LongSet items = buildContext.getItems();
        LongIterator outer = items.iterator();
        while (outer.hasNext()) {
            final long item1 = outer.nextLong();
            final SparseVector vec1 = buildContext.itemVector(item1);
            LongIterator inner = items.iterator();
            while (inner.hasNext()) {
                final long item2 = inner.nextLong();
                SparseVector vec2 = buildContext.itemVector(item2);
                DAMatrix.putItemPair(item1, vec1, item2, vec2);
            }
        }
        return new HIRModel(DAMatrix.buildMatrix(), RSMatrix.RowStochastic(), TFMatrix.ColumnStochastic());
    }
}

