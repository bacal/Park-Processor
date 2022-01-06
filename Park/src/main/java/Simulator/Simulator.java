package Simulator;

import java.io.File;
import java.util.Scanner;

public class Simulator {
    private int pc = 0;
    private ALU alu;
    private ProgramLoader pl;
    private RegisterFile rf;
    private DataMemory dm;
    private InstructionMemory im;
    private InstructionDecoder id;
    private boolean isProgramLoaded;

    Simulator(){
        alu = new ALU();
        pl = new ProgramLoader();
        im = new InstructionMemory();
        id = new InstructionDecoder();
        dm = new DataMemory();
        rf = new RegisterFile();
    }
    void Reset(){
        rf.Clear();
        dm.Clear();
        im.Clear();
        isProgramLoaded = false;
    }

    void LoadProgramFromFile(String FileName){
        int [] loaded_program;
        loaded_program = pl.LoadFile(FileName);
        int i=0;
        for(int inst : loaded_program){
            im.Write(i,inst);
            i++;
        }
        im.length = i;
        isProgramLoaded = true;
    }

    public void UserInput(){
        Scanner stdin = new Scanner(System.in);
        while(stdin.hasNext()){

            String s = stdin.nextLine();
            if(s.contains("quit")|| s.contains("exit")) {
                break;
            }
            else if(s.contains("load")){
                String [] split = s.split("(load )");
                if(split.length != 2){
                    System.out.println("usage: load <filename>");
                }
                else{
                    File f = new File(split[1]);
                    if(f.isFile()) {
                        LoadProgramFromFile(split[1]);
                        System.out.println("[loaded program]");
                    }
                    else{
                        System.out.println("error: file not found");
                    }
                }

            }
            else if (s.contains("step")){
                SingleStep();
            }
            else if(s.contains("print")){
                String[] split = s.split("(print )");
                if(split.length != 2){
                    System.out.println("usage: print [mem_addr,registers]");
                }
                if(split[1].equals("registers")){
                    System.out.println(rf.toString());
                }
            }
            else if(s.equals("run") || s.equals("continue")){
                RunProgram();
            }
            else if(s.equals("reset")){
                Reset();
            }
            else{
                System.out.println("error: Unknown input");
            }
        }
    }

    public void ExecuteInstruction(int inst){
        id.Decode(inst);

        switch(id.opcode){
            case 0b0000:
                rf.Write(id.rd,alu.operation(id.func,rf.Read(id.rs),rf.Read(id.rt)));
                break;

            case 0b0001:
                pc = pc + id.jump_dst ;
                break;

            case 0b0010:
                pc = pc + alu.operation(0,id.immediate,rf.Read(id.rs));
                break;

            case 0b0011:
                rf.Write(7,alu.operation(0,id.immediate,rf.Read(id.rs)));
                pc = pc + id.jump_dst;
                break;

            case 0b0100: //Branch if Greater Than
                if(alu.operation(0b0001,rf.Read(id.rs),rf.Read(id.rt)) > 0)
                {
                    if(id.immediate <= im.length  && id.immediate != 0)
                        pc = id.immediate;
                    else if(id.immediate == 0){
                        pc = 0;
                    }
                    else{
                        System.out.println("error: attempted to jump out of bounds");
                        System.exit(1);
                    }
                }
                break;
            case 0b0101: //Branch if Less Than
                if(alu.operation(0b0001,rf.Read(id.rs),rf.Read(id.rt)) < 0)
                {
                    if(id.immediate <= im.length && id.immediate != 0)
                        pc = id.immediate-2;
                    else if(id.immediate == 0){
                        pc = 0;
                    }
                    else{
                        System.out.println("error: attempted to jump out of bounds");
                        System.exit(1);
                    }

                }
                break;

            case 0b0110: //Load Word
                rf.Write(id.rt,dm.Read(alu.operation(0,rf.Read(id.rs),id.immediate)));
                break;
            case 0b0111: //Store Word
                dm.Write(alu.operation(0,rf.Read(id.rt),id.immediate),rf.Read(id.rs));
                break;

            case 0b1001: //I-Type version of an src.Simulator.Simulator.Simulator.ALU op
            case 0b1010: //I-Type version of an src.Simulator.Simulator.Simulator.ALU op
            case 0b1011: //I-Type version of an src.Simulator.Simulator.Simulator.ALU op
            case 0b1110: //I-Type version of an src.Simulator.Simulator.Simulator.ALU op
            case 0b1111: //I-Type version of an src.Simulator.Simulator.Simulator.ALU op
            case 0b1000: //I-Type version of an src.Simulator.Simulator.Simulator.ALU op
                rf.Write(id.rt,alu.operation(id.func,rf.Read(id.rs),id.immediate));
                break;

            case 0b1100: //System Call
                break;

            case 0b1101: //Branch If Equal
                if(alu.operation(0b0001,rf.Read(id.rs),rf.Read(id.rt)) ==0){
                    if(id.immediate <= im.length && id.immediate >=0)
                        pc = id.immediate-1;
                    else{
                        System.out.println("error: attempted to jump out of bounds");
                        System.exit(1);
                    }
                }
                break;
        }
    }

    public void SingleStep(){
        System.out.println("[pc: " + pc + "\tinst: " + String.format("0x%1$04x]",im.Read(pc)));
        ExecuteInstruction(im.Read(pc));
        pc++; // this is equivalent to "pc + 2" but since we're using an array it's the next index: "current inst + 2 bytes"
    }
    public void RunProgram(){
        while(pc < im.length)
        {
            System.out.println("[pc: " + pc + "\tinst: " + String.format("0x%1$04x]",im.Read(pc)));
            ExecuteInstruction(im.Read(pc));
            pc++; // this is equivalent to "pc + 2" but since we're using an array it's the next index: "current inst + 2 bytes"
        }
        pc =0;
    }


    public static void main(String[] args) {
        Simulator simulator = new Simulator();

        simulator.UserInput();
    }

}
