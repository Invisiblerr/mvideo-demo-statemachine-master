package ru.oz.demostatemachine.app.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.oz.demostatemachine.logistic.model.LogisticCardStatus.LogisticActionIds.TO_WORK;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.oz.demostatemachine.common.usecases.DoActionUsecase.DoActionError;
import ru.oz.demostatemachine.common.usecases.impl.DoAction;
import ru.oz.demostatemachine.common.usecases.impl.DoAction.DoActionRequest;
import ru.oz.demostatemachine.common.usecases.impl.GetStatus;
import ru.oz.demostatemachine.logistic.interfaces.rest.LogisticCardResource;
import ru.oz.demostatemachine.logistic.interfaces.rest.TestResource2;
import ru.oz.demostatemachine.logistic.model.LogisticCard;
import ru.oz.demostatemachine.logistic.model.LogisticCardStatus.LogisticActionIds;

/**
 * Unit tests for {@link LogisticCardResource}.
 *
 * @author Igor_Ozol
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LogisticCardResourceTest.TestConfig.class)
@WebMvcTest(controllers = {BaseResource.class, LogisticCardResource.class, TestResource2.class})
class LogisticCardResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoAction<LogisticCard, LogisticActionIds> doAction;

    @MockBean
    private GetStatus<LogisticCard> getStatus;

    @Test
    void whenValidInput_thenReturns200() throws Exception {
        final DoActionRequest<LogisticCard, LogisticActionIds> dto = createDoActionRequest();
        when(doAction.execute(any())).thenReturn(Either.right(1L));

        mockMvc.perform(MockMvcRequestBuilders.post("/logistic-product-card/do-action")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(doAction, times(1)).execute(any());
    }

    @Test
    void whenDoActionError_thenReturns400() throws Exception {
        final DoActionRequest<LogisticCard, LogisticActionIds> dto = createDoActionRequest();
        when(doAction.execute(any())).thenReturn(Either.left(DoActionError.DOMAIN_OBJECT_NOT_FOUND_ERROR));

        mockMvc.perform(MockMvcRequestBuilders.post("/logistic-product-card/do-action")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(doAction, times(1)).execute(any());
    }

    private DoActionRequest<LogisticCard, LogisticActionIds> createDoActionRequest() {
        return DoActionRequest.<LogisticCard, LogisticActionIds>builder()
                .action(TO_WORK)
                .lfsId(1L)
                .build();
    }

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Configuration
    static class TestConfig {
        @Bean
        public LogisticCardResource logisticDoActionResource(
                DoAction<LogisticCard, LogisticActionIds> logisticCardDoActionUseCase,
                GetStatus<LogisticCard> getStatus) {
            return new LogisticCardResource(logisticCardDoActionUseCase, getStatus);
        }
    }
}