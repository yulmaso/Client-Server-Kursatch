import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int SERVER_PORT=1234; //порт, который будет прослушиваться сервером
    private static final int MAX_CLIENTS=50; //максимальное количество подключенных клиентов
    private ServerSocket listenSocket; //сокет
    private boolean keepRunning = true;

    //запуск приложения
    public static void main(String args[]) {
        Server server = new Server();
        server.serverQuotes();
    }

    public Server()
    {
        System.out.print("Вас приветствует консоль сервера программы контроля контроллеров Mitsubishi Electric...\n");
        try
        {
            listenSocket = new ServerSocket(SERVER_PORT, MAX_CLIENTS); //создать сокет
        }
        catch(IOException except)
        {
            System.err.println("Unable to listen on port "+SERVER_PORT);
            System.exit(1);
        }
    }

    //Этот метод ожидает обращение от клиента
    public void serverQuotes()
    {
        Socket clientSocket;
        try
        {
            while(keepRunning)
            {
                clientSocket = listenSocket.accept(); //присоединить нового клиента
                //Создать новый обработчик
                QueryHandler newHandler = new QueryHandler(clientSocket);
                newHandler.start();
            }
            listenSocket.close();
        } catch(IOException e)
        {
            System.err.println("Failed I/O "+e);
        }
    }

}
