package com.example.notesmanager.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesmanager.R;
import com.example.notesmanager.models.Notes;

import java.util.ArrayList;

public class FilesAdapter_RecyclerView extends RecyclerView.Adapter<FilesAdapter_RecyclerView.ViewHolder> {

    private final ClickListener clickListener;

    private final ArrayList<String> localDataSet;

    public FilesAdapter_RecyclerView(ClickListener clickListener, ArrayList<String> dataSet) {
        this.clickListener = clickListener;
        localDataSet = dataSet;
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener,View.OnLongClickListener{

        private View view;
        private final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.textView3);
        }
        @Override
        public void onClick(View v){
            int position = getAdapterPosition();
            if(position>=0){
                clickListener.onItemClick(position,v);
            }
        }
        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            if(position>=0){
                clickListener.onItemLongClick(position,v);
                return true;
            }
            return false;
        }

        public TextView getTextView() {
            return textView;
        }


    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.files_recycler_view, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {



        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.setText(localDataSet.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }


    public interface ClickListener{
        void onItemClick(int position, View v);
        void onItemLongClick(int position ,View v);
    }

}
