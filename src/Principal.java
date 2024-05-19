import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) throws IOException, InterruptedException {


        Scanner lectura = new Scanner(System.in);
        int seleccion = 0;

        //Definimos algunos validadores que ocuparemos para salir de próximos bucles
        boolean validador1 = false, validador2 = false, validador3 = false, validador4 = false, validador5 = false;
        System.out.println("Bienvenido al conversor de mondedas");

        //Primer bucle en el cual buscamos sea seleccionada una opción del menú
        //A partir de aqui, pusimos cada validador un try por si se ingresa un caracter distinto al esperado
        //Ocurre lo mismo para el tercer validador
        while (!validador1){
            try {

                System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
                System.out.println("     1. Peso argentino \n     2. Dolar Estadounidense " +
                        "\n     3. Real Brasileño \n     4. Boliviano boliviano " +
                        "\n     5. Ingresar manualmente \n     0. Salir");
                System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
                System.out.println("Selecciona la moneda que quieres convertir (número): ");
                seleccion = lectura.nextInt();

                if (seleccion >= 0 & seleccion < 6 ){
                    validador1 = true;
                }else {
                    System.out.println("Opcion invalida, por favor ingresa un numero de la lista");
                }

            }catch (InputMismatchException e){
                System.out.println("Opcion invalida, por favor ingresa un numero de la lista");
                lectura.next();

            }
        }
        Monedas moneda = new Monedas();
        Scanner lecturaISO = new Scanner(System.in);
        String json = "";


        if (seleccion == 0) {
            System.out.println("Gracias por usar nuestro servicio");
        } else {

        //El segundo validador es para asignar una clave,
        // En caso de que se halla seleccionado la opcion 5 entramos a un bucle hasta que se ingrese una clave valida
        //Ocurre lo mismo para el cuarto validador
            while (!validador2){
                try {

                    switch (seleccion) {
                        case 1:
                            moneda.setIso("ARS");
                            break;
                        case 2:
                            moneda.setIso("USD");
                            break;
                        case 3:
                            moneda.setIso("BRL");
                            break;
                        case 4:
                            moneda.setIso("BOB");
                            break;
                        case 5:
                            System.out.println("Escriba el código de divisa (ISO): ");
                            String iso = "";
                            iso = lecturaISO.nextLine();
                            moneda.setIso(iso.toUpperCase());
                            break;

                    }

                    String direccion = "https://v6.exchangerate-api.com/v6/255be9bc48338f627cc2d55c/latest/" + moneda.getIso();

                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(direccion))
                            .build();
                    HttpResponse<String> response = client
                            .send(request, HttpResponse.BodyHandlers.ofString());

                    json = response.body();

                    Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                            .create();


                    Resultado resultadoBusqueda = gson.fromJson(json, Resultado.class);


                    if (resultadoBusqueda.getResultadoBusqueda().equals("success")){
                        validador2 = true;
                    }else {
                        System.out.println("Clave invalida, por favor ingresa una clave válida");
                    }



                }catch (JsonSyntaxException e){
                    System.out.println("Clave invalida, por favor ingresa una clave válida");
                }

            }
            Scanner segundaLectura = new Scanner(System.in);
            int segundaSeleccion = 0;

            while (!validador3){
                try {
                    System.out.println("/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/");
                    System.out.println("     1. Peso argentino \n     2. Dolar Estadounidense " +
                            "\n     3. Real Brasileño \n     4. Boliviano boliviano " +
                            "\n     5. Ingresar manualmente");
                    System.out.println("/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/");
                    System.out.println("Selecciona la divisa a la que quieres convertir " + moneda.getIso() + ":");

                    segundaSeleccion = segundaLectura.nextInt();

                    if (segundaSeleccion >= 1 & segundaSeleccion < 6 ){
                        validador3 = true;
                    }else {
                        System.out.println("Opcion invalida, por favor ingresa un numero de la lista");
                    }

                }catch (InputMismatchException e){
                    System.out.println("Opcion invalida, por favor ingresa un numero de la lista");
                    segundaLectura.next();

                }
            }


            Monedas segundaMoneda = new Monedas();
            double conversion = 0.0;
            while (!validador4){
                try {

                    switch (segundaSeleccion) {
                        case 1:
                            segundaMoneda.setIso("ARS");
                            break;
                        case 2:
                            segundaMoneda.setIso("USD");
                            break;
                        case 3:
                            segundaMoneda.setIso("BRL");
                            break;
                        case 4:
                            segundaMoneda.setIso("BOB");
                            break;
                        case 5:
                            System.out.println("Escriba el código de la segunda divisa (ISO): ");
                            Scanner segundaLecturaISO = new Scanner(System.in);
                            var iso2 = segundaLecturaISO.nextLine();
                            segundaMoneda.setIso(iso2.toUpperCase());
                            break;
                    }

                    Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                            .create();
                    DatoJson nuevaMoneda = gson.fromJson(json, DatoJson.class);
                    Map<String, Double> tarifa = nuevaMoneda.conversionRates();
                    conversion = tarifa.get(segundaMoneda.getIso());
                    validador4 = true;

                }catch (NullPointerException e){
                    System.out.println("Clave invalida, por favor ingresa una clave válida");
                }
            }


            System.out.println("Escribe la cantidad que quieres convertir a "+ segundaMoneda.getIso());


            //Buscamos que se ingrese la cantidad a convertir
            while (!validador5){
                try {
                    Scanner terceraLectura = new Scanner(System.in);
                    double cantidadAConvertir = 0.0;
                    cantidadAConvertir = terceraLectura.nextDouble();

                    DecimalFormat df = new DecimalFormat("#.00");
                    double calculo = cantidadAConvertir * conversion;
                    String calculoFormato = df.format(calculo);


                    System.out.println("1 " + moneda.getIso() + " en " + segundaMoneda.getIso() + " es: "+ conversion);
                    System.out.println("\n"+ cantidadAConvertir + " " + moneda.getIso()+
                            " es equicalente a "+ calculoFormato + " " + segundaMoneda.getIso() );

                    validador5 = true;

                }catch (InputMismatchException e){
                    System.out.println("Por favor ingresa una cantidad válida");

                }
            }

        }
    }

}