package org.amt.microservicelabeldetector.labeldetector;

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

    // TODO ajouter un paramètre pour limiter le niveau confience minimal accepté
    /**
     *
     * <p>
     * This method detects the labels on the image.
     * </p>
     * 
     * @param image    the image to detect the labels on in base64.
     * @param nbLabels the number of labels to detect.
     * @return the result of the label detection.
     */
    ILabelDetectorResult detectLabels(byte[] image, int nbLabels) throws Exception;

    // TODO ajouter un paramètre pour limiter le niveau confience minimal accepté
    /**
     *
     * <p>
     * This method detects the labels on the image.
     * </p>
     * 
     * @param bucket   the bucket where the image is stored.
     * @param key      the key of the image.
     * @param nbLabels the number of labels to detect.
     * @return the result of the label detection.
     */
    ILabelDetectorResult detectLabels(String bucket, String key, int nbLabels) throws Exception;

    // TODO ajouter un paramètre pour limiter le niveau confience minimal accepté
    /**
     *
     * <p>
     * This method detects the labels on the image.
     * </p>
     *
     * @param url      the url of the image.
     * @param nbLabels the number of labels to detect.
     * @return the result of the label detection.
     */
    ILabelDetectorResult detectLabels(URL url, int nbLabels) throws Exception;
}
