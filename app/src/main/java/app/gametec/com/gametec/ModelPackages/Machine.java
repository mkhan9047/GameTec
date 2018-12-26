package app.gametec.com.gametec.ModelPackages;


import com.google.gson.annotations.SerializedName;
import java.util.List;


public class Machine{

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
			"Machine{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}


	public class Data{

		@SerializedName("machines")
		private List<MachinesItem> machines;

		public List<MachinesItem> getMachines(){
			return machines;
		}

		@Override
		public String toString(){
			return
					"Data{" +
							"machines = '" + machines + '\'' +
							"}";
		}
	}


	public class MachinesItem{

		@SerializedName("name")
		private String name;

		@SerializedName("id")
		private int id;

		public String getName(){
			return name;
		}

		public int getId(){
			return id;
		}

		@Override
		public String toString(){
			return
					"MachinesItem{" +
							"name = '" + name + '\'' +
							",id = '" + id + '\'' +
							"}";
		}
	}
}