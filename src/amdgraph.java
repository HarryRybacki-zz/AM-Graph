/**
 * @author Sphere
 * CSC330 - Fall2012
 * 
 * Create a simple adjacency matrix based directed graph.
 * 
 * (1)Insert the following edges:
 * D->C:10, A->B:12, D->B:23, A->D:87, E->D:43, B->E:11, C->A:19
 * 
 * (2)Change the weight of A->B from 12 to 13 and
 *    remove edge A->D:87
 *    
 * (3)Print the resulting graph utilizing BFS
 */

import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Queue;

public class amdgraph {
	
	// simple object representing a Vertex	
	public static class Vertex {
		public String name;		// vertex name
		public int vertNumber;	// number of vertex for matrix referencing
		public int dist;		// cost
		public Vertex prev;		// Previous vertex on sp
		public int scratch;		// used in algorithms
			
		// default Vertex constructor
		public Vertex( String n) {
			name = n;
			reset();
		}
			
		public void reset() {
			dist = AdjMatrix.INFINITY;
			prev = null;
			scratch = 0;
		}
	}
	
	// simple object representing an adjacency matrix
	public static class AdjMatrix {
		// fields
		protected int[][] matrix;
		protected final static int INFINITY = Integer.MAX_VALUE;
	
	// constructor for blank graph of n vertices
	public AdjMatrix(int n) {
		matrix = new int[n][n];
		// iterate through matrix and initialize all points
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				matrix[i][j] = -1;
		}
	}
	
	/**
	 * class representing an adjacency matrix based directed graph
	 */
	public static class Graph extends AdjMatrix {
		// fields
		private int numVertices;
		private int numEdges;
		private Map<String, Vertex> verts = new HashMap();
		private Map<Integer, Vertex> numbers = new HashMap();
		
		// default constructor
		public Graph(int n) {
			super(n);						// create adjacency matrix to store vertices/edges						
			numVertices = 0; numEdges = 0;	// set vertices/edges to 0
		}
		
		/**
		 * adds new edge to adjacency matrix
		 * 
		 * @param Source Vertex
		 * @param Destination Vertex
		 * @param Weight of the edge
		 */
		public void addEdge(String source, String dest, int weight) {
		    // get source/destination vertexes
			Vertex s = getVert(source);
		    Vertex d = getVert(dest);
			// add edge
			this.matrix[verts.get(source).vertNumber][verts.get(dest).vertNumber] = weight;
			//testing
			System.out.println(source + "->" + dest + ":" + this.matrix[verts.get(source).vertNumber][verts.get(dest).vertNumber]);
		}
		
		/**
		 * removes an edge from adjacency matrix
		 * 
		 * @param Source Vertex
		 * @param Destination Vertex
		 * @param Supposed weight of edge
		 */
		private void removeEdge(String source, String dest, int weight) {
			// get source/destination vertexes
			Vertex s = getVert(source);
			Vertex d = getVert(dest);
			
			//testing
			System.out.println("Deleting: " + source + "->" + dest + ":" + this.matrix[verts.get(source).vertNumber][verts.get(dest).vertNumber]);
			
			// check if edge exists
			if ((this.matrix[verts.get(source).vertNumber][verts.get(dest).vertNumber]) != weight)
				throw new NoSuchElementException("This edge does not exist");
			// remove edge
			else
				this.matrix[verts.get(source).vertNumber][verts.get(dest).vertNumber] = -1;
		}
		
		/**
		 * changes the weight of an edge in the adjacency matrix
		 * 
		 * @param Source Vertex
		 * @param Destination Vertex
		 * @param New weight of edge
		 */
		private void changeWeight(String source, String dest, int weight) {
			// get source/destination vertexes
			Vertex s = getVert(source);
			Vertex d = getVert(dest);
			
			//testing
			System.out.print("Changing: " + source + "->" + dest + ":" + 
					this.matrix[verts.get(source).vertNumber][verts.get(dest).vertNumber]);
			
			// modify weight if edge exists
			if ((this.matrix[verts.get(source).vertNumber][verts.get(dest).vertNumber]) > 0)
				this.matrix[verts.get(source).vertNumber][verts.get(dest).vertNumber] = weight;
				
			else								// else throw exception
				throw new NoSuchElementException("This edge does not exist.");
			
			//testing -- verify change
			System.out.println(" To: " + source + "->" + dest + ":" + 
					this.matrix[verts.get(source).vertNumber][verts.get(dest).vertNumber]);
		}
		
