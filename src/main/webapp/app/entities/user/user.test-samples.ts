import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 'bbd938c4-e021-4a1a-8c1c-b39b1e86ac68',
  login: 'g8eEI@82cMY\\57\\:h7M\\Bf\\kV93y\\AMjd4J',
};

export const sampleWithPartialData: IUser = {
  id: '0c7ca649-62f5-4b01-9e6a-1ad6c7507727',
  login: 'Ng',
};

export const sampleWithFullData: IUser = {
  id: '98146557-b0ab-45b3-a8a6-8fd0e410d740',
  login: '73M',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
