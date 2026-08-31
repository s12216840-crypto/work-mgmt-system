import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { OrganizationRequest, OrganizationResponse } from '../models/organization.model';

@Injectable({
  providedIn: 'root',
})
export class OrganizationService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl = '/api/organizations';

  getOrganizations(): Observable<OrganizationResponse[]> {
    return this.http.get<OrganizationResponse[]>(this.apiUrl);
  }

  getOrganizationById(id: number): Observable<OrganizationResponse> {
    return this.http.get<OrganizationResponse>(`${this.apiUrl}/${id}`);
  }

  createOrganization(request: OrganizationRequest): Observable<OrganizationResponse> {
    return this.http.post<OrganizationResponse>(this.apiUrl, request);
  }

  updateOrganization(id: number, request: OrganizationRequest): Observable<OrganizationResponse> {
    return this.http.put<OrganizationResponse>(`${this.apiUrl}/${id}`, request);
  }

  deleteOrganization(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
