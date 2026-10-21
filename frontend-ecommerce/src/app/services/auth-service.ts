import { computed, Service, signal, inject } from '@angular/core';
import { AuthResponse, LoginRequest } from '../models/auth.model';
import { Observable, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Service()
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/auth';

  // State via Signals
  private currentUserSignal = signal<AuthResponse | null>(this.loadUserFromStorage());

  readonly currentUser = this.currentUserSignal.asReadonly();
  readonly isAuthenticated = computed(() => this.currentUser() !== null);
  readonly isB2B = computed(() => {
    const user = this.currentUser();
    return user !== null && user.companyId !== null;
  });

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((response) => {
        this.currentUserSignal.set(response);
        localStorage.setItem('user_session', JSON.stringify(response));
      }),
    );
  }

  logout() {
    this.currentUserSignal.set(null);
    localStorage.removeItem('user_session');
  }

  private loadUserFromStorage(): AuthResponse | null {
    if (typeof window === 'undefined' || !window.localStorage) {
      return null;
    }

    const stored = localStorage.getItem('user_session');
    return stored ? JSON.parse(stored) : null;
  }
}
