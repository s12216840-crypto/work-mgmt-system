import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Project, ProjectRequest, ProjectStatus } from '../../core/models/project.model';
import { ProjectService } from '../../core/services/project.service';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.css',
})
export class ProjectsComponent implements OnInit {
  private readonly projectService = inject(ProjectService);
  private readonly cdr = inject(ChangeDetectorRef);

  projects: Project[] = [];
  selectedProject: Project | null = null;

  loadingProjects = true;
  loading = false;
  errorMessage = '';
  successMessage = '';

  showForm = false;
  editingProject: Project | null = null;

  form: ProjectRequest = this.emptyForm();

  statuses: ProjectStatus[] = ['PLANNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'];

  memberUserId = '';

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this.loadingProjects = true;

    this.projectService.getProjects().subscribe({
      next: (projects) => {
        this.projects = projects;
        this.loadingProjects = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load projects.';
        this.loadingProjects = false;
        this.cdr.detectChanges();
      },
    });
  }

  openCreateForm(): void {
    this.editingProject = null;
    this.form = this.emptyForm();
    this.showForm = true;
    this.clearMessages();
  }

  openEditForm(project: Project): void {
    this.editingProject = project;
    this.form = {
      name: project.name,
      description: project.description ?? '',
      organizationId: project.organizationId,
      ownerId: project.ownerId,
      status: project.status,
      startDate: project.startDate,
      endDate: project.endDate,
    };
    this.showForm = true;
    this.clearMessages();
  }

  closeForm(): void {
    this.showForm = false;
    this.editingProject = null;
  }

  submitForm(): void {
    if (!this.form.name.trim() || !this.form.organizationId || !this.form.ownerId) {
      this.errorMessage = 'Please fill in all required fields.';
      return;
    }

    this.loading = true;
    this.clearMessages();

    const request: ProjectRequest = {
      ...this.form,
      name: this.form.name.trim(),
      description: this.form.description?.trim() ?? '',
    };

    const request$ = this.editingProject
      ? this.projectService.updateProject(this.editingProject.id, request)
      : this.projectService.createProject(request);

    request$.subscribe({
      next: (project) => {
        if (this.editingProject) {
          const index = this.projects.findIndex((item) => item.id === project.id);

          if (index !== -1) {
            this.projects[index] = project;
          }

          this.successMessage = 'Project updated successfully.';
        } else {
          this.projects = [...this.projects, project];
          this.successMessage = 'Project created successfully.';
        }

        this.loading = false;
        this.closeForm();
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Failed to save project.';
        this.cdr.detectChanges();
      },
    });
  }

  viewProject(project: Project): void {
    this.projectService.getProject(project.id).subscribe({
      next: (result) => {
        this.selectedProject = result;
        this.memberUserId = '';
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load project details.';
        this.cdr.detectChanges();
      },
    });
  }

  closeDetails(): void {
    this.selectedProject = null;
  }

  deleteProject(project: Project): void {
    if (!confirm(`Delete project "${project.name}"?`)) {
      return;
    }

    this.loading = true;
    this.clearMessages();

    this.projectService.deleteProject(project.id).subscribe({
      next: () => {
        this.projects = this.projects.filter((item) => item.id !== project.id);

        if (this.selectedProject?.id === project.id) {
          this.selectedProject = null;
        }

        this.loading = false;
        this.successMessage = 'Project deleted successfully.';
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Failed to delete project.';
        this.cdr.detectChanges();
      },
    });
  }

  addMember(): void {
    if (!this.selectedProject || !this.memberUserId) {
      return;
    }

    const userId = Number(this.memberUserId);

    this.projectService.addMember(this.selectedProject.id, userId).subscribe({
      next: () => {
        if (!this.selectedProject) {
          return;
        }

        this.selectedProject = {
          ...this.selectedProject,
          memberIds: [...this.selectedProject.memberIds, userId],
        };

        this.memberUserId = '';
        this.successMessage = 'Member added successfully.';
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to add member.';
        this.cdr.detectChanges();
      },
    });
  }

  removeMember(userId: number): void {
    if (!this.selectedProject) {
      return;
    }

    this.projectService.removeMember(this.selectedProject.id, userId).subscribe({
      next: () => {
        if (!this.selectedProject) {
          return;
        }

        this.selectedProject = {
          ...this.selectedProject,
          memberIds: this.selectedProject.memberIds.filter((id) => id !== userId),
        };

        this.successMessage = 'Member removed successfully.';
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to remove member.';
        this.cdr.detectChanges();
      },
    });
  }

  private emptyForm(): ProjectRequest {
    return {
      name: '',
      description: '',
      organizationId: 0,
      ownerId: 0,
      status: 'PLANNED',
      startDate: null,
      endDate: null,
    };
  }

  private clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }
}
