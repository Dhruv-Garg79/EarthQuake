package com.example.android.earthquake;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.MyViewHolder> {

    private ArrayList earthquakeList;
    private Context context;

    EarthquakeAdapter(Context context, ArrayList list)
    {
        this.context = context;
        earthquakeList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_view,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        final Earthquake earthquake = (Earthquake) earthquakeList.get(i);

        GradientDrawable magnitudeCircle = (GradientDrawable) holder.magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        magnitudeCircle.setColor(getColorMag(earthquake.getMagnitude()));

        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String mag = decimalFormat.format(earthquake.getMagnitude());

        holder.near.setText(earthquake.getOffSet());
        holder.magnitude.setText(mag);
        holder.location.setText(earthquake.getLocation());
        holder.edate.setText(earthquake.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(earthquake.getUrl()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return earthquakeList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView magnitude;
        TextView edate;
        TextView near;
        TextView location;
        MyViewHolder(View listItem){
            super(listItem);
            magnitude = listItem.findViewById(R.id.magnitude);
            edate = listItem.findViewById(R.id.date);
            near = listItem.findViewById(R.id.near_the);
            location = listItem.findViewById(R.id.location);
        }
    }

    private int getColorMag(double magColor){
        int magnitudeColorResourceId;
        int magFloor = (int)Math.floor(magColor);
        switch(magFloor){
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(context, magnitudeColorResourceId);
    }
}


