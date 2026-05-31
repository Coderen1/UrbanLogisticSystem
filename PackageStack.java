/**
 * The PackageStack class is a manual Stack implementation for the
 * FastRoute Kayseri Truck Loading System.
 *
 * Purpose:
 * Packages loaded into a narrow cargo vehicle follow LIFO logic, which
 * means "Last In, First Out." The last package loaded is closest to the
 * exit, so it should be unloaded first.
 */
public class PackageStack {

    /** Top pointer stores the package currently at the top of the stack. */
    private StackNode top;

    /**
     * Creates an empty package stack.
     */
    public PackageStack() {
        top = null;
    }

    /**
     * Checks whether the stack contains no packages.
     *
     * @return true if the stack is empty, otherwise false
     */
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * Pushes a package onto the top of the stack.
     *
     * The new package becomes the top node, and its next reference points
     * to the package that was previously on top.
     *
     * @param pkg package to load onto the stack
     */
    public void push(Package pkg) {
        StackNode newNode = new StackNode(pkg);

        // Link the new node to the previous top before moving the top pointer.
        newNode.next = top;
        top = newNode;
    }

    /**
     * Removes and returns the package from the top of the stack.
     *
     * This follows LIFO order because the newest loaded package is removed
     * first.
     *
     * @return removed Package object, or null if the stack is empty
     */
    public Package pop() {
        if (isEmpty()) {
            return null;
        }

        Package removedPackage = top.data;

        // Move the top pointer down to the next package in the stack.
        top = top.next;

        return removedPackage;
    }

    /**
     * Displays all packages in the stack from top to bottom.
     *
     * This shows the exact LIFO order in which packages will be unloaded.
     */
    public void displayStack() {
        if (isEmpty()) {
            System.out.println("The Truck Loading Stack is currently empty.");
            return;
        }

        StackNode current = top;
        int level = 1;

        // Traverse from top to bottom using next references.
        while (current != null) {
            System.out.println("Truck Stack Level #" + level);
            current.data.displayPackage();
            System.out.println("----------------------------------------");

            current = current.next;
            level++;
        }
    }
}
