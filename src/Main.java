import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.println("Executing with args: " + Arrays.toString(args));
        if (args[0].contains("fail")) {
            throw new RuntimeException("It's supposed to fail");
        }
        
        System.out.println("Write an expression:");
        Parser myParser = new Parser(System.in);
        
        try {
            SimpleNode root = myParser.Program(); // returns reference to root node
        
            root.dump(""); // prints the tree on the screen
        
            System.out.println("Expression value: " + Main.eval(root));
        
        } catch (Exception e) {
        }
        
    }

    public static int eval(SimpleNode node) {

        if (node.jjtGetNumChildren() == 0) // leaf node with integer value
            return node.val;
        else if (node.jjtGetNumChildren() == 1) // only one child
            return eval((SimpleNode) node.jjtGetChild(0));

        SimpleNode lhs = (SimpleNode) node.jjtGetChild(0); // left child
        SimpleNode rhs = (SimpleNode) node.jjtGetChild(1); // right child

        switch (node.id) {
        case ParserTreeConstants.JJTADD:
            return eval(lhs) + eval(rhs);
        case ParserTreeConstants.JJTSUB:
            return eval(lhs) - eval(rhs);
        case ParserTreeConstants.JJTMUL:
            return eval(lhs) * eval(rhs);
        case ParserTreeConstants.JJTDIV:
            return eval(lhs) / eval(rhs);
        default: // abort
            throw new RuntimeException("Ilegal operator!");
        }

    }

}