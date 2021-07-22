package me.legrange.tree;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

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

    @Override
    public boolean contains(T object) {
        return index.containsKey(object);
    }

    @Override
    public Stream<T> depthStream() {
        return makeDepthStream(root).map(node -> node.getData());
    }

    @Override
    public Stream<T> breadthStream() {
        return makeBreadthStream(Collections.singletonList(root)).map(node -> node.getData());
    }

    @Override
    public T getRoot() {
        return root.getData();
    }

    @Override
    public Optional<T> getParent(T child) {
        return Optional.ofNullable(getNode(child).getParentNode()).map(node -> node.getData());
    }

    @Override
    public int getDepth() {
        return calculateDepth(root);
    }

    @Override
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
            throw new IllegalArgumentException(format("Data '%s' is already in the tree", child));
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
        if (index.containsKey(child)) {
            throw new IllegalArgumentException(format("Data '%s' is already in the tree", child));
        }
        GeneralNode<T> parentNode = getNode(parent);
        GeneralNode node = new GeneralNode(parentNode, child);
        parentNode.add(node);
        index.put(child, node);
    }



    /**
     * Get the child data for specific parent data.
     *
     * @param parent The parent data
     * @return The child data
     */
    public List<T> getChildren(T parent) {
        if (!index.containsKey(parent)) {
            throw new NoSuchElementException(format("No data found for object '%s'", parent));
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
    private Stream<GeneralNode<T>> makeDepthStream(GeneralNode<T> data) {
        return Stream.concat(Stream.of(data),
                data.getChildren().stream().flatMap(this::makeDepthStream));
    }

    /**
     * Recursively set up a breadth first stream.
     *
     * @param data The point in the tree to work from
     * @return The stream
     */
    private Stream<GeneralNode<T>> makeBreadthStream(List<GeneralNode<T>> data) {
        if (data.isEmpty()) {
            return Stream.empty();
        }
        return Stream.concat(data.stream(),
                makeBreadthStream(data.stream().flatMap(object -> object.getChildren().stream()).collect(Collectors.toList())));
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
