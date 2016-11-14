package com.a500.sweng.sickness_locator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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
    List<String> spinnerItems = new ArrayList<String>();
    String[] arrayOfStrings;
    List<String> selectedSickList = new ArrayList<String>();
    List<Integer> sickCountList = new ArrayList<Integer>();
    int selectedDisease;

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
    TextView predictiveReport, selectDisease;
    TextView customChart;
    Spinner disease;
    Boolean isLoaded = false;

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
        selectDisease = (TextView) findViewById(R.id.select_disease);
        buttonString="Line";
        spinnerItems.add("All");
        serviceCall();


//        new LoadGraphDetails().execute("");

        disease.setVisibility(View.VISIBLE);
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
                        selectDisease.setVisibility(View.GONE);
                        timeSeries.setBackgroundResource(R.color.blue);
                        barChart.setBackgroundResource(R.color.grey);
                        predictiveReport.setBackgroundResource(R.color.grey);
                        customChart.setBackgroundResource(R.color.grey);
                        //serviceCall();
                        if(!isLoaded) {
//                            loadXYvalues();
                        }
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
                        selectDisease.setVisibility(View.GONE);
                        //  serviceCall();
                        if(!isLoaded) {
//                            loadXYvalues();
                        }
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
                        selectDisease.setVisibility(View.GONE);
                        //  serviceCall();
                        if(!isLoaded) {
//                            loadXYvalues();
                        }
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
                        disease.setVisibility(View.VISIBLE);
//                        selectDisease.setVisibility(View.VISIBLE);
                        //  serviceCall();
                        if(!isLoaded) {
//                            loadXYvalues();
                        }

                        arrayOfStrings = checkedSickList.toArray(new String[checkedSickList.size()]);
                        refreshListData2();
                    }
                });

        selectDisease.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectDiseaseCall();
                    }
                });

    }

    private void selectDiseaseCall() {
        selectedSickList.clear();
        AlertDialog.Builder mAlertDialogBuilder;
        mAlertDialogBuilder = new AlertDialog.Builder(this);
        mAlertDialogBuilder.setMessage("Select Disease");
        mAlertDialogBuilder.setCancelable(false);
        mAlertDialogBuilder.setItems(arrayOfStrings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                String sick = arrayOfStrings[which];
                selectedSickList.add(sick);
            }
        });
        mAlertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        piechart();
                    }
                });
        mAlertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private void piechart() {

        float value = (sickCountList.get(selectedDisease));
        //entries.add(new Entry(Float.parseFloat(value), i));

        Log.i("disease", String.valueOf(value));
        entries.add(new Entry((float)value, 0));
        //i = i + 1;

        PieDataSet dataset = new PieDataSet(entries, " ");
        data = new PieData(selectedSickList, dataset);
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

    }

    private void loadXYvalues() {
        isLoaded=true;
        if(sickList!=null) {
            Set<String> uniqueSet = new HashSet<String>(sickList);
            for (String temp : uniqueSet) {
                int count = Collections.frequency(sickList, temp);
                System.out.println(temp + ": " + count);
                checkedSickList.add(temp);
                spinnerItems.add(temp);
                sickCountList.add(count);

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            disease.setAdapter(adapter);
            disease.setOnItemSelectedListener(this);
//            adapter.notifyDataSetChanged();
        }

        Log.i("sickList", sickList.toString());
        Log.i("spinnerItems", spinnerItems.toString());
        Log.i("sickCount", sickCountList.toString());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        diseaseName = parent.getItemAtPosition(position).toString();
        selectedDisease = parent.getSelectedItemPosition()-1;
        Log.i("Graph_item : ", String.valueOf(selectedDisease));
        Log.i("Graph_item : ", diseaseName);

//        data.clearValues();
        entries.clear();

        if(diseaseName.equals("All")) {
//            selectedSickList.add("All");

            for (int j=0; j<sickCountList.size();j++) {
                Integer value = sickCountList.get(j);
                //entries.add(new Entry(Float.parseFloat(value), i));
                entries.add(new Entry((value), position));
                position = position + 1;

                PieDataSet dataset = new PieDataSet(entries, " ");
                PieData data = new PieData(checkedSickList, dataset);
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
        }else{

            selectedSickList.add(diseaseName);

            float value = (sickCountList.get(selectedDisease));
            //entries.add(new Entry(Float.parseFloat(value), i));

            Log.i("disease", String.valueOf(value));
            entries.add(new Entry((float)value, 0));
            //i = i + 1;

            PieDataSet dataset = new PieDataSet(entries, " ");
            data = new PieData(selectedSickList, dataset);
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
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
//                    loadXYvalues();
                }
            },10000);
        }

    }

    private void serviceCall() {

        final DatabaseReference ref = db.getReference("sicknessEntries");

        Query queryRef = ref.orderByChild("sickness");

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChild) {
                System.out.println(dataSnapshot.getValue());
                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                String name1 = String.valueOf(value.get("sickness"));
                String name2 = String.valueOf(value.get("latitude"));
                Log.i("sickness", String.valueOf(value.get("sickness")));

                latList.add(name2);
                sickList.add(name1);
                Log.i("groupList Array ", sickList.toString());
                Log.i("sickness", "value = "+value);
                Log.i("sickness", "value size = "+value.size());
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


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    loadXYvalues();

                buttonString = "Line";
                chart.setVisibility(View.GONE);
                linechart.setVisibility(View.VISIBLE);
                pieChart.setVisibility(View.GONE);
                predictiveChart.setVisibility(View.GONE);
                disease.setVisibility(View.GONE);
                selectDisease.setVisibility(View.GONE);
                timeSeries.setBackgroundResource(R.color.blue);
                barChart.setBackgroundResource(R.color.grey);
                predictiveReport.setBackgroundResource(R.color.grey);
                customChart.setBackgroundResource(R.color.grey);
                refreshListData2();
            }
        },5000);
//        loadXYvalues();
//        refreshListData2();
        /*queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ...

                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                for(int i=0; i<value.size(); i++){

                    String name1 = String.valueOf(value.get(i));
                    String name2 = String.valueOf(value.get("latitude"));
                    Log.i("sickness", "name1 = "+name1);
                }
//                Log.i("sickness", String.valueOf(value.get("sickness")));
                Log.i("sickness", "value = "+value);
                Log.i("sickness", "value size = "+value.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });*/


    }


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
            LineData data = new LineData(checkedSickList, dataset);
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
                BarDataSet barDataSet1 = new BarDataSet(valueSet1, "");
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

            disease.setOnItemSelectedListener(this);

            for (int j=0; j<sickCountList.size();j++) {
                Integer value = sickCountList.get(j);
                //entries.add(new Entry(Float.parseFloat(value), i));
                entries.add(new Entry((value), i));
                i = i + 1;

                PieDataSet dataset = new PieDataSet(entries, " ");
                PieData data = new PieData(checkedSickList, dataset);
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

    private List<String> getXAxisValuesContent() {

        return checkedSickList;
    }

    private List<String> getXAxisValues() {

        return checkedSickList;
    }




}