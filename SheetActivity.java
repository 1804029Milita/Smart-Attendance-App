package com.example.smartattendance;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;

public class SheetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);

        showTable();
    }

    private void showTable() {
        DbHelper dbHelper= new DbHelper(this);
        TableLayout tableLayout= findViewById(R.id.tableLayout);
        long[] idArray=getIntent().getLongArrayExtra("idArray");
        int[] rollArray= getIntent().getIntArrayExtra("rollArray");
        String[] nameArray=getIntent().getStringArrayExtra("nameArray");
        String[] month= getIntent().getStringArrayExtra("month");

        int DAY_IN_MONTH = getDayInMonth(month);
        int rowSize=idArray.length+1;
        TableRow[] rows= new TableRow[rowSize];
        TextView[] rolls_tvs=new TextView[rowSize];
        TextView[] name_tvs=new TextView[rowSize];
        TextView[][] status_tvs= new TextView[rowSize][DAY_IN_MONTH+1];
        for(int i=0; i<rowSize;i++){
            rolls_tvs[i]= new TextView(this);
            name_tvs[i]= new TextView(this);
            for(int j=1 ; j<=DAY_IN_MONTH;j++){
                status_tvs[i][j]= new TextView(this);
            }
        }

        rolls_tvs[0].setText("Roll");
        rolls_tvs[0].setTypeface(rolls_tvs[0].getTypeface(), Typeface.BOLD);
        name_tvs[0].setText("Name");
        name_tvs[0].setTypeface(name_tvs[0].getTypeface(), Typeface.BOLD);
        for(int i=1; i<=DAY_IN_MONTH;i++){
            status_tvs[0][i].setText(String.valueOf(i));
            status_tvs[0][i].setTypeface(status_tvs[0][i].getTypeface(), Typeface.BOLD);
        }
        for(int i =1;i<rowSize;i++){
            rolls_tvs[i].setText(String.valueOf(rollArray[i-1]));
            name_tvs[i].setText(nameArray[i-1]);
            for(int j=1;j<=DAY_IN_MONTH;j++){
                String day = String.valueOf(j);
                if(day.length()==1) day= "0"+day;
                String date= day+ "." +month;
                String status= dbHelper.getStatus(idArray[i-1],date);
                status_tvs[i][j].setText(status);

            }
        }
        for(int i=0; i<rowSize;i++){

            rows[i].addView(rolls_tvs[i]);
            rows[i].addView(name_tvs[i]);

            for(int j=1;j<=DAY_IN_MONTH;j++){
                rows[i].addView(status_tvs[i][j]);
            }
            tableLayout.addView(rows[i]);

        }
    }

    private int getDayInMonth(String[] monthArray) {
        String month = monthArray[0];
        int monthIndex= Integer.valueOf(month.substring(0,1));
        int year= Integer.valueOf(month.substring(4));

        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.MONTH, monthIndex - 1); // Adjusting for zero-based indices
        calendar.set(Calendar.YEAR, year);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

}