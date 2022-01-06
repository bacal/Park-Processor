package Simulator;

public class DataMemory {
    private int []memory;

    DataMemory(){
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
