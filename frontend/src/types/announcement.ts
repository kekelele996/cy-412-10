import type { User } from './user';

export type AnnouncementCategory = 'notice' | 'event' | 'urgent';

export interface Announcement {
  id: number;
  title: string;
  content: string;
  category: AnnouncementCategory;
  publisherId: number;
  publishAt: string;
  top: boolean;
  readCount: number;
  publisher?: User;
}
