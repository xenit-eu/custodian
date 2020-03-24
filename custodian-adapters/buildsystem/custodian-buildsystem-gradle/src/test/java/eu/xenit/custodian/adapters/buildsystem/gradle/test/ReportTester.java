package eu.xenit.custodian.adapters.buildsystem.gradle.test;

import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto.Report;


public class ReportTester {


    public static Report.ReportBuilder withDefaults() {
        return Report.builder();
    }
}