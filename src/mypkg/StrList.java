package mypkg;

public class StrList {
    private StrNode start, end;

    public StrList(String s) {
        end = start = new StrNode(s);
    }

    public void add(String s) {
        end.setNext(s);
        end = end.getNext();
    }

    public String getStart() {
        return start.getVal();
    }

    public void remove() {
        start = start.getNext();
    }

    public boolean isEmpty() {
        if (start == null) {
            end = null;
            return true;
        } else
            return false;
    }

    public void display() {
        StrNode save = start;
        while (save != null) {
            System.out.print(save.getVal() + " ");
            save = save.getNext();
        }
        System.out.println();
    }
}
