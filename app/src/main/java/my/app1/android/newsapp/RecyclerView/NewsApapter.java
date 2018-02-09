package my.app1.android.newsapp.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import my.app1.android.newsapp.NewsInfo;
import my.app1.android.newsapp.R;
import my.app1.android.newsapp.Web;



public class NewsApapter extends RecyclerView.Adapter<NewsApapter.MyViewHolder> {


    final int TIME = 0;
    final int DATE = 1;
    final int MIN = 2 ;
    private List<NewsInfo> newsList;


    public NewsApapter(List<NewsInfo> newsList) {
        this.newsList = newsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final NewsInfo news = newsList.get(position);

        holder.titleTextView.setText(news.getTitle());
        holder.sourceTextView.setText(news.getSource());
        holder.publishedAtTextView.setText(timeFormatter1(news.getPublishedAt(), TIME));
        holder.authorTextView.setText(timeFormatter1(news.getPublishedAt(), DATE));

        final Context context = holder.newsImageView.getContext();


        Picasso.with(context).load(news.getUrlToImage()).placeholder(R.drawable.place).resize(400, 200)
                .centerCrop().into(holder.newsImageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Web.class);
                Bundle extras = new Bundle();
                extras.putString("url", news.getUrl());
                extras.putString("imageUrl", news.getUrlToImage());
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void clear() {
        int size = newsList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                newsList.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, sourceTextView, publishedAtTextView, authorTextView;
        public ImageView newsImageView;

        public MyViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.title);
            sourceTextView = view.findViewById(R.id.source);
            publishedAtTextView = view.findViewById(R.id.publishedAt);
            authorTextView = view.findViewById(R.id.author);
            newsImageView = view.findViewById(R.id.image);
        }
    }





    public String timeFormatter1(String dateFormat, int category) {

        String input;

        if (dateFormat != null && !dateFormat.isEmpty()) {
            input = getTimeAndDate(dateFormat);
        } else {
            return "today";
        }

        SimpleDateFormat parser;
        Date time;

        SimpleDateFormat formatter1;
        String formattedTime;

        SimpleDateFormat formatter2;
        String formattedDate;


        parser = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        try {
            time = parser.parse(input);
        } catch (ParseException e) {

            parser = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
            try {
                time = parser.parse(input);
            } catch (ParseException e1) {
                parser = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS");
                try {
                    time = parser.parse(input);
                } catch (ParseException e2) {
                    return "today";
                }
            }
        }


        formatter1 = new SimpleDateFormat("hh:mm a");
        formattedTime = formatter1.format(time);


        formatter2 = new SimpleDateFormat("dd MMM,yyyy");
        formattedDate = formatter2.format(time);



        if (category == TIME) {
            return formattedTime;
        } else {
            return formattedDate;
        }

    }

    public String getTimeAndDate(String input){


        SimpleDateFormat parser;
        Date time;

        SimpleDateFormat formatterHour;
        String formattedHOUR;

        SimpleDateFormat formatterMin;
        String formattedMINUTES;

        SimpleDateFormat formatterSec;
        String formattedSECONDS;



        SimpleDateFormat formatterYear;
        String formattedYEAR;

        SimpleDateFormat formatterMonth;
        String formattedMONTH;

        SimpleDateFormat formatterDate;
        String formattedDATE;




        int varDate,varHour,varMinutes,varSeconds,varYear,varMonth ;
        String append ;



        parser = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        try {
            time = parser.parse(input);
            append = "Z";
        } catch (ParseException e) {

            parser = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
            try {
                time = parser.parse(input);
                append = "Z";

            } catch (ParseException e1) {
                parser = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS");
                try {
                    time = parser.parse(input);
                    append = "Z";

                } catch (ParseException e2) {
                    return "Today" ;
                }
            }
        }

        formatterHour = new SimpleDateFormat("HH");
        formattedHOUR = formatterHour.format(time);
        varHour = Integer.parseInt(formattedHOUR);

        formatterMin = new SimpleDateFormat("mm");
        formattedMINUTES = formatterMin.format(time);
        varMinutes = Integer.parseInt(formattedMINUTES);


        formatterSec = new SimpleDateFormat("ss");
        formattedSECONDS = formatterSec.format(time);
        varSeconds = Integer.parseInt(formattedSECONDS);


        formatterYear = new SimpleDateFormat("yyyy");
        formattedYEAR = formatterYear.format(time);
        varYear = Integer.parseInt(formattedYEAR);


        formatterMonth = new SimpleDateFormat("MM");
        formattedMONTH = formatterMonth.format(time);
        varMonth = Integer.parseInt(formattedMONTH);


        formatterDate = new SimpleDateFormat("dd");
        formattedDATE = formatterDate.format(time);
        varDate = Integer.parseInt(formattedDATE);


        if(varMinutes>8){
            varMinutes = varMinutes - 8;
        }else {
            varMinutes = 00;
        }



        if(varHour > 5){
            varHour = varHour - 5;
        }else
        {
            varHour = 5-varHour ;
            varHour = 24-varHour;
            varDate = varDate - 1 ;
        }


        String output = varYear+"-"+formattedMONTH+"-"+varDate+"T"+varHour+":"+varMinutes+":"+varSeconds+append;


        return output;


    }



}

