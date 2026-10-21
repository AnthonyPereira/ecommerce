import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';
import { Toolbar } from 'primeng/toolbar';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { MenuItem } from 'primeng/api';

@Component({
  imports: [CommonModule, Toolbar, ButtonModule, MenuModule],
  selector: 'app-header',
  styleUrl: './header.scss',
  templateUrl: './header.html',
})
export class Header {
  authService = inject(AuthService);
  private router = inject(Router);

  // État local avec un Signal pour ouvrir/fermer le menu déroulant du profil
  isProfileMenuOpen = signal(false);

  profileItems: MenuItem[] = [
    {
      label: 'Informations personnelles',
      icon: 'pi pi-id-card',
      command: () => this.goToProfile(),
    },
    {
      separator: true,
    },
    {
      label: 'Déconnexion',
      icon: 'pi pi-sign-out',
      command: () => this.logout(),
    },
  ];

  toggleProfileMenu() {
    this.isProfileMenuOpen.update((isOpen) => !isOpen);
  }

  goToCatalog() {
    void this.router.navigate(['/catalog']);
  }

  goToCart() {
    // TODO: Rediriger vers la page panier si elle existe
    console.log('Redirection panier');
  }

  goToProfile() {
    this.isProfileMenuOpen.set(false);
    void this.router.navigate(['/profile']);
  }

  logout() {
    this.isProfileMenuOpen.set(false);
    this.authService.logout();
    void this.router.navigate(['/']);
  }
}
