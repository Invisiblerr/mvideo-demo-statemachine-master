package ru.oz.demostatemachine.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.oz.demostatemachine.common.lifecycle.ActionId;
import ru.oz.demostatemachine.common.lifecycle.StatusId;

@AllArgsConstructor
public enum ProductStatus implements StatusId<Product> {

    /**
     * Статус черновик.
     */
    DRAFT(1, "Черновик"),

    /**
     * Статус на модерации.
     */
    PREMODERATION_PROCESS(2, "На премодерации"),

    /**
     * Премодерация не пройдена.
     */
    PREMODERATION_FAILED(3, "Премодерация не пройдена"),

    /**
     * Отправлен в ERP.
     */
    SENT_IN_ERP(4, "Отправлен в ERP"),

    /**
     * Премодерация пройдена.
     */
    PREMODERATION_PASSED(5, "Премодерация пройдена");

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
    public static ProductStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ProductStatus value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }

    @RequiredArgsConstructor
    @Getter
    public enum ProductActionIds implements ActionId<Product> {
        TO_PROCESS("Обработать"),
        TO_REJECT("Отклонить"),
        TO_PREMODERATE("Перевести на премодерацию");

        private final String description;
    }

}
