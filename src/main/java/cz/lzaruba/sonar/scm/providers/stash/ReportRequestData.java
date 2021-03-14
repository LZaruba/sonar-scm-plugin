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
package cz.lzaruba.sonar.scm.providers.stash;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
@Data
@AllArgsConstructor
public class ReportRequestData {

    public enum ReportRequestDataType {
        BOOLEAN, DATE, DURATION, LINK, NUMBER, PERCENTAGE, TEXT
    }

    private String title;
    private ReportRequestDataType type;
    private Object value;

}
