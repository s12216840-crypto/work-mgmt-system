import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { User, UserRequest } from '../../core/models/user.model';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './users.html',
  styleUrl: './users.css',
})
export class Users implements OnInit {
  private readonly userService = inject(UserService);
  private readonly fb = inject(FormBuilder);
  private readonly cdr = inject(ChangeDetectorRef);

  users: User[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  showForm = false;
  editingUser: User | null = null;
  selectedUser: User | null = null;

  userForm = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    role: ['VIEWER' as UserRequest['role'], Validators.required],
  });

  ngOnInit(): void {
    console.log('USERS COMPONENT LOADED');
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.errorMessage = '';

console.log('LOADING USERS...');

this.userService.getUsers().subscribe({
  next: (users) => {
    console.log('USERS RECEIVED:', users);

    this.users = [...users];
    this.loading = false;

    console.log('USERS COUNT:', this.users.length);
    console.log('LOADING FINISHED:', this.loading);

    this.cdr.detectChanges();
  },

  error: (error) => {
    console.error('FAILED TO LOAD USERS:', error);

    this.errorMessage = 'Failed to load users.';
    this.loading = false;

    this.cdr.detectChanges();
  },
});
  }

  openCreateForm(): void {
    this.editingUser = null;


this.userForm.reset({
  name: '',
  email: '',
  password: '',
  role: 'VIEWER',
});

this.showForm = true;
this.errorMessage = '';
this.successMessage = '';
  }

  openEditForm(user: User): void {
    this.editingUser = user;

this.userForm.reset({
  name: user.name,
  email: user.email,
  password: '',
  role: user.role,
});

this.showForm = true;
this.errorMessage = '';
this.successMessage = '';
  }

  closeForm(): void {
    this.showForm = false;
    this.editingUser = null;

this.userForm.reset({
  name: '',
  email: '',
  password: '',
  role: 'VIEWER',
});
  }

  submitForm(): void {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

const request: UserRequest = this.userForm.getRawValue();

this.loading = true;
this.errorMessage = '';
this.successMessage = '';

if (this.editingUser) {
  this.userService.updateUser(this.editingUser.id, request).subscribe({
    next: () => {
      this.successMessage = 'User updated successfully.';
      this.closeForm();
      this.loadUsers();
    },
    error: (error) => {
      console.error('FAILED TO UPDATE USER:', error);
      this.errorMessage = 'Failed to update user.';
      this.loading = false;
    },
  });
} else {
  this.userService.createUser(request).subscribe({
    next: () => {
      this.successMessage = 'User created successfully.';
      this.closeForm();
      this.loadUsers();
    },
    error: (error) => {
      console.error('FAILED TO CREATE USER:', error);
      this.errorMessage = 'Failed to create user.';
      this.loading = false;
    },
  });
}
  }

  viewUser(user: User): void {
    this.selectedUser = user;
  }

  closeDetails(): void {
    this.selectedUser = null;
  }

  deactivateUser(id: number): void {
    const confirmed = confirm('Are you sure you want to deactivate this user?');

if (!confirmed) {
  return;
}

this.loading = true;
this.errorMessage = '';
this.successMessage = '';

this.userService.deactivateUser(id).subscribe({
  next: () => {
    this.successMessage = 'User deactivated successfully.';
    this.loadUsers();
  },
  error: (error) => {
    console.error('FAILED TO DEACTIVATE USER:', error);
    this.errorMessage = 'Failed to deactivate user.';
    this.loading = false;
  },
});

  }

  isInvalid(controlName: string): boolean {
    const control = this.userForm.get(controlName);

return !!control && control.invalid && control.touched;

  }
}
