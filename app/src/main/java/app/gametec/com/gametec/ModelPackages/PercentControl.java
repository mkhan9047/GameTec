package app.gametec.com.gametec.ModelPackages;


import com.google.gson.annotations.SerializedName;

public class PercentControl{

	@SerializedName("data")
	private Data data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("machine")
	private String machine;

	@SerializedName("last_update")
	private String lastUpdate;

	@SerializedName("user")
	private String user;

	public Data getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}

	public String getMachine(){
		return machine;
	}

	public String getLastUpdate(){
		return lastUpdate;
	}

	public String getUser(){
		return user;
	}

	@Override
 	public String toString(){
		return 
			"PercentControl{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			",machine = '" + machine + '\'' + 
			",last_update = '" + lastUpdate + '\'' + 
			",user = '" + user + '\'' + 
			"}";
		}

	public class Data{

		@SerializedName("percent_control")
		private PercentControl percentControl;

		public PercentControl getPercentControl(){
			return percentControl;
		}

		@Override
		public String toString(){
			return
					"Data{" +
							"percent_control = '" + percentControl + '\'' +
							"}";
		}
	}

}