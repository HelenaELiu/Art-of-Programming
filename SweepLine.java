import java.io.*;
import java.util.*;

// O(nlogn)
public class SweepLine {
    public static class Event implements Comparable<Event> {
        int time;
        SegmentHV segment;

        public Event(int time, SegmentHV segment) {
            this.time    = time;
            this.segment = segment;
        }

        @Override
        public int compareTo(Event that) {
            if      (this.time < that.time) return -1;
            else if (this.time > that.time) return +1;
            else                            return  0; 
        }
    }
    
    public static List<Pair> sweep(SegmentHV[] segments, int N)  {
        int INFINITY = Integer.MAX_VALUE; 
        PriorityQueue<Event> pq = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            if (segments[i].isVertical()) {
                Event e = new Event(segments[i].x1, segments[i]);
                pq.add(e);
            } else if (segments[i].isHorizontal()) {
                Event e1 = new Event(segments[i].x1, segments[i]);
                Event e2 = new Event(segments[i].x2, segments[i]);
                pq.add(e1);
                pq.add(e2);
            }
        }

        RangeSearch<SegmentHV> st = new RangeSearch<>();
        List<Pair> intersectedPairs = new ArrayList<>();

        while (!pq.isEmpty()) {
            Event e = pq.poll();
            int sweep = e.time;
            SegmentHV segment = e.segment;
           
            if (segment.isVertical()) {
                // use infinity to handle degenerate intersections
                SegmentHV seg1 = new SegmentHV(-INFINITY, segment.y1, -INFINITY, segment.y1);
                SegmentHV seg2 = new SegmentHV(+INFINITY, segment.y2, +INFINITY, segment.y2);
                Iterable<SegmentHV> list = st.range(seg1, seg2);
                for (SegmentHV seg : list)
                    intersectedPairs.add(new Pair(segment, seg));
            } else if (sweep == segment.x1) { // next event is left endpoint of horizontal h-v segment
                st.add(segment);
            } else if (sweep == segment.x2) { // next event is right endpoint of horizontal h-v segment
                st.remove(segment);
            }
        }
        
        return intersectedPairs;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("4.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("4.out")));
        int N = Integer.parseInt(f.readLine());
        
        StringTokenizer token;
        int x1, y1, x2, y2;
        SegmentHV[] segments = new SegmentHV[N];
        for (int i = 0; i < N; i++) {
            token = new StringTokenizer(f.readLine());
            x1 = Integer.parseInt(token.nextToken());
            y1 = Integer.parseInt(token.nextToken());
            x2 = Integer.parseInt(token.nextToken());
            y2 = Integer.parseInt(token.nextToken());
            segments[i] = new SegmentHV(x1, y1, x2, y2);
            //System.out.println(segments[i]);
        }
        
        List<Pair> intersectedPairs = sweep(segments, N);

        for (Pair p : intersectedPairs) {
            out.println("Intersection:  " + p.seg1);
            out.println("               " + p.seg2);
        }

        out.close();
    }
}

class SegmentHV implements Comparable<SegmentHV> {
    public final int x1, y1;  // lower or left
    public final int x2, y2;  // upper or right

    // pre-condition: x1 <= x2, y1 <= y2 and either x1 == x2 or y1 == y2
    public SegmentHV(int x1, int y1, int x2, int y2) {
        if (x1 >  x2 || y1 >  y2) throw new RuntimeException("Illegal hv-segment");
        if (x1 != x2 && y1 != y2) throw new RuntimeException("Illegal hv-segment");
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public boolean isHorizontal() { return (y1 == y2); }
    public boolean isVertical()   { return (x1 == x2); }

    // compare on y1 coordinate; break ties with other coordinates
    @Override
    public int compareTo(SegmentHV that) {
        if      (this.y1 < that.y1) return -1;
        else if (this.y1 > that.y1) return +1;
        else if (this.y2 < that.y2) return -1;
        else if (this.y2 > that.y2) return +1;
        else if (this.x1 < that.x1) return -1;
        else if (this.x1 > that.x1) return +1;
        else if (this.x2 < that.x2) return -1;
        else if (this.x2 > that.x2) return +1;
        return 0;
    }
        
    @Override
    public String toString() {
        String s = "";
        if      (isHorizontal()) s = "horizontal: ";
        else if (isVertical())   s = "vertical:   ";
        return s + "(" + x1 + ", " + y1 + ") -> (" + x2 + ", " + y2 + ")";
    }
}

class RangeSearch<Key extends Comparable<Key>>  {
    private Node root;      // root of the BST

