package retrofitclient;

import java.util.List;

import models.Repositories;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface RepositoriesApi {

    @GET("/user/repos")
    Call<List<Repositories>> getUserRepos(@Header("Authorization") String token);

    public static RepositoriesApi getRepositoriesApi(){
        Retrofit retrofit = RetrofitClient.getClient();
        return retrofit.create(RepositoriesApi.class);
    }

    //pendiente metodos aqui
}
