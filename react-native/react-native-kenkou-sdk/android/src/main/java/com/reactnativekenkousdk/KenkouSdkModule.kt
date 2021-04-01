package com.reactnativekenkousdk

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import de.kenkou.measurement_sdk.KenkouSdk
import de.kenkou.measurement_sdk.service.AuthTokenProvider

class KenkouSdkModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

  override fun initialize() {
    super.initialize()
    KenkouSdk.initialize(object : AuthTokenProvider() {
      override fun onTokenRequest(listener: (String) -> Unit) {
        listener.invoke("<your token>")
      }
    }, currentActivity!!.applicationContext)
  }

  override fun getName(): String {
    return "KenkouSdk"
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  fun multiply(a: Int, b: Int, promise: Promise) {

    promise.resolve(a * b)
  }

  @ReactMethod
  fun startMeasurement(a: Int, promise: Promise) {

    currentActivity?.let { KenkouSdk.startMeasurement(it) }
    promise.resolve(1)
  }

  @ReactMethod
  fun clearUserData(a: Int, promise: Promise) {

    currentActivity?.let { KenkouSdk.clearUserData(it) }
    promise.resolve(1)
  }

  @ReactMethod
  fun startMeasurementOnboarding(a: Int, promise: Promise) {

    currentActivity?.let { KenkouSdk.startMeasurementOnboarding(it) }
    promise.resolve(1)
  }

}