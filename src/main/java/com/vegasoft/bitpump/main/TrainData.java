package com.vegasoft.bitpump.main;

public class TrainData {
    private final double TRAINING_PERCENTAGE = 80;
    private final double VERIFICATION_PERCENTAGE = 10;

    private NumericData trainInput;
    private NumericData trainOutput;

    private NumericData verificationInput;

    private NumericData verificationOutput;

    private NumericData validationInput;

    private NumericData validationOutput;

    public TrainData(NumericData input, NumericData output) {
        assert input.getRowCount() == output.getRowCount();
        int all = input.getRowCount();
        int trainingIndex = (int) (all * (TRAINING_PERCENTAGE / 100.0));
        int verificationIndex = trainingIndex + (int) (all * (VERIFICATION_PERCENTAGE / 100.0));
        Utils.log().info("Data train count {}, data verification count {}, data verification count {}", trainingIndex + 1, verificationIndex - trainingIndex + 1, all - verificationIndex);
        trainInput = input.extractRows(0, trainingIndex);
        trainOutput = output.extractRows(0, trainingIndex);
        verificationInput = input.extractRows(trainingIndex + 1, verificationIndex);
        verificationOutput = output.extractRows(trainingIndex + 1, verificationIndex);
        validationInput = input.extractRows(verificationIndex + 1, all - 1);
        validationOutput = output.extractRows(verificationIndex + 1, all - 1);
    }

    public NumericData getTrainInput() {
        return trainInput;
    }

    public NumericData getTrainOutput() {
        return trainOutput;
    }

    public NumericData getVerificationInput() {
        return verificationInput;
    }

    public NumericData getVerificationOutput() {
        return verificationOutput;
    }

    public NumericData getValidationInput() {
        return validationInput;
    }

    public NumericData getValidationOutput() {
        return validationOutput;
    }
}
