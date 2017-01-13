package xyz.leosap.paastore.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import xyz.leosap.paastore.R;
import xyz.leosap.paastore.common.Constants;
import xyz.leosap.paastore.common.Functions;
import xyz.leosap.paastore.models.App;


public class SplashActivity extends AppCompatActivity {

    private TextView tv1;
    private TextView tv2;
    private ImageView iv1;
    private boolean connected;
    private int flag;//1 Estado inicial

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Functions.checkOrientaton(this);
        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        iv1 = (ImageView) findViewById(R.id.imageView2);

        connected = false;
        connected = Functions.isConnected(this);

        if (connected) {
            setFlag(1);//COnectado
            cargaInicial();
        } else {
            setFlag(2);//COnectado
            Snackbar.make(findViewById(R.id.vw_container), R.string.offline, Snackbar.LENGTH_LONG).show();
        }


        animacion();
    }

    private void animacion() {
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        anim.setDuration(2000);
        tv1.startAnimation(anim);//aparecemos el primer titulo
        iv1.startAnimation(anim);

        Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        anim2.setDuration(4000);
        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {//Cuando acabe la segunda animación, se abre el activity principal si no hay conexion
                if (flag == 2 || flag == 4) {//Si no está conectado o ya terminó la descarga, abrimos el main pirincipal
                    goMain();
                } else {
                    setFlag(3);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tv2.startAnimation(anim2);//aparecemos el segundo titulo

    }

    private void cargaInicial() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setLoggingEnabled(true);
        client.setTimeout(600000);
        client.get(Constants.url_json, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (Constants.debug) Log.d("LS response", response.toString());
                SQLiteDatabase db = Functions.getDB(getApplicationContext());
                try {
                    App.truncate(db);
                    JSONArray items = response.getJSONObject("feed").getJSONArray("entry");
                    for (int i = 0; i < items.length(); i++) {

                        JSONObject object = items.getJSONObject(i);
                        App app = new App();
                        app.setId(object.getJSONObject("id").getJSONObject("attributes").getString("im:bundleId"));
                        app.setName(object.getJSONObject("im:name").getString("label"));
                        app.setTitle(object.getJSONObject("title").getString("label"));
                        app.setImage(object.getJSONArray("im:image").getJSONObject(2).getString("label"));
                        app.setSummary(object.getJSONObject("summary").getString("label"));
                        app.setPrice(object.getJSONObject("im:price").getJSONObject("attributes").getString("amount"));
                        app.setPriceCurrency(object.getJSONObject("im:price").getJSONObject("attributes").getString("currency"));
                        app.setRights(object.getJSONObject("rights").getString("label"));
                        app.setLink(object.getJSONObject("link").getJSONObject("attributes").getString("href"));
                        app.setArtist(object.getJSONObject("im:artist").getString("label"));
                        app.setCategory(object.getJSONObject("category").getJSONObject("attributes").getString("label"));
                        app.setReleaseDate(object.getJSONObject("im:releaseDate").getJSONObject("attributes").getString("label"));

                        app.save(db);

                    }


                } catch (JSONException e) {
                    if (Constants.debug) Log.d("LS error", e.getMessage());
                }

                db.close();
                if (flag == 3) {//Si ya terminó la animacion, abrimos el activity principal
                    goMain();
                } else {
                    setFlag(4);
                }//De lo  contrario indicamos que ya temino de desargar para que la animacion suceda

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject responseString) {

                if (throwable != null) {
                    Snackbar.make(findViewById(R.id.vw_container), R.string.error_server, Snackbar.LENGTH_SHORT).show();
                    if (Constants.debug) Log.d("LS error", throwable.getMessage());
                    goMain();
                }

            }

        });

    }

    private void goMain() {
        if (Constants.debug) Log.d("LS flag final", String.valueOf(flag));
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void setFlag(int flag){
        if (Constants.debug) Log.d("LS flag", String.valueOf(flag));
        this.flag=flag;
    }

}
