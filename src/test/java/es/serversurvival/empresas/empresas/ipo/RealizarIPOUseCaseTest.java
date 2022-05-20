package es.serversurvival.empresas.empresas.ipo;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.IllegalState;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.empresas.accionistasempresasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.TipoAccionista;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static es.serversurvival.MockitoArgEqualsMatcher.*;
import static es.serversurvival.empresas.empresas.EmpresasTestMother.createEmpresa;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class RealizarIPOUseCaseTest {
     @Mock private EmpresasService empresasService;
     @Mock private OfertasAccionesServerService ofertasAccionesServerService;
     @Mock private AccionistasServerService accionistasEmpresasServerService;
     @Mock private EventBus eventBus;
     private RealizarIPOUseCase useCase;

     @BeforeEach
     public void init(){
          MockitoAnnotations.initMocks(this);
          this.useCase = new RealizarIPOUseCase(
                  this.empresasService,
                  this.ofertasAccionesServerService,
                  this.accionistasEmpresasServerService,
                  this.eventBus
          );
     }

     @Test
     public void makeTheIPO(){
          Empresa empresaToIPO = createEmpresa("empresa", "jaime");
          when(this.empresasService.getByNombre("empresa")).thenReturn(empresaToIPO);

          this.useCase.makeIPO("jaime", new IPOCommand("empresa", 5, 2, 1));

          verify(this.accionistasEmpresasServerService, times(1)).save(
                  argThat(of("jaime")), argThat(of(TipoAccionista.JUGADOR)), argThat(of(empresaToIPO.getNombre())), anyInt(), anyDouble()
          );

          verify(this.empresasService, times(1)).save(argThat(of(
                  empresaToIPO.setCotizadaToTrue().withAccionesTotales(5)
          )));

          verify(this.eventBus, times(1)).publish(argThat(of(
                  IPORealizada.of("empresa", 1, 5, 2)
          )));
     }

     @Test
     public void notAlreadyCotizada(){
          when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "jaime").setCotizadaToTrue());

          assertThatCode(() -> this.useCase.makeIPO("jaime", new IPOCommand("empresa", 5, 2, 1)))
                  .isInstanceOf(IllegalState.class);
     }

     @Test
     public void notTheOwner(){
          when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "otro"));

          assertThatCode(() -> this.useCase.makeIPO("jaime", new IPOCommand("empresa", 5, 2, 1)))
                  .isInstanceOf(NotTheOwner.class);
     }

     @Test
     public void empresaExists(){
          when(this.empresasService.getByNombre("empresa")).thenThrow(ResourceNotFound.class);

          assertThatCode(() -> this.useCase.makeIPO("alkjs", new IPOCommand("empresa", 5, 2, 1)))
                  .isInstanceOf(ResourceNotFound.class);
     }

     @Test
     public void accionesOwnerNotBiggerThanAccionesTotales(){
          assertThatCode(() -> this.useCase.makeIPO("alkjs", new IPOCommand("empresa", 1, 2, 1)))
                  .isInstanceOf(IllegalQuantity.class);
     }

     @Test
     public void accionesTotalesMinValue(){
          assertThatCode(() -> this.useCase.makeIPO("alkjs", new IPOCommand("empresa", 1, 1, 1)))
                  .isInstanceOf(IllegalQuantity.class);
     }

     @Test
     public void correctFormatAccionesTotalesOwnerPrecioPorAccion(){
          assertThatCode(() -> this.useCase.makeIPO("alkjs", new IPOCommand("empresa", -1, 1, 1)))
                  .isInstanceOf(IllegalQuantity.class);
          assertThatCode(() -> this.useCase.makeIPO("alkjs", new IPOCommand("empresa", 1, -1, 1)))
                  .isInstanceOf(IllegalQuantity.class);
          assertThatCode(() -> this.useCase.makeIPO("alkjs", new IPOCommand("empresa", 1, 1, -1)))
                  .isInstanceOf(IllegalQuantity.class);
          assertThatCode(() -> this.useCase.makeIPO("alkjs", new IPOCommand("empresa", 0, 1, 1)))
                  .isInstanceOf(IllegalQuantity.class);
          assertThatCode(() -> this.useCase.makeIPO("alkjs", new IPOCommand("empresa", 1, 0, 1)))
                  .isInstanceOf(IllegalQuantity.class);
          assertThatCode(() -> this.useCase.makeIPO("alkjs", new IPOCommand("empresa", 1, 1, 0)))
                  .isInstanceOf(IllegalQuantity.class);
     }
}
