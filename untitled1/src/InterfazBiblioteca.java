import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

/** Interfaz gráfica de uso interno que reutiliza el modelo del menú de consola. */
public class InterfazBiblioteca extends Application {
    private final Biblioteca biblioteca = new Biblioteca();
    private VBox cuerpoPagina;
    private Label tituloPagina;
    private Label subtituloPagina;
    private Button botonSeleccionado;

    /** Construye y muestra la ventana con navegación por secciones. */
    @Override
    public void start(Stage ventana) {
        VBox barraLateral = new VBox(16);
        barraLateral.setPadding(new Insets(24, 14, 18, 14));
        barraLateral.setPrefWidth(255);
        barraLateral.setStyle("-fx-background-color: #102A4C;");

        Image imagenLogo = new Image(new File("assets/librologia-logo.png").toURI().toString());
        ImageView logo = new ImageView(imagenLogo);
        logo.setFitWidth(46);
        logo.setFitHeight(46);
        logo.setPreserveRatio(true);
        Label marca = new Label("Librologia");
        marca.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        HBox encabezadoLateral = new HBox(12, logo, marca);
        encabezadoLateral.setAlignment(Pos.CENTER_LEFT);

        VBox opciones = new VBox(5);
        agregarTituloSeccion(opciones, "INICIO");
        agregarBoton(opciones, "Panel principal", this::mostrarInicio);
        agregarTituloSeccion(opciones, "CATÁLOGO");
        agregarBoton(opciones, "Registrar libro", this::mostrarFormularioLibro);
        agregarBoton(opciones, "Catálogo de libros", this::mostrarCatalogo);
        agregarTituloSeccion(opciones, "USUARIOS");
        agregarBoton(opciones, "Registrar usuario", this::mostrarFormularioUsuario);
        agregarBoton(opciones, "Consultar usuarios", this::mostrarUsuarios);
        agregarTituloSeccion(opciones, "PRÉSTAMOS");
        agregarBoton(opciones, "Registrar préstamo", this::mostrarFormularioPrestamo);
        agregarBoton(opciones, "Registrar devolución", this::mostrarFormularioDevolucion);
        agregarBoton(opciones, "Consultar préstamos", this::mostrarPrestamos);

        ScrollPane navegacion = new ScrollPane(opciones);
        navegacion.setFitToWidth(true);
        navegacion.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        navegacion.setStyle("-fx-background: #102A4C; -fx-background-color: #102A4C;");
        VBox.setVgrow(navegacion, javafx.scene.layout.Priority.ALWAYS);

        barraLateral.getChildren().addAll(encabezadoLateral, navegacion);

        tituloPagina = new Label();
        tituloPagina.setStyle("-fx-font-size: 27px; -fx-font-weight: bold; -fx-text-fill: #172B4D;");
        subtituloPagina = new Label();
        subtituloPagina.setStyle("-fx-font-size: 13px; -fx-text-fill: #718096;");
        cuerpoPagina = new VBox(16);
        cuerpoPagina.setPadding(new Insets(8, 0, 22, 0));
        VBox contenido = new VBox(8, tituloPagina, subtituloPagina, new Separator(), cuerpoPagina);
        contenido.setPadding(new Insets(30, 34, 24, 34));
        contenido.setStyle("-fx-background-color: #F3F6FB;");

        ScrollPane pagina = new ScrollPane(contenido);
        pagina.setFitToWidth(true);
        pagina.setStyle("-fx-background: #F3F6FB; -fx-background-color: #F3F6FB;");

        BorderPane raiz = new BorderPane();
        raiz.setLeft(barraLateral);
        raiz.setCenter(pagina);
        raiz.setStyle("-fx-background-color: #F3F6FB;");

        ventana.getIcons().add(imagenLogo);
        ventana.setTitle("Librologia | Gestión interna");
        ventana.setMinWidth(980);
        ventana.setMinHeight(650);
        ventana.setScene(new Scene(raiz, 1180, 760));
        ventana.show();
        mostrarInicio();
    }

