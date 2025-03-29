import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class MyException extends Exception {
    public MyException(String m) {
        super(m);
    }
}

class List {
    String node;
    List link;

    public List(String s) {
        node = s;
        link = null;

    }
}

class Double2 {
    double num;
    Double2 link;

    public Double2(double n) {
        num = n;
        link = null;
    }
}

public class Calculator {
    private static double ans;
    private static int numPost;
    // Sorted Array(s)
    private static char[] arrFn = { 'c', 'd', 'l', 'r', 's', 't' };
    private static char[] binOp = { '%', '*', '+', '-', '/', 'C', 'P', '^', '|' };
    private static char[] allOp = { '!', '%', '*', '+', '-', '/', 'C', 'P', '^', '|' };
    private static char[] arrOp = { '!', '%', '*', '+', '-', '/', 'C', 'P', '^', 'n', 'u', '|' };
    // Tree Level
    private static int[] idOp = { 0, 4, 4, 5, 5, 4, 1, 1, 2, 3, 3, 2 };

    private static void dispList(List show) {
        while (show != null) {
            System.out.print(show.node + " ");
            show = show.link;
        }
        System.out.println();
    }

    private static boolean isFunc(char ch) {
        int f = -1, m = f, l = 0, u = arrFn.length - 1;
        while (l <= u) {
            m = (l + u) / 2;
            if (ch > arrFn[m])
                l = m + 1;
            else if (ch < arrFn[m])
                u = m - 1;
            else {
                f = m;
                break;
            }
        }
        return (f >= 0);
    }

    private static int findOp(char ch) {
        int f = -1, m = f, l = 0, u = arrOp.length - 1;
        while (l <= u) {
            m = (l + u) / 2;
            if (ch > arrOp[m])
                l = m + 1;
            else if (ch < arrOp[m])
                u = m - 1;
            else {
                f = m;
                break;
            }
        }
        return f;
    }

    private static boolean isValidOp(char ch) {
        int f = -1, m = f, l = 0, u = allOp.length - 1;
        while (l <= u) {
            m = (l + u) / 2;
            if (ch > allOp[m])
                l = m + 1;
            else if (ch < allOp[m])
                u = m - 1;
            else {
                f = m;
                break;
            }
        }
        return (f >= 0);
    }

    private static boolean isBinary(char ch) {
        int f = -1, m = f, l = 0, u = binOp.length - 1;
        while (l <= u) {
            m = (l + u) / 2;
            if (ch > binOp[m])
                l = m + 1;
            else if (ch < binOp[m])
                u = m - 1;
            else {
                f = m;
                break;
            }
        }
        return (f >= 0);
    }

    private static int isUnary(char ch, char cp) {
        int res = 3;
        if (((ch == '-' || ch == '+') && (isBinary(cp) || cp == '('))
                || (ch == '!' && (Character.isDigit(cp) || cp == ')')))
            res = 1;
        else if (isBinary(cp) && isValidOp(ch))
            res = 2;
        else
            res = 0;
        return res;
    }

    private static boolean isPrior(char ch, char cp) {
        int i = findOp(ch), j = findOp(cp);
        if (idOp[i] == 3 && idOp[j] <= idOp[i])
            return false;
        else
            return (idOp[j] <= idOp[i]);
    }

    private static double fact(double n) throws MyException {
        if (n < 0)
            throw new MyException("Factorial can't be computed for NEGATIVE number(s), EXITING...");
        else if ((n - (long) n) != 0)
            throw new MyException("Factorial can't be computed for FRACTIONAL number(s), EXITING...");
        double f = 1;
        for (double j = 1; j <= n; j++)
            f *= j;
        return f;
    }

