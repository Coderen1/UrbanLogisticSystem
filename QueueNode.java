/**
 * The QueueNode class represents one node in the PackageQueue.
 *
 * Each node stores one Package object and a reference to the next node.
 * This manual node class allows the queue to work without using Java's
 * built-in Queue or collection classes.
 */
public class QueueNode {

    /** Package data stored inside this queue node. */
    Package data;

    /** Reference to the next node in the queue. */
    QueueNode next;

    /**
     * Creates a new queue node that stores a package.
     *
     * @param data package record to store in this node
     */
    public QueueNode(Package data) {
        this.data = data;
        this.next = null;
    }
}
