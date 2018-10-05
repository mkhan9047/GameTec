package app.gametec.com.gametec.ModelPackages;


import com.google.gson.annotations.SerializedName;


public class Ticket{

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
			"Ticket{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}

	public class Data{

		@SerializedName("tickets")
		private Tickets tickets;

		public Tickets getTickets(){
			return tickets;
		}

		@Override
		public String toString(){
			return
					"Data{" +
							"tickets = '" + tickets + '\'' +
							"}";
		}
	}



	public class Tickets{

		@SerializedName("month_total")
		private int monthTotal;

		@SerializedName("last_update")
		private String lastUpdate;

		public int getMonthTotal(){
			return monthTotal;
		}

		public String getLastUpdate(){
			return lastUpdate;
		}

		@Override
		public String toString(){
			return
					"Tickets{" +
							"month_total = '" + monthTotal + '\'' +
							",last_update = '" + lastUpdate + '\'' +
							"}";
		}
	}
}