    private static double calc(double a, char ch, double b) throws MyException {
        double res = 0;
        if (ch == '+')
            res = a + b;
        else if (ch == '-')
            res = a - b;
        else if (ch == '*')
            res = a * b;
        else if (ch == '/') {
            if (b == 0)
                throw new MyException("Division by ZERO encountered, EXITING...");
            res = a / b;
        } else if (ch == '^')
            res = Math.pow(a, b);
        else if (ch == '|')
            res = Math.pow(b, 1 / a);
        else if (ch == 'C') {
            if (a < 0 || b < 0)
                throw new MyException("Combination(s) can't be computed for NEGATIVE input(s)");
            else if ((a - (long) a) != 0 || (b - (long) b) != 0)
                throw new MyException("Combination(s) can't be computed for FRACTIONAL input(s)");
            else if (a < b)
                throw new MyException("Combination(s) can't be computed for (n(= " + a + ") < r(= " + b + "))");
            else
                res = fact(a) / (fact(a - b) * fact(b));
        } else if (ch == 'P') {
            if (a < 0 || b < 0)
                throw new MyException("Permutation(s) can't be computed for NEGATIVE input(s)");
            else if ((a - (long) a) != 0 || (b - (long) b) != 0)
                throw new MyException("Permutation(s) can't be computed for FRACTIONAL input(s)");
            else if (a < b)
                throw new MyException("Permutation(s) can't be computed for (n(= " + a + ") < r(= " + b + "))");
            else
                res = fact(a) / fact(a - b);
        } else if (ch == '%') {
            if (b == 0)
                throw new MyException("Division by ZERO encountered, EXITING...");
            res = (long) Math.round(a) % (long) Math.round(b);
        }
        return res;
    }

    private static double getResult(List pol) throws MyException {
        Double2 stack = null;
        while (pol != null) {
            char ch = pol.node.charAt(0);
            if (isBinary(ch)) {
                stack.link.num = calc(stack.link.num, ch, stack.num);
                stack = stack.link;
            } else if (ch == 'n')
                stack.num = -stack.num;
            else if (ch == '!') {
                stack.num = fact(stack.num);
            } else if (ch == 'u')
                ;
            else {
                Double2 nptr = new Double2(parseDouble2(pol.node));
                if (stack == null)
                    stack = nptr;
                else {
                    nptr.link = stack;
                    stack = nptr;
                }
            }
            pol = pol.link;
        }
        return stack.num;
    }

    private static void invalidExpr() throws MyException {
        throw new MyException("Invalid expression entered, EXITING . . .");
    }

    private static List getExpr(String str) throws MyException {
        str = "(" + str + ")";
        List expr = new List("("), p = expr;
        char cp = '(', ch;
        int i = 1, tmp = 0, l = str.length();
        while (i < l) {
            if (tmp == i)
                invalidExpr();
            ch = str.charAt(i);
            tmp = i;
            if (Character.isDigit(ch)) {
                String n = String.valueOf(ch);
                cp = ch;
                i++;
                ch = str.charAt(i);
                while (Character.isDigit(ch) || ch == '.' || ch == 'E'
                        || ((ch == '-' || ch == '+') && cp == 'E')) {
                    n += ch;
                    cp = ch;
                    i++;
                    ch = str.charAt(i);
                }
                p.link = new List(n);
                p = p.link;
            } else if (ch == '.') {
                String n = String.valueOf(ch);
                cp = ch;
                int t = i;
                i++;
                ch = str.charAt(i);
                while (Character.isDigit(ch) || ch == 'E'
                        || ((ch == '-' || ch == '+') && cp == 'E')) {
                    n += ch;
                    cp = ch;
                    i++;
                    ch = str.charAt(i);
                }
                if (i == t + 1) {
                    i = t;
                    continue;
                }
                p.link = new List(n);
                p = p.link;
            } else if (ch == 'e') {
                p.link = new List("e");
                p = p.link;
                cp = ch;
                i++;
            } else if (isFunc(ch)) {
                List stack = null;
                String n = String.valueOf(ch);
                i++;
                while (i < l) {
                    ch = str.charAt(i);
                    n += ch;
                    i++;
                    if (ch == '(') {
                        if (stack == null)
                            stack = new List("(");
                        else {
                            List nptr = new List("(");
                            nptr.link = stack;
                            stack = nptr;
                        }
                    } else if (ch == ')') {
                        if (stack == null)
                            invalidExpr();
                        stack = stack.link;
                        if (stack == null) {
                            cp = ch;
                            break;
                        }
                    }
                }
                p.link = new List(n);
                p = p.link;
            } else if (str.substring(i).startsWith("pi")) {
                p.link = new List("pi");
                p = p.link;
                cp = 'i';
                i += 2;
            } else if (ch == '(' || ch == ')') {
                p.link = new List(String.valueOf(ch));
                p = p.link;
                cp = ch;
                i++;
            } else if (isValidOp(ch)) {
                char k;
                int r = isUnary(ch, cp);
                if (r == 1) {
                    if (ch == '-')
                        k = 'n';
                    else if (ch == '!')
                        k = ch;
                    else
                        k = 'u';
                } else if (r == 2)
                    continue;
                else
                    k = ch;
                p.link = new List(String.valueOf(k));
                p = p.link;
                cp = ch;
                i++;
            } else if (str.substring(i).startsWith("ans")) {
                p.link = new List("ans");
                p = p.link;
                cp = 's';
                i += 3;
            }
        }
        return expr;
    }

