package com.todocode.jpa.controllers;

import com.todocode.jpa.entities.Auto;
import com.todocode.jpa.entities.Persona;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class AutoController {

    private EntityManagerFactory emf;
    private EntityManager em;

    public AutoController() {
        this.emf = Persistence.createEntityManagerFactory("jpa-todocode-pu");
        this.em = emf.createEntityManager();
    }

    // CREATE - Asociar un auto a una persona (relación 1-1)
    public void crearAutoConPersona(Auto auto, Persona persona) {
        try {
            em.getTransaction().begin();

            // Vincular ambas entidades
            auto.setPersona(persona);
            persona.setAuto(auto);

            // Si la persona ya existe en BD, la mergeamos; si no, la persistimos
            if (persona.getId() == null) {
                em.persist(persona);
            } else {
                em.merge(persona);
            }

            em.getTransaction().commit();
            System.out.println("✅ Auto creado y asociado a: " + persona.getNombre());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Error al crear auto: " + e.getMessage());
        }
    }

    // FIND ALL - Listar todos los autos
    public List<Auto> listarAutos() {
        String jpql = "SELECT a FROM Auto a";
        return em.createQuery(jpql, Auto.class).getResultList();
    }

    // FIND - Buscar auto por ID
    public Auto buscarAuto(Long id) {
        return em.find(Auto.class, id);
    }

    // DELETE
    public void eliminarAuto(Long id) {
        Auto auto = buscarAuto(id);
        if (auto != null) {
            try {
                em.getTransaction().begin();
                em.remove(auto);
                em.getTransaction().commit();
                System.out.println("✅ Auto eliminado con ID: " + id);
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.err.println("❌ Error al eliminar auto: " + e.getMessage());
            }
        }
    }

    public void cerrar() {
        em.close();
        emf.close();
    }
}