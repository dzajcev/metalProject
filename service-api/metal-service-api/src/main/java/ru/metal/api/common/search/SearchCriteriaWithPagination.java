package ru.metal.api.common.search;

/**
 * Критерий поиска c постраничной навигацией
 * User: KKiselev
 * Date: 04.07.14
 * Time: 11:54
 */
public class    SearchCriteriaWithPagination extends AbstractSearchCriteria {

    private Integer pageIndex;
    private Integer elementsPerPage;
    /**
     *  Параметры сортировки. Используется при необходимости.
     *  Необязательное поле.
     */
    private SortCriteria sortCriteria;

    public SortCriteria getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(SortCriteria sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getElementsPerPage() {
        return elementsPerPage;
    }

    public void setElementsPerPage(Integer elementsPerPage) {
        this.elementsPerPage = elementsPerPage;
    }
}
