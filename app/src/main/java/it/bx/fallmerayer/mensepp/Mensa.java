package it.bx.fallmerayer.mensepp;

public class Mensa {
    /*private String[][] mensaplan = new String[7][5];
    private Schulkalender schulkalender = new Schulkalender(".csv");
    private String[][] schulkalender = new String[7][5];
    private String[][] schulkalederMitEssen = new String[12][31];

    public void schulkalenderMitEssenFuellen() {
        int wochenPointer = 0;
        int lastSchultag = 0;
        int ferienTage = 0;
        for (int monat = 8; monat < 12; monat++) {
            for (int tag = 0; tag < 31; tag++) {
                if (schulkalender[monat][tag] > 0) {
                    if (wochenPointer == 7) {
                        wochenPointer = 0;
                    }
                    if (schulkalender[monat][tag] <= lastSchultag) {
                        wochenPointer += 1;
                    } else if (ferienTage >= 7) {
                        wochenPointer += 1;
                    }
                    schulkalederMitEssen[monat][tag] = mensaplan[wochenPointer][tag];
                    lastSchultag = schulkalender[monat][tag];
                    ferienTage = 0;
                } else if (schulkalender[monat][tag] == 0) {
                    ferienTage += ferienTage;
                }
                if (monat == 11 && tag == 30) {
                    monat = -1;
                }
            }
        }
    }*/
}
