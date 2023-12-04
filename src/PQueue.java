import java.util.ArrayList;
import java.util.HashSet;

public class PQueue {
    private int size;
    private ArrayList<Song> array;
    private HashSet<Integer> removed = new HashSet<>();
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
        removed.remove(song.id);
        int hole = size;
        while (hole > 1 && (maxHeap ? song.compare(array.get(hole / 2), category) > 0 : song.compare(array.get(hole / 2), category) < 0)) {
            Song parent = array.get(hole / 2);
            array.set(hole / 2, song);
            array.set(hole, parent);
            hole = hole / 2;
        }
    }

    public Song peek() {
        if (size == 0) {
            return null;
        }
        Song maxSong = array.get(1);
        while (removed.contains(maxSong.id)) {
            pop();
            if(size == 0) return null;
            maxSong = array.get(1);
        }
        return maxSong;
    }

    public Song pop() {
        if (size == 0) {
            return null;
        }
        Song maxSong = array.get(1);
        do{
            removed.remove(maxSong.id);
            array.set(1, array.get(size));
            array.remove(size);
            size--;
            if(size > 1) percolateDown(1);
            if(size == 0) return null;
            maxSong = array.get(1);
        }while(removed.contains(maxSong.id));

        return maxSong;
    }

    private void buildHeap() {
        for (int i = size / 2; i > 0; i--) {
            percolateDown(i);
        }
    }

    public int size() {
        return size - removed.size();
    }

    private void percolateDown(int hole) {
		int child;
		Song temp = array.get(hole);
		
		while(hole * 2 <= size) {
			child = hole * 2;
			if(child != size && (maxHeap ? array.get(child + 1).compare(array.get(child), category) > 0 : array.get(child + 1).compare(array.get(child), category) < 0)) {
				child++;
			}
			if(maxHeap ? array.get(child).compare(temp, category) > 0 : array.get(child).compare(temp, category) < 0) {
				array.set(hole, array.get(child));
			}else {
				break;
			}
			
			hole = child;
		}
		array.set(hole, temp);
	}

    public void remove(int id) {
        removed.add(id);
    }

    public ArrayList<Song> getArray() {
        return array;
    }

    public void print() {
        for (int i = 1; i <= size; i++) {
            System.out.print(array.get(i).id + " ");
        }
        System.out.println();
    }

}
