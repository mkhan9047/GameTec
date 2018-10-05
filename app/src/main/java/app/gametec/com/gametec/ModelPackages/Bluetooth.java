package app.gametec.com.gametec.ModelPackages;


import com.google.gson.annotations.SerializedName;

public class Bluetooth{

	@SerializedName("data")
	private Data data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("last_update")
	private String lastUpdate;

	@SerializedName("status")
	private int status;

	public Data getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}

	public String getLastUpdate(){
		return lastUpdate;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Bluetooth{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			",last_update = '" + lastUpdate + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}

	public class Data{

		@SerializedName("bluetooth")
		private Bluetooth bluetooth;

		public Bluetooth getBluetooth(){
			return bluetooth;
		}

		@Override
		public String toString(){
			return
					"Data{" +
							"bluetooth = '" + bluetooth + '\'' +
							"}";
		}
	}
}