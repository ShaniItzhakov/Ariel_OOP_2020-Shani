package ex1;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WGraph_DSTest {

	private WGraph_DS graph = new WGraph_DS();

	@BeforeEach
	void initGraph() {
		createGraph();
	}

	@Test
	void testDefaultConstructor() {
		WGraph_DS defaultGraph = new WGraph_DS();
		assertEquals(0, defaultGraph.edgeSize());
		assertEquals(0, defaultGraph.getMC());
	}

	@Test
	void testDeepCopyConstructor() {
		WGraph_DS g1 = new WGraph_DS(graph);
		assertEqualGraphs(graph, g1);
		assertEqualGraphs(g1, graph);
	}

	@Test
	void testDeepCopyConstructorNullGraph() {
		WGraph_DS nullGraph = new WGraph_DS(null);
		assertEquals(0, nullGraph.edgeSize());
		assertEquals(0, nullGraph.getMC());
	}

	@Test
	void testGetNode() {
		assertEquals(7, graph.getNode(7).getKey());
	}

	@Test
	void testHasEdge() {
		assertTrue(graph.hasEdge(0, 6));
		assertFalse(graph.hasEdge(1, 7));
		assertFalse(graph.hasEdge(2, 11));
		assertFalse(graph.hasEdge(3, 3));
		assertFalse(graph.hasEdge(10, 0));
		assertFalse(graph.hasEdge(0, 10));
	}

	@Test
	void testGetEdge() {
		assertEquals(8.3, graph.getEdge(1, 9));
		assertEquals(-1, graph.getEdge(1, 6));
		assertEquals(-1, graph.getEdge(8, 13));
	}

	@Test
	void testAddNode() {
		int mc = graph.getMC();
		int nodeSize = graph.nodeSize();
		graph.addNode(15);
		graph.addNode(15);

		assertEquals(15, graph.getNode(15).getKey());
		assertEquals(mc+1, graph.getMC());
		assertEquals(nodeSize+1, graph.nodeSize());
	}

	@Test
	void testConnect() {
		int mc = graph.getMC();
		int edgeSize = graph.edgeSize();
		int nodeSize = graph.nodeSize();

		graph.connect(0, 7, 5.5);
		assertEquals(5.5, graph.getEdge(0, 7));

		graph.connect(9, 9, 3.2);
		assertEquals(-1, graph.getEdge(9, 9));

		graph.connect(12, 5, 10);
		assertEquals(-1, graph.getEdge(12, 5));

		graph.connect(1, 14, 7.5);
		assertEquals(-1, graph.getEdge(1, 14));

		graph.connect(0, 7, -1);
		assertEquals(5.5, graph.getEdge(0, 7));

		graph.connect(0, 7, 3);
		assertEquals(3, graph.getEdge(0, 7));

		graph.connect(3, 1, 9.2);
		assertEquals(9.2, graph.getEdge(3, 1));

		assertEquals(mc+3, graph.getMC());
		assertEquals(edgeSize+2, graph.edgeSize());
		assertEquals(nodeSize, graph.nodeSize());
	}
	
	@Test
	void testGetV() {
		Collection<node_info> nodes = graph.getV();
		int[] keysArr = new int[nodes.size()];
		int[] expectedKeys = {0,1,2,3,4,5,6,7,8,9,10};
		int i = 0;
		for (node_info node: nodes) {
			keysArr[i++] = node.getKey();
		}
		Arrays.sort(keysArr);
		assertArrayEquals(expectedKeys, keysArr);
	}

	@Test
	void testGetVByNodeId() {
		Collection<node_info> neighbors = graph.getV(5);
		int[] keysArr = new int[neighbors.size()];
		int[] expectedKeys = {1, 2, 3};
		int i = 0;
		for (node_info neighbor: neighbors) {
			keysArr[i++] = neighbor.getKey();
		}
		Arrays.sort(keysArr);
		assertArrayEquals(expectedKeys, keysArr);
		assertNull(graph.getV(10));
	}

	@Test
	void testRemoveNode() {
		int mc = graph.getMC();
		int edgeSize = graph.edgeSize();
		int nodeSize = graph.nodeSize();

		graph.removeNode(1);
		graph.removeNode(1);
		assertNull(graph.getNode(1));
		for (node_info node: graph.getV()) {
			Collection<node_info> neighbors = graph.getV(node.getKey());
			if (neighbors == null) {
				continue;
			}
			for (node_info neighbor: neighbors) {
				assertNotEquals(1, neighbor.getKey());
			}
		}
		assertEquals(mc+3, graph.getMC());
		assertEquals(edgeSize-2, graph.edgeSize());
		assertEquals(nodeSize-1, graph.nodeSize());
	}

	@Test
	void testRemoveEdge() {
		int mc = graph.getMC();
		int edgeSize = graph.edgeSize();
		int nodeSize = graph.nodeSize();

		assertTrue(graph.hasEdge(1, 9));
		graph.removeEdge(1, 9);
		assertNotNull(graph.getNode(1));
		assertFalse(graph.hasEdge(1, 9));

		graph.removeEdge(1, 9);
		graph.removeEdge(12, 9);
		graph.removeEdge(9, 12);

		assertEquals(mc+1, graph.getMC());
		assertEquals(edgeSize-1, graph.edgeSize());
		assertEquals(nodeSize, graph.nodeSize());
	}

	@Test
	void testNodeInfo() {
		node_info node = graph.getNode(1);
		node.setTag(5.98);
		node.setInfo("test info");

		assertEquals(5.98, node.getTag());
		assertEquals("test info", node.getInfo());
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

	private void createGraph() {
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
	}
}