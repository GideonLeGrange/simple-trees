package me.legrange.tree;

import java.util.stream.Stream;

public final class BinaryTree<T> extends AbstractBinaryTree<T> {

    /**
     * Create a new binary ree with the given data at the root.
     *
     * @param rootData The data for the root
     */
    public BinaryTree(T rootData) {
        super(rootData);
    }

    /**
     * Add a left child to the root of the tree
     *
     * @param child The child data
     */
    public void addLeft(T child) {
        super.addLeft(child);
    }

    /**
     * Add a right child to the root of the tree
     *
     * @param child The child data
     */
    public void addRight(T child) {
        super.addRight(child);
    }

    /**
     * Add a left child to a specific parent.
     *
     * @param parent The parent data
     * @param child  The child data
     */
    public void addLeft(T parent, T child) {
        super.addLeft(parent, child);
    }

    /**
     * Add a right child to a specific parent.
     *
     * @param parent The parent data
     * @param child  The child data
     */
    public void addRight(T parent, T child) {
        super.addRight(parent, child);
    }

}
