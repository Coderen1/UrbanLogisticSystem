/**
 * The Graph class represents the Kayseri city road network.
 *
 * This graph is weighted and undirected:
 * - Weighted means every road has a distance value.
 * - Undirected means travel is possible in both directions on each road.
 *
 * The implementation uses an adjacency matrix because it is beginner-friendly
 * and uses simple arrays instead of Java Collections Framework classes.
 */
public class Graph {

    /** Large value used to represent infinity in shortest path calculations. */
    private static final int INF = 1000000;

    /** Maximum number of neighborhoods that can be stored in this graph. */
    private int maxVertices;

    /** Current number of neighborhoods added to the graph. */
    private int vertexCount;

    /** Array that stores neighborhood names. */
    private String[] vertices;

    /** Adjacency matrix that stores road distances between neighborhoods. */
    private int[][] adjacencyMatrix;

    /**
     * Creates a graph with a fixed maximum number of vertices.
     *
     * @param maxVertices maximum number of neighborhoods in the graph
     */
    public Graph(int maxVertices) {
        this.maxVertices = maxVertices;
        this.vertexCount = 0;
        this.vertices = new String[maxVertices];
        this.adjacencyMatrix = new int[maxVertices][maxVertices];

        // A value of 0 means no road exists between two different vertices.
        for (int i = 0; i < maxVertices; i++) {
            for (int j = 0; j < maxVertices; j++) {
                adjacencyMatrix[i][j] = 0;
            }
        }
    }

    /**
     * Adds an undirected weighted road between two neighborhoods.
     *
     * If a neighborhood does not already exist in the graph, it is added
     * automatically before the road connection is stored.
     *
     * @param source      first neighborhood
     * @param destination second neighborhood
     * @param weight      road distance in kilometers
     */
    public void addEdge(String source, String destination, int weight) {
        int sourceIndex = getOrAddVertex(source);
        int destinationIndex = getOrAddVertex(destination);

        if (sourceIndex == -1 || destinationIndex == -1) {
            System.out.println("Road could not be added because the graph is full.");
            return;
        }

        // Because the graph is undirected, store the same distance both ways.
        adjacencyMatrix[sourceIndex][destinationIndex] = weight;
        adjacencyMatrix[destinationIndex][sourceIndex] = weight;
    }

    /**
     * Displays all road connections in the city road network.
     *
     * The loop prints only one direction of each undirected edge to avoid
     * showing duplicate road records.
     */
    public void displayGraph() {
        if (vertexCount == 0) {
            System.out.println("The city road network is currently empty.");
            return;
        }

        for (int i = 0; i < vertexCount; i++) {
            for (int j = i + 1; j < vertexCount; j++) {
                if (adjacencyMatrix[i][j] > 0) {
                    Edge edge = new Edge(vertices[i], vertices[j], adjacencyMatrix[i][j]);
                    edge.displayEdge();
                }
            }
        }
    }

    /**
     * Calculates and displays the shortest path between two neighborhoods
     * using Dijkstra's Algorithm.
     *
     * @param start starting neighborhood
     * @param end   destination neighborhood
     */
    public void calculateShortestPath(String start, String end) {
        int startIndex = findVertexIndex(start);
        int endIndex = findVertexIndex(end);

        if (startIndex == -1 || endIndex == -1) {
            System.out.println("Shortest path cannot be calculated because a neighborhood was not found.");
            return;
        }

        int[] distances = new int[maxVertices];
        int[] previous = new int[maxVertices];
        boolean[] visited = new boolean[maxVertices];

        // Initialize distances as infinity and previous vertices as unknown.
        for (int i = 0; i < vertexCount; i++) {
            distances[i] = INF;
            previous[i] = -1;
            visited[i] = false;
        }

        distances[startIndex] = 0;

        // Dijkstra repeats until all reachable vertices have been processed.
        for (int count = 0; count < vertexCount; count++) {
            int currentIndex = findUnvisitedVertexWithSmallestDistance(distances, visited);

            if (currentIndex == -1) {
                break;
            }

            visited[currentIndex] = true;

            // Check all possible neighbors of the current vertex.
            for (int neighbor = 0; neighbor < vertexCount; neighbor++) {
                boolean hasRoad = adjacencyMatrix[currentIndex][neighbor] > 0;

                if (hasRoad && !visited[neighbor]) {
                    int newDistance = distances[currentIndex] + adjacencyMatrix[currentIndex][neighbor];

                    if (newDistance < distances[neighbor]) {
                        distances[neighbor] = newDistance;
                        previous[neighbor] = currentIndex;
                    }
                }
            }
        }

        System.out.println("Start       : " + start);
        System.out.println("Destination : " + end);

        if (distances[endIndex] == INF) {
            System.out.println("No route exists between these neighborhoods.");
            return;
        }

        System.out.println("Shortest Distance : " + distances[endIndex] + " KM");
        System.out.print("Route : ");
        printPath(previous, endIndex);
        System.out.println();

        System.out.print("Visited Vertices : ");
        printVisitedVertices(visited);
        System.out.println();
    }

