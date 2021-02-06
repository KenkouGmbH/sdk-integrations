import { NativeModules } from 'react-native';

type KenkouSdkType = {
  multiply(a: number, b: number): Promise<number>;
};

const { KenkouSdk } = NativeModules;

export default KenkouSdk as KenkouSdkType;
