import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class EpicBlend {
    private int categoryLimit;
    private int[] limits = new int[3];
    private int[] counts = new int[3];
    private HashMap<Integer, Song> songs;
    private HashMap<Integer, Playlist> playlists;
    private PQueue[] minQueues = new PQueue[3];
    private PQueue[] maxQueues = new PQueue[3];
    private PQueue[] maxBuildQueues = new PQueue[3];
    private HashSet<Integer>[] inEpicBlend = new HashSet[3];

    public EpicBlend(int categoryLimit, int[] limits, HashMap<Integer, Song> songs, HashMap<Integer, Playlist> playlists) {
        this.categoryLimit = categoryLimit;
        this.limits = limits;
        this.songs = songs;
        this.playlists = playlists;
        for(int i = 0; i < 3; i++){
            minQueues[i] = new PQueue(i, false);
            maxQueues[i] = new PQueue(i, true);
            maxBuildQueues[i] = new PQueue(i, true);
            for (Playlist playlist : playlists.values()) {
                if(playlist.maxQueues[i].peek() == null) continue;
                maxQueues[i].insert(playlist.maxQueues[i].peek());
                maxBuildQueues[i].insert(playlist.maxQueues[i].peek());
            }
            inEpicBlend[i] = new HashSet<>();
        }
    }

    public void build(){
        for(int i = 0; i < 3; i++){
            while(counts[i] < limits[i] && maxBuildQueues[i].size() > 0){
                Song maxSong = maxBuildQueues[i].pop();
                Playlist maxPlaylist = playlists.get(maxSong.playlistID);
                if(maxPlaylist.counts[i] < categoryLimit){
                    maxPlaylist.maxQueues[i].pop();
                    maxPlaylist.minQueues[i].insert(maxSong);
                    minQueues[i].insert(maxSong);
                    maxQueues[i].remove(maxSong.id);
                    if(maxPlaylist.maxQueues[i].size() > 0) maxQueues[i].insert(maxPlaylist.maxQueues[i].peek());
                    if(maxPlaylist.maxQueues[i].size() > 0) maxBuildQueues[i].insert(maxPlaylist.maxQueues[i].peek());
                    maxPlaylist.counts[i]++;
                    counts[i]++;
                    inEpicBlend[i].add(maxSong.id);
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
                    Song oldMax = playlist.maxQueues[i].peek();
                    playlist.maxQueues[i].insert(minSong);
                    if(oldMax == null){
                        maxQueues[i].insert(playlist.maxQueues[i].peek());
                    }
                    else if(oldMax.id != playlist.maxQueues[i].peek().id){
                        maxQueues[i].remove(oldMax.id);
                        maxQueues[i].insert(playlist.maxQueues[i].peek());
                    }
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
                    Playlist minPlaylist = playlists.get(minSong.playlistID);
                    minPlaylist.minQueues[i].pop();
                    minQueues[i].pop();
                    Song oldMax = minPlaylist.maxQueues[i].peek();
                    minPlaylist.maxQueues[i].insert(minSong);
                    if(oldMax == null){
                        maxQueues[i].insert(minPlaylist.maxQueues[i].peek());
                    }
                    else if(oldMax.id != minPlaylist.maxQueues[i].peek().id){
                        maxQueues[i].remove(oldMax.id);
                        maxQueues[i].insert(minPlaylist.maxQueues[i].peek());
                    }
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
                Song oldMax = playlist.maxQueues[i].peek();
                playlist.maxQueues[i].insert(song);
                if(oldMax == null){
                    maxQueues[i].insert(playlist.maxQueues[i].peek());
                }
                else if(oldMax.id != playlist.maxQueues[i].peek().id){
                    maxQueues[i].remove(oldMax.id);
                    maxQueues[i].insert(playlist.maxQueues[i].peek());
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
            if(inEpicBlend[i].contains(song.id)){
                playlist.counts[i]--;
                counts[i]--;
                if(maxQueues[i].size() > 0){
                    Song maxSong = maxQueues[i].peek();
                    Playlist maxPlaylist = playlists.get(maxSong.playlistID);
                    ArrayList<Song> maxArray = maxQueues[i].getArray();
                    int level = 2;
                    while(maxPlaylist.counts[i] == categoryLimit && maxQueues[i].size() > 1){
                        Song tempMaxSong = null;
                        for(int j = (int) Math.pow(2, level - 1); (j < Math.pow(2, level) && j <= maxQueues[i].size()); j++){
                            Song currentSong = maxArray.get(j);
                            Playlist currentPlaylist = playlists.get(currentSong.playlistID);
                            if(currentSong != null && currentPlaylist.counts[i] < categoryLimit && (tempMaxSong == null || currentSong.compare(tempMaxSong, i) > 0)){
                                tempMaxSong = currentSong;
                            }
                        }
                        if(tempMaxSong == null) break;
                        maxSong = tempMaxSong;
                        maxPlaylist = playlists.get(maxSong.playlistID);
                        if(Math.pow(2, level) > maxQueues[i].size()) break;
                        level++;
                    }
                    if(maxSong != null && maxPlaylist.counts[i] < categoryLimit){
                        maxPlaylist.maxQueues[i].pop();
                        maxPlaylist.minQueues[i].insert(maxSong);
                        minQueues[i].insert(maxSong);
                        maxQueues[i].remove(maxSong.id);
                        if(maxPlaylist.maxQueues[i].size() > 0) maxQueues[i].insert(maxPlaylist.maxQueues[i].peek());
                        maxPlaylist.counts[i]++;
                        counts[i]++;
                        additions[i] = maxSong.id;
                        inEpicBlend[i].add(maxSong.id);
                    }
                }
                minQueues[i].remove(song.id);
                removals[i] = song.id;
                playlist.minQueues[i].remove(song.id);
                inEpicBlend[i].remove(song.id);
            }
            else{
                Song oldMax = playlist.maxQueues[i].peek();
                playlist.maxQueues[i].remove(song.id);
                if(playlist.maxQueues[i].size() > 0 && oldMax.id != playlist.maxQueues[i].peek().id){
                    maxQueues[i].insert(playlist.maxQueues[i].peek()); //l
                }
                maxQueues[i].remove(song.id);
            }
        }
        song.playlistID = -1;
        writer.write(additions[0] + " " + additions[1] + " " + additions[2] + "\n" + removals[0] + " " + removals[1] + " " + removals[2] + "\n");
    }

    public static int partition(Song A[], int l, int h) {
        Song pivot = A[l];
        int i = l, j = h;
        do {
            do {i++;} while (A[i].playCount > pivot.playCount || 
                (A[i].playCount == pivot.playCount && A[i].name.compareTo(pivot.name) <= 0));
        
            do {j--;} while (A[j].playCount < pivot.playCount || 
                (A[j].playCount == pivot.playCount && A[j].name.compareTo(pivot.name) > 0));

            if (i < j) {
                Song temp = A[i];
                A[i] = A[j];
                A[j] = temp;
            }

        } while (i < j);

        Song temp = A[l];
        A[l] = A[j];
        A[j] = temp;
        return j;
    }

    public static void quickSort(Song A[], int l, int h) {
        int j;
        if (l < h) {
            j = partition(A, l, h);
            quickSort(A, l, j);
            quickSort(A, j+1, h);
        }
    }

    public void ask(FileWriter writer) throws Exception{
        Song[] epicBlendSongs = new Song[minQueues[0].size() + minQueues[1].size() + minQueues[2].size() + 1];
        int index = 0;
        for(int i = 0; i < 3; i++){
            for(Song song : minQueues[i].getArray()){
                if(song != null){
                    epicBlendSongs[index] = song;
                    index++;
                }
            }
        }
        epicBlendSongs[index] = new Song(0, "inf", Integer.MIN_VALUE, new int[3]);
        quickSort(epicBlendSongs, 0, epicBlendSongs.length - 1);
        for(int i = 0; i < epicBlendSongs.length - 1; i++){
            if(epicBlendSongs[i].id == epicBlendSongs[i + 1].id) continue;
            if(i == epicBlendSongs.length - 2){
                writer.write(epicBlendSongs[i].id + "\n");
                break;
            }
            writer.write(epicBlendSongs[i].id + " ");
        }
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
