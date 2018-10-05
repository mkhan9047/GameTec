package app.gametec.com.gametec.ModelPackages;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Features{

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
			"Features{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}



	public static class Data{

		@SerializedName("features")
		private List<FeaturesItem> features;

		public List<FeaturesItem> getFeatures(){
			return features;
		}

		@Override
		public String toString(){
			return
					"Data{" +
							"features = '" + features + '\'' +
							"}";
		}
	}
}