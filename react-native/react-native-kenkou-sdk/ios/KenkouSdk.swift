import Foundation
import KenkouSDK

@objc(KenkouSdk)
class KenkouSdk: NSObject, KenkouMeasurementSDKDelegate {

    public func kenkouMeasurementSDK(_ sdk: KenkouMeasurementSDKInterface, didFinishMeasurement measurement: KenkouMeasurement) {
        print(measurement)
    }

    var measurementSDK: KenkouMeasurementSDKInterface?
    var measurementHeadlessSDK: KenkouMeasurementSDKHeadlessInterface?

    // self.measurementSDK = KenkouMeasurementSDK(tokenBlock: nil)
    // self.measurementHeadlessSDK = KenkouMeasurementSDKHeadless(tokenBlock: nil)

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

    @objc(startHeadlessMeasurement:withResolver:withRejecter:)
    func startHeadlessMeasurement(a: Float,resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        DispatchQueue.main.async {
            
            self.measurementHeadlessSDK = KenkouMeasurementSDKHeadless(tokenBlock: nil)
            
            print("startHeadlessMeasurement =====>")
            
            self.measurementHeadlessSDK?.startMeasurement(isBaseline: true, cameraPreviewLayer: nil, syntheticPeakTemplate: nil, startedCallback: {
                
            }, ppgDataCallback: nil, syntheticPeaksCallback: nil, signalQualityCallback: nil, fingerOnCameraCallback: {(value: Bool) in
                print("fingerOnCameraCallback =====>", value)
            }, fingerIsStableCallback: nil, heartBeatCallback: { (value: Int) in
                print("heartBeatCallback =====>", value)
                ModelWithEmitter.heartBeat(body: value)
            }, hrLocalMinCallback: nil, hrLocalMaxCallback: nil, statisticsCallback: nil, stoppedCallback: {
                
            })
        }
        resolve(1)
    }
}
