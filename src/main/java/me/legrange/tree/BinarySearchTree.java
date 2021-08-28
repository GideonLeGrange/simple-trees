package me.legrange.tree;

public final class BinarySearchTree<T extends Comparable> extends AbstractBinaryTree<T> {

    /**
     * Create a new binary ree with the given data at the root.
     *
     * @param rootData The data for the root
     */
    public BinarySearchTree(T rootData) {
        super(rootData);
    }

    /**
     * Add a right child to a specific parent.
     *
     * @param child The child data
     */
    public void add(T child) {
        if (contains(child)) {
            throw new IllegalArgumentException("Data is already in the tree");
        }
        BinaryNode<T> parent = findParent(root, child);
        BinaryNode<T> newNode = new BinaryNode<>(parent, child);
        int diff = child.compareTo(parent.getData());
        if (diff < 0) {
            parent.addLeft(newNode);
        } else if (diff > 0) {
            parent.addRight(newNode);
        } else {
            throw new IllegalArgumentException("Duplicate element");
        }
    }

    /**
     * Find the data closest to the search term in the tree.
     *
     * @param data The search term
     * @return The closest found data
     */
    public T find(T data) {
        return findParent(root, data).getData();
    }

    /** Find the best node for the given data. Best means the value
     * compares to the data in the node, or there are no children to search for this node.
     *
     * @param node
     * @param value
     * @return
     */
    private BinaryNode<T> findParent(BinaryNode<T> node, T value) {
        int diff = value.compareTo(node.getData());
        if (diff < 0) {
            if (node.getLeft() == null) {
                return node;
            }
            return findParent(node.getLeft(), value);
        } else if (diff > 0) {
            if (node.getRight() == null) {
                return node;
            }
            return findParent(node.getRight(), value);
        } else {
            return node;
        }
    }


}
