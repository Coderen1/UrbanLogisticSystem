/**
 * The DoublyLinkedList class is a manual implementation of a Doubly
 * Linked List for the FastRoute Kayseri Intake Buffer.
 *
 * Purpose:
 * The intake buffer manages packages that have just arrived at the
 * warehouse. New packages are inserted at the end of the buffer, and
 * the oldest package can be removed from the front when it is ready for
 * the next warehouse operation.
 */
public class DoublyLinkedList {

    /** Head pointer stores the first node in the buffer. */
    private DLLNode head;

    /** Tail pointer stores the last node in the buffer. */
    private DLLNode tail;

    /**
     * Creates an empty Doubly Linked List.
     */
    public DoublyLinkedList() {
        head = null;
        tail = null;
    }

    /**
     * Checks whether the intake buffer has no package records.
     *
     * @return true if the buffer is empty, otherwise false
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Inserts a package at the end of the intake buffer.
     *
     * Because this list has a tail pointer, the method does not need to
     * traverse from the head to find the last node. The new node can be
     * connected directly after the current tail.
     *
     * @param pkg package to add to the end of the intake buffer
     */
    public void insertAtTail(Package pkg) {
        DLLNode newNode = new DLLNode(pkg);

        // If the buffer is empty, the new node becomes both head and tail.
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
            return;
        }

        // Connect the new node after the current tail.
        tail.next = newNode;
        newNode.prev = tail;

        // Move the tail pointer forward to the new last node.
        tail = newNode;
    }

    /**
     * Removes and returns the first package from the intake buffer.
     *
     * This method safely handles three cases:
     * 1. The buffer is empty.
     * 2. The buffer has only one node.
     * 3. The buffer has more than one node.
     *
     * @return the removed Package object, or null if the buffer is empty
     */
    public Package removeFromHead() {
        if (isEmpty()) {
            return null;
        }

        Package removedPackage = head.data;

        // If there is only one node, removing it makes the buffer empty.
        if (head == tail) {
            head = null;
            tail = null;
            return removedPackage;
        }

        // Move head forward and remove the backward link from the new head.
        head = head.next;
        head.prev = null;

        return removedPackage;
    }

    /**
     * Displays all packages in the intake buffer from head to tail.
     *
     * If the buffer is empty, a clear message is printed instead.
     */
    public void displayBuffer() {
        if (isEmpty()) {
            System.out.println("The Intake Buffer is currently empty.");
            return;
        }

        DLLNode current = head;
        int position = 1;

        // Traverse from the first node to the last node using next links.
        while (current != null) {
            System.out.println("Buffer Position #" + position);
            current.data.displayPackage();
            System.out.println("----------------------------------------");

            current = current.next;
            position++;
        }
    }
}
