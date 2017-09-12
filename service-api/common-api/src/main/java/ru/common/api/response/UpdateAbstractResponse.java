package ru.common.api.response;


import ru.common.api.dto.UpdateResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16.08.2017.
 */
public abstract class UpdateAbstractResponse<RESULT extends UpdateResult> extends AbstractResponse {
    /**
     * Информация об ошибке
     */
    private Error error;
    /**
     * Результат импорта сведений по каждой записи
     */
    private List<RESULT> importResults;


    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public List<RESULT> getImportResults() {
        if (importResults == null) {
            this.importResults = new ArrayList<>();
        }
        return importResults;
    }

    public void setImportResults(List<RESULT> importResults) {
        this.importResults = importResults;
    }
}
