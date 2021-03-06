package me.legrange.tree;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static me.legrange.tree.GeneralNode.changeParent;

/**
 * A simple tree class that hides the tree implementation. The user works only with the Tree object
 * and with their data. Since the tree relies on hashCode() and equals() internally, for best results
 * it should be used by objects that implement those properly.
 *
 * @param <T> The type of data stored in the tree
 */
public final class GeneralTree<T> implements Tree<T> {

    private final GeneralNode<T> root;

    /**
     * Create a new tree with the given data at the root.
     *
     * @param rootData The data for the root
     */
    public GeneralTree(T rootData) {
        this.root = new GeneralNode(null, rootData);
    }

    @Override
    public boolean contains(T object) {
        return preOrderDepthStream().anyMatch(data -> data.equals(object));
    }

    @Override
    public Stream<T> preOrderDepthStream() {
        return makePreOrderDepthStream(root).map(node -> node.getData());
    }

    @Override
    public Stream<T> postOrderDepthStream() {
        return makePostOrderDepthStream(root).map(node -> node.getData());
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
        root.add(new GeneralNode(root, child));
    }

    /**
     * Add a child to a specific parent.
     *
     * @param parent The parent data
     * @param child  The child data
     */
    public void add(T parent, T child) {
        GeneralNode<T> parentNode = getNode(parent);
        parentNode.add(new GeneralNode(parentNode, child));
    }

    /**
     * Add a move to a new parent.
     *
     * @param parent The new parent data
     * @param child  The new child data
     */
    public void move(T parent, T child) {
        GeneralNode<T> childNode = getNode(child);
        GeneralNode<T> currentParentNode = childNode.getParentNode();
        currentParentNode.remove(childNode);
        GeneralNode<T> newParentNode = getNode(parent);
        newParentNode.add(changeParent(newParentNode, childNode));
    }

    /**
     * Get the child data for specific parent data.
     *
     * @param parent The parent data
     * @return The child data
     */
    public List<T> getChildren(T parent) {
        return getNode(parent).getChildren().stream()
                .map(node -> node.getData()).collect(Collectors.toList());
    }

    /**
     * Convenience method to find the node for an object
     *
     * @param object The data
     * @return The node
     */
    private GeneralNode<T> getNode(T object) {
        Optional<GeneralNode<T>> first = makePreOrderDepthStream(root)
                .filter(node -> node.getData().equals(object))
                .findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        throw new NoSuchElementException(format("No data found for object '%s'", object));
    }

    /**
     * Recursively set up a pre-order depth first stream.
     *
     * @param data The point in the tree to work from
     * @return The stream
     */
    private Stream<GeneralNode<T>> makePreOrderDepthStream(GeneralNode<T> data) {
        return Stream.concat(Stream.of(data),
                data.getChildren().stream().flatMap(this::makePreOrderDepthStream));
    }

    /**
     * Recursively set up a post-order depth first stream.
     *
     * @param data The point in the tree to work from
     * @return The stream
     */
    private Stream<GeneralNode<T>> makePostOrderDepthStream(GeneralNode<T> data) {
        return Stream.concat(data.getChildren().stream().flatMap(this::makePostOrderDepthStream), Stream.of(data));
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
