package poo;

import io.javalin.Javalin;

public class WebApp {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);

        app.get("/", ctx -> ctx.result("Hola desde Javalin"));

        // Puedes agregar más rutas después:
        app.get("/ping", ctx -> ctx.result("pong"));
    }
}
