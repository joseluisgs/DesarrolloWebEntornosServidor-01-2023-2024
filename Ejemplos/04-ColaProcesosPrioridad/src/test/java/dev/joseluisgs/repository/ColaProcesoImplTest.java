package dev.joseluisgs.repository;

import dev.joseluisgs.model.Proceso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/*La función assertAll en JUnit permite agrupar varias aserciones en un solo bloque de pruebas.
Si alguna de las aserciones falla, el bloque completo falla, pero todas las aserciones se ejecutan
independientemente de si las anteriores pasaron o no.*/

class ColaProcesoImplTest {

    private ColaProcesoImpl cola;

    @BeforeEach
    public void setUp() {
        cola = new ColaProcesoImpl();
    }

    @Test
    public void testEncolar() {
        Proceso p = new Proceso("Proceso1", 5, 3);
        cola.encolar(p);
        assertAll(
                () -> assertFalse(cola.esVacia()),
                () -> assertEquals(1, cola.size())
        );
    }

    @Test
    public void testDesencolar() {
        Proceso p1 = new Proceso("Proceso1", 5, 3);
        Proceso p2 = new Proceso("Proceso2", 4, 1);
        cola.encolar(p1);
        cola.encolar(p2);
        Optional<Proceso> desencolado = cola.desencolar();
        assertAll(
                () -> assertTrue(desencolado.isPresent()),
                () -> assertEquals(p2, desencolado.get()),
                () -> assertEquals(1, cola.size())
        );
    }

    @Test
    public void testDesencolarConColaVacia() {
        Optional<Proceso> desencolado = cola.desencolar();
        assertAll(
                () -> assertFalse(desencolado.isPresent())
        );
    }

    @Test
    public void testFrente() {
        Proceso p1 = new Proceso("Proceso1", 5, 3);
        Proceso p2 = new Proceso("Proceso2", 4, 1);
        cola.encolar(p1);
        cola.encolar(p2);
        Optional<Proceso> frente = cola.frente();
        assertAll(
                () -> assertTrue(frente.isPresent()),
                () -> assertEquals(p2, frente.get())
        );
    }

    @Test
    public void testFrenteConColaVacia() {
        Optional<Proceso> frente = cola.frente();
        assertAll(
                () -> assertFalse(frente.isPresent())
        );
    }

    @Test
    public void testEsVacia() {
        Proceso p = new Proceso("Proceso1", 5, 3);
        assertAll(
                () -> assertTrue(cola.esVacia()),
                () -> {
                    cola.encolar(p);
                    assertFalse(cola.esVacia());
                }
        );
    }

    @Test
    public void testSize() {
        Proceso p = new Proceso("Proceso1", 5, 3);
        assertAll(
                () -> assertEquals(0, cola.size()),
                () -> {
                    cola.encolar(p);
                    assertEquals(1, cola.size());
                }
        );
    }

    @Test
    public void testVaciar() {
        Proceso p = new Proceso("Proceso1", 5, 3);
        assertAll(
                () -> {
                    cola.encolar(p);
                    assertFalse(cola.esVacia());
                },
                () -> {
                    cola.vaciar();
                    assertTrue(cola.esVacia());
                }
        );
    }
}