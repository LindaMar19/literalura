package com.literalura.literalura.Principal;

import com.literalura.literalura.Repository.LibrosRepository;
import com.literalura.literalura.model.Autor;
import com.literalura.literalura.model.DatosLibro;
import com.literalura.literalura.model.Libro;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private final LibrosRepository repositorio;
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private String URL_BASE = "https://gutendex.com/books/";
    private AutorRepository repository;

    public Principal(AutorRepository repository){
        this.repository = repository;
    }

    public Principal(LibrosRepository) {
        LibrosRepository repository = null;
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                   *** Bienvenido(a) a Literalura *** 
            
             *** MENU PRINCIPAL ***
            
            1 - Buscar Libros por tÍtulo
            2 - Buscar por Nombre de autor 
            3 - Mostrar libros Registrados
            4 - Mostrar autores Registrados
            5 - Top 5 libros más buscados           
            ----------------------------------------------
            0 -  SALIR DEL PROGRAMA 
            ----------------------------------------------
            Elija una opción:
            """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibrosPortitulo();
                    break;
                case 2:
                    buscarPorNombreDeAutor();
                    break;

                case 3:
                    mostrarLibrosRegistrados();
                    break;

                case 4:
                    mostrarAutoresRegistrados();
                    break;

                case 5:
                    top5DeLibros();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private void buscarLibrosPortitulo() {
        System.out.println("Buscar libros por titulo");
        System.out.println("Introduzca el nombre del libro que desea buscar:");
        var nombre = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombre.replace(" ", "+").toLowerCase());

        if (json.isEmpty() || !json.contains("\"count\":0,\"next\":null,\"previous\":null,\"results\":[]")) {
            var datos = conversor.obtenerDatos(json, Datos.class);
        }
    }

    private void buscarPorNombreDeAutor() {
        System.out.println("Buscar por nombre de autor");
        System.out.println("Ingrese el nombre del autor que deseas buscar:");
        var nombre = teclado.nextLine();
        Optional<Autor> autor = repository.buscarPorNombreDeAutor(nombre);
        if (autor.isPresent()) {
            System.out.println(
                    "\nAutor: " + autor.get().getNombre() +
                            "\nFecha de Nacimiento: " + autor.get().getNacimiento() +
                            "\nLibros: " + autor.get().getLibros().stream()
                            .map(l -> l.getTitulo()).collect(Collectors.toList()) + "\n"
            );
        } else {
            System.out.println("El autor no existe en la BD");
        }
    }

    private void mostrarLibrosRegistrados() {
        System.out.println("Mostrar libros registrados");
        List<Libro> libros = repository.buscarTodosLosLibros();
        libros.forEach(l -> System.out.println(
                "-------------- LIBRO \uD83D\uDCD9  -----------------" +
                        "\nTítulo: " + l.getTitulo() +
                        "\nAutor: " + l.getAutor().getNombre() +
                        "\nIdioma: " + l.getIdioma().getIdioma() +
                        "\nNúmero de descargas: " + l.getDescargas() +
                        "\n----------------------------------------\n"
        ));
    }

    private void mostrarAutoresRegistrados() {
        System.out.println("Mostrar autores registrados");
        List<Autor> autores = repository.findAll();
        System.out.println();
        autores.forEach(l -> System.out.println(
                "Autor: " + l.getNombre() +
                        "\nFecha de Nacimiento: " + l.getNacimiento() +
                        "\nFecha de Fallecimiento: " + l.getFallecimiento() +
                        "\nLibros: " + l.getLibros().stream()
                        .map(t -> t.getTitulo()).collect(Collectors.toList()) + "\n"
        ));
    }

    private void top5DeLibros() {
        System.out.println("Top 5 libros más buscados");
        List<Libro> libros = repository.top5libros();
        System.out.println();
        libros.forEach(l -> System.out.println(
                "----------------- LIBRO \uD83D\uDCD9  ----------------" +
                        "\nTítulo: " + l.getTitulo() +
                        "\nAutor: " + l.getAutor().getNombre() +
                        "\nNúmero de descargas: " + l.getDescargas() +
                        "\n-------------------------------------------\n"
        ));
    }

}
