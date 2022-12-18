package org.amt.microservicelabeldetector;

import org.amt.microservicelabeldetector.labeldetector.AwsLabelDetectorHelperImpl;
import org.amt.microservicelabeldetector.labeldetector.AwsServiceConfigurator;
import org.amt.microservicelabeldetector.labeldetector.ILabelDetector;
import org.amt.microservicelabeldetector.labeldetector.ILabelDetectorResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;

@RestController
public class LabelDetectorController {

    private final ILabelDetector labelDetector;

    public LabelDetectorController() {
        this.labelDetector = new AwsLabelDetectorHelperImpl(new AwsServiceConfigurator.Builder().build());
    }

    @PostMapping("/labels")
    public ResponseEntity<String> getLabels(@RequestParam("imageURL") String imageURL) throws Exception {
            ILabelDetectorResult result = labelDetector.detectLabels(new URL(imageURL), 10);
            return ResponseEntity.ok().body(result.toJson());
    }
}
