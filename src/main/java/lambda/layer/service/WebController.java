package lambda.layer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lambda.layer.data.access.MongoDBDAO;
import lambda.layer.data.access.SingletonMongoDBDAO;
import lambda.layer.data.access.dto.ModelDTO;

@Controller
public class WebController {

	private MongoDBDAO dao;

	public WebController() {
		dao = SingletonMongoDBDAO.getMongoDBDAO();
	}

	@RequestMapping("/online")
	public String online(Map<String, Object> model) {
		Map<String, Long> data = getOnlineModel();

		try {
			model.put("data", getJsonFromMap(data));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "view";
	}

	@RequestMapping("/offline")
	public String offline(Map<String, Object> model) {
		Map<String, Long> data = getOfflineModel();

		try {
			model.put("data", getJsonFromMap(data));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "view";

	}

	@RequestMapping("/lambda")
	public String lambda(Map<String, Object> model) {
		Map<String, Long> data = getLambdaModel();

		try {
			model.put("data", getJsonFromMap(data));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "view";
	}

	@RequestMapping("/online_language")
	public String onlineLanguage(Map<String, Object> model) {
		Map<String, Long> data = getOnlineLanguageModel();

		try {
			model.put("data", getJsonListFromMap(data));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "view_language";
	}

	@RequestMapping("/offline_language")
	public String offlineLanguage(Map<String, Object> model) {
		Map<String, Long> data = getOfflineLanguageModel();

		try {
			model.put("data", getJsonListFromMap(data));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "view_language";

	}

	@RequestMapping("/lambda_language")
	public String lambdaLanguage(Map<String, Object> model) {
		Map<String, Long> data = getLambdaLanguageModel();

		try {
			model.put("data", getJsonListFromMap(data));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "view_language";
	}

	private JSONObject getJsonFromMap(Map<String, Long> map) throws JSONException {
		JSONObject jsonData = new JSONObject();
		for (String key : map.keySet()) {
			Long value = map.get(key);
			jsonData.put(key, value);
		}
		return jsonData;
	}

	private JSONArray getJsonListFromMap(Map<String, Long> map) throws JSONException {
		JSONArray jsonArrayData = new JSONArray();

		for (String key : map.keySet()) {
			JSONObject jsonData = new JSONObject();
			Long value = map.get(key);
			jsonData.put("id", key);
			jsonData.put("value", value);
			jsonArrayData.put(jsonData);
		}
		return jsonArrayData;
	}

	@RequestMapping("/online_model")
	@ResponseBody
	public Map<String, Long> getOnlineModel() {
		Long timestamp = dao.getOfflineModel().getTimestamp();
		return getTags(dao.getOnlineModel(timestamp));
	}

	@RequestMapping("/offline_model")
	@ResponseBody
	public Map<String, Long> getOfflineModel() {
		List<ModelDTO> result = new ArrayList<>();
		result.add(dao.getOfflineModel());
		return getTags(result);
	}

	@RequestMapping("/lambda_model")
	@ResponseBody
	public Map<String, Long> getLambdaModel() {
		List<ModelDTO> result = new ArrayList<>();
		ModelDTO model = dao.getOfflineModel();
		result.add(model);
		result.addAll(dao.getOnlineModel(model.getTimestamp()));
		return getTags(result);
	}

	@RequestMapping("/online_language_model")
	@ResponseBody
	public Map<String, Long> getOnlineLanguageModel() {
		Long timestamp = dao.getOfflineModel().getTimestamp();
		return getLanguages(dao.getOnlineModel(timestamp));
	}

	@RequestMapping("/offline_language_model")
	@ResponseBody
	public Map<String, Long> getOfflineLanguageModel() {
		List<ModelDTO> result = new ArrayList<>();
		result.add(dao.getOfflineModel());
		return getLanguages(result);
	}

	@RequestMapping("/lambda_language_model")
	@ResponseBody
	public Map<String, Long> getLambdaLanguageModel() {
		List<ModelDTO> result = new ArrayList<>();
		ModelDTO model = dao.getOfflineModel();
		result.add(model);
		result.addAll(dao.getOnlineModel(model.getTimestamp()));
		return getLanguages(result);
	}

	private Map<String, Long> getTags(List<? extends ModelDTO> lista) {
		Map<String, Long> result = new HashMap<>();
		for (ModelDTO model: lista){
			List<String> hashtags = model.getClaves();
			List<Long> frequencies = model.getFrecuencias();
			for (int i = 0; i < hashtags.size(); i++){
				String clave = hashtags.get(i);
				Long value = frequencies.get(i);
				if (result.containsKey(clave)) {
					result.put(clave, result.get(clave) + value);
				} else {
					result.put(clave, value);
				}
			}

		}
		return result;
	}

	private Map<String, Long> getLanguages(List<? extends ModelDTO> lista) {
		Map<String, Long> result = new HashMap<>();
		for (ModelDTO model: lista){
			List<String> languages = model.getIdiomas();
			List<Long> freqIdiomas = model.getFrecuenciasIdiomas();
			for (int i = 0; i < languages.size(); i++){
				String language = languages.get(i);
				Long value = freqIdiomas.get(i);
				if (result.containsKey(language)) {
					result.put(language, result.get(language) + value);
				} else {
					result.put(language, value);
				}
			}

		}
		return result;
	}

}