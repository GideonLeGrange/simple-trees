package me.legrange;

import java.util.Optional;
import java.util.stream.Stream;

/** A tree for organising data.
 *
 * @param <T> The type of data contained in the tree
 */
public interface Tree<T> {

    /**
     * Check if the tree contains the given data somewhere.
     *
     * @param object The object
     * @return Is it in the tree?
     */
    boolean contains(T object);

    /**
     * Return a stream that does depth-first traversal of the tree.
     *
     * @return The stream
     */
    Stream<T> depthStream();

    /**
     * Return a stream that does breadth-first traversal of the tree.
     *
     * @return The stream
     */
    Stream<T> breadthStream();

    /**
     * Return the data at the root of the tree
     *
     * @return The data
     */
    T getRoot();

    /**
     * Return the depth of the tree.
     *
     * @return The depth
     */
    int getDepth();

    /**
     * Return the width of the tree
     *
     * @return
     */
    int getWidth();

    /**
     * Get the parent data for child data
     *
     * @param child The child data
     * @return The parent data
     */
    Optional<T> getParent(T child);

}
