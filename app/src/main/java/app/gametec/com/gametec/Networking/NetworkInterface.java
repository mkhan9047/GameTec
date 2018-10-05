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
import app.gametec.com.gametec.ModelPackages.MachineReset;
import app.gametec.com.gametec.ModelPackages.PercentControl;
import app.gametec.com.gametec.ModelPackages.SignIn;
import app.gametec.com.gametec.ModelPackages.Ticket;
import app.gametec.com.gametec.ModelPackages.UpdateFeatures;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface NetworkInterface {

    @FormUrlEncoded
    @POST("api/login")
    Call<String> getSignedIN(@Field("email") String email, @Field("password")String password);

    @GET("api/user/features")
    Call<Features> getFeatures(@Header("Authorization") String token);

    @GET("api/alarm")
    Call<Alarm> getAlarm(@Header("Authorization")String token);

    @GET("api/balance")
    Call<Balance> getBalance(@Header("Authorization")String token);

    @GET("api/tickets")
    Call<Ticket> getTicket(@Header("Authorization")String token);

    @GET("api/clock")
    Call<Clock> getClock(@Header("Authorization")String token);

    @GET("api/door-opening")
    Call<Door> getDoor(@Header("Authorization")String token);

    @GET("api/block-machine")
    Call<Block> getBlock(@Header("Authorization")String token);

    @GET("api/bluetooth")
    Call<Bluetooth> getBluetooth(@Header("Authorization")String token);


    @GET("api/reset-machine")
    Call<MachineReset> getMachine(@Header("Authorization")String token);


    @GET("api/percent-control")
    Call<PercentControl> getPercent(@Header("Authorization")String token);

    @GET("api/gps")
    Call<Location> getLocation(@Header("Authorization")String token);


    @POST("api/balance-update")
    Call<UpdateFeatures> PostBalanceUpdate(@Header("Authorization")String token);

    @POST("api/tickets-update")
    Call<UpdateFeatures> PostTicketUdpate(@Header("Authorization")String token);

    @FormUrlEncoded
    @POST("api/alarm-update")
    Call<UpdateFeatures> PostAlarmUpdate(@Header("Authorization")String token, @Field("status") int status);

    @FormUrlEncoded
    @POST("api/door-opening-update")
    Call<UpdateFeatures> PostDoorUpdate(@Header("Authorization")String token, @Field("status") int status);

    @POST("api/gps-update")
    Call<UpdateFeatures> PostGpsUpdate(@Header("Authorization")String token);

    @POST("api/clock-update")
    Call<UpdateFeatures> PostClockUpdate(@Header("Authorization")String token);

    @FormUrlEncoded
    @POST("api/block-machine-update")
    Call<UpdateFeatures> PostBlockUpdate(@Header("Authorization")String token, @Field("status") int status);

    @POST("api/percent-control-update")
    Call<UpdateFeatures> PostPercentControl(@Header("Authorization")String token);

    @FormUrlEncoded
    @POST("api/bluetooth-update")
    Call<UpdateFeatures> PostBluetoothUpdate(@Header("Authorization")String token, @Field("status") int status);

    @POST("api/reset-machine-update")
    Call<UpdateFeatures> PostResetUpdate(@Header("Authorization")String token);

}
