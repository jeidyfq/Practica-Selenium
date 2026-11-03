package com.nttdata.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"html:target/cucumber/cucumber-report.html",
                "json:target/cucumber/cucumber.json",
                "com.nttdata.reporting.PdfReportPlugin"},
        features = "src/test/resources/features/ProductStore.feature",
        glue = "com.nttdata",
        tags = "@storeRegression"
)
public class StoreRegressionTest {
}
