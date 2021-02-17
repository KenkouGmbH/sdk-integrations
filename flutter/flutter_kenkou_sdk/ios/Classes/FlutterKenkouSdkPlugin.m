#import "FlutterKenkouSdkPlugin.h"
#if __has_include(<flutter_kenkou_sdk/flutter_kenkou_sdk-Swift.h>)
#import <flutter_kenkou_sdk/flutter_kenkou_sdk-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_kenkou_sdk-Swift.h"
#endif

@implementation FlutterKenkouSdkPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterKenkouSdkPlugin registerWithRegistrar:registrar];
}
@end
