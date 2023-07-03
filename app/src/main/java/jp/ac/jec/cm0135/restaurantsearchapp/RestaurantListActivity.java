package jp.ac.jec.cm0135.restaurantsearchapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RestaurantListActivity extends AppCompatActivity {
    private MapViewModel mapViewModel;
    private ListView listView;
    private MyAdapter adapter;
    private ArrayList<String> accessList;
    private ArrayList<String> namesListA;
    private ArrayList<String> photoList;
    private ArrayList<String> genreList;
    private ArrayList<String> photoAList;
    private ArrayList<String> addressList;
    private ArrayList<String> opneList;
    private ToggleButton toggleButton;
    private EditText search_edit_text_list;
    private ImageButton search_button_list;
    private String apiUrl;
    private String edtStr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        Intent intent = getIntent();
        String areaCode = intent.getStringExtra("selectedCode");

        // ListView 초기화
        listView = findViewById(R.id.listView_A);

        // 데이터 리스트 초기화
        accessList = new ArrayList<>();
        namesListA = new ArrayList<>();
        photoList = new ArrayList<>();
        genreList = new ArrayList<>();
        photoAList = new ArrayList<>();
        addressList = new ArrayList<>();;
        opneList = new ArrayList<>();

        // 어댑터 설정
        adapter = new MyAdapter(this, accessList, namesListA, photoList);
        listView.setAdapter(adapter);

        // 데이터를 가져오는 메서드 호출
        apiUrl = "https://webservice.recruit.co.jp/hotpepper/gourmet/v1/?key=4d928fd8f36961a4&&format=json&order=4&large_area=" + areaCode;
        areaList(apiUrl, edtStr);

        toggleButton = findViewById(R.id.toggleBtn);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 데이터 리스트 초기화
                accessList.clear();
                namesListA.clear();
                photoList.clear();
                genreList.clear();
                photoAList.clear();
                addressList.clear();
                opneList.clear();

                // 어댑터에 변경된 데이터 알림
                adapter.notifyDataSetChanged();
                if(toggleButton.isChecked()) {
                    apiUrl = "https://webservice.recruit.co.jp/hotpepper/gourmet/v1/?key=4d928fd8f36961a4&&format=json&order=1&large_area=" + areaCode;
                }else {
                    apiUrl = "https://webservice.recruit.co.jp/hotpepper/gourmet/v1/?key=4d928fd8f36961a4&&format=json&order=4&large_area=" + areaCode;
                }
                areaList(apiUrl, edtStr);
            }
        });

        search_edit_text_list = findViewById(R.id.search_edit_text_list);
        search_button_list = findViewById(R.id.search_button_list);
        search_button_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 데이터 리스트 초기화
                accessList.clear();
                namesListA.clear();
                photoList.clear();
                genreList.clear();
                photoAList.clear();
                addressList.clear();
                opneList.clear();

                // 어댑터에 변경된 데이터 알림
                adapter.notifyDataSetChanged();

                edtStr = search_edit_text_list.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                areaList(apiUrl, edtStr);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String name = namesListA.get(i);
                String genre = genreList.get(i);
                String photoUrl = photoAList.get(i);
                String open = opneList.get(i);
                String address = addressList.get(i);
                String access = accessList.get(i);

                // 상세정보 Activity로 전환
                Intent intent = new Intent(RestaurantListActivity.this, RestaurantDetailActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("genre", genre);
                intent.putExtra("photoUrl", photoUrl);
                intent.putExtra("open", open);
                intent.putExtra("address", address);
                intent.putExtra("access", access);
                startActivity(intent);
            }
        });
    }

    private void areaList(String apiUrl, String keyword) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        JSONObject results = jsonObject.getJSONObject("results");
                        JSONArray shopArray = results.getJSONArray("shop");

                        for (int i = 0; i < shopArray.length(); i++) {
                            JSONObject shopObject = shopArray.getJSONObject(i);
                            String address = shopObject.getString("address");
                            String access = shopObject.getString("access");
                            String name = shopObject.getString("name");
                            JSONObject middleAreaObject = shopObject.getJSONObject("middle_area");
                            String middelArea = middleAreaObject.getString("name");
                            String open = shopObject.getString("open");

                            JSONObject photoObject = shopObject.getJSONObject("photo");
                            String mobileImageURLA = photoObject.getJSONObject("mobile").getString("l");
                            String mobileImageURL = photoObject.getJSONObject("mobile").getString("s");

                            JSONObject genreObject = shopObject.getJSONObject("genre");
                            String genreName = genreObject.getString("name");

                            if (name.contains(keyword) || genreName.contains(keyword) || middelArea.contains(keyword)) {
                                // 데이터 리스트에 추가
                                accessList.add(access);
                                namesListA.add(name);
                                photoList.add(mobileImageURL);
                                genreList.add(genreName);
                                addressList.add(address);
                                opneList.add(open);
                                photoAList.add(mobileImageURLA);
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 어댑터에 데이터 변경 알림
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle error response
                }
            }
        });
    }
}
