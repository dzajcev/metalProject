package ru.metal.gui.controls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import ru.common.api.dto.TableElement;
import ru.common.api.dto.TreeviewElement;
import ru.metal.gui.windows.LabelButton;

import java.util.*;

/**
 * Created by User on 23.09.2017.
 */
public class MultiSelectTree<G extends TreeviewElement, I extends TableElement<G>> extends VBox {

    private TreeView<MultiSelectTreeValue<G, I>> treeView;

    private Map<String, I> selectedItems = new HashMap<String, I>();

    private Map<String, TreeItem<MultiSelectTreeValue<G, I>>> leafItems = new HashMap<>();

    private boolean isMultiSelect;

    public MultiSelectTree(List<G> groups, List<I> items, String name, boolean multiSelect) {
        isMultiSelect = multiSelect;
        setPrefHeight(400);
        setPrefWidth(300);
        LabelButton collapseAll = new LabelButton(null, new Image(getClass().getResourceAsStream("/icons/collapse_tree.png")));

        collapseAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (TreeItem<MultiSelectTreeValue<G, I>> item : treeView.getRoot().getChildren()) {
                    expandTreeView(item, false);
                }
            }
        });
        LabelButton expandAll = new LabelButton(null, new Image(getClass().getResourceAsStream("/icons/tree_expand.png")));
        expandAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (TreeItem<MultiSelectTreeValue<G, I>> item : treeView.getRoot().getChildren()) {
                    expandTreeView(item, true);
                }
            }
        });
        HBox buttons = new HBox(expandAll, collapseAll);
        buttons.setSpacing(5);
        buttons.setPadding(new Insets(0, 5, 0, 5));

        treeView = new TreeView<>();
        getChildren().addAll(buttons, treeView);
        VBox.setVgrow(treeView, Priority.ALWAYS);
        treeView.setCellFactory(new Callback<TreeView<MultiSelectTreeValue<G, I>>, TreeCell<MultiSelectTreeValue<G, I>>>() {
            @Override
            public TreeCell<MultiSelectTreeValue<G, I>> call(TreeView<MultiSelectTreeValue<G, I>> param) {
                return new TextFieldTreeCellImpl(selectedItems);
            }
        });

        MultiSelectTreeValue root = new MultiSelectTreeValue();
        if (groups == null || groups.isEmpty()) {
            return;
        } else {
            G g = groups.get(0);
            try {
                TreeviewElement treeviewElement = g.getClass().newInstance();
                treeviewElement.setActive(true);
                treeviewElement.setGuid("-1");
                treeviewElement.setName(name);
                root.setGroup(treeviewElement);
                treeView.setRoot(new TreeItem<>(root));
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        treeView.getRoot().setExpanded(true);
        Map<String, TreeItem<MultiSelectTreeValue<G, I>>> groupItems = fillTreeGroups(treeView.getRoot(), groups);
        for (I i : items) {
            TreeItem<MultiSelectTreeValue<G, I>> multiSelectTreeValueTreeItem = groupItems.get(i.getGroup().getGuid());
            MultiSelectTreeValue<G, I> treeValue = new MultiSelectTreeValue<G, I>();
            treeValue.setItem(i);
            TreeItem<MultiSelectTreeValue<G, I>> treeItem = new TreeItem(treeValue);
            leafItems.put(i.getGuid(), treeItem);
            multiSelectTreeValueTreeItem.getChildren().add(treeItem);
        }
    }

    public void setSelected(List<I> selectedItems, boolean selected) {
        for (I i : selectedItems) {
            TreeItem<MultiSelectTreeValue<G, I>> multiSelectTreeValueTreeItem = leafItems.get(i.getGuid());
            multiSelectTreeValueTreeItem.getValue().setSelectMode(MultiSelectTreeValue.FULL_SELECT);
            selectParentBranch(multiSelectTreeValueTreeItem, selected);
        }
        treeView.refresh();
    }

    private Map<String, TreeItem<MultiSelectTreeValue<G, I>>> fillTreeGroups(TreeItem<MultiSelectTreeValue<G, I>> root, List<G> items) {
        HashMap<String, TreeItem<MultiSelectTreeValue<G, I>>> treeItems = new HashMap<>();
        List<G> copyItems = new ArrayList<G>(items);
        Iterator<G> iterator = copyItems.iterator();
        while (iterator.hasNext()) {
            G next = iterator.next();
            MultiSelectTreeValue<G, I> treeValue = new MultiSelectTreeValue<G, I>();
            treeValue.setGroup(next);
            TreeItem<MultiSelectTreeValue<G, I>> addedItem = new TreeItem(treeValue);

            if (next.getGroupGuid() == null) {
                root.getChildren().add(addedItem);
                treeItems.put(next.getGuid(), addedItem);
                iterator.remove();
                for (Map.Entry<String, TreeItem<MultiSelectTreeValue<G, I>>> entry : treeItems.entrySet()) {
                    if (next.getGuid().equals(entry.getValue().getValue().getGroup().getGroupGuid())) {
                        addedItem.getChildren().add(entry.getValue());
                    }
                }
            } else {
                boolean isFinded = false;
                for (Map.Entry<String, TreeItem<MultiSelectTreeValue<G, I>>> entry : treeItems.entrySet()) {
                    if (next.getGroupGuid().equals(entry.getKey())) {
                        entry.getValue().getChildren().add(addedItem);
                        iterator.remove();
                        isFinded = true;
                    }
                    if (next.getGuid().equals(entry.getValue().getValue().getGroup().getGroupGuid())) {
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
            treeView.getRoot().getChildren().sort(new Comparator<TreeItem<MultiSelectTreeValue<G, I>>>() {
                @Override
                public int compare(TreeItem<MultiSelectTreeValue<G, I>> o1, TreeItem<MultiSelectTreeValue<G, I>> o2) {
                    return o1.getValue().getGroup().getName().compareToIgnoreCase(o2.getValue().getGroup().getName());
                }
            });
            for (Map.Entry<String, TreeItem<MultiSelectTreeValue<G, I>>> entry : treeItems.entrySet()) {
                entry.getValue().getChildren().sort(new Comparator<TreeItem<MultiSelectTreeValue<G, I>>>() {
                    @Override
                    public int compare(TreeItem<MultiSelectTreeValue<G, I>> o1, TreeItem<MultiSelectTreeValue<G, I>> o2) {
                        return o1.getValue().getGroup().getName().compareToIgnoreCase(o2.getValue().getGroup().getName());
                    }
                });
            }
        }
        return treeItems;
    }

    private void expandTreeView(TreeItem<MultiSelectTreeValue<G, I>> item, boolean value) {
        if (item != null && !item.isLeaf()) {
            item.setExpanded(value);
            for (TreeItem<MultiSelectTreeValue<G, I>> child : item.getChildren()) {
                expandTreeView(child, value);
            }
        }
    }

    private List<TreeItem<MultiSelectTreeValue<G, I>>> getAllChildren(TreeItem<MultiSelectTreeValue<G, I>> treeItem) {
        List<TreeItem<MultiSelectTreeValue<G, I>>> list = new ArrayList<TreeItem<MultiSelectTreeValue<G, I>>>();
        if (treeItem != null) {
            list.add(treeItem);
            for (TreeItem<MultiSelectTreeValue<G, I>> item : treeItem.getChildren()) {
                list.addAll(getAllChildren(item));
            }
        }
        return list;
    }

    private void selectParentBranch(TreeItem<MultiSelectTreeValue<G, I>> item, boolean selected) {
        TreeItem<MultiSelectTreeValue<G, I>> parent = item.getParent();
        if (parent == null) {
            return;
        }
        if (parent.getChildren().size() == 1) {
            parent.getValue().setSelectMode(item.getValue().getSelectMode());
        } else {
            boolean isFinded = false;
            for (TreeItem<MultiSelectTreeValue<G, I>> child : parent.getChildren()) {
                if (selected) {
                    if (child.getValue().getSelectMode() != MultiSelectTreeValue.FULL_SELECT) {
                        isFinded = true;
                        break;
                    }
                } else {
                    if (child.getValue().getSelectMode() != MultiSelectTreeValue.UNSELECT) {
                        isFinded = true;
                        break;
                    }
                }
            }
            if (isFinded) {
                parent.getValue().setSelectMode(MultiSelectTreeValue.HALF_SELECT);
            } else {
                if (selected) {
                    parent.getValue().setSelectMode(MultiSelectTreeValue.FULL_SELECT);
                } else {
                    parent.getValue().setSelectMode(MultiSelectTreeValue.UNSELECT);
                }
            }
        }
        selectParentBranch(parent, selected);

    }

    private final class TextFieldTreeCellImpl extends TreeCell<MultiSelectTreeValue<G, I>> {
        ImageView openFolder = new ImageView(new Image(getClass().getResourceAsStream("/icons/open_folder.png")));
        ImageView selectedOpenFolder = new ImageView(new Image(getClass().getResourceAsStream("/icons/selected_open_folder.png")));
        ImageView halfSelectedOpenFolder = new ImageView(new Image(getClass().getResourceAsStream("/icons/half_selected_open_folder.png")));

        ImageView closeFolder = new ImageView(new Image(getClass().getResourceAsStream("/icons/close_folder.png")));
        ImageView selectedCloseFolder = new ImageView(new Image(getClass().getResourceAsStream("/icons/selected_close_folder.png")));
        ImageView halfSelectedCloseFolder = new ImageView(new Image(getClass().getResourceAsStream("/icons/half_selected_close_folder.png")));

        ImageView leaf = new ImageView(new Image(getClass().getResourceAsStream("/icons/leaf.png")));
        ImageView selectedLeaf = new ImageView(new Image(getClass().getResourceAsStream("/icons/selected_leaf.png")));

        public TextFieldTreeCellImpl(Map<String, I> selectedItems) {
            this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (isMultiSelect) {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            int selectMode = getTreeItem().getValue().getSelectMode();

                            List<TreeItem<MultiSelectTreeValue<G, I>>> allChildren = getAllChildren(getTreeItem());

                            for (TreeItem<MultiSelectTreeValue<G, I>> treeItem : allChildren) {
                                if (selectMode == MultiSelectTreeValue.UNSELECT) {
                                    treeItem.getValue().setSelectMode(MultiSelectTreeValue.FULL_SELECT);
                                } else if (treeItem.getValue().isLeaf()) {
                                    treeItem.getValue().setSelectMode(MultiSelectTreeValue.UNSELECT);
                                }
                                selectParentBranch(treeItem, selectMode == MultiSelectTreeValue.UNSELECT);
                                if (treeItem.getValue().isLeaf()) {
                                    if (treeItem.getValue().getSelectMode() != MultiSelectTreeValue.FULL_SELECT) {
                                        selectedItems.remove(treeItem.getValue().getItem().getGuid());
                                    } else {
                                        selectedItems.put(treeItem.getValue().getItem().getGuid(), treeItem.getValue().getItem());
                                    }
                                }
                            }
                            getTreeView().refresh();

                        }
                    } else {
                        //todo: singleselect
                    }

                }
            });
        }

        @Override
        public void updateItem(MultiSelectTreeValue<G, I> item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setText(getString());
                if (getItem().isLeaf()) {
                    if (getItem().getSelectMode() == MultiSelectTreeValue.FULL_SELECT) {
                        setGraphic(selectedLeaf);
                    } else {
                        setGraphic(leaf);
                    }
                } else {
                    if (getTreeItem().isExpanded()) {
                        if (getItem().getSelectMode() == MultiSelectTreeValue.FULL_SELECT) {
                            setGraphic(selectedOpenFolder);
                        } else if (getItem().getSelectMode() == MultiSelectTreeValue.HALF_SELECT) {
                            setGraphic(halfSelectedOpenFolder);
                        } else {
                            setGraphic(openFolder);
                        }
                    } else {
                        if (getItem().getSelectMode() == MultiSelectTreeValue.FULL_SELECT) {
                            setGraphic(selectedCloseFolder);
                        } else if (getItem().getSelectMode() == MultiSelectTreeValue.HALF_SELECT) {
                            setGraphic(halfSelectedCloseFolder);
                        } else {
                            setGraphic(closeFolder);
                        }
                    }

                }

            }
        }

        private String getString() {
            if (getItem() == null) {
                return "";
            } else {
                if (getItem().isLeaf()) {
                    return getItem().getItem().getName();
                } else {
                    return getItem().getGroup().getName();
                }
            }
        }
    }

    public List<I> getSelectedItems() {
        return new ArrayList<I>(selectedItems.values());
    }

}
