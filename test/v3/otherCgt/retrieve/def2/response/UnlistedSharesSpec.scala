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

package v3.otherCgt.retrieve.def2.response

import play.api.libs.json.{JsError, JsObject, JsValue, Json}
import support.UnitSpec
import v3.otherCgt.retrieve.def2.model.response.UnlistedShares

class UnlistedSharesSpec extends UnitSpec {

  val validResponseJson: JsValue = Json.parse(
    """
      |{
      |     "numberOfDisposals": 1,
      |     "assetDescription": "My asset",
      |     "companyName": "Bob the Builder",
      |     "companyRegistrationNumber": "11111111",
      |     "acquisitionDate": "2025-04-10",
      |     "disposalDate": "2025-04-12",
      |     "disposalProceeds": 99999999999.99,
      |     "allowableCosts": 99999999999.99,
      |     "gainsWithBadr": 99999999999.99,
      |     "gainsWithInv": 99999999999.99,
      |     "gainsBeforeLosses": 99999999999.99,
      |     "losses": 99999999999.99,
      |     "claimOrElectionCodes": [
      |          "GHO"
      |     ],
      |     "gainsReportedOnRtt": 99999999999.99,
      |     "gainsExceedingLifetimeLimit": 99999999999.99,
      |     "gainsUnderSeis": 99999999999.99,
      |     "lossUsedAgainstGeneralIncome": 99999999999.99,
      |     "eisOrSeisReliefDueCurrentYear": 99999999999.99,
      |     "lossesUsedAgainstGeneralIncomePreviousYear": 99999999999.99,
      |     "eisOrSeisReliefDuePreviousYear": 99999999999.99,
      |     "rttTaxPaid": 99999999999.99
      |}
       """.stripMargin
  )

  val minimumValidResponseJson: JsValue = Json.parse(
    """
      |{
      |     "numberOfDisposals": 1,
      |     "assetDescription": "My asset",
      |     "companyName": "Bob the Builder",
      |     "acquisitionDate": "2025-04-10",
      |     "disposalDate": "2025-04-12",
      |     "disposalProceeds": 99999999999.99,
      |     "allowableCosts": 99999999999.99,
      |     "gainsBeforeLosses": 99999999999.99
      |}
       """.stripMargin
  )

  val validMtdResponseJson: JsValue = Json.parse(
    """
      |{
      |     "numberOfDisposals": 1,
      |     "assetDescription": "My asset",
      |     "companyName": "Bob the Builder",
      |     "companyRegistrationNumber": "11111111",
      |     "acquisitionDate": "2025-04-10",
      |     "disposalDate": "2025-04-12",
      |     "disposalProceeds": 99999999999.99,
      |     "allowableCosts": 99999999999.99,
      |     "gainsWithBadr": 99999999999.99,
      |     "gainsWithInv": 99999999999.99,
      |     "gainsBeforeLosses": 99999999999.99,
      |     "losses": 99999999999.99,
      |     "claimOrElectionCodes": [
      |          "GHO"
      |     ],
      |     "gainsReportedOnRtt": 99999999999.99,
      |     "gainsExceedingLifetimeLimit": 99999999999.99,
      |     "gainsUnderSeis": 99999999999.99,
      |     "lossUsedAgainstGeneralIncome": 99999999999.99,
      |     "eisOrSeisReliefDueCurrentYear": 99999999999.99,
      |     "lossesUsedAgainstGeneralIncomePreviousYear": 99999999999.99,
      |     "eisOrSeisReliefDuePreviousYear": 99999999999.99,
      |     "rttTaxPaid": 99999999999.99
      |}
       """.stripMargin
  )

  val invalidJson: JsValue = JsObject.empty

