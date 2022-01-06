package Assembler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Lexer {
    public HashMap<String,Integer> labels = new HashMap<>();
    public ArrayList<Token> tokens;
    boolean label = false;

    Lexer(String FileName){
        Scanner scanner = null;
        try{
            scanner = new Scanner(new FileInputStream(FileName));
        }catch (FileNotFoundException f){
            f.printStackTrace();
        }
        assert scanner != null;
        ArrayList<String> list = new ArrayList<String>();
        while (scanner.hasNextLine()) {
            list.add(scanner.nextLine());
            if(list.get(list.size()-1).contains(";")){ // Truncates string if comment is detected.
                //System.out.println(list.get(list.size()-1).substring(list.get(list.size()-1).indexOf(";")));
                list.set(list.size()-1,list.get(list.size()-1).substring(0,list.get(list.size()-1).indexOf(";")));
            }
        }

        generateTokens(list);
    }

    private void parseLine(String line){
        line = line.replace(",", " ");
        String[] split = line.split(" ");
        boolean rb = false,rp = false;
        for(String l : split){
            if(l.contains("[")){
                l = l.replace("[","");
                tokens.add(new Token(token_type.TK_LBRACKET,"["));
            }
            if(l.contains("]")){
                l = l.replace("]","");
                rb = true;
            }
            if(l.contains("(")){
                l = l.replace("(","");
                tokens.add(new Token(token_type.TK_LPAREN,"("));
            }
            if(l.contains(")")){
                l = l.replace(")","");
                rp = true;
            }
            if(l.matches("\\d+") && !l.matches("#\\d+") && !l.matches("#0[xX][0-9a-fA-F]+")){
                System.err.println("Error: immediates must be prefixed with a #");
                System.exit(1);
            }
            if(l.matches("\\w+") && !l.matches("\\d+") && !l.matches("0[xX][0-9a-fA-F]+") && !l.matches("b[0-7]"))
                tokens.add(new Token(token_type.TK_SYMBOL,l));
            if(l.matches("#\\d+") || l.matches("#0[xX][0-9a-fA-F]+")){
                l = l.replace("#","");
                if(l.matches("0[xX][0-9a-fA-F]+")) {
                    l = l.replace("0x", "");
                    l = String.valueOf(Integer.parseInt(l,16));
                    tokens.add(new Token(token_type.TK_INTEGER,l));
                }
                else{
                    tokens.add(new Token(token_type.TK_INTEGER,l));
                }
            }
            if(l.matches("@\\w+")) {
                l = l.replace("@","");
                tokens.add(new Token(token_type.TK_AT, l));
            }
            if(l.matches("\\+")){
                tokens.add(new Token(token_type.TK_PLUS,"+"));
            }
            if(l.matches("-")){
                tokens.add(new Token(token_type.TK_MINUS,"-"));
            }
            if(l.matches("b[0-7]"))
            {
                tokens.add(new Token(token_type.TK_REGISTER,l));
            }
            if(rb){
                tokens.add(new Token(token_type.TK_RBRACKET,"]"));
                rb = false;
            }
            if(rp){
                tokens.add(new Token(token_type.TK_RPAREN,")"));
                rp = false;
            }
            if(l.contains(":")){
                tokens.add(new Token(token_type.TK_SYMBOL,l.replace(":","")));
                tokens.add(new Token(token_type.TK_COLON,":"));
                label = true;
            }
        }
    }

    public void generateTokens(ArrayList<String> data){
        tokens = new ArrayList<Token>();
        int i=0;
        for(String s : data){
            parseLine(s);
            if(label) {
                labels.put(tokens.get(tokens.size() - 2).data, i);
                tokens.remove(tokens.size()-1);
                tokens.remove(tokens.size()-1);
                label = false;
            }
            i++;
        }
    }
}
