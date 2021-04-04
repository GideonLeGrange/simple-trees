package me.legrange;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

final class Node<T> {

    private final Node<T> parentNode;
    private final T data;
    private final List<Node<T>> children = new ArrayList();

    Node(Node<T> parentNode, T data) {
        this.parentNode = parentNode;
        this.data = data;
    }

    Node<T> getParentNode() {
        return parentNode;
    }

    T getData() {
        return data;
    }

    void add(Node<T> child) {
        children.add(child);
    }

    List<Node<T>> getChildren() {
        return children;
    }



}
