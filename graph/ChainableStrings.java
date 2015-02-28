import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * http://www.geeksforgeeks.org/given-array-strings-find-strings-can-chained-
 * form-circle/
 * 
 * Given an array of strings, find if the given strings can be chained to form a
 * circle. A string X can be put before another string Y in circle if the last
 * character of X is same as first character of Y.
 * 
 * The algorithm:
 * 
 * 1) Create a directed graph g with number of nodes equal to the number of
 * unique characters in the given array of strings.
 * 
 * 2) Do following for every string in the given array of strings: Add an edge
 * from first character to last character of the given graph.
 * 
 * 3) If the created graph has Eulerian Circuit, then return true, else return
 * false.
 * 
 * @author osonmez
 * 
 */
public class ChainableStrings {

	public static void main(String[] args) {
		// arr[] = {"geek", "king"}
		// arr[] = {"aaa", "bbb", "baa", "aab"};
		String arr[] = { "for", "geek", "rig", "kaf" };
		System.out.println(isChainable(arr));
	}

	public static boolean isChainable(String[] arr) {
		int idCounter = 0;

		// assign an id for each unique character
		Map<Character, Integer> charMap = new HashMap<>();
		for (String string : arr) {
			char first = string.charAt(0);
			char last = string.charAt(string.length() - 1);

			if (!charMap.containsKey(first)) {
				charMap.put(first, idCounter++);
			}

			if (!charMap.containsKey(last)) {
				charMap.put(last, idCounter++);
			}
		}

		Graph graph = new Graph(charMap.size());
		char first, last;
		for (String string : arr) {
			string = string.toLowerCase();
			first = string.charAt(0);
			last = string.charAt(string.length() - 1);
			graph.addEdge(charMap.get(first), charMap.get(last));
		}

		return graph.isEulerianCircuit();
	}
}

class Graph {

	private int numOfNodes;
	private Map<Integer, List<Integer>> adjacencies;

	public Graph(int numOfNodes) {
		if (numOfNodes < 0) {
			throw new IllegalArgumentException("invalid input");
		}

		this.numOfNodes = numOfNodes;

		adjacencies = new HashMap<>();
		for (int i = 0; i < numOfNodes; i++)
			adjacencies.put(i, new ArrayList<Integer>());
	}

	public void addEdge(int fromNode, int toNode) {
		adjacencies.get(fromNode).add(toNode);
	}

	/**
	 * A directed graph has Eulerian Circuit only if in degree and out degree of
	 * every vertex is same, and all non-zero degree vertices form a single
	 * strongly connected component.
	 */
	public boolean isEulerianCircuit() {
		if (!isDirectedGraphConnected())
			return false;

		// Check if in degree and out degree of every vertex is same
		int inDegrees[] = new int[numOfNodes];
		for (Integer fromNode : adjacencies.keySet()) {
			for (Integer toNode : adjacencies.get(fromNode)) {
				inDegrees[toNode]++;
			}
		}

		for (int i = 0; i < inDegrees.length; i++)
			if (adjacencies.get(i).size() != inDegrees[i])
				return false;

		return true;
	}

	/**
	 * http://www.geeksforgeeks.org/connectivity-in-a-directed-graph/
	 * 
	 * Following is Kosaraju’s DFS based simple algorithm that does two DFS
	 * traversals of graph: 1) Initialize all vertices (nodes) as not visited.
	 * 2) Do a DFS traversal of graph starting from any arbitrary vertex v. If
	 * DFS traversal doesn’t visit all vertices, then return false. 3) Reverse
	 * all arcs (or find transpose or reverse of graph) 4) Mark all vertices as
	 * not-visited in reversed graph. 5) Do a DFS traversal of reversed graph
	 * starting from same vertex v (Same as step 2). If DFS traversal doesn’t
	 * visit all vertices, then return false. Otherwise return true.
	 */
	public boolean isDirectedGraphConnected() {
		if (!isConnected())
			return false;

		Graph g = transpose();
		return g.isConnected();
	}

	public boolean isConnected() {
		boolean[] isVisited = new boolean[numOfNodes];

		// Find the first node with non-zero degree
		int root = 0;
		for (int i = 0; i < numOfNodes; i++) {
			if (adjacencies.get(i).size() > 0) {
				root = i;
				break;
			}
		}

		runDFS(root, isVisited);

		for (int i = 0; i < isVisited.length; i++) {
			if (!isVisited[i])
				return false;
		}
		return true;
	}

	public void runDFS() {
		boolean[] isVisited = new boolean[numOfNodes];
		runDFS(0, isVisited);
	}

	private void runDFS(int root, boolean[] isVisited) {
		isVisited[root] = true;

		for (int i = 0; i < adjacencies.get(root).size(); i++) {
			int nodeIndex = adjacencies.get(root).get(i);
			if (!isVisited[nodeIndex]) {
				runDFS(nodeIndex, isVisited);
			}
		}
	}

	public Graph transpose() {
		Graph graph = new Graph(numOfNodes);
		for (Integer fromNode : adjacencies.keySet()) {
			for (Integer toNode : adjacencies.get(fromNode)) {
				graph.addEdge(toNode, fromNode);
			}
		}

		return graph;
	}
}
