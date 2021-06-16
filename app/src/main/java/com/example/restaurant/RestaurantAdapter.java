package com.example.restaurant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView RestaurantName, Restaurantlocation;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

             imageView = itemView.findViewById(R.id.detail_restaurant_image);
            RestaurantName = itemView.findViewById(R.id.detail_restaurant_name);
            Restaurantlocation = itemView.findViewById(R.id.detail_restaurant_location);

        }
    }
    private Context mcontext;
    private List<UpdateRestaurant> RestaurantList;


    public RestaurantAdapter(Context context, List<UpdateRestaurant> RestaurantList) {
        this.RestaurantList = RestaurantList;
        this.mcontext = context;
    }


    @NonNull
    @NotNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.restaurant_detail, parent, false);
        return new RestaurantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RestaurantAdapter.ViewHolder holder, int position) {
        final UpdateRestaurant displayRestaurant = RestaurantList.get(position);
        Glide.with(mcontext).load(displayRestaurant.getImageURL()).into(holder.imageView);
        holder.RestaurantName.setText(displayRestaurant.getName());
        holder.Restaurantlocation.setText(displayRestaurant.getAddress());
        displayRestaurant.getRandomUID();
        displayRestaurant.getRestaurantId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(mcontext, "Good scene", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mcontext, RestaurantReservation.class);
                intent.putExtra("Name",displayRestaurant.getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return RestaurantList.size();    }

}

