/**
 * The SLLNode class represents one node in a Singly Linked List.
 *
 * A node stores one Package object and a reference to the next node.
 * This class is implemented manually and does not use any Java
 * Collections Framework classes.
 */
public class SLLNode {

    /** Package data stored inside this node. */
    Package data;

    /** Reference to the next node in the list. */
    SLLNode next;

    /**
     * Creates a new node that stores a Package object.
     *
     * @param data package record to store in this node
     */
    public SLLNode(Package data) {
        this.data = data;
        this.next = null;
    }
}
