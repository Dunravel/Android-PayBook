package com.example.miskra.paybook;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by miskra on 26.05.2017.
 */

public class PaymentsListAdapter extends RecyclerView.Adapter<PaymentsListAdapter.MyViewHolder> {

    private List<Payment> paymentsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product, type, shop, amount, date;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            //product = (TextView) view.findViewById(R.id.product);
            type = (TextView) view.findViewById(R.id.type);
            shop = (TextView) view.findViewById(R.id.shop);
            amount = (TextView) view.findViewById(R.id.amount);
            date = (TextView) view.findViewById(R.id.date);
            image = (ImageView) view.findViewById(R.id.picture);
        }
    }


    public PaymentsListAdapter(List<Payment> paymentsList) {
        this.paymentsList = paymentsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_payments_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Payment payment = paymentsList.get(position);
        //holder.product.setText(payment.getProduct());
        holder.type.setText(payment.getProduct() + "   -   " + payment.getType());
        holder.shop.setText(payment.getShop());
        holder.amount.setText(String.valueOf(payment.getAmount()));
        holder.date.setText(payment.getDate());
        holder.image.setImageResource(R.mipmap.pop);
    }

    @Override
    public int getItemCount() {
        return paymentsList.size();
    }


}
