package ru.metal.gui.controls.tableviewcontrol.customcells;

import javafx.scene.control.TableCell;
import ru.common.api.dto.AbstractDto;

/**
 * Created by User on 02.09.2017.
 */
public abstract class AbstractCell<T extends AbstractDto, E> extends TableCell<T, E> {


    protected Class<E> clazz;
    private boolean isNull;

    public void setIsNull(boolean isNull) {
        this.isNull = isNull;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setError() {
        setStyle("-fx-background-color: lightcoral;");
    }

    public AbstractCell(Class<E> clazz) {
        this.clazz = clazz;
    }

    public E getEmptyValue() {
        if (clazz == Short.class) {
            return (E) new Short(Short.MIN_VALUE);
        }
        if (clazz == Integer.class) {
            return (E) new Integer(Integer.MIN_VALUE);
        }
        if (clazz == Long.class) {
            return (E) new Long(Long.MIN_VALUE);
        }
        if (clazz == Double.class) {
            return (E) new Double(Double.MIN_VALUE);
        } else if (clazz == String.class) {
            return (E) "EMPTY";
        } else {
            return null;
        }
    }
}
