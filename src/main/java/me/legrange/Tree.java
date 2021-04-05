package me.legrange;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** A simple tree class that hides the tree implementation
 *
 * @param <T> The type of data stored in the tree
 */
public final class Tree<T> {

    private Map<T, Node<T>> index = new HashMap();
    private Node<T> root;

    /** Create a new tree with the given data at the root.
     *
     * @param rootData The data for the root
     */
    public Tree(T rootData) {
        this.root = new Node(null, rootData);
        index.put(rootData, root);
    }

    /** Check if the tree contains the given object.
     *
     * @param object The object
     * @return Is it in the tree?
     */
    public boolean contains(T object) {
        return index.containsKey(object);
    }

    /** Return a stream that does depth-first traversal of the tree.
     *
     * @return The stream
     */
   public Stream<T> depthStream() {
        return makeDepthStream(root.getData());
    }

    /** Return a stream that does breadth-first traversal of the tree.
     *
     * @return The stream
     */
    public Stream<T> breadthStream() {
        return makeBreadthStream(Collections.singletonList(root.getData()));
    }

    /** Return the data at the root of the tree
     *
     * @return The data
     */
    public T getRoot() {
        return root.getData();
    }

    /** Add a child to the root of the tree
     *
     * @param child The child data
     */
    public void add(T child) {
        if (index.containsKey(child)) {
            throw new IllegalArgumentException("Data is already in the tree");
        }
        Node node = new Node(null, child);
        index.put(child, node);
        root.add(node);
    }

    /** Add a child to a specific parent.
     *
     * @param parent The parent data
     * @param child The child data
     */
    public void add(T parent, T child ) {
        Node<T> parentNode = getNode(parent);
        if (index.containsKey(child)) {
            throw new IllegalArgumentException("Data is already in the tree");
        }
        Node node = new Node(parentNode, child);
        parentNode.add(node);
        index.put(child, node);
    }

    /** Get the parent data for child data
     *
     * @param child The child data
     * @return The parent data
     */
    public Optional<T> getParent(T child) {
        return Optional.ofNullable(getNode(child).getParentNode()).map(node -> node.getData());
    }

    /** Get the child data for specific parent data.
     *
     * @param parent The parent data
     * @return The child data
     */
    public List<T> getChildren(T parent) {
        if (!index.containsKey(parent)) {
            throw  new NoSuchElementException("No data found for object");
        }
        return index.get(parent).getChildren().stream()
                .map(node -> node.getData()).collect(Collectors.toList());
    }

    /** Convenience method to find the node for an object
     *
     * @param object The data
     * @return The node
     */
    private Node<T> getNode(T object) {
        if (!index.containsKey(object)) {
            throw  new NoSuchElementException("No data found for object");
        }
        return index.get(object);
    }

    private Stream<T> makeDepthStream(T data) {
        return Stream.concat(Stream.of(data),
                this.getChildren(data).stream().flatMap(this::makeDepthStream));
    }

    private Stream<T> makeBreadthStream(List<T> data) {
        if (data.isEmpty()) {
            return Stream.empty();
        }
        return Stream.concat(data.stream(),
                makeBreadthStream(data.stream().flatMap(object -> this.getChildren(object).stream()).collect(Collectors.toList())));
    }

}
