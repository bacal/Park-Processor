package Simulator;

public class InstructionMemory {
    private int []memory;
    public int length;

    InstructionMemory(){
        memory = new int[65536];
    }
    public void Clear(){
        memory = new int[65536];
    }

    public int Read(int addr){
        return memory[addr];
    }
    public void Write(int addr, int data){
        memory[addr] = data;
    }
}
