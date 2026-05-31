/**
 * The Edge class represents one weighted road between two neighborhoods.
 *
 * In the graph system, neighborhoods are vertices and roads are edges.
 * The weight stores the road distance in kilometers.
 */
public class Edge {

    /** Starting neighborhood of the road connection. */
    String source;

    /** Ending neighborhood of the road connection. */
    String destination;

    /** Road distance between source and destination. */
    int weight;

    /**
     * Creates a new weighted edge.
     *
     * @param source      starting neighborhood
     * @param destination ending neighborhood
     * @param weight      road distance in kilometers
     */
    public Edge(String source, String destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    /**
     * Displays this road connection in a readable format.
     */
    public void displayEdge() {
        System.out.println(source + " <-> " + destination + " = " + weight + " KM");
    }
}
