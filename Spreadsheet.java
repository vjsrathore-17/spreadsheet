import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

public class Spreadsheet {
    //private element sheet
    private Map<String, Integer> sheet;

    // constructor
    public Spreadsheet() {
        this.sheet = new HashMap<>(); 
    }

    // Getter
    public int getCellValue(String cellId) {
        Integer cellValue = sheet.get(cellId);
        return cellValue != null ? (int)cellValue : 0;
    }

    // Setter
    public void setCellValue(String cellId, Object value) {
        int val = evaluateCell(value);
        sheet.put(cellId, val);
    }
    
    private int evaluateCell(Object cellValue) {
        if (cellValue instanceof Integer) {
            return (int) cellValue;
        } else if (cellValue instanceof String) {
            char[] formulaCharacters = ((String)cellValue).toCharArray();
            StringBuilder expression = new StringBuilder("");
            for (int i=1;i<formulaCharacters.length;i++){
                if(formulaCharacters[i]=='A') {
                    int val = getCellValue(String.valueOf(formulaCharacters[i])+String.valueOf(formulaCharacters[i+1]));
                    expression.append(val);
                    i++;
                } else {
                    expression.append(formulaCharacters[i]);
                }
            }
            return evaluateExpression(expression.toString());
        }
        return 0;
    }

    private int evaluateExpression(String expression)
    {
        char[] tokens = expression.toCharArray();
        Stack<Integer> values = new Stack<Integer>();
        Stack<Character> ops = new Stack<Character>();
 
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ') continue;

            if (tokens[i] >= '0' && tokens[i] <= '9') {
                StringBuffer sbuf = new StringBuffer();
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') sbuf.append(tokens[i++]);
                values.push(Integer.parseInt(sbuf.toString()));
                i--;
            } else if (tokens[i] == '('){
                ops.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (ops.peek() != '(') values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.pop();
            } else if (tokens[i] == '+' || tokens[i] == '*' || tokens[i] == '/' || tokens[i] == '^') {
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.push(tokens[i]);
            } else if (tokens[i] == '-') {
                if(tokens[i-1] == '+' || tokens[i-1] == '*' || tokens[i-1] == '/' || tokens[i-1] == '^' || tokens[i-1]=='-' || tokens[i-1]=='('){
                    StringBuffer sbuf = new StringBuffer();
                    sbuf.append(tokens[i++]);
                    while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') sbuf.append(tokens[i++]);
                    values.push(Integer.parseInt(sbuf.toString()));
                    i--;
                } else {
                    while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    ops.push(tokens[i]);
                }
            }
        }
 
        while (!ops.empty()) values.push(applyOp(ops.pop(),values.pop(),values.pop()));
        return values.pop();
    }

    private boolean hasPrecedence(char op1, char op2){
        if (op1 == '^') return false;
        if (op2 == '(' || op2 == ')') return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false;
        else return true;
    }
 
    private int applyOp(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
                return (int) a / b;
            case '^':
                return (int) Math.pow(a,b);
        }
        return 0;
    }
}

