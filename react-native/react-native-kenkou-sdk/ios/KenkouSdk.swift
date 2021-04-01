import Foundation
import KenkouSDK

@objc(KenkouSdk)
class KenkouSdk: NSObject, KenkouMeasurementSDKDelegate {

    public func kenkouMeasurementSDK(_ sdk: KenkouMeasurementSDKInterface, didFinishMeasurement measurement: KenkouMeasurement) {
        print(measurement)
    }

    var measurementSDK: KenkouMeasurementSDKInterface?

    @objc(multiply:withB:withResolver:withRejecter:)
    func multiply(a: Float, b: Float, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        resolve(a*b)
    }

    @objc(startMeasurement:withResolver:withRejecter:)
    func startMeasurement(a: Float,resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        DispatchQueue.main.async {

            self.measurementSDK = KenkouMeasurementSDK(tokenBlock: nil)
            self.measurementSDK?.delegate = self

            let viewController = UIApplication.shared.windows.first!.rootViewController!

            self.measurementSDK?.presentMeasurement(from: viewController,
                                                    withOnboarding: true,
                                                    fakeSignal: true,
                                                    withResults: true)
        }
        resolve(1)
    }

    @objc(clearUserData:withResolver:withRejecter:)
    func clearUserData(a: Float,resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        resolve(1)
    }

    @objc(startMeasurementOnboarding:withResolver:withRejecter:)
    func startMeasurementOnboarding(a: Float,resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        DispatchQueue.main.async {

            self.measurementSDK = KenkouMeasurementSDK(tokenBlock: nil)
            self.measurementSDK?.delegate = self

            let viewController = UIApplication.shared.windows.first!.rootViewController!

            self.measurementSDK?.presentMeasurementOnboarding(from: viewController)
        }
        resolve(1)
    }
}
