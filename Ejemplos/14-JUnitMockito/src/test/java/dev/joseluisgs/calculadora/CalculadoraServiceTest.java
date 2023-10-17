package dev.joseluisgs.calculadora;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculadoraServiceTest {

    @Mock
    private Calculadora calculadora;

    @InjectMocks
    private CalculadoraService calculadoraService;

    @Test
    void sumar() {
        // Arrange
        // Cuando llamemos a calculadora....
        when(calculadora.sumar(2, 3)).thenReturn(5);

        // Act
        int resultado = calculadoraService.sumar(2, 3);

        // Assert
        assertEquals(resultado, 5);

        // Verificar que que ha llamado a...
        verify(calculadora, times(1)).sumar(2, 3);
    }

    @Test
    void restar() {
        // Arrange
        // Cuando llamemos a calculadora....
        when(calculadora.restar(2, 3)).thenReturn(1);

        // Act
        int resultado = calculadoraService.restar(2, 3);

        // Assert
        assertEquals(resultado, 1);

        // Verificar que que ha llamado a...
        verify(calculadora, times(1)).restar(2, 3);
    }

    @Test
    void multiplicar() {
        // Arrange
        // Cuando llamemos a calculadora.... (podemos usar comodines)
        when(calculadora.multiplicar(anyInt(), anyInt())).thenReturn(6);

        // Act
        int resultado = calculadoraService.multiplicar(2, 3);

        // Assert
        assertEquals(resultado, 6);

        // Verificar que que ha llamado a...
        verify(calculadora, times(1)).multiplicar(anyInt(), anyInt());
    }

    @Test
    void dividir() throws OperacionNoValidaException {
        // Arrange
        // Cuando llamemos a calculadora....
        when(calculadora.dividir(6, 3)).thenReturn(2);

        // Act
        int resultado = calculadoraService.dividir(6, 3);

        // Assert
        assertEquals(resultado, 2);

        // Verificar que que ha llamado a...
        verify(calculadora, times(1)).dividir(6, 3);
    }

    @Test
    void dividirPorZero() throws OperacionNoValidaException {
        // Arrange
        // Cuando llamemos a calculadora....
        when(calculadora.dividir(6, 0)).thenThrow(new OperacionNoValidaException("No se puede dividir por cero"));

        // Act
        try {
            calculadoraService.dividir(6, 0);
        } catch (OperacionNoValidaException e) {
            // Assert
            assertEquals(e.getMessage(), "No se puede dividir por cero");
        }

        // Verificar que que ha llamado a...
        verify(calculadora, times(1)).dividir(6, 0);
    }
}