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
import shared.utils.{EmptinessChecker, EmptyPathsResult}
import support.UnitSpec

class LifetimeAllowanceSpec extends UnitSpec {

  val mtdJson: JsValue = Json.parse(
    """
      |{
      |  "lifetimeAllowanceBadr": 101.99,
      |  "lifetimeAllowanceInv": 102.99
      |}
      |""".stripMargin
  )

  val mtdRequestBody: LifetimeAllowance = LifetimeAllowance(
    Some(101.99),
    Some(102.99)
  )

  val desJson: JsValue = Json.parse(
    """
      |{
      |  "lifetimeAllowanceBadr": 101.99,
      |  "lifetimeAllowanceInv": 102.99
      |}
      |""".stripMargin
  )

  val emptyJson: JsValue = JsObject.empty

  val invalidJson: JsValue = Json.parse(
    """
      |{
      |  "lifetimeAllowanceBadr": "fghj",
      |  "lifetimeAllowanceInv": 102.99
      |}
      |""".stripMargin
  )

  "LifetimeAllowance" when {
    "read from a valid JSON" should {
      "produce the expected object" in {
        mtdJson.as[LifetimeAllowance] shouldBe mtdRequestBody
      }
    }

    "read from an empty JSON" should {
      "produce an empty object" in {
        emptyJson.as[LifetimeAllowance] shouldBe LifetimeAllowance.empty
      }
    }

    "read from an invalid JSON" should {
      "produce a JsError" in {
        invalidJson.validate[LifetimeAllowance] shouldBe a[JsError]
      }
    }

    "written JSON" should {
      "produce the expected JsObject" in {
        Json.toJson(mtdRequestBody) shouldBe desJson
      }
    }

    "written from an empty object" should {
      "produce an empty JSON" in {
        Json.toJson(LifetimeAllowance.empty) shouldBe emptyJson
      }
    }

    "Checking for emptiness" when {
      "has no fields" should {
        "be deemed empty" in {
          EmptinessChecker.findEmptyPaths(LifetimeAllowance.empty) shouldBe EmptyPathsResult.CompletelyEmpty
        }
      }
    }
  }

}
