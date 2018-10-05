package app.gametec.com.gametec.ModelPackages;


import com.google.gson.annotations.SerializedName;

public class SignIn {

    private boolean success;
    private String message;


    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    private Data data;

    public Data getData() {
        return data;
    }


    public class Data{

        @SerializedName("credentials")
        private Credentials credentials;

        @SerializedName("user")
        private User user;

        public Credentials getCredentials(){
            return credentials;
        }

        public User getUser(){
            return user;
        }

        @Override
        public String toString(){
            return
                    "Data{" +
                            "credentials = '" + credentials + '\'' +
                            ",user = '" + user + '\'' +
                            "}";
        }
    }

}