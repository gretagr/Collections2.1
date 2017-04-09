package com.collections2.grigelionyte.greta.collections.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.model.CardItem;
import com.collections2.grigelionyte.greta.collections.ui.main.ListActivity;

import java.util.ArrayList;
import java.util.List;


public class CardAdapter extends RecyclerView.Adapter <CardAdapter.CardHolder>{

    private List <CardItem> cardData;
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

    public CardAdapter(List <CardItem> cardData, Context c){
        inflater = LayoutInflater.from(c);
        this.cardData = cardData;
    }

    @Override
    public CardAdapter.CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        CardItem item = cardData.get(position);
        holder.title.setText(item.getTitle());
        holder.subTitle.setText(item.getSubTitle());
        holder.image.setImageResource(item.getImageResId());
       // if (item.isFavourite()){
       //     holder.favourite.setImageResource(R.drawable.ic_favorite_black_18dp);
      //  } else {
       //     holder.favourite.setImageResource(R.drawable.ic_favorite_border_black_18dp);
      //  }
    }

    public void setCardData(ArrayList <CardItem> exerciseList) {
        this.cardData.clear();
        this.cardData.addAll(exerciseList);
    }

    @Override
    public int getItemCount() {
        return cardData.size();
    }

    class CardHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        //ImageView favourite;
        TextView title;
        TextView subTitle;
        View container;


        public CardHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.im_item_image);
           // favourite = (ImageView)itemView.findViewById(R.id.im_item_icon);
           // favourite.setOnClickListener(this);
            subTitle = (TextView)itemView.findViewById(R.id.lbl_item_sub_title);
            title = (TextView)itemView.findViewById(R.id.lbl_item_text);
            container = itemView.findViewById(R.id.cont_item_root);
            container.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
       public void onClick(View v) {
//        if (v.getId() == R.id.cont_item_root){
//            itemClickCallback.onItemClick(getAdapterPosition());
//       } else {
//           itemClickCallback.onSecondaryIconClick(getAdapterPosition());
//
//        }
            final Intent intent;
            intent = new Intent(context, ListActivity.class);
            context.startActivity(intent);
    }
        }}
