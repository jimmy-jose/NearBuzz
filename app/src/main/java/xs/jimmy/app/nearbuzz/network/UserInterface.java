package xs.jimmy.app.nearbuzz.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import xs.jimmy.app.nearbuzz.models.User;

public interface UserInterface {
    @GET("todos/{id}")
    Call<User> getUserData(@Path("id") int id);
}
