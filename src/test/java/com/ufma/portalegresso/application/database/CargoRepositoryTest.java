package com.ufma.portalegresso.application.database;

import com.ufma.portalegresso.application.domain.Cargo;
import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.domain.TipoAreaTrabalho;
import com.ufma.portalegresso.application.out.CargoJpaRepository;
import com.ufma.portalegresso.application.out.EgressoJpaRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CargoRepositoryTest {
    @Autowired
    private CargoJpaRepository cargoJpaRepository;
    @Autowired
    private EgressoJpaRepository egressoJpaRepository;
    private static Egresso egressoBase;
    @BeforeAll
    public static void setUp(@Autowired EgressoJpaRepository egressoJpaRepository){
        Egresso egresso = Egresso.builder()
                .nome("egresso para teste")
                .email("egressoparateste@gmail.com")
                .descricao("descricao egresso para teste")
                .curriculo("curriculo egresso para teste")
                .instagram("egresso_teste")
                .linkedin("https://linkedin/egressoTeste")
                .foto("foto codificada")
                .build();
        egressoBase = egressoJpaRepository.save(egresso);
    }
    @AfterAll
    public static void cleanUp(@Autowired EgressoJpaRepository egressoJpaRepository){
        egressoJpaRepository.deleteById(egressoBase.getIdEgresso());
    }
    @Test
    @Transactional
    public void deveSalvarCargoFluxoPadrao(){
        Cargo cargo = Cargo.builder()
                .egresso(egressoBase)
                .anoInicio(2022)
                .anoFim(2023)
                .descricao("descricao teste cargo")
                .local("local teste cargo")
                .tipoAreaTrabalho(TipoAreaTrabalho.FINANCEIRO)
                .build();

        Cargo cargoSalvo = cargoJpaRepository.save(cargo);

        assertNotNull(cargoSalvo);
        assertEquals(cargo.getEgresso(), cargoSalvo.getEgresso());
        assertEquals(cargo.getLocal(), cargoSalvo.getLocal());
        assertEquals(cargo.getDescricao(), cargoSalvo.getDescricao());
        assertEquals(cargo.getAnoInicio(), cargoSalvo.getAnoInicio());
        assertEquals(cargo.getAnoFim(), cargoSalvo.getAnoFim());
    }
    @Test
    @Transactional
    public void deveVerificarBuscarCargoPorIdCargo(){
        Cargo cargoCriado1 = cargoJpaRepository.save(Cargo.builder()
                .egresso(egressoBase)
                .anoInicio(2022)
                .anoFim(2023)
                .descricao("descricao teste cargo")
                .local("local teste cargo")
                .tipoAreaTrabalho(TipoAreaTrabalho.TECNOLOGIA)
                .build());
        Cargo cargoCriado2 = cargoJpaRepository.save(Cargo.builder()
                .egresso(egressoBase)
                .anoInicio(2021)
                .anoFim(2024)
                .descricao("descricao teste cargo 2")
                .local("local teste cargo 2")
                .tipoAreaTrabalho(TipoAreaTrabalho.FINANCEIRO)
                .build());

        Optional<Cargo> resposta1 = cargoJpaRepository.findById(cargoCriado1.getIdCargo());
        Optional<Cargo> resposta2 = cargoJpaRepository.findById(cargoCriado2.getIdCargo());

        // resultado1
        assertTrue(resposta1.isPresent());
        Cargo cargoEncontrado1 = resposta1.get();
        assertEquals(cargoCriado1.getIdCargo(), cargoEncontrado1.getIdCargo());
        assertEquals(cargoCriado1.getDescricao(), cargoEncontrado1.getDescricao());

        //resultado 2
        assertTrue(resposta2.isPresent());
        Cargo cargoEncontrado2 = resposta2.get();
        assertEquals(cargoCriado2.getIdCargo(), cargoEncontrado2.getIdCargo());
        assertEquals(cargoCriado2.getDescricao(), cargoEncontrado2.getDescricao());
    }

    @Test
    public void naoDeveSalvarCargoSemEgressoOuDescricaoOuLocalOuAnoInicioOuAreaTrabalho(){
        Cargo cargo = Cargo.builder()
                .local("localteste")
                .descricao("descricao teste")
                .anoInicio(2021)
                .anoFim(2024)
                .tipoAreaTrabalho(TipoAreaTrabalho.FINANCEIRO)
                .build();

        DataIntegrityViolationException exceptionSemIdEgresso = assertThrows(DataIntegrityViolationException.class, () -> cargoJpaRepository.save(cargo));
        //pegar o campo que deu problema pra ver se foi ele que deu errado mesmo
        assertEquals("id_egresso", exceptionSemIdEgresso.getCause().getCause().getLocalizedMessage().split("\"")[1].toLowerCase());

        cargo.setEgresso(egressoBase);
        cargo.setLocal(null);
        DataIntegrityViolationException exceptionSemLocal = assertThrows(DataIntegrityViolationException.class, () -> cargoJpaRepository.save(cargo));
        assertEquals("local", exceptionSemLocal.getCause().getCause().getLocalizedMessage().split("\"")[1].toLowerCase());

        cargo.setLocal("local teste do teste do teste");
        cargo.setDescricao(null);
        DataIntegrityViolationException exceptionSemDescricao = assertThrows(DataIntegrityViolationException.class, () -> cargoJpaRepository.save(cargo));
        assertEquals("descricao", exceptionSemDescricao.getCause().getCause().getLocalizedMessage().split("\"")[1].toLowerCase());


        cargo.setDescricao("descricao teste");
        cargo.setAnoInicio(null);
        DataIntegrityViolationException exceptionSemAnoInicio = assertThrows(DataIntegrityViolationException.class, () -> cargoJpaRepository.save(cargo));
        assertEquals("ano_inicio", exceptionSemAnoInicio.getCause().getCause().getLocalizedMessage().split("\"")[1].toLowerCase());

        cargo.setAnoInicio(2021);
        cargo.setTipoAreaTrabalho(null);
        DataIntegrityViolationException exceptionSemAreaTrabalho = assertThrows(DataIntegrityViolationException.class, () -> cargoJpaRepository.save(cargo));
        assertEquals("tipo_area_trabalho", exceptionSemAreaTrabalho.getCause().getCause().getLocalizedMessage().split("\"")[1].toLowerCase());

        //ter certza que consigo salvar com os dados necessarios colocados de volta
        cargo.setTipoAreaTrabalho(TipoAreaTrabalho.FINANCEIRO);
        Cargo cargoSalvo = assertDoesNotThrow(() -> cargoJpaRepository.save(cargo));
        cargoJpaRepository.deleteById(cargoSalvo.getIdCargo());
    }

    @Test
    @Transactional
    public void deveVerificarBuscarTodosCargos(){
        Cargo cargo1 = Cargo.builder()
                .egresso(egressoBase)
                .descricao("descricao teste 1")
                .local("local teste 1")
                .anoInicio(2015)
                .tipoAreaTrabalho(TipoAreaTrabalho.FINANCEIRO)
                .build();
        Cargo cargo2 = Cargo.builder()
                .egresso(egressoBase)
                .descricao("descricao teste 2")
                .local("local teste 2")
                .anoInicio(2015)
                .anoFim(2019)
                .tipoAreaTrabalho(TipoAreaTrabalho.RH)
                .build();
        Cargo cargo3 = Cargo.builder()
                .egresso(egressoBase)
                .descricao("descricao teste 3")
                .local("local teste 3")
                .anoInicio(2020)
                .anoFim(2030)
                .tipoAreaTrabalho(TipoAreaTrabalho.TECNOLOGIA)
                .build();
        cargoJpaRepository.save(cargo1);
        cargoJpaRepository.save(cargo2);
        cargoJpaRepository.save(cargo3);

        List<Cargo> resposta = cargoJpaRepository.findAll();

        assertFalse(resposta.isEmpty());
        assertEquals(3, resposta.size());
        assertTrue(resposta.contains(cargo1));
    }
    @Test
    @Transactional
    public void deveAtualizarCargoFluxoPadrao(){
        Cargo cargoCriado = cargoJpaRepository.save(Cargo.builder()
                .egresso(egressoBase)
                .anoInicio(2022)
                .anoFim(2023)
                .descricao("descricao teste cargo")
                .local("local teste cargo")
                .tipoAreaTrabalho(TipoAreaTrabalho.FINANCEIRO)
                .build());

        Cargo cargoAtualizado = Cargo.builder()
                .idCargo(cargoCriado.getIdCargo())
                .egresso(egressoBase)
                .anoInicio(2021)
                .anoFim(2024)
                .descricao("descricao teste cargo atualizado")
                .local("local teste cargo atualizado")
                .tipoAreaTrabalho(TipoAreaTrabalho.EDUCACAO)
                .build();

        cargoJpaRepository.save(cargoAtualizado);

        Optional<Cargo> resultado = cargoJpaRepository.findById(cargoAtualizado.getIdCargo());

        assertTrue(resultado.isPresent());
        Cargo cargoEncontrado = resultado.get();
        assertEquals(cargoAtualizado.getIdCargo(), cargoEncontrado.getIdCargo());
        assertEquals(cargoAtualizado.getEgresso().getIdEgresso(), cargoEncontrado.getEgresso().getIdEgresso());
        assertEquals(cargoAtualizado.getAnoInicio(), cargoEncontrado.getAnoInicio());
        assertEquals(cargoAtualizado.getAnoFim(), cargoEncontrado.getAnoFim());
        assertEquals(cargoAtualizado.getDescricao(), cargoEncontrado.getDescricao());
        assertEquals(cargoAtualizado.getLocal(), cargoEncontrado.getLocal());
        assertEquals(cargoAtualizado.getTipoAreaTrabalho(), cargoEncontrado.getTipoAreaTrabalho());
    }

    @Test
    @Transactional
    public void deveDeletarCargoPorIdFluxoPadrao(){
        Cargo cargoCriado = cargoJpaRepository.save(Cargo.builder()
                .egresso(egressoBase)
                .anoInicio(2022)
                .anoFim(2023)
                .descricao("descricao teste cargo")
                .local("local teste cargo")
                .tipoAreaTrabalho(TipoAreaTrabalho.FINANCEIRO)
                .build());

        cargoJpaRepository.deleteById(cargoCriado.getIdCargo());

        assertThrows(NoSuchElementException.class, () -> cargoJpaRepository.findById(cargoCriado.getIdCargo()).get());
    }

    @Test
    @Transactional
    public void deveTestarEgressoPoderTerMaisDeUmCargo(){
        Cargo cargoSalvo = cargoJpaRepository.save(Cargo.builder().egresso(egressoBase).anoInicio(2022).anoFim(2023).descricao("descricao teste cargo").local("local teste cargo").tipoAreaTrabalho(TipoAreaTrabalho.FINANCEIRO).build());
        Cargo cargoSalvo2 = cargoJpaRepository.save(Cargo.builder().egresso(egressoBase).anoInicio(2021).anoFim(2024).descricao("descricao teste cargo 2").local("local teste cargo 2").tipoAreaTrabalho(TipoAreaTrabalho.RH).build());

        List<Cargo> cargos = egressoJpaRepository.findById(egressoBase.getIdEgresso()).get().getCargos();

        assertFalse(cargos.isEmpty());
        assertEquals(2, cargos.size());
        assertEquals(cargoSalvo.getIdCargo(), cargos.get(0).getIdCargo());
        assertEquals(cargoSalvo2.getIdCargo(), cargos.get(1).getIdCargo());
    }
}
