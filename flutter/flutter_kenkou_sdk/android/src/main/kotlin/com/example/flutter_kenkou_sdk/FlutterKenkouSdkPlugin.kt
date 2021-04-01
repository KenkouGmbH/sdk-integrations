package com.example.flutter_kenkou_sdk

import android.app.Activity
import android.content.Context
import androidx.annotation.NonNull
import de.kenkou.measurement_sdk.KenkouSdk
import de.kenkou.measurement_sdk.service.AuthTokenProvider

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

/** FlutterKenkouSdkPlugin */
class FlutterKenkouSdkPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var context: Context
  private lateinit var activity: Activity

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_kenkou_sdk")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
    KenkouSdk.initialize(object : AuthTokenProvider() {
      override fun onTokenRequest(listener: (String) -> Unit) {
        listener.invoke("<your token>")
      }
    }, context)
  }

  override fun onDetachedFromActivity() {
    TODO("Not yet implemented")
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    TODO("Not yet implemented")
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity;
  }

  override fun onDetachedFromActivityForConfigChanges() {
    TODO("Not yet implemented")
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else if (call.method == "startMeasurement") {
      KenkouSdk.startMeasurement(activity)
      result.success("startMeasurement")
    } else if (call.method == "clearUserData") {
      KenkouSdk.clearUserData(activity)
      result.success("clearUserData")
    } else if (call.method == "startMeasurementOnboarding") {
      KenkouSdk.startMeasurementOnboarding(activity)
      result.success("startMeasurementOnboarding")
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
