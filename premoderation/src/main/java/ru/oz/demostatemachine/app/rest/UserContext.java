package ru.oz.demostatemachine.app.rest;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * UserContext.
 *
 * @author Igor_Ozol
 */
@RequestScope
@Component
@Data
public class UserContext {
    private String userName;
}
