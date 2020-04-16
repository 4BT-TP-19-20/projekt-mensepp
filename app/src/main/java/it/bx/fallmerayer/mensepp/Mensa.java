package it.bx.fallmerayer.mensepp;

import java.util.Date;

public class Mensa {
    private String[][] mensaplan = new String[7][5];
    private int[][] schulkalenderInt;
    private String[][] schulkalederMitEssen = new String[12][31];

    public Mensa(int[][] schulkalender) {
        this.schulkalenderInt = schulkalender;
    }

    public void schulkalenderMitEssenFuellen() {
        int wochenPointer = 0;
        int lastSchultag = 0;
        int ferienTage = 0;
        for (int monat = 8; monat < 12; monat++) {
            for (int tag = 0; tag < 31; tag++) {
                if (schulkalenderInt[monat][tag] > 0) {
                    if (wochenPointer == 7) {
                        wochenPointer = 0;
                    }
                    if (schulkalenderInt[monat][tag] <= lastSchultag) {
                        wochenPointer += 1;
                    } else if (ferienTage >= 7) {
                        wochenPointer += 1;
                    }
                    schulkalederMitEssen[monat][tag] = mensaplan[wochenPointer][tag];
                    lastSchultag = schulkalenderInt[monat][tag];
                    ferienTage = 0;
                } else if (schulkalenderInt[monat][tag] == 0) {
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
        int currentMonth = time.getMonth();
        int currentDay = time.getDate();

        return schulkalederMitEssen[currentDay - 1][currentMonth];
    }

}
