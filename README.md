# Car Trip (A Heap Project)

In this project, we are on a car trip and in charge of the playlist. We have many playlists and many songs in those playlists. We want to choose the "best" songs from those playlists based on their category scores. We call this selection of songs the "Epic Blend". There are 3 categories: Heartache, Roadtrip, Blissful. We have a limit on the number of songs we can choose from each playlist in each category (I will call this limit the "playlist limit" from now on). Also, there is a limit for the number of songs we can choose for a category overall (and "category limit" for this one). After the initial selection process, the operations come: add or remove a song from a playlist, and ask operation to print the whole Epic Blend. We want to do all these operations efficiently. Therefore, I used heaps to implement this project.

## Operations

### - Add a song to a playlist

Add a song to a playlist. Make necessary changes to the epic blend:

1. If the category limit and the playlist limit are not exceeded, add the song to the epic blend.
2. If the playlist limit is exceeded and the new song scores better than the lowest scored song from the epic blend which is in the same playlist as the song we want to add, remove the lowest scored song. Then add the song to the epic blend.
3. If the category limit is exceeded and the lowest scored song from the epic blend which is in the same category as the song we want to add, remove the lowest scored song. Then add the song to the epic blend.

### - Remove a song from a playlist

Remove a song from a playlist. Make necessary changes to the epic blend:

1. If the song is in the epic blend, remove it from the epic blend. Then, add the highest scored song which does not exceed the limits to the epic blend.
2. If the song is not in the epic blend, just remove it from the playlist.

### - Ask operation

Sort the epic blend based on the play count of the songs. Then print the epic blend.

## Implementation

I used min and max heaps to implement this project. Each playlist has a total of 6 heaps: 3 min heaps and 3 max heaps for each category. The min heaps are used to keep the songs in the playlist which is in the epic blend. The max heaps are used to store the songs in the playlist which is not in the epic blend. Other than these, I have 6 more heaps: 3 min heaps and 3 max heaps for the epic blend as well. The min heaps are used for the third case of the add operation and the max heaps are used for the first case of the remove operation. This approach sort of ignores the space complexity but it makes the time complexity so much better. Since I have strict time limits, I chose this approach.

For the second case of the add operation, I made use of the min heaps of the playlist. I popped the minimum scored song from the min heap of the playlist and added the new song to the epic blend. For the third case of the add operation, I made use of the min heaps of the epic blend. I popped the minimum scored song from the min heap of the epic blend and added the new song to the epic blend.

For the first case of the remove operation, I made use of the max heaps of the epic blend. I popped the maximum scored song from the max heap of the epic blend and added it to the epic blend while removing the song we want to remove.

For the ask operation, I sorted the epic blend based on the play count of the songs. I used the quick sort algorithm for this.

The most challenging part of this project was to handle the remove operation in O(logn) especially for the first case. I had to implement a remove method for the heaps. I used a hash map to store the index of the songs in the heaps. This way, I could find the index of the song in the heap in O(1) and remove it in O(logn). After that, I needed to find the "best" song to replace the removed song. I kept the songs which are not in the epic blend in the max heaps of the epic blend. But if the root of this max heap exceeds the limits, I needed to find a second best which does not exceed the limits. However, this made the remove operation O(n). To overcome this, I stored only the roots of the max heaps of the playlists in the max heap of the epic blend. Also, if the root of a max of a playlist exceeds the limits, I removed it from the max heap of the epic blend, and when it is not exceeding the limits anymore, I added it back to the max heap of the epic blend. Keeping the max heaps of the epic blend and the max heaps of the playlists in sync was the most challenging part of this project. But after all, with this approach I could implement the remove operation in O(logn).

## Complexity Analysis

### - Add a song to a playlist

Works in O(logn) time for all cases.

### - Remove a song from a playlist

Works in O(logn) time for all cases.

### - Ask operation

Works in O(nlogn) time.

## Testcases

I only uploaded the small testcases. I also tested the program with the large testcases. The program works correctly and efficiently for all testcases. However, the large testcases' outputs are too long to upload (about 2 million lines).
