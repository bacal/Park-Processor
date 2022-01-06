package Simulator;

public class RegisterFile {
    private int[] registers;
    private int reg_wr;
    RegisterFile(){
        registers = new int[8];
    }
    public int Read(int addr){
        return registers[addr];
    }
    public void Write(int addr, int data){
        registers[addr] = data;
    }
    public void Clear(){
        for(int i=0; i<8; i++){
            registers[i] = 0;
        }
    }
    public String toString(){
        String ret = "";
        for(int i=0; i<registers.length; i++)
            ret += "Register b" + i + ": " + registers[i] + "\n";
        return ret;
    }


}
