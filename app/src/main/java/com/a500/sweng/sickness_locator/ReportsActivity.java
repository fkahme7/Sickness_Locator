package com.a500.sweng.sickness_locator;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.a500.sweng.sickness_locator.common.Constants;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportsActivity extends AppCompatActivity {
        //implements AdapterView.OnItemSelectedListener {
    //private static final String TAG = "ListDatasetsActivity";

    //protected GlobalCache gCache = GlobalCache.getInstance();

    FirebaseDatabase db;
    //private DatasetsAdapter adapter;
    private TextView tvTitle;
    boolean mergeInProgress = false;
    ProgressDialog dialog;
    String DatasetName="";
    private ArrayList<String> Month,xAxis,City;
    List<String> sick = new ArrayList<String>();
    List<Integer> daysSick = new ArrayList<Integer>();
    List<String> sickList = new ArrayList<String>();
    List<String> latList = new ArrayList<String>();
    List<String> checkedSickList = new ArrayList<String>();
    List<Integer> sickCountList = new ArrayList<Integer>();

    List<String> severityList = new ArrayList<String>();
    ArrayList<String> pieLabels3;
    ArrayList<Entry> entries = new ArrayList<>();
    String diseaseName;
    PieData data;

    BarChart chart;
    BarChart predictiveChart;
    LineChart linechart;
    PieChart pieChart;
    String buttonString;
    TextView timeSeries;
    TextView barChart;
    TextView predictiveReport;
    TextView customChart;
    Spinner disease;

    int sickCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        db = FirebaseDatabase.getInstance();

        timeSeries = (TextView) findViewById(R.id.timeseries_txt);
        barChart   = (TextView) findViewById(R.id.barchart_txt);
        predictiveReport   = (TextView) findViewById(R.id.predictive_txt);
        customChart   = (TextView) findViewById(R.id.customChart_txt);
        timeSeries.setBackgroundResource(R.color.blue);
        barChart.setBackgroundResource(R.color.grey);
        predictiveReport.setBackgroundResource(R.color.grey);
        customChart.setBackgroundResource(R.color.grey);


        chart     = (BarChart) findViewById(R.id.chart);
        predictiveChart = (BarChart) findViewById(R.id.predictive_chart);
        linechart = (LineChart) findViewById(R.id.line);
        pieChart  = (PieChart) findViewById(R.id.pie);
        disease = (Spinner) findViewById(R.id.disease_spinner);

//        serviceCall();

        new LoadGraphDetails().execute("");
        buttonString="Line";

      /*  if(buttonString.equals("Bar") || buttonString.equals("Line") || buttonString.equals("Predictive") || buttonString.equals("Pie")){
            chart.setVisibility(View.VISIBLE);
            linechart.setVisibility(View.GONE);
            pieChart.setVisibility(View.GONE);
            predictiveChart.setVisibility(View.GONE);
            disease.setVisibility(View.GONE);
           // serviceCall();
        }*/

        timeSeries.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonString = "Line";
                        chart.setVisibility(View.GONE);
                        linechart.setVisibility(View.VISIBLE);
                        pieChart.setVisibility(View.GONE);
                        predictiveChart.setVisibility(View.GONE);
                        disease.setVisibility(View.GONE);
                        timeSeries.setBackgroundResource(R.color.blue);
                        barChart.setBackgroundResource(R.color.grey);
                        predictiveReport.setBackgroundResource(R.color.grey);
                        customChart.setBackgroundResource(R.color.grey);
                        //serviceCall();
                        refreshListData2();
                    }
                });
        barChart.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonString = "Bar";
                        chart.setVisibility(View.VISIBLE);
                        linechart.setVisibility(View.GONE);
                        pieChart.setVisibility(View.GONE);
                        predictiveChart.setVisibility(View.GONE);
                        disease.setVisibility(View.GONE);
                      //  serviceCall();
                        refreshListData2();
                    }
                });
        predictiveReport.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonString = "Predictive";
                        chart.setVisibility(View.GONE);
                        linechart.setVisibility(View.GONE);
                        pieChart.setVisibility(View.GONE);
                        predictiveChart.setVisibility(View.VISIBLE);
                        disease.setVisibility(View.GONE);
                      //  serviceCall();
                        refreshListData2();
                    }
                });
        customChart.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonString = "Pie";
                        chart.setVisibility(View.GONE);
                        linechart.setVisibility(View.GONE);
                        pieChart.setVisibility(View.VISIBLE);
                        predictiveChart.setVisibility(View.GONE);
                        disease.setVisibility(View.GONE);
                      //  serviceCall();
                        refreshListData2();
                    }
                });

    }

    class LoadGraphDetails extends AsyncTask<String, Void, Void>{

        ProgressDialog mProgressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ReportsActivity.this);
            mProgressDialog.setCancelable(false);
            if (mProgressDialog != null) {
                mProgressDialog.setMessage("Getting details");
                mProgressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            serviceCall();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshListData2();
                }
            },5000);
        }

    }

    private void serviceCall() {

        City=new ArrayList<>();
        Month=new ArrayList<>();
        xAxis=new ArrayList<>();
        Month.add("Malariya");
        Month.add("Jaundice");
        Month.add("Dengue");



        xAxis.add("Flu");
        xAxis.add("Shingles");
        xAxis.add("Mumps");
        xAxis.add("Chickenpox");

        City.add("New York");
        City.add("Boston");
        City.add("Seattle");
        City.add("Austin");
        City.add("Miami");

       /* daysSick.add(1);
        daysSick.add(2);
        daysSick.add(4);
        daysSick.add(3);
        daysSick.add(5);*/



        final DatabaseReference ref = db.getReference("sicknessEntries");
        //ref.addValueEventListener(new ValueEventListener() {
        //    @Override
        //    public void onDataChange(DataSnapshot dataSnapshot) {
        // Is better to use a List, because you don't know the size
        // of the iterator returned by dataSnapshot.getChildren() to
        // initialize the array

        Query queryRef = ref.orderByChild("sickness");

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChild) {
                System.out.println(dataSnapshot.getValue());
                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();

                //SicknessEntry sicknessEntry = gson.fromJson(value, SicknessEntry.class);
                //gCache.setSickEntry(sicknessEntry);

                String name1 = String.valueOf(value.get("sickness"));
                String name2 = String.valueOf(value.get("latitude"));
                Log.i("sickness", String.valueOf(value.get("sickness")));

                latList.add(name2);
                sickList.add(name1);
                Log.i("groupList Array ", sickList.toString());

                for (int i = 0; i < sickList.size(); i++)
                {
                    String sick = sickList.get(i);
                    if (!checkedSickList.contains(sick))
                    {
                        Set<String> unique = new HashSet<String>(sickList);
                        for (String key : unique) {
                            sickCount = Collections.frequency(sickList, key);
                            Log.i("sickcountLoop", String.valueOf(sickCount));
                            sickCountList.add(sickCount);
                        }


                        //sickCount = Collections.frequency(sickList, sick);
                        Log.i("sickcountViswa", String.valueOf(sickCount));
                        checkedSickList.add(sick);

                    }
                }

                Log.i("sickList", sickList.toString());
                Log.i("checkedSickLvist", checkedSickList.toString());
                Log.i("sickCount", sickCountList.toString());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i("groupList Array ", "onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i("groupList Array ", "onChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.i("groupList Array ", "onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("groupList Array ", "onChildCancelled");
            }
        });





    }

 /* @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        diseaseName = disease.getSelectedItem().toString();
        //Log.i("Selected item : ", items);

//        data.clearValues();
        entries.clear();

        if(diseaseName.equals("All")) {
            for (Record record1 : dataset.getAllRecords()) {
                if (record1.getValue() != null) {
                    String value = record1.getValue();
                    //entries.add(new Entry(Float.parseFloat(value), i));
                    entries.add(new Entry(Float.parseFloat(value), i));
                }
                i = i + 1;

                PieDataSet dataset = new PieDataSet(entries, " ");
                data = new PieData(pieLabels, dataset);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(13f);
                data.setValueTextColor(Color.WHITE);
                data.setValueTypeface(Typeface.DEFAULT_BOLD);
                dataset.setColors(Constants.CHART_COLORS); //
                dataset.setSliceSpace(3f);
                pieChart.setDescription("Description");
                pieChart.setData(data);
                // pieChart.setHoleRadius(0f);
                pieChart.setDrawHoleEnabled(false);
                pieChart.setUsePercentValues(false);
                pieChart.animateY(2000);
            }
        }else if(diseaseName.equals("Malaria")) {
            float value = Float.parseFloat(dataset.get("1"));
            //entries.add(new Entry(Float.parseFloat(value), i));

            Log.i("disease", String.valueOf(value));
            entries.add(new Entry((float)value, 0));
            //i = i + 1;

            PieDataSet dataset = new PieDataSet(entries, " ");
            data = new PieData(pieLabels1, dataset);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(13f);
            data.setValueTextColor(Color.WHITE);
            data.setValueTypeface(Typeface.DEFAULT_BOLD);
            dataset.setColors(Constants.CHART_COLORS); //
            dataset.setSliceSpace(0f);
            pieChart.setDescription("Description");
            pieChart.setData(data);
            //pieChart.setDrawSliceText(false);

            // pieChart.setHoleRadius(0f);
            pieChart.setDrawHoleEnabled(false);
            pieChart.setUsePercentValues(false);
            pieChart.animateY(2000);

        }else if(diseaseName.equals("Dengue")) {
            String value = dataset.get("2");
            //entries.add(new Entry(Float.parseFloat(value), i));
            entries.add(new Entry(Integer.parseInt(value), 1));

            //i = i + 1;


            PieDataSet dataset = new PieDataSet(entries, " ");
            PieData data = new PieData(pieLabels2, dataset);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(13f);
            data.setValueTextColor(Color.WHITE);
            data.setValueTypeface(Typeface.DEFAULT_BOLD);
            dataset.setColors(Constants.CHART_COLORS); //
            dataset.setSliceSpace(0f);
            pieChart.setDescription("Description");
            pieChart.setData(data);
            // pieChart.setHoleRadius(0f);
            pieChart.setDrawHoleEnabled(false);

            pieChart.setUsePercentValues(false);
            pieChart.animateY(2000);

        }else if(diseaseName.equals("Jaundice")) {
            String value = dataset.get("3");
            //entries.add(new Entry(Float.parseFloat(value), i));
            entries.add(new Entry(Integer.parseInt(value), 2));

            //i = i + 1;


            PieDataSet dataset = new PieDataSet(entries, " ");
            data = new PieData(pieLabels3, dataset);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(13f);
            data.setValueTextColor(Color.WHITE);
            data.setValueTypeface(Typeface.DEFAULT_BOLD);
            dataset.setColors(Constants.CHART_COLORS); //
            dataset.setSliceSpace(0f);
            pieChart.setDescription("Description");
            pieChart.setData(data);
            // pieChart.setHoleRadius(0f);
            pieChart.setDrawHoleEnabled(false);
            pieChart.setUsePercentValues(false);
            pieChart.animateY(2000);
        }



    }*/

  /*  @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }*/

    private void refreshListData2() {
        //adapter.clear();
        entries.clear();
        ArrayList<BarEntry> valueSet1 = null,valueSet2 = null,valueSet3 = null;
        ArrayList<BarDataSet> dataSets = null;
        valueSet1 = new ArrayList<>();
        valueSet2 = new ArrayList<>();
        valueSet3 = new ArrayList<>();
        //ArrayList<EntryYAxis> entries = new ArrayList<>();
        int i = 0;
        if(buttonString.equals("Line")){
            timeSeries.setBackgroundResource(R.color.blue);
            barChart.setBackgroundResource(R.color.grey);
            predictiveReport.setBackgroundResource(R.color.grey);
            customChart.setBackgroundResource(R.color.grey);

            //Log.i("daysSick Size ", String.valueOf(daysSick.size()));

            for (int j=0; j<sickCountList.size();j++) {
                    Integer value = sickCountList.get(j);
                    //entries.add(new Entry(Float.parseFloat(value), i));
                    entries.add(new Entry((value), i));
                    i = i + 1;
            }
            LineDataSet dataset = new LineDataSet(entries,"");
            LineData data = new LineData(xAxis, dataset);
            linechart.setData(data); // set the data and list of lables into chart
            linechart.setDescription("Description");
            dataset.setDrawFilled(false); // to fill the below area of line in graph
            dataset.setColor(Color.rgb(7, 71, 242));
            dataset.setCircleColor(Color.rgb(7, 71, 242));
            dataset.setDrawCircleHole(false);
            //linechart.set

            Legend l = linechart.getLegend();

            // modify the legend ...
            // l.setPosition(LegendPosition.LEFT_OF_CHART);
            l.setForm(Legend.LegendForm.LINE);

            YAxis leftAxis = linechart.getAxisLeft();
            leftAxis.setAxisMaxValue(5);
            leftAxis.setAxisMinValue(0);
            //leftAxis.setValueFormatter();
            //leftAxis.setYOffset(20f);

            XAxis xAxis = linechart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            linechart.getAxisRight().setEnabled(false);
            linechart.animateXY(2000, 2000);
            linechart.invalidate();// to change the color scheme

        }

        else if (buttonString.equals("Bar")){
            timeSeries.setBackgroundResource(R.color.grey);
            barChart.setBackgroundResource(R.color.blue);
            predictiveReport.setBackgroundResource(R.color.grey);
            customChart.setBackgroundResource(R.color.grey);


            for(int j=0; j<sickCountList.size(); j++){
                BarEntry v1e1 = new BarEntry(sickCountList.get(j), j); // Jan
                valueSet1.add(v1e1);

            }


            if (valueSet1 != null) {
                BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Flu");
                barDataSet1.setColor(Color.rgb(0, 0, 225));
               // BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Female");
               // barDataSet2.setColor(Color.rgb(225, 0, 0));

                dataSets = new ArrayList<>();
                dataSets.add(barDataSet1);
                //dataSets.add(barDataSet2);
                //adapter.notifyDataSetChanged();

                BarData data = new BarData(getXAxisValues(), dataSets);
                chart.setData(data);
                chart.setDescription(" ");
                XAxis xAxis = chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                chart.animateXY(2000, 2000);
                chart.invalidate();
            }
        }
        else if (buttonString.equals("Pie")){
            timeSeries.setBackgroundResource(R.color.grey);
            barChart.setBackgroundResource(R.color.grey);
            predictiveReport.setBackgroundResource(R.color.grey);
            customChart.setBackgroundResource(R.color.blue);

           // disease.setOnItemSelectedListener(this);

            for (int j=0; j<sickCountList.size();j++) {
                Integer value = sickCountList.get(j);
                //entries.add(new Entry(Float.parseFloat(value), i));
                entries.add(new Entry((value), i));
                i = i + 1;

                PieDataSet dataset = new PieDataSet(entries, " ");
                PieData data = new PieData(xAxis, dataset);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(13f);
                data.setValueTextColor(Color.WHITE);
                data.setValueTypeface(Typeface.DEFAULT_BOLD);
                dataset.setColors(Constants.CHART_COLORS); //
                dataset.setSliceSpace(3f);
                pieChart.setDescription("Description");
                pieChart.setData(data);
                // pieChart.setHoleRadius(0f);
                pieChart.setDrawHoleEnabled(false);
                pieChart.setUsePercentValues(true);
                pieChart.animateY(2000);
            }


            // pieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image

        }
        else {
            timeSeries.setBackgroundResource(R.color.grey);
            barChart.setBackgroundResource(R.color.grey);
            predictiveReport.setBackgroundResource(R.color.blue);
            customChart.setBackgroundResource(R.color.grey);

            for(int j=0; j<sickCountList.size(); j++){
                BarEntry v1e1 = new BarEntry(sickCountList.get(j), j); // Jan
                valueSet1.add(v1e1);

            }

            if (valueSet1 != null) {
                BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Flu");
                barDataSet1.setColor(Color.rgb(0, 155, 0));
                /*BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Jaundice");
                barDataSet2.setColor(Color.rgb(255, 127, 37));
                BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Dengue");
                barDataSet3.setColor(Color.rgb(154, 227, 160));
*/
                dataSets = new ArrayList<>();
                dataSets.add(barDataSet1);
               // dataSets.add(barDataSet2);
               // dataSets.add(barDataSet3);
                // adapter.notifyDataSetChanged();

                BarData data = new BarData(getXAxisValuesContent(), dataSets);
                predictiveChart.setData(data);
                predictiveChart.setDescription(" ");
                XAxis xAxis = predictiveChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                predictiveChart.getAxisRight().setEnabled(false);
                predictiveChart.animateXY(2000, 2000);
                predictiveChart.invalidate();
            }
        }

        // adapter.notifyDataSetChanged();
    }

    private ArrayList<String> getXAxisValuesContent() {

        return xAxis;
    }

    private ArrayList<String> getXAxisValues() {

        return xAxis;
    }




}
