#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(ModelWithEmitter, RCTEventEmitter)

RCT_EXTERN_METHOD(supportedEvents)

@end
