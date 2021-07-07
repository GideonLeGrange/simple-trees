package me.legrange;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A simple tree class that hides the tree implementation. The user works only with the Tree object
 * and with their data. Since the tree relies on hashCode() and equals() internally, for best results
 * it should be used by objects that implement those properly.
 *
 * @param <T> The type of data stored in the tree
 */
public final class GeneralTree<T> implements Tree<T> {

    private final Map<T, GeneralNode<T>> index = new HashMap();
    private final GeneralNode<T> root;

    /**
     * Create a new tree with the given data at the root.
     *
     * @param rootData The data for the root
     */
    public GeneralTree(T rootData) {
        this.root = new GeneralNode(null, rootData);
        index.put(rootData, root);
    }

    /**
     * Check if the tree contains the given data somewhere.
     *
     * @param object The object
     * @return Is it in the tree?
     */
    public boolean contains(T object) {
        return index.containsKey(object);
    }

    /**
     * Return a stream that does depth-first traversal of the tree.
     *
     * @return The stream
     */
    public Stream<T> depthStream() {
        return makeDepthStream(root.getData());
    }

    /**
     * Return a stream that does breadth-first traversal of the tree.
     *
     * @return The stream
     */
    public Stream<T> breadthStream() {
        return makeBreadthStream(Collections.singletonList(root.getData()));
    }

    /**
     * Return the data at the root of the tree
     *
     * @return The data
     */
    public T getRoot() {
        return root.getData();
    }

    public int getDepth() {
        return calculateDepth(root);
    }

    public int getWidth() {
        return calculateWidth(root);
    }

    /**
     * Add a child to the root of the tree
     *
     * @param child The child data
     */
    public void add(T child) {
        if (index.containsKey(child)) {
            throw new IllegalArgumentException("Data is already in the tree");
        }
        GeneralNode node = new GeneralNode(null, child);
        index.put(child, node);
        root.add(node);
    }

    /**
     * Add a child to a specific parent.
     *
     * @param parent The parent data
     * @param child  The child data
     */
    public void add(T parent, T child) {
        GeneralNode<T> parentNode = getNode(parent);
        if (index.containsKey(child)) {
            throw new IllegalArgumentException("Data is already in the tree");
        }
        GeneralNode node = new GeneralNode(parentNode, child);
        parentNode.add(node);
        index.put(child, node);
    }

    /**
     * Get the parent data for child data
     *
     * @param child The child data
     * @return The parent data
     */
    public Optional<T> getParent(T child) {
        return Optional.ofNullable(getNode(child).getParentNode()).map(node -> node.getData());
    }

    /**
     * Get the child data for specific parent data.
     *
     * @param parent The parent data
     * @return The child data
     */
    public List<T> getChildren(T parent) {
        if (!index.containsKey(parent)) {
            throw new NoSuchElementException("No data found for object");
        }
        return index.get(parent).getChildren().stream()
                .map(node -> node.getData()).collect(Collectors.toList());
    }

    /**
     * Convenience method to find the node for an object
     *
     * @param object The data
     * @return The node
     */
    private GeneralNode<T> getNode(T object) {
        if (!index.containsKey(object)) {
            throw new NoSuchElementException("No data found for object");
        }
        return index.get(object);
    }

    /**
     * Recursively set up a depth first stream.
     *
     * @param data The point in the tree to work from
     * @return The stream
     */
    private Stream<T> makeDepthStream(T data) {
        return Stream.concat(Stream.of(data),
                this.getChildren(data).stream().flatMap(this::makeDepthStream));
    }

    /**
     * Recursively set up a breadth first stream.
     *
     * @param data The point in the tree to work from
     * @return The stream
     */
    private Stream<T> makeBreadthStream(List<T> data) {
        if (data.isEmpty()) {
            return Stream.empty();
        }
        return Stream.concat(data.stream(),
                makeBreadthStream(data.stream().flatMap(object -> this.getChildren(object).stream()).collect(Collectors.toList())));
    }

    private int calculateDepth(GeneralNode<T> node) {
        return 1 + node.getChildren().stream()
                .map(child -> calculateDepth(child))
                .max(Comparator.comparingInt(a -> a)).orElse(0);
    }

    private int calculateWidth(GeneralNode<T> node) {
        if (node.getChildren().isEmpty()) {
            return 1;
        }
        return node.getChildren().stream()
                .map(this::calculateWidth)
                .mapToInt(a -> a)
                .sum();
    }

}
