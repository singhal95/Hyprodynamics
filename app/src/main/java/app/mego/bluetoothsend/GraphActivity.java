package app.mego.bluetoothsend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {
    GraphView graphph,graphrt,graphwt,graphwl;
    LineGraphSeries<DataPoint> seriesph,seriesrt,serieswt,serieswl;
    DataPoint dataPointph[],dataPointrt[],dataPointwt[],dataPointwl[];
    private Button READING,GRAPH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        graphph=findViewById(R.id.graphph);
        graphrt=findViewById(R.id.graphrt);
        graphwl=findViewById(R.id.graphwl);
        graphwt=findViewById(R.id.graphWt);
        GRAPH=findViewById(R.id.graph);
        READING=findViewById(R.id.readings);
        GRAPH.setEnabled(false);// set manual X bounds
        READING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GraphActivity.this,MainActivity.class));
                finish();
            }
        });
        graphph.getViewport().setYAxisBoundsManual(true);
        graphph.getViewport().setMinY(0);
        graphph.getViewport().setMaxY(30);
        graphph.getViewport().setXAxisBoundsManual(true);
        graphph.getViewport().setMinX(0);
        graphph.getViewport().setMaxX(5);
        graphph.getViewport().setScalable(true);
        graphph.getViewport().setScalableY(true);
        graphrt.getViewport().setYAxisBoundsManual(true);
        graphrt.getViewport().setMinY(0);
        graphrt.getViewport().setMaxY(30);
        graphrt.getViewport().setXAxisBoundsManual(true);
        graphrt.getViewport().setMinX(0);
        graphrt.getViewport().setMaxX(5);
        graphrt.getViewport().setScalable(true);
        graphrt.getViewport().setScalableY(true);
        graphwt.getViewport().setYAxisBoundsManual(true);
        graphwt.getViewport().setMinY(0);
        graphwt.getViewport().setMaxY(30);
        graphwt.getViewport().setXAxisBoundsManual(true);
        graphwt.getViewport().setMinX(0);
        graphwt.getViewport().setMaxX(5);
        graphwt.getViewport().setScalable(true);
        graphwt.getViewport().setScalableY(true);
        graphwl.getViewport().setYAxisBoundsManual(true);
        graphwl.getViewport().setMinY(0);
        graphwl.getViewport().setMaxY(30);
        graphwl.getViewport().setXAxisBoundsManual(true);
        graphwl.getViewport().setMinX(0);
        graphwl.getViewport().setMaxX(5);
        graphwl.getViewport().setScalable(true);
        graphwl.getViewport().setScalableY(true);
        dataPointph=new DataPoint[Constants.ph.size()];
        dataPointwt=new DataPoint[Constants.ph.size()];
        dataPointrt=new DataPoint[Constants.ph.size()];
        dataPointwl=new DataPoint[Constants.ph.size()];
        for (int i=0;i<Constants.ph.size();i++){
            dataPointph[i]=new DataPoint(i+1,Constants.ph.get(i));
            dataPointwt[i]=new DataPoint(i+1,Constants.wt.get(i));
            dataPointrt[i]=new DataPoint(i+1,Constants.RT.get(i));
            dataPointwl[i]=new DataPoint(i+1,Constants.WL.get(i));
        }


       seriesph=new LineGraphSeries<>(dataPointph);
        seriesrt=new LineGraphSeries<>(dataPointrt);
        serieswt=new LineGraphSeries<>(dataPointwt);
        serieswl=new LineGraphSeries<>(dataPointwl);
        graphph.addSeries(seriesph);
        graphrt.addSeries(seriesrt);
        graphwt.addSeries(serieswt);
        graphwl.addSeries(serieswl);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
