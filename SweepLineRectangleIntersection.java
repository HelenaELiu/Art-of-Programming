import java.io.*;
import java.util.*;

public class SweepLineRectangleIntersection {
    public static void main(String[] args) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("rectangle.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("rectangle.out")));
        StringTokenizer token = new StringTokenizer(f.readLine());
        int N = Integer.parseInt(token.nextToken());
        int K = Integer.parseInt(token.nextToken());

        int x1, y1, x2, y2;
        Interval2D[] rects = new Interval2D[N];
        for (int i = 0; i < N; i++) {
            token = new StringTokenizer(f.readLine());
            x1 = Integer.parseInt(token.nextToken());
            y1 = Integer.parseInt(token.nextToken());
            x2 = Integer.parseInt(token.nextToken());
            y2 = Integer.parseInt(token.nextToken());
            rects[i] = new Interval2D(new Interval1D(x1, x2), new Interval1D(y1, y2));
            System.out.println(rects[i]);
        }
        
        List<Pair> intersectedPairs = sweep(rects, N);

        for (Pair p : intersectedPairs) {
            out.println("Intersection:  " + p.rect1);
            out.println("               " + p.rect2);
        }

        out.close();
    }
    
    public static class Event implements Comparable<Event> {
        int time;
        Interval2D rect;

        public Event(int time, Interval2D rect) {
            this.time = time;
            this.rect = rect;
        }

        @Override
        public int compareTo(Event that) {
            if      (this.time < that.time) return -1;
            else if (this.time > that.time) return +1;
            else                            return  0; 
        }
    }

     public static List<Pair> sweep(Interval2D[] rects, int N) {
        PriorityQueue<Event> pq = new PriorityQueue<>();  
        for (int i = 0; i < N; i++) {
            Event e1 = new Event(rects[i].intervalX.min, rects[i]);
            Event e2 = new Event(rects[i].intervalX.max, rects[i]);
            pq.add(e1); 
            pq.add(e2);
        }

        IntervalST<Interval2D> st = new IntervalST<Interval2D>();
        List<Pair> intersectedPairs = new ArrayList<>();
        while (!pq.isEmpty()) {
            Event e = pq.poll();
            int time = e.time;
            Interval2D rect = e.rect;
           
            if (time == rect.intervalX.max)
                st.remove(rect.intervalY);
            else {
                for (Interval1D x : st.searchAll(rect.intervalY))
                    intersectedPairs.add(new Pair(rect, st.get(x)));

                st.put(rect.intervalY, rect);
            }
        }
        
        return intersectedPairs;
    }
}

 class Interval2D { 
    public final Interval1D intervalX;
    public final Interval1D intervalY;
   
    public Interval2D(Interval1D intervalX, Interval1D intervalY) {
        this.intervalX = intervalX;
        this.intervalY = intervalY;
    }

    public boolean intersects(Interval2D b) {
        if (!intervalX.intersects(b.intervalX)) return false;
        if (!intervalY.intersects(b.intervalY)) return false;
        return true;
    }

    public boolean contains(int x, int y) {
        return intervalX.contains(x) && intervalY.contains(y);
    }

    @Override
    public String toString() {
        //return intervalX + " - " + intervalY;
        return "[" + intervalX.min + " " + intervalY.min + "] - [" + intervalX.max + " " + intervalY.max + "]";
    }
}

class Interval1D implements Comparable<Interval1D> {
    public final int min;  // min endpoint
    public final int max;  // max endpoint

    public Interval1D(int min, int max) {
        if (min <= max) {
            this.min = min;
            this.max = max;
        }
        else throw new RuntimeException("Illegal interval");
    }

    public boolean intersects(Interval1D that) {
        if (that.max < this.min) return false;
        if (this.max < that.min) return false;
        return true;
    }

    public boolean contains(int x) {
        return (min <= x) && (x <= max);
    }

    @Override
    public int compareTo(Interval1D that) {
        if      (this.min < that.min) return -1;
        else if (this.min > that.min) return +1;
        else if (this.max < that.max) return -1;
        else if (this.max > that.max) return +1;
        else                          return  0;
    }

    @Override
    public String toString() {
        return "[" + min + ", " + max + "]";
    }
}

class IntervalST<Value>  {
    private Node root;   

    private class Node {
        Interval1D interval;      // key
        Value value;              // associated data
        Node left, right;         // left and right subtrees
        int N;                    // size of subtree rooted at this node
        int max;                  // max endpoint in subtree rooted at this node

        Node(Interval1D interval, Value value) {
            this.interval = interval;
            this.value    = value;
            this.N        = 1;
            this.max      = interval.max;
        }
    }

