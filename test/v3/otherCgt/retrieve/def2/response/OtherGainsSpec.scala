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
import v3.otherCgt.retrieve.def2.model.response.AssetType.`other-property`
import v3.otherCgt.retrieve.def2.model.response.DownstreamAssetType.`otherProperty`
import v3.otherCgt.retrieve.def2.model.response.OtherGains

class OtherGainsSpec extends UnitSpec {

  val validResponseJson: JsValue = Json.parse(
    """
      |{
      |     "assetType": "otherProperty",
      |     "numberOfDisposals": 1,
      |     "assetDescription": "example of this asset",
      |     "companyName": "Bob the Builder",
      |     "companyRegistrationNumber": "11111111",
      |     "acquisitionDate": "2025-04-07",
      |     "disposalDate": "2025-07-10",
      |     "disposalProceeds": 99999999999.99,
      |     "allowableCosts": 99999999999.99,
      |     "gainsWithBadr": 99999999999.99,
      |     "gainsWithInv": 99999999999.99,
      |     "gainsBeforeLosses": 99999999999.99,
      |     "losses": 99999999999.99,
      |     "claimOrElectionCodes": [
      |          "GHO"
      |     ],
      |     "amountOfNetGain": 99999999999.99,
      |     "amountOfNetLoss": 99999999999.99,
      |     "rttTaxPaid": 99999999999.99
      |}
     """.stripMargin
  )

  val minimumValidResponseJson: JsValue = Json.parse(
    """
      |{
      |   "assetType": "otherProperty",
      |   "numberOfDisposals": 1,
      |   "assetDescription": "example of this asset",
      |   "acquisitionDate": "2025-04-07",
      |   "disposalDate": "2025-07-10",
      |   "disposalProceeds": 99999999999.99,
      |   "allowableCosts": 99999999999.99,
      |   "gainsBeforeLosses": 99999999999.99
      |}
     """.stripMargin
  )

  val validMtdResponseJson: JsValue = Json.parse(
    """
      |{
      |     "assetType": "other-property",
      |     "numberOfDisposals": 1,
      |     "assetDescription": "example of this asset",
      |     "companyName": "Bob the Builder",
      |     "companyRegistrationNumber": "11111111",
      |     "acquisitionDate": "2025-04-07",
      |     "disposalDate": "2025-07-10",
      |     "disposalProceeds": 99999999999.99,
      |     "allowableCosts": 99999999999.99,
      |     "gainsWithBadr": 99999999999.99,
      |     "gainsWithInv": 99999999999.99,
      |     "gainsBeforeLosses": 99999999999.99,
      |     "losses": 99999999999.99,
      |     "claimOrElectionCodes": [
      |          "GHO"
      |     ],
      |     "amountOfNetGain": 99999999999.99,
      |     "amountOfNetLoss": 99999999999.99,
      |     "rttTaxPaid": 99999999999.99
      |}
     """.stripMargin
  )

  val invalidJson: JsValue = JsObject.empty

  val responseModel: OtherGains = OtherGains(
    assetType = `otherProperty`.toMtd,
    numberOfDisposals = 1,
    assetDescription = "example of this asset",
    companyName = Some("Bob the Builder"),
    companyRegistrationNumber = Some("11111111"),
    acquisitionDate = "2025-04-07",
    disposalDate = "2025-07-10",
    disposalProceeds = 99999999999.99,
    allowableCosts = 99999999999.99,
    gainsWithBadr = Some(99999999999.99),
    gainsWithInv = Some(99999999999.99),
    gainsBeforeLosses = 99999999999.99,
    losses = Some(99999999999.99),
    claimOrElectionCodes = Some(Seq("GHO")),
    amountOfNetGain = Some(99999999999.99),
    amountOfNetLoss = Some(99999999999.99),
    rttTaxPaid = Some(99999999999.99)
  )

  val minimumResponseModel: OtherGains = OtherGains(
    assetType = `otherProperty`.toMtd,
    numberOfDisposals = 1,
    assetDescription = "example of this asset",
    companyName = None,
    companyRegistrationNumber = None,
    acquisitionDate = "2025-04-07",
    disposalDate = "2025-07-10",
    disposalProceeds = 99999999999.99,
    allowableCosts = 99999999999.99,
    gainsWithBadr = None,
    gainsWithInv = None,
    gainsBeforeLosses = 99999999999.99,
    losses = None,
    claimOrElectionCodes = None,
    amountOfNetGain = None,
    amountOfNetLoss = None,
    rttTaxPaid = None
  )

  val responseDownstreamModel: OtherGains = OtherGains(
    assetType = `other-property`,
    numberOfDisposals = 1,
    assetDescription = "example of this asset",
    companyName = Some("Bob the Builder"),
    companyRegistrationNumber = Some("11111111"),
    acquisitionDate = "2025-04-07",
    disposalDate = "2025-07-10",
    disposalProceeds = 99999999999.99,
    allowableCosts = 99999999999.99,
    gainsWithBadr = Some(99999999999.99),
    gainsWithInv = Some(99999999999.99),
    gainsBeforeLosses = 99999999999.99,
    losses = Some(99999999999.99),
    claimOrElectionCodes = Some(Seq("GHO")),
    amountOfNetGain = Some(99999999999.99),
    amountOfNetLoss = Some(99999999999.99),
    rttTaxPaid = Some(99999999999.99)
  )

  "OtherGains" when {
    "read from valid JSON" should {
      "produce the expected response model" in {
        validResponseJson.as[OtherGains] shouldBe responseModel
      }
    }

    "read from the minimum valid JSON" should {
      "produce the expected response model" in {
        minimumValidResponseJson.as[OtherGains] shouldBe minimumResponseModel
      }
    }

    "read from invalid JSON" should {
      "produce a JsError" in {
        invalidJson.validate[OtherGains] shouldBe a[JsError]
      }
    }

    "written to JSON" should {
      "produce the expected JSON" in {
        Json.toJson(responseDownstreamModel) shouldBe validMtdResponseJson
      }
    }
  }

}
