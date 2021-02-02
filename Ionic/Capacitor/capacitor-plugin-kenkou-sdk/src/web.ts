import { WebPlugin } from '@capacitor/core';
import { KenkouSDKPlugin } from './definitions';

export class KenkouSDKWeb extends WebPlugin implements KenkouSDKPlugin {
  constructor() {
    super({
      name: 'KenkouSDK',
      platforms: ['web'],
    });
  }

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async startMeasurement(): Promise<any> {
    return;
  }

  async clearUserData(): Promise<any> {
    return;
  }

  async startMeasurementOnboarding(): Promise<any> {
    return;
  }
}

const KenkouSDK = new KenkouSDKWeb();

export { KenkouSDK };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(KenkouSDK);
