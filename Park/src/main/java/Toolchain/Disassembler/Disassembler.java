package Toolchain.Disassembler;


import Simulator.ALU;
import Simulator.InstructionDecoder;
import Simulator.ProgramLoader;
import Toolchain.Assembler.Generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Disassembler {
    private static InstructionDecoder decoder;
    private static HashMap<String, Integer> opcodes;

    private static void fillMap(){
        opcodes = new HashMap<>();
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
    public static List<String> Disassemble(String filePath){
        ArrayList<String> data = new ArrayList<String>();
        ProgramLoader pl = new ProgramLoader();
        fillMap();


        int[] insts = pl.LoadFile(filePath);
        for(int inst : insts){
            decodeInst(inst);
        }


        return data;
    }

    private static String decodeInst(int inst){
        String str = "";

        InstructionDecoder id = new InstructionDecoder();
        id.Decode(inst);
        if(id.opcode==0){ // r-type
            for (Map.Entry<String,Integer> entry:
                 opcodes.entrySet()) {
                if(entry.getValue() == id.func){
                    str = entry.getKey();
                    break;
                }
            }
            str = str.concat(" r" + id.rd + ",");
            str = str.concat("r" + id.rs + ",");
            str = str.concat("r" + id.rt);
        }
        else{ // i-type
            for (Map.Entry<String,Integer> entry:
                    opcodes.entrySet()) {
                if(entry.getValue()-7 == id.opcode){
                    str = entry.getKey();
                    break;
                }
            }
            str = str.concat(" r" + id.rd + ",");
            str = str.concat("r" + id.rs + ",");
            str = str.concat(String.valueOf(id.immediate));
            if(str.equals("b")){
                str = str.concat(String.valueOf(id.jump_dst));
            }

        }

        System.out.println(str);

        return str;
    }

    public static void main(String[] args) {
        Disassembler.Disassemble("Programs/4x3.bin");
    }
}
