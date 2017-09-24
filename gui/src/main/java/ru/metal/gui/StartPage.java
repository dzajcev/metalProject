package ru.metal.gui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ru.metal.api.contragents.request.ObtainContragentRequest;
import ru.metal.api.documents.DocumentStatus;
import ru.metal.api.documents.order.dto.OrderHeaderDto;
import ru.metal.api.documents.order.dto.OrderStatus;
import ru.metal.dto.OrderHeaderFx;
import ru.metal.gui.controllers.documents.DocumentJournalController;
import ru.metal.gui.controllers.documents.order.OrderDocumentSelector;
import ru.metal.gui.utils.SecurityChecker;
import ru.metal.security.ejb.UserContextHolder;
import ru.metal.exceptions.ExceptionShower;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.controllers.auth.AuthorizationController;
import ru.metal.gui.controllers.contragents.ContragentsForm;
import ru.metal.gui.controllers.nomenclature.NomenclatureForm;
import ru.metal.gui.controllers.documents.order.OrderController;
import ru.metal.gui.windows.MainFrame;
import ru.metal.gui.windows.Window;
import ru.metal.security.ejb.dto.Role;

import java.io.IOException;


public class StartPage extends Application {

    private final static MainFrame mainFrame = new MainFrame();

    public static Stage primaryStage;

