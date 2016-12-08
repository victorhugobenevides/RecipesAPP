package itbenevides.com.br.recipesapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import itbenevides.com.br.recipesapp.model.Recipe;

public class DetailListViewActivity extends AppCompatActivity {

    private Recipe recipes;
    private String jsonMyObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("recipe");
        }
         recipes = new Gson().fromJson(jsonMyObject, Recipe.class);
        TextView name = (TextView)findViewById(R.id.name);
        TextView count = (TextView)findViewById(R.id.count);
        TextView time = (TextView)findViewById(R.id.time);

        if(recipes!=null){
            name.setText(recipes.name);
            if(recipes.bookmarks!=null)
            count.setText(String.valueOf(recipes.bookmarks.length));
            else
                count.setText("0");
            time.setText(String.valueOf(recipes.time)+"min");

        }

        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        Picasso.with(getApplicationContext()).load(recipes.image).into(new Target(){

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    (findViewById(R.id.coordinator)).setBackground(new BitmapDrawable(getResources(), bitmap));


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




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
