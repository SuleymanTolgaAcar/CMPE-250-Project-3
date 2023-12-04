public class Song {
    public int id;
    public String name;
    public int playCount;
    public int playlistID;
    public int[] scores;
    public boolean[] inEpicBlend;

    public Song(int id, String name, int playCount, int[] scores) {
        this.id = id;
        this.name = name;
        this.playCount = playCount;
        this.scores = scores;
    }

    public int compare(Song song, int category) {
        if(this.scores[category] == song.scores[category]){
            return song.name.compareTo(this.name);
        }
        return this.scores[category] - song.scores[category];
    }
}
