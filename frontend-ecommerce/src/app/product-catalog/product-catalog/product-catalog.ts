import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { toObservable } from '@angular/core/rxjs-interop';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { BadgeModule } from 'primeng/badge';
import { InputTextModule } from 'primeng/inputtext';
import { ProductService } from '../../services/product';
import { AuthService } from '../../services/auth-service';
import { Product } from '../../models/product.model';
import { catchError, combineLatest, distinctUntilChanged, of, switchMap, tap, } from 'rxjs';

@Component({
  imports: [CommonModule, FormsModule, CardModule, ButtonModule, BadgeModule, InputTextModule],
  selector: 'app-product-catalog',
  styleUrl: './product-catalog.scss',
  templateUrl: './product-catalog.html',
})
export class ProductCatalog {
  private productService = inject(ProductService);
  private authService = inject(AuthService);

  readonly products = signal<Product[]>([]);
  readonly loading = signal<boolean>(true);
  readonly searchTerm = signal('');

  private readonly session$ = toObservable(this.authService.currentUser);
  private readonly search$ = toObservable(this.searchTerm);

  constructor() {
    combineLatest([this.session$, this.search$])
      .pipe(
        distinctUntilChanged(
          ([previousUser, previousSearch], [currentUser, currentSearch]) =>
            previousUser === currentUser && previousSearch === currentSearch,
        ),
        tap(() => this.loading.set(true)),
        switchMap(([, search]) =>
          this.productService.getProducts(search).pipe(
            catchError((error) => {
              console.error('Erreur lors du chargement des produits', error);
              return of([]);
            }),
          ),
        ),
      )
      .subscribe((data: Product[]) => {
        this.products.set(data);
        this.loading.set(false);
      });
  }

  onSearchChange(): void {
    // Le changement du signal déclenche automatiquement la recherche.
  }
}
