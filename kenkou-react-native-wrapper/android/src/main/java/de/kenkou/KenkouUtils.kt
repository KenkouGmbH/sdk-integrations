package de.kenkou

import android.os.Build
import androidx.annotation.RequiresApi
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import com.google.gson.*
import de.kenkou.sdk.headless.domain.model.anamnesis.obqa.OnboardingQuestionnaireAnswersModel
import de.kenkou.sdk.headless.domain.model.measurement.HistoryItem
import de.kenkou.sdk.headless.domain.model.measurement.Measurement
import de.kenkou.sdk.headless.domain.model.measurement.PostMeasurementQuestionnaireAnswers
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant

class KenkouUtils {

  companion object {

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getGson() : Gson {
      return GsonBuilder()
        .registerTypeAdapter(Instant::class.java, InstantConverter())
        .create()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWritableAnswers(answers: OnboardingQuestionnaireAnswersModel) : WritableMap {
      val jsonString = getGson().toJson(answers)
      return JsonUtils.jsonObjectToWritableMap(JSONObject(jsonString))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getReadableAnswers(map: ReadableMap) : OnboardingQuestionnaireAnswersModel {
      val jsonObject = JsonUtils.convertMapToJson(map)
      return getGson().fromJson(jsonObject.toString(), OnboardingQuestionnaireAnswersModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWritableMeasurement(measurement: Measurement) : WritableMap {
      val jsonString = getGson().toJson(measurement)
      return JsonUtils.jsonObjectToWritableMap(JSONObject(jsonString))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getReadableMeasurement(map: ReadableMap) : Measurement {
      val jsonObject = JsonUtils.convertMapToJson(map)
      return getGson().fromJson(jsonObject.toString(), Measurement::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWritableHistoryItem(historyItem: HistoryItem) : WritableMap {
      val jsonString = getGson().toJson(historyItem)
      return JsonUtils.jsonObjectToWritableMap(JSONObject(jsonString))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getReadableHistoryItem(map: ReadableMap) : HistoryItem {
      val jsonObject = JsonUtils.convertMapToJson(map)
      return getGson().fromJson(jsonObject.toString(), HistoryItem::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWritableHistory(history: List<HistoryItem>) : WritableArray {
      val jsonString = getGson().toJson(history)
      return JsonUtils.jsonArrayToWritableArray(JSONArray(jsonString))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getReadableHistory(array: ReadableArray) : List<HistoryItem> {
      val jsonArray = JsonUtils.convertArrayToJson(array)
      return getGson().fromJson(jsonArray.toString(), Array<HistoryItem>::class.java).toList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWritablePostAnswers(answers: PostMeasurementQuestionnaireAnswers) : WritableMap {
      val jsonString = getGson().toJson(answers)
      return JsonUtils.jsonObjectToWritableMap(JSONObject(jsonString))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getReadablePostAnswers(map: ReadableMap) : PostMeasurementQuestionnaireAnswers {
      val jsonObject = JsonUtils.convertMapToJson(map)
      return getGson().fromJson(jsonObject.toString(), PostMeasurementQuestionnaireAnswers::class.java)
    }
  }
}
