#import <React/RCTViewManager.h>

@interface RCT_EXTERN_MODULE(RNKenkouCameraViewManager, RCTViewManager)

RCT_EXTERN_METHOD(startMeasurement:)

RCT_EXPORT_VIEW_PROPERTY(onMeasure, RCTDirectEventBlock)

@end
