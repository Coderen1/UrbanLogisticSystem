/**
 * The DLLNode class represents one node in a Doubly Linked List.
 *
 * Each node stores one Package object, a reference to the next node,
 * and a reference to the previous node. These two references allow the
 * list to maintain connections in both directions.
 */
public class DLLNode {

    /** Package data stored inside this node. */
    Package data;

    /** Reference to the next node in the Doubly Linked List. */
    DLLNode next;

    /** Reference to the previous node in the Doubly Linked List. */
    DLLNode prev;

    /**
     * Creates a new Doubly Linked List node that stores a package.
     *
     * @param data package record to store in this node
     */
    public DLLNode(Package data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
