package it.bx.fallmerayer.mensepp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

public class StartseiteActivity extends AppCompatActivity {
    private ImageView minusDays;
    private ImageView plusDays;
    private ImageView help;
    private ImageView attention;

    private TextView timeLeft;

    private TextView datum1;
    private TextView datum2;
    private TextView datum3;

    private TextView essen1;
    private TextView essen2;
    private TextView essen3;

    Date date = Calendar.getInstance().getTime();
    private boolean goingBack = false;
    private int lastDay3 = date.getDate();//wenn man die zukünftigen Mittagessen wissen will
    private int lastDay1 = date.getDate();//wenn man die vergangenen Mittagessen wissen will
    private int lastMonth3 = date.getMonth();//wenn man die zukünftigen Mittagessen wissen will
    private int lastMonth1 = date.getMonth();//wenn man die vergangenen Mittagessen wissen will


    @SuppressLint("SourceLockedOrientationActivity")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startseite);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        timeLeft = findViewById(R.id.timeleft);

        datum1 = findViewById(R.id.datum1);
        datum2 = findViewById(R.id.datum2);
        datum3 = findViewById(R.id.datum3);

        essen1 = findViewById(R.id.essen1);
        essen2 = findViewById(R.id.essen2);
        essen3 = findViewById(R.id.essen3);

        minusDays = findViewById(R.id.minus);
        minusDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//zurück schauen
                goingBack = true;
                lastDay1 -= 1;
                if (lastDay1 < 1) {
                    lastDay1 = 31;
                    if (lastMonth1 == 0)
                        lastMonth1 = 11;
                    else
                        lastMonth1--;
                }
                setEssenDatum();
            }
        });

        plusDays = findViewById(R.id.plus);
        plusDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//in die Zukunft schauen
                goingBack = false;
                lastDay3 += 1;
                if (lastDay3 > 31) {
                    lastDay3 = 1;
                    if (lastMonth3 == 11)
                        lastMonth3 = 0;
                    else
                        lastMonth3++;
                }
                setEssenDatum();
            }
        });

        attention = findViewById(R.id.attention);
        attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityAttention(v);
            }
        });

        help = findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityHelp(v);
            }
        });

        setEssenDatum();
    }

    @Override
    public void finish() {
    }

    private void openActivityHelp(View v){
        Intent intent = new Intent(this, Help.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void openActivityAttention(View v){
        Intent intent = new Intent(this, Attention.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setEssenDatum() {
        try {
            InputStream inputStreamKalender = getAssets().open("schulkalender.csv");
            Schulkalender schulkalender = new Schulkalender(inputStreamKalender);
            schulkalender.dateiToKalender();

            InputStream inputStreamMensa = getAssets().open("wochenplan.csv");
            Mensa mensa = new Mensa(schulkalender.getSchulkalender(), inputStreamMensa);
            mensa.dateiToMensaplan();
            mensa.schulkalenderMitEssenFuellen();

            int month1;
            int day1;

            int month2;
            int day2;

            int month3;
            int day3;

            if (goingBack) {
                month3 = lastMonth1;
                day3 = lastDay1;
                if (day3 < 1) {
                    day3 = 31;
                    if (month3 == 0)
                        month3 = 11;
                    else
                        month3--;
                }
                while (mensa.getEssen(month3, day3) == null) {//wenn es am diesem Tag keine Schule gibt
                    day3 -= 1;
                    if (day3 < 1) {
                        day3 = 31;
                        if (month3 == 0)
                            month3 = 11;
                        else
                            month3--;
                    }
                }

                month2 = month3;
                day2 = day3 - 1;
                if (day2 < 1) {
                    day2 = 31;
                    if (month2 == 0)
                        month2 = 11;
                    else
                        month2--;
                }
                while (mensa.getEssen(month2, day2) == null) {//wenn es am diesem Tag keine Schule gibt
                    day2 -= 1;
                    if (day2 < 1) {
                        day2 = 31;
                        if (month2 == 0)
                            month2 = 11;
                        else
                            month2--;
                    }
                }

                month1 = month2;
                day1 = day2 - 1;
                if (day1 < 1) {
                    day1 = 31;
                    if (month1 == 0)
                        month1 = 11;
                    else
                        month1--;
                }
                while (mensa.getEssen(month1, day1) == null) {//wenn es am diesem Tag keine Schule gibt
                    day1 -= 1;
                    if (day1 < 1) {
                        day1 = 31;
                        if (month1 == 0)
                            month1 = 11;
                        else
                            month1--;
                    }
                }
            } else {
                month1 = lastMonth3;
                day1 = lastDay3;
                if (day1 > 31) {
                    day1 = 1;
                    if (month1 == 11)
                        month1 = 0;
                    else
                        month1++;
                }
                while (mensa.getEssen(month1, day1) == null) {//wenn es am diesem Tag keine Schule gibt
                    day1 += 1;
                    if (day1 > 31) {
                        day1 = 1;
                        if (month1 == 11)
                            month1 = 0;
                        else
                            month1++;
                    }
                }

                month2 = month1;
                day2 = day1 + 1;
                if (day2 > 31) {
                    day2 = 1;
                    if (month2 == 11)
                        month2 = 0;
                    else
                        month2++;
                }
                while (mensa.getEssen(month2, day2) == null) {//wenn es am diesem Tag keine Schule gibt
                    day2 += 1;
                    if (day2 > 31) {
                        day2 = 1;
                        if (month2 == 11)
                            month2 = 0;
                        else
                            month2++;
                    }
                }

                month3 = month2;
                day3 = day2 + 1;
                if (day3 > 31) {
                    day3 = 1;
                    if (month3 == 11)
                        month3 = 0;
                    else
                        month3++;
                }
                while (mensa.getEssen(month3, day3) == null) {//wenn es am diesem Tag keine Schule gibt
                    day3 += 1;
                    if (day3 > 31) {
                        day3 = 1;
                        if (month3 == 11)
                            month3 = 0;
                        else
                            month3++;
                    }
                }
            }
            essen1.setText(mensa.getEssen(month1, day1));
            essen2.setText(mensa.getEssen(month2, day2));
            essen3.setText(mensa.getEssen(month3, day3));

            lastMonth1 = month1;
            lastDay1 = day1;

            lastMonth3 = month3;
            lastDay3 = day3;


            if (date.getDate() == day1 && date.getMonth() == month1) {
                datum1.setText("Heute");
                timeLeft.setText(getTimeLeft());
            } else {
                timeLeft.setText(" ");
                datum1.setText(day1 + " " + schulkalender.getMonat(month1));
            }
            datum2.setText(day2 + " " + schulkalender.getMonat(month2));
            datum3.setText(day3 + " " + schulkalender.getMonat(month3));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTimeLeft() {
            if (date.getHours() < 9) {
                if (date.getHours() == 8) {
                    return "noch: " + (60 - date.getMinutes()) + "min";
                } else {
                    if (date.getMinutes() < 30) {
                        return "noch ca.: " + (9 - date.getHours()) + "h";
                    } else {
                        return "noch ca.: " + (8 - date.getHours()) + "h";
                    }
                }
            } else {
            return "zu spät :(";
        }
    }
}
