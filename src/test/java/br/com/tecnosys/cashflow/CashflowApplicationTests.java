package br.com.tecnosys.cashflow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CashflowApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Deve carregar o contexto da aplicação com sucesso")
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }

    @Test
    @DisplayName("Deve executar método main sem erros")
    void main() {
        CashflowApplication.main(new String[]{});
    }

    @Test
    @DisplayName("Deve verificar se as anotações estão presentes")
    void checkAnnotations() {
        assertThat(CashflowApplication.class)
                .hasAnnotation(SpringBootApplication.class)
                .hasAnnotation(EnableFeignClients.class);
    }

}
