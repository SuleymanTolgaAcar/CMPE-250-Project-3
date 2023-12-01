public class Song {
    public int id;
    public String name;
    public int playCount;
    public int[] scores;

    public Song(int id, String name, int playCount, int[] scores) {
        this.id = id;
        this.name = name;
        this.playCount = playCount;
        this.scores = scores;
    }
}
