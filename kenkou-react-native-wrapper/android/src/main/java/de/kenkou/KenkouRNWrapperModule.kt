package de.kenkou

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.bridge.*
import de.kenkou.sdk.headless.KenkouSDKHeadless
import de.kenkou.sdk.headless.core.camera.CameraView
import de.kenkou.sdk.headless.core.service.AuthTokenProvider
import de.kenkou.sdk.headless.domain.model.OnboardingQuestionnaireException
import de.kenkou.sdk.headless.domain.model.exceptions.MeasurementFinishedException
import de.kenkou.sdk.headless.domain.model.measurement.RealtimeData
import de.kenkou.sdk.visual.KenkouSDKVisual
import de.kenkou.sdk.visual.activityresults.KenkouActivityResult
import de.kenkou.sdk.visual.activityresults.KenkouResultsMapper
import kotlinx.coroutines.runBlocking

class KenkouRNWrapperModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ActivityEventListener {

  private lateinit var promise: Promise

  override fun getName(): String {
    return "RNKenkou"
  }

  override fun initialize() {
    reactApplicationContext.addActivityEventListener(this)
  }

  override fun onCatalystInstanceDestroy() {
    reactApplicationContext.removeActivityEventListener(this)
  }

  @ReactMethod
  fun initialize(token: String) {
    KenkouSDKVisual.initialize(
      object : AuthTokenProvider() {
        override fun onTokenRequest(listener: (String) -> Unit) {
          listener.invoke(token)
        }
      },
      reactApplicationContext.applicationContext
    )
  }

  @ReactMethod
  fun presentMeasurementInstructions(promise: Promise) {
    this.promise = promise
    currentActivity?.let { KenkouSDKVisual.presentMeasurementInstructions(it) }
  }

  @ReactMethod
  fun presentOnboardingQuestionnaire(promise: Promise) {
    this.promise = promise
    currentActivity?.let { KenkouSDKVisual.presentOnboardingQuestionnaire(it) }
  }

