package com.example.vluadmin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vluadmin.Model.Product;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class
RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    RecyclerView recyclerView;
    private static final String TAG ="loi";
    private Context mcontext;
    private ArrayList<Product> productsList;

    public RecyclerAdapter(Context mcontext, ArrayList<Product> productsList) {
        this.mcontext = mcontext;
        this.productsList = productsList;
        Log.d(TAG, "RecyclerAdapter: thanh cong");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.products_item_display,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


            holder.ten.setText(productsList.get(position).getPname());

            holder.gia.setText(productsList.get(position).getPrice() + " $");
            holder.mota.setText(productsList.get(position).getDescription());
            //image
            Glide.with(mcontext)
                    .load(productsList.get(position).getImage())
                    .into(holder.imgview);

        try {
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, AdminDetailProduct.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.putExtra("day_create",productsList.get(position).getDate());
                    intent.putExtra("time_create",productsList.get(position).getTime());
                    intent.putExtra("pname", productsList.get(position).getPname());
                    intent.putExtra("price", productsList.get(position).getPrice());
                    intent.putExtra("des", productsList.get(position).getDescription());
                    intent.putExtra("img", productsList.get(position).getImage());



                    mcontext.startActivity(intent);

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_product)
        ImageView imgview;

        @BindView(R.id.productName)
        TextView ten;

        @BindView(R.id.productDes)
        TextView mota;

        @BindView(R.id.productPrice)
        TextView gia;

        ConstraintLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            imgview =(ImageView) itemView.findViewById(R.id.img_product);
            ten =(TextView) itemView.findViewById(R.id.productName);
            gia = (TextView)itemView.findViewById(R.id.productPrice);
            mota =(TextView) itemView.findViewById(R.id.productDes);

            try {
                mainLayout = itemView.findViewById(R.id.mainLayout);
            }catch (Exception e){
                Log.d("abc", "ViewHolder: " +e.getMessage());
            }



        }
    }

}
