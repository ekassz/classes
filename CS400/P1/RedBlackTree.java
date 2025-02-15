import java.util.LinkedList;
import java.util.Stack;

/**
 * Red-Black Tree implementation with a Node inner class for representing
 * the nodes of the tree. Currently, this implements a Binary Search Tree that
 * we will turn into a red black tree by modifying the insert functionality.
 * In this activity, we will start with implementing rotations for the binary
 * search tree insert algorithm.
 */
public class RedBlackTree<T extends Comparable<T>> implements SortedCollectionInterface<T> {

    /**
     * This class represents a node holding a single value within a binary tree.
     */
    protected static class Node<T> {
        public T data;
        // The context array stores the context of the node in the tree:
        // - context[0] is the parent reference of the node,
        // - context[1] is the left child reference of the node,
        // - context[2] is the right child reference of the node.
        // The @SupressWarning("unchecked") annotation is used to supress an unchecked
        // cast warning. Java only allows us to instantiate arrays without generic
        // type parameters, so we use this cast here to avoid future casts of the
        // node type's data field.
        @SuppressWarnings("unchecked")
        public Node<T>[] context = (Node<T>[])new Node[3];
        public Node(T data) { this.data = data; }
        
        /**
         * @return true when this node has a parent and is the right child of
         * that parent, otherwise return false
         */
        public boolean isRightChild() {
            return context[0] != null && context[0].context[2] == this;
        }

    }

    protected Node<T> root; // reference to root node of tree, null when empty
    protected int size = 0; // the number of values in the tree

    /**
     * Performs a naive insertion into a binary search tree: adding the input
     * data value to a new node in a leaf position within the tree. After  
     * this insertion, no attempt is made to restructure or balance the tree.
     * This tree will not hold null references, nor duplicate data values.
     * @param data to be added into this binary search tree
     * @return true if the value was inserted, false if not
     * @throws NullPointerException when the provided data argument is null
     * @throws IllegalArgumentException when data is already contained in the tree
     */
    public boolean insert(T data) throws NullPointerException, IllegalArgumentException {
        // null references cannot be stored within this tree
        if(data == null) throw new NullPointerException(
                "This RedBlackTree cannot store null references.");

        Node<T> newNode = new Node<>(data);
        if (this.root == null) {
            // add first node to an empty tree
            root = newNode; size++; return true;
        } else {
            // insert into subtree
            Node<T> current = this.root;
            while (true) {
                int compare = newNode.data.compareTo(current.data);
                if (compare == 0) {
                    throw new IllegalArgumentException("This RedBlackTree already contains value " + data.toString());
                } else if (compare < 0) {
                    // insert in left subtree
                    if (current.context[1] == null) {
                        // empty space to insert into
                        current.context[1] = newNode;
                        newNode.context[0] = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.context[1];
                    }
                } else {
                    // insert in right subtree
                    if (current.context[2] == null) {
                        // empty space to insert into
                        current.context[2] = newNode;
                        newNode.context[0] = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.context[2]; 
                    }
                }
            }
        }
    }

    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a
     * right child of the provided parent, this method will perform a left rotation.
     * When the provided nodes are not related in one of these ways, this method
     * will throw an IllegalArgumentException.
     * @param child is the node being rotated from child to parent position
     *      (between these two node arguments)
     * @param parent is the node being rotated from parent to child position
     *      (between these two node arguments)
     * @throws IllegalArgumentException when the provided child and parent
     *      node references are not initially (pre-rotation) related that way
     */
    @SuppressWarnings("unused")
    private void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {
        // TODO: Implement this method.
      if(child == null || parent == null) {
        throw new IllegalArgumentException("Child or parent is null");
      }
      //context[0] is now - parent
      //context[1] - left child
      //context[2] - right child
      //child - reference to either left/right

      if(parent.context[1] == child) {
        //want to rotate right
        parent.context[1] = child.context[2];
        if(child.context[2] != null) {
          child.context[2].context[0] = parent;
        }
        child.context[2] = parent;
        //update childs parent
        child.context[0] = parent.context[0];
        
        //parent.context[0] = child;
        
        //if og parent had a parent- update child link
        if(parent.context[0] == null) {
          this.root = child;
        }else if(parent == parent.context[0].context[1]) {
            parent.context[0].context[1] = child;
          }else {
            parent.context[0].context[2] = child;
          }
  
        parent.context[0] = child;
      }else if(parent.context[2] == child) {
        //want to rotate left
        parent.context[2] = child.context[1];
        if(child.context[1] != null) {
          child.context[1].context[0] = parent;
              
        }
        child.context[1] = parent;
     // Update the childs parent.
        child.context[0] = parent.context[0]; 
     //The parent becomes the left child of the 'child', so its parent becomes the child._.
        parent.context[0] = child; 
        //doing if - If the original parent had a parent, update its child link.
        if(child.context[0] != null) { 
            if(parent == child.context[0].context[2]) {
                child.context[0].context[2] = child;
            }else {
                child.context[0].context[1] = child;
            }
        }
      }else {
        //nodes aren't valid
        throw new IllegalArgumentException("Nodes aren't valid for parent/child");
      }
    }

