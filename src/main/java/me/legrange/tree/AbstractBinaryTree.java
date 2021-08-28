package me.legrange.tree;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class AbstractBinaryTree<T> implements Tree<T> {

    protected final Map<T, BinaryNode<T>> index = new HashMap();
    protected final BinaryNode<T> root;

    /**
     * Create a new binary ree with the given data at the root.
     *
     * @param rootData The data for the root
     */
    protected AbstractBinaryTree(T rootData) {
        this.root = new BinaryNode<>(null, rootData);
        index.put(rootData, root);
    }

    @Override
    public final boolean contains(T object) {
        return index.containsKey(object);
    }

    public final Stream<T> inOrderDepthStream() {
        return makeInOrderDepthStream(root.getData());
    }

    public final Stream<T> preOrderDepthStream() {
        return makePreOrderDepthStream(root.getData());
    }

    public final Stream<T> postOrderDepthStream() {
        return makePostOrderDepthStream(root.getData());
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
        return Optional.ofNullable(getNode(child).getParentNode()).map(node -> node.getData());
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
        if (index.containsKey(child)) {
            throw new IllegalArgumentException("Data is already in the tree");
        }
        BinaryNode node = new BinaryNode(root, child);
        index.put(child, node);
        root.addLeft(node);
    }

    /**
     * Add a right child to the root of the tree
     *
     * @param child The child data
     */
    protected void addRight(T child) {
        if (index.containsKey(child)) {
            throw new IllegalArgumentException("Data is already in the tree");
        }
        BinaryNode node = new BinaryNode(root, child);
        index.put(child, node);
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
        if (index.containsKey(child)) {
            throw new IllegalArgumentException("Data is already in the tree");
        }
        BinaryNode node = new BinaryNode(parentNode, child);
        parentNode.addLeft(node);
        index.put(child, node);
    }

    /**
     * Add a right child to a specific parent.
     *
     * @param parent The parent data
     * @param child  The child data
     */
    protected void addRight(T parent, T child) {
        BinaryNode<T> parentNode = getNode(parent);
        if (index.containsKey(child)) {
            throw new IllegalArgumentException("Data is already in the tree");
        }
        BinaryNode node = new BinaryNode(parentNode, child);
        parentNode.addRight(node);
        index.put(child, node);
    }

    /**
     * Get the left child data for specific parent data.
     *
     * @param parent The parent data
     * @return The child data
     */
    public final Optional<T> getLeft(T parent) {
        return Optional.ofNullable(index.get(parent)).flatMap(node -> Optional.ofNullable(node.getLeft()).map(n -> n.getData()));
    }

    /**
     * Get the right child data for specific parent data.
     *
     * @param parent The parent data
     * @return The child data
     */
    public final Optional<T> getRight(T parent) {
        return Optional.ofNullable(index.get(parent)).flatMap(node -> Optional.ofNullable(node.getRight()).map(n -> n.getData()));
    }

    /**
     * Convenience method to find the node for an object
     *
     * @param object The data
     * @return The node
     */
    private BinaryNode<T> getNode(T object) {
        if (!index.containsKey(object)) {
            throw new NoSuchElementException("No data found for object");
        }
        return index.get(object);
    }

    /**
     * Recursively set up an in-order depth first stream.
     *
     * @param data The point in the tree to work from
     * @return The stream
     */
    private Stream<T> makeInOrderDepthStream(T data) {
        // Inorder   (Left, Root, Right)
        Optional<T> left = getLeft(data);
        Optional<T> right = getRight(data);
        return Stream.concat(
                left.isPresent() ? Stream.of(left.get()).flatMap(this::makeInOrderDepthStream) : Stream.empty(),
                Stream.concat(Stream.of(data),
                        right.isPresent() ? Stream.of(right.get()).flatMap(this::makeInOrderDepthStream) : Stream.empty()));
    }

    /**
     * Recursively set up a pre-order depth first stream.
     *
     * @param data The point in the tree to work from
     * @return The stream
     */
    private Stream<T> makePreOrderDepthStream(T data) {
        // Preorder  (Root, Left, Right)
        Optional<T> left = getLeft(data);
        Optional<T> right = getRight(data);
        return Stream.concat(Stream.of(data),
                Stream.concat(
                        left.isPresent() ? Stream.of(left.get()).flatMap(this::makePreOrderDepthStream) : Stream.empty(),
                        right.isPresent() ? Stream.of(right.get()).flatMap(this::makePreOrderDepthStream) : Stream.empty()));
    }

    /**
     * Recursively set up a post-order depth first stream.
     *
     * @param data The point in the tree to work from
     * @return The stream
     */
    private Stream<T> makePostOrderDepthStream(T data) {
        // Postorder (Left, Right, Root) : 4 5 2 3 1
        Optional<T> left = getLeft(data);
        Optional<T> right = getRight(data);
        return Stream.concat(
                left.isPresent() ? Stream.of(left.get()).flatMap(this::makePostOrderDepthStream) : Stream.empty(),
                Stream.concat(
                        right.isPresent() ? Stream.of(right.get()).flatMap(this::makePostOrderDepthStream) : Stream.empty(),
                        Stream.of(data)));
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
