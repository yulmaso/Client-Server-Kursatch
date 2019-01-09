

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ServerQuery {
    private static final int SERVER_PORT=1234; //сервер слушает этот порт
    private static Socket quoteSocket=null; //сокет
    private static ObjectInputStream inputStream; //поток очереди получения
    private static ObjectOutputStream outputStream; //поток очереди отправки
    private static boolean flag = false;

    //соединиться с сервером
    protected static void contactServer()
    {
        try
        {
            quoteSocket =  new Socket("localhost", SERVER_PORT); //открыть сокет на сервер
            inputStream = new ObjectInputStream(quoteSocket.getInputStream());
            outputStream = new ObjectOutputStream(quoteSocket.getOutputStream());

        } catch (IOException except)
        {
            System.err.println("Unknown host "+except);
        }
    }

    //сделать запрос на сервер
    public static Message queryPerform(Message message)
    {
        if (!flag) {
            contactServer();
        }
        Message result = null;
        if(connectionOK())
        {
            try
            {
                outputStream.writeObject(message);
                outputStream.flush();
                result = (Message) inputStream.readObject();
                try { if (Boolean.valueOf(result.getMessage()) && result.getArguments().get(0).equals("Connection Successful")) flag = !flag; }
                catch (IndexOutOfBoundsException e) {System.err.println("Failed I/O to server "+e);}


            } catch (IOException except)
            {
                System.err.println("Failed I/O to server "+except);
            } catch (ClassNotFoundException e){
                System.err.println(e);
            }
        } else {
            System.out.print("Соединение отсутствует\n");
        }
        if (result != null) return result;
        else return new Message("Что-то пошло не так");
    }

    //отключение от сервера
    public static void quitServer()
    {
        try
        {
            //если ссылки не равны NULL, то надо закрыть потоки и сокет
            if(outputStream!=null)  outputStream.close();
            if(inputStream!=null) inputStream.close();
            if(quoteSocket!=null) quoteSocket.close();
            flag = false;

        } catch (IOException except)
        {
            System.err.println("Failed I/O to server "+except);
        }
    }


    //проверить, что с соединением все в порядке
    protected static boolean connectionOK()
    {
        return (outputStream!=null && inputStream!=null && quoteSocket!=null);
    }
}
