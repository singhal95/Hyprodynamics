package app.mego.bluetoothsend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PlantInfo extends AppCompatActivity {


    ArrayList<String> plants,ph,ec;
    ArrayList<PlantInforModel> plantInforModels;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_info);
        recyclerView=findViewById(R.id.recycler_view);
        plants=new ArrayList<>();
        ph=new ArrayList<>();
        ec=new ArrayList<>();
        plantInforModels=new ArrayList<>();
        plants.add("PLANTS");
        plants.add("Asparagus");
        plants.add("Lettuce");
        plants.add("Spinach");
        plants.add("Beetroot");
        plants.add("Carrots");
        plants.add("Potato");
        plants.add("Radish");
        plants.add("Cabbage");
        plants.add("Cucumber");
        plants.add("Okra");
        plants.add("Pea");
        plants.add("Tomato");
        plants.add("Dablia");
        plants.add("Roses");
        plants.add("Lavendes");
        ph.add("PH");
        ph.add("6.0-6.3");
        ph.add("5.5-6.5");
        ph.add("5,5-6,6");
        ph.add("6.0-6.5");
        ph.add("6.3");
        ph.add("5.0-6.0");
        ph.add("6.0-7.0");
        ph.add("6.5-7.0");
        ph.add("5.8-6.0");
        ph.add("6.5");
        ph.add("6.0-7.0");
        ph.add("5.5-6.5");
        ph.add("6.0-7.0");
        ph.add("5.5-6.0");
        ph.add("6.4-6.8");
        ec.add("EC");
        ec.add("1.4-1.8");
        ec.add("0.8-1.2");
        ec.add("1.8-2.3");
        ec.add("0.8-5.0");
        ec.add("1.6-2.0");
        ec.add("2.0-2.5");
        ec.add("1.6-2.2");
        ec.add("2.5-3.0");
        ec.add("1.7-2.5");
        ec.add("2.0-2.4");
        ec.add("0.8-1.8");
        ec.add("2.0-5.0");
        ec.add("1.5-2.0");
        ec.add("1.5-2.5");
        ec.add("1.0-1.4");
        for(int i=0;i<plants.size();i++){
            plantInforModels.add(new PlantInforModel(plants.get(i),ph.get(i),ec.get(i)));
        }
        PlantAdaper plantAdaper=new PlantAdaper(plantInforModels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(plantAdaper);
plantAdaper.notifyDataSetChanged();

    }
}
