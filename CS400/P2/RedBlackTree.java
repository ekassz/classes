import java.util.LinkedList;
import java.util.Stack;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.Test;

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
        public int blackHeight; //2
        public Node(T data) { 
          this.data = data;
          this.blackHeight = 0; //default node = red
        }
        
        /**
         * @return true when this node has a parent and is the right child of
         * that parent, otherwise return false
         */
        public boolean isRightChild() {
            return context[0] != null && context[0].context[2] == this;
        }
        
        /**
         * another helper method for checking color
         * checking if node is red
         */
        public boolean isRed() {
          return blackHeight == 0;
        }
        
        /**
         * anotherrr helper for checking color
         * this time, if node is black!
         */
        public boolean isBlack() {
          return blackHeight == 1; 
      }

    }

    
    protected Node<T> root; // reference to root node of tree, null when empty
    protected int size = 0; // the number of values in the tree

    // 3. Add a new protected method RedBlackTree
    /**
     * 
     * We're going to be doing a red black tree performance here, so this method 
     * essentially is the build of RBT Propertie rules.
     * We also will fix if anything is wrong in the tree and make sure it follows
     * those properties.
     * Root ALWAYS will be 1, while every insert is ALWAYS 0
     * 
     * @param node - newly inserted node in the tree
     * @return root node of the tree after RBT properties run
     */      
    protected Node<T> enforceRBTreePropertiesAfterInsert(Node<T> node) {
      //0 = red 
      //1 = black
      while (node != root && node.context[0].blackHeight == 0) {
          Node<T> grandp = grandparent(node);
          //added this because it kept breaking on a test saying that it was
          //failing because grandp was null??
          if (grandp == null) {
              break;
          }
          //for reference
          Node<T> parent = parent(node);
          //will make it easier to call later
          Node<T> uncle = parent.isRightChild() ? grandp.context[1] : grandp.context[2];

          
          if (parent.isRed() && (uncle == null || uncle.isBlack())) {
              // WHAT IF - Uncle is black or null, and parent is red
              if (node.isRightChild() && parent.isRightChild()) {
                  // 1. Node and parent are right children
                  rotate(parent, grandp);
                  swapColors(parent, grandp);
              } else if (!node.isRightChild() && !parent.isRightChild()) {
                  // 2. Node and parent are left children
                  rotate(parent, grandp);
                  swapColors(parent, grandp);
              } else if (node.isRightChild() && !parent.isRightChild()) {
                  // 3. Node is right child and parent is left child
                  rotate(node, parent);
                  rotate(node, grandp);
                  swapColors(node, grandp);
              } else {
                  //4. Node is left child and parent is right child
                  rotate(node, parent);
                  rotate(node, grandp);
                  swapColors(node, grandp);
              }
          } else {
              //WHAT IF - Uncle is red or null, and parent is black
              parent.blackHeight = 1;
              if (uncle != null) {
                  uncle.blackHeight = 1;
              }
              grandp.blackHeight = 0;
          }

          node = parent;
          parent = parent(node);
      }

      root.blackHeight = 1; // root is ALWAYS black!
      return root;
  }

    /**
     * ANOTHER helper method :D 
     * Did this to be able to distinguish when we are switching red or black colors
     * on nodes 
     * Did this specifically so that I could catch the test on the black uncle
     * opposite child side tree ( faulty test with wrong colored nodes)
     * @param node1
     * @param node2
     */
    private void swapColors(Node<T> node1, Node<T> node2) {
      int tempColor = node1.blackHeight;
      node1.blackHeight = node2.blackHeight;
      node2.blackHeight = tempColor;
  }

    /**
     * building helper methods to be able to call more easily in enforceRBTreePropertiesAfterInser
     * parent
     * @param node
     * @return parent
     */
    private Node<T> parent(Node<T> node){
      return node.context[0];
    }
    
    /**
     * parent's parent = grandparent! 
     * helper method to make it more simple when calling from rbt
     * @param node
     * @return grandparent 
     */
    private Node<T> grandparent(Node<T> node){
      Node<T> parentNode = parent(node);
      if(parentNode == null) {
        //node is the root
        return null;
      }
      return parent(parentNode);
    }
    
    /**
     * uncle - if there is node grandparent
     * helper method to make things again, more simple
     * @param node
     * @return uncle
     */
    @SuppressWarnings("unused")
    private Node<T> uncle(Node<T> node){
      Node<T> grandparentNode = grandparent(node);
      if(grandparentNode == null) {
        return null;
      }
      if(parent(node) == grandparentNode.context[1]) {
        return grandparentNode.context[2];
      }
      else {
        return grandparentNode.context[1];
      }
    }
    
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
                        enforceRBTreePropertiesAfterInsert(newNode); //calling 4.
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
                        enforceRBTreePropertiesAfterInsert(newNode); //calling 4.
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

   // Check if child is a left child or right child, for more simplicity
      boolean isRightChild = (parent.context[2] == child);
      
      if (isRightChild) {
          // Right rotation
          parent.context[2] = child.context[1]; 
          // Set the left child of the node to be the right child of the parent
          if (child.context[1] != null) {
              child.context[1].context[0] = parent; 
              // Update parent of left child 
          }
          child.context[1] = parent; 
          // The parent becomes the left child
      } else {
          // Left rotation
          parent.context[1] = child.context[2]; 
          // Set right child of the node to be left child of parent
          if (child.context[2] != null) {
              child.context[2].context[0] = parent; 
              // Update parent of right child
          }
          child.context[2] = parent; 
          // The parent becomes the right child of the node
      }
      
      // Updating parent
      child.context[0] = parent.context[0];

      // Updating now parent parent (grandp)
      if (parent.context[0] != null) {
          if (parent == parent.context[0].context[1]) {
              parent.context[0].context[1] = child;
          } else {
              parent.context[0].context[2] = child;
          }
      }
      
     
      parent.context[0] = child;

      //Updating root only if - parent was root!
      if (root == parent) {
          root = child;
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
    /**
     * ...another helper case but for my JUnit test
     * so that it's more simple to call on when checking if the enforce RBT method
     * implements a tree correctly
     * 
     * @return
     */
    public boolean isValidRBTree() {
      return isValidRBTree(root, 0, -1);
  }

    /**
     * helper method for public helper method,
     * this is checking to make sure the nodes are the right color or not
     * @param node
     * @param blackCount
     * @param pathBlackCount
     * @return
     */
  private boolean isValidRBTree(Node<T> node, int blackCount, int pathBlackCount) {
      if (node == null) {
          // This is a leaf node (null), check the black count
          if (pathBlackCount == -1) {
              pathBlackCount = blackCount;
          }
          return pathBlackCount == blackCount;
      }

      // Increment current black count if the node is black
      if (node.blackHeight == 1) {
          blackCount++;
      }

      // Check the property: Red nodes should have black children
      if (node.blackHeight == 0) { // If the node is red
          if (node.context[1] != null && node.context[1].blackHeight == 0) {
              return false;
          }
          if (node.context[2] != null && node.context[2].blackHeight == 0) {
              return false;
          }
      }

      // Check left and right subtrees
      return isValidRBTree(node.context[1], blackCount, pathBlackCount) &&
             isValidRBTree(node.context[2], blackCount, pathBlackCount);
  }


    /**
     * JUnit test!!
     * This will test enforcement enforceRBTreePropertiesAfterInsert to see 
     * if the expected tree actually matches what the toLevelOrderString method displays
     * 
     */
    @Test
    public void testRBTreePropertyAfterInsertion() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(30);
        tree.insert(20);
        tree.insert(40);
        tree.insert(50);
        
        tree.enforceRBTreePropertiesAfterInsert(tree.root);
        assertTrue(tree.isValidRBTree());
    }

    /**
     * JUnit test!!
     * checking to see if the root is indeed always black
     */
    @Test
    public void testRootColor() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        tree.insert(15);
        tree.insert(20);
        // root always black
        assertTrue(tree.root.isBlack(), "Root node should always be black");
    }
    
    /**
     * JUnit test!
     * checking the color of the leaf nodes to see if they match or not
     */
    @Test
    public void testLeafColor() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        tree.insert(15);
        tree.insert(20);
        tree.insert(25);
        // The leaves (null nodes) should always be black
        Node<Integer> leafNode = tree.findNodeWithData(25).context[2]; 
        assertTrue(leafNode.isBlack(), "Leaf nodes should always be black");
    }
    
   

//    /**
//     * Main method to run tests. Comment out the lines for each test
//     * to run them.
//     * @param args
//     */
//    public static void main(String[] args) {
////        System.out.println("Test 1 passed: " + test1());
////        System.out.println("Test 2 passed: " + test2());
////        System.out.println("Test 3 passed: " + test3());
////       System.out.println("Test 4 passed: " + test4());  
//      
//    }

    

}
