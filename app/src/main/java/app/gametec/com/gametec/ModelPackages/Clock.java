package app.gametec.com.gametec.ModelPackages;

import com.google.gson.annotations.SerializedName;

public class Clock{

	@SerializedName("data")
	private Data data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("last_update")
	private String lastUpdate;

	@SerializedName("time")
	private String time;

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

	public String getTime(){
		return time;
	}

	@Override
 	public String toString(){
		return 
			"Clock{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			",last_update = '" + lastUpdate + '\'' + 
			",time = '" + time + '\'' + 
			"}";
		}


	public class Data{

		@SerializedName("clock")
		private Clock clock;

		public Clock getClock(){
			return clock;
		}

		@Override
		public String toString(){
			return
					"Data{" +
							"clock = '" + clock + '\'' +
							"}";
		}
	}
}