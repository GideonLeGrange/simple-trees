package me.legrange.tree;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BinaryTreeTest {

    private static final String[] BREADTH = {
            "ROOT", "A", "B", "1", "2", "3", "4", "a","b","c","d","e","f","g","h"
    };

    private static final String[] IN_ORDER_DEPTH = {
            "a","1","b","A","c","2","d","ROOT","e","3","f","B","g","4","h"
    };

    private static final String[] PRE_ORDER_DEPTH = {
            "ROOT", "A", "1", "a", "b", "2", "c", "d", "B", "3", "e","f", "4", "g","h"
    };

    private static final String[] POST_ORDER_DEPTH = {
            "a", "b", "1", "c","d","2","A","e","f","3","g","h","4","B","ROOT"
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
    void inOrderDepthSearch() {
        List<String> have = symmetric.inOrderDepthStream().collect(Collectors.toList());
        List<String> want = Arrays.asList(IN_ORDER_DEPTH);
        assertArrayEquals(want.toArray(), have.toArray(), "In-order depth first must match");
    }

    @Test
    void preOrderDepthSearch() {
        List<String> have = symmetric.preOrderDepthStream().collect(Collectors.toList());
        List<String> want = Arrays.asList(PRE_ORDER_DEPTH);
        assertArrayEquals(want.toArray(), have.toArray(), "Pre-order depth first must match");
    }

    @Test
    void postOrderDepthSearch() {
        List<String> have = symmetric.postOrderDepthStream().collect(Collectors.toList());
        List<String> want = Arrays.asList(POST_ORDER_DEPTH);
        assertArrayEquals(want.toArray(), have.toArray(), "Post-order depth first must match");
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

    @Test
    void correctRootParent() {
        assertEquals(true, symmetric.getParent("A").isPresent(), "Have parent for A");
        assertEquals(true, symmetric.getParent("A").get().equals("ROOT"), "Parent for A is ROOT");
    }

    @Test
    void correctParent() {
        assertEquals(true, symmetric.getParent("h").isPresent(), "Have parent for h");
        assertEquals(true, symmetric.getParent("h").get().equals("4"), "Parent for h is 4");
    }

}