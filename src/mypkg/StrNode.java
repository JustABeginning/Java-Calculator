package mypkg;

class StrNode {
    private String str;
    private StrNode next;

    public StrNode(String s) {
        str = s;
        next = null;

    }

    public String getVal() {
        return str;
    }

    public void setNext(String s) {
        next = new StrNode(s);
    }

    public StrNode getNext() {
        return next;
    }
}
