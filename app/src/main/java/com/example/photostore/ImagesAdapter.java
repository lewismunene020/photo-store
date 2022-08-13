package com.example.photostore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>{

    private  Context mContext ;
    private  List<Upload> mUploads ;
    public   ImagesAdapter(Context context , List<Upload> upload){
        mContext = context;
        mUploads = upload ;
    }

    public   class ImageViewHolder extends  RecyclerView.ViewHolder{
        public TextView imageTextView;
        public ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTextView = itemView.findViewById(R.id.image_name_text_view);
            imageView = itemView.findViewById(R.id.image_view_display);
        }
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mContext).inflate(R.layout.image_item , parent , false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Upload  upload_current  = mUploads.get(position);
        holder.imageTextView.setText(upload_current.getImage_name());
        //Loading  it   normally
        // TODO:
            // Try  loading  it   normally without   a  library
//        loadUrlIntoImageView(holder.imageView , upload_current.getImage_url());
        // loading  using  Picasso  library
        Picasso.get()
                .load(upload_current.getImage_url())
                .fit()
                .centerInside()
                .into(holder.imageView);

    }

    private void loadUrlIntoImageView(ImageView imageView, String image_url) {
//        todo  this    is   for  loading the image  into  the  image  view  set  the   functionalitu   to   be   fully  effective
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }


}
