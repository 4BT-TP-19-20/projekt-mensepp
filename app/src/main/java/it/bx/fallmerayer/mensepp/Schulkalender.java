package it.bx.fallmerayer.mensepp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Schulkalender {
    private int[][] kalender = new int[31][12];

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void dateiToKalender(String dateiname) {
        String[] values;
        try (BufferedReader br = new BufferedReader(new FileReader(dateiname))) {
            String line;
            for (int monate = 0; monate < 12; monate++) {
                line = br.readLine();
                values = line.split(";");
                for (int tage = 0; tage < 31; ++tage) {//int Integer umwandeln
                    kalender[tage][monate] = Integer.parseInt(values[tage]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[][] getSchulkalender() {
        return kalender;
    }
}
