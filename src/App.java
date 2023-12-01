import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner songsScanner = new Scanner(new File("sample-test-cases/songs.txt"));
        Scanner inputScanner = new Scanner(new File("sample-test-cases/inputs/sample_1.txt"));
        FileWriter writer = new FileWriter("sample-test-cases/outputs/output.txt");
        
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
        EpicBlend epicBlend = new EpicBlend(Integer.parseInt(limitInfo[0]), new int[] { Integer.parseInt(limitInfo[1]),
                Integer.parseInt(limitInfo[2]), Integer.parseInt(limitInfo[3]) }, songs, playlists);


        int numOfPlaylists = Integer.parseInt(inputScanner.nextLine());
        for (int i = 0; i < numOfPlaylists * 2; i += 2) {
            String[] playlistInfo = inputScanner.nextLine().split(" ");
            String[] playlistSongs = inputScanner.nextLine().split(" ");
            Song[] songsInPlaylist = new Song[playlistSongs.length];
            for (int j = 0; j < playlistSongs.length; j++) {
                songsInPlaylist[j] = songs.get(Integer.parseInt(playlistSongs[j]));
            }
            playlists.put(Integer.parseInt(playlistInfo[0]), new Playlist(Integer.parseInt(playlistInfo[0]), songsInPlaylist));
        }

        epicBlend.build();

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
                    epicBlend.print(writer);
                    break;
            }
        }

        writer.close();
        songsScanner.close();
        inputScanner.close();
    }
}
