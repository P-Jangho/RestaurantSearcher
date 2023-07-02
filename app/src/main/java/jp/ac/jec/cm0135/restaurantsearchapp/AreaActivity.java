package jp.ac.jec.cm0135.restaurantsearchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AreaActivity extends AppCompatActivity {
    private static final String[] AREAS = {"東京", "神奈川", "埼玉", "千葉", "茨城", "栃木", "群馬", "滋賀", "京都", "大阪", "兵庫", "奈良", "和歌山", "岐阜", "静岡", "愛知",
            "三重", "北海道", "青森", "岩手", "宮城", "秋田", "山形", "福島", "新潟", "富山", "石川", "福井", "山梨", "長野", "鳥取", "島根", "岡山", "広島", "山口", "徳島", "香川",
            "愛媛", "高知", "福岡", "佐賀", "長崎", "熊本", "大分", "宮崎", "鹿児島", "沖縄"};

    private String apiUrl = "https://webservice.recruit.co.jp/hotpepper/large_area/v1/?key=4d928fd8f36961a4&format=json";
    private static List<String> largeArea = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        ListView areaListView = findViewById(R.id.areaListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, AREAS);
        areaListView.setAdapter(adapter);
        areaCode(apiUrl);

        areaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedArea = (String) parent.getItemAtPosition(position);
                Toast.makeText(AreaActivity.this, "Selected Area: " + selectedArea, Toast.LENGTH_SHORT).show();
                String areaCode = largeArea.get(position);
                Log.i("aaa", "aaa" + largeArea.get(position));

                // Pass the selected area back to MainActivity
                Intent intent = new Intent(AreaActivity.this, RestaurantListActivity.class);
                intent.putExtra("selectedCode", areaCode);
                startActivity(intent);
            }
        });
    }

    private static void areaCode(String apiUrl) {
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
                        JSONArray largeAreaArray = results.getJSONArray("large_area");

                        for (int i = 0; i < largeAreaArray.length(); i++) {
                            JSONObject largeAreaObject = largeAreaArray.getJSONObject(i);
                            String code = largeAreaObject.getString("code");
                            largeArea.add(code);
                        }
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