  @ReactMethod
  fun presentMeasurement(promise: Promise) {
    this.promise = promise
    try {
      currentActivity?.let { KenkouSDKVisual.presentMeasurement(it) }
    } catch (e: OnboardingQuestionnaireException) {
      promise.reject(e)
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  @ReactMethod
  fun presentPostMeasurementQuestionnaire(measurement: ReadableMap, promise: Promise) {
    this.promise = promise
    currentActivity?.let { KenkouSDKVisual.presentPostMeasurementQuestionnaire(it, KenkouUtils.getReadableMeasurement(measurement)) }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  @ReactMethod
  fun presentMeasurementResults(measurement: ReadableMap, promise: Promise) {
    this.promise = promise
    currentActivity?.let { KenkouSDKVisual.presentMeasurementResults(it, KenkouUtils.getReadableMeasurement(measurement)) }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  @ReactMethod
  fun presentHistoryItem(historyItem: ReadableMap, promise: Promise) {
    this.promise = promise
    currentActivity?.let { KenkouSDKVisual.presentMeasurementResults(it, KenkouUtils.getReadableHistoryItem(historyItem)) }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  @ReactMethod
  fun presentHistory(history: ReadableArray, promise: Promise) {
    this.promise = promise
    currentActivity?.let { KenkouSDKVisual.presentHistory(it, KenkouUtils.getReadableHistory(history)) }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
    when (val activityResult = KenkouResultsMapper.onActivityResult(requestCode, resultCode, data)) {
      is KenkouActivityResult.Instructions -> {
        // measurement instruction showed
        promise.resolve(activityResult.isFullyViewed)
      }
      is KenkouActivityResult.Measurement -> {
        // measurement finished
        KenkouSDKVisual.presentPostMeasurementQuestionnaire(activity, activityResult.measurement)
      }
      is KenkouActivityResult.PostMeasurementQuestionnaire -> {
        // questionnaire finished
        KenkouSDKVisual.presentMeasurementResults(activity, activityResult.measurement)
      }
      is KenkouActivityResult.Results -> {
        // user watched measurement result presentation
        promise.resolve(KenkouUtils.getWritableMeasurement(activityResult.measurement))
      }
      is KenkouActivityResult.OnboardingQuestionnaireAnswers -> {
        // user pass on boarding questionnaire with given activityResult.answers
        promise.resolve(KenkouUtils.getWritableAnswers(activityResult.answers))
      }
      is KenkouActivityResult.Failed -> {
        promise.reject(if (resultCode == Activity.RESULT_CANCELED) "CANCELED" else "FAILED", "")
      }
    }
  }

  override fun onNewIntent(intent: Intent?) {

  }

  // Headless Methods

  @ReactMethod
  fun initializeHeadless(token: String) {
    KenkouSDKHeadless.initialize(
      object : AuthTokenProvider() {
        override fun onTokenRequest(listener: (String) -> Unit) {
          listener.invoke(token)
        }
      },
      reactApplicationContext.applicationContext
    )
  }

  @RequiresApi(Build.VERSION_CODES.O)
  @ReactMethod
  fun saveOnboardingQuestionnaireAnswers(answers: ReadableMap, promise: Promise) {
    try {
      runBlocking {
        KenkouSDKHeadless.saveOnboardingQuestionnaireAnswers(KenkouUtils.getReadableAnswers(answers))
        promise.resolve(true)
      }
    } catch (e: Exception) {
      promise.reject(e)
    }
  }

  private val realtimeDataCallback: (RealtimeData) -> Unit = { liveData ->
    Log.v("KenkouExample", "LiveData: $liveData")
  }

  @ReactMethod
  fun startMeasurement(params: ReadableMap, promise: Promise) {
    try {
      UiThreadUtil.runOnUiThread {
        val view = CameraView(reactApplicationContext)
        val density = reactApplicationContext.resources.displayMetrics.density
        val layoutParams = FrameLayout.LayoutParams(
          ((if (params.hasKey("width")) params.getInt("width") else 80) * density).toInt(),
          ((if (params.hasKey("height")) params.getInt("height") else 80) * density).toInt(),
        )
        layoutParams.setMargins(
          ((if (params.hasKey("left")) params.getInt("left") else 0) * density).toInt(),
          ((if (params.hasKey("top")) params.getInt("top") else 0) * density).toInt(),
          0,
          0)
        currentActivity!!.findViewById<FrameLayout>(android.R.id.content).addView(view, layoutParams)

        KenkouSDKHeadless.startMeasurement(
          currentActivity as AppCompatActivity,
          realtimeDataCallback,
          view
        )
        promise.resolve(true)
      }
    } catch (e: OnboardingQuestionnaireException) {
      promise.reject(e)
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  @ReactMethod
  fun stopMeasurement(promise: Promise) {
    try {
      promise.resolve(KenkouSDKHeadless.stopMeasurement()
        ?.let { KenkouUtils.getWritableMeasurement(it) })
    } catch (e: MeasurementFinishedException) {
      promise.reject(e)
    } catch (e: UninitializedPropertyAccessException) {
      promise.reject(IllegalStateException("Call startMeasurement first"))
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  @ReactMethod
  fun savePostMeasurementQuestionnaireAnswers(measurement: ReadableMap, answers: ReadableMap, promise: Promise) {
    try {
      runBlocking {
        val newMeasurement = KenkouSDKHeadless.savePostMeasurementQuestionnaireAnswers(
          KenkouUtils.getReadableMeasurement(measurement),
          KenkouUtils.getReadablePostAnswers(answers)
        )
        promise.resolve(KenkouUtils.getWritableMeasurement(newMeasurement))
      }
    } catch (e: Exception) {
      promise.reject(e)
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  @ReactMethod
  fun getHistory(promise: Promise) {
    try {
      runBlocking {
        promise.resolve(KenkouUtils.getWritableHistory(KenkouSDKHeadless.getHistory()))
      }
    } catch (e: Exception) {
      promise.reject(e)
    }
  }
}
