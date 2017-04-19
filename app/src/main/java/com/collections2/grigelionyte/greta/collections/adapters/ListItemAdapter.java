package com.collections2.grigelionyte.greta.collections.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.model.Item;
import com.collections2.grigelionyte.greta.collections.model.MyDBHandler;

import java.util.ArrayList;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter <ListItemAdapter.ListHolder>{

    private List <Item> listData;
    private LayoutInflater inflater;
    private ItemClickCallback itemClickCallback;
    MyDBHandler db;



    public interface ItemClickCallback {
        void onItemClick(int p);
        void onSecondaryIconClick(int p);

    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public ListItemAdapter(List <Item> listData, Context c){
        inflater = LayoutInflater.from(c);
        this.listData = listData;
        this.db = new MyDBHandler(c);
    }

    @Override
    public ListItemAdapter.ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        Item item = listData.get(position);
        holder.title.setText(item.getTitle());
        holder.subTitle.setText(item.getSubTitle());
        byte[] image = item.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.thumbnail.setImageBitmap(bitmap);

        if (item.getFavorite() == 1) {
            holder.secondaryIcon.setImageResource(R.drawable.ic_favorite_black_18dp);
        }
        else if (item.getFavorite() == 0){
        holder.secondaryIcon.setImageResource(R.drawable.ic_favorite_border_black_18dp);
        }
    }
    public void remove(int position) {
        listData.remove(position);
        Item item = listData.get(position);
        db.deleteItem(item);
        notifyItemRemoved(position);
    }
    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView thumbnail;
        ImageView secondaryIcon;
        TextView title;
        TextView subTitle;
        View container;

        public ListHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView)itemView.findViewById(R.id.im_item_icon);
            secondaryIcon = (ImageView)itemView.findViewById(R.id.im_item_icon_secondary);
            secondaryIcon.setOnClickListener(this);
            subTitle = (TextView)itemView.findViewById(R.id.lbl_item_sub_title);
            title = (TextView)itemView.findViewById(R.id.lbl_item_text);
            container = (View)itemView.findViewById(R.id.cont_item_root);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cont_item_root){
                itemClickCallback.onItemClick(getAdapterPosition());
            } else {
                itemClickCallback.onSecondaryIconClick(getAdapterPosition());
            }
        }

    }
    public void setListData(ArrayList<Item> exerciseList) {
        this.listData.clear();
        this.listData.addAll(exerciseList);
    }

}
