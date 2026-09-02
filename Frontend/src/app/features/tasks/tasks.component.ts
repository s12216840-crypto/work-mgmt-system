import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Task, TaskPriority, TaskRequest, TaskStatus } from '../../core/models/task.model';
import { TaskService } from '../../core/services/task.service';

@Component({
  selector: 'app-tasks',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tasks.component.html',
  styleUrl: './tasks.component.css',
})
export class TasksComponent implements OnInit {
  private readonly taskService = inject(TaskService);
  private readonly cdr = inject(ChangeDetectorRef);

  tasks: Task[] = [];
  filteredTasks: Task[] = [];
  selectedTask: Task | null = null;

  loadingTasks = true;
  loading = false;

  errorMessage = '';
  successMessage = '';

  showForm = false;
  editingTask: Task | null = null;

  form: TaskRequest = this.emptyForm();

  statuses: TaskStatus[] = ['TODO', 'IN_PROGRESS', 'IN_REVIEW', 'DONE', 'CANCELLED'];

  priorities: TaskPriority[] = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];

  searchText = '';
  filterStatus = '';
  filterPriority = '';
  filterAssignee = '';
  filterProject = '';

  sortField = 'id';
  sortDirection = 'asc';

  page = 1;
  pageSize = 5;

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.loadingTasks = true;

    this.taskService.getTasks().subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.loadingTasks = false;
        this.applyFilters();
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load tasks.';
        this.loadingTasks = false;
        this.cdr.detectChanges();
      },
    });
  }

  openCreateForm(): void {
    this.editingTask = null;
    this.form = this.emptyForm();
    this.showForm = true;
    this.clearMessages();
  }

  openEditForm(task: Task): void {
    this.editingTask = task;
    this.form = {
      title: task.title,
      description: task.description ?? '',
      projectId: task.projectId,
      assigneeId: task.assigneeId,
      reporterId: task.reporterId,
      status: task.status,
      priority: task.priority,
      dueDate: task.dueDate,
    };
    this.showForm = true;
    this.clearMessages();
  }

  closeForm(): void {
    this.showForm = false;
    this.editingTask = null;
  }

  submitForm(): void {
    if (!this.form.title.trim() || !this.form.projectId || !this.form.reporterId) {
      this.errorMessage = 'Please fill in all required fields.';
      return;
    }

    this.loading = true;
    this.clearMessages();

    const request: TaskRequest = {
      ...this.form,
      title: this.form.title.trim(),
      description: this.form.description?.trim() ?? '',
    };

    const request$ = this.editingTask
      ? this.taskService.updateTask(this.editingTask.id, request)
      : this.taskService.createTask(request);

    request$.subscribe({
      next: (task) => {
        if (this.editingTask) {
          const index = this.tasks.findIndex((item) => item.id === task.id);

          if (index !== -1) {
            this.tasks[index] = task;
          }

          this.successMessage = 'Task updated successfully.';
        } else {
          this.tasks = [...this.tasks, task];
          this.successMessage = 'Task created successfully.';
        }

        this.loading = false;
        this.closeForm();
        this.applyFilters();
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Failed to save task.';
        this.cdr.detectChanges();
      },
    });
  }

  viewTask(task: Task): void {
    this.taskService.getTask(task.id).subscribe({
      next: (result) => {
        this.selectedTask = result;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load task details.';
        this.cdr.detectChanges();
      },
    });
  }

  closeDetails(): void {
    this.selectedTask = null;
  }

  deleteTask(task: Task): void {
    if (!confirm(`Delete task "${task.title}"?`)) {
      return;
    }

    this.loading = true;
    this.clearMessages();

    this.taskService.deleteTask(task.id).subscribe({
      next: () => {
        this.tasks = this.tasks.filter((item) => item.id !== task.id);

        if (this.selectedTask?.id === task.id) {
          this.selectedTask = null;
        }

        this.loading = false;
        this.successMessage = 'Task deleted successfully.';
        this.applyFilters();
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Failed to delete task.';
        this.cdr.detectChanges();
      },
    });
  }

  assignTask(task: Task, userId: number): void {
    this.taskService.assignTask(task.id, userId).subscribe({
      next: () => {
        task.assigneeId = userId;

        if (this.selectedTask?.id === task.id) {
          this.selectedTask = {
            ...this.selectedTask,
            assigneeId: userId,
          };
        }

        this.successMessage = 'Task assigned successfully.';
        this.applyFilters();
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to assign task.';
        this.cdr.detectChanges();
      },
    });
  }

  updateStatus(task: Task, status: TaskStatus): void {
    this.taskService.changeStatus(task.id, status).subscribe({
      next: () => {
        task.status = status;

        if (this.selectedTask?.id === task.id) {
          this.selectedTask = {
            ...this.selectedTask,
            status,
          };
        }

        this.successMessage = 'Task status updated.';
        this.applyFilters();
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to change task status.';
        this.cdr.detectChanges();
      },
    });
  }

  updatePriority(task: Task, priority: TaskPriority): void {
    this.taskService.changePriority(task.id, priority).subscribe({
      next: () => {
        task.priority = priority;

        if (this.selectedTask?.id === task.id) {
          this.selectedTask = {
            ...this.selectedTask,
            priority,
          };
        }

        this.successMessage = 'Task priority updated.';
        this.applyFilters();
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to change task priority.';
        this.cdr.detectChanges();
      },
    });
  }

  applyFilters(): void {
    let result = [...this.tasks];

    const search = this.searchText.trim().toLowerCase();

    if (search) {
      result = result.filter(
        (task) =>
          task.title.toLowerCase().includes(search) ||
          (task.description ?? '').toLowerCase().includes(search),
      );
    }

    if (this.filterStatus) {
      result = result.filter((task) => task.status === this.filterStatus);
    }

    if (this.filterPriority) {
      result = result.filter((task) => task.priority === this.filterPriority);
    }

    if (this.filterAssignee) {
      result = result.filter((task) => task.assigneeId === Number(this.filterAssignee));
    }

    if (this.filterProject) {
      result = result.filter((task) => task.projectId === Number(this.filterProject));
    }

    result.sort((a, b) => {
      let valueA: string | number = a.id;
      let valueB: string | number = b.id;

      if (this.sortField === 'title') {
        valueA = a.title.toLowerCase();
        valueB = b.title.toLowerCase();
      }

      if (this.sortField === 'status') {
        valueA = a.status;
        valueB = b.status;
      }

      if (this.sortField === 'priority') {
        valueA = a.priority;
        valueB = b.priority;
      }

      if (this.sortField === 'project') {
        valueA = a.projectId;
        valueB = b.projectId;
      }

      if (this.sortField === 'assignee') {
        valueA = a.assigneeId ?? 0;
        valueB = b.assigneeId ?? 0;
      }

      if (valueA < valueB) {
        return this.sortDirection === 'asc' ? -1 : 1;
      }

      if (valueA > valueB) {
        return this.sortDirection === 'asc' ? 1 : -1;
      }

      return 0;
    });

    this.filteredTasks = result;

    const maxPage = Math.max(1, this.totalPages);
    if (this.page > maxPage) {
      this.page = maxPage;
    }
  }

  changeSort(field: string): void {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }

    this.applyFilters();
  }

  clearFilters(): void {
    this.searchText = '';
    this.filterStatus = '';
    this.filterPriority = '';
    this.filterAssignee = '';
    this.filterProject = '';
    this.page = 1;
    this.applyFilters();
  }

  get totalPages(): number {
    return Math.max(1, Math.ceil(this.filteredTasks.length / this.pageSize));
  }

  get paginatedTasks(): Task[] {
    const start = (this.page - 1) * this.pageSize;
    return this.filteredTasks.slice(start, start + this.pageSize);
  }

  nextPage(): void {
    if (this.page < this.totalPages) {
      this.page++;
    }
  }

  previousPage(): void {
    if (this.page > 1) {
      this.page--;
    }
  }

  private emptyForm(): TaskRequest {
    return {
      title: '',
      description: '',
      projectId: 0,
      assigneeId: null,
      reporterId: 0,
      status: 'TODO',
      priority: 'MEDIUM',
      dueDate: null,
    };
  }

  private clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }
}
