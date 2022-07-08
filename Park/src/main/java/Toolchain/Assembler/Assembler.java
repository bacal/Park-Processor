package Toolchain.Assembler;
import org.apache.commons.cli.*;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Assembler {

    static String intTo16String(int data){
        String s = new String();
        for(int i=0; i<16; i++){
            s = s.concat(((1<<15-i) & data) > 0?"1":"0");
        }
        return s;
    }
    public void writeToFile(String FileName, ArrayList<Integer> data){
        DataOutputStream dataOutputStream = null;
        try {
             dataOutputStream= new DataOutputStream(new FileOutputStream(FileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert dataOutputStream != null;
        for(int d : data){
            try {
                dataOutputStream.writeChar(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void assembleFile(String inputFile){
        Lexer lexer = new Lexer(inputFile);
        ArrayList<Integer> data = new ArrayList<>();
        //System.out.println(lexer.tokens);
        //System.out.println(lexer.labels);

        Toolchain.Assembler.Parser parser = new Toolchain.Assembler.Parser(lexer.tokens,lexer.labels);
        Generator generator = new Generator();
        for(Stack<Token> tokenStack: parser.stackArrayList) {
            int a = generator.createInstruction(tokenStack);
            data.add(a);
            //System.out.println(String.format("0x%1$04X",a) + ": " + intTo16String(a));
        }
        writeToFile("out.bin",data);
    }

    public static void main(String [] args){

        Options options = new Options();
        Option outputFile = new Option("o","output",true,"output file name");
        outputFile.setRequired(false);
        options.addOption(outputFile);

        CommandLineParser c_parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try{
            cmd = c_parser.parse(options,args);

        }catch(ParseException e){
            formatter.printHelp("mgsm",options);
        }
        String inputFile = cmd.getArgList().get(0);


        Lexer lexer = new Lexer(inputFile);
        ArrayList<Integer> data = new ArrayList<>();
        //System.out.println(lexer.tokens);
        //System.out.println(lexer.labels);

        Toolchain.Assembler.Parser parser = new Parser(lexer.tokens,lexer.labels);
        Generator generator = new Generator();
        for(Stack<Token> tokenStack: parser.stackArrayList) {
            int a = generator.createInstruction(tokenStack);
            data.add(a);
            //System.out.println(String.format("0x%1$04X",a) + ": " + intTo16String(a));

        }
        Assembler assembler = new Assembler();
        String outfile = cmd.getOptionValue("output");
        if(outfile == null){
            outfile = "out.bin";
        }
        assembler.writeToFile(outfile,data);
    }
}