  val responseModel: UnlistedShares = UnlistedShares(
    numberOfDisposals = 1,
    assetDescription = "My asset",
    companyName = "Bob the Builder",
    companyRegistrationNumber = Some("11111111"),
    acquisitionDate = "2025-04-10",
    disposalDate = "2025-04-12",
    disposalProceeds = 99999999999.99,
    allowableCosts = 99999999999.99,
    gainsWithBadr = Some(99999999999.99),
    gainsWithInv = Some(99999999999.99),
    gainsBeforeLosses = 99999999999.99,
    losses = Some(99999999999.99),
    claimOrElectionCodes = Some(Seq("GHO")),
    gainsReportedOnRtt = Some(99999999999.99),
    gainsExceedingLifetimeLimit = Some(99999999999.99),
    gainsUnderSeis = Some(99999999999.99),
    lossUsedAgainstGeneralIncome = Some(99999999999.99),
    eisOrSeisReliefDueCurrentYear = Some(99999999999.99),
    lossesUsedAgainstGeneralIncomePreviousYear = Some(99999999999.99),
    eisOrSeisReliefDuePreviousYear = Some(99999999999.99),
    rttTaxPaid = Some(99999999999.99)
  )

  val minimumResponseModel: UnlistedShares = UnlistedShares(
    numberOfDisposals = 1,
    assetDescription = "My asset",
    companyName = "Bob the Builder",
    companyRegistrationNumber = None,
    acquisitionDate = "2025-04-10",
    disposalDate = "2025-04-12",
    disposalProceeds = 99999999999.99,
    allowableCosts = 99999999999.99,
    gainsWithBadr = None,
    gainsWithInv = None,
    gainsBeforeLosses = 99999999999.99,
    losses = None,
    claimOrElectionCodes = None,
    gainsReportedOnRtt = None,
    gainsExceedingLifetimeLimit = None,
    gainsUnderSeis = None,
    lossUsedAgainstGeneralIncome = None,
    eisOrSeisReliefDueCurrentYear = None,
    lossesUsedAgainstGeneralIncomePreviousYear = None,
    eisOrSeisReliefDuePreviousYear = None,
    rttTaxPaid = None
  )

  val responseDownstreamModel: UnlistedShares = UnlistedShares(
    numberOfDisposals = 1,
    assetDescription = "My asset",
    companyName = "Bob the Builder",
    companyRegistrationNumber = Some("11111111"),
    acquisitionDate = "2025-04-10",
    disposalDate = "2025-04-12",
    disposalProceeds = 99999999999.99,
    allowableCosts = 99999999999.99,
    gainsWithBadr = Some(99999999999.99),
    gainsWithInv = Some(99999999999.99),
    gainsBeforeLosses = 99999999999.99,
    losses = Some(99999999999.99),
    claimOrElectionCodes = Some(Seq("GHO")),
    gainsReportedOnRtt = Some(99999999999.99),
    gainsExceedingLifetimeLimit = Some(99999999999.99),
    gainsUnderSeis = Some(99999999999.99),
    lossUsedAgainstGeneralIncome = Some(99999999999.99),
    eisOrSeisReliefDueCurrentYear = Some(99999999999.99),
    lossesUsedAgainstGeneralIncomePreviousYear = Some(99999999999.99),
    eisOrSeisReliefDuePreviousYear = Some(99999999999.99),
    rttTaxPaid = Some(99999999999.99)
  )

  "UnlistedShares" when {
    "read from valid JSON" should {
      "produce the expected response model" in {
        validResponseJson.as[UnlistedShares] shouldBe responseModel
      }
    }

    "read from the minimum valid JSON" should {
      "produce the expected response model" in {
        minimumValidResponseJson.as[UnlistedShares] shouldBe minimumResponseModel
      }
    }

    "read from invalid JSON" should {
      "produce a JsError" in {
        invalidJson.validate[UnlistedShares] shouldBe a[JsError]
      }
    }

    "written to JSON" should {
      "produce the expected JSON" in {
        Json.toJson(responseDownstreamModel) shouldBe validMtdResponseJson
      }
    }
  }

}
