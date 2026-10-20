import { Component, OnInit } from '@angular/core';
import { ProductService } from './services/product';
import { Product } from './models/product.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { BadgeModule } from 'primeng/badge';
import { InputTextModule } from 'primeng/inputtext';

@Component({
  imports: [FormsModule, CommonModule, InputTextModule, CardModule, ButtonModule, BadgeModule],
  selector: 'app-root',
  styleUrl: './app.scss',
  templateUrl: './app.html',
})
export class App implements OnInit {
  products: Product[] = [];
  loading = true;
  searchTerm: string = '';

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.productService.getProducts(this.searchTerm).subscribe({
      next: (data) => {
        this.products = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des produits', err);
        this.loading = false;
      },
    });
  }

  // Déclenché à chaque modification du texte de recherche
  onSearchChange(): void {
    this.loadProducts();
  }
}
