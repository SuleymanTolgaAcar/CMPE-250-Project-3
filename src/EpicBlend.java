import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class EpicBlend {
    private int categoryLimit;
    private int[] limits = new int[3];
    private int[] counts = new int[3];
    private HashMap<Integer, Song> songs;
    private HashMap<Integer, Playlist> playlists;

    public EpicBlend(int categoryLimit, int[] limits, HashMap<Integer, Song> songs, HashMap<Integer, Playlist> playlists) {
        this.categoryLimit = categoryLimit;
        this.limits = limits;
        this.songs = songs;
        this.playlists = playlists;
    }

    // Can use priority queue to find kth largest element
    public void build(){
        for(int i = 0; i < 3; i++){
            while(counts[i] < limits[i]){ // Break if no more songs to add
                int maxScore = -1;
                Playlist maxPlaylist = null;
                for(Playlist playlist : playlists.values()){
                    if(playlist.maxQueues[i].size() > 0 && playlist.counts[i] < categoryLimit){
                        Song song = playlist.maxQueues[i].peek();
                        if(song.scores[i] > maxScore){
                            maxScore = song.scores[i];
                            maxPlaylist = playlist;
                        }
                    }
                }
                if(maxPlaylist != null){
                    Song song = maxPlaylist.maxQueues[i].pop();
                    maxPlaylist.counts[i]++;
                    maxPlaylist.minQueues[i].insert(song);
                    counts[i]++;
                }
            }
        }
    }

    public void add(int songID, int playlistID, FileWriter writer) throws Exception{
        Song song = songs.get(songID);
        Playlist playlist = playlists.get(playlistID);
        int[] additions = new int[3];
        int[] removals = new int[3];
        for(int i = 0; i < 3; i++){
            if(playlist.counts[i] < categoryLimit && counts[i] < limits[i]){
                playlist.counts[i]++;
                playlist.minQueues[i].insert(song);
                counts[i]++;
                additions[i] = song.id;
            }
            else if(playlist.counts[i] == categoryLimit){
                Song minSong = playlist.minQueues[i].peek();
                if(song.scores[i] > minSong.scores[i]){
                    playlist.minQueues[i].pop();
                    playlist.maxQueues[i].insert(minSong);
                    playlist.minQueues[i].insert(song);
                    additions[i] = song.id;
                    removals[i] = minSong.id;
                }
            }
            else if(playlist.counts[i] < categoryLimit && counts[i] == limits[i]){
                int minScore = 100;
                Playlist minPlaylist = null;
                for(Playlist p : playlists.values()){
                    if(p.minQueues[i].size() > 0 && p.minQueues[i].peek().scores[i] < minScore){
                        minScore = p.minQueues[i].peek().scores[i];
                        minPlaylist = p;
                    }
                }
                if(minPlaylist != null){
                    Song minSong = minPlaylist.minQueues[i].pop();
                    minPlaylist.counts[i]--;
                    playlist.counts[i]++;
                    playlist.minQueues[i].insert(song);
                    minPlaylist.maxQueues[i].insert(minSong);
                    additions[i] = song.id;
                    removals[i] = minSong.id;
                }
            }
        }
        writer.write(additions[0] + " " + additions[1] + " " + additions[2] + "\n" + removals[0] + " " + removals[1] + " " + removals[2] + "\n");
    }

    public void remove(int songID, int playlistID, FileWriter writer) throws Exception{
        Song song = songs.get(songID);
        Playlist playlist = playlists.get(playlistID);
        int[] additions = new int[3];
        int[] removals = new int[3];
        for(int i = 0; i < 3; i++){
            boolean inEpicBlend = false;
            inEpicBlend = playlist.minQueues[i].remove(songID);
            if(inEpicBlend){
                counts[i]--;
                playlist.counts[i]--;
                removals[i] = song.id;
                int maxScore = -1;
                Playlist maxPlaylist = null;
                for(Playlist p : playlists.values()){
                    if(p.maxQueues[i].size() > 0 && p.maxQueues[i].peek().scores[i] > maxScore && p.counts[i] < categoryLimit){
                        maxScore = p.maxQueues[i].peek().scores[i];
                        maxPlaylist = p;
                    }
                }
                if(maxPlaylist != null){
                    Song maxSong = maxPlaylist.maxQueues[i].pop();
                    maxPlaylist.counts[i]++;
                    maxPlaylist.minQueues[i].insert(maxSong);
                    playlist.counts[i]--;
                    additions[i] = maxSong.id;
                    counts[i]++;
                }
            }
            else{
                for(PQueue queue : playlist.maxQueues){
                    queue.remove(songID);
                }
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
                            writer.write(song.id + " ");
                        }
                    }
                }
            }
        }
        writer.write("\n");
    }
}