    /**
     * Get the size of the tree (its number of nodes).
     * @return the number of nodes in the tree
     */
    public int size() {
        return size;
    }

    /**
     * Method to check if the tree is empty (does not contain any node).
     * @return true of this.size() return 0, false if this.size() > 0
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Removes the value data from the tree if the tree contains the value.
     * This method will not attempt to rebalance the tree after the removal and
     * should be updated once the tree uses Red-Black Tree insertion.
     * @return true if the value was remove, false if it didn't exist
     * @throws NullPointerException when the provided data argument is null
     * @throws IllegalArgumentException when data is not stored in the tree
     */
    public boolean remove(T data) throws NullPointerException, IllegalArgumentException {
        // null references will not be stored within this tree
        if (data == null) {
            throw new NullPointerException("This RedBlackTree cannot store null references.");
        } else {
            Node<T> nodeWithData = this.findNodeWithData(data);
            // throw exception if node with data does not exist
            if (nodeWithData == null) {
                throw new IllegalArgumentException("The following value is not in the tree and cannot be deleted: " + data.toString());
            }  
            boolean hasRightChild = (nodeWithData.context[2] != null);
            boolean hasLeftChild = (nodeWithData.context[1] != null);
            if (hasRightChild && hasLeftChild) {
                // has 2 children
                Node<T> successorNode = this.findMinOfRightSubtree(nodeWithData);
                // replace value of node with value of successor node
                nodeWithData.data = successorNode.data;
                // remove successor node
                if (successorNode.context[2] == null) {
                    // successor has no children, replace with null
                    this.replaceNode(successorNode, null);
                } else {
                    // successor has a right child, replace successor with its child
                    this.replaceNode(successorNode, successorNode.context[2]);
                }
            } else if (hasRightChild) {
                // only right child, replace with right child
                this.replaceNode(nodeWithData, nodeWithData.context[2]);
            } else if (hasLeftChild) {
                // only left child, replace with left child
                this.replaceNode(nodeWithData, nodeWithData.context[1]);
            } else {
                // no children, replace node with a null node
                this.replaceNode(nodeWithData, null);
            }
            this.size--;
            return true;
        } 
    }

    /**
     * Checks whether the tree contains the value *data*.
     * @param data the data value to test for
     * @return true if *data* is in the tree, false if it is not in the tree
     */
    public boolean contains(T data) {
        // null references will not be stored within this tree
        if (data == null) {
            throw new NullPointerException("This RedBlackTree cannot store null references.");
        } else {
            Node<T> nodeWithData = this.findNodeWithData(data);
            // return false if the node is null, true otherwise
            return (nodeWithData != null);
        }
    }

    /**
     * Helper method that will replace a node with a replacement node. The replacement
     * node may be null to remove the node from the tree.
     * @param nodeToReplace the node to replace
     * @param replacementNode the replacement for the node (may be null)
     */
    protected void replaceNode(Node<T> nodeToReplace, Node<T> replacementNode) {
        if (nodeToReplace == null) {
            throw new NullPointerException("Cannot replace null node.");
        }
        if (nodeToReplace.context[0] == null) {
            // we are replacing the root
            if (replacementNode != null)
                replacementNode.context[0] = null;
            this.root = replacementNode;
        } else {
            // set the parent of the replacement node
            if (replacementNode != null)
                replacementNode.context[0] = nodeToReplace.context[0];
            // do we have to attach a new left or right child to our parent?
            if (nodeToReplace.isRightChild()) {
                nodeToReplace.context[0].context[2] = replacementNode;
            } else {
                nodeToReplace.context[0].context[1] = replacementNode;
            }
        }
    }

