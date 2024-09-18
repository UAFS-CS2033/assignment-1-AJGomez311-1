
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int portNo;

    public Server(int portNo) {
        this.portNo = portNo;
    }

    private void processConnection() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        /*  //*** Application Protocol *****
        String buffer = in.readLine();
        while(buffer.length() != 0){
            System.out.println(buffer);
            buffer = in.readLine();
        }
        out.printf("HTTP/1.1 200 OK\n");
        out.printf("Content-Length: 34\n");
        out.printf("Content-Type: text/html\n\n");
        out.printf("<h1>Welcome to the web Server</h1>");

        in.close();
        out.close();
         */
        String request = in.readLine();
        if(request != null && !request.isEmpty()){
            System.out.println("The request: " + request);

        }
        

        String[] requestPart = request.split(" ");
        if(requestPart.length >=2 && requestPart[0].equals("GET")){
            String requestedFile = requestPart[1].substring(1);
            if(requestedFile.isEmpty()){
                requestedFile = "home.html";
            }
        

        File file = new File("/home/student/projects/assignment-1-AJGomez311-1/docroot/" + requestedFile);  
        if(file.exists() && !file.isDirectory()){
            out.printf("HTTP/1.1 200 OK\n");
            out.printf("Content-Length: " + file.length() + "\n");
            out.printf("Content-Type: text/html\n\n");

            BufferedReader fr = new BufferedReader(new FileReader(file));
            String fl;
            while((fl = fr.readLine()) != null){
                out.println(fl);
            }
            fr.close();

        }else{
            out.printf("HTTP/1.1 404 Not Found\n");
            out.printf("Content-Type: text/html\n\n");
            out.printf("<h1>404 Not Found</h1>");
        }
        }
        
        in.close();
        out.close();

    }

    public void run() throws IOException {
        boolean running = true;

        serverSocket = new ServerSocket(portNo);
        System.out.printf("Listen on Port: %d\n", portNo);
        while (running) {
            clientSocket = serverSocket.accept();
            //** Application Protocol
            processConnection();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static void main(String[] args0) throws IOException {
        Server server = new Server(8080);
        server.run();
    }
}