    /**
     * Calculates and displays a Minimum Spanning Tree using Prim's Algorithm.
     *
     * A Minimum Spanning Tree connects all neighborhoods with the smallest
     * possible total road distance without creating cycles.
     */
    public void calculateMST() {
        if (vertexCount == 0) {
            System.out.println("MST cannot be calculated because the graph is empty.");
            return;
        }

        int[] parent = new int[maxVertices];
        int[] key = new int[maxVertices];
        boolean[] inMST = new boolean[maxVertices];

        // Initialize all keys as infinity and parents as unknown.
        for (int i = 0; i < vertexCount; i++) {
            key[i] = INF;
            parent[i] = -1;
            inMST[i] = false;
        }

        // Start Prim's Algorithm from the first inserted vertex.
        key[0] = 0;

        for (int count = 0; count < vertexCount; count++) {
            int currentIndex = findVertexWithSmallestKey(key, inMST);

            if (currentIndex == -1) {
                break;
            }

            inMST[currentIndex] = true;

            // Update neighboring vertices if this road is a better MST choice.
            for (int neighbor = 0; neighbor < vertexCount; neighbor++) {
                int roadWeight = adjacencyMatrix[currentIndex][neighbor];

                if (roadWeight > 0 && !inMST[neighbor] && roadWeight < key[neighbor]) {
                    key[neighbor] = roadWeight;
                    parent[neighbor] = currentIndex;
                }
            }
        }

        int totalWeight = 0;

        for (int i = 1; i < vertexCount; i++) {
            if (parent[i] == -1) {
                System.out.println("MST cannot include all neighborhoods because the graph is disconnected.");
                return;
            }

            System.out.println("Selected Edge:");
            Edge edge = new Edge(vertices[parent[i]], vertices[i], adjacencyMatrix[i][parent[i]]);
            edge.displayEdge();
            totalWeight = totalWeight + adjacencyMatrix[i][parent[i]];
            System.out.println();
        }

        System.out.println("Total MST Weight: " + totalWeight + " KM");
    }

    /**
     * Finds the index of a neighborhood. If it does not exist, this method
     * adds it to the graph.
     *
     * @param neighborhood neighborhood name to find or add
     * @return index of the neighborhood, or -1 if the graph is full
     */
    private int getOrAddVertex(String neighborhood) {
        int existingIndex = findVertexIndex(neighborhood);

        if (existingIndex != -1) {
            return existingIndex;
        }

        if (vertexCount == maxVertices) {
            return -1;
        }

        vertices[vertexCount] = neighborhood;
        vertexCount++;

        return vertexCount - 1;
    }

    /**
     * Finds the index of a neighborhood using manual linear search.
     *
     * @param neighborhood neighborhood name to find
     * @return index of the neighborhood, or -1 if it does not exist
     */
    private int findVertexIndex(String neighborhood) {
        for (int i = 0; i < vertexCount; i++) {
            if (vertices[i].equalsIgnoreCase(neighborhood)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Finds the unvisited vertex with the smallest distance value.
     *
     * @param distances current shortest known distances
     * @param visited   visited status of each vertex
     * @return index of the best unvisited vertex, or -1 if none exists
     */
    private int findUnvisitedVertexWithSmallestDistance(int[] distances, boolean[] visited) {
        int minimumDistance = INF;
        int minimumIndex = -1;

        for (int i = 0; i < vertexCount; i++) {
            if (!visited[i] && distances[i] < minimumDistance) {
                minimumDistance = distances[i];
                minimumIndex = i;
            }
        }

        return minimumIndex;
    }

    /**
     * Finds the vertex not yet in the MST with the smallest key value.
     *
     * @param key   best known connection cost for each vertex
     * @param inMST true if a vertex is already included in the MST
     * @return index of the best vertex to add next, or -1 if none exists
     */
    private int findVertexWithSmallestKey(int[] key, boolean[] inMST) {
        int minimumKey = INF;
        int minimumIndex = -1;

        for (int i = 0; i < vertexCount; i++) {
            if (!inMST[i] && key[i] < minimumKey) {
                minimumKey = key[i];
                minimumIndex = i;
            }
        }

        return minimumIndex;
    }

    /**
     * Recursively prints the shortest path from the start vertex to the
     * current vertex using the previous array created by Dijkstra.
     *
     * @param previous     previous vertex index for each vertex
     * @param currentIndex current vertex index being printed
     */
    private void printPath(int[] previous, int currentIndex) {
        if (currentIndex == -1) {
            return;
        }

        printPath(previous, previous[currentIndex]);

        if (previous[currentIndex] != -1) {
            System.out.print(" -> ");
        }

        System.out.print(vertices[currentIndex]);
    }

    /**
     * Prints the vertices that were visited by Dijkstra's Algorithm.
     *
     * @param visited visited status of each vertex
     */
    private void printVisitedVertices(boolean[] visited) {
        boolean firstPrinted = true;

        for (int i = 0; i < vertexCount; i++) {
            if (visited[i]) {
                if (!firstPrinted) {
                    System.out.print(", ");
                }

                System.out.print(vertices[i]);
                firstPrinted = false;
            }
        }
    }
}
