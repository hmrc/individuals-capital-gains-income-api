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

package v1.controllers.requestParsers

import api.models.domain.{MtdSourceEnum, Nino, TaxYear}
import api.models.errors._
import support.UnitSpec
import v1.mocks.validators.MockRetrieveAllResidentialPropertyCgtValidator
import v1.models.request.retrieveAllResidentialPropertyCgt.{RetrieveAllResidentialPropertyCgtRawData, RetrieveAllResidentialPropertyCgtRequest}

class RetrieveAllResidentialPropertyCgtRequestParserSpec extends UnitSpec {

  val nino: String                   = "AA123456B"
  val taxYear: String                = "2021-22"
  val source: Option[String]         = Some("hmrcHeld")
  implicit val correlationId: String = "a1e8057e-fbbc-47a8-a8b4-78d9f015c253"

  val retrieveAllResidentialPropertyCgtRawData: RetrieveAllResidentialPropertyCgtRawData = RetrieveAllResidentialPropertyCgtRawData(
    nino = nino,
    taxYear = taxYear,
    source = source
  )

  trait Test extends MockRetrieveAllResidentialPropertyCgtValidator {

    lazy val parser: RetrieveAllResidentialPropertyCgtRequestParser = new RetrieveAllResidentialPropertyCgtRequestParser(
      validator = mockRetrieveAllResidentialPropertyCgtValidator
    )

  }

  "parse" should {
    "return a request object" when {
      "valid request data is supplied" in new Test {
        MockRetrieveAllResidentialPropertyCgtValidator.validate(retrieveAllResidentialPropertyCgtRawData).returns(Nil)

        parser.parseRequest(retrieveAllResidentialPropertyCgtRawData) shouldBe
          Right(RetrieveAllResidentialPropertyCgtRequest(Nino(nino), TaxYear.fromMtd(taxYear), MtdSourceEnum.hmrcHeld))
      }

      "valid request with no source is supplied" in new Test {
        MockRetrieveAllResidentialPropertyCgtValidator.validate(retrieveAllResidentialPropertyCgtRawData.copy(source = None)).returns(Nil)

        parser.parseRequest(retrieveAllResidentialPropertyCgtRawData.copy(source = None)) shouldBe
          Right(RetrieveAllResidentialPropertyCgtRequest(Nino(nino), TaxYear.fromMtd(taxYear), MtdSourceEnum.latest))
      }
    }

    "return an ErrorWrapper" when {
      "a single validation error occurs" in new Test {
        MockRetrieveAllResidentialPropertyCgtValidator
          .validate(retrieveAllResidentialPropertyCgtRawData)
          .returns(List(NinoFormatError))

        parser.parseRequest(retrieveAllResidentialPropertyCgtRawData) shouldBe
          Left(ErrorWrapper(correlationId, NinoFormatError, None))
      }

      "multiple validation errors occur (NinoFormatError and TaxYearFormatError errors)" in new Test {
        MockRetrieveAllResidentialPropertyCgtValidator
          .validate(retrieveAllResidentialPropertyCgtRawData)
          .returns(List(NinoFormatError, TaxYearFormatError))

        parser.parseRequest(retrieveAllResidentialPropertyCgtRawData) shouldBe
          Left(ErrorWrapper(correlationId, BadRequestError, Some(Seq(NinoFormatError, TaxYearFormatError))))
      }

      "multiple validation errors occur (NinoFormatError, TaxYearFormatError and SourceFormatError errors)" in new Test {
        MockRetrieveAllResidentialPropertyCgtValidator
          .validate(retrieveAllResidentialPropertyCgtRawData)
          .returns(List(NinoFormatError, TaxYearFormatError, SourceFormatError))

        parser.parseRequest(retrieveAllResidentialPropertyCgtRawData) shouldBe
          Left(ErrorWrapper(correlationId, BadRequestError, Some(Seq(NinoFormatError, TaxYearFormatError, SourceFormatError))))
      }
    }
  }

}
