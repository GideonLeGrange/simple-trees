package me.legrange.tree;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class AbstractBinaryTree<T> implements Tree<T> {

    protected final BinaryNode<T> root;

    /**
     * Create a new binary ree with the given data at the root.
     *
     * @param rootData The data for the root
     */
    protected AbstractBinaryTree(T rootData) {
        this.root = new BinaryNode<>(null, rootData);
    }

    @Override
    public final boolean contains(T object) {
        return preOrderDepthStream().anyMatch(val -> val.equals(object));
    }

    public final Stream<T> inOrderDepthStream() {
        return makeInOrderDepthStream(root).map(node -> node.getData());
    }

    public final Stream<T> preOrderDepthStream() {
        return makePreOrderDepthStream(root).map(node -> node.getData());
    }

    public final Stream<T> postOrderDepthStream() {
        return makePostOrderDepthStream(root)
                .map(node -> node.getData());
    }

    @Override
    public final Stream<T> breadthStream() {
        return makeBreadthStream(Collections.singletonList(getRoot()));
    }

    @Override
    public final T getRoot() {
        return root.getData();
    }

    @Override
    public final Optional<T> getParent(T child) {
        return makePreOrderDepthStream(root)
                .filter(node -> node.getData().equals(child)).findFirst()
                .map(node -> node.getParentNode())
                .map(node -> node.getData());
    }

    @Override
    public final int getDepth() {
        return calculateDepth(root);
    }

    @Override
    public final int getWidth() {
        return calculateWidth(root);
    }

    /**
     * Add a left child to the root of the tree
     *
     * @param child The child data
     */
    protected void addLeft(T child) {
        if (contains(child)) {
            throw new IllegalArgumentException("Data is already in the tree");
        }
        BinaryNode node = new BinaryNode(root, child);
        root.addLeft(node);
    }

    /**
     * Add a right child to the root of the tree
     *
     * @param child The child data
     */
    protected void addRight(T child) {
        if (contains(child)) {
            throw new IllegalArgumentException("Data is already in the tree");
        }
        BinaryNode node = new BinaryNode(root, child);
        root.addRight(node);
    }

    /**
     * Add a left child to a specific parent.
     *
     * @param parent The parent data
     * @param child  The child data
     */
    protected void addLeft(T parent, T child) {
        BinaryNode<T> parentNode = getNode(parent);
        if (contains(child)) {
            throw new IllegalArgumentException("Data is already in the tree");
        }
        BinaryNode node = new BinaryNode(parentNode, child);
        parentNode.addLeft(node);
    }

    /**
     * Add a right child to a specific parent.
     *
     * @param parent The parent data
     * @param child  The child data
     */
    protected void addRight(T parent, T child) {
        BinaryNode<T> parentNode = getNode(parent);
        if (contains(child)) {
            throw new IllegalArgumentException("Data is already in the tree");
        }
        BinaryNode node = new BinaryNode(parentNode, child);
        parentNode.addRight(node);
    }

    /**
     * Get the left child data for specific parent data.
     *
     * @param parent The parent data
     * @return The child data
     */
    public final Optional<T> getLeft(T parent) {
        return makePreOrderDepthStream(root)
                .filter(node -> node.getData().equals(parent))
                .findFirst()
                .map(BinaryNode::getLeft)
                .map(BinaryNode::getData);
    }

    /**
     * Get the right child data for specific parent data.
     *
     * @param parent The parent data
     * @return The child data
     */
    public final Optional<T> getRight(T parent) {
        return makePreOrderDepthStream(root)
                .filter(node -> node.getData().equals(parent))
                .findFirst()
                .map(BinaryNode::getRight)
                .map(BinaryNode::getData);
    }

    /**
     * Convenience method to find the node for an object
     *
     * @param object The data
     * @return The node
     */
    private BinaryNode<T> getNode(T object) {
        Optional<BinaryNode<T>> found = makePreOrderDepthStream(root)
                .filter(node -> node.getData().equals(object))
                .findFirst();
        if (found.isPresent()) {
            return found.get();
        }
        throw new NoSuchElementException("No data found for object");
    }

    /**
     * Recursively set up an in-order depth first stream.
     *
     * @param node The point in the tree to work from
     * @return The stream
     */
    private Stream<BinaryNode<T>> makeInOrderDepthStream(BinaryNode<T> node) {
        // Inorder   (Left, Root, Right)
        BinaryNode<T> left = node.getLeft();
        BinaryNode<T> right = node.getRight();
        return Stream.concat(
                left != null ? Stream.of(left).flatMap(this::makeInOrderDepthStream) : Stream.empty(),
                Stream.concat(Stream.of(node),
                        right != null ? Stream.of(right).flatMap(this::makeInOrderDepthStream) : Stream.empty()));
    }

    /**
     * Recursively set up a pre-order depth first stream.
     *
     * @param node The point in the tree to work from
     * @return The stream
     */
    private Stream<BinaryNode<T>> makePreOrderDepthStream(BinaryNode<T> node) {
        // Preorder  (Root, Left, Right)
        BinaryNode<T> left = node.getLeft();
        BinaryNode<T> right = node.getRight();
        return Stream.concat(Stream.of(node),
                Stream.concat(
                        left != null ? Stream.of(left).flatMap(this::makePreOrderDepthStream) : Stream.empty(),
                        right != null ? Stream.of(right).flatMap(this::makePreOrderDepthStream) : Stream.empty()));
    }

    /**
     * Recursively set up a post-order depth first stream.
     *
     * @param node The point in the tree to work from
     * @return The stream
     */
    private Stream<BinaryNode<T>> makePostOrderDepthStream(BinaryNode<T> node) {
        // Postorder (Left, Right, Root) : 4 5 2 3 1
        BinaryNode<T> left = node.getLeft();
        BinaryNode<T> right = node.getRight();
        return Stream.concat(
                left != null ? Stream.of(left).flatMap(this::makePostOrderDepthStream) : Stream.empty(),
                Stream.concat(
                        right != null ? Stream.of(right).flatMap(this::makePostOrderDepthStream) : Stream.empty(),
                        Stream.of(node)));
    }

    /**
     * Recursively set up a breadth first stream.
     *
     * @return The stream
     */
    private Stream<T> makeBreadthStream(List<T> data) {
        if (data.isEmpty()) {
            return Stream.empty();
        }
        return Stream.concat(data.stream(),
                makeBreadthStream(data.stream().flatMap(object -> streamOfChildren(object)).collect(Collectors.toList())));
    }

    private Stream<T> streamOfChildren(T val) {
        Optional<T> left = getLeft(val);
        Optional<T> right = getRight(val);
        return Stream.concat(left.isPresent() ? Stream.of(left.get()) : Stream.empty(), right.isPresent() ? Stream.of(right.get()) : Stream.empty());
    }

    private int calculateDepth(BinaryNode<T> node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(calculateDepth(node.getLeft()), calculateDepth(node.getRight()));
    }

    private int calculateWidth(BinaryNode<T> node) {
        if (node == null) {
            return 0;
        }
        return Math.max(1, calculateWidth(node.getLeft()) + calculateWidth(node.getRight()));
    }
}
