package com.example.app.firabasedbandstorageforandroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app.firabasedbandstorageforandroid.model.Image;
import com.example.app.firabasedbandstorageforandroid.R;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tolik on 31.12.2016.
 */

public class ImagesRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public interface OnItemClickListener {
        void onImageDeleteButtonClicked(Image image, int position);
    }

    private List<Image> imagesCollection;
    private final LayoutInflater layoutInflater;
    private int countImages;
    private String lastUpdate;
    private String creationDate;
    private String description;

    private ImagesRecycleViewAdapter.OnItemClickListener onItemClickListener;

    public ImagesRecycleViewAdapter(Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imagesCollection = Collections.emptyList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_HEADER)
        {
            final View view = this.layoutInflater.inflate(R.layout.header_item, parent, false);
            return new ImagesRecycleViewAdapter.HeaderViewHolder(view);
        }
        if(viewType == TYPE_ITEM)
        {
            final View view = this.layoutInflater.inflate(R.layout.image_item, parent, false);
            return new ImagesRecycleViewAdapter.ImageViewHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof HeaderViewHolder)
        {
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setFullSpan(true);
            layoutParams.setMargins(20, 10, 20, 10);
            holder.itemView.setLayoutParams(layoutParams);

            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.overall.setText(String.valueOf(countImages));
            headerHolder.lastUpdate.setText(lastUpdate);
            headerHolder.dateOfCreation.setText(creationDate);
            headerHolder.description.setText(description);
        }
        else if(holder instanceof ImageViewHolder)
        {
            ImageViewHolder imHolder = (ImageViewHolder) holder;

            final Image image = this.imagesCollection.get(position-1);

            imHolder.photo.setImageBitmap(image.getImageBitmap());
            imHolder.name.setText(image.getName());
            imHolder.dateOfCreation.setText(image.getDateOfCreation());

            imHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ImagesRecycleViewAdapter.this.onItemClickListener != null){
                        ImagesRecycleViewAdapter.this.onItemClickListener.onImageDeleteButtonClicked(image, position -1);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (this.imagesCollection != null) ? this.imagesCollection.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public void setImagesCollection(Collection<Image> imagesCollection) {
        this.validateImagesCollection(imagesCollection);
        this.imagesCollection = (List<Image>) imagesCollection;
        this.notifyDataSetChanged();
    }

    public List<Image> getImagesCollection() {
        return imagesCollection;
    }

    public void removeItem(int position){
        this.imagesCollection.remove(position);
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(ImagesRecycleViewAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateImagesCollection(Collection<Image> imagesCollection) {
        if (imagesCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.header_overall)
        TextView overall;
        @BindView(R.id.header_last_update)
        TextView lastUpdate;
        @BindView(R.id.header_date)
        TextView dateOfCreation;
        @BindView(R.id.header_description)
        TextView description;


        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.image_photo)
        ImageView photo;
        @BindView(R.id.image_name)
        TextView name;
        @BindView(R.id.image_date)
        TextView dateOfCreation;
        @BindView(R.id.image_delete)
        TextView deleteButton;


        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setCountImages(int countImages) {
        this.countImages = countImages;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
