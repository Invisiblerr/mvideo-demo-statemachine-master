package ru.oz.demostatemachine.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.oz.demostatemachine.common.lifecycle.ActionId;
import ru.oz.demostatemachine.common.lifecycle.StatusId;

/**
 * Статус премодерации для потребительской карточки продукта.
 */
@AllArgsConstructor
public enum ConsumerCardStatus implements StatusId<ConsumerCard> {

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
    IN_WORK(5, "В работе");

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
    public static ConsumerCardStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ConsumerCardStatus value : values()) {
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
    public enum ConsumerActionIds implements ActionId<ConsumerCard> {
        TO_PROCESS("Обработать"),
        TO_REJECT("Отклонить"),
        TO_WORK("Взять в работу");

        private final String description;
    }
}
