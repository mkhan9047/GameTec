package app.gametec.com.gametec.ModelPackages;


import com.google.gson.annotations.SerializedName;


public class User{

	@SerializedName("role")
	private String role;

	@SerializedName("mobile")
	private Object mobile;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("id")
	private int id;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("email")
	private String email;

	@SerializedName("status")
	private int status;

	public String getRole(){
		return role;
	}

	public Object getMobile(){
		return mobile;
	}

	public String getLastName(){
		return lastName;
	}

	public int getId(){
		return id;
	}

	public String getFirstName(){
		return firstName;
	}

	public String getEmail(){
		return email;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"User{" + 
			"role = '" + role + '\'' + 
			",mobile = '" + mobile + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",id = '" + id + '\'' + 
			",first_name = '" + firstName + '\'' + 
			",email = '" + email + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}