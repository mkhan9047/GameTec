package app.gametec.com.gametec.ModelPackages;


import com.google.gson.annotations.SerializedName;

public class Location{

	@SerializedName("data")
	private Data data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	public Data getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"Location{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}


	public class Gps{

		@SerializedName("address")
		private String address;

		@SerializedName("latitude")
		private String latitude;

		@SerializedName("last_update")
		private String lastUpdate;

		@SerializedName("longitude")
		private String longitude;

		public String getAddress(){
			return address;
		}

		public String getLatitude(){
			return latitude;
		}

		public String getLastUpdate(){
			return lastUpdate;
		}

		public String getLongitude(){
			return longitude;
		}

		@Override
		public String toString(){
			return
					"Gps{" +
							"address = '" + address + '\'' +
							",latitude = '" + latitude + '\'' +
							",last_update = '" + lastUpdate + '\'' +
							",longitude = '" + longitude + '\'' +
							"}";
		}
	}

	public class Data{

		@SerializedName("gps")
		private Gps gps;

		public Gps getGps(){
			return gps;
		}

		@Override
		public String toString(){
			return
					"Data{" +
							"gps = '" + gps + '\'' +
							"}";
		}
	}
}