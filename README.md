# Simple Java Tree Library 

This library provides a very simple tree implementation in Java. 

## Usage 

Create a new Tree:

```java
    var tree = new Tree<String>("Root");
```

Add a data to the tree directly below the root:

```java
  tree.add("First");
  tree.add("Second");
```

Add a child data as child of another node:

```java
  tree.add("First", "Alpha");
  tree.add("First", "Bravo");
  tree.add("Second", "Charlie");
  tree.add("Second", "Delta");
```

Check if the tree contains certain data:

```java

```
