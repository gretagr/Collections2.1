package com.collections2.grigelionyte.greta.collections.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.model.Item;
import com.collections2.grigelionyte.greta.collections.model.MyDBHandler;

import java.util.ArrayList;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter <ListItemAdapter.ListHolder> implements Filterable {

    public List <Item> listData;
    ArrayList<Item> filterListItem;
    private LayoutInflater inflater;
    private ItemClickCallback itemClickCallback;
    FilterForList filterForList;
    private Context context;
    MyDBHandler db;



    public interface ItemClickCallback {
        void onItemClick(int p);
        void onSecondaryIconClick(int p);
        void onEditClick(int p);

    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public ListItemAdapter(List <Item> listData, Context c){
        inflater = LayoutInflater.from(c);
        this.context = c;
        this.listData = listData;
        this.filterListItem = (ArrayList<Item>) listData;
        this.db = new MyDBHandler(c);
    }

    @Override
    public ListItemAdapter.ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        holder.title.setText(listData.get(position).getTitle());
        holder.subTitle.setText(listData.get(position).getSubTitle());
        byte[] image = listData.get(position).getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.thumbnail.setImageBitmap(bitmap);
        holder.edit.setImageResource(R.drawable.ic_mode_edit_black_18dp);

        if (listData.get(position).getFavorite() == 1) {
            holder.secondaryIcon.setImageResource(R.drawable.ic_favorite_black_18dp);
        }
        else if (listData.get(position).getFavorite() == 0){
            holder.secondaryIcon.setImageResource(R.drawable.ic_favorite_border_black_18dp);
        }
    }
    public void remove(int position) {
        Item item = listData.get(position);
        db.deleteItem(item);
        listData.remove(position);
        notifyItemRemoved(position);
    }@Override
    public Filter getFilter(){
        if (filterForList == null){
            filterForList = new FilterForList(filterListItem, this);
        }
        return filterForList;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView thumbnail;
        ImageView secondaryIcon;
        ImageView edit;
        TextView title;
        TextView subTitle;
        View container;

        public ListHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView)itemView.findViewById(R.id.im_item_icon);
            secondaryIcon = (ImageView)itemView.findViewById(R.id.im_item_icon_secondary);
            secondaryIcon.setOnClickListener(this);
            edit = (ImageView)itemView.findViewById(R.id.im_item_icon_edit);
            edit.setOnClickListener(this);
            subTitle = (TextView)itemView.findViewById(R.id.lbl_item_sub_title);
            title = (TextView)itemView.findViewById(R.id.lbl_item_text);
            container = (View)itemView.findViewById(R.id.cont_item_root);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cont_item_root) {
                itemClickCallback.onItemClick(getAdapterPosition());
            }
            else if (v.getId() == R.id.im_item_icon_edit){
                itemClickCallback.onEditClick(getAdapterPosition());
            }
            else if (v.getId() == R.id.im_item_icon_secondary){
                itemClickCallback.onSecondaryIconClick(getAdapterPosition());
            }
        }

    }

}
