import React from 'react';
import {
  findNodeHandle,
  NativeModules,
  Platform,
  requireNativeComponent,
  UIManager,
} from 'react-native';
import type {
  CameraViewProps,
  HistoryItem,
  Measurement,
  OnboardingQuestionnaireAnswers,
  PostMeasurementQuestionnaireAnswers,
  StartMeasurementParams,
} from './utils';

const LINKING_ERROR =
  `The package 'kenkou-react-native-wrapper' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const Kenkou = NativeModules.RNKenkou
  ? NativeModules.RNKenkou
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

// Visual Methods

export function initialize(token: string) {
  return Kenkou.initialize(token);
}

export function presentMeasurementInstructions(): boolean {
  return Kenkou.presentMeasurementInstructions();
}

export function presentOnboardingQuestionnaire(): OnboardingQuestionnaireAnswers {
  return Kenkou.presentOnboardingQuestionnaire();
}

export function presentMeasurement(): Measurement {
  return Kenkou.presentMeasurement();
}

export function presentPostMeasurementQuestionnaire(measurement: Measurement) {
  return Kenkou.presentPostMeasurementQuestionnaire(measurement);
}

export function presentMeasurementResults(measurement: Measurement) {
  return Kenkou.presentMeasurementResults(measurement);
}

export function presentHistoryItem(historyItem: HistoryItem) {
  return Kenkou.presentHistoryItem(historyItem);
}

export function presentHistory(history: Array<HistoryItem>) {
  return Kenkou.presentHistory(history);
}

// Headless Methods

export function initializeHeadless(token: string) {
  return Kenkou.initializeHeadless(token);
}

export function saveOnboardingQuestionnaireAnswers(
  answers: OnboardingQuestionnaireAnswers
) {
  return Kenkou.saveOnboardingQuestionnaireAnswers(answers);
}

export function startMeasurement(params: StartMeasurementParams) {
  return Kenkou.startMeasurement(params);
}

export function stopMeasurement(): Measurement {
  return Kenkou.stopMeasurement();
}

export function savePostMeasurementQuestionnaireAnswers(
  measurement: Measurement,
  answers: PostMeasurementQuestionnaireAnswers
): Measurement {
  return Kenkou.savePostMeasurementQuestionnaireAnswers(measurement, answers);
}

export function getHistory() {
  return Kenkou.getHistory();
}

// Native UI Component

const ComponentName = 'RNKenkouCameraView';

const RCTCameraView =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<any>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };

export const CameraView = React.forwardRef(
  ({ onError, onMeasure, ...props }: CameraViewProps, ref) => {
    const cameraRef = React.useRef();
    React.useImperativeHandle(ref, () => ({
      startMeasurement: () => {
        UIManager.dispatchViewManagerCommand(
          findNodeHandle(cameraRef.current as any),
          UIManager.getViewManagerConfig(ComponentName).Commands
            .startMeasurement,
          []
        );
      },
    }));
    return (
      <RCTCameraView
        ref={cameraRef}
        onError={(event: any) => onError(event.nativeEvent)}
        onMeasure={(event: any) => onMeasure(event.nativeEvent)}
        {...props}
      />
    );
  }
);
