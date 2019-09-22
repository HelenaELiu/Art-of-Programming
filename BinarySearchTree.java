public class BinarySearchTree {
    Node root;

    static class Node {
        int key, value;
        Node l, r, p;

        public Node(int key, int value, Node p) {
            this.key = key;
            this.value = value;
            this.p = p;
        }
    }

    public Node search(int key) {
        return search(root, key);
    }
    
    private Node search(Node t, int key) {
        if (t == null || t.key == key)
            return t;

        if (key < t.key)
            return search(t.l, key);
        else
            return search(t.r, key);
    }

    public void insert(int key, int value) {
        root = insert(root, null, key, value);
    }
    
    private Node insert(Node t, Node p, int key, int value) {
        if (t == null)
            t = new Node(key, value, p);
        else if (key < t.key)
            t.l = insert(t.l, t, key, value);
        else
            t.r = insert(t.r, t, key, value);

        return t;
    }

    public void remove(int key) {
        remove(root, key);
    }

    private void remove(Node t, int key) {
        if (t == null)
            return;

        if (key < t.key) {
            remove(t.l, key);
        } else if (key > t.key) {
            remove(t.r, key);
        } else if (t.l != null && t.r != null) {
            Node m = t.r;
            while (m.l != null)
                m = m.l;

            t.key = m.key;
            t.value = m.value;
            replace(m, m.r);
        } else if (t.l != null) {
            replace(t, t.l);
        } else if (t.r != null) {
            replace(t, t.r);
        } else {
            replace(t, null);
        }
    }

    private void replace(Node a, Node b) {
        if (a.p == null)
            root = b;
        else if (a == a.p.l)
            a.p.l = b;
        else
            a.p.r = b;

        if (b != null)
            b.p = a.p;
    }

    private void print(Node t) {
        if (t != null) {
            print(t.l);
            System.out.print(t.key + ":" + t.value + " ");
            print(t.r);
        }
    }

    public void print() {
        print(root);
        System.out.println();
    }

    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();
        tree.insert(3, 1);
        tree.insert(2, 2);
        tree.insert(4, 5);
        tree.print();

        Node n = tree.search(2);
        System.out.println(n.key + ":" + n.value);

        tree.remove(2);
        tree.remove(3);
        tree.print();
        tree.remove(4);
        tree.print();
    }
}