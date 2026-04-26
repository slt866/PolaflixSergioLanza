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
    private Persona davidBenioff;
    private Persona dbWeiss;
    private Persona emilia;
    private Persona peterdinklage;
    private Persona shaunlevy;
    private Persona winona;
    private Persona david;
    private Persona millie;

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

        davidBenioff = new Persona();
        davidBenioff.setNombrePersona("David Benioff");

        dbWeiss = new Persona();
        dbWeiss.setNombrePersona("D.B. Weiss");

        emilia = new Persona();
        emilia.setNombrePersona("Emilia Clarke");

        peterdinklage = new Persona();
        peterdinklage.setNombrePersona("Peter Dinklage");

        shaunlevy = new Persona();
        shaunlevy.setNombrePersona("Shawn Levy");

        winona = new Persona();
        winona.setNombrePersona("Winona Ryder");

        david = new Persona();
        david.setNombrePersona("David Harbour");

        millie = new Persona();
        millie.setNombrePersona("Millie Bobby Brown");

        personaRepository.saveAll(Arrays.asList(vince, bryan, aaron, davidBenioff, dbWeiss, emilia, peterdinklage, shaunlevy, winona, david, millie));
    }

    private void seedSeries() {
        // Breaking Bad
        Serie breakingBad = new Serie();
        breakingBad.setTitulo("Breaking Bad");
        breakingBad.setSinopsis("Un profesor de química se vuelve narco para pagar su tratamiento.");
        breakingBad.setTipo(TipoSerie.GOLD);
        breakingBad.setCreadores(Arrays.asList(vince));
        breakingBad.setActores(Arrays.asList(bryan, aaron));

        Temporada bb_t1 = new Temporada();
        bb_t1.setNumeroTemp(1);
        bb_t1.setTitulo("Piloto y comienzos");
        bb_t1.setSerie(breakingBad);

        Capitulo bb_c1 = new Capitulo();
        bb_c1.setNumeroCap(1);
        bb_c1.setTitulo("Piloto");
        bb_c1.setDescripcion("Walter descubre su cáncer y cocina por primera vez.");
        bb_c1.setTemporada(bb_t1);

        Capitulo bb_c2 = new Capitulo();
        bb_c2.setNumeroCap(2);
        bb_c2.setTitulo("Cat's in the Bag...");
        bb_c2.setDescripcion("Walter y Jesse deben limpiar su desastre.");
        bb_c2.setTemporada(bb_t1);

        bb_t1.setCapitulos(Arrays.asList(bb_c1, bb_c2));

        Temporada bb_t2 = new Temporada();
        bb_t2.setNumeroTemp(2);
        bb_t2.setTitulo("La conspiración");
        bb_t2.setSerie(breakingBad);

        Capitulo bb_t2_c1 = new Capitulo();
        bb_t2_c1.setNumeroCap(1);
        bb_t2_c1.setTitulo("Seven Thirty-Seven");
        bb_t2_c1.setDescripcion("Walter planea su primer crimen mayor.");
        bb_t2_c1.setTemporada(bb_t2);

        bb_t2.setCapitulos(Arrays.asList(bb_t2_c1));

        breakingBad.setTemporadas(Arrays.asList(bb_t1, bb_t2));
        serieRepository.save(breakingBad);

        // Game of Thrones
        Serie gameOfThrones = new Serie();
        gameOfThrones.setTitulo("Game of Thrones");
        gameOfThrones.setSinopsis("La lucha por el Trono de Hierro en un mundo medieval de fantasía.");
        gameOfThrones.setTipo(TipoSerie.GOLD);
        gameOfThrones.setCreadores(Arrays.asList(davidBenioff, dbWeiss));
        gameOfThrones.setActores(Arrays.asList(emilia, peterdinklage));

        Temporada got_t1 = new Temporada();
        got_t1.setNumeroTemp(1);
        got_t1.setTitulo("El Comienzo");
        got_t1.setSerie(gameOfThrones);

        Capitulo got_c1 = new Capitulo();
        got_c1.setNumeroCap(1);
        got_c1.setTitulo("Winter is Coming");
        got_c1.setDescripcion("Los Stark descubren una conspiración en Winterfell.");
        got_c1.setTemporada(got_t1);

        Capitulo got_c2 = new Capitulo();
        got_c2.setNumeroCap(2);
        got_c2.setTitulo("The Kingsroad");
        got_c2.setDescripcion("El viaje comienza hacia King's Landing.");
        got_c2.setTemporada(got_t1);

        got_t1.setCapitulos(Arrays.asList(got_c1, got_c2));
        gameOfThrones.setTemporadas(Arrays.asList(got_t1));
        serieRepository.save(gameOfThrones);

        // Stranger Things
        Serie strangerThings = new Serie();
        strangerThings.setTitulo("Stranger Things");
        strangerThings.setSinopsis("Un grupo de adolescentes enfrenta fenómenos sobrenaturales en los años 80.");
        strangerThings.setTipo(TipoSerie.GOLD);
        strangerThings.setCreadores(Arrays.asList(shaunlevy));
        strangerThings.setActores(Arrays.asList(winona, david, millie));

        Temporada st_t1 = new Temporada();
        st_t1.setNumeroTemp(1);
        st_t1.setTitulo("Desaparición");
        st_t1.setSerie(strangerThings);

        Capitulo st_c1 = new Capitulo();
        st_c1.setNumeroCap(1);
        st_c1.setTitulo("Chapter One: The Vanishing of Will Byers");
        st_c1.setDescripcion("Un niño desaparece misteriosamente en Hawkins.");
        st_c1.setTemporada(st_t1);

        Capitulo st_c2 = new Capitulo();
        st_c2.setNumeroCap(2);
        st_c2.setTitulo("Chapter Two: The Weirdo on Maple Street");
        st_c2.setDescripcion("Los amigos descubren a una niña extraña.");
        st_c2.setTemporada(st_t1);

        Capitulo st_c3 = new Capitulo();
        st_c3.setNumeroCap(3);
        st_c3.setTitulo("Chapter Three: Holly, Jolly");
        st_c3.setDescripcion("La tensión aumenta en Hawkins.");
        st_c3.setTemporada(st_t1);

        st_t1.setCapitulos(Arrays.asList(st_c1, st_c2, st_c3));
        strangerThings.setTemporadas(Arrays.asList(st_t1));
        serieRepository.save(strangerThings);
    }

    private void seedUsuarios() {
        SuscripcionFija subFija1 = new SuscripcionFija();
        Usuario paco = new Usuario();
        paco.setNombreUsuario("Paco");
        paco.setContrasena("1234");
        paco.setCuentaBancaria("ES111122223333");
        paco.setSuscripcion(subFija1);

        SuscripcionBajoDemanda subVariable1 = new SuscripcionBajoDemanda();
        Usuario lola = new Usuario();
        lola.setNombreUsuario("Lola");
        lola.setContrasena("5678");
        lola.setCuentaBancaria("ES999988887777");
        lola.setSuscripcion(subVariable1);

        SuscripcionFija subFija2 = new SuscripcionFija();
        Usuario carlos = new Usuario();
        carlos.setNombreUsuario("Carlos");
        carlos.setContrasena("abcd");
        carlos.setCuentaBancaria("ES444455556666");
        carlos.setSuscripcion(subFija2);

        SuscripcionBajoDemanda subVariable2 = new SuscripcionBajoDemanda();
        Usuario maria = new Usuario();
        maria.setNombreUsuario("Maria");
        maria.setContrasena("xyz789");
        maria.setCuentaBancaria("ES777788889999");
        maria.setSuscripcion(subVariable2);

        SuscripcionFija subFija3 = new SuscripcionFija();
        Usuario sergio = new Usuario();
        sergio.setNombreUsuario("Sergio");
        sergio.setContrasena("pass123");
        sergio.setCuentaBancaria("ES555566667777");
        sergio.setSuscripcion(subFija3);

        SuscripcionBajoDemanda subVariable3 = new SuscripcionBajoDemanda();
        Usuario ana = new Usuario();
        ana.setNombreUsuario("Ana");
        ana.setContrasena("securepwd");
        ana.setCuentaBancaria("ES222233334444");
        ana.setSuscripcion(subVariable3);

        usuarioRepository.saveAll(Arrays.asList(paco, lola, carlos, maria, sergio, ana));
    }
}