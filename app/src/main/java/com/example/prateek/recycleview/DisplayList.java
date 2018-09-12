package com.example.prateek.recycleview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class DisplayList extends RecyclerView.Adapter<DisplayList.MyHolder>{

    ArrayList<String> arrTitle;
    ArrayList<String> arrDesc;
    ArrayList<String> arrImg;
    public int pos;
    Context context;

    public DisplayList(Context context,ArrayList<String> arrTitle,ArrayList<String> arrDesc,ArrayList<String> arrImg){
         this.context = context;
        this.arrTitle = arrTitle;
         this.arrDesc = arrDesc;
          this.arrImg = arrImg;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_data,null,true);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///Toast.makeText(holder.itemView.getContext(),"Position = " + (position+1),Toast.LENGTH_SHORT).show();
                String title = arrTitle.get(position);
                title.replace(" ","_");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://en.wikipedia.org/wiki/" + title));
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.txtTitle.setText(arrTitle.get(position).toString());
        if(arrDesc.get(position) != null){
            holder.txtDesc.setText(arrDesc.get(position));
        }

        if(arrImg.get(position) != null){
            ImageRequest imageRequest = new ImageRequest(arrImg.get(position), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Log.i("Bitmap",response.toString());
                holder.imgVw.setImageBitmap(response);
                Log.i("ImageSize",arrImg.size() + "");
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(imageRequest);
        }
    }

    @Override
    public int getItemCount() {
        return arrTitle.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{

        ImageView imgVw;
        TextView txtTitle;
        TextView txtDesc;

        int pos = 0;

        MyHolder(View view){
            super(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pos = getLayoutPosition();
                }
            });

            txtTitle = (TextView)view.findViewById(R.id.txtVwTitle);
             txtDesc = (TextView)view.findViewById(R.id.txtVwDesc);
               imgVw = (ImageView)view.findViewById(R.id.imgVw);
        }
    }
}
