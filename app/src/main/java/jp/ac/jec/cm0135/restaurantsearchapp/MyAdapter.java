package jp.ac.jec.cm0135.restaurantsearchapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> accessList;
    private final ArrayList<String> namesListA;
    private final ArrayList<String> photoList;
    private MapViewModel mapViewModel;

    public MyAdapter(Context context, ArrayList<String> accessList, ArrayList<String> namesListA, ArrayList<String> photoList) {
        super(context, R.layout.listview_item);
        this.context = context;
        this.accessList = accessList;
        this.namesListA = namesListA;
        this.photoList = photoList;
    }

    @Override
    public int getCount() {
        return namesListA.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_item, parent, false);

        ImageView photoImageView = rowView.findViewById(R.id.photo_image);
        TextView nameTextView = rowView.findViewById(R.id.name_list);
        TextView accessTextView = rowView.findViewById(R.id.access_list);

        // 데이터 설정
        nameTextView.setText(namesListA.get(position));
        accessTextView.setText(accessList.get(position));
//        Picasso.get().load(photoList.get(position)).into(photoImageView);
        Glide.with(rowView)
                .load(photoList.get(position))
                .into(photoImageView);

        return rowView;
    }
}
