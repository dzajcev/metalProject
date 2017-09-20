package ru.metal.gui.controllers.order;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import ru.common.api.dto.CellFormats;
import ru.metal.dto.OrderBodyFx;
import ru.metal.dto.GoodFx;
import ru.metal.gui.StartPage;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.controllers.nomenclature.NomenclatureForm;
import ru.metal.gui.controls.tableviewcontrol.TableViewChangeProperty;
import ru.metal.gui.controls.tableviewcontrol.ValidatedTableView;
import ru.metal.gui.controls.tableviewcontrol.customcells.CellFormatKeys;
import ru.metal.gui.windows.Window;

import java.math.BigDecimal;

/**
 * Created by User on 06.09.2017.
 */
public class OrderBodyTable extends ValidatedTableView<OrderBodyFx> {
    private ObservableList<OrderBodyFx> orderBodySource = FXCollections.observableArrayList();
    private FilteredList<OrderBodyFx> orderBodyFilteredList = new FilteredList<>(orderBodySource);
    private TableViewChangeProperty<OrderBodyFx> orderBodyTableViewChangeProperty;

    private OrderBodyInfo orderBodyInfo = new OrderBodyInfo();

    private void createNomenclatureForm() {
        Window<AbstractController, NomenclatureForm> window = StartPage.getWindow(NomenclatureForm.ID);
        final Window[] windows = new Window[1];
        if (window != null && !window.getContent().isObtainMode()) {
            window.setCloseRequest(true);
            window = null;
        }
        NomenclatureForm nomenclatureForm;
        if (window == null) {
            nomenclatureForm = new NomenclatureForm(true);
            nomenclatureForm.setObtainMode(true);
            window = StartPage.openContent(nomenclatureForm, null, null);
            window.setTitle("Товары");
            window.setClosable(true);
            window.setMinimizable(true);
            window.setMaximizable(true);
            window.setModal(true);
            windows[0] = window;
            nomenclatureForm.obtainItemProperty().addListener(new ChangeListener<GoodFx>() {
                @Override
                public void changed(ObservableValue<? extends GoodFx> observable, GoodFx oldValue, GoodFx newValue) {
                    if (newValue != null) {
                        windows[0].setCloseRequest(true);
                        boolean exist = false;
                        for (OrderBodyFx bodyFx : orderBodySource) {
                            if (bodyFx.getGood().getGuid().equals(newValue.getGuid())) {
                                getSelectionModel().select(bodyFx);
                                exist = true;
                                break;
                            }
                        }
                        if (!exist) {
                            OrderBodyFx orderBodyFx = new OrderBodyFx();
                            orderBodyFx.setGood(newValue);
                            orderBodySource.add(orderBodyFx);
                        }

                    }
                }
            });
        }
        window.setModal(true);
        StartPage.addWindow(window);
    }

