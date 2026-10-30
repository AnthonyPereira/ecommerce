import { Routes } from '@angular/router';
import { Login } from './login/login/login';
import { ProductCatalog } from './product-catalog/product-catalog/product-catalog';
import { Profile } from './profile/profile/profile';
import { authGuard, guestGuard } from './auth.guard';

export const routes: Routes = [
  // Page d'accueil : le catalogue de produits
  {
    path: 'catalog',
    component: ProductCatalog,
  },
  {
    path: 'profile',
    component: Profile,
    canActivate: [authGuard],
  },
  // Page de connexion
  {
    path: '',
    component: Login,
    canActivate: [guestGuard],
  },
  // Redirection par sécurité
  {
    path: '**',
    redirectTo: '',
  },
];
