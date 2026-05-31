/**
 * The PackageQueue class is a manual Queue implementation for the
 * FastRoute Kayseri Standard Delivery System.
 *
 * Purpose:
 * Packages are processed using FIFO logic, which means "First In,
 * First Out." The package that arrives first at the sorting station
 * should be the first package sent for delivery.
 */
public class PackageQueue {

    /** Front pointer stores the first package waiting for delivery. */
    private QueueNode front;

    /** Rear pointer stores the most recently added package. */
    private QueueNode rear;

    /**
     * Creates an empty package queue.
     */
    public PackageQueue() {
        front = null;
        rear = null;
    }

    /**
     * Checks whether the queue contains no packages.
     *
     * @return true if the queue is empty, otherwise false
     */
    public boolean isEmpty() {
        return front == null;
    }

    /**
     * Adds a package to the rear of the queue.
     *
     * The rear pointer allows this method to add a new package without
     * traversing the whole queue.
     *
     * @param pkg package to add to the queue
     */
    public void enqueue(Package pkg) {
        QueueNode newNode = new QueueNode(pkg);

        // If the queue is empty, the new node becomes both front and rear.
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
            return;
        }

        // Link the new node after the current rear and then update rear.
        rear.next = newNode;
        rear = newNode;
    }

    /**
     * Removes and returns the package at the front of the queue.
     *
     * This follows FIFO order because the oldest package is removed first.
     *
     * @return removed Package object, or null if the queue is empty
     */
    public Package dequeue() {
        if (isEmpty()) {
            return null;
        }

        Package removedPackage = front.data;

        // Move the front pointer to the next package in line.
        front = front.next;

        // If the queue became empty, rear must also become null.
        if (front == null) {
            rear = null;
        }

        return removedPackage;
    }

    /**
     * Displays all packages in the queue from front to rear.
     *
     * This shows the exact FIFO order in which packages will be delivered.
     */
    public void displayQueue() {
        if (isEmpty()) {
            System.out.println("The Standard Delivery Queue is currently empty.");
            return;
        }

        QueueNode current = front;
        int position = 1;

        // Traverse from front to rear using next references.
        while (current != null) {
            System.out.println("Delivery Queue Position #" + position);
            current.data.displayPackage();
            System.out.println("----------------------------------------");

            current = current.next;
            position++;
        }
    }
}
