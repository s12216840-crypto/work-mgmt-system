export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'IN_REVIEW' | 'DONE' | 'CANCELLED';

export type TaskPriority = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';

export interface Task {
  id: number;
  title: string;
  description: string | null;
  projectId: number;
  assigneeId: number | null;
  reporterId: number;
  status: TaskStatus;
  priority: TaskPriority;
  dueDate: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface TaskRequest {
  title: string;
  description: string;
  projectId: number;
  assigneeId: number | null;
  reporterId: number;
  status: TaskStatus;
  priority: TaskPriority;
  dueDate: string | null;
}
