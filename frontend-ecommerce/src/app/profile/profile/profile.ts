import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { BadgeModule } from 'primeng/badge';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  imports: [CommonModule, CardModule, ButtonModule, BadgeModule],
  selector: 'app-profile',
  styleUrl: './profile.scss',
  templateUrl: './profile.html',
})
export class Profile {
  authService = inject(AuthService);
  private router = inject(Router);

  user = this.authService.currentUser;
  isB2B = this.authService.isB2B;

  goToCatalog(): void {
    void this.router.navigate(['/catalog']);
  }
}
