package jp.ac.jec.cm0135.restaurantsearchapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String BLUE_MARKER_TAG = "blue_marker";

    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private MapViewModel mapViewModel;
    private TextView distanceTextView;
    private int searchRadius = 3;
    private Marker currentLocationMarker;
    private double latitude;
    private double longitude;
    private EditText searchEdit;
    private ImageButton searchBtn;
    private ImageButton btnUp;
    private ImageButton btnDown;
    static String edtStr = "";
    String apiKey = "4d928fd8f36961a4";;
    String apiUrl;
    String locationStr = "My Location";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        searchEdit = findViewById(R.id.search_edit_text);
        searchBtn = findViewById(R.id.search_button);
        btnUp = findViewById(R.id.btnUp);
        btnDown = findViewById(R.id.btnDown);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        distanceTextView = findViewById(R.id.distance_textview);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtStr = searchEdit.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                fetchNearbyPlaces();
            }
        });


        // 기본값으로 3 설정
        String defaultDistanceText = String.format("Distance: %d m", getSearchRadiusValue(searchRadius));
        distanceTextView.setText(defaultDistanceText);
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchRadius += 1;
                if(searchRadius == 5) {
                    btnUp.setEnabled(false);
                    btnUp.setImageAlpha(128);
                }
                if(searchRadius == 2) {
                    btnDown.setEnabled(true);
                    btnDown.setImageAlpha(255);
                }

                String defaultDistanceText = String.format("Distance: %d m", getSearchRadiusValue(searchRadius));
                distanceTextView.setText(defaultDistanceText);
                fetchNearbyPlaces();
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchRadius -= 1;
                if(searchRadius == 1) {
                    btnDown.setEnabled(false);
                    btnDown.setImageAlpha(128);
                }
                if(searchRadius == 4){
                    btnUp.setEnabled(true);
                    btnUp.setImageAlpha(255);
                }
                String defaultDistanceText = String.format("Distance: %d m", getSearchRadiusValue(searchRadius));
                distanceTextView.setText(defaultDistanceText);
                fetchNearbyPlaces();
            }
        });

        observeLocations();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMapClickListener(this);
        this.googleMap.setOnMarkerClickListener(this);
        getCurrentLocation();
    }

    public boolean onMarkerClick(Marker marker) {
        Object tag = marker.getTag(); // 마커에 설정한 태그 가져오기

        if (BLUE_MARKER_TAG.equals(marker.getTag())) {
            // 클릭된 마커의 정보를 가져옴
            String name = marker.getTitle();
            String genre = marker.getSnippet();

            mapViewModel.fetchNearbyPlaces(apiUrl, edtStr);
            Log.i("aaa", "aaa" + Objects.requireNonNull(mapViewModel.names.getValue()).indexOf(name));
            int i = Objects.requireNonNull(mapViewModel.names.getValue()).indexOf(name);
            String photoUrl = mapViewModel.getPhotos().getValue().get(i);
            String open = mapViewModel.getOpens().getValue().get(i);
            String address = mapViewModel.getAddress().getValue().get(i);
            String access = mapViewModel.getAccess().getValue().get(i);

            // 상세정보 Activity로 전환
            Intent intent = new Intent(MapActivity.this, RestaurantDetailActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("genre", genre);
            intent.putExtra("photoUrl", photoUrl);
            intent.putExtra("open", open);
            intent.putExtra("address", address);
            intent.putExtra("access", access);
            startActivity(intent);

            return true;
        }
        return false; // false 반환하여 기본 동작인 정보 창 표시를 유지합니다.
    }

    private void observeLocations() {
        mapViewModel.getLocations().observe(this, new Observer<List<LatLng>>() {
            @Override
            public void onChanged(List<LatLng> locations) {
                mapViewModel.getNames().observe(MapActivity.this, new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> names) {
                        mapViewModel.getGenreNames().observe(MapActivity.this, new Observer<List<String>>() {
                            @Override
                            public void onChanged(List<String> genreNames) {
                                addMarkersToMap(locations, names, genreNames);
                            }
                        });
                    }
                });
            }
        });
    }

    private void addMarkersToMap(List<LatLng> locations, List<String> names, List<String> genreNames) {
        if (googleMap != null) {
            googleMap.clear(); // 이전에 추가된 마커들을 모두 지움
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, location -> {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(latitude, longitude);
                                currentLocationMarker = googleMap.addMarker(new MarkerOptions().position(currentLocation).title(locationStr));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));

                            }
                        });
            }

            for (int i = 0; i < locations.size(); i++) {
                LatLng location = locations.get(i);
                String name = (i < names.size()) ? names.get(i) : "";
                String genreName = (i < genreNames.size()) ? genreNames.get(i) : "";

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(location)
                        .title(name)
                        .snippet(genreName)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                Marker marker = googleMap.addMarker(markerOptions);
                marker.setTag(BLUE_MARKER_TAG);
//                googleMap.addMarker(markerOptions);
            }
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            currentLocationMarker = googleMap.addMarker(new MarkerOptions().position(currentLocation).title(locationStr));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
                            apiUrl = "https://webservice.recruit.co.jp/hotpepper/gourmet/v1/?key=" + apiKey +
                                    "&range=" + searchRadius + "&format=json&lat=" + latitude + "&lng=" + longitude;

                            // 검색 실행
                            fetchNearbyPlaces();
                        }
                    });
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (googleMap != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            latitude = latLng.latitude;
            longitude = latLng.longitude;

            Log.i("aaa", "aaa" + latitude);
            Log.i("aaa", "aaa" + longitude);

            apiUrl = "https://webservice.recruit.co.jp/hotpepper/gourmet/v1/?key=" + apiKey +
                    "&range=" + searchRadius + "&format=json&lat=" + latitude + "&lng=" + longitude;

            Log.i("aaa", "aaa" + apiUrl);
            locationStr = "Selected Location";
            mapViewModel.fetchNearbyPlaces(apiUrl, edtStr);
        }
    }

    private void fetchNearbyPlaces() {
        if (googleMap == null) {
            return;
        }

        googleMap.clear(); // 이전에 추가된 마커들을 모두 지움

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 위치 권한이 없는 경우
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(latitude, longitude);
                        currentLocationMarker = googleMap.addMarker(new MarkerOptions().position(currentLocation).title(locationStr));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));

                        String apiKey = "4d928fd8f36961a4";
                        String apiUrl = "https://webservice.recruit.co.jp/hotpepper/gourmet/v1/?key=" + apiKey +
                                "&range=" + searchRadius + "&format=json&lat=" + latitude + "&lng=" + longitude;

                        Log.i("aaa", "aaa" + apiUrl);
                        mapViewModel.fetchNearbyPlaces(apiUrl, edtStr);
                    }
                });
    }

    private int getSearchRadiusValue(int distance) {
        switch (distance) {
            case 1:
                return 300;
            case 2:
                return 500;
            case 3:
                return 1000;
            case 4:
                return 2000;
            case 5:
                return 3000;
            default:
                return 1000;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}