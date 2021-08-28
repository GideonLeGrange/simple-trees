package me.legrange.tree;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BinarySearchTreeTest {

    private static final Integer[] DEPTH = {
            0,-1,-10,-100,-1000,1,10,100,1000
    };
    private static final Integer[] BREADTH = {
            0,-1,1,-10,10,-100,100,-1000,1000
    };

    private static BinarySearchTree<Integer> symmetric;

    @BeforeAll
    static void setup() {
        symmetric = new BinarySearchTree(0);
        symmetric.add(1);
        symmetric.add(10);
        symmetric.add(100);
        symmetric.add(1000);
        symmetric.add(-1);
        symmetric.add(-10);
        symmetric.add(-100);
        symmetric.add(-1000);
    }

    @Test
    void breadthStream() {
        List<Integer> have = symmetric.breadthStream().collect(Collectors.toList());
        List<Integer> want = Arrays.asList(BREADTH);
        assertArrayEquals(want.toArray(), have.toArray(), "Breadth first must match");
    }

    @Test
    void depth() {
        int depth = symmetric.getDepth();
        assertEquals(5, depth, "Depth must match");
    }


    @Test
    void width() {
        int width = symmetric.getWidth();
        assertEquals(2, width, "Width must match");
    }

    @Test
    void contains() {
        assertEquals(true, symmetric.contains(0), "Must contain ");
        assertEquals(true, symmetric.contains(1), "Must contain ");
        assertEquals(true, symmetric.contains(10), "Must contain ");
        assertEquals(true, symmetric.contains(100), "Must contain ");
        assertEquals(true, symmetric.contains(1000), "Must contain ");
        assertEquals(true, symmetric.contains(-1), "Must contain ");
        assertEquals(true, symmetric.contains(-10), "Must contain");
        assertEquals(true, symmetric.contains(-100), "Must contain ");
        assertEquals(true, symmetric.contains(-1000), "Must contain ");
        assertEquals(false, symmetric.contains(200), "Must noy contain ");
    }

    @Test
    void find() {
        assertEquals(0, symmetric.find(0), "Must match ");
        assertEquals(1, symmetric.find(1), "Must match ");
        assertEquals(-1000, symmetric.find(-1000), "Must match ");
        assertEquals(0, symmetric.find(0), "Must match ");
        assertEquals(1, symmetric.find(1), "Must match ");
        assertEquals(-100, symmetric.find(-50), "Must match ");
        assertEquals(10, symmetric.find(5), "Must match ");
    }

    @Test
    void getParent() {
        Optional<Integer> p1 = symmetric.getParent(1);
        assertTrue(p1.isPresent());
        assertEquals(p1.get(), 0);
    }

}