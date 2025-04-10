package br.com.tecnosys.cashflow.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();

    @Column(name = "create_by")
    private String createBy = "dado fixo";

    @Column(name = "alter_at")
    private LocalDateTime alterAt;

    @Column(name = "alter_by")
    private String alterBy;

    @Version
    private Integer version;
}