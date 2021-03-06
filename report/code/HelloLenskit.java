/**
 * Demonstration app for LensKit. This application builds an item-item CF model
 * from a CSV file, then generates recommendations for a user.
 *
 * Usage: java org.grouplens.lenskit.hello.HelloLenskit ratings.csv user
 */
public class HelloLenskit implements Runnable {
    public static void main(String[] args) {
        HelloLenskit hello = new HelloLenskit(args);
        try {
            hello.run();
        } catch (RuntimeException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private File inputFile = new File("data/ratings.csv");
    private File movieFile = new File("data/movies.csv");
    private File genreFile = new File("data/genres.csv");

    private List<Long> users;

    public HelloLenskit(String[] args) {
        users = new ArrayList<>(args.length);
        for (String arg: args) {
            users.add(Long.parseLong(arg));
        }
    }

    public void run() {
        EventDAO dao = TextEventDAO.create(inputFile, Formats.movieLensLatest());
        ItemNameDAO names;
        MapItemGenreDAO genres;

        try {
            names = MapItemNameDAO.fromCSVFile(movieFile, 1);
        } catch (IOException e) {
            throw new RuntimeException("cannot load names", e);
        }

        try {
            genres = MapItemGenreDAO.fromCSVFile(genreFile);
        } catch (IOException g) {
            throw new RuntimeException("cannot load names", g);
        }

        // Next: load the LensKit algorithm configuration
        LenskitConfiguration config = null;
        try {
            config = ConfigHelpers.load(new File("etc/hir.groovy"));
        } catch (IOException e) {
            throw new RuntimeException("could not load configuration", e);
        }
        // Add our data component to the configuration

        config.addComponent(dao);
        config.bind(EventDAO.class).to(dao);
        config.bind(MapItemGenreDAO.class).to(genres);
        config.bind(PreferenceDomain.class).to(new PreferenceDomain(0, 1));

        LenskitRecommenderEngine engine = LenskitRecommenderEngine.build(config);

        try (LenskitRecommender rec = engine.createRecommender()) {
            ItemRecommender irec = rec.getItemRecommender();
            assert irec != null;
            for (long user : users) {
                // get 10 recommendation for the user
                List<Result> recs = irec.recommendWithDetails(user, 10, null, null);
                System.out.format("Recommendations for user %d:\n", user);
                for (Result item : recs) {
                    String name = names.getItemName(item.getId());
                    System.out.format("\t %s \n", name);
                }
            }
        }
    }
}
