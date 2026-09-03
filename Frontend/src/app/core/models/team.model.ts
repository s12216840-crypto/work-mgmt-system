export interface Team {
  id: number;
  name: string;
  description: string | null;
  organizationId: number;
  userIds: number[];
}

export interface TeamRequest {
  name: string;
  description: string;
  organizationId: number;
}
