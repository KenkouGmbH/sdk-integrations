package de.kenkou

import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Choreographer
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ReactStylesDiffMap
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter
import de.kenkou.sdk.headless.KenkouSDKHeadless
import de.kenkou.sdk.headless.core.camera.CameraView
import de.kenkou.sdk.headless.domain.model.measurement.RealtimeData

class KenkouRNCameraViewManager : SimpleViewManager<CameraView>() {

  companion object {
    const val START_MEASUREMENT_COMMAND_NAME = "startMeasurement"
    const val START_MEASUREMENT_COMMAND = 1
    var statusBarTranslucent = false
  }

  override fun getName(): String {
    return "RNKenkouCameraView"
  }

  override fun createViewInstance(reactContext: ThemedReactContext): CameraView {
    return CameraView(reactContext)
  }

  override fun updateProperties(viewToUpdate: CameraView, props: ReactStylesDiffMap?) {
    super.updateProperties(viewToUpdate, props)
    Handler(Looper.getMainLooper()).postDelayed({ this.setupLayoutHack(viewToUpdate) },100)
  }

  @ReactProp(name = "statusBarTranslucent")
  fun setStatusBarTranslucent(view: CameraView, translucent: Boolean) {
    statusBarTranslucent = translucent
  }

  override fun getCommandsMap(): MutableMap<String, Int> {
    return MapBuilder.of(START_MEASUREMENT_COMMAND_NAME, START_MEASUREMENT_COMMAND)
  }

  @RequiresApi(Build.VERSION_CODES.O)
  override fun receiveCommand(root: CameraView, commandId: Int, args: ReadableArray?) {
    when (commandId) {
      START_MEASUREMENT_COMMAND -> {
        startHeadlessMeasurement(root)
      }
    }
  }

  override fun getExportedCustomBubblingEventTypeConstants(): MutableMap<String, Any> {
    return MapBuilder.builder<String, Any>()
      .put(
        "realtimeData", MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onMeasure")
        )
      ).build()
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun emitRealtimeEvent(view: CameraView, data: RealtimeData) {
    (view.context as ThemedReactContext).getJSModule(RCTEventEmitter::class.java)
      .receiveEvent(view.getId(), "realtimeData", KenkouUtils.getWritableRealtimeData(data))
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun startHeadlessMeasurement(view: CameraView) {
    val realtimeDataCallback: (RealtimeData) -> Unit = { liveData ->
      emitRealtimeEvent(view, liveData)
    }
    KenkouSDKHeadless.startMeasurement(
      (view.context as ThemedReactContext).currentActivity as AppCompatActivity,
      realtimeDataCallback = realtimeDataCallback,
      preview = view
    )
  }

  private fun getStatusBarHeight(activity: AppCompatActivity): Int {
    val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) activity.resources.getDimensionPixelSize(resourceId)
    else Rect().apply { activity.window.decorView.getWindowVisibleDisplayFrame(this) }.top
  }

  private fun setupLayoutHack(view: CameraView) {
    if (view.width == 0 || view.height == 0) return
    val currentActivity = (view.context as ThemedReactContext).currentActivity as AppCompatActivity
    val offset = if (statusBarTranslucent) 0 else getStatusBarHeight(currentActivity)
    val rect = Rect()
    view.getGlobalVisibleRect(rect)
    Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {
      override fun doFrame(frameTimeNanos: Long) {
        view.measure(
          View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
          View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY)
        )
        view.layout(rect.left, rect.top - offset, rect.right, rect.bottom - offset)

        view.viewTreeObserver.dispatchOnGlobalLayout()
        Choreographer.getInstance().postFrameCallback(this)
      }
    })
  }
}
