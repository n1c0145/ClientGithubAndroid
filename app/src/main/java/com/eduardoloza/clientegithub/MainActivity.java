package com.eduardoloza.clientegithub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import adapters.RepositoryAdapter;
import models.Repositories;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofitclient.RepositoriesApi;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RepositoryAdapter repositoryAdapter;
    private List<Repositories> repositories;
    private FloatingActionButton fabAlbumForm;

    private RepositoriesApi repositoriesApi = RepositoriesApi.getRepositoriesApi();

    private String githubToken = enviroment.githubToken.githubToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        fabAlbumForm = findViewById(R.id.fabNewAlbum);
        fabAlbumForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FormFragment.class);
                startActivity(intent);
            }
        });
        loadRepositories();
    }

    @Override
    protected void onResume(){
        super.onResume();
        this.loadRepositories();
    }

    private void loadRepositories() {
        repositories = new ArrayList<>();
        Call<List<Repositories>> call = repositoriesApi.getUserRepos("Bearer " + githubToken);

        call.enqueue(new Callback<List<Repositories>>() {
            @Override
            public void onResponse(Call<List<Repositories>> call, Response<List<Repositories>> response) {
                if (response.isSuccessful()) {
                    repositories = response.body();
                    displayList();
                } else {
                    showToast("Error al obtener repositorios de GitHub");
                }
            }

            @Override
            public void onFailure(Call<List<Repositories>> call, Throwable t) {
                showToast("Error de conexión con GitHub");
            }
        });
    }
    private void displayList() {
        repositoryAdapter = new RepositoryAdapter(repositories, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(repositoryAdapter);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}