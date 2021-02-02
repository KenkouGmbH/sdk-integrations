import { Component } from '@angular/core';

import {Plugins} from '@capacitor/core';
import 'capacitor-plugin-kenkou-sdk';

const {KenkouSDK} = Plugins;

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {

  constructor() {}

  startMeasurement() {
    KenkouSDK.startMeasurement()
  }

  clearUserData() {
    KenkouSDK.clearUserData();
  }

  startMeasurementOnboarding() {
    KenkouSDK.startMeasurementOnboarding()
  }

}
