package com.example.vluadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BillRecyclerAdapter extends RecyclerView.Adapter<BillRecyclerAdapter.BillViewHolder> {
    private ArrayList<Billitems> _billList;
    public static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView txt_user, txt_date;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_user = itemView.findViewById(R.id.cv_user);
            txt_date = itemView.findViewById(R.id.cv_date);
        }
    }

    public BillRecyclerAdapter(ArrayList<Billitems> billList) {
        _billList = billList;
    }

    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        BillViewHolder billViewHolder = new BillViewHolder(v);
        return billViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Billitems currentItem = _billList.get(position);

        holder.txt_user.setText(currentItem.get_cvUser());
        holder.txt_date.setText(currentItem.get_cvDate());
    }

    @Override
    public int getItemCount() {
        return _billList.size();
    }


}
