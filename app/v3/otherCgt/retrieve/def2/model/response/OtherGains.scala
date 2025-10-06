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

import play.api.libs.json.{Json, OFormat, Reads}

case class OtherGains(assetType: AssetType,
                      numberOfDisposals: Int,
                      assetDescription: String,
                      companyName: Option[String],
                      companyRegistrationNumber: Option[String],
                      acquisitionDate: String,
                      disposalDate: String,
                      disposalProceeds: BigDecimal,
                      allowableCosts: BigDecimal,
                      gainsWithBadr: Option[BigDecimal],
                      gainsWithInv: Option[BigDecimal],
                      gainsBeforeLosses: BigDecimal,
                      losses: Option[BigDecimal],
                      claimOrElectionCodes: Option[Seq[String]],
                      amountOfNetGain: Option[BigDecimal],
                      amountOfNetLoss: Option[BigDecimal],
                      rttTaxPaid: Option[BigDecimal])

object OtherGains {

  implicit val format: OFormat[OtherGains] = {
    implicit val assetTypeReads: Reads[AssetType] = implicitly[Reads[DownstreamAssetType]].map(_.toMtd)
    Json.format[OtherGains]
  }

}
