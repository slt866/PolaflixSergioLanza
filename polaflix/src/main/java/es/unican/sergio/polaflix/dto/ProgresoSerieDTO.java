package es.unican.sergio.polaflix.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProgresoSerieDTO {
    private Long idProgreso;
    private SerieDTO serie;
    private int ultimaTempVista;
    private int ultimoCapVisto;
    private List<VisualizacionDTO> visualizaciones;
    
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class VisualizacionDTO {
        private Long idVisualizacion;
        private int numeroTemp;
        private int numeroCap;
    }
}
