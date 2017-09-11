package ru.metal.gui.controls.treeviewcontrol;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import ru.common.api.dto.Error;
import ru.common.api.dto.TableElement;
import ru.common.api.dto.TreeviewElement;
import ru.common.api.dto.UpdateResult;
import ru.common.api.request.DeleteTreeItemRequest;
import ru.common.api.request.ObtainTreeItemRequest;
import ru.common.api.request.UpdateTreeItemRequest;
import ru.common.api.response.UpdateTreeItemResponse;
import ru.metal.dto.FxEntity;
import ru.metal.dto.response.ObtainTreeItemResponse;
import ru.metal.exceptions.ServerErrorException;
import ru.metal.gui.controls.tableviewcontrol.TableViewPane;
import ru.metal.gui.windows.Button;
import ru.metal.rest.TreeClient;

import java.util.*;

/**
 * Created by User on 09.08.2017.
 */
public class TreeViewPane<T extends TreeviewElement> extends VBox {
    public final static String ROOT_GUID = "-1";
    private Class<T> clazz;
    TreeClient<T> treeClient;
    private TreeView<T> treeView;
    private ObjectProperty<List<String>> selectedGroups = new SimpleObjectProperty<>();
    private ObjectProperty<T> selectedItem = new SimpleObjectProperty<T>();
    private boolean onlyActive = true;


    private ObtainTreeItemRequest obtainTreeItemRequest = new ObtainTreeItemRequest();

    public List<String> getSelectedGroups() {
        return selectedGroups.get();
    }

    public ObjectProperty<List<String>> selectedGroupsProperty() {
        return selectedGroups;
    }

    public void setRequest(ObtainTreeItemRequest obtainTreeItemRequest) {
        this.obtainTreeItemRequest = obtainTreeItemRequest;
        this.obtainTreeItemRequest.setActive(true);
        try {
            ObtainTreeItemResponse<T> items = treeClient.getItems(obtainTreeItemRequest);
            fillTree(treeView.getRoot(), items.getDataList());
        } catch (ServerErrorException e) {

        }
    }

    public TreeViewPane(TreeClient<T> treeClient, String name, Class<T> clazz) {
        this.clazz = clazz;
        this.treeClient = treeClient;
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(2));
        HBox buttonBlock = new HBox();
        buttonBlock.setPadding(new Insets(2));
        buttonBlock.setPrefWidth(200);

        buttonBlock.setAlignment(Pos.CENTER_RIGHT);
        getChildren().addAll(buttonBlock);

