package com.michaeldavidsim.utils.chart;

import java.util.List;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.time.LocalDate;

import com.michaeldavidsim.models.openweathermodels.WeatherResponse;

public class WeatherChartRenderer {
    public static byte[] renderChart(WeatherResponse weatherResponse, LocalDate targetDate) throws IOException {
        List<String> hours = WeatherChartData.getHourLabels(weatherResponse, targetDate);
        List<Double> rainPercent = WeatherChartData.getRainPercentages(weatherResponse, targetDate);

        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("Hourly Rain Chance")
                .xAxisTitle("Hour")
                .yAxisTitle("Rain %")
                .build();

        // Styling
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotGridLinesVisible(false);
        chart.getStyler().setAxisTickLabelsColor(Color.DARK_GRAY);
        chart.getStyler().setChartTitleFont(new Font("SansSerif", Font.BOLD, 16));
        chart.getStyler().setAxisTitleFont(new Font("SansSerif", Font.PLAIN, 12));
        chart.getStyler().setAxisTickLabelsFont(new Font("SansSerif", Font.PLAIN, 11));
        chart.getStyler().setYAxisMin(0.0);
        chart.getStyler().setYAxisMax(100.0);

        // Nice bar look
        chart.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Bar);
        chart.getStyler().setOverlapped(false);

        // Add series
        chart.addSeries("Rain %", hours, rainPercent);

        // change color based on intensity
        chart.getStyler().setSeriesColors(
            rainPercent.stream()
                    .map(p -> {
                        if (p < 30) return new Color(173,216,230); // light blue
                        else if (p < 60) return new Color(0,191,255); // medium blue
                        else return new Color(0,0,255); // dark blue
                    }).toArray(Color[]::new)
        );

        BufferedImage chartImage = BitmapEncoder.getBufferedImage(chart);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", os);
        return os.toByteArray();
    }
}
