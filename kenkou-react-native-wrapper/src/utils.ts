import type { ViewProps } from 'react-native';

type Map = {
  [key: string]: any;
};

export enum Sex {
  MALE = 10,
  FEMALE = 20,
  OTHER = 30,
}

export interface OnboardingQuestionnaireAnswers {
  birthYear: number;
  heightCm: number;
  sex: Sex;
  timestamp: number;
  weightKg: number;
}

export interface Statistics {
  avg: number;
  count: number;
  max: number;
  med: number;
  min: number;
  std: number;
}

export interface HRData {
  durationMsec: number;
  hrStatsBpm: Statistics;
  nnStatsMsec: Statistics;
  rmssdMsec: number;
  rrIntervals: Array<number>;
  rrIsNn: Array<number>;
  samplingRateHz: number;
  startMsec: number;
  stressIndexSqHz: number;
}

export enum Eval {
  POOR = 0,
  OKAY = 1,
  GOOD = 2,
  EXCELLENT = 3,
}

export interface HrvIndices {
  hrAvgBpm: number;
  hrAvgEval: Eval;
  rmssdEval: Eval;
  rmssdMsec: number;
  rmssdNorm: number;
  stressIndexEval: Eval;
  stressIndexNorm: number;
  stressIndexSqHz: number;
}

export enum Intake {
  NEVER = 1,
  NO = 2,
  YES = 3,
}

export enum Mood {
  BAD = 10,
  OKAY = 20,
  GOOD = 30,
  HAPPY = 40,
}

export enum PhysicalFeeling {
  SICK = 1,
  TENSE = 2,
  TIRED = 3,
  GREAT = 50,
}

export interface PostMeasurementQuestionnaireAnswers {
  caffeineIntake: Intake;
  mood: Mood;
  nicotineIntake: Intake;
  physicalFeeling: PhysicalFeeling;
  physicalNote: string;
  timestamp: number;
}

export interface Measurement {
  hr: HRData;
  hrv: HrvIndices;
  id: string;
  pmqa: PostMeasurementQuestionnaireAnswers;
}

export interface HistoryItem {
  endedAt: number;
  hrv: HrvIndices;
  pmqa: PostMeasurementQuestionnaireAnswers;
  startedAt: number;
}

export interface StartMeasurementParams {
  width?: number;
  height?: number;
  left?: number;
  top?: number;
}

export enum MeasurementError {
  NO_ERROR = 0,
  TIMEOUT = 1,
}

export interface RealtimeData extends Map {
  bpm: number;
  drawData: Array<number>;
  error: MeasurementError;
  peaks: Array<number>;
  progress: number;
  signalQuality: number;
}

export interface CameraViewProps extends ViewProps {
  onError(error: any): void;
  onMeasure(data: RealtimeData): void;
  statusBarTranslucent?: boolean;
}
