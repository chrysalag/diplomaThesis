/**
 * Tests HIR Model Builder
 */
@SuppressWarnings("deprecation")
public class HIRModelBuilderTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    MapItemGenreDAO gdao;
    MapItemNameDAO idao;
    Collection<Long> items = new HashSet<>();
    List<Rating> rs1 = new ArrayList<>();
    List<Rating> rs2 = new ArrayList<>();

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
        idao = MapItemNameDAO.fromCSVFile(f);
        items.add((long)0);
        items.add((long)1);
        items.add((long)2);
        items.add((long)3);
        items.add((long)4);
        items.add((long)5);
    }

    @Before
    public void createRating1() throws IOException {
        rs1.add(Rating.create(1,0,5));
        rs1.add(Rating.create(1,2,5));
        rs1.add(Rating.create(2,0,4));
        rs1.add(Rating.create(2,2,4));
        rs1.add(Rating.create(3,1,5));
        rs1.add(Rating.create(4,1,1));
    }

    @Before
    public void createRating2() throws IOException {
        rs2.add(Rating.create(1, 0, 4));
        rs2.add(Rating.create(1, 4, 3));
        rs2.add(Rating.create(1, 5, 1));
        rs2.add(Rating.create(2, 0, 4));
        rs2.add(Rating.create(2, 4, 4));
        rs2.add(Rating.create(2, 5, 4));
        rs2.add(Rating.create(3, 0, 1));
        rs2.add(Rating.create(3, 4, 1));
        rs2.add(Rating.create(3, 5, 3));
    }

    private HIRModel getModel(List<Rating> rs) {
        EventDAO dao = EventCollectionDAO.create(rs);
        UserEventDAO udao = new PrefetchingUserEventDAO(dao);
        ItemDAO idao = new ItemListItemDAO(LongUtils.packedSet(0, 1, 2, 3, 4, 5));
        UserHistorySummarizer summarizer = new RatingVectorUserHistorySummarizer();
        ItemItemBuildContextProvider contextFactory = new ItemItemBuildContextProvider(
                udao, new DefaultUserVectorNormalizer(), summarizer);
        HIRModelBuilder provider = new HIRModelBuilder(idao, gdao, contextFactory.get());
        return provider.get();
    }

    @Test
    public void testBuild1() {
        HIRModel model1 = getModel(rs1);
        MutableSparseVector msv1 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector msv2 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector msv3 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector msv4 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector msv5 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector msv6 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);

        msv1.set(0, 0);
        msv1.set(1, 0);
        msv1.set(2, 1);
        msv1.set(3, 0);
        msv1.set(4, 0);
        msv1.set(5, 0);

        msv2.set(0, 0);
        msv2.set(1, 0);
        msv2.set(2, 0);
        msv2.set(3, 0);
        msv2.set(4, 0);
        msv2.set(5, 0);

        msv3.set(0, 1);
        msv3.set(1, 0);
        msv3.set(2, 0);
        msv3.set(3, 0);
        msv3.set(4, 0);
        msv3.set(5, 0);

        msv4.set(0, 0);
        msv4.set(1, 0);
        msv4.set(2, 0);
        msv4.set(3, 0);
        msv4.set(4, 0);
        msv4.set(5, 0);

        msv5.set(0, 0);
        msv5.set(1, 0);
        msv5.set(2, 0);
        msv5.set(3, 0);
        msv5.set(4, 0);
        msv5.set(5, 0);

        msv6.set(0, 0);
        msv6.set(1, 0);
        msv6.set(2, 0);
        msv6.set(3, 0);
        msv6.set(4, 0);
        msv6.set(5, 0);

        assertEquals(msv1, model1.getCoratingsVector(0, items));
        assertEquals(msv2, model1.getCoratingsVector(1, items));
        assertEquals(msv3, model1.getCoratingsVector(2, items));
        assertEquals(msv4, model1.getCoratingsVector(3, items));
        assertEquals(msv5, model1.getCoratingsVector(4, items));
        assertEquals(msv6, model1.getCoratingsVector(5, items));
    }

    @Test
    public void testBuild2() {
        HIRModel model2 = getModel(rs2);

        MutableSparseVector msv0 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector msv1 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector msv2 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector msv3 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector msv4 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector msv5 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector pv0 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector pv1 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector pv2 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector pv3 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);
        MutableSparseVector pv4 = MutableSparseVector.create(0, 1, 2, 3, 4, 5);

        msv0.set(0, 0);
        msv0.set(1, 0);
        msv0.set(2, 0);
        msv0.set(3, 0);
        msv0.set(4, 0.5);
        msv0.set(5, 0.5);

        msv1.set(0, 0);
        msv1.set(1, 0);
        msv1.set(2, 0);
        msv1.set(3, 0);
        msv1.set(4, 0);
        msv1.set(5, 0);

        msv2.set(0, 0);
        msv2.set(1, 0);
        msv2.set(2, 0);
        msv2.set(3, 0);
        msv2.set(4, 0);
        msv2.set(5, 0);

        msv3.set(0, 0);
        msv3.set(1, 0);
        msv3.set(2, 0);
        msv3.set(3, 0);
        msv3.set(4, 0);
        msv3.set(5, 0);

        msv4.set(0, 0.5);
        msv4.set(1, 0);
        msv4.set(2, 0);
        msv4.set(3, 0);
        msv4.set(4, 0);
        msv4.set(5, 0.5);

        msv5.set(0, 0.5);
        msv5.set(1, 0);
        msv5.set(2, 0);
        msv5.set(3, 0);
        msv5.set(4, 0.5);
        msv5.set(5, 0);

        pv0.set(0, 7 / 20.0);
        pv0.set(1, 7 / 20.0);
        pv0.set(2, 1 / 10.0);
        pv0.set(3, 1 / 10.0);
        pv0.set(4, 1 / 10.0);
        pv0.set(5, 0.0);

        pv1.set(0, 7 / 20.0);
        pv1.set(1, 7 / 20.0);
        pv1.set(2, 1 / 10.0);
        pv1.set(3, 1 / 10.0);
        pv1.set(4, 1 / 10.0);
        pv1.set(5, 0);

        pv2.set(0, 1 / 15.0);
        pv2.set(1, 1 / 15.0);
        pv2.set(2, 17 / 30.0);
        pv2.set(3, 7 / 30.0);
        pv2.set(4, 1 / 15.0);
        pv2.set(5, 0);

        pv3.set(0, 1 / 15.0);
        pv3.set(1, 1 / 15.0);
        pv3.set(2, 7 / 30.0);
        pv3.set(3, 2 / 5.0);
        pv3.set(4, 1 / 15.0);
        pv3.set(5, 1 / 6.0);

        pv4.set(0, 1 / 5.0);
        pv4.set(1, 1 / 5.0);
        pv4.set(2, 1 / 5.0);
        pv4.set(3, 1 / 5.0);
        pv4.set(4, 1 / 5.0);
        pv4.set(5, 0);

        assertEquals(msv0, model2.getCoratingsVector(0, items));
        assertEquals(msv1, model2.getCoratingsVector(1, items));
        assertEquals(msv2, model2.getCoratingsVector(2, items));
        assertEquals(msv3, model2.getCoratingsVector(3, items));
        assertEquals(msv4, model2.getCoratingsVector(4, items));
        assertEquals(msv5, model2.getCoratingsVector(5, items));
        assertEquals(pv0, model2.getProximityVector(0, items));
        assertEquals(pv1, model2.getProximityVector(1, items));
        assertEquals(pv2, model2.getProximityVector(2, items));
        assertEquals(pv3, model2.getProximityVector(3, items));
        assertEquals(pv4, model2.getProximityVector(4, items));
    }
}
