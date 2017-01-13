package xyz.leosap.paastore.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import xyz.leosap.paastore.R;
import xyz.leosap.paastore.common.Constants;
import xyz.leosap.paastore.common.Functions;
import xyz.leosap.paastore.helpers.BlurTransformation;
import xyz.leosap.paastore.models.App;

public class DetailActivity extends AppCompatActivity {

    private App app;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Functions.checkOrientaton(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        app = App.find(Functions.getDB(getApplicationContext()), getIntent().getStringExtra("id"));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.show();
            }
        }, 1000);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.debug) Log.d("LS url", app.getLink());

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(app.getLink()));
                startActivity(browserIntent);
            }
        });


        cargaInicial();


    }

    private void cargaInicial() {
        setTitle(Functions.cleanContent(app.getName()));

        String img = app.getImage();
        if (Constants.debug) Log.d("LS img icon", "--> " + app.getImage());

        Picasso.with(getApplicationContext())
                .load(img)

                .fit()
                .noFade()

                .transform(new BlurTransformation(getApplicationContext()))
                .error(R.drawable.ic_icon_ph)
                .centerInside()
                .into((ImageView) findViewById(R.id.imgToolbar));


        ((TextView) findViewById(R.id.tv_title)).setText(app.getTitle());
        ((TextView) findViewById(R.id.tv_autor)).setText(app.getArtist());
        ((TextView) findViewById(R.id.tv_categoria)).setText(app.getCategory());

        ((TextView) findViewById(R.id.tv_desc)).setText(app.getSummary());
        ((TextView) findViewById(R.id.tv_date)).setText(app.getReleaseDate());
        ((TextView) findViewById(R.id.tv_price)).setText(Functions.convertPrice(app.getPrice(), app.getPriceCurrency(), getApplicationContext()));
        ((TextView) findViewById(R.id.tv_copy)).setText(app.getRights());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();

    }

}
