package it.bx.fallmerayer.mensepp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public class Mensa {
    private String[][] mensaplan = new String[6][5];//6 wochen und 5 tage
    private int[][] schulkalenderInt;
    private String[][] schulkalederMitEssen = new String[12][31];

    public Mensa(int[][] schulkalender) {
        this.schulkalenderInt = schulkalender;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void dateiToMensaplan(String dateiname){
        String[] values;

        try (BufferedReader br = new BufferedReader(new FileReader(dateiname))) {
            String line;
            for (int woche = 0; woche < 6; woche++) {
                line = br.readLine();
                values = line.split(";");
                for (int tage = 0; tage < 5; tage++) {//int Integer umwandeln
                    mensaplan[woche][tage] = values[tage];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void schulkalenderMitEssenFuellen() {
        int wochenPointer = 0;
        int lastSchultag = 0;
        int ferienTage = 0;
        for (int monat = 8; monat < 12; monat++) {
            for (int tag = 0; tag < 31; tag++) {
                if (schulkalenderInt[tag][monat] > 0) {

                    if (schulkalenderInt[tag][monat] <= lastSchultag) {
                        wochenPointer += 1;
                    } else if (ferienTage >= 7) {
                        wochenPointer += 1;
                    }
                    if (wochenPointer == 6) {
                        wochenPointer = 0;
                    }
                    schulkalederMitEssen[monat][tag] = mensaplan[wochenPointer][schulkalenderInt[tag][monat]-1];
                    lastSchultag = schulkalenderInt[tag][monat];
                    ferienTage = 0;
                } else if (schulkalenderInt[tag][monat] == 0) {
                    ferienTage += ferienTage;
                }
                if (monat == 11 && tag == 30) {
                    monat = -1;
                } else if (monat == 7) {
                    return;
                }
            }
        }
    }

    public String getEssen(Date time) {
        return schulkalederMitEssen[time.getMonth()][time.getDate() - 1];
    }
}
