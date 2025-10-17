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

import play.api.libs.json.{JsValue, Json}
import support.UnitSpec

class OtherGainSpec extends UnitSpec {

  val mtdJson: JsValue = Json.parse(
    """
      |{
      |  "assetType": "other-property",
      |  "numberOfDisposals": 1,
      |  "assetDescription": "example of this asset",
      |  "companyName": "Bob the Builder",
      |  "companyRegistrationNumber": "11111111",
      |  "acquisitionDate": "2025-04-07",
      |  "disposalDate": "2025-07-10",
      |  "disposalProceeds": 100.11,
      |  "allowableCosts": 100.12,
      |  "gainsWithBadr": 100.13,
      |  "gainsWithInv": 100.14,
      |  "gainsBeforeLosses": 100.15,
      |  "losses": 100.16,
      |  "claimOrElectionCodes": [
      |    "PRR"
      |  ],
      |  "amountOfNetGain": 100.17,
      |  "rttTaxPaid": 100.19
      |}
      |""".stripMargin
  )

  val mtdRequestBody: OtherGain = OtherGain(
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

  val desJson: JsValue = Json.parse(
    """
      |{
      |  "assetType": "other-property",
      |  "numberOfDisposals": 1,
      |  "assetDescription": "example of this asset",
      |  "companyName": "Bob the Builder",
      |  "companyRegistrationNumber": "11111111",
      |  "acquisitionDate": "2025-04-07",
      |  "disposalDate": "2025-07-10",
      |  "disposalProceeds": 100.11,
      |  "allowableCosts": 100.12,
      |  "gainsWithBadr": 100.13,
      |  "gainsWithInv": 100.14,
      |  "gainsBeforeLosses": 100.15,
      |  "losses": 100.16,
      |  "claimOrElectionCodes": [
      |    "PRR"
      |  ],
      |  "amountOfNetGain": 100.17,
      |  "rttTaxPaid": 100.19
      |}
      |""".stripMargin
  )

  "OtherGain" when {
    "read from a valid JSON" should {
      "produce the expected object" in {
        mtdJson.as[OtherGain] shouldBe mtdRequestBody
      }
    }

    "written JSON" should {
      "produce the expected JsObject" in {
        Json.toJson(mtdRequestBody) shouldBe desJson
      }
    }
  }

}
