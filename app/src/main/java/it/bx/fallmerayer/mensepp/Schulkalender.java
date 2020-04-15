package it.bx.fallmerayer.mensepp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Schulkalender {
    private int [][] kalender=new int[31][12];
    private String  dateipfad="kalender.txt";
    public Schulkalender() {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void dateiToKalender(){
        int monate=0;
        int tage=0;
        String [] values;

        try (BufferedReader br = new BufferedReader(new FileReader(dateipfad))) {
            String line;
            while (monate<12) {//dass jede Zeile durchgegangen wird
                line = br.readLine();
                values=line.split("  ");
                for (tage=0;tage<31;++tage){//int Integer umwandeln
                    kalender[tage][monate]=Integer.parseInt(values[tage]);
                }
                ++monate;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
