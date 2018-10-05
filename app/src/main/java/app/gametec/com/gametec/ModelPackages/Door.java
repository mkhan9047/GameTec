package app.gametec.com.gametec.ModelPackages;


import com.google.gson.annotations.SerializedName;

public class Door{

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
			"Door{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}


	public class DoorOpening{

		@SerializedName("last_update")
		private String lastUpdate;

		@SerializedName("status")
		private int status;

		public String getLastUpdate(){
			return lastUpdate;
		}

		public int getStatus(){
			return status;
		}

		@Override
		public String toString(){
			return
					"DoorOpening{" +
							"last_update = '" + lastUpdate + '\'' +
							",status = '" + status + '\'' +
							"}";
		}
	}

	public class Data{

		@SerializedName("door_opening")
		private DoorOpening doorOpening;

		public DoorOpening getDoorOpening(){
			return doorOpening;
		}

		@Override
		public String toString(){
			return
					"Data{" +
							"door_opening = '" + doorOpening + '\'' +
							"}";
		}
	}
}