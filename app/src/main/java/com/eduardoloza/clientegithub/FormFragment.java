package com.eduardoloza.clientegithub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import models.Repositories;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofitclient.RepositoriesApi;


public class FormFragment extends AppCompatActivity {
    private EditText txtName, txtDescription;
    private Button saveButton;

    private String githubToken = enviroment.githubToken.githubToken;

    private RepositoriesApi repositoriesApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);

        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);

        saveButton = findViewById(R.id.saveButton);
        // Obtén los datos del Intent
        Intent intent = getIntent();
        if (intent != null) {
            String repoName = intent.getStringExtra("repoName");
            String repoDescription = intent.getStringExtra("repoDescription");

            // Llena las cajas de texto con los datos del repositorio
            txtName.setText(repoName);
            txtDescription.setText(repoDescription);
        }
        repositoriesApi = RepositoriesApi.getRepositoriesApi();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtén los datos del Intent
                Intent intent = getIntent();

                if (intent != null) {
                    String repoName = intent.getStringExtra("repoName");

                    // Verifica si el campo de nombre está vacío
                    if (txtName.getText().toString().isEmpty()) {
                        showToast("El campo de nombre no puede estar vacío");
                    } else {
                        // Si el campo de nombre no está vacío, verifica si se trata de una actualización
                        if (repoName != null && !repoName.isEmpty()) {
                            // Es una actualización
                            updateRepo(repoName);
                        } else {
                            // No es una actualización, es una creación
                            saveRepo();
                        }
                    }
                }
            }
        });
    }


    private void saveRepo(){
        String name = txtName.getText().toString();
        String description = txtDescription.getText().toString();

        if (name.isEmpty() || description.isEmpty()){
            Toast.makeText(this,"Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();

        }else{
            Repositories repositories = new Repositories(name,description);
            Call<Repositories>createRepoCall = RepositoriesApi.getRepositoriesApi().createRepo("Bearer " + githubToken, repositories);
            createRepoCall.enqueue(new Callback<Repositories>() {
                @Override
                public void onResponse(Call<Repositories> call, Response<Repositories> response) {
                    if (response.isSuccessful()) {
                        showToast("Repositorio creado con éxito");
                        Intent intent = new Intent(FormFragment.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showToast("Existe un error");

                    }
                }
                @Override
                public void onFailure(Call<Repositories> call, Throwable t) {
                    showToast("Error de conexion con Github");
                }
            });

        }

    }

    private void updateRepo(String repoName){

        String name = txtName.getText().toString();
        String description = txtDescription.getText().toString();

        if (name.isEmpty() || description.isEmpty()){
            Toast.makeText(this,"Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();

        }else{
            Repositories repositories = new Repositories(name,description);
            Call<Repositories> createRepoCall = repositoriesApi.updateRepo("Bearer " + githubToken, repoName,repositories);
            createRepoCall.enqueue(new Callback<Repositories>() {
                @Override
                public void onResponse(Call<Repositories> call, Response<Repositories> response) {
                    if (response.isSuccessful()) {
                        showToast("Repositorio actualizado con éxito");
                        Intent intent = new Intent(FormFragment.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showToast("Existe un error");

                    }
                }
                @Override
                public void onFailure(Call<Repositories> call, Throwable t) {
                    showToast("Error de conexion con Github");
                }
            });

        }

    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}