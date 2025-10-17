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

import play.api.libs.json.{JsError, JsObject, JsValue, Json}
import support.UnitSpec

class QualifyingAssetHoldingCompanySpec extends UnitSpec {

  val mtdJson: JsValue = Json.parse(
    """
      |{
      |  "gainsFromQahcBeforeLosses": 120.99,
      |  "lossesFromQahc": 130.99
      |}
      |""".stripMargin
  )

  val mtdRequestBody: QualifyingAssetHoldingCompany = QualifyingAssetHoldingCompany(
    Some(120.99),
    Some(130.99)
  )

  val desJson: JsValue = Json.parse(
    """
      |{
      |  "gainsFromQahcBeforeLosses": 120.99,
      |  "lossesFromQahc": 130.99
      |}
      |""".stripMargin
  )

  val emptyJson: JsValue = JsObject.empty

  val invalidJson: JsValue = Json.parse(
    """
      |{
      |  "gainsFromQahcBeforeLosses": "sdfghjk",
      |  "lossesFromQahc": 130.99
      |}
      |""".stripMargin
  )

  "QualifyingAssetHoldingCompany" when {
    "read from a valid JSON" should {
      "produce the expected object" in {
        mtdJson.as[QualifyingAssetHoldingCompany] shouldBe mtdRequestBody
      }
    }

    "read from an empty JSON" should {
      "produce an empty object" in {
        emptyJson.as[QualifyingAssetHoldingCompany] shouldBe QualifyingAssetHoldingCompany.empty
      }
    }

    "read from an invalid JSON" should {
      "produce a JsError" in {
        invalidJson.validate[QualifyingAssetHoldingCompany] shouldBe a[JsError]
      }
    }

    "written JSON" should {
      "produce the expected JsObject" in {
        Json.toJson(mtdRequestBody) shouldBe desJson
      }
    }

    "written from an empty object" should {
      "produce an empty JSON" in {
        Json.toJson(QualifyingAssetHoldingCompany.empty) shouldBe emptyJson
      }
    }
  }

}
