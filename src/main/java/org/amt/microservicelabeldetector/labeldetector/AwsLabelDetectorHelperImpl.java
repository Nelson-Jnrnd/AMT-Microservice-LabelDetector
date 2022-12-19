package org.amt.microservicelabeldetector.labeldetector;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;
import software.amazon.awssdk.utils.IoUtils;

import java.io.IOException;
import java.net.URL;
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

    public DetectLabelResult detectLabels(String bucket, String key, int maxLabels, float minConfidence) throws LabelDetectorException {
            return detectLabels(DetectLabelsRequest.builder()
                    .image(Image.builder().s3Object(S3Object.builder().bucket(bucket).name(key).build()).build()), maxLabels, minConfidence);
    }

    public DetectLabelResult detectLabels(byte[] image, int maxLabels, float minConfidence) throws LabelDetectorException {
            return detectLabels( DetectLabelsRequest.builder()
                    .image(getImage(image)), maxLabels, minConfidence);
    }

    public DetectLabelResult detectLabels(URL url, int maxLabels, float minConfidence) throws IOException, LabelDetectorException {
            return detectLabels(DetectLabelsRequest.builder()
                    .image(getImage(downloadImage(url))), maxLabels, minConfidence);
    }

    private DetectLabelResult detectLabels(DetectLabelsRequest.Builder request, int maxLabels, float minConfidence) throws LabelDetectorException {
        if (maxLabels < 1) {
            throw new InvalidParamException("Max labels must be greater than 0");
        }
        if (minConfidence < 0 || minConfidence > 100) {
            throw new InvalidParamException("Min confidence must be between 0 and 100");
        }
        if (request == null) {
            throw new InvalidParamException("Request must not be null");
        }
        try {
            return new DetectLabelResult(rekClient.detectLabels(request.maxLabels(maxLabels).minConfidence((minConfidence)).build()));

        } catch (InvalidS3ObjectException e) {
            throw new InvalidDataObjectException(e.getMessage());
        } catch (InvalidParameterException e) {
            throw new InvalidParamException(e.getMessage());
        } catch (ImageTooLargeException e) {
            throw new InvalidImageSizeException(e.getMessage());
        } catch (InvalidImageFormatException e) {
            throw new ImageFormatException(e.getMessage());
        } catch (AccessDeniedException e) {
            throw new DeniedAccessException(e.getMessage());
        } catch (ProvisionedThroughputExceededException e) {
            throw new TooManyRequestsException(e.getMessage());
        } catch (InternalServerErrorException e) {
            throw new InternalErrorException(e.getMessage());
        } catch (ThrottlingException e) {
            throw new ServiceUnavailableException(e.getMessage());
        } catch (Exception e) {
            throw new LabelDetectorException(e.getMessage());
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
    }
}
