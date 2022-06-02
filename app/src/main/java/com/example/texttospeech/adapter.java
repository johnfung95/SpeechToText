package com.example.texttospeech;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class adapter extends RecyclerView.Adapter<adapter.viewHolder> {

    Context context;
    String[] titles, summaries, dates, contents ;
    int ids[];

    public adapter(Context newContext, String newTitles[], String newSummaries[], String newDates[], String newContents[], int newIds[]) {
        context = newContext;
        titles = newTitles;
        summaries = newSummaries;
        dates = newDates;
        contents = newContents;
        ids = newIds;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View inflaterView = inflater.inflate(R.layout.article_rows, parent, false);
        return new viewHolder(inflaterView);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.titleView.setText(titles[position]);
        holder.summaryView.setText(summaries[position]);
        holder.dateView.setText(dates[position]);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, article_detail.class);
                intent.putExtra("article_id", ids[position]);
                intent.putExtra("article_title", titles[position]);
                intent.putExtra("article_date", dates[position]);
                intent.putExtra("article_content", contents[position]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView titleView, summaryView, dateView;
        ConstraintLayout mainLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.article_row_title);
            summaryView = itemView.findViewById(R.id.article_row_summary);
            dateView = itemView.findViewById(R.id.article_row_date);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
