package com.mycompany.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.mycompany.steps", "com.mycompany.hooks"},
        plugin = {"pretty", "summary"},
        monochrome = true
)
public class TestRunner { }
