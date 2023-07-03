package jp.ac.jec.cm0135.restaurantsearchapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

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

public class MapViewModel extends ViewModel {

    public MutableLiveData<List<String>> getAddress() {
        return address;
    }

    public void setAddress(MutableLiveData<List<String>> address) {
        this.address = address;
    }

    public MutableLiveData<List<String>> getAccess() {
        return access;
    }

    public void setAccess(MutableLiveData<List<String>> access) {
        this.access = access;
    }

    public MutableLiveData<List<String>> getOpens() {
        return opens;
    }

    public void setOpens(MutableLiveData<List<String>> opens) {
        this.opens = opens;
    }

    public MutableLiveData<List<String>> address = new MutableLiveData<>();
    public MutableLiveData<List<String>> access = new MutableLiveData<>();
    private MutableLiveData<List<LatLng>> locations = new MutableLiveData<>();
    public MutableLiveData<List<String>> names = new MutableLiveData<>();
    private MutableLiveData<List<String>> genreNames = new MutableLiveData<>();
    public MutableLiveData<List<String>> photos = new MutableLiveData<>();
    public MutableLiveData<List<String>> opens = new MutableLiveData<>();

    public MutableLiveData<List<String>> getPhotos() {
        return photos;
    }

    public void setPhotos(MutableLiveData<List<String>> photos) {
        this.photos = photos;
    }

    public void setLocations(MutableLiveData<List<LatLng>> locations) {
        this.locations = locations;
    }

    public void setNames(MutableLiveData<List<String>> names) {
        this.names = names;
    }

    public MutableLiveData<List<String>> getGenreNames() {
        return genreNames;
    }

    public void setGenreNames(MutableLiveData<List<String>> genreNames) {
        this.genreNames = genreNames;
    }

    public LiveData<List<LatLng>> getLocations() {
        return locations;
    }

    public LiveData<List<String>> getNames() {
        return names;
    }

    public void setNames(List<String> namesList) {
        names.setValue(namesList);
    }


    public void fetchNearbyPlaces(String apiUrl, String keyword) {
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

                        List<String> addressList = new ArrayList<>();
                        List<String> accessList = new ArrayList<>();
                        List<LatLng> locationsList = new ArrayList<>();
                        List<String> namesList = new ArrayList<>();
                        List<String> genreNamesList = new ArrayList<>();
                        List<String> photoList = new ArrayList<>();
                        List<String> openList = new ArrayList<>();

                        for (int i = 0; i < shopArray.length(); i++) {
                            JSONObject shopObject = shopArray.getJSONObject(i);
                            String address = shopObject.getString("address");
                            String access = shopObject.getString("access");
                            double lat = shopObject.getDouble("lat");
                            double lng = shopObject.getDouble("lng");
                            String name = shopObject.getString("name");
                            String open = shopObject.getString("open");

                            JSONObject photoObject = shopObject.getJSONObject("photo");
                            String mobileImageURL = photoObject.getJSONObject("mobile").getString("l");

                            JSONObject genreObject = shopObject.getJSONObject("genre");
                            String genreName = genreObject.getString("name");

                            if (name.contains(keyword) || genreName.contains(keyword)) {
                                LatLng shopLocation = new LatLng(lat, lng);
                                addressList.add(address);
                                accessList.add(access);
                                locationsList.add(shopLocation);
                                namesList.add(name);
                                genreNamesList.add(genreName);
                                photoList.add(mobileImageURL);
                                openList.add(open);
                            }
                        }
                        address.postValue(addressList);
                        access.postValue(accessList);
                        locations.postValue(locationsList);
                        names.postValue(namesList);
                        genreNames.postValue(genreNamesList);
                        photos.postValue(photoList);
                        opens.postValue(openList);
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
