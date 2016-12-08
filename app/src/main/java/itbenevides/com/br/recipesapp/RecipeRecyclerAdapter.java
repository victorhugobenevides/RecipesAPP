package itbenevides.com.br.recipesapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import itbenevides.com.br.recipesapp.model.Recipe;

/**
 * Created by Suleiman on 14-04-2015.
 */
public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.VersionViewHolder> {
    Recipe[] recipes;


    public interface OnItemClickListener {
        void onItemClick(View v,Recipe item);
    }

    Context context;
    OnItemClickListener clickListener;



    public RecipeRecyclerAdapter(Recipe[] recipes, Context context,OnItemClickListener clickListener) {

        this.recipes = recipes;
        this.context=context;
        this.clickListener=clickListener;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerlist_item, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }





    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {



        try {
            versionViewHolder.name.setText(recipes[i].name);
            versionViewHolder.time.setText(String.valueOf(recipes[i].time));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                versionViewHolder.cardItemLayout.setBackgroundResource(R.color.cardview_dark_background);
            }

            versionViewHolder.bind(recipes[i], clickListener);
            Picasso.with(context).load(recipes[i].image).into(new Target(){

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        versionViewHolder.cardItemLayout.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                        versionViewHolder.cardItemLayout.setRadius(10);

                    }
                }

                @Override
                public void onBitmapFailed(final Drawable errorDrawable) {
                    Log.d("TAG", "FAILED");
                }

                @Override
                public void onPrepareLoad(final Drawable placeHolderDrawable) {
                    Log.d("TAG", "Prepare Load");
                }
            });
        }catch (Exception e){
        }




    }

    @Override
    public int getItemCount() {

            return recipes == null ? 0 : recipes.length;
    }


    class VersionViewHolder extends RecyclerView.ViewHolder   {
        CardView cardItemLayout;
        TextView name;
        TextView time;

        public VersionViewHolder(View itemView) {
            super(itemView);

            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
            name     = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);





        }



        public void bind(final Recipe item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(v,item);
                }
            });
        }

    }





}
