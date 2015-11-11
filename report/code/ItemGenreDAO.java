public interface ItemGenreDAO {
    /**
     * Get the genre for an item.
     * @param item The item ID.
     * @return A display genre for the item.
     */
    @Nullable
    RealVector getItemGenre(long item);

    /**
     * Get the number of genres in the dataset.
     * @return the number of genres in the dataset.
     */
    int getGenreSize();
}
