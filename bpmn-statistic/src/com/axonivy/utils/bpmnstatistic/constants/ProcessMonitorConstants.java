package com.axonivy.utils.bpmnstatistic.constants;

public class ProcessMonitorConstants {
  public static final String REMOVE_DEFAULT_HIGHLIGHT_JS_FUNCTION = "santizeDiagram();";
  public static final String UPDATE_FREQUENCY_COUNT_FOR_TASK_FUNCTION = "addElementFrequency('%s', '%s', '%s', '%s');";
  public static final String FREQUENCY_BACKGROUND_COLOR_LEVEL_VARIABLE_PATTERN = "frequencyBackgroundColorLevel%s";
  public static final int DEFAULT_BACKGROUND_COLOR_LEVEL = 1;
  public static final int HIGHEST_LEVEL_OF_BACKGROUND_COLOR = 6;
  public static final String ADDITIONAL_INFORMATION_FORMAT = "%s instances (investigation period:%s - %s)";
  public static final String UPDATE_ADDITIONAL_INFORMATION_FUNCTION = "renderAdditionalInformation('%s')";
  public static final int DEFAULT_INITIAL_STATISTIC_NUMBER = 0;
  public static final String HYPHEN_SIGN = "-";
  public static final String QUOTED_CONTENT_PATTERN = "\"([^\"]*)\"";
  public static final String BPMN_STATISTIC_PMV_NAME = "bpmn-statistic";
  public static final String PORTAL_PMV_SUFFIX = "portal";
}
