import java.util.*;

public class Treap {
    static Random random = new Random();

    static class Node {
        int key;
        long priority;
        Node left, right;
        int count;

        Node(int key) {
            this.key = key;
            priority = random.nextLong();
            count = 1;
        }

        void update() {
            count = 1 + getCount(left) + getCount(right);
        }
    }

    static int getCount(Node root) {
        return root == null ? 0 : root.count;
    }

    static class Pair {
        Node left, right;

        Pair(Node left, Node right) {
            this.left = left;
            this.right = right;
        }
    }

    static Pair split(Node root, int minRight) {
        if (root == null) return new Pair(null, null);

        if (root.key >= minRight) {
            Pair leftSplit = split(root.left, minRight);
            root.left = leftSplit.right;
            root.update();
            leftSplit.right = root;
            return leftSplit;
        } else {
            Pair rightSplit = split(root.right, minRight);
            root.right = rightSplit.left;
            root.update();
            rightSplit.left = root;
            return rightSplit;
        }
    }

    static Node merge(Node left, Node right) {
        if (left == null)  return right;
        if (right == null) return left;

        if (left.priority > right.priority) {
            left.right = merge(left.right, right);
            left.update();
            return left;
        } else {
            right.left = merge(left, right.left);
            right.update();
            return right;
        }
    }

    static Node insert(Node root, int x) {
        Pair t = split(root, x);
        return merge(merge(t.left, new Node(x)), t.right);
    }

    static Node remove(Node root, int x) {
        if (root == null) return null;

        if (x < root.key) {
            root.left = remove(root.left, x);
            root.update();
            return root;
        } else if (x > root.key) {
            root.right = remove(root.right, x);
            root.update();
            return root;
        } else {
            return merge(root.left, root.right);
        }
    }

    static int kth(Node root, int k) {
        if (k < getCount(root.left))
            return kth(root.left, k);
        else if (k > getCount(root.left))
            return kth(root.right, k - getCount(root.left) - 1);

        return root.key;
    }

    static void print(Node root) {
        if (root == null) return;

        print(root.left);
        System.out.println(root.key);
        print(root.right);
    }

    public static void main(String[] args) {
        Node node = null;
        Set<Integer> set = new TreeSet<>();
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(50);
            if (random.nextBoolean()) {
                node = remove(node, x);
                set.remove(x);
            } else if (!set.contains(x)) {
                node = insert(node, x);
                set.add(x);
            }
            
            if (set.size() != getCount(node))
                throw new RuntimeException();
        }
        print(node);
    }
}