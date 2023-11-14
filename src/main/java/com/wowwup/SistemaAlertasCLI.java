package com.wowwup;

import java.util.Scanner;

import com.wowwup.domain.SistemaAlertas;
import com.wowwup.domain.Topic;
import com.wowwup.domain.User;

public class SistemaAlertasCLI {
    private static Scanner scanner = new Scanner(System.in);
    private static SistemaAlertas system = new SistemaAlertas();

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    registerNewUser();
                    break;
                case 2:
                    registerNewTopic();
                    break;
                case 3:
                    subscribeUserToTopic();
                    break;
                // Agrega más casos para otras funciones...

                case 0:
                    System.out.println("Saliendo del sistema. ¡Hasta luego!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n--- Sistema de Alertas ---");
        System.out.println("1. Registrar Usuario");
        System.out.println("2. Registrar Tema");
        System.out.println("3. Suscribir Usuario a Tema");
        // Agrega más opciones para otras funciones...
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }

    private static void registerNewUser() {
        System.out.print("Ingrese el nombre del usuario: ");
        String name = scanner.nextLine();
        System.out.print("Ingrese el apellido del usuario: ");
        String surName = scanner.nextLine();
        User newUser = new User(name, surName);
        system.registerUser(newUser);
        System.out.println("Usuario registrado exitosamente.");
    }

    private static void registerNewTopic() {
        System.out.print("Ingrese el nombre del tema: ");
        String nombreTema = scanner.nextLine();
        Topic nuevoTema = new Topic(nombreTema);
        system.registerTopic(nuevoTema);
        System.out.println("Tema registrado exitosamente.");
    }

    private static void subscribeUserToTopic() {
        // Listar y seleccionar un usuario
        System.out.println("Usuarios registrados:");
        for (int i = 0; i < system.getUsers().size(); i++) {
            System.out.println((i + 1) + ". " + system.getUsers().get(i).getName());
        }
        System.out.print("Seleccione un usuario (ingrese el número): ");
        int selectedUserIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consumir el salto de línea

        if (selectedUserIndex < 0 || selectedUserIndex >= system.getUsers().size()) {
            System.out.println("Número de usuario no válido.");
            return;
        }

        var selectedUser = system.getUsers().get(selectedUserIndex);

        // Listar y seleccionar un tema
        System.out.println("Temas registrados:");
        for (int i = 0; i < system.getTopics().size(); i++) {
            System.out.println((i + 1) + ". " + system.getTopics().get(i).getName());
        }
        System.out.print("Seleccione un tema (ingrese el número): ");
        int choosedTopicIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consumir el salto de línea

        if (choosedTopicIndex < 0 || choosedTopicIndex >= system.getTopics().size()) {
            System.out.println("Número de tema no válido.");
            return;
        }

        var choosedTopic = system.getTopics().get(choosedTopicIndex);

        selectedUser.subscribeToTopic(choosedTopic);
        System.out.println("Usuario " + selectedUser.getName() + " suscrito al tema " + choosedTopic.getName() + ".");
    }

    // Agrega más métodos para otras funciones...

}
