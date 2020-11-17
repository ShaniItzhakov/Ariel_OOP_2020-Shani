package ex1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WGraph_AlgoTest {

	private WGraph_DS graph = new WGraph_DS();
	private WGraph_Algo algoGraph;

	@BeforeEach
	void initGraph() {
		algoGraph = createGraph(false);
	}

	@Test
	void testDefaultConstructor() {
		WGraph_Algo defaultAlgoGraph = new WGraph_Algo();
		assertNull(defaultAlgoGraph.getGraph());
	}

	@Test
	void testShallowCopyConstructor() {
		assertEquals(graph, algoGraph.getGraph());
	}

	@Test
	void testInit() {
		WGraph_Algo defaultAlgoGraph = new WGraph_Algo();
		defaultAlgoGraph.init(graph);
		assertEquals(graph, defaultAlgoGraph.getGraph());
	}

	@Test
	void testGetGraph() {
		assertEquals(graph, algoGraph.getGraph());
	}

	@Test
	void testCopy() {
		weighted_graph graph1 = algoGraph.copy();
		assertEqualGraphs(graph, graph1);
		assertEqualGraphs(graph1, graph);
		
		WGraph_Algo emptyAlgoGraph = new WGraph_Algo(null);
		graph1 = emptyAlgoGraph.copy();
		assertNull(graph1);
	}

	@Test
	void testIsConnected() {
		assertFalse(algoGraph.isConnected());

		algoGraph = createGraph(true);
		assertTrue(algoGraph.isConnected());

		WGraph_DS emptyGraph = new WGraph_DS();
		WGraph_Algo emptyAlgoGraph = new WGraph_Algo(emptyGraph);
		assertTrue(emptyAlgoGraph.isConnected());

		emptyAlgoGraph = new WGraph_Algo(null);
		assertTrue(emptyAlgoGraph.isConnected());
	}

	@Test
	void testShortestPathDist() {

		assertEquals(9.8, algoGraph.shortestPathDist(2, 9));
		assertEquals(9.8, algoGraph.shortestPathDist(9, 2));
		assertEquals(-1.0, algoGraph.shortestPathDist(2, 15));
		assertEquals(-1.0, algoGraph.shortestPathDist(15, 2));
		assertEquals(0.0, algoGraph.shortestPathDist(2, 2));

		WGraph_Algo emptyAlgoGraph = new WGraph_Algo(null);
		assertEquals(-1.0, emptyAlgoGraph.shortestPathDist(0, 1));
	}

	@Test
	void testShortestPath() {
		List<node_info> expectedNodes = new ArrayList<>();
		expectedNodes.add(graph.getNode(2));
		expectedNodes.add(graph.getNode(5));
		expectedNodes.add(graph.getNode(1));
		expectedNodes.add(graph.getNode(9));

		assertEquals(expectedNodes, algoGraph.shortestPath(2, 9));
		assertEquals(null, algoGraph.shortestPath(2, 10));
		assertEquals(null, algoGraph.shortestPath(2, 15));
		assertEquals(null, algoGraph.shortestPath(15, 2));
		assertEquals(Arrays.asList(graph.getNode(2)), algoGraph.shortestPath(2, 2));

		WGraph_Algo emptyAlgoGraph = new WGraph_Algo(null);
		assertEquals(null, emptyAlgoGraph.shortestPath(0, 1));
	}

	@Test
	void testSaveAndLoad() {
		
		assertTrue(algoGraph.save("fileName"));
		assertTrue(algoGraph.load("fileName"));
		assertEqualGraphs(graph, algoGraph.getGraph());

		assertFalse(algoGraph.save(null));
		assertFalse(algoGraph.load(null));

		assertFalse(algoGraph.save("|fileName"));
		assertFalse(algoGraph.load("|fileName"));
	}

	private void assertEqualGraphs(weighted_graph g, weighted_graph g1) {
		for (node_info node: g.getV()) {
			node_info node1 = g1.getNode(node.getKey());
			assertNotSame(node, node1);
			assertEquals(node.getKey(), node1.getKey());
			assertEquals(node.getTag(), node1.getTag());
			assertEquals(node.getInfo(), node1.getInfo());
			if (g.getV(node.getKey()) == null) {
				continue;
			}
			for (node_info neighbor: g.getV(node.getKey())) {
				node_info neighbor1 = g1.getNode(neighbor.getKey());
				double weight = g.getEdge(node.getKey(), neighbor.getKey());
				double weight1 = g1.getEdge(node1.getKey(), neighbor1.getKey());
				assertEquals(weight, weight1);
			}
		}
		assertEquals(g.nodeSize(), g1.nodeSize());
		assertEquals(g.edgeSize(), g1.edgeSize());
		assertEquals(g.getMC(), g1.getMC());
		assertNotSame(g, g1);
	}

	private WGraph_Algo createGraph(boolean isConnected) {
		graph = new WGraph_DS();

		for (int i = 0; i <= 10; i++) {
			graph.addNode(i);
		}
		graph.connect(2, 5, 1);
		graph.connect(1, 9, 8.3);
		graph.connect(3, 5, 2);
		graph.connect(1, 5, 0.5);
		graph.connect(8, 4, 3);
		graph.connect(0, 6, 4.1);
		graph.connect(7, 4, 9);
		if (isConnected) {
			graph.connect(9, 10, 7.3);
		}	
		return new WGraph_Algo(graph);
	}
}