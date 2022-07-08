package Simulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/* Since bytes are signed in java the file must be read using java chars :( */


public class ProgramLoader {

    public int[] LoadFile(String FileName){
        char[] raw_data = null;
        try
        {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(FileName),"ISO-8859-15");
            raw_data = new char[(int)new File(FileName).length()];
            isr.read(raw_data);
            isr.close();
        } catch( IOException f)
        {
            f.printStackTrace();
        }

        assert raw_data != null;
        int[] instructions = new int[raw_data.length / 2];

        int pos =0;
        for(int i=0; i<raw_data.length-1; i+=2){
            instructions[pos] = (raw_data[i] << 8);
            instructions[pos] = instructions[pos] | raw_data[i+1];
            pos++;
        }


        return instructions;
    }
}
