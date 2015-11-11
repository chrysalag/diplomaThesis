/**
 * A matrix to store the second factor of the matrix
 * that contains the inter-item relationships
 * that derive from their categorization.
 */

public class TransposedFactorOfProximity {
    private RealMatrix transposed;
    private int genreSize;

    /**
     * Creates a matrix to process genre data and generate the second factor of the proximity
     * matrix needed for a {@code HIRItemScorer}.
     *
     * @param dao    The DataAccessObject interfacing with the item data for the model
     * @param gDao   The genreDataAccessObject interfacing with the genre data for the model
     *
     */

    public TransposedFactorOfProximity(ItemDAO dao, ItemGenreDAO gDao) {
        LongSet items = dao.getItemIds();
        genreSize = gDao.getGenreSize();
        int itemSize = items.size();
        double[][] dataTransposed = new double[genreSize][itemSize];
        transposed = MatrixUtils.createRealMatrix(dataTransposed);

        int i = 0;
        LongIterator iter = items.iterator();
        while (iter.hasNext()) {
            long item = iter.nextLong();
            transposed.setColumnVector(i, gDao.getItemGenre(item));
            i++;
        }
    }

    /**
     * @return A matrix containing the row stochastic values of the matrix
     * that contains the information about the item categorization transposed,
     * to be used by a {@code HIRItemScorer}.
     */

    public RealMatrix ColumnStochastic() {

        for (int i = 0; i < genreSize; i++) {
            RealVector forIter = transposed.getRowVector(i);
            double sum = forIter.getL1Norm();
            if (sum!=0){
                forIter.mapDivideToSelf(sum);
                transposed.setRowVector(i, forIter);
            }
        }

        return transposed;
    }
}
