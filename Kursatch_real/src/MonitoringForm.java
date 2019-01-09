import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MonitoringForm extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel panelMain;
    private JPanel Statistics;
    private JPanel Unloading;
    private JPanel ViewingApps;
    private JButton ButtonPushingRight;
    private JButton ButtonPushingLeft;
    private JButton unloadButton;
    private JCheckBox wholeSystemCheckBox;
    private JTable applicationsTable;
    private JTextArea textArea1;
    private JButton changeButton;
    private JRadioButton doneRadioButton;
    private JRadioButton didntAcceptedRadioButton;
    private JRadioButton onConsiderationRadioButton;
    private JButton addButton2;
    private JButton removeButton2;
    private JComboBox formatComboBox;
    private JList chosenModules;
    private JList availibleModules;
    private JList modulesList;
    private JButton resetButton;
    private JButton updateButton;
    private JButton chooseFileButton;
    private JButton superPuperUpdateButton;
    private JButton renewButton1;
    private JButton chooseFileButton2;
    private JTextField firstNameTextField;
    private JTextField secondNameTextField;
    private JComboBox positionComboBox4;
    private JTextField emailTextField;
    private JButton AddButton1;
    private JButton editButton;
    private JButton deleteButton1;
    private JPanel UpdateTab;
    private JPanel UsersTab;
    private JTextField passwordTextField;
    private JTextField loginTextField;
    private JComboBox listOfUsers;
    private JLabel loginField;
    private JLabel passwordField;
    private JLabel nameField;
    private JLabel secondNameField;
    private JLabel emailField;
    private JLabel positionField;
    private JPanel ModulesTab;
    private JTextArea idTextArea;
    private JTextField nameTextArea;
    private JTextField serialTextArea;
    private JTextField dateTextArea;
    private JButton addModuleButton;
    private JTable modulesTable;
    private JButton editModuleButton;
    private JButton deleteModuleButton;
    private JPanel applicationCreatorPanel;
    private JLabel accountCreatingLabel;
    private JButton editButton1;
    private JTextField maxTemperatureField;
    private JTextField maxFrequencyField;
    private JTextField maxMemoryField;
    private JComboBox modulesComboBox;
    private JComboBox updateFrequency;
    private JComboBox moduleStateComboBox;
    private JTable modulesInfoTable;
    private static Logger log = Logger.getLogger(MonitoringForm.class.getName());
    private static Object[] moduleHeaders = {"ID", "Название", "Серийный номер", "Дата установки", "Пор.Температура", "Пор.Частота", "Пор.Память", "Частота обновления"};
    private static Object[] applicationHeaders = {"ID", "Заявитель", "Статус", "Замена", "Приоритет"};
    private static Object[] moduleInfoHeaders = {"Дата", "Температура", "Частота", "Память"};
    private static boolean temperature = false;
    private static boolean frequency = false;
    private static boolean memory = false;
    private static List<String> choosedModules = new ArrayList<>();
    private static List<String> modulesNames = ServerQuery.queryPerform(new Message("ReadModulesNames")).getArguments();

    public void setEngineersForm() {
        tabbedPane.remove(ModulesTab);
        tabbedPane.remove(UsersTab);
    }

    public void setOwnersForm() {
        tabbedPane.remove(UpdateTab);
    }

    private void updatingUsersList() {
        List<String> items = ServerQuery.queryPerform(new Message("ReadUsers")).getArguments();
        DefaultComboBoxModel model = (DefaultComboBoxModel) listOfUsers.getModel();
        model.removeAllElements();

        for (String item : items) {
            model.addElement(item);
        }

        listOfUsers.setModel(model);
    }

    private void updatingUsersInfo() {
        String item = (String) listOfUsers.getSelectedItem();

        if (item != null) {
            List<String> info = ServerQuery.queryPerform(new Message("ReadUsersData", item)).getArguments();

            loginField.setText(info.get(0));
            passwordField.setText(info.get(1));
            nameField.setText(info.get(2));
            secondNameField.setText((info.get(3)));
            emailField.setText(info.get(4));
            positionField.setText(info.get(5));
        }
    }

    private void updatingModulesTable() {
        List<Object[]> data = ServerQuery.queryPerform(new Message("ReadModules")).getTableArguments();
        DefaultTableModel model = (DefaultTableModel) modulesTable.getModel();
        model.setRowCount(0);
        model.setColumnIdentifiers(moduleHeaders);
        for (int i = 0; i < data.size(); i++) model.addRow(data.get(i));

        modulesTable.setModel(model);
    }

    private void updatingModulesList() {
        List<String> items = ServerQuery.queryPerform(new Message("ReadModulesNames")).getArguments();
        DefaultListModel model = (DefaultListModel) modulesList.getModel();
        DefaultComboBoxModel model1 = (DefaultComboBoxModel) modulesComboBox.getModel();
        DefaultComboBoxModel model2 = (DefaultComboBoxModel) moduleStateComboBox.getModel();
        model.removeAllElements();
        model1.removeAllElements();
        model2.removeAllElements();

        for (String item : items) {
            model.addElement(item);
            model1.addElement(item);
            model2.addElement(item);
        }

        modulesList.setModel(model);
        modulesComboBox.setModel(model1);
        moduleStateComboBox.setModel(model2);
    }

    public void updatingApplicationsTable() {
        List<Object[]> data = ServerQuery.queryPerform(new Message("ReadApplications", String.valueOf(modulesComboBox.getSelectedItem()))).getTableArguments();
        DefaultTableModel model = (DefaultTableModel) applicationsTable.getModel();
        model.setRowCount(0);
        model.setColumnIdentifiers(applicationHeaders);
        for (int i = 0; i < data.size(); i++) model.addRow(data.get(i));
    }

    private void applicationsCreatorCall() {
        Main.openApplicationCreator(String.valueOf(modulesComboBox.getSelectedItem()), this);
    }

    private void updatingUnloadLists() {

        DefaultListModel model = (DefaultListModel) availibleModules.getModel();
        DefaultListModel model1 = (DefaultListModel) chosenModules.getModel();
        model.removeAllElements();
        model1.removeAllElements();

        for (String item : choosedModules) {
            model1.addElement(item);
        }

        for (String item : modulesNames) {
            model.addElement(item);
        }

        availibleModules.setModel(model);
        chosenModules.setModel(model1);
    }

    private void updatingModulesInfoTable() {
        List<Object[]> data = ServerQuery.queryPerform(new Message("ReadModulesInfo", String.valueOf(moduleStateComboBox.getSelectedItem()))).getTableArguments();
        DefaultTableModel model = (DefaultTableModel) modulesInfoTable.getModel();
        model.setRowCount(0);
        model.setColumnIdentifiers(moduleInfoHeaders);
        for (int i = 0; i < data.size(); i++) model.addRow(data.get(i));

        modulesInfoTable.setModel(model);
    }

    public MonitoringForm() {
        $$$setupUI$$$();
        tabbedPane.remove(Statistics);
        updatingUsersList();
        updatingUsersInfo();
        updatingModulesTable();
        updatingModulesList();
        updatingApplicationsTable();
        updatingUnloadLists();
        updatingModulesInfoTable();

        add(panelMain);
        setSize(700, 450);
        setTitle("Monitoring");

        final ButtonGroup group = new ButtonGroup();
        group.add(doneRadioButton);
        group.add(didntAcceptedRadioButton);
        group.add(onConsiderationRadioButton);
        doneRadioButton.setEnabled(false);
        didntAcceptedRadioButton.setEnabled(false);
        onConsiderationRadioButton.setEnabled(false);

        AddButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login, password, firstName, secondName, email, position;
                login = loginTextField.getText();
                loginTextField.setText("");
                password = passwordTextField.getText();
                passwordTextField.setText("");
                firstName = firstNameTextField.getText();
                firstNameTextField.setText("");
                secondName = secondNameTextField.getText();
                secondNameField.setText("");
                email = emailTextField.getText();
                emailTextField.setText("");
                position = (String) positionComboBox4.getSelectedItem();

                List<String> args = new ArrayList<>();
                args.add(login);
                args.add(password);
                args.add(firstName);
                args.add(secondName);
                args.add(email);
                args.add(position);
                ServerQuery.queryPerform(new Message("CreateUser", args));
                updatingUsersList();

                Main.openNotificationForm("Новый пользователь добавлен");
            }
        });

        listOfUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatingUsersInfo();
            }
        });
        deleteButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ServerQuery.queryPerform(new Message("RemoveUser", String.valueOf(listOfUsers.getSelectedItem())));
                updatingUsersList();
            }
        });
        addModuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> arguments = new ArrayList<>();
                arguments.add(String.valueOf(nameTextArea.getText()));
                arguments.add(String.valueOf(serialTextArea.getText()));
                arguments.add(String.valueOf(dateTextArea.getText()));
                arguments.add(String.valueOf(maxTemperatureField.getText()));
                arguments.add(String.valueOf(maxFrequencyField.getText()));
                arguments.add(String.valueOf(maxMemoryField.getText()));
                arguments.add(updateFrequency.getSelectedItem().toString());
                ServerQuery.queryPerform(new Message("CreateModule", arguments));
                updatingModulesTable();
                updatingModulesList();
                choosedModules.clear();
                modulesNames = ServerQuery.queryPerform(new Message("ReadModulesNames")).getArguments();
                updatingUnloadLists();
                wholeSystemCheckBox.setSelected(false);
            }
        });
        deleteModuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> arguments = new ArrayList<>();
                arguments.add(String.valueOf(modulesTable.getValueAt(modulesTable.getSelectedRow(), 0)));
                arguments.add(String.valueOf(modulesTable.getValueAt(modulesTable.getSelectedRow(), 1)));
                ServerQuery.queryPerform(new Message("RemoveModule", arguments));
                updatingModulesTable();
                updatingModulesList();
                choosedModules.clear();
                modulesNames = ServerQuery.queryPerform(new Message("ReadModulesNames")).getArguments();
                updatingUnloadLists();
                wholeSystemCheckBox.setSelected(false);
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                List<String> data = ServerQuery.queryPerform(new Message("ReadUsersData", String.valueOf(listOfUsers.getSelectedItem()))).getArguments();
                loginTextField.setText(data.get(0));
                loginTextField.setEnabled(false);
                passwordTextField.setText(data.get(1));
                firstNameTextField.setText(data.get(2));
                secondNameTextField.setText(data.get(3));
                emailTextField.setText(data.get(4));
                positionComboBox4.setSelectedItem(data.get(5));
                accountCreatingLabel.setText("Изменение аккаунта");
                AddButton1.setEnabled(false);
                editButton1.setVisible(true);
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.openNotificationForm("Прошивка на вашем модуле успешно сброшена!");
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.openNotificationForm("Обновлено!");
            }
        });
        superPuperUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.openNotificationForm("У вас очень хорошая прошивка");
            }
        });
        renewButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.openNotificationForm("Обновлено!");
            }
        });
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) fileopen.hide();
            }
        });
        chooseFileButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) fileopen.hide();
            }
        });

        editButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> arguments = new ArrayList<>();
                arguments.add(String.valueOf(loginTextField.getText()));
                arguments.add(String.valueOf(passwordTextField.getText()));
                arguments.add(String.valueOf(firstNameTextField.getText()));
                arguments.add(String.valueOf(secondNameTextField.getText()));
                arguments.add(String.valueOf(emailTextField.getText()));
                arguments.add(String.valueOf(positionComboBox4.getSelectedItem()));
                ServerQuery.queryPerform(new Message("ChangeUsersData", arguments));

                loginTextField.setEnabled(true);
                editButton1.setVisible(false);
                AddButton1.setEnabled(true);
                accountCreatingLabel.setText("Создание Аккаунта");
                loginTextField.setText("");
                passwordTextField.setText("");
                firstNameTextField.setText("");
                secondNameTextField.setText("");
                emailTextField.setText("");
                updatingUsersInfo();
                Main.openNotificationForm("Данные успешно изменены");
            }
        });
        addButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applicationsCreatorCall();
            }
        });
        removeButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<String> arguments = new ArrayList<>();
                    arguments.add(String.valueOf(modulesComboBox.getSelectedItem()));
                    arguments.add(String.valueOf(applicationsTable.getValueAt(applicationsTable.getSelectedRow(), 0)));
                    ServerQuery.queryPerform(new Message("RemoveApplication", arguments));
                    updatingApplicationsTable();
                } catch (ArrayIndexOutOfBoundsException except) {
                    System.err.println("Затруднение: " + except);
                    Main.openNotificationForm("Заявки отсутствуют");
                }
            }
        });
        applicationsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (applicationsTable.getSelectedRow() > -1) {
                    List<String> arguments = new ArrayList<>();
                    arguments.add(String.valueOf(modulesComboBox.getSelectedItem()));
                    arguments.add(String.valueOf(applicationsTable.getValueAt(applicationsTable.getSelectedRow(), 0)));
                    String info = ServerQuery.queryPerform(new Message("ReadApplicationsInfo", arguments)).getMessage();
                    textArea1.setText(info);
                    doneRadioButton.setEnabled(true);
                    didntAcceptedRadioButton.setEnabled(true);
                    onConsiderationRadioButton.setEnabled(true);
                    String status = String.valueOf(applicationsTable.getValueAt(applicationsTable.getSelectedRow(), 2));
                    if (status.equals("Выполнена")) group.setSelected(doneRadioButton.getModel(), true);
                    else if (status.equals("Не принята")) group.setSelected(didntAcceptedRadioButton.getModel(), true);
                    else if (status.equals("На рассмотрении"))
                        group.setSelected(onConsiderationRadioButton.getModel(), true);
                }
            }
        });
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String status = null;
                    if (doneRadioButton.isSelected()) status = "Выполнена";
                    else if (didntAcceptedRadioButton.isSelected()) status = "Не принята";
                    else if (onConsiderationRadioButton.isSelected()) status = "На рассмотрении";
                    List<String> arguments = new ArrayList<>();
                    arguments.add(String.valueOf(modulesComboBox.getSelectedItem()));
                    arguments.add(status);
                    arguments.add(String.valueOf(textArea1.getText()));
                    arguments.add(String.valueOf(applicationsTable.getValueAt(applicationsTable.getSelectedRow(), 0)));
                    ServerQuery.queryPerform(new Message("ChangeApplicationsData", arguments));
                    doneRadioButton.setEnabled(false);
                    didntAcceptedRadioButton.setEnabled(false);
                    onConsiderationRadioButton.setEnabled(false);
                    updatingApplicationsTable();
                } catch (ArrayIndexOutOfBoundsException except) {
                    System.err.println("Затруднение: " + except);
                    Main.openNotificationForm("Заявки отсутствуют");
                }
            }
        });
        modulesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modulesComboBox.hidePopup();
                updatingApplicationsTable();
                if (applicationsTable.getRowCount() != 0) applicationsTable.setRowSelectionInterval(0, 0);
                else textArea1.setText("");
            }
        });
        ButtonPushingRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    choosedModules.add(availibleModules.getSelectedValue().toString());
                    modulesNames.remove(availibleModules.getSelectedValue());
                    updatingUnloadLists();
                } catch (NullPointerException r) {
                }
            }
        });
        ButtonPushingLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    modulesNames.add(chosenModules.getSelectedValue().toString());
                    choosedModules.remove(chosenModules.getSelectedValue());
                    updatingUnloadLists();
                } catch (NullPointerException r) {
                }
            }
        });
        wholeSystemCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (wholeSystemCheckBox.isSelected()) {
                    choosedModules.addAll(modulesNames);
                    modulesNames.clear();
                    updatingUnloadLists();
                } else {
                    modulesNames.addAll(choosedModules);
                    choosedModules.clear();
                    updatingUnloadLists();
                }
            }
        });
        unloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Unload unload = new Unload();
                List<String> names = new ArrayList<>();
                for (int i = 0; i < chosenModules.getModel().getSize(); i++) {
                    names.add(chosenModules.getModel().getElementAt(i).toString());
                }
                switch (formatComboBox.getSelectedItem().toString()) {
                    case "XML": {
                        unload.toXML(names);
                    }
                    break;

                    case "XSL": {
                        unload.toXSL(names);
                    }
                    break;

                    case "CSV": {
                        unload.toCSV(names);
                    }
                    break;
                }
            }
        });
        moduleStateComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moduleStateComboBox.hidePopup();
                updatingModulesInfoTable();
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        // final DefaultListModel defaultListModel1 = new DefaultListModel();
        // availibleModules.setModel(defaultListModel1);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1, true, true));
        tabbedPane = new JTabbedPane();
        panelMain.add(tabbedPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        Unloading = new JPanel();
        Unloading.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Выгрузка", Unloading);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:214px:grow,left:5dlu:noGrow,fill:190px:grow,left:4dlu:noGrow,fill:d:grow", "center:d:grow"));
        Unloading.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        CellConstraints cc = new CellConstraints();
        panel1.add(panel2, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        availibleModules = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        availibleModules.setModel(defaultListModel1);
        scrollPane1.setViewportView(availibleModules);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, cc.xy(3, 1));
        ButtonPushingRight = new JButton();
        ButtonPushingRight.setText(">>>>>");
        panel3.add(ButtonPushingRight, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ButtonPushingLeft = new JButton();
        ButtonPushingLeft.setText("<<<<<");
        panel3.add(ButtonPushingLeft, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, cc.xy(5, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        wholeSystemCheckBox = new JCheckBox();
        wholeSystemCheckBox.setText("По всей системе");
        panel4.add(wholeSystemCheckBox, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel4.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        chosenModules = new JList();
        final DefaultListModel defaultListModel2 = new DefaultListModel();
        chosenModules.setModel(defaultListModel2);
        scrollPane2.setViewportView(chosenModules);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        Unloading.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel5.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel5.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Выбрать формат: ");
        panel5.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(112, 18), null, 0, false));
        unloadButton = new JButton();
        unloadButton.setText("Выгрузить");
        panel5.add(unloadButton, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(144, 30), null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        panel5.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        formatComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("XML");
        defaultComboBoxModel1.addElement("XSL");
        defaultComboBoxModel1.addElement("CSV");
        formatComboBox.setModel(defaultComboBoxModel1);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel6.add(formatComboBox, gbc);
        ViewingApps = new JPanel();
        ViewingApps.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Просмотр заявок", ViewingApps);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        ViewingApps.add(panel7, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(154, 285), null, 0, false));
        final JScrollPane scrollPane3 = new JScrollPane();
        panel7.add(scrollPane3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        applicationsTable = new JTable();
        applicationsTable.setAutoCreateRowSorter(true);
        scrollPane3.setViewportView(applicationsTable);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        ViewingApps.add(panel8, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new FormLayout("fill:154px:noGrow,left:4dlu:noGrow,fill:189px:grow", "center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        panel8.add(panel9, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 4, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textArea1 = new JTextArea();
        panel9.add(textArea1, cc.xywh(3, 1, 1, 2, CellConstraints.FILL, CellConstraints.FILL));
        changeButton = new JButton();
        changeButton.setText("Изменить");
        panel9.add(changeButton, cc.xy(3, 5));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridBagLayout());
        panel9.add(panel10, cc.xywh(1, 1, 1, 5));
        addButton2 = new JButton();
        addButton2.setText("Добавить");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel10.add(addButton2, gbc);
        removeButton2 = new JButton();
        removeButton2.setText("Удалить");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel10.add(removeButton2, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Модули");
        panel8.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel8.add(panel11, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        applicationCreatorPanel = new JPanel();
        applicationCreatorPanel.setLayout(new GridBagLayout());
        panel8.add(applicationCreatorPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 4, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        doneRadioButton = new JRadioButton();
        doneRadioButton.setText("Выполнена");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        applicationCreatorPanel.add(doneRadioButton, gbc);
        didntAcceptedRadioButton = new JRadioButton();
        didntAcceptedRadioButton.setText("Не принята");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        applicationCreatorPanel.add(didntAcceptedRadioButton, gbc);
        onConsiderationRadioButton = new JRadioButton();
        onConsiderationRadioButton.setText("На рассмотрении");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        applicationCreatorPanel.add(onConsiderationRadioButton, gbc);
        modulesComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        modulesComboBox.setModel(defaultComboBoxModel2);
        panel8.add(modulesComboBox, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        UpdateTab = new JPanel();
        UpdateTab.setLayout(new FormLayout("fill:m:noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:noGrow", "center:d:noGrow,top:4dlu:noGrow,center:111px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;99px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        tabbedPane.addTab("Обновление", UpdateTab);
        final JLabel label3 = new JLabel();
        label3.setText("Выберите модули, которые вы хотите обновить");
        UpdateTab.add(label3, cc.xy(3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        UpdateTab.add(panel12, cc.xy(3, 3));
        modulesList = new JList();
        final DefaultListModel defaultListModel3 = new DefaultListModel();
        modulesList.setModel(defaultListModel3);
        panel12.add(modulesList, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        UpdateTab.add(panel13, cc.xy(3, 5));
        resetButton = new JButton();
        resetButton.setText("Сброс ");
        panel13.add(resetButton);
        updateButton = new JButton();
        updateButton.setText("Обновление");
        panel13.add(updateButton);
        chooseFileButton = new JButton();
        chooseFileButton.setText("Выбрать файл");
        panel13.add(chooseFileButton);
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        UpdateTab.add(panel14, cc.xy(3, 7));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        UpdateTab.add(spacer5, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        UpdateTab.add(spacer6, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        UpdateTab.add(panel15, cc.xy(3, 11));
        superPuperUpdateButton = new JButton();
        superPuperUpdateButton.setText("Прошивка");
        panel15.add(superPuperUpdateButton);
        renewButton1 = new JButton();
        renewButton1.setText("Обновление");
        panel15.add(renewButton1);
        chooseFileButton2 = new JButton();
        chooseFileButton2.setText("Выбрать файл");
        panel15.add(chooseFileButton2);
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        UpdateTab.add(panel16, cc.xy(3, 9));
        panel16.setBorder(BorderFactory.createTitledBorder(""));
        final JLabel label4 = new JLabel();
        label4.setText("Ver.1.0 SPbGETU PPA tech inc.");
        panel16.add(label4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ModulesTab = new JPanel();
        ModulesTab.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:noGrow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:21px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:21px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        tabbedPane.addTab("Модули", ModulesTab);
        final com.intellij.uiDesigner.core.Spacer spacer7 = new com.intellij.uiDesigner.core.Spacer();
        ModulesTab.add(spacer7, cc.xy(1, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        final com.intellij.uiDesigner.core.Spacer spacer8 = new com.intellij.uiDesigner.core.Spacer();
        ModulesTab.add(spacer8, cc.xy(9, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label5 = new JLabel();
        label5.setText("Дата установки");
        ModulesTab.add(label5, cc.xy(7, 3));
        final JLabel label6 = new JLabel();
        label6.setText("Серийный номер");
        ModulesTab.add(label6, cc.xy(5, 3));
        final JLabel label7 = new JLabel();
        label7.setText("Название");
        ModulesTab.add(label7, cc.xy(3, 3));
        maxTemperatureField = new JTextField();
        ModulesTab.add(maxTemperatureField, cc.xy(3, 9, CellConstraints.FILL, CellConstraints.FILL));
        maxFrequencyField = new JTextField();
        ModulesTab.add(maxFrequencyField, cc.xy(5, 9, CellConstraints.FILL, CellConstraints.FILL));
        maxMemoryField = new JTextField();
        ModulesTab.add(maxMemoryField, cc.xy(7, 9, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label8 = new JLabel();
        label8.setText("Пороговая температура (C)");
        ModulesTab.add(label8, cc.xy(3, 7));
        final JLabel label9 = new JLabel();
        label9.setText("Пороговая частота (МГц)");
        ModulesTab.add(label9, cc.xy(5, 7));
        final JLabel label10 = new JLabel();
        label10.setText("Пороговая память (Гб)");
        ModulesTab.add(label10, cc.xy(7, 7));
        nameTextArea = new JTextField();
        ModulesTab.add(nameTextArea, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.FILL));
        serialTextArea = new JTextField();
        ModulesTab.add(serialTextArea, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.FILL));
        dateTextArea = new JTextField();
        dateTextArea.setText("");
        ModulesTab.add(dateTextArea, cc.xy(7, 5, CellConstraints.FILL, CellConstraints.FILL));
        final JScrollPane scrollPane4 = new JScrollPane();
        ModulesTab.add(scrollPane4, cc.xyw(3, 13, 5, CellConstraints.FILL, CellConstraints.FILL));
        modulesTable = new JTable();
        scrollPane4.setViewportView(modulesTable);
        updateFrequency = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("Каждые полчаса");
        defaultComboBoxModel3.addElement("Каждый час");
        defaultComboBoxModel3.addElement("Каждые два часа");
        defaultComboBoxModel3.addElement("Каждый день");
        defaultComboBoxModel3.addElement("Каждые два дня");
        defaultComboBoxModel3.addElement("Каждую неделю");
        updateFrequency.setModel(defaultComboBoxModel3);
        updateFrequency.setName("Частота обновления");
        ModulesTab.add(updateFrequency, cc.xy(3, 11));
        addModuleButton = new JButton();
        addModuleButton.setText("Добавить модуль");
        ModulesTab.add(addModuleButton, cc.xy(7, 11));
        deleteModuleButton = new JButton();
        deleteModuleButton.setText("Удалить модуль");
        ModulesTab.add(deleteModuleButton, cc.xy(3, 15));
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Состояние", panel17);
        final JPanel panel18 = new JPanel();
        panel18.setLayout(new FormLayout("fill:m:noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:m:noGrow", "center:d:noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:d:noGrow"));
        panel17.add(panel18, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        moduleStateComboBox = new JComboBox();
        panel18.add(moduleStateComboBox, cc.xy(3, 1));
        final com.intellij.uiDesigner.core.Spacer spacer9 = new com.intellij.uiDesigner.core.Spacer();
        panel18.add(spacer9, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final com.intellij.uiDesigner.core.Spacer spacer10 = new com.intellij.uiDesigner.core.Spacer();
        panel18.add(spacer10, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final com.intellij.uiDesigner.core.Spacer spacer11 = new com.intellij.uiDesigner.core.Spacer();
        panel18.add(spacer11, cc.xy(7, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final com.intellij.uiDesigner.core.Spacer spacer12 = new com.intellij.uiDesigner.core.Spacer();
        panel18.add(spacer12, cc.xyw(3, 5, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JScrollPane scrollPane5 = new JScrollPane();
        panel18.add(scrollPane5, cc.xyw(3, 3, 3, CellConstraints.FILL, CellConstraints.FILL));
        modulesInfoTable = new JTable();
        scrollPane5.setViewportView(modulesInfoTable);
        UsersTab = new JPanel();
        UsersTab.setLayout(new FormLayout("fill:min(d;96px):grow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:d:grow"));
        tabbedPane.addTab("Пользователи", UsersTab);
        final JPanel panel19 = new JPanel();
        panel19.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;64px):grow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        UsersTab.add(panel19, cc.xywh(1, 1, 1, 3, CellConstraints.DEFAULT, CellConstraints.TOP));
        final JLabel label11 = new JLabel();
        label11.setText("Существующие аккаунты");
        panel19.add(label11, cc.xyw(1, 1, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));
        listOfUsers = new JComboBox();
        panel19.add(listOfUsers, cc.xyw(1, 3, 3));
        loginField = new JLabel();
        loginField.setText("Label");
        panel19.add(loginField, cc.xy(3, 5));
        final JLabel label12 = new JLabel();
        label12.setHorizontalTextPosition(4);
        label12.setText("Должность:");
        panel19.add(label12, cc.xy(1, 15));
        passwordField = new JLabel();
        passwordField.setText("Label");
        panel19.add(passwordField, cc.xy(3, 7));
        nameField = new JLabel();
        nameField.setText("Label");
        panel19.add(nameField, cc.xy(3, 9));
        secondNameField = new JLabel();
        secondNameField.setText("Label");
        panel19.add(secondNameField, cc.xy(3, 11));
        positionField = new JLabel();
        positionField.setText("Label");
        panel19.add(positionField, cc.xy(3, 15));
        final JLabel label13 = new JLabel();
        label13.setText("Email:");
        panel19.add(label13, cc.xy(1, 13));
        emailField = new JLabel();
        emailField.setText("Label");
        panel19.add(emailField, cc.xy(3, 13));
        final JLabel label14 = new JLabel();
        label14.setHorizontalTextPosition(2);
        label14.setText("Логин:");
        panel19.add(label14, cc.xy(1, 5));
        final JLabel label15 = new JLabel();
        label15.setHorizontalTextPosition(4);
        label15.setText("Имя:");
        panel19.add(label15, cc.xy(1, 9));
        final JLabel label16 = new JLabel();
        label16.setHorizontalTextPosition(4);
        label16.setText("Пароль:");
        panel19.add(label16, cc.xy(1, 7));
        final JLabel label17 = new JLabel();
        label17.setHorizontalTextPosition(4);
        label17.setText("Фамилия:");
        panel19.add(label17, cc.xy(1, 11));
        final JPanel panel20 = new JPanel();
        panel20.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:min(d;58px):grow,left:4dlu:noGrow,fill:d:noGrow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        UsersTab.add(panel20, cc.xy(3, 1, CellConstraints.DEFAULT, CellConstraints.TOP));
        accountCreatingLabel = new JLabel();
        accountCreatingLabel.setText("Создание аккаунта");
        panel20.add(accountCreatingLabel, cc.xy(3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
        firstNameTextField = new JTextField();
        firstNameTextField.setText("");
        firstNameTextField.setToolTipText("Имя");
        panel20.add(firstNameTextField, cc.xy(3, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        final com.intellij.uiDesigner.core.Spacer spacer13 = new com.intellij.uiDesigner.core.Spacer();
        panel20.add(spacer13, cc.xy(5, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        final com.intellij.uiDesigner.core.Spacer spacer14 = new com.intellij.uiDesigner.core.Spacer();
        panel20.add(spacer14, cc.xy(1, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        secondNameTextField = new JTextField();
        secondNameTextField.setText("");
        secondNameTextField.setToolTipText("Фамилия");
        panel20.add(secondNameTextField, cc.xy(3, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        positionComboBox4 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        defaultComboBoxModel4.addElement("Инжинер");
        defaultComboBoxModel4.addElement("Администратор");
        positionComboBox4.setModel(defaultComboBoxModel4);
        positionComboBox4.setToolTipText("Должность");
        panel20.add(positionComboBox4, cc.xy(3, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        emailTextField = new JTextField();
        emailTextField.setText("");
        emailTextField.setToolTipText("Email");
        panel20.add(emailTextField, cc.xy(3, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        passwordTextField = new JTextField();
        passwordTextField.setToolTipText("Password");
        panel20.add(passwordTextField, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        loginTextField = new JTextField();
        loginTextField.setToolTipText("Login");
        panel20.add(loginTextField, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        editButton1 = new JButton();
        editButton1.setEnabled(true);
        editButton1.setText("Изменить");
        editButton1.setVisible(false);
        panel20.add(editButton1, cc.xy(3, 15));
        final JPanel panel21 = new JPanel();
        panel21.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        UsersTab.add(panel21, cc.xywh(3, 3, 1, 3, CellConstraints.DEFAULT, CellConstraints.BOTTOM));
        AddButton1 = new JButton();
        AddButton1.setText("Добавить");
        panel21.add(AddButton1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel22 = new JPanel();
        panel22.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        UsersTab.add(panel22, cc.xy(1, 5, CellConstraints.DEFAULT, CellConstraints.BOTTOM));
        editButton = new JButton();
        editButton.setText("Редактировать");
        panel22.add(editButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteButton1 = new JButton();
        deleteButton1.setText("Удалить");
        panel22.add(deleteButton1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
