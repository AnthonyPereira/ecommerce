import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/products'; // URL de votre API Spring Boot

  constructor(private http: HttpClient) {}

  getProducts(search?: string): Observable<Product[]> {
    let params = new HttpParams();
    if (search && search.trim() !== '') {
      params = params.set('name', search.trim());
    }
    return this.http.get<Product[]>(this.apiUrl, { params });
  }
}