    private class Node {
        Key key;            // key
        Node left, right;   // left and right subtrees
        int N;              // node count of descendents

        public Node(Key key) {
            this.key = key;
            this.N   = 1;
        }
    }

    public boolean contains(Key key) {
        return contains(root, key);
    }

    private boolean contains(Node x, Key key) {
        if (x == null) return false;
        int cmp = key.compareTo(x.key);
        if      (cmp == 0) return true;
        else if (cmp  < 0) return contains(x.left,  key);
        else               return contains(x.right, key);
    }

    public void add(Key key) {
        root = add(root, key);
    }

    // make new node the root with uniform probability
    private Node add(Node x, Key key) {
        if (x == null) return new Node(key);
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (bernoulli(1.0 / (size(x) + 1.0))) return addRoot(x, key);
        if (cmp < 0) x.left  = add(x.left,  key); 
        else         x.right = add(x.right, key); 
        fix(x);
        return x;
    }

    private Node addRoot(Node x, Key key) {
        if (x == null) return new Node(key);
        int cmp = key.compareTo(x.key);
        if      (cmp == 0) { return x; }
        else if (cmp  < 0) { x.left  = addRoot(x.left,  key); x = rotR(x); }
        else               { x.right = addRoot(x.right, key); x = rotL(x); }
        return x;
    }

    private boolean bernoulli(double p) {
        if (!(p >= 0.0 && p <= 1.0))
            throw new IllegalArgumentException("probability p must be between 0.0 and 1.0: " + p);

        long seed = System.currentTimeMillis();
        Random random = new Random(seed);

        return random.nextDouble() < p;
    }
    
    public void remove(Key key) {
        root = remove(root, key);
    }
    
    private Node joinLR(Node a, Node b) { 
        if (a == null) return b;
        if (b == null) return a;

        if (bernoulli((double) size(a) / (size(a) + size(b))))  {
            a.right = joinLR(a.right, b);
            fix(a);
            return a;
        } else {
            b.left = joinLR(a, b.left);
            fix(b);
            return b;
        }
    }

    private Node remove(Node x, Key key) {
        if (x == null) return null; 
        int cmp = key.compareTo(x.key);
        if      (cmp == 0) x = joinLR(x.left, x.right);
        else if (cmp  < 0) x.left  = remove(x.left,  key);
        else               x.right = remove(x.right, key);
        fix(x);
        return x;
    }

    // return all keys between k1 and k2
    public Iterable<Key> range(Key k1, Key k2) {
        Queue<Key> list = new LinkedList<Key>();
        if (less(k2, k1)) return list;
        range(root, k1, k2, list);
        return list;
    }
    private void range(Node x, Key k1, Key k2, Queue<Key> list) {
        if (x == null) return;
        if (lte(k1, x.key))  range(x.left, k1, k2, list);
        if (lte(k1, x.key) && lte(x.key, k2)) list.add(x.key);
        if (lte(x.key, k2)) range(x.right, k1, k2, list);
    }

    // return number of nodes in subtree rooted at x
    public int size() { return size(root); }
    private int size(Node x) { 
        if (x == null) return 0;
        else           return x.N;
    }

    // recount subtree size
    private void fix(Node x) {
        if (x == null) return;
        x.N = 1 + size(x.left) + size(x.right);
    }

    private Node rotR(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        fix(h);
        fix(x);
        return x;
    }

    private Node rotL(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        fix(h);
        fix(x);
        return x;
    }

    private boolean less(Key k1, Key k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean lte(Key k1, Key k2) {
        return k1.compareTo(k2) <= 0;
    }
}

class Pair {
    SegmentHV seg1, seg2;
    
    public Pair(SegmentHV seg1, SegmentHV seg2) {
        this.seg1 = seg1;
        this.seg2 = seg2;
    }
}
