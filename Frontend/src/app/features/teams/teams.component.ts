import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { Team, TeamRequest } from '../../core/models/team.model';
import { OrganizationResponse } from '../../core/models/organization.model';
import { User } from '../../core/models/user.model';

import { TeamService } from '../../core/services/team.service';
import { OrganizationService } from '../../core/services/organization.service';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-teams',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './teams.component.html',
  styleUrl: './teams.component.css',
})
export class TeamsComponent implements OnInit {
  private readonly teamService = inject(TeamService);
  private readonly organizationService = inject(OrganizationService);
  private readonly userService = inject(UserService);
  private readonly fb = inject(FormBuilder);
  private readonly cdr = inject(ChangeDetectorRef);

  teams: Team[] = [];
  organizations: OrganizationResponse[] = [];
  users: User[] = [];

  loading = false;
  loadingTeams = false;

  errorMessage = '';
  successMessage = '';

  showForm = false;

  editingTeam: Team | null = null;
  selectedTeam: Team | null = null;

  teamForm = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(2)]],
    description: [''],
    organizationId: [0, [Validators.required, Validators.min(1)]],
  });

  ngOnInit(): void {
    console.log('TEAMS COMPONENT LOADED');

    this.loadOrganizations();
    this.loadUsers();
    this.loadTeams();
  }

  loadTeams(): void {
    this.loadingTeams = true;
    this.errorMessage = '';

    console.log('LOADING TEAMS...');

    this.teamService.getTeams().subscribe({
      next: (teams) => {
        console.log('TEAMS RECEIVED:', teams);
        console.log('TEAMS LENGTH:', teams.length);

        this.teams = [...teams];
        this.loadingTeams = false;

        console.log('LOADING TEAMS AFTER:', this.loadingTeams);
        console.log('TEAMS AFTER:', this.teams);

        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('FAILED TO LOAD TEAMS:', error);

        this.errorMessage = 'Failed to load teams.';
        this.loadingTeams = false;

        this.cdr.detectChanges();
      },
    });
  }

  loadOrganizations(): void {
    this.organizationService.getOrganizations().subscribe({
      next: (organizations) => {
        console.log('ORGANIZATIONS RECEIVED FOR TEAMS:', organizations);

        this.organizations = [...organizations];
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('FAILED TO LOAD ORGANIZATIONS:', error);

        this.errorMessage = 'Failed to load organizations.';
        this.cdr.detectChanges();
      },
    });
  }

  loadUsers(): void {
    this.userService.getUsers().subscribe({
      next: (users) => {
        console.log('USERS RECEIVED FOR TEAMS:', users);

        this.users = [...users];
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('FAILED TO LOAD USERS:', error);
      },
    });
  }

  getOrganizationName(organizationId: number): string {
    const organization = this.organizations.find((org) => org.id === organizationId);

    return organization?.name ?? '-';
  }

  getUserName(userId: number): string {
    const user = this.users.find((user) => user.id === userId);

    return user?.name ?? `User #${userId}`;
  }

  openCreateForm(): void {
    this.editingTeam = null;

    this.teamForm.reset({
      name: '',
      description: '',
      organizationId: this.organizations.length > 0 ? this.organizations[0].id : 0,
    });

    this.showForm = true;
    this.errorMessage = '';
    this.successMessage = '';
  }

  openEditForm(team: Team): void {
    this.editingTeam = team;

    this.teamForm.reset({
      name: team.name,
      description: team.description ?? '',
      organizationId: team.organizationId,
    });

    this.showForm = true;
    this.errorMessage = '';
    this.successMessage = '';
  }

  closeForm(): void {
    this.showForm = false;
    this.editingTeam = null;

    this.teamForm.reset({
      name: '',
      description: '',
      organizationId: 0,
    });
  }

  submitForm(): void {
    if (this.teamForm.invalid) {
      this.teamForm.markAllAsTouched();
      return;
    }

    const request: TeamRequest = this.teamForm.getRawValue();

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.editingTeam) {
      this.teamService.updateTeam(this.editingTeam.id, request).subscribe({
        next: () => {
          this.successMessage = 'Team updated successfully.';
          this.closeForm();
          this.loading = false;
          this.loadTeams();
        },
        error: (error) => {
          console.error('FAILED TO UPDATE TEAM:', error);

          this.errorMessage = 'Failed to update team.';
          this.loading = false;

          this.cdr.detectChanges();
        },
      });
    } else {
      this.teamService.createTeam(request).subscribe({
        next: () => {
          this.successMessage = 'Team created successfully.';
          this.closeForm();
          this.loading = false;
          this.loadTeams();
        },
        error: (error) => {
          console.error('FAILED TO CREATE TEAM:', error);

          this.errorMessage = 'Failed to create team.';
          this.loading = false;

          this.cdr.detectChanges();
        },
      });
    }
  }

  viewTeam(team: Team): void {
    this.selectedTeam = team;
    this.cdr.detectChanges();
  }

  closeDetails(): void {
    this.selectedTeam = null;
  }

  deleteTeam(id: number): void {
    const confirmed = confirm('Are you sure you want to delete this team?');

    if (!confirmed) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.teamService.deleteTeam(id).subscribe({
      next: () => {
        this.successMessage = 'Team deleted successfully.';
        this.selectedTeam = null;
        this.loading = false;
        this.loadTeams();
      },
      error: (error) => {
        console.error('FAILED TO DELETE TEAM:', error);

        this.errorMessage = 'Failed to delete team.';
        this.loading = false;

        this.cdr.detectChanges();
      },
    });
  }

  addUserToTeam(teamId: number, userId: number): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.teamService.addUserToTeam(teamId, userId).subscribe({
      next: () => {
        this.successMessage = 'User added to team successfully.';

        if (this.selectedTeam?.id === teamId && !this.selectedTeam.userIds.includes(userId)) {
          this.selectedTeam = {
            ...this.selectedTeam,
            userIds: [...this.selectedTeam.userIds, userId],
          };
        }

        this.loadTeams();
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('FAILED TO ADD USER TO TEAM:', error);

        this.errorMessage = 'Failed to add user to team.';
        this.cdr.detectChanges();
      },
    });
  }

  removeUserFromTeam(teamId: number, userId: number): void {
    const confirmed = confirm('Are you sure you want to remove this user from the team?');

    if (!confirmed) {
      return;
    }

    this.errorMessage = '';
    this.successMessage = '';

    this.teamService.removeUserFromTeam(teamId, userId).subscribe({
      next: () => {
        this.successMessage = 'User removed from team successfully.';

        if (this.selectedTeam?.id === teamId) {
          this.selectedTeam = {
            ...this.selectedTeam,
            userIds: this.selectedTeam.userIds.filter((id) => id !== userId),
          };
        }

        this.loadTeams();
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('FAILED TO REMOVE USER FROM TEAM:', error);

        this.errorMessage = 'Failed to remove user from team.';
        this.cdr.detectChanges();
      },
    });
  }

  isUserInTeam(team: Team, userId: number): boolean {
    return team.userIds.includes(userId);
  }

  isInvalid(controlName: string): boolean {
    const control = this.teamForm.get(controlName);

    return !!control && control.invalid && control.touched;
  }
}
