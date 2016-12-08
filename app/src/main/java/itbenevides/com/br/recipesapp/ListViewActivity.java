package itbenevides.com.br.recipesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import itbenevides.com.br.recipesapp.model.Recipe;

import static java.security.AccessController.getContext;


public class ListViewActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleContainerVisible = true;

    private AppBarLayout appbar;
    private ImageView coverImage;
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private ImageView avatar;
    SwipeRefreshLayout swipeRefreshLayout;


    private void findViews() {
        appbar = (AppBarLayout)findViewById( R.id.appbar );
        coverImage = (ImageView)findViewById( R.id.imageview_placeholder );
        toolbar = (Toolbar)findViewById( R.id.toolbar );
        avatar = (ImageView) findViewById(R.id.avatar);
        recyclerView=(RecyclerView)findViewById(R.id.RecView);
        setSupportActionBar(toolbar);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_view);
        findViews();



        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        Recipe[] obj = new Recipe[0];
                        try {

                            RestTemplate restTemplate = new RestTemplate();
                            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                            String result = restTemplate.getForObject("http://inshake.herokuapp.com/v1/recipes", String.class,"Android");
                            obj = (new Gson()).fromJson(result, Recipe[].class);




                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {

                        }
                        final Recipe[] finalObj = obj;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RecipeRecyclerAdapter simpleRecyclerAdapter = new RecipeRecyclerAdapter(finalObj,getApplicationContext(),new RecipeRecyclerAdapter.OnItemClickListener(){


                                    @Override
                                    public void onItemClick(View v, Recipe item) {

                                        Intent intent = new Intent(ListViewActivity.this,DetailListViewActivity.class);
                                        intent.putExtra("recipe", new Gson().toJson(item));
                                        startActivity(intent);
                                    }
                                });
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setItemViewCacheSize(20);
                                recyclerView.setDrawingCacheEnabled(true);
                                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                                recyclerView.setAdapter(simpleRecyclerAdapter);

                                ((TextView)findViewById(R.id.text3)).setText(String.valueOf(finalObj.length)+" recipes favorites");
                                swipeRefreshLayout.setRefreshing(false);


                            }
                        });



                    }
                };

                thread.start();




            }
        });








        toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);




        avatar.setImageResource(R.mipmap.ic_launcher);
        coverImage.setImageResource(R.drawable.background_toolbar);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbar.setTitle("Grumpy Cat");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {




        swipeRefreshLayout.setEnabled(offset == 0);

        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;


        handleAlphaOnTitle(percentage);

    }




    @Override
    protected void onResume() {
        super.onResume();
        appbar.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appbar.removeOnOffsetChangedListener(this);
    }




    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {

                startAlphaAnimation(avatar, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(findViewById(R.id.text2), ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);

                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {


                startAlphaAnimation(avatar, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);

                startAlphaAnimation(findViewById(R.id.text2), ALPHA_ANIMATIONS_DURATION, View.VISIBLE);




                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}