package ru.metal.gui.controls.tableviewcontrol.customcells;

import javafx.scene.control.DatePicker;
import ru.common.api.dto.AbstractDto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;

public class DatePickerCell<T extends AbstractDto> extends AbstractCell<T, Date> {

    public static final Date EMPTY_DATE;

    static {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1900);
        c.set(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        EMPTY_DATE = c.getTime();
    }

    private DatePicker datePicker;

    public DatePickerCell() {
        super(Date.class);
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();
            setText(null);
            setGraphic(datePicker);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        if (getDate() != null) {
            setText(getDate().toString());
        } else {
            setText(null);
        }
        setGraphic(null);
    }

    @Override
    public void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (datePicker != null) {
                    datePicker.setValue(getDate());
                }
                setText(null);
                setGraphic(datePicker);
            } else {
                if (getDate() != null) {
                    setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                    setIsNull(false);
                } else {
                    setText(null);
                    setIsNull(true);
                    setItem(EMPTY_DATE);
                }
                setGraphic(null);
            }
        }
    }

    private void createDatePicker() {
        datePicker = new DatePicker(getDate());
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        datePicker.setValue(getDate() == null ? LocalDate.now() : getDate());
        datePicker.setOnAction((e) -> {
            DatePickerCell.this.setStyle(null);
            if (datePicker.getValue() != null) {
                commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            } else {
                commitEdit(null);
            }
        });
    }

    private LocalDate getDate() {
        return getItem() == null ? null : getItem().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}