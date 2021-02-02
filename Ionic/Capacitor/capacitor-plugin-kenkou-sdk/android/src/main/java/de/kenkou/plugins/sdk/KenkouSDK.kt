package de.kenkou.plugins.sdk

import com.getcapacitor.*

@NativePlugin
class KenkouSDK : Plugin() {
    @PluginMethod
    fun echo(call: PluginCall) {
        val value = call.getString("value")
        val ret = JSObject()
        ret.put("value", value)
        call.success(ret)
    }
}