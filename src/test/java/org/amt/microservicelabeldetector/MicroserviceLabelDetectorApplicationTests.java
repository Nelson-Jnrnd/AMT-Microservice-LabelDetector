package org.amt.microservicelabeldetector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MicroserviceLabelDetectorApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    private static final String IMAGE_URL = "https://raw.githubusercontent.com/Nelson-Jnrnd/AMT-Microservice-LabelDetector/main/src/test/resources/car.jpg";

    @Test
    public void postLabelsShouldReturnLabel() throws Exception {
        // Given
        String url = getBaseUrl() + "/labels";
        String maxLabels = "3";
        String minConfidence = "50";
        String expectedLabel = "car";
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // When
        ResultActions response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                        .param("imageURL", IMAGE_URL)
                        .param("confidence", minConfidence)
                        .param("maxLabels", maxLabels)
        );

        // Then
        response.andExpect(result -> assertTrue(result.getResponse().getContentAsString().toLowerCase().contains(expectedLabel))).andDo(print());
    }

    @Test
    public void postLabelsShouldReturnCorrectNumberOfLabels() throws Exception {
        // Given
        String url = getBaseUrl() + "/labels";
        int maxLabels = 3;
        String minConfidence = "50";
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // When
        ResultActions response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                        .param("imageURL", IMAGE_URL)
                        .param("confidence", minConfidence)
                        .param("maxLabels", String.valueOf(maxLabels))
        );

        // Then
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(Objects.requireNonNull(response.andReturn().getResponse().getContentAsString()));
        JsonNode labelsNode = rootNode.get("labels");
        assert labelsNode.size() <= maxLabels;
    }

    @Test
    public void postLabelsShouldReturnCorrectAmountOfConfidence() throws Exception {
        // Given
        String url = getBaseUrl() + "/labels";
        String maxLabels = "3";
        int minConfidence = 50;
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // When
        ResultActions response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                        .param("imageURL", IMAGE_URL)
                        .param("confidence", String.valueOf(minConfidence))
                        .param("maxLabels", maxLabels)
        );

        // Then
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(Objects.requireNonNull(response.andReturn().getResponse().getContentAsString()));

        // Get the "labels" field from the JSON object
        JsonNode labelsNode = rootNode.get("labels");

        // Iterate over the key-value pairs in the "labels" object
        for (Iterator<Map.Entry<String, JsonNode>> it = labelsNode.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            double confidence = entry.getValue().asDouble();
            assertTrue(confidence >= minConfidence);
        }
    }

    @Test
    public void postLabelsWithoutImageURLShouldReturnError() throws Exception {
        // Given
        String url = getBaseUrl() + "/labels";
        String maxLabels = "3";
        String minConfidence = "50";
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // When
        ResultActions response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                        .param("confidence", minConfidence)
                        .param("maxLabels", maxLabels)
        );

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void postLabelsWithoutConfidenceShouldReturnError() throws Exception {
        // Given
        String url = getBaseUrl() + "/labels";
        String maxLabels = "3";
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // When
        ResultActions response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                        .param("imageURL", IMAGE_URL)
                        .param("maxLabels", maxLabels)
        );

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void postLabelsWithoutMaxLabelsShouldReturnError() throws Exception {
        // Given
        String url = getBaseUrl() + "/labels";
        String minConfidence = "50";
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // When
        ResultActions response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                        .param("imageURL", IMAGE_URL)
                        .param("confidence", minConfidence)
        );

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void postLabelsWithNonIntConfidenceShouldReturnError() throws Exception {
        // Given
        String url = getBaseUrl() + "/labels";
        String maxLabels = "3";
        String minConfidence = "invalid";
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // When
        ResultActions response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                        .param("imageURL", IMAGE_URL)
                        .param("confidence", minConfidence)
                        .param("maxLabels", maxLabels)
        );

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void postLabelsWithNonIntMaxLabelsShouldReturnError() throws Exception {
        // Given
        String url = getBaseUrl() + "/labels";
        String maxLabels = "invalid";
        String minConfidence = "50";
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // When
        ResultActions response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                        .param("imageURL", IMAGE_URL)
                        .param("confidence", minConfidence)
                        .param("maxLabels", maxLabels)
        );

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void postLabelsWithInvalidImageURLShouldReturnError() throws Exception {
        // Given
        String url = getBaseUrl() + "/labels";
        String maxLabels = "3";
        String minConfidence = "50";
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // When
        ResultActions response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                        .param("imageURL", "invalid")
                        .param("confidence", minConfidence)
                        .param("maxLabels", maxLabels)
        );

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void postLabelsWithConfidenceBelowZeroShouldReturnError() throws Exception {
        // Given
        String url = getBaseUrl() + "/labels";
        String maxLabels = "3";
        String minConfidence = "-1";
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // When
        ResultActions response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                        .param("imageURL", IMAGE_URL)
                        .param("confidence", minConfidence)
                        .param("maxLabels", maxLabels)
        );

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void postLabelsWithConfidenceAbove100ShouldReturnError() throws Exception {
        // Given
        String url = getBaseUrl() + "/labels";
        String maxLabels = "3";
        String minConfidence = "101";
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // When
        ResultActions response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                        .param("imageURL", IMAGE_URL)
                        .param("confidence", minConfidence)
                        .param("maxLabels", maxLabels)
        );

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void postLabelsWithMaxLabelsBelowZeroShouldReturnError() throws Exception {
        // Given
        String url = getBaseUrl() + "/labels";
        String maxLabels = "-1";
        String minConfidence = "50";
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // When
        ResultActions response = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(url)
                        .param("imageURL", IMAGE_URL)
                        .param("confidence", minConfidence)
                        .param("maxLabels", maxLabels)
        );

        // Then
        response.andExpect(status().isBadRequest());
    }
}
