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
import com.collections2.grigelionyte.greta.collections.model.ItemsCollection;

import java.util.ArrayList;
import java.util.List;


public class CardAdapter extends RecyclerView.Adapter <CardAdapter.CardHolder>{

    private List <ItemsCollection> cardData;
    private LayoutInflater inflater;
    private ItemClickCallback itemClickCallback;
    private Context context;


    public interface ItemClickCallback {
        void onItemClick(int p);
        void onSecondaryIconClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public CardAdapter(List <ItemsCollection> cardData, Context c){
        inflater = LayoutInflater.from(c);
        this.cardData = cardData;
    }
    // initialize cardview holder
    @Override
    public CardAdapter.CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item, parent, false);
        return new CardHolder(view);
    }
    //bind view to data
    @Override
    public void onBindViewHolder(CardHolder holder, int position) {

        ItemsCollection item = cardData.get(position);
        holder.title.setText(item.getColTitle());
        holder.subTitle.setText(item.getSubTitle());
        byte[] image = item.getColImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.image.setImageBitmap(bitmap);
        // if (item.isFavourite()){
        //     holder.favourite.setImageResource(R.drawable.ic_favorite_black_18dp);
        //  } else {
        //     holder.favourite.setImageResource(R.drawable.ic_favorite_border_black_18dp);
        //  }
    }

    public void setCardData(ArrayList <ItemsCollection> exerciseList) {
        this.cardData.clear();
        this.cardData.addAll(exerciseList);
    }

    @Override
    public int getItemCount() {
        return cardData.size();
    }

    class CardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView title;
        TextView subTitle;
        View container;


        public CardHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.im_item_image);
            subTitle = (TextView) itemView.findViewById(R.id.lbl_item_sub_title);
            title = (TextView) itemView.findViewById(R.id.lbl_item_text);
            container = itemView.findViewById(R.id.cont_item_root);
            container.setOnClickListener(this);
            context = itemView.getContext();
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
    public void setListData(ArrayList<ItemsCollection> exerciseList) {
        this.cardData.clear();
        this.cardData.addAll(exerciseList);
    }
    public long getItemId(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getCardId() : position;
    }

    public ItemsCollection getItem(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
    }
    public void setItems(List<ItemsCollection> listData) {
        this.cardData = listData;
    }

    public List<ItemsCollection> getItems() {
        return cardData;
    }
}
