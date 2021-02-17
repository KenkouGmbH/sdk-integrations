import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_kenkou_sdk/flutter_kenkou_sdk.dart';

void main() {
  const MethodChannel channel = MethodChannel('flutter_kenkou_sdk');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await FlutterKenkouSdk.platformVersion, '42');
  });
}
