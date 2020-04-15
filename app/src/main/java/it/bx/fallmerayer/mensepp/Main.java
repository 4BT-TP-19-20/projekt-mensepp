package it.bx.fallmerayer.mensepp;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class Main {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void main(String[] args) {
        Schulkalender schulkalender=new Schulkalender();
        schulkalender.dateiToKalender();
    }
}