    private static List getPolish(List expr) {
        List stack = null, pol = null, p = null;
        while (expr != null) {
            char ch = expr.node.charAt(0);
            if (ch == '(') {
                List nptr = new List("(");
                if (stack == null)
                    stack = nptr;
                else {
                    nptr.link = stack;
                    stack = nptr;
                }
            } else if (findOp(ch) >= 0) {
                char cp = stack.node.charAt(0);
                while (cp != '(' && isPrior(ch, cp)) {
                    p.link = new List(String.valueOf(cp));
                    p = p.link;
                    stack = stack.link;
                    cp = stack.node.charAt(0);
                }
                List nptr = new List(String.valueOf(ch));
                nptr.link = stack;
                stack = nptr;
            } else if (ch == ')') {
                char cp = stack.node.charAt(0);
                while (cp != '(') {
                    p.link = new List(String.valueOf(cp));
                    p = p.link;
                    stack = stack.link;
                    cp = stack.node.charAt(0);
                }
                stack = stack.link;
            } else {
                if (pol == null) {
                    pol = new List(expr.node);
                    p = pol;
                } else {
                    p.link = new List(expr.node);
                    p = p.link;
                }
            }
            expr = expr.link;
        }
        numPost++;
        if (numPost == 0) {
            System.out.println("\nEquivalent POSTFIX EXPRESSION is :\n");
            dispList(pol);
        } else {
            System.out.println("\nSub-POSTFIX EXPRESSION " + numPost + " is :\n");
            dispList(pol);
        }
        return pol;
    }

    private static int skipParen(String p) {
        // Calling Method takes care of `p.indexOf('(')`;
        // Last Character of `p` is ')';
        int i = 1, l = p.length() - 1;
        while (i < l) {
            if (p.charAt(i) != '(')
                break;
            i++;
        }
        return i;
    }

    private static boolean isMath(String p) {
        boolean b = false;
        // Last Character of `p` is ')';
        int l = p.length() - 1;
        // Calling Method takes care of `p.indexOf('(')`;
        for (int i = 1; i < l; i++) {
            char ch = p.charAt(i);
            if (isValidOp(ch)) {
                b = true;
                break;
            }
        }
        return b;
    }

