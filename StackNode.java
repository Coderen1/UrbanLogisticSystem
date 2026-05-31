/**
 * The StackNode class represents one node in the PackageStack.
 *
 * Each node stores one Package object and a reference to the node below
 * it. This manual node class allows the stack to work without using
 * Java's built-in Stack or collection classes.
 */
public class StackNode {

    /** Package data stored inside this stack node. */
    Package data;

    /** Reference to the next node below this node in the stack. */
    StackNode next;

    /**
     * Creates a new stack node that stores a package.
     *
     * @param data package record to store in this node
     */
    public StackNode(Package data) {
        this.data = data;
        this.next = null;
    }
}
