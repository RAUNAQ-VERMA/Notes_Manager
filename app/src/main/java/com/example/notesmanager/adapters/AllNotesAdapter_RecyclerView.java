package com.example.notesmanager.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesmanager.R;
import com.example.notesmanager.models.Notes;

import java.util.ArrayList;

public class AllNotesAdapter_RecyclerView extends RecyclerView.Adapter<AllNotesAdapter_RecyclerView.ViewHolder> {

    private final ClickListener clickListener;

    private final ArrayList<Notes> localDataSet;

    public AllNotesAdapter_RecyclerView(ClickListener clickListener, ArrayList<Notes> dataSet) {
        this.clickListener = clickListener;
        localDataSet = dataSet;
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener,View.OnLongClickListener{

        private final View view;
        private final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.textView);
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
            if(true){
                clickListener.onItemLongClick(position,v);
                localDataSet.get(position).setSelected(!localDataSet.get(position).isSelected());
                v.setBackgroundColor(localDataSet.get(position).isSelected()  ? Color.GRAY : Color.WHITE);
                return true;
            }
            return false;
        }
        public TextView getTextView() {
            return textView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.add_notes_recycler_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.textView.setText(localDataSet.get(position).getTag());


        //This is to change all the other views to white
        Notes n = localDataSet.get(position);
        viewHolder.view.setBackgroundColor(n.isSelected() ?Color.GRAY: Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }


    public interface ClickListener{
        void onItemClick(int position, View v);
        void onItemLongClick(int position ,View v);
    }

}
