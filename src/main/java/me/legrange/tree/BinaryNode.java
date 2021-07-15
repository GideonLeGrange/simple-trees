package me.legrange.tree;

/** Binary tree node structure.
 *
 * @param <T> The type of the data contained in the node.
 */
final class BinaryNode<T> {

    private final BinaryNode<T> parentNode;
    private final T data;
    private BinaryNode<T> left;
    private BinaryNode<T> right;

    BinaryNode(BinaryNode<T> parentNode, T data) {
        this.parentNode = parentNode;
        this.data = data;
    }

    BinaryNode<T> getParentNode() {
        return parentNode;
    }

    T getData() {
        return data;
    }

    void addLeft(BinaryNode<T> child) {
        left = child;
    }

    void addRight(BinaryNode<T> child) {
        right = child;
    }

     BinaryNode<T> getLeft() {
        return left;
    }

     BinaryNode<T> getRight() {
        return right;
    }
}
