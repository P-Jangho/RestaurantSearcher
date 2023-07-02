package jp.ac.jec.cm0135.restaurantsearchapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

class MyObject {
    private MutableLiveData<List<LatLng>> locations = new MutableLiveData<>();
    public MutableLiveData<List<String>> names = new MutableLiveData<>();
    private MutableLiveData<List<String>> genreNames = new MutableLiveData<>();

    public LiveData<List<LatLng>> getLocations() {
        return locations;
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


    public LiveData<List<String>> getNames() {
        return names;
    }
    public void setNames(List<String> namesList) {
        names.setValue(namesList);
    }
}







