package it.bx.fallmerayer.mensepp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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

public class MenuActivity extends AppCompatActivity {
    private ImageView minusDays;//werden als Buttons verwendet
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
    private int lastDay3 = date.getDate();//braucht man um die zukünftigen Mittagessen zu wissen
    private int lastDay1 = date.getDate();//braucht man um die vergangenen Mittagessen zu wissen
    private int lastMonth3 = date.getMonth();//braucht man um die zukünftigen Mittagessen zu wissen
    private int lastMonth1 = date.getMonth();//braucht man um die vergangenen Mittagessen zu wissen

    int[] month = new int[3];
    int[] day = new int[3];

    Schulkalender schulkalender = null;
    Mensa mensa = null;

    @SuppressLint("SourceLockedOrientationActivity")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Portrait only modus

        try {
            loadFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            public void onClick(View v) {//wenn aufs Minus Button gedrückt wird
                setDaysBack();
            }
        });

        plusDays = findViewById(R.id.plus);
        plusDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//wenn aufs Plus Button gedrückt wird
                setDaysFurther();
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

        updateTextviews();
    }

    @Override
    public void finish() {//mann kann nicht zurück gehen, weil die Start Activity das "Ladebildschirm" sein sollte
    }

    private void openActivityHelp(View v) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void openActivityAttention(View v) {
        Intent intent = new Intent(this, AttentionActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadFiles() throws IOException {
        InputStream inputStreamKalender = getAssets().open("schulkalender.csv");//Im assets Ordner ist diese Datei abgespeichert
        schulkalender = new Schulkalender(inputStreamKalender);
        schulkalender.dateiToKalender();

        InputStream inputStreamMensa = getAssets().open("wochenplan.csv");
        mensa = new Mensa(schulkalender.getSchulkalender(), inputStreamMensa);
        mensa.dateiToMensaplan();
        mensa.schulkalenderMitEssenFuellen();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setDaysFurther() {
        goingBack = false;
        lastDay3 += 1;
        if (lastDay3 > 31) {//wenn neuer Monat
            lastDay3 = 1;
            if (lastMonth3 == 11)//wenn neues Jahr
                lastMonth3 = 0;
            else
                lastMonth3++;
        }
        updateTextviews();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setDaysBack() {
        goingBack = true;
        lastDay1 -= 1;
        if (lastDay1 < 1) {
            lastDay1 = 31;
            if (lastMonth1 == 0)
                lastMonth1 = 11;
            else
                lastMonth1--;
        }
        updateTextviews();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateTextviews() {
        if (goingBack) {
            setDaysMonthsBack();
        } else {
            setDaysMonthsForward();
        }

        lastMonth1 = month[0];
        lastDay1 = day[0];

        lastMonth3 = month[2];
        lastDay3 = day[2];

        updateEssen();
        updateDate();
    }

    private void setDaysMonthsForward() {
        month[0] = lastMonth3;
        day[0] = lastDay3;
        checkNewDayNewMonthForward(0);
        while (mensa.getEssen(month[0], day[0]) == null) {//wenn es am diesem Tag keine Schule gibt
            day[0] += 1;//+1 wenn keine Schule ist
            checkNewDayNewMonthForward(0);
        }

        month[1] = month[0];
        day[1] = day[0] + 1;
        checkNewDayNewMonthForward(1);
        while (mensa.getEssen(month[1], day[1]) == null) {//wenn es am diesem Tag keine Schule gibt
            day[1] += 1;
            checkNewDayNewMonthForward(1);
        }

        month[2] = month[1];
        day[2] = day[1] + 1;
        checkNewDayNewMonthForward(2);
        while (mensa.getEssen(month[2], day[2]) == null) {//wenn es am diesem Tag keine Schule gibt
            day[2] += 1;
            checkNewDayNewMonthForward(2);
        }
    }

    private void setDaysMonthsBack() {
        month[2] = lastMonth1;
        day[2] = lastDay1;
        checkNewDayNewMonthBack(2);
        while (mensa.getEssen(month[2], day[2]) == null) {//wenn es am diesem Tag keine Schule gibt
            day[2] -= 1;
            checkNewDayNewMonthBack(2);
        }

        month[1] = month[2];
        day[1] = day[2] - 1;
        checkNewDayNewMonthBack(1);
        while (mensa.getEssen(month[1], day[1]) == null) {//wenn es am diesem Tag keine Schule gibt
            day[1] -= 1;
            checkNewDayNewMonthBack(1);
        }

        month[0] = month[1];
        day[0] = day[1] - 1;
        checkNewDayNewMonthBack(0);
        while (mensa.getEssen(month[0], day[0]) == null) {//wenn es am diesem Tag keine Schule gibt
            day[0] -= 1;
            checkNewDayNewMonthBack(0);
        }
    }

    private void checkNewDayNewMonthForward(int index) {
        if (day[index] > 31) {//neuer Monat
            day[index] = 1;
            if (month[index] == 11)
                month[index] = 0;
            else
                this.month[index]++;
        }
    }

    private void checkNewDayNewMonthBack(int index) {
        if (day[index] < 1) {//neuer Monat
            day[index] = 31;
            if (month[index] == 0)
                month[index] = 11;
            else
                month[index]--;
        }
    }

    private void updateEssen() {
        essen1.setText(mensa.getEssen(month[0], day[0]));
        essen2.setText(mensa.getEssen(month[1], day[1]));
        essen3.setText(mensa.getEssen(month[2], day[2]));
    }

    @SuppressLint("SetTextI18n")
    private void updateDate() {
        if (date.getDate() == day[0] && date.getMonth() == month[0]) {
            datum1.setText("Heute");
            datum1.setTextColor(Color.parseColor("#FFCA84"));
            timeLeft.setText(getTimeLeft());
        } else {
            timeLeft.setText(" ");
            datum1.setTextColor(Color.parseColor("#FFFFFF"));
            datum1.setText(day[0] + "." + (month[0]+1) + " " + schulkalender.getWochentag(month[0], day[0]));
        }
        datum2.setText(day[1] + "." + (month[1]+1) + " " + schulkalender.getWochentag(month[1], day[1]));
        datum3.setText(day[2] + "." + (month[2]+1) + " " + schulkalender.getWochentag(month[2], day[2]));
    }

    private String getTimeLeft() {
        if (date.getHours() < 9) {
            if (date.getHours() == 8) {
                timeLeft.setTextColor(Color.parseColor("#E5A34D"));
                return "noch: " + (60 - date.getMinutes()) + "min";
            } else {
                return "noch: " + (9-date.getHours()) + "h und " + + (60 - date.getMinutes()) + "min";
            }
        } else
            timeLeft.setTextColor(Color.parseColor("#C54848"));
        return "zu spät";
    }
}
