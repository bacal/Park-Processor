package Simulator;

public class ALU {
    public boolean overflow = false;
    public boolean zero = false;
    public boolean carry_out = false;

    private int add(int a, int b){
        overflow = (a + b) > Short.MAX_VALUE;
        zero = a + b == 0;
        carry_out = false;
        return (short)(a + b);
    }
    private int sub(int a, int b){
        overflow = (a - b) < Short.MIN_VALUE;
        zero = a - b == 0;
        carry_out = false;
        return (short)(a-b);
    }
    private int and(int a, int b){
        return (short)a&b;
    }
    private int or(int a, int b){
        return (short)a|b;
    }
    private int xor(int a, int b){
        return (short)a^b;
    }
    private int lls(int a, int b){

      return (short)(a<<b);
    }
    private int lrs(int a, int b){
        return (short)(a>>b);
    }
    private int nor(int a, int b){
        return (short)~(a|b); // Is nor not the result or a nor b
    }

    public int operation(int alu_op, int a, int b) {
        switch (alu_op) {
            case 0b0000:
                return add(a, b);
            case 0b0001:
                return sub(a, b);
            case 0b0010:
                return and(a, b);
            case 0b0011:
                return or(a, b);
            case 0b0100:
                return xor(a, b);
            case 0b0101:
                return nor(a, b);
            case 0b0110:
                return lls(a,b);
            case 0b0111:
                return lrs(a,b);
        }
        return Integer.MIN_VALUE;
    }
}
