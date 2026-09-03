import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';

import { CommonModule } from '@angular/common';

import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { OrganizationRequest, OrganizationResponse } from '../../core/models/organization.model';

import { OrganizationService } from '../../core/services/organization.service';

@Component({
  selector: 'app-organizations',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './organizations.component.html',
  styleUrl: './organizations.component.css',
})
export class OrganizationsComponent implements OnInit {
  private readonly organizationService = inject(OrganizationService);
  private readonly fb = inject(FormBuilder);
  private readonly cdr = inject(ChangeDetectorRef);

  organizations: OrganizationResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  showForm = false;

  editingOrganization: OrganizationResponse | null = null;
  selectedOrganization: OrganizationResponse | null = null;

  organizationForm = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(2)]],
    description: [''],
  });

  ngOnInit(): void {
    console.log('ORGANIZATIONS COMPONENT LOADED');

    this.loadOrganizations();
  }

  loadOrganizations(): void {
    this.loading = true;
    this.errorMessage = '';

    console.log('LOADING ORGANIZATIONS...');

    this.organizationService.getOrganizations().subscribe({
      next: (organizations) => {
        console.log('ORGANIZATIONS RECEIVED:', organizations);

        this.organizations = [...organizations];

        this.loading = false;

        console.log('ORGANIZATIONS COUNT:', this.organizations.length);

        console.log('LOADING FINISHED:', this.loading);

        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error('FAILED TO LOAD ORGANIZATIONS:', error);

        this.errorMessage = 'Failed to load organizations.';

        this.loading = false;

        this.cdr.detectChanges();
      },
    });
  }

  openCreateForm(): void {
    this.editingOrganization = null;

    this.organizationForm.reset({
      name: '',
      description: '',
    });

    this.showForm = true;

    this.errorMessage = '';
    this.successMessage = '';
  }

  openEditForm(organization: OrganizationResponse): void {
    this.editingOrganization = organization;

    this.organizationForm.reset({
      name: organization.name,
      description: organization.description ?? '',
    });

    this.showForm = true;

    this.errorMessage = '';
    this.successMessage = '';
  }

  closeForm(): void {
    this.showForm = false;

    this.editingOrganization = null;

    this.organizationForm.reset({
      name: '',
      description: '',
    });
  }

  submitForm(): void {
    if (this.organizationForm.invalid) {
      this.organizationForm.markAllAsTouched();

      return;
    }

    const request: OrganizationRequest = this.organizationForm.getRawValue();

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.editingOrganization) {
      this.organizationService.updateOrganization(this.editingOrganization.id, request).subscribe({
        next: () => {
          this.successMessage = 'Organization updated successfully.';

          this.closeForm();

          this.loadOrganizations();
        },

        error: (error) => {
          console.error('FAILED TO UPDATE ORGANIZATION:', error);

          this.errorMessage = 'Failed to update organization.';

          this.loading = false;

          this.cdr.detectChanges();
        },
      });
    } else {
      this.organizationService.createOrganization(request).subscribe({
        next: () => {
          this.successMessage = 'Organization created successfully.';

          this.closeForm();

          this.loadOrganizations();
        },

        error: (error) => {
          console.error('FAILED TO CREATE ORGANIZATION:', error);

          this.errorMessage = 'Failed to create organization.';

          this.loading = false;

          this.cdr.detectChanges();
        },
      });
    }
  }

  viewOrganization(organization: OrganizationResponse): void {
    this.selectedOrganization = organization;
  }

  closeDetails(): void {
    this.selectedOrganization = null;
  }

  deleteOrganization(id: number): void {
    const confirmed = confirm('Are you sure you want to delete this organization?');

    if (!confirmed) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.organizationService.deleteOrganization(id).subscribe({
      next: () => {
        this.successMessage = 'Organization deleted successfully.';

        this.loadOrganizations();
      },

      error: (error) => {
        console.error('FAILED TO DELETE ORGANIZATION:', error);

        this.errorMessage = 'Failed to delete organization.';

        this.loading = false;

        this.cdr.detectChanges();
      },
    });
  }

  isInvalid(controlName: string): boolean {
    const control = this.organizationForm.get(controlName);

    return !!control && control.invalid && control.touched;
  }
}
