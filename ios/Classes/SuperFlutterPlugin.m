#import "SuperFlutterPlugin.h"
#import <super_flutter/super_flutter-Swift.h>

@implementation SuperFlutterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftSuperFlutterPlugin registerWithRegistrar:registrar];
}
@end
