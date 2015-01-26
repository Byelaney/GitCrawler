package tojson;

import java.util.List;

import usefuldata.Vitality;

import com.google.gson.JsonObject;

public class VitalityToJson {

	public String ToJson(Vitality vitality) {
		JsonObject json = new JsonObject();
		json.addProperty("date", vitality.getDate());
		json.addProperty("number", vitality.getVitality());
		return json.toString();
	}

	public String ToJson(List<Vitality> vitalities) {
		String result = "";
		if(vitalities.size()!=1){
		result = "[";
		for(Vitality a :vitalities){
			String tmp = ToJson(a);
			result += tmp + ",";
		}
		
		result = (String) result.subSequence(0, result.length()-1);
		result += "]";}
		else{
			result = ToJson(vitalities.get(0));
		}
		return result;
	}

}
