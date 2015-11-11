/**
 * Tests if the HIR Item Recommender is created
 * and properly configured.
 */

public class HIRItemRecommenderTest {
    private LenskitRecommenderEngine engine;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    MapItemGenreDAO gdao;
    ItemDAO idao;
    EventDAO dao;

    @Before
    public void createFile() throws IOException {
        File f = folder.newFile("genres.csv");
        PrintStream str = new PrintStream(f);
        try {
            str.println("0,\"Shawshank Redemption, The (1994)\",0|0|0|0|0|1|0|1|0|0|0|0|0|0|0|0|0|0|0|0");
            str.println("1,American History X (1998),0|0|0|0|0|1|0|1|0|0|0|0|0|0|0|0|0|0|0|0");
            str.println("2,Z (1969),0|0|0|0|0|0|0|1|0|0|0|0|1|0|0|1|0|0|0|0");
            str.println("3,\"Pan's Labyrinth (Laberinto del fauno, El) (2006)\",0|0|0|0|0|0|0|1|1|0|0|0|0|0|0|1|0|0|0|0");
            str.println("4,Seven Pounds (2008),0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0");
            str.println("5,Song of the Sea (2014),0|0|1|1|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0");
        } finally {
            str.close();
        }
        gdao = MapItemGenreDAO.fromCSVFile(f);
    }

    @SuppressWarnings("deprecation")
    @Before
    public void setup() throws RecommenderBuildException {
        List<Rating> rs = new ArrayList<>();
        rs.add(Rating.create(1, 0, 2));
        rs.add(Rating.create(1, 1, 4));
        rs.add(Rating.create(2, 2, 5));
        rs.add(Rating.create(2, 3, 4));

        dao = new EventCollectionDAO(rs);
        idao = new ItemListItemDAO(LongUtils.packedSet(0, 1, 2, 3, 4, 5));


        LenskitConfiguration config = new LenskitConfiguration();
        config.bind(EventDAO.class).to(dao);
        config.bind(ItemDAO.class).to(idao);
        config.bind(MapItemGenreDAO.class).to(gdao);
        config.bind(ItemScorer.class).to(HIRItemScorer.class);
        config.bind(PreferenceDomain.class).to(new PreferenceDomain(0, 1));
        config.bind(BaselineScorer.class, ItemScorer.class)
              .to(UserMeanItemScorer.class);
        config.bind(UserMeanBaseline.class, ItemScorer.class)
              .to(ItemMeanRatingItemScorer.class);
        engine = LenskitRecommenderEngine.build(config);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testHIRRecommenderEngineCreate() {
        try (Recommender rec = engine.createRecommender()) {
            assertThat(rec.getItemScorer(),
                       instanceOf(HIRItemScorer.class));
            RatingPredictor rp = rec.getRatingPredictor();
            assertThat(rp, notNullValue());
            assertThat(rp, instanceOf(SimpleRatingPredictor.class));
            assertThat(((SimpleRatingPredictor) rp).getItemScorer(),
                       sameInstance(rec.getItemScorer()));
            assertThat(rec.getItemRecommender(),
                       instanceOf(TopNItemRecommender.class));
        }
    }

    @Test
    public void testConfigSeparation() {
        try (LenskitRecommender rec1 = engine.createRecommender();
             LenskitRecommender rec2 = engine.createRecommender()) {

            assertThat(rec1.getItemScorer(),
                       not(sameInstance(rec2.getItemScorer())));
            assertThat(rec1.get(HIRModel.class),
                       allOf(not(nullValue()),
                             sameInstance(rec2.get(HIRModel.class))));
        }
    }
}
