package app.gametec.com.gametec.ModelPackages;


import com.google.gson.annotations.SerializedName;

public class Alarm{

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
			"Alarm{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			",last_update = '" + lastUpdate + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}




	public class Data{

		@SerializedName("alarm")
		private Alarm alarm;

		public Alarm getAlarm(){
			return alarm;
		}

		@Override
		public String toString(){
			return
					"Data{" +
							"alarm = '" + alarm + '\'' +
							"}";
		}
	}
}