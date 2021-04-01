
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterKenkouSdk {
  static const MethodChannel _channel =
      const MethodChannel('flutter_kenkou_sdk');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> get startMeasurement async {
    final String version = await _channel.invokeMethod('startMeasurement');
    return version;
  }

  static Future<String> get clearUserData async {
    final String version = await _channel.invokeMethod('clearUserData');
    return version;
  }

  static Future<String> get startMeasurementOnboarding async {
    final String version = await _channel.invokeMethod('startMeasurementOnboarding');
    return version;
  }
}
