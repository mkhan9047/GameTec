package app.gametec.com.gametec.ModelPackages;


import com.google.gson.annotations.SerializedName;


public class FeaturesItem{

	@SerializedName("name")
	private String name;

	@SerializedName("slug")
	private String slug;

	public String getName(){
		return name;
	}

	public String getSlug(){
		return slug;
	}

	@Override
 	public String toString(){
		return 
			"FeaturesItem{" + 
			"name = '" + name + '\'' + 
			",slug = '" + slug + '\'' + 
			"}";
		}
}