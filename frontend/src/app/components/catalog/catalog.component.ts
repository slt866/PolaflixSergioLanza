import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { switchMap, tap } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { PolaflixService, Serie } from '../../services/polaflix.service';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent implements OnInit {
  userId = 0;
  series: Serie[] = [];
  initials: string[] = [];
  selectedInitial = 'A';
  searchTerm = '';
  highlightedSerieId: number | null = null;
  expandedSerieId: number | null = null;
  loading = true;
  message = '';
  errorMessage = '';
  selectedSerie: Serie | null = null;
  showDetailModal = false;
  userSeries: Set<number> = new Set();

  constructor(private route: ActivatedRoute, private polaflixService: PolaflixService) {}

  ngOnInit(): void {
    this.initials = Array.from({length:26}).map((_,i)=> String.fromCharCode(65+i));
    this.route.paramMap
      .pipe(
        tap(params => {
          this.userId = Number(params.get('usuarioId') ?? 0);
          this.errorMessage = '';
          this.message = '';
        }),
        switchMap(() => this.polaflixService.getSeries())
      )
      .subscribe({
        next: series => {
          this.series = series.sort((a, b) => a.titulo.localeCompare(b.titulo));
          this.loading = false;
          this.loadUserSeries();
        },
        error: error => {
          this.errorMessage = error;
          this.loading = false;
        }
      });
  }

  get displayedSeries(): Serie[] {
    if (!this.series || this.series.length === 0) return [];
    return this.series.filter(s => {
      const titulo = s.titulo ?? '';
      if (!titulo) return false;
      const first = titulo.charAt(0).toUpperCase();
      if (this.selectedInitial === '0-9') {
        return first >= '0' && first <= '9';
      }
      return first === this.selectedInitial;
    });
  }

  setInitial(letter: string): void {
    this.selectedInitial = letter;
    this.highlightedSerieId = null;
    this.expandedSerieId = null;
  }

  searchByName(): void {
    const term = (this.searchTerm ?? '').trim().toLowerCase();
    if (!term) return;
    const found = this.series.find(s => (s.titulo ?? '').toLowerCase() === term || (s.titulo ?? '').toLowerCase().includes(term));
    if (!found) {
      window.alert(`No se encontró la serie: ${this.searchTerm}`);
      return;
    }
    const first = (found.titulo ?? '').charAt(0).toUpperCase();
    const initial = (first >= 'A' && first <= 'Z') ? first : '0-9';
    this.setInitial(initial);
    this.highlightedSerieId = found.idSerie;
    this.expandedSerieId = found.idSerie;
    // small delay to allow view to update, then scroll to element
    setTimeout(()=>{
      const el = document.getElementById('serie-'+found.idSerie);
      if (el) el.scrollIntoView({behavior:'smooth', block:'center'});
    }, 50);
  }

  loadUserSeries(): void {
    this.polaflixService.getSeriesPendientes(this.userId).subscribe({
      next: (series) => {
        series.forEach(s => this.userSeries.add(s.idSerie));
        this.polaflixService.getSeriesEmpezadas(this.userId).subscribe({
          next: (series) => {
            series.forEach(s => this.userSeries.add(s.idSerie));
            this.polaflixService.getSeriesTerminadas(this.userId).subscribe({
              next: (series) => {
                series.forEach(s => this.userSeries.add(s.idSerie));
              }
            });
          }
        });
      }
    });
  }

  viewSerieDetail(serie: Serie): void {
    // Toggle inline expansion; fetch details from API first
    if (this.expandedSerieId === serie.idSerie) {
      this.expandedSerieId = null;
    } else {
      this.polaflixService.getSerieById(serie.idSerie).subscribe({
        next: (serieDetailed) => {
          const idx = this.series.findIndex(s => s.idSerie === serie.idSerie);
          if (idx >= 0) this.series[idx] = serieDetailed;
          this.expandedSerieId = serie.idSerie;
        },
        error: (error) => {
          this.errorMessage = error;
        }
      });
    }
  }

  closeDetailModal(): void {
    this.showDetailModal = false;
    this.selectedSerie = null;
  }

  addPending(serie: Serie): void {
    if (!this.userId) {
      this.errorMessage = 'ID de usuario inválido.';
      return;
    }
    this.polaflixService.addSeriePendiente(this.userId, serie.idSerie)
      .subscribe({
        next: () => {
          this.message = `La serie '${serie.titulo}' se ha agregado a pendientes.`;
          this.errorMessage = '';
          this.userSeries.add(serie.idSerie);
        },
        error: error => {
          this.errorMessage = error;
          this.message = '';
        }
      });
  }

  isSerieAdded(serie: Serie): boolean {
    return this.userSeries.has(serie.idSerie);
  }

  getCreadores(): string {
    if (!this.selectedSerie || !this.selectedSerie.creadores) return '';
    return this.selectedSerie.creadores.map(c => c.nombrePersona).join(', ');
  }

  getActores(): string {
    if (!this.selectedSerie || !this.selectedSerie.actores) return '';
    return this.selectedSerie.actores.map(a => a.nombrePersona).join(', ');
  }

  getCreadoresFor(serie: Serie): string {
    if (!serie || !serie.creadores) return '';
    return serie.creadores.map(c => c.nombrePersona).join(', ');
  }

  getActoresFor(serie: Serie): string {
    if (!serie || !serie.actores) return '';
    return serie.actores.map(a => a.nombrePersona).join(', ');
  }
}
