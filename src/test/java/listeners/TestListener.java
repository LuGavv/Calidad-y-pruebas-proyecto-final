package listeners;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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
            File destDir = Paths.get(screenshotsDir).toFile();
            if (!destDir.exists()) destDir.mkdirs();
            String fileName = testName + "_" + System.currentTimeMillis() + ".png";
            File dest = new File(destDir, fileName);
            FileUtils.copyFile(src, dest);
            System.out.println("Screenshot saved to " + dest.getAbsolutePath());
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
