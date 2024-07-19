import dayjs from 'dayjs/esm';

import { ISpaceEvent, NewSpaceEvent } from './space-event.model';

export const sampleWithRequiredData: ISpaceEvent = {
  id: 11485,
  name: 'in around continuity',
  date: dayjs('2024-07-11'),
  description: '../fake-data/blob/hipster.txt',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  type: 'LAUNCH',
};

export const sampleWithPartialData: ISpaceEvent = {
  id: 31432,
  name: 'bloom immediately',
  date: dayjs('2024-07-12'),
  description: '../fake-data/blob/hipster.txt',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  type: 'LAUNCH',
};

export const sampleWithFullData: ISpaceEvent = {
  id: 14135,
  name: 'correspondence than',
  date: dayjs('2024-07-11'),
  description: '../fake-data/blob/hipster.txt',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  type: 'LAUNCH',
};

export const sampleWithNewData: NewSpaceEvent = {
  name: 'illustrious starchy whereas',
  date: dayjs('2024-07-12'),
  description: '../fake-data/blob/hipster.txt',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  type: 'LANDING',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
