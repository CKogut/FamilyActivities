export interface IActivity {
  id?: number;
  description?: string | null;
  cost?: string | null;
  minParticipants?: number | null;
  maxParticipants?: number | null;
  time?: string | null;
  location?: string | null;
}

export const defaultValue: Readonly<IActivity> = {};
