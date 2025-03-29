package mypkg;

public class DblStack {
    private DblNode top;

    public DblStack(double n) {
        top = new DblNode(n);
    }

    public void push(double n) {
        DblNode nptr = new DblNode(n);
        nptr.setNext(top);
        top = nptr;
    }

    public double getTop() throws MyException {
        if (top == null)
            throw new MyException("Insufficient OPERAND(s) !");
        return top.getVal();
    }

    public void pop() {
        top = top.getNext();
    }
}