    /**
     * Helper method that will return the inorder successor of a node with two children.
     * @param node the node to find the successor for
     * @return the node that is the inorder successor of node
     */
    protected Node<T> findMinOfRightSubtree(Node<T> node) {
        if (node.context[1] == null && node.context[2] == null) {
            throw new IllegalArgumentException("Node must have two children");
        }
        // take a steop to the right
        Node<T> current = node.context[2];
        while (true) {
            // then go left as often as possible to find the successor
            if (current.context[1] == null) {
                // we found the successor
                return current;
            } else {
                current = current.context[1];
            }
        }
    }

    /**
     * Helper method that will return the node in the tree that contains a specific
     * value. Returns null if there is no node that contains the value.
     * @return the node that contains the data, or null of no such node exists
     */
    protected Node<T> findNodeWithData(T data) {
        Node<T> current = this.root;
        while (current != null) {
            int compare = data.compareTo(current.data);
            if (compare == 0) {
                // we found our value
                return current;
            } else if (compare < 0) {
                // keep looking in the left subtree
                current = current.context[1];
            } else {
                // keep looking in the right subtree
                current = current.context[2];
            }
        }
        // we're at a null node and did not find data, so it's not in the tree
        return null; 
    }

    /**
     * This method performs an inorder traversal of the tree. The string 
     * representations of each data value within this tree are assembled into a
     * comma separated string within brackets (similar to many implementations 
     * of java.util.Collection, like java.util.ArrayList, LinkedList, etc).
     * @return string containing the ordered values of this tree (in-order traversal)
     */
    public String toInOrderString() {
        // generate a string of all values of the tree in (ordered) in-order
        // traversal sequence
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        if (this.root != null) {
            Stack<Node<T>> nodeStack = new Stack<>();
            Node<T> current = this.root;
            while (!nodeStack.isEmpty() || current != null) {
                if (current == null) {
                    Node<T> popped = nodeStack.pop();
                    sb.append(popped.data.toString());
                    if(!nodeStack.isEmpty() || popped.context[2] != null) sb.append(", ");
                    current = popped.context[2];
                } else {
                    nodeStack.add(current);
                    current = current.context[1];
                }
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    /**
     * This method performs a level order traversal of the tree. The string
     * representations of each data value
     * within this tree are assembled into a comma separated string within
     * brackets (similar to many implementations of java.util.Collection).
     * This method will be helpful as a helper for the debugging and testing
     * of your rotation implementation.
     * @return string containing the values of this tree in level order
     */
    public String toLevelOrderString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        if (this.root != null) {
            LinkedList<Node<T>> q = new LinkedList<>();
            q.add(this.root);
            while(!q.isEmpty()) {
                Node<T> next = q.removeFirst();
                if(next.context[1] != null) q.add(next.context[1]);
                if(next.context[2] != null) q.add(next.context[2]);
                sb.append(next.data.toString());
                if(!q.isEmpty()) sb.append(", ");
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    public String toString() {
        return "level order: " + this.toLevelOrderString() +
                "\nin order: " + this.toInOrderString();
    }

    
    // Implement at least 3 boolean test methods by using the method signatures below,
    // removing the comments around them and addind your testing code to them. You can
    // use your notes from lecture for ideas on concrete examples of rotation to test for.
    // Make sure to include rotations within and at the root of a tree in your test cases.
    // Give each of the methods a meaningful header comment that describes what is being
    // tested and make sure your test hafe inline comments to help developers read through them.
    // If you are adding additional tests, then name the method similar to the ones given below.
    // Eg: public static boolean test4() {}
    // Do not change the method name or return type of the existing tests.
    // You can run your tests by commenting in the calls to the test methods 

    /**
     * Going to be testing the right rotation at the root of BST
     * small bst - root, leftChild, rightChild, leftGran
     * This should test out the rotation at LeftChild 
     * new BST should look like:
     * parent of leftChild -> null
     * leftchild -> root
     * LeftGran -> leftChild
     * rightChild -> parent
     * @return true - if everything works else false if it doesn't pass
     */
    public static boolean test1() {
      // creating the small BST first
      // parent = 4
      // leftChild = 2
      // leftGran = 1
      
      //create to be able to call rotate!!
      RedBlackTree<Integer> tree = new RedBlackTree<>();
      
      Node<Integer> root = new Node<>(4);
      Node<Integer> leftChild = new Node<>(2);
      Node<Integer> leftGrandChild = new Node<>(1);
      
      //context[0] - parent
      //context[1] - left child
      //context[2] - right child
      
      root.context[1] = leftChild;
      leftChild.context[1] = leftGrandChild;
      leftChild.context[0] = root;
      leftGrandChild.context[0] = leftChild;
      
      //rotating right
      tree.rotate(leftChild, root);
      
      //now we check if its successful
      //1. leftChild = root
      //2. root == rightgranchild of leftchild
      //3. leftGrandChild == left child of leftChild
      return leftChild.context[0] == null && root.context[0] == leftChild && leftChild.context[2] == root
          && leftChild.context[1] == leftGrandChild;
    }
    
    /**
     * Testing left rotation at the root
     * Gonna make a BST of 2 as parent with childs 1 and 3 
     * and then test if a left rotation starting at 3 works
     * @return
     */
    public static boolean test2() {
      //Parent = 2
      //LeftChild = 1
      //Right Child = 3
      RedBlackTree<Integer> tree = new RedBlackTree<>();
      
      Node<Integer> root = new Node<>(1);
      Node<Integer> rightChild = new Node<>(2);
      Node<Integer> rightGrandChild = new Node<>(3);
      //Node<Integer> leftGrandChild = new Node<>(null);
      
      //context[0] - parent
      //context[1] - left child
      //context[2] - right child
      
      root.context[2] = rightChild;
      rightChild.context[2] = rightGrandChild;
      rightChild.context[0] = root;
      rightGrandChild.context[0] = rightChild;
      
      
      //rotating left
      tree.rotate(rightChild, root);
      
     
      return rightChild.context[0] == null && root.context[0] == rightChild 
          && rightChild.context[1] == root && rightChild.context[2] == rightGrandChild;
    }
    
    /**
     * Simple test to catch the null values on a child and parent node
     * If both pass then we return true
     * 
     * @return true if they do pass null
     */
    public static boolean test3() {
      RedBlackTree<Integer> bst = new RedBlackTree<>();
      
      Node<Integer> nonNullNode = new Node<>(1);

      try {
          // null child
          bst.rotate(null, nonNullNode);
          return false;
      } catch (IllegalArgumentException e) {
      }

      try {
        //nuull parent
          bst.rotate(nonNullNode, null);
          return false;
      } catch (IllegalArgumentException e) {
      }

      return true;
    }
    
    /**
     * Lets test the insertion of a rotation
     * 10,20,30,15
     * will test to see if the toInOrderString & toLevelOrderString match
     * @return true if both toInOrder and toLevelOrder is correct when inserting
     */
    public static boolean test4() {
      RedBlackTree<Integer> bst = new RedBlackTree<>();
      bst.insert(10);
      bst.insert(20);
      bst.insert(30);
      bst.insert(15);
      
      // sorted BST
      String inOrder = bst.toInOrderString();
      //left to right , level by level
      String levelOrder = bst.toLevelOrderString();
      boolean correctInOrder = "[ 10, 15, 20, 30 ]".equals(inOrder);
      boolean correctLevelOrder = "[ 10, 20, 15, 30 ]".equals(levelOrder);
      
      //return true if both are correct pls
      return correctInOrder && correctLevelOrder;
      
    }
    /**
     * Main method to run tests. Comment out the lines for each test
     * to run them.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Test 1 passed: " + test1());
        System.out.println("Test 2 passed: " + test2());
        System.out.println("Test 3 passed: " + test3());
        System.out.println("Test 4 passed: " + test4());

        
    }

}
