import java.util.ArrayList;
import java.util.HashMap;

public class PQueue {
    private int size;
    private ArrayList<Song> array;
    private HashMap<Integer, Integer> map = new HashMap<>();
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
            size++;
            array.add(song);
            map.put(song.id, size);
        }
        this.category = category;
        this.maxHeap = maxHeap;
        buildHeap();
    }

    public void insert(Song song) {
        if(song == null) return;
        if(map.containsKey(song.id)) return;
        size++;
        array.add(song);
        map.put(song.id, size);
        int hole = size;
        while (hole > 1 && (maxHeap ? song.compare(array.get(hole / 2), category) > 0 : song.compare(array.get(hole / 2), category) < 0)) {
            Song parent = array.get(hole / 2);
            array.set(hole / 2, song);
            array.set(hole, parent);
            map.put(parent.id, hole);
            map.put(song.id, hole / 2);
            hole = hole / 2;
        }
    }

    public Song peek() {
        if (size == 0) {
            return null;
        }
        Song maxSong = array.get(1);
        return maxSong;
    }

    public Song pop() {
        if (size == 0) {
            return null;
        }

        Song maxSong = array.get(1);
        array.set(1, array.get(size));
        map.put(array.get(1).id, 1);
        array.remove(size);
        map.remove(maxSong.id);
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
			if(child != size && (maxHeap ? array.get(child + 1).compare(array.get(child), category) > 0 : array.get(child + 1).compare(array.get(child), category) < 0)) {
				child++;
			}
			if(maxHeap ? array.get(child).compare(temp, category) > 0 : array.get(child).compare(temp, category) < 0) {
				array.set(hole, array.get(child));
                map.put(array.get(child).id, hole);
			}else {
				break;
			}
			
			hole = child;
		}
		array.set(hole, temp);
        map.put(temp.id, hole);
	}

    private void percolateUp(int hole) {
        Song temp = array.get(hole);
        while (hole > 1 && (maxHeap ? temp.compare(array.get(hole / 2), category) > 0 : temp.compare(array.get(hole / 2), category) < 0)) {
            Song parent = array.get(hole / 2);
            array.set(hole / 2, temp);
            array.set(hole, parent);
            map.put(parent.id, hole);
            map.put(temp.id, hole / 2);
            hole = hole / 2;
        }
    }

    // public void remove(int id) {
    //     if(!map.containsKey(id)) return;
    //     int index = map.get(id);
    //     if(index == size) {
    //         array.remove(size);
    //         map.remove(id);
    //         size--;
    //         return;
    //     }
    //     Song song = array.get(index);
    //     array.set(index, array.get(size));
    //     map.put(array.get(size).id, index);
    //     array.remove(size);
    //     map.remove(id);
    //     size--;
    //     if(size > 1) percolateDown(index);
    // }

    public void remove(int id) {
        if (!map.containsKey(id)) return;
        int indexToRemove = map.get(id);
        if (indexToRemove == size) {
            array.remove(size);
            size--;
            map.remove(id);
            return;
        }
        Song lastSong = array.get(size);
        array.set(indexToRemove, lastSong);
        map.put(lastSong.id, indexToRemove);
        array.remove(size);
        size--;
        map.remove(id);
        
        if (size > 1) {
            percolateDown(indexToRemove);
            percolateUp(indexToRemove);
        }
    }
    

    public ArrayList<Song> getArray() {
        return array;
    }

    public void print() {
        for (int i = 1; i <= size; i++) {
            System.out.print(array.get(i).id + "-" + array.get(i).playlistID +  "-" + array.get(i).scores[category] + " ");
        }
        System.out.println();
    }

}
