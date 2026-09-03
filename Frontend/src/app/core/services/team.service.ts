import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Team, TeamRequest } from '../models/team.model';

@Injectable({
  providedIn: 'root',
})
export class TeamService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl = '/api/teams';

  getTeams(): Observable<Team[]> {
    return this.http.get<Team[]>(this.apiUrl);
  }

  getTeam(id: number): Observable<Team> {
    return this.http.get<Team>(`${this.apiUrl}/${id}`);
  }

  createTeam(request: TeamRequest): Observable<Team> {
    return this.http.post<Team>(this.apiUrl, request);
  }

  updateTeam(id: number, request: TeamRequest): Observable<Team> {
    return this.http.put<Team>(`${this.apiUrl}/${id}`, request);
  }

  deleteTeam(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  addUserToTeam(teamId: number, userId: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${teamId}/users/${userId}`, {});
  }

  removeUserFromTeam(teamId: number, userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${teamId}/users/${userId}`);
  }
}
