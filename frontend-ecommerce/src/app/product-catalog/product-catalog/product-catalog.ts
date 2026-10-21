import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { BadgeModule } from 'primeng/badge';
import { InputTextModule } from 'primeng/inputtext';
import { ProductService } from '../../services/product';
import { Product } from '../../models/product.model';

@Component({
  imports: [CommonModule, FormsModule, CardModule, ButtonModule, BadgeModule, InputTextModule],
  selector: 'app-product-catalog',
  styleUrl: './product-catalog.scss',
  templateUrl: './product-catalog.html',
})
export class ProductCatalog implements OnInit {
  private productService = inject(ProductService);

  readonly products = signal<Product[]>([]);
  readonly loading = signal<boolean>(true);
  searchTerm: string = '';

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading.set(true);

    this.productService.getProducts(this.searchTerm).subscribe({
      next: (data) => {
        this.products.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des produits', err);
        this.loading.set(false);
      },
    });
  }

  onSearchChange(): void {
    this.loadProducts();
  }
}
