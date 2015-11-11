/**
 * Provider for {@link org.lenskit.data.dao.ItemListItemDAO}
 * that reads a list of item IDs from a file, one per line.
 */
public class CSVFileItemGenreDAOProvider implements Provider<MapItemGenreDAO> {
   private final File itemFile;

   @Inject
   public CSVFileItemGenreDAOProvider(@ItemFile File file) {
            itemFile = file;
        }

   @Override
   public MapItemGenreDAO get() {
        try {
            return MapItemGenreDAO.fromCSVFile(itemFile);
        } catch (IOException e) {
            throw new DataAccessException("error reading " + itemFile, e);
        }
   }
}
