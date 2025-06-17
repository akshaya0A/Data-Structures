public class Node {
    public int lo;
    public int hi;
    public int sum;
    public int rightSum;
    public Node leftChildNode;
    public Node rightChildNode;
  
    public Node(int lo, int hi, int sum) {
        this.lo = lo;
        this.hi = hi;
        this.sum = sum;
    }
  
    public Node(int lo, int hi, int sum, Node leftChildNode, Node rightChildNode) {
        this.lo = lo;
        this.hi = hi;
        this.sum = sum;
        this.leftChildNode = leftChildNode;
        this.rightChildNode = rightChildNode;
    }
  }