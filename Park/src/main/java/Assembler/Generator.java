package Assembler;

import java.util.HashMap;
import java.util.Stack;

public class Generator {
    HashMap<String, Integer> opcodes = new HashMap<>();


    private void fillMap(){
        opcodes.put("add", 0);
        opcodes.put("sub", 1);
        opcodes.put("and", 2);
        opcodes.put("or",  3);
        opcodes.put("xor", 4);
        opcodes.put("nor", 5);
        opcodes.put("lls", 6);
        opcodes.put("lrs", 7);
        opcodes.put("b", 8);
        opcodes.put("bx", 9);
        opcodes.put("bl", 10);
        opcodes.put("bgt", 11);
        opcodes.put("blt", 12);
        opcodes.put("lw", 13);
        opcodes.put("sw", 14);
        opcodes.put("addi", 15);
        opcodes.put("subi", 16);
        opcodes.put("andi", 17);
        opcodes.put("ori", 18);
        opcodes.put("sys", 19);
        opcodes.put("beq", 20);
        opcodes.put("llsi",21);
        opcodes.put("lrsi",22);
    }

    private int getOpcode(String data){
        if(opcodes.get(data) < 8){
            // Probably can be put into a hash table
            return 0;
        }
        else if(opcodes.get(data) > 0){
            return opcodes.get(data)-7;
        }
        return -1;
    }


    public int createInstruction(Stack<Token> tokenStack){
        int instruction = 0;
        int opcode = getOpcode(tokenStack.get(0).data);
        int reg = 0;
        String s;
        switch(opcode){
            case 0b0000:
                if(tokenStack.peek().type == token_type.TK_INTEGER){
                    System.out.println("Error: extra immediate added to R-Type instruction");
                    System.exit(1);
                }
                s = tokenStack.pop().data.replace("b","");
                instruction |= (Integer.parseInt(s) << 6);
                s = tokenStack.pop().data.replace("b","");
                instruction |= (Integer.parseInt(s) << 9);
                s = tokenStack.pop().data.replace("b","");
                instruction |= (Integer.parseInt(s) << 3);
                instruction |= opcodes.get(tokenStack.get(0).data);
                tokenStack.pop();
                break;

            case 0b0001:
            case 0b0011:
                instruction |= opcode << 12;
                instruction |= Integer.parseInt(tokenStack.pop().data);
                tokenStack.pop();
                break;

            case 0b0010:
                instruction |= opcode << 12;
                s = tokenStack.pop().data.replace("b","");
                instruction |= (Integer.parseInt(s) << 9);
                break;

            case 0b0100:
            case 0b0101:
            case 0b1101:
                instruction |= opcode << 12;
                if(tokenStack.peek().type != token_type.TK_INTEGER){
                    System.err.println("Error: invalid instruction");
                    System.exit(1);
                }
                instruction |= Integer.parseInt(tokenStack.pop().data);
                s = tokenStack.pop().data.replace("b","");
                instruction |= (Integer.parseInt(s) << 6);
                s = tokenStack.pop().data.replace("b","");
                instruction |= (Integer.parseInt(s) << 9);
                tokenStack.pop();
                break;

            case 0b0110:
                if(tokenStack.peek().type == token_type.TK_RBRACKET){
                    tokenStack.pop();
                    instruction |= Integer.parseInt(tokenStack.pop().data);

                    if(tokenStack.peek().type == token_type.TK_PLUS) {
                        tokenStack.pop();
                        if (tokenStack.peek().type != token_type.TK_REGISTER) {
                            System.out.println("Error: brackets are only for offsetting mem located in register");
                            System.exit(1);
                        }

                        s = tokenStack.pop().data.replace("b", "");
                        reg = Integer.parseInt(s);
                        instruction |= (reg << 9);
                        tokenStack.pop();
                        s = tokenStack.pop().data.replace("b", "");
                        reg = Integer.parseInt(s);
                        instruction |= (reg << 6);
                        instruction |= opcode << 12;
                        tokenStack.pop();
                    }
                    break;
                }
                else if(tokenStack.peek().type == token_type.TK_INTEGER){
                    instruction |= Integer.parseInt(tokenStack.pop().data);
                    s = tokenStack.pop().data.replace("b", "");
                    reg = Integer.parseInt(s);
                    instruction |= (reg << 6);

                }
                instruction |= opcode << 12;
                tokenStack.pop();
                break;

            case 0b0111:
                if(tokenStack.peek().type == token_type.TK_RBRACKET){
                    tokenStack.pop();
                    instruction |= Integer.parseInt(tokenStack.pop().data);

                    if(tokenStack.peek().type == token_type.TK_PLUS) {
                        tokenStack.pop();
                        if (tokenStack.peek().type != token_type.TK_REGISTER) {
                            System.out.println("Error: brackets are only for offsetting mem located in register");
                            System.exit(1);
                        }

                        s = tokenStack.pop().data.replace("b", "");
                        reg = Integer.parseInt(s);
                        instruction |= (reg << 6);
                        tokenStack.pop();
                        s = tokenStack.pop().data.replace("b", "");
                        reg = Integer.parseInt(s);
                        instruction |= (reg << 9);
                        instruction |= opcode << 12;
                        tokenStack.pop();
                    }
                    break;
                }
                else if(tokenStack.peek().type == token_type.TK_INTEGER){
                    instruction |= Integer.parseInt(tokenStack.pop().data);
                    s = tokenStack.pop().data.replace("b", "");
                    reg = Integer.parseInt(s);
                    instruction |= (reg << 9);

                }
                instruction |= opcode << 12;
                tokenStack.pop();
                break;


            case 0b1000:
            case 0b1001:
            case 0b1010:
            case 0b1011:
            case 0b1110:
            case 0b1111:
                if(tokenStack.peek().type == token_type.TK_REGISTER){
                    System.err.println("Error: extra register in I-Type instruction");
                    System.exit(1);
                }
                instruction |= opcode << 12;
                instruction |= Integer.parseInt(tokenStack.pop().data);
                s = tokenStack.pop().data.replace("b", "");
                reg = Integer.parseInt(s);
                instruction |= (reg << 9);
                s = tokenStack.pop().data.replace("b", "");
                reg = Integer.parseInt(s);
                instruction |= (reg << 6);
                tokenStack.pop();
                break;



            case 0b1100:
                instruction |= opcode << 12;
                tokenStack.pop();
                break;


        }


        return instruction;
    }
    Generator(){
        fillMap();
    }
}
