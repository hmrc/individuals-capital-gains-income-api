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

import play.api.libs.json.{JsError, JsValue, Json}
import support.UnitSpec

class UnlistedShareSpec extends UnitSpec {

  val mtdJson: JsValue = Json.parse(
    """
      |{
      |  "numberOfDisposals": 1,
      |  "assetDescription": "My asset",
      |  "companyName": "Bob the Builder",
      |  "companyRegistrationNumber": "11111111",
      |  "acquisitionDate": "2025-04-10",
      |  "disposalDate": "2025-04-12",
      |  "disposalProceeds": 100.11,
      |  "allowableCosts": 100.12,
      |  "gainsWithBadr": 100.13,
      |  "gainsWithInv": 100.14,
      |  "gainsBeforeLosses": 100.15,
      |  "losses": 100.16,
      |  "claimOrElectionCodes": [
      |    "GHO"
      |  ],
      |  "gainsReportedOnRtt": 100.17,
      |  "gainsExceedingLifetimeLimit": 100.18,
      |  "gainsUnderSeis": 100.19,
      |  "lossUsedAgainstGeneralIncome": 100.20,
      |  "eisOrSeisReliefDueCurrentYear": 100.21,
      |  "lossesUsedAgainstGeneralIncomePreviousYear": 100.22,
      |  "eisOrSeisReliefDuePreviousYear": 100.23,
      |  "rttTaxPaid": 100.24
      |}
      |""".stripMargin
  )

  val mtdRequestBody: UnlistedShare = UnlistedShare(
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

  val desJson: JsValue = Json.parse(
    """
      |{
      |  "numberOfDisposals": 1,
      |  "assetDescription": "My asset",
      |  "companyName": "Bob the Builder",
      |  "companyRegistrationNumber": "11111111",
      |  "acquisitionDate": "2025-04-10",
      |  "disposalDate": "2025-04-12",
      |  "disposalProceeds": 100.11,
      |  "allowableCosts": 100.12,
      |  "gainsWithBadr": 100.13,
      |  "gainsWithInv": 100.14,
      |  "gainsBeforeLosses": 100.15,
      |  "losses": 100.16,
      |  "claimOrElectionCodes": [
      |    "GHO"
      |  ],
      |  "gainsReportedOnRtt": 100.17,
      |  "gainsExceedingLifetimeLimit": 100.18,
      |  "gainsUnderSeis": 100.19,
      |  "lossUsedAgainstGeneralIncome": 100.20,
      |  "eisOrSeisReliefDueCurrentYear": 100.21,
      |  "lossesUsedAgainstGeneralIncomePreviousYear": 100.22,
      |  "eisOrSeisReliefDuePreviousYear": 100.23,
      |  "rttTaxPaid": 100.24
      |}
      |""".stripMargin
  )

  val invalidJson: JsValue = Json.parse(
    """
      |{
      |  "numberOfDisposals": "jythrg",
      |  "assetDescription": "My asset",
      |  "companyName": "Bob the Builder",
      |  "companyRegistrationNumber": "11111111",
      |  "acquisitionDate": "2025-04-10",
      |  "disposalDate": "2025-04-12",
      |  "disposalProceeds": 100.11,
      |  "allowableCosts": 100.12,
      |  "gainsWithBadr": 100.13,
      |  "gainsWithInv": 100.14,
      |  "gainsBeforeLosses": 100.15,
      |  "losses": 100.16,
      |  "claimOrElectionCodes": [
      |    "GHO"
      |  ],
      |  "gainsReportedOnRtt": 100.17,
      |  "gainsExceedingLifetimeLimit": 100.18,
      |  "gainsUnderSeis": 100.19,
      |  "lossUsedAgainstGeneralIncome": 100.20,
      |  "eisOrSeisReliefDueCurrentYear": 100.21,
      |  "lossesUsedAgainstGeneralIncomePreviousYear": 100.22,
      |  "eisOrSeisReliefDuePreviousYear": 100.23,
      |  "rttTaxPaid": 100.24
      |}
      |""".stripMargin
  )

  "UnlistedShare" when {
    "read from a valid JSON" should {
      "produce the expected object" in {
        mtdJson.as[UnlistedShare] shouldBe mtdRequestBody
      }
    }

    "read from an invalid JSON" should {
      "produce a JsError" in {
        invalidJson.validate[UnlistedShare] shouldBe a[JsError]
      }
    }

    "written JSON" should {
      "produce the expected JsObject" in {
        Json.toJson(mtdRequestBody) shouldBe desJson
      }
    }
  }

}
