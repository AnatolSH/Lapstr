package com.lapstr.lapstr;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView personName;
        TextView tit;
        ImageView im_dd;
        VideoView post_videoo;

        PersonViewHolder(View itemView) {
            super(itemView);
            personName = (TextView)itemView.findViewById(R.id.post_name);
            im_dd = (ImageView) itemView.findViewById(R.id.awko);
            post_videoo = (VideoView) itemView.findViewById(R.id.post_video);
            tit = (TextView)itemView.findViewById(R.id.post_title);

        }
    }

    List<User> persons;

    RVAdapter(List<User> persons){
        this.persons = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_row, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(persons.get(i).getName());
       // personViewHolder.im_dd.setImageBitmap(getBitmapFromURL(persons.get(i).getAwaurl()));
        Picasso.with(personViewHolder.itemView.getContext()).load(persons.get(i).getAwaurl()).into(personViewHolder.im_dd);
        personViewHolder.tit.setText(persons.get(i).getTitle());


        personViewHolder.post_videoo.setVideoPath(persons.get(i).getUrl());
        personViewHolder.post_videoo.setVideoURI(Uri.parse(persons.get(i).getUrl()));

        personViewHolder.post_videoo.start();
        personViewHolder.post_videoo.requestFocus();

    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
}