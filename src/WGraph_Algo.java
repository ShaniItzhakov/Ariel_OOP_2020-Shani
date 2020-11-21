package ex1.src;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * This class implements weighted_graph_algorithms interface
 * that represents an Undirected (positive) Weighted Graph Theory algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected();
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file);
 * 6. Load(file);
 */
public class WGraph_Algo implements weighted_graph_algorithms {

	private weighted_graph graph;
	// This priority queue is used for the Dijkstra algorithm 
	private Queue<node_info> pq = new PriorityQueue<>(new NodeInfoComparable());

	/**
	 * Default constructor.
	 */
	public WGraph_Algo() {
		graph = null;
	}

	/**
	 * Shallow copy constructor that copies the graph.
	 */
	public WGraph_Algo(weighted_graph g) {
		graph = g;
	}

	/**
	 * Init the graph on which this set of algorithms operates on.
	 * @param weighted_graph g
	 */
	@Override
	public void init(weighted_graph g) {
		graph = g;
		pq.clear();
	}

	/**
	 * Return the underlying graph of which this class works.
	 * @return weighted_graph
	 */
	@Override
	public weighted_graph getGraph() {
		return graph;
	}

	/**
	 * Compute a deep copy of this weighted graph.
	 * @return weighted_graph
	 */
	@Override
	public weighted_graph copy() {
		// If this graph is null, then return null
		if (graph == null) {
			return null;
		}
		// Using deep copy constructor in Graph_DS class
		return new WGraph_DS(graph);
	}

	/** 
	 * Private function to set the tags in all the graph to the given value.
	 * @param double tag
	 */
	private void setTagsAndInfo(double tag, String info) {
		for (node_info node: graph.getV()) {
			node.setTag(tag);
			node.setInfo(info);
		}
	}

	/**
	 * Returns true if and only if there is a valid path from EVREY node to each
	 * other node in an undirected graph.
	 * @return boolean
	 */
	@Override
	public boolean isConnected() {
		// If this graph is null, then return true,
		// If the graph contains no nodes or contains only one node
		// then the graph is connected
		if (graph == null || graph.getV().size() < 2) {
			return true;
		}

		// Counter for each node that was passed through
		int counter = 0;
		// Use private function clearTags in case the tags were changed
		setTagsAndInfo(0, null);
		// Loop over the nodes in the graph
		for (node_info node: graph.getV()) {
			// Check if this node has no neighbor, if not then continue to the next node
			if (graph.getV(node.getKey()) == null) {
				continue;
			}
			// Loop over the neighbors of each node
			for (node_info neighbor: graph.getV(node.getKey())) {
				// Check if this node was not passed through
				if (node.getTag() == 0) {
					// Set the tag with 1 to indicate this node was already handled
					node.setTag(1);
					// Increment the counter of the number of nodes that were handled
					counter++;
				}
				// Check if this neighbor was not passed through
				if (neighbor.getTag() == 0) {
					// Set the tag with 1 to indicate this neighbor was already handled
					neighbor.setTag(1);
					// Increment the counter of the number of nodes that were handled
					counter++;
				}
				// Exit quickly if the counter is equal to the number of nodes in the graph
				if (counter == graph.getV().size())
					return true;
			}
		}
		// If the counter is equal to the number of nodes
		// then all the nodes in the graph are connected
		return counter == graph.getV().size();
	}

	/**
	 * Returns the length of the shortest path between src to dest
	 * Note: if no such path --> returns -1
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return double
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		//  If src or dest are not in the graph, then return -1
		if (graph == null || graph.getNode(src) == null || graph.getNode(dest) == null) {
			return -1;
		}
		//  If src and dest are equal, then return 0
		if (src == dest) {
			return 0;
		}
		// Using Dijkstra algorithm to compute the levels and place them in the node tags
		Dijkstra(src, dest);
		// Return the tag in dest node which was set to the shortest distance from src
		return graph.getNode(dest).getTag();
	}

	/** 
	 * Private function that uses Dijkstra algorithm to find the shortest path
	 * according to the weight.
	 * @param int src
	 * @return Map<Integer, Integer>
	 */
	private Map<Integer, Integer> Dijkstra(int src, int dest) {
		// Use private function setTagsAndInfo in case the tags/info were changed
		setTagsAndInfo(Double.POSITIVE_INFINITY, "False");
		// This HashMap holds in its key the son key and its value holds the parent key 
		Map<Integer, Integer> sonToParentMap = new HashMap<>();
		// Add the src node to the queue
		node_info srcNode = graph.getNode(src);
		// Set the src node tag to 0 and add it to the priority queue
		srcNode.setTag(0);
		pq.add(srcNode);
		// Loop while queue is not empty
		while (!pq.isEmpty()) {
			// Get the node with the lowest weight from the priority queue
			node_info node = pq.poll();
			// Check if node exists and if was visited	
			if (node != null && node.getInfo().equals("False")) {
				// Set the info of the node as visited
				node.setInfo("True");
				int nodeKey = node.getKey();
				// If the current node is equal to dest then return map
				// and clear the priority queue
				if (nodeKey == dest) {
					pq.clear();
					return sonToParentMap;
				}
				// Check if current node has neighbors
				if (graph.getV(nodeKey) != null && !graph.getV(nodeKey).isEmpty()) {
					// Loop over the neighbors
					for (node_info neighbor: graph.getV(nodeKey)) {
						int neighborKey = neighbor.getKey();
						// Calculate the new distance from src to this neighbor
						double newTag = node.getTag() + graph.getEdge(nodeKey, neighborKey);
						// Check if neighbor was not visited
						// and the new distance is shorter than the current neighbor tag
						if (neighbor.getInfo().equals("False") && newTag < neighbor.getTag()) {
							// Set the neighbor with the shorter distance
							neighbor.setTag(newTag);
							// Add the neighbor to the priority queue
							pq.add(neighbor);
							// Add the node (parent) and neighbor (son) keys to the HashMap
							sonToParentMap.put(neighborKey, nodeKey);
						}	
					}
				}
			}
		}	
		return sonToParentMap;
	}

