import { Service, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';

@Service()
export class ProductService {
  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8080/products';

  getProducts(search?: string): Observable<Product[]> {
    let params = new HttpParams();

    if (search && search.trim() !== '') {
      params = params.set('name', search.trim());
    }

    return this.http.get<Product[]>(this.apiUrl, { params });
  }
}
