package ru.metal.api.common.search;

import java.io.Serializable;

/**
 * Критерий сортировки
 * @author kkiselev
 */
public class SortCriteria implements Serializable {
    /**
     * Поле, по которому необходимо выполнить сортировку
     */
    private String sortedBy;
    /**
     * Условие сортировки (по возрастанию или убыванию)
     */
    private boolean ascending;
    /**
     * NULL значения по колонке должны отображаться первым в списке результатов
     */
    private boolean nullFirst;

    public SortCriteria(String sortedBy, boolean ascending) {
        this.sortedBy = sortedBy;
        this.ascending = ascending;
    }

    public SortCriteria() {
    }

    public String getSortedBy() {
        return sortedBy;
    }

    public boolean isAscending() {
        return ascending;
    }

    public boolean isNullFirst() {
        return nullFirst;
    }

    public void setNullFirst(boolean nullFirst) {
        this.nullFirst = nullFirst;
    }
}
