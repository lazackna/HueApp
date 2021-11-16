package com.lazackna.hueapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lazackna.hueapp.Light.ColorLight;
import com.lazackna.hueapp.Light.Light;
import com.lazackna.hueapp.Util.ColorHelper;

import java.util.List;

public class LightAdapter extends RecyclerView.Adapter<LightAdapter.LightViewHolder>{
    private static final String LOGTAG = LightAdapter.class.getName();

    private Context appContext;
    private List<Light> photoList;
    private OnItemClickListener clickListener;

    // This interface isolates us from classes that want to listen to item clicks
    // so we don't need to know what those classes are
    public interface OnItemClickListener {
        void onItemClick(int clickedPosition);
    }

    public LightAdapter(Context context, List<Light> photos, OnItemClickListener listener) {
        appContext = context;
        photoList = photos;
        clickListener = listener;
    }

    @NonNull
    @Override
    public LightViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(LOGTAG, "onCreateViewHolder() called");
        //TODO change layout
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.light_list_item, viewGroup, false);
        LightViewHolder viewHolder = new LightViewHolder(itemView, this);
        return viewHolder;
    }

    //@SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull LightViewHolder holder, int position) {
        Light light = this.photoList.get(position);
        holder.name.setText(light.name);
        if (light instanceof ColorLight) {
            ColorLight colorLight = (ColorLight) light;
            holder.lightColorView.setBackgroundColor(ColorHelper.hueToColor(colorLight.hue, colorLight.sat, colorLight.bri));
            //float[] hsv = new float[]{((ColorLight) light).hue, ((ColorLight) light).sat, ((ColorLight) light).bri};
            //int color = Color.HSVToColor(hsv);
            //holder.lightColorView.setBackgroundColor(color);

        }
        //int color = R.color.purple_200;

        //holder.lightColorView.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        System.err.println(photoList.size());
        return photoList.size();
    }

    class LightViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // TODO DONE Add attributes for the views that make up each item in the list
        public View lightColorView;
        public TextView name;

        private LightAdapter adapter;


        public LightViewHolder(View itemView, LightAdapter adapter) {
            super(itemView);
            // TODO DONE Get the views from the item layout using findViewById(R.id.xxx)
            //this.textView = itemView.findViewById(R.id.captionTextView);
            //this.imageView = (ImageView) itemView.findViewById(R.id.photoImageView);
            name = itemView.findViewById(R.id.light_name);
            lightColorView = itemView.findViewById(R.id.lightColor);
            this.adapter = adapter;

            // Make this object a listener for clicks on our item view
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            // Find out which item in the list was clicked by retrieving the position in the adapter
            int clickedPosition = getAdapterPosition();
            Log.i(LOGTAG, "Item " + clickedPosition + " was clicked");
            // Notify our listener that an item was clicked
            clickListener.onItemClick(clickedPosition);

        }
    }
}
