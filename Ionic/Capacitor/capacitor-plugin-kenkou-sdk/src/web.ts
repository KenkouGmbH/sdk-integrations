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
}

const KenkouSDK = new KenkouSDKWeb();

export { KenkouSDK };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(KenkouSDK);
