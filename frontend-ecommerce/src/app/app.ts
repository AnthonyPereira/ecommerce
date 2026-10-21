import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { BadgeModule } from 'primeng/badge';
import { InputTextModule } from 'primeng/inputtext';
import { AuthService } from './services/auth-service';
import { Router, RouterOutlet } from '@angular/router';
import {Header} from './header/header/header';

@Component({
  imports: [
    FormsModule,
    CommonModule,
    InputTextModule,
    CardModule,
    ButtonModule,
    BadgeModule,
    RouterOutlet,
    Header,
  ],
  selector: 'app-root',
  styleUrl: './app.scss',
  templateUrl: './app.html',
})
export class App {
  authService = inject(AuthService);
  router = inject(Router);

  currentUser = this.authService.currentUser;
  isAuthenticated = this.authService.isAuthenticated;
  isB2B = this.authService.isB2B;

  goToLogin() {
    void this.router.navigate(['/login']);
  }

  goToCatalog() {
    void this.router.navigate(['/']);
  }

  logout() {
    this.authService.logout();
    void this.router.navigate(['/']);
  }
}
