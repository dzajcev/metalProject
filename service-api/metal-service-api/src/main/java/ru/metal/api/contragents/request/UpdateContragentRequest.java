package ru.metal.api.contragents.request;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import ru.metal.api.common.request.UpdateAbstractRequest;
import ru.metal.api.contragents.dto.ContragentDto;
import ru.metal.api.nomenclature.dto.GoodDto;

/**
 * Created by User on 15.08.2017.
 */
public class UpdateContragentRequest extends UpdateAbstractRequest<ContragentDto> {

}
