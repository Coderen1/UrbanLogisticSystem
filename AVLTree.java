/**
 * The AVLTree class is a manual AVL Tree implementation for the
 * FastRoute Kayseri Address Directory.
 *
 * Purpose:
 * The tree stores neighborhood and customer ID records in sorted order.
 * After each insertion, the AVL Tree checks its balance and performs
 * rotations when needed so searches stay efficient.
 */
public class AVLTree {

    /** Root pointer stores the first node of the AVL Tree. */
    private AVLNode root;

    /**
     * Creates an empty AVL Tree.
     */
    public AVLTree() {
        root = null;
    }

    /**
     * Public insert method used by other classes.
     *
     * This method starts the recursive insertion from the root and then
     * updates the root in case a rotation changes the top of the tree.
     *
     * @param neighborhood neighborhood name to insert
     * @param customerID   customer ID connected to the neighborhood
     */
    public void insert(String neighborhood, String customerID) {
        root = insert(root, neighborhood, customerID);
    }

    /**
     * Recursive helper method that inserts a new record like a BST, then
     * balances the tree before returning the updated subtree root.
     *
     * @param node         current subtree root
     * @param neighborhood neighborhood name to insert
     * @param customerID   customer ID connected to the neighborhood
     * @return updated and balanced subtree root
     */
    private AVLNode insert(AVLNode node, String neighborhood, String customerID) {
        // Normal BST insertion: create a new node when the empty position is found.
        if (node == null) {
            return new AVLNode(neighborhood, customerID);
        }

        int comparison = neighborhood.compareToIgnoreCase(node.neighborhood);

        if (comparison < 0) {
            node.left = insert(node.left, neighborhood, customerID);
        } else if (comparison > 0) {
            node.right = insert(node.right, neighborhood, customerID);
        } else {
            // If the neighborhood already exists, update the customer ID.
            node.customerID = customerID;
            return node;
        }

        // Update height because the subtree may have changed after insertion.
        node.height = 1 + max(height(node.left), height(node.right));

        // Return the balanced version of this subtree.
        return balance(node);
    }

    /**
     * Searches for a neighborhood in the AVL Tree and prints the result.
     *
     * @param neighborhood neighborhood name to search for
     * @return matching AVLNode if found, otherwise null
     */
    public AVLNode search(String neighborhood) {
        AVLNode result = search(root, neighborhood);

        if (result != null) {
            System.out.println("Neighborhood Found: " + result.neighborhood);
            System.out.println("Customer ID: " + result.customerID);
        } else {
            System.out.println("Neighborhood not found.");
        }

        return result;
    }

    /**
     * Recursive helper method for searching the AVL Tree.
     *
     * @param node         current node being checked
     * @param neighborhood neighborhood name to search for
     * @return matching node if found, otherwise null
     */
    private AVLNode search(AVLNode node, String neighborhood) {
        if (node == null) {
            return null;
        }

        int comparison = neighborhood.compareToIgnoreCase(node.neighborhood);

        if (comparison == 0) {
            return node;
        } else if (comparison < 0) {
            return search(node.left, neighborhood);
        } else {
            return search(node.right, neighborhood);
        }
    }

    /**
     * Displays the AVL Tree in sorted neighborhood order.
     */
    public void displayInOrder() {
        if (root == null) {
            System.out.println("The Address Directory is currently empty.");
            return;
        }

        displayInOrder(root);
    }

    /**
     * Recursive in-order traversal helper.
     *
     * In-order traversal visits the left subtree, then the current node,
     * then the right subtree. For a search tree, this prints sorted data.
     *
     * @param node current node being visited
     */
    private void displayInOrder(AVLNode node) {
        if (node == null) {
            return;
        }

        displayInOrder(node.left);
        System.out.println("Neighborhood : " + node.neighborhood);
        System.out.println("Customer ID  : " + node.customerID);
        System.out.println("Node Height  : " + node.height);
        System.out.println("----------------------------------------");
        displayInOrder(node.right);
    }

    /**
     * Returns the height of a node.
     *
     * A null node has height 0. A leaf node has height 1.
     *
     * @param node node whose height is needed
     * @return height of the node
     */
    private int height(AVLNode node) {
        if (node == null) {
            return 0;
        }

        return node.height;
    }

    /**
     * Calculates the balance factor of a node.
     *
     * Balance factor = height(left subtree) - height(right subtree).
     * Valid AVL balance factors are -1, 0, and +1.
     *
     * @param node node whose balance factor is needed
     * @return balance factor of the node
     */
    private int getBalance(AVLNode node) {
        if (node == null) {
            return 0;
        }

        return height(node.left) - height(node.right);
    }

    /**
     * Performs a right rotation to fix a left-heavy subtree.
     *
     * This rotation is used directly for the LL case and as part of
     * the LR case.
     *
     * @param y root of the unbalanced subtree
     * @return new root after rotation
     */
    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode middleSubtree = x.right;

        // Perform rotation.
        x.right = y;
        y.left = middleSubtree;

        // Update heights from bottom to top after changing links.
        y.height = 1 + max(height(y.left), height(y.right));
        x.height = 1 + max(height(x.left), height(x.right));

        return x;
    }

    /**
     * Performs a left rotation to fix a right-heavy subtree.
     *
     * This rotation is used directly for the RR case and as part of
     * the RL case.
     *
     * @param x root of the unbalanced subtree
     * @return new root after rotation
     */
    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode middleSubtree = y.left;

        // Perform rotation.
        y.left = x;
        x.right = middleSubtree;

        // Update heights from bottom to top after changing links.
        x.height = 1 + max(height(x.left), height(x.right));
        y.height = 1 + max(height(y.left), height(y.right));

        return y;
    }

    /**
     * Balances a node if its balance factor is outside the AVL range.
     *
     * This method handles all four AVL rotation cases:
     * LL: left-left case
     * RR: right-right case
     * LR: left-right case
     * RL: right-left case
     *
     * @param node subtree root that may be unbalanced
     * @return balanced subtree root
     */
    private AVLNode balance(AVLNode node) {
        int balanceFactor = getBalance(node);

        // LL Case: left subtree is heavy and its left side is also heavy.
        if (balanceFactor > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }

        // LR Case: left subtree is heavy but its right side is heavier.
        if (balanceFactor > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // RR Case: right subtree is heavy and its right side is also heavy.
        if (balanceFactor < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }

        // RL Case: right subtree is heavy but its left side is heavier.
        if (balanceFactor < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        // Already balanced.
        return node;
    }

    /**
     * Returns the larger of two integer values.
     *
     * @param first  first value
     * @param second second value
     * @return larger value
     */
    private int max(int first, int second) {
        if (first > second) {
            return first;
        }

        return second;
    }
}
