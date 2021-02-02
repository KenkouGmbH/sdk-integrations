import Foundation
import Capacitor
import KenkouSDK
/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(KenkouSDK)
public class KenkouSDK: CAPPlugin, KenkouMeasurementSDKDelegate {

    public func kenkouMeasurementSDK(_ sdk: KenkouMeasurementSDKInterface, didFinishMeasurement measurement: KenkouMeasurement) {
        print(measurement)
    }

    var measurementSDK: KenkouMeasurementSDKInterface?

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.success([
            "value": value
        ])
    }

    @objc func startMeasurement(_ call: CAPPluginCall) {

        DispatchQueue.main.async { [self] in

            self.measurementSDK = KenkouMeasurementSDK(tokenBlock: nil)
            self.measurementSDK?.delegate = self

            let viewController = UIApplication.shared.windows.first!.rootViewController!

            self.measurementSDK?.presentMeasurement(from: viewController,
                                                     withOnboarding: true,
                                                     fakeSignal: true,
                                                     withResults: true)
        }

        call.resolve()
    }

    @objc func clearUserData(_ call: CAPPluginCall) {

        call.resolve()
    }

    @objc func startMeasurementOnboarding(_ call: CAPPluginCall) {

        DispatchQueue.main.async { [self] in

            self.measurementSDK = KenkouMeasurementSDK(tokenBlock: nil)
            self.measurementSDK?.delegate = self

            let viewController = UIApplication.shared.windows.first!.rootViewController!

            self.measurementSDK?.presentMeasurementOnboarding(from: viewController)
        }

        call.resolve()
    }
}
