/*
 * Copyright 2023 HM Revenue & Customs
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

package v2.otherCgt.createAmend.def1.model.request

import play.api.libs.json.{JsError, JsObject, JsValue, Json}
import support.UnitSpec
import v2.otherCgt.createAmend.def1.fixture.ClaimOrElectionCodes

class Def1CreateAmendOtherCgtRequestBodySpec extends UnitSpec {

  val mtdJson: JsValue = Json.parse(
    """
      |{
      |   "disposals": [
      |      {
      |         "assetType": "other-property",
      |         "assetDescription": "Property Sale",
      |         "acquisitionDate": "2021-01-01",
      |         "disposalDate": "2021-02-01",
      |         "disposalProceeds": 1000.12,
      |         "allowableCosts": 100.13,
      |         "gain": 900.12,
      |         "claimOrElectionCodes": [
      |            "PRR"
      |         ],
      |         "gainAfterRelief": 10.12,
      |         "rttTaxPaid": 12.12
      |      }
      |   ],
      |   "nonStandardGains": {
      |      "carriedInterestGain": 101.99,
      |      "carriedInterestRttTaxPaid": 102.99,
      |      "attributedGains": 103.99,
      |      "attributedGainsRttTaxPaid": 104.99,
      |      "otherGains": 105.99,
      |      "otherGainsRttTaxPaid": 106.99
      |   },
      |   "losses": {
      |      "broughtForwardLossesUsedInCurrentYear": 120.99,
      |      "setAgainstInYearGains": 130.99,
      |      "setAgainstInYearGeneralIncome": 140.99,
      |      "setAgainstEarlierYear": 150.99
      |   },
      |   "adjustments": 160.99
      |}
      |""".stripMargin
  )

  val mtdJsonWithMultipleDisposals: JsValue = Json.parse(
    """
      |{
      |   "disposals": [
      |      {
      |         "assetType": "other-property",
      |         "assetDescription": "Property Sale",
      |         "acquisitionDate": "2021-01-01",
      |         "disposalDate": "2021-02-01",
      |         "disposalProceeds": 1000.12,
      |         "allowableCosts": 100.13,
      |         "gain": 900.12,
      |         "claimOrElectionCodes": [
      |            "PRR"
      |         ],
      |         "gainAfterRelief": 10.12,
      |         "rttTaxPaid": 12.12
      |      },
      |      {
      |         "assetType":"other-property",
      |         "assetDescription":"Property Sale",
      |         "acquisitionDate":"2021-02-01",
      |         "disposalDate":"2021-03-01",
      |         "disposalProceeds":11000.12,
      |         "allowableCosts":1100.13,
      |         "gain":1900.12,
      |         "claimOrElectionCodes":[
      |            "PRR",
      |            "BAD"
      |         ],
      |         "gainAfterRelief":110.12,
      |         "rttTaxPaid":112.12
      |      }
      |   ],
      |   "nonStandardGains": {
      |      "carriedInterestGain": 101.99,
      |      "carriedInterestRttTaxPaid": 102.99,
      |      "attributedGains": 103.99,
      |      "attributedGainsRttTaxPaid": 104.99,
      |      "otherGains": 105.99,
      |      "otherGainsRttTaxPaid": 106.99
      |   },
      |   "losses": {
      |      "broughtForwardLossesUsedInCurrentYear": 120.99,
      |      "setAgainstInYearGains": 130.99,
      |      "setAgainstInYearGeneralIncome": 140.99,
      |      "setAgainstEarlierYear": 150.99
      |   },
      |   "adjustments": 160.99
      |}
      |""".stripMargin
  )

  val disposal: Disposal = Disposal(
    AssetType.`other-property`.toString,
    "Property Sale",
    "2021-01-01",
    "2021-02-01",
    1000.12,
    100.13,
    Some(900.12),
    None,
    Some(Seq(ClaimOrElectionCodes.PRR.toString)),
    Some(10.12),
    None,
    Some(12.12)
  )

  val disposal2: Disposal = Disposal(
    AssetType.`other-property`.toString,
    "Property Sale",
    "2021-02-01",
    "2021-03-01",
    11000.12,
    1100.13,
    Some(1900.12),
    None,
    Some(Seq(ClaimOrElectionCodes.PRR.toString, ClaimOrElectionCodes.BAD.toString)),
    Some(110.12),
    None,
    Some(112.12)
  )

  val nonStandardGains: NonStandardGains = NonStandardGains(
    Some(101.99),
    Some(102.99),
    Some(103.99),
    Some(104.99),
    Some(105.99),
    Some(106.99)
  )

  val losses: Losses = Losses(
    Some(120.99),
    Some(130.99),
    Some(140.99),
    Some(150.99)
  )

  val mtdRequestBody: Def1_CreateAmendOtherCgtRequestBody =
    Def1_CreateAmendOtherCgtRequestBody(Some(Seq(disposal)), Some(nonStandardGains), Some(losses), Some(160.99))

  val mtdRequestBodyWithMultipleDisposals: Def1_CreateAmendOtherCgtRequestBody =
    Def1_CreateAmendOtherCgtRequestBody(Some(Seq(disposal, disposal2)), Some(nonStandardGains), Some(losses), Some(160.99))

  val desJson: JsValue = Json.parse(
    """
      |{
      |   "disposals":[
      |      {
      |         "assetType":"otherProperty",
      |         "assetDescription":"Property Sale",
      |         "acquisitionDate":"2021-01-01",
      |         "disposalDate":"2021-02-01",
      |         "disposalProceeds":1000.12,
      |         "allowableCosts":100.13,
      |         "gain":900.12,
      |         "claimOrElectionCodes":[
      |            "PRR"
      |         ],
      |         "gainAfterRelief":10.12,
      |         "rttTaxPaid":12.12
      |      }
      |   ],
      |   "nonStandardGains":{
      |      "carriedInterestGain":101.99,
      |      "carriedInterestRttTaxPaid":102.99,
      |      "attributedGains":103.99,
      |      "attributedGainsRttTaxPaid":104.99,
      |      "otherGains":105.99,
      |      "otherGainsRttTaxPaid":106.99
      |   },
      |   "losses":{
      |      "broughtForwardLossesUsedInCurrentYear":120.99,
      |      "setAgainstInYearGains":130.99,
      |      "setAgainstInYearGeneralIncome":140.99,
      |      "setAgainstEarlierYear":150.99
      |   },
      |   "adjustments":160.99
      |}
      |""".stripMargin
  )

  val desJsonWithMultipleDisposals: JsValue = Json.parse(
    """
      |{
      |   "disposals":[
      |      {
      |         "assetType":"otherProperty",
      |         "assetDescription":"Property Sale",
      |         "acquisitionDate":"2021-01-01",
      |         "disposalDate":"2021-02-01",
      |         "disposalProceeds":1000.12,
      |         "allowableCosts":100.13,
      |         "gain":900.12,
      |         "claimOrElectionCodes":[
      |            "PRR"
      |         ],
      |         "gainAfterRelief":10.12,
      |         "rttTaxPaid":12.12
      |      },
      |      {
      |         "assetType":"otherProperty",
      |         "assetDescription":"Property Sale",
      |         "acquisitionDate":"2021-02-01",
      |         "disposalDate":"2021-03-01",
      |         "disposalProceeds":11000.12,
      |         "allowableCosts":1100.13,
      |         "gain":1900.12,
      |         "claimOrElectionCodes":[
      |            "PRR",
      |            "BAD"
      |         ],
      |         "gainAfterRelief":110.12,
      |         "rttTaxPaid":112.12
      |      }
      |   ],
      |   "nonStandardGains":{
      |      "carriedInterestGain":101.99,
      |      "carriedInterestRttTaxPaid":102.99,
      |      "attributedGains":103.99,
      |      "attributedGainsRttTaxPaid":104.99,
      |      "otherGains":105.99,
      |      "otherGainsRttTaxPaid":106.99
      |   },
      |   "losses":{
      |      "broughtForwardLossesUsedInCurrentYear":120.99,
      |      "setAgainstInYearGains":130.99,
      |      "setAgainstInYearGeneralIncome":140.99,
      |      "setAgainstEarlierYear":150.99
      |   },
      |   "adjustments":160.99
      |}
      |""".stripMargin
  )

  val emptyJson: JsValue = JsObject.empty

  val invalidJson: JsValue = Json.parse(
    """
      |{
      |   "disposals":[
      |      {
      |
      |      }
      |   ],
      |   "nonStandardGains":{
      |
      |   },
      |   "losses":{
      |
      |   },
      |   "adjustments":160.99
      |}
      |""".stripMargin
  )

  "Def1_CreateAmendOtherCgtRequestBody" when {
    "read from a valid JSON" should {
      "produce the expected object" in {
        mtdJson.as[Def1_CreateAmendOtherCgtRequestBody] shouldBe mtdRequestBody
      }
    }

    "read from a valid JSON With Multiple Disposals" should {
      "produce the expected object" in {
        mtdJsonWithMultipleDisposals.as[Def1_CreateAmendOtherCgtRequestBody] shouldBe mtdRequestBodyWithMultipleDisposals
      }
    }

    "read from invalid Json" should {
      "provide a JsError" in {
        invalidJson.validate[Def1_CreateAmendOtherCgtRequestBody] shouldBe a[JsError]
      }
    }

    "read from an empty JSON" should {
      "produce an empty object" in {
        emptyJson.as[Def1_CreateAmendOtherCgtRequestBody] shouldBe Def1_CreateAmendOtherCgtRequestBody.empty
      }
    }

    "written JSON" should {
      "produce the expected JsObject" in {
        Json.toJson(mtdRequestBody) shouldBe desJson
      }
    }

    "written JSON With Multiple Disposals" should {
      "produce the expected JsObject" in {
        Json.toJson(mtdRequestBodyWithMultipleDisposals) shouldBe desJsonWithMultipleDisposals
      }
    }

    "written from an empty object" should {
      "produce an empty JSON" in {
        Json.toJson(Def1_CreateAmendOtherCgtRequestBody.empty) shouldBe emptyJson
      }
    }
  }

}
