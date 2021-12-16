package de.kenkou;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
public class JsonUtils {
  private static final String TAG = "Utils";
  private static final String RN_DEVSUPPORT_CLASS = "DevSupportManagerImpl";
  private static final String RN_DEVSUPPORT_PACKAGE = "com.facebook.react.devsupport";

  private static final String EXPO_REGISTRY_CLASS = "ModuleRegistry";
  private static final String EXPO_CORE_PACKAGE = "expo.core";

  private static final String FLUTTER_REGISTRY_CLASS = "PluginRegistry";
  private static final String FLUTTER_CORE_PACKAGE = "io.flutter.plugin.common";

  private static final String REACT_NATIVE_REGISTRY_CLASS = "NativeModuleRegistry";
  private static final String REACT_NATIVE_CORE_PACKAGE = "com.facebook.react.bridge";

  public static WritableMap getExceptionMap(Exception exception) {
    WritableMap exceptionMap = Arguments.createMap();
    String code = "unknown";
    String message = exception.getMessage();
    exceptionMap.putString("code", code);
    exceptionMap.putString("nativeErrorCode", code);
    exceptionMap.putString("message", message);
    exceptionMap.putString("nativeErrorMessage", message);
    return exceptionMap;
  }

  public static WritableMap jsonObjectToWritableMap(JSONObject jsonObject) throws JSONException {
    Iterator<String> iterator = jsonObject.keys();
    WritableMap writableMap = Arguments.createMap();

    while (iterator.hasNext()) {
      String key = iterator.next();
      Object value = jsonObject.get(key);
      if (value instanceof Integer) {
        writableMap.putInt(key, jsonObject.getInt(key));
      } else if (value instanceof Number) {
        writableMap.putDouble(key, jsonObject.getDouble(key));
      } else if (value instanceof Boolean) {
        writableMap.putBoolean(key, jsonObject.getBoolean(key));
      } else if (value instanceof String) {
        writableMap.putString(key, jsonObject.getString(key));
      } else if (value instanceof JSONObject) {
        writableMap.putMap(key, jsonObjectToWritableMap(jsonObject.getJSONObject(key)));
      } else if (value instanceof JSONArray) {
        writableMap.putArray(key, jsonArrayToWritableArray(jsonObject.getJSONArray(key)));
      } else if (value == JSONObject.NULL) {
        writableMap.putNull(key);
      }
    }

    return writableMap;
  }

  public static WritableArray jsonArrayToWritableArray(JSONArray jsonArray) throws JSONException {
    WritableArray writableArray = Arguments.createArray();

    for (int i = 0; i < jsonArray.length(); i++) {
      Object value = jsonArray.get(i);
      if (value instanceof Integer) {
        writableArray.pushInt(jsonArray.getInt(i));
      } else if (value instanceof Number) {
        writableArray.pushDouble(jsonArray.getDouble(i));
      } else if (value instanceof Boolean) {
        writableArray.pushBoolean(jsonArray.getBoolean(i));
      } else if (value instanceof String) {
        writableArray.pushString(jsonArray.getString(i));
      } else if (value instanceof JSONObject) {
        writableArray.pushMap(jsonObjectToWritableMap(jsonArray.getJSONObject(i)));
      } else if (value instanceof JSONArray) {
        writableArray.pushArray(jsonArrayToWritableArray(jsonArray.getJSONArray(i)));
      } else if (value == JSONObject.NULL) {
        writableArray.pushNull();
      }
    }
    return writableArray;
  }

  public static WritableMap mapToWritableMap(Map<String, Object> value) {
    WritableMap writableMap = Arguments.createMap();

    for (Map.Entry<String, Object> entry : value.entrySet()) {
      mapPutValue(entry.getKey(), entry.getValue(), writableMap);
    }

    return writableMap;
  }

  public static WritableArray listToWritableArray(List<Object> objects) {
    WritableArray writableArray = Arguments.createArray();
    for (Object object : objects) {
      arrayPushValue(object, writableArray);
    }
    return writableArray;
  }

