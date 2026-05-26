import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { CatalogComponent } from './components/catalog/catalog.component';

export const ROUTES: Routes = [
  { path: '', component: HomeComponent },
  { path: 'catalogo/:usuarioId', component: CatalogComponent },
  { path: '**', redirectTo: '' }
];
