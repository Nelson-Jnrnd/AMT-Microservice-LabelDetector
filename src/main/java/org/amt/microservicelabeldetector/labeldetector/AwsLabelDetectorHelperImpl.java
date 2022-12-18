package org.amt.microservicelabeldetector.labeldetector;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;
import software.amazon.awssdk.utils.IoUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ILabelDetector} using AWS services
 * @author De Bleser Dimitri
 * @author Peer Vincent
 * @author Nelson Jeanreneaud
 */
public class AwsLabelDetectorHelperImpl implements ILabelDetector {
    final RekognitionClient rekClient;

    public AwsLabelDetectorHelperImpl(AwsServiceConfigurator awsServiceConfigurator) {
        rekClient = RekognitionClient.builder()
                .region(awsServiceConfigurator.getRegion())
                .credentialsProvider(awsServiceConfigurator.getCredentialsProvider())
                .build();
    }

    public DetectLabelResult detectLabels(String bucket, String key, int maxLabels) throws Exception {
        try {
            DetectLabelsRequest request = DetectLabelsRequest.builder()
                    .image(Image.builder().s3Object(S3Object.builder().bucket(bucket).name(key).build()).build())
                    .maxLabels(maxLabels)
                    .build();

            return new DetectLabelResult(rekClient.detectLabels(request));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public DetectLabelResult detectLabels(byte[] image, int nbLabels) throws Exception {
        try {
            DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                    .image(getImage(image))
                    .maxLabels(nbLabels)
                    .build();

            return new DetectLabelResult(rekClient.detectLabels(detectLabelsRequest));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public DetectLabelResult detectLabels(URL url, int nbLabels) throws Exception {
        try {
            DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                    .image(getImage(downloadImage(url)))
                    .maxLabels(nbLabels)
                    .build();

            return new DetectLabelResult(rekClient.detectLabels(detectLabelsRequest));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private Image getImage(byte[] image64) {
        return Image.builder()
                .bytes(SdkBytes.fromByteArray(image64))
                .build();
    }

    private byte[] downloadImage(URL url) throws IOException {
        return IoUtils.toByteArray(url.openStream());
    }

    public static class DetectLabelResult implements ILabelDetectorResult {
        private final DetectLabelsResponse response;
        private DetectLabelResult(DetectLabelsResponse response) {
            this.response = response;
        }

        public Map<String, Float> getLabels() {
            return response.labels().stream()
                    .collect(Collectors.toMap(Label::name, Label::confidence));
        }

        /**
         * <p>
         * This method returns the number of labels detected on the image.
         * </p>
         *
         * @return the number of labels detected on the image.
         */
        @Override
        public int getNbLabels() {
            return response.labels().size();
        }

        public String toJson() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"labels\": [");
            for (Label label : response.labels()) {
                sb.append("{");
                sb.append("\"name\": \"").append(label.name()).append("\",");
                sb.append("\"confidence\": ").append(label.confidence());
                sb.append("},");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
            sb.append("}");
            return sb.toString();
        }
    }
}
