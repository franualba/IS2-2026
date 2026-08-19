package com.todocode.jpa.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long id;

    @Column(name = "descripcion", length = 200, nullable = false)
    private String descripcion;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "monto", precision = 10, scale = 2)
    private Double monto;

    // Relación MUCHOS A UNO: Muchos pedidos pertenecen a UN cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente_fk", referencedColumnName = "id_cliente")
    private Cliente cliente;

    public Pedido() {
    }

    public Pedido(String descripcion, LocalDate fecha, Double monto) {
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.monto = monto;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", monto=" + monto +
                '}';
    }
}