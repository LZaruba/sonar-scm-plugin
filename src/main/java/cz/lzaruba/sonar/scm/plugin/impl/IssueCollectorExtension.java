/*
 * Copyright 2021 Lukas Zaruba, lukas.zaruba@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.lzaruba.sonar.scm.plugin.impl;

import cz.lzaruba.sonar.scm.model.Issue;
import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.MeasureComputer;

import java.lang.reflect.Field;

import static java.util.stream.Collectors.toList;

/**
 * Extension that is invoked for each file that is processed in the analysis.
 * It stores the issues together with its resources in the {@link ThreadLocal}
 * to make it accessible down the stream after the whole analysis is finished.
 *
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class IssueCollectorExtension implements MeasureComputer {

    public static final ThreadLocal<IssuesHolder> ISSUES_CACHE = new ThreadLocal<>();
    private static final String NOT_AVAILABLE = "<Message not available>";
    private static final String DEFAULT_ISSUE = "org.sonar.core.issue.DefaultIssue";

    @Override
    public void compute(MeasureComputerContext context) {
        if (context.getComponent().getType() != Component.Type.FILE) {
            return;
        }

        IssuesHolder holder = ISSUES_CACHE.get();
        if (holder == null) {
            holder = new IssuesHolder();
            ISSUES_CACHE.set(holder);
        }

        holder.getIssues().addAll(context.getIssues().stream()
                .map(i -> convertIssue(i, context.getComponent().getKey()))
                .collect(toList()));
    }

    private Issue convertIssue(org.sonar.api.ce.measure.Issue issue, String component) {
        return new Issue(issue.key(), issue.ruleKey().toString(), issue.severity(), issue.type().name(),
                reflectGetValue(issue, "message", NOT_AVAILABLE), component,
                reflectGetValue(issue, "line", null));
    }

    private <T> T reflectGetValue(org.sonar.api.ce.measure.Issue issue, String fieldName, T defaultValue) {
        if (!issue.getClass().getName().equals(DEFAULT_ISSUE)) {
            return defaultValue;
        }

        try {
            Field field = issue.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(issue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Expecting instance of %s, got %s",
                    DEFAULT_ISSUE, issue.getClass().getName()));
        }
    }

    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext defContext) {
        return defContext.newDefinitionBuilder()
                .setOutputMetrics(CustomMetrics.METRIC_KEY)
                .build();
    }

}

