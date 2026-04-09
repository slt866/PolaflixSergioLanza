package es.unican.sergio.polaflix;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import es.unican.sergio.polaflix.model.Capitulo;
import es.unican.sergio.polaflix.model.Persona;
import es.unican.sergio.polaflix.model.Serie;
import es.unican.sergio.polaflix.model.SuscripcionBajoDemanda;
import es.unican.sergio.polaflix.model.SuscripcionFija;
import es.unican.sergio.polaflix.model.Temporada;
import es.unican.sergio.polaflix.model.TipoSerie;
import es.unican.sergio.polaflix.model.Usuario;
import es.unican.sergio.polaflix.repository.PersonaRepository;
import es.unican.sergio.polaflix.repository.SerieRepository;
import es.unican.sergio.polaflix.repository.UsuarioRepository;

// @Component le dice a Spring: "Oye, cuando arranques, busca esta clase y gestiónala"
@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final SerieRepository serieRepository;
    private final PersonaRepository personaRepository;

    // Inyección de dependencias por constructor. 
    public DatabaseSeeder(UsuarioRepository usuarioRepository, 
                          SerieRepository serieRepository, 
                          PersonaRepository personaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.serieRepository = serieRepository;
        this.personaRepository = personaRepository;
    }

    // Este método se ejecutará automáticamente justo después de que arranque la aplicación
    @Override
    public void run(String... args) throws Exception {
        
        System.out.println("=========================================================");
        System.out.println("⏳ Cargando catálogo y usuarios en Polaflix...");
        System.out.println("=========================================================");

        // --- 1. CREACIÓN DE PERSONAS ---
        Persona vince = new Persona(); 
        vince.setNombrePersona("Vince Gilligan");
        
        Persona bryan = new Persona(); 
        bryan.setNombrePersona("Bryan Cranston");
        
        Persona aaron = new Persona(); 
        aaron.setNombrePersona("Aaron Paul");
        
        // Guardamos las personas primero usando su propio repositorio
        personaRepository.saveAll(Arrays.asList(vince, bryan, aaron));

        // --- 2. CREACIÓN DE SERIES Y SU ESTRUCTURA (Agregado de Catálogo) ---
        Serie breakingBad = new Serie();
        breakingBad.setTitulo("Breaking Bad");
        breakingBad.setSinopsis("Un profesor de química se vuelve narco para pagar su tratamiento.");
        breakingBad.setTipo(TipoSerie.GOLD);
        breakingBad.setCreadores(Arrays.asList(vince));
        breakingBad.setActores(Arrays.asList(bryan, aaron));

        Temporada t1 = new Temporada();
        t1.setNumeroTemp(1);
        t1.setTitulo("Piloto y comienzos");
        t1.setSerie(breakingBad); // Enganchamos la temporada a la serie

        Capitulo c1 = new Capitulo();
        c1.setNumeroCap(1);
        c1.setTitulo("Piloto");
        c1.setDescripcion("Walter descubre su cáncer y cocina por primera vez.");
        c1.setTemporada(t1); // Enganchamos el capítulo a la temporada

        // Montamos la composición en memoria (De Padre a Hijo)
        t1.setCapitulos(Arrays.asList(c1));
        breakingBad.setTemporadas(Arrays.asList(t1));

        // Magia de JPA: Al guardar la serie, por el 'CascadeType.ALL', 
        // se guardan automáticamente las temporadas y sus capítulos en H2
        serieRepository.save(breakingBad);

        // --- 3. CREACIÓN DE USUARIOS ---
        // Creamos a Paco con Suscripción Fija
        SuscripcionFija subFija = new SuscripcionFija(); // Ya tiene la cuota de 20.0 por el constructor
        
        Usuario paco = new Usuario();
        paco.setNombreUsuario("Paco");
        paco.setContrasena("1234");
        paco.setCuentaBancaria("ES111122223333");
        paco.setSuscripcion(subFija);

        // Creamos a Lola con Suscripción Bajo Demanda
        SuscripcionBajoDemanda subVariable = new SuscripcionBajoDemanda();
        
        Usuario lola = new Usuario();
        lola.setNombreUsuario("Lola");
        lola.setContrasena("5678");
        lola.setCuentaBancaria("ES999988887777");
        lola.setSuscripcion(subVariable);

        // Guardamos los usuarios en la base de datos usando el Repositorio
        usuarioRepository.save(paco);
        usuarioRepository.save(lola);

        System.out.println("✅ ¡Datos cargados! Catálogo y usuarios listos en la BD.");
        System.out.println("=========================================================");
    }
}