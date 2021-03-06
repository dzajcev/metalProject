package ru.metal.gui.windows;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.util.Iterator;
import java.util.List;

/**
 * Created by User on 08.08.2017.
 */
public class MainFrame extends VBox {
    private AnchorPane contentPane = new AnchorPane();
    private ObservableList<Window> windows = FXCollections.observableArrayList();
    private HBox bottomPane;
    private MenuBar menuBar;

    private BooleanProperty lock = new SimpleBooleanProperty();
    Menu windowsMenu = new Menu("Окна");

    public MainFrame() {
        init();
    }

    private void init() {
        setPrefSize(800, 600);
        initMenuBar();
        initContentPane();
        initBottomPane();
        lock.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                menuBar.setDisable(newValue);
            }
        });
        windows.addListener(new ListChangeListener<Window>() {
            @Override
            public void onChanged(Change<? extends Window> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (Window addedNode : c.getAddedSubList()) {
                            addedNode.setMainFrame(MainFrame.this);
                            if (addedNode.isModal()) {
                                Stage dialog = new Stage();
                                dialog.initOwner(getScene().getWindow());
                                dialog.setScene(new Scene(new AnchorPane(addedNode)));
                                dialog.setTitle(addedNode.getTitle());
                                dialog.initStyle(StageStyle.DECORATED);
                                dialog.initOwner(getScene().getWindow());
                                dialog.setResizable(false);
                                dialog.initModality(Modality.WINDOW_MODAL);
                                dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                    @Override
                                    public void handle(WindowEvent event) {

                                        addedNode.setCloseRequest(true);
                                        addedNode.setCloseRequest(false);
                                        event.consume();
                                    }
                                });
                                dialog.showAndWait();
                            } else {
                                contentPane.getChildren().add(addedNode);
                                addedNode.toFront();
                            }
                            MenuItem menuItem = new MenuItem(addedNode.getTitle());
                            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    addedNode.setMinimized(false);
                                    addedNode.toFront();
                                }
                            });
                            windowsMenu.getItems().add(menuItem);
                        }
                    } else if (c.wasRemoved()) {
                        for (Window removedNode : c.getRemoved()) {
                            if (removedNode.isModal()) {
                                lock.setValue(false);
                            }
                            removeNode(removedNode);
                            Iterator<MenuItem> iterator = windowsMenu.getItems().iterator();
                            while (iterator.hasNext()) {
                                MenuItem next = iterator.next();
                                if (removedNode.getTitle().equals(next.getText())) {
                                    iterator.remove();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void removeNode(Window removedNode) {
        removeNodeSub(removedNode, contentPane.getChildren());
        removeNodeSub(removedNode, bottomPane.getChildren());

    }

    private void removeNodeSub(Window removedNode, List<Node> list) {
        Node toRemove;
        if (removedNode.isModal()) {
            toRemove = removedNode.getParent();
        } else {
            toRemove = removedNode;
        }
        Iterator<Node> iterator = list.iterator();
        while (iterator.hasNext()) {
            Node next = iterator.next();
            if (next.equals(toRemove)) {
                iterator.remove();
            }
        }
    }

    private void initMenuBar() {
        menuBar = new MenuBar();
        menuBar.setMinHeight(30);
        menuBar.setMaxHeight(30);
        getChildren().add(menuBar);

        MenuItem closeAll = new MenuItem("Закрыть все");
        closeAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                windows.clear();
            }
        });
        SeparatorMenuItem separator = new SeparatorMenuItem();
        windowsMenu.getItems().addAll(closeAll, separator);
        menuBar.getMenus().add(windowsMenu);

    }

    private void initContentPane() {
        contentPane = new AnchorPane();
        contentPane.setStyle("-fx-background-color: beige");
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        getChildren().add(contentPane);
    }

    private void initBottomPane() {
        bottomPane = new HBox();
        bottomPane.setStyle("-fx-background-color: antiquewhite");
        bottomPane.setMinHeight(35);
        bottomPane.setMaxHeight(35);
        getChildren().add(bottomPane);
    }

    public AnchorPane getContentPane() {
        return contentPane;
    }

    protected HBox getBottomPane() {
        return bottomPane;
    }

    public void addMenuItem(Menu menu) {
        menuBar.getMenus().add(menuBar.getMenus().size() - 1, menu);
    }

    public ObservableList<Window> getWindows() {
        return windows;
    }
}

