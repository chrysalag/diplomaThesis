/**
 * A matrix to store the direct inter-item relationships
 * that derive from the number of their coratings.
 */
public class DirectAssociationMatrix {
    private RealMatrix workMatrix;
    private int itemSize;
    /**
     * Creates a matrix to process rating data and generate coratings for
     * a {@code HIRItemScorer}.
     * @param dao       The DataAccessObject interfacing with the data for the model
     */
    public DirectAssociationMatrix(ItemDAO dao) {
        LongSet items = dao.getItemIds();
        itemSize = items.size();
        workMatrix = MatrixUtils.createRealMatrix(itemSize, itemSize);
    }
    /**
     * Puts the item pair into the accumulator.
     * @param id1      The id of the first item.
     * @param itemVec1 The rating vector of the first item.
     * @param id2      The id of the second item.
     * @param itemVec2 The rating vector of the second item.
     */
    public void putItemPair(long id1, SparseVector itemVec1, long id2, SparseVector itemVec2) {
        if (id1 == id2) { workMatrix.setEntry((int) id1, (int)id2, 0); }
        else {
            long coratings = 0;
            for (Pair<VectorEntry,VectorEntry> pair: Vectors.fastIntersect(itemVec1, itemVec2)) {
                coratings++;
            }
            workMatrix.setEntry((int) id1, (int) id2, coratings);
        }
    }
    /**
     * @return A matrix of item corating values to be used by a {@code HIRItemScorer}.
     */
    public RealMatrix buildMatrix() {
        for (int i=0; i<itemSize; i++) {
            RealVector testRow = workMatrix.getRowVector(i);
            double testSum = testRow.getL1Norm();
            if (testSum != 0){
                testRow.mapDivideToSelf(testSum);
                workMatrix.setRowVector(i, testRow);
            }
        }
        return workMatrix;
    }
}