	/**
	 * Returns the shortest path between src to dest - as an ordered List of nodes:
	 * src--> n1-->n2-->...dest
	 * Note if no such path --> returns null;
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return List<node_info>
	 */
	@Override
	public List<node_info> shortestPath(int src, int dest) {
		// List of nodes of the shortest path
		List<node_info> path = new ArrayList<>();
		//  If src or dest are not in the graph, then return null
		if (graph == null || graph.getNode(src) == null || graph.getNode(dest) == null) {
			return null;
		}
		//  If src and dest are equal, then return the path with the one node
		if (src == dest) {
			path.add(graph.getNode(src));
			return path;
		}
		// Using Dijkstra algorithm to get a HashMap that maps between a son and its parent keys
		Map<Integer, Integer> sonToParentMap = Dijkstra(src, dest);
		// Get the parent key of the dest
		Integer parentKey = sonToParentMap.get(dest);
		// If parent key does not exist, then return null
		// because there is no path between src and dest
		if (parentKey == null) {
			return null;
		}
		// Add the parent of dest to the list
		path.add(graph.getNode(parentKey));
		// Loop while not reaching src
		while (parentKey != src) {
			parentKey = sonToParentMap.get(parentKey);
			// Add the parent as the first item in the list
			// to make sure it is included in the list before its son
			path.add(0, graph.getNode(parentKey));
		}
		// Add the dest as the last item in the list
		path.add(graph.getNode(dest));
		return path;
	}

	/**
	 * This method saves this weighted (undirected) graph to the given
	 * file name
	 * @param file - the file name (may include a relative path).
	 * @return true - if and only if the file was successfully saved
	 */
	@Override
	public boolean save(String file) {
		try {
			if (file == null) {
				return false;
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			// Write graph object which is Serializable
			objectOutputStream.writeObject(graph);
			objectOutputStream.flush();
			// Close all streams
			objectOutputStream.close();
			fileOutputStream.close();
		}
		catch(IOException ex) {
			System.out.print("Error saving file\n" + ex);
			return false;
		}
		return true;
	}	

	/**
	 * This method loads a graph to this graph algorithm.
	 * If the file was successfully loaded - the underlying graph
	 * of this class will be changed (to the loaded one), in case the
	 * graph was not loaded the original graph should remain "as is".
	 * @param file - file name
	 * @return true - if and only if the graph was successfully loaded.
	 */
	@Override
	public boolean load(String file) {
		try {
			if (file == null) {
				return false;
			}
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			// Read graph object which is Serializable
			graph = (WGraph_DS) objectInputStream.readObject();
			// Close all streams
			objectInputStream.close();
			fileInputStream.close();
		}
		catch(Exception ex) {
			System.out.print("Error loading file\n" + ex);
			return false;
		}
		return true;
	}

	/**
	 * This class implements Comparator interface and compare tags
	 * that are used to save the distance from src.
	 * This is needed for the priority queue.
	 */
	private class NodeInfoComparable implements Comparator<node_info> {

		/**
		 * This method override compare method between two nodes
		 * according to their tag
		 * @param node_info node1
		 * @param node_info node2
		 * @return int
		 */
		@Override
		public int compare(node_info node1, node_info node2) {
			if (node1.getTag() < node2.getTag()) {
				return -1;
			}
			else if (node1.getTag() > node2.getTag()) {
				return 1;
			}
			return 0;
		}	
	}
}	