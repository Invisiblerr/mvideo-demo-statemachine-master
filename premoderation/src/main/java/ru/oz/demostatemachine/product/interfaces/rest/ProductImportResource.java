package ru.oz.demostatemachine.product.interfaces.rest;

import static ru.oz.demostatemachine.product.usecase.ImportProductDataUseCase.ImportDataRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.oz.demostatemachine.product.usecase.ImportProductDataUseCase;

/**
 * ProductImportResource.
 *
 * @author Igor_Ozol
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductImportResource {

    private final ImportProductDataUseCase usecase;

    @PostMapping("/import")
    public ResponseEntity<?> createProductData(@RequestBody ImportDataRequest request) {
        return usecase.execute(request).fold(
                error -> new ResponseEntity<>(error, HttpStatus.BAD_REQUEST),
                id -> new ResponseEntity<>("productId: " + id, HttpStatus.OK)
        );
    }
}
