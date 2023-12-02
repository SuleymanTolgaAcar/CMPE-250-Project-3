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
        array.remove(size);
        size--;
        if(size > 1) percolateDown(1);
        return maxSong;
    }

    private void buildHeap() {
        for (int i = size / 2; i > 0; i--) {
            percolateDown(i);
        }
    }

    public int size() {
        return size;
    }

    private void percolateDown(int hole) {
		int child;
		Song temp = array.get(hole);
		
		while(hole * 2 <= size) {
			child = hole * 2;
			if(child != size && (maxHeap ? array.get(child + 1).scores[category] > array.get(child).scores[category]
                    : array.get(child + 1).scores[category] < array.get(child).scores[category])) {
				child++;
			}
			if(maxHeap ? array.get(child).scores[category] > temp.scores[category]
                    : array.get(child).scores[category] < temp.scores[category]) {
				array.set(hole, array.get(child));
			}else {
				break;
			}
			
			hole = child;
		}
		array.set(hole, temp);
	}

    public boolean remove(int id) {
        for (int i = 1; i <= size; i++) {
            if (array.get(i).id == id) {
                array.set(i, array.get(size));
                size--;
                percolateDown(i);
                return true;
            }
        }
        return false;
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
