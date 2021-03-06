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

import cz.lzaruba.sonar.scm.AnalysisProcessor;
import cz.lzaruba.sonar.scm.model.Analysis;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class PostProjectAnalysisExtension implements PostProjectAnalysisTask {

    @Override
    public void finished(Context context) {
        ProjectAnalysis projectAnalysis = context.getProjectAnalysis();
        IssuesHolder holder = IssueCollectorExtension.ISSUES_CACHE.get();
        new AnalysisProcessor().process(convert(holder, projectAnalysis));
    }

    private Analysis convert(IssuesHolder holder, ProjectAnalysis projectAnalysis) {
        return new Analysis(holder.getIssues(), projectAnalysis.getScannerContext().getProperties());
    }

    @Override
    public String getDescription() {
        return "sonar-scm-plugin:post-project-extension";
    }

}
