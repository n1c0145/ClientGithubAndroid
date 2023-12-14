package adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.eduardoloza.clientegithub.R;

import java.util.List;

import models.Repositories;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofitclient.RepositoriesApi;


public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ViewHolder> {

    private List<Repositories> repositories;
    private Context context;
    private RepositoriesApi repositoriesApi;


    public RepositoryAdapter(List<Repositories> repos, Context context) {
        this.repositories = repos;
        this.context = context;
        this.repositoriesApi = RepositoriesApi.getRepositoriesApi();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.repo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Repositories repo = repositories.get(position);
        holder.setRepoData(repo);

        holder.buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mostrarConfirmacionEliminar(repo.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, description;
        private Button buttonEditar, buttonEliminar;
        private String githubToken = enviroment.githubToken.githubToken;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            buttonEditar = itemView.findViewById(R.id.buttonEditar);
            buttonEliminar = itemView.findViewById(R.id.buttonEliminar);


        }

        private void mostrarConfirmacionEliminar(String repoName) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirmación")
                    .setMessage("¿Estás seguro de que deseas eliminar el repositorio '" + repoName + "'?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Lógica para eliminar el repositorio
                            eliminarRepositorio(repoName);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        private void eliminarRepositorio(String repoName) {
            // Realiza la petición DELETE usando Retrofit
            Call<Void> call = repositoriesApi.deleteRepo("Bearer " + githubToken, repoName);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        showToast("Repositorio eliminado correctamente");
                        reiniciarApp();
                    } else {
                        showToast("Error al eliminar el repositorio");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showToast("Error de conexión al intentar eliminar el repositorio");
                }
            });
        }

        private void showToast(String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
        // Método para reiniciar la aplicación
        private void reiniciarApp() {
            // Obtiene la instancia de la actividad actual
            Activity currentActivity = (Activity) context;

            // Crea una nueva intención para reiniciar la actividad actual
            Intent intent = currentActivity.getIntent();
            currentActivity.finish();
            currentActivity.startActivity(intent);
        }

        public void setRepoData(Repositories repo) {
            name.setText(repo.getName());
            description.setText(repo.getDescription());
        }


    }
}
