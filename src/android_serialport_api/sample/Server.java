package android_serialport_api.sample;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.Enumeration;
import java.util.Timer;

public class Server {
    HttpServiceThread httpServiceThread;
    private String CurrentLocalIpAddress;
    boolean isRunning = false;
    private Timer t;
    private Context MAinContext;

    protected void onCreate(Bundle savedInstanceState) {
     /*   t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                //todo
            }

        } ,5000,5000);*/
    }

    public Server(Context MAinContext) {
        httpServiceThread = new HttpServiceThread();
        this.MAinContext = MAinContext;
    }

    public void startServer() {
        httpServiceThread.start();
        isRunning = true;
        CurrentLocalIpAddress = getIpAddress();
        Toast.makeText(MAinContext, "Local Server Start!   & IP = "+CurrentLocalIpAddress, Toast.LENGTH_SHORT).show();
    }

    public void stopServer() {
        httpServiceThread.stopServer();
        isRunning = false;
        Toast.makeText(MAinContext, "Local Server Stop!", Toast.LENGTH_SHORT).show();
    }

    public boolean isServerRunning() {
        return isRunning;
    }

    public int getPort() {
        return HttpServiceThread.HttpServerPORT;
    }

    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    private class HttpServiceThread extends Thread {

        ServerSocket serverSocket;
        Socket socket;
        HttpService httpService;
        BasicHttpContext basicHttpContext;
        static final int HttpServerPORT = 8002;
        boolean RUNNING = false;

        HttpServiceThread() {
            RUNNING = true;
            startHttpService();
        }

        @Override
        public void run() {

            try {
                serverSocket = new ServerSocket(HttpServerPORT);
                serverSocket.setReuseAddress(true);

                while (RUNNING) {
                    socket = serverSocket.accept();
                    DefaultHttpServerConnection httpServerConnection = new DefaultHttpServerConnection();
                    httpServerConnection.bind(socket,
                            new BasicHttpParams());
                    httpService.handleRequest(httpServerConnection,
                            basicHttpContext);
                    //                   httpServerConnection.shutdown();
                }
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (HttpException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        private synchronized void startHttpService() {
            BasicHttpProcessor basicHttpProcessor = new BasicHttpProcessor();
            basicHttpContext = new BasicHttpContext();

            basicHttpProcessor.addInterceptor(new ResponseDate());
            basicHttpProcessor.addInterceptor(new ResponseServer());
            basicHttpProcessor.addInterceptor(new ResponseContent());
            basicHttpProcessor.addInterceptor(new ResponseConnControl());

            httpService = new HttpService(basicHttpProcessor,
                    new DefaultConnectionReuseStrategy(),
                    new DefaultHttpResponseFactory());

            HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
            registry.register("/", new HomeCommandHandler());
            registry.register("/local", new ConnectLocalHandler());
            httpService.setHandlerResolver(registry);
        }

        public synchronized void stopServer() {
            RUNNING = false;
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        class ConnectLocalHandler implements HttpRequestHandler {

            @Override
            public void handle(HttpRequest request, HttpResponse response,
                               HttpContext httpContext) throws HttpException,
                    IOException {

                HttpEntity httpEntity = new EntityTemplate(
                        new ContentProducer() {

                            public void writeTo(
                                    final OutputStream outstream)
                                    throws IOException {

                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                                        outstream, "UTF-8");
                                String response = CurrentLocalIpAddress;

                                outputStreamWriter.write(response);
                                outputStreamWriter.flush();
                            }
                        });
                response.setHeader("Content-Type", "text/html");
                response.setEntity(httpEntity);
                response.setHeader("access-control-allow-origin", "*");
                response.setHeader("access-control-allow-headers", "Origin, X-Requested-With, Content-Type, Accept");
            }
        }

        class HomeCommandHandler implements HttpRequestHandler {

            @Override
            public void handle(HttpRequest request, HttpResponse response,
                               HttpContext httpContext) throws HttpException,
                    IOException {

                HttpEntity httpEntity = new EntityTemplate(
                        new ContentProducer() {

                            public void writeTo(
                                    final OutputStream outstream)
                                    throws IOException {

                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                                        outstream, "UTF-8");
                                String response = "<html><head></head><body><h1>"
                                        + "Hello HttpService, from Cs2GuRu"
                                        + "<h1></body></html>";

                                outputStreamWriter.write(response);
                                outputStreamWriter.flush();
                            }
                        });
                response.setHeader("Content-Type", "text/html");
                response.setEntity(httpEntity);
            }
        }
    }
}
