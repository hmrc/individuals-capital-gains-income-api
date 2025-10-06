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

package v3.otherCgt.retrieve.def2

import shared.models.domain.{Nino, TaxYear}
import shared.models.errors.*
import support.UnitSpec
import v3.otherCgt.retrieve.def2.model.request.Def2_RetrieveOtherCgtRequestData
import v3.otherCgt.retrieve.model.request.RetrieveOtherCgtRequestData

class Def2_RetrieveOtherCgtValidatorSpec extends UnitSpec {

  private implicit val correlationId: String = "1234"

  private val validNino    = "AA123456A"
  private val validTaxYear = "2020-21"

  private val parsedNino    = Nino(validNino)
  private val parsedTaxYear = TaxYear.fromMtd(validTaxYear)

  private def validator(nino: String, taxYear: String) = new Def2_RetrieveOtherCgtValidator(nino, taxYear).validateAndWrapResult()

  "validator" should {
    "return the parsed domain object" when {
      "a valid request is supplied" in {
        val result: Either[ErrorWrapper, RetrieveOtherCgtRequestData] = validator(validNino, validTaxYear)

        result shouldBe Right(Def2_RetrieveOtherCgtRequestData(parsedNino, parsedTaxYear))
      }
    }

    "return NinoFormatError error" when {
      "an invalid nino is supplied" in {
        val result: Either[ErrorWrapper, RetrieveOtherCgtRequestData] = validator("A12344A", validTaxYear)

        result shouldBe Left(ErrorWrapper(correlationId, NinoFormatError))
      }
    }
  }

}
