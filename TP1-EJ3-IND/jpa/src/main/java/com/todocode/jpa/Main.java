package com.todocode.jpa;

import com.todocode.jpa.controllers.PersonaController;
import com.todocode.jpa.controllers.AutoController;
import com.todocode.jpa.controllers.ClienteController;
import com.todocode.jpa.entities.Auto;
import com.todocode.jpa.entities.Cliente;
import com.todocode.jpa.entities.Pedido;
import com.todocode.jpa.entities.Persona;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================================");
        System.out.println("   🚀 MINICURSO JPA - TodoCode (IntelliJ IDEA)   ");
        System.out.println("=================================================\n");

        // ============================================
        // PARTE 1: CRUD de Persona y Auto (Relación 1-1)
        // ============================================
        System.out.println("📌 PARTE 1: Persona y Auto (Relación Uno a Uno)\n");

        PersonaController personaCtrl = new PersonaController();
        AutoController autoCtrl = new AutoController();

        // 1. CREAR (CREATE)
        System.out.println("--- CREAR PERSONA ---");
        Persona persona1 = new Persona("Juan", "Pérez", "12345678");
        Persona persona2 = new Persona("María", "López", "87654321");
        personaCtrl.crearPersona(persona1);
        personaCtrl.crearPersona(persona2);

        // 2. ASOCIAR AUTO (Relación 1-1)
        System.out.println("\n--- ASOCIAR AUTO A PERSONA ---");
        Auto auto1 = new Auto("Toyota", "Corolla", "ABC123");
        autoCtrl.crearAutoConPersona(auto1, personaCtrl.buscarPersona(persona1.getId()));

        Auto auto2 = new Auto("Ford", "Fiesta", "XYZ789");
        autoCtrl.crearAutoConPersona(auto2, personaCtrl.buscarPersona(persona2.getId()));

        // 3. LEER (FIND / FIND ALL)
        System.out.println("\n--- LISTAR TODAS LAS PERSONAS ---");
        List<Persona> personas = personaCtrl.listarPersonas();
        personas.forEach(System.out::println);

        System.out.println("\n--- LISTAR TODOS LOS AUTOS ---");
        List<Auto> autos = autoCtrl.listarAutos();
        autos.forEach(auto -> System.out.println(auto + " - Dueño: " + auto.getPersona().getNombre()));

        // 4. EDITAR
        System.out.println("\n--- EDITAR PERSONA ---");
        Persona personaEditada = personaCtrl.buscarPersona(persona1.getId());
        if (personaEditada != null) {
            personaEditada.setNombre("Juan Carlos");
            personaCtrl.editarPersona(personaEditada);
            System.out.println("Persona después de editar: " + personaCtrl.buscarPersona(persona1.getId()));
        }

        // 5. ELIMINAR
        System.out.println("\n--- ELIMINAR (comentar la siguiente línea si no quieres borrar) ---");
        // personaCtrl.eliminarPersona(persona2.getId());

        // ============================================
        // PARTE 2: Cliente y Pedidos (Relación 1-N)
        // ============================================
        System.out.println("\n\n📌 PARTE 2: Cliente y Pedidos (Relación Uno a Muchos)\n");

        ClienteController clienteCtrl = new ClienteController();

        // 1. CREAR CLIENTE CON PEDIDOS
        System.out.println("--- CREAR CLIENTE CON PEDIDOS ---");
        Cliente cliente = new Cliente("Pedro Martínez", "pedro@email.com");

        Pedido pedido1 = new Pedido("Notebook Lenovo", LocalDate.now(), 1500.00);
        Pedido pedido2 = new Pedido("Mouse Inalámbrico", LocalDate.now().minusDays(5), 25.50);
        Pedido pedido3 = new Pedido("Teclado Mecánico", LocalDate.now().minusDays(10), 89.99);

        cliente.agregarPedido(pedido1);
        cliente.agregarPedido(pedido2);
        cliente.agregarPedido(pedido3);

        clienteCtrl.crearClienteConPedidos(cliente);

        // 2. LISTAR CLIENTES Y SUS PEDIDOS
        System.out.println("\n--- LISTAR CLIENTES ---");
        List<Cliente> clientes = clienteCtrl.listarClientes();
        for (Cliente c : clientes) {
            System.out.println(c);
            for (Pedido p : c.getPedidos()) {
                System.out.println("  └─ " + p);
            }
        }

        // 3. AGREGAR PEDIDO A CLIENTE EXISTENTE
        System.out.println("\n--- AGREGAR NUEVO PEDIDO AL CLIENTE ---");
        Cliente clienteExistente = clienteCtrl.buscarCliente(cliente.getId());
        if (clienteExistente != null) {
            Pedido pedidoNuevo = new Pedido("Monitor 27'", LocalDate.now(), 350.00);
            clienteCtrl.agregarPedidoACliente(clienteExistente.getId(), pedidoNuevo);

            System.out.println("Cliente actualizado:");
            Cliente actualizado = clienteCtrl.buscarCliente(clienteExistente.getId());
            actualizado.getPedidos().forEach(p -> System.out.println("  └─ " + p));
        }

        // Cerrar recursos
        personaCtrl.cerrar();
        autoCtrl.cerrar();
        clienteCtrl.cerrar();
        scanner.close();

        System.out.println("\n=================================================");
        System.out.println("         ✅ PROYECTO EJECUTADO CON ÉXITO         ");
        System.out.println("=================================================");
    }
}