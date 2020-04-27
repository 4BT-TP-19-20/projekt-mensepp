package it.bx.fallmerayer.mensepp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Schulkalender {
    private int[][] kalender = new int[31][12];
    private String[] monate = {"Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};
    private InputStream inputStream = null;

    public Schulkalender(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void dateiToKalender() {
        String[] values;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
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

   public String getMonat(int month) {
        return monate[month];
    }
}
