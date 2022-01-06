package Simulator;

public class InstructionDecoder {
    public int opcode;
    public int rs;
    public int rt;
    public int rd;
    public int func;
    public int immediate;
    public int jump_dst;

    public void Decode(int inst){
        opcode = ((inst & (0xF << 12)) >> 12);
        rs = (inst & (0x7<<9))>>9;
        rt = (inst & (0x7 << 6)) >> 6;
        rd = (inst & 0x1A) >> 3;
        if(opcode == 0) {
            func = (inst & 0x7);
        }
        else{
            func = (opcode&0x7);
        }
        immediate = (inst & 0x3F);
        jump_dst = (inst & 0xFFF);
    }
}
