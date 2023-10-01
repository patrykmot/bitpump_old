package com.vegasoft.bitpump.main;

public class TrainData {
    private final double TRAINING_PERCENTAGE = 80;
    private final double VERIFICATION_PERCENTAGE = 10;

    private NumericData dataTrain;

    private NumericData dataVerification;


    private NumericData dataValidation;


    public TrainData(NumericData data) {
        int all = data.getRowCount();
        int trainingIndex = (int) (all * (TRAINING_PERCENTAGE / 100.0));
        int verificationIndex = trainingIndex + (int) (all * (VERIFICATION_PERCENTAGE / 100.0));
        Utils.log().info("Data train count {}, data verification count {}, data verification count {}", trainingIndex + 1, verificationIndex - trainingIndex + 1, all - verificationIndex);
        dataTrain = data.extractRows(0, trainingIndex);
        dataVerification = data.extractRows(trainingIndex + 1, verificationIndex);
        dataValidation = data.extractRows(verificationIndex + 1, all);
        // TODO Do shuffling of data?
    }


    public NumericData getDataTrain() {
        return dataTrain;
    }

    public NumericData getDataVerification() {
        return dataVerification;
    }

    public NumericData getDataValidation() {
        return dataValidation;
    }

}
