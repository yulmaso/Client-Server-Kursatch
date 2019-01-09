import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Console;
import java.util.List;

public class Main {

    public static String LOGIN;

    public static void main(String[] args) {
        openAuthForm();

    }

    public static void openAuthForm(){
        AuthForm authForm = new AuthForm();
        authForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        authForm.setLocationRelativeTo(null);
        authForm.setVisible(true);

        authForm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                ServerQuery.quitServer();
                System.exit(0);
            }
        });
    }

    public static void openSession(String Login){
        List<String> data = ServerQuery.queryPerform(new Message("ReadUsersData", Login)).getArguments();
        LOGIN = data.get(0);
        String NAME = data.get(2);
        String SECONDNAME = data.get(3);
        String POSITION = data.get(5);

        if (POSITION.equals("Администратор")) openOwnersMonitoringForm();
        else if (POSITION.equals("Инжинер")) openEngineersMonitoringForm();
        else openNotificationForm(POSITION + " не имеет доступа");
    }

    public static void openOwnersMonitoringForm(){
        MonitoringForm monitoringForm = new MonitoringForm();
        monitoringForm.setOwnersForm();
        monitoringForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        monitoringForm.setLocationRelativeTo(null);
        monitoringForm.setVisible(true);

        monitoringForm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                ServerQuery.quitServer();
                System.exit(0);
            }
        });
    }

    public static void openEngineersMonitoringForm(){
        MonitoringForm monitoringForm = new MonitoringForm();
        monitoringForm.setEngineersForm();
        monitoringForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        monitoringForm.setLocationRelativeTo(null);
        monitoringForm.setVisible(true);

        monitoringForm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                ServerQuery.quitServer();
                System.exit(0);
            }
        });
    }

    public static void openApplicationCreator(String module, MonitoringForm monitoringForm){
        ApplicationCreator applicationCreator = new ApplicationCreator(module,LOGIN);
        applicationCreator.monitoringForm = monitoringForm;
        applicationCreator.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        applicationCreator.setLocationRelativeTo(null);
        applicationCreator.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        applicationCreator.setVisible(true);
    }

    public static void openNotificationForm(String s){
        NotificationForm notificationForm = new NotificationForm();
        notificationForm.giveText(s);
        notificationForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        notificationForm.setLocationRelativeTo(null);
        notificationForm.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        notificationForm.setVisible(true);
    }

}