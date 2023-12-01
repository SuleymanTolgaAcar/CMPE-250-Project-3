import java.util.ArrayList;

public class PQueue {
    private int size;
    private ArrayList<Song> array;
    public int category;
    private boolean maxHeap;

    PQueue(int category, boolean maxHeap) {
        array = new ArrayList<>();
        array.add(null);
        this.category = category;
        this.maxHeap = maxHeap;
    }

    PQueue(Song[] songs, int category, boolean maxHeap) {
        array = new ArrayList<>();
        array.add(null);
        for (Song song : songs) {
            array.add(song);
            size++;
        }
        this.category = category;
        this.maxHeap = maxHeap;
        buildHeap();
    }

    public void insert(Song song) {
        size++;
        array.add(song);
        int hole = size;
        while (hole > 1 && (maxHeap ? song.scores[category] > array.get(hole / 2).scores[category]
                : song.scores[category] < array.get(hole / 2).scores[category])) {
            Song parent = array.get(hole / 2);
            array.set(hole / 2, song);
            array.set(hole, parent);
            hole = hole / 2;
        }
    }

    public Song peek() {
        return array.get(1);
    }

    public Song pop() {
        Song maxSong = peek();
        array.set(1, array.get(size));
        size--;
        bubbleDown(1);
        return maxSong;
    }

    private void buildHeap() {
        for (int i = size / 2; i > 0; i--) {
            bubbleDown(i);
        }
    }

    public int size() {
        return size;
    }

    private void bubbleDown(int hole) {
        Song tmp = array.get(hole);
        int child;

        for (; hole * 2 <= size; hole = child) {
            child = hole * 2;
            if (child != size && (maxHeap ? array.get(child + 1).scores[category] > array.get(child).scores[category]
                    : array.get(child + 1).scores[category] < array.get(child).scores[category])) {
                child++;
            }
            if (maxHeap ? array.get(child).scores[category] > tmp.scores[category]
                    : array.get(child).scores[category] < tmp.scores[category]) {
                array.set(hole, array.get(child));
            } else {
                break;
            }
        }
        array.set(hole, tmp);
    }

    public boolean remove(int id) {
        int hole = 0;
        for (int i = 1; i <= size; i++) {
            if (array.get(i).id == id) {
                hole = i;
                break;
            }
        }
        if (hole == 0) {
            return false;
        }
        array.set(hole, array.get(size));
        size--;
        bubbleDown(hole);
        return true;
    }

    public ArrayList<Song> getArray() {
        return array;
    }
}