    /** Añade un encabezado para separar las opciones por área de trabajo. */
    private void agregarTituloSeccion(VBox panel, String texto) {
        Label titulo = new Label(texto);
        titulo.setPadding(new Insets(11, 0, 2, 10));
        titulo.setStyle("-fx-text-fill: #8FA9C8; -fx-font-size: 10px; -fx-font-weight: bold;");
        panel.getChildren().add(titulo);
    }

    /** Crea un botón de navegación y conecta la página correspondiente. */
    private void agregarBoton(VBox panel, String texto, Runnable accion) {
        Button boton = new Button(texto);
        boton.setMaxWidth(Double.MAX_VALUE);
        boton.setMinHeight(38);
        boton.setAlignment(Pos.CENTER_LEFT);
        boton.setStyle(estiloBoton(false));
        boton.setOnMouseEntered(evento -> {
            if (boton != botonSeleccionado) boton.setStyle(estiloHoverBoton());
        });
        boton.setOnMouseExited(evento -> {
            if (boton != botonSeleccionado) boton.setStyle(estiloBoton(false));
        });
        boton.setOnAction(evento -> {
            if (botonSeleccionado != null) botonSeleccionado.setStyle(estiloBoton(false));
            botonSeleccionado = boton;
            botonSeleccionado.setStyle(estiloBoton(true));
            accion.run();
        });
        panel.getChildren().add(boton);
    }

    /** Devuelve el estilo azul del botón de navegación. */
    private String estiloBoton(boolean resaltado) {
        String fondo = resaltado ? "#244B78" : "transparent";
        return "-fx-background-color: " + fondo + "; -fx-text-fill: #EAF2FC; "
                + "-fx-font-size: 12px; -fx-padding: 9px 12px; -fx-background-radius: 9; "
                + "-fx-cursor: hand; -fx-font-weight: " + (resaltado ? "bold" : "normal") + ";";
    }

    /** Usa un color distinto para el paso del cursor y evita confundirlo con la selección. */
    private String estiloHoverBoton() {
        return "-fx-background-color: #1A385C; -fx-text-fill: #EAF2FC; "
                + "-fx-font-size: 12px; -fx-padding: 9px 12px; -fx-background-radius: 9; "
                + "-fx-cursor: hand; -fx-font-weight: normal;";
    }

    /** Cambia el título, la descripción y el contenido de la página central. */
    private void mostrarPagina(String titulo, String descripcion, Node... contenido) {
        tituloPagina.setText(titulo);
        subtituloPagina.setText(descripcion);
        cuerpoPagina.getChildren().setAll(contenido);
    }

    /** Muestra la bienvenida y accesos a las tareas más comunes. */
    private void mostrarInicio() {
        VBox bienvenida = crearTarjeta("Bienvenido", "Sistema de gestión interna",
                "Selecciona una sección del menú para comenzar.");
        HBox cifras = new HBox(14,
                tarjetaCifra("Títulos", biblioteca.getCantidadLibros(), "#2563EB"),
                tarjetaCifra("Usuarios", biblioteca.getCantidadPersonas(), "#0891B2"),
                tarjetaCifra("Ejemplares disponibles", biblioteca.contarEjemplaresDisponibles(), "#15803D"));
        VBox accesos = new VBox(10,
                crearAccesoRapido("Registrar préstamo", "Registrar la salida de un ejemplar", this::mostrarFormularioPrestamo),
                crearAccesoRapido("Registrar libro", "Añadir un título al catálogo", this::mostrarFormularioLibro));
        mostrarPagina("Librologia", "Resumen de la actividad y accesos de uso frecuente.", bienvenida,
                cifras, encabezadoSeccion("Accesos rápidos"), accesos);
    }

