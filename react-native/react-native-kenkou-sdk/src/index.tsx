import { NativeModules } from 'react-native';

type KenkouSdkType = {
  multiply(a: number, b: number): Promise<number>;
  startMeasurement(a: number): Promise<number>;
  clearUserData(a: number): Promise<number>;
  startMeasurementOnboarding(a: number): Promise<number>;
  startHeadlessMeasurement(a: number): Promise<number>;
};

const { KenkouSdk } = NativeModules;

export default KenkouSdk as KenkouSdkType;
