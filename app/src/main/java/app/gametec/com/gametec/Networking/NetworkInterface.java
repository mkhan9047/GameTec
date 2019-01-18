package app.gametec.com.gametec.Networking;

import android.icu.text.MessagePattern;

import app.gametec.com.gametec.ModelPackages.Alarm;
import app.gametec.com.gametec.ModelPackages.Balance;
import app.gametec.com.gametec.ModelPackages.Block;
import app.gametec.com.gametec.ModelPackages.Bluetooth;
import app.gametec.com.gametec.ModelPackages.Clock;
import app.gametec.com.gametec.ModelPackages.Door;
import app.gametec.com.gametec.ModelPackages.Features;
import app.gametec.com.gametec.ModelPackages.Location;
import app.gametec.com.gametec.ModelPackages.Machine;
import app.gametec.com.gametec.ModelPackages.MachineReset;
import app.gametec.com.gametec.ModelPackages.PercentControl;
import app.gametec.com.gametec.ModelPackages.Ticket;
import app.gametec.com.gametec.ModelPackages.UpdateFeatures;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NetworkInterface {

    @FormUrlEncoded
    @POST("api/login")
    Call<String> getSignedIN(@Field("email") String email, @Field("password") String password);

    @GET("api/user/features")
    Call<String> getFeatures(@Header("Authorization") String token);

    @GET("api/alarm")
    Call<String> getAlarm(@Header("Authorization") String token, @Query("machine_id") int id);

    @GET("api/balance")
    Call<String> getBalance(@Header("Authorization") String token, @Query("machine_id") int id);

    @GET("api/tickets")
    Call<String> getTicket(@Header("Authorization") String token, @Query("machine_id") int id);

    @GET("api/clock")
    Call<String> getClock(@Header("Authorization") String token, @Query("machine_id") int id);

    @GET("api/door-opening")
    Call<String> getDoor(@Header("Authorization") String token, @Query("machine_id") int id);

    @GET("api/block-machine")
    Call<String> getBlock(@Header("Authorization") String token, @Query("machine_id") int id);

    @GET("api/bluetooth")
    Call<String> getBluetooth(@Header("Authorization") String token, @Query("machine_id") int id);


    @GET("api/reset-machine")
    Call<String> getMachine(@Header("Authorization") String token, @Query("machine_id") int id);


    @GET("api/percent-control")
    Call<String> getPercent(@Header("Authorization") String token, @Query("machine_id") int id);

    @GET("api/gps")
    Call<String> getLocation(@Header("Authorization") String token, @Query("machine_id") int id);


    @POST("api/balance-update")
    Call<String> PostBalanceUpdate(@Header("Authorization") String token, @Query("machine_id") int id);

    @POST("api/tickets-update")
    Call<String> PostTicketUdpate(@Header("Authorization") String token, @Query("machine_id") int id);

    @FormUrlEncoded
    @POST("api/alarm-update")
    Call<String> PostAlarmUpdate(@Header("Authorization") String token, @Field("status") int status, @Field("machine_id") int machine_id);

    @FormUrlEncoded
    @POST("api/door-opening-update")
    Call<String> PostDoorUpdate(@Header("Authorization") String token, @Field("status") int status, @Field("machine_id") int id);

    @POST("api/gps-update")
    Call<String> PostGpsUpdate(@Header("Authorization") String token, @Query("machine_id") int id);

    @POST("api/clock-update")
    Call<String> PostClockUpdate(@Header("Authorization") String token, @Query("machine_id") int id);

    @FormUrlEncoded
    @POST("api/block-machine-update")
    Call<String> PostBlockUpdate(@Header("Authorization") String token, @Field("status") int status, @Field("machine_id") int id);

    @POST("api/percent-control-update")
    Call<String> PostPercentControl(@Header("Authorization") String token, @Query("machine_id") int id);

    @FormUrlEncoded
    @POST("api/bluetooth-update")
    Call<String> PostBluetoothUpdate(@Header("Authorization") String token, @Field("status") int status, @Field("machine_id") int id);

    @FormUrlEncoded
    @POST("api/reset-machine-update")
    Call<String> PostResetUpdate(@Header("Authorization") String token, @Field("machine_id") int id);

    @GET("api/user/machines")
    Call<String> getMachineList(@Header("Authorization") String token);


    @FormUrlEncoded
    @POST("api/change-percentage")
    Call<String> setPercentengeToMachine(@Header("Authorization") String token, @Field("machine_id") int id, @Field("machine_percentage") double number);

}
