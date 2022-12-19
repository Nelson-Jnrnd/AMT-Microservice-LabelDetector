package org.amt.microservicelabeldetector.labeldetector;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.utils.IoUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AwsLabelDetectorHelperImplTest {
    private static ILabelDetector labelDetector;
    private static byte[] imageBytes;
    private static URL imageURL;
    private static final String EXPECTED_LABEL = "Car";
    private final static String TEST_DATAOBJECT = "amt.team08.diduno.education";
    private final static String TEST_DATAOBJECT_KEY = "car";

    @BeforeAll
    public static void setUp() throws IOException {
        labelDetector = new AwsLabelDetectorHelperImpl(new AwsServiceConfigurator.Builder().build());
        imageBytes = IoUtils.toByteArray(Files.newInputStream(Paths.get("src/test/resources/car.jpg")));
        imageURL = new URL("https://raw.githubusercontent.com/Nelson-Jnrnd/AMT-Microservice-LabelDetector/main/src/test/resources/car.jpg");
    }

    // TODO pas de tests concernant la labélisation d'une image depuis le s3

    @Test
    public void testDetectLabelsFromByteArrayShouldReturnCorrectLabels() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        ILabelDetectorResult result = labelDetector.detectLabels(imageBytes, expectedNumberOfLabels, minimumConfidence);

        // Then
        assert (result.getLabels().containsKey(EXPECTED_LABEL));
    }

    @Test
    public void testDetectLabelsFromByteArrayShouldReturnTheCorrectNumberOfLabels() throws Exception {
        // Given
        final int maxNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        ILabelDetectorResult result = labelDetector.detectLabels(imageBytes, maxNumberOfLabels, minimumConfidence);

        // Then
        assert (result.getLabels().size() <= maxNumberOfLabels);
    }

    @Test
    public void testDetectLabelsFromByteArrayShouldReturnConfidenceGreaterThanMinConfidence() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        ILabelDetectorResult result = labelDetector.detectLabels(imageBytes, expectedNumberOfLabels, minimumConfidence);

        // Then
        result.getLabels().forEach((label, confidence) -> assertTrue(confidence > minimumConfidence));
    }

    @Test
    public void testDetectLabelsFromByteArrayShouldReturnConfidenceLessThanOneHundred() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        ILabelDetectorResult result = labelDetector.detectLabels(imageBytes, expectedNumberOfLabels, minimumConfidence);

        // Then
        result.getLabels().forEach((label, confidence) -> assertTrue(confidence < 100));
    }

    @Test
    public void testDetectLabelsFromByteArrayWithMaxLabelsInferiorToZeroShouldRaiseException() throws Exception {
        // Given
        final int expectedNumberOfLabels = -1;
        final int minimumConfidence = 50;

        // When
        try {
            labelDetector.detectLabels(imageBytes, expectedNumberOfLabels, minimumConfidence);
            assert false;
        } catch (ILabelDetector.InvalidParamException e) {
            assert true;
        }
    }

    @Test
    public void testDetectLabelsFromByteArrayWithMinConfidenceInferiorToZeroShouldRaiseException() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = -1;

        // When
        try {
            labelDetector.detectLabels(imageBytes, expectedNumberOfLabels, minimumConfidence);
            assert false;
        } catch (ILabelDetector.InvalidParamException e) {
            assert true;
        }
    }

    @Test
    public void testDetectLabelsFromByteArrayWithMinConfidenceSuperiorToOneHundredShouldRaiseException() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 101;

        // When
        try {
            labelDetector.detectLabels(imageBytes, expectedNumberOfLabels, minimumConfidence);
            assert false;
        } catch (ILabelDetector.InvalidParamException e) {
            assert true;
        }
    }

    @Test
    public void testDetectLabelsFromByteArrayWithNullImageShouldRaiseException() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        try {
            labelDetector.detectLabels((byte[]) null, expectedNumberOfLabels, minimumConfidence);
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
    }

    @Test
    public void testDetectLabelsFromURLShouldReturnCorrectLabels() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        ILabelDetectorResult result = labelDetector.detectLabels(imageURL, expectedNumberOfLabels, minimumConfidence);

        // Then
        assert (result.getLabels().containsKey(EXPECTED_LABEL));
    }

    @Test
    public void testDetectLabelsFromURLShouldReturnTheCorrectNumberOfLabels() throws Exception {
        // Given
        final int maxNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        ILabelDetectorResult result = labelDetector.detectLabels(imageURL, maxNumberOfLabels, minimumConfidence);

        // Then
        assert (result.getLabels().size() <= maxNumberOfLabels);
    }

    @Test
    public void testDetectLabelsFromURLShouldReturnConfidenceGreaterThanMinConfidence() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        ILabelDetectorResult result = labelDetector.detectLabels(imageURL, expectedNumberOfLabels, minimumConfidence);

        // Then
        result.getLabels().forEach((label, confidence) -> assertTrue(confidence > minimumConfidence));
    }

    @Test
    public void testDetectLabelsFromURLShouldReturnConfidenceLessThanOneHundred() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        ILabelDetectorResult result = labelDetector.detectLabels(imageURL, expectedNumberOfLabels, minimumConfidence);

        // Then
        result.getLabels().forEach((label, confidence) -> assertTrue(confidence < 100));
    }

    @Test
    public void testDetectLabelsFromURLWithMaxLabelsInferiorToZeroShouldRaiseException() throws Exception {
        // Given
        final int expectedNumberOfLabels = -1;
        final int minimumConfidence = 50;

        // When
        try {
            labelDetector.detectLabels(imageURL, expectedNumberOfLabels, minimumConfidence);
            assert false;
        } catch (ILabelDetector.InvalidParamException e) {
            assert true;
        }
    }

    @Test
    public void testDetectLabelsFromURLWithMinConfidenceInferiorToZeroShouldRaiseException() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = -1;

        // When
        try {
            labelDetector.detectLabels(imageURL, expectedNumberOfLabels, minimumConfidence);
            assert false;
        } catch (ILabelDetector.InvalidParamException e) {
            assert true;
        }
    }

    @Test
    public void testDetectLabelsFromURLWithMinConfidenceSuperiorToOneHundredShouldRaiseException() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 101;

        // When
        try {
            labelDetector.detectLabels(imageURL, expectedNumberOfLabels, minimumConfidence);
            assert false;
        } catch (ILabelDetector.InvalidParamException e) {
            assert true;
        }
    }

    @Test
    public void testDetectLabelsFromURLWithNullImageShouldRaiseException() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        try {
            labelDetector.detectLabels((URL) null, expectedNumberOfLabels, minimumConfidence);
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
    }

    @Test
    public void testDetectLabelsFromDataObjectShouldReturnCorrectLabels() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        ILabelDetectorResult result = labelDetector.detectLabels(TEST_DATAOBJECT, TEST_DATAOBJECT_KEY, expectedNumberOfLabels, minimumConfidence);

        // Then
        assert (result.getLabels().containsKey(EXPECTED_LABEL));
    }

    @Test
    public void testDetectLabelsFromDataObjectShouldReturnTheCorrectNumberOfLabels() throws Exception {
        // Given
        final int maxNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        ILabelDetectorResult result = labelDetector.detectLabels(TEST_DATAOBJECT, TEST_DATAOBJECT_KEY, maxNumberOfLabels, minimumConfidence);

        // Then
        assert (result.getLabels().size() <= maxNumberOfLabels);
    }

    @Test
    public void testDetectLabelsFromDataObjectShouldReturnConfidenceGreaterThanMinConfidence() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        ILabelDetectorResult result = labelDetector.detectLabels(TEST_DATAOBJECT, TEST_DATAOBJECT_KEY, expectedNumberOfLabels, minimumConfidence);

        // Then
        result.getLabels().forEach((label, confidence) -> assertTrue(confidence > minimumConfidence));
    }

    @Test
    public void testDetectLabelsFromDataObjectShouldReturnConfidenceLessThanOneHundred() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        ILabelDetectorResult result = labelDetector.detectLabels(TEST_DATAOBJECT, TEST_DATAOBJECT_KEY, expectedNumberOfLabels, minimumConfidence);

        // Then
        result.getLabels().forEach((label, confidence) -> assertTrue(confidence < 100));
    }

    @Test
    public void testDetectLabelsFromDataObjectWithMaxLabelsInferiorToZeroShouldRaiseException() throws Exception {
        // Given
        final int expectedNumberOfLabels = -1;
        final int minimumConfidence = 50;

        // When
        try {
            labelDetector.detectLabels(TEST_DATAOBJECT, TEST_DATAOBJECT_KEY, expectedNumberOfLabels, minimumConfidence);
            assert false;
        } catch (ILabelDetector.InvalidParamException e) {
            assert true;
        }
    }

    @Test
    public void testDetectLabelsFromDataObjectWithMinConfidenceInferiorToZeroShouldRaiseException() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = -1;

        // When
        try {
            labelDetector.detectLabels(TEST_DATAOBJECT, TEST_DATAOBJECT_KEY, expectedNumberOfLabels, minimumConfidence);
            assert false;
        } catch (ILabelDetector.InvalidParamException e) {
            assert true;
        }
    }

    @Test
    public void testDetectLabelsFromDataObjectWithMinConfidenceSuperiorToOneHundredShouldRaiseException() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 101;

        // When
        try {
            labelDetector.detectLabels(TEST_DATAOBJECT, TEST_DATAOBJECT_KEY, expectedNumberOfLabels, minimumConfidence);
            assert false;
        } catch (ILabelDetector.InvalidParamException e) {
            assert true;
        }
    }

    @Test
    public void testDetectLabelsFromDataObjectWithNullDataObjectShouldRaiseException() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        try {
            labelDetector.detectLabels(null, TEST_DATAOBJECT_KEY, expectedNumberOfLabels, minimumConfidence);
            assert false;
        } catch (ILabelDetector.InvalidParamException e) {
            assert true;
        }
    }

    @Test
    public void testDetectLabelsFromDataObjectWithNullDataObjectKeyShouldRaiseException() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        try {
            labelDetector.detectLabels(TEST_DATAOBJECT, null, expectedNumberOfLabels, minimumConfidence);
            assert false;
        } catch (ILabelDetector.InvalidParamException e) {
            assert true;
        }
    }
    
    @Test
    public void testDetectLabelsFromDataObjectWithNonExistingDataObjectKeyShouldRaiseException() throws Exception {
        // Given
        final int expectedNumberOfLabels = 3;
        final int minimumConfidence = 50;

        // When
        try {
            labelDetector.detectLabels(TEST_DATAOBJECT, "nonExistingKey", expectedNumberOfLabels, minimumConfidence);
            assert false;
        } catch (ILabelDetector.InvalidDataObjectException e) {
            assert true;
        }
    }
}
