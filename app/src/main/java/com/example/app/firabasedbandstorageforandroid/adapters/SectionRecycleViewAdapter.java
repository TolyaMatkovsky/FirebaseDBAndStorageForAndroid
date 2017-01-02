package com.example.app.firabasedbandstorageforandroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.app.firabasedbandstorageforandroid.R;
import com.example.app.firabasedbandstorageforandroid.model.Section;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tolik on 31.12.2016.
 */

public class SectionRecycleViewAdapter extends RecyclerView.Adapter<SectionRecycleViewAdapter.SectionViewHolder> {

    public interface OnItemClickListener {
        void onSectionItemClicked(Section section);
        void onSectionEditButtonClicked(Section section);
    }

    private List<Section> sectionsCollection;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    public SectionRecycleViewAdapter(Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.sectionsCollection = Collections.emptyList();
    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.section_item, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {
        final Section section = this.sectionsCollection.get(position);

        holder.name.setText(section.getName());
        holder.lastUpdate.setText(section.getLastUpdate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SectionRecycleViewAdapter.this.onItemClickListener != null){
                    SectionRecycleViewAdapter.this.onItemClickListener.onSectionItemClicked(section);
                }
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SectionRecycleViewAdapter.this.onItemClickListener != null){
                    SectionRecycleViewAdapter.this.onItemClickListener.onSectionEditButtonClicked(section);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (this.sectionsCollection != null) ? this.sectionsCollection.size() : 0;
    }

    public void setSectionsCollection(Collection<Section> sectionsCollection) {
        this.validateSectionsCollection(sectionsCollection);
        this.sectionsCollection = (List<Section>) sectionsCollection;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateSectionsCollection(Collection<Section> sectionsCollection) {
        if (sectionsCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.section_name)
        TextView name;
        @BindView(R.id.section_last_update)
        TextView lastUpdate;
        @BindView(R.id.section_edit)
        TextView editButton;


        public SectionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