    /** Construye el formulario para registrar un libro en el inventario. */
    private void mostrarFormularioLibro() {
        TextField codigo = crearCampo("Código del libro");
        TextField titulo = crearCampo("Título");
        TextField autor = crearCampo("Autor");
        TextField cantidad = crearCampo("Cantidad de ejemplares");
        Label estado = crearEtiquetaEstado();
        Button guardar = crearBotonAccion("Guardar libro");
        guardar.setOnAction(evento -> {
            if (estaVacio(codigo) || estaVacio(titulo) || estaVacio(autor) || estaVacio(cantidad)) {
                mostrarEstado(estado, "Completa todos los campos.", false);
                return;
            }
            try {
                int unidades = Integer.parseInt(cantidad.getText().trim());
                if (unidades < 0) throw new NumberFormatException();
                boolean guardado = biblioteca.registrarLibro(new Libro(codigo.getText().trim(),
                        titulo.getText().trim(), autor.getText().trim(), unidades));
                mostrarEstado(estado, guardado ? "Libro guardado en el inventario." : "No se pudo guardar el libro.", guardado);
                if (guardado) {
                    codigo.clear(); titulo.clear(); autor.clear(); cantidad.clear();
                }
            } catch (NumberFormatException error) {
                mostrarEstado(estado, "La cantidad debe ser un número entero igual o mayor que cero.", false);
            }
        });
        VBox formulario = crearFormulario("Datos del libro", "Ingresa la identificación y la cantidad inicial de ejemplares.",
                campoConEtiqueta("Código", codigo), campoConEtiqueta("Título", titulo),
                campoConEtiqueta("Autor", autor), campoConEtiqueta("Ejemplares", cantidad),
                filaAccion(guardar, estado));
        mostrarPagina("Registrar libro", "Catálogo · Registro de un nuevo título.", formulario);
    }

    /** Construye el formulario para registrar un usuario y su tipo. */
    private void mostrarFormularioUsuario() {
        TextField nombre = crearCampo("Nombre completo");
        TextField documento = crearCampo("Documento");
        TextField datoAdicional = crearCampo("Programa académico");
        ToggleGroup tipo = new ToggleGroup();
        RadioButton estudiante = crearOpcionTipo("Estudiante", tipo, true);
        RadioButton docente = crearOpcionTipo("Docente", tipo, false);
        RadioButton directivo = crearOpcionTipo("Directivo", tipo, false);
        RadioButton investigador = crearOpcionTipo("Investigador", tipo, false);
        estudiante.setOnAction(evento -> datoAdicional.setPromptText("Programa académico"));
        docente.setOnAction(evento -> datoAdicional.setPromptText("Área"));
        directivo.setOnAction(evento -> datoAdicional.setPromptText("Departamento"));
        investigador.setOnAction(evento -> datoAdicional.setPromptText("Línea de investigación"));
        FlowPane opcionesTipo = new FlowPane(10, 8, estudiante, docente, directivo, investigador);
        Label estado = crearEtiquetaEstado();
        Button guardar = crearBotonAccion("Guardar usuario");
        guardar.setOnAction(evento -> {
            if (estaVacio(nombre) || estaVacio(documento) || estaVacio(datoAdicional)) {
                mostrarEstado(estado, "Completa el nombre, documento y dato del tipo de usuario.", false);
                return;
            }
            try {
                int numeroDocumento = Integer.parseInt(documento.getText().trim());
                if (numeroDocumento <= 0) throw new NumberFormatException();
                String dato = datoAdicional.getText().trim();
                Persona persona;
                if (estudiante.isSelected()) persona = new Estudiante(nombre.getText().trim(), numeroDocumento, dato);
                else if (docente.isSelected()) persona = new Docente(nombre.getText().trim(), numeroDocumento, dato);
                else if (directivo.isSelected()) persona = new Directivo(nombre.getText().trim(), numeroDocumento, dato);
                else persona = new Investigador(nombre.getText().trim(), numeroDocumento, dato);
                boolean guardado = biblioteca.registrarPersona(persona);
                mostrarEstado(estado, guardado ? "Usuario guardado correctamente." : "No se pudo guardar el usuario.", guardado);
                if (guardado) {
                    nombre.clear(); documento.clear(); datoAdicional.clear();
                }
            } catch (NumberFormatException error) {
                mostrarEstado(estado, "El documento debe ser un número entero positivo.", false);
            }
        });
        VBox formulario = crearFormulario("Información del usuario", "Selecciona el tipo y completa sus datos.",
                campoConEtiqueta("Nombre", nombre), campoConEtiqueta("Documento", documento),
                campoConEtiqueta("Tipo de usuario", opcionesTipo),
                campoConEtiqueta("Dato asociado", datoAdicional), filaAccion(guardar, estado));
        mostrarPagina("Registrar usuario", "Usuarios · Inscripción de estudiantes y personal.", formulario);
    }

