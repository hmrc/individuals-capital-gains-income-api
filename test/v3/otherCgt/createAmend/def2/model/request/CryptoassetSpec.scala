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

class CryptoassetSpec extends UnitSpec {

  val mtdJson: JsValue = Json.parse(
    """
      |{
      |  "numberOfDisposals": 1,
      |  "assetDescription": "description string",
      |  "tokenName": "Name of token",
      |  "acquisitionDate": "2025-08-04",
      |  "disposalDate": "2025-09-04",
      |  "disposalProceeds": 100.11,
      |  "allowableCosts": 100.12,
      |  "gainsWithBadr": 100.13,
      |  "gainsBeforeLosses": 100.14,
      |  "losses": 100.15,
      |  "claimOrElectionCodes": [
      |    "GHO"
      |  ],
      |  "amountOfNetGain": 100.16,
      |  "rttTaxPaid": 100.18
      |}
      |""".stripMargin
  )

  val mtdRequestBody: Cryptoasset = Cryptoasset(
    1,
    "description string",
    "Name of token",
    "2025-08-04",
    "2025-09-04",
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

  val desJson: JsValue = Json.parse(
    """
      |{
      |  "numberOfDisposals": 1,
      |  "assetDescription": "description string",
      |  "tokenName": "Name of token",
      |  "acquisitionDate": "2025-08-04",
      |  "disposalDate": "2025-09-04",
      |  "disposalProceeds": 100.11,
      |  "allowableCosts": 100.12,
      |  "gainsWithBadr": 100.13,
      |  "gainsBeforeLosses": 100.14,
      |  "losses": 100.15,
      |  "claimOrElectionCodes": [
      |    "GHO"
      |  ],
      |  "amountOfNetGain": 100.16,
      |  "rttTaxPaid": 100.18
      |}
      |""".stripMargin
  )

  val invalidJson: JsValue = Json.parse(
    """
      |{
      |  "numberOfDisposals": "tfvgd",
      |  "assetDescription": "description string",
      |  "tokenName": "Name of token",
      |  "acquisitionDate": "2025-08-04",
      |  "disposalDate": "2025-09-04",
      |  "disposalProceeds": 100.11,
      |  "allowableCosts": 100.12,
      |  "gainsWithBadr": 100.13,
      |  "gainsBeforeLosses": 100.14,
      |  "losses": 100.15,
      |  "claimOrElectionCodes": [
      |    "GHO"
      |  ],
      |  "amountOfNetGain": 100.16,
      |  "rttTaxPaid": 100.18
      |}
      |""".stripMargin
  )

  "Cryptoasset" when {
    "read from a valid JSON" should {
      "produce the expected object" in {
        mtdJson.as[Cryptoasset] shouldBe mtdRequestBody
      }
    }

    "read from an invalid JSON" should {
      "produce a JsError" in {
        invalidJson.validate[Cryptoasset] shouldBe a[JsError]
      }
    }

    "written JSON" should {
      "produce the expected JsObject" in {
        Json.toJson(mtdRequestBody) shouldBe desJson
      }
    }
  }

}