        CheckBox activeBox = new CheckBox("Активные");
        activeBox.setSelected(true);
        Tooltip tooltipActiveBox = new Tooltip("Только активные");
        activeBox.setTooltip(tooltipActiveBox);
        activeBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    obtainTreeItemRequest.setActive(true);
                    onlyActive = true;
                } else {
                    obtainTreeItemRequest.setActive(false);
                    onlyActive = false;
                }
                treeView.getRoot().getChildren().clear();
                try {
                    ObtainTreeItemResponse<T> items = treeClient.getItems(obtainTreeItemRequest);
                    fillTree(treeView.getRoot(), items.getDataList());
                } catch (ServerErrorException e) {
                }

                List<TreeItem<T>> allChildren = getAllChildren(treeView.getSelectionModel().getSelectedItem());
                List<String> result = new ArrayList();
                for (TreeItem<T> item : allChildren) {
                    result.add(item.getValue().getGuid());
                }
                selectedGroups.setValue(result);

            }
        });
        Button expandAll = new Button(null, new Image(getClass().getResourceAsStream("/icons/tree_expand.png")));
        expandAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (TreeItem<T> item : treeView.getRoot().getChildren()) {
                    expandTreeView(item, true);
                }
            }
        });
        Button collapseAll = new Button(null, new Image(getClass().getResourceAsStream("/icons/collapse_tree.png")));

        collapseAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (TreeItem<T> item : treeView.getRoot().getChildren()) {
                    expandTreeView(item, false);
                }
            }
        });
        buttonBlock.getChildren().addAll(activeBox, expandAll, collapseAll);
        TextField findField = new TextField();
        findField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            int i = 0;
            List<TreeItem<T>> items = new ArrayList<>();

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    if (i < items.size() - 1) {
                        i++;
                    }
                    if (!items.isEmpty()) {
                        treeView.getSelectionModel().select(items.get(i));
                    }
                } else {
                    items = findItems(treeView.getRoot(), findField.getText());
                    if (!items.isEmpty()) {
                        treeView.getSelectionModel().select(items.get(0));
                        i = 0;
                    }
                }
            }
        });

        treeView = new TreeView();
        treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<T>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<T>> observable, TreeItem<T> oldValue, TreeItem<T> newValue) {
                if (newValue != null) {
                    selectedItem.setValue(newValue.getValue());
                    List<TreeItem<T>> allChildren = getAllChildren(newValue);
                    List<String> result = new ArrayList();
                    for (TreeItem<T> item : allChildren) {
                        result.add(item.getValue().getGuid());
                    }
                    selectedGroups.setValue(result);
                }
            }
        });
        VBox.setVgrow(treeView, Priority.ALWAYS);
        getChildren().add(treeView);

        TreeviewElement rootNode = null;
        try {
            rootNode = clazz.newInstance();
            rootNode.setGuid(ROOT_GUID);
            rootNode.setName(name);
            rootNode.setActive(true);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        TreeItem<T> rootItem = new TreeItem();
        rootItem.setValue((T) rootNode);
        rootItem.setExpanded(true);
        treeView.setRoot(rootItem);
        //     obtainTreeItemRequest.setActive(true);
//        try {
//            ObtainTreeItemResponse<T> items = treeClient.getItems(obtainTreeItemRequest);
//            fillTree(rootItem, items.getDataList());
//        } catch (ServerErrorException e) {
//            e.printStackTrace();
//        }
        treeView.setCellFactory(new Callback<TreeView<T>, TreeCell<T>>() {
            @Override
            public TreeCell<T> call(TreeView<T> param) {
                TextFieldTreeCellImpl textFieldTreeCell = new TextFieldTreeCellImpl();
                textFieldTreeCell.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        TextFieldTreeCellImpl source = (TextFieldTreeCellImpl) event.getSource();
                        if (source.getItem() != null) {
                            Dragboard dragBoard = startDragAndDrop(TransferMode.MOVE);
                            dragBoard.setDragView(textFieldTreeCell.snapshot(null, null));
                            ClipboardContent content = new ClipboardContent();
                            DraggableItem draggableItem = new DraggableItem();
                            draggableItem.setGroupGuid(source.getItem().getGroupGuid());
                            draggableItem.setGuid(source.getItem().getGuid());
                            content.put(treeItemDataFormat, draggableItem);
                            dragBoard.setContent(content);
                        }
                        event.consume();
                    }
                });
                textFieldTreeCell.setOnDragOver(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {

                        TextFieldTreeCellImpl source = (TextFieldTreeCellImpl) event.getSource();
                        final T item = source.getItem();
                        TableElement contentTableRow = (TableElement) event.getDragboard().getContent(TableViewPane.tableRowDataFormat);
                        DraggableItem contentTreeItem = (DraggableItem) event.getDragboard().getContent(treeItemDataFormat);
                        if (contentTableRow != null) {
                            event.acceptTransferModes(TransferMode.MOVE);
                        } else if (contentTreeItem != null) {
                            if (!contentTreeItem.getGuid().equals(item.getGuid())) {
                                boolean child = isChild(source.getTreeItem(), contentTreeItem.getGuid());
                                if (!child) {
                                    event.acceptTransferModes(TransferMode.MOVE);
                                }
                            }
                        }
                        event.consume();
                    }
                });
                textFieldTreeCell.setOnDragDropped(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        TextFieldTreeCellImpl source = (TextFieldTreeCellImpl) event.getSource();
                        DraggableItem contentTreeView = (DraggableItem) event.getDragboard().getContent(treeItemDataFormat);
                        TableElement contentTableRow = (TableElement) event.getDragboard().getContent(TableViewPane.tableRowDataFormat);
                        if (contentTreeView != null) {
                            TreeItem<T> itemToMove = search(treeView.getRoot(), contentTreeView.getGuid());
                            TreeItem<T> newParent = search(treeView.getRoot(), source.getItem().getGuid());
                            itemToMove.getValue().setGroupGuid(ROOT_GUID.equals(newParent.getValue().getGuid()) ? null : newParent.getValue().getGuid());
                            itemToMove.getParent().getChildren().remove(itemToMove);
                            newParent.getChildren().add(itemToMove);
                            treeView.getSelectionModel().select(itemToMove);
                            newParent.setExpanded(true);
                            event.setDropCompleted(true);

                            UpdateTreeItemRequest<T> updateTreeItemRequest = new UpdateTreeItemRequest();
                            updateTreeItemRequest.getDataList().add(itemToMove.getValue());
                            try {
                                treeClient.updateItems(updateTreeItemRequest);
                            } catch (ServerErrorException e) {
                            }
                            newParent.getChildren().sort(new Comparator<TreeItem<T>>() {
                                @Override
                                public int compare(TreeItem<T> o1, TreeItem<T> o2) {
                                    return o1.getValue().getName().compareToIgnoreCase(o2.getValue().getName());
                                }
                            });
                        } else if (contentTableRow != null) {
                            T item = source.getItem();
                            if (item instanceof FxEntity) {
                                FxEntity fxEntity = (FxEntity) item;
                                contentTableRow.setGroup((TreeviewElement) fxEntity.getEntity());
                                ClipboardContent cc = new ClipboardContent();
                                cc.put(TableViewPane.tableRowDataFormat, contentTableRow);
                                event.getDragboard().setContent(cc);
                            }


                        }
                        event.consume();
                    }
                });
                return textFieldTreeCell;
            }
        });
    }

    public void selectRoot() {
        treeView.getSelectionModel().select(treeView.getRoot());
    }

    public T getSelectedItem() {
        return selectedItem.get();
    }

    public boolean hasChildren(T item) {
        return !search(treeView.getRoot(), item.getGuid()).getChildren().isEmpty();
    }

    public ObjectProperty<T> selectedItemProperty() {
        return selectedItem;
    }

    public void setSelectedItem(T selectedItem) {
        this.selectedItem.set(selectedItem);
    }

    private List<TreeItem<T>> findItems(final TreeItem<T> currentNode, String text) {
        List<TreeItem<T>> result = new ArrayList<>();
        if (currentNode.getValue().getName().toLowerCase().contains(text.toLowerCase())) {
            result.add(currentNode);
        }
        if (!currentNode.isLeaf()) {
            for (TreeItem<T> child : currentNode.getChildren()) {
                result.addAll(findItems(child, text));
            }
        }
        return result;
    }

    public TreeItem<T> search(final TreeItem<T> currentNode, final String guid) {
        TreeItem<T> result = null;
        if (currentNode.getValue().getGuid().equals(guid)) {
            result = currentNode;
        } else if (!currentNode.isLeaf()) {
            for (TreeItem<T> child : currentNode.getChildren()) {
                result = search(child, guid);
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }


    private boolean isChild(TreeItem<T> target, String draggableGuid) {
        if (target.getParent() == null) {
            return false;
        }
        if (target.getParent().getValue().getGuid().equals(draggableGuid)) {
            return true;
        } else {
            if (isChild(target.getParent(), draggableGuid)) {
                return true;
            }
        }
        return false;
    }

    private void expandTreeView(TreeItem<T> item, boolean value) {
        if (item != null && !item.isLeaf()) {
            item.setExpanded(value);
            for (TreeItem<T> child : item.getChildren()) {
                expandTreeView(child, value);
            }
        }
    }


    public void setEditable(boolean value) {
        treeView.setEditable(value);
    }

    private void fillTree(TreeItem<T> root, List<T> items) {
        HashMap<String, TreeItem<T>> treeItems = new HashMap<>();
        Iterator<T> iterator = items.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            TreeItem<T> addedItem = new TreeItem<>(next);

            if (next.getGroupGuid() == null) {
                root.getChildren().add(addedItem);
                treeItems.put(next.getGuid(), addedItem);
                iterator.remove();
                for (Map.Entry<String, TreeItem<T>> entry : treeItems.entrySet()) {
                    if (next.getGuid().equals(entry.getValue().getValue().getGroupGuid())) {
                        addedItem.getChildren().add(entry.getValue());
                    }
                }
            } else {
                boolean isFinded = false;
                for (Map.Entry<String, TreeItem<T>> entry : treeItems.entrySet()) {
                    if (next.getGroupGuid().equals(entry.getKey())) {
                        entry.getValue().getChildren().add(addedItem);
                        iterator.remove();
                        isFinded = true;
                    }
                    if (next.getGuid().equals(entry.getValue().getValue().getGroupGuid())) {
                        addedItem.getChildren().add(entry.getValue());
                    }

                }
                if (!isFinded) {
                    treeItems.put(next.getGuid(), addedItem);
                    iterator.remove();
                } else {
                    treeItems.put(next.getGuid(), addedItem);
                }
            }
            treeView.getRoot().getChildren().sort(new Comparator<TreeItem<T>>() {
                @Override
                public int compare(TreeItem<T> o1, TreeItem<T> o2) {
                    return o1.getValue().getName().compareToIgnoreCase(o2.getValue().getName());
                }
            });
            for (Map.Entry<String, TreeItem<T>> entry : treeItems.entrySet()) {
                entry.getValue().getChildren().sort(new Comparator<TreeItem<T>>() {
                    @Override
                    public int compare(TreeItem<T> o1, TreeItem<T> o2) {
                        return o1.getValue().getName().compareToIgnoreCase(o2.getValue().getName());
                    }
                });
            }
        }

    }

    private static final DataFormat treeItemDataFormat = new DataFormat("treeItem");

    private final class TextFieldTreeCellImpl extends TreeCell<T> {

        private ContextMenu menu = new ContextMenu();
        public boolean clickedFirstTime = false;
        private TextField textField;

        public TextFieldTreeCellImpl() {


            MenuItem addMenuItem = new MenuItem("Добавить");
            addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    T item = getItem();
                    if (item == null) {
                        return;
                    }
                    try {
                        T addedElement = clazz.newInstance();
                        addedElement.setGuid(UUID.randomUUID().toString());
                        addedElement.setName("Новый элемент");
                        if (!ROOT_GUID.equals(item.getGuid())) {
                            addedElement.setGroupGuid(item.getGuid());
                        }
                        TreeItem<T> addedItem = new TreeItem(addedElement);
                        getTreeItem().getChildren().add(addedItem);
                        getTreeItem().setExpanded(true);
                        getTreeItem().getChildren().sort(new Comparator<TreeItem<T>>() {
                            @Override
                            public int compare(TreeItem<T> o1, TreeItem<T> o2) {
                                return o1.getValue().getName().compareToIgnoreCase(o2.getValue().getName());
                            }
                        });
                        getTreeView().getSelectionModel().clearSelection();
                        getTreeView().getSelectionModel().select(addedItem);
                        UpdateTreeItemRequest<T> updateTreeItemRequest = new UpdateTreeItemRequest();
                        updateTreeItemRequest.getDataList().add(addedElement);
                        UpdateTreeItemResponse updateTreeItemResponse = treeClient.updateItems(updateTreeItemRequest);
                        addedElement.setGuid(updateTreeItemRequest.getDataList().get(0).getGuid());

                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ServerErrorException e) {
                        e.printStackTrace();
                    }

                }
            });
            menu.getItems().add(addMenuItem);

            MenuItem removeMenuItem = new MenuItem();
            removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    TreeItem<T> treeItem = getTreeItem();
                    boolean active = !treeItem.getValue().getActive();
                    Set<Node> treeCells = treeView.lookupAll(".tree-cell");
                    List<Node> cells = new ArrayList<>(treeCells);
                    List<TreeItem<T>> allChildren = getAllChildren(treeItem);
                    boolean toUpdate = true;
                    boolean hasError = false;
                    List<String> groupGuids = new ArrayList<>();
                    List<T> allChildrenElements = new ArrayList<T>();
                    for (TreeItem<T> item : allChildren) {
                        groupGuids.add(item.getValue().getGuid());
                    }

                    if (!active) {
                        DeleteTreeItemRequest<T> treeItemRequest = new DeleteTreeItemRequest<T>();
                        treeItemRequest.setGuids(groupGuids);
                        StringBuilder errorText = new StringBuilder();
                        try {
                            UpdateTreeItemResponse<UpdateResult> response = treeClient.deleteItem(treeItemRequest);
                            for (UpdateResult updateResult : response.getImportResults()) {
                                Iterator<TreeItem<T>> itemIterator = allChildren.iterator();
                                while ((itemIterator.hasNext())) {
                                    TreeItem<T> next = itemIterator.next();
                                    if (next.getValue().getGuid().equals(updateResult.getGuid()) && updateResult.getErrors().isEmpty()) {
                                        next.getParent().getChildren().remove(next);
                                        itemIterator.remove();
                                    }
                                }
                                if (!updateResult.getErrors().isEmpty()) {
                                    hasError = true;
                                    for (Error error : updateResult.getErrors()) {
                                        errorText.append(error.getDescription()).append("\n");
                                    }
                                }
                            }
                            for (TreeItem<T> item : allChildren) {
                                allChildrenElements.add(item.getValue());
                            }
                        } catch (ServerErrorException e) {
                            e.printStackTrace();
                        }
                        if (hasError) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Внимание");
                            alert.setHeaderText("Ошибка при удалении групп:\n" + errorText.toString());
                            alert.setContentText("Изменить флаг активности?");
                            alert.initOwner(getScene().getWindow());
                            ButtonType buttonTypeOne = new ButtonType("Да");
                            ButtonType buttonTypeTwo = new ButtonType("Нет");

                            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
                            Optional<ButtonType> result = alert.showAndWait();

                            if (result.get() == buttonTypeTwo) {
                                toUpdate = false;
                            }
                        } else {
                            toUpdate = false;
                        }
                    }
                    if (toUpdate) {
                        List<T> requestList = new ArrayList<T>();
                        if (!active) {
                            for (T t : allChildrenElements) {
                                t.setActive(active);
                                requestList.add(t);
                            }
                        } else {
                            List<TreeItem<T>> chain = getChain(treeItem);
                            for (TreeItem<T> item : chain) {
                                item.getValue().setActive(active);
                                requestList.add(item.getValue());
                            }
                        }
                        UpdateTreeItemRequest<T> request = new UpdateTreeItemRequest();

                        request.setDataList(requestList);
                        try {
                            treeClient.updateItems(request);
                        } catch (ServerErrorException e) {
                            e.printStackTrace();
                        }
                    }
                    if (toUpdate) {
                        if (active) {
                            applyStyle(getChain(treeItem), active, cells);
                        } else {
                            for (TreeItem<T> tTreeItem : allChildren) {
                                tTreeItem.getValue().setActive(active);
                                applyStyle(tTreeItem, active, cells);
                            }
                        }
                    }
                    if (toUpdate && (onlyActive && !active)) {
                        getTreeItem().getParent().getChildren().remove(getTreeItem());
                    }
                }
            });
            menu.getItems().add(removeMenuItem);
            menu.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    T item = getItem();
                    if (item != null) {

                        if (!ROOT_GUID.equals(item.getGuid())) {
                            removeMenuItem.setVisible(true);
                            removeMenuItem.setText(item.getActive() ? "Удалить" : "Восстановить");
                        } else {
                            removeMenuItem.setVisible(false);
                        }
                    } else {
                        addMenuItem.setVisible(false);
                        removeMenuItem.setVisible(false);
                    }
                }
            });
        }

        private List<TreeItem<T>> getChain(TreeItem<T> leaf) {
            List<TreeItem<T>> result = new ArrayList<>();
            result.add(leaf);
            if (leaf.getParent() != null && !leaf.getParent().getValue().getGuid().equals("-1")) {
                result.addAll(getChain(leaf.getParent()));
            }
            return result;
        }

        private void applyStyle(List<TreeItem<T>> items, boolean active, List<Node> cells) {
            for (TreeItem<T> item : items) {
                applyStyle(item, active, cells);
            }
        }

        private void applyStyle(TreeItem<T> item, boolean active, List<Node> cells) {
            int row = treeView.getRow(item);
            item.setExpanded(true);
            TreeCell cell = ((TreeCell) cells.get(row));
            if (active) {
                cell.setStyle("-fx-font-style: normal;");
            } else {
                cell.setStyle("-fx-font-style: oblique;");
            }
        }

        @Override
        public void startEdit() {
            if (getTreeItem().getParent() == null) {
                return;
            }
            super.startEdit();
            if (textField == null) {
                createTextField();
            }
            textField.setText(getText());
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem().getName());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (item.getActive()) {
                    setStyle("-fx-font-style: normal;");
                } else {
                    setStyle("-fx-font-style: oblique;");
                }
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                }

            }
            if (!this.clickedFirstTime) {
                setContextMenu(menu);
                this.clickedFirstTime = true;
            }
        }

        private void save() {
            T item = getItem();
            item.setName(textField.getText());
            commitEdit(item);
            getTreeItem().getParent().getChildren().sort(new Comparator<TreeItem<T>>() {
                @Override
                public int compare(TreeItem<T> o1, TreeItem<T> o2) {
                    return o1.getValue().getName().compareToIgnoreCase(o2.getValue().getName());
                }
            });
            getTreeView().getSelectionModel().clearSelection();
            getTreeView().getSelectionModel().select(getTreeItem());
            UpdateTreeItemRequest<T> updateTreeItemRequest = new UpdateTreeItemRequest();
            updateTreeItemRequest.getDataList().add(item);
            try {
                treeClient.updateItems(updateTreeItemRequest);
            } catch (ServerErrorException e) {
                e.printStackTrace();
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    save();
                }
            });
            textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (!newValue) {
                    save();
                }
            });
//            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
//
//                @Override
//                public void handle(KeyEvent t) {
//                    if (t.getCode() == KeyCode.ENTER) {
//                        save();
//
//                    } else if (t.getCode() == KeyCode.ESCAPE) {
//                        cancelEdit();
//                    }
//                }
//            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().getName();
        }
    }

    private List<TreeItem<T>> getAllChildren(TreeItem<T> treeItem) {
        List<TreeItem<T>> list = new ArrayList<TreeItem<T>>();
        if (treeItem != null) {
            list.add(treeItem);
            for (TreeItem<T> item : treeItem.getChildren()) {
                list.addAll(getAllChildren(item));
            }
        }
        return list;
    }
}
