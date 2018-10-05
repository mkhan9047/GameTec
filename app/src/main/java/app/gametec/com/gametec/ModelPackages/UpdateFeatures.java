package app.gametec.com.gametec.ModelPackages;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UpdateFeatures{

	@SerializedName("data")
	private List<Object> data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	public List<Object> getData(){
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
			"UpdateFeatures{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}