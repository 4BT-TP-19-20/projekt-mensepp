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
    private String dateiname;

    public Schulkalender(String dateiname) {
        this.dateiname = dateiname;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void dateiToKalender() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(dateiname));//csv kaleder mit scanner öffnen
        for (int monate = 0; monate < 12; monate++) {
            for (int tage = 0; tage < 31; ++tage) {
                kalender[tage][monate] = scanner.nextInt();//immer das nächste integer einlesen
            }
        }
        scanner.close();
    }

    public int[][] getKalender() {
        return kalender;
    }
}
