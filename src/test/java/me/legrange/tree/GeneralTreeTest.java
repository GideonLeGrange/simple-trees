package me.legrange.tree;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GeneralTreeTest {

    private static final String[] PRE_ORDER_DEPTH = {
            "ROOT","A","1","2","3","B","4","5","6","C","7","8","9"
    };

    private static final String[] POST_ORDER_DEPTH = {
            "1","2","3","A","4","5","6","B","7","8","9","C","ROOT"
    };


    private static final String[] BREADTH = {
            "ROOT", "A", "B", "C", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    };

    private static GeneralTree<String> symmetric;
    private static GeneralTree<String> asymmetric;

    @BeforeAll
    static void setup() {
        symmetric = new GeneralTree("ROOT");
        symmetric.add("A");
        symmetric.add("B");
        symmetric.add("C");
        symmetric.add("A", "1");
        symmetric.add("A", "2");
        symmetric.add("A", "3");
        symmetric.add("B", "4");
        symmetric.add("B", "5");
        symmetric.add("B", "6");
        symmetric.add("C", "7");
        symmetric.add("C", "8");
        symmetric.add("C", "9");

        asymmetric = new GeneralTree("ROOT");
        asymmetric.add("A");
        asymmetric.add("B");
        asymmetric.add("C");
        asymmetric.add("A", "1");
        asymmetric.add("A", "2");
        asymmetric.add("A", "3");
        asymmetric.add("B", "4");
        asymmetric.add("B", "5");
        asymmetric.add("C", "6");
        asymmetric.add("3", "a");
        asymmetric.add("a", "!");
    }

    @Test
    void preOrderDepthStream() {
        List<String> have = symmetric.preOrderDepthStream().collect(Collectors.toList());
        List<String> want = Arrays.asList(PRE_ORDER_DEPTH);
        assertArrayEquals(want.toArray(), have.toArray(), "Pre-order depth first must match");
    }

    @Test
    void postOrderDepthStream() {
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
        assertEquals(3, depth, "Depth must match");
    }

    @Test
    void asymmetricDepth() {
        int depth = asymmetric.getDepth();
        assertEquals(5, depth, "Depth must match");
    }

    @Test
    void width() {
        int width = symmetric.getWidth();
        assertEquals(9, width, "Width must match");
    }

    @Test
    void asymmetricWidth() {
        int width = asymmetric.getWidth();
        assertEquals(6, width, "Width must match");
    }

    @Test
    void contains() {
        assertEquals(true, symmetric.contains("A"), "Must contain A");
        assertEquals(true, symmetric.contains("1"), "Must contain 1");
        assertEquals(false, symmetric.contains("z"), "Must not contain z");
        assertEquals(false, symmetric.contains("0"), "Must not contain 0");
    }

    @Test
    void correctRootParent() {
        assertEquals(true, symmetric.getParent("A").isPresent(), "Have parent for A");
        assertEquals(true, symmetric.getParent("A").get().equals("ROOT"), "Parent for A is ROOT");
    }

    @Test
    void correctParent() {
        assertEquals(true, symmetric.getParent("9").isPresent(), "Have parent for 9");
        assertEquals(true, symmetric.getParent("9").get().equals("C"), "Parent for 9 is C");
    }
}