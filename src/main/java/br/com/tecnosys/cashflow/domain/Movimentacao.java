package br.com.tecnosys.cashflow.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor")
    private BigDecimal valor;

    @Column(name = "tp_mov_id")
    private Long tipoMovId;

    @ManyToOne
    @JoinColumn(name = "tp_mov_id", insertable = false, updatable = false)
    private TipoMov tipoMov;

    @Column(name = "data_movimento")
    private LocalDateTime dataMovimento;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "sessao_caixa_id")
    private Long sessaoCaixaId;

    @ManyToOne
    @JoinColumn(name = "sessao_caixa_id", insertable = false, updatable = false)
    private SessaoCaixa sessaoCaixa;
}