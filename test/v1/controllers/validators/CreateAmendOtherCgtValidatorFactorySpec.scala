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

import api.models.domain.{Nino, TaxYear}
import api.models.errors._
import mocks.MockAppConfig
import play.api.libs.json.{JsValue, Json}
import support.UnitSpec
import v1.models.request.createAmendOtherCgt.{CreateAmendOtherCgtRequestBody, CreateAmendOtherCgtRequestData}

class CreateAmendOtherCgtValidatorFactorySpec extends UnitSpec with MockAppConfig {

  private implicit val correlationId: String = "1234"

  private val validNino    = "AA123456A"
  private val validTaxYear = "2020-21"

  private val validAssetType = "listed-shares"
  private val validDescription = "shares"
  private val validDisposalDate      = "2020-03-01"
  private val validAcquisitionDate   = "2020-02-01"
  private val validValue             = 1000.12
  private val validCode              = "LET"

  private val validRequestBodyJson: JsValue = Json.parse(
    s"""
       |{
       |  "disposals":[
       |    {
       |      "assetType":"$validAssetType",
       |      "assetDescription":"$validDescription",
       |      "acquisitionDate":"$validAcquisitionDate",
       |      "disposalDate":"$validDisposalDate",
       |      "disposalProceeds":$validValue,
       |      "allowableCosts":$validValue,
       |      "gain":$validValue,
       |      "claimOrElectionCodes":["$validCode"],
       |      "gainAfterRelief":$validValue,
       |      "rttTaxPaid":$validValue
       |    }
       |  ]
       |}
""".stripMargin
  )

  private val bothLossGainBodyJson: JsValue = Json.parse(
    s"""
       |{
       |  "disposals":[
       |    {
       |      "assetType":"$validAssetType",
       |      "assetDescription":"$validDescription",
       |      "acquisitionDate":"$validAcquisitionDate",
       |      "disposalDate":"$validDisposalDate",
       |      "disposalProceeds":$validValue,
       |      "allowableCosts":$validValue,
       |      "gain":$validValue,
       |      "loss":$validValue,
       |      "claimOrElectionCodes":["$validCode"],
       |      "gainAfterRelief":$validValue,
       |      "rttTaxPaid":$validValue
       |    }
       |  ]
       |}
""".stripMargin
  )

//  private val emptyRequestBodyJson: JsValue = Json.parse("""{}""")
//
//  private val nonsenseRequestBodyJson: JsValue = Json.parse("""{"field": "value"}""")
//
//  private val nonValidRequestBodyJson: JsValue = Json.parse(
//    """
//      |{
//      |  "disposals": [
//      |    {
//      |      "disposalDate": true
//      |    }
//      |  ]
//      |}
//""".stripMargin
//  )
//
//  private val missingMandatoryFieldsJson: JsValue = Json.parse(
//    """
//      |{
//      |  "disposals":[{}]
//      |}
//""".stripMargin
//  )
//
//  private val oneBadValueFieldJson: JsValue = Json.parse(
//    s"""
//       |{
//       |  "disposals": [
//       |    {
//       |      "assetType":"$validCustomerReference",
//       |      "assetDescription":"$validCustomerReference",
//       |      "acquisitionDate":"$validAcquisitionDate",
//       |      "disposalDate":"$validDisposalDate",
//       |      "disposalProceeds":$validValue,
//       |      "allowableCosts":1000.123,
//       |      "gain":$validValue,
//       |      "loss":$validValue,
//       |      "claimOrElectionCodes":$validCode,
//       |      "gainAfterRelief":$validValue,
//       |      "lossAfterRelief":$validValue,
//       |      "rttTaxPaid":$validValue,
//       |    }
//       |  ]
//       |}
//""".stripMargin
//  )

  private val validBody = validRequestBodyJson

  private val parsedNino    = Nino(validNino)
  private val parsedTaxYear = TaxYear.fromMtd(validTaxYear)
  private val parsedBody    = validRequestBodyJson.as[CreateAmendOtherCgtRequestBody]

  val validatorFactory = new CreateAmendOtherCgtValidatorFactory(mockAppConfig)

  private def validator(nino: String, taxYear: String, body: JsValue) =
    validatorFactory.validator(nino, taxYear, body)

  MockedAppConfig.minimumPermittedTaxYear
    .returns(2021)
    .anyNumberOfTimes()

  "validator" should {
    "return the parsed domain object" when {
      "a valid request is supplied" in {
        val result = validator(validNino, validTaxYear, validBody).validateAndWrapResult()
        result shouldBe Right(CreateAmendOtherCgtRequestData(parsedNino, parsedTaxYear, parsedBody))
      }
    }

    "return NinoFormatError error" when {
      "an invalid nino is supplied" in {
        val result = validator("A12344A", validTaxYear, validRequestBodyJson).validateAndWrapResult()
        result shouldBe Left(
          ErrorWrapper(correlationId, NinoFormatError)
        )
      }
    }

    "return TaxYearFormatError error" when {
      "an invalid tax year is supplied" in {
        val result = validator(validNino, "201718", validRequestBodyJson).validateAndWrapResult()
        result shouldBe Left(
          ErrorWrapper(correlationId, TaxYearFormatError)
        )
      }
    }

    "return RuleTaxYearNotSupportedError error" when {
      "an out of range tax year is supplied" in {
        val result = validator(validNino, "2016-17", validRequestBodyJson).validateAndWrapResult()
        result shouldBe Left(
          ErrorWrapper(correlationId, RuleTaxYearNotSupportedError)
        )
      }
    }

    "return RuleTaxYearRangeInvalidError error" when {
      "an invalid tax year range is supplied" in {
        val result = validator(validNino, "2017-19", validRequestBodyJson).validateAndWrapResult()
        result shouldBe Left(
          ErrorWrapper(correlationId, RuleTaxYearRangeInvalidError)
        )
      }
    }

    "return multiple errors" when {
      "multiple invalid parameters are provided" in {
        val result = validator("not-a-nino", "2017-19", validRequestBodyJson).validateAndWrapResult()

        result shouldBe Left(
          ErrorWrapper(
            correlationId,
            BadRequestError,
            Some(List(NinoFormatError, RuleTaxYearRangeInvalidError))
          )
        )
      }
    }

    "return RuleGainLossError error" when {
      "an both gain and loss are supplied" in {
        val result = validator(validNino, validTaxYear, bothLossGainBodyJson).validateAndWrapResult()
        result shouldBe Left(
          ErrorWrapper(correlationId, RuleGainLossError)
        )
      }
    }
  }

}
