package se.inera.intyg.common.support.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;

public class InternalModelResponse {

    private final String internalModel;

    public InternalModelResponse(String internalModel) {
        hasText(internalModel, "'internalModel' must not be empty");
        this.internalModel = internalModel;
    }

    public String getInternalModel() {
        return internalModel;
    }
}
