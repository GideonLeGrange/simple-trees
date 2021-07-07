package me.legrange;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BinaryTreeTest {

    private static final String[] DEPTH = {
            "ROOT", "A", "1", "a", "b", "2", "c", "d", "B", "3", "e","f", "4", "g","h"
    };
    private static final String[] BREADTH = {
            "ROOT", "A", "B", "1", "2", "3", "4", "a","b","c","d","e","f","g","h"
    };

    private static BinaryTree<String> symmetric;

    @BeforeAll
    static void setup() {
        symmetric = new BinaryTree("ROOT");
        symmetric.addLeft("A");
        symmetric.addRight("B");
        symmetric.addLeft("A", "1");
        symmetric.addRight("A", "2");
        symmetric.addLeft("B", "3");
        symmetric.addRight("B", "4");

        symmetric.addLeft("1", "a");
        symmetric.addRight("1", "b");
        symmetric.addLeft("2", "c");
        symmetric.addRight("2", "d");
        symmetric.addLeft("3", "e");
        symmetric.addRight("3", "f");
        symmetric.addLeft("4", "g");
        symmetric.addRight("4", "h");

    }

    @Test
    void depthStream() {
        List<String> have = symmetric.depthStream().collect(Collectors.toList());
        List<String> want = Arrays.asList(DEPTH);
        assertArrayEquals(want.toArray(), have.toArray(), "Depth first must match");
    }

    @Test
    void breadthStream() {
        List<String> have = symmetric.breadthStream().collect(Collectors.toList());
        List<String> want = Arrays.asList(BREADTH);
        assertArrayEquals(want.toArray(), have.toArray(), "Breadth first must match");
    }

    @Test
    void depth() {
        int depth = symmetric.getDepth();
        assertEquals(4, depth, "Depth must match");
    }


    @Test
    void width() {
        int width = symmetric.getWidth();
        assertEquals(8, width, "Width must match");
    }

}