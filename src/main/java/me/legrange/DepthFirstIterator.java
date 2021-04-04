package me.legrange;

import java.util.Iterator;
import java.util.stream.Stream;

class DepthFirstIterator<T> implements Iterator<T> {

    private final Tree<T> tree;
    private Iterator<T> it;

    DepthFirstIterator(Tree<T> tree) {
        this.tree = tree;
        T root = tree.getRoot();
        this.it = makeStream(root).iterator();
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public T next() {
        return it.next();
    }

    private Stream<T> makeStream(T data) {
        return Stream.concat(Stream.of(data),
                tree.getChildren(data).stream().flatMap(this::makeStream));
    }
}
