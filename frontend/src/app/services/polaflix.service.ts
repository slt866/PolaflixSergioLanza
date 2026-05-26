import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface Capitulo {
  idCap: number;
  numeroCap: number;
  titulo: string;
  descripcion: string;
}

export interface Temporada {
  idTemp: number;
  numeroTemp: number;
  titulo: string;
  capitulos?: Capitulo[];
  numeroCapitulos?: number;
}

export interface Persona {
  idPersona: number;
  nombrePersona: string;
}

export interface Serie {
  idSerie: number;
  titulo: string;
  sinopsis: string;
  tipo: string;
  numeroTemporadas?: number;
  creadores?: Persona[];
  actores?: Persona[];
  temporadas?: Temporada[];
}

export interface Usuario {
  idUsuario: number;
  nombreUsuario: string;
  cuentaBancaria: string;
}

export interface ProgresoSerie {
  idProgreso: number;
  serie: Serie;
  ultimaTempVista: number;
  ultimoCapVisto: number;
  visualizaciones: Visualizacion[];
}

export interface Visualizacion {
  idVisualizacion: number;
  numeroTemp: number;
  numeroCap: number;
}

@Injectable({
  providedIn: 'root'
})
export class PolaflixService {
  private apiUrl = environment.apiUrl;
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) {}

  // ====== Métodos de Series ======
  getSeries(): Observable<Serie[]> {
    return this.http
      .get<Serie[]>(`${this.apiUrl}/series`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  getSerieById(serieId: number): Observable<Serie> {
    return this.http
      .get<Serie>(`${this.apiUrl}/series/${serieId}`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  // ====== Métodos de Usuario ======
  getUsuarioById(usuarioId: number): Observable<Usuario> {
    return this.http
      .get<Usuario>(`${this.apiUrl}/usuarios/${usuarioId}`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  // ====== Métodos de Series del Usuario ======
  addSeriePendiente(usuarioId: number, serieId: number): Observable<Usuario> {
    return this.http
      .post<Usuario>(`${this.apiUrl}/usuarios/${usuarioId}/series/${serieId}`, {}, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  getSeriesPendientes(usuarioId: number): Observable<Serie[]> {
    return this.http
      .get<Serie[]>(`${this.apiUrl}/usuarios/${usuarioId}/series/pendientes`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  getSeriesEmpezadas(usuarioId: number): Observable<Serie[]> {
    return this.http
      .get<Serie[]>(`${this.apiUrl}/usuarios/${usuarioId}/series/empezadas`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  getSeriesTerminadas(usuarioId: number): Observable<Serie[]> {
    return this.http
      .get<Serie[]>(`${this.apiUrl}/usuarios/${usuarioId}/series/terminadas`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  // ====== Métodos de Progreso ======
  marcarCapituloVisto(usuarioId: number, serieId: number, temp: number, cap: number): Observable<Usuario> {
    return this.http
      .post<Usuario>(`${this.apiUrl}/usuarios/${usuarioId}/capitulos/visto?serieId=${serieId}&temp=${temp}&cap=${cap}`, {}, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  getProgresoSerie(usuarioId: number, serieId: number): Observable<ProgresoSerie> {
    return this.http
      .get<ProgresoSerie>(`${this.apiUrl}/usuarios/${usuarioId}/progreso/${serieId}`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  // ====== Métodos de Temporadas ======
  getTemporadasDeSerie(serieId: number): Observable<Temporada[]> {
    return this.http
      .get<Temporada[]>(`${this.apiUrl}/series/${serieId}/temporadas`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      return throwError(() => `Error de cliente: ${error.error.message}`);
    }
    const message = error.error?.message || error.statusText || 'Error del servidor';
    return throwError(() => `Error ${error.status}: ${message}`);
  }
}