    /** Construye el formulario para registrar la salida de un libro. */
    private void mostrarFormularioPrestamo() {
        TextField documento = crearCampo("Documento del usuario");
        TextField codigo = crearCampo("Código del libro");
        Label estado = crearEtiquetaEstado();
        Button guardar = crearBotonAccion("Registrar préstamo");
        guardar.setOnAction(evento -> {
            Integer numeroDocumento = convertirPositivo(documento.getText());
            if (numeroDocumento == null || estaVacio(codigo)) {
                mostrarEstado(estado, "Ingresa un documento positivo y el código del libro.", false);
                return;
            }
            boolean guardado = biblioteca.prestarLibro(numeroDocumento, codigo.getText().trim());
            mostrarEstado(estado, guardado ? "Préstamo registrado. Revisa la fecha límite en préstamos activos."
                    : "No se pudo registrar. Verifica el usuario, el límite y la disponibilidad.", guardado);
            if (guardado) { documento.clear(); codigo.clear(); }
        });
        VBox formulario = crearFormulario("Datos del préstamo", "El sistema valida el límite del usuario y los ejemplares disponibles.",
                campoConEtiqueta("Documento", documento), campoConEtiqueta("Código del libro", codigo),
                filaAccion(guardar, estado));
        mostrarPagina("Registrar préstamo", "Préstamos · Registro de salida de un ejemplar.", formulario);
    }

    /** Construye el formulario para registrar la devolución de un ejemplar. */
    private void mostrarFormularioDevolucion() {
        TextField documento = crearCampo("Documento del usuario");
        TextField codigo = crearCampo("Código del libro");
        Label estado = crearEtiquetaEstado();
        Button guardar = crearBotonAccion("Registrar devolución");
        guardar.setOnAction(evento -> {
            Integer numeroDocumento = convertirPositivo(documento.getText());
            if (numeroDocumento == null || estaVacio(codigo)) {
                mostrarEstado(estado, "Ingresa un documento positivo y el código del libro.", false);
                return;
            }
            boolean guardado = biblioteca.devolverLibro(numeroDocumento, codigo.getText().trim());
            mostrarEstado(estado, guardado ? "Devolución registrada y ejemplar agregado al inventario."
                    : "No se encontró un préstamo activo con esos datos.", guardado);
            if (guardado) { documento.clear(); codigo.clear(); }
        });
        VBox formulario = crearFormulario("Datos de la devolución", "Identifica al usuario y el libro que está entregando.",
                campoConEtiqueta("Documento", documento), campoConEtiqueta("Código del libro", codigo),
                filaAccion(guardar, estado));
        mostrarPagina("Registrar devolución", "Préstamos · Cierre de un préstamo activo.", formulario);
    }

    /** Muestra la búsqueda y el listado dentro de una sola sección de catálogo. */
    private void mostrarCatalogo() {
        TextField busqueda = crearCampo("Buscar por título, autor o código");
        VBox lista = new VBox(12);
        Button buscar = crearBotonAccion("Buscar");
        Button verTodo = crearBotonAccion("Ver todo");
        buscar.setOnAction(evento -> actualizarCatalogo(busqueda.getText(), lista));
        verTodo.setOnAction(evento -> {
            busqueda.clear();
            actualizarCatalogo("", lista);
        });
        actualizarCatalogo("", lista);
        VBox panel = crearFormulario("Catálogo e inventario",
                "Busca un título o revisa todos los libros y ejemplares disponibles.",
                campoConEtiqueta("Buscar libro", busqueda), new HBox(10, buscar, verTodo), lista);
        mostrarPagina("Catálogo de libros", "Consulta de títulos y existencias.", panel);
    }

