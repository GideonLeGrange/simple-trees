package me.legrange.tree;

import java.util.ArrayList;
import java.util.List;

/** Tree node structure.
 *
 * @param <T> The type of the data contained in the node.
 */
final class GeneralNode<T> {

    private final GeneralNode<T> parentNode;
    private final T data;
    private final List<GeneralNode<T>> children = new ArrayList();

    GeneralNode(GeneralNode<T> parentNode, T data) {
        this.parentNode = parentNode;
        this.data = data;
    }

    GeneralNode<T> getParentNode() {
        return parentNode;
    }

    T getData() {
        return data;
    }

    void add(GeneralNode<T> child) {
        children.add(child);
    }

    List<GeneralNode<T>> getChildren() {
        return children;
    }

}
