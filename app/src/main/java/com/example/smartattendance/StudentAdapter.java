package com.example.smartattendance;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    ArrayList<StudentItem>studentItems;
    Context context;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    public StudentAdapter(Context context, ArrayList<StudentItem> studentItems){
        this.studentItems=studentItems;
        this.context=context;
    }
    public static class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView Std_ID;
        TextView std_nm;
        TextView status;
        CardView cardView;
        public StudentViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            Std_ID= itemView.findViewById(R.id.stnt_id);
            std_nm=itemView.findViewById(R.id.std_name);
            status=itemView.findViewById(R.id.status);
            cardView=itemView.findViewById(R.id.cardview);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),1,0,"Delete");
        }
    }
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){

        
        View ItemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,parent,false);
        return new StudentViewHolder(ItemView,onItemClickListener);
    }
    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.Std_ID.setText(studentItems.get(position).getStudentId()+"");
        holder.std_nm.setText(studentItems.get(position).getStudentName());
        holder.status.setText(studentItems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));

    }
    private int getColor (int position){
        String status= studentItems.get(position).getStatus();
        if (status.equals("P"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.present)));
        else if(status.equals("A"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.absent)));

        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.normal)));

    }
    @Override
    public int getItemCount() {

        return studentItems.size();
    }

}
