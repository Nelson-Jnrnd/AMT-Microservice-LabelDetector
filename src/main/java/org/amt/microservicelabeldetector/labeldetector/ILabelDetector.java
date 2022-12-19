package org.amt.microservicelabeldetector.labeldetector;

import java.io.IOException;
import java.net.URL;

/**
 *
 * <p>
 * This interface represents a label detector.
 * </p>
 * 
 * @author nelson.jeanrenaud@heig-vd.ch
 * 
 */
public interface ILabelDetector {

    /**
     *
     * <p>
     * This method detects the labels on the image.
     * </p>
     * 
     * @param image    the image to detect the labels on in base64.
     * @param maxLabels the number of labels to detect.
     * @param minConfidence the minimum confidence level to accept a label.
     * @return the result of the label detection.
     */
    ILabelDetectorResult detectLabels(byte[] image, int maxLabels, float minConfidence) throws LabelDetectorException;

    /**
     *
     * <p>
     * This method detects the labels on the image.
     * </p>
     * 
     * @param bucket   the bucket where the image is stored.
     * @param key      the key of the image.
     * @param maxLabels the number of labels to detect.
     * @param minConfidence the minimum confidence level to accept a label.
     * @return the result of the label detection.
     */
    ILabelDetectorResult detectLabels(String bucket, String key, int maxLabels, float minConfidence) throws LabelDetectorException;

    /**
     *
     * <p>
     * This method detects the labels on the image.
     * </p>
     *
     * @param url      the url of the image.
     * @param maxLabels the number of labels to detect.
     * @param minConfidence the minimum confidence level to accept a label.
     * @return the result of the label detection.
     */
    ILabelDetectorResult detectLabels(URL url, int maxLabels, float minConfidence) throws IOException, LabelDetectorException;

    class LabelDetectorException extends Exception {
        public LabelDetectorException(String message) {
            super(message);
        }
    }

    class LabelDetectorRequestException extends LabelDetectorException {
        public LabelDetectorRequestException(String message) {
            super(message);
        }
    }

    class DeniedAccessException extends LabelDetectorRequestException {
        public DeniedAccessException(String message) {
            super(message);
        }
    }

    class ImageFormatException extends LabelDetectorRequestException {
        public ImageFormatException(String message) {
            super(message);
        }
    }

    class InvalidImageSizeException extends LabelDetectorRequestException {
        public InvalidImageSizeException(String message) {
            super(message);
        }
    }

    class InvalidParamException extends LabelDetectorRequestException {
        public InvalidParamException(String message) {
            super(message);
        }
    }

    class InvalidDataObjectException extends LabelDetectorRequestException {
        public InvalidDataObjectException(String message) {
            super(message);
        }
    }

    class TooManyRequestsException extends LabelDetectorRequestException {
        public TooManyRequestsException(String message) {
            super(message);
        }
    }

    class InternalErrorException extends LabelDetectorException {
        public InternalErrorException(String message) {
            super(message);
        }
    }

    class ServiceUnavailableException extends LabelDetectorException {
        public ServiceUnavailableException(String message) {
            super(message);
        }
    }
}
