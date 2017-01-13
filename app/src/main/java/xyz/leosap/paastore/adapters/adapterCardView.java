package xyz.leosap.paastore.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import xyz.leosap.paastore.R;
import xyz.leosap.paastore.activities.DetailActivity;
import xyz.leosap.paastore.helpers.CircleTransformation;
import xyz.leosap.paastore.common.Constants;
import xyz.leosap.paastore.common.Functions;
import xyz.leosap.paastore.models.App;


/**
 * Created by LeoSap on 30/11/2016.
 */

public class adapterCardView extends RecyclerView.Adapter<adapterCardView.MyViewHolder> {


    private final Context context;
    private final ArrayList<App> apps;

    public adapterCardView(Context context, ArrayList<App> apps) {
        this.context = context;
        this.apps = apps;
    }

    @Override
    public adapterCardView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final adapterCardView.MyViewHolder holder, int position) {
        final App app = apps.get(position);
        holder.tv_title.setText(Functions.cleanContent(app.getName()));
        holder.tv_title.setSelected(true);
        holder.tv_title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.tv_title.setSingleLine(true);
        holder.tv_price.setText(Functions.convertPrice(app.getPrice(),app.getPriceCurrency(),context));
        holder.tv_category.setText(app.getCategory());
        holder.tv_autor.setText(app.getArtist());

        String img = app.getImage();
        if (Constants.debug) Log.d("LS img icon", "--> " + img);

        final String finalImg = img;
        Picasso.with(context)
                .load(img)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .transform(new CircleTransformation())
                //.config(Bitmap.Config.RGB_565)
                .fit()
                .placeholder(R.drawable.ic_icon_ph)
                .centerInside()
                .into(holder.iv_image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(finalImg)
                                .transform(new CircleTransformation())
                                // .networkPolicy(NetworkPolicy.OFFLINE)
                                //.config(Bitmap.Config.RGB_565)
                                .fit()
                                .placeholder(R.drawable.ic_icon_ph)
                                .centerInside()
                                .into(holder.iv_image);
                    }
                });

        holder.cv_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", app.getId());
                String transitionName = context.getString(R.string.transition_image);
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                                holder.iv_image,   // The view which starts the transition
                                transitionName    // The transitionName of the view we’re transitioning to
                        );
                ActivityCompat.startActivity(context, intent, options.toBundle());

                //context.startActivity(intent);

                if (Constants.debug)
                    Log.d("LS click", app.getName());

            }
        });

    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView tv_title;
        public final TextView tv_price;
        public final TextView tv_category;
        public final TextView tv_autor;
        public final ImageView iv_image;
        public final CardView cv_container;

        public MyViewHolder(View view) {
            super(view);

            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_category = (TextView) view.findViewById(R.id.tv_category);
            tv_autor = (TextView) view.findViewById(R.id.tv_autor);
            iv_image = (ImageView) view.findViewById(R.id.iv_icon);
            cv_container = (CardView) view.findViewById(R.id.card_view);
        }
    }
}
