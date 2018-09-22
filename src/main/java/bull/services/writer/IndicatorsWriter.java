/*
 * Copyright (c) 2016 Bull/Atos.
 * All rights reserved.
 */
package bull.services.writer;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import bull.internal.lines.RedmineIssueLineBug;

/**
 * [Enter type description here].
 *
 * @author Bull/Atos
 */
public interface IndicatorsWriter<T> {
    void writeIssues(List<T> indicatorList, String fileName);
}
