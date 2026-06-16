import { request } from '../utils/request';
import type { Announcement } from '../types/announcement';

export const listAnnouncements = () => request.get<never, Announcement[]>('/announcements');
export const publishAnnouncement = (data: Pick<Announcement, 'title' | 'content' | 'category' | 'top'>) =>
  request.post<never, Announcement>('/announcements', data);
export const markAnnouncementRead = (id: number) => request.post<never, Announcement>(`/announcements/${id}/read`, {});
