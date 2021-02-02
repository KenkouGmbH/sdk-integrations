#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(KenkouSDK, "KenkouSDK",
           CAP_PLUGIN_METHOD(echo, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(startMeasurement, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(clearUserData, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(startMeasurementOnboarding, CAPPluginReturnPromise);
)
