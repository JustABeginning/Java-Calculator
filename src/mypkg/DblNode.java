package mypkg;

class DblNode {
    private double num;
    private DblNode next;

    public DblNode(double n) {
        num = n;
        next = null;
    }

    public double getVal() {
        return num;
    }

    public void setNext(DblNode nxt) {
        next = nxt;
    }

    public DblNode getNext() {
        return next;
    }
}
