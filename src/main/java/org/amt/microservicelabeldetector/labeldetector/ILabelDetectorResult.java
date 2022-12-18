package org.amt.microservicelabeldetector.labeldetector;

import java.util.Map;

/**
 *
 * <p>
 *     This interface represents the result of a label detection on an image.
 * </p>
 * @author      nelson.jeanrenaud@heig-vd.ch
 */
public interface ILabelDetectorResult {
    /**
     * <p>
     * This method returns the list of labels detected on the image.
     * </p>
     *
     * @return the list of labels detected on the image.
     */
    Map<String, Float> getLabels();

    /**
     * <p>
     *     This method returns the number of labels detected on the image.
     * </p>
     * @return     the number of labels detected on the image.
     */
    int getNbLabels();

    /**
     * <p>
     *     This method returns the result of the detection in json format.
     * </p>
     * @return     the result of the detection in json format.
     */
    String toJson();

}