    /** Filtra los libros por código, título o autor recorriendo el arreglo. */
    private void actualizarCatalogo(String texto, VBox lista) {
        lista.getChildren().clear();
        String criterio = texto == null ? "" : texto.trim().toLowerCase();
        int encontrados = 0;
        for (int i = 0; i < biblioteca.getCantidadLibros(); i++) {
            Libro libro = biblioteca.obtenerLibroRegistrado(i);
            boolean coincide = criterio.isEmpty()
                    || libro.getCodigo().toLowerCase().contains(criterio)
                    || libro.getTitulo().toLowerCase().contains(criterio)
                    || libro.getAutor().toLowerCase().contains(criterio);
            if (coincide) {
                lista.getChildren().add(crearTarjeta(libro.getTitulo(), "Código: " + libro.getCodigo(),
                        "Autor: " + libro.getAutor(), "Ejemplares disponibles: " + libro.getCantidadDisponible()));
                encontrados++;
            }
        }
        if (encontrados == 0) lista.getChildren().add(mensajeVacio("No hay libros que coincidan con la búsqueda."));
    }

    /** Muestra el listado de usuarios y permite consultar su estado desde la misma sección. */
    private void mostrarUsuarios() {
        TextField documento = crearCampo("Documento del usuario");
        VBox lista = new VBox(12);
        Label estado = crearEtiquetaEstado();
        Button consultar = crearBotonAccion("Consultar estado");
        Button verTodos = crearBotonAccion("Ver todos");
        consultar.setOnAction(evento -> {
            lista.getChildren().clear();
            Integer numeroDocumento = convertirPositivo(documento.getText());
            if (numeroDocumento == null) {
                mostrarEstado(estado, "Ingresa un documento entero positivo.", false);
                return;
            }
            Persona persona = biblioteca.buscarPersona(numeroDocumento);
            if (persona == null) {
                mostrarEstado(estado, "No se encontró un usuario con ese documento.", false);
                return;
            }
            mostrarEstado(estado, "Usuario encontrado.", true);
            lista.getChildren().add(crearTarjeta(persona.getNombre(), "Documento: " + persona.getDocumento(),
                    "Tipo: " + persona.getTipoUsuario(), "Préstamos activos: " + persona.getPrestamosActivos()
                            + " de " + persona.getlimiteLibros(),
                    "Días de suspensión pendientes: " + persona.getDiasSuspension()));
        });
        verTodos.setOnAction(evento -> {
            documento.clear();
            estado.setText("");
            actualizarUsuarios(lista);
        });
        actualizarUsuarios(lista);
        VBox panel = crearFormulario("Usuarios registrados",
                "Consulta la ficha y habilitación de una persona o revisa el listado completo.",
                campoConEtiqueta("Buscar por documento", documento), new HBox(10, consultar, verTodos),
                estado, lista);
        mostrarPagina("Usuarios", "Registro y consulta interna de personas.", panel);
    }

    /** Lista las personas registradas recorriendo el arreglo de usuarios. */
    private void actualizarUsuarios(VBox lista) {
        lista.getChildren().clear();
        for (int i = 0; i < biblioteca.getCantidadPersonas(); i++) {
            Persona persona = biblioteca.obtenerPersonaRegistrada(i);
            lista.getChildren().add(crearTarjeta(persona.getNombre(), "Documento: " + persona.getDocumento(),
                    "Tipo: " + persona.getTipoUsuario(), "Préstamos activos: " + persona.getPrestamosActivos()));
        }
        if (biblioteca.getCantidadPersonas() == 0) lista.getChildren().add(mensajeVacio("No hay usuarios registrados."));
    }

    /** Muestra activos, vencidos e historial mediante filtros de una sola vista. */
    private void mostrarPrestamos() {
        VBox lista = new VBox(12);
        Button activos = crearBotonAccion("Activos");
        Button vencidos = crearBotonAccion("Vencidos");
        Button historial = crearBotonAccion("Historial completo");
        activos.setOnAction(evento -> actualizarPrestamos("activos", lista));
        vencidos.setOnAction(evento -> actualizarPrestamos("vencidos", lista));
        historial.setOnAction(evento -> actualizarPrestamos("historial", lista));
        actualizarPrestamos("activos", lista);
        VBox panel = crearFormulario("Consulta de préstamos",
                "Usa los filtros para revisar préstamos activos, vencidos o ya devueltos.",
                new HBox(10, activos, vencidos, historial), lista);
        mostrarPagina("Préstamos", "Seguimiento interno de circulación y devoluciones.", panel);
    }

