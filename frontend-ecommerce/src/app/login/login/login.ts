import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    InputTextModule,
    ButtonModule,
    MessageModule,
  ],
  selector: 'app-login',
  styleUrl: './login.scss',
  templateUrl: './login.html',
})
export class Login {
  loginForm: FormGroup;
  loading = false;
  errorMessage = signal<string | null>(null);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit() {
    this.errorMessage.set(null);

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loading = true;

    this.authService.login(this.loginForm.value).subscribe({
      next: (res) => {
        this.loading = false;
        this.router.navigate(['/catalog']);
      },
      error: (err: HttpErrorResponse) => {
        this.loading = false;

        switch (err.status) {
          case 401:
            this.errorMessage.set(
              'Identifiants incorrects. Vérifiez votre adresse e-mail et votre mot de passe.',
            );
            break;

          case 429:
            this.errorMessage.set(
              'Trop de tentatives de connexion. Veuillez réessayer dans 15 minutes.',
            );
            break;

          case 0:
            this.errorMessage.set('Impossible de contacter le serveur. Vérifiez votre connexion.');
            break;

          default:
            this.errorMessage.set('Une erreur est survenue. Veuillez réessayer ultérieurement.');
        }
      },
    });
  }
}
