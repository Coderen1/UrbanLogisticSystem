/**
 * The AVLNode class represents one node in the AVL Tree Address Directory.
 *
 * Each node stores a neighborhood name, a customer ID, the height of the
 * node, and references to left and right child nodes.
 */
public class AVLNode {

    /** Neighborhood name used as the sorting key in the AVL Tree. */
    String neighborhood;

    /** Customer ID connected to this neighborhood record. */
    String customerID;

    /** Height of this node inside the AVL Tree. */
    int height;

    /** Left child node, containing alphabetically smaller neighborhoods. */
    AVLNode left;

    /** Right child node, containing alphabetically larger neighborhoods. */
    AVLNode right;

    /**
     * Creates a new AVL Tree node with a neighborhood and customer ID.
     *
     * @param neighborhood neighborhood name used as the tree key
     * @param customerID   customer ID stored with the neighborhood
     */
    public AVLNode(String neighborhood, String customerID) {
        this.neighborhood = neighborhood;
        this.customerID = customerID;
        this.height = 1;
        this.left = null;
        this.right = null;
    }
}
