package me.legrange;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class BreadthFirstIterator<T> implements Iterator<T> {

    private final Tree<T> tree;
    private Iterator<T> it;

    BreadthFirstIterator(Tree<T> tree) {
        this.tree = tree;
        T root = tree.getRoot();
        this.it = makeStream(Collections.singletonList(root)).iterator();
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public T next() {
        return it.next();
    }

    private Stream<T> makeStream(List<T> data) {
        if (data.isEmpty()) {
            return Stream.empty();
        }
        return Stream.concat(data.stream(),
                makeStream(data.stream().flatMap(object -> tree.getChildren(object).stream()).collect(Collectors.toList())));
    }
}
