import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Task, TaskRequest, TaskPriority, TaskStatus } from '../models/task.model';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/tasks';

  getTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.apiUrl);
  }

  getTask(id: number): Observable<Task> {
    return this.http.get<Task>(`${this.apiUrl}/${id}`);
  }

  createTask(request: TaskRequest): Observable<Task> {
    return this.http.post<Task>(this.apiUrl, request);
  }

  updateTask(id: number, request: TaskRequest): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${id}`, request);
  }

  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  assignTask(taskId: number, userId: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${taskId}/assignee/${userId}`, {});
  }

  changeStatus(taskId: number, status: TaskStatus): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${taskId}/status/${status}`, {});
  }

  changePriority(taskId: number, priority: TaskPriority): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${taskId}/priority/${priority}`, {});
  }
}
