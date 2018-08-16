package com.udacity.bakingapp.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.service.model.Step;

import java.util.List;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepListViewHolder> {

    final private StepClickListener mStepClickListener;

    private List<Step> mSteps;
    private Context mContext;

    public StepListAdapter(Context context, StepClickListener listener) {
        mContext = context;
        mStepClickListener = listener;
    }

    @Override
    public StepListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.step_list_item, parent, false);

        return new StepListAdapter.StepListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(StepListViewHolder holder, int position) {

        Step step =  mSteps.get(position);
        String shortDescription = step.getShortDescription();

        holder.tvShortDescription.setText(shortDescription);

    }



    @Override
    public int getItemCount() {
        if (mSteps == null) {
            return 0;
        }
        return mSteps.size();
    }

    public List<Step> getSteps() {
        return mSteps;
    }


    public void setSteps(List<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    public interface StepClickListener {
        void onStepItemClick(Step step);
    }


    // Inner class for creating ViewHolders
    class StepListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvShortDescription;


        public StepListViewHolder(View itemView) {
            super(itemView);

            tvShortDescription = itemView.findViewById(R.id.tv_step_short_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Step step = mSteps.get(getAdapterPosition());
            mStepClickListener.onStepItemClick(step);
        }
    }
}
