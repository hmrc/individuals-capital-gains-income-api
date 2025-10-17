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

import play.api.libs.json.{JsObject, JsValue, Json}
import support.UnitSpec
import v3.otherCgt.createAmend.def2.fixture
import v3.otherCgt.createAmend.def2.fixture.Def2_CreateAmendOtherCgtConnectorServiceFixture.mtdRequestBodyAll

class Def2CreateAmendOtherCgtRequestBodySpec extends UnitSpec {

  val validJson: JsValue =
    Json
      .parse(
        s"""
           |{
           |    "cryptoassets": [
           |        {
           |            "numberOfDisposals": 1,
           |            "assetDescription": "description string",
           |            "tokenName": "Name of token",
           |            "acquisitionDate": "2025-04-04",
           |            "disposalDate": "2025-05-04",
           |            "disposalProceeds": 100.11,
           |            "allowableCosts": 100.12,
           |            "gainsWithBadr": 100.13,
           |            "gainsBeforeLosses": 100.14,
           |            "losses": 100.15,
           |            "claimOrElectionCodes": [
           |                "GHO"
           |            ],
           |            "amountOfNetGain": 100.16,
           |            "rttTaxPaid": 100.18
           |        }
           |    ],
           |    "otherGains": [
           |        {
           |            "assetType": "other-property",
           |            "numberOfDisposals": 1,
           |            "assetDescription": "example of this asset",
           |            "companyName": "Bob the Builder",
           |            "companyRegistrationNumber": "11111111",
           |            "acquisitionDate": "2025-04-07",
           |            "disposalDate": "2025-07-10",
           |            "disposalProceeds": 100.11,
           |            "allowableCosts": 100.12,
           |            "gainsWithBadr": 100.13,
           |            "gainsWithInv": 100.14,
           |            "gainsBeforeLosses": 100.15,
           |            "losses": 100.16,
           |            "claimOrElectionCodes": [
           |                "PRR"
           |            ],
           |            "amountOfNetGain": 100.17,
           |            "rttTaxPaid": 100.19
           |        }
           |    ],
           |    "unlistedShares": [
           |        {
           |            "numberOfDisposals": 1,
           |            "assetDescription": "My asset",
           |            "companyName": "Bob the Builder",
           |            "companyRegistrationNumber": "11111111",
           |            "acquisitionDate": "2025-04-10",
           |            "disposalDate": "2025-04-12",
           |            "disposalProceeds": 100.11,
           |            "allowableCosts": 100.12,
           |            "gainsWithBadr": 100.13,
           |            "gainsWithInv": 100.14,
           |            "gainsBeforeLosses": 100.15,
           |            "losses": 100.16,
           |            "claimOrElectionCodes": [
           |                "GHO"
           |            ],
           |            "gainsReportedOnRtt": 100.17,
           |            "gainsExceedingLifetimeLimit": 100.18,
           |            "gainsUnderSeis": 100.19,
           |            "lossUsedAgainstGeneralIncome": 100.20,
           |            "eisOrSeisReliefDueCurrentYear": 100.21,
           |            "lossesUsedAgainstGeneralIncomePreviousYear": 100.22,
           |            "eisOrSeisReliefDuePreviousYear": 100.23,
           |            "rttTaxPaid": 100.24
           |        }
           |    ],
           |    "gainExcludedIndexedSecurities": {
           |        "gainsFromExcludedSecurities": 100.11
           |    },
           |    "qualifyingAssetHoldingCompany": {
           |        "gainsFromQahcBeforeLosses": 100.11,
           |        "lossesFromQahc": 100.12
           |    },
           |    "nonStandardGains": {
           |        "attributedGains": 100.11,
           |        "attributedGainsRttTaxPaid": 100.12,
           |        "otherGains": 100.13,
           |        "otherGainsRttTaxPaid": 100.14
           |    },
           |    "losses": {
           |        "broughtForwardLossesUsedInCurrentYear": 100.11,
           |        "setAgainstInYearGains": 100.12,
           |        "setAgainstEarlierYear": 100.13,
           |        "lossesToCarryForward": 100.14
           |    },
           |    "adjustments": {
           |        "adjustmentAmount": 100.11
           |    },
           |    "lifetimeAllowance": {
           |        "lifetimeAllowanceBadr": 100.11,
           |        "lifetimeAllowanceInv": 100.12
           |    }
           |}
           |""".stripMargin
      )
      .as[JsObject]

  val mtdRequestBody: Def2_CreateAmendOtherCgtRequestBody = mtdRequestBodyAll

  val emptyJson: JsValue = JsObject.empty

  "Def2_CreateAmendOtherCgtRequestBody" when {
    "read from a valid JSON" should {
      "produce the expected object" in {
        validJson.as[Def2_CreateAmendOtherCgtRequestBody] shouldBe mtdRequestBody
      }
    }

    "read from an empty JSON" should {
      "produce an empty object" in {
        emptyJson.as[Def2_CreateAmendOtherCgtRequestBody] shouldBe Def2_CreateAmendOtherCgtRequestBody.empty
      }
    }

    "written JSON" should {
      "produce the expected JsObject" in {
        Json.toJson(mtdRequestBody) shouldBe validJson
      }
    }

    "written from an empty object" should {
      "produce an empty JSON" in {
        Json.toJson(Def2_CreateAmendOtherCgtRequestBody.empty) shouldBe emptyJson
      }
    }
  }

}
