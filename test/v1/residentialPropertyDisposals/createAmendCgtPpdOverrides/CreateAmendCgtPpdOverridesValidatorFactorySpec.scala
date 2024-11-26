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

package v1.residentialPropertyDisposals.createAmendCgtPpdOverrides

import api.models.utils.JsonErrorValidators
import config.MockAppConfig
import play.api.libs.json.{JsValue, Json}
import support.UnitSpec
import v1.residentialPropertyDisposals.createAmendCgtPpdOverrides.def1.Def1_CreateAmendCgtPpdOverridesValidator

class CreateAmendCgtPpdOverridesValidatorFactorySpec extends UnitSpec with JsonErrorValidators with MockAppConfig {

  private val validNino    = "AA123456A"
  private val validTaxYear = "2021-22"

  def requestBodyJson(): JsValue = Json.parse(
    s"""
       |{
       |
       |}
     """.stripMargin
  )

  private val validRequestBody = requestBodyJson()

  private val validatorFactory = new CreateAmendCgtPpdOverridesValidatorFactory(mockAppConfig)

  "running a validation" should {
    "return the Def1 validator" when {
      "given a request handled by a Def1 schema" in {
        val result = validatorFactory.validator(validNino, validTaxYear, validRequestBody)
        result shouldBe a[Def1_CreateAmendCgtPpdOverridesValidator]

      }
    }

  }

}
