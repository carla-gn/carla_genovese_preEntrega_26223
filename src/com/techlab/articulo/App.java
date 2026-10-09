package com.techlab.articulo;

import java.util.ArrayList;
import java.util.Scanner;

import com.techlab.articulo.model.Articulo;
import com.techlab.articulo.model.ArticuloAlimenticio;
import com.techlab.articulo.model.ArticuloElectronico;
import com.techlab.articulo.model.Categoria;

public class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        ArrayList<Articulo> articulos = new ArrayList<>();

        ArrayList<Categoria> categorias = new ArrayList<>();
        precargarCategorias(categorias);
        precargarArticulos(articulos, categorias);

        int opcion;

        //Menu de operaciones
        do {
            System.out.println("\n======================================================");
            System.out.println(" JAM & SPOON - TIENDA DE MASCOTAS");
            System.out.println("======================================================");
            System.out.println("1 - Ingresar artículo");
            System.out.println("2 - Listar artículos");
            System.out.println("3 - Consultar un artículo");
            System.out.println("4 - Modificar un artículo");
            System.out.println("5 - Eliminar un artículo");
            System.out.println("6 - Listar categorías");
            System.out.println("0 - Salir");
            System.out.println("======================================================");

            opcion = leerEntero(scanner, "Ingrese una opción: ");

            switch (opcion) {
                case 1:
                    ingresarArticulo(scanner, articulos, categorias);
                    break;
                case 2:
                    listarArticulos(articulos);
                    break;
                case 3:
                    consultarArticulo(scanner, articulos);
                    break;
                case 4:
                    modificarArticulo(scanner, articulos, categorias);
                    break;
                case 5:
                    eliminarArticulo(scanner, articulos);
                    break;
                case 6:
                    listarCategorias(categorias);
                    break;
                case 0:
                    System.out.println("\nSaliendo del sistema. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("\nError: la opción ingresada no es válida.");
            }

        } while (opcion != 0);

        scanner.close();
    }

    //Carga inicial de productos
    public static void precargarArticulos(ArrayList<Articulo> articulos, ArrayList<Categoria> categorias) {
        Categoria tecnologia = buscarCategoriaPorCodigo(categorias, 1);
        Categoria juguetes = buscarCategoriaPorCodigo(categorias, 2);
        Categoria alimentos = buscarCategoriaPorCodigo(categorias, 3);
        Categoria premios = buscarCategoriaPorCodigo(categorias, 4);

        articulos.add(new ArticuloAlimenticio(100, "Nutrique Pavo 1kg", 20000, alimentos, 180));
        articulos.add(new ArticuloAlimenticio(101, "Nutrique Cordero 1kg", 21000, alimentos, 180));
        articulos.add(new ArticuloAlimenticio(102, "Old Prince 1kg", 18000, alimentos, 150));
        articulos.add(new ArticuloAlimenticio(103, "Royal Canin Fit 1kg", 23000, alimentos, 120));
        articulos.add(new ArticuloAlimenticio(104, "Premios para gatos", 6000, premios, 90));
        articulos.add(new ArticuloElectronico(105, "Cámara para mascotas", 85000, tecnologia, 12));
        articulos.add(new ArticuloElectronico(106, "Dispensador automático de comida", 120000, tecnologia, 12));
        articulos.add(new ArticuloElectronico(107, "Pelota interactiva", 35000, juguetes, 6));
    }

    public static void precargarCategorias(ArrayList<Categoria> categorias) {
        categorias.add(new Categoria(1, "Tecnología para mascotas", "Cámaras y dispositivos electrónicos"));
        categorias.add(new Categoria(2, "Juguetes electrónicos", "Juguetes interactivos para mascotas"));
        categorias.add(new Categoria(3, "Alimentos para mascotas", "Alimentos para perros y gatos"));
        categorias.add(new Categoria(4, "Premios y snacks", "Premios alimenticios para mascotas"));
    }

    //Permite ingresar un nuevo producto al stock
    public static void ingresarArticulo(
            Scanner scanner,
            ArrayList<Articulo> articulos,
            ArrayList<Categoria> categorias
    ) {
        System.out.println("\n--- INGRESAR ARTÍCULO ---");
        System.out.println("1 - Artículo electrónico");
        System.out.println("2 - Artículo alimenticio");

        int tipo;
        do {
            tipo = leerEntero(scanner, "Seleccione el tipo de artículo: ");

            if (tipo != 1 && tipo != 2) {
                System.out.println("Error: debe elegir 1 o 2.");
            }

        } while (tipo != 1 && tipo != 2);

        int codigo = leerEntero(scanner, "Ingrese el código del artículo: ");

        if (buscarArticuloPorCodigo(articulos, codigo) != null) {
            System.out.println("Error: ya existe un artículo con ese código.");
            return;
        }

        String nombre = leerTextoNoVacio(scanner, "Ingrese el nombre del artículo: ");
        double precio = leerDoubleNoNegativo(scanner, "Ingrese el precio del artículo: ");

        listarCategorias(categorias);
        Categoria categoria = pedirCategoriaExistente(scanner, categorias);

        Articulo articulo;

        if (tipo == 1) {
            int garantiaMeses = leerEnteroNoNegativo(scanner, "Ingrese la garantía en meses: ");

            articulo = new ArticuloElectronico(codigo, nombre, precio, categoria, garantiaMeses);
        } else {
            int diasParaVencimiento = leerEnteroNoNegativo(scanner, "Ingrese los días para vencimiento: ");

            articulo = new ArticuloAlimenticio(codigo, nombre, precio, categoria, diasParaVencimiento);
        }

        articulos.add(articulo);

        System.out.println("Artículo ingresado correctamente.");
        System.out.println("Resumen del objeto creado:");
        System.out.println(articulo);
    }

    //Lista los productos en stock
    public static void listarArticulos(ArrayList<Articulo> articulos) {
        System.out.println("\n--- LISTADO DE ARTÍCULOS ---");

        if (articulos.isEmpty()) {
            System.out.println("No hay artículos cargados.");
            return;
        }

        for (Articulo articulo : articulos) {
            System.out.println(articulo);
}
    }

    //Busca un producto por codigo
    public static void consultarArticulo(Scanner scanner, ArrayList<Articulo> articulos) {
        System.out.println("\n--- CONSULTAR ARTÍCULO ---");

        if (articulos.isEmpty()) {
            System.out.println("No hay artículos cargados.");
            return;
        }

        int codigo = leerEntero(scanner, "Ingrese el código del artículo a consultar: ");

        Articulo articulo = buscarArticuloPorCodigo(articulos, codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe.");
            return;
        }

        System.out.println("Artículo encontrado:");
        System.out.println(articulo);
        System.out.println("Detalle específico: " + articulo.getDetalleEspecifico());
    }

    //Permite modificar un producto
    public static void modificarArticulo(
            Scanner scanner,
            ArrayList<Articulo> articulos,
            ArrayList<Categoria> categorias
    ) {
        System.out.println("\n--- MODIFICAR ARTÍCULO ---");

        if (articulos.isEmpty()) {
            System.out.println("No hay artículos cargados.");
            return;
        }

        int codigo = leerEntero(scanner, "Ingrese el código del artículo a modificar: ");

        Articulo articulo = buscarArticuloPorCodigo(articulos, codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe.");
            return;
        }

        String nuevoNombre = leerTextoNoVacio(scanner, "Ingrese el nuevo nombre del artículo: ");
        double nuevoPrecio = leerDoubleNoNegativo(scanner, "Ingrese el nuevo precio del artículo: ");

        listarCategorias(categorias);
        Categoria nuevaCategoria = pedirCategoriaExistente(scanner, categorias);

        articulo.setNombre(nuevoNombre);
        articulo.setPrecio(nuevoPrecio);
        articulo.setCategoria(nuevaCategoria);

        if (articulo instanceof ArticuloElectronico) {
            ArticuloElectronico electronico = (ArticuloElectronico) articulo;

            int nuevaGarantia = leerEnteroNoNegativo(scanner, "Ingrese la nueva garantía en meses: ");
            electronico.setGarantiaMeses(nuevaGarantia);
        }

        if (articulo instanceof ArticuloAlimenticio) {
            ArticuloAlimenticio alimenticio = (ArticuloAlimenticio) articulo;

            int nuevosDias = leerEnteroNoNegativo(scanner, "Ingrese los nuevos días para vencimiento: ");
            alimenticio.setDiasParaVencimiento(nuevosDias);
        }

        System.out.println("Artículo modificado correctamente.");
    }

    //Elimina un producto por codigo
    public static void eliminarArticulo(Scanner scanner, ArrayList<Articulo> articulos) {
        System.out.println("\n--- ELIMINAR ARTÍCULO ---");

        if (articulos.isEmpty()) {
            System.out.println("No hay artículos cargados.");
            return;
        }

        int codigo = leerEntero(scanner, "Ingrese el código del artículo a eliminar: ");

        Articulo articulo = buscarArticuloPorCodigo(articulos, codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe.");
            return;
        }

        articulos.remove(articulo);
        System.out.println("Artículo eliminado correctamente.");
    }

    public static void listarCategorias(ArrayList<Categoria> categorias) {
        System.out.println("\n--- CATEGORÍAS DISPONIBLES ---");

        for (Categoria categoria : categorias) {
            System.out.println(categoria);
        }
    }

    public static Categoria pedirCategoriaExistente(Scanner scanner, ArrayList<Categoria> categorias) {
        while (true) {
            int codigoCategoria = leerEntero(scanner, "Ingrese el código de la categoría: ");

            Categoria categoria = buscarCategoriaPorCodigo(categorias, codigoCategoria);

            if (categoria != null) {
                return categoria;
            }

            System.out.println("Error: la categoría no existe.");
        }
    }

    public static Articulo buscarArticuloPorCodigo(ArrayList<Articulo> articulos, int codigo) {
        for (Articulo articulo : articulos) {
            if (articulo.getCodigo() == codigo) {
                return articulo;
            }
        }
        return null;
    }

    public static Categoria buscarCategoriaPorCodigo(ArrayList<Categoria> categorias, int codigo) {
        for (Categoria categoria : categorias) {
            if (categoria.getCodigo() == codigo) {
                return categoria;
            }
        }
        return null;
    }

    public static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número entero válido.");
            }
        }
    }

    public static int leerEnteroNoNegativo(Scanner scanner, String mensaje) {
        while (true) {
            int valor = leerEntero(scanner, mensaje);

            if (valor < 0) {
                System.out.println("Error: el valor no puede ser negativo.");
                continue;
            }

            return valor;
        }
    }

    public static double leerDoubleNoNegativo(Scanner scanner, String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double valor = Double.parseDouble(scanner.nextLine());

                if (!Double.isFinite(valor) || valor < 0) {
                    System.out.println("Error: el precio debe ser un número no negativo.");
                    continue;
                }

                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número decimal válido.");
            }
        }
    }

    public static String leerTextoNoVacio(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine();

            if (!texto.trim().isEmpty()) {
                return texto.trim();
            }

            System.out.println("Error: el texto no puede estar vacío.");
        }
    }
}
