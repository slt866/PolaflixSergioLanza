package es.unican.sergio.polaflix;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import es.unican.sergio.polaflix.model.Capitulo;
import es.unican.sergio.polaflix.model.EntradaFactura;
import es.unican.sergio.polaflix.model.Factura;
import es.unican.sergio.polaflix.model.Persona;
import es.unican.sergio.polaflix.model.PlanSuscripcion;
import es.unican.sergio.polaflix.model.Serie;
import es.unican.sergio.polaflix.model.SuscripcionBajoDemanda;
import es.unican.sergio.polaflix.model.SuscripcionFija;
import es.unican.sergio.polaflix.model.Temporada;
import es.unican.sergio.polaflix.model.TipoSerie;
import es.unican.sergio.polaflix.model.Usuario;
import es.unican.sergio.polaflix.model.Visualizacion;
import es.unican.sergio.polaflix.repository.FacturaRepository;
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

    @Autowired
    protected FacturaRepository facturaRepository;

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
        System.out.println("Cargando catálogo y usuarios en Polaflix...");
        System.out.println("=========================================================");

        seedPersonas();
        seedSeries();
        seedUsuarios();

        System.out.println("¡Datos cargados! Catálogo y usuarios listos en la BD.");
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
        Serie breakingBad = new Serie("Breaking Bad", "Un profesor de química se vuelve narco para pagar su tratamiento.", TipoSerie.GOLD);
        breakingBad.setCreadores(Arrays.asList(vince));
        breakingBad.setActores(Arrays.asList(bryan, aaron));

        Temporada bb_t1 = new Temporada(1, "Piloto y comienzos", breakingBad);

        Capitulo bb_c1 = new Capitulo(1, "Piloto", "Walter descubre su cáncer y cocina por primera vez.", bb_t1);

        Capitulo bb_c2 = new Capitulo(2, "Cat's in the Bag...", "Walter y Jesse deben limpiar su desastre.", bb_t1);

        bb_t1.setCapitulos(Arrays.asList(bb_c1, bb_c2));

        Temporada bb_t2 = new Temporada(2, "La conspiración", breakingBad);

        Capitulo bb_t2_c1 = new Capitulo(1, "Seven Thirty-Seven", "Walter planea su primer crimen mayor.", bb_t2);

        bb_t2.setCapitulos(Arrays.asList(bb_t2_c1));

        breakingBad.setTemporadas(Arrays.asList(bb_t1, bb_t2));
        serieRepository.save(breakingBad);

        // Game of Thrones
        Serie gameOfThrones = new Serie("Game of Thrones", "La lucha por el Trono de Hierro en un mundo medieval de fantasía.", TipoSerie.GOLD);
        gameOfThrones.setCreadores(Arrays.asList(davidBenioff, dbWeiss));
        gameOfThrones.setActores(Arrays.asList(emilia, peterdinklage));

        Temporada got_t1 = new Temporada(1, "El Comienzo", gameOfThrones);

        Capitulo got_c1 = new Capitulo(1, "Winter is Coming", "Los Stark descubren una conspiración en Winterfell.", got_t1);

        Capitulo got_c2 = new Capitulo(2, "The Kingsroad", "El viaje comienza hacia King's Landing.", got_t1);

        got_t1.setCapitulos(Arrays.asList(got_c1, got_c2));
        gameOfThrones.setTemporadas(Arrays.asList(got_t1));
        serieRepository.save(gameOfThrones);

        // Stranger Things
        Serie strangerThings = new Serie("Stranger Things", "Un grupo de adolescentes enfrenta fenómenos sobrenaturales en los años 80.", TipoSerie.GOLD);
        strangerThings.setCreadores(Arrays.asList(shaunlevy));
        strangerThings.setActores(Arrays.asList(winona, david, millie));

        Temporada st_t1 = new Temporada(1, "Desaparición", strangerThings);

        Capitulo st_c1 = new Capitulo(1, "Chapter One: The Vanishing of Will Byers", "Un niño desaparece misteriosamente en Hawkins.", st_t1);

        Capitulo st_c2 = new Capitulo(2, "Chapter Two: The Weirdo on Maple Street", "Los amigos descubren a una niña extraña.", st_t1);

        Capitulo st_c3 = new Capitulo(3, "Chapter Three: Holly, Jolly", "La tensión aumenta en Hawkins.", st_t1);

        st_t1.setCapitulos(Arrays.asList(st_c1, st_c2, st_c3));
        strangerThings.setTemporadas(Arrays.asList(st_t1));
        serieRepository.save(strangerThings);
    }

    private void seedUsuarios() {
        SuscripcionFija subFija1 = new SuscripcionFija();
        Usuario paco = new Usuario("Paco", "1234", "ES111122223333", subFija1);

        SuscripcionBajoDemanda subVariable1 = new SuscripcionBajoDemanda();
        Usuario lola = new Usuario("Lola", "5678", "ES999988887777", subVariable1);

        SuscripcionFija subFija2 = new SuscripcionFija();
        Usuario carlos = new Usuario("Carlos", "abcd", "ES444455556666", subFija2);

        SuscripcionBajoDemanda subVariable2 = new SuscripcionBajoDemanda();
        Usuario maria = new Usuario("Maria", "xyz789", "ES777788889999", subVariable2);

        SuscripcionFija subFija3 = new SuscripcionFija();
        Usuario sergio = new Usuario("Sergio", "pass123", "ES555566667777", subFija3);

        SuscripcionBajoDemanda subVariable3 = new SuscripcionBajoDemanda();
        Usuario ana = new Usuario("Ana", "securepwd", "ES222233334444", subVariable3);

        usuarioRepository.saveAll(Arrays.asList(paco, lola, carlos, maria, sergio, ana));

        // Crear facturas de ejemplo
        Factura facturaPaco = new Factura();
        facturaPaco.setMes(10);
        facturaPaco.setAno(2023);
        facturaPaco.setUsuario(paco);
        EntradaFactura entradaPaco1 = new EntradaFactura();
        entradaPaco1.setFecha(LocalDateTime.of(2023, 10, 1, 10, 0));
        entradaPaco1.setCargo(1.5);
        entradaPaco1.setNumeroTemp(1);
        entradaPaco1.setNumeroCap(1);
        entradaPaco1.setFactura(facturaPaco);
        facturaPaco.setEntradas(Arrays.asList(entradaPaco1));
        facturaPaco.calcularImporteTotal();
        facturaRepository.save(facturaPaco);

        Factura facturaLola = new Factura();
        facturaLola.setMes(10);
        facturaLola.setAno(2023);
        facturaLola.setUsuario(lola);
        EntradaFactura entradaLola1 = new EntradaFactura();
        entradaLola1.setFecha(LocalDateTime.of(2023, 10, 5, 14, 30));
        entradaLola1.setCargo(1.5);
        entradaLola1.setNumeroTemp(1);
        entradaLola1.setNumeroCap(1);
        entradaLola1.setFactura(facturaLola);
        EntradaFactura entradaLola2 = new EntradaFactura();
        entradaLola2.setFecha(LocalDateTime.of(2023, 10, 10, 16, 0));
        entradaLola2.setCargo(1.5);
        entradaLola2.setNumeroTemp(1);
        entradaLola2.setNumeroCap(2);
        entradaLola2.setFactura(facturaLola);
        facturaLola.setEntradas(Arrays.asList(entradaLola1, entradaLola2));
        facturaLola.calcularImporteTotal();
        facturaRepository.save(facturaLola);
    }
}