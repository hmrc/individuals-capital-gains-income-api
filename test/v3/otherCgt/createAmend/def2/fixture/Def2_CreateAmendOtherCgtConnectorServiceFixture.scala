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

package v3.otherCgt.createAmend.def2.fixture

import v3.otherCgt.createAmend.def2.model.request.{
  Adjustments,
  Cryptoasset,
  Def2_CreateAmendOtherCgtRequestBody,
  GainExcludedIndexedSecurities,
  LifetimeAllowance,
  Losses,
  NonStandardGains,
  OtherGain,
  QualifyingAssetHoldingCompany,
  UnlistedShare
}

object Def2_CreateAmendOtherCgtConnectorServiceFixture {

  val cryptoassetAll: Cryptoasset = Cryptoasset(
    1,
    "description string",
    "Name of token",
    "2025-04-04",
    "2025-05-04",
    100.11,
    100.12,
    Some(100.13),
    100.14,
    Some(100.15),
    Some(Seq("GHO")),
    Some(100.16),
    None,
    Some(100.18)
  )

  val cryptoassetMandatory: Cryptoasset = Cryptoasset(
    1,
    "description string",
    "Name of token",
    "2025-04-04",
    "2025-05-04",
    100.11,
    100.12,
    None,
    100.14,
    None,
    None,
    None,
    None,
    None
  )

  val otherGainAll: OtherGain = OtherGain(
    "other-property",
    1,
    "example of this asset",
    Some("Bob the Builder"),
    Some("11111111"),
    "2025-04-07",
    "2025-07-10",
    100.11,
    100.12,
    Some(100.13),
    Some(100.14),
    100.15,
    Some(100.16),
    Some(Seq("PRR")),
    Some(100.17),
    None,
    Some(100.19)
  )

  val otherGainMandatory: OtherGain = OtherGain(
    "other-property",
    1,
    "example of this asset",
    Some("Bob the Builder"),
    Some("11111111"),
    "2025-04-07",
    "2025-07-10",
    100.11,
    100.12,
    None,
    None,
    100.15,
    None,
    None,
    None,
    None,
    None
  )

  val unlistedSharesAll: UnlistedShare = UnlistedShare(
    1,
    "My asset",
    "Bob the Builder",
    Some("11111111"),
    "2025-04-10",
    "2025-04-12",
    100.11,
    100.12,
    Some(100.13),
    Some(100.14),
    100.15,
    Some(100.16),
    Some(Seq("GHO")),
    Some(100.17),
    Some(100.18),
    Some(100.19),
    Some(100.20),
    Some(100.21),
    Some(100.22),
    Some(100.23),
    Some(100.24)
  )

  val unlistedSharesMandatory: UnlistedShare = UnlistedShare(
    1,
    "My asset",
    "Bob the Builder",
    Some("11111111"),
    "2025-04-10",
    "2025-04-12",
    100.11,
    100.12,
    None,
    None,
    100.15,
    None,
    None,
    None,
    None,
    None,
    None,
    None,
    None,
    None,
    None
  )

  val gainExcludedIndexedSecuritiesAll: GainExcludedIndexedSecurities = GainExcludedIndexedSecurities(
    Some(100.11)
  )

  val qualifyingAssetHoldingCompanyAll: QualifyingAssetHoldingCompany = QualifyingAssetHoldingCompany(
    Some(100.11),
    Some(100.12)
  )

  val nonStandardGainsAll: NonStandardGains = NonStandardGains(
    Some(100.11),
    Some(100.12),
    Some(100.13),
    Some(100.14)
  )

  val lossesAll: Losses = Losses(
    Some(100.11),
    Some(100.12),
    Some(100.13),
    Some(100.14)
  )

  val adjustmentsAll: Adjustments = Adjustments(
    Some(100.11)
  )

  val lifetimeAllowanceAll: LifetimeAllowance = LifetimeAllowance(
    Some(100.11),
    Some(100.12)
  )

  val mtdRequestBodyAll: Def2_CreateAmendOtherCgtRequestBody =
    Def2_CreateAmendOtherCgtRequestBody(
      Some(Seq(cryptoassetAll)),
      Some(Seq(otherGainAll)),
      Some(Seq(unlistedSharesAll)),
      Some(gainExcludedIndexedSecuritiesAll),
      Some(qualifyingAssetHoldingCompanyAll),
      Some(nonStandardGainsAll),
      Some(lossesAll),
      Some(adjustmentsAll),
      Some(lifetimeAllowanceAll)
    )

  val mtdRequestBodyMandatory: Def2_CreateAmendOtherCgtRequestBody =
    Def2_CreateAmendOtherCgtRequestBody(
      Some(Seq(cryptoassetMandatory)),
      Some(Seq(otherGainMandatory)),
      Some(Seq(unlistedSharesMandatory)),
      None,
      None,
      None,
      None,
      None,
      None)

}
