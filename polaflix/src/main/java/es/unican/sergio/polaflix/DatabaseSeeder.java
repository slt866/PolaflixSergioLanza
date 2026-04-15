package es.unican.sergio.polaflix;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    protected UsuarioRepository usuarioRepository;

    @Autowired
    protected SerieRepository serieRepository;

    @Autowired
    protected PersonaRepository personaRepository;

    private Persona vince;
    private Persona bryan;
    private Persona aaron;

    @Override
    public void run(String... args) throws Exception {
        
        System.out.println("=========================================================");
        System.out.println("⏳ Cargando catálogo y usuarios en Polaflix...");
        System.out.println("=========================================================");

        seedPersonas();
        seedSeries();
        seedUsuarios();

        System.out.println("✅ ¡Datos cargados! Catálogo y usuarios listos en la BD.");
        System.out.println("=========================================================");
    }

    private void seedPersonas() {
        vince = new Persona();
        vince.setNombrePersona("Vince Gilligan");

        bryan = new Persona();
        bryan.setNombrePersona("Bryan Cranston");

        aaron = new Persona();
        aaron.setNombrePersona("Aaron Paul");

        personaRepository.saveAll(Arrays.asList(vince, bryan, aaron));
    }

    private void seedSeries() {
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

        t1.setCapitulos(Arrays.asList(c1));
        breakingBad.setTemporadas(Arrays.asList(t1));

        serieRepository.save(breakingBad);
    }

    private void seedUsuarios() {
        SuscripcionFija subFija = new SuscripcionFija();

        Usuario paco = new Usuario();
        paco.setNombreUsuario("Paco");
        paco.setContrasena("1234");
        paco.setCuentaBancaria("ES111122223333");
        paco.setSuscripcion(subFija);

        SuscripcionBajoDemanda subVariable = new SuscripcionBajoDemanda();

        Usuario lola = new Usuario();
        lola.setNombreUsuario("Lola");
        lola.setContrasena("5678");
        lola.setCuentaBancaria("ES999988887777");
        lola.setSuscripcion(subVariable);

        usuarioRepository.save(paco);
        usuarioRepository.save(lola);
    }
}