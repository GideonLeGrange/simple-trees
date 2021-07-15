package me.legrange;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        if (index.containsKey(child)) {
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
        index.put(child, newNode);
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


    /**
     * Recursively set up a depth first stream.
     *
     * @param data The point in the tree to work from
     * @return The stream
     */
    private Stream<T> makeDepthStream(T data) {
        Optional<T> left = getLeft(data);
        Optional<T> right = getRight(data);
        return Stream.concat(Stream.of(data),
                Stream.concat(
                        left.isPresent() ? Stream.of(left.get()).flatMap(this::makeDepthStream) : Stream.empty(),
                        right.isPresent() ? Stream.of(right.get()).flatMap(this::makeDepthStream) : Stream.empty()));
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
