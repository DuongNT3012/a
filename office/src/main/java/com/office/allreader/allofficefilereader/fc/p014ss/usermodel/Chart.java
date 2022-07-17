package com.office.allreader.allofficefilereader.fc.p014ss.usermodel;

import com.office.allreader.allofficefilereader.fc.p014ss.usermodel.charts.ChartAxis;
import com.office.allreader.allofficefilereader.fc.p014ss.usermodel.charts.ChartAxisFactory;
import com.office.allreader.allofficefilereader.fc.p014ss.usermodel.charts.ChartData;
import com.office.allreader.allofficefilereader.fc.p014ss.usermodel.charts.ChartDataFactory;
import com.office.allreader.allofficefilereader.fc.p014ss.usermodel.charts.ChartLegend;
import com.office.allreader.allofficefilereader.fc.p014ss.usermodel.charts.ManuallyPositionable;
import java.util.List;

/* renamed from: com.allreader.office.allofficefilereader.fc.ss.usermodel.Chart */

public interface Chart extends ManuallyPositionable {
    void deleteLegend();

    List<? extends ChartAxis> getAxis();

    ChartAxisFactory getChartAxisFactory();

    ChartDataFactory getChartDataFactory();

    ChartLegend getOrCreateLegend();

    void plot(ChartData chartData, ChartAxis... chartAxisArr);
}
