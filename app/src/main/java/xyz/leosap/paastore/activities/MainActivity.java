package xyz.leosap.paastore.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import xyz.leosap.paastore.R;
import xyz.leosap.paastore.adapters.adapterCardView;
import xyz.leosap.paastore.common.Functions;
import xyz.leosap.paastore.models.App;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private boolean doubleBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Functions.checkOrientaton(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        setLayout();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        cargaInicial();

    }

    private void setLayout() {
        // Checks the orientation of the screen, portrait linear, landscape grid 2 columns
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_info:
                startActivity(new Intent(this,InfoActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setLayout();
    }


    private void cargaInicial() {
        SQLiteDatabase db = Functions.getDB(getApplicationContext());

        final ArrayList<App> list = App.getAll(db);
        adapterCardView adapter_card = new adapterCardView(MainActivity.this, list);

        recyclerView.setAdapter(adapter_card);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (doubleBack) {
            System.gc();
            finish();
            return;
        }
        this.doubleBack = true;
        Snackbar snack = Snackbar.make(findViewById(R.id.content_main), R.string.double_back_exit, Snackbar.LENGTH_SHORT);
        snack.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                    doubleBack = false;
                }

            }
        });
        snack.show();


    }
}
