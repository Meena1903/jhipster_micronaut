import { IMission, NewMission } from './mission.model';

export const sampleWithRequiredData: IMission = {
  id: 1763,
  name: 'ugh spirited',
};

export const sampleWithPartialData: IMission = {
  id: 6310,
  name: 'materialize upliftingly unwelcome',
};

export const sampleWithFullData: IMission = {
  id: 4609,
  name: 'gaseous importance',
  description: 'when shock',
};

export const sampleWithNewData: NewMission = {
  name: 'unnecessarily',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