    /** Filtra el arreglo de préstamos y muestra sus datos y estado. */
    private void actualizarPrestamos(String filtro, VBox lista) {
        lista.getChildren().clear();
        int encontrados = 0;
        for (int i = 0; i < biblioteca.getCantidadPrestamos(); i++) {
            Prestamo prestamo = biblioteca.obtenerPrestamoRegistrado(i);
            boolean vencido = prestamo.isActivo()
                    && java.time.LocalDate.now().isAfter(prestamo.getFechaLimiteDevolucion());
            boolean mostrar = filtro.equals("historial")
                    || (filtro.equals("activos") && prestamo.isActivo())
                    || (filtro.equals("vencidos") && vencido);
            if (mostrar) {
                String estado = vencido ? "Vencido" : (prestamo.isActivo() ? "Activo" : "Devuelto");
                lista.getChildren().add(crearTarjeta(prestamo.getLibro().getTitulo(),
                        "Usuario: " + prestamo.getUsuario().getNombre() + " · " + prestamo.getUsuario().getDocumento(),
                        "Préstamo: " + prestamo.getFechaPrestamo() + " · Límite: " + prestamo.getFechaLimiteDevolucion(),
                        "Estado: " + estado));
                encontrados++;
            }
        }
        if (encontrados == 0) lista.getChildren().add(mensajeVacio("No hay préstamos para mostrar en este filtro."));
    }