    private void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(StartPage.class.getResource("/fxml/Authorization.fxml"));
        primaryStage.setTitle("Авторизация");
        Parent load = null;
        AuthorizationController authorizationController = null;
        try {
            load = loader.load();
            authorizationController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        authorizationController.doneProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals(AuthorizationController.AUTHORIZATION_ACCEPT)) {
                    authorizationAccept(primaryStage);
                }
            }
        });
        Scene scene = new Scene(load, 350, 300);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    private Window getParentWindow(Node node) {
        final Parent parent = node.getParent();
        if (parent != null) {
            if (parent instanceof Window) {
                return (Window) parent;
            } else {
                return getParentWindow(parent);
            }
        }
        return null;
    }

    public static void addWindow(Window window) {
        Window selectedWindow = getSelectedWindow();
        mainFrame.getWindows().add(mainFrame.getWindows().isEmpty() ? 0 : mainFrame.getWindows().size() - 1, window);
        if (selectedWindow == null) {
            window.setLayoutX(100);
            window.setLayoutY(100);
        } else {
            int deltaX = 40;
            int deltaY = 40;

            if (selectedWindow.getLayoutX() + window.getWidth() + deltaX > mainFrame.getContentPane().getWidth()) {
                deltaX = -40;
            }
            window.setLayoutX(selectedWindow.getLayoutX() + deltaX);
            window.setLayoutY(selectedWindow.getLayoutY() + deltaY);
        }


    }

    private static Window getSelectedWindow() {
        if (mainFrame.getWindows().isEmpty()) {
            return null;
        }
        for (Window window : mainFrame.getWindows()) {
            if (window.isSelected()) {
                return window;
            }
        }
        return null;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Window openContent(String fxml, Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(StartPage.class.getResource(fxml));

        Node load = null;
        AbstractController abstractController = null;
        try {
            load = loader.load();
            abstractController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Window window = openContent(load, abstractController, primaryStage);
        return window;
    }

    public static Window getWindow(String id) {
        for (Window w : mainFrame.getWindows()) {
            if (id.equals(w.getIdFrame())) {
                return w;
            }
        }
        return null;
    }

    public static Window openContent(Node form, AbstractController controller, Stage primaryStage) {
        if (controller != null) {
            controller.setPrimaryStage(primaryStage);
        }
        if (form == null) {
            return null;
        }
        Window window = null;
        if (mainFrame.getWindows().isEmpty()) {
            window = new Window();
            window.setContent(form, controller);

        } else {
            boolean isFinded = false;
            for (Window w : mainFrame.getWindows()) {
                if (form.getId() != null && form.getId().equals(w.getIdFrame())) {
                    w.setHide(false);
                    w.toFront();
                    isFinded = true;
                    window = w;
                    break;
                }
            }
            if (!isFinded) {
                window = new Window();
                window.setContent(form, controller);
            }
        }
        return window;
    }

    private void authorizationAccept(Stage primaryStage) {
        primaryStage.setResizable(true);
        primaryStage.setTitle("Управление (" + UserContextHolder.getPermissionContextDataThreadLocal().getUser().getShortName() + ")");
        Menu menuFile = new Menu("Файл");
        Menu create = new Menu("Создать");
        MenuItem createOrder = new MenuItem("Счет на оплату");
        createOrder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Window<OrderController, Node> window = openContent("/fxml/Order.fxml", primaryStage);
                if (window != null) {
                    window.getController().setOrder(new OrderHeaderDto());
                    window.setTitle("Счет на оплату");
                    window.setClosable(true);
                    window.setMinimizable(true);
                    addWindow(window);

                }
            }
        });
        create.getItems().add(createOrder);

        Menu settings = new Menu("Настройки");
        MenuItem settingProgram = new MenuItem("Служебные");
        settingProgram.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Window window = openContent("/fxml/BusinesSettings.fxml", primaryStage);
                if (window != null) {
                    window.setTitle("Служебные настройки");
                    window.setClosable(true);
                    window.setMinimizable(true);
                    addWindow(window);

                }
            }
        });
        MenuItem settingBusines = new MenuItem("Программа");
        settingBusines.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Window window = openContent("/fxml/OrganizationInfo.fxml", primaryStage);

                if (window != null) {
                    window.setTitle("Настройки программы");
                    window.setClosable(true);
                    window.setMinimizable(true);
                    addWindow(window);
                }
            }
        });
        settings.getItems().addAll(settingProgram, settingBusines);
        menuFile.getItems().addAll(create, settings);


        Menu menuDictionaries = new Menu("Справочники");
        MenuItem nomenclature = new MenuItem("Номенклатура");
        nomenclature.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Window<AbstractController, NomenclatureForm> window = StartPage.getWindow(NomenclatureForm.ID);

                if (window != null && window.getContent().isObtainMode()) {
                    window.setCloseRequest(true);
                    window = null;
                }
                if (window == null) {
                    NomenclatureForm nomenclatureForm = new NomenclatureForm(true);
                    window = openContent(nomenclatureForm, null, primaryStage);
                }
                if (window != null) {
                    window.setTitle("Номенклатура");
                    window.setClosable(true);
                    window.setMinimizable(true);
                    window.setMaximizable(true);
                    addWindow(window);
                }
            }
        });


        MenuItem contragents = new MenuItem("Контрагенты");
        contragents.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObtainContragentRequest obtainContragentRequest = new ObtainContragentRequest();
                obtainContragentRequest.setActive(true);

                Window<AbstractController, ContragentsForm> window = StartPage.getWindow(ContragentsForm.ID);

                if (window != null && window.getContent().isObtainMode()) {
                    window.setCloseRequest(true);
                    window = null;
                }
                if (window == null) {
                    ContragentsForm contragentsForm = new ContragentsForm(true, obtainContragentRequest);
                    window = openContent(contragentsForm, null, primaryStage);
                }
                if (window != null) {
                    window.setTitle("Контрагенты");
                    window.setClosable(true);
                    window.setMinimizable(true);
                    window.setMaximizable(true);
                    addWindow(window);
                }

            }
        });
        menuDictionaries.getItems().addAll(nomenclature, contragents);

        if (SecurityChecker.checkRoles(Role.ADMIN)) {
            if (!menuDictionaries.getItems().isEmpty()) {
                menuDictionaries.getItems().add(new SeparatorMenuItem());
            }
            MenuItem registrationRequestsMenuItem = new MenuItem("Журнал запросов на регистрацию");
            registrationRequestsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Window window = openContent("/fxml/RegistrationRequests.fxml", primaryStage);

                    if (window != null) {
                        window.setTitle("Журнал запросов на регистрацию");
                        window.setClosable(true);
                        window.setMinimizable(true);
                        window.setMaximizable(true);
                        addWindow(window);
                    }

                }
            });
            MenuItem usersMenuItem = new MenuItem("Пользователи");
            usersMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Window window = openContent("/fxml/Users.fxml", primaryStage);

                    if (window != null) {
                        window.setTitle("Пользователи");
                        window.setClosable(true);
                        window.setMinimizable(true);
                        window.setMaximizable(true);
                        addWindow(window);
                    }

                }
            });
            menuDictionaries.getItems().addAll(registrationRequestsMenuItem, usersMenuItem);
        }
        Menu menuJournals = new Menu("Журналы");
        MenuItem orderJournal = new MenuItem("Журнал счетов");
        orderJournal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Window<DocumentJournalController<OrderHeaderFx, OrderStatus>, Node> window = openContent("/fxml/DocumentJournal.fxml", primaryStage);

                if (window != null) {
                    DocumentJournalController<OrderHeaderFx, OrderStatus> controller = window.getController();

                    controller.setDocumentSelector(new OrderDocumentSelector());
                    window.setTitle("Журнал счетов");
                    window.setClosable(true);
                    window.setMinimizable(true);
                    window.setMaximizable(true);
                    addWindow(window);
                }

            }
        });
        menuJournals.getItems().addAll(orderJournal);
        mainFrame.addMenuItem(menuFile);
        mainFrame.addMenuItem(menuDictionaries);
        mainFrame.addMenuItem(menuJournals);
        final Scene scene = new Scene(mainFrame);
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                EventTarget target = mouseEvent.getTarget();
                if (target instanceof Node) {
                    Node targetNode = (Node) target;
                    Window parentWindow = getParentWindow(targetNode);
                    if (parentWindow != null) {
                        parentWindow.setSelected(true);
                        for (Window w : mainFrame.getWindows()) {
                            if (!w.equals(parentWindow)) {
                                w.setSelected(false);
                            }
                        }
                    }

                }

            }
        });
        primaryStage.setScene(scene);
    }

    private void showErrorDialog(Thread t, Throwable e) {
        ExceptionShower.showException("Произошла ошибка выполнения", e.getMessage());
    }

}