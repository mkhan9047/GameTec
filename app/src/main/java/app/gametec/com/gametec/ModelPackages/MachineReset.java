package app.gametec.com.gametec.ModelPackages;


import com.google.gson.annotations.SerializedName;


public class MachineReset{

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
			"MachineReset{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}

	public class ResetMachine{

		@SerializedName("last_update")
		private String lastUpdate;

		@SerializedName("time")
		private String time;

		public String getLastUpdate(){
			return lastUpdate;
		}

		public String getTime(){
			return time;
		}

		@Override
		public String toString(){
			return
					"ResetMachine{" +
							"last_update = '" + lastUpdate + '\'' +
							",time = '" + time + '\'' +
							"}";
		}
	}

	public class Data{

		@SerializedName("reset_machine")
		private ResetMachine resetMachine;

		public ResetMachine getResetMachine(){
			return resetMachine;
		}

		@Override
		public String toString(){
			return
					"Data{" +
							"reset_machine = '" + resetMachine + '\'' +
							"}";
		}
	}
}