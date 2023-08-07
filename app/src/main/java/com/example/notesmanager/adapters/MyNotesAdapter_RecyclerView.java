package com.example.notesmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesmanager.R;
import com.example.notesmanager.models.Folder;

import java.util.ArrayList;
import java.util.List;

public class MyNotesAdapter_RecyclerView extends RecyclerView.Adapter<MyNotesAdapter_RecyclerView.ViewHolder> {

    private final ClickListener clickListener;

    private final ArrayList<Folder> localDataSet;

    public MyNotesAdapter_RecyclerView(ClickListener clickListener, ArrayList<Folder> dataSet) {
        this.clickListener = clickListener;
        localDataSet = dataSet;
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener,View.OnLongClickListener{
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

            textView = view.findViewById(R.id.textView2);
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
                .inflate(R.layout.my_notes_recycler_view, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).getFolderName());
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
