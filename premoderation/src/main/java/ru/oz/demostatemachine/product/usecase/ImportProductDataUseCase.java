package ru.oz.demostatemachine.product.usecase;

import io.vavr.control.Either;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * ImportProductDataAction.
 *
 * @author Igor
 */
public interface ImportProductDataUseCase {

    Either<ImportDataError, Void> execute(ImportDataRequest request);

    @RequiredArgsConstructor
    @Getter
    enum ImportDataError {
        IMPORT_DATA_ERROR("error occurred while import data.");

        private final String errorMsg;
    }

    @Data
    class ImportDataRequest {
        String productName;
    }
}
