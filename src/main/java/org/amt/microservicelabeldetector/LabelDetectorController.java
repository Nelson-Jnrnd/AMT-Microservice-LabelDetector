package org.amt.microservicelabeldetector;

import org.amt.microservicelabeldetector.labeldetector.AwsLabelDetectorHelperImpl;
import org.amt.microservicelabeldetector.labeldetector.AwsServiceConfigurator;
import org.amt.microservicelabeldetector.labeldetector.LabelDetector;
import org.amt.microservicelabeldetector.labeldetector.LabelDetectorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;

@RestController
public class LabelDetectorController {

    private final LabelDetector labelDetector;

    public LabelDetectorController() {
        this.labelDetector = new AwsLabelDetectorHelperImpl(new AwsServiceConfigurator.Builder().build());
    }

    @PostMapping("/labels")
    public ResponseEntity<LabelDetectorResult> getLabels(@RequestParam("imageURL") String imageURL, @RequestParam("maxLabels") int maxLabels, @RequestParam("confidence") int confidence) {
        try {
            LabelDetectorResult result = labelDetector.detectLabels(new URL(imageURL), maxLabels, confidence);
            return ResponseEntity.ok().body(result);
        } catch (LabelDetector.DeniedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (LabelDetector.LabelDetectorRequestException | MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
