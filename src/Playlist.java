public class Playlist {
    public int id;
    public int[] counts = new int[3];
    public PQueue[] minQueues = new PQueue[3];
    public PQueue[] maxQueues = new PQueue[3];

    public Playlist(int id, Song[] songs) {
        this.id = id;
        for (int i = 0; i < 3; i++) {
            maxQueues[i] = new PQueue(songs, i, true);
            minQueues[i] = new PQueue(i, false);
        }
    }
}
