import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PolaflixService, Serie, ProgresoSerie } from '../../services/polaflix.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  userName = '';
  userId = environment.userId;
  
  seriesPendientes: Serie[] = [];
  seriesEmpezadas: Serie[] = [];
  seriesTerminadas: Serie[] = [];
  
  loading = true;
  errorMessage = '';
  selectedSerie: Serie | null = null;
  serieProgress: ProgresoSerie | null = null;
  showSerieDetail = false;
  expandedChapterKey: string | null = null;

  constructor(private polaflixService: PolaflixService) {}

  ngOnInit(): void {
    this.loadUsuario();
    this.loadAllSeries();
  }

  loadUsuario(): void {
    if (!this.userId) {
      return;
    }

    this.polaflixService.getUsuarioById(this.userId).subscribe({
      next: (usuario) => {
        if (usuario?.nombreUsuario) {
          this.userName = usuario.nombreUsuario;
        }
      },
      error: () => {
      }
    });
  }

  loadAllSeries(): void {
    this.loading = true;
    this.polaflixService.getSeriesPendientes(this.userId).subscribe({
      next: (series) => {
        this.seriesPendientes = series;
        this.polaflixService.getSeriesEmpezadas(this.userId).subscribe({
          next: (series) => {
            this.seriesEmpezadas = series;
            this.polaflixService.getSeriesTerminadas(this.userId).subscribe({
              next: (series) => {
                this.seriesTerminadas = series;
                this.loading = false;
              },
              error: (error) => {
                this.errorMessage = error;
                this.loading = false;
              }
            });
          },
          error: (error) => {
            this.errorMessage = error;
            this.loading = false;
          }
        });
      },
      error: (error) => {
        this.errorMessage = error;
        this.loading = false;
      }
    });
  }

  viewSerieDetail(serie: Serie): void {
    this.polaflixService.getSerieById(serie.idSerie).subscribe({
      next: (serieDetailed) => {
        this.selectedSerie = serieDetailed;
        this.serieProgress = null;
        this.expandedChapterKey = null;
        this.showSerieDetail = true;
        this.loadSerieProgress(serie.idSerie);
      },
      error: (error) => {
        this.errorMessage = error;
      }
    });
  }

  loadSerieProgress(serieId: number): void {
    this.polaflixService.getProgresoSerie(this.userId, serieId).subscribe({
      next: (progreso) => {
        this.serieProgress = progreso;
      },
      error: () => {
        this.serieProgress = null;
      }
    });
  }

  closeSerieDetail(): void {
    this.showSerieDetail = false;
    this.selectedSerie = null;
    this.serieProgress = null;
    this.expandedChapterKey = null;
  }

  toggleCapituloDescripcion(temp: number, cap: number): void {
    const key = `${temp}-${cap}`;
    this.expandedChapterKey = this.expandedChapterKey === key ? null : key;
  }

  isCapituloExpanded(temp: number, cap: number): boolean {
    return this.expandedChapterKey === `${temp}-${cap}`;
  }

  markChapterAsWatched(temp: number, cap: number): void {
    if (!this.selectedSerie) return;
    
    this.polaflixService.marcarCapituloVisto(this.userId, this.selectedSerie.idSerie, temp, cap).subscribe({
      next: () => {
        this.loadAllSeries();
        if (this.selectedSerie) {
          this.viewSerieDetail(this.selectedSerie);
        }
      },
      error: (error) => {
        this.errorMessage = error;
      }
    });
  }

  isChapterWatched(temp: number, cap: number): boolean {
    if (!this.serieProgress || !this.serieProgress.visualizaciones) {
      return false;
    }
    return this.serieProgress.visualizaciones.some(
      (vis) => vis.numeroTemp === temp && vis.numeroCap === cap
    );
  }

  isSerieInPendientes(serie: Serie): boolean {
    return this.seriesPendientes.some(s => s.idSerie === serie.idSerie);
  }

  isSerieInEmpezadas(serie: Serie): boolean {
    return this.seriesEmpezadas.some(s => s.idSerie === serie.idSerie);
  }

  isSerieInTerminadas(serie: Serie): boolean {
    return this.seriesTerminadas.some(s => s.idSerie === serie.idSerie);
  }

  getCreadores(): string {
    if (!this.selectedSerie || !this.selectedSerie.creadores) return '';
    return this.selectedSerie.creadores.map(c => c.nombrePersona).join(', ');
  }

  getActores(): string {
    if (!this.selectedSerie || !this.selectedSerie.actores) return '';
    return this.selectedSerie.actores.map(a => a.nombrePersona).join(', ');
  }
}
