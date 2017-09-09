package ru.metal.dto;

/**
 * Created by User on 02.09.2017.
 */
public interface Formats {
    String GUID_FORMAT = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";

    /**
     * Only letters, max 60 symbols
     */
    String NAME_FORMAT = "(.){0,60}";

    /**
     * Only letters, max 150 symbols
     */
    String ADDRESS_FORMAT = "(.){0,150}";

    String COMMENT_FORMAT = "(.){0,150}";
    String BANK_FORMAT = "(.){0,100}";

    String EMAIL_FORMAT = "^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$";

    /**
     * Only digits, 13 or 15 symbols
     */
    String OGRN_FORMAT = "^(?=[0-9]*$)(?:.{13}|.{15})$";


    /**
     * Only digits, 9 symbols
     */
    String KPP_FORMAT = "[0-9]{9}";

    String NDS_FORMAT = "[0-9]{2}";

    String ACCOUNT_FORMAT = "[0-9]{20}";
    /**
     * цифры, максимум два знака после разделителя
     */
    String TWO_SYMBOLS_AFTER_LIMIT_FORMAT = "^(\\d+((\\.|\\,)\\d{1,2})?)$";

    /**
     * цифры
     */
    String INTEGER = "^(\\d+)$";

    /**
     * день месяца
     */
    String MONTH_DAY_FORMAT = "^(?:[0-9]|[12][0-9]|3[01])$";

    /**
     * формат денежных сумм
     */
    String MONEY_FORMAT = "^[-+]?[0-9]{0,15}(\\.|\\,)?[0-9]{0,2}$";

    /**
     * положительные или отрицательные, максимум 2 знака после разделителя
     */
    String ANY_INTEGER_OR_DECIMAL_TWO_DIGITS_AFTER_DELIMITER = "^[-+]?\\d+((\\.|\\,)\\d{1,2})?$";

    /**
     * положительные или отрицательные, максимум 4 знака после разделителя
     */
    String ANY_INTEGER_OR_DECIMAL_FOUR_DIGITS_AFTER_DELIMITER = "^[-+]?\\d+((\\.|\\,)\\d{1,4})?$";

    String POSITIVE_DECIMAL ="^[+]?([1-9][0-9]*(?:[\\.][0-9]*)?|0*\\.0*[1-9][0-9]*)(?:[eE][+-][0-9]+)?$";
    /**
     * положительные или отрицательные, максимум 6 знака после разделителя
     */
    String ANY_INTEGER_OR_DECIMAL_SIX_DIGITS_AFTER_DELIMITER = "^[-+]?\\d+((\\.|\\,)\\d{1,6})?$";


    String DATE_FORMAT = "dd.MM.yyyy";

    String INN_FORMAT = "^(?=[0-9]*$)(?:.{10}|.{12})$";

    String OKPO_FORMAT = "^(?=[0-9]*$)(?:.{8}|.{10})$";

    String OKVED_FORMAT = "^(?=[0-9]*$)(?:.{2}|.{4}|.{6})$";


    String BIK_FORMAT = "[0-9]{9}";

    String PATTERN_BANK_NUMBERS = "[0-9]{20}";
}