    public OrderBodyTable() {
        orderBodyTableViewChangeProperty = new TableViewChangeProperty<>(this, orderBodySource);
        setEditable(true);

        TableColumn<OrderBodyFx, Integer> npp = new TableColumn("№ п.п");
        npp.setMinWidth(50);
        npp.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderBodyFx, Integer>, ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<OrderBodyFx, Integer> p) {
                ObservableValue simpleIntegerProperty = new SimpleIntegerProperty(p.getTableView().getItems().indexOf(p.getValue()) + 1);
                return simpleIntegerProperty;
            }
        });

        TableColumn<OrderBodyFx, String> goodName = new TableColumn("Наименование*");
        goodName.setMinWidth(100);
        goodName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderBodyFx, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderBodyFx, String> p) {
                return p.getValue().getGood().nameProperty();
            }
        });

        TableColumn<OrderBodyFx, String> okei = new TableColumn("Ед. изм");
        okei.setMinWidth(100);
        okei.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderBodyFx, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderBodyFx, String> p) {
                return p.getValue().getGood().getOkei().nameProperty();
            }
        });

        Callback<TableColumn<OrderBodyFx, Double>, TableCell<OrderBodyFx, Double>> doubleCellFactory = getTextCellFactory(Double.class);

        TableColumn<OrderBodyFx, Double> count = new TableColumn("Количество*");
        count.setMinWidth(100);
        count.setCellValueFactory(new PropertyValueFactory<>("count"));
        count.setCellFactory(doubleCellFactory);
        count.getProperties().put(CellFormatKeys.NUMERIC_CELL_PATTERN, CellFormats.THREE_DECIMAL_PLACES);

        TableColumn<OrderBodyFx, Double> price = new TableColumn("Цена*");
        price.setMinWidth(100);
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        price.setCellFactory(doubleCellFactory);
        price.getProperties().put(CellFormatKeys.NUMERIC_CELL_PATTERN, CellFormats.TWO_DECIMAL_PLACES);

        TableColumn<OrderBodyFx, Integer> nds = new TableColumn("НДС");
        nds.setMinWidth(100);
        nds.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderBodyFx, Integer>, ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<OrderBodyFx, Integer> p) {
                ObservableValue integerProperty = p.getValue().getGood().ndsProperty();
                return integerProperty;
            }
        });

        TableColumn<OrderBodyFx, Double> summa = new TableColumn("Сумма");
        summa.setMinWidth(100);
        summa.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderBodyFx, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<OrderBodyFx, Double> p) {
                DoubleBinding multiply = p.getValue().countProperty().multiply(p.getValue().priceProperty());
                ObservableValue integerProperty = multiply;
                recalc();
                return integerProperty;
            }
        });

        summa.getProperties().put(CellFormatKeys.NUMERIC_CELL_PATTERN, CellFormats.TWO_DECIMAL_PLACES);
        summa.setCellFactory(doubleCellFactory);
        getColumns().addAll(npp, goodName, okei, count, price, nds, summa);

        SortedList<OrderBodyFx> sortedList = new SortedList<>(orderBodyFilteredList);
        sortedList.comparatorProperty().bind(comparatorProperty());
        setItems(sortedList);

        ContextMenu cm = new ContextMenu();
        MenuItem add = new MenuItem("Добавить");
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createNomenclatureForm();
            }
        });
        MenuItem del = new MenuItem("Удалить");
        del.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                OrderBodyFx selectedItem = getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    orderBodySource.remove(selectedItem);
                }
            }
        });
        cm.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                OrderBodyFx selectedItem = getSelectionModel().getSelectedItem();
                if (selectedItem != null && selectedItem.getGuid() == null) {
                    del.setVisible(true);
                } else {
                    del.setVisible(false);
                }
            }
        });
        cm.getItems().addAll(add, del);
        setContextMenu(cm);
    }

    private void recalc() {
        double sum = 0;
        double sumWoNds = 0;
        double weight = 0;
        int count = 0;
        for (OrderBodyFx orderBodyFx : getOrderBodySource()) {
            count++;
            sum += orderBodyFx.getCount() * orderBodyFx.getPrice();
            double rowSum = orderBodyFx.getCount() * orderBodyFx.getPrice();
            double preResult0 = 1 + new Double(orderBodyFx.getGood().getNds()) / new Double(100);
            double preResult = rowSum / preResult0;
            BigDecimal bd = new BigDecimal(preResult);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            sumWoNds += bd.doubleValue();
            //todo: calc weight
        }
        orderBodyInfo.setRowCount(count);
        orderBodyInfo.setSum(sum);
        orderBodyInfo.setSumWoNds(sumWoNds);
        orderBodyInfo.setWeight(weight);
    }

    public ObservableList<OrderBodyFx> getOrderBodySource() {
        return orderBodySource;
    }

    public boolean isOrderBodyTableViewChangeProperty() {
        return orderBodyTableViewChangeProperty.get();
    }

    public TableViewChangeProperty<OrderBodyFx> orderBodyTableViewChangePropertyProperty() {
        return orderBodyTableViewChangeProperty;
    }

    public void setOrderBodyTableViewChangeProperty(boolean orderBodyTableViewChangeProperty) {
        this.orderBodyTableViewChangeProperty.set(orderBodyTableViewChangeProperty);
    }

    public OrderBodyInfo getOrderBodyInfo() {
        return orderBodyInfo;
    }

    public static class OrderBodyInfo {
        private DoubleProperty sum = new SimpleDoubleProperty();

        private DoubleProperty sumWoNds = new SimpleDoubleProperty();

        private IntegerProperty rowCount = new SimpleIntegerProperty();

        private DoubleProperty weight = new SimpleDoubleProperty();

        public double getSum() {
            return sum.get();
        }

        public DoubleProperty sumProperty() {
            return sum;
        }

        public void setSum(double sum) {
            this.sum.set(sum);
        }

        public double getSumWoNds() {
            return sumWoNds.get();
        }

        public DoubleProperty sumWoNdsProperty() {
            return sumWoNds;
        }

        public void setSumWoNds(double sumWoNds) {
            this.sumWoNds.set(sumWoNds);
        }

        public int getRowCount() {
            return rowCount.get();
        }

        public IntegerProperty rowCountProperty() {
            return rowCount;
        }

        public void setRowCount(int rowCount) {
            this.rowCount.set(rowCount);
        }

        public double getWeight() {
            return weight.get();
        }

        public DoubleProperty weightProperty() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight.set(weight);
        }
    }
}
