import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;

public class EpicBlend {
    private int categoryLimit;
    private int[] limits = new int[3];
    private int[] counts = new int[3];
    private HashMap<Integer, Song> songs;
    private HashMap<Integer, Playlist> playlists;
    private PQueue[] minQueues = new PQueue[3];
    private HashSet<Integer>[] inEpicBlend = new HashSet[3];

    public EpicBlend(int categoryLimit, int[] limits, HashMap<Integer, Song> songs, HashMap<Integer, Playlist> playlists) {
        this.categoryLimit = categoryLimit;
        this.limits = limits;
        this.songs = songs;
        this.playlists = playlists;
        for(int i = 0; i < 3; i++){
            minQueues[i] = new PQueue(i, false);
            inEpicBlend[i] = new HashSet<>();
        }
    }

    // Can use priority queue to find kth largest element
    public void build(){
        for(int i = 0; i < 3; i++){
            while(counts[i] < limits[i]){
                Song maxSong = null;
                Playlist maxPlaylist = null;
                for(Playlist playlist : playlists.values()){
                    if(playlist.maxQueues[i].size() > 0 && playlist.counts[i] < categoryLimit){
                        Song song = playlist.maxQueues[i].peek();
                        if(maxSong == null || song.compare(maxSong, i) > 0){
                            maxSong = song;
                            maxPlaylist = playlist;
                        }
                    }
                }
                if(maxSong != null){
                    maxPlaylist.maxQueues[i].pop();
                    maxPlaylist.counts[i]++;
                    maxPlaylist.minQueues[i].insert(maxSong);
                    minQueues[i].insert(maxSong);
                    inEpicBlend[i].add(maxSong.id);
                    counts[i]++;
                }
                else{
                    break;
                }
            }
        }
    }

    public void add(int songID, int playlistID, FileWriter writer) throws Exception{
        Song song = songs.get(songID);
        Playlist playlist = playlists.get(playlistID);
        song.playlistID = playlistID;
        int[] additions = new int[3];
        int[] removals = new int[3];
        for(int i = 0; i < 3; i++){
            if(playlist.counts[i] < categoryLimit && counts[i] < limits[i]){
                playlist.counts[i]++;
                playlist.minQueues[i].insert(song);
                minQueues[i].insert(song);
                counts[i]++;
                additions[i] = song.id;
                inEpicBlend[i].add(song.id);
            }
            else if(playlist.counts[i] == categoryLimit){
                Song minSong = playlist.minQueues[i].peek();
                if(song.compare(minSong, i) > 0){
                    playlist.minQueues[i].pop();
                    playlist.maxQueues[i].insert(minSong);
                    playlist.minQueues[i].insert(song);
                    minQueues[i].remove(minSong.id);
                    minQueues[i].insert(song);
                    additions[i] = song.id;
                    removals[i] = minSong.id;
                    inEpicBlend[i].add(song.id);
                    inEpicBlend[i].remove(minSong.id);
                }
            }
            else if(playlist.counts[i] < categoryLimit && counts[i] == limits[i]){
                Song minSong = minQueues[i].peek();
                if(minSong != null && song.compare(minSong, i) > 0){
                    if(minSong.playlistID == -1) {
                        System.out.println(minSong.id + " " + song.id + " " + i + " " + playlist.id);
                    }
                    Playlist minPlaylist = playlists.get(minSong.playlistID);
                    minPlaylist.minQueues[i].pop();
                    minQueues[i].pop();
                    minPlaylist.maxQueues[i].insert(minSong);
                    minPlaylist.counts[i]--;
                    playlist.counts[i]++;
                    playlist.minQueues[i].insert(song);
                    minQueues[i].insert(song);
                    additions[i] = song.id;
                    removals[i] = minSong.id;
                    inEpicBlend[i].add(song.id);
                    inEpicBlend[i].remove(minSong.id);
                }
            }
        }
        for(int i = 0; i < 3; i++){
            if(additions[i] == 0){
                playlist.maxQueues[i].insert(song);
            }
        }
        writer.write(additions[0] + " " + additions[1] + " " + additions[2] + "\n" + removals[0] + " " + removals[1] + " " + removals[2] + "\n");
    }

    public void remove(int songID, int playlistID, FileWriter writer) throws Exception{
        Song song = songs.get(songID);
        Playlist playlist = playlists.get(playlistID);
        song.playlistID = -1;
        int[] additions = new int[3];
        int[] removals = new int[3];
        for(int i = 0; i < 3; i++){
            if(inEpicBlend[i].contains(song.id)){
                Song maxSong = null;
                Playlist maxPlaylist = null;
                counts[i]--;
                playlist.counts[i]--;
                for(Playlist p : playlists.values()){
                    if(p.maxQueues[i].peek() != null){
                        Song s = p.maxQueues[i].peek();
                        if((maxSong == null || s.compare(maxSong, i) > 0) && p.counts[i] < categoryLimit){
                            maxSong = s;
                            maxPlaylist = p;
                        }
                    }
                }
                if(maxSong != null){
                    maxPlaylist.maxQueues[i].pop();
                    maxPlaylist.minQueues[i].insert(maxSong);
                    minQueues[i].insert(maxSong);
                    additions[i] = maxSong.id;
                    inEpicBlend[i].add(maxSong.id);
                    maxPlaylist.counts[i]++;
                    counts[i]++;
                }
                playlist.minQueues[i].remove(song.id);
                minQueues[i].remove(song.id);
                inEpicBlend[i].remove(song.id);
                removals[i] = song.id;
            }
            else{
                playlist.maxQueues[i].remove(song.id);
            }
        }
        writer.write(additions[0] + " " + additions[1] + " " + additions[2] + "\n" + removals[0] + " " + removals[1] + " " + removals[2] + "\n");
    }

    public void print(FileWriter writer) throws Exception{
        for(Playlist playlist : playlists.values()){
            for(int i = 0; i < 3; i++){
                if(playlist.minQueues[i].size() > 0){
                    for(Song song : playlist.minQueues[i].getArray()){
                        if(song != null){
                            writer.write(i + "-" + song.id + "-" + playlist.id + " ");
                        }
                    }
                }
            }
        }
        writer.write("\n");
    }
}
