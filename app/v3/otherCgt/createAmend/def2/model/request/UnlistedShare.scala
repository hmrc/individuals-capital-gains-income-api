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

case class UnlistedShare(numberOfDisposals: Int,
                         assetDescription: String,
                         companyName: String,
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
                         gainsReportedOnRtt: Option[BigDecimal],
                         gainsExceedingLifetimeLimit: Option[BigDecimal],
                         gainsUnderSeis: Option[BigDecimal],
                         lossUsedAgainstGeneralIncome: Option[BigDecimal],
                         eisOrSeisReliefDueCurrentYear: Option[BigDecimal],
                         lossesUsedAgainstGeneralIncomePreviousYear: Option[BigDecimal],
                         eisOrSeisReliefDuePreviousYear: Option[BigDecimal],
                         rttTaxPaid: Option[BigDecimal])

object UnlistedShare {

  val empty: UnlistedShare =
    UnlistedShare(0, "", "", None, "", "", 0.00, 0.00, None, None, 0.00, None, None, None, None, None, None, None, None, None, None)

  implicit val format: OFormat[UnlistedShare] = Json.format[UnlistedShare]
}
