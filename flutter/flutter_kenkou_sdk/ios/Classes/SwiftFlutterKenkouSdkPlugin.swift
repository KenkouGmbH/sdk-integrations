import Flutter
import UIKit
import KenkouSDK

public class SwiftFlutterKenkouSdkPlugin: NSObject, FlutterPlugin, KenkouMeasurementSDKDelegate {

  public func kenkouMeasurementSDK(_ sdk: KenkouMeasurementSDKInterface, didFinishMeasurement measurement: KenkouMeasurement) {
    print(measurement)
  }

  var measurementSDK: KenkouMeasurementSDKInterface?

  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutter_kenkou_sdk", binaryMessenger: registrar.messenger())
    let instance = SwiftFlutterKenkouSdkPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch (call.method) {
    case "getPlatformVersion":
      result("iOS " + UIDevice.current.systemVersion)
      break;
    case "startMeasurement":
      DispatchQueue.main.async {
        self.measurementSDK = KenkouMeasurementSDK(tokenBlock: nil)
        self.measurementSDK?.delegate = self

        let viewController = UIApplication.shared.windows.first!.rootViewController!

        self.measurementSDK?.presentMeasurement(from: viewController,
                                                withOnboarding: true,
                                                fakeSignal: true,
                                                withResults: true)
      }
      result("iOS " + UIDevice.current.systemVersion)
      break;
    case "cleanUserData":
      result("iOS " + UIDevice.current.systemVersion)
      break;
    case "startMeasurementOnboarding":
      DispatchQueue.main.async {
        self.measurementSDK = KenkouMeasurementSDK(tokenBlock: nil)
        self.measurementSDK?.delegate = self

        let viewController = UIApplication.shared.windows.first!.rootViewController!

        self.measurementSDK?.presentMeasurementOnboarding(from: viewController)
      }
      result("iOS " + UIDevice.current.systemVersion)
      break;      
    default:
      result("Not Implemented!")
    }
  }
}
