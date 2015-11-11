/**
 * A model for a {@link HIRItemScorer}.
 * Stores calculated proximity values and number of co-rating users for each item pair.
 */
@DefaultProvider(HIRModelBuilder.class)
@Shareable
@SuppressWarnings("deprecation")
public class HIRModel implements Serializable {
    private static final long serialVersionUID  = 1L;
    private final RealMatrix cmatrix;
    private final RealMatrix xmatrix;
    private final RealMatrix ymatrix;

    public HIRModel(RealMatrix cmatrix, RealMatrix xmatrix, RealMatrix ymatrix) {
        this.cmatrix = cmatrix;
        this.xmatrix = xmatrix;
        this.ymatrix = ymatrix;
    }
    public MutableSparseVector getCoratingsVector(long item, Collection<Long> items) {
        RealVector data = cmatrix.getRowVector((int) item);
        Map<Long, Double> forResults = new HashMap<>();
        LongIterator iter = LongIterators.asLongIterator(items.iterator());
        int i = 0;
        while (iter.hasNext()) {
            final long meti = iter.nextLong();
            forResults.put(meti, data.getEntry(i));
            i++;
        }
        return MutableSparseVector.create(forResults);
    }
    public MutableSparseVector getProximityVector(long item, Collection<Long> items) {
        RealVector data = xmatrix.getRowVector((int) item);
        RealVector resM = ymatrix.preMultiply(data);
        Map<Long, Double> forResults = new HashMap<>();
        LongIterator iter = LongIterators.asLongIterator(items.iterator());
        int i = 0;
        while (iter.hasNext()) {
            final long meti = iter.nextLong();
            forResults.put(meti, resM.getEntry(i));
            i++;
        }
        return MutableSparseVector.create(forResults);
    }
}
