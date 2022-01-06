package Assembler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
public class Parser {
    private HashMap<String, Integer> labels = null;
    public ArrayList<Stack<Token>> stackArrayList = new ArrayList<>();

    private int evaluateExpression(Stack<Token> stack){
        int sum = 0;
        while(stack.peek().type != token_type.TK_LPAREN){
            stack.pop();
            int a = Integer.parseInt(stack.peek().data);
            stack.pop();
            switch(stack.peek().type){
                case TK_PLUS:
                    stack.pop();
                    if(stack.peek().type != token_type.TK_INTEGER){
                        System.out.println("Error: invalid operation");
                        System.exit(1);
                    }
                    else{
                        int b = Integer.parseInt(stack.pop().data);
                        sum = a+b;
                    }
                    break;

                case TK_MINUS:
                    stack.pop();
                    if(stack.peek().type != token_type.TK_INTEGER){
                        System.out.println("Error: invalid operation");
                        System.exit(1);
                    }
                    else{
                        int b = Integer.parseInt(stack.pop().data);
                        sum = a+b;
                    }
                default:
                    System.out.println("Error invalid arithmetic operation!");
            }
        }
        return sum;
    }

    Parser(ArrayList<Token> tokens,HashMap<String,Integer> labels){
        this.labels = labels;
        for(Token t: tokens){
            if(t.type==token_type.TK_AT){
                if(labels.containsKey(t.data)){
                   t.type = token_type.TK_INTEGER;
                   t.data = labels.get(t.data).toString();
                }
                else{
                    System.err.println("Error: invalid label: \"" + t.data + "\"");
                }
            }

            if(t.type == token_type.TK_SYMBOL && !labels.containsKey(t.data)){
                stackArrayList.add(new Stack<Token>());
                stackArrayList.get(stackArrayList.size()-1).push(t);
            }
            else{
                if(t.type == token_type.TK_RPAREN){
                    stackArrayList.get(stackArrayList.size()-1).push(t);
                    String data = Integer.toString(evaluateExpression(stackArrayList.get(stackArrayList.size()-1)));
                    stackArrayList.get(stackArrayList.size()-1).pop();
                    stackArrayList.get(stackArrayList.size()-1).push(new Token(token_type.TK_INTEGER,data));
                }
                else
                stackArrayList.get(stackArrayList.size()-1).push(t);
            }
        }
        /*for(Stack<Assembler.Token> t : stackArrayList){
            System.out.println(t.toString());
        }*/
    }




}