    private static double parseDouble2(String p) throws MyException {
        char ch = p.charAt(0);
        double num = 0;
        if (Character.isDigit(ch) || ch == '.') {
            int i = p.indexOf(')');
            if (i >= 0)
                p = p.substring(0, i);
            num = Double.parseDouble(p);
        } else if (ch == 'e')
            num = Math.exp(1);
        else if (p.startsWith("pi"))
            num = Math.PI;
        else if (p.startsWith("ans"))
            num = ans;
        else if (ch == 'l') {
            char k = p.charAt(1);
            int i = p.indexOf('(');
            if (k == 'o') {
                if (i < 3)
                    invalidExpr();
                p = p.substring(i);
                if (isMath(p))
                    num = Math.log10(getResult(getPolish(getExpr(p))));
                else
                    num = Math.log10(parseDouble2(p.substring(skipParen(p))));
            } else if (k == 'n') {
                p = p.substring(i);
                if (isMath(p))
                    num = Math.log(getResult(getPolish(getExpr(p))));
                else
                    num = Math.log(parseDouble2(p.substring(skipParen(p))));
            } else
                invalidExpr();
        } else if (ch == 's' || ch == 'c' || ch == 't') {
            int i = p.indexOf('(');
            if (i < 3)
                invalidExpr();
            int trad = 0, ti = 0, th = 0;
            char k = p.charAt(3);
            if (k == 'r')
                trad = 1;
            else if (k == 'i') {
                ti = 1;
                if (p.charAt(4) == 'r')
                    ti = 2;
            } else if (k == 'h')
                th = 1;
            p = p.substring(i);
            double ang = 0;
            if (isMath(p))
                ang = getResult(getPolish(getExpr(p)));
            else
                ang = parseDouble2(p.substring(skipParen(p)));
            if (ti == 0 && trad == 0 && th == 0)
                ang = Math.PI / 180 * ang;
            else if (ti == 1)
                num = 180 / Math.PI;
            if (ch == 's') {
                if (ti == 0 && th == 0)
                    num = Math.sin(ang);
                else if (ti == 1)
                    num *= Math.asin(ang);
                else if (ti == 2)
                    num = Math.asin(ang);
                else
                    num = .5 * (Math.exp(ang) - Math.exp(-ang));
            } else if (ch == 'c') {
                if (ti == 0 && th == 0)
                    num = Math.cos(ang);
                else if (ti == 1)
                    num *= Math.acos(ang);
                else if (ti == 2)
                    num = Math.acos(ang);
                else
                    num = .5 * (Math.exp(ang) + Math.exp(-ang));
            } else {
                if (ti == 0 && th == 0)
                    num = Math.tan(ang);
                else if (ti == 1)
                    num *= Math.atan(ang);
                else if (ti == 2)
                    num = Math.atan(ang);
                else
                    num = (Math.exp(ang) - Math.exp(-ang)) / (Math.exp(ang) + Math.exp(-ang));
            }
        } else if (ch == 'r' || ch == 'd') {
            int i = p.indexOf('(');
            if (i < 3)
                invalidExpr();
            p = p.substring(i);
            double ang = 0;
            if (isMath(p))
                ang = getResult(getPolish(getExpr(p)));
            else
                ang = parseDouble2(p.substring(skipParen(p)));
            if (ch == 'r')
                num = Math.PI * ang / 180;
            else
                num = 180 * ang / Math.PI;
        } else
            invalidExpr();
        return num;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int cont = 1;
        do {
            numPost = -1;
            System.out.println("\nEnter the EXPRESSION :=>\n");
            String str = br.readLine().trim().replace(" ", "");
            if (str.length() == 0) {
                System.out.println("\nSORRY ! You have entered NOTHING");
                System.out.print("\nEnter 1 to CONTINUE : ");
                try {
                    cont = Integer.parseInt(br.readLine().trim());
                } catch (Exception e) {
                    System.out.println("\n\nSORRY ! The following EXCEPTION has occurred :\n\n" + e.getMessage());
                } finally {
                    continue;
                }
            }
            try {
                List post = getPolish(getExpr(str));
                ans = getResult(post);
                System.out.println("\nThe RESULT of the EXPRESSION is = " + ans);
            } catch (Exception e) {
                System.out.println("\n\nSORRY ! The following EXCEPTION has occurred :\n\n" + e.getMessage());
            } finally {
                System.out.print("\nEnter 1 to CONTINUE : ");
                try {
                    cont = Integer.parseInt(br.readLine().trim());
                } catch (Exception e) {
                    System.out.println("\n\nSORRY ! The following EXCEPTION has occurred :\n\n" + e.getMessage());
                } finally {
                    continue;
                }
            }
        } while (cont == 1);
        br.readLine();
    }
}
