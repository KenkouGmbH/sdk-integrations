import Foundation

@objc(ModelWithEmitter)
open class ModelWithEmitter: RCTEventEmitter  {
    
    public static var emitter: RCTEventEmitter!
    
    override init() {
        super.init()
        ModelWithEmitter.emitter = self
    }
    
    /// Base overide for RCTEventEmitter.
    @objc open override func supportedEvents() -> [String] {
        return ["heartBeat"];
        // return EventEmitter.sharedInstance.allEvents;
    }
    
    @objc static func heartBeat(body: Any) {
        ModelWithEmitter.emitter.sendEvent(withName: "heartBeat", body: body)
    }

}
