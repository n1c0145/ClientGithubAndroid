package retrofitclient;

import java.util.List;

import models.Repositories;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RepositoriesApi {

    @GET("/user/repos")
    Call<List<Repositories>> getUserRepos(@Header("Authorization") String token);

    @POST("/user/repos")
    Call<Repositories> createRepo(@Header("Authorization") String token, @Body Repositories Repositories);

    public static RepositoriesApi getRepositoriesApi(){
        Retrofit retrofit = RetrofitClient.getClient();
        return retrofit.create(RepositoriesApi.class);
    }

}



