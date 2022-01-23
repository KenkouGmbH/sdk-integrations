import AVFoundation

@objc(RNKenkouCameraViewManager)
class RNKenkouCameraViewManager: RCTViewManager {

    override func view() -> (RNKenkouCameraView) {
        return RNKenkouCameraView()
    }
    
    @objc(startMeasurement:)
    func startMeasurement(reactTag: NSNumber) -> Void {
        self.bridge.uiManager.addUIBlock { uiManager, viewRegistry in
            if let view = viewRegistry?[reactTag] as? RNKenkouCameraView {
                do {
                    try RNKenkou.headlessSDK.startMeasurement(preview: view.previewLayer, realtimeDataCallback: { data in
                        KenkouUtils.encodeData(data: data) { data in
                            view.onMeasure!(data as? [AnyHashable : Any])
                        } failure: { error in
                            view.onError!(["message": error.localizedDescription])
                        }
                    })
                } catch let error {
                    view.onError!(["message": error.localizedDescription])
                }
            }
        }
    }
}

class RNKenkouCameraView : UIView {
    
    @objc var onError: RCTDirectEventBlock?
    @objc var onMeasure: RCTDirectEventBlock?

    public let previewLayer: AVCaptureVideoPreviewLayer = {
        let previewLayer = AVCaptureVideoPreviewLayer()
        previewLayer.videoGravity = .resizeAspectFill

        return previewLayer
    }()

    func setSession(session: AVCaptureSession) {
        previewLayer.session = session
    }

    override init(frame: CGRect) {
        super.init(frame: frame)

        layer.addSublayer(previewLayer)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    public override func layoutSubviews() {
        super.layoutSubviews()

        previewLayer.frame = bounds
    }
}

