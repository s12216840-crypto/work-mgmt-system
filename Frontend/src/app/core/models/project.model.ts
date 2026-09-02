export type ProjectStatus = 'PLANNED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';

export interface Project {
  id: number;
  name: string;
  description: string | null;
  organizationId: number;
  ownerId: number;
  status: ProjectStatus;
  startDate: string | null;
  endDate: string | null;
  memberIds: number[];
}

export interface ProjectRequest {
  name: string;
  description: string;
  organizationId: number;
  ownerId: number;
  status: ProjectStatus;
  startDate: string | null;
  endDate: string | null;
}
