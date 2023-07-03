package jp.ac.jec.cm0135.restaurantsearchapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class RestaurantDetailActivity extends AppCompatActivity {

    ImageView imageView;
    ImageButton call_button;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        TextView nameTextView = findViewById(R.id.name_textview);
        TextView genreTextView = findViewById(R.id.genre_textview);
        TextView addressTextView = findViewById(R.id.address_textview);
        TextView accessTextView = findViewById(R.id.access_textview);
        TextView openTextView = findViewById(R.id.open_textview);
        imageView = findViewById(R.id.imageView);
        call_button = findViewById(R.id.call_button);

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String genre = intent.getStringExtra("genre");
            String imageURL = intent.getStringExtra("photoUrl");
            String address = intent.getStringExtra("address");
            String access = intent.getStringExtra("access");
            String open = intent.getStringExtra("open");
            if (name != null && genre != null) {
                nameTextView.setText(name);
                genreTextView.setText(genre);
                addressTextView.setText(address);
                accessTextView.setText(access);
                openTextView.setText(open);
                Glide.with(this)
                        .load(imageURL)
                        .into(imageView);
            }
        }

        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = "080-5187-9673"; // 전화를 걸고자 하는 전화번호를 입력하세요

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });
    }
}