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

package v3.otherCgt.createAmend.def2.model.request

import play.api.libs.json.{Json, OFormat}
import v3.otherCgt.createAmend.model.request.CreateAmendOtherCgtRequestBody

case class Def2_CreateAmendOtherCgtRequestBody(cryptoassets: Option[Seq[Cryptoasset]],
                                               otherGains: Option[Seq[OtherGain]],
                                               unlistedShares: Option[Seq[UnlistedShare]],
                                               gainExcludedIndexedSecurities: Option[GainExcludedIndexedSecurities],
                                               qualifyingAssetHoldingCompany: Option[QualifyingAssetHoldingCompany],
                                               nonStandardGains: Option[NonStandardGains],
                                               losses: Option[Losses],
                                               adjustments: Option[Adjustments],
                                               lifetimeAllowance: Option[LifetimeAllowance])
    extends CreateAmendOtherCgtRequestBody

object Def2_CreateAmendOtherCgtRequestBody {
  val empty: Def2_CreateAmendOtherCgtRequestBody = Def2_CreateAmendOtherCgtRequestBody(None, None, None, None, None, None, None, None, None)

  implicit val format: OFormat[Def2_CreateAmendOtherCgtRequestBody] = Json.format[Def2_CreateAmendOtherCgtRequestBody]

}
