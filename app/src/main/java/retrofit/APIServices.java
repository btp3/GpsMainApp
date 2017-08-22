package retrofit;

/**
 * Created by Iron_Man on 24/06/17.
 */

import models.LocationModel;
import models.QRModel;
import models.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIServices {

    @POST("/api/location/create/")
    Call<LocationModel> savePost(@Body LocationModel locationModel);

    @Multipart
    @POST("/api/accounts/users/create/")
    Call<UserModel> createUser();

    @GET("/api/accounts/drivers/{license_num}")
    Call<QRModel> getDriverDetails(@Path("license_num") String license_num);
}