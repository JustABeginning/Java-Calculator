import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import mypkg.MyException;
import mypkg.StrList;
import mypkg.ChrStack;
import mypkg.DblStack;

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
            throw new MyException("Factorial can't be computed for NEGATIVE number(s)");
        else if ((n - (long) n) != 0)
            throw new MyException("Factorial can't be computed for FRACTIONAL number(s)");
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
                throw new MyException("Division by ZERO encountered !");
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
                throw new MyException("Division by ZERO encountered !");
            res = (long) Math.round(a) % (long) Math.round(b);
        }
        return res;
    }

    private static double getResult(StrList pol) throws MyException {
        DblStack stack = null;
        while (!pol.isEmpty()) {
            char ch = pol.getStart().charAt(0);
            if (isBinary(ch)) {
                double b = stack.getTop();
                stack.pop();
                double a = stack.getTop();
                stack.pop();
                stack.push(calc(a, ch, b));
            } else if (ch == 'n') {
                double n = -stack.getTop();
                stack.pop();
                stack.push(n);
            } else if (ch == '!') {
                double n = fact(stack.getTop());
                stack.pop();
                stack.push(n);
            } else if (ch == 'u')
                ;
            else {
                double n = parseDoubleM(pol.getStart());
                if (stack == null)
                    stack = new DblStack(n);
                else
                    stack.push(n);
            }
            pol.remove();
        }
        return stack.getTop();
    }

    private static boolean validParen(char ch, char cp) {
        if (ch == '(' && (cp == '(' || isBinary(cp)))
            return true;
        else if (ch == ')' && !isBinary(cp))
            return true;
        else
            return false;
    }

    private static int isPower10(char ch, char cp) {
        int b = 3;
        if (ch == 'E' && Character.isDigit(cp))
            b = 1;
        else if ((ch == '-' || ch == '+') && cp == 'E')
            b = 2;
        else if (!(ch == '-' || ch == '+') && cp == 'E')
            b = -1;
        else
            b = 0;
        return b;
    }

    private static void invalidExpr() throws MyException {
        throw new MyException("Invalid EXPRESSION !");
    }

    private static StrList getExpr(String str) throws MyException {
        str = "(" + str + ")";
        StrList expr = new StrList("(");
        char cp = '(', ch;
        int i = 1, tmp = 0, l = str.length();
        while (i < l) {
            if (tmp == i)
                invalidExpr();
            ch = str.charAt(i);
            tmp = i;
            if (Character.isDigit(ch)) {
                StringBuffer n = new StringBuffer();
                n.append(ch);
                cp = ch;
                int t = i;
                i++;
                ch = str.charAt(i);
                while (Character.isDigit(ch) || ch == '.' || isPower10(ch, cp) > 0) {
                    n.append(ch);
                    cp = ch;
                    i++;
                    ch = str.charAt(i);
                }
                if (isPower10(ch, cp) < 0) {
                    i = t;
                    continue;
                }
                expr.add(n.toString());
            } else if (ch == '.') {
                StringBuffer n = new StringBuffer();
                n.append(ch);
                cp = ch;
                int t = i;
                i++;
                ch = str.charAt(i);
                while (Character.isDigit(ch) || isPower10(ch, cp) > 0) {
                    n.append(ch);
                    cp = ch;
                    i++;
                    ch = str.charAt(i);
                }
                if (i == t + 1 || isPower10(ch, cp) < 0) {
                    i = t;
                    continue;
                }
                expr.add(n.toString());
            } else if (ch == 'e') {
                expr.add("e");
                cp = ch;
                i++;
            } else if (isFunc(ch)) {
                ChrStack stack = null;
                StringBuffer n = new StringBuffer();
                n.append(ch);
                i++;
                while (i < l) {
                    ch = str.charAt(i);
                    n.append(ch);
                    i++;
                    if (ch == '(') {
                        if (stack == null)
                            stack = new ChrStack(ch);
                        else
                            stack.push(ch);
                    } else if (ch == ')') {
                        if (stack == null)
                            invalidExpr();
                        stack.pop();
                        if (stack.isEmpty()) {
                            cp = ch;
                            break;
                        }
                    }
                }
                expr.add(n.toString());
            } else if (str.startsWith("pi", i)) {
                expr.add("pi");
                cp = 'i';
                i += 2;
            } else if (validParen(ch, cp)) {
                expr.add(String.valueOf(ch));
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
                expr.add(String.valueOf(k));
                cp = ch;
                i++;
            } else if (str.startsWith("ans", i)) {
                expr.add("ans");
                cp = 's';
                i += 3;
            }
        }
        return expr;
    }

    private static StrList getPolish(StrList expr) throws MyException {
        ChrStack stack = null;
        StrList pol = null;
        while (!expr.isEmpty()) {
            String st = expr.getStart();
            char ch = st.charAt(0);
            if (ch == '(')
                if (stack == null)
                    stack = new ChrStack(ch);
                else
                    stack.push(ch);
            else if (findOp(ch) >= 0) {
                char cp = stack.getTop();
                while (cp != '(' && isPrior(ch, cp)) {
                    pol.add(String.valueOf(cp));
                    stack.pop();
                    cp = stack.getTop();
                }
                stack.push(ch);
            } else if (ch == ')') {
                char cp = stack.getTop();
                while (cp != '(') {
                    pol.add(String.valueOf(cp));
                    stack.pop();
                    cp = stack.getTop();
                }
                stack.pop();
            } else if (pol == null)
                pol = new StrList(st);
            else
                pol.add(st);
            expr.remove();
        }
        numPost++;
        if (numPost == 0) {
            System.out.println("\nEquivalent POSTFIX EXPRESSION is :\n");
            pol.display();
        } else {
            System.out.println("\nSub-POSTFIX EXPRESSION " + numPost + " is :\n");
            pol.display();
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
        char ch = '(', cp = ch;
        for (int i = 1; i < l; i++) {
            ch = p.charAt(i);
            if (isValidOp(ch)) {
                if (isPower10(ch, cp) == 2)
                    continue;
                else {
                    b = true;
                    break;
                }
            }
            cp = ch;
        }
        return b;
    }

    private static double parseDoubleM(String p) throws MyException {
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
                    num = Math.log10(parseDoubleM(p.substring(skipParen(p))));
            } else if (k == 'n') {
                p = p.substring(i);
                if (isMath(p))
                    num = Math.log(getResult(getPolish(getExpr(p))));
                else
                    num = Math.log(parseDoubleM(p.substring(skipParen(p))));
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
                ang = parseDoubleM(p.substring(skipParen(p)));
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
                ang = parseDoubleM(p.substring(skipParen(p)));
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
                StrList post = getPolish(getExpr(str));
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
