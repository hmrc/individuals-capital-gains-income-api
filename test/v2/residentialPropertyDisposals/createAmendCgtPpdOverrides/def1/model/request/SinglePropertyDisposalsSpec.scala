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

package v2.residentialPropertyDisposals.createAmendCgtPpdOverrides.def1.model.request

import play.api.libs.json.{JsError, JsValue, Json}
import support.UnitSpec

class SinglePropertyDisposalsSpec extends UnitSpec {

  val mtdJson: JsValue = Json.parse(
    """
      |{
      |   "ppdSubmissionId": "AB0000000098",
      |   "completionDate": "2020-02-28",
      |   "disposalProceeds": 454.24,
      |   "acquisitionDate": "2020-03-29",
      |   "acquisitionAmount": 3434.45,
      |   "improvementCosts": 233.45,
      |   "additionalCosts": 423.34,
      |   "prfAmount": 2324.67,
      |   "otherReliefAmount": 3434.23,
      |   "lossesFromThisYear": 436.23,
      |   "lossesFromPreviousYear": 234.23,
      |   "amountOfNetGain": 4567.89
      |}
      |""".stripMargin
  )

  val singlePropertyDisposalsModel: SinglePropertyDisposals =
    SinglePropertyDisposals(
      "AB0000000098",
      "2020-02-28",
      454.24,
      Some("2020-03-29"),
      3434.45,
      233.45,
      423.34,
      2324.67,
      3434.23,
      Some(436.23),
      Some(234.23),
      Some(4567.89),
      None
    )

  val desJson: JsValue = Json.parse(
    """
      |{
      |   "ppdSubmissionId": "AB0000000098",
      |   "completionDate": "2020-02-28",
      |   "disposalProceeds": 454.24,
      |   "acquisitionDate": "2020-03-29",
      |   "acquisitionAmount": 3434.45,
      |   "improvementCosts": 233.45,
      |   "additionalCosts": 423.34,
      |   "prfAmount": 2324.67,
      |   "otherReliefAmount": 3434.23,
      |   "lossesFromThisYear": 436.23,
      |   "lossesFromPreviousYear": 234.23,
      |   "amountOfNetGain": 4567.89
      |}
      |""".stripMargin
  )

  val invalidJson: JsValue = Json.parse(
    """
      |{
      |   "ppdSubmissionId": 68836.11,
      |   "completionDate": "2020-02-28",
      |   "disposalProceeds": 454.24,
      |   "acquisitionDate": "2020-03-29",
      |   "acquisitionAmount": 3434.45,
      |   "improvementCosts": 233.45,
      |   "additionalCosts": 423.34,
      |   "prfAmount": 2324.67,
      |   "otherReliefAmount": 3434.23,
      |   "lossesFromThisYear": 436.23,
      |   "lossesFromPreviousYear": 234.23,
      |   "amountOfNetGain": 4567.89
      |}
      |""".stripMargin
  )

  "MultiplePropertyDisposals" when {
    "read from a valid JSON" should {
      "produce the expected object" in {
        mtdJson.as[SinglePropertyDisposals] shouldBe singlePropertyDisposalsModel
      }
    }

    "read from an invalid json" should {
      "provide a JsError" in {
        invalidJson.validate[MultiplePropertyDisposals] shouldBe a[JsError]
      }
    }

    "written JSON" should {
      "produce the expected JsObject" in {
        Json.toJson(singlePropertyDisposalsModel) shouldBe desJson
      }
    }
  }

}