    public boolean contains(Interval1D interval) {
        return (get(interval) != null);
    }

    public Value get(Interval1D interval) {
        return get(root, interval);
    }

    private Value get(Node x, Interval1D interval) {
        if (x == null) return null;
        
        int cmp = interval.compareTo(x.interval);
        if (cmp < 0)      return get(x.left, interval);
        else if (cmp > 0) return get(x.right, interval);
        else              return x.value;
    }

    public void put(Interval1D interval, Value value) {
        if (contains(interval)) {
            System.out.println("duplicate");    //!! need to handle degeneration
            remove(interval);
        }
        
        root = randomizedInsert(root, interval, value);
    }

    private Node randomizedInsert(Node x, Interval1D interval, Value value) {
        if (x == null) return new Node(interval, value);
        if (Math.random() * size(x) < 1.0) return rootInsert(x, interval, value);
        int cmp = interval.compareTo(x.interval);
        if (cmp < 0)  x.left  = randomizedInsert(x.left,  interval, value);
        else          x.right = randomizedInsert(x.right, interval, value);
        fix(x);
        return x;
    }

    private Node rootInsert(Node x, Interval1D interval, Value value) {
        if (x == null) return new Node(interval, value);
        int cmp = interval.compareTo(x.interval);
        if (cmp < 0) { x.left  = rootInsert(x.left,  interval, value); x = rotR(x); }
        else         { x.right = rootInsert(x.right, interval, value); x = rotL(x); }
        return x;
    }

    private Node joinLR(Node a, Node b) { 
        if (a == null) return b;
        if (b == null) return a;

        if (Math.random() * (size(a) + size(b)) < size(a))  {
            a.right = joinLR(a.right, b);
            fix(a);
            return a;
        } else {
            b.left = joinLR(a, b.left);
            fix(b);
            return b;
        }
    }

    public Value remove(Interval1D interval) {
        Value value = get(interval);
        root = remove(root, interval);
        return value;
    }

    private Node remove(Node h, Interval1D interval) {
        if (h == null) return null;
        int cmp = interval.compareTo(h.interval);
        if      (cmp < 0) h.left  = remove(h.left,  interval);
        else if (cmp > 0) h.right = remove(h.right, interval);
        else              h = joinLR(h.left, h.right);
        fix(h);
        return h;
    }

    // return an interval in this BST that intersects the given inteval; return null if no such interval 
    public Interval1D search(Interval1D interval) {
        return search(root, interval);
    }

    // search in subtree with x as root
    public Interval1D search(Node x, Interval1D interval) {
        while (x != null) {
            if (interval.intersects(x.interval)) return x.interval;
            else if (x.left == null)             x = x.right;
            else if (x.left.max < interval.min)  x = x.right;
            else                                 x = x.left;
        }
        return null;
    }

    // return all intervals that intersect the given interval
    public Iterable<Interval1D> searchAll(Interval1D interval) {
        LinkedList<Interval1D> list = new LinkedList<>();
        searchAll(root, interval, list);
        return list;
    }

    // search in subtree with x as root; return all intervals in list
    public boolean searchAll(Node x, Interval1D interval, LinkedList<Interval1D> list) {
         boolean found1 = false;
         boolean found2 = false;
         boolean found3 = false;
         if (x == null)
            return false;
        if (interval.intersects(x.interval)) {
            list.add(x.interval);
            found1 = true;
        }
        if (x.left != null && x.left.max >= interval.min)
            found2 = searchAll(x.left, interval, list);
        if (found2 || x.left == null || x.left.max < interval.min)
            found3 = searchAll(x.right, interval, list);
        return found1 || found2 || found3;
    }
    
    public int size() {
        return size(root);
    }
    
    private int size(Node x) { 
        if (x == null) return 0;
        else return x.N;
    }

    public int height() {
        return height(root);
    }
    
    private int height(Node x) {
        if (x == null) return 0;    // emplty
        return 1 + Math.max(height(x.left), height(x.right));
    }

    private void fix(Node x) {
        if (x == null) return;
        x.N = 1 + size(x.left) + size(x.right);
        x.max = max3(x.interval.max, max(x.left), max(x.right));
    }

    private int max(Node x) {
        if (x == null) return Integer.MIN_VALUE;
        return x.max;
    }

    private int max3(int a, int b, int c) {
        return Math.max(a, Math.max(b, c));
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
}

class Pair {
    Interval2D rect1, rect2;
    
    public Pair(Interval2D rect1, Interval2D rect2) {
        this.rect1 = rect1;
        this.rect2 = rect2;
    }
}
