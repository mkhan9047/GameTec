package app.gametec.com.gametec.ModelPackages;

import com.google.gson.annotations.SerializedName;

public class Balance{

	@SerializedName("data")
	private Data data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("total")
	private String total;

	@SerializedName("in")
	private String in;

	@SerializedName("last_update")
	private String lastUpdate;

	@SerializedName("out")
	private String out;

	public Data getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}

	public String getTotal(){
		return total;
	}

	public String getIn(){
		return in;
	}

	public String getLastUpdate(){
		return lastUpdate;
	}

	public String getOut(){
		return out;
	}



	public class Data{

		@SerializedName("balance")
		private Balance balance;

		public Balance getBalance(){
			return balance;
		}


	}
}