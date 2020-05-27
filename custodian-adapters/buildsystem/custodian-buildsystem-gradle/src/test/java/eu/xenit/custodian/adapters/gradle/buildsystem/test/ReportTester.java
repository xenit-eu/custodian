package eu.xenit.custodian.adapters.gradle.buildsystem.test;

import eu.xenit.custodian.adapters.gradle.buildsystem.spi.sentinel.dto.Report;


public class ReportTester {


    public static Report.ReportBuilder withDefaults() {
        return Report.builder();
    }
}