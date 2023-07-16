package mypkg;

public class ChrStack {
    private ChrNode top;

    public ChrStack(char c) {
        top = new ChrNode(c);
    }

    public void push(char c) {
        ChrNode nptr = new ChrNode(c);
        nptr.setNext(top);
        top = nptr;
    }

    public char getTop() {
        return top.getVal();
    }

    public void pop() {
        top = top.getNext();
    }

    public boolean isEmpty() {
        return top == null;
    }
}
