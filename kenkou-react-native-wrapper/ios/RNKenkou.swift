import KenkouSDK
import KenkouSDKUIComponents
import KenkouCoreMeasurement

@objc(RNKenkou)
class RNKenkou: NSObject {
    
    private var measurementSDK: KenkouSDKVisualInterface!
    static var headlessSDK: KenkouSDKHeadless!
    
    private var resolve: RCTPromiseResolveBlock!
    private var reject: RCTPromiseRejectBlock!

    @objc(initialize:)
    func initialize(token: String) -> Void {
        measurementSDK = KenkouSDKVisual(tokenBlock: { () -> String in return token })
        measurementSDK.delegate = self
    }
    
    @objc(presentMeasurementInstructions:withRejecter:)
    func presentMeasurementInstructions(resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void {
        self.resolve = resolve
        self.reject = reject
        DispatchQueue.main.async {
            let viewController = UIApplication.shared.windows.first!.rootViewController!
            self.measurementSDK.presentMeasurementInstructions(from: viewController)
        }
    }
    
    @objc(presentOnboardingQuestionnaire:withRejecter:)
    func presentOnboardingQuestionnaire(resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void {
        self.resolve = resolve
        self.reject = reject
        DispatchQueue.main.async {
            let viewController = UIApplication.shared.windows.first!.rootViewController!
            self.measurementSDK.presentOnboardingQuestionnaire(from: viewController)
        }
    }
    
    @objc(presentMeasurement:withRejecter:)
    func presentMeasurement(resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void {
        self.resolve = resolve
        self.reject = reject
        DispatchQueue.main.async {
            self.measurementSDK.presentMeasurement(from: self.getViewController())
        }
    }
    
    @objc(presentPostMeasurementQuestionnaire:withResolver:withRejecter:)
    func presentPostMeasurementQuestionnaire(data: NSDictionary, resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void {
        self.resolve = resolve
        self.reject = reject
        DispatchQueue.main.async {
            KenkouUtils.decodeData(data: data, returningClass: KenkouCoreMeasurement.Measurement.self) { measurement in
                self.measurementSDK.presentPostMeasurementQuestionnaire(from: self.getViewController(), measurement: measurement)
            } failure: { error in
                reject("presentPostMeasurementQuestionnaire", error.localizedDescription, error)
            }
        }
    }
    
    @objc(presentMeasurementResults:withResolver:withRejecter:)
    func presentMeasurementResults(data: NSDictionary, resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void {
        self.resolve = resolve
        self.reject = reject
        DispatchQueue.main.async {
            KenkouUtils.decodeData(data: data, returningClass: KenkouCoreMeasurement.Measurement.self) { measurement in
                do {
                    try self.measurementSDK.presentMeasurementResults(from: self.getViewController(), measurement: measurement)
                } catch let error {
                    reject("presentMeasurementResults", error.localizedDescription, error)
                }
            } failure: { error in
                reject("presentMeasurementResults", error.localizedDescription, error)
            }
        }
    }
    
    @objc(presentHistoryItem:withResolver:withRejecter:)
    func presentHistoryItem(data: NSDictionary, resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void {
        self.resolve = resolve
        self.reject = reject
        DispatchQueue.main.async {
            KenkouUtils.decodeData(data: data, returningClass: HistoryItem.self) { history in
                do {
                    try self.measurementSDK.presentMeasurementResults(from: self.getViewController(), history: history)
                } catch let error {
                    reject("presentHistoryItem", error.localizedDescription, error)
                }
            } failure: { error in
                reject("presentHistoryItem", error.localizedDescription, error)
            }
        }
    }
    
    @objc(presentHistory:withResolver:withRejecter:)
    func presentHistory(data: NSArray, resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void {
        self.resolve = resolve
        self.reject = reject
        DispatchQueue.main.async {
            KenkouUtils.decodeData(data: data, returningClass: [HistoryItem].self) { history in
                self.measurementSDK.presentMeasurementHistory(from: self.getViewController(), history: history)
            } failure: { error in
                reject("presentHistory", error.localizedDescription, error)
            }
        }
    }
    
    @objc(initializeHeadless:)
    func initializeHeadless(token: String) -> Void {
        RNKenkou.headlessSDK = KenkouSDKHeadless(tokenBlock: { () -> String in return token }, locale: nil)
    }
    
    @objc(saveOnboardingQuestionnaireAnswers:withResolver:withRejecter:)
    func saveOnboardingQuestionnaireAnswers(data: NSDictionary, resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void {
        KenkouUtils.decodeData(data: data, returningClass: OnboardingQuestionnaireAnswers.self) { answers in
            RNKenkou.headlessSDK.saveOnboardingQuestionnaireAnswers(answers: answers)
            resolve(true)
        } failure: { error in
            reject("saveOnboardingQuestionnaireAnswers", error.localizedDescription, error)
        }
    }
    
    @objc(startMeasurement:withRejecter:)
    func startMeasurement(resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void {
//        do {
//            try headlessSDK.startMeasurement(preview: <#T##AVCaptureVideoPreviewLayer#>, realtimeDataCallback: { data in
//                <#code#>
//            })
//        } catch let error {
//            reject("startMeasurement", error.localizedDescription, error)
//        }
    }
    
    @objc(stopMeasurement:withRejecter:)
    func stopMeasurement(resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void {
        self.resolve = resolve
        self.reject = reject
        handleData(code: "stopMeasurement", data: RNKenkou.headlessSDK.stopMeasurement())
    }
    
    @objc(savePostMeasurementQuestionnaireAnswers:withAnswers:withResolver:withRejecter:)
    func savePostMeasurementQuestionnaireAnswers(measurement: NSDictionary, answers: NSDictionary, resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void {
        KenkouUtils.decodeData(data: measurement, returningClass: KenkouCoreMeasurement.Measurement.self) { measurement in
            KenkouUtils.decodeData(data: answers, returningClass: PostMeasurementQuestionnaireAnswers.self) { answers in
                handleData(code: "savePostMeasurementQuestionnaireAnswers", data: RNKenkou.headlessSDK.savePostMeasurementQuestionnaireAnswers(measurement: measurement, answers: answers))
            } failure: { error in
                reject("savePostMeasurementQuestionnaireAnswers", error.localizedDescription, error)
            }

        } failure: { error in
            reject("savePostMeasurementQuestionnaireAnswers", error.localizedDescription, error)
        }
    }
    
    @objc(getHistory:withRejecter:)
    func getHistory(resolve:@escaping RCTPromiseResolveBlock, reject:@escaping RCTPromiseRejectBlock) -> Void {
        self.resolve = resolve
        self.reject = reject
        handleData(code: "getHistory", data: measurementSDK.getHistory())
    }
}

extension RNKenkou: KenkouSDKVisualDelegate {
    
    func getViewController() -> UIViewController {
        return UIApplication.shared.windows.first!.rootViewController!
    }
    
    func handleData<T: Encodable>(code: String, data: T) -> () {
        KenkouUtils.encodeData(data: data) { data in
            resolve(data)
        } failure: { error in
            reject(code, error.localizedDescription, error)
        }
    }
    
    func kenkouSDK(_ sdk: KenkouSDKVisualInterface, didFinishMeasurement measurement: KenkouCoreMeasurement.Measurement?) {
        measurementSDK.presentPostMeasurementQuestionnaire(from: getViewController(), measurement: measurement!)
    }
    
    func kenkouSDK(_ sdk: KenkouSDKVisualInterface, didFinishPostMeasurementQuestionnaire measurement: KenkouCoreMeasurement.Measurement) {
        do {
            try measurementSDK.presentMeasurementResults(from: getViewController(), measurement: measurement)
        } catch let error {
            reject("presentPostMeasurementQuestionnaire", error.localizedDescription, error)
        }
    }
    
    func kenkouSDK(_ sdk: KenkouSDKVisualInterface, didFinishOnboardingQuestionnaire answers: OnboardingQuestionnaireAnswers) {
        handleData(code: "presentOnboardingQuestionnaire", data: answers)
    }
    
    func kenkouSDK(_ sdk: KenkouSDKVisualInterface, didFinishMeasurementResultsPresentation measurement: HistoryItem) {
        handleData(code: "presentMeasurementResults", data: measurement)
    }
    
    func kenkouSDK(_ sdk: KenkouSDKVisualInterface, didFinishMeasurementInstructionsPresentation isFullyViewed: Bool) {
        resolve(isFullyViewed)
    }
    
    func kenkouSDK(_ sdk: KenkouSDKVisualInterface, didStopMeasurementWithError error: Error) {
        reject("presentMeasurement", error.localizedDescription, error)
    }
    
}
