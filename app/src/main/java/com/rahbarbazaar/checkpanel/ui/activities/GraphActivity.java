package com.rahbarbazaar.checkpanel.ui.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rahbarbazaar.checkpanel.R;
import com.rahbarbazaar.checkpanel.models.message.Message;
import com.rahbarbazaar.checkpanel.utilities.MyValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {


    BarChart chart;
    ArrayList<BarEntry> BARENTRY;
    ArrayList<String> BarEntryLabels;
    BarDataSet Bardataset;
    BarData BARDATA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        initView();


        BARENTRY = new ArrayList<>();





        BarEntryLabels = new ArrayList<String>();
        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();
        Bardataset = new BarDataSet(BARENTRY, "Projects");
        BARDATA = new BarData(BarEntryLabels, Bardataset);



//        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        int[] colors = new int[]{
//https://www.rapidtables.com/web/color/RGB_Color.html  to get RDBcolorCodes
                Color.argb(255, 79, 129, 189) //blue
                , Color.argb(255, 155, 187, 89)   //green
                , Color.argb(255, 128, 100, 162)   // violet
                , Color.argb(255, 192, 80, 77)      //red
                , Color.argb(255, 221, 157, 157)    //pink
                , Color.argb(255, 219, 132, 61)    //orange
                , Color.argb(255, 66, 152, 175)};     //blue_green mixed
//        Bardataset.setColors(new int[] {Color.BLUE, Color.GREEN, Color.GRAY, Color.RED});
        Bardataset.setColors(colors);




        chart.setData(BARDATA);
        chart.animateY(1000);

        //to show all top labels
        XAxis xAxis=chart.getXAxis();
        xAxis.setLabelsToSkip(0);


//change font and color of labels
        Typeface font = Typeface.createFromAsset(GraphActivity.this.getAssets(), "IRANSansMobile.ttf");
        xAxis.setTypeface(font);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.argb(255, 0, 51 ,102));
        xAxis.setEnabled(true);




        chart.setDescription("");

        //remove the word project and colors bottomLeft
        chart.getLegend().setEnabled(false);


// to remove endLeft and endRight values
        YAxis yAxisRight = chart.getAxisRight();

        //to remove just rightEndValues and remove backgroung grid
        yAxisRight.setDrawLabels(false);
        yAxisRight.setEnabled(false);
        YAxis xAxisLeft = chart.getAxisLeft();
        xAxisLeft.setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);


        //to convert float to int for all colomn
        BARDATA.setValueFormatter(new MyValueFormatter());


//// padding (make chart small)
//        chart.setExtraLeftOffset(30);
//        chart.setExtraRightOffset(30);


    }

    private void initView() {

        chart =findViewById(R.id.chart);
    }

    public void AddValuesToBARENTRY() {
//        BARENTRY.add(new BarEntry(Float.parseFloat(String.format("%.0f",17.1)), 0));
        BARENTRY.add(new BarEntry(16f, 0));
        BARENTRY.add(new BarEntry(14f, 1));
        BARENTRY.add(new BarEntry(13f, 2));
        BARENTRY.add(new BarEntry(15f, 3));
    }


    public void AddValuesToBarEntryLabels() {
        BarEntryLabels.add("تعدا کل خرید");
        BarEntryLabels.add("خریدهای ناقص");
        BarEntryLabels.add("موجودی");
        BarEntryLabels.add("برگشتی");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
