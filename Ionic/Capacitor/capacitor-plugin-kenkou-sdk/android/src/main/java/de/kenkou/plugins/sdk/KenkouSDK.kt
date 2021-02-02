package de.kenkou.plugins.sdk

import com.getcapacitor.*
import de.kenkou.measurement_sdk.KenkouSdk
import de.kenkou.measurement_sdk.service.AuthTokenProvider

@NativePlugin
class KenkouSDK : Plugin() {

    override fun load() {
        super.load()
        KenkouSdk.initialize(object : AuthTokenProvider() {
            override fun onTokenRequest(listener: (String) -> Unit) {
                listener.invoke("<your token>")
            }
        }, activity.applicationContext)
    }

    @PluginMethod
    fun echo(call: PluginCall) {
        val value = call.getString("value")
        val ret = JSObject()
        ret.put("value", value)
        call.success(ret)
    }

    @PluginMethod
    fun startMeasurement(call: PluginCall) {
        KenkouSdk.startMeasurement(activity)
        call.resolve()
    }

    @PluginMethod
    fun clearUserData(call: PluginCall) {
        KenkouSdk.clearUserData(activity)
        call.resolve()
    }

    @PluginMethod
    fun startMeasurementOnboarding(call: PluginCall) {
        KenkouSdk.startMeasurementOnboarding(activity)
        call.resolve()
    }

}