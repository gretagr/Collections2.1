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
import com.collections2.grigelionyte.greta.collections.model.MyDBHandler;

import java.util.ArrayList;
import java.util.List;


public class CardAdapter extends RecyclerView.Adapter <CardAdapter.CardHolder>{

    private List <ItemsCollection> cardData;
    private LayoutInflater inflater;
    private ItemClickCallback itemClickCallback;
    private Context context;
    MyDBHandler db;


    public interface ItemClickCallback {
        void onItemClick(int p);
        void onSecondaryIconClick(int p);
        void onDeleteClick(int p);

    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public CardAdapter(List <ItemsCollection> cardData, Context c){
        inflater = LayoutInflater.from(c);
        this.cardData = cardData;
        this.db = new MyDBHandler(c);
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
        ItemsCollection itemsCollection = cardData.get(position);

        holder.title.setText(itemsCollection.getColTitle());
        holder.subTitle.setText(itemsCollection.getSubTitle());
        byte[] image = itemsCollection.getColImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.delete.setImageResource(R.drawable.ic_delete_black_18dp);
        holder.image.setImageBitmap(bitmap);
        if (itemsCollection.getFavoriteCol() == 1) {
            holder.favorite.setImageResource(R.drawable.ic_favorite_black_18dp);
        }
        else if (itemsCollection.getFavoriteCol() == 0){
            holder.favorite.setImageResource(R.drawable.ic_favorite_border_black_18dp);
        }
    }

    public void setCardData(ArrayList <ItemsCollection> exerciseList) {
        this.cardData.clear();
        this.cardData.addAll(exerciseList);
    }

    @Override
    public int getItemCount() {
        return cardData.size();
    }

    public void remove(int position) {
        cardData.remove(position);
        notifyItemRemoved(position);
    }

    class CardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView title;
        TextView subTitle;
        View container;
        ImageView delete;
        ImageView favorite;


        public CardHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.im_item_image);
            favorite = (ImageView)itemView.findViewById(R.id.secondary);
            favorite.setOnClickListener(this);
            delete = (ImageView)itemView.findViewById(R.id.delete);
            delete.setOnClickListener(this);
            subTitle = (TextView) itemView.findViewById(R.id.lbl_item_sub_title);
            title = (TextView) itemView.findViewById(R.id.lbl_item_text);
            container = itemView.findViewById(R.id.cont_item_root);
            container.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cont_item_root) {
                itemClickCallback.onItemClick(getAdapterPosition());
            }
            else if (v.getId() == R.id.delete){
                itemClickCallback.onDeleteClick(getAdapterPosition());
            }
            else if (v.getId() == R.id.secondary){
                itemClickCallback.onSecondaryIconClick(getAdapterPosition());
            }
        }
    }
}
