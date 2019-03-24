package app.mego.bluetoothsend;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by nitinsinghal on 11/03/19.
 */

public class PlantAdaper extends RecyclerView.Adapter<PlantAdaper.MyViewHolder> {



    ArrayList<PlantInforModel> arrayList;

    public PlantAdaper(ArrayList<PlantInforModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_client, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PlantInforModel client=arrayList.get(position);
        if(position==0){
            holder.clientid.setTextColor(Color.BLACK);
            holder.email.setTextColor(Color.BLACK);
            holder.name.setTextColor(Color.BLACK);
        }
        else {
            holder.clientid.setTextColor(Color.GRAY);
            holder.email.setTextColor(Color.GRAY);
            holder.name.setTextColor(Color.GRAY);
        }
        holder.clientid.setText(client.getPLANT());
        holder.name.setText(client.getPH());
        holder.email.setText(client.getEC());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView clientid, username, email, phoneno, name;

        public MyViewHolder(View view) {
            super(view);
            clientid = view.findViewById(R.id.id);
            email = view.findViewById(R.id.email);
            name = view.findViewById(R.id.name);
        }

    }
}