		/**
		 * return vertex associated with name or 
		 * create new vertex if none is found
		 * 
		 * @param Name of Vertex
		 * @return Vertex associated with name
		 */
		private Vertex getVert(String vertName) {
			Vertex v = verts.get(vertName);
			
			if (v == null) {					// if vertex does not exist, add it to the map
				v = new Vertex(vertName);		
				v.vertNumber = numVertices;		// determine row number of Vertex
				numbers.put(numVertices, v);	// add to "number key" map
				verts.put(vertName, v);			// add to "name key" map
				numVertices++;					// increment the number of vertices
				
			}
			return v;
		}

		/**
		 * breadth first search -- ONLY prints edges in this version
		 * 
		 * @param Starting vertex
		 */
		public void bfs(String start) {
			clearAll();											// reset scratches for all Vertices
			Vertex root = verts.get(start);						// get root Vertex
			root.dist = 0;										// set distance to zero
			
			if (root == null)									// throw exception if root Vertex DNE
				throw new NoSuchElementException("Start Vertex not found");
			
			Queue<Vertex> queue = new LinkedList<Vertex>();		// create queue
			queue.add(root);									// add root Vertex to queue
			
			while (!queue.isEmpty()) {							// continue while not empty
				Vertex tempVertex = queue.poll();				// grab next Vertex from queue
				tempVertex.scratch = 2;							// mark vertex as visited
				
				for (int i = 0; i < numVertices; i++) {			// search for nodes in tempVetex's row
					// if an edge exists and dest Vertex is unseen add to queue
					if ((matrix[tempVertex.vertNumber][i] > 0) && numbers.get(i).scratch == 0) {
						Vertex neighbor = numbers.get(i);
						neighbor.dist = tempVertex.dist + 1;	// set distance
						neighbor.prev = tempVertex;				// set previous Vertex
						neighbor.scratch = 1;					// mark as seen
						queue.add(neighbor);					// add to queue
						// print edge
						System.out.println(neighbor.prev.name + "->" + neighbor.name + ":"
								+ matrix[neighbor.prev.vertNumber][neighbor.vertNumber]);		
					}
				}
			}
		}
		
		/**
		 * reset all vertices values for search algorithm
		 */
		private void clearAll() {
			for (Vertex v : verts.values())
				v.reset();
		}
		
		public void printMatrix() {
			for (int i = 0; i < numVertices; i++) {
				
				for (int j = 0; j < numVertices; j++) {
					System.out.print(matrix[i][j] + " " );
				}
				System.out.println();
			}
		}
		
	}
		
	// driver
	public static void main(String[] args) {
		// create empty graph with 5 vertices
		Graph myGraph = new Graph(5);
		
		//myGraph.printMatrix();
		
		// addEdge: D->C:10, A->B:12, D->B:23, A->D:87, E->D:43, B->E:11, C->A:19 
		myGraph.addEdge("D", "C", 10);
		myGraph.addEdge("A", "B", 12);
		myGraph.addEdge("D", "B", 23);
		myGraph.addEdge("A", "D", 87);
		myGraph.addEdge("E", "D", 43);
		myGraph.addEdge("B", "E", 11);
		myGraph.addEdge("C", "A", 19);
		
		myGraph.removeEdge("A", "D", 87);
		
		myGraph.changeWeight("A", "B", 13);
		
		//myGraph.printMatrix();
		
		myGraph.bfs("D");
		
		System.out.println("Finished");
		
	}

}
