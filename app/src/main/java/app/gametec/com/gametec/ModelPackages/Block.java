package app.gametec.com.gametec.ModelPackages;


import com.google.gson.annotations.SerializedName;

public class Block{

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
			"Block{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}

	public class BlockMachine{

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
					"BlockMachine{" +
							"last_update = '" + lastUpdate + '\'' +
							",status = '" + status + '\'' +
							"}";
		}
	}

	public class Data{

		@SerializedName("block_machine")
		private BlockMachine blockMachine;

		public BlockMachine getBlockMachine(){
			return blockMachine;
		}

		@Override
		public String toString(){
			return
					"Data{" +
							"block_machine = '" + blockMachine + '\'' +
							"}";
		}
	}

}