  @SuppressWarnings("unchecked")
  public static void arrayPushValue(@Nullable Object value, WritableArray array) {
    if (value == null || value == JSONObject.NULL) {
      array.pushNull();
      return;
    }

    String type = value.getClass().getName();
    switch (type) {
      case "java.lang.Boolean":
        array.pushBoolean((Boolean) value);
        break;
      case "java.lang.Long":
        Long longVal = (Long) value;
        array.pushDouble((double) longVal);
        break;
      case "java.lang.Float":
        float floatVal = (float) value;
        array.pushDouble((double) floatVal);
        break;
      case "java.lang.Double":
        array.pushDouble((double) value);
        break;
      case "java.lang.Integer":
        array.pushInt((int) value);
        break;
      case "java.lang.String":
        array.pushString((String) value);
        break;
      case "org.json.JSONObject$1":
        try {
          array.pushMap(jsonObjectToWritableMap((JSONObject) value));
        } catch (JSONException e) {
          array.pushNull();
        }
        break;
      case "org.json.JSONArray$1":
        try {
          array.pushArray(jsonArrayToWritableArray((JSONArray) value));
        } catch (JSONException e) {
          array.pushNull();
        }
        break;
      default:
        if (List.class.isAssignableFrom(value.getClass())) {
          array.pushArray(listToWritableArray((List<Object>) value));
        } else if (Map.class.isAssignableFrom(value.getClass())) {
          array.pushMap(mapToWritableMap((Map<String, Object>) value));
        } else {
          Log.d(TAG, "utils:arrayPushValue:unknownType:" + type);
          array.pushNull();
        }
    }
  }

  @SuppressWarnings("unchecked")
  public static void mapPutValue(String key, @Nullable Object value, WritableMap map) {
    if (value == null || value == JSONObject.NULL) {
      map.putNull(key);
      return;
    }

    String type = value.getClass().getName();
    switch (type) {
      case "java.lang.Boolean":
        map.putBoolean(key, (Boolean) value);
        break;
      case "java.lang.Long":
        Long longVal = (Long) value;
        map.putDouble(key, (double) longVal);
        break;
      case "java.lang.Float":
        float floatVal = (float) value;
        map.putDouble(key, (double) floatVal);
        break;
      case "java.lang.Double":
        map.putDouble(key, (double) value);
        break;
      case "java.lang.Integer":
        map.putInt(key, (int) value);
        break;
      case "java.lang.String":
        map.putString(key, (String) value);
        break;
      case "org.json.JSONObject$1":
        try {
          map.putMap(key, jsonObjectToWritableMap((JSONObject) value));
        } catch (JSONException e) {
          map.putNull(key);
        }
        break;
      case "org.json.JSONArray$1":
        try {
          map.putArray(key, jsonArrayToWritableArray((JSONArray) value));
        } catch (JSONException e) {
          map.putNull(key);
        }
        break;
      default:
        if (List.class.isAssignableFrom(value.getClass())) {
          map.putArray(key, listToWritableArray((List<Object>) value));
        } else if (Map.class.isAssignableFrom(value.getClass())) {
          map.putMap(key, mapToWritableMap((Map<String, Object>) value));
        } else {
          Log.d(TAG, "utils:mapPutValue:unknownType:" + type);
          map.putNull(key);
        }
    }
  }


  /**
   * Convert a ReadableMap to a WritableMap for the purposes of re-sending back to JS
   *
   * @param map ReadableMap
   * @return WritableMap
   */
  public static WritableMap readableMapToWritableMap(ReadableMap map) {
    WritableMap writableMap = Arguments.createMap();
    writableMap.merge(map);
    return writableMap;
  }

  public static JSONObject convertMapToJson(ReadableMap readableMap) throws JSONException {
    JSONObject object = new JSONObject();
    ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
    while (iterator.hasNextKey()) {
      String key = iterator.nextKey();
      switch (readableMap.getType(key)) {
        case Null:
          object.put(key, JSONObject.NULL);
          break;
        case Boolean:
          object.put(key, readableMap.getBoolean(key));
          break;
        case Number:
          object.put(key, readableMap.getDouble(key));
          break;
        case String:
          object.put(key, readableMap.getString(key));
          break;
        case Map:
          object.put(key, convertMapToJson(readableMap.getMap(key)));
          break;
        case Array:
          object.put(key, convertArrayToJson(readableMap.getArray(key)));
          break;
      }
    }
    return object;
  }

  public static JSONArray convertArrayToJson(ReadableArray readableArray) throws JSONException {
    JSONArray array = new JSONArray();
    for (int i = 0; i < readableArray.size(); i++) {
      switch (readableArray.getType(i)) {
        case Null:
          break;
        case Boolean:
          array.put(readableArray.getBoolean(i));
          break;
        case Number:
          array.put(readableArray.getDouble(i));
          break;
        case String:
          array.put(readableArray.getString(i));
          break;
        case Map:
          array.put(convertMapToJson(readableArray.getMap(i)));
          break;
        case Array:
          array.put(convertArrayToJson(readableArray.getArray(i)));
          break;
      }
    }
    return array;
  }
}
