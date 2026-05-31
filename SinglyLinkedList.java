/**
 * The SinglyLinkedList class is a manual implementation of a Singly
 * Linked List for the FastRoute Kayseri Master Registry.
 *
 * Purpose:
 * This list stores the daily record of every package that enters the
 * warehouse. Each package is added to the end of the list so the log
 * keeps the same order as the arrival order.
 */
public class SinglyLinkedList {

    /** Head pointer stores the first node of the list. */
    private SLLNode head;

    /**
     * Creates an empty Singly Linked List.
     */
    public SinglyLinkedList() {
        head = null;
    }

    /**
     * Checks whether the list contains no package records.
     *
     * @return true if the list is empty, otherwise false
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Appends a package record to the end of the Master Registry log.
     *
     * This method uses manual traversal. If the list is empty, the new
     * node becomes the head. Otherwise, the method walks through the list
     * until it reaches the last node, then links the new node after it.
     *
     * @param pkg package record to add to the registry
     */
    public void addRecord(Package pkg) {
        SLLNode newNode = new SLLNode(pkg);

        // If the list has no nodes, the new node becomes the first record.
        if (isEmpty()) {
            head = newNode;
            return;
        }

        // Traverse manually until the last node is found.
        SLLNode current = head;
        while (current.next != null) {
            current = current.next;
        }

        // Link the new package record at the end of the list.
        current.next = newNode;
    }

    /**
     * Traverses the list from head to tail and prints all package records.
     *
     * If the list is empty, a clear message is printed instead of trying
     * to access missing data.
     */
    public void displayLog() {
        if (isEmpty()) {
            System.out.println("The Master Registry is currently empty.");
            return;
        }

        SLLNode current = head;
        int recordNumber = 1;

        // Visit each node one by one until the end of the list is reached.
        while (current != null) {
            System.out.println("Record #" + recordNumber);
            current.data.displayPackage();
            System.out.println("----------------------------------------");

            current = current.next;
            recordNumber++;
        }
    }
}
