package com.oritmalki.quizapp.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oritmalki.quizapp.R;
import com.oritmalki.quizapp.model.Quiz;

import java.util.List;

/**
 * Created by Orit on 1.3.2018.
 */

public class QuizListAdapter extends ArrayAdapter<Quiz> {

    ViewHolder viewHolder;

    private static class ViewHolder {
        TextView itemView;
        CardView cardView;
    }

    List<Quiz> quizList;
    QuizListAdapterCallback mCallback;

    public QuizListAdapter(@NonNull Context context, int textViewResource, List<Quiz> quizList, QuizListAdapterCallback callback) {
        super(context, textViewResource, quizList);
        this.mCallback = callback;
        this.quizList = quizList;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.quiz_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemView = convertView.findViewById(R.id.quiz_list_name);
            viewHolder.cardView = convertView.findViewById(R.id.list_item_card);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Quiz item = quizList.get(position);
        if (item != null) {
            viewHolder.itemView.setText(item.getQuizName());
            viewHolder.cardView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mCallback.onAdapterClick(quizList.get(position), position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return convertView;
    }
}
