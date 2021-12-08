package ru.oz.demostatemachine.logistic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.oz.demostatemachine.common.lifecycle.ActionId;
import ru.oz.demostatemachine.common.lifecycle.StatusId;

/**
 * Статус премодерации для логистической карточки продукта.
 */
@AllArgsConstructor
public enum LogisticCardStatus implements StatusId<LogisticCard> {

    /**
     * Новый.
     */
    NEW(1, "Новый"),

    /**
     * Обработан.
     */
    PROCESSED(2, "Обработан"),

    /**
     * Обновлен - в обработанный продукт пришли изменения (новое значение атрибута).
     */
    UPDATED(3, "Обновлен"),

    /**
     * Отклонен.
     */
    REJECTED(4, "Отклонен"),

    /**
     * В работе.
     */
    IN_WORK(5, "В работе"),

    /**
     * Ошибка в ЕРП.
     */
    ERROR_IN_ERP(6, "Ошибка в ЕРП");

    /**
     * Код статуса.
     */
    @Getter
    private final int code;

    /**
     * Наименование статуса.
     */
    @Getter
    private final String name;

    /**
     * Получить статус по его коду.
     *
     * @param code код статуса.
     * @return статус.
     */
    public static LogisticCardStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (LogisticCardStatus value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalStateException("Unexpected value: " + code);
    }

    public boolean isTerminal() {
        return REJECTED.equals(this) || PROCESSED.equals(this);
    }

    @RequiredArgsConstructor
    @Getter
    public enum LogisticActionIds implements ActionId<LogisticCard> {
        TO_PROCESS("Обработать"),
        TO_REJECT("Отклонить"),
        TO_WORK("Взять в работу");

        private final String description;
    }
}
