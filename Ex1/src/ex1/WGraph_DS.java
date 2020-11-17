package ex1;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class implements weighted_graph interface
 * that represents an undirected weighted graph.
 */
public class WGraph_DS implements weighted_graph, Serializable {

	private static final long serialVersionUID = 1L;

	// This HashMap holds the nodes of this graph
	private HashMap<Integer, node_info> nodes = new HashMap<>();
	// This HashMap holds the neighbor weights of each node 
	private HashMap<Integer, HashMap<node_info, Double>> neighborWeights = new HashMap<>();
	
	private int edgeSize;
	private int modeCounter;

	/**
	 * Default constructor.
	 */
	public WGraph_DS() {
		edgeSize = 0;
		modeCounter = 0;
	}

	/**
	 * Deep copy constructor that copies
	 * an existing graph and creates a new graph.
	 */
	public WGraph_DS(weighted_graph graph) {
		// Check if graph is null, if null do nothing
		if (graph == null) {
			return;
		}
		// Loop over the graph nodes
		for (node_info node: graph.getV()) {
			int nodeKey = node.getKey();
			// Create a new node and add it to the graph
			nodes.put(nodeKey, new NodeInfo(node));
			
			// Check if this node has no neighbor, if not then continue to the next node
			if (graph.getV(nodeKey) == null) {
				continue;
			}
			// Loop over the neighbors of this node
			for (node_info neighbor: graph.getV(nodeKey)) {
				int neighborKey = neighbor.getKey();
				// Check if keys are not the same and if they are neighbors
				if (nodeKey != neighborKey && graph.hasEdge(nodeKey, neighborKey)) {
					// Connect the nodes
					connect(nodeKey, neighborKey, graph.getEdge(nodeKey, neighborKey));
				}
			}	
		}
		// Set the mode counter to the same value as the copied graph
		modeCounter = graph.getMC();
	}

	/**
	 * Returns the node_info by the key.
	 * @param int key
	 * @return the node_info by the key, null if none.
	 */
	@Override
	public node_info getNode(int key) {
		return nodes.get(key);
	}

