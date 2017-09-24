package ru.metal.gui.windows;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.windows.icons.CloseIcon;
import ru.metal.gui.windows.icons.MaximizeIcon;
import ru.metal.gui.windows.icons.MinimizeIcon;
import ru.metal.gui.windows.utils.DragResizerXY;

import java.util.Iterator;
import java.util.UUID;

/**
 * Created by User on 08.08.2017.
 */
public class Window<T extends AbstractController, E extends Node> extends VBox {

    private boolean selected;
    private String id;
    private MainFrame mainFrame;
    private T controller;
    private E content;
    private HBox header;

    private AnchorPane contentPane;

    private StringProperty title = new SimpleStringProperty();

    private final String guid;

    public String getGuid() {
        return guid;
    }

    protected void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public Window() {
        guid = UUID.randomUUID().toString();
        init();
    }

    public String getIdFrame() {
        return id;
    }

    public void setIdFrame(String id) {
        this.id = id;
    }

    public E getContent() {
        return content;
    }

    public void setContent(E content) {
        setContent(content, null);
    }

    public void setContent(E node, T controller) {
        this.content = node;
        this.controller = controller;
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        this.contentPane.getChildren().add(node);

        this.id = node.getId();
        if (controller != null) {
            controller.closeRequestProperty().bindBidirectional(closeRequest);
            controller.toCloseProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        setClosed(newValue);
                        controller.setToClose(!newValue);
                    }
                }
            });
        } else {
            closeRequest.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        setClosed(true);
                    }
                }
            });
        }
    }

    public Window(String title) {
        this();
        this.title.set(title);

    }

    public T getController() {
        return controller;
    }

    private void init() {
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                toFront();
            }
        });
        setStyle("-fx-border-color: black; -fx-background-color: white;-fx-border-radius: 10 10 0 0;-fx-background-radius: 10 10 0 0");
        setPadding(new Insets(5));
        header = new HBox();
        header.setMaxHeight(30);
        header.setMinHeight(30);
        header.setAlignment(Pos.CENTER_RIGHT);
        header.setPadding(new Insets(0, 5, 0, 0));
        header.setStyle("-fx-border-color: black;-fx-border-width: 0 0 1 0");
        HBox titlePane = new HBox();
        titlePane.setPadding(new Insets(0, 0, 0, 10));
        HBox.setHgrow(titlePane, Priority.ALWAYS);
        Label titleLabel = new Label();
        titleLabel.setStyle("-fx-font: 14 italic");
        titlePane.getChildren().add(titleLabel);
        titlePane.setAlignment(Pos.CENTER_LEFT);
        titlePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                    if (isMaximizable()) {
                        maximized.setValue(!maximized.getValue());
                    }
                }
            }
        });
        title.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                titleLabel.setText(newValue);
            }
        });
        header.getChildren().add(titlePane);

        contentPane = new AnchorPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: lightgrey;-fx-background-radius: 0 0 10 10");
        getChildren().addAll(header, contentPane);
        addDnd(titlePane);

        closable.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    header.getChildren().add(header.getChildren().size(), new CloseIcon(closeRequest));
                }
            }
        });
        closedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Iterator<Window> iterator = mainFrame.getWindows().iterator();
                    while (iterator.hasNext()) {
                        Window next = iterator.next();
                        if (next.equals(Window.this)) {
                            iterator.remove();
                        }
                    }
                    setCloseRequest(!newValue);
                    setClosed(!newValue);

                }
            }
        });
        maximizable.addListener(new ChangeListener<Boolean>() {
            MaximizeIcon maximizeIcon;

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (maximizeIcon == null) {
                        maximizeIcon = new MaximizeIcon(maximized);
                        minimized.addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                if (newValue) {
                                    maximizeIcon.setDisable(true);
                                } else {
                                    maximizeIcon.setDisable(false);
                                }
                            }
                        });
                        header.getChildren().add(header.getChildren().size() - (isClosable() ? 1 : 0), maximizeIcon);
                    }

                } else {
                    if (maximizeIcon != null) {
                        maximizeIcon.setDisable(true);
                    }
                }
            }
        });
        maximized.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                maximizeEvent(newValue);
            }
        });
        minimizable.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    int idx = header.getChildren().size();
                    if (isClosable()) {
                        idx -= 1;
                    }
                    if (isMaximizable()) {
                        idx -= 1;
                    }
                    header.getChildren().add(idx, new MinimizeIcon(minimized));
                }
            }
        });

        minimized.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (isModal()) {
                    return;
                }
                if (newValue) {
                    minimize();
                } else {
                    restoreFromMinimize();
                }
            }
        });
    }

    private void maximizeEvent(boolean maximize) {
        if (maximize) {
            maximize();
        } else {
            restoreFromMaximize();
        }
    }

    SizeHolder sizeHolder = new SizeHolder();

    public void minimize() {
        maximizable.setValue(false);
        prepareSize(this);

        if (!isMaximized()) {
            sizeHolder.setHeight(getHeight());
            sizeHolder.setWigth(getWidth());

            sizeHolder.setX(getLayoutX());
            sizeHolder.setY(getLayoutY());
        }
        Timeline timeline = new Timeline();
        KeyValue minHeightValue = new KeyValue(minHeightProperty(), 35);
        KeyValue maxHeightValue = new KeyValue(maxHeightProperty(), 35);
        KeyValue prefHeightValue = new KeyValue(prefHeightProperty(), 35);

        KeyValue minWidthValue = new KeyValue(minWidthProperty(), 200);
        KeyValue maxWidthValue = new KeyValue(maxWidthProperty(), 200);
        KeyValue prefWidthValue = new KeyValue(prefWidthProperty(), 200);

        KeyValue xLoc = new KeyValue(layoutXProperty(), mainFrame.getBottomPane().getChildren().size() * 200);
        KeyValue yLoc = new KeyValue(layoutYProperty(), mainFrame.getContentPane().getHeight());

        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mainFrame.getContentPane().getChildren().remove(Window.this);
                mainFrame.getBottomPane().getChildren().add(Window.this);

            }
        };
        KeyFrame keyFrame = new KeyFrame(new Duration(200), onFinished, minHeightValue, minWidthValue, maxHeightValue, maxWidthValue,
                prefHeightValue, prefWidthValue, xLoc, yLoc);
        timeline.getKeyFrames().add(keyFrame);

        timeline.playFromStart();
    }

    private void restoreFromMinimize() {
        setMinimized(false);
        prepareSize(this);
        setLayoutY(mainFrame.getContentPane().getHeight());
        setLayoutX((mainFrame.getBottomPane().getChildren().size() - 1) * 200);
        mainFrame.getContentPane().getChildren().add(this);
        mainFrame.getBottomPane().getChildren().remove(this);
        layoutYProperty().unbind();
        Timeline timeline = new Timeline();
        KeyValue minHeightValue = new KeyValue(minHeightProperty(), sizeHolder.getHeight());
        KeyValue maxHeightValue = new KeyValue(maxHeightProperty(), sizeHolder.getHeight());
        KeyValue prefHeightValue = new KeyValue(prefHeightProperty(), sizeHolder.getHeight());

        KeyValue minWidthValue = new KeyValue(minWidthProperty(), sizeHolder.getWigth());
        KeyValue maxWidthValue = new KeyValue(maxWidthProperty(), sizeHolder.getWigth());
        KeyValue prefWidthValue = new KeyValue(prefWidthProperty(), sizeHolder.getWigth());

        KeyValue xLoc = new KeyValue(layoutXProperty(), sizeHolder.getX());
        KeyValue yLoc = new KeyValue(layoutYProperty(), sizeHolder.getY());

        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                if (isMaximized()) {
                    setLayoutX(0);
                    setLayoutY(0);
                    minHeightProperty().bind(mainFrame.getContentPane().heightProperty());
                    maxHeightProperty().bind(mainFrame.getContentPane().heightProperty());
                    prefHeightProperty().bind(mainFrame.getContentPane().heightProperty());

                    minWidthProperty().bind(mainFrame.getContentPane().widthProperty());
                    maxWidthProperty().bind(mainFrame.getContentPane().widthProperty());
                    prefWidthProperty().bind(mainFrame.getContentPane().widthProperty());
                }

            }
        };

        KeyFrame keyFrame = new KeyFrame(new Duration(200), onFinished, minHeightValue, minWidthValue, maxHeightValue, maxWidthValue,
                prefHeightValue, prefWidthValue, xLoc, yLoc);
        timeline.getKeyFrames().add(keyFrame);
        timeline.playFromStart();
    }

    private void prepareSize(Pane pane) {
        pane.minWidthProperty().unbind();
        pane.maxWidthProperty().unbind();
        pane.prefWidthProperty().unbind();

        pane.minHeightProperty().unbind();
        pane.maxHeightProperty().unbind();
        pane.prefHeightProperty().unbind();

        if (pane.getMinHeight() == -1) {
            pane.setMinHeight(pane.getHeight());
        }
        if (pane.getMaxHeight() == -1) {
            pane.setMaxHeight(pane.getHeight());
        }
        if (pane.getPrefHeight() == -1) {
            pane.setPrefHeight(pane.getHeight());
        }
        if (pane.getMinWidth() == -1) {
            pane.setMinWidth(pane.getWidth());
        }
        if (pane.getMaxWidth() == -1) {
            pane.setMaxWidth(pane.getWidth());
        }
        if (pane.getPrefWidth() == -1) {
            pane.setPrefWidth(pane.getWidth());
        }
    }

    private void maximize() {
        if (isDisable()) {
            return;
        }
        sizeHolder.setHeight(this.getHeight());
        sizeHolder.setWigth(getWidth());

        sizeHolder.setX(getLayoutX());
        sizeHolder.setY(getLayoutY());

        setLayoutX(0);
        setLayoutY(0);

        minHeightProperty().bind(mainFrame.getContentPane().heightProperty());
        maxHeightProperty().bind(mainFrame.getContentPane().heightProperty());
        prefHeightProperty().bind(mainFrame.getContentPane().heightProperty());

        minWidthProperty().bind(mainFrame.getContentPane().widthProperty());
        maxWidthProperty().bind(mainFrame.getContentPane().widthProperty());
        prefWidthProperty().bind(mainFrame.getContentPane().widthProperty());
    }

    private void restoreFromMaximize() {
        if (isDisable()) {
            return;
        }
        minWidthProperty().unbind();
        maxWidthProperty().unbind();
        prefWidthProperty().unbind();

        minHeightProperty().unbind();
        maxHeightProperty().unbind();
        prefHeightProperty().unbind();

        setMinWidth(sizeHolder.getWigth());
        setMaxWidth(sizeHolder.getWigth());
        setPrefWidth(sizeHolder.getWigth());

        setMinHeight(sizeHolder.getHeight());
        setMaxHeight(sizeHolder.getHeight());
        setPrefHeight(sizeHolder.getHeight());

        setLayoutX(sizeHolder.getX());
        setLayoutY(sizeHolder.getY());
    }

    private double initX = 0;
    private double initY = 0;
    Bounds boundsInLocal;

    private void addDnd(Node node) {
        DragResizerXY.makeResizable(this);
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                boundsInLocal = getParent().getBoundsInLocal();
                initX = event.getX();
                initY = event.getY() + 30;
                event.consume();
            }
        });
        node.setOnMouseDragged(event -> {
            if (!isMaximized() && !isMinimized()) {

                if (event.getSceneX() <= 0
                        || event.getSceneY() <= 40
                        || event.getSceneX() >= boundsInLocal.getWidth()
                        || event.getSceneY() >= boundsInLocal.getHeight()) {
                    return;
                }

                super.setLayoutX(event.getSceneX() - initX);
                super.setLayoutY(event.getSceneY() - initY);
            }
            event.consume();
        });
        node.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                initX = 0;
                initY = 0;
                event.consume();
            }
        });
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    private BooleanProperty closable = new SimpleBooleanProperty();
    private BooleanProperty closed = new SimpleBooleanProperty();
    private BooleanProperty closeRequest = new SimpleBooleanProperty();

    private BooleanProperty maximizable = new SimpleBooleanProperty();
    private BooleanProperty maximized = new SimpleBooleanProperty();

    private BooleanProperty minimizable = new SimpleBooleanProperty();
    private BooleanProperty minimized = new SimpleBooleanProperty();

    private BooleanProperty modal = new SimpleBooleanProperty();

    public boolean isClosable() {
        return closable.get();
    }

    public BooleanProperty closableProperty() {
        return closable;
    }

    public void setClosable(boolean closable) {
        this.closable.set(closable);
    }

    public boolean isClosed() {
        return closed.get();
    }

    public BooleanProperty closedProperty() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed.set(closed);
    }

    public boolean isCloseRequest() {
        return closeRequest.get();
    }

    public BooleanProperty closeRequestProperty() {
        return closeRequest;
    }

    public void setCloseRequest(boolean closeRequest) {
        this.closeRequest.set(closeRequest);
    }

    public boolean isMaximizable() {
        return maximizable.get();
    }

    public BooleanProperty maximizableProperty() {
        return maximizable;
    }

    public void setMaximizable(boolean maximizable) {
        this.maximizable.set(maximizable);
    }

    public boolean isMaximized() {
        return maximized.get();
    }

    public BooleanProperty maximizedProperty() {
        return maximized;
    }

    public void setMaximized(boolean maximized) {
        this.maximized.set(maximized);
    }

    public boolean isMinimizable() {
        return minimizable.get();
    }

    public BooleanProperty minimizableProperty() {
        return minimizable;
    }

    public void setMinimizable(boolean minimizable) {
        this.minimizable.set(minimizable);
    }

    public boolean isMinimized() {
        return minimized.get();
    }

    public BooleanProperty minimizedProperty() {
        return minimized;
    }

    public void setMinimized(boolean minimized) {
        this.minimized.set(minimized);
    }

    public boolean isModal() {
        return modal.get();
    }

    public BooleanProperty modalProperty() {
        return modal;
    }

    public void setModal(boolean modal) {
        modalOld = modal;
        this.modal.set(modal);
    }


    private boolean modalOld;

    public void setHide(boolean hide) {
        if (modalOld) {
            this.modal.set(!hide);
            getParent().setVisible(!hide);
        } else {
            setVisible(!hide);
        }
    }

    public boolean isSelected() {
        return selected;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Window window = (Window) o;

        return guid.equals(window.guid);
    }

    @Override
    public int hashCode() {
        return guid.hashCode();
    }
}
