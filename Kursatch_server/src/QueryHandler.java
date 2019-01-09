import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//тред обмена с определенным клиентом
public class QueryHandler extends Thread {
    private Socket mySocket;
    private ObjectOutputStream outputStream = null;
    private ObjectInputStream inputStream = null;


    public QueryHandler(Socket newSocket)
    {
        mySocket = newSocket;
        try {
            outputStream = new ObjectOutputStream(this.mySocket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(this.mySocket.getInputStream());
            System.out.print("Подключение установлено...\n");
        } catch (IOException e){
            System.err.println("Failed I/O "+e);
        }

    }

    //поток, релизующий обмен данными
    public void run()
    {
        Message message;
        Boolean trustedUser = false;
        try
        {
            try {
                message = (Message) inputStream.readObject();
                System.out.print(" ЧИТАЮ ЗАПРОС \n");

                // case "CheckAuth"
                if (DatabaseOperator.logChecker(message.getArguments())) {
                    trustedUser = true;
                    outputStream.writeObject(new Message(String.valueOf(trustedUser), "Connection Successful"));
                    outputStream.flush();
                } else {
                    outputStream.writeObject(new Message(String.valueOf(trustedUser), "Wrong user's data"));
                    outputStream.flush();
                    this.stop();
                }

                while (trustedUser) {
                    message = (Message) inputStream.readObject();
                    System.out.print(" ЧИТАЮ ЗАПРОС \n");
                    switch (message.getMessage()) {

                        case "CreateUser": {
                            DatabaseOperator.userCreator(message.getArguments());
                            outputStream.writeObject(new Message("successful"));
                            outputStream.flush();
                        }
                        break;

                        case "ReadUsers": {
                            List<String> logins;
                            logins = DatabaseOperator.nameReader();
                            outputStream.writeObject(new Message("successful", logins));
                            outputStream.flush();
                        }
                        break;

                        case "ReadUsersData": {
                            List<String> data;
                            data = DatabaseOperator.usersDataReader(message.getArguments().get(0));
                            outputStream.writeObject(new Message("successful", data));
                            outputStream.flush();
                        }
                        break;

                        case "ChangeUsersData": {
                            DatabaseOperator.usersDataChanger(message.getArguments());
                            outputStream.writeObject(new Message("successful"));
                            outputStream.flush();
                        }
                        break;

                        case "RemoveUser": {
                            DatabaseOperator.userRemover(message.getArguments().get(0));
                            outputStream.writeObject(new Message("successful"));
                            outputStream.flush();
                        }
                        break;

                        case "CreateModule": {
                            DatabaseOperator.moduleCreator(message.getArguments());
                            outputStream.writeObject(new Message("successful"));
                            outputStream.flush();
                        }
                        break;

                        case "ReadModulesInfo": {
                            outputStream.writeObject(new Message(DatabaseOperator.modulesInfoReader(message.getArguments().get(0))));
                            outputStream.flush();
                        }
                        break;

                        case "ReadModules": {
                            message = new Message(DatabaseOperator.modulesReader());
                            outputStream.writeObject(message);
                            outputStream.flush();
                        }
                        break;

                        case "RemoveModule": {
                            DatabaseOperator.moduleRemover(message.getArguments());
                            outputStream.writeObject(new Message("successful"));
                            outputStream.flush();
                        }
                        break;

                        case "ReadModulesNames": {
                            outputStream.writeObject(new Message("successful", DatabaseOperator.modulesNamesReader()));
                            outputStream.flush();
                        }
                        break;

                        case "ReadApplications": {
                            outputStream.writeObject(new Message(DatabaseOperator.applicationsReader(message.getArguments().get(0))));
                            outputStream.flush();
                        }
                        break;

                        case "CreateApplication": {
                            DatabaseOperator.applicationCreator(message.getArguments());
                            outputStream.writeObject(new Message("successful"));
                            outputStream.flush();
                        }
                        break;

                        case "RemoveApplication": {
                            DatabaseOperator.applicationRemover(message.getArguments());
                            outputStream.writeObject(new Message("successful"));
                            outputStream.flush();
                        }
                        break;

                        case "ReadApplicationsInfo": {
                            outputStream.writeObject(new Message(DatabaseOperator.applicationInfoReader(message.getArguments())));
                            outputStream.flush();
                        }
                        break;

                        case "ChangeApplicationsData": {
                            DatabaseOperator.applicationDataChanger(message.getArguments());
                            outputStream.writeObject(new Message("successful"));
                            outputStream.flush();
                        }
                        break;

                        default: {
                            outputStream.writeObject(new Message("There is no such case"));
                        }

                    }
                }
            } catch (ClassNotFoundException e){
                System.err.println(e);
            }
        } catch(IOException e)
        {
            System.err.println("Failed I/O "+e);
        } finally
        {
            //закроем потоки и сокет
            try
            {
                if(outputStream!=null) outputStream.close();
                if(inputStream!=null) inputStream.close();
                if(mySocket!=null) mySocket.close();
                System.out.print("Подключение разорвано\n");
            } catch(IOException e)
            {
                System.err.println("Failed I/O "+e);
            }
        }
    }
}
