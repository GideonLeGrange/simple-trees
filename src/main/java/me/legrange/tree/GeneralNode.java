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

    void remove(GeneralNode<T> child) {
        children.remove(child);
    }

    static <T> GeneralNode<T> changeParent(GeneralNode<T> parentNode, GeneralNode<T> node) {
        GeneralNode<T> newNode = new GeneralNode<>(parentNode, node.data);
        for (GeneralNode<T> childNode : node.children) {
            newNode.add(changeParent(newNode, childNode));
        }
        return newNode;
    }

    List<GeneralNode<T>> getChildren() {
        return children;
    }

}