	/**
	 * Returns true if and only if there
	 * is an edge between node1 and node2
	 * @param int node1
	 * @param int node2
	 * @return boolean
	 */
	@Override
	public boolean hasEdge(int node1, int node2) {
		node_info nodeInfo1 = nodes.get(node1);
		node_info nodeInfo2 = nodes.get(node2);
		HashMap<node_info, Double> node1Neighbors = neighborWeights.get(node1);
		HashMap<node_info, Double> node2Neighbors = neighborWeights.get(node2);
		// Check if node1 is not equal to node2, if they exist in the graph and if they have neighbors
		if (node1 != node2 && nodeInfo1 != null && nodeInfo2 != null 
				&&  node1Neighbors != null && node2Neighbors != null) {
			// Check if and only if node1 is neighbor of node2
			if (node1Neighbors.containsKey(nodeInfo2) && node2Neighbors.containsKey(nodeInfo1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the weight of the edge between node1 and node2.
	 * In case there is no such edge - returns -1
	 * @param int node1
	 * @param int node2
	 * @return double
	 */
	@Override
	public double getEdge(int node1, int node2) {
		double weight = -1;
		node_info nodeInfo1 = nodes.get(node1);
		node_info nodeInfo2 = nodes.get(node2);
		HashMap<node_info, Double> node1Neighbors = neighborWeights.get(node1);
		HashMap<node_info, Double> node2Neighbors = neighborWeights.get(node2);
		// Check if node1 is not equal to node2, if they exist in the graph and if they have neighbors
				if (node1 != node2 && nodeInfo1 != null && nodeInfo2 != null 
						&&  node1Neighbors != null && node2Neighbors != null) {
			// Get the weight if and only if node1 is neighbor of node2
			if (node1Neighbors.get(nodeInfo2) != null && node2Neighbors.get(nodeInfo1) != null) {
				weight = node1Neighbors.get(nodeInfo2);
			}
		}
		return weight;
	}

	/**
	 * Adds a new node to the graph with the given key.
	 * If there is already a node with such a key -> no action is performed.
	 * @param int key
	 */
	@Override
	public void addNode(int key) {
		if (!nodes.containsKey(key)) {
			nodes.put(key, new NodeInfo(key));
			modeCounter++;
		}
	}

	/**
	 * Connect an edge between node1 and node2, with an edge with weight >=0.
	 * If the edge node1-node2 already exists
	 * then the method simply updates the weight of the edge.
	 * @param int node1
	 * @param int node2
	 * @param double w - weight
	 */
	@Override
	public void connect(int node1, int node2, double w) {
		// If node1 is equal to node2 or if they are not in the graph
		// or the weight is less then 0, then do nothing
		if (node1 == node2 || nodes.get(node1) == null || nodes.get(node2) == null || w < 0) {
			return;
		}
		// Look for node1 neighbor weights
		HashMap<node_info, Double> neighborWeights1 = neighborWeights.get(node1);
		// If none exists then create a new inner hash map and put it in the hash map
		if (neighborWeights1 == null) {
			neighborWeights1 = new HashMap<>();
			neighborWeights.put(node1, neighborWeights1);
		}
		// Look for node2 neighbor weights
		HashMap<node_info, Double> neighborWeights2 = neighborWeights.get(node2);
		// If none exists then create a new inner hash map and put it in the hash map
		if (neighborWeights2 == null) {
			neighborWeights2 = new HashMap<>();
			neighborWeights.put(node2, neighborWeights2);
		}
		
		node_info nodeInfo1 = nodes.get(node1);
		node_info nodeInfo2 = nodes.get(node2);
		// Check if node1 and node2 are not neighbors
		if (neighborWeights1.get(nodeInfo2) == null && neighborWeights2.get(nodeInfo1) == null) {
			// Connect between node1 and node2
			neighborWeights1.put(nodeInfo2, w);
			neighborWeights2.put(nodeInfo1, w);
			edgeSize++;
			modeCounter++;
		}
		// If node1 and node2 are neighbors
		else {
			// Update the weight between node1 and node2
			neighborWeights1.put(nodeInfo2, w);
			neighborWeights2.put(nodeInfo1, w);
			modeCounter++;
		}
	}

	/**
	 * This method returns a pointer (shallow copy) for a
	 * Collection representing all the nodes in the graph.
	 * @return Collection<node_info>
	 */
	@Override
	public Collection<node_info> getV() {
		return nodes.values();
	}

	/**
	 * This method returns a Collection containing all the
	 * nodes connected to node_id
	 * @param int node_id - key
	 * @return Collection<node_info>
	 */
	@Override
	public Collection<node_info> getV(int node_id) {
		if (neighborWeights.get(node_id) != null) {
			return neighborWeights.get(node_id).keySet();
		}
		return null;
	}

	/**
	 * Deletes the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 * @param int key
	 * @return node_info - the data of the removed node (null if none).
	 */
	@Override
	public node_info removeNode(int key) {
		node_info nodeInfo = nodes.get(key);
		if (nodeInfo != null) {
			// Loop over the neighbors
			for (int neighborKey: neighborWeights.keySet()) {
				// Remove the edge between this node and this neighbor
				removeEdge(key, neighborKey);
			}
			// Remove the node from the list of nodes in the graph
			nodeInfo = nodes.remove(key);	
			modeCounter++;
		}
		return nodeInfo;
	}

	/**
	 * Deletes the edge from the graph.
	 * @param int node1
	 * @param int node2
	 */
	@Override
	public void removeEdge(int node1, int node2) {
		// If node1 is equal to node2 or if they are not in the graph then do nothing
		if (node1 == node2 || nodes.get(node1) == null || nodes.get(node2) == null) {
			return;
		}
		HashMap<node_info, Double> neighborWeights1 = neighborWeights.get(node1);
		HashMap<node_info, Double> neighborWeights2 = neighborWeights.get(node2);
		node_info nodeInfo1 = nodes.get(node1);
		node_info nodeInfo2 = nodes.get(node2);
		
		// If node1 and node2 have neighbors
		if (neighborWeights1 != null && neighborWeights2 != null) {
			// If and only if node1 is neighbor of node2 
			if (neighborWeights1.get(nodeInfo2) != null && neighborWeights2.get(nodeInfo1) != null) {
				// Remove the edge between node1 and node2 from the hash maps
				neighborWeights1.remove(nodeInfo2);
				neighborWeights2.remove(nodeInfo1);
				edgeSize--;
				modeCounter++;
			}
		}
	}

	/** 
	 * Returns the number of vertices (nodes) in the graph.
	 * @return int node size
	 */
	@Override
	public int nodeSize() {
		return nodes.size();
	}

	/**
	 * Returns the number of edges (undirected graph).
	 * @return int edge size
	 */
	@Override
	public int edgeSize() {
		return edgeSize;
	}

	/**
	 * Returns the Mode Count - for testing changes in the graph.
	 * Any change in the inner state of the graph causes an increment in the ModeCount.
	 * @return int mode count
	 */
	@Override
	public int getMC() {
		return modeCounter;
	}

	@Override
	public String toString() {
		return "nodesSize=" + nodes.size() //+ ", neighborWeights=" + neighborWeights
				+ ", edgeSize=" + edgeSize + ", modeCounter=" + modeCounter;
	}

	/**
	 * This class implements node_info interface
	 * that represents the node information.
	 */
	private class NodeInfo implements node_info, Serializable {

		private static final long serialVersionUID = 1L;

		private int key = 0;
		private String info;
		private double tag = 0;

		/**
		 * Constructor that copies the key.
		 * @param int key
		 */
		private NodeInfo(int key) {
			this.key = key;
		}

		/**
		 * Shallow copy constructor that copies key, info and tag.
		 * @param node_info node
		 */
		private NodeInfo(node_info node) {
			key = node.getKey();
			info = node.getInfo();
			tag = node.getTag();
		}

		/**
		 * Returns the key (id) associated with this node.
		 * Each node_info has a unique key.
		 * @return int key
		 */
		@Override
		public int getKey() {
			return key;
		}

		/**
		 * Returns the remark (meta data) associated with this node.
		 * @return String info
		 */
		@Override
		public String getInfo() {
			return info;
		}

		/**
		 * Allows changing the remark (meta data) associated with this node.
		 * @param String s
		 */
		@Override
		public void setInfo(String s) {
			info = s;
		}

		/**
		 * Temporal data (aka distance, color, or state)
		 * which can be used by algorithms.
		 * @return double tag
		 */
		@Override
		public double getTag() {
			return tag;
		}

		/**
		 * Allows setting the "tag" value for temporal marking an node.
		 * @param double t - the new value of the tag
		 */
		@Override
		public void setTag(double t) {
			tag = t;
		}

		@Override
		public String toString() {
			return "key=" + key;
		}
	}
}