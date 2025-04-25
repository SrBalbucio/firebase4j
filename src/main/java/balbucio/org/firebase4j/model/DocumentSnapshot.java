package balbucio.org.firebase4j.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@RequiredArgsConstructor
public class DocumentSnapshot {

    @Getter
    private String path;
    @Getter
    @NonNull
    private String collection;
    @Getter
    @NonNull
    private String database;
    @Getter
    private String name = "";
    @Getter
    private Map<String, Object> fields = new HashMap<>();
    @Getter
    private String createTime;
    @Getter
    private String updateTime;

    public String asString(String key) {
        return (String) fields.get(key);
    }

    public Integer asInt(String key) {
        return (Integer) fields.get(key);
    }

    public Boolean asBool(String key) {
        return (Boolean) fields.get(key);
    }

    public Map<String, Object> asMap(String key) {
        return (Map<String, Object>) fields.get(key);
    }

    public List<Object> asList(String key) {
        return (List<Object>) fields.get(key);
    }

    public DocumentSnapshot put(String key, Object value) {
        fields.put(key, value);
        return this;
    }

    public void fromPayload(JSONObject jsonData) {
        this.path = jsonData.getString("name");
        this.name = path.substring(path.lastIndexOf("/")+1);
        this.createTime = jsonData.getString("createTime");
        this.updateTime = jsonData.getString("updateTime");

        JSONObject fields = jsonData.getJSONObject("fields");
        for (String key : fields.keySet()) {
            JSONObject value = fields.getJSONObject(key);
            this.fields.put(key, deserializeValue(value));
        }
    }

    public void replaceValues(DocumentSnapshot snapshot){
        this.fields = snapshot.getFields();
    }

    public void mergeValues(DocumentSnapshot snapshot){
        this.fields.putAll(snapshot.getFields());
    }

    public Map<String, Object> toPayload() {
        Map<String, Object> objs = new HashMap<>();

        fields.forEach((k, v) -> objs.put(k, serializeValue(v)));

        return objs;
    }

    public static JSONObject serializeValue(Object value) {
        JSONObject json = new JSONObject();
        if (value instanceof String) {
            json.put("stringValue", value);
        } else if (value instanceof Integer) {
            json.put("integerValue", value);
        } else if (value instanceof Boolean) {
            json.put("booleanValue", value);
        } else if (value instanceof Map) {
            JSONObject mapValues = new JSONObject();
            JSONObject fields = new JSONObject();
            Map<String, Object> map = (Map<String, Object>) value;
            map.forEach((key, value1) -> {
                fields.put(key, serializeValue(value1));
            });
            mapValues.put("fields", fields);
            json.put("mapValue", mapValues);
        } else if (value instanceof List) {
            JSONObject arrayValues = new JSONObject();
            JSONArray array = new JSONArray();
            List<Object> list = (List<Object>) value;
            list.forEach((obj) -> array.put(serializeValue(obj)));
            arrayValues.put("values", array);
            json.put("arrayValue", arrayValues);
        }
        return json;
    }

    public static Object deserializeValue(JSONObject value) {
        if (value.has("stringValue")) {
            return value.getString("stringValue");
        } else if (value.has("integerValue")) {
            return value.getInt("integerValue");
        } else if (value.has("arrayValue")) {
            JSONArray array = value.getJSONObject("arrayValue").getJSONArray("values");
            List<Object> objs = new ArrayList<>();
            array.forEach(o -> objs.add(deserializeValue((JSONObject) o)));
            return objs;
        } else if (value.has("mapValue")) {
            Map<String, Object> map = new HashMap<>();
            JSONObject mapValues = value.getJSONObject("mapValue").getJSONObject("fields");
            mapValues.keySet().forEach(key -> map.put(key, deserializeValue(mapValues.getJSONObject(key))));
            return map;
        } else if (value.has("booleanValue")) {
            return value.getBoolean("booleanValue");
        }

        return null;
    }
}
