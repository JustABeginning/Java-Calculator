package mypkg;

class ChrNode {
    private char ch;
    private ChrNode next;

    public ChrNode(char c) {
        ch = c;
        next = null;
    }

    public char getVal() {
        return ch;
    }

    public void setNext(ChrNode nxt) {
        next = nxt;
    }

    public ChrNode getNext() {
        return next;
    }
}
