/*
 * Copyright 2025 HM Revenue & Customs
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

package v3.otherCgt.retrieve.def2.model.response

import play.api.libs.json.{Json, OWrites, Reads}
import shared.models.domain.Timestamp
import v3.otherCgt.retrieve.model.response.RetrieveOtherCgtResponse

case class Def2_RetrieveOtherCgtResponse(submittedOn: Timestamp,
                                         cryptoassets: Option[Seq[Cryptoassets]],
                                         otherGains: Option[Seq[OtherGains]],
                                         unlistedShares: Option[Seq[UnlistedShares]],
                                         gainExcludedIndexedSecurities: Option[GainExcludedIndexedSecurities],
                                         qualifyingAssetHoldingCompany: Option[QualifyingAssetHoldingCompany],
                                         nonStandardGains: Option[NonStandardGains],
                                         losses: Option[Losses],
                                         adjustments: Option[Adjustments],
                                         lifetimeAllowance: Option[LifetimeAllowance])
    extends RetrieveOtherCgtResponse

object Def2_RetrieveOtherCgtResponse {
  implicit val reads: Reads[Def2_RetrieveOtherCgtResponse] = Json.reads[Def2_RetrieveOtherCgtResponse]

  implicit val writes: OWrites[Def2_RetrieveOtherCgtResponse] = Json.writes[Def2_RetrieveOtherCgtResponse]
}
