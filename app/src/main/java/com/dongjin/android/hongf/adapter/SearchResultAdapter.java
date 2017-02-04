package com.dongjin.android.hongf.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dongjin.android.hongf.R;
import com.dongjin.android.hongf.model.Item;
import com.dongjin.android.hongf.view.RegisterStoreActivity;

import java.util.ArrayList;

/**
 * Created by kimdongjin on 2017. 2. 2..
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {


    private ArrayList<Item> items;
    private Item item;
    private Context context;

    public SearchResultAdapter(Context context){
        this.context=context;
        items=new ArrayList<>();
    }
    public void setAdapterData(ArrayList<Item> items){
        this.items=items;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_results,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        item=items.get(position);
        if(item.getImageUrl()!=null){
            Glide.with(context).load(items.get(position).getImageUrl()).into(holder.resultIg);
        }else{
            holder.resultIg.setImageResource(android.R.drawable.ic_menu_camera);
        }

        holder.resultNmTv.setText(items.get(position).getTitle());
        holder.resultAdrsTv.setText(items.get(position).getAddress());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, RegisterStoreActivity.class);
                intent.putExtra("item",item);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView resultIg;
        TextView resultNmTv;
        TextView resultAdrsTv;
        LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            resultIg = (ImageView) itemView.findViewById(R.id.igResult);
            resultNmTv = (TextView) itemView.findViewById(R.id.vhStoreName);
            resultAdrsTv = (TextView) itemView.findViewById(R.id.vhAddress);
            container = (LinearLayout) itemView.findViewById(R.id.container);
        }
    }
}
