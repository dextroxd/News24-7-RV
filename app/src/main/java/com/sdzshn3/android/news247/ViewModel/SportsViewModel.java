package com.sdzshn3.android.news247.ViewModel;

import android.app.Application;
import android.util.Log;

import com.sdzshn3.android.news247.Fragments.SportsNewsFragment;
import com.sdzshn3.android.news247.Retrofit.ApiService;
import com.sdzshn3.android.news247.Retrofit.Article;
import com.sdzshn3.android.news247.Retrofit.Client;
import com.sdzshn3.android.news247.Retrofit.NewsModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SportsViewModel extends AndroidViewModel {
    private static MutableLiveData<List<Article>> data = new MutableLiveData<>();

    private ApiService apiService;
    private static Call<NewsModel> call;

    public SportsViewModel(@NonNull Application application) {
        super(application);
        apiService = Client.getApiService();
        loadData();
    }

    public LiveData<List<Article>> getData() {
        return data;
    }

    private void loadData() {
        call = apiService.getResponse(SportsNewsFragment.URL);
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                if (response.isSuccessful()) {
                    data.postValue(response.body().getArticles());
                }
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Log.e("SportsViewModel", "onFailure", t);
                data.postValue(null);
            }
        });
    }

    public void refresh() {
        call = apiService.getResponse(SportsNewsFragment.URL);
        call.clone().enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                if (response.isSuccessful()) {
                    data.postValue(response.body().getArticles());
                }
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Log.e("SportsViewModel", "onFailure", t);
                data.postValue(null);
            }
        });
    }
}
