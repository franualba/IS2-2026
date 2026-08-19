package com.todocode.jpa.controllers;

import com.todocode.jpa.entities.Cliente;
import com.todocode.jpa.entities.Pedido;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class ClienteController {

    private EntityManagerFactory emf;
    private EntityManager em;

    public ClienteController() {
        this.emf = Persistence.createEntityManagerFactory("jpa-todocode-pu");
        this.em = emf.createEntityManager();
    }

    // CREATE - Crear cliente con pedidos (relación 1-N)
    public void crearClienteConPedidos(Cliente cliente) {
        try {
            em.getTransaction().begin();
            em.persist(cliente); // Gracias a CascadeType.ALL, persiste los pedidos también
            em.getTransaction().commit();
            System.out.println("✅ Cliente creado con " + cliente.getPedidos().size() + " pedidos: " + cliente.getNombre());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Error al crear cliente: " + e.getMessage());
        }
    }

    // FIND ALL
    public List<Cliente> listarClientes() {
        String jpql = "SELECT c FROM Cliente c";
        return em.createQuery(jpql, Cliente.class).getResultList();
    }

    // FIND by ID
    public Cliente buscarCliente(Long id) {
        return em.find(Cliente.class, id);
    }

    // EDIT - Agregar un pedido a un cliente existente
    public void agregarPedidoACliente(Long idCliente, Pedido pedido) {
        try {
            em.getTransaction().begin();
            Cliente cliente = em.find(Cliente.class, idCliente);
            if (cliente != null) {
                cliente.agregarPedido(pedido);
                em.merge(cliente);
            }
            em.getTransaction().commit();
            System.out.println("✅ Pedido agregado al cliente: " + cliente.getNombre());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Error al agregar pedido: " + e.getMessage());
        }
    }

    // DELETE
    public void eliminarCliente(Long id) {
        Cliente cliente = buscarCliente(id);
        if (cliente != null) {
            try {
                em.getTransaction().begin();
                em.remove(cliente); // También elimina los pedidos por orphanRemoval
                em.getTransaction().commit();
                System.out.println("✅ Cliente eliminado con ID: " + id);
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.err.println("❌ Error al eliminar cliente: " + e.getMessage());
            }
        }
    }

    public void cerrar() {
        em.close();
        emf.close();
    }
}