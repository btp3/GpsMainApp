package retrofit;

/**
 * Created by Iron_Man on 24/06/17.
 */

import models.LocationModel;
import models.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface APIServices {

    @POST("/api/create/")
    Call<LocationModel> savePost(@Body LocationModel locationModel);

    @Multipart
    @POST("/api/accounts/user/create/")
    Call<UserModel> createUser();
}