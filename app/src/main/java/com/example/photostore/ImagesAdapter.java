package com.example.photostore;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Upload> mUploads;
    private OnItemClickListener adapter_listener;

    public ImagesAdapter(Context context, List<Upload> upload) {
        mContext = context;
        mUploads = upload;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        adapter_listener = listener;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
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
                .placeholder(R.mipmap.placeholder_gif_foreground) //this  is the placeholder for image placeholder set a BETTER PLACEHOLDER
                .fit()
                .centerInside()
                .into(holder.imageView);

//        set  the  following   features  in the  image view
        /*
                android:scaleType="fitStart"
                android:adjustViewBounds="true"
         */
    }

    private void loadUrlIntoImageView(ImageView imageView, String image_url) {
//        todo  this    is   for  loading the image  into  the  image  view  set  the   functionality   to   be   fully  effective
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);

        void OnItemEdit(int position);

        void OnItemDelete(int position);

        void OnItemShare(int position);
    }

//    FLOATING CONTEXT CLICK LISTENER

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView imageTextView;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTextView = itemView.findViewById(R.id.image_name_text_view);
            imageView = itemView.findViewById(R.id.image_view_display);

            itemView.setOnClickListener(this);
            //CONTEXT MENU LISTENER
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if (adapter_listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    adapter_listener.OnItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Choose an action");
            MenuItem edit = contextMenu.add(Menu.NONE, 1, 1, "Edit image");
            MenuItem delete = contextMenu.add(Menu.NONE, 2, 2, "Delete");
            MenuItem share = contextMenu.add(Menu.NONE, 3, 3, " Share ");

            edit.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
            share.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (adapter_listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (menuItem.getItemId()) {
                        case 1:
                            adapter_listener.OnItemEdit(position);
                            return true;
                        case 2:
                            adapter_listener.OnItemDelete(position);
                            return true;
                        case 3:
                            adapter_listener.OnItemShare(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }
}