    /** Crea una ficha con título y datos para mostrar un registro consultado. */
    private VBox crearTarjeta(String titulo, String... detalles) {
        Label nombre = new Label(titulo);
        nombre.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1E3A5F;");
        VBox tarjeta = new VBox(8);
        tarjeta.getChildren().add(nombre);
        for (String detalle : detalles) {
            Label texto = new Label(detalle);
            texto.setWrapText(true);
            texto.setStyle("-fx-font-size: 12px; -fx-text-fill: #60758F;");
            tarjeta.getChildren().add(texto);
        }
        tarjeta.setPadding(new Insets(17));
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 13; "
                + "-fx-border-color: #E0E8F2; -fx-border-radius: 13; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(25, 45, 75, 0.04), 10, 0, 0, 3);");
        return tarjeta;
    }

    /** Crea una tarjeta numérica para el resumen de inventario. */
    private VBox tarjetaCifra(String titulo, int valor, String color) {
        Label etiqueta = new Label(titulo);
        etiqueta.setStyle("-fx-text-fill: #718096; -fx-font-size: 10px; -fx-font-weight: bold;");
        Label numero = new Label(String.valueOf(valor));
        numero.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        VBox tarjeta = new VBox(10, etiqueta, numero);
        tarjeta.setPadding(new Insets(18));
        tarjeta.setPrefWidth(240);
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 13; "
                + "-fx-border-color: #E0E8F2; -fx-border-radius: 13;");
        HBox.setHgrow(tarjeta, javafx.scene.layout.Priority.ALWAYS);
        return tarjeta;
    }

    /** Crea una tarjeta seleccionable que abre una sección de uso frecuente. */
    private VBox crearAccesoRapido(String titulo, String detalle, Runnable accion) {
        VBox tarjeta = crearTarjeta(titulo, detalle);
        tarjeta.setMinWidth(180);
        tarjeta.setOnMouseEntered(evento -> tarjeta.setStyle("-fx-background-color: #F8FBFF; "
                + "-fx-background-radius: 13; -fx-border-color: #60A5FA; -fx-border-radius: 13; -fx-cursor: hand;"));
        tarjeta.setOnMouseExited(evento -> tarjeta.setStyle("-fx-background-color: white; "
                + "-fx-background-radius: 13; -fx-border-color: #E0E8F2; -fx-border-radius: 13; -fx-cursor: hand;"));
        tarjeta.setOnMouseClicked(evento -> accion.run());
        HBox.setHgrow(tarjeta, javafx.scene.layout.Priority.ALWAYS);
        return tarjeta;
    }

    /** Crea una tarjeta con una indicación cuando una lista está vacía. */
    private VBox mensajeVacio(String mensaje) {
        Label texto = new Label(mensaje);
        texto.setStyle("-fx-text-fill: #718096; -fx-font-size: 13px;");
        return crearTarjeta("Sin registros", texto.getText());
    }

    /** Crea una tarjeta con un formulario, su título y una explicación breve. */
    private VBox crearFormulario(String titulo, String descripcion, Node... campos) {
        VBox formulario = new VBox(14);
        formulario.getChildren().addAll(encabezadoSeccion(titulo));
        Label ayuda = new Label(descripcion);
        ayuda.setWrapText(true);
        ayuda.setStyle("-fx-text-fill: #718096; -fx-font-size: 12px;");
        formulario.getChildren().add(ayuda);
        formulario.getChildren().addAll(campos);
        formulario.setPadding(new Insets(22));
        formulario.setMaxWidth(760);
        formulario.setStyle("-fx-background-color: white; -fx-background-radius: 15; "
                + "-fx-border-color: #E0E8F2; -fx-border-radius: 15; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(25, 45, 75, 0.05), 12, 0, 0, 3);");
        return formulario;
    }

    /** Crea el encabezado de una sección central. */
    private Label encabezadoSeccion(String texto) {
        Label etiqueta = new Label(texto);
        etiqueta.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #20334F;");
        return etiqueta;
    }

    /** Crea un campo de texto con estilo y un texto de ayuda. */
    private TextField crearCampo(String ayuda) {
        TextField campo = new TextField();
        campo.setPromptText(ayuda);
        campo.setMaxWidth(520);
        campo.setStyle("-fx-background-color: #F8FAFC; -fx-background-radius: 9; "
                + "-fx-border-color: #D7E1ED; -fx-border-radius: 9; -fx-padding: 11px;");
        return campo;
    }

    /** Coloca una etiqueta encima del campo o selector indicado. */
    private VBox campoConEtiqueta(String etiqueta, Node control) {
        Label titulo = new Label(etiqueta);
        titulo.setStyle("-fx-text-fill: #38516F; -fx-font-size: 12px; -fx-font-weight: bold;");
        return new VBox(7, titulo, control);
    }

    /** Crea un selector de tipo de usuario usando botones de opción. */
    private RadioButton crearOpcionTipo(String texto, ToggleGroup grupo, boolean seleccionado) {
        RadioButton opcion = new RadioButton(texto);
        opcion.setToggleGroup(grupo);
        opcion.setSelected(seleccionado);
        opcion.setStyle("-fx-text-fill: #38516F; -fx-font-size: 12px;");
        return opcion;
    }

    /** Crea el botón azul principal de un formulario. */
    private Button crearBotonAccion(String texto) {
        Button boton = new Button(texto);
        boton.setMinHeight(40);
        boton.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white; "
                + "-fx-background-radius: 9; -fx-padding: 10px 18px; -fx-font-weight: bold; -fx-cursor: hand;");
        return boton;
    }

    /** Agrupa un botón de acción y un mensaje de resultado. */
    private HBox filaAccion(Button boton, Label estado) {
        HBox fila = new HBox(14, boton, estado);
        fila.setAlignment(Pos.CENTER_LEFT);
        return fila;
    }

    /** Crea una etiqueta inicialmente vacía para mostrar el resultado del formulario. */
    private Label crearEtiquetaEstado() {
        Label estado = new Label();
        estado.setWrapText(true);
        return estado;
    }

    /** Muestra el resultado de una operación con color de éxito o error. */
    private void mostrarEstado(Label etiqueta, String mensaje, boolean correcto) {
        etiqueta.setText(mensaje);
        etiqueta.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: "
                + (correcto ? "#15803D" : "#B42318") + ";");
    }

    /** Indica si un campo está vacío. */
    private boolean estaVacio(TextField campo) {
        return campo.getText() == null || campo.getText().trim().isEmpty();
    }

    /** Convierte un texto a entero positivo, o devuelve null si no es válido. */
    private Integer convertirPositivo(String texto) {
        try {
            int valor = Integer.parseInt(texto.trim());
            return valor > 0 ? valor : null;
        } catch (NumberFormatException error) {
            return null;
        }
    }

    /** Inicia la aplicación JavaFX. */
    public static void main(String[] args) {
        launch(args);
    }
}
