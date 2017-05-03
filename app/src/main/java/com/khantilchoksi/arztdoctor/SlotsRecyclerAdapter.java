package com.khantilchoksi.arztdoctor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Khantil on 24-03-2017.
 */

public class SlotsRecyclerAdapter extends RecyclerView.Adapter<SlotsRecyclerAdapter.ViewHolder> {


    private final String LOG_TAG = getClass().getSimpleName();

    private ArrayList<Slot> mSlotsList;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView slotDayTextView;
        private TextView slotStartTimeTextView;

        public TextView getSlotDayTextView() {
            return slotDayTextView;
        }

        public TextView getSlotStartTimeTextView() {
            return slotStartTimeTextView;
        }

        public TextView getSlotEndTimeTextView() {
            return slotEndTimeTextView;
        }

        public TextView getFirstTimeSlotFeesTextView() {
            return firstTimeSlotFeesTextView;
        }

        private TextView slotEndTimeTextView;
        private TextView firstTimeSlotFeesTextView;
        private TextView normalSlotFeesTextView;

        public TextView getNormalSlotFeesTextView() {
            return normalSlotFeesTextView;
        }

        public ViewHolder(View itemView) {
            super(itemView);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });*/

            slotDayTextView = (TextView)itemView.findViewById(R.id.slot_day_text_view);
            slotStartTimeTextView = (TextView)itemView.findViewById(R.id.slot_start_time_text_view);
            slotEndTimeTextView = (TextView) itemView.findViewById(R.id.slot_end_time_text_view);
            firstTimeSlotFeesTextView = (TextView) itemView.findViewById(R.id.first_time_slot_fees_text_view);
            normalSlotFeesTextView = (TextView) itemView.findViewById(R.id.normal_slot_fees_text_view);
        }
    }

    public SlotsRecyclerAdapter(ArrayList<Slot> slotsList, Context context) {
        this.mSlotsList = slotsList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slot_row_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(LOG_TAG, "Slot Day Row Item Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        holder.getSlotDayTextView().setText(mSlotsList.get(position).getDay());
        holder.getSlotStartTimeTextView().setText(mSlotsList.get(position).getStartTime());
        holder.getSlotEndTimeTextView().setText(mSlotsList.get(position).getEndTime());
        holder.getFirstTimeSlotFeesTextView().setText(mContext.getResources().getString(R.string.rupee_symbol).
                concat(" "+mSlotsList.get(position).getFirstTimeSlotFees()));
        holder.getNormalSlotFeesTextView().setText(mContext.getResources().getString(R.string.rupee_symbol).
                concat(" "+mSlotsList.get(position).getNormalSlotFees()));
    }

    @Override
    public int getItemCount() {
        return mSlotsList.size();
    }


}
