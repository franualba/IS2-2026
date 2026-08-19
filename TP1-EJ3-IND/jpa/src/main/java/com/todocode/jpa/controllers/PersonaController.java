package com.todocode.jpa.controllers;

import com.todocode.jpa.entities.Persona;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class PersonaController {

    private EntityManagerFactory emf;
    private EntityManager em;

    public PersonaController() {
        this.emf = Persistence.createEntityManagerFactory("jpa-todocode-pu");
        this.em = emf.createEntityManager();
    }

    // CREATE - Crear una nueva persona
    public void crearPersona(Persona persona) {
        try {
            em.getTransaction().begin();
            em.persist(persona);
            em.getTransaction().commit();
            System.out.println("✅ Persona creada con éxito: " + persona);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Error al crear persona: " + e.getMessage());
        }
    }

    // FIND - Buscar persona por ID
    public Persona buscarPersona(Long id) {
        return em.find(Persona.class, id);
    }

    // FIND ALL - Listar todas las personas
    public List<Persona> listarPersonas() {
        String jpql = "SELECT p FROM Persona p";
        return em.createQuery(jpql, Persona.class).getResultList();
    }

    // EDIT - Modificar una persona existente
    public void editarPersona(Persona persona) {
        try {
            em.getTransaction().begin();
            em.merge(persona);
            em.getTransaction().commit();
            System.out.println("✅ Persona actualizada: " + persona);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Error al editar persona: " + e.getMessage());
        }
    }

    // DELETE - Eliminar una persona
    public void eliminarPersona(Long id) {
        Persona persona = buscarPersona(id);
        if (persona != null) {
            try {
                em.getTransaction().begin();
                em.remove(persona);
                em.getTransaction().commit();
                System.out.println("✅ Persona eliminada con ID: " + id);
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.err.println("❌ Error al eliminar persona: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ No se encontró la persona con ID: " + id);
        }
    }

    public void cerrar() {
        em.close();
        emf.close();
    }
}