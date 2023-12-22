import { IUser } from 'app/shared/model/user.model';
import { IActivity } from 'app/shared/model/activity.model';

export interface IRelationship {
  id?: number;
  liked?: boolean | null;
  shared?: boolean | null;
  user?: IUser | null;
  description?: IActivity | null;
}

export const defaultValue: Readonly<IRelationship> = {
  liked: false,
  shared: false,
};
