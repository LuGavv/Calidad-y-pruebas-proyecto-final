package listeners;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private String screenshotsDir = System.getProperty("screenshots.dir", "reports/screenshots");

    @Override
    public void onTestFailure(ITestResult result) {
        Object obj = result.getInstance();
        try {
            WebDriver driver = (WebDriver) result.getTestContext().getAttribute("driver");
            if (driver == null && obj instanceof Object) {
                // intenta sacar driver desde la instancia si está disponible
                try {
                    driver = (WebDriver) obj.getClass().getDeclaredField("driver").get(obj);
                } catch (Exception ignored) {}
            }
            if (driver != null) {
                takeScreenshot(driver, result.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takeScreenshot(WebDriver driver, String testName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destDir = Paths.get(screenshotsDir);
            if (!Files.exists(destDir)) Files.createDirectories(destDir);
            String fileName = testName + "_" + System.currentTimeMillis() + ".png";
            Path dest = destDir.resolve(fileName);
            Files.copy(src.toPath(), dest);
            System.out.println("Screenshot saved to " + dest.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // otros callbacks vacíos (opcionalmente implementarlos)
    @Override public void onTestStart(ITestResult result) {}
    @Override public void onTestSuccess(ITestResult result) {}
    @Override public void onTestSkipped(ITestResult result) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}
}
