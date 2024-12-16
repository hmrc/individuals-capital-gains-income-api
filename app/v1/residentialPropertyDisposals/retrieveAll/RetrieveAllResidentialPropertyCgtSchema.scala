/*
 * Copyright 2024 HM Revenue & Customs
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

package v1.residentialPropertyDisposals.retrieveAll

import api.schema.DownstreamReadable
import play.api.libs.json.Reads
import v1.residentialPropertyDisposals.retrieveAll.def1.model.response.Def1_RetrieveAllResidentialPropertyCgtResponse
import v1.residentialPropertyDisposals.retrieveAll.model.response.RetrieveAllResidentialPropertyCgtResponse

sealed trait RetrieveAllResidentialPropertyCgtSchema extends DownstreamReadable[RetrieveAllResidentialPropertyCgtResponse]

object RetrieveAllResidentialPropertyCgtSchema {

  case object Def1 extends RetrieveAllResidentialPropertyCgtSchema {
    type DownstreamResp = Def1_RetrieveAllResidentialPropertyCgtResponse
    val connectorReads: Reads[DownstreamResp] = Def1_RetrieveAllResidentialPropertyCgtResponse.reads
  }

  val schema: RetrieveAllResidentialPropertyCgtSchema = Def1

}