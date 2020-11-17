#EXERCISE1

####Data structures used:

**HashMap**


HashMap class implements Map interface.
It is a Map based collection class that is used for storing key and value pairs.
It works on hashing principles. Hash functions are used to link key and value in HashMap.
HashMap is a data structure which allows us to store object and retrieve it in
constant time O(1) provided we know the key.
This class makes no guarantees as to the order of the map.

HasHMap is used in the following classes:

WGraph_DS: A HashMap that holds the nodes of the graph. 
Another HashMap of HashMaps that holds for each node its neighbors and their weights.
WGraph_Algo: The Dijkstra algorithm returns a HashMap that maps
between a son and its parent keys.
It is used to trace back from the dest node to the src node.

**ArrayList**

ArrayList class implements List interface, it is based on an Array data structure and it is a resizable array.
It implements all optional list operations, and permits all elements, including null.

ArrayList is used in the following classes:

WGraph_Algo: The ArrayList is used to return a node list of the shortest path.

**PriorityQueue**

A priority queue is a special type of queue in which each element is associated with a priority
and is served according to its priority.
If elements with the same priority occur, they are served according to their order in the queue.

PriorityQueue is used in the following class:

WGraph_Algo: The priority queue is used for the Dijkstra algorithm,
it is used to return a node list of the shortest path by polling each time the lowest tag
which holds the distance between a node and the src node.
The priority queue is initialized with a class NodeInfoComparable
that implements the Comparator interface.
It is used by the priority queue to compare between the tags of each node info. 

####Algorithm used:

**Dijkstra**

Dijkstra is an algorithm that is used to graph data or searching tree or traversing structures.
Dijkstra's algorithm computes the cost of the shortest paths from a given starting node
to all other nodes in the graph in O(E*logV+V) - (E-Edges, V-Vertices).
The algorithm starts at its starting node and iteratively updates the current shortest paths
from the current node. The algorithm may, in the process, find shortcuts.
It continues until all edges have been visited and no improvement is possible.
The algorithm keeps the nodes that it has found, but not processed yet, in a priority queue.
The nodes with the smallest distance to the starting point is the first node in the queue.

Dijkstra is used in WGraph_Algo to find the shortest path
and shortest path distance by weight cost.
