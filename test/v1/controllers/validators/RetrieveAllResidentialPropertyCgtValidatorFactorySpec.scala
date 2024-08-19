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

package v1.controllers.validators

import api.models.domain.{MtdSourceEnum, Nino, TaxYear}
import api.models.errors._
import mocks.MockAppConfig
import support.UnitSpec
import v1.models.request.retrieveAllResidentialPropertyCgt.RetrieveAllResidentialPropertyCgtRequestData

class RetrieveAllResidentialPropertyCgtValidatorFactorySpec extends UnitSpec with MockAppConfig {
  private implicit val correlationId: String = "1234"
  private val validNino                      = "AA123456A"
  private val validTaxYear                   = "2021-22"
  private val validSource                    = Some("hmrc-held")

  private val parsedNino    = Nino(validNino)
  private val parsedTaxYear = TaxYear.fromMtd(validTaxYear)
  private val parsedSource  = MtdSourceEnum.parser("hmrc-held")

  private val validatorFactory                                                 = new RetrieveAllResidentialPropertyCgtValidatorFactory(mockAppConfig)
  private def validator(nino: String, taxYear: String, source: Option[String]) = validatorFactory.validator(nino, taxYear, source)

  MockAppConfig.minimumPermittedTaxYear
    .returns(2021)
    .anyNumberOfTimes()

  "validator" should {
    "return the parsed domain object" when {
      "a valid request is supplied" in {
        val result: Either[ErrorWrapper, RetrieveAllResidentialPropertyCgtRequestData] =
          validator(validNino, validTaxYear, validSource).validateAndWrapResult()

        result shouldBe Right(RetrieveAllResidentialPropertyCgtRequestData(parsedNino, parsedTaxYear, parsedSource))
      }
      "a valid request is supplied with no source" in {
        val result: Either[ErrorWrapper, RetrieveAllResidentialPropertyCgtRequestData] =
          validator(validNino, validTaxYear, None).validateAndWrapResult()

        result shouldBe Right(RetrieveAllResidentialPropertyCgtRequestData(parsedNino, parsedTaxYear, MtdSourceEnum.latest))
      }
    }

    "return NinoFormatError error" when {
      "an invalid nino is supplied" in {
        val result: Either[ErrorWrapper, RetrieveAllResidentialPropertyCgtRequestData] =
          validator("A12344A", validTaxYear, validSource).validateAndWrapResult()

        result shouldBe Left(ErrorWrapper(correlationId, NinoFormatError))
      }
    }

    "return TaxYearFormatError error" when {
      "an invalid tax year is supplied" in {
        val result: Either[ErrorWrapper, RetrieveAllResidentialPropertyCgtRequestData] =
          validator(validNino, "20178", validSource).validateAndWrapResult()

        result shouldBe Left(ErrorWrapper(correlationId, TaxYearFormatError))
      }
    }

    "return RuleTaxYearRangeInvalidError error" when {
      "an invalid tax year range is supplied" in {
        val result: Either[ErrorWrapper, RetrieveAllResidentialPropertyCgtRequestData] =
          validator(validNino, "2019-21", validSource).validateAndWrapResult()

        result shouldBe Left(ErrorWrapper(correlationId, RuleTaxYearRangeInvalidError))
      }
    }

    "return RuleTaxYearNotSupportedError error" when {
      "a tax year that is not supported is supplied" in {
        val result: Either[ErrorWrapper, RetrieveAllResidentialPropertyCgtRequestData] =
          validator(validNino, "2018-19", validSource).validateAndWrapResult()

        result shouldBe Left(ErrorWrapper(correlationId, RuleTaxYearNotSupportedError))
      }
    }

    "return NinoFormatError and TaxYearFormatError errors" when {
      "request supplied has invalid nino and tax year" in {
        val result: Either[ErrorWrapper, RetrieveAllResidentialPropertyCgtRequestData] =
          validator("A12344A", "20178", validSource).validateAndWrapResult()

        result shouldBe Left(
          ErrorWrapper(
            correlationId,
            BadRequestError,
            Some(List(NinoFormatError, TaxYearFormatError))
          )
        )
      }
    }

    "return NinoFormatError, TaxYearFormatError and SourceFormatError errors" when {
      "request supplied has invalid nino, tax year and source" in {
        val result: Either[ErrorWrapper, RetrieveAllResidentialPropertyCgtRequestData] =
          validator("A12344A", "20178", Some("ABCDE12345FG")).validateAndWrapResult()

        result shouldBe Left(
          ErrorWrapper(
            correlationId,
            BadRequestError,
            Some(List(NinoFormatError, SourceFormatError, TaxYearFormatError))
          )
        )
      }
    }
  }

}
