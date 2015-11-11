/**
 * An item genre DAO backed by a map of item IDs to genres.
 *
 * @see org.lenskit.data.dao.ItemGenreDAO
 */

public class MapItemGenreDAO implements ItemGenreDAO, ItemDAO, Serializable {
    private static final Logger logger = LoggerFactory.getLogger(MapItemGenreDAO.class);
    private static final long serialVersionUID = 1L;
    private final Map<Long, RealVector> itemGenreMap;
    private final LongSortedSet itemIds;
    private static int genreSize = 0;

    public MapItemGenreDAO(Map<Long, RealVector> items) {
        itemGenreMap = ImmutableMap.copyOf(items);
        itemIds = LongUtils.packedSet(itemGenreMap.keySet());
    }

    @Nullable
    @Override
    public LongSet getItemIds() {
        return itemIds;
    }

    @Nullable
    @Override
    public RealVector getItemGenre(long item) {
        return itemGenreMap.get(item);
    }

    @Override
    public int getGenreSize() { return genreSize; }


    /**
     * Read an item list DAO from a file with no header rows.
     * @param file A file of item IDs, one per line.
     * @return The item list DAO.
     * @throws java.io.IOException if there is an error reading the list of items.
     */
    public static MapItemGenreDAO fromCSVFile(File file) throws IOException {
        return fromCSVFile(file, 0);
    }

    /**
     * Read an item list DAO from a file.
     * @param file A file of item IDs, one per line.
     * @param skipLines The number of initial header to skip
     * @return The item list DAO.
     * @throws java.io.IOException if there is an error reading the list of items.
     */
    public static MapItemGenreDAO fromCSVFile(File file, int skipLines) throws IOException {
        Preconditions.checkArgument(skipLines >= 0, "cannot skip negative lines");
        LineStream stream = LineStream.openFile(file, CompressionMode.AUTO);
        try {
            ObjectStreams.consume(skipLines, stream);
            ImmutableMap.Builder<Long, RealVector> genres = ImmutableMap.builder();
            StrTokenizer tok = StrTokenizer.getCSVInstance();
            for (String line : stream) {
                tok.reset(line);
                long item = Long.parseLong(tok.next());
                String title = tok.nextToken();
                String genre = tok.nextToken();
                if (genre != null) {
                    StrTokenizer gen = new StrTokenizer(genre, "|");
                    genreSize = gen.size();
                    double[] genValues = new double[genreSize];
                    int i = 0;
                    while (gen.hasNext()) {
                        double genValue = Double.parseDouble(gen.nextToken());
                        genValues[i] = genValue;
                        i++;
                    }
                    RealVector genVec = MatrixUtils.createRealVector(genValues);
                    genres.put(item, genVec);
                }
            }
            return new MapItemGenreDAO(genres.build());
        } catch (NoSuchElementException ex) {
            throw new IOException(String.format("%s:%s: not enough columns",
                                                file, stream.getLineNumber()),
                                  ex);
        } catch (NumberFormatException ex) {
            throw new IOException(String.format("%s:%s: id not an integer",
                                                file, stream.getLineNumber()),
                                  ex);
        } finally {
            stream.close();
        }
    }
}
