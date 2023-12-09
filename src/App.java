import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        double startTime = System.currentTimeMillis();
        Scanner songsScanner = new Scanner(new File("test-cases/songs.txt"));
        Scanner inputScanner = new Scanner(new File("test-cases/inputs/tiny_playlists_small.txt"));
        FileWriter writer = new FileWriter("test-cases/outputs/output.txt");
        
        HashMap<Integer, Song> songs = new HashMap<Integer, Song>();
        HashMap<Integer, Playlist> playlists = new HashMap<Integer, Playlist>();


        int numOfSongs = Integer.parseInt(songsScanner.nextLine());
        for (int i = 0; i < numOfSongs; i++) {
            String[] songInfo = songsScanner.nextLine().split(" ");
            Song song = new Song(Integer.parseInt(songInfo[0]), songInfo[1], Integer.parseInt(songInfo[2]), new int[] {
                    Integer.parseInt(songInfo[3]), Integer.parseInt(songInfo[4]), Integer.parseInt(songInfo[5]) });
            songs.put(Integer.parseInt(songInfo[0]), song);
        }
        
        String[] limitInfo = inputScanner.nextLine().split(" ");

        int numOfPlaylists = Integer.parseInt(inputScanner.nextLine());
        for (int i = 0; i < numOfPlaylists; i++) {
            String[] playlistInfo = inputScanner.nextLine().split(" ");
            String[] playlistSongs = inputScanner.nextLine().split(" ");
            int playlistID = Integer.parseInt(playlistInfo[0]);
            int numOfSongsInPlaylist = Integer.parseInt(playlistInfo[1]);
            Song[] songsInPlaylist = new Song[numOfSongsInPlaylist];
            for (int j = 0; j < numOfSongsInPlaylist; j++) {
                Song currentSong = songs.get(Integer.parseInt(playlistSongs[j]));
                songsInPlaylist[j] = currentSong;
                currentSong.playlistID = playlistID;
            }
            playlists.put(playlistID, new Playlist(playlistID, songsInPlaylist));
        }

        double buildStartTime = System.currentTimeMillis();
        EpicBlend epicBlend = new EpicBlend(Integer.parseInt(limitInfo[0]), new int[] { Integer.parseInt(limitInfo[1]),
        Integer.parseInt(limitInfo[2]), Integer.parseInt(limitInfo[3]) }, songs, playlists);
        epicBlend.build();
        System.out.println("Build Time: " + (System.currentTimeMillis() - buildStartTime) / 1000 + " seconds");

        int numOfOperations = Integer.parseInt(inputScanner.nextLine());
        for(int i = 0; i < numOfOperations; i++){
            String[] operationInfo = inputScanner.nextLine().split(" ");
            String operation = operationInfo[0];
            int songID; int playlistID;

            switch(operation){
                case "ADD":
                    songID = Integer.parseInt(operationInfo[1]);
                    playlistID = Integer.parseInt(operationInfo[2]);
                    epicBlend.add(songID, playlistID, writer);
                    break;

                case "REM":
                    songID = Integer.parseInt(operationInfo[1]);
                    playlistID = Integer.parseInt(operationInfo[2]);
                    epicBlend.remove(songID, playlistID, writer);
                    break;

                case "ASK":
                    epicBlend.ask(writer);
                    break;
            }
        }

        writer.close();
        songsScanner.close();
        inputScanner.close();
        System.out.println("Total Time: " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
    }